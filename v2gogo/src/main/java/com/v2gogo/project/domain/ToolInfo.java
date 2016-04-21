/**    
 * @{#} ToolInfo.java Create on 2015-12-19 下午9:55:23    
 *    
 * Copyright (c) 2013 by BlueSky.    
 *
 *    
 * @author <a href="1084986314@qq.com">BlueSky</a>   
 * @version 1.0    
 */
package com.v2gogo.project.domain;

import java.io.Serializable;

import com.v2gogo.project.utils.qiniu.VersionPhotoUrlBuilder;

/**
 * 功能：底部导航栏菜单
 * 
 * @ahthor：黄荣星
 * @date:2015-12-19
 * @version::V1.0
 */
public class ToolInfo implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String id;
	private String toolName;
	private String toolBackImg;
	private String toolUrl;
	private String sortNum;
	private int contentType;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getToolName()
	{
		return toolName;
	}

	public void setToolName(String toolName)
	{
		this.toolName = toolName;
	}

	public String getToolBackImg()
	{
		return VersionPhotoUrlBuilder.createHomeBottomToolBar(VersionPhotoUrlBuilder.createVersionImageUrl(toolBackImg));
	}

	public void setToolBackImg(String toolBackImg)
	{
		this.toolBackImg = toolBackImg;
	}

	public String getToolUrl()
	{
		return toolUrl;
	}

	public void setToolUrl(String toolUrl)
	{
		this.toolUrl = toolUrl;
	}

	public String getSortNum()
	{
		return sortNum;
	}

	public void setSortNum(String sortNum)
	{
		this.sortNum = sortNum;
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
