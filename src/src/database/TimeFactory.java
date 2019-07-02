package database;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeFactory {

	public static String getTimeGroupStandard() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String currentTime = df.format(new Date());
		return currentTime;
	}
	
	public static String getTimeSomeWhat() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String currentTime = df.format(new Date());
		return currentTime;
	}

	public static String parseGroupStandard2SomeWhat(String gs) {
		String sw = "";
		String[] gss = gs.split("-");
		for (int i = 0; i < 6; i++) {
			sw += gss[i];
		}
		return sw;
	}

	public static String parseSomeWhat2GroupStandard(String sw) {
		String res = "";
		res += sw.substring(0, 4);
		res += "-";
		res += sw.substring(4, 6);
		res += "-";
		res += sw.substring(6, 8);
		res += "-";
		res += sw.substring(8, 10);
		res += "-";
		res += sw.substring(10, 12);
		res += "-";
		res += sw.substring(12, 14);
		return res;
	}

}
