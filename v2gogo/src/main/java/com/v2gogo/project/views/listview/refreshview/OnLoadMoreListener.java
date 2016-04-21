package com.v2gogo.project.views.listview.refreshview;

import android.widget.AbsListView;

/**
 * 加载更多监听器
 * 
 * @author houjun
 */
public interface OnLoadMoreListener
{
	/**
	 * 加载更多
	 * 
	 * @param pullRefreshView
	 */
	public void onLoadMore(AbsListView pullRefreshView);
}
