package com.v2gogo.project.domain.home.subject;

import java.io.Serializable;

import com.v2gogo.project.utils.common.DateUtil;
import com.v2gogo.project.utils.qiniu.VersionPhotoUrlBuilder;

/**
 * 功能：专题文章实体
 * 
 * @ahthor：黄荣星
 * @date:2015-11-17
 * @version::V1.0
 */
public class SubjectArticle implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String title;
	private String intro;
	private String img;
	private String typelabel;
	private int browser;
	private String href;
	private long publishedtime;
	private String infoId;// 文章ID

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getIntro()
	{
		return intro;
	}

	public void setIntro(String intro)
	{
		this.intro = intro;
	}

	public String getImg()
	{
		return VersionPhotoUrlBuilder.createVersionImageUrl(img);
	}

	public void setImg(String img)
	{
		this.img = img;
	}

	public String getThumbialUrl()
	{
		return VersionPhotoUrlBuilder.createThumbialUrl(getImg());
	}

	public String getPublishedTimeString()
	{
		return DateUtil.convertStringWithTimeStamp(publishedtime);
	}

	public String getTypelabel()
	{
		return typelabel;
	}

	public void setTypelabel(String typelabel)
	{
		this.typelabel = typelabel;
	}

	public int getBrowser()
	{
		return browser;
	}

	public void setBrowser(int browser)
	{
		this.browser = browser;
	}

	public String getHref()
	{
		return href;
	}

	public void setHref(String href)
	{
		this.href = href;
	}

	public long getPublishedtime()
	{
		return publishedtime;
	}

	public void setPublishedtime(long publishedtime)
	{
		this.publishedtime = publishedtime;
	}

	public String getInfoId()
	{
		return infoId;
	}

	public void setInfoId(String infoId)
	{
		this.infoId = infoId;
	}

}
