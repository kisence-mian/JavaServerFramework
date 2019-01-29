package core.message;

import org.apache.mina.core.session.IoSession;

import core.config.ConfigService;
import core.log.LogService;
import core.message.define.MessageBase;
import core.message.define.MessageEnum;
import core.message.encryption.EncryptionService;
import net.sf.json.JSONObject;

public class MessageSendService 
{
	public static void SendHeartBeat(IoSession session) 
	{
		JSONObject jsonMes = new JSONObject();
		jsonMes.put("MT"            , MessageEnum.HeartBeatMessageType);
		String meString = jsonMes.toString();
		
		Send(session,meString);
	}
	
	public static void SendSuccessCode(IoSession session,String MT) 
	{
		JSONObject jsonMes = new JSONObject();
		jsonMes.put("MT"            , MT);
		jsonMes.put("Code"          , MessageEnum.s_SuccessCode);
		String meString = jsonMes.toString();
		
		Send(session,meString);
	}
	
	public static void SendErrorCode(IoSession session,String MT,String errorCode)
	{
		JSONObject jsonMes = new JSONObject();
		jsonMes.put("MT"            , MT);
		jsonMes.put("Code"          , errorCode);
		String meString = jsonMes.toString();
		
		Send(session,meString);
	}
	
	public static void SendMessage(IoSession session,MessageBase msg) 
	{
		int code = 0;
		
		if(session.getAttribute("MsgCode") != null)
		{
			code = (int)session.getAttribute("MsgCode");
		}
		
		JSONObject jo = new JSONObject();
		jo.put("MT", msg.getClass().getSimpleName());
		jo.put("MsgCode",  code );
		jo.put("Content", "\"" + JSONObject.fromObject(msg).toString() + "\"");
		
		code++;
		session.setAttribute("MsgCode", code);
		
		Send(session,jo.toString());
	}
	
	public static void SendMessage(IoSession session,JSONObject jsonMes) 
	{
		Send(session,jsonMes.toString());
	}
	
	public static void SendMessageNoSafe(IoSession session,JSONObject jsonMes) 
	{
		Send(session,jsonMes.toString());
	}
	
	//发送
	public static void Send(IoSession session,String message)
	{
		if(ConfigService.GetBool("SecretKeyConfig", "IsSecret", false))
		{
			SafeSend(session,message);
		}
		else
		{
			NoSafeSendMessage(session,message);
		}
	}
	
	//不加密发送
	static void NoSafeSendMessage(IoSession session,String message) 
	{
		if(session != null)
		{
			LogService.Log("MessageSendService", "NoSafeSendMessage ->" + message);
			
			session.write(message);
		}
	}
	
	//加密发送
	static void SafeSend(IoSession session,String message)
	{
		try 
		{
//			LogService.Log("MessageSendService", "SafeSend SendMessage ->" + message);
			JSONObject encryption = EncryptionService.Encryption(message);
			
			if(session != null)
			{
				session.write(encryption.toString());
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
