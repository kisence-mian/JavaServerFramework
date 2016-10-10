package com.frameWork.service;

import java.util.Base64;
import java.util.List;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.example.tapedeck.PlayCommand;

import com.frameWork.service.config.ConfigService;
import com.frameWork.service.encryption.EncryptionService;

import net.sf.json.JSONObject;

public class MessageService 
{
	public static MessageService instance;

	public static MessageService getInstance() 
	{
		if(instance == null)
		{
			instance = new MessageService();
		}
		return instance;
	}
	
	//不加密发送
	public void sendMessageNoSafe(IoSession session,JSONObject jsonMes) 
	{
		send(session,jsonMes.toString());
	}

	//加密发送
	void send(IoSession session,String message)
	{

		
		if(session != null)
		{
			session.write(message);
		}
	}
	
	//加密报文
	void encryptionSend(IoSession session,String message)
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