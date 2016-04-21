package com.v2gogo.project.views.listview.refreshview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.utils.common.DensityUtil;

/**
 * listview头部刷新
 * 
 * @author houjun
 */
public class PullRefreshHeader extends LinearLayout
{

	public static final int STATE_INIT = 0;
	public static final int STATE_RELEASE_REFRESH = 1;

	public static final int STATE_REFRESHING = 2;

	private final int ROTATE_ANIM_DURATION = 180;

	private ImageView mIcon;
	private ProgressBar mProgressBar;
	private TextView mTextView;
	private View mContainer;

	private Animation mRotateUpAnim;
	private Animation mRotateDownAnim;

	private int mState = STATE_INIT;

	public PullRefreshHeader(Context context)
	{
		super(context);
		initView();
	}

	public View getContainer()
	{
		return mContainer;
	}

	@SuppressLint("InflateParams")
	private void initView()
	{
		mContainer = LayoutInflater.from(getContext()).inflate(R.layout.refresh_listview_header, null);
		mTextView = (TextView) mContainer.findViewById(R.id.refresh_header_textview);
		mProgressBar = (ProgressBar) mContainer.findViewById(R.id.refresh_header_progressbar);
		mIcon = (ImageView) mContainer.findViewById(R.id.refresh_header_icon);

		setGravity(Gravity.BOTTOM);

		this.addView(mContainer, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));

		mRotateUpAnim = new RotateAnimation(0.0f, -180.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateUpAnim.setFillAfter(true);
		mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateDownAnim.setFillAfter(true);
	}

	public void setState(int state)
	{
		if (state == mState)
			return;

		if (state == STATE_REFRESHING)
		{
			mIcon.clearAnimation();
			mIcon.setVisibility(View.INVISIBLE);
			mProgressBar.setVisibility(View.VISIBLE);
		}
		else
		{
			mIcon.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.INVISIBLE);
		}

		switch (state)
		{
			case STATE_INIT:
				if (mState == STATE_INIT)
				{
					mIcon.startAnimation(mRotateDownAnim);
				}
				if (mState == STATE_REFRESHING)
				{
					mIcon.clearAnimation();
				}
				mTextView.setText(R.string.refresh_pull_to_refresh);
				break;
			case STATE_RELEASE_REFRESH:
				if (mState != STATE_RELEASE_REFRESH)
				{
					mIcon.clearAnimation();
					mIcon.startAnimation(mRotateUpAnim);
					mTextView.setText(R.string.refresh_release_to_refresh);
				}
				break;
			case STATE_REFRESHING:
				mTextView.setText(R.string.refresh_refreshing);
				break;
			default:
		}

		mState = state;
	}

	public void setVisiableHeight(int height)
	{
		if (height < 0)
			height = 0;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContainer.getLayoutParams();
		lp.height = height;
		mContainer.setLayoutParams(lp);
	}

	public int getContentHeight()
	{
		return DensityUtil.dp2px(getContext(), 60);
	}

	public int getVisiableHeight()
	{
		return mContainer.getHeight();
	}
}
