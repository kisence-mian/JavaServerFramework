package com.service.config;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigServiceBase 
{
	public String m_configName = "";
	
	Properties m_propertys;
	
	public void Init(String l_configName)
	{
		m_configName = l_configName;
		ConfigService.configs.put(m_configName, this);
		
		LoadConfig();
	}
	
	public void LoadConfig()
	{
		m_propertys = new Properties();  

		try {
			FileInputStream fis = new FileInputStream( ConfigService.configPath + m_configName + ".txt" );
			m_propertys.load(fis);
			
			fis.close();
		} 
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public String GetString(String key) throws Exception 
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
	
	public int GetInt(String key) throws Exception
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
	
	public float GetFloat(String key) throws Exception
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
	
	public boolean GetBool(String key) throws Exception
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
