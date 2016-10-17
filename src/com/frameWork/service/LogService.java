package com.frameWork.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.frameWork.service.LogService.LogLevel;
import com.frameWork.service.config.configs.LogConfig;
import com.frameWork.service.timer.TimerEvent;
import com.frameWork.service.timer.TimerEventListener;
import com.frameWork.service.timer.TimerService;
import com.frameWork.service.timer.TimerService.TimerEnum;

public class LogService
{
	final static String s_logPath = "./Log/";
	
	static String s_modelName = "LogService";
	static String s_LogName = "";
	
	static List<LogInfo> s_LogList = new ArrayList<LogInfo>();
	
	static Map<String, FileOutputStream>  s_FileStream = null;   
	
	public enum LogLevel
	{
		Log,
		Warn,
		Error,
		Exception
	}
	
	public static void Init()
	{
		LogServiceTimerListener lService = new LogServiceTimerListener();
		TimerService.AddListener(TimerEnum.preDay, lService);
	}
	
	static public void Log(String modelName,String LogConetnt)
	{
		if(LogConfig.s_isDebug)
		{
			System.out.println(LogConetnt);
		}
		
		AddLog(LogLevel.Log, modelName, LogConetnt, null);
	}
	
	static public void Warn(String modelName,String LogConetnt)
	{
		if(LogConfig.s_isDebug)
		{
			System.out.println(LogConetnt);
		}
		
		AddLog(LogLevel.Warn, modelName, LogConetnt, null);
	}
	
	static public void Error(String modelName,String LogConetnt)
	{
		if(LogConfig.s_isDebug)
		{
			System.err.println(LogConetnt);
		}
		
		AddLog(LogLevel.Error, modelName, LogConetnt, null);
	}
	
	static public void Exception(String modelName,String LogConetnt , Exception e)
	{
		if(LogConfig.s_isDebug)
		{
			System.err.println(LogConetnt);
			e.printStackTrace();
		}
		
		AddLog(LogLevel.Exception, modelName, LogConetnt, e);
	}
	
	static void AddLog(LogLevel level,String modelName, String LogConetnt,Exception exception)
	{
		LogInfo log = new LogInfo();
		
		log.m_Level      = level;
		log.m_modelName  = modelName;
		log.m_logContent = LogConetnt;
		log.m_Exception  = exception;
		log.m_Date       = new Date();
		
		s_LogList.add(log);
		
		if(s_LogList.size() > LogConfig.s_MaxLogCount)
		{
			SaveLog();
		}
	}
	
	//新的一天
	public static void NewDay()
	{
		SaveLog();
		NewLogFileStream();
		
		LogService.Log(s_modelName, "NewDay " + s_LogName);
	}
	
	static void NewLogFileStream()
	{
		Date currentDate = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);
		
		int year  = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day   = cal.get(Calendar.DAY_OF_MONTH);
		
		s_LogName = "Log" + year+"" + month + "" + day;
		
		if(s_FileStream != null)
		{
			for (Entry<String, FileOutputStream> entry : s_FileStream.entrySet()) 
			{  
				try {
					entry.getValue().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		s_FileStream = new HashMap<String, FileOutputStream>();
	}
	
	static FileOutputStream GetFileStream(String modelName) throws IOException
	{
		if(s_FileStream.containsKey(modelName))
		{
			return s_FileStream.get(modelName);
		}
		else
		{
			String l_path = s_logPath + s_LogName;
			
			String l_pathFile = s_logPath + s_LogName + "/" + modelName + ".txt";
			
			System.out.println(l_path);
			
			File file = new File(l_path);
			
			if(!file.isDirectory())
			{
				file.mkdirs();
			}
			
			if(!file.exists())
			{
				file.createNewFile();
			}
			
			FileOutputStream stream = new FileOutputStream(l_pathFile,true);
			
			s_FileStream.put(modelName, stream);
			
			return stream;
		}
	}
	
	//定时保存日志
	synchronized static void SaveLog()
	{
		if(s_FileStream == null)
		{
			NewLogFileStream();
		}
		
		
		List<LogInfo> LogList = new ArrayList<LogInfo>();
		
		for (int i = 0; i < s_LogList.size(); i++) 
		{
			LogList.add(s_LogList.get(i));
		}
		s_LogList.clear();
		
		
		for (int i = 0; i < LogList.size(); i++) 
		{
			FileOutputStream stream =  null;
			try {
				stream = GetFileStream(LogList.get(i).m_modelName);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			WriteHighLight(stream,LogList.get(i)); //高亮显示错误和异常
			
			WriteFile(stream,LogList.get(i).m_Date.toString() +"\n");
			WriteFile(stream,LogList.get(i).m_modelName.toString() + "    " + LogList.get(i).m_Level + "\n");
			WriteFile(stream,LogList.get(i).m_logContent.toString() +"\n");
			
			//调用堆栈
			WriteStackTrace(stream,LogList.get(i));
			
			WriteHighLight(stream,LogList.get(i)); 
			WriteFile(stream,"\n");
		}
	}
	
	static void WriteHighLight(FileOutputStream stream,LogInfo logInfo)
	{
		if(logInfo.m_Level == LogLevel.Error || 
		   logInfo.m_Level == LogLevel.Exception)
		{
			WriteFile(stream,"--------------------------------------------------------\n");
		}
	}
	
	static void WriteStackTrace(FileOutputStream stream,LogInfo logInfo)
	{
		if(logInfo.m_Exception != null)
		{
			StackTraceElement[] stack = logInfo.m_Exception.getStackTrace();
			
			for (int i = 0; i < stack.length; i++)
			{
				WriteFile(stream,stack[i].toString() + "\n");
			}
		}
	}
	
	static void WriteFile(FileOutputStream stream ,String content)
	{
		try {
			stream.write(content.getBytes("UTF-8"));
		}
		catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}

class LogServiceTimerListener  implements TimerEventListener
{
	@Override
	public void TimeEvent(TimerEvent event) 
	{
		LogService.NewDay();
	}
}

class LogInfo
{
	public Date m_Date;
	public LogLevel m_Level;
	public String m_modelName;
	
	public String m_logContent;
	public Exception m_Exception;
}
