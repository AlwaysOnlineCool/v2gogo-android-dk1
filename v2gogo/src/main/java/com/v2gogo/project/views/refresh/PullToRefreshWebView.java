package com.v2gogo.project.views.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.v2gogo.project.R;
import com.v2gogo.project.views.webview.ProgressWebView;
import com.v2gogo.project.views.webview.ProgressWebView.IonRefreshCompleteCallback;

public class PullToRefreshWebView extends PullToRefreshBase<ProgressWebView> implements IonRefreshCompleteCallback
{

	private final OnRefreshListener defaultOnRefreshListener = new OnRefreshListener()
	{

		@Override
		public void onRefresh()
		{
			refreshableView.reload();
		}

	};

	public PullToRefreshWebView(Context context)
	{
		super(context);

		/**
		 * Added so that by default, Pull-to-Refresh refreshes the page
		 */
		setOnRefreshListener(defaultOnRefreshListener);
		refreshableView.setmIonRefreshCompleteCallback(this);
	}

	public PullToRefreshWebView(Context context, int mode)
	{
		super(context, mode);

		/**
		 * Added so that by default, Pull-to-Refresh refreshes the page
		 */
		setOnRefreshListener(defaultOnRefreshListener);
		refreshableView.setmIonRefreshCompleteCallback(this);
	}

	public PullToRefreshWebView(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		/**
		 * Added so that by default, Pull-to-Refresh refreshes the page
		 */
		setOnRefreshListener(defaultOnRefreshListener);
		refreshableView.setmIonRefreshCompleteCallback(this);
	}

	@Override
	protected ProgressWebView createRefreshableView(Context context, AttributeSet attrs)
	{
		ProgressWebView webView = new ProgressWebView(context, attrs);

		webView.setId(R.id.webview);
		return webView;
	}

	@Override
	protected boolean isReadyForPullDown()
	{
		return refreshableView.getScrollY() == 0;
	}

	@Override
	protected boolean isReadyForPullUp()
	{
		return refreshableView.getScrollY() >= (refreshableView.getContentHeight() - refreshableView.getHeight());
	}

	@Override
	public void onProgressChanged(WebView view, int newProgress)
	{
		if (newProgress >= 100)
		{
			onRefreshComplete();
		}
	}
}
