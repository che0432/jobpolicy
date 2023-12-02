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

    // 정렬(날짜->최신순, 등록순)
    public PolicyApi[] sortedByDate(String sortOrder, int page) {
        PolicyApi[] policies = new PolicyApi[20];

        try {
            // DB 연결 확인
            validateDBConnection();

            String orderClause = sortOrder.equals("등록순") ? "ASC" : "DESC";
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

    // 카테고리별 필터링 기능
    public PolicyApi[] getPolicyByCategory(String category, int page) {
        PolicyApi[] filteredPolicies = new PolicyApi[20];

        try {
            // DB 연결 확인
            validateDBConnection();

            // 기업, 생활, 구직, 주거, 재직활동
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
            // DB 연결 확인
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