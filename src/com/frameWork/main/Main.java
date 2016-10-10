package com.frameWork.main;

import com.frameWork.service.config.ConfigService;
import com.frameWork.service.config.configs.DataBaseConfig;

public class Main 
{
	public static void main(String[] args) 
	{
		System.out.println(DataBaseConfig.s_DataBaseURL);
		
	}
}
