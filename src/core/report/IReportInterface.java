package core.report;

import java.util.HashMap;

import core.player.PlayerBase;

public interface IReportInterface 
{
	void Init();
	
	void Log(PlayerBase player, String eventKey,HashMap<String, String> data);
	
	void UserData(PlayerBase player, HashMap<String, String> data);
	
	void DeviceData(PlayerBase player, HashMap<String, String> data);
}
