package com.v2gogo.project.domain.home;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.v2gogo.project.domain.SliderInfo;

/**
 * 关注实体类
 * 
 * @author houjun
 */
public class ConcernInfo implements Serializable
{

	private static final long serialVersionUID = 7366832945006386960L;

	@SerializedName("result")
	private String mResult;

	@SerializedName("list")
	private List<ArticeInfo> mArticeInfos;

	@SerializedName("sliders")
	private List<SliderInfo> mSliderInfos;

	@SerializedName("count")
	private int mCount;

	@SerializedName("page")
	private int mCurrentPage;

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

	public String getResult()
	{
		return mResult;
	}

	public void setResult(String mResult)
	{
		this.mResult = mResult;
	}

	public List<ArticeInfo> getArticeInfos()
	{
		return mArticeInfos;
	}

	public void setArticeInfos(List<ArticeInfo> mArticeInfos)
	{
		this.mArticeInfos = mArticeInfos;
	}

	public List<SliderInfo> getSliderInfos()
	{
		return mSliderInfos;
	}

	public void setSliderInfos(List<SliderInfo> mSliderInfos)
	{
		this.mSliderInfos = mSliderInfos;
	}

	public void addAll(ConcernInfo concernInfo)
	{
		if (null == mArticeInfos)
		{
			mArticeInfos = new ArrayList<ArticeInfo>();
		}
		if (null != concernInfo && null != concernInfo.getArticeInfos() && concernInfo.getArticeInfos().size() > 0)
		{
			mArticeInfos.addAll(concernInfo.getArticeInfos());
		}
	}

	public void clear()
	{
		if (null != mArticeInfos && mArticeInfos.size() > 0)
		{
			mArticeInfos.clear();
		}
	}

	@Override
	public String toString()
	{
		return "ConcernInfo [mResult=" + mResult + ", mConcernItemInfos=" + mArticeInfos + ", mSliderInfos=" + mSliderInfos + ", mCount=" + mCount
				+ ", mCurrentPage=" + mCurrentPage + "]";
	}

	/**
	 * 是否为空
	 * 
	 * @return
	 */
	public boolean isEmpty()
	{
		return mSliderInfos == null || mSliderInfos.size() == 0 || mArticeInfos == null || mArticeInfos.size() == 0;
	}

}
