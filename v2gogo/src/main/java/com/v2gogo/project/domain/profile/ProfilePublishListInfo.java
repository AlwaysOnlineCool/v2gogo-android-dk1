package com.v2gogo.project.domain.profile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.v2gogo.project.domain.home.ArticeInfo;

public class ProfilePublishListInfo implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5184194582586329223L;

	@SerializedName("page")
	private int mPage;

	@SerializedName("count")
	private int mCount;

	@SerializedName("list")
	private List<ArticeInfo> mArticeInfos;

	public int getPage()
	{
		return mPage;
	}

	public void setPage(int mPage)
	{
		this.mPage = mPage;
	}

	public int getCount()
	{
		return mCount;
	}

	public void setCount(int mCount)
	{
		this.mCount = mCount;
	}

	public List<ArticeInfo> getArticeInfos()
	{
		return mArticeInfos;
	}

	public void setArticeInfos(List<ArticeInfo> mArticeInfos)
	{
		this.mArticeInfos = mArticeInfos;
	}

	/**
	 * 清空数据
	 */
	public void clear()
	{
		if (null != mArticeInfos && mArticeInfos.size() > 0)
		{
			mArticeInfos.clear();
		}
	}

	/**
	 * 添加数据
	 */
	public void addAll(ProfilePublishListInfo profilePublishListInfo)
	{
		if (null != profilePublishListInfo)
		{
			if (mArticeInfos == null)
			{
				mArticeInfos = new ArrayList<ArticeInfo>();
			}
			if (null != profilePublishListInfo.getArticeInfos())
			{
				mArticeInfos.addAll(profilePublishListInfo.getArticeInfos());
			}
		}
	}

	@Override
	public String toString()
	{
		return "ProfilePublishListInfo [mPage=" + mPage + ", mCount=" + mCount + ", mArticeInfos=" + mArticeInfos + "]";
	}

}
