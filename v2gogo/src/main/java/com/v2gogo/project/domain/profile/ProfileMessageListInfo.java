package com.v2gogo.project.domain.profile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * 消息列表实体类
 * 
 * @author AW
 */
public class ProfileMessageListInfo implements Serializable
{

	private static final long serialVersionUID = 3667142759685256087L;

	@SerializedName("page")
	private int mCurrentPage;

	@SerializedName("count")
	private int mCount;

	@SerializedName("list")
	private List<MessageInfo> mMessageInfos;

	public int getCurrentPage()
	{
		return mCurrentPage;
	}

	public void setCurrentPage(int mCurrentPage)
	{
		this.mCurrentPage = mCurrentPage;
	}

	public int getCount()
	{
		return mCount;
	}

	public void setCount(int mCount)
	{
		this.mCount = mCount;
	}

	public List<MessageInfo> getMessageInfos()
	{
		return mMessageInfos;
	}

	public void setMessageInfos(List<MessageInfo> mMessageInfos)
	{
		this.mMessageInfos = mMessageInfos;
	}

	/**
	 * 清空数据
	 */
	public void clear()
	{
		if (null != mMessageInfos && mMessageInfos.size() > 0)
		{
			mMessageInfos.clear();
		}
	}

	/**
	 * 添加数据
	 */
	public void addAll(ProfileMessageListInfo messageListInfo)
	{
		if (null != messageListInfo)
		{
			if (mMessageInfos == null)
			{
				mMessageInfos = new ArrayList<MessageInfo>();
			}
			if (null != messageListInfo.getMessageInfos())
			{
				mMessageInfos.addAll(messageListInfo.getMessageInfos());
			}
		}
	}

	@Override
	public String toString()
	{
		return "MessageListInfo [mCurrentPage=" + mCurrentPage + ", mCount=" + mCount + ", mMessageInfos=" + mMessageInfos + "]";
	}

	/**
	 * 是否为空
	 * 
	 * @return
	 */
	public boolean isEmpty()
	{
		return mMessageInfos == null || mMessageInfos.size() == 0;
	}
}
