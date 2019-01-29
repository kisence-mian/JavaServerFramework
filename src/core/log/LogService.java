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
		 GetLoger().error(LogConetnt + "\n" + GetCallStrack());
	}
	
	static public void Exception(String modelName,String LogConetnt , Exception e)
	{
		 GetLoger().error(LogConetnt+ "\n" + GetCallStrack(e));
	}
	
	public static String GetCallStrack() {
		
		String content = "";
		
        Throwable ex = new Throwable();
        StackTraceElement[] stackElements = ex.getStackTrace();
        
        if (stackElements != null) {
            for (int i = 2; i < stackElements.length; i++) {
            	content += "\t at " +  stackElements[i].getClassName() +"." + stackElements[i].getMethodName()
            			+ "(" + stackElements[i].getFileName() + ":" + stackElements[i].getLineNumber()+ ")\n";
            }
        }
        
        return content;
    }
	
	public static String GetCallStrack(Exception e) {
		
		String content = "";
		
        StackTraceElement[] stackElements = e.getStackTrace();
        
        if (stackElements != null) {
            for (int i = 0; i < stackElements.length; i++) {
            	content += "\t at " +  stackElements[i].getClassName() +"." + stackElements[i].getMethodName()
            			+ "(" + stackElements[i].getFileName() + ":" + stackElements[i].getLineNumber()+ ")\n";
            }
        }
        
        return content;
    }
}
