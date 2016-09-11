package com.service;

import java.io.UnsupportedEncodingException;
import java.nio.channels.ScatteringByteChannel;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.mail.Session;

import service.config.ConfigService;
import service.config.ErrorCode;
import service.config.socketEnum;

import org.apache.mina.core.session.IoSession;

import service.Model.Player;
import database.DatabaseService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class loginJudge
{
	MessageService messageService = new MessageService();
	int maxID = -1;
	public Player login(IoSession session,JSONObject josnMes)
	{
		Date date = new Date();//获得系统时间
		String nowTime ="";
		nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);//将时间格式转换成符合Timestamp要求的格式.
		String ID = josnMes.getString("ID");
		Player pl = null;
		
		if(ID != null && ID != "")
		{
			try
			{
				if(ConfigService.isDebug)
				{
					System.out.println("请求数据库");
				}
				
				List<Map<String, Object>> resultList = DatabaseService.getInstance().executeQuery(
						"select * from "
						+ConfigService.dataBaseName +".player WHERE "
						+ConfigService.dataBaseName +".player.ID = " +"'"+ID+"'");

				if(resultList.size()>0)
				{
					pl = oldPlayer(ID,resultList.get(0));
				}
				else
				{
					pl = newPlayer(session,josnMes,nowTime);
				}
				
				LoginEvent(pl,session,nowTime);
				
				return pl;

			} 
			catch (Exception e)
			{
				e.printStackTrace();

				messageService.sendErrorCode(session, socketEnum.loginReturn, ErrorCode.UnknownError);
			}
		}
		else 
		{
			System.out.println("没有传入ID :" + josnMes.toString());
			messageService.sendErrorCode(session, socketEnum.loginReturn, ErrorCode.UnknownError);
		}
		
		return null;
	}
	
	public Player oldPlayer(String ID,Map<String, Object> mapTmp) 
	{	
		Player pl = GetOldPlayerData(mapTmp);
		pl.ID = ID;
		
		return pl;
	}
	
	public Player newPlayer(IoSession session,JSONObject json,String nowTime)
	{
		Player pl = GetNewPlayerData(json);
		
		if(json.containsKey("deviceID"))
		{
			List<Map<String, Object>> resultList = DatabaseService.getInstance().executeQuery(
					"select * from "
					+ConfigService.dataBaseName +".player WHERE "
					+ConfigService.dataBaseName +".player.ID = " +"'"+json.getString("deviceID")+"'");

			if(resultList.size()>0)
			{
				Player devicePlayer = GetOldPlayerData(resultList.get(0));
				pl = mergeData(pl, devicePlayer);
			}
		}
		
		CreatPlayer(pl, nowTime);
        return pl;
	}
	
	//得到一个新的玩家ID
	public int getNewID()
	{
		return RandomService.RandomInt(10000, 20000);
	}

	public void updateHeadIcon(Player pl,String headStr)
	{
		pl.headIcon = headStr;
		String dateSQL = "update "+ConfigService.dataBaseName +".player set "+ConfigService.dataBaseName +".player.headIcon = '"+headStr+"' where "+ConfigService.dataBaseName +".player.ID = "+pl.ID+"";
        DatabaseService.getInstance().executeUpdate(dateSQL);
	}
	
	public Player GetNewPlayerData(JSONObject json) 
	{
		Player pl = new Player();
		//没有找到该玩家，创建新玩家
		pl.ID = json.getString("ID");
		try 
		{
			if(json.containsKey("nickName"))
			{
				pl.nickName = java.net.URLEncoder.encode(json.getString("nickName"),"UTF-8");
				
				if(pl.nickName.equals(""))
				{
					pl.nickName = java.net.URLEncoder.encode("tourist " + getNewID(),"UTF-8");
				}
			}
			else
			{
				pl.nickName = java.net.URLEncoder.encode("tourist " + getNewID(),"UTF-8");
			}
			
	        if(json.containsKey("headIcon"))
			{
				pl.headIcon = json.getString("headIcon");
			}
	        else {
	        	pl.headIcon = ConfigService.defaultHeadIcon;
			}
	        
	        pl.stamina  = ConfigService.maxStamina;
		} 
		catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace();
		}
        
        return pl;
	}
	
	public Player GetOldPlayerData(Map<String, Object> mapTmp)
	{
		Player pl = new Player();
		if(mapTmp != null)
		{
	         pl.ID       = (String)mapTmp.get("ID"); 
	 		 pl.nickName = (String)mapTmp.get("name");
	 		 pl.headIcon = (String)mapTmp.get("headIcon"); //头像
	         pl.coinNumber    = (Integer)mapTmp.get("coinNumber");
	         pl.stamina       = (Integer)mapTmp.get("stamina");
	         pl.highScore     = (Integer)mapTmp.get("highScore");
	         pl.diamondNumber = (Integer)mapTmp.get("diamondNumber");
	         
	         pl.rankAward     = (Integer)mapTmp.get("rankAward");
	         pl.levelAward    = (Integer)mapTmp.get("levelAward");
	         pl.rank          = 0;
	         
	         if(pl.headIcon == null)
	         {
	        	 pl.headIcon = ConfigService.defaultHeadIcon;
	         }
	         
//	         pl.rankAward  =10;
//	         pl.levelAward  = 10;
	         
//	         pl.diamondNumber = 20;
//	         pl.lastGetStaminaTime = (Integer)mapTmp.get("lastGetStaminaTime");
	         
	         if(pl.highScore != 0)
	         {
	        	 RankService.getInstance().UpdateGlobalRankCatch(pl);
	         }
	         
	         Number number = (Number) mapTmp.get("lastGetStaminaTime");
	         if(number !=  null)
	         {
	        	 pl.lastGetStaminaTime = number.longValue();
	         }
	         else
	         {
	        	 pl.lastGetStaminaTime = 0;
			}
	         
	         if((String)mapTmp.get("missionData") != null)
	         {
	        	 pl.missionData        = JSONArray.fromObject((String)mapTmp.get("missionData"));
	         }
	         else
	         {
	        	 System.out.println("没有找到关卡数据！");
			 }
	         
	         if((String)mapTmp.get("skinData") != null)
	         {
	        	 pl.skinData        = JSONArray.fromObject((String)mapTmp.get("skinData"));
	         }
	         else
	         {
	        	 System.out.println("没有找到皮肤数据！");
			 }
	         
	         if((String)mapTmp.get("skinData") != null)
	         {
	        	 pl.headIconData        = JSONArray.fromObject((String)mapTmp.get("headIconData"));
	         }
	         else
	         {
	        	 System.out.println("没有找到皮肤数据！");
			 }
	         
	         if((String)mapTmp.get("skinData") != null)
	         {
	        	 pl.TrailData        = JSONArray.fromObject((String)mapTmp.get("TrailData"));
	         }
	         else
	         {
	        	 System.out.println("没有找到皮肤数据！");
			 }
		}
		
		return pl;
	}
	
	public Player mergeData(Player current,Player DataPlayer) 
	{
		DataPlayer.ID = current.ID;
		DataPlayer.nickName = current.nickName;
		DataPlayer.headIcon = current.headIcon;
		
		return DataPlayer;
	}
	
	public void CreatPlayer(Player pl,String nowTime) 
	{
        DatabaseService.getInstance().executeUpdate(
        		"insert into "
        				+ConfigService.dataBaseName +
        				".player (ID,name,coinNumber,lastLoginTime)values('"
        					+pl.ID            +"','"
        					+pl.nickName      +"',"
        					+pl.coinNumber    +",'"
        					+nowTime          +"')");
	}
	
	public void LoginEvent (Player pl,IoSession session,String nowTime)
	{
		pl.session = session;
		
        session.setAttribute("isLogin",true);
        session.setAttribute("player",pl);
        //发送登陆成功
        messageService.loginSuccess(session, pl);
		
        //推送奖励钻石
        pl.pushAwardMessage();
        
        //重新计算体力
//        pl.calcStamina();
        //登录时间
//        String dateSQL = "update "
//       		 +ConfigService.dataBaseName +".player set "
//		         +ConfigService.dataBaseName +".player.lastLoginTime = '"+nowTime
//		         +"' where "
//       		 +ConfigService.dataBaseName +".player.ID = '"+pl.ID+"'";
//        	DatabaseService.getInstance().executeUpdate(dateSQL);
        
	}
}
