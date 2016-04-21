package com.v2gogo.project.views.webview;

import io.vov.vitamio.utils.Log;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.v2gogo.project.R;
import com.v2gogo.project.utils.common.LogUtil;

/**
 * 有加载进度的webview
 * 
 * @author houjun
 */
public class ProgressWebView extends NoFadeColorWebView
{
	public final static int FILECHOOSER_RESULTCODE = 0x30;

	private ProgressBar mProgressBar;
	private final int progressBarHeight = 5;
	public ValueCallback<Uri> mUploadMessage;
	private IonReceiveTitleCallback mIonReceiveTitleCallback;
	private IonRefreshCompleteCallback mIonRefreshCompleteCallback;

	public ProgressWebView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initPro(context);
		initSettings();
	}

	public ProgressWebView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initPro(context);
		initSettings();
	}

	public ProgressWebView(Context context)
	{
		super(context);
		initPro(context);
		initSettings();
	}

	@SuppressWarnings("deprecation")
	private void initPro(Context context)
	{
		mProgressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
		mProgressBar.setProgress(0);
		mProgressBar.setMax(100);
		mProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.shape_webview_progressbar_color));
		mProgressBar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, progressBarHeight, 0, 0));
		this.addView(mProgressBar);
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initSettings()
	{
		WebSettings webSettings = getSettings();
		webSettings.setAllowFileAccess(true);
		webSettings.setJavaScriptEnabled(true);// 可用JS
		registerImageListenerClick(getContext());
		webSettings.setDefaultTextEncodingName("UTF-8");
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setUseWideViewPort(true);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		webSettings.setLoadsImagesAutomatically(true);
		webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		webSettings.setDomStorageEnabled(false);

		setWebChromeClient();
		setWebView();
	}

	private void setWebChromeClient()
	{
		this.setWebChromeClient(new WebChromeClient()
		{
			@Override
			public void onProgressChanged(WebView view, int newProgress)
			{
				if (newProgress >= 100)
				{
					mProgressBar.setVisibility(View.GONE);
					mProgressBar.setProgress(0);
				}
				else
				{
					if (mProgressBar.getVisibility() == View.GONE)
					{
						mProgressBar.setVisibility(View.VISIBLE);
					}
					mProgressBar.setProgress(newProgress);
				}

				if (mIonRefreshCompleteCallback != null)
				{
					mIonRefreshCompleteCallback.onProgressChanged(view, newProgress);
				}
			}

			@Override
			public void onReceivedTitle(WebView view, String title)
			{
				if (null != mIonReceiveTitleCallback)
				{
					mIonReceiveTitleCallback.onReceiveTitle(title);
				}
			}

			@SuppressWarnings("unused")
			public void openFileChooser(ValueCallback<Uri> uploadMsg)
			{
				if (mUploadMessage != null)
					return;
				mUploadMessage = uploadMsg;
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.addCategory(Intent.CATEGORY_OPENABLE);
				i.setType("*/*");
				if (getContext() instanceof Activity)
				{
					((Activity) (getContext())).startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
				}
			}

			@SuppressWarnings("unused")
			public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType)
			{
				if (mUploadMessage != null)
					return;
				mUploadMessage = uploadMsg;
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.addCategory(Intent.CATEGORY_OPENABLE);
				i.setType("*/*");
				if (getContext() instanceof Activity)
				{
					((Activity) (getContext())).startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
				}
			}

			@SuppressWarnings("unused")
			public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture)
			{
				if (mUploadMessage != null)
					return;
				mUploadMessage = uploadMsg;
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.addCategory(Intent.CATEGORY_OPENABLE);
				i.setType("*/*");
				if (getContext() instanceof Activity)
				{
					((Activity) (getContext())).startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
				}
			}

			@Override
			public boolean onJsAlert(WebView view, String url, String message, JsResult result)
			{
				return super.onJsAlert(view, url, message, result);
			}
		});
	}

	private void setWebView()
	{
		this.setWebViewClient(new WebViewClient()
		{
			@SuppressLint("SetJavaScriptEnabled")
			@Override
			public void onPageFinished(WebView view, String url)
			{
				super.onPageFinished(view, url);
			}

			@SuppressLint("SetJavaScriptEnabled")
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon)
			{
				super.onPageStarted(view, url, favicon);
				view.getSettings().setJavaScriptEnabled(true);
				addUserInfoListner();
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
			{
				super.onReceivedError(view, errorCode, description, failingUrl);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url)
			{
				boolean isSuccess = dealwithurl(url);
				// if (isSuccess)
				// {
				// addUserInfoListner();
				// }
				return isSuccess;
			}

			@Override
			public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error)
			{
				handler.proceed();
			}
		});
	}

	/**
	 * 设置标题数据回调
	 * 
	 * @param mIonReceiveTitleCallback
	 */
	public void setOnReceiveTitleCallback(IonReceiveTitleCallback mIonReceiveTitleCallback)
	{
		this.mIonReceiveTitleCallback = mIonReceiveTitleCallback;
	}

	/**
	 * method desc：设置webview网络加载进度回调
	 * 
	 * @param mIonRefreshCompleteCallback
	 */
	public void setmIonRefreshCompleteCallback(IonRefreshCompleteCallback mIonRefreshCompleteCallback)
	{
		this.mIonRefreshCompleteCallback = mIonRefreshCompleteCallback;
	}

	/**
	 * 返回标题数据回调
	 * 
	 * @author houjun
	 */
	public interface IonReceiveTitleCallback
	{
		public void onReceiveTitle(String title);
	}

	/**
	 * 加载网络数据进度回调方法
	 * 
	 * @author hrx
	 */
	public interface IonRefreshCompleteCallback
	{
		public void onProgressChanged(WebView view, int newProgress);
	}

	/**
	 * 处理url
	 * 
	 * @param url
	 * @return
	 */
	private boolean dealwithurl(String url)
	{
		if (TextUtils.isEmpty(url))
		{
			return false;
		}
		else
		{
			url = url.trim();
			LogUtil.d("houjun", "url->" + url);
			if (url.contains("v2gogo://"))
			{
				try
				{
					Uri uri = Uri.parse(url);
					int type = Integer.parseInt(uri.getQueryParameter("type"));
					String url_target = uri.getQueryParameter("url");
					String srcId = uri.getQueryParameter("srcId");
					jump2ActivityNew(type, srcId, url_target, url);
				}
				catch (Exception e)
				{
					LogUtil.d("houjun", "e->" + e.getLocalizedMessage());
					e.printStackTrace();
				}
			}
			else if (url.contains("tel:"))// 拨打电话
			{
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_CALL);
				intent.setData(Uri.parse(url));
				getContext().startActivity(intent);
			}
			else
			{
				ProgressWebView.this.loadUrl(url);
			}
			return true;
		}
	}
}
