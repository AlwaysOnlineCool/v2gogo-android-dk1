/**    
 * @{#} ShakeAdInfo.java Create on 2016-1-5 下午9:06:47    
 *    
 * Copyright (c) 2013 by BlueSky.    
 *
 *    
 * @author <a href="1084986314@qq.com">BlueSky</a>   
 * @version 1.0    
 */
package com.v2gogo.project.domain.home;

import java.io.Serializable;

import com.v2gogo.project.utils.qiniu.VersionPhotoUrlBuilder;

/**
 * 功能：摇一摇广告实体
 * 
 * @ahthor：黄荣星
 * @date:2016-1-5
 * @version::V1.0
 */
public class ShakeAdInfo implements Serializable
{
	private static final long serialVersionUID = 1L;
	// id
	private String id;
	// 跳转地址
	private String url;
	// 图片地址
	private String img;
	// 创建时间
	private long createtime;
	// 发布状态
	private int status;
	// 广告名称
	private String advtName;
	// 14:摇摇乐背景 15:摇摇乐圆底板 16：摇摇乐手 17：摇摇乐空心圆
	private int type;

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

	public String getImg()
	{
		if (type == 14)
		{
			return VersionPhotoUrlBuilder.createVersionImageUrl(img);
		}
		else
		{
			return VersionPhotoUrlBuilder.createShakeImage(VersionPhotoUrlBuilder.createVersionImageUrl(img));
		}
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

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public String getAdvtName()
	{
		return advtName;
	}

	public void setAdvtName(String advtName)
	{
		this.advtName = advtName;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}
}
