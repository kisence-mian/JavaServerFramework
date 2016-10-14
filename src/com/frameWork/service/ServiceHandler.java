package com.frameWork.service;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.frameWork.model.Player;
import com.frameWork.service.config.configs.SecretKeyConfig;
import com.frameWork.service.encryption.EncryptionService;
import com.frameWork.service.message.MessageEnum;
import com.frameWork.service.message.MessageErrorEnum;
import com.frameWork.service.message.MessageReceviceService;
import com.frameWork.service.message.MessageSendService;

import net.sf.json.JSONObject;

public class ServiceHandler extends IoHandlerAdapter 
{
	public NioSocketAcceptor socketAcceptor;
	String m_modelName = "ServiceHandler";

	public ServiceHandler()
	{
	}
	
	@Override
	public void sessionCreated(IoSession session) throws Exception
	{
		super.sessionCreated(session);
		
		LogService.Log(m_modelName, "sessionCreated: " + session.toString());
	}
	
	@Override
	public void sessionOpened(IoSession session) throws Exception 
	{
		LogService.Log(m_modelName, "sessionOpened: " + session.toString());
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception 
	{
		super.sessionIdle(session, status);
		
		LogService.Log(m_modelName, "sessionIdle: " + session.toString());
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception
	{
		LogService.Log(m_modelName, "sessionClosed: " + session.toString());
	}
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception 
	{
		LogService.Error(m_modelName, "exceptionCaught: " + session.toString());
	}
	
	public void messageReceived(IoSession session,Object message) throws Exception
	{
		String str = message.toString();
		
		if(message == null || message == "")
		{
			return;
		}

		DealMessage(session,str);
	}
	
	void DealMessage(IoSession session,String message)
	{
		try 
		{
			//消息解密
			JSONObject jsonMessage = JSONObject.fromObject(message);
			String messageType = "";

			if(SecretKeyConfig.s_IsSecret)
			{
				jsonMessage = EncryptionService.decryption(jsonMessage);
			}
			
			messageType = jsonMessage.getString("MT");
			
			if((boolean)session.getAttribute("isLogin") == false) //如果未登录只能进行登陆操作
			{
				LoginService.Login(session, message);
				//此处可以添加管理员登录分支
			}
			else
			{
				Player tmpPlayer = (Player)session.getAttribute("player");
				
				if(tmpPlayer == null)
				{
					MessageSendService.sendErrorCode(session, messageType, MessageErrorEnum.s_unkonwError);
					LogService.Error(m_modelName, "DealMessage Player is null:"+ "\n"
						                        	+"session: " + session.toString());
					return;
				}
				
				MessageReceviceService.ReceviceMessgae(messageType,tmpPlayer, jsonMessage);
			}
		}
		catch(Exception e)
		{
			LogService.Exception(m_modelName, "DealMessage :" + message + "\n"
											+"session: " + session.toString(), e);
			
			MessageSendService.sendErrorCode(session, MessageEnum.s_MeaasgeType_Unkonw, MessageEnum.s_FailCode);
		}
	}
}
