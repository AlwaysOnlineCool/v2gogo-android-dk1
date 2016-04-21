/**    
 * @{#} PullRefreshProgressWebview.java Create on 2016-1-7 下午1:09:07    
 *    
 * Copyright (c) 2013 by BlueSky.    
 *
 *    
 * @author <a href="1084986314@qq.com">BlueSky</a>   
 * @version 1.0    
 */
package com.v2gogo.project.views.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.v2gogo.project.views.listview.refreshview.PullRefreshHeader;

/**
 * 功能：
 * 
 * @ahthor：黄荣星
 * @date:2016-1-7
 * @version::V1.0
 */
public class PullRefreshProgressWebview extends LinearLayout
{
	private PullRefreshHeader mRefreshHeaderView;
	private ProgressWebView mProgressWebView;

	@SuppressLint("NewApi")
	public PullRefreshProgressWebview(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initView();
	}

	public PullRefreshProgressWebview(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initView();
	}

	public PullRefreshProgressWebview(Context context)
	{
		super(context);
		initView();
	}

	/**
	 * method desc：
	 */
	private void initView()
	{
		mRefreshHeaderView = new PullRefreshHeader(getContext());
		mProgressWebView = new ProgressWebView(getContext());
		addView(mProgressWebView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		addView(mRefreshHeaderView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		mRefreshHeaderView.getContainer().setVisibility(View.VISIBLE);

	}

	public ProgressWebView getmProgressWebView()
	{
		return mProgressWebView;
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		Log.e("hrx", "ACTION_DOWN-->deltaly:" + event.getY());
		float Y = 0;
		float deltalY = 0;
		switch (event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				Y = event.getY();
				Log.e("hrx", "ACTION_DOWN-->deltaly:" + deltalY);
				break;
			case MotionEvent.ACTION_MOVE:
				deltalY = event.getY() - Y;
				Log.e("hrx", "ACTION_MOVE-->deltaly:" + deltalY);
				break;
			case MotionEvent.ACTION_UP:
				deltalY = event.getY() - Y;
				Log.e("hrx", "ACTION_UP-->deltaly:" + deltalY);

				break;
			default:
				break;
		}
		return super.onTouchEvent(event);
	}

}
