package com.v2gogo.project.domain;

import java.io.Serializable;

import com.v2gogo.project.utils.qiniu.VersionPhotoUrlBuilder;

/**
 * 轮播实体类
 * 
 * @author houjun
 */
public class SliderInfo implements Serializable
{
	private static final long serialVersionUID = -3249042248593904631L;

	public static final int SRC_TYPE_WEBSITE = -1;// 外部链接
	public static final int SRC_TYPE_INFO = 0;// 文章
	public static final int SRC_TYPE_PRIZE = 1;// 奖品
	public static final int SRC_TYPE_PRODUCT = 2;// 商品
	public static final int SRC_TYPE_SHOP = 3;// 团购列表
	public static final int SRC_TYPE_THEME = 4;// 主题图片
	public static final int SRC_TYPE_EXCHANGE = 5;// 兑换列表
	public static final int SRC_TYPE_SHARED = 6;// 分享
	public static final int SRC_TYPE_UPLOAD_PIC = 7;// 上传图片
	public static final int SRC_TYPE_UPLOAD_VOICE = 8;// 上传声音
	public static final int SRC_TYPE_PRODUCT_TYPE = 9;// 商品类型
	public static final int SRC_TYPE_ORDER_lIST = 10;// 订单查询
	public static final int SRC_TYPE_BUY_NOW = 11;// 立即购买
	public static final int SRC_TYPE_BUY_lOVE = 12;// 公益
	public static final int SRC_TYPE_UPDATE = 13;// 升级
	public static final int SRC_TYPE_UPLOADE_FILE = 14;// 上传多媒体文件【图片、视频、音频】

	private int srctype;

	private String srcid;
	private String title;
	private String url;
	private String href;

	private String mThumbialUrl;

	public int getSrctype()
	{
		return srctype;
	}

	public void setSrctype(int srctype)
	{
		this.srctype = srctype;
	}

	public String getSrcid()
	{
		return srcid;
	}

	public void setSrcid(String srcid)
	{
		this.srcid = srcid;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	private String getUrl()
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

	public String getThumbialUrl()
	{
		if (null == mThumbialUrl)
		{
			mThumbialUrl = VersionPhotoUrlBuilder.createThumbialUrlByHalf(getUrl());
		}
		return mThumbialUrl;
	}

	@Override
	public String toString()
	{
		return "SliderInfo [srctype=" + srctype + ", srcid=" + srcid + ", title=" + title + ", url=" + url + ", href=" + href + "]";
	}

}
