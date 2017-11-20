package foo.zongzhe.indexprofit.model;

import foo.zongzhe.utils.LogUtil;
import java.sql.*;

public class StartingPoint {

	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/index_profit_prod?autoReconnect=true&useSSL=false";
	static final String USER = "root";
	static final String PASS = "root";

	static LogUtil log = new LogUtil();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		initialize();
		getSampleData();
	}

	private static void getSampleData() {
		// TODO Auto-generated method stub
		Connection conn = null;
		Statement stmt = null;
		try {
			// ע�� JDBC ����
			Class.forName("com.mysql.jdbc.Driver");

			// ������
			System.out.println("�������ݿ�...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// ִ�в�ѯ
			System.out.println(" ʵ����Statement��...");
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM sh_510500";
			ResultSet rs = stmt.executeQuery(sql);

			// չ����������ݿ�
			while (rs.next()) {
				// ͨ���ֶμ���
				String date = rs.getString("date");
				double price_origin = rs.getDouble("price_origin");
				double price_balanced = rs.getDouble("price_balanced");
				String event = rs.getString("event");

				// �������
				System.out.println("date: " + date);
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
		System.out.println("Goodbye!");
	}

	private static void initialize() {
		// TODO Auto-generated method stub
		log.info("Starting index profit.");
	}

}
