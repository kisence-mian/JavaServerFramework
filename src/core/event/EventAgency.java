package core.event;

import java.util.ArrayList;
import java.util.List;

import core.log.LogService;

public class EventAgency<T> 
{
	List<EventHandle<T>> listener = new ArrayList<EventHandle<T>>();
	
	public void AddListener(EventHandle<T> handle)
	{
		listener.add(handle);
	}
	
	public void RemoveListener(EventHandle<T> handle)
	{
		listener.remove(handle);
	}
	
	public void Dispatch(T e)
	{
		for (int i = 0; i < listener.size(); i++)
		{
			try {
				listener.get(i).HandleEvent(e);
			} 
			catch (Exception e2) {
				LogService.Exception("EventAgency", "Exception", e2);
			}
		}
	}
}
