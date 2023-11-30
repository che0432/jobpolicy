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

    //�ϸ�ũ �߰�(���ڸ�������å)
    public void addBookmarkPolicyapi(Bookmark info) {

        try {
        	// DB ���� Ȯ��
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

    //�ϸ�ũ �߰�(�������ڸ�����)
    public void addBookmarkPublicjob(Bookmark info) {
        try {
        	// DB ���� Ȯ��
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

    //�ϸ�ũ ����
    public void delBookmark(int bookmarkId) {
        try {
        	// DB ���� Ȯ��
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

    //�ϸ�ũ ��ȸ
    public Bookmark[] checkBookmark(String userId, int page) {
        Bookmark[] bookmarkList = new Bookmark[20];

        try {
        	// DB ���� Ȯ��
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