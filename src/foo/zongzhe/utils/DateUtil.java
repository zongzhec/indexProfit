package foo.zongzhe.utils;

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
}
