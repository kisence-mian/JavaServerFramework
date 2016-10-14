package com.frameWork.service.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigServiceBase 
{
	public static  String m_configName = "";
	
	static Properties m_propertys;
	
	public static void SetConfigName(String l_configName) throws IOException
	{
		m_configName = l_configName;
		
		LoadConfig();
	}
	
	public static  void LoadConfig() throws IOException
	{
		m_propertys = new Properties();  

		FileInputStream fis = new FileInputStream( ConfigService.configPath + m_configName + ".txt" );
		m_propertys.load(fis);
		
		fis.close();

	}
	
	public static String GetString(String key) throws Exception 
	{
		if(m_propertys != null)
		{
			return m_propertys.getProperty(key);
		}
		else 
		{
			throw new Exception("not find " + key + " property");
		}
	}
	
	public static int GetInt(String key) throws Exception
	{
		if(m_propertys != null)
		{
			return Integer.parseInt(  m_propertys.getProperty(key));
		}
		else 
		{
			throw new Exception("not find " + key + " property");
		}
	}
	
	public static float GetFloat(String key) throws Exception
	{
		if(m_propertys != null)
		{
			return Float.parseFloat( m_propertys.getProperty(key));
		}
		else 
		{
			throw new Exception("not find " + key + " property");
		}
	}
	
	public static boolean GetBool(String key) throws Exception
	{
		
		if(m_propertys != null)
		{
			return Boolean.parseBoolean(m_propertys.getProperty(key));
		}
		else 
		{
			throw new Exception("not find " + key + " property");
		}
	}
}
