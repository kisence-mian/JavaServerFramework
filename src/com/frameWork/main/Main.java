package com.frameWork.main;
import com.frameWork.service.LogService;

public class Main 
{
	public static void main(String[] args) 
	{
		
		System.out.println("3501");
		for (int i = 0; i <3501; i++) 
		{
			LogService.Log("Main", "log " + i);
//			LogService.Error("Main", "Error " + i);
//			LogService.Exception("Main", "Error " + i, new Exception("Exception!!!!!"));
		}
		  
	}
}
