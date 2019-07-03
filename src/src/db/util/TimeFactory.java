package db.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Formatted time factory
 * 
 * @author DesmondCobb
 * @version 20190703.2225
 *
 */
public class TimeFactory {

	/**
	 * Get current time
	 * @return
	 */
	public static long getTime() {
		Date now = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String timeStr = formatter.format(now);
		long time = Long.parseLong(timeStr);
		return time;
	}

	/**
	 * Get time 30 minutes before now
	 * @return
	 */
	public static long getTimeSomeWhatBeforeNow() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar timeAgo = Calendar.getInstance();
		timeAgo.add(Calendar.MINUTE, -30);
		Date before30Min = timeAgo.getTime();
		String timeStr = formatter.format(before30Min);
		long time = Long.parseLong(timeStr);
		return time;
	}

	/**
	 * Get time 30 minutes before a given time
	 * @param aTime
	 * @return
	 */
	public static long getTimeSomeWhatBefore(long aTime) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String aTimeStr = String.valueOf(aTime);
		Date aTimeDate;

		String timeStr = null;
		try {
			aTimeDate = formatter.parse(aTimeStr);
			Calendar timeAgo = Calendar.getInstance();
			timeAgo.setTime(aTimeDate);
			timeAgo.add(Calendar.MINUTE, -30);
			Date before30Min = timeAgo.getTime();
			timeStr = formatter.format(before30Min);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		long time = Long.parseLong(timeStr);
		return time;
	}
}
