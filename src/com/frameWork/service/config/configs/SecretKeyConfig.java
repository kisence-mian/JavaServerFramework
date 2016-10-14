package com.frameWork.service.config.configs;

import com.frameWork.service.config.ConfigServiceBase;

public class SecretKeyConfig extends ConfigServiceBase 
{
	public static String RSA_private_key;
	public static String RSA_public_key;
	
	public static boolean s_IsSecret = true; // «∑Òº”√‹
	
	public static void Init() 
	{
		m_configName = "SecretKeyConfig";
		
		try 
		{
			SetConfigName(m_configName);
			
			RSA_private_key  = GetString("PrivateKey"); 
			RSA_public_key = GetString("PublicKey");
			
			s_IsSecret = GetBool("IsSecret");
		} 
		catch (Exception e)
		{
			System.out.println("Config " + m_configName + " not find !");
		}
	}
}
