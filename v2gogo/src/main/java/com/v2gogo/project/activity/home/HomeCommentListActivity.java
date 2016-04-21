package com.v2gogo.project.activity.home;

import java.util.ArrayList;

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
import com.v2gogo.project.domain.home.CommentInfo;
import com.v2gogo.project.utils.common.ScreenUtil;

/**
 * 评论列表界面
 * 
 * @author houjun
 */
@SuppressWarnings("deprecation")
public class HomeCommentListActivity extends BaseTabHostActivity implements OnCheckedChangeListener, OnClickListener
{
	public static final String ARTICE_ID = "artice_id";
	public static final String SRC_TYPE = "src_type";

	private final int NEWEST_POSITION = 0;
	private final int HOT_POSITION = 1;
	private int mCurrentPosition = NEWEST_POSITION;

	private TabHost mTabHost;
	private ImageButton mIbtnBack;
	private ImageView mIvLine;
	private RadioGroup mRadioGroup;

	public ArrayList<CommentInfo> mAddCommentInfos = new ArrayList<CommentInfo>();
	public ArrayList<String> mDeleteCommentInfos = new ArrayList<String>();

	private float mHalfScreenWidth;

	public int mSrcType;
	public String mArticeId;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_comment_list_activity_layout);
		getInitentDatas();
		initViews();
		initTabHost();
		setStatusBarbg();
		setActionBarHeight();
	}

	private void initViews()
	{
		mIvLine = (ImageView) findViewById(R.id.home_common_list_tab_comment_line);
		mIbtnBack = (ImageButton) findViewById(R.id.common_app_action_bar_back);
		mRadioGroup = (RadioGroup) findViewById(R.id.home_common_list_tab_main_top_rg);
		mRadioGroup.setOnCheckedChangeListener(this);
		mIbtnBack.setOnClickListener(this);
		mIbtnBack.setOnClickListener(this);
		initLinePositionSize();
	}

	/**
	 * 得到界面传入的数据
	 */
	private void getInitentDatas()
	{
		Intent intent = getIntent();
		if (null != intent)
		{
			mArticeId = intent.getStringExtra(ARTICE_ID);
			mSrcType = intent.getIntExtra(SRC_TYPE, CommentInfo.SRC_ARTICE_TYPE);
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int checkId)
	{
		Animation animation = null;
		switch (checkId)
		{
			case R.id.home_common_list_tab_main_newest_rb:
				if (mCurrentPosition == HOT_POSITION)
				{
					animation = new TranslateAnimation(mHalfScreenWidth, 0, 0, 0);
				}
				mTabHost.setCurrentTab(NEWEST_POSITION);
				mCurrentPosition = NEWEST_POSITION;
				break;

			case R.id.home_common_list_tab_main_hot_rb:
				if (mCurrentPosition == NEWEST_POSITION)
				{
					animation = new TranslateAnimation(0, mHalfScreenWidth, 0, 0);
				}
				mTabHost.setCurrentTab(HOT_POSITION);
				mCurrentPosition = HOT_POSITION;
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

	@Override
	public void onClick(View view)
	{
		finish();
	}

	@Override
	public void finish()
	{
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putSerializable(BaseDetailsctivity.ADD_COMMENT, mAddCommentInfos);
		bundle.putSerializable(BaseDetailsctivity.DELETE_COMMENT, mDeleteCommentInfos);
		intent.putExtras(bundle);
		setResult(RESULT_OK, intent);
		super.finish();
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

	/**
	 * 初始化tabhost
	 */
	private void initTabHost()
	{
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setFocusable(true);
		TabHost.TabSpec tabSpec = mTabHost.newTabSpec("1");
		Intent intent = new Intent(this, NewestCommentListActivity.class);
		tabSpec.setIndicator("one").setContent(intent);
		mTabHost.setup(this.getLocalActivityManager());
		mTabHost.addTab(tabSpec);

		TabHost.TabSpec tabSpec2 = mTabHost.newTabSpec("2");
		Intent intent2 = new Intent(this, HotCommentListActivity.class);
		tabSpec2.setIndicator("two").setContent(intent2);
		mTabHost.addTab(tabSpec2);
		mTabHost.setCurrentTab(mCurrentPosition);
	}
}
