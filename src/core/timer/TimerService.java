package core.timer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import core.log.LogService;

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
			CreatTimerEvent(TimerEnum.preHour);
		}
		
		if(IsNewDay())
		{
			CreatTimerEvent(TimerEnum.preDay);
		}
		
		if(IsNewWeek())
		{
			CreatTimerEvent(TimerEnum.preWeek);
		}
		
		if(IsNewMonth())
		{
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
					LogService.Exception(s_modelName , "DispatchEvent Error", e);
				}
			}
		}
		else 
		{
			return;
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
		
		if(counterMinute >60)
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
