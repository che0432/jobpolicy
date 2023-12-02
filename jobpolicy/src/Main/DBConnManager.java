package Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnManager {
	static String url = "jdbc:mysql://localhost:3306/jobpolicy?serverTimezone=UTC";
	static String id = "root"; 
	static String pwd = "비밀번호";

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, id, pwd);
	}
	public static void closeConnection(Connection conn) throws SQLException {
		if (conn != null)
			conn.close();
	}
}