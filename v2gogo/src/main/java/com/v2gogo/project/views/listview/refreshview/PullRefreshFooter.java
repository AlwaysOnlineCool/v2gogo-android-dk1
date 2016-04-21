package com.v2gogo.project.views.listview.refreshview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.v2gogo.project.R;

/**
 * 加载更多布局
 * 
 * @author houjun
 */
@SuppressLint("InflateParams")
public class PullRefreshFooter extends FrameLayout
{
	private boolean isEnable = false;
	private View mFooter;

	public PullRefreshFooter(Context context)
	{
		super(context);
		mFooter = LayoutInflater.from(getContext()).inflate(R.layout.refresh_listview_footer, null);
		addView(mFooter);
		setEnabledLoadMore(isEnable);
	}

	public boolean isEnabledLoadMore()
	{
		return isEnable;
	}

	public void setEnabledLoadMore(boolean isEnable)
	{
		this.isEnable = isEnable;
		mFooter.setVisibility(isEnable ? View.VISIBLE : View.GONE);
	}
}
