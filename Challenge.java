package PoolGame;

import java.io.Serializable;

public class Challenge implements Serializable
{
	private String username;
	private ChatClient client;
	private boolean accept = false;
	
	public Challenge(String username, ChatClient client, boolean accept)
	{
		this.username = username;
		this.client = client;
		this.accept = accept;
	}
	
	public boolean getAccept()
	{
		return accept;
	}
	
	public ChatClient getClient()
	{
		return client;
	}
	
	public String getUsername()
	{
		return username;
	}
}
