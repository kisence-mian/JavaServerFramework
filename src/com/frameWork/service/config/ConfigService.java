package com.frameWork.service.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.frameWork.service.config.configs.LogConfig;
import com.frameWork.service.config.configs.DataBaseConfig;
import com.frameWork.service.config.configs.SecretKeyConfig;
import com.frameWork.service.config.configs.ServiceConfig;

public class ConfigService 
{
	public static String configPath = "./config/"; 

	public static void Init() 
	{
		ReLoadConfig();
	}
	
	public static void ReLoadConfig()
	{
		LogConfig.Init();
		DataBaseConfig.Init();
		ServiceConfig.Init();
		SecretKeyConfig.Init();
	}
}
