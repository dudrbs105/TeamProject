package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionProvider {
	public static Connection getConnection() throws SQLException	{
		final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
		final String USER_ID = "lim8143";
		final String USER_PW = "1234";
		
		return DriverManager.getConnection(URL, USER_ID, USER_PW);	
	}
}