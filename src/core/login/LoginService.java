package core.login;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.mina.core.session.IoSession;

import core.dataBase.DataBaseUtil;
import core.dataBase.DatabaseService;
import core.log.LogService;
import core.message.MessageBase;
import core.message.MessageErrorEnum;
import core.message.MessageSendService;
import core.player.PlayerService;
import core.report.ReportService;
import core.time.TimeUtil;
import core.util.JsonUtils;
import game.module.GameData;
import message.GameData2Client;
import message.LoginMessage2Client;
import message.LoginMessage2Server;
import message.UserData2Client;
import user.module.Player;
import user.module.UserData;

public class LoginService {
	public static String s_LoginMessageType = "login";

	public static void Login(IoSession session, MessageBase message) {
		try {
			LoginMessage2Server msg = (LoginMessage2Server) message;
			Player player = null;

			List<Map<String, Object>> data = DatabaseService.GetInstance().SelectData("Player", null,
					"ID = '" + msg.getUserID() + "'");

			if (data.size() > 0) {
				player = GetOldPlayer(session, data.get(0));
				LogService.Log("", "GetOldPlayer :-> " + player.m_ID);
			} else {
				player = GetNewPlayer(session, msg);
				LogService.Log("", "GetNewPlayer :-> " + player.m_ID);
				InsertPlayerData(player);
				
				PlayerService.NewPlayerRegister(player);
			}
			if (player.deviceUniqueIdentifier==null) {
				player.deviceUniqueIdentifier=msg.deviceUniqueIdentifier;
			}
			if (player.platform==null) {
				player.platform=msg.platform;
			}
			player = (Player) PlayerService.PlayerLogin(player);

			LoginMessage2Client res = new LoginMessage2Client();
			res.code=0;
			MessageSendService.SendMessage(session, res);

			UserData2Client udMsg = new UserData2Client();
			udMsg.setUserData(player.userData);
			MessageSendService.SendMessage(session, udMsg);
			
			GameData2Client gdmsg = new GameData2Client();
			gdmsg.gameData = player.gameData;
			MessageSendService.SendMessage(session, gdmsg);
			
			HashMap<String, String> hsData = new HashMap<String, String>();
			hsData.put("ID", msg.getUserID());
			ReportService.GetInstance().Log(player, "Player Login", hsData);
			
		} catch (Exception e) {
			LogService.Exception("", "Exception", e);
			MessageSendService.SendErrorCode(session, s_LoginMessageType, MessageErrorEnum.s_UnkonwError);
		}
	}

	public static void LoginOut(IoSession session) {
		Player player = (Player) PlayerService.GetPlayer(session);

		if (player != null) {
			// Ð´Êý¾Ý¿â
			UpdatePlayerData(player);
			PlayerService.PlayerExit(player);
			HashMap<String, String> hsData = new HashMap<String, String>();
			hsData.put("ID", player.m_ID);
			ReportService.GetInstance().Log(player, "Player Logout", hsData);
		}
	}

	static Player GetNewPlayer(IoSession session, LoginMessage2Server msg)
	{
		String ID = msg.getUserID();

		Player player = new Player();

		player.m_session = session;
		player.m_ID = ID;
		
		player.LastLoginDate = TimeUtil.Now();
		player.RegistrationDate = TimeUtil.Now();
		
		player.userData = new UserData();
		player.userData.NewUser();
		
		player.userData.coin = 0;
		player.userData.debris = 0;

		return player;
	}

	static Player GetOldPlayer(IoSession session, Map<String, Object> data) 
	{
		String ID = (String) data.get("ID");
		String nickName = (String)data.get("NickName");
		String gameData = (String) data.get("GameData");
		String userData = (String) data.get("UserData");
		Date lastLoginTime = (Date)data.get("LastLoginDate");
		int score = (int)data.get("Score");

		Player player = new Player();

		player.m_session = session;
		player.m_ID = ID;
		player.nickName = nickName;
		player.LastLoginDate = lastLoginTime;
		player.score = score;
		
		if (gameData != null && gameData != "") {
			// LogService.Log("", "gameData :-> " + gameData);
			try {
				player.gameData = JsonUtils.json2BeanF(gameData, GameData.class);
			} catch (Exception e) {
				e.printStackTrace();
				player.gameData = null;
			}
			if (player.gameData != null) {
				player.gameData.ReInit();
			}
		}

		if (userData != null && userData != "") {
			LogService.Log("", "userData :-> " + userData);
			try {
				player.userData = JsonUtils.json2BeanF(userData, UserData.class);
				
				LogService.Log("", "player.userData ->" + player.userData + "<-");
				
			} catch (Exception e) {
				e.printStackTrace();
				player.userData = new UserData();
			}
			
			if (player.userData != null) {
				player.userData.Init();
			}
		}

		return player;
	}

	static void InsertPlayerData(Player player) {
		
		String[] keys = new String[5];
		keys[0] = "ID";
		keys[1] = "GameData";
		keys[2] = "UserData";
		keys[3] = "RegistrationDate";
		keys[4] = "LastLoginDate";

		String[] values = new String[5];
		values[0] = "'" + player.m_ID + "'";
		values[1] = "''";
		values[2] = "'" + JsonUtils.bean2JsonF(player.userData) + "'";
		values[3] = "'" + DataBaseUtil.DateToString(new Date())+ "'";
		values[4] = values[3];

		DatabaseService.GetInstance().InsertData("Player", keys, values);
	}

	static void UpdatePlayerData(Player player) {

		String[] key = new String[5];
		key[0] = "GameData";
		key[1] = "UserData";
		key[2] = "LastLoginDate";
		key[3] = "Score";
		key[4] = "NickName";

		String[] Value = new String[5];

		if (player.gameData != null) {
			Value[0] = "'" + JsonUtils.bean2JsonF(player.gameData) + "'";
		} else {
			Value[0] = "''";
		}

		if (player.userData != null) {
			Value[1] = "'" + JsonUtils.bean2JsonF(player.userData) + "'";
		} else {
			Value[1] = "''";
		}
		
		Value[2] = "'" + DataBaseUtil.DateToString(new Date())+ "'";
		Value[3] = "" + player.score;
		Value[4] = "'" + player.nickName+ "'";

		DatabaseService.GetInstance().UpdateData("Player", key, Value, "ID = '" + player.m_ID + "'");
	}
}
