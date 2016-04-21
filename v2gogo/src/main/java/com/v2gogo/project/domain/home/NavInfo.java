package com.v2gogo.project.domain.home;

import java.io.Serializable;

import com.v2gogo.project.utils.qiniu.VersionPhotoUrlBuilder;

/**
 * 功能：首页导航栏实体信息
 * 
 * @ahthor：黄荣星
 * @date:2015-11-16
 * @version::V1.0
 */
public class NavInfo implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String id;// id
	private String name; // 名称
	private String url;// URL地址
	private String bgImg;// 图片地址
	private String rowNum;// 行数
	private String columnNum;// 列数
	private int contentType;// 类型，根据类型不同跳转到不到界面

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getBgImg()
	{
		return VersionPhotoUrlBuilder.createVersionImageUrl(bgImg);
	}

	public void setBgImg(String bgImg)
	{
		this.bgImg = bgImg;
	}

	/**
	 * method desc：获取加工后的图片地址
	 * 
	 * @return
	 */
	public String getThumbImageUrl()
	{
		return VersionPhotoUrlBuilder.createThumbialUserAvatar(getBgImg());
	}

	public String getRowNum()
	{
		return rowNum;
	}

	public void setRowNum(String rowNum)
	{
		this.rowNum = rowNum;
	}

	public String getColumnNum()
	{
		return columnNum;
	}

	public void setColumnNum(String columnNum)
	{
		this.columnNum = columnNum;
	}

	public int getContentType()
	{
		return contentType;
	}

	public void setContentType(int contentType)
	{
		this.contentType = contentType;
	}

}
