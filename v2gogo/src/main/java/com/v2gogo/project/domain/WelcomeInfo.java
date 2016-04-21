package com.v2gogo.project.domain;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * 程序启动信息类
 * 
 * @author houjun
 */
public class WelcomeInfo implements Serializable
{
	private static final long serialVersionUID = -9216110961370780518L;

	@SerializedName("result")
	private String mResult;

	@SerializedName("welcomes")
	private List<WelcomeItemInfo> mWelcomeItemInfos;

	@SerializedName("version")
	private VersionInfo mVersionInfo;
	

	public String getResult()
	{
		return mResult;
	}

	public void setResult(String result)
	{
		this.mResult = result;
	}

	public List<WelcomeItemInfo> getmWelcomeItemInfos()
	{
		return mWelcomeItemInfos;
	}

	public void setmWelcomeItemInfos(List<WelcomeItemInfo> mWelcomeItemInfos)
	{
		this.mWelcomeItemInfos = mWelcomeItemInfos;
	}

	public VersionInfo getVersionInfo()
	{
		return mVersionInfo;
	}

	public void setVersionInfo(VersionInfo mVersionInfo)
	{
		this.mVersionInfo = mVersionInfo;
	}

	@Override
	public String toString()
	{
		return "WelcomeInfo [mResult=" + mResult + ", mWelcomeItemInfos=" + mWelcomeItemInfos + ", mVersionInfo=" + mVersionInfo + "]";
	}

}
