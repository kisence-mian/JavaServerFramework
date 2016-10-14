package com.frameWork.service.config;

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
