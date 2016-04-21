package com.v2gogo.project.domain.home;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.v2gogo.project.domain.SliderInfo;
import com.v2gogo.project.domain.ToolInfo;

/**
 * 首页实体类
 * 
 * @author houjun
 */
public class HomeInfo implements Serializable
{

	private static final long serialVersionUID = 3153263313293031844L;

	@SerializedName("result")
	private String mResult;

	@SerializedName("sliders")
	private List<SliderInfo> mSliderInfos;

	@SerializedName("typs")
	private List<PopularizeInfo> mPopularizeInfos;

	@SerializedName("navs")
	private List<NavInfo> mNavInfos;

	@SerializedName("toolbars")
	private List<ToolInfo> mToolInfos;

	public String getResult()
	{
		return mResult;
	}

	public void setResult(String mResult)
	{
		this.mResult = mResult;
	}

	public List<SliderInfo> getSliderInfos()
	{
		return mSliderInfos;
	}

	public void setSliderInfos(List<SliderInfo> mSliderInfos)
	{
		this.mSliderInfos = mSliderInfos;
	}

	public List<PopularizeInfo> getPopularizeInfos()
	{
		return mPopularizeInfos;
	}

	public void setPopularizeInfos(List<PopularizeInfo> mPopularizeInfos)
	{
		this.mPopularizeInfos = mPopularizeInfos;
	}

	public List<NavInfo> getmNavInfos()
	{
		return mNavInfos;
	}

	/**
	 * 清除数据
	 */
	public void clear()
	{
		if (mPopularizeInfos != null)
		{
			mPopularizeInfos.clear();
		}
		if (mSliderInfos != null)
		{
			mSliderInfos.clear();
		}
		if (mNavInfos != null)
		{
			mNavInfos.clear();
		}
	}

	/**
	 * 添加数据
	 */
	public void addAll(HomeInfo homeInfo)
	{
		if (homeInfo != null)
		{
			if (null == mPopularizeInfos)
			{
				mPopularizeInfos = new ArrayList<PopularizeInfo>();
			}
			if (null == mSliderInfos)
			{
				mSliderInfos = new ArrayList<SliderInfo>();
			}
			if (null == mNavInfos)
			{
				mNavInfos = new ArrayList<NavInfo>();
			}
			if (homeInfo.getPopularizeInfos() != null)
			{
				mPopularizeInfos.addAll(homeInfo.getPopularizeInfos());
			}
			if (homeInfo.getSliderInfos() != null)
			{
				mSliderInfos.addAll(homeInfo.getSliderInfos());
			}
			if (homeInfo.getmNavInfos() != null)
			{
				mNavInfos.addAll(homeInfo.getmNavInfos());
			}
		}
	}

	/**
	 * method desc：添加加载更多数据
	 */
	public void addLoadMoreAll(PopularizeInfo info)
	{
		if (null == mPopularizeInfos)
		{
			mPopularizeInfos = new ArrayList<PopularizeInfo>();
		}
		mPopularizeInfos.add(info);
	}

	public List<ToolInfo> getmToolInfos()
	{
		return mToolInfos;
	}

	public void setmToolInfos(List<ToolInfo> mToolInfos)
	{
		this.mToolInfos = mToolInfos;
	}

	@Override
	public String toString()
	{
		return "HomeInfo [mResult=" + mResult + ", mSliderInfos=" + mSliderInfos + ", mPopularizeInfos=" + mPopularizeInfos + "]";
	}

	/**
	 * 是否为空
	 * 
	 * @return
	 */
	public boolean isEmpty()
	{
		return mPopularizeInfos == null || mSliderInfos == null || mNavInfos == null || mSliderInfos.size() == 0 || mPopularizeInfos.size() == 0
				|| mNavInfos.size() == 0;
	}
}
