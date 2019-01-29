package core.report.model;

import java.util.Date;
import java.util.Map;

import core.localizedLanguage.SystemLanguage;

public class ReportUserData extends ReportDataBase
{
	public String uuid;      //用户ID 
	public String version;   //游戏版本
	public String channel;   //渠道
	
	public String brand;     //品牌
	public String device;    //设备
	public int resolution_w; //分辨率
	public int resolution_h; 
	
	public String processorType; //处理器架构
	public String processorCount;//处理器核心数
	
	public int memorySize;        //内存大小
	public int graphicMemorySize; //显存大小public 
	public String graphicDeviceType; //OpenGL 类型
	public int shaderLevel; //着色器等级
	
	public String os;        //操作系统
	public String ov;        //操作系统版本
	public String net;       //网络类型
	public String ping;      //网络延迟
	public SystemLanguage language; //用户语言
	
	public Map<String, String> properties; //自定义属性
	public Date lastLoginTime;   //最后登陆时间
	public String lastLoginIp;   //最后登录IP
}
