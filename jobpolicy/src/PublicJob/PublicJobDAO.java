package PublicJob;

import Main.DBConnManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PublicJobDAO {
    Connection CN = null;
    PreparedStatement PST = null; //PST=CN.prePareStatement("insert~")
    ResultSet RS = null; //RS = ST.executeQuery("select~");
    String sql = "";
    
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

    // ����(��¥->�ֽż�, ��ϼ�)
    public PublicJob[] sortedByDate(String sortOrder, int page) {
        PublicJob[] jobs = new PublicJob[20];

        try {
        	// DB ���� Ȯ��
            validateDBConnection();
            
            String orderClause = sortOrder.equals("��ϼ�") ? "ASC" : "DESC";
            sql = "SELECT * FROM publicjob ORDER BY beginDate " + orderClause + " LIMIT 20 OFFSET ?";
            PST = CN.prepareStatement(sql);
            PST.setInt(1, (page - 1) * 20);
            RS = PST.executeQuery();

            int i = 0;
            while (RS.next()) {
                PublicJob pj = new PublicJob(RS.getInt(1),RS.getString(2),
                        RS.getString(3),RS.getString(4),RS.getString(5),
                        RS.getString(6));
                jobs[i] = pj;
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (RS != null)	    try { RS.close(); }		catch (Exception e) {}
            if (PST != null)	try { PST.close(); }	catch (Exception e) {}
        }
        return jobs;
    }

    public PublicJob[] selectPublicJob(int page){
        PublicJob[] policyApiList = new PublicJob[20];
        try{
        	// DB ���� Ȯ��
            validateDBConnection();
            
            sql = "select * from Publicjob order by jobId limit 20 offset ?";
            PST = CN.prepareStatement(sql);
            PST.setInt(1, (page - 1) * 20);
            RS = PST.executeQuery();

            int i = 0;
            while (RS.next()) {
                PublicJob newOne = new PublicJob(RS.getInt(1),RS.getString(2),
                        RS.getString(3),RS.getString(4),RS.getString(5),
                        RS.getString(6));
                policyApiList[i] = newOne;
                i++;
            }
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            if (RS != null)		try { RS.close(); }		catch (Exception e) {}
            if (PST != null)	try { PST.close(); }		catch (Exception e) {}
        }
        return policyApiList;
    }
}