package com.v2gogo.project.domain;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

/**
 * houjun
 * 
 * @author houjun
 */
public class KickOffInfo implements Serializable
{

	private static final long serialVersionUID = -445664251999705096L;

	@SerializedName("kickoff")
	private String kickOff;

	@SerializedName("username")
	private String username;

	@SerializedName("lastlogintime")
	private long lastlogintime;

	public String getKickOff()
	{
		return kickOff;
	}

	public void setKickOff(String kickOff)
	{
		this.kickOff = kickOff;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public long getLastlogintime()
	{
		return lastlogintime;
	}

	public void setLastlogintime(long lastlogintime)
	{
		this.lastlogintime = lastlogintime;
	}

	@Override
	public String toString()
	{
		return "KickOffInfo [kickOff=" + kickOff + ", username=" + username + ", lastlogintime=" + lastlogintime + "]";
	}

}
