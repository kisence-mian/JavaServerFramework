package com.frameWork.service.message;

import org.apache.mina.core.session.IoSession;

import com.frameWork.service.config.configs.SecretKeyConfig;
import com.frameWork.service.encryption.EncryptionService;
import net.sf.json.JSONObject;


public class MessageSendService 
{
	
	public static void sendSuccessCode(IoSession session,String MT) 
	{
		JSONObject jsonMes = new JSONObject();
		jsonMes.put("MT"            , MT);
		jsonMes.put("Code"          , MessageEnum.s_SuccessCode);
		String meString = jsonMes.toString();
		
		Send(session,meString);
	}
	
	public static void sendErrorCode(IoSession session,String MC,String errorCode)
	{
		JSONObject jsonMes = new JSONObject();
		jsonMes.put("MC"            , MC);
		jsonMes.put("Code"          , errorCode);
		String meString = jsonMes.toString();
		
		Send(session,meString);
	}
	
	public static void sendMessage(IoSession session,JSONObject jsonMes) 
	{
		Send(session,jsonMes.toString());
	}
	
	public static void sendMessageNoSafe(IoSession session,JSONObject jsonMes) 
	{
		Send(session,jsonMes.toString());
	}
	
	
	//发送
	public static void Send(IoSession session,String message)
	{
		if(SecretKeyConfig.s_IsSecret)
		{
			SafeSend(session,message);
		}
		else
		{
			NoSafeSendMessage(session,message);
		}
	}
	
	
	//不加密发送
	static void NoSafeSendMessage(IoSession session,String message) 
	{
		if(session != null)
		{
			session.write(message);
		}
	}
	
	//加密发送
	static void SafeSend(IoSession session,String message)
	{
		try 
		{
			JSONObject encryption = EncryptionService.Encryption(message);
			
			if(session != null)
			{
				session.write(encryption.toString());
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
