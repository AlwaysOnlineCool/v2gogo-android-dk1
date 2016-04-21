package com.v2gogo.project.domain.profile;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;
import com.v2gogo.project.utils.common.DateUtil;

/**
 * 消息实体类
 * 
 * @author AW
 */
public class MessageInfo implements Serializable
{

	private static final long serialVersionUID = -2969325182670855843L;

	public static final int NO_READ = 0;
	public static final int YET_READ = 1;

	@SerializedName("isread")
	private int mRead;

	@SerializedName("createtime")
	private long mCreateTime;

	@SerializedName("id")
	private String mId;

	@SerializedName("title")
	private String mTitle;

	@SerializedName("message")
	private String mMsg;

	private String mMessageTime;

	public int getRead()
	{
		return mRead;
	}

	public void setRead(int mRead)
	{
		this.mRead = mRead;
	}

	public long getCreateTime()
	{
		return mCreateTime;
	}

	public void setCreateTime(long mCreateTime)
	{
		this.mCreateTime = mCreateTime;
	}

	public String getId()
	{
		return mId;
	}

	public void setId(String mId)
	{
		this.mId = mId;
	}

	public String getTitle()
	{
		return mTitle;
	}

	public void setTitle(String mTitle)
	{
		this.mTitle = mTitle;
	}

	public String getMsg()
	{
		return mMsg;
	}

	public void setMsg(String mMsg)
	{
		this.mMsg = mMsg;
	}

	public String getmMessageTime()
	{
		if (mMessageTime == null)
		{
			mMessageTime = DateUtil.convertStringWithTimeStamp(mCreateTime);
		}
		return mMessageTime;
	}

	@Override
	public String toString()
	{
		return "MessageInfo [mRead=" + mRead + ", mCreateTime=" + mCreateTime + ", mId=" + mId + ", mTitle=" + mTitle + ", mMsg=" + mMsg + "]";
	}

}
