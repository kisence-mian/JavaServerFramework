package core.login.define;

import core.login.RuntimePlatform;
import core.player.PlayerBase;

public abstract class PlayerCreateBase 
{
	public String ID;
	
	public RuntimePlatform platform;
	public String deviceUniqueIdentifier;
	
	public abstract void NewPlayer(PlayerBase player);
	
	public abstract void OldPlayer(PlayerBase player);
	
	public void GeneratePlayer(PlayerBase player)
	{
		player.platform = platform;
		player.deviceUniqueIdentifier = deviceUniqueIdentifier;
	}
}
