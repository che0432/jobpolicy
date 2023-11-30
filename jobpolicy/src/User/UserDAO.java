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

    //DB 연결이 유효한지 확인. 유효하지 않으면 새로운 연결 수립
    void validateDBConnection() {
        try {
            if (CN == null) {
                CN = DBConnManager.getConnection();
                System.err.println("DB가 연결되었습니다.");
            }
            else if (!CN.isValid(15)) { //15초 이내에 정상적인 응답이 없으면,
                CN.close();
                CN = DBConnManager.getConnection();
                System.err.println("DB가 재연결되었습니다.");
            }
        }
        catch (SQLException e) {
            System.err.println("DB에 접근할 수 없습니다.");
            e.printStackTrace();
        }
    }
    
    // 아이디 존재 여부 체크
    public int userIdCheck(String userid) {
        try {
        	// DB 연결 확인
            validateDBConnection();
        	
            msg = "select userid from user where userid = ?";
            PST = CN.prepareStatement(msg);
            PST.setString(1, userid);
            RS = PST.executeQuery();
            if(RS.next())
                return 1; // 아이디 존재 1 리턴
            return 0; //존재하지 않는 아이디 0 리턴
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (RS != null)		try { RS.close(); }		catch (Exception e) {}
            if (PST != null)	try { PST.close(); }	catch (Exception e) {}
        }
        return -2; //오류
    }

    // 회원가입 저장
    public void userRegister(User user) {
        try {
        	// DB 연결 확인
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

    // 로그인
    public int login(String userid, String password) {
        try {
            if (userIdCheck(userid) == -1) {
                return 0; //존재하지 않는 아이디 0 리턴
            }
            
            // DB 연결 확인
            validateDBConnection();
            
            msg = "select userid from user where userid = ? and password = UNHEX(MD5(?))";
            PST = CN.prepareStatement(msg);
            PST.setString(1, userid);
            PST.setString(2, password);
            RS = PST.executeQuery();

            //RS.next()을 실행했을 때 결과값이 존재한다면 true
            if(RS.next())
                return 1; //로그인 성공 정수 1 리턴
            return -1; //비밀번호 불일치 정수 -1 리턴
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (RS != null)		try { RS.close(); }		catch (Exception e) {}
            if (PST != null)	try { PST.close(); }	catch (Exception e) {}
        }
        return -2; //오류
    }
}
