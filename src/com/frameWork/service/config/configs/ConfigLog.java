package com.frameWork.service.config.configs;

import com.frameWork.service.config.ConfigServiceBase;

public class ConfigLog extends ConfigServiceBase
{
	static public boolean s_isDebug = false;
	
	
	public ConfigLog() 
	{
		m_configName = "DataBaseConfig";
		
		Init(m_configName);
		
		try 
		{
			s_isDebug  = GetBool("s_isDebug");
		} 
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
