package com.v2gogo.project.views.logic;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.v2gogo.project.R;
import com.v2gogo.project.views.webview.JsWebview;
import com.v2gogo.project.views.webview.JsWebview.IonPageLoadFinishedCallback;

@SuppressWarnings("deprecation")
public class VoteOptionDescriptionLayout extends LinearLayout implements IonPageLoadFinishedCallback, OnClickListener
{

	private JsWebview mWebview;
	private ImageView mArrow;
	private RelativeLayout mLayout;

	private AbsoluteLayout mAbsoluteLayout;
	private IonPageLoadFinished mIonPageLoadFinished;
	private boolean mIsFirst = true;

	public VoteOptionDescriptionLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(context);
	}

	public VoteOptionDescriptionLayout(Context context)
	{
		super(context);
		init(context);
	}

	@Override
	public void onPageLoadFinished()
	{
		if (null != mIonPageLoadFinished)
		{
			mIonPageLoadFinished.onPageLoadFinished();
		}
	}

	public void setOnPageLoadFinished(IonPageLoadFinished mIonPageLoadFinished)
	{
		this.mIonPageLoadFinished = mIonPageLoadFinished;
	}

	/**
	 * 设置html数据
	 * 
	 * @param url
	 */
	public void setHtmlDatas(String datas)
	{
		mWebview.setHtmlDatas(datas);
	}

	/**
	 * 初始化
	 */
	private void init(Context context)
	{
		View view = LayoutInflater.from(context).inflate(R.layout.vote_option_description_layout, null);
		initViews(view);
		this.addView(view);
	}

	private void initViews(View view)
	{
		mArrow = (ImageView) view.findViewById(R.id.vote_option_description_layout_arrow);
		mLayout = (RelativeLayout) view.findViewById(R.id.vote_option_description_layout_click);
		mLayout.setClickable(true);
		mWebview = (JsWebview) view.findViewById(R.id.vote_option_description_layout_webview);
		mAbsoluteLayout = (AbsoluteLayout) view.findViewById(R.id.vote_option_description_layout_webview_container);
		mWebview.setVisibility(View.GONE);
		mWebview.setOnPageLoadFinishedCallback(this);
		mLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View view)
	{
		Animation animation = null;
		if (mWebview.getVisibility() == View.GONE)
		{
			animation = new RotateAnimation(0, 90, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			if (mIsFirst)
			{
				mWebview.setVisibility(View.VISIBLE);
				mWebview.setVisibility(View.VISIBLE);
			}
			else
			{
				mWebview.setVisibility(View.VISIBLE);
			}
			mIsFirst = false;
		}
		else
		{
			animation = new RotateAnimation(90, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			mWebview.setVisibility(View.GONE);
		}
		if (null != animation)
		{
			animation.setFillAfter(true);
			animation.setDuration(350);
			mArrow.startAnimation(animation);
		}
	}

	public void hide()
	{
		mWebview.setVisibility(View.GONE);
	}

	public interface IonPageLoadFinished
	{
		public void onPageLoadFinished();
	}

	public void onPause()
	{
		if (null != mWebview)
		{
			mWebview.pauseWebview();
		}
	}

	public void onResume()
	{
		if (null != mWebview)
		{
			mWebview.resumeWebview();
		}
	}

	public void destory()
	{
		if (null != mWebview)
		{
			mAbsoluteLayout.removeView(mWebview);
			mWebview.destoryWebview();
		}
	}
}
