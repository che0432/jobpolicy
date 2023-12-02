package PolicyApi;

import Main.DBConnManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PolicyApiDAO {
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
    public PolicyApi[] sortedByDate(String sortOrder, int page) {
        PolicyApi[] policies = new PolicyApi[20];

        try {
            // DB ���� Ȯ��
            validateDBConnection();

            String orderClause = sortOrder.equals("��ϼ�") ? "ASC" : "DESC";
            sql = "SELECT * FROM policyapi ORDER BY beginDate " + orderClause + " LIMIT 20 OFFSET ?";
            PST = CN.prepareStatement(sql);
            PST.setInt(1, (page - 1) * 20);
            RS = PST.executeQuery();

            int i = 0;
            while (RS.next()) {
                PolicyApi policy = new PolicyApi(RS.getInt(1),RS.getString(2),
                        RS.getString(3),RS.getString(4),RS.getString(5),
                        RS.getString(6),RS.getString(7));
                policies[i] = policy;
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (RS != null)		try { RS.close(); }		catch (Exception e) {}
            if (PST != null)	try { PST.close(); }		catch (Exception e) {}
        }
        return policies;
    }

    // ī�װ��� ���͸� ���
    public PolicyApi[] getPolicyByCategory(String category, int page) {
        PolicyApi[] filteredPolicies = new PolicyApi[20];

        try {
            // DB ���� Ȯ��
            validateDBConnection();

            // ���, ��Ȱ, ����, �ְ�, ����Ȱ��
            sql = "SELECT * FROM policyapi WHERE category = \"" + category
                    + "\" limit 20 offset " + (page - 1) * 20;
            PST = CN.prepareStatement(sql);
            RS = PST.executeQuery();

            int i = 0;
            while (RS.next()) {
                PolicyApi policy = new PolicyApi(RS.getInt(1),RS.getString(2),
                        RS.getString(3),RS.getString(4),RS.getString(5),
                        RS.getString(6),RS.getString(7));
                filteredPolicies[i] = policy;
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (RS != null)		try { RS.close(); }		catch (Exception e) {}
            if (PST != null)	try { PST.close(); }		catch (Exception e) {}
        }

        return filteredPolicies;
    }

    public PolicyApi[] selectPolicyApi(int page){
        PolicyApi[] policyApiList = new PolicyApi[20];
        try{
            // DB ���� Ȯ��
            validateDBConnection();

            sql = "select * from policyapi order by policyAId limit 20 offset ?";
            PST = CN.prepareStatement(sql);
            PST.setInt(1, (page - 1) * 20);
            RS = PST.executeQuery();

            int i = 0;
            while (RS.next()) {
                PolicyApi newOne = new PolicyApi(RS.getInt(1),RS.getString(2),
                        RS.getString(3),RS.getString(4),RS.getString(5),
                        RS.getString(6),RS.getString(7));
                policyApiList[i++] = newOne;
            }
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            if (RS != null)		try { RS.close(); }		catch (Exception e) {}
            if (PST != null)	try { PST.close(); }	catch (Exception e) {}
        }
        return policyApiList;
    }
}