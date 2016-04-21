package com.v2gogo.project.domain.home.theme;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * 主题图片列表信息
 * 
 * @author houjun
 */
public class ThemePhotoListResultInfo implements Serializable
{

	private static final long serialVersionUID = 246380685552282489L;

	private int page;
	private int pageCount;

	@SerializedName("photos")
	private List<ThemePhotoInfo> mThemePhotoInfos;

	public List<ThemePhotoInfo> getThemePhotoInfos()
	{
		return mThemePhotoInfos;
	}

	public void setThemePhotoInfos(List<ThemePhotoInfo> mThemePhotoInfos)
	{
		this.mThemePhotoInfos = mThemePhotoInfos;
	}

	public int getPage()
	{
		return page;
	}

	public void setPage(int page)
	{
		this.page = page;
	}

	public int getPageCount()
	{
		return pageCount;
	}

	public void setPageCount(int pageCount)
	{
		this.pageCount = pageCount;
	}

	public void addAll(ThemePhotoListResultInfo themePhotoListResultInfo)
	{
		if (null == mThemePhotoInfos)
		{
			mThemePhotoInfos = new ArrayList<ThemePhotoInfo>();
		}
		if (null != themePhotoListResultInfo && null != themePhotoListResultInfo.getThemePhotoInfos()
				&& themePhotoListResultInfo.getThemePhotoInfos().size() > 0)
		{
			mThemePhotoInfos.addAll(themePhotoListResultInfo.getThemePhotoInfos());
		}
	}

	public void addTop(ThemePhotoInfo themePhotoInfo)
	{
		if (null == mThemePhotoInfos)
		{
			mThemePhotoInfos = new ArrayList<ThemePhotoInfo>();
		}
		if (null != themePhotoInfo)
		{
			mThemePhotoInfos.add(0, themePhotoInfo);
		}
	}

	public void clear()
	{
		if (null != mThemePhotoInfos && mThemePhotoInfos.size() > 0)
		{
			mThemePhotoInfos.clear();
		}
	}

	public String getLastTime()
	{
		if (null != mThemePhotoInfos && mThemePhotoInfos.size() > 0)
		{
			ThemePhotoInfo themePhotoInfo = mThemePhotoInfos.get(mThemePhotoInfos.size() - 1);
			if (themePhotoInfo != null)
			{
				return themePhotoInfo.getTimestamp() + "";
			}
		}
		return "0";
	}

	@Override
	public String toString()
	{
		return "ThemePhotoListResultInfo [page=" + page + ", pageCount=" + pageCount + ", mThemePhotoInfos=" + mThemePhotoInfos + "]";
	}
}
