package com.v2gogo.project.domain;

import java.io.Serializable;

import android.graphics.Bitmap;

/**
 * 分享信息实体类
 * 
 * @author houjun
 */
public class ShareInfo implements Serializable
{

	private static final long serialVersionUID = -962088428066239732L;

	private String href;
	private String title;
	private String imageUrl;
	private String description;
	private Bitmap mBitmap;
	private String targedId;// 分享目标ID。例如：文章ID，商品ID，奖品ID...

	public String getHref()
	{
		return href;
	}

	public void setHref(String href)
	{
		this.href = href;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getImageUrl()
	{
		return imageUrl;
	}

	public void setImageUrl(String imageUrl)
	{
		this.imageUrl = imageUrl;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public Bitmap getBitmap()
	{
		return mBitmap;
	}

	public void setBitmap(Bitmap mBitmap)
	{
		this.mBitmap = mBitmap;
	}

	public String getTargedId()
	{
		return targedId;
	}

	public void setTargedId(String targedId)
	{
		this.targedId = targedId;
	}

	@Override
	public String toString()
	{
		return "ShareInfo [href=" + href + ", title=" + title + ", imageUrl=" + imageUrl + ", description=" + description + ", mBitmap=" + mBitmap + "]";
	}

}
