package com.v2gogo.project.domain.profile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * 收藏信息
 * 
 * @author houjun
 */
public class CollectionsListInfo implements Serializable
{
	private static final long serialVersionUID = 6586296269870928260L;

	@SerializedName("page")
	private int mCurrentPage;

	@SerializedName("count")
	private int mCount;

	@SerializedName("list")
	private List<CollectionsInfo> mCollectionsInfos;

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

	public List<CollectionsInfo> getCollectionsInfos()
	{
		return mCollectionsInfos;
	}

	public void setCollectionsInfos(List<CollectionsInfo> mCollectionsInfos)
	{
		this.mCollectionsInfos = mCollectionsInfos;
	}

	/**
	 * 清空数据
	 */
	public void clear()
	{
		if (null != mCollectionsInfos && mCollectionsInfos.size() > 0)
		{
			mCollectionsInfos.clear();
		}
	}

	/**
	 * 添加数据
	 */
	public void addAll(CollectionsListInfo collectionsListInfo)
	{
		if (null != collectionsListInfo)
		{
			if (mCollectionsInfos == null)
			{
				mCollectionsInfos = new ArrayList<CollectionsInfo>();
			}
			if (null != collectionsListInfo.getCollectionsInfos())
			{
				mCollectionsInfos.addAll(collectionsListInfo.getCollectionsInfos());
			}
		}
	}

	@Override
	public String toString()
	{
		return "CollectionsListInfo [mCurrentPage=" + mCurrentPage + ", mCount=" + mCount + ", mCollectionsInfos=" + mCollectionsInfos + "]";
	}
}
