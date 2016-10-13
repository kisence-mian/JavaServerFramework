package com.frameWork.service.config.configs;

import com.frameWork.service.config.ConfigServiceBase;

public class ServiceConfig  extends ConfigServiceBase
{
	public static int s_Port = 23333;
	
	public static void Init() 
	{
		m_configName = "ServiceConfig";
		
		try 
		{
			SetConfigName(m_configName);
			
			s_Port  = GetInt("Port"); 
		} 
		catch (Exception e)
		{
			System.out.println("Config " + m_configName + " not find !");
		}

	}
}
