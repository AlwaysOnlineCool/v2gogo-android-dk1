package com.v2gogo.project.domain.home;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

/**
 * 视频实体
 * 
 * @author houjun
 */
public class VedioInfo implements Serializable
{

	private static final long serialVersionUID = -7727414839918181411L;
	public static int STATUS_ADD_NEW = 0;
	public static int STATUS_SHENHEI = 1;
	public static int STATUS_COMPLETED = 2;

	@SerializedName("id")
	private String id;

	@SerializedName("title")
	private String title;// 标题

	@SerializedName("intro")
	private String intro;// 简介

	@SerializedName("img")
	private String img;// 缩略图

	@SerializedName("url")
	private String url;// 视频地址

	@SerializedName("serialcode")
	private String period;// 期数

	@SerializedName("firstplay")
	private long playdate;// 首播时间

	@SerializedName("times")
	private String playtime;// 播放时长

	@SerializedName("createtime")
	private long createtime;// 上传时间

	@SerializedName("praise")
	private int praise;// 赞数

	@SerializedName("comments")
	private int comments;// 评论数

	@SerializedName("status")
	private int status;// 状态 0：新增1：审核 2：发布

	@SerializedName("iscom")
	private int isCom;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

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
		return img;
	}

	public void setImg(String img)
	{
		this.img = img;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getPeriod()
	{
		return period;
	}

	public void setPeriod(String period)
	{
		this.period = period;
	}

	public long getPlaydate()
	{
		return playdate;
	}

	public void setPlaydate(long playdate)
	{
		this.playdate = playdate;
	}

	public String getPlaytime()
	{
		return playtime;
	}

	public void setPlaytime(String playtime)
	{
		this.playtime = playtime;
	}

	public long getCreatetime()
	{
		return createtime;
	}

	public void setCreatetime(long createtime)
	{
		this.createtime = createtime;
	}

	public int getPraise()
	{
		return praise;
	}

	public void setPraise(int praise)
	{
		this.praise = praise;
	}

	public int getComments()
	{
		return comments;
	}

	public void setComments(int comments)
	{
		this.comments = comments;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public int getIsCom()
	{
		return isCom;
	}

	public void setIsCom(int isCom)
	{
		this.isCom = isCom;
	}

	@Override
	public String toString()
	{
		return "VedioInfo [id=" + id + ", title=" + title + ", intro=" + intro + ", img=" + img + ", url=" + url + ", period=" + period + ", playdate="
				+ playdate + ", playtime=" + playtime + ", createtime=" + createtime + ", praise=" + praise + ", comments=" + comments + ", status=" + status
				+ ", isCom=" + isCom + "]";
	}

}
