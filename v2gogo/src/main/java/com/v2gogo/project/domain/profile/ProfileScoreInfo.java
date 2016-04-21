package com.v2gogo.project.domain.profile;

import java.io.Serializable;
import java.util.List;

/**
 * 功能：评分实体
 * 
 * @ahthor：黄荣星
 * @date:2015-12-9
 * @version::V1.0
 */
public class ProfileScoreInfo implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String uploadToken;// 上传token
	private List<ProfileInfoItem> list;

	public String getUploadToken()
	{
		return uploadToken;
	}

	public void setUploadToken(String uploadToken)
	{
		this.uploadToken = uploadToken;
	}

	public List<ProfileInfoItem> getList()
	{
		return list;
	}

	public void setList(List<ProfileInfoItem> list)
	{
		this.list = list;
	}

}
