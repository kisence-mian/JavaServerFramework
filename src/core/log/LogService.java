package core.log;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class LogService 
{
	final static String s_logPath = "./Log/";
	
	static String s_modelName = "LogService";
	static String s_LogName = "";
	
	static Log s_log;
	
	public enum LogLevel
	{
		Log,
		Warn,
		Error,
		Exception
	}
	
	public static Log GetLoger()
	{
		if(s_log ==null)
		{
			s_log = LogFactory.getLog(LogService.class);
		
		}
		
		return s_log;
	}
	
	static public void Log(String modelName,String LogConetnt)
	{
		 GetLoger().info(LogConetnt);
	}
	
	static public void Warn(String modelName,String LogConetnt)
	{
		 GetLoger().warn(LogConetnt);
	}
	
	static public void Error(String modelName,String LogConetnt)
	{
		 GetLoger().error(LogConetnt);
	}
	
	static public void Exception(String modelName,String LogConetnt , Exception e)
	{
		 GetLoger().fatal(LogConetnt, e);
	}
}
