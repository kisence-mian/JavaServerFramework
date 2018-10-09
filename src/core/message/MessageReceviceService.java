package core.message;

import java.util.ArrayList;
import java.util.HashMap;

import core.log.LogService;
import core.player.PlayerBase;
import core.util.JsonUtils;
import net.sf.json.JSONObject;

public class MessageReceviceService 
{
	public final static String s_modelName = "MessageReceviceService";
	static HashMap<String, ArrayList<MessageEventListener>>  listeners = new HashMap<String, ArrayList<MessageEventListener>>();
	
	static ArrayList<MessageEventListener> allListeners = new ArrayList<MessageEventListener>();
	
	public static MessageBase AnalysisJson(JSONObject json) throws ClassNotFoundException
	{
		String content = json.getString("Content");
		String messageType =json.getString("MT");

		MessageBase msg = (MessageBase)JsonUtils.json2Bean(content, Class.forName("message." + messageType));
		msg.messageType = messageType; 
		
		return msg;
	}
	
	//Ìí¼Ó¼àÌý
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
	
	public static void AddAllListener(MessageEventListener listener)
	{
		allListeners.add(listener);
	}
	
	public static void ReceviceMessage(PlayerBase player,MessageBase message)
	{
		MessageEvent event = new MessageEvent();
		event.message  = message;
		event.player      = player;
		event.messageType = message.messageType;
		
		DispatchEvent(event);
	}
	
	static void DispatchEvent(MessageEvent event ) 
	{
		if(listeners.containsKey(event.messageType)){
			
			ArrayList<MessageEventListener> list = listeners.get(event.messageType);
			
			for (int i = 0; i < list.size(); i++) {
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
		
		for (int i = 0; i < allListeners.size(); i++) {
			try 
			{
				allListeners.get(i).ReceviceMessage(event);
			} 
			catch (Exception e) 
			{
				LogService.Exception(s_modelName, "DispatchEvent Error", e);
			}
		}
	}
}