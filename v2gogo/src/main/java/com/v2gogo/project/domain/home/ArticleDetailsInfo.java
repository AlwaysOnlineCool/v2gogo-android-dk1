package com.v2gogo.project.domain.home;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * 文章详情实体类
 * 
 * @author houjun
 */
public class ArticleDetailsInfo implements Serializable
{

	private static final long serialVersionUID = 6992592000448955709L;

	@SerializedName("result")
	private String mResult;

	@SerializedName("info")
	private ArticeInfo mArticeInfo;

	@SerializedName("hotcomment")
	private List<CommentInfo> mHotCommentInfos;

	@SerializedName("newcomment")
	private List<CommentInfo> mNewestCommentInfos;

	public String getResult()
	{
		return mResult;
	}

	public void setResult(String mResult)
	{
		this.mResult = mResult;
	}

	public ArticeInfo getArticeInfo()
	{
		return mArticeInfo;
	}

	public void setArticeInfo(ArticeInfo mArticeInfo)
	{
		this.mArticeInfo = mArticeInfo;
	}

	public List<CommentInfo> getHotCommentInfos()
	{
		return mHotCommentInfos;
	}

	public void setHotCommentInfos(List<CommentInfo> mHotCommentInfos)
	{
		this.mHotCommentInfos = mHotCommentInfos;
	}

	public List<CommentInfo> getNewestCommentInfos()
	{
		return mNewestCommentInfos;
	}

	public void setNewestCommentInfos(List<CommentInfo> mNewestCommentInfos)
	{
		this.mNewestCommentInfos = mNewestCommentInfos;
	}

}
