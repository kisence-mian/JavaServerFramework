package com.frameWork.dataBase;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.frameWork.dataBase.ConnectionPool.PooledConnection;
import com.frameWork.service.config.configs.ConfigLog;
import com.frameWork.service.config.configs.DataBaseConfig;
import com.frameWork.service.timer.TimerEvent;
import com.frameWork.service.timer.TimerEventListener;
import com.frameWork.service.timer.TimerService;
import com.frameWork.service.timer.TimerService.TimerEnum;

public class DatabaseService implements TimerEventListener
{

	String driverString ="com.mysql.jdbc.Driver";

	List<String> SQLlist = new ArrayList<String>();
	List<String> errorSQLlist = new ArrayList<String>();
	
	static DatabaseService instance;
	public static DatabaseService getInstance() 
	{
		if(instance == null)
		{
			instance = new DatabaseService();
			instance.Init();
		}
		return instance;
	}
	
	@Override
	public void TimeEvent(TimerEvent event) 
	{
		timertask();
	}
	
	public void Init()
	{
		TimerService.AddListener(TimerEnum.preSecond, this);
	}

	//查询数据库，返回结果
	public List<Map<String, Object>> executeQuery(String sql)
	{
		List<Map<String, Object>> list = new ArrayList(); 
		
		PooledConnection connection = null;
		
		try 
		{
			long time =  System.currentTimeMillis();   
			
			connection = DBManager.getConnection(); 
			ResultSet rs = connection.executeQuery(sql);
			list = Ret2Map(rs);
			rs.close();
			
			if(ConfigLog.s_isDebug)
			{
				long useTime = (System.currentTimeMillis() - time)/1000;
//				System.out.println("数据库返回         用时" + useTime + "s");
			}
		} 
		catch ( Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			connection.close();
		}
		
		return list;
	
	}
	//操作数据库
	public void executeUpdate(String sql)
	{
		SQLlist.add(sql);
	}
	void timertask()
	{
		if(SQLlist.size() > 0)
		{
			PooledConnection connection = null;
			
			try 
			{
				long time =  System.currentTimeMillis();   
				
				connection = DBManager.getConnection();
	            //依次执行传入的SQL语句  
	            for (int i = 0; i < SQLlist.size(); i++) 
	            {  
	            	connection.executeUpdate(SQLlist.get(i));
	            }
	            
	            long useTime = (System.currentTimeMillis() - time)/1000;
	            
		        System.out.print("sql 操作成功 操作数："+ SQLlist.size()+"用时" + useTime + "s \n");
			} 
			catch ( Exception e) 
			{
				for (int i = 0; i < SQLlist.size() &&  i < DataBaseConfig.s_dataBaseMaxRequest; i++) 
	            { 
					System.out.println(SQLlist.get(i));
	            }
				
				System.out.println("sql error :\n" + e.toString());
			}
			finally
			{
				SQLlist.clear();
				connection.close();
			}
		}
		
	}
	
	public List<Map<String, Object>> Ret2Map(ResultSet rs) 
	{
		List<Map<String, Object>> list = new ArrayList(); 
		try
		{
		
			ResultSetMetaData md  = rs.getMetaData();
			
			int columnCount = md.getColumnCount(); 
			
			 while (rs.next()) 
			 {
	
			    Map<String, Object> map = new HashMap(); 
			 
			    for (int i = 1; i <= columnCount; i++)
			    {
			    	map.put(md.getColumnName(i), rs.getObject(i)); 
			    	
			    }
			 
			    list.add(map);
			} 
		} 
		catch (Exception e)
		{
			// TODO: handle exception
		}
		
		 return list;
	}
	
	//改增加某些属性
	public void addSomeProperty(String PropertyName,String playerID,int changeNumber) 
	{
		String operationString = "";
		
		if(changeNumber>=0)
		{
			operationString = " + ";
		}
		
		String sqlString = "update "
				+DataBaseConfig.s_DataBaseName +".player "
				+" set "
					+ DataBaseConfig.s_DataBaseName +".player"+ "." + PropertyName 
					+ " = " 
					+ DataBaseConfig.s_DataBaseName +".player"+ "." + PropertyName +operationString + changeNumber
				+  " where "
				+DataBaseConfig.s_DataBaseName +".player.ID = " +"'"+playerID+"'";
		
		executeUpdate(sqlString);
	}

}
