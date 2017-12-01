package foo.zongzhe.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtil {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DateUtil du = new DateUtil();
		System.out.println(du.getCurrentTime());
	}

	public String getCurrentTime() {
		String currentTime = "";
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		currentTime = sdf.format(cal.getTime()).toString();
		return currentTime;
	}

	public String getNextDay(String currentDay) {
		String nextDay = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(sdf.parse(currentDay));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.add(Calendar.DATE, 1); // number of days to add
		nextDay = sdf.format(c.getTime()); // dt is now the new date
		return nextDay;
	}
}
