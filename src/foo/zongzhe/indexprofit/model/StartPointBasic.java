package foo.zongzhe.indexprofit.model;

import foo.zongzhe.indexprofit.entity.Fund;
import foo.zongzhe.utils.DataUtil;
import foo.zongzhe.utils.LogUtil;
import java.sql.*;
import java.util.HashMap;

/**
 * 假设： 1. 每个月末存1000元； 2. 每上涨10%，便卖出10%；
 */

public class StartPointBasic {

	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/index_profit_prod?autoReconnect=true&useSSL=false";
	static final String USER = "root";
	static final String PASS = "root";

	static LogUtil log = new LogUtil();

	static HashMap<Integer, Fund> fundMap = new HashMap<Integer, Fund>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		initialize();
		getMarketData();
		inputPerMonth();
	}

	private static void inputPerMonth() {
		// TODO Auto-generated method stub
		
	}

	private static void getMarketData() {
		// TODO Auto-generated method stub
		DataUtil dataUtil = new DataUtil();
		fundMap = dataUtil.getDataAsMap("sh_481009");
	}

	private static void getSampleData() {
		// TODO Auto-generated method stub
		Connection conn = null;
		Statement stmt = null;
		try {
			// 注册 JDBC 驱动
			Class.forName("com.mysql.jdbc.Driver");

			// 打开链接
			System.out.println("连接数据库...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// 执行查询
			System.out.println(" 实例化Statement对...");
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM sh_481009";
			ResultSet rs = stmt.executeQuery(sql);

			// 展开结果集数据库
			while (rs.next()) {
				// 通过字段检索
				String date = rs.getString("date");
				double price_origin = rs.getDouble("price_origin");
				double price_balanced = rs.getDouble("price_balanced");

				// 输出数据
				System.out.println("date: " + date);
			}
			// 完成后关闭
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException se) {
			// 处理 JDBC 错误
			se.printStackTrace();
		} catch (Exception e) {
			// 处理 Class.forName 错误
			e.printStackTrace();
		} finally {
			// 关闭资源
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} // 什么都不做
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
