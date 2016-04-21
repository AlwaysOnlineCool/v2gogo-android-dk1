package com.v2gogo.project.domain.home.theme;

import java.io.Serializable;

public class UploadProgressInfo implements Serializable
{

	private static final long serialVersionUID = 6307599829170108821L;
	
	
	private String key;
	private int progress;

	public String getKey()
	{
		return key;
	}

	public void setKey(String key)
	{
		this.key = key;
	}

	public int getProgress()
	{
		return progress;
	}

	public void setProgress(int progress)
	{
		this.progress = progress;
	}

	@Override
	public String toString()
	{
		return "UploadProgressInfo [key=" + key + ", progress=" + progress + "]";
	}

}
