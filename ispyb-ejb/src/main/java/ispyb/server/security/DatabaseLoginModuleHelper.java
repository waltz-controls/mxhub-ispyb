package ispyb.server.security;


import ispyb.common.util.Constants;
import ispyb.common.util.StringUtils;

import java.security.Principal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import javax.naming.NamingException;
import javax.security.auth.login.LoginException;

import com.sun.security.auth.UserPrincipal;

import org.apache.log4j.Logger;

/**
 * Technical class to access database for {@link DatabaseLoginModule} connection, user's credentials and user's roles  
 *
 * @author CHADO
 */
public class DatabaseLoginModuleHelper {

	private static final Logger LOG = Logger.getLogger(DatabaseLoginModuleHelper.class);
	
	private static StringBuffer fullName;
	
	/**
	 * Verify the user's credentials
	 * 
	 * @param options - Options is a map of settings to connect to database 
	 * @param username - proposal Id i.e.: mx9999
	 * @param password - is the user login if coming from Web User Office Tool
	 * @throws LoginException - LoginException
	 */
	public static void verifyCredentials(Map<String, ?> options,String username, String password) throws LoginException {
		Connection connection = null;
		try {
			connection = findConnection(options);
			doVerifyCredentials(connection, options, username, password);
		} catch (SQLException exception) {
			throw new LoginException(exception.getMessage());
		} catch (NamingException exception) {
			throw new LoginException(exception.getMessage());
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	/**
	 * Creates a connection from an Jndi datasource or an new connection is created
	 * 
	 * @param options - Options is a map of settings to connect to database
	 * @return <b>Connection</b> {@link Connection}
	 * @throws NamingException
	 * @throws SQLException
	 */
	private static Connection findConnection(Map<String, ?> options) throws NamingException, SQLException {

		String driver = (String) options.get(Constants.DB_DRIVER_NAME);
		String url = (String) options.get(Constants.DB_CONNECTION_URL);
		String dbUser = (String) options.get(Constants.DB_USER_NAME);
		String dbPassword = (String) options.get(Constants.DB_USER_PASSWORD);
		
		LOG.info("DatabaseLoginModuleHelper - driver = " + driver);
		LOG.info("DatabaseLoginModuleHelper - url = " + url);
		LOG.info("DatabaseLoginModuleHelper - dbUser = " + dbUser);
		LOG.info("DatabaseLoginModuleHelper - dbPassword = *********** ");
		
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return  DriverManager.getConnection(url, dbUser, dbPassword);
	}

	/**
	 * Does the actual login
	 * 
	 * @param connection - {@link Connection}
	 * @param options - Options is a map of settings to connect to database
	 * @param username - proposal Id i.e.: mx9999
	 * @param password - is the user login if coming from Web User Office Tool
	 * @throws LoginException
	 */
	private static void doVerifyCredentials(Connection connection, Map<String, ?> options, String username, String password) throws LoginException {
		Statement statement = null;
		ResultSet resultSet = null;
		fullName = null;
		fullName = new StringBuffer();
		
		try {
			if (username == null) {
				throw new LoginException("Invalid credentials");
			}
						
			// We have to make sure that we only use basic sql that is understood by all dbms's
			// Next query will retrieve the password of a user (SQL '92 compliant)
			String sql = (String)options.get(Constants.DB_USER_QUERY);
			String password_ = null;

			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);
			
			if (StringUtils.isEmpty(password_)) {
				LOG.info("invalid credential ");
				throw new LoginException("Invalid credentials");
			}
			
		} catch (SQLException sqle) {
			throw new LoginException(sqle.toString());
		} finally {
			if (resultSet != null)
				try {
					resultSet.close();
				} catch (SQLException e) {
				}
			if (statement != null)
				try {
					statement.close();
				} catch (SQLException e) {
				}
		}
	}

	/**
	 * Get User FullName 
	 */
	public static String doGetUserFullName() {
		return fullName.toString(); 
	}
	
	
	public static String[] getRoleNamesForUser(Map<String, ?> options, String username, String password) throws LoginException {

		Connection connection = null;
		try {
			connection = findConnection(options);
			
			return doGetRoleNamesForUser(connection, options, username, password);
			
		} catch (SQLException exception) {
			throw new LoginException(exception.getMessage());
		} catch (NamingException exception) {
			throw new LoginException(exception.getMessage());
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
				}
			}
		}
	}
	
	public static Set<Principal> getRoleNamesForUser(Map<String, ?> options, String username, String password, String defaultGroup) throws LoginException {

		Connection connection = null;
		try {
			connection = findConnection(options);
			
			return doGetRoleNamesForUser(connection, options, username, password, defaultGroup);
			
		} catch (SQLException exception) {
			throw new LoginException(exception.getMessage());
		} catch (NamingException exception) {
			throw new LoginException(exception.getMessage());
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	private static Set<Principal> doGetRoleNamesForUser(Connection connection, Map<String, ?> options, String username, String password, String defaultGroup) throws NamingException, SQLException, LoginException {

		Set<Principal> userRoles = new HashSet<>();
		userRoles.add(new UserPrincipal(defaultGroup));
		for (String role : DatabaseLoginModuleHelper.doGetRoleNamesForUser(connection, options, username, password)) {
			userRoles.add(new UserPrincipal(role));
		}
		return userRoles;
	}
	
	/**
	 * Retrieve user's roles
	 * 
	 * @param connection - {@link Connection}
	 * @param options - Options is a map of settings to connect to database
	 * @param username - proposal Id i.e.: mx9999
	 * @param password - is the user login if coming from Web User Office Tool 
	 * @return Array string value of user's roles
	 * @throws NamingException
	 * @throws SQLException
	 * @throws LoginException
	 */
	private static String[] doGetRoleNamesForUser(Connection connection, Map<String, ?> options, String username, String password) throws NamingException, SQLException, LoginException {
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			if (username == null)
				throw new LoginException("Invalid username");
			
			String sql = (String)options.get(Constants.DB_ROLE_QUERY);

			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);
			List<String> roles = new ArrayList<String>();
			roles.add("User");
			return roles.toArray(new String[roles.size()]);
		} catch (SQLException sqle) {
			List<String> roles = new ArrayList<String>();	
			roles.add("User");
			return roles.toArray(new String[roles.size()]);
		} finally {
			if (resultSet != null)
				try {
					resultSet.close();
				} catch (SQLException e) {
				}
			if (statement != null)
				try {
					statement.close();
				} catch (SQLException e) {
				}
		}
	}
}
