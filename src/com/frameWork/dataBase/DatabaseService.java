package com.frameWork.dataBase;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.frameWork.dataBase.ConnectionPool.PooledConnection;
import com.frameWork.service.LogService;
import com.frameWork.service.config.configs.DataBaseConfig;
import com.frameWork.service.timer.TimerEvent;
import com.frameWork.service.timer.TimerEventListener;
import com.frameWork.service.timer.TimerService;
import com.frameWork.service.timer.TimerService.TimerEnum;

public class DatabaseService 
{
	String m_ModelName = "DataBaseService";
	
	String m_DriverString ="com.mysql.jdbc.Driver";

	List<String> m_SQLlist = new ArrayList<String>();
	List<String> m_ErrorSQLlist = new ArrayList<String>();
	
	static DatabaseService m_Instance;
	public static DatabaseService GetInstance() 
	{
		if(m_Instance == null)
		{
			m_Instance = new DatabaseService();
			m_Instance.Init();
		}
		return m_Instance;
	}
	

	public void Init()
	{
		DataBaseTimerListener dataBaseTimerListener = new DataBaseTimerListener();
		TimerService.AddListener(TimerEnum.preDay, dataBaseTimerListener);
	}
	
	//增
	public void InsertData(String tableName,String[] keyValue) 
	{
		String SQL = "INSERT INTO " + DataBaseConfig.s_DataBaseName + "." + tableName ;
        					
        String Value = "";
        String Keys = "";
       
       for (int i = 0; i < keyValue.length; i++) 
       {
    	   if(i%2 == 0)
    	   {
    		   Keys += keyValue[i];
    	   }
    	   else
    	   {
    		   Value += keyValue[i];
    	   }
    	   
    	   if(i != keyValue.length - 1)
    	   {
    		   if(i%2 == 0)
        	   {
        		   Keys += ",";
        	   }
        	   else
        	   {
        		   Value += ",";
        	   }
    	   }
       }
        					
       SQL = SQL + "(" + Keys + ")" + "VALUES" + "(" + Value + ");";				

       ExecuteUpdate(SQL);
	}
	
	//删
	public void DeleteData(String tableName,String where)
	{
		String SQL = "DELETE FROM " + DataBaseConfig.s_DataBaseName + "." + tableName;
		
		SQL = SQL + " WHERE " + where;
		
		ExecuteUpdate(SQL);
	}
	
	//改
	public void UpdateData(String tableName,String[] keyValue,String where)
	{
		String SQL = "UPDATE " + DataBaseConfig.s_DataBaseName + "." + tableName + " set ";
		String values = "";
		
		for (int i = 0; i < keyValue.length; i += 2) 
		{
			values += keyValue[i] + " = " + keyValue[i + 1];
			
			if( i + 2 != keyValue.length - 1)
			{
				values += ",";
			}
		}
		
		SQL = SQL + values + " WHERE " + where;
		
		ExecuteUpdate(SQL);
	}
	
	//查
	public  List<Map<String, Object>> SelectData(String tableName,String[] keys,String where) 
	{
		String SQL = "SELECT ";
		String keyString = "";
		
		if(keys == null || keys.length == 0)
		{
			keyString = "*";
		}
		
		for (int i = 0; i < keys.length; i++) 
		{
			keyString += keys[i];
			
			if(i != keys.length - 1)
			{
				keyString += ",";
			}
		}
		
		if(where != null && where != "")
		{
			SQL = SQL + keyString + "FROM " + DataBaseConfig.s_DataBaseName + "." + tableName + " WHERE " + where;
		}
		else
		{
			SQL = SQL + keyString + "FROM " + DataBaseConfig.s_DataBaseName + "." + tableName;
		}
		
		return ExecuteQuery(SQL);
	}

	//查询数据库，返回结果
	public List<Map<String, Object>> ExecuteQuery(String sql)
	{
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(); 
		
		PooledConnection connection = null;
		
		try 
		{
			long time =  System.currentTimeMillis();   
			
			connection = DBManager.getConnection(); 
			ResultSet rs = connection.executeQuery(sql);
			list = Ret2Map(rs);
			rs.close();
			
			long useTime = (System.currentTimeMillis() - time)/1000;
			LogService.Log(m_ModelName,"数据库返回         用时" + useTime + "s");

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
	public void ExecuteUpdate(String sql)
	{
		m_SQLlist.add(sql);
	}
	
	void TimerTask()
	{
		if(m_SQLlist.size() > 0)
		{
			PooledConnection connection = null;
			
			try 
			{
				long time =  System.currentTimeMillis();   
				
				connection = DBManager.getConnection();
	            //依次执行传入的SQL语句  
	            for (int i = 0; i < m_SQLlist.size(); i++) 
	            {  
	            	connection.executeUpdate(m_SQLlist.get(i));
	            }
	            
	            long useTime = (System.currentTimeMillis() - time)/1000;
	            
	            LogService.Log(m_ModelName , "sql 操作成功 操作数："+ m_SQLlist.size()+"用时" + useTime + "s ");
			} 
			catch ( Exception e) 
			{
				for (int i = 0; i < m_SQLlist.size() &&  i < DataBaseConfig.s_dataBaseMaxRequest; i++) 
	            { 
					LogService.Error(m_ModelName , m_SQLlist.get(i));
	            }
				
				LogService.Exception(m_ModelName , "SQL Exception" + e.toString() , e );
			}
			finally
			{
				m_SQLlist.clear();
				connection.close();
			}
		}
		
	}
	
	public List<Map<String, Object>> Ret2Map(ResultSet rs) 
	{
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(); 
		try
		{
		
			ResultSetMetaData md  = rs.getMetaData();
			
			int columnCount = md.getColumnCount(); 
			
			 while (rs.next()) 
			 {
	
			    Map<String, Object> map = new HashMap<String, Object>(); 
			 
			    for (int i = 1; i <= columnCount; i++)
			    {
			    	map.put(md.getColumnName(i), rs.getObject(i)); 
			    	
			    }
			 
			    list.add(map);
			} 
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		 return list;
	}
}

class DataBaseTimerListener implements TimerEventListener
{

	@Override
	public void TimeEvent(TimerEvent event) 
	{
		DatabaseService.GetInstance().TimerTask();
		
	}
	
}
