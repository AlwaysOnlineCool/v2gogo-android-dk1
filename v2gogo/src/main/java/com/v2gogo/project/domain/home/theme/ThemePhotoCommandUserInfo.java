package com.v2gogo.project.domain.home.theme;

import java.io.Serializable;

import com.v2gogo.project.utils.qiniu.VersionPhotoUrlBuilder;

public class ThemePhotoCommandUserInfo implements Serializable
{

	private static final long serialVersionUID = -898209137580435078L;

	private String fullName;
	private String headImg;
	private String mThumbialUrl;

	public String getFullName()
	{
		return fullName;
	}

	public void setFullName(String fullName)
	{
		this.fullName = fullName;
	}

	public String getHeadImg()
	{
		return VersionPhotoUrlBuilder.createVersionImageUrl(headImg);
	}

	public void setHeadImg(String headImg)
	{
		this.headImg = headImg;
	}

	public String getThumbialUrl()
	{
		if (null == mThumbialUrl)
		{
			mThumbialUrl = VersionPhotoUrlBuilder.createThumbialUserAvatar(getHeadImg());
		}
		return mThumbialUrl;
	}

	@Override
	public String toString()
	{
		return "ThemePhotoCommandUserInfo [fullName=" + fullName + ", headImg=" + headImg + "]";
	}

}
