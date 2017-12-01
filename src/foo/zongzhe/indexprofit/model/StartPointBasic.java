package foo.zongzhe.indexprofit.model;

import foo.zongzhe.indexprofit.entity.Fund;
import foo.zongzhe.utils.DataUtil;
import foo.zongzhe.utils.LogUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.ArrayList;
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
	static ArrayList<Fund> fundList = new ArrayList<Fund>();
	static BigDecimal originInput = new BigDecimal(1000);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		initialize();
		getMarketData();
		inputPerMonth();
	}

	private static void inputPerMonth() {
		// TODO Auto-generated method stub
		String prevDate = "2009-03-05";
		BigDecimal curAmount = new BigDecimal(0);
		curAmount = curAmount.setScale(0, BigDecimal.ROUND_HALF_UP);
		BigDecimal curPrice = new BigDecimal(0.00);
		curPrice = curPrice.setScale(4, BigDecimal.ROUND_HALF_UP);
		BigDecimal curInput = new BigDecimal(0.00);
		curInput = curInput.setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal curProfit = new BigDecimal(0.00);
		curProfit = curProfit.setScale(3, BigDecimal.ROUND_HALF_UP);

		BigDecimal allAmount = new BigDecimal(0);
		allAmount = allAmount.setScale(0, BigDecimal.ROUND_HALF_UP);
		BigDecimal allProfit = new BigDecimal(0.00);
		allProfit = allProfit.setScale(3, BigDecimal.ROUND_HALF_UP);
		BigDecimal allInput = new BigDecimal(0.00);
		allInput = allInput.setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal avgCost = new BigDecimal(0.00);
		avgCost = avgCost.setScale(2, BigDecimal.ROUND_HALF_UP);

		for (int i = 1; i < fundList.size(); i++) {
			Fund fund = fundList.get(i);
			String curDate = fund.getDate();
			// Input
			if (!curDate.substring(5, 7).equals(fundList.get(i - 1).getDate().substring(5, 7))) {
				// it is the beginning of next month.
				curPrice = BigDecimal.valueOf(fund.getPriceOrigin());
				curAmount = (originInput.divide(curPrice, 0, BigDecimal.ROUND_HALF_UP));
				curInput = curAmount.multiply(curPrice);
				curInput = curInput.setScale(2, BigDecimal.ROUND_HALF_UP);
				System.out.println(String.format("粽子君在 %s 以每份 %s 的价格购入了 %s 份，共计花费 %s 元", curDate, curPrice.toString(),
						curAmount.toString(), curInput.toString()));

				// current holding.
				allInput = allInput.add(curInput);
				allAmount = allAmount.add(curAmount);
				avgCost = (allInput.divide(allAmount, 2, BigDecimal.ROUND_HALF_UP));
				System.out.println(String.format("粽子君合计投入了 %s 元，持有基金份额 %s 份，每份平均成本 %s 元。", allInput.toString(),
						allAmount.toString(), avgCost.toString()));
			}
			// Output

		}
	}

	private static void getMarketData() {
		// TODO Auto-generated method stub
		DataUtil dataUtil = new DataUtil();
		// fundMap = dataUtil.getDataAsMap("sh_481009");
		fundList = dataUtil.getDataAsList("sh_481009");
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
	}

	private static void initialize() {
		// TODO Auto-generated method stub
		log.info("Starting index profit.");
	}

}
