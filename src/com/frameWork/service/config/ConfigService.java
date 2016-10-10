package com.frameWork.service.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.frameWork.service.config.configs.DataBaseConfig;

public class ConfigService 
{
	public static String configPath = "./config/"; 
	public static HashMap<String, ConfigServiceBase> configs = new HashMap<String,ConfigServiceBase>();
	
	public static ConfigServiceBase GetConfig(String configName) throws Exception 
	{
		if(configs.containsKey(configName))
		{
			return configs.get(configName);
		}
		else
		{
			throw new Exception("not find " + configName);
		}
	}
	
	public static void Init() 
	{
		new DataBaseConfig(); // ˝æ›ø‚…Ë÷√
	}
	
	public static void ReLoadConfig()
	{
		Iterator<Entry<String, ConfigServiceBase>> iter = configs.entrySet().iterator();
		
		while (iter.hasNext()) 
		{
			Entry<String, ConfigServiceBase> entry = iter.next();
			ConfigServiceBase csb =  entry.getValue();
			csb.LoadConfig();
		}
	}
}
