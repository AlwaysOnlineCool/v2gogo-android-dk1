package com.v2gogo.project.domain.home.theme;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ThemePhotoCommandListInfo implements Serializable
{

	private static final long serialVersionUID = 6366822274711061099L;

	private int page;
	private int pageCount;

	@SerializedName("photoUserPraise")
	private List<ThemePhotoCommandUserInfo> mCommandUserInfos;

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

	public List<ThemePhotoCommandUserInfo> getmCommandUserInfos()
	{
		return mCommandUserInfos;
	}

	public void setmCommandUserInfos(List<ThemePhotoCommandUserInfo> mCommandUserInfos)
	{
		this.mCommandUserInfos = mCommandUserInfos;
	}

	@Override
	public String toString()
	{
		return "ThemePhotoCommandListInfo [page=" + page + ", pageCount=" + pageCount + ", mCommandUserInfos=" + mCommandUserInfos + "]";
	}
	
	/**
	 * 清除数据
	 */
	public void clear()
	{
		if(null != mCommandUserInfos && mCommandUserInfos.size() >0)
		{
			mCommandUserInfos.clear();
		}
	}
	
	
	/**
	 * 增加数据
	 * @param commandListInfo
	 */
	public void addAll(ThemePhotoCommandListInfo commandListInfo)
	{
		if(null != commandListInfo && null !=commandListInfo.getmCommandUserInfos() )
		{
			if(null == mCommandUserInfos)
			{
				mCommandUserInfos =  new ArrayList<ThemePhotoCommandUserInfo>();
			}
			mCommandUserInfos.addAll(commandListInfo.getmCommandUserInfos());
		}
	}
}
