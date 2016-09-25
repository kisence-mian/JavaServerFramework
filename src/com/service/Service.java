package com.service;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.concurrent.Executors;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.service.config.ConfigService;


///import com.handler.ServerHandler;

public class Service 
{
	public static NioSocketAcceptor socketAcceptor;
	private Logger log = null;
	ServiceHandler handler;

	public static Service instance;

	static public Service getInstance()
	{
		if (instance == null)
		{
			instance = new Service();
			instance.startService();
		}
		return instance;
	}
	
	public Service() 
	{
		log = LogManager.getLogger(Service.class.getName());

		// 创建一个非阻塞的server端的socket。
		socketAcceptor = new NioSocketAcceptor(Runtime.getRuntime().availableProcessors() + 1);
		socketAcceptor.setReuseAddress(true);
		socketAcceptor.getSessionConfig().setKeepAlive(true);
		

		// 绑定逻辑处理器，主要的逻辑都在handler里面
		handler = new ServiceHandler();
		socketAcceptor.setHandler(handler);
		handler.socketAcceptor = socketAcceptor;
		
//		socketAcceptor.setBacklog(ConfigService.MAX_CONNECTOR);

	//	socketAcceptor.getFilterChain().addLast("logger", new LoggingFilter());
//		socketAcceptor.getFilterChain().addLast("json",
//				new ProtocolCodecFilter(new JSONCodecFactory()));
		TextLineCodecFactory lineCode = new TextLineCodecFactory(Charset.forName("utf8"),"&","&");
		lineCode.setDecoderMaxLineLength(1024*512 * 1);
		lineCode.setEncoderMaxLineLength(1024*512 * 1);
		
		socketAcceptor.getFilterChain().addLast(
				"codec",
				new ProtocolCodecFilter(lineCode));
		
		socketAcceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 60);

		socketAcceptor.getSessionConfig().setReadBufferSize(1024*10); // 发送缓冲区1kb//
																	    // 1M=1024*1024
		socketAcceptor.getSessionConfig().setReceiveBufferSize(1024*10);// 接收缓冲区1kb
		
		log.error("服务器启动~~~\n服务器版本 ： 2016/9/25");
		log.error( "处理核心数： " + Runtime.getRuntime().availableProcessors());
	}
	
	public void startService()
	{
		socketAcceptor.getManagedSessions();
		try {
			// 端口绑定。
//			socketAcceptor.bind(new InetSocketAddress(ConfigService.Port));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
