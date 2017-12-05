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
 * ���裺 1. ÿ����ĩ��1000Ԫ�� 2. ÿ����ֵ����ﵽ10%�����Ǳ����10%�Ļ���ݶ�ﵽ20%�ͳ���20%�����Դ����ơ�
 */

public class StartPointEnhance2 {

	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/index_profit_prod?autoReconnect=true&useSSL=false";
	static final String USER = "root";
	static final String PASS = "root";
	static final BigDecimal ZERO = new BigDecimal(0);
	static final BigDecimal ONE = new BigDecimal(1);
	static final BigDecimal ONE_HUNDRED = new BigDecimal(100);
	static final BigDecimal EQUAL_THRESHOLD = new BigDecimal(0.000000001);
	static final BigDecimal PROFIT_THRESHOLD = new BigDecimal(0.10);

	static LogUtil log = new LogUtil();

	static HashMap<Integer, Fund> fundMap = new HashMap<Integer, Fund>();
	static ArrayList<Fund> fundList = new ArrayList<Fund>();
	static BigDecimal originInput = new BigDecimal(1000);
	static BigDecimal avgCost = new BigDecimal(0.00);
	static int profitCount = 0;
	// static boolean

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		simulate();
	}

	public static void simulate() {
		initialize();
		getMarketData();
		inputPerMonth();
	}

	private static void inputPerMonth() {
		// TODO Auto-generated method stub
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
		avgCost = new BigDecimal(0.00);
		avgCost = avgCost.setScale(2, BigDecimal.ROUND_HALF_UP);

		BigDecimal profitLevel = new BigDecimal(0);
		profitLevel = profitLevel.setScale(0, BigDecimal.ROUND_HALF_UP);
		BigDecimal curProfitLevel = new BigDecimal(0);
		curProfitLevel = curProfitLevel.setScale(0, BigDecimal.ROUND_HALF_UP);

		for (int i = 1; i < fundList.size(); i++) {
			Fund fund = fundList.get(i);
			String curDate = fund.getDate();
			curPrice = BigDecimal.valueOf(fund.getPriceBalanced());
			// ��������
			if (avgCost.subtract(ZERO).compareTo(EQUAL_THRESHOLD) > 0) {
				curProfitLevel = curPrice.divide(avgCost, 2, BigDecimal.ROUND_DOWN).subtract(ONE);
				// log.info("curProfitLevel1: " + curProfitLevel.toString());
				// curProfitLevel = curProfitLevel.setScale(2, BigDecimal.ROUND_DOWN);
				// log.info("curProfitLevel2: " + curProfitLevel.toString());
				// log.info("compare: " + curProfitLevel.compareTo(PROFIT_THRESHOLD));
				// log.info("compare2: " +
				// curProfitLevel.subtract(PROFIT_THRESHOLD).compareTo(EQUAL_THRESHOLD));
				if ((curProfitLevel.subtract(PROFIT_THRESHOLD).compareTo(EQUAL_THRESHOLD) >= 0)
						&& (!curProfitLevel.equals(profitLevel))) {
					curProfitLevel = curProfitLevel.setScale(1, BigDecimal.ROUND_DOWN);
					curAmount = allAmount.multiply(curProfitLevel);
					curAmount = curAmount.setScale(0, BigDecimal.ROUND_HALF_UP);
					curProfit = curPrice.multiply(curAmount);
					allProfit = allProfit.add(curProfit);
					allAmount = allAmount.subtract(curAmount);
					allInput = allInput.subtract(curAmount.multiply(avgCost));
					log.info(String.format("���Ӿ��� %s ��ÿ�� %s �ļ۸������� %s �ݣ��������� %s Ԫ", curDate, curPrice.toString(),
							curAmount.toString(), curProfit.toString()));
					log.info(String.format("�ϼƣ����Ӿ���Ͷ�� %s Ԫ��������� %s Ԫ�����л���ݶ� %s �ݣ�ÿ��ƽ���ɱ� %s Ԫ��", allInput.toString(),
							allProfit.toString(), allAmount.toString(), avgCost.toString()));
					profitLevel = curProfitLevel;
					profitCount++;
					if (profitCount == 3) {
						System.exit(0);
					}
				}
			}

			// Input
			if (!curDate.substring(5, 7).equals(fundList.get(i - 1).getDate().substring(5, 7))) {
				// it is the beginning of next month.
				curAmount = (originInput.divide(curPrice, 0, BigDecimal.ROUND_HALF_UP));
				curInput = curAmount.multiply(curPrice);
				curInput = curInput.setScale(2, BigDecimal.ROUND_HALF_UP);
				log.info(String.format("���Ӿ��� %s ��ÿ�� %s �ļ۸����� %s �ݣ����ƻ��� %s Ԫ", curDate, curPrice.toString(),
						curAmount.toString(), curInput.toString()));

				// current holding.
				allInput = allInput.add(curInput);
				allAmount = allAmount.add(curAmount);
				avgCost = (allInput.divide(allAmount, 2, BigDecimal.ROUND_HALF_UP));
				log.info(String.format("�ϼƣ����Ӿ���Ͷ�� %s Ԫ��������� %s Ԫ�����л���ݶ� %s �ݣ�ÿ��ƽ���ɱ� %s Ԫ��", allInput.toString(),
						allProfit.toString(), allAmount.toString(), avgCost.toString()));
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
		log.info("�Ը�Ȩ�۸���д���");
	}

}
