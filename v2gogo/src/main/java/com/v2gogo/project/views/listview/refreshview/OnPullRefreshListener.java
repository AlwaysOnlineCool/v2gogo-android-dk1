package com.v2gogo.project.views.listview.refreshview;

import android.widget.AbsListView;

/**
 * 下拉刷新监听器
 * 
 * @author houjun
 */
public interface OnPullRefreshListener
{
	/**
	 * 下拉刷新
	 * 
	 * @param pullRefreshView
	 */
	public void onPullDownRefresh(AbsListView pullRefreshView);
}
