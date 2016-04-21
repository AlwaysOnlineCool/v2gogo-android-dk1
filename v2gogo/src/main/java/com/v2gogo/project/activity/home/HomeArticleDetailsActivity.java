package com.v2gogo.project.activity.home;

import java.io.File;
import java.io.FileOutputStream;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.widget.AbsoluteLayout;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.home.theme.UploadPictureActivity;
import com.v2gogo.project.domain.ShareInfo;
import com.v2gogo.project.domain.home.ArticeInfo;
import com.v2gogo.project.domain.home.ArticleDetailsInfo;
import com.v2gogo.project.domain.home.CommentInfo;
import com.v2gogo.project.domain.home.theme.ThemePhotoUploadResultInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.manager.home.ArticeManager;
import com.v2gogo.project.manager.home.ArticeManager.IonArticeDetailsCallback;
import com.v2gogo.project.manager.home.CommentManager;
import com.v2gogo.project.manager.upload.ThemePhotoUploadManager;
import com.v2gogo.project.utils.common.PhotoUtil;
import com.v2gogo.project.utils.common.ToastUtil;
import com.v2gogo.project.utils.http.IntentExtraConstants;
import com.v2gogo.project.views.webview.JsWebview;
import com.v2gogo.project.views.webview.JsWebview.IonPageLoadFinishedCallback;

/*
 * 文章详情
 */
public class HomeArticleDetailsActivity extends BaseDetailsctivity implements IonPageLoadFinishedCallback
{

	private JsWebview mJsWebview;
	private AbsoluteLayout mAbsoluteLayout;
	private ArticleDetailsInfo mArticleDetailsInfo;

	private WebChromeClient chromeClient = null;
	private WebChromeClient.CustomViewCallback myCallBack = null;

	@Override
	protected void registerListener()
	{
		super.registerListener();
		mJsWebview.setOnPageLoadFinishedCallback(this);
	}

	/**
	 * 显示文章详情数据
	 * 
	 * @param articleDetailsInfo
	 */
	private void displayArticeDetailsDatas(ArticleDetailsInfo articleDetailsInfo)
	{
		if (null != articleDetailsInfo && null != articleDetailsInfo.getArticeInfo())
		{
			mArticleDetailsInfo = articleDetailsInfo;
			mJsWebview.setHtmlDatas(mArticleDetailsInfo.getArticeInfo().getContent());

//			mJsWebview.getSettings().setJavaScriptEnabled(true);
//			mJsWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//
//			mJsWebview.setWebViewClient(new MyWebviewCient());
//
//			chromeClient = new MyChromeClient();
//
//			mJsWebview.setWebChromeClient(chromeClient);
//			mJsWebview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//			mJsWebview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
//
//			mJsWebview.setHorizontalScrollBarEnabled(false);
//			mJsWebview.setVerticalScrollBarEnabled(false);
//
//			final String USER_AGENT_STRING = mJsWebview.getSettings().getUserAgentString() + " Rong/2.0";
//			mJsWebview.getSettings().setUserAgentString(USER_AGENT_STRING);
//			mJsWebview.getSettings().setSupportZoom(false);
//			mJsWebview.getSettings().setPluginState(WebSettings.PluginState.ON);
//			mJsWebview.getSettings().setLoadWithOverviewMode(true);

			mTotalComment = articleDetailsInfo.getArticeInfo().getCommentNum();
			mBottomOparationLayout.setCommentNum(mTotalComment);
			if (articleDetailsInfo.getArticeInfo().getIsCom() == 1)// 正常显示
			{
				mBottomOparationLayout.setCommentClickable(View.VISIBLE);
			}
			else
			{
				mBottomOparationLayout.setCommentUnClickable(View.INVISIBLE);// 显示“评论”及“查看评论”按钮为灰色。
			}
		}
	}

	@SuppressLint("NewApi")
	public class MyWebviewCient extends WebViewClient
	{
		@Override
		public WebResourceResponse shouldInterceptRequest(WebView view, String url)
		{
			WebResourceResponse response = null;
			response = super.shouldInterceptRequest(view, url);
			return response;
		}
	}

	public class MyChromeClient extends WebChromeClient
	{

		@Override
		public void onShowCustomView(View view, CustomViewCallback callback)
		{
			Log.d("ddd", "onShowCustomView");

		}

		@Override
		public void onHideCustomView()
		{
			Log.d("ddd", "onHideCustomView");
		}

		@Override
		public boolean onConsoleMessage(ConsoleMessage consoleMessage)
		{
			// TODO Auto-generated method stub
			Log.d("ZR", consoleMessage.message() + " at " + consoleMessage.sourceId() + ":" + consoleMessage.lineNumber());
			return super.onConsoleMessage(consoleMessage);
		}
	}

	@Override
	public void onPageLoadFinished()
	{
		loadFirstPageComments();
	}

