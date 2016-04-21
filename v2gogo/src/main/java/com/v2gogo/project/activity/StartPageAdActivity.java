package com.v2gogo.project.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.v2gogo.project.R;
import com.v2gogo.project.db.MatserInfo;
import com.v2gogo.project.domain.WelcomeItemInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.manager.account.AccountLoginManager;
import com.v2gogo.project.manager.account.AccountLoginManager.IAccountLoginCallback;
import com.v2gogo.project.utils.common.SPUtil;
import com.v2gogo.project.utils.common.ToastUtil;
import com.v2gogo.project.utils.http.Constants;
import com.v2gogo.project.views.dialog.AppNoticeDialog;
import com.v2gogo.project.views.dialog.AppNoticeDialog.IonClickSureCallback;

/**
 * 启动广告页
 * 
 * @author houjun
 */
public class StartPageAdActivity extends BaseActivity implements OnClickListener, IonClickSureCallback
{
	public static final String WELCOME_ITEM_INFO = "welcome_item_info";

	private Button mBtnJoin;
	private Button mBtnSkip;

	private boolean isStop = false;

	private RelativeLayout mJoinLayout;
	private ImageView mStartPageAdImageView;

	private ArrayList<WelcomeItemInfo> mWelcomeItemInfos;
	private AppNoticeDialog mAppNoticeDialog;
	private CustomCountTimer mCustomCountTimer;

	private int mCurrentAdIndex;// 当前广告的索引
	private TextView mTotalDownTimeTv;// 总倒计时textview
	private int mTotalDownTime;// 计算全部广告的总倒计时

	private StartPageAdActivity mActivity;

	@Override
	public void clearRequestTask()
	{
		if (mCustomCountTimer != null)
		{
			mCustomCountTimer.cancel();
		}
	}

	@Override
	public void onInitViews()
	{
		mBtnSkip = (Button) findViewById(R.id.start_page_skip_btn);
		mBtnJoin = (Button) findViewById(R.id.start_page_join_look_deatils);
		mJoinLayout = (RelativeLayout) findViewById(R.id.start_page_join_layout);
		mStartPageAdImageView = (ImageView) findViewById(R.id.start_page_ad_imageview);
		mTotalDownTimeTv = (TextView) findViewById(R.id.start_page_total_downtime_tv);

		mActivity = this;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onInitIntentData(Intent intent)
	{
		super.onInitIntentData(intent);
		if (null != intent)
		{
			mWelcomeItemInfos = (ArrayList<WelcomeItemInfo>) intent.getSerializableExtra(WELCOME_ITEM_INFO);
		}
	}

	/**
	 * method desc：
	 */
	private void countTotalDownTime()
	{
		for (WelcomeItemInfo welcomeItemInfo : mWelcomeItemInfos)
		{
			mTotalDownTime += welcomeItemInfo.getShowtime();
		}
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if (mCustomCountTimer != null)
		{
			mCustomCountTimer.cancel();
		}
	}

	@Override
	protected void onInitLoadDatas()
	{
		super.onInitLoadDatas();
		accountAutoLogin();
		if (mWelcomeItemInfos != null && mWelcomeItemInfos.size() > 0)
		{
			countTotalDownTime();
			initViewByAdIndex(mCurrentAdIndex);
			if (mCustomCountTimer == null)
			{
				mCustomCountTimer = new CustomCountTimer(mTotalDownTime * 1000, 1 * 1000);
			}
			mCustomCountTimer.start();

		}
	}

	private void initViewByAdIndex(int currentAdIndex)
	{
		if (mCurrentAdIndex < mWelcomeItemInfos.size() && mActivity != null && !mActivity.isFinishing())// 数据游标小于等于才做计算,免得报越狱
		{
			WelcomeItemInfo welcomeItemInfo = mWelcomeItemInfos.get(currentAdIndex);
			// DisplayImageOptions displayImageOptions =
			// DisplayImageOptionsFactory.getDefaultDisplayImageOptionsWithNoPhoto(true);
			// ImageLoader.getInstance().displayImage(welcomeItemInfo.getRealImage(),
			// mStartPageAdImageView, displayImageOptions);
			// GlideImageLoader.loadImageWithNoFixedSize(this, welcomeItemInfo.getUrl(),
			// mStartPageAdImageView);
			Glide.with(this).load(welcomeItemInfo.getUrl()).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(mStartPageAdImageView);
			if (welcomeItemInfo.isSkip())
			{
				isStop = true;
				mBtnSkip.setVisibility(View.VISIBLE);
			}
			else
			{
				mBtnSkip.setVisibility(View.GONE);
			}
			if (!TextUtils.isEmpty(welcomeItemInfo.getHerf()))
			{
				mJoinLayout.setVisibility(View.GONE);
			}
		}
	}

	@Override
	protected void registerListener()
	{
		super.registerListener();
		mBtnSkip.setOnClickListener(this);
		mBtnJoin.setOnClickListener(this);
	}

	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{

			case R.id.start_page_skip_btn:
				skipAd();
				break;

			case R.id.start_page_join_look_deatils:
				lookAdDetails(mCurrentAdIndex);
				break;

			default:
				break;
		}
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.start_page_ad_activity_layout;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 用户自动登录
	 */
	private void accountAutoLogin()
	{
		if (V2GogoApplication.getMasterLoginState())
		{
			MatserInfo matserInfo = V2GogoApplication.getCurrentMatser();
			String pwd = (String) SPUtil.get(this, Constants.USER_PASS, "");
			AccountLoginManager.accountLogin(matserInfo.getUsername(), pwd, true, new IAccountLoginCallback()
			{
				@Override
				public void onAccountLoginSuccess(MatserInfo masterInfo)
				{

				}

				@Override
				public void onAccountLoginFail(String erroMessage)
				{
					if (getResources().getString(R.string.user_pwd_error_tip).equals(erroMessage))
					{
						if (mCustomCountTimer != null)
						{
							mCustomCountTimer.cancel();
						}
						V2GogoApplication.clearMatserInfo(true);
						showUserExceptionDialog();
					}
				}
			});
		}
	}

	private long lastTime;

	/**
	 * 跳过广告
	 */
	private void skipAd()
	{
		if (System.currentTimeMillis() - lastTime < 1000)
		{
			ToastUtil.showAlertToast(this, "点击太过频繁");
			lastTime = System.currentTimeMillis();
			return;
		}
		if (mWelcomeItemInfos != null)
		{
			if (mCustomCountTimer != null)
			{
				mCustomCountTimer.cancel();
			}
			if (mCurrentAdIndex >= mWelcomeItemInfos.size() - 1)
			{
				enterApp();
				return;
			}
			mTotalDownTime = mTotalDownTime - mWelcomeItemInfos.get(mCurrentAdIndex).getShowtime();// 余下总时间
			mCurrentAdIndex = mCurrentAdIndex + 1;
			initViewByAdIndex(mCurrentAdIndex);
			// 开始倒计时
			mCustomCountTimer = new CustomCountTimer(mTotalDownTime * 1000, 1000);
			mCustomCountTimer.start();
		}
	}

	/**
	 * 查看广告详情
	 */
	private void lookAdDetails(int currentIndex)
	{
		if (null != mWelcomeItemInfos)
		{
			if (mCustomCountTimer != null)
			{
				mCustomCountTimer.cancel();
			}
			Intent intent = new Intent(this, WebViewActivity.class);
			intent.putExtra(WebViewActivity.URL, mWelcomeItemInfos.get(currentIndex).getHerf());
			intent.putExtra(WebViewActivity.IS_BACK_HOME, true);
			startActivity(intent);
			finish();
		}
	}

	@Override
	public void onClickSure()
	{
		Intent intent = new Intent(StartPageAdActivity.this, MainTabActivity.class);
		intent.putExtra("isLogin", true);
		startActivity(intent);
	}

	/**
	 * 进入app
	 */
	private void enterApp()
	{
		Intent intent = new Intent(this, MainTabActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * 显示用户异常对话框
	 */
	private void showUserExceptionDialog()
	{
		try
		{
			if (mAppNoticeDialog == null)
			{
				mAppNoticeDialog = new AppNoticeDialog(this, R.style.style_action_sheet_dialog);
				mAppNoticeDialog.setCancelable(false);
				mAppNoticeDialog.setOnSureCallback(this);
				mAppNoticeDialog.setCanceledOnTouchOutside(false);
			}
			if (!mAppNoticeDialog.isShowing())
			{
				mAppNoticeDialog.show();
				mAppNoticeDialog.setSureTitleAndMessage(R.string.user_account_exception_tip, R.string.app_notice_sure_tip);
			}
		}
		catch (Exception exception)
		{
		}
	}

	/**
	 * 进度主页的倒计时
	 * 
	 * @author houjun
	 */
	private class CustomCountTimer extends CountDownTimer
	{
		private int intervalTime;// 累计时间
		private int mCurrentIndex;// 当前

		public CustomCountTimer(long millisInFuture, long countDownInterval)
		{
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish()
		{
//			mTotalDownTimeTv.setVisibility(View.GONE);
			enterApp();
		}

		@Override
		public void onTick(long time)
		{
			if (mCurrentAdIndex < mWelcomeItemInfos.size() && mActivity != null && !mActivity.isFinishing())// 数据游标小于等于才做计算,免得报越狱
			{
				int showTiming = (int) (mTotalDownTime - time / 1000);// 动态计算展示时间
				intervalTime = mWelcomeItemInfos.get(mCurrentAdIndex).getShowtime();
				if (showTiming == intervalTime)
				{
					intervalTime = intervalTime + mWelcomeItemInfos.get(mCurrentAdIndex).getShowtime();
					mCurrentAdIndex = mCurrentAdIndex + 1;
					initViewByAdIndex(mCurrentAdIndex);
				}
				int showTime = (int) (time / 1000) + 1;
				mTotalDownTimeTv.setVisibility(View.VISIBLE);
				mTotalDownTimeTv.setText(String.valueOf(showTime));
				// mBtnSkip.setText(String.valueOf(showTime) + " 跳过");
				if (showTime <= 2)// 处理最后一秒
				{
					new Handler().postDelayed(new Runnable()
					{
						@Override
						public void run()
						{
							// mBtnSkip.setText(String.valueOf(1) + " 跳过");
							mTotalDownTimeTv.setText(String.valueOf(1));
						}
					}, 1000);
				}
			}
		}
	}
}
