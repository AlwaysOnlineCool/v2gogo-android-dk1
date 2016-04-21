/**
 * 
 */
package com.v2gogo.project.domain.home;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.v2gogo.project.domain.SliderInfo;

/**
 * v2gogo文章实体类
 * 
 * @author houjun
 */
public class V2gogoArticeInfo implements Serializable
{

	private static final long serialVersionUID = 6620932986715231013L;

	@SerializedName("sliders")
	private List<SliderInfo> mSliderInfos;

	@SerializedName("list")
	private List<ArticeInfo> mArticeInfos;

	@SerializedName("page")
	private int mCurrentPage;

	@SerializedName("count")
	private int mCount;

	public List<SliderInfo> getSliderInfos()
	{
		return mSliderInfos;
	}

	public void setSliderInfos(List<SliderInfo> mSliderInfos)
	{
		this.mSliderInfos = mSliderInfos;
	}

	public List<ArticeInfo> getArticeInfos()
	{
		return mArticeInfos;
	}

	public void setArticeInfos(List<ArticeInfo> mArticeInfos)
	{
		this.mArticeInfos = mArticeInfos;
	}

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

	/**
	 * 添加数据
	 * 
	 * @param v2gogoArticeInfo
	 */
	public void addAll(V2gogoArticeInfo v2gogoArticeInfo)
	{
		if (null == mArticeInfos)
		{
			mArticeInfos = new ArrayList<ArticeInfo>();
		}
		if (null != v2gogoArticeInfo && null != v2gogoArticeInfo.getArticeInfos() && v2gogoArticeInfo.getArticeInfos().size() > 0)
		{
			mArticeInfos.addAll(v2gogoArticeInfo.getArticeInfos());
		}
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

	@Override
	public String toString()
	{
		return "V2gogoArticeInfo [mSliderInfos=" + mSliderInfos + ", mArticeInfos=" + mArticeInfos + ", mCurrentPage=" + mCurrentPage + ", mCount=" + mCount
				+ "]";
	}

}
