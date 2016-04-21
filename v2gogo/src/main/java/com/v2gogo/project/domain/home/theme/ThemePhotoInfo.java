package com.v2gogo.project.domain.home.theme;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.v2gogo.project.utils.common.DateUtil;
import com.v2gogo.project.utils.qiniu.VersionPhotoUrlBuilder;

/**
 * 主题图片信息
 * 
 * @author houjun
 */
public class ThemePhotoInfo implements Serializable
{
	private static final long serialVersionUID = 3478446724344688417L;

	private String id;
	private String tId;
	private String userId;
	// private String photoNo;
	private String resourceNo;
	// private String photoImg;
	private String resourceUrl;
	private String headImg;
	private String fullName;

	private long timestamp;
	// private long photoUploadTime;
	private long resourceUploadTime;

	private String photoDescription;
	private String resourceDescription;

	private String shareAddress;

	private String mFriendlyTime;
	private String realPhotoImage;

	private String thumbialPhotoImage;
	private String mThumbialAvatarUrl;

	private int imgWidth;
	private int praiseNum;
	private int imgHeight;

	private boolean praise;

	@SerializedName("praiseUsers")
	private List<ThemePhotoCommandUserInfo> mCommandUserInfos;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String gettId()
	{
		return tId;
	}

	public void settId(String tId)
	{
		this.tId = tId;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getPhotoImg()
	{
		return VersionPhotoUrlBuilder.createVersionImageUrl(resourceUrl);
	}

	public String getPhotoDescription()
	{
		return photoDescription;
	}

	public void setPhotoDescription(String photoDescription)
	{
		this.photoDescription = photoDescription;
	}

	public int getImgWidth()
	{
		return imgWidth;
	}

	public void setImgWidth(int imgWidth)
	{
		this.imgWidth = imgWidth;
	}

	public int getPraiseNum()
	{
		return praiseNum;
	}

	public void setPraiseNum(int praiseNum)
	{
		this.praiseNum = praiseNum;
	}

	public int getImgHeight()
	{
		return imgHeight;
	}

	public void setImgHeight(int imgHeight)
	{
		this.imgHeight = imgHeight;
	}

	public boolean isPraise()
	{
		return praise;
	}

	public void setPraise(boolean praise)
	{
		this.praise = praise;
	}

	public String getHeadImg()
	{
		return VersionPhotoUrlBuilder.createVersionImageUrl(headImg);
	}

	public void setHeadImg(String headImg)
	{
		this.headImg = headImg;
	}

	public String getFullName()
	{
		return fullName;
	}

	public void setFullName(String fullName)
	{
		this.fullName = fullName;
	}

	public List<ThemePhotoCommandUserInfo> getCommandUserInfos()
	{
		return mCommandUserInfos;
	}

	public void setCommandUserInfos(List<ThemePhotoCommandUserInfo> mCommandUserInfos)
	{
		this.mCommandUserInfos = mCommandUserInfos;
	}

	public long getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(long timestamp)
	{
		this.timestamp = timestamp;
	}

	public String getShareAddress()
	{
		return shareAddress;
	}

	public void setShareAddress(String shareAddress)
	{
		this.shareAddress = shareAddress;
	}

	public String getResourceNo()
	{
		return resourceNo;
	}

	public void setResourceNo(String resourceNo)
	{
		this.resourceNo = resourceNo;
	}

	public String getResourceUrl()
	{
		return resourceUrl;
	}

	public void setResourceUrl(String resourceUrl)
	{
		this.resourceUrl = resourceUrl;
	}

	public long getResourceUploadTime()
	{
		return resourceUploadTime;
	}

	public void setResourceUploadTime(long resourceUploadTime)
	{
		this.resourceUploadTime = resourceUploadTime;
	}

	public String getResourceDescription()
	{
		return resourceDescription;
	}

	public void setResourceDescription(String resourceDescription)
	{
		this.resourceDescription = resourceDescription;
	}

	/**
	 * 得到小图头像
	 * 
	 * @return
	 */
	public String getAvatarThumbialUrl()
	{
		if (null == mThumbialAvatarUrl)
		{
			mThumbialAvatarUrl = VersionPhotoUrlBuilder.createThumbialUserAvatar(getHeadImg());
		}
		return mThumbialAvatarUrl;
	}

	/**
	 * 得到小图路径
	 * 
	 * @return
	 */
	public String getThumbialPhotoImage()
	{
		if (null == thumbialPhotoImage)
		{
			thumbialPhotoImage = VersionPhotoUrlBuilder.createThumbialUrl(getPhotoImg());
		}
		return thumbialPhotoImage;
	}

	/**
	 * 得到原图路径
	 * 
	 * @return
	 */
	public String getRealPhotoImage()
	{
		if (null == realPhotoImage)
		{
			realPhotoImage = VersionPhotoUrlBuilder.createNoFixedImage(getPhotoImg());
		}
		return realPhotoImage;
	}

	public String getFriendlyTime()
	{
		if (mFriendlyTime == null)
		{
			mFriendlyTime = DateUtil.getTimeDiff(resourceUploadTime);
		}
		return mFriendlyTime;
	}

	/**
	 * 添加最新赞的人的列表
	 * 
	 * @param commandUserInfo
	 */
	public void addTop(ThemePhotoCommandUserInfo commandUserInfo)
	{
		if (mCommandUserInfos == null)
		{
			mCommandUserInfos = new ArrayList<ThemePhotoCommandUserInfo>();
		}
		mCommandUserInfos.add(0, commandUserInfo);
	}

	@Override
	public String toString()
	{
		return "ThemePhotoInfo [id=" + id + ", tId=" + tId + ", userId=" + userId + ", photoNo=" + resourceNo + ", photoImg=" + resourceUrl + ", headImg="
				+ headImg + ", fullName=" + fullName + ", photoUploadTime=" + resourceUploadTime + ", photoDescription=" + photoDescription
				+ ", mFriendlyTime=" + mFriendlyTime + ", thumbialPhotoImage=" + thumbialPhotoImage + ", imgWidth=" + imgWidth + ", praiseNum=" + praiseNum
				+ ", imgHeight=" + imgHeight + ", praise=" + praise + ", mCommandUserInfos=" + mCommandUserInfos + "]";
	}
}
