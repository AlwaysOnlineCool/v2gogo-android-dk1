package com.v2gogo.project.domain.home;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * 视频实体
 * 
 * @author houjun
 */
public class VedioDatilsInfo implements Serializable
{

	private static final long serialVersionUID = 3652174563881473582L;

	@SerializedName("video")
	private VedioInfo mVedioInfo;

	@SerializedName("hotcomment")
	private List<CommentInfo> mHotCommentInfos;

	@SerializedName("newcomment")
	private List<CommentInfo> mNewestCommentInfos;

	public VedioInfo getVedioInfo()
	{
		return mVedioInfo;
	}

	public void setVedioInfo(VedioInfo mVedioInfo)
	{
		this.mVedioInfo = mVedioInfo;
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

	@Override
	public String toString()
	{
		return "VedioDatilsInfo [mVedioInfo=" + mVedioInfo + ", mHotCommentInfos=" + mHotCommentInfos + ", mNewestCommentInfos=" + mNewestCommentInfos + "]";
	}

}
