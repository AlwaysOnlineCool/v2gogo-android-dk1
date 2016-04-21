package com.v2gogo.project.domain.home;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;
import com.v2gogo.project.utils.qiniu.VersionPhotoUrlBuilder;

/**
 * 摇一摇奖品实体类
 * 
 * @author houjun
 */
public class ShakePrizeInfo implements Serializable
{

	private static final long serialVersionUID = -457655421415420806L;

	@SerializedName("id")
	private String mId;

	@SerializedName("type")
	private String mType;

	@SerializedName("gid")
	private String mGid;

	@SerializedName("title")
	private String mTitle;

	@SerializedName("url")
	private String mThumb;

	@SerializedName("prizetime")
	private String mPrizeTime;

	@SerializedName("isgread")
	private int misGread;

	private String mThumbialUrl;

	public String getId()
	{
		return mId;
	}

	public void setId(String mId)
	{
		this.mId = mId;
	}

	public String getType()
	{
		return mType;
	}

	public void setType(String mType)
	{
		this.mType = mType;
	}

	public String getGid()
	{
		return mGid;
	}

	public void setGid(String mGid)
	{
		this.mGid = mGid;
	}

	public String getTitle()
	{
		return mTitle;
	}

	public void setTitle(String mTitle)
	{
		this.mTitle = mTitle;
	}

	private String getThumb()
	{
		return VersionPhotoUrlBuilder.createVersionImageUrl(mThumb);
	}

	public void setThumb(String mThumb)
	{
		this.mThumb = mThumb;
	}

	public String getPrizeTime()
	{
		return mPrizeTime;
	}

	public void setPrizeTime(String mPrizeTime)
	{
		this.mPrizeTime = mPrizeTime;
	}

	public int isGread()
	{
		return misGread;
	}

	public void setIsGread(int misGread)
	{
		this.misGread = misGread;
	}

	public String getThumialUrl()
	{
		if (null == mThumbialUrl)
		{
			mThumbialUrl = VersionPhotoUrlBuilder.createThumbialUrl(getThumb());
		}
		return mThumbialUrl;
	}

	@Override
	public String toString()
	{
		return "ShakePrizeInfo [mId=" + mId + ", mType=" + mType + ", mGid=" + mGid + ", mTitle=" + mTitle + ", mThumb=" + mThumb + ", mPrizeTime="
				+ mPrizeTime + ", misGread=" + misGread + "]";
	}
}
