package com.v2gogo.project.views.webview;

import java.net.URI;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
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
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.WebViewActivity;
import com.v2gogo.project.manager.config.ServerUrlConfig;

/**
 * js交互的webview
 * 
 * @author houjun
 */
@SuppressLint("SetJavaScriptEnabled")
public class JsWebview extends NoFadeColorWebView
{

	private WebSettings mWebSettings;

	private IonPageLoadFinishedCallback mIonPageLoadFinishedCallback;

	public JsWebview(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init(context);
	}

	public JsWebview(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(context);
	}

	public JsWebview(Context context)
	{
		super(context.getApplicationContext());
		init(context);
	}

	public void setOnPageLoadFinishedCallback(IonPageLoadFinishedCallback mIonPageLoadFinishedCallback)
	{
		this.mIonPageLoadFinishedCallback = mIonPageLoadFinishedCallback;
	}

	/**
	 * 设置html数据
	 * 
	 * @param datas
	 */
	public void setHtmlDatas(String datas)
	{
		try
		{
			this.loadDataWithBaseURL(ServerUrlConfig.SERVER_URL, datas, "text/html", "utf-8", null);
		}
		catch (Exception exception)
		{
		}
	}

	/**
	 * 初始化
	 */
	private void init(Context context)
	{
		this.setBackgroundColor(context.getResources().getColor(R.color.window_background_color));
		mWebSettings = getSettings();
		this.setWebViewClient(new CustomWebViewClient());
		this.setWebChromeClient(new MyWebChromeClient());
		mWebSettings.setJavaScriptEnabled(true);
		registerImageListenerClick(getContext());
		mWebSettings.setBuiltInZoomControls(false);
		mWebSettings.setSupportZoom(false);
		mWebSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		mWebSettings.setDefaultTextEncodingName("utf-8");
		this.setScrollContainer(false);
	}

	/**
	 * 注入js函数监听
	 */
	public void addImageClickListner()
	{
		this.loadUrl("javascript:(function(){"
				+ "var objs = document.getElementsByTagName(\"img\"); "
				+ "for(var i=0;i<objs.length;i++)  "
				+ "{"
				+ "window.AndroidJSListener.getImages(objs[i].src);"
				+ "    objs[i].onclick=function()  "
				+ "    {currentObj = this;  "
				+ "if(this!==undefined&&this.className.indexOf('UploadImage')>-1){window.AndroidJSListener.UploadImageTo7NiuServer(document.getElementById('topicId').value);}else{window.AndroidJSListener.openImage(this.src,this.attributes.href!=null?this.attributes.href.nodeValue:'');}"
				+ "}}})()");
	}

	private class CustomWebViewClient extends WebViewClient
	{
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url)
		{
			return dealWithUrl(view, url);
			// return super.shouldOverrideUrlLoading(view, url);
		}

		@Override
		public void onPageFinished(WebView view, String url)
		{
			view.getSettings().setJavaScriptEnabled(true);
			super.onPageFinished(view, url);
			addImageClickListner();
			if (null != mIonPageLoadFinishedCallback)
			{
				mIonPageLoadFinishedCallback.onPageLoadFinished();
			}
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon)
		{
			view.getSettings().setJavaScriptEnabled(true);
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
		{
			super.onReceivedError(view, errorCode, description, failingUrl);
		}

		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error)
		{
			handler.proceed();
		}
	}

	private class MyWebChromeClient extends WebChromeClient
	{
		@Override
		public boolean onJsAlert(WebView view, String url, String message, JsResult result)
		{
			return super.onJsAlert(view, url, message, result);
		}
	}

	/**
	 * webview页面加载完成回调
	 * 
	 * @author
	 */
	public interface IonPageLoadFinishedCallback
	{
		public void onPageLoadFinished();
	}

	/**
	 * 处理url
	 * 
	 * @return
	 */
	private boolean dealWithUrl(WebView view, String url)
	{
		if (TextUtils.isEmpty(url))
		{
			return false;
		}
		else
		{
			url = url.trim();
			if (url.contains("v2gogo://"))
			{
				try
				{
					Uri uri = Uri.parse(url);
					int type = Integer.parseInt(uri.getQueryParameter("type"));
					String srcId = uri.getQueryParameter("srcId");
					String url_target = uri.getQueryParameter("url");
					jump2ActivityNew(type, srcId, url_target, url);
				}
				catch (Exception e)
				{
				}
			}
			else
			{
				if (url.startsWith("http") || url.startsWith("https"))
				{
					Intent intent = new Intent(getContext(), WebViewActivity.class);
					intent.putExtra(WebViewActivity.URL, url);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					getContext().startActivity(intent);
				}
			}
			return true;
		}
	}
}
