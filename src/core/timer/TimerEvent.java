package core.timer;

import core.timer.TimerService.TimerEnum;

public class TimerEvent
{
	public TimerEnum m_TimerType;
	
	public TimerEvent(TimerEnum timerType) 
	{
		m_TimerType = timerType;
	}
}


