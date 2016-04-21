package com.v2gogo.project.domain.home;

import java.io.Serializable;

/**
 * 评论盖楼实体类
 * 
 * @author houjun
 */
public class CommentReplyInfo implements Serializable
{

	private static final long serialVersionUID = -8431128727542600902L;

	private String mName;
	private String mContent;

	public String getName()
	{
		return mName;
	}

	public void setName(String mName)
	{
		this.mName = mName;
	}

	public String getContent()
	{
		return mContent;
	}

	public void setContent(String mContent)
	{
		this.mContent = mContent;
	}

	@Override
	public String toString()
	{
		return "CommentReplyInfo [mName=" + mName + ", mContent=" + mContent + "]";
	}

}
