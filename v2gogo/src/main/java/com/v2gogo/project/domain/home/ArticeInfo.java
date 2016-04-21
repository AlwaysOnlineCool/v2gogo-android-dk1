package com.v2gogo.project.domain.home;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;
import com.v2gogo.project.utils.common.DateUtil;
import com.v2gogo.project.utils.qiniu.VersionPhotoUrlBuilder;

/**
 * 文章实体类
 * 
 * @author houjun
 */
public class ArticeInfo implements Serializable
{

	private static final long serialVersionUID = 7366832945006386960L;

	public static final int COMMENT_NO_IMG = 0;
	public static final int COMMENT_YET_IMG = 1;

	public static final int STATUS_NEW_ADD = 0;// 新增
	public static final int STATUS_CHECK = 1;// 审核
	public static final int STATUS_COMPLETED = 2;// 已发布

	@SerializedName("id")
	private String mId;

	@SerializedName("infotype")
	private int mType;

	@SerializedName("img")
	private String mThumb;

	@SerializedName("title")
	private String mTitle;

	@SerializedName("userid")
	private String mUserId;

	@SerializedName("intro")
	private String mDescription;

	@SerializedName("publishedtime")
	private long mPublishTime;

	@SerializedName("praise")
	private int mLikeNum;

	@SerializedName("comments")
	private int mCommentNum;

	@SerializedName("publishedid")
	private String mPublishId;

	@SerializedName("pubfullname")
	private String mPublishName;

	@SerializedName("browser")
	private int mBrower;

	@SerializedName("content")
	private String mContent;

	@SerializedName("iscom")
	private int mIsCom;

	private int iscomUpload;// * 是否允许评论时上传图片，0：否，1：是. */

	@SerializedName("status")
	private int mStatus;

	@SerializedName("href")
	private String mHref;

	private String typelabel;

	private String mPublishTimeString;

	private int video;

	private String mThumbialUrl;

	public int getBrower()
	{
		return mBrower;
	}

	public void setBrower(int mBrower)
	{
		this.mBrower = mBrower;
	}

	public String getId()
	{
		return mId;
	}

	public void setId(String mId)
	{
		this.mId = mId;
	}

	public String getThumb()
	{
		return VersionPhotoUrlBuilder.createVersionImageUrl(mThumb);
	}

	public void setThumb(String mThumb)
	{
		this.mThumb = mThumb;
	}

	public String getTitle()
	{
		return mTitle;
	}

	public void setTitle(String mTitle)
	{
		this.mTitle = mTitle;
	}

	public String getUserId()
	{
		return mUserId;
	}

	public void setUserId(String mUserId)
	{
		this.mUserId = mUserId;
	}

	public String getDescription()
	{
		return mDescription;
	}

	public void setDescription(String mDescription)
	{
		this.mDescription = mDescription;
	}

	public long getPublishTime()
	{
		return mPublishTime;
	}

	public void setPublishTime(long mPublishTime)
	{
		this.mPublishTime = mPublishTime;
	}

	public int getLikeNum()
	{
		return mLikeNum;
	}

	public void setLikeNum(int mLikeNum)
	{
		this.mLikeNum = mLikeNum;
	}

	public int getCommentNum()
	{
		return mCommentNum;
	}

	public void setCommentNum(int mCommentNum)
	{
		this.mCommentNum = mCommentNum;
	}

	public int getType()
	{
		return mType;
	}

	public void setType(int mType)
	{
		this.mType = mType;
	}

	public String getPublishId()
	{
		return mPublishId;
	}

	public void setPublishId(String mPublishId)
	{
		this.mPublishId = mPublishId;
	}

	public String getPublishName()
	{
		return mPublishName;
	}

	public void setPublishName(String mPublishName)
	{
		this.mPublishName = mPublishName;
	}

	public String getContent()
	{
		return mContent;
	}

	public void setContent(String mContent)
	{
		this.mContent = mContent;
	}

	public int getIsCom()
	{
		return mIsCom;
	}

	public void setIsCom(int mIsCom)
	{
		this.mIsCom = mIsCom;
	}

	public int getStatus()
	{
		return mStatus;
	}

	public void setStatus(int mStatus)
	{
		this.mStatus = mStatus;
	}

	public String getHref()
	{
		return mHref;
	}

	public void setHref(String mHref)
	{
		this.mHref = mHref;
	}

	public String getTypelabel()
	{
		return typelabel;
	}

	public void setTypelabel(String typelabel)
	{
		this.typelabel = typelabel;
	}

	public String getPublishTimeString()
	{
		if (mPublishTimeString == null)
		{
			mPublishTimeString = DateUtil.convertStringWithTimeStamp(mPublishTime);
		}
		return mPublishTimeString;
	}

	public String getThumbialUrl()
	{
		if (null == mThumbialUrl)
		{
			mThumbialUrl = VersionPhotoUrlBuilder.createThumbialUrl(getThumb());
		}
		return mThumbialUrl;
	}

	public int getVideo()
	{
		return video;
	}

	public void setVideo(int video)
	{
		this.video = video;
	}
	
	

	public int getIscomUpload()
	{
		return iscomUpload;
	}

	public void setIscomUpload(int iscomUpload)
	{
		this.iscomUpload = iscomUpload;
	}

	@Override
	public String toString()
	{
		return "ArticeInfo [mId=" + mId + ", mType=" + mType + ", mThumb=" + mThumb + ", mTitle=" + mTitle + ", mUserId=" + mUserId + ", mDescription="
				+ mDescription + ", mPublishTime=" + mPublishTime + ", mLikeNum=" + mLikeNum + ", mCommentNum=" + mCommentNum + ", mPublishId=" + mPublishId
				+ ", mPublishName=" + mPublishName + ", mBrower=" + mBrower + ", mContent=" + mContent + ", mIsCom=" + mIsCom + ", mStatus=" + mStatus
				+ ", mHref=" + mHref + ", typelabel=" + typelabel + ", mPublishTimeString=" + mPublishTimeString + ", video=" + video + "]";
	}

}
