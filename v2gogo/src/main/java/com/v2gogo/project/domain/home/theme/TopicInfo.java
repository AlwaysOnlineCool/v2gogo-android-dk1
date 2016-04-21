package com.v2gogo.project.domain.home.theme;

import java.io.Serializable;

public class TopicInfo implements Serializable
{

	private static final long serialVersionUID = -3763506436188111026L;

	public static final int TOPIC_STATUS_NORMAL =0;//该主题才新增
	public static final int TOPIC_STATUS_YET_PUBLISHED = 1;//该主题发布
	public static final int TOPIC_STATUS_YET_END =2;//该主题已经结束
	
	private String shareAddress;
	private String tTitle;
	private String intro;
	private int status;

	private String policy;

	private int comments;
	private boolean audit;

	public String getShareAddress()
	{
		return shareAddress;
	}

	public void setShareAddress(String shareAddress)
	{
		this.shareAddress = shareAddress;
	}

	public String gettTitle()
	{
		return tTitle;
	}

	public void settTitle(String tTitle)
	{
		this.tTitle = tTitle;
	}

	public String getPolicy()
	{
		return policy;
	}

	public void setPolicy(String policy)
	{
		this.policy = policy;
	}

	public boolean isAudit()
	{
		return audit;
	}

	public void setAudit(boolean audit)
	{
		this.audit = audit;
	}

	public int getComments()
	{
		return comments;
	}

	public void setComments(int comments)
	{
		this.comments = comments;
	}

	public String getIntro()
	{
		return intro;
	}

	public void setIntro(String intro)
	{
		this.intro = intro;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	@Override
	public String toString()
	{
		return "TopicInfo [shareAddress=" + shareAddress + ", tTitle=" + tTitle + ", intro=" + intro + ", status=" + status + ", policy=" + policy
				+ ", comments=" + comments + ", audit=" + audit + "]";
	}

}
