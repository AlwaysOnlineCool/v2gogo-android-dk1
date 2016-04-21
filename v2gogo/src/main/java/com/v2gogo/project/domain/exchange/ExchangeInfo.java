package com.v2gogo.project.domain.exchange;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * 兑换信息实体类
 * 
 * @author houjun
 */
public class ExchangeInfo implements Serializable
{

	private static final long serialVersionUID = -5604001729289644499L;

	@SerializedName("result")
	private String mResult;

	@SerializedName("count")
	private int mCount;

	@SerializedName("page")
	private int mCurrentPage;

	@SerializedName("list")
	private List<PrizeInfo> mPrizeInfos;

	public String getResult()
	{
		return mResult;
	}

	public void setResult(String mResult)
	{
		this.mResult = mResult;
	}

	public List<PrizeInfo> getPrizeInfos()
	{
		return mPrizeInfos;
	}

	public void setPrizeInfos(List<PrizeInfo> mPrizeInfos)
	{
		this.mPrizeInfos = mPrizeInfos;
	}

	public int getCount()
	{
		return mCount;
	}

	public void setCount(int mCount)
	{
		this.mCount = mCount;
	}

	public int getCurrentPage()
	{
		return mCurrentPage;
	}

	public void setCurrentPage(int mCurrentPage)
	{
		this.mCurrentPage = mCurrentPage;
	}

	@Override
	public String toString()
	{
		return "ExchangeInfo [mResult=" + mResult + ", mCount=" + mCount + ", mCurrentPage=" + mCurrentPage + ", mPrizeInfos=" + mPrizeInfos + "]";
	}

	public void addAll(ExchangeInfo exchangeInfo)
	{
		if (exchangeInfo.getPrizeInfos() != null && exchangeInfo.getPrizeInfos().size() > 0)
		{
			if (null == mPrizeInfos)
			{
				mPrizeInfos = new ArrayList<PrizeInfo>();
			}
			mPrizeInfos.addAll(exchangeInfo.getPrizeInfos());
		}
	}

	public void clear()
	{
		if (mPrizeInfos != null && mPrizeInfos.size() > 0)
		{
			mPrizeInfos.clear();
		}
	}

	/**
	 * 是否为空
	 * 
	 * @return
	 */
	public boolean isEmpty()
	{
		return mPrizeInfos == null || mPrizeInfos.size() == 0;
	}
}
