package com.v2gogo.project.domain.home;

import java.io.Serializable;

import android.text.TextUtils;

import com.v2gogo.project.utils.common.DateUtil;
import com.v2gogo.project.utils.qiniu.VersionPhotoUrlBuilder;

/**
 * 推广item实体类
 * 
 * @author houjun
 */
public class PopularizeItemInfo implements Serializable
{

	private static final long serialVersionUID = 1154350053069325813L;
	public static final int TYPE_GOGO = 0;// 微兔gogo
	public static final int TYPE_KANDIAN = 1;// 百姓看点
	public static final int TYPE_HUATI = 2;// 百姓话题
	public static final int TYPE_HUODONG = 3;// 活动专区
	public static final int TYPE_SHOPPING = 4;// 平台购物
	public static final int TYPE_GAME = 5;// 平台游戏
	public static final int TYPE_GOGO_HUDONG = 6;// 微兔gogo互动

	private int infotype;// 文章的详细分类
	private int browser;// 浏览数
	private int srctype;// 自定义的类型（如文章，商品） 为-1是外部链接
	private int video;

	private String url;// 图片url
	private String href;// 外部链接地址
	private String intro;// 描述信息
	private String tittle;// title
	private String infoid;// 跳转详情id
	private String publishedtime;// 时间
	private String typelabel;

	private String publishedTimeString;

	private String mThumbialUrl;

	public int getInfotype()
	{
		return infotype;
	}

	public void setInfotype(int infotype)
	{
		this.infotype = infotype;
	}

	public int getBrowser()
	{
		return browser;
	}

	public void setBrowser(int browser)
	{
		this.browser = browser;
	}

	public String getUrl()
	{
		return VersionPhotoUrlBuilder.createVersionImageUrl(url);
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getHref()
	{
		return href;
	}

	public void setHref(String href)
	{
		this.href = href;
	}

	public String getIntro()
	{
		return intro;
	}

	public void setIntro(String intro)
	{
		this.intro = intro;
	}

	public String getTittle()
	{
		return tittle;
	}

	public void setTittle(String tittle)
	{
		this.tittle = tittle;
	}

	public String getInfoid()
	{
		return infoid;
	}

	public void setInfoid(String infoid)
	{
		this.infoid = infoid;
	}

	public int getSrctype()
	{
		return srctype;
	}

	public void setSrctype(int srctype)
	{
		this.srctype = srctype;
	}

	public String getPublishedtime()
	{
		return publishedtime;
	}

	public void setPublishedtime(String publishedtime)
	{
		this.publishedtime = publishedtime;
	}

	
	public void setPublishedTimeString(String publishedTimeString)
	{
		this.publishedTimeString = publishedTimeString;
	}

	public String getPublishedTimeString()
	{
		if (publishedTimeString == null)
		{
			if (TextUtils.isEmpty(publishedtime))
			{
				publishedTimeString = "";
			}
			else
			{
				publishedTimeString = DateUtil.convertStringWithTimeStamp(Long.parseLong(publishedtime));
			}
		}
		return publishedTimeString;
	}

	public String getTypelabel()
	{
		return typelabel;
	}

	public void setTypelabel(String typelabel)
	{
		this.typelabel = typelabel;
	}

	public int getVideo()
	{
		return video;
	}

	public void setVideo(int video)
	{
		this.video = video;
	}

	public String getThumbialUrl()
	{
		if (null == mThumbialUrl)
		{
			mThumbialUrl = VersionPhotoUrlBuilder.createThumbialUrl(getUrl());
		}
		return mThumbialUrl;
	}

	@Override
	public String toString()
	{
		return "PopularizeItemInfo [infotype=" + infotype + ", browser=" + browser + ", srctype=" + srctype + ", video=" + video + ", url=" + url + ", href="
				+ href + ", intro=" + intro + ", tittle=" + tittle + ", infoid=" + infoid + ", publishedtime=" + publishedtime + ", typelabel=" + typelabel
				+ ", publishedTimeString=" + publishedTimeString + "]";
	}

}
