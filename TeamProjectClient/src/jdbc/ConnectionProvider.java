package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import setting.ServerSettings;

public class ConnectionProvider {
	public static Connection getConnection() throws SQLException	{
		final String URL = "jdbc:oracle:thin:@" + ServerSettings.DB_SEVER_IP.getString() + ":" + ServerSettings.DB_SERVER_PORT.getInteger() + ":xe";
		final String USER_ID = ServerSettings.DB_ID.getString();
		final String USER_PW = ServerSettings.DB_PW.getString();
		
		return DriverManager.getConnection(URL, USER_ID, USER_PW);	
	}
}