package com.v2gogo.project.domain;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

/**
 * 应用程序的版本信息
 * 
 * @author houjun
 */
public class VersionInfo implements Serializable
{

	private static final long serialVersionUID = -4672284510387372005L;

	public static final int NO_UPDATE_VERSION = 0;// 没有更新版本
	public static final int HAVE_UPDATE_VERSION = 1;// 有版本更新
	public static final int FOUSE_UPDATE_VERSION = 2;// 强制更新

	@SerializedName("type")
	private int type;

	@SerializedName("text")
	private String text;

	@SerializedName("up")
	private int update;

	@SerializedName("vername")
	private String vername;

	@SerializedName("download")
	private String downloadUrl;

	@SerializedName("md5")
	private String md5String;

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public int getUpdate()
	{
		return update;
	}

	public void setUpdate(int update)
	{
		this.update = update;
	}

	public String getVername()
	{
		return vername;
	}

	public void setVername(String vername)
	{
		this.vername = vername;
	}

	public String getDownloadUrl()
	{
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl)
	{
		this.downloadUrl = downloadUrl;
	}

	public String getMd5String()
	{
		return md5String;
	}

	public void setMd5String(String md5String)
	{
		this.md5String = md5String;
	}

	@Override
	public String toString()
	{
		return "VersionInfo [type=" + type + ", text=" + text + ", update=" + update + ", vername=" + vername + ", downloadUrl=" + downloadUrl + ", md5String="
				+ md5String + "]";
	}

}
