package com.frameWork.player;

import org.apache.mina.core.session.IoSession;

import com.frameWork.service.message.MessageErrorEnum;
import com.frameWork.service.message.MessageSendService;

import net.sf.json.JSONObject;

public class LoginService
{
	public static String s_LoginMessageType = "login";
	
	public static void Login(IoSession session,JSONObject jsonMessage) 
	{
		Player player = GetPlayer(session,jsonMessage);
		
		if(player != null)
		{
			PlayerManager.PlayerLogin(player);
		}
		else 
		{
			MessageSendService.SendErrorCode(session, s_LoginMessageType, MessageErrorEnum.s_UnkonwError);
		}
	}
	
	static Player GetPlayer(IoSession session,JSONObject jsonMessage)
	{
		String ID = jsonMessage.getString("ID");
		
		Player player = new Player();
		
		player.m_session = session;
		player.m_ID = ID;
		
		return player;
	}
}
