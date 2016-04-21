package com.v2gogo.project.domain.profile;

import java.io.Serializable;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.v2gogo.project.utils.common.DateUtil;
import com.v2gogo.project.utils.qiniu.VersionPhotoUrlBuilder;

/**
 * 收藏信息
 * 
 * @author houjun
 */
public class CollectionsInfo implements Serializable
{
	private static final long serialVersionUID = 6586296269870928260L;

	public static final int TYPE_ARTICE = 0;
	public static final int TYPE_GOODS = 1;
	public static final int TYPE_PRIZE = 2;

	@SerializedName("title")
	private String mTitle;

	private String mCreateTimeString;

	@SerializedName("intro")
	private String mDescription;

	@SerializedName("type")
	private int mType;

	@SerializedName("url")
	private String mThumb;

	@SerializedName("id")
	private String mId;

	@SerializedName("srcid")
	private String mSrcId;

	private String mThumbialUrl;

	private long createtime;

	public String getTitle()
	{
		return mTitle;
	}

	public void setTitle(String mTitle)
	{
		this.mTitle = mTitle;
	}

	public String getDescription()
	{
		return mDescription;
	}

	public void setDescription(String mDescription)
	{
		this.mDescription = mDescription;
	}

	public int getType()
	{
		return mType;
	}

	public void setType(int mType)
	{
		this.mType = mType;
	}

	public String getThumb()
	{
		return VersionPhotoUrlBuilder.createVersionImageUrl(mThumb);
	}

	public void setThumb(String mThumb)
	{
		this.mThumb = mThumb;
	}

	public String getId()
	{
		return mId;
	}

	public void setId(String mId)
	{
		this.mId = mId;
	}

	public String getSrcId()
	{
		return mSrcId;
	}

	public void setSrcId(String mSrcId)
	{
		this.mSrcId = mSrcId;
	}

	public String getCreateTimeString()
	{
		if (TextUtils.isEmpty(mCreateTimeString))
		{
			mCreateTimeString = DateUtil.convertStringWithTimeStamp(createtime);
		}
		return mCreateTimeString;
	}

	public long getCreatetime()
	{
		return createtime;
	}

	public void setCreatetime(long createtime)
	{
		this.createtime = createtime;
	}

	public String getThumbialUrl()
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
		return "CollectionsInfo [mTitle=" + mTitle + ", mCreateTimeString=" + mCreateTimeString + ", mDescription=" + mDescription + ", mType=" + mType
				+ ", mThumb=" + mThumb + ", mId=" + mId + ", mSrcId=" + mSrcId + ", createtime=" + createtime + "]";
	}
}
