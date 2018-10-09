package core.report;

import core.player.PlayerBase;
import core.player.PlayerEventListener;
import core.player.PlayerService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import core.player.PlayerEvent.PlayerEventEnum;

public class ReportService implements PlayerEventListener
{
	static ReportService instance;
	
	List<IReportInterface> reports = new ArrayList<IReportInterface>();
	
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
		
//		reports.add(new ZhuGeIOReport());
		
		for (int i = 0; i <reports.size(); i++) 
		{
			reports.get(i).Init();
		}
	}
	
	public void Log(PlayerBase player ,String eventName,HashMap<String, String> data)
	{
		for (int i = 0; i < reports.size(); i++) {
			reports.get(i).Log(player,eventName,data);
		}
	}
	
	public void DeviceData(PlayerBase player , HashMap<String, String> data)
	{
		for (int i = 0; i < reports.size(); i++) {
			reports.get(i).DeviceData(player, data);
		}
	}
	
	public void UserData(PlayerBase player ,HashMap<String, String> data)
	{
		for (int i = 0; i < reports.size(); i++) {
			reports.get(i).UserData(player,data);
		}
	}

	@Override
	public void PlayerEvent(core.player.PlayerEvent event) 
	{
		Log(event.m_Player,event.m_eventType.toString(),null);
	}
}
