package com.v2gogo.project.domain.profile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.v2gogo.project.domain.exchange.PrizeInfo;

/**
 * 奖品列表实体类
 * 
 * @author AW
 */
public class ProfilePrizeListInfo implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 93804570176998548L;

	@SerializedName("page")
	private int mCurrentPage;

	@SerializedName("count")
	private int mCount;

	@SerializedName("list")
	private List<PrizeInfo> mPrizeInfos;

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

	public List<PrizeInfo> getPrizeInfos()
	{
		return mPrizeInfos;
	}

	public void setPrizeInfos(List<PrizeInfo> mPrizeInfos)
	{
		this.mPrizeInfos = mPrizeInfos;
	}

	/**
	 * 清空数据
	 */
	public void clear()
	{
		if (null != mPrizeInfos && mPrizeInfos.size() > 0)
		{
			mPrizeInfos.clear();
		}
	}

	/**
	 * 添加数据
	 */
	public void addAll(ProfilePrizeListInfo profilePrizeListInfo)
	{
		if (null != profilePrizeListInfo)
		{
			if (mPrizeInfos == null)
			{
				mPrizeInfos = new ArrayList<PrizeInfo>();
			}
			if (null != profilePrizeListInfo.getPrizeInfos())
			{
				mPrizeInfos.addAll(profilePrizeListInfo.getPrizeInfos());
			}
		}
	}

	@Override
	public String toString()
	{
		return "ProfilePrizeListInfo [mCurrentPage=" + mCurrentPage + ", mCount=" + mCount + ", mPrizeInfos=" + mPrizeInfos + "]";
	}

}
