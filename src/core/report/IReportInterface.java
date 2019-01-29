package core.report;

import core.player.PlayerBase;
import core.report.model.ReportEventData;
import core.report.model.ReportPayData;
import core.report.model.ReportUserData;

public interface IReportInterface 
{
	void Init();
	
	void ReportEvent(PlayerBase player, ReportEventData data);
	
	void ReportUserData(PlayerBase player, ReportUserData data);
	
	void ReportDeviceData(PlayerBase player, ReportUserData data);
	
	void ReportPay(PlayerBase player, ReportPayData data);
}
