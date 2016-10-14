package com.frameWork.service.message;

import java.util.ArrayList;
import java.util.HashMap;
import com.frameWork.model.Player;
import com.frameWork.service.LogService;
import com.frameWork.service.encryption.EncryptionService;
import net.sf.json.JSONObject;

public class MessageReceviceService 
{
	public final static String s_modelName = "MessageReceviceService";
	public static HashMap<String, ArrayList<MessageEventListener>>  listeners = new HashMap<String, ArrayList<MessageEventListener>>();
	
	//ÃÌº”º‡Ã˝
	public static void AddListener(String messageType ,MessageEventListener listener)
	{
		ArrayList<MessageEventListener> list = null;
		
		if(!listeners.containsKey(messageType))
		{
			list = new ArrayList<MessageEventListener>();
			listeners.put(messageType, list);
		}
		else
		{
			list = listeners.get(messageType);
		}
		
		list.add(listener);
	}
	
	public static void ReceviceMessgae(String messageType,Player player,JSONObject message)
	{
		MessageEvent event = new MessageEvent();
		event.m_message     = message;
		event.m_Player      = player;
		event.m_messageType = messageType;
		
//		//Ω‚√‹
//		if(event.m_message.containsKey("securityKey"))
//		{
//			event.m_message = EncryptionService.decryption(event.m_message);
//		}
//		
//		event.m_messageType = event.m_message.getString("");
		
		DispatchEvent(event);
	}
	
	static void DispatchEvent(MessageEvent event ) 
	{
		if(listeners.containsKey(event.m_messageType))
		{
			ArrayList<MessageEventListener> list = listeners.get(event.m_messageType);
			
			for (int i = 0; i < list.size(); i++) 
			{
				try 
				{
					list.get(i).ReceviceMessage(event);
				} 
				catch (Exception e) 
				{
					LogService.Exception(s_modelName, "DispatchEvent Error", e);
				}
			}
		}
		else 
		{
			return;
		}
	}
}