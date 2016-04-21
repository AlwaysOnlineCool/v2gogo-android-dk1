package com.v2gogo.project.domain.home;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * 评论列表类
 * 
 * @author houjun
 */
public class CommentListInfo implements Serializable
{

	private static final long serialVersionUID = 7879848076731729673L;

	@SerializedName("result")
	private String mResult;

	@SerializedName("list")
	private List<CommentInfo> mCommentInfos;

	@SerializedName("page")
	private int mCurrentPage;

	@SerializedName("count")
	private int mCount;

	@SerializedName("sort")
	private String mType;

	public String getResult()
	{
		return mResult;
	}

	public void setResult(String mResult)
	{
		this.mResult = mResult;
	}

	public List<CommentInfo> getCommentInfos()
	{
		return mCommentInfos;
	}

	public void setCommentInfos(List<CommentInfo> mCommentInfos)
	{
		this.mCommentInfos = mCommentInfos;
	}

	public int getCurrentPage()
	{
		return mCurrentPage;
	}

	public void setCurrentPage(int mCurrentPage)
	{
		this.mCurrentPage = mCurrentPage;
	}

	public int getCount()
	{
		return mCount;
	}

	public void setCount(int mCount)
	{
		this.mCount = mCount;
	}

	public String getType()
	{
		return mType;
	}

	public void setType(String mType)
	{
		this.mType = mType;
	}

	/**
	 * 清除数据
	 */
	public void clear()
	{
		if (null != mCommentInfos && 0 != mCommentInfos.size())
		{
			mCommentInfos.clear();
		}
	}

	/**
	 * 添加数据
	 * 
	 * @param commentListInfo
	 */
	public void addAll(CommentListInfo commentListInfo)
	{
		if (null != commentListInfo)
		{
			if (null == mCommentInfos)
			{
				mCommentInfos = new ArrayList<CommentInfo>();
			}
			if (null != commentListInfo.getCommentInfos())
			{
				mCommentInfos.addAll(commentListInfo.getCommentInfos());
			}
		}
	}

	/**
	 * 添加
	 */
	public void addTop(CommentInfo commentInfo)
	{
		if (null == mCommentInfos)
		{
			mCommentInfos = new ArrayList<CommentInfo>();
		}
		mCommentInfos.add(0, commentInfo);
	}

	@Override
	public String toString()
	{
		return "CommentListInfo [mResult=" + mResult + ", mCommentInfos=" + mCommentInfos + ", mCurrentPage=" + mCurrentPage + ", mCount=" + mCount
				+ ", mType=" + mType + "]";
	}
}
