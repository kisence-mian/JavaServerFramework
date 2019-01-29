package core.report.model;

import java.util.Date;

public class ReportPayData extends ReportDataBase
{
	public String uuid;          //用户ID 
	public String goods_id;      //商品编号
	public String order_id;      //订单编号
	public String response;      //订单回执
	public float price;          //支付金额
	public String channel;       //支付方式
	public String version;       //游戏版本
	public Date create_time;     //创建时间
	public String create_ip;     //创建IP
}
