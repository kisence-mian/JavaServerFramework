package core.login;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.mina.core.session.IoSession;

import core.dataBase.DataBaseUtil;
import core.dataBase.DatabaseService;
import core.log.LogService;
import core.login.define.PlayerCreateBase;
import core.message.MessageSendService;
import core.message.define.MessageErrorEnum;
import core.player.PlayerBase;
import core.player.PlayerService;
import core.time.TimeUtil;

public class LoginServiceBase 
{
	public String LoginMessageType = "login";
	public String PlayerTableName = "Player";
	
	public void Init()
	{
	}
	
	public PlayerBase Login(IoSession session, String ID,PlayerCreateBase creater) 
	{
		try {

			PlayerBase player = null;
			List<Map<String, Object>> data = LoadDataBase(ID);
			
			if(data == null)
			{
				LogService.Error("LoginService", "无返回数据，请检查数据库连接！");
				MessageSendService.SendErrorCode(session, LoginMessageType, MessageErrorEnum.s_UnkonwError);
				return null;
			}
			
			boolean isNewPlayer = data.size() <= 0;
			
			if (!isNewPlayer) 
			{
				player = GetOldPlayer(session, data.get(0));
				LogService.Log("", "GetOldPlayer :-> " + player.m_ID);
				
				if(creater != null)
				{
					creater.OldPlayer(player);
				}
			} 
			else 
			{
				player = GetNewPlayer(session, ID);
				LogService.Log("", "GetNewPlayer :-> " + player.m_ID);
				
				if(creater != null)
				{
					creater.NewPlayer(player);
				}
				
				InsertPlayerData(player);
			}
			
			//构造接口
			if(creater != null)
			{
				creater.GeneratePlayer(player);
			}
			
			player = PlayerService.PlayerLogin(player);
			
			OnBeforeDispatchLogin(session,player);

			if(isNewPlayer)
			{
				PlayerService.DispatchNewPlayer(player);
			}
			
			PlayerService.DispatchPlayerLogin(player);
			
			return player;
			
		} catch (Exception e) {
			LogService.Exception("", "Exception", e);
			MessageSendService.SendErrorCode(session, LoginMessageType, MessageErrorEnum.s_UnkonwError);
			return null;
		}
	}

	public void LoginOut(IoSession session) {
		PlayerBase player = PlayerService.GetPlayer(session);

		if (player != null) {
			
			PlayerService.PlayerExit(player);
			
			// 写数据库
			UpdatePlayerData(player);
		}
	}
	
	
	protected List<Map<String, Object>> LoadDataBase(String ID)
	{
		return DatabaseService.GetInstance().SelectData(PlayerTableName, null,"ID = '" + ID + "'");
	}
	
	protected void OnBeforeDispatchLogin(IoSession session,PlayerBase player)
	{
		
	}
	
	protected PlayerBase GetNewPlayer(IoSession session, String ID)
	{
		PlayerBase player = new PlayerBase();

		player.m_session = session;
		player.m_ID = ID;
		
		player.LastLoginDate = TimeUtil.Now();
		player.RegistrationDate = TimeUtil.Now();
		
		return player;
	}
	
	protected PlayerBase GetOldPlayer(IoSession session, Map<String, Object> data) 
	{
		String ID = (String) data.get("ID");
		String nickName = (String)data.get("NickName");
		Date lastLoginTime = (Date)data.get("LastLoginDate");

		PlayerBase player = new PlayerBase();

		player.m_session = session;
		player.m_ID = ID;
		player.nickName = nickName;
		player.LastLoginDate = lastLoginTime;

		return player;
	}
	
	protected void InsertPlayerData(PlayerBase player) {
		
		String[] keys = new String[3];
		keys[0] = "ID";
		keys[1] = "RegistrationDate";
		keys[2] = "LastLoginDate";

		Object[] values = new Object[3];
		values[0] =  player.m_ID;
		values[1] = DataBaseUtil.DateToString(new Date());
		values[2] = values[1];

		DatabaseService.GetInstance().InsertData(PlayerTableName, keys, values);
	}

	protected void UpdatePlayerData(PlayerBase player) {

		String[] key = new String[2];
		key[0] = "LastLoginDate";
		key[1] = "NickName";

		Object[] Value = new Object[2];
		Value[0] =  DataBaseUtil.DateToString(new Date());
		Value[1] =  player.nickName;


		DatabaseService.GetInstance().UpdateData(PlayerTableName, key, Value, "ID = '" + player.m_ID + "'");
	}
	
}
