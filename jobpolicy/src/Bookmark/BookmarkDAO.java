package Bookmark;

import Main.DBConnManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookmarkDAO {
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

    //북마크 추가(일자리지원정책)
    public void addBookmarkPolicyapi(Bookmark info) {

        try {
        	// DB 연결 확인
            validateDBConnection();
            
            //bookmarkId, policyAId, userId, jobId, createAt
            msg = "insert into bookmark values(NULL,?,?,NULL,default)";
            PST = CN.prepareStatement(msg);
            PST.setInt(1, info.getPolicyAId());
            PST.setString(2, info.getUserId());
            PST.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (PST != null)	try { PST.close(); }	catch (Exception e) {}
        }
    }

    //북마크 추가(공공일자리정보)
    public void addBookmarkPublicjob(Bookmark info) {
        try {
        	// DB 연결 확인
            validateDBConnection();
            
            //bookmarkId, policyAId, userId, jobId, createAt
            msg = "insert into bookmark values(NULL,NULL,?,?,default)";
            PST = CN.prepareStatement(msg);
            PST.setObject(1, info.getUserId());
            PST.setObject(2, info.getJobId());
            PST.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (PST != null)	try { PST.close(); }	catch (Exception e) {}
        }
    }

    //북마크 삭제
    public void delBookmark(int bookmarkId) {
        try {
        	// DB 연결 확인
            validateDBConnection();
            
            msg = "delete from bookmark where bookmarkId = ?";
            PST = CN.prepareStatement(msg);
            PST.setInt(1, bookmarkId);
            PST.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (PST != null)		try { PST.close(); }		catch (Exception e) {}
        }
    }

    //북마크 조회
    public Bookmark[] checkBookmark(String userId, int page) {
        Bookmark[] bookmarkList = new Bookmark[20];

        try {
        	// DB 연결 확인
            validateDBConnection();
            
            msg = "select * from bookmark where userid = ? order by jobId ASC limit 20 offset ?";
            PST = CN.prepareStatement(msg);
            PST.setString(1, userId);
            PST.setInt(2, (page - 1) * 20);
            RS = PST.executeQuery();

            int i = 0;
            while (RS.next()) {
                Bookmark bm =  new Bookmark(RS.getInt(1), RS.getInt(2), RS.getString(3), RS.getInt(4), RS.getString(5));
                bookmarkList[i] = bm;
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (RS != null)		try { RS.close(); }		catch (Exception e) {}
            if (PST != null)	try { PST.close(); }	catch (Exception e) {}
        }
        return bookmarkList;
    }
}