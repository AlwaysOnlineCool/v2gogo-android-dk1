package com.v2gogo.project.domain.home.theme;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

/**
 * 主题图片列表信息
 * 
 * @author houjun
 */
public class ThemePhotoListInfo implements Serializable
{

	private static final long serialVersionUID = -5273528074186569860L;

	private int mCurrentPage = 1;

	@SerializedName("result")
	private ThemePhotoListResultInfo mThemePhotoListResultInfo;

	public ThemePhotoListResultInfo getThemePhotoListResultInfo()
	{
		return mThemePhotoListResultInfo;
	}

	public void setThemePhotoListResultInfo(ThemePhotoListResultInfo mThemePhotoListResultInfo)
	{
		this.mThemePhotoListResultInfo = mThemePhotoListResultInfo;
	}

	public int getCurrentPage()
	{
		return mCurrentPage;
	}

	public void setCurrentPage(int mCurrentPage)
	{
		this.mCurrentPage = mCurrentPage;
	}

	public void addAll(ThemePhotoListInfo themePhotoListInfo)
	{
		if (null == mThemePhotoListResultInfo)
		{
			mThemePhotoListResultInfo = new ThemePhotoListResultInfo();
		}
		if (themePhotoListInfo != null)
		{
			mThemePhotoListResultInfo.addAll(themePhotoListInfo.getThemePhotoListResultInfo());
		}
	}

	public void addTop(ThemePhotoInfo themePhotoInfo)
	{
		if (null == mThemePhotoListResultInfo)
		{
			mThemePhotoListResultInfo = new ThemePhotoListResultInfo();
		}
		if (null != themePhotoInfo)
		{
			mThemePhotoListResultInfo.addTop(themePhotoInfo);
		}
	}

	public void clear()
	{
		if (mThemePhotoListResultInfo != null)
		{
			mThemePhotoListResultInfo.clear();
		}
	}

	
	public String getLastTimestamp()
	{
		if (mThemePhotoListResultInfo != null)
		{
			return mThemePhotoListResultInfo.getLastTime();
		}
		return "0";
	}
	
	@Override
	public String toString()
	{
		return "ThemePhotoListInfo [mCurrentPage=" + mCurrentPage + ", mThemePhotoListResultInfo=" + mThemePhotoListResultInfo + "]";
	}

}
