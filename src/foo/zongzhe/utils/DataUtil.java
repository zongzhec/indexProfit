package foo.zongzhe.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import foo.zongzhe.indexprofit.entity.Fund;

public class DataUtil {

	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/index_profit_prod?autoReconnect=true&useSSL=false";
	static final String USER = "root";
	static final String PASS = "root";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HashMap<Integer, Fund> fundMap = new HashMap<Integer, Fund>();
		// fundMap = getDataAsMap("sh_481009");
	}

	public HashMap<Integer, Fund> getDataAsMap(String tableName) {
		// TODO Auto-generated method stub
		Connection conn = null;
		Statement stmt = null;
		HashMap<Integer, Fund> fundMap = new HashMap<Integer, Fund>();

		try {
			// ע�� JDBC ����
			Class.forName("com.mysql.jdbc.Driver");

			// ������
			System.out.println("�������ݿ�...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			// ִ�в�ѯ
			System.out.println("���ڶ�ȡ����...");
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM " + tableName;
			ResultSet rs = stmt.executeQuery(sql);

			// չ����������ݿ�
			while (rs.next()) {
				// ͨ���ֶμ���
				int seq = rs.getInt("seq");
				String date = rs.getString("date");
				double priceOrigin = rs.getDouble("price_origin");
				double priceBalanced = rs.getDouble("price_balanced");
				Fund fund = new Fund();
				fund.setDate(date);
				fund.setPriceOrigin(priceOrigin);
				fund.setPriceBalanced(priceBalanced);
				// Store the data in map
				fundMap.put(seq, fund);
			}
			// ��ɺ�ر�
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException se) {
			// ���� JDBC ����
			se.printStackTrace();
		} catch (Exception e) {
			// ���� Class.forName ����
			e.printStackTrace();
		} finally {
			// �ر���Դ
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} // ʲô������
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		System.out.println("�������Ӿ����˴ζ�ȡ��" + fundMap.size() + "������!");
		return fundMap;
	}

}
