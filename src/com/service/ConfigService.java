package com.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class ConfigService 
{
	static public boolean isDebug = true;          //debug 开关
	static public final int MAX_CONNECTOR = 30000;        //服务器最大连接数
	static public final int  Port =  23333;
	
	//数据库用
	static public final String dataBaseURL = "jdbc:mysql://114.215.131.149"; //CN
	//static public final String dataBaseURL = "jdbc:mysql://47.89.188.57";  //US
//	static public final String dataBaseURL = "jdbc:mysql://192.168.0.12";
	
	static public final String userName = "root";
	static public final String pwd = "88355352"; //CN
	//static public final String pwd = "83dd961d3ce758ce"; //US
	//static public final String pwd = "";
	
	static public final String dataBaseName = "slimeDataBase";
	
	static public final int dataBaseMaxRequest = 5000;
	
	static public int maxStamina = 20;               //最大体力值
	static public int StaminaRegainSpace = 15 * 60;  //体力值恢复间隔 （秒）
	
	static public final String defaultHeadIcon = "";          //默认头像
	
	static public final int updateGlobalRankSpace = 60;          //更新全球排名时间间隔（分钟）
	
	static public final int giftStaminaSpace = 60 * 24;          //好友送体力间隔（分钟）
	
//	static public final String OssPath = "http://slime-record-cn.oss-cn-hangzhou.aliyuncs.com";
	static public final String OssPath = "http://slime-test-record.oss-cn-hangzhou.aliyuncs.com";
	
//	static public final String endpoint ="oss-cn-hangzhou.aliyuncs.com";
//	static public final String endpoint ="oss-us-west-1.aliyuncs.com";
	static public final String endpoint ="oss-cn-hangzhou.aliyuncs.com";
	
//	static public final String bucketName ="slime-record-cn";
//	static public final String bucketName ="slime-record-us";
	static public final String bucketName ="slime-test-record";
	
	static public final String accessKeyId = "wlZXEThWA2es0a3D";                             //accessKeyId 
	static public final String accessKeySecret = "ntRa878Q2ypcJmTEiv5be9qcaDlf7N";          //accessKeySecret
	
	static public int MatchAITime = 10;          //匹配不到人给AI的时间
	
	static public int shareAward = 5;
	static public int signAward  = 5;
	static public int watchADAward  = 5;
	
	static public int PVPTax = 80;
	
	static public int LevelResurgenceCost = 5;
	static public int EndlessResurgenceCost = 5;
//	static public int PVPResurgenceCost = 5;
	
	static public String RSA_private_key = "RSA_private_key"; 
	static public String RSA_public_key = "RSA_public_key"; 
	
	static public final String configpath = "./config/config.ini";
	static public final String keypath = "./config/key.ini";
	static FileInputStream fis = null; // 读  
	static FileInputStream fis2 = null; // 读  
	static OutputStream fos ;  
	static Properties pp;  
	
	static Properties keys;  
	
	public static void Init() 
	{
		if(LoadConfig())
		{
			isDebug = Boolean.parseBoolean( pp.getProperty("IsDebug"));
		
			maxStamina         = Integer.parseInt(pp.getProperty("MaxStamina"));
			StaminaRegainSpace = Integer.parseInt(pp.getProperty("StaminaRegainSpace"));

			shareAward   = Integer.parseInt(pp.getProperty("ShareAward"));
			signAward    = Integer.parseInt(pp.getProperty("SignAward"));
			watchADAward = Integer.parseInt(pp.getProperty("WatchADAward"));
			
			PVPTax       = Integer.parseInt(pp.getProperty("PVPTax"));
			MatchAITime  = Integer.parseInt(pp.getProperty("MatchAITime"));
			
			LevelResurgenceCost    = Integer.parseInt(pp.getProperty("LevelResurgenceCost"));
			EndlessResurgenceCost  = Integer.parseInt(pp.getProperty("EndlessResurgenceCost"));
//			PVPResurgenceCost      = Integer.parseInt(pp.getProperty("PVPResurgenceCost"));
			
//			EndlessResurgenceCost  = Integer.parseInt(pp.getProperty("EndlessResurgenceCost"));
//			EndlessResurgenceCost  = Integer.parseInt(pp.getProperty("EndlessResurgenceCost"));
//			EndlessResurgenceCost  = Integer.parseInt(pp.getProperty("EndlessResurgenceCost"));
//			EndlessResurgenceCost  = Integer.parseInt(pp.getProperty("EndlessResurgenceCost"));
			
			RSA_private_key  = keys.getProperty("RSA_private_key");
			RSA_public_key   = keys.getProperty("RSA_public_key");
			
			
			System.out.println("isDebug :" + isDebug + "  " + pp.getProperty("IsDebug"));
//			System.out.println("RSA_private_key" + RSA_private_key);
//			System.out.println("RSA_public_key"  + RSA_public_key);
		}
	}
	
	public static boolean LoadConfig()
	{
		pp = new Properties();  
		keys = new Properties(); 
		try {
			fis = new FileInputStream(configpath);
			pp.load(fis);
			
			fis2 = new FileInputStream(keypath);
			keys.load(fis2);
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return false;
		}  
		catch (IOException e)
		{
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				return false;
		}  
		
		return true;

	}
}
