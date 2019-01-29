package core.player;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.mina.core.session.IoSession;
import core.log.LogService;
import core.player.PlayerEvent.PlayerEventEnum;
import user.service.login.LoginService;

public class PlayerService 
{
	static final String s_modelName = "PlayerManager";
	
	static final String s_playerAttributeName = "player";
	
	static HashMap<String, PlayerBase> s_Players = new HashMap<>();
	
	public static HashMap<PlayerEventEnum, ArrayList<PlayerEventListener>>  listeners = new HashMap<PlayerEventEnum, ArrayList<PlayerEventListener>>();
	
	public static PlayerBase GetPlayer(String ID)
	{
		if(s_Players.containsKey(ID))
		{
			return s_Players.get(ID);
		}
		else
		{
			return null;
		}
	}
	
	public static PlayerBase GetPlayer(IoSession session)
	{
		Object player = session.getAttribute(s_playerAttributeName);
		
		if(player != null)
		{
			return (PlayerBase)player;
		}
		else
		{
			return null;
		}
	}
	
	public static HashMap<String, PlayerBase> GetAllPlayer() 
	{
		return s_Players;
	}
	
	public static void DispatchNewPlayer(PlayerBase player)
	{
		PlayerEvent pEvent = new PlayerEvent();
		pEvent.m_Player = player;
		pEvent.m_eventType = PlayerEventEnum.NewPlayer;
		
		DispatchEvent(pEvent);
	}
	
	//玩家登陆
	public static PlayerBase PlayerLogin(PlayerBase player)
	{
		if(!s_Players.containsKey(player.m_ID))
		{
			LogService.Log(s_modelName, "玩家登陆 " + player.m_ID);
			
			s_Players.put(player.m_ID, player);
			player.m_session.setAttribute(s_playerAttributeName,player);
		}
		else //重复登录
		{
			PlayerBase oldPlayer = s_Players.get(player.m_ID);
			
			LogService.Log(s_modelName, "玩家重复登陆 ！" + player.m_ID +"\n"
		                              + "NEW session: " + player.m_session    + "\n"
		                              + "OLD session: " + oldPlayer.m_session + "\n");
			OtherPlaceLogin(oldPlayer);
			
			player.m_session.setAttribute(s_playerAttributeName,oldPlayer);
			oldPlayer.m_session = player.m_session;
			player = oldPlayer;
		}
		
		return player;
	}
	
	public static void DispatchPlayerLogin(PlayerBase player)
	{
		PlayerEvent pEvent = new PlayerEvent();
		pEvent.m_Player = player;
		pEvent.m_eventType = PlayerEventEnum.Login;
		
		DispatchEvent(pEvent);
	}
	
	//玩家退出
	public static void PlayerExit(PlayerBase player)
	{
		LogService.Log(s_modelName, "玩家退出 " + player.m_ID);
		
		PlayerEvent pEvent = new PlayerEvent();
		pEvent.m_Player = player;
		pEvent.m_eventType = PlayerEventEnum.Exit;
		
		DispatchEvent(pEvent);
		
		if(s_Players.containsKey(player.m_ID))
		{
			s_Players.remove(player.m_ID);
		}
		else
		{
			LogService.Error(s_modelName, "没有找到该玩家！ " + player.m_ID);
		}
		
		player.m_session.setAttribute(s_playerAttributeName, null);
	}
	
	static void OtherPlaceLogin(PlayerBase player)
	{
		LogService.Error("", "OtherPlaceLogin ->" + player.m_ID);
		
		PlayerEvent pEvent = new PlayerEvent();
		pEvent.m_Player = player;
		pEvent.m_eventType = PlayerEventEnum.Exit;
		
		DispatchEvent(pEvent);
		
		player.m_session.close(true);
		
//		LoginService.Instance.LoginOut(player.m_session);
		
//		MessageSendService.SendErrorCode(player.m_session, LoginService.s_LoginMessageType, MessageErrorEnum.s_AccountOtherPlaceLogin);
		
		player.m_session.setAttribute(s_playerAttributeName, null);
	}
	
	
	public static void AddListener(PlayerEventEnum eventType ,PlayerEventListener listener)
	{
		ArrayList<PlayerEventListener> list = null;
		
		if(!listeners.containsKey(eventType))
		{
			list = new ArrayList<PlayerEventListener>();
			
			listeners.put(eventType, list);
		}
		else
		{
			list = listeners.get(eventType);
		}
		
		list.add(listener);
	}
	
	static void DispatchEvent(PlayerEvent event ) 
	{
		if(listeners.containsKey(event.m_eventType))
		{
			ArrayList<PlayerEventListener> list = listeners.get(event.m_eventType);
			
			for (int i = 0; i < list.size(); i++) 
			{
				try 
				{
					list.get(i).PlayerEvent(event);
				} 
				catch (Exception e) 
				{
					LogService.Error(s_modelName , "PlayerService DispatchEvent Error");
				}
			}
		}
		else 
		{
			return;
		}
	}
}
