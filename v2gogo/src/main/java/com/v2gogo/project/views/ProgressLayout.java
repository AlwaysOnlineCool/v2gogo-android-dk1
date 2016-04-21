package com.v2gogo.project.views;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.v2gogo.project.R;

/**
 * 进度layout
 * 
 * @author houjun
 */
public class ProgressLayout extends RelativeLayout
{
	private static final String TAG_PROGRESS = "progresslayout.tag_progress";
	private static final String TAG_ERROR = "progresslayout.tag_error";

	private ProgressBar mProgressView;
	private TextView mErrorTextView;
	private List<View> mContentViews = new ArrayList<View>();

	private State mState = State.CONTENT;
	private IonRetryLoadDatasCallback mLoadDatasCallback;

	public ProgressLayout(Context context)
	{
		super(context);
		init(null);
	}

	public ProgressLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(attrs);
	}

	public ProgressLayout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init(attrs);
	}

	private void init(AttributeSet attrs)
	{
		initProgressView();
		initErrorTextView();
		registerListener();
	}

	/**
	 * 注册监听
	 */
	private void registerListener()
	{
		this.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if (mState == State.ERROR)
				{
					if (mLoadDatasCallback != null)
					{
						showProgress();
						mLoadDatasCallback.onRetryLoadDatas();
					}
				}
			}
		});
	}

	/**
	 * 设置重新加载的数据回调
	 * 
	 * @param mLoadDatasCallback
	 */
	public void setOnTryLoadDatasCallback(IonRetryLoadDatasCallback mLoadDatasCallback)
	{
		this.mLoadDatasCallback = mLoadDatasCallback;
	}

	/**
	 * 加载出错视图
	 */
	private void initErrorTextView()
	{
		Drawable drawable = getResources().getDrawable(R.drawable.iconfont_wangluo);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		mErrorTextView = new TextView(getContext());
		mErrorTextView.setTag(TAG_ERROR);
		mErrorTextView.setCompoundDrawablePadding(20);
		mErrorTextView.setGravity(Gravity.CENTER_HORIZONTAL);
		mErrorTextView.setCompoundDrawables(null, drawable, null, null);
		mErrorTextView.setTextSize(16f);
		mErrorTextView.setTextColor(0xff696969);
		LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(CENTER_IN_PARENT);
		addView(mErrorTextView, layoutParams);
		mErrorTextView.setVisibility(View.GONE);
	}

	/**
	 * 初始化加载视图
	 * 
	 * @param backgroundColor
	 */
	private void initProgressView()
	{
		mProgressView = new ProgressBar(getContext());
		mProgressView.setIndeterminateDrawable(getContext().getResources().getDrawable(R.drawable.anim_loading_data_drawable));
		mProgressView.setIndeterminate(true);
		LayoutParams layoutParams = new LayoutParams(100, 100);
		layoutParams.addRule(CENTER_IN_PARENT);
		mProgressView.setTag(TAG_PROGRESS);
		addView(mProgressView, layoutParams);
		mProgressView.clearAnimation();
		mProgressView.setVisibility(View.GONE);
	}

	@Override
	public void addView(View child, int index, ViewGroup.LayoutParams params)
	{
		super.addView(child, index, params);
		if (child.getTag() == null || (!child.getTag().equals(TAG_PROGRESS) && !child.getTag().equals(TAG_ERROR)))
		{
			mContentViews.add(child);
		}
	}

	public void showProgress()
	{
		switchState(State.PROGRESS, null, Collections.<Integer> emptyList());
	}

	public void showProgress(List<Integer> skipIds)
	{
		switchState(State.PROGRESS, null, skipIds);
	}

	public void showErrorText()
	{
		switchState(State.ERROR, null, Collections.<Integer> emptyList());
	}

	public void showErrorText(List<Integer> skipIds)
	{
		switchState(State.ERROR, null, skipIds);
	}

	public void showErrorText(String error)
	{
		switchState(State.ERROR, error, Collections.<Integer> emptyList());
	}

	public void showErrorText(String error, List<Integer> skipIds)
	{
		switchState(State.ERROR, error, skipIds);
	}

	public void showEmptyText(String tip)
	{
		switchState(State.EMPTY, tip, Collections.<Integer> emptyList());
	}

	public void showEmptyText()
	{
		switchState(State.EMPTY, null, Collections.<Integer> emptyList());
	}

	public void showContent()
	{
		switchState(State.CONTENT, null, Collections.<Integer> emptyList());
	}

	public void showContent(List<Integer> skipIds)
	{
		switchState(State.CONTENT, null, skipIds);
	}

	public void switchState(State state)
	{
		switchState(state, null, Collections.<Integer> emptyList());
	}

	public void switchState(State state, String errorText)
	{
		switchState(state, errorText, Collections.<Integer> emptyList());
	}

	public void switchState(State state, List<Integer> skipIds)
	{
		switchState(state, null, skipIds);
	}

	public void switchState(State state, String errorText, List<Integer> skipIds)
	{
		if (state != mState)
		{
			switch (state)
			{
				case CONTENT:
					mErrorTextView.setVisibility(View.GONE);
					mProgressView.clearAnimation();
					mProgressView.setVisibility(View.GONE);
					setContentVisibility(true, skipIds);
					break;

				case PROGRESS:
					mErrorTextView.setVisibility(View.GONE);
					mProgressView.setVisibility(View.VISIBLE);
					setContentVisibility(false, skipIds);
					break;

				case ERROR:
					if (TextUtils.isEmpty(errorText))
					{
						mErrorTextView.setText(R.string.unknown_error);
					}
					else
					{
						mErrorTextView.setText(errorText);
					}
					Drawable drawable = getResources().getDrawable(R.drawable.iconfont_wangluo);
					drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
					mErrorTextView.setCompoundDrawables(null, drawable, null, null);
					mErrorTextView.setVisibility(View.VISIBLE);
					mProgressView.clearAnimation();
					mProgressView.setVisibility(View.GONE);
					setContentVisibility(false, skipIds);
					break;

				case EMPTY:
					if (TextUtils.isEmpty(errorText))
					{
						mErrorTextView.setText(R.string.empty_tip);
					}
					else
					{
						mErrorTextView.setText(errorText);
					}
					Drawable drawable1 = getResources().getDrawable(R.drawable.iconfont_empty);
					drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
					mErrorTextView.setCompoundDrawables(null, drawable1, null, null);
					mErrorTextView.setVisibility(View.VISIBLE);
					mProgressView.clearAnimation();
					mProgressView.setVisibility(View.GONE);
					setContentVisibility(false, skipIds);
					break;

			}
		}
		mState = state;
	}

	public State getState()
	{
		return mState;
	}

	public boolean isProgress()
	{
		return mState == State.PROGRESS;
	}

	public boolean isContent()
	{
		return mState == State.CONTENT;
	}

	public boolean isError()
	{
		return mState == State.ERROR;
	}

	public boolean isEmpty()
	{
		return mState == State.EMPTY;
	}

	private void setContentVisibility(boolean visible, List<Integer> skipIds)
	{
		for (View v : mContentViews)
		{
			if (!skipIds.contains(v.getId()))
			{
				v.setVisibility(visible ? View.VISIBLE : View.GONE);
			}
		}
	}

	/**
	 * 重试拉取数据回调接口
	 * 
	 * @author houjun
	 */
	public interface IonRetryLoadDatasCallback
	{
		public void onRetryLoadDatas();
	}

	public static enum State
	{
		CONTENT, PROGRESS, ERROR, EMPTY
	}
}
