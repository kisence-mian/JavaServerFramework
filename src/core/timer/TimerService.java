package core.timer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import core.dataBase.DatabaseService;
import core.log.LogService;
import service.CardGameServer;

public class TimerService 
{
	private final static String s_modelName = "TimerService";
	
	public enum TimerEnum 
	{
	    preSecond,
	    pre5S,
	    pre10S,
	    preMinute,
	    preHour,
	    preDay, 
	    preWeek,
	    preMonth;
	}
	
	public static HashMap<TimerEnum, ArrayList<TimerEventListener>>  listeners = new HashMap<TimerEnum, ArrayList<TimerEventListener>>();
	
	public TimerService()
	{
	}
	
	public static void Init() 
	{
		try
        {
		  Timer timer = new Timer();  //定时任务启动
		  System.out.println("Timer启动");  
		  DataBaseDataLoad();
		  timer.schedule(new TimerTask()
		  {
		        public void run() 
		        {  
		        	TimerEvent();
		        }  
		    }, 1000, 1000);  
		  
        }
        catch (Exception e) 
        {
        	e.printStackTrace();
		}
	}
	
	public static void AddListener(TimerEnum timerType ,TimerEventListener listener)
	{
		ArrayList<TimerEventListener> list = null;
		
		if(!listeners.containsKey(timerType))
		{
			list = new ArrayList<TimerEventListener>();
			
			listeners.put(timerType, list);
		}
		else
		{
			list = listeners.get(timerType);
		}
		
		list.add(listener);
	}
	
    private static final String TableName="ServerTime";
    private static final String counterHour_Key="counterHour";
    private static final String lastWeek_Key="lastWeek";
    private static final String lastDay_Key="lastDay";
    private static final String lastMounth_Key="lastMounth";
    
	private static void DataBaseDataLoad()
	{
//		String[] keys =new String[4];
//		keys[0]=counterHour_Key;
//		keys[1]=lastDay_Key;
//		keys[2]=lastWeek_Key;		
//		keys[3]=lastMounth_Key;
		List<Map<String, Object>> resList=	DatabaseService.GetInstance().SelectData(TableName, null,"ID='" + CardGameServer.ServerName + "'");
		if (resList!=null &&resList.size()>0)
		{
			Map<String, Object> keyValues = resList.get(0);
			counterHour=(int)keyValues.get(counterHour_Key);
			lastDay=(int)keyValues.get(lastDay_Key);
			lastWeek=(int)keyValues.get(lastWeek_Key);
			lastMounth=(int)keyValues.get(lastMounth_Key);
		}else
		{
			IsNewHour();
			IsNewDay();
			IsNewWeek();
			IsNewMonth();
			String[] keys =new String[5];
			 keys[0]="ID";
			keys[1]=counterHour_Key;
			keys[2]=lastDay_Key;
			keys[3]=lastWeek_Key;		
			keys[4]=lastMounth_Key;
			Object[] values = new Object[5];
			values[0]=CardGameServer.ServerName;
			values[1]=counterHour;
			values[2]=lastDay;
			values[3]=lastWeek;
			values[4]=lastMounth;
			DatabaseService.GetInstance().InsertData(TableName, keys, values);
		}
	}
	private static void DataBaseDataWrite(String key ,Object value)
	{
		DatabaseService.GetInstance().UpdateData(TableName, new String[]{key}, new Object[]{value}, " ID = '" + CardGameServer.ServerName + "'");
	}
	//该函数每秒调用
	public static void TimerEvent()
	{
		
		CreatTimerEvent(TimerEnum.preSecond);
		
		if(IsNew5S())
		{
			CreatTimerEvent(TimerEnum.pre5S);
		}
		
		if(IsNew10S())
		{
			CreatTimerEvent(TimerEnum.pre10S);
		}
		
		if(IsNewMintus())
		{
			CreatTimerEvent(TimerEnum.preMinute);
		}
		
		if(IsNewHour())
		{
			DataBaseDataWrite(counterHour_Key,counterHour);
			CreatTimerEvent(TimerEnum.preHour);
		}
		
		if(IsNewDay())
		{
			DataBaseDataWrite(lastDay_Key,lastDay);
			CreatTimerEvent(TimerEnum.preDay);
		}
		
		if(IsNewWeek())
		{
			DataBaseDataWrite(lastWeek_Key,lastWeek);
			CreatTimerEvent(TimerEnum.preWeek);
		}
		
		if(IsNewMonth())
		{
			DataBaseDataWrite(lastMounth_Key,lastMounth);
			CreatTimerEvent(TimerEnum.preMonth);
		}
	}
	
	public static void CreatTimerEvent(TimerEnum timerType) 
	{
		TimerEvent event = new TimerEvent(timerType);
		DispatchEvent(event);
	}
	
	static void DispatchEvent(TimerEvent event ) 
	{
		if(listeners.containsKey(event.m_TimerType))
		{
			ArrayList<TimerEventListener> list = listeners.get(event.m_TimerType);
			
			for (int i = 0; i < list.size(); i++) 
			{
				try 
				{
					list.get(i).TimeEvent(event);
				} 
				catch (Exception e) 
				{
					LogService.Error(s_modelName , "TimerService DispatchEvent Error " +e.toString());
				}
			}
		}
	}
	
	static int counter5S = 0;
	
	static boolean IsNew5S()
	{
		counter5S ++;
		
		if(counter5S >5)
		{
			counter5S = 0;
			return true;
		}
		else
		{
			return false;
		}
	}
	
	static int counter10S = 0;
	
	static boolean IsNew10S()
	{
		counter10S ++;
		
		if(counter10S >10)
		{
			counter10S = 0;
			return true;
		}
		else
		{
			return false;
		}
	}
	
	static int counterMinute = 0;
	
	static boolean IsNewMintus()
	{
		counterMinute ++;
		
		if(counterMinute > 60)
		{
			counterMinute = 0;
			return true;
		}
		else
		{
			return false;
		}
	}
	
	static int counterHour = 0;
	
	static boolean IsNewHour()
	{
		counterHour ++;
		
		if(counterHour > 3600)
		{
			counterHour = 0;
			return true;
		}
		else
		{
			return false;
		}
	}
	
	static int lastWeek = 0;
	static boolean IsNewWeek()
	{
		Date today =  new Date();
		Calendar cal = Calendar.getInstance();
		
		cal.setTime(today);
		int day = cal.get(Calendar.DATE);
		int wednesDay = cal.get(Calendar.DAY_OF_WEEK) -1;
		
		//周末晚上重新
		if(day != lastWeek
			&& wednesDay == 1
			)
		{
			lastWeek = day;
			return true;
		}
		else
		{
			return false;
		}
	}
	
	static int lastDay = 0;
	static boolean IsNewDay()
	{
		Date today =  new Date();
		Calendar cal = Calendar.getInstance();
		
		cal.setTime(today);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		
		if(day != lastDay)
		{
			lastDay = day;
			return true;
		}
		else
		{
			return false;
		}
	}
	
	static int lastMounth = 0;
	static boolean IsNewMonth()
	{
		Date today =  new Date();
		Calendar cal = Calendar.getInstance();
		
		cal.setTime(today);
		int month = cal.get(Calendar.MONTH);
		
		if(month != lastMounth)
		{
			lastMounth = month;
			return true;
		}
		else
		{
			return false;
		}
	}
}
