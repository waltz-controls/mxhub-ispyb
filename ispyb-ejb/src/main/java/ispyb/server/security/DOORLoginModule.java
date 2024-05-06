package ispyb.server.security;

import ispyb.common.util.Constants;
import ispyb.common.util.PropertyLoader;
import org.apache.commons.io.IOUtils;
import org.apache.cxf.common.security.SimplePrincipal;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Principal;
import java.util.*;

public class DOORLoginModule implements LoginModule {
    private Subject subject;
    private CallbackHandler callbackHandler;
    private volatile boolean loginSuccess = false;
    private List<Principal> userPrincipals = new ArrayList<>();
    private Map<String, ?> options;

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
        this.subject = subject;
        this.callbackHandler = callbackHandler;
        this.options = options;
    }

    @Override
    public boolean login() throws LoginException {
        NameCallback nameCallback = new NameCallback("Username: ");
        PasswordCallback passwordCallback = new PasswordCallback("Password: ", false);
        try {
            callbackHandler.handle(new Callback[] {nameCallback, passwordCallback});
            String username = nameCallback.getName();
            String password = new String(passwordCallback.getPassword());

            return doorAuthentication(username, password);
        } catch (IOException | UnsupportedCallbackException e) {
            throw new LoginException("Failed to handle callback: " + e.getMessage());
        }
    }

    @Override
    public boolean commit() throws LoginException {
        if (loginSuccess) {
            subject.getPrincipals().addAll(userPrincipals);
            return true;
        } else {
            clear();
            return false;
        }
    }

    @Override
    public boolean abort() throws LoginException {
        clear();
        return true;
    }

    @Override
    public boolean logout() throws LoginException {
        subject.getPrincipals().removeAll(userPrincipals);
        clear();
        return true;
    }

    private void clear() {
        userPrincipals.clear();
        loginSuccess = false;
    }

    private boolean doorAuthentication(String username, String password){
        Properties props = PropertyLoader.loadProperties(System.getProperty(Constants.ISPYB_PROPERTIES, Constants.CLASSPATH_ISPYB_PROPERTIES));
        String serverUrl = props.getProperty("userportal.webservices.url");
        // Build the endpoint url for door authentication
        StringBuilder url = new StringBuilder(serverUrl).append("/doorauth/auth");

        String door_token = props.getProperty("userportal.webservices.token");
        // Create a Post request
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url.toString());
        // Add the door token within the headers
        httpPost.addHeader("x-door-token", door_token);

        // Encode the password in Base64
        String passbase64 = new String(Base64.getEncoder().encodeToString(password.getBytes()));

        //Add the values to be passed as post parameters
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("user", username));
        formparams.add(new BasicNameValuePair("pass", passbase64));


        try(CloseableHttpResponse response = httpClient.execute(httpPost)){
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httpPost.setEntity(entity);
            String jsonStr = IOUtils.toString(new InputStreamReader((response.getEntity().getContent())));
            JSONObject jsonUser = new JSONObject(jsonStr);
            // Get the user id to retrieve the user roles
            int userid = jsonUser.getJSONObject("userdata").getInt("userid");
            return doorAuthorization(userid);
        }
        catch(Exception ex)
        {
            throw new RuntimeException("Error authenticating the user. " + ex.getMessage());
        }
    }

    private boolean doorAuthorization(int userid) throws LoginException {
        Properties props = PropertyLoader.loadProperties(System.getProperty(Constants.ISPYB_PROPERTIES,Constants.CLASSPATH_ISPYB_PROPERTIES));
        String serverUrl = props.getProperty("userportal.webservices.url");
        String door_token = props.getProperty("userportal.webservices.token");
        String door_service_account = props.getProperty("smis.ws.username");
        String door_service_password = props.getProperty("smis.ws.password");
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
        try(CloseableHttpResponse response = httpClient.execute(httpGet)){
            String jsonStr = IOUtils.toString(new InputStreamReader((response.getEntity().getContent())));
            JSONObject obj = new JSONObject(jsonStr);
            JSONArray roles = obj.getJSONArray("roles");
            for (int i = 0; i < roles.length(); i++)
            {
                String role_name = roles.getJSONObject(i).getString("name");
                String role_value = roles.getJSONObject(i).getString("value");
                if(role_name.equals("Beamline Manager") && role_value.equals("PETRA III: P11")){
                    // Assign the Manager/Local Contact role
                    userPrincipals.add(new SimplePrincipal(Constants.ROLE_MANAGER));
                    userPrincipals.add(new SimplePrincipal(Constants.ROLE_LOCALCONTACT));
                } else if (role_name.equals("General User")){
                    userPrincipals.add(new SimplePrincipal("User"));
                }
            }
        }
        catch(Exception ex)
        {
            throw new LoginException("Error reading doorAuthorization json. " + ex.getMessage());
        }
        return false;
    }
}
