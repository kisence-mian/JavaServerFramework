package core.player;

import java.net.InetSocketAddress;
import java.util.Date;

import org.apache.mina.core.session.IoSession;

import core.login.RuntimePlatform;

public class PlayerBase 
{
	public IoSession m_session = null;
	public String m_ID = "";
	public String nickName = "";

	public Date RegistrationDate; // 注册日期
	public Date LastLoginDate; // 最后登陆日期

	public RuntimePlatform platform;
	public String deviceUniqueIdentifier;
	
	public String vsrsion; //版本号
	public String channel; //渠道
	
	public void setPlatform(RuntimePlatform platform) {
		this.platform = platform;
	}

	public RuntimePlatform getPlatform() {
		return platform;
	}

	public void setDeviceUniqueIdentifier(String deviceUniqueIdentifier) {
		this.deviceUniqueIdentifier = deviceUniqueIdentifier;
	}

	public String getDeviceUniqueIdentifier() {
		return deviceUniqueIdentifier;
	}

	public String GetIP() {
		if (m_session != null) {
			return ((InetSocketAddress) m_session.getRemoteAddress()).getAddress().getHostAddress();
		} else {
			return "null";
		}
	}
}
