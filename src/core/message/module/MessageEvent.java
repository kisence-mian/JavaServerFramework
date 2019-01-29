package core.message.module;

import org.apache.mina.core.session.IoSession;

import core.message.define.MessageBase;
import core.player.PlayerBase;

public class MessageEvent 
{
	public IoSession session;
	public String messageType;
	public PlayerBase player;
	public MessageBase message;
}
