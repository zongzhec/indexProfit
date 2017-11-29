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
			// 注册 JDBC 驱动
			Class.forName("com.mysql.jdbc.Driver");

			// 打开链接
			System.out.println("连接数据库...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			// 执行查询
			System.out.println("正在读取数据...");
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM " + tableName;
			ResultSet rs = stmt.executeQuery(sql);

			// 展开结果集数据库
			while (rs.next()) {
				// 通过字段检索
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
		System.out.println("报告粽子君，此次读取了" + fundMap.size() + "条数据!");
		return fundMap;
	}

}
