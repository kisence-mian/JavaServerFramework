package com.frameWork.service.config.configs;

import com.frameWork.service.config.ConfigServiceBase;

public class ConfigLog extends ConfigServiceBase
{
	static public boolean s_isDebug = false;
	static public int s_MaxLogCount = 1000; 
	
	public ConfigLog() 
	{
		m_configName = "DataBaseConfig";
		
		Init(m_configName);
		
		try 
		{
			s_isDebug     = GetBool("IsDebug");
			s_MaxLogCount = GetInt("MaxLogCount");
		} 
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
