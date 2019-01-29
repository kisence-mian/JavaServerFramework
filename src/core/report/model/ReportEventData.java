package core.report.model;

import java.util.Date;
import java.util.Map;

public class ReportEventData  extends ReportDataBase
{
	public String uuid;      //用户ID 
	public String version;   //游戏版本
	public String name;   //渠道
	
	public String os;        //操作系统
	public String ov;        //操作系统版本
	public String net;       //网络类型
	public String ping;      //网络延迟
	
	public Map<String, String> properties; //自定义属性
	public Date time;         //事件时间
}