	@Override
	public void clearRequestTask()
	{
		CommentManager.clearGetCommentListTask();
		ArticeManager.clearGetArticeDetailsTask();
		mAbsoluteLayout.removeView(mJsWebview);
		mJsWebview.destoryWebview();
	}

	@Override
	protected boolean isNotifyIntent()
	{
		return true;
	}

	@Override
	public View getHeaderView()
	{
		View view = LayoutInflater.from(this).inflate(R.layout.home_artice_deatils_activity_header_layout, null);
		mAbsoluteLayout = (AbsoluteLayout) view.findViewById(R.id.topic_details_activity_header_webview_container);
		mJsWebview = (JsWebview) view.findViewById(R.id.topic_details_activity_header_webview);
		return view;
	}

	@Override
	public boolean isPublishImage()
	{
		if (null != mArticleDetailsInfo && null != mArticleDetailsInfo.getArticeInfo())
		{
			return mArticleDetailsInfo.getArticeInfo().getIscomUpload() == 1 ? true : false;
		}
		return false;
	}

	@Override
	public ShareInfo getShareInfo()
	{
		if (null != mArticleDetailsInfo && null != mArticleDetailsInfo.getArticeInfo())
		{
			ArticeInfo articeInfo = mArticleDetailsInfo.getArticeInfo();
			ShareInfo shareInfo = new ShareInfo();
			shareInfo.setTitle(articeInfo.getTitle());
			shareInfo.setDescription(articeInfo.getDescription());
			shareInfo.setImageUrl(articeInfo.getThumb());
			shareInfo.setHref(ServerUrlConfig.SERVER_URL + articeInfo.getHref());
			shareInfo.setTargedId(articeInfo.getId());
			return shareInfo;
		}
		return null;
	}

	@Override
	public void loadNetData()
	{
		ArticeManager.getArticeDetails(mId, new IonArticeDetailsCallback()
		{
			@Override
			public void onArticeDetailsSuccess(ArticleDetailsInfo articleDetailsInfo)
			{
				mProgressLayout.showContent();
				displayArticeDetailsDatas(articleDetailsInfo);
			}

			@Override
			public void onArticeDetailsFail(String errorString)
			{
				mProgressLayout.showContent();
				ToastUtil.showAlertToast(HomeArticleDetailsActivity.this, errorString);
			}
		});
	}

	@Override
	public int getSrcType()
	{
		return CommentInfo.SRC_ARTICE_TYPE;
	}

	@Override
	public int getType()
	{
		return CommentInfo.COMMENT_VEDIO;
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		mJsWebview.resumeWebview();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		mJsWebview.pauseWebview();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		mJsWebview.destoryWebview();
	}

	/**
	 * 得到相册图片的路径
	 * 
	 * @param albumPath
	 */
	@Override
	protected void getAlbumPath(String albumPath)
	{
		super.getAlbumPath(albumPath);
		if (null != albumPath)
		{
			PhotoUtil.forward2Crop(this, Uri.fromFile(new File(albumPath)));
		}
	}

	/**
	 * 得到拍照的图片
	 * 
	 * @param CameraPath
	 */
	@Override
	protected void getCameraPath(String cameraPath)
	{
		super.getCameraPath(cameraPath);
		if (cameraPath != null)
		{
			PhotoUtil.cameraCropImageUri(this, Uri.fromFile(new File(cameraPath)));
		}
	}

	/**
	 * 得到图片压缩后的路径
	 * 
	 * @param albumPath
	 */
	@Override
	protected void getCompressPath(Bitmap bitmap)
	{
		super.getCompressPath(bitmap);
		if (null != bitmap)
		{
			try
			{
				boolean result = bitmap.compress(CompressFormat.JPEG, 100, new FileOutputStream(ThemePhotoUploadManager.FILE_PATH));
				if (!bitmap.isRecycled())
				{
					bitmap.recycle();
				}
				if (result)
				{
					forward2Upload(ThemePhotoUploadManager.FILE_PATH);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * 接收上传图片成功的消息
	 * 
	 * @param progress
	 */
	public void onEventMainThread(ThemePhotoUploadResultInfo photoUploadResultInfo)
	{
		if (null != photoUploadResultInfo && photoUploadResultInfo.getmThemePhotoInfo() != null)
		{
			String imgPathStr = photoUploadResultInfo.getmThemePhotoInfo().getPhotoImg();
			String userName = V2GogoApplication.getCurrentMatser().getUsername();
			// mJsWebview.loadUrl("javascript:replaceImgSrc('" + imgPathStr + "');");
			mJsWebview.loadUrl("javascript:replaceImgSrc('" + imgPathStr + "','" + userName + "');");
		}
	}

	/**
	 * 跳转到上传
	 * 
	 * @param resultPhotoPath
	 */
	private void forward2Upload(String resultPhotoPath)
	{
		Intent intent = new Intent(this, UploadPictureActivity.class);
		intent.putExtra(IntentExtraConstants.PATH, resultPhotoPath);
		intent.putExtra(IntentExtraConstants.TID, mJsWebview.getmTid());
		// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

}
