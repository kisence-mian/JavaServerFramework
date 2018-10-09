package core.player;

public class PlayerEvent 
{
	public enum PlayerEventEnum
	{
		NewPlayer,
		Login,
		Exit,
	}
	
	public PlayerBase m_Player;
	public PlayerEventEnum m_eventType;
}
