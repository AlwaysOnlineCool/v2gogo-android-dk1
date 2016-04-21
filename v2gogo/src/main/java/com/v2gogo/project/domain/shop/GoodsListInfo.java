package com.v2gogo.project.domain.shop;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.v2gogo.project.domain.SliderInfo;

public class GoodsListInfo implements Serializable
{
	private static final long serialVersionUID = 1199378306648652436L;

	@SerializedName("pageNo")
	private int mPage;

	@SerializedName("count")
	private int mCount;

	@SerializedName("list")
	private List<GoodsInfo> mGoodsInfos;

	@SerializedName("sliders")
	private List<SliderInfo> mSliderInfos;

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

	public List<GoodsInfo> getGoodsInfos()
	{
		return mGoodsInfos;
	}

	public void setGoodsInfos(List<GoodsInfo> mGoodsInfos)
	{
		this.mGoodsInfos = mGoodsInfos;
	}

	public List<SliderInfo> getSliderInfos()
	{
		return mSliderInfos;
	}

	public void setSliderInfos(List<SliderInfo> mSliderInfos)
	{
		this.mSliderInfos = mSliderInfos;
	}

	@Override
	public String toString()
	{
		return "GoodsListInfo [mPage=" + mPage + ", mCount=" + mCount + ", mGoodsInfos=" + mGoodsInfos + ", mSliderInfos=" + mSliderInfos + "]";
	}

	/**
	 * 清除
	 */
	public void clear()
	{
		if (null != mGoodsInfos)
		{
			mGoodsInfos.clear();
		}
	}

	/**
	 * 添加
	 * 
	 * @param goodsListInfo
	 */
	public void addAll(GoodsListInfo goodsListInfo)
	{
		if (null != goodsListInfo)
		{
			if (mGoodsInfos == null)
			{
				mGoodsInfos = new ArrayList<GoodsInfo>();
			}
			if (null != goodsListInfo.getGoodsInfos())
			{
				mGoodsInfos.addAll(goodsListInfo.getGoodsInfos());
			}
		}
	}

	/**
	 * 是否为空
	 * 
	 * @return
	 */
	public boolean isEmpty()
	{
		return mGoodsInfos == null || mGoodsInfos.size() == 0;
	}
}
