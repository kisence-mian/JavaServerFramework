package com.service;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Base64;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.springframework.core.ErrorCoded;

import com.service.config.ConfigService;



public class ServiceHandler extends IoHandlerAdapter 
{
	public NioSocketAcceptor socketAcceptor;

	public ServiceHandler()
	{
		//log = Logger.getLogger(ServiceHandler.class.getName());
	}
	
	@Override
	public void sessionCreated(IoSession session) throws Exception
	{
		// TODO Auto-generated method stub
		super.sessionCreated(session);
	}
	
	@Override
	public void sessionOpened(IoSession session) throws Exception 
	{

	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception 
	{

		super.sessionIdle(session, status);
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception
	{

	}
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		
	}
	
	public void messageReceived(IoSession session,Object message) throws Exception{

	}
	
}
