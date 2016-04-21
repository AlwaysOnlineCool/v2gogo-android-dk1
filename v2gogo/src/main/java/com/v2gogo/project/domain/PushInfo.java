package com.v2gogo.project.domain;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

/**
 * 推送实体类
 * 
 * @author houjun
 */
public class PushInfo implements Serializable
{

	private static final long serialVersionUID = -5022101536158288690L;
	// 兑换信息推送
	public static final int PUSH_TYPE_DUIHUAN = 0;
	// 团购信息
	public static final int PUSH_TYPE_TUANGOU = 1;
	// 秒杀信息
	public static final int PUSH_TYPE_MIAOSHA = 2;
	// 文章信息
	public static final int PUSH_TYPE_INFO = 3;
	// 奖品过期
	public static final int PUSH_TYPE_JIANIANHUA = 4;
	// 商品
	public static final int PUSH_TYPE_PRODUCT = 5;
	// 单个奖品
	public static final int PUSH_TYPE_PRIZE = 6;
	// 我的消息
	public static final int PUSH_TYPE_MESSAGE = 7;
	// 外部链接
	public static final int PUSH_TYPE_URL = 8;
	// 评论
	public static final int PUSH_TYPE_COMMENT = 9;
	// 电子券
	public static final int PUSH_TYPE_ETC_QUAN = 10;
	// 专题列表
	public static final int PUSH_TYPE_SPECIALTOPIC = 11;

	@SerializedName("id")
	private String id;

	@SerializedName("type")
	private int type;

	@SerializedName("href")
	private String href;

	@SerializedName("title")
	private String title;

	@SerializedName("content")
	private String content;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

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

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	@Override
	public String toString()
	{
		return "PushInfo [id=" + id + ", type=" + type + ", href=" + href + ", title=" + title + ", content=" + content + "]";
	}

}
