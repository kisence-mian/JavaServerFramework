package com.frameWork.main;
import java.util.StringTokenizer;

import com.frameWork.service.LogService;
import com.frameWork.service.Service;
import com.frameWork.service.config.ConfigService;
import com.frameWork.service.timer.TimerService;

public class Main 
{
	public static void main(String[] args) 
	{
		ConfigService.Init();
		TimerService.Init();
		LogService.Init();
		Service.getInstance();
		
		StringTokenizer pas = new StringTokenizer(" public static void,main", " ,");
		System.out.println(pas.countTokens());
		
//		System.out.println("3501");
//		for (int i = 0; i <3501; i++) 
//		{
//			LogService.Log("Main", "log " + i);
////			LogService.Error("Main", "Error " + i);
////			LogService.Exception("Main", "Error " + i, new Exception("Exception!!!!!"));
//		}
		  
	}
}
