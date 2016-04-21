package com.v2gogo.project.domain;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

/**
 * 金币排行榜实体类
 * 
 * @author houjun
 */
public class RankInfo implements Serializable
{

	private static final long serialVersionUID = 1739738283839731106L;

	@SerializedName("fullname")
	private String mNinkname;

	@SerializedName("img")
	private String mAvatar;

	@SerializedName("weekcoin")
	private int mCoin;

	public String getNinkname()
	{
		return mNinkname;
	}

	public void setNinkname(String mNinkname)
	{
		this.mNinkname = mNinkname;
	}

	public String getAvatar()
	{
		return mAvatar;
	}

	public void setAvatar(String mAvatar)
	{
		this.mAvatar = mAvatar;
	}

	public int getCoin()
	{
		return mCoin;
	}

	public void setCoin(int mCoin)
	{
		this.mCoin = mCoin;
	}

	@Override
	public String toString()
	{
		return "RankInfo [mNinkname=" + mNinkname + ", mAvatar=" + mAvatar + ", mCoin=" + mCoin + "]";
	}

}
