package com.v2gogo.project.activity.home.v2gogo;



import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnInfoListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.MediaController.OnHiddenListener;
import io.vov.vitamio.widget.MediaController.OnShownListener;
import io.vov.vitamio.widget.VideoView;
import android.app.Activity;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Process;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.v2gogo.project.R;

/**
 * 视频播放
 * 
 * @author houjun
 */
public class VedioPlayActivity extends Activity implements OnInfoListener, OnBufferingUpdateListener, OnClickListener, OnCompletionListener,
		OnPreparedListener, OnShownListener, OnHiddenListener
{

	public static final String TITLE = "title";
	public static final String ENABLE = "enable";
	public static final String VIDEO_PATH = "video_path";

	private String mPath;
	private boolean mEnable;

	private TextView mTvLoad;
	private TextView mTvTitle;
	private TextView mTvRate;
	private VideoView mVideoView;

	private ImageButton mIbtnBack;
	private ProgressBar mProgressBar;
	private RelativeLayout mActionBar;

	private MediaController mMediaController;

	@Override
	public void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		if (!LibsChecker.checkVitamioLibs(this))
		{
			return;
		}
		setContentView(R.layout.video_play_activity_layout);
		initViews();
		initVideoView();
		registerListener();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		if (mVideoView != null)
			mVideoView.pause();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		if (mVideoView != null)
			mVideoView.resume();
	}

	@Override
	protected void onDestroy()
	{
		Process.killProcess(Process.myPid());
		super.onDestroy();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		if (mVideoView != null)
			mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_STRETCH, 0);
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void finish()
	{
		if (mVideoView != null)
		{
			mVideoView.stopPlayback();
		}
		super.finish();
	}

	@Override
	public void onCompletion(MediaPlayer arg0)
	{
		if (mEnable)
		{
			finish();
		}
	}

	
	@Override
	public boolean onInfo(MediaPlayer player, int whatInfo, int extra)
	{
		switch (whatInfo)
		{
			case MediaPlayer.MEDIA_INFO_BUFFERING_START:
				if (isPlaying())
				{
					stopPlayer();
				}
				mTvLoad.setText("");
				mTvLoad.setVisibility(View.VISIBLE);
				mProgressBar.setVisibility(View.VISIBLE);
				break;

			case MediaPlayer.MEDIA_INFO_BUFFERING_END:
				startPlayer();
				mTvLoad.setVisibility(View.GONE);
				mProgressBar.setVisibility(View.GONE);
				break;

			case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
				mMediaController.setEnabled(false);
				break;

			case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
				mTvRate.setText(extra + " K/S");
				break;

		}
		return true;
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mediaPlayer, int update)
	{
		mTvLoad.setText(update + "%");
	}

	@Override
	public void onPrepared(MediaPlayer arg0)
	{
		arg0.setPlaybackSpeed(1.0f);
		mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_STRETCH, 0);
	}

	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.common_app_action_bar_back:
				finish();
				break;
		}
	}

	@Override
	public void onShown()
	{
		mActionBar.setVisibility(View.VISIBLE);
	}

	@Override
	public void onHidden()
	{
		mActionBar.setVisibility(View.GONE);
	}

	private void initViews()
	{
		mTvTitle = (TextView) findViewById(R.id.video_play_activity_title);
		mTvLoad = (TextView) findViewById(R.id.video_play_activity_load_tip);
		mIbtnBack = (ImageButton) findViewById(R.id.common_app_action_bar_back);
		mVideoView = (VideoView) findViewById(R.id.video_play_activity_videoview);
		mTvRate = (TextView) findViewById(R.id.video_play_activity_download_rate);
		mActionBar = (RelativeLayout) findViewById(R.id.video_play_activity_action_bar);
		mProgressBar = (ProgressBar) findViewById(R.id.video_play_activity_progressbar);
		mTvTitle.setText(getIntent().getStringExtra(TITLE));
		mProgressBar.setProgress(0);
	}

	/**
	 * 注册监听
	 */
	private void registerListener()
	{
		mIbtnBack.setOnClickListener(this);
		mVideoView.setOnInfoListener(this);
		mVideoView.setOnPreparedListener(this);
		mVideoView.setOnCompletionListener(this);
		mMediaController.setOnShownListener(this);
		mMediaController.setOnHiddenListener(this);
		mVideoView.setOnBufferingUpdateListener(this);
	}

	/**
	 * 初始化VideoView
	 */
	private void initVideoView()
	{
		mVideoView.requestFocus();
		mPath = getIntent().getStringExtra(VIDEO_PATH);
		mEnable = getIntent().getBooleanExtra(ENABLE, false);
		mVideoView.setVideoURI(Uri.parse(mPath));
		mMediaController = new MediaController(this);
		mMediaController.setEnabled(mEnable);
		if (!mEnable)
		{
			mMediaController.setSeekAble(mEnable);
		}
		mVideoView.setMediaController(mMediaController);
	}

	/**
	 * 暂停播放
	 */
	private void stopPlayer()
	{
		if (mVideoView != null)
		{
			mVideoView.pause();
		}
	}

	/**
	 * 开始播放
	 */
	private void startPlayer()
	{
		if (mVideoView != null)
		{
			mVideoView.start();
		}
	}

	/**
	 * 是否正在播放
	 * 
	 * @return
	 */
	private boolean isPlaying()
	{
		return mVideoView != null && mVideoView.isPlaying();
	}

}
