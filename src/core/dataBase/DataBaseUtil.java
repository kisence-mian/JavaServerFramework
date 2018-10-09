package core.dataBase;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataBaseUtil 
{
	public static Date StringToDate(Timestamp timestamp)
	{
		return timestamp;
	}
	
	public static String DateToString(Date date)
	{
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String content = df.format(date);
		
		return content;
	}
}
