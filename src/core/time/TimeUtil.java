package core.time;

import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

public class TimeUtil
{
	public static Date Now()
	{
		return new Date();
	}
	
	public static boolean IsToday(Date date)
	{
		return DateUtils.isSameDay(Now(),date);
	}
}
