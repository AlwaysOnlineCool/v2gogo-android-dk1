package com.v2gogo.project.domain.home.theme;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

/**
 * 上传主题图片
 * 
 * @author houjun
 */
public class ThemePhotoUploadResultInfo implements Serializable
{

	private static final long serialVersionUID = 9223105240354496289L;

	@SerializedName("photo")
	private ThemePhotoInfo mThemePhotoInfo;
	
	private String flag;
	

	public String getFlag()
	{
		return flag;
	}

	public void setFlag(String flag)
	{
		this.flag = flag;
	}

	public ThemePhotoInfo getmThemePhotoInfo()
	{
		return mThemePhotoInfo;
	}

	public void setmThemePhotoInfo(ThemePhotoInfo mThemePhotoInfo)
	{
		this.mThemePhotoInfo = mThemePhotoInfo;
	}

	@Override
	public String toString()
	{
		return "ThemePhotoUploadResultInfo [mThemePhotoInfo=" + mThemePhotoInfo + "]";
	}

}
