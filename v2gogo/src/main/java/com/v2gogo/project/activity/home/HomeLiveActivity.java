package com.v2gogo.project.activity.home;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsoluteLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.BaseActivity;
import com.v2gogo.project.activity.home.v2gogo.VedioPlayActivity;
import com.v2gogo.project.domain.LiveVideoInfo;
import com.v2gogo.project.main.image.GlideImageLoader;
import com.v2gogo.project.manager.home.HomeLiveManager;
import com.v2gogo.project.manager.home.HomeLiveManager.IonHomeLiveListCallback;
import com.v2gogo.project.utils.common.ScreenUtil;
import com.v2gogo.project.utils.common.ToastUtil;
import com.v2gogo.project.views.ProgressLayout;
import com.v2gogo.project.views.webview.JsWebview;

/**
 * 在线直播界面
 * 
 * @author houjun
 */
@SuppressWarnings("deprecation")
public class HomeLiveActivity extends BaseActivity implements OnClickListener
{

	private AbsoluteLayout mAbsoluteLayout;
	private ProgressLayout mProgressLayout;
	private ImageView mVideoThumb;
	private ImageButton mBtnPlay;
	private JsWebview mWebview;
	private LiveVideoInfo mLiveVideoInfo;

	@Override
	public void clearRequestTask()
	{
		mAbsoluteLayout.removeView(mWebview);
		mWebview.destoryWebview();
	}

	@Override
	public void onInitViews()
	{
		mWebview = (JsWebview) findViewById(R.id.home_live_webview);
		mBtnPlay = (ImageButton) findViewById(R.id.home_live_play_video);
		mVideoThumb = (ImageView) findViewById(R.id.home_live_video_thumb);
		mProgressLayout = (ProgressLayout) findViewById(R.id.home_live_progress_layout);
		mAbsoluteLayout = (AbsoluteLayout) findViewById(R.id.home_live_webview_container);
		int height = (int) (ScreenUtil.getScreenWidth(this) / 2f);
		RelativeLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, height);
		mVideoThumb.setLayoutParams(layoutParams);
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.home_live_activity_layout;
	}

	@Override
	protected void onInitLoadDatas()
	{
		super.onInitLoadDatas();
		mProgressLayout.showProgress();
		HomeLiveManager.getHomeLiveList(new IonHomeLiveListCallback()
		{
			@Override
			public void onHomeLiveSuccess(LiveVideoInfo liveVideoInfo)
			{
				mProgressLayout.showContent();
				if (liveVideoInfo != null)
				{
					mLiveVideoInfo = liveVideoInfo;
					mWebview.setHtmlDatas(liveVideoInfo.getContent());
					GlideImageLoader.loadImageWithFixedSize(HomeLiveActivity.this, liveVideoInfo.getThumbialUrl(), mVideoThumb);
				}
			}

			@Override
			public void onHomeLiveFail(String errorMessage)
			{
				mProgressLayout.showContent();
				ToastUtil.showAlertToast(HomeLiveActivity.this, errorMessage);
			}
		});
	}

	@Override
	protected void registerListener()
	{
		super.registerListener();
		mBtnPlay.setOnClickListener(this);
	}

	@Override
	public void onClick(View view)
	{
		clickPlay();
	}

	/**
	 * 点击播放
	 */
	private void clickPlay()
	{
		if (mLiveVideoInfo != null)
		{
			Intent intent = new Intent(this, VedioPlayActivity.class);
			intent.putExtra(VedioPlayActivity.VIDEO_PATH, mLiveVideoInfo.getUrl());
			intent.putExtra(VedioPlayActivity.TITLE, getString(R.string.home_live_video));
			intent.putExtra(VedioPlayActivity.ENABLE, false);
			startActivity(intent);
		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		mWebview.resumeWebview();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		mWebview.pauseWebview();
	}
}
