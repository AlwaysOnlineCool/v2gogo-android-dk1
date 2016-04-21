package com.v2gogo.project.domain;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;
import com.v2gogo.project.utils.qiniu.VersionPhotoUrlBuilder;

/**
 * 程序启动信息类
 * 
 * @author houjun
 */
public class WelcomeItemInfo implements Serializable
{

	private static final long serialVersionUID = 1085203118548004894L;

	private String mRealImage;

	@SerializedName("title")
	private String mTitle;

	@SerializedName("href")
	private String mHerf;

	@SerializedName("url")
	private String mUrl;

	@SerializedName("sortNum")
	private String sortNum;// 欢迎广告页排序

	private boolean skip;// 是否跳过

	@SerializedName("showtime")
	private int showtime;// 广告时间

	public String getTitle()
	{
		return mTitle;
	}

	public void setTitle(String mTitle)
	{
		this.mTitle = mTitle;
	}

	public String getHerf()
	{
		return mHerf;
	}

	public void setHerf(String mHerf)
	{
		this.mHerf = mHerf;
	}

	public String getUrl()
	{
		return VersionPhotoUrlBuilder.createVersionImageUrl(mUrl);
	}

	public void setUrl(String mUrl)
	{
		this.mUrl = mUrl;
	}

	/**
	 * 得到图片
	 * 
	 * @return
	 */
	public String getRealImage()
	{
		if (null == mRealImage)
		{
			mRealImage = VersionPhotoUrlBuilder.createNoFixedImage(getUrl());
		}
		return mRealImage;
	}

	public String getSortNum()
	{
		return sortNum;
	}

	public void setSortNum(String sortNum)
	{
		this.sortNum = sortNum;
	}

	public int getShowtime()
	{
		return showtime;
	}

	public void setShowtime(int showtime)
	{
		this.showtime = showtime;
	}

	public boolean isSkip()
	{
		return skip;
	}

	public void setSkip(boolean skip)
	{
		this.skip = skip;
	}
	

	@Override
	public String toString()
	{
		return "WelcomeItemInfo [mTitle=" + mTitle + ", mHerf=" + mHerf + ", mUrl=" + mUrl + "]";
	}

}
