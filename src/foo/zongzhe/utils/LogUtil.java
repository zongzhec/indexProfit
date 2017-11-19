package foo.zongzhe.utils;

public class LogUtil {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LogUtil lu = new LogUtil();
		lu.info("Hello there.");
	}

	public void info(String infoMsg) {
		DateUtil du = new DateUtil();
		String currentTime = du.getCurrentTime();
		System.out.println(currentTime + " [INFO] - " + infoMsg);
	}

}
