package com.v2gogo.project.domain;

import java.io.Serializable;

import com.v2gogo.project.utils.qiniu.VersionPhotoUrlBuilder;

/**
 * 直播视频实体类
 * 
 * @author houjun
 */
public class LiveVideoInfo implements Serializable
{

	private static final long serialVersionUID = 5129485578737555774L;

	private String id;
	private String url;
	private String content;
	private String img;
	private long createtime;

	private String mThumbialUrl;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public String getImg()
	{
		return VersionPhotoUrlBuilder.createVersionImageUrl(img);
	}

	public void setImg(String img)
	{
		this.img = img;
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
			mThumbialUrl = VersionPhotoUrlBuilder.createThumbialUrlByHalf(getImg());
		}
		return mThumbialUrl;
	}

	@Override
	public String toString()
	{
		return "LiveVideoInfo [id=" + id + ", url=" + url + ", content=" + content + ", img=" + img + ", createtime=" + createtime + "]";
	}

}
