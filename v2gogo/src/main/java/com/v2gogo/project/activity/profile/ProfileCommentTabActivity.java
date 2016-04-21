package com.v2gogo.project.activity.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.MainTabActivity;
import com.v2gogo.project.activity.home.BaseTabHostActivity;
import com.v2gogo.project.utils.common.AppUtil;
import com.v2gogo.project.utils.common.ScreenUtil;

@SuppressWarnings("deprecation")
public class ProfileCommentTabActivity extends BaseTabHostActivity implements OnCheckedChangeListener, OnClickListener
{
	private final int FIRST_POSITION = 0;
	private final int SECOND_POSITION = 1;
	private int mCurrentPosition = FIRST_POSITION;

	private float mHalfScreenWidth;
	private TabHost mTabHost;
	private ImageView mIvLine;
	private ImageButton mIbtnBack;
	private RadioGroup mRadioGroup;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_comment_tab_activity_layout);
		initViews();
		initTaHost();
		setStatusBarbg();
		setActionBarHeight();
	}

	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.common_app_action_bar_back:
				finish();
				break;

			default:
				break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int checkId)
	{
		switchDisplayContent(checkId);
	}

	@Override
	public void finish()
	{
		if (!AppUtil.isMainIntentExist(this))
		{
			Intent intent = new Intent(this, MainTabActivity.class);
			startActivity(intent);
		}
		super.finish();
	}

	private void initTaHost()
	{
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setFocusable(true);
		TabHost.TabSpec tabSpec = mTabHost.newTabSpec("1");
		Intent intent = new Intent(this, ProfileReplyCommentActivity.class);
		tabSpec.setIndicator("one").setContent(intent);
		mTabHost.setup(this.getLocalActivityManager());
		mTabHost.addTab(tabSpec);

		TabHost.TabSpec tabSpec2 = mTabHost.newTabSpec("2");
		Intent intent2 = new Intent(this, ProfileCommentActivity.class);
		tabSpec2.setIndicator("two").setContent(intent2);
		mTabHost.addTab(tabSpec2);
		mTabHost.setCurrentTab(mCurrentPosition);
	}

	/**
	 * 初始化位置滑块
	 */
	private void initLinePositionSize()
	{
		mHalfScreenWidth = ScreenUtil.getScreenWidth(this) / 2f;
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.common_ui_popup_titleline);
		float scaleX = mHalfScreenWidth / bitmap.getWidth();
		Matrix matrix = new Matrix();
		matrix.postScale(scaleX, 0.6f);
		mIvLine.setImageMatrix(matrix);
		if (!bitmap.isRecycled())
		{
			bitmap.recycle();
		}
	}

	private void initViews()
	{
		mIbtnBack = (ImageButton) findViewById(R.id.common_app_action_bar_back);
		mIvLine = (ImageView) findViewById(R.id.profile_comment_tab_comment_line);
		mRadioGroup = (RadioGroup) findViewById(R.id.profile_comment_tab_rg);
		registerListener();
		initLinePositionSize();
	}

	/**
	 * 注册监听
	 */
	private void registerListener()
	{
		mIbtnBack.setOnClickListener(this);
		mRadioGroup.setOnCheckedChangeListener(this);
	}

	/**
	 * 切换显示内容
	 * 
	 * @param checkId
	 */
	private void switchDisplayContent(int checkId)
	{
		Animation animation = null;
		switch (checkId)
		{
			case R.id.profile_comment_tab_my_comment_rb:
				if (mCurrentPosition == FIRST_POSITION)
				{
					animation = new TranslateAnimation(0, mHalfScreenWidth, 0, 0);
				}
				mTabHost.setCurrentTab(SECOND_POSITION);
				mCurrentPosition = SECOND_POSITION;
				break;

			case R.id.profile_comment_tab_reply_me_rb:
				if (mCurrentPosition == SECOND_POSITION)
				{
					animation = new TranslateAnimation(mHalfScreenWidth, 0, 0, 0);
				}
				mTabHost.setCurrentTab(FIRST_POSITION);
				mCurrentPosition = FIRST_POSITION;
				break;

			default:
				break;
		}
		if (null != animation)
		{
			animation.setDuration(250);
			animation.setInterpolator(new AccelerateInterpolator());
			animation.setFillEnabled(true);
			animation.setFillAfter(true);
			mIvLine.startAnimation(animation);
		}
	}
}
