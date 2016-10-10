package com.frameWork.service.config.configs;

import com.frameWork.service.config.ConfigServiceBase;

public class DataBaseConfig extends ConfigServiceBase 
{
	static public String s_DataBaseURL = "";
	static public String s_UserName    = "";
	static public String s_Pwd         = "";

	static public String s_DataBaseName = "";
	public static int s_dataBaseMaxRequest;
	
	public DataBaseConfig() 
	{
		m_configName = "DataBaseConfig";
		
		Init(m_configName);
		
		try 
		{
			s_DataBaseURL  = GetString("DataBaseURL"); 
			s_DataBaseName = GetString("DataBaseName");
			s_UserName     = GetString("UserName");
			s_Pwd          = GetString("PWD");
			s_dataBaseMaxRequest = GetInt("s_dataBaseMaxRequest");
				
		} 
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
