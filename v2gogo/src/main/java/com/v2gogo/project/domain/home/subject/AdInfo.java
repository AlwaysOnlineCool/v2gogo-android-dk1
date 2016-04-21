package com.v2gogo.project.domain.home.subject;

import java.io.Serializable;

import com.v2gogo.project.utils.qiniu.VersionPhotoUrlBuilder;

/**
 * 功能：专题公告实体
 * 
 * @ahthor：黄荣星
 * @date:2015-11-23
 * @version::V1.0
 */
public class AdInfo implements Serializable
{
	/**
	 * serialVersionUID：
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private long createTime;
	private long beginTime;
	private long endTime;
	private double price;
	private String stopicId;
	private String adId;
	private int sortNum;
	private String status;
	private String advtName;
	private String stopicName;
	private String url;
	private String img;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public long getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(long createTime)
	{
		this.createTime = createTime;
	}

	public long getBeginTime()
	{
		return beginTime;
	}

	public void setBeginTime(long beginTime)
	{
		this.beginTime = beginTime;
	}

	public long getEndTime()
	{
		return endTime;
	}

	public void setEndTime(long endTime)
	{
		this.endTime = endTime;
	}

	public double getPrice()
	{
		return price;
	}

	public void setPrice(double price)
	{
		this.price = price;
	}

	public String getStopicId()
	{
		return stopicId;
	}

	public void setStopicId(String stopicId)
	{
		this.stopicId = stopicId;
	}

	public String getAdId()
	{
		return adId;
	}

	public void setAdId(String adId)
	{
		this.adId = adId;
	}

	public int getSortNum()
	{
		return sortNum;
	}

	public void setSortNum(int sortNum)
	{
		this.sortNum = sortNum;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
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

	public String getStopicName()
	{
		return stopicName;
	}

	public void setStopicName(String stopicName)
	{
		this.stopicName = stopicName;
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
		return VersionPhotoUrlBuilder.createVersionImageUrl(img);
	}

	public void setImg(String img)
	{
		this.img = img;
	}

}
