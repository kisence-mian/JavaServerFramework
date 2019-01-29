package core.message;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.mina.core.session.IoSession;

import core.log.LogService;
import core.message.define.MessageBase;
import core.message.define.MessageEventListener;
import core.message.module.MessageEvent;
import core.player.PlayerBase;
import core.util.JsonUtils;
import net.sf.json.JSONObject;

public class MessageReceviceService 
{
	public final static String s_modelName = "MessageReceviceService";
	static HashMap<String, ArrayList<MessageEventListener>>  LoginMsglisteners = new HashMap<String, ArrayList<MessageEventListener>>();
	static HashMap<String, ArrayList<MessageEventListener>>  NotLoginListeners = new HashMap<String, ArrayList<MessageEventListener>>();
	
	static ArrayList<MessageEventListener> AllLoginMsgListeners = new ArrayList<MessageEventListener>();
	static ArrayList<MessageEventListener> AllNotLoginMsgListeners = new ArrayList<MessageEventListener>();
	
	public static MessageBase AnalysisJson(JSONObject json) throws ClassNotFoundException
	{
		String content = json.getString("Content");
		String messageType =json.getString("MT");

		MessageBase msg = (MessageBase)JsonUtils.json2Bean(content, Class.forName("message." + messageType));
		msg.messageType = messageType; 
		
		return msg;
	}
	
	//添加登陆后消息监听
	public static void AddListener(String messageType ,MessageEventListener listener)
	{
		ArrayList<MessageEventListener> list = null;
		
		if(!LoginMsglisteners.containsKey(messageType))
		{
			list = new ArrayList<MessageEventListener>();
			LoginMsglisteners.put(messageType, list);
		}
		else
		{
			list = LoginMsglisteners.get(messageType);
		}
		
		list.add(listener);
	}
	
	//添加*未登录*消息监听
	public static void AddNoLoginMsgListener(String messageType ,MessageEventListener listener)
	{
		ArrayList<MessageEventListener> list = null;
		
		if(!NotLoginListeners.containsKey(messageType))
		{
			list = new ArrayList<MessageEventListener>();
			NotLoginListeners.put(messageType, list);
		}
		else
		{
			list = NotLoginListeners.get(messageType);
		}
		
		list.add(listener);
	}
	
	//添加全部登陆消息监听
	public static void AddAllListener(MessageEventListener listener)
	{
		AllLoginMsgListeners.add(listener);
	}
	
	//添加全部-未登陆-消息监听
	public static void AddAllNoLoginListener(MessageEventListener listener)
	{
		AllNotLoginMsgListeners.add(listener);
	}
	
	public static void ReceviceLoginMessage(PlayerBase player,MessageBase message)
	{
		MessageEvent event = new MessageEvent();
		event.session = player.m_session;
		event.message  = message;
		event.player      = player;
		event.messageType = message.messageType;
		
		DispatchLoginMsgEvent(event);
	}
	
	public static void ReceviceNotLoginMessage(IoSession session,MessageBase message)
	{
		MessageEvent event = new MessageEvent();
		event.session = session;
		event.message  = message;
		event.messageType = message.messageType;
		
		DispatchNotLoginMsgEvent(event);
	}
	
	static void DispatchLoginMsgEvent(MessageEvent event ) 
	{
		if(LoginMsglisteners.containsKey(event.messageType)){
			
			ArrayList<MessageEventListener> list = LoginMsglisteners.get(event.messageType);
			
			for (int i = 0; i < list.size(); i++) {
				try 
				{
					list.get(i).ReceviceMessage(event);
				} 
				catch (Exception e) 
				{
					LogService.Exception(s_modelName, "DispatchLoginMsgEvent"+event.messageType+" Error " + e.toString(),e);
				}
			}
		}
		
		for (int i = 0; i < AllLoginMsgListeners.size(); i++) {
			try 
			{
				if(AllLoginMsgListeners.get(i) != null)
				{
					AllLoginMsgListeners.get(i).ReceviceMessage(event);
				}
				else
				{
					LogService.Error("", "AllLoginMsgListeners get i is null " + i);
				}

			} 
			catch (Exception e) 
			{
				LogService.Exception(s_modelName, "DispatchLoginMsgEvent  AllLoginMsgListeners "+event.messageType+" Error " + e.toString(),e);
			}
		}
	}
	
	static void DispatchNotLoginMsgEvent(MessageEvent msg) 
	{
		if(NotLoginListeners.containsKey(msg.messageType)){
			
			ArrayList<MessageEventListener> list = NotLoginListeners.get(msg.messageType);
			
			for (int i = 0; i < list.size(); i++) {
				try 
				{
					list.get(i).ReceviceMessage(msg);
				} 
				catch (Exception e) 
				{
					LogService.Exception(s_modelName, "DispatchNotLoginMsgEvent" + msg.messageType + " Error", e);
				}
			}
		}
		
		for (int i = 0; i < AllNotLoginMsgListeners.size(); i++) {
			try 
			{
				AllNotLoginMsgListeners.get(i).ReceviceMessage(msg);
			} 
			catch (Exception e) 
			{
				LogService.Exception(s_modelName, "DispatchNotLoginMsgEvent Error", e);
			}
		}
	}
}