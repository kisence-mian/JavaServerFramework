package com.frameWork.service.config.configs;

import com.frameWork.service.config.ConfigServiceBase;

public class LogConfig extends ConfigServiceBase
{
	static public boolean s_isDebug = true;
	static public int s_MaxLogCount = 50; 
	
	public static void Init() 
	{
		m_configName = "LogConfig";
		
		try 
		{
			SetConfigName(m_configName);
			
			s_isDebug     = GetBool("IsDebug");
			s_MaxLogCount = GetInt("MaxLogCount");
		} 
		catch (Exception e)
		{
			System.out.println("Config " + m_configName + " not find !");
		}
		
	}

}
