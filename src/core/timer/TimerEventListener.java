package core.timer;

import java.util.EventListener;

public interface TimerEventListener extends EventListener
{
	public void TimeEvent(TimerEvent event);
	
}
