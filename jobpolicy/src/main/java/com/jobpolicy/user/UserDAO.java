package main.java.com.jobpolicy.user;
import java.sql.Connection;
import java.sql.Statement;

import main.java.com.jobpolicy.DBConn;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
	Connection CN = null;
	Statement ST = null; //ST = CN.createStatement()
	PreparedStatement PST = null; //PST=CN.prePareStatement("insert~")
	ResultSet RS = null; //RS = ST.executeQuery("select~");
	
	String msg = "";
	
	public UserDAO() {}

	// �Ҹ���
	protected void finalize() {
		closeDBConnection();
	}

	//DB ������ ��ȿ���� Ȯ��. ��ȿ���� ������ ���ο� ���� ����
	void validateDBConnection() {
		try {
			if (CN == null) {
				CN = DBConn.getConnection();
				System.err.println("DB�� ����Ǿ����ϴ�.");
			}
			else if (!CN.isValid(15)) { //15�� �̳��� �������� ������ ������,
				CN.close();
				CN = DBConn.getConnection();
				System.err.println("DB�� �翬��Ǿ����ϴ�.");
			}
		}
		catch (SQLException e) {
			System.err.println("DB�� ������ �� �����ϴ�.");
			e.printStackTrace();
		}
	}
	
	//DB ���� ����
	public void closeDBConnection() {
		try {
			DBConn.closeConnection(CN);
			System.err.println("DB ������ �����Ǿ����ϴ�.");
		}
		catch (SQLException e) {
			System.err.println("DB ���� ���� �� ����!");
			e.printStackTrace();
		}
	}

	// ���̵� ���� ���� üũ
	public int userIdCheck(String userid) {
		try {
			// DB ���� Ȯ��
			validateDBConnection();
			
			msg = "select userid from user where userid = ?";
			PST = CN.prepareStatement(msg);
			PST.setString(1, userid);
			RS = PST.executeQuery();
			if(RS.next())
				return 1; // ���̵� ���� 1 ����
			return 0; //�������� �ʴ� ���̵� 0 ���� 
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (RS != null)		try { RS.close(); }		catch (Exception e) {}
			if (PST != null)	try { PST.close(); }	catch (Exception e) {}
		}
		return -2; //����
	}
	
	// ȸ������ ����
	public void userRegister(User user) {
		try {
			// DB ���� Ȯ��
			validateDBConnection();
			
			//userid, password, gender, birth, createAt
			msg = "insert into user values(?,UNHEX(MD5(?)),?,?,default)";
			PST = CN.prepareStatement(msg);
			PST.setString(1, user.getUserid());
			PST.setString(2, user.getPassword());
			PST.setInt(3, user.getGender());
			PST.setString(4, user.getBirth());
			PST.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (PST != null)	try { PST.close(); }	catch (Exception e) {}
		}
	}
	
	// �α���
	public int login(String userid, String password) {
		try {
			if (userIdCheck(userid) == -1) {
				return 0; //�������� �ʴ� ���̵� 0 ����
			}
			// DB ���� Ȯ��
			validateDBConnection();

			msg = "select userid from user where userid = ? and password = UNHEX(MD5(?))";
			PST = CN.prepareStatement(msg);
			PST.setString(1, userid);
			PST.setString(2, password);
			RS = PST.executeQuery();
			
			//RS.next()�� �������� �� ������� �����Ѵٸ� true
			if(RS.next()) 
				return 1; //�α��� ���� ���� 1 ����
			return -1; //��й�ȣ ����ġ ���� -1 ����
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (RS != null)		try { RS.close(); }		catch (Exception e) {}
			if (PST != null)	try { PST.close(); }	catch (Exception e) {}
		}
		return -2; //����
	}
	
	// ȸ�� ���� ��ȸ
	public User userInfo(String userid) {
		User user = new User();
		try {
			// DB ���� Ȯ��
			validateDBConnection();
			
			msg = "select * from user where userid = ?";
			PST = CN.prepareStatement(msg);
			PST.setString(1, userid);
			RS = PST.executeQuery();
			//userid, password, gender, birth, createAt
			if(RS.next()==true) {
				user.setUserid(RS.getString("userid"));
				user.setPassword(RS.getString("password"));
				user.setGender(RS.getInt("gender"));
				user.setBirth(RS.getString("birth"));
				user.setCreateAt(RS.getString("createAt"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (RS != null)		try { RS.close(); }		catch (Exception e) {}
			if (ST != null)		try { ST.close(); }		catch (Exception e) {}
		}
		return user;
	}
	
	// ȸ�� Ż��
	public void userDelete(String userid) {
		try {
			// DB ���� Ȯ��
			validateDBConnection();
			
			msg = "delete from user where userid = ?";
			PST = CN.prepareStatement(msg);
			PST.setString(1, userid);
			PST.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (ST != null)		try { ST.close(); }		catch (Exception e) {}
		}
	}
}
