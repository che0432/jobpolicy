package User;

import Main.DBConnManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    Connection CN = null;
    PreparedStatement PST = null; //PST=CN.prePareStatement("insert~")
    ResultSet RS = null; //RS = ST.executeQuery("select~");

    String msg = "";

    //DB ������ ��ȿ���� Ȯ��. ��ȿ���� ������ ���ο� ���� ����
    void validateDBConnection() {
        try {
            if (CN == null) {
                CN = DBConnManager.getConnection();
                System.err.println("DB�� ����Ǿ����ϴ�.");
            }
            else if (!CN.isValid(15)) { //15�� �̳��� �������� ������ ������,
                CN.close();
                CN = DBConnManager.getConnection();
                System.err.println("DB�� �翬��Ǿ����ϴ�.");
            }
        }
        catch (SQLException e) {
            System.err.println("DB�� ������ �� �����ϴ�.");
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
}
