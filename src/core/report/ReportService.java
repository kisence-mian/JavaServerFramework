package core.report;

import core.log.LogService;
import core.player.PlayerBase;
import core.player.PlayerEventListener;
import core.player.PlayerService;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.player.PlayerEvent.PlayerEventEnum;
import core.report.model.ReportDataBase;
import core.report.model.ReportEventData;
import core.report.model.ReportPayData;
import core.report.model.ReportUserData;
import core.timer.TimerEvent;
import core.timer.TimerEventListener;
import core.timer.TimerService;
import core.timer.TimerService.TimerEnum;
import user.service.report.MyDataReport;

public class ReportService implements PlayerEventListener,TimerEventListener
{
	static ReportService instance;
	
	List<IReportInterface> reports = new ArrayList<IReportInterface>();
	List<ReportDataBase> reportDatas = new ArrayList<ReportDataBase>();
	
	public static ReportService GetInstance()
	{
		if(instance == null)
		{
			instance = new ReportService();
			instance.Init();
		}
		
		return instance;
	}
	
	public void Init() 
	{
		PlayerService.AddListener(PlayerEventEnum.Login, this);
		PlayerService.AddListener(PlayerEventEnum.Exit, this);
		PlayerService.AddListener(PlayerEventEnum.NewPlayer, this);
		
		TimerService.AddListener(TimerEnum.preSecond, this);
		
		reports.add(new MyDataReport());
		for (int i = 0; i <reports.size(); i++) 
		{
			reports.get(i).Init();
		}
	}
	
	@Override
	public void TimeEvent(TimerEvent event) 
	{
		ReportData();
	}
	
	@Override
	public void PlayerEvent(core.player.PlayerEvent event) 
	{
		if(event.m_eventType == PlayerEventEnum.Exit)
		{
			HashMap<String, String> hsData = new HashMap<String, String>();
			hsData.put("ID", event.m_Player.m_ID);
			ReportEvent(event.m_Player, "Player Logout", hsData);
		}
		
		else if(event.m_eventType == PlayerEventEnum.Login)
		{
			HashMap<String, String> hsData = new HashMap<String, String>();
			hsData.put("ID", event.m_Player.m_ID);
			ReportEvent(event.m_Player, "Player Logout", hsData);
		}
	}
	
	public void ReportData()
	{
		try
		{
			for (int i = 0; i < reportDatas.size(); i++) 
			{
				ReportDataBase data = reportDatas.get(i);
				
				if(data.getClass() == ReportEventData.class)
				{
					UploadEvent((ReportEventData)data);
				}
				
				if(data.getClass() == ReportUserData.class)
				{
					UploadUserData((ReportUserData)data);
				}
				
				if(data.getClass() == ReportPayData.class)
				{
					UploadPayData((ReportPayData)data);
				}
				
				reportDatas.remove(i);
				i--;
			}
		}
		catch (Exception e)
		{
			LogService.Error("", "ReportData  Exception-> " + e.toString());
		}
	}
	
	void UploadEvent(ReportEventData data )
	{
		try
		{
			for (int i = 0; i < reports.size(); i++) {
				reports.get(i).ReportEvent(data.player,(ReportEventData)data);
			}
		}
		catch (Exception e) 
		{
			LogService.Error("", "ReportEvent  Exception-> "+e.toString());
		}
	}
	
	void UploadUserData(ReportUserData data )
	{
		try
		{
			for (int i = 0; i < reports.size(); i++) {
				reports.get(i).ReportUserData(data.player,(ReportUserData)data);
			}
		}
		catch (Exception e) 
		{
			LogService.Error("", "UploadUserData  Exception-> "+e.toString());
		}
	}
	
	void UploadPayData(ReportPayData data )
	{
		try
		{
			for (int i = 0; i < reports.size(); i++) {
				reports.get(i).ReportPay(data.player,(ReportPayData)data);
			}
		}
		catch (Exception e) 
		{
			LogService.Error("", "UploadPayData  Exception-> "+e.toString());
		}
	}
	
	public void ReportEvent(PlayerBase player ,String eventName,Map<String, String> data)
	{
		try
		{
			ReportEventData reportEventData = new ReportEventData();
			reportEventData.player = player;
			reportEventData.uuid = player.m_ID;
			reportEventData.name = eventName;
			reportEventData.time = new Date();
			reportEventData.properties = data;
			reportEventData.version = player.vsrsion;
			
			reportDatas.add(reportEventData);
		
		}
		catch (Exception e) 
		{
			LogService.Error("", "ReportEvent  Exception-> "+e.toString());
		}
	}
	
	public void ReportEvent(PlayerBase player ,ReportEventData data)
	{
		try
		{
			data.player = player;
			reportDatas.add(data);
		}
		catch (Exception e) 
		{
			LogService.Error("", "ReportEvent  Exception-> "+e.toString());
		}
	}
	
	public void ReportDeviceData(PlayerBase player , ReportUserData data)
	{
		try
		{
			data.player = player;
			reportDatas.add(data);
		}
		catch (Exception e) 
		{
			LogService.Error("", "ReportDeviceData  Exception-> "+e.toString());
		}
	}
	
	public void ReportPayData(PlayerBase player , ReportPayData data)
	{
		try
		{
			data.player = player;
			reportDatas.add(data);
		}
		catch (Exception e) 
		{
			LogService.Error("", "ReportDeviceData  Exception-> "+e.toString());
		}
	}
	
	public void ReportUserData(PlayerBase player)
	{
		try
		{
			ReportUserData data = new ReportUserData();
			data.uuid = player.m_ID;
			data.lastLoginIp = player.GetIP();
			data.lastLoginTime = new Date();
			
			data.player = player;
			reportDatas.add(data);
		
		}
		catch (Exception e) 
		{
			LogService.Error("", "ReportUserData  Exception-> "+e.toString());
		}
	}
	
	public void ReportUserData(PlayerBase player ,ReportUserData data)
	{
		try
		{
			data.player = player;
			reportDatas.add(data);
		}
		catch (Exception e) 
		{
			LogService.Error("", "ReportUserData  Exception-> "+e.toString());
		}
	}

	//把List类型的数据拼接成数据上报格式
	public static String ListSort2String(List<String> listData) 
	{
		listData.sort(null);
		String tempStr="";
		for (String key : listData)
		{
			tempStr+=key+"_";
		}
		
		return tempStr;
	}
}
