package main.java.com.jobpolicy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConn {

	static String driver = "com.mysql.cj.jdbc.Driver";
	static String url = "jdbc:mysql://localhost:3306/jobpolicy?serverTimezone=UTC";
	static String id = "root"; 
	static String pwd = "비밀번호";
	
	public static Connection getConnection() throws SQLException {
		Connection conn = null;
		try {
        	Class.forName(driver);
            try {
            	conn = DriverManager.getConnection(url, id, pwd);
			} catch (SQLException e) {
                System.out.println("Connection Failed!");
                e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
            System.out.println("Connection Failed. Check Driver or URL");
            e.printStackTrace();
		}
		return conn;
	}
}
