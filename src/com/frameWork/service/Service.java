package com.frameWork.service;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.frameWork.service.config.configs.ServiceConfig;

///import com.handler.ServerHandler;

public class Service 
{
	public static NioSocketAcceptor socketAcceptor;
	ServiceHandler handler;
	
	String m_modelName = "NioService";

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
		
		ShowInfo();
	}
	
	public void startService()
	{
		socketAcceptor.getManagedSessions();
		try {
			// 端口绑定。
			socketAcceptor.bind(new InetSocketAddress(ServiceConfig.s_Port));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void ShowInfo()
	{
		String Info = "启动Socket监听  端口号：" + ServiceConfig.s_Port + "\n"
					+ "服务器版本 ： 2016/10/13 \n" 
					+ "处理核心数： " + Runtime.getRuntime().availableProcessors() + "\n";
		try 
		{
			String hostName = InetAddress.getLocalHost().getHostName();
			for (InetAddress it : InetAddress.getAllByName( hostName)) 
			{
				Info += ipv4OrIpv6(it) + "\n";
			}
		}
		catch (UnknownHostException e) 
		{
			e.printStackTrace();
		}
		
		LogService.Log(m_modelName, Info);
	}
	
	String ipv4OrIpv6(InetAddress ita) 
	{
			String[] itn = ita.toString().split("/");
			String str = itn[1];
			if (str.length() > 16) 
			{
					return "IPv6\t" + ita.toString();
			}
			return "IPv4\t" + ita.toString();
	}
}
