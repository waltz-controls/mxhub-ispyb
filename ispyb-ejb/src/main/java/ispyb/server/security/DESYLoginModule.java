/*******************************************************************************
 * This file is part of ISPyB.
 * 
 * ISPyB is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ISPyB is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with ISPyB.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors : S. Delageniere, R. Leal, L. Launer, K. Levik, S. Veyrier, P. Brenchereau, M. Bodin, A. De Maria Antolinos
 ******************************************************************************************************************************/


/**
 * LdapLoginModule
 *  
 * Created on Nov 20, 2004
 *
 * Ricardo LEAL
 * ESRF - European Synchrotron Radiation Facility
 * B.P. 220
 * 38043 Grenoble Cedex - France
 * Phone: 00 33 (0)4 38 88 19 59
 * Fax: 00 33 (0)4 76 88 25 42
 * ricardo.leal@esrf.fr
 * 
 */
package ispyb.server.security;

import ispyb.common.util.Constants;
import ispyb.common.util.PropertyLoader;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.jboss.security.SimpleGroup;
import org.jboss.security.SimplePrincipal;
import org.jboss.security.auth.spi.UsernamePasswordLoginModule;
import org.json.JSONArray;
import org.json.JSONObject;


import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * An implementation of LoginModule that authenticates against DESY DOOR user portal.
 *
 * @author BORGES
 * @version
 */
public class DESYLoginModule extends UsernamePasswordLoginModule {

	// default role. If a user has a valid password
	// it will have a regular User role.
	private static final String DEFAULT_GROUP = "User";

	private static final String ALLOW_EMPTY_PASSWORDS_OPT = "allowEmptyPasswords";

	private transient SimpleGroup userRoles = new SimpleGroup("Roles");

	private final Logger LOG = Logger.getLogger(DESYLoginModule.class);

	public DESYLoginModule() {

	}

	/**
	 * Overriden to return an empty password string as typically one cannot obtain a user's password. We also override
	 * the validatePassword so this is ok.
	 * 
	 * @return and empty password String
	 */
	@Override
	protected String getUsersPassword() throws LoginException {
		return "";
	}

	/**
	 * Overriden by subclasses to return the Groups that correspond to the role sets assigned to the user.
	 * Subclasses should create at least a Group named "Roles" that contains the roles assigned to the user. A second
	 * common group is "CallerPrincipal" that provides the application identity of the user rather than the security
	 * domain identity.
	 * 
	 * @return Group[] containing the sets of roles
	 */
	@Override
	protected Group[] getRoleSets() throws LoginException {
		Group[] roleSets = { userRoles };
		return roleSets;
	}

	/**
	 * Validate the inputPassword by calling the doorAuthentication method.
	 * 
	 * @param inputPassword
	 *            the password to validate.
	 * @param expectedPassword
	 *            ignored
	 */
	@Override
	protected boolean validatePassword(String inputPassword, String expectedPassword) {
		if (inputPassword != null) {
			// See if this is an empty password that should be disallowed
			if (inputPassword.length() == 0) {
				// Check for an allowEmptyPasswords option
				boolean allowEmptyPasswords = true;
				String flag = (String) options.get(ALLOW_EMPTY_PASSWORDS_OPT);
				if (flag != null)
					allowEmptyPasswords = Boolean.valueOf(flag).booleanValue();
				if (allowEmptyPasswords == false) {
					LOG.debug("Rejecting empty password due to allowEmptyPasswords");
					return false;
				}
			}

			try {
				// Validate the password by calling the DOOR API
				String username = getUsername();

				doorAuthentication(username, inputPassword);

				/** if there's no Role for this username, so it will be a User **/
				if (!userRoles.members().hasMoreElements())
						userRoles.addMember(new SimplePrincipal(DEFAULT_GROUP));
				return true;

			} catch (Exception e) {

				LOG.error("Failed to validate password", e);
			}
		}
		return false;
	}


	/**
	 * Create an HTTP post request to DOOR, connects to server and fill the Roles/Groups array
	 * 
	 * @param username
	 * @param inputPassword
	 *
	 */
	private void doorAuthentication(String username, String inputPassword) throws IOException {
		Properties ispybProp = PropertyLoader.loadProperties("ISPyB");
		String serverUrl = ispybProp.getProperty("userportal.webservices.url");
		// Build the endpoint url for door authentication
		StringBuilder url = new StringBuilder(serverUrl).append("/doorauth/auth");

		String door_token = ispybProp.getProperty("userportal.webservices.token");
		// Create a Post request
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url.toString());
		// Add the door token within the headers
		httpPost.addHeader("x-door-token", door_token);

		// Encode the password in Base64
		Base64 base64 = new Base64();
		String passbase64 = new String(base64.encode(inputPassword.getBytes()));

		//Add the values to be passed as post parameters
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("user", username));
		formparams.add(new BasicNameValuePair("pass", passbase64));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
		httpPost.setEntity(entity);

		// Execute the POST call
		CloseableHttpResponse response = httpClient.execute(httpPost);
		try{
			String jsonStr = IOUtils.toString(new InputStreamReader((response.getEntity().getContent())));
			JSONObject jsonUser = new JSONObject(jsonStr);
			// Get the user id to retrieve the user roles
			Integer userid = jsonUser.getJSONObject("userdata").getInt("userid");
			LOG.info("DOOR Authenticated userid for username " + username + ": " + userid);
			doorAuthorization(userid);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			throw new RuntimeException("Error authenticating the user. " + ex.getMessage());
		}

	}

	private void doorAuthorization(Integer userid) throws IOException {
		Properties ispybProp = PropertyLoader.loadProperties("ISPyB");
		String serverUrl = ispybProp.getProperty("userportal.webservices.url");
		String door_token = ispybProp.getProperty("userportal.webservices.token");
		String door_service_account = ispybProp.getProperty("smis.ws.username");
		String door_service_password = ispybProp.getProperty("smis.ws.password");
		// Build the endpoint url for door authorization
		StringBuilder url = new StringBuilder(serverUrl).append("/roles/userid/").append(userid);

		// Create a Get request
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url.toString());
		// Add the door token and the service account within the headers
		httpGet.addHeader("x-door-token", door_token);
		httpGet.addHeader("x-door-service-account", door_service_account);
		httpGet.addHeader("x-door-service-auth", door_service_password);

		// Execute the call
		CloseableHttpResponse response = httpClient.execute(httpGet);
		try{
			String jsonStr = IOUtils.toString(new InputStreamReader((response.getEntity().getContent())));
			JSONObject obj = new JSONObject(jsonStr);
			JSONArray roles = obj.getJSONArray("roles");
			for (int i = 0; i < roles.length(); i++)
			{
				String role_name = roles.getJSONObject(i).getString("name");
				String role_value = roles.getJSONObject(i).getString("value");
				if(role_name.equals("Beamline Manager") && role_value.equals("PETRA III: P11")){
					// Assign the Manager/Local Contact role
					userRoles.addMember(new SimplePrincipal(Constants.ROLE_MANAGER));
					userRoles.addMember(new SimplePrincipal(Constants.ROLE_LOCALCONTACT));
				} else if (role_name.equals("General User")){
					userRoles.addMember(new SimplePrincipal(DEFAULT_GROUP));
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			throw new RuntimeException("Error reading doorAuthorization json. " + ex.getMessage());
		}
	}
}