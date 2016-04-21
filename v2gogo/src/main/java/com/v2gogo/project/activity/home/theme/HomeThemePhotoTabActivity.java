package com.v2gogo.project.activity.home.theme;

import java.io.File;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.WebViewActivity;
import com.v2gogo.project.activity.home.BaseTabHostActivity;
import com.v2gogo.project.domain.home.theme.UploadErrorInfo;
import com.v2gogo.project.domain.home.theme.UploadProgressInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.manager.upload.ThemePhotoUploadManager;
import com.v2gogo.project.utils.common.LogUtil;
import com.v2gogo.project.utils.common.ScreenUtil;
import com.v2gogo.project.utils.common.ToastUtil;
import com.v2gogo.project.utils.http.IntentExtraConstants;
import com.v2gogo.project.utils.qiniu.QiNiuUploadManager;
import com.v2gogo.project.views.UploadProgressPopupWindow;
import com.ypy.eventbus.EventBus;

@SuppressWarnings("deprecation")
public class HomeThemePhotoTabActivity extends BaseTabHostActivity implements OnCheckedChangeListener, OnClickListener
{

	private final int NEWEST_POSITION = 0;
	private final int HOT_POSITION = 1;
	private int mCurrentPosition = NEWEST_POSITION;

	private float mHalfScreenWidth;

	private Button mBtnRule;
	private TextView mTitle;
	private TabHost mTabHost;
	private ImageView mIvLine;
	private ImageButton mIbtnBack;
	private RadioGroup mRadioGroup;
	private RelativeLayout mRelativeLayout;
	private UploadProgressPopupWindow mProgressPopupWindow;

	public String mTid;
	public String mUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mTid = getIntent().getStringExtra(IntentExtraConstants.TID);
		setContentView(R.layout.home_them_photo_list_activity_layout);
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

			case R.id.home_theme_post_rule_button:
				forward2LookRuel();
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

//	/**
//	 * 进度回调
//	 * 
//	 * @param progress
//	 */
//	public void onEventMainThread(UploadProgressInfo uploadProgressInfo)
//	{
//		displayUploadProgress(uploadProgressInfo);
//	}
//
//	/**
//	 * 上传失败
//	 * 
//	 * @param progress
//	 */
//	public void onEventMainThread(UploadErrorInfo uploadErrorInfo)
//	{
//		displayUploadErrorTip(uploadErrorInfo);
//	}

	@Override
	protected void onNewIntent(Intent intent)
	{
		super.onNewIntent(intent);
		if (intent != null && !TextUtils.isEmpty(mTid))
		{
			File file = new File(ThemePhotoUploadManager.FILE_PATH);
			if (!file.exists())
			{
				ToastUtil.showAlertToast(this, R.string.you_select_file_not_exsits);
			}
			else
			{
				String content = intent.getStringExtra(IntentExtraConstants.THEME_PHOTO_DESC);
				uploadThemePhoto(content, file);
			}
		}
	}

	private void initTaHost()
	{
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setFocusable(true);
		TabHost.TabSpec tabSpec = mTabHost.newTabSpec("1");
		Intent intent = new Intent(this, NewestThemePhotoListActivity.class);
		tabSpec.setIndicator("one").setContent(intent);
		mTabHost.setup(this.getLocalActivityManager());
		mTabHost.addTab(tabSpec);

		TabHost.TabSpec tabSpec2 = mTabHost.newTabSpec("2");
		Intent intent2 = new Intent(this, HotestThemePhotoListActivity.class);
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
		mTitle = (TextView) findViewById(R.id.common_app_action_bar_text);
		mBtnRule = (Button) findViewById(R.id.home_theme_post_rule_button);
		mIbtnBack = (ImageButton) findViewById(R.id.common_app_action_bar_back);
		mRelativeLayout = (RelativeLayout) findViewById(R.id.common_app_action_bar);
		mIvLine = (ImageView) findViewById(R.id.home_theme_photo_list_tab_comment_line);
		mRadioGroup = (RadioGroup) findViewById(R.id.home_theme_photo_list_tab_main_top_rg);
		registerListener();
		initLinePositionSize();
	}

	/**
	 * 注册监听
	 */
	private void registerListener()
	{
		mBtnRule.setOnClickListener(this);
		mIbtnBack.setOnClickListener(this);
//		EventBus.getDefault().register(this);
		mRadioGroup.setOnCheckedChangeListener(this);
	}

	public void setTitleText(String title)
	{
		mTitle.setText(title);
	}

	/**
	 * 查看规则
	 */
	private void forward2LookRuel()
	{
		if (!TextUtils.isEmpty(mTid))
		{
			Intent intent = new Intent(this, WebViewActivity.class);
			intent.putExtra(WebViewActivity.URL, mUrl);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	}

	/**
	 * 上传主题图片
	 * 
	 * @param intent
	 * @param file
	 */
	private void uploadThemePhoto(String content, File file)
	{
		String username = V2GogoApplication.getCurrentMatser().getUsername();
		ThemePhotoUploadManager.uploadThemePhoto(mTid, file, username, content, mTid);
	}

	@Override
	public void finish()
	{
		EventBus.getDefault().unregister(this);
		super.finish();
	}

	/**
	 * 显示上传进度
	 * 
	 * @param uploadProgressInfo
	 */
	private void displayUploadProgress(UploadProgressInfo uploadProgressInfo)
	{
		LogUtil.d("houjun", "uploadProgressInfo->" + uploadProgressInfo);
		if (null != uploadProgressInfo)
		{
			if (uploadProgressInfo.getProgress() != 100)
			{
				if (null == mProgressPopupWindow)
				{
					mProgressPopupWindow = new UploadProgressPopupWindow(this);
				}
				mProgressPopupWindow.show(mRelativeLayout, 0, 0);
				mProgressPopupWindow.setProgress(uploadProgressInfo.getProgress());
			}
			else
			{
				mProgressPopupWindow.dismiss();
			}
		}
	}

	/**
	 * 显示上传失败提示信息
	 * 
	 * @param uploadErrorInfo
	 */
	private void displayUploadErrorTip(UploadErrorInfo uploadErrorInfo)
	{
		LogUtil.d("houjun", "uploadErrorInfo->" + uploadErrorInfo);
		if (null != uploadErrorInfo)
		{
			String errorMessage = null;
			if (uploadErrorInfo.getCode() == QiNiuUploadManager.UPLOAD_STATUS_CODE_NET_BROKEN)
			{
				errorMessage = getString(R.string.you_network_yet_broke_tip);
			}
			else if (uploadErrorInfo.getCode() == QiNiuUploadManager.UPLOAD_STATUS_CODE_NOT_QINIU)
			{
				errorMessage = getString(R.string.you_is_not_qiniu_tip);
			}
			else if (uploadErrorInfo.getCode() == QiNiuUploadManager.UPLOAD_STATUS_CODE_SERVER_ERROR)
			{
				errorMessage = getString(R.string.you_server_error_tip);
			}
			else if (uploadErrorInfo.getCode() == QiNiuUploadManager.UPLOAD_STATUS_CODE_YET_CANCELED)
			{
				errorMessage = getString(R.string.you_photo_yet_cancel_tip);
			}
			else
			{
				errorMessage = uploadErrorInfo.getMessage();
			}
			if (null != mProgressPopupWindow && mProgressPopupWindow.isShowing())
			{
				mProgressPopupWindow.dismiss();
			}
			ToastUtil.showAlertToast(this, errorMessage);
		}
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
			case R.id.home_theme_photo_list_tab_main_hot_rb:
				if (mCurrentPosition == NEWEST_POSITION)
				{
					animation = new TranslateAnimation(0, mHalfScreenWidth, 0, 0);
				}
				mTabHost.setCurrentTab(HOT_POSITION);
				mCurrentPosition = HOT_POSITION;
				break;

			case R.id.home_theme_photo_list_tab_main_newest_rb:
				if (mCurrentPosition == HOT_POSITION)
				{
					animation = new TranslateAnimation(mHalfScreenWidth, 0, 0, 0);
				}
				mTabHost.setCurrentTab(NEWEST_POSITION);
				mCurrentPosition = NEWEST_POSITION;
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
