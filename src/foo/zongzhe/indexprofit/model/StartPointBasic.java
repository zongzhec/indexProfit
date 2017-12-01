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
 * ���裺 1. ÿ����ĩ��1000Ԫ�� 2. ÿ����10%��������10%��
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
				System.out.println(String.format("���Ӿ��� %s ��ÿ�� %s �ļ۸����� %s �ݣ����ƻ��� %s Ԫ", curDate, curPrice.toString(),
						curAmount.toString(), curInput.toString()));

				// current holding.
				allInput = allInput.add(curInput);
				allAmount = allAmount.add(curAmount);
				avgCost = (allInput.divide(allAmount, 2, BigDecimal.ROUND_HALF_UP));
				System.out.println(String.format("���Ӿ��ϼ�Ͷ���� %s Ԫ�����л���ݶ� %s �ݣ�ÿ��ƽ���ɱ� %s Ԫ��", allInput.toString(),
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
			// ע�� JDBC ����
			Class.forName("com.mysql.jdbc.Driver");

			// ������
			System.out.println("�������ݿ�...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// ִ�в�ѯ
			System.out.println(" ʵ����Statement��...");
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM sh_481009";
			ResultSet rs = stmt.executeQuery(sql);

			// չ����������ݿ�
			while (rs.next()) {
				// ͨ���ֶμ���
				String date = rs.getString("date");
				double price_origin = rs.getDouble("price_origin");
				double price_balanced = rs.getDouble("price_balanced");

				// �������
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
	}

	private static void initialize() {
		// TODO Auto-generated method stub
		log.info("Starting index profit.");
	}

}
