package com.v2gogo.project.domain.profile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.v2gogo.project.domain.shop.EtcOrderInfo;

public class ProfileEtcOrderListInfo implements Serializable
{

	private static final long serialVersionUID = -4474671983755508500L;

	@SerializedName("pageNo")
	private int mCurrentPage;

	@SerializedName("pageCount")
	private int mCount;

	@SerializedName("orderProductElectronics")
	private List<EtcOrderInfo> mEtcOrderInfos;

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

	public List<EtcOrderInfo> getEtcOrderInfos()
	{
		return mEtcOrderInfos;
	}

	public void setEtcOrderInfos(List<EtcOrderInfo> mEtcOrderInfos)
	{
		this.mEtcOrderInfos = mEtcOrderInfos;
	}

	/**
	 * 清空数据
	 */
	public void clear()
	{
		if (null != mEtcOrderInfos && mEtcOrderInfos.size() > 0)
		{
			mEtcOrderInfos.clear();
		}
	}

	/**
	 * 添加数据
	 */
	public void addAll(ProfileEtcOrderListInfo profilePrizeListInfo)
	{
		if (null != profilePrizeListInfo)
		{
			if (mEtcOrderInfos == null)
			{
				mEtcOrderInfos = new ArrayList<EtcOrderInfo>();
			}
			if (null != profilePrizeListInfo.getEtcOrderInfos())
			{
				mEtcOrderInfos.addAll(profilePrizeListInfo.getEtcOrderInfos());
			}
		}
	}

	@Override
	public String toString()
	{
		return "ProfileEtcOrderListInfo [mCurrentPage=" + mCurrentPage + ", mCount=" + mCount + ", mEtcOrderInfos=" + mEtcOrderInfos + "]";
	}

}
