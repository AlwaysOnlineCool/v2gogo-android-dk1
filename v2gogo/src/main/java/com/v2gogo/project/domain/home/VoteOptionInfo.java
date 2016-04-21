package com.v2gogo.project.domain.home;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

/**
 * 投票选项实体类
 * 
 * @author houjun
 */
public class VoteOptionInfo implements Serializable
{
	private static final long serialVersionUID = -3072684883703587291L;

	@SerializedName("id")
	private String mId;

	@SerializedName("name")
	private String mName;

	@SerializedName("praise")
	private int mPraise;

	@SerializedName("srcid")
	private String mSrcId;

	public String getId()
	{
		return mId;
	}

	public void setId(String mId)
	{
		this.mId = mId;
	}

	public String getName()
	{
		return mName;
	}

	public void setName(String mName)
	{
		this.mName = mName;
	}

	public int getPraise()
	{
		return mPraise;
	}

	public void setmPraise(int mPraise)
	{
		this.mPraise = mPraise;
	}

	public String getSrcId()
	{
		return mSrcId;
	}

	public void setSrcId(String mSrcId)
	{
		this.mSrcId = mSrcId;
	}

	@Override
	public String toString()
	{
		return "VoteOptionInfo [mId=" + mId + ", mName=" + mName + ", mPraise=" + mPraise + ", mSrcId=" + mSrcId + "]";
	}

}
