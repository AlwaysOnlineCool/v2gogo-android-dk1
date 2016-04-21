package com.v2gogo.project.activity.profile.setting;

import java.io.File;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.igexin.sdk.PushManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.v2gogo.project.R;
import com.v2gogo.project.activity.BaseActivity;
import com.v2gogo.project.activity.MainTabActivity;
import com.v2gogo.project.activity.profile.ProfileModifyPasswordActivity;
import com.v2gogo.project.domain.VersionInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.manager.VersionManager;
import com.v2gogo.project.manager.VersionManager.IonServerVersionInfosCallback;
import com.v2gogo.project.manager.account.AccountLoginOutManager;
import com.v2gogo.project.manager.account.AccountLoginOutManager.IonAccountLogoutCallback;
import com.v2gogo.project.utils.common.AppUtil;
import com.v2gogo.project.utils.common.NotificationUtil;
import com.v2gogo.project.utils.common.SPUtil;
import com.v2gogo.project.utils.common.ToastUtil;
import com.v2gogo.project.utils.common.apk.ApkUtil;
import com.v2gogo.project.utils.common.apk.DownloadApkService;
import com.v2gogo.project.utils.common.apk.StorageUtils;
import com.v2gogo.project.utils.http.Constants;
import com.v2gogo.project.views.dialog.ApkDownloadProgressDialog;
import com.v2gogo.project.views.dialog.AppNoticeDialog;
import com.v2gogo.project.views.dialog.AppNoticeDialog.IonClickSureCallback;
import com.v2gogo.project.views.dialog.AppVersionUpdateDialog;
import com.v2gogo.project.views.dialog.AppVersionUpdateDialog.IonStartDownloadCallback;

/**
 * 个人设置
 * 
 * @author houjun
 */
public class ProfileSettingActivity extends BaseActivity implements OnClickListener, IonStartDownloadCallback, IonClickSureCallback
{
	private Button mBtnLogout;

	private TextView mTextCacheSize;
	private ImageView mNewVersionIcon;

	private RelativeLayout mAboutUsLayout;
	private RelativeLayout mFeedbackLayout;
	private RelativeLayout mModifyPwdLayout;
	private RelativeLayout mClearCacheLayout;
	private RelativeLayout mPayRelativeLayout;
	private RelativeLayout mVersionUpdateLayout;

	private AppNoticeDialog mAppNoticeDialog;
	private AppVersionUpdateDialog mVersionUpdateDialog;
	private ApkDownloadProgressDialog mDownloadProgressDialog;
	private DownloadApkBroadcastReceiver mDownloadApkBroadcastReceiver;

	private CheckBox mNewsInfoCheckBox;
	private CheckBox mNewsInfoVoiceCheckBox;

	@Override
	public void onInitViews()
	{
		mBtnLogout = (Button) findViewById(R.id.profile_setting_logout_btn);
		mTextCacheSize = (TextView) findViewById(R.id.profile_setting_cache_size);
		mPayRelativeLayout = (RelativeLayout) findViewById(R.id.profile_setting_pay_layout);
		mAboutUsLayout = (RelativeLayout) findViewById(R.id.profile_setting_about_us_layout);
		mFeedbackLayout = (RelativeLayout) findViewById(R.id.profile_setting_feedback_layout);
		mNewVersionIcon = (ImageView) findViewById(R.id.profile_setting_update_version_new_icon);
		mClearCacheLayout = (RelativeLayout) findViewById(R.id.profile_setting_clear_cache_layout);
		mModifyPwdLayout = (RelativeLayout) findViewById(R.id.profile_setting_modify_password_layout);
		mVersionUpdateLayout = (RelativeLayout) findViewById(R.id.profile_setting_version_update_layout);
		mNewsInfoCheckBox = (CheckBox) findViewById(R.id.news_info_push_checkbox);
		mNewsInfoVoiceCheckBox = (CheckBox) findViewById(R.id.news_info_voice_push_checkbox);

		setchecked();
	}

	/**
	 * method desc：设置推送VIew状态
	 */
	private void setchecked()
	{
		if ((Boolean) SPUtil.get(this, NotificationUtil.IS_SHOW_PUSH_VOICE, true))
		{
			mNewsInfoVoiceCheckBox.setChecked(true);
		}
		else
		{
			mNewsInfoVoiceCheckBox.setChecked(false);
		}
		if ((Boolean) SPUtil.get(this, NotificationUtil.IS_SHOW_PUSH_NEWS, true))
		{
			mNewsInfoCheckBox.setChecked(true);
		}
		else
		{
			mNewsInfoCheckBox.setChecked(false);
		}
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.profile_setting_activity_layout;
	}

	@Override
	protected void registerListener()
	{
		super.registerListener();
		mBtnLogout.setOnClickListener(this);
		mAboutUsLayout.setOnClickListener(this);
		mFeedbackLayout.setOnClickListener(this);
		mModifyPwdLayout.setOnClickListener(this);
		mClearCacheLayout.setOnClickListener(this);
		mPayRelativeLayout.setOnClickListener(this);
		mVersionUpdateLayout.setOnClickListener(this);
		setCheckBoxChangeListener();
	}

	/**
	 * method desc：推送设置
	 */
	private void setCheckBoxChangeListener()
	{
		mNewsInfoCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if (isChecked)
				{
					SPUtil.put(ProfileSettingActivity.this, NotificationUtil.IS_SHOW_PUSH_NEWS, true);
					PushManager.getInstance().turnOnPush(ProfileSettingActivity.this);
				}
				else
				{
					SPUtil.put(ProfileSettingActivity.this, NotificationUtil.IS_SHOW_PUSH_NEWS, false);
					PushManager.getInstance().turnOffPush(ProfileSettingActivity.this);
				}
			}
		});
		mNewsInfoVoiceCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if (isChecked)
				{
					SPUtil.put(ProfileSettingActivity.this, NotificationUtil.IS_SHOW_PUSH_VOICE, true);
				}
				else
				{
					SPUtil.put(ProfileSettingActivity.this, NotificationUtil.IS_SHOW_PUSH_VOICE, false);
				}
			}
		});
	}

	@Override
	protected void onInitLoadDatas()
	{
		super.onInitLoadDatas();
		if (V2GogoApplication.getMasterLoginState())
		{
			mBtnLogout.setVisibility(View.VISIBLE);
		}
		else
		{
			mBtnLogout.setVisibility(View.GONE);
		}
		displayVersionNew();
		setFileSize();
	}

	@Override
	public void onClick(View view)
	{
		Intent intent = null;
		switch (view.getId())
		{
			case R.id.profile_setting_logout_btn:
				accountLogout();
				break;

			case R.id.profile_setting_modify_password_layout:
				if (V2GogoApplication.getMasterLoginState())
				{
					intent = new Intent(this, ProfileModifyPasswordActivity.class);
				}
				break;

			case R.id.profile_setting_clear_cache_layout:
				displayDeleteCacheDialog();
				break;

			case R.id.profile_setting_about_us_layout:
				intent = new Intent(this, AboutUsActivity.class);
				break;

			case R.id.profile_setting_feedback_layout:
				intent = new Intent(this, FeedbackActivity.class);
				break;

			case R.id.profile_setting_pay_layout:
				intent = new Intent(this, ProfileSettingPayActivity.class);
				break;

			case R.id.profile_setting_version_update_layout:
				getServerVersion();
				break;

			default:
				break;
		}
		if (null != intent)
		{
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	}

	@Override
	public void clearRequestTask()
	{
		VersionManager.clearServerVersionInfosTask();
		AccountLoginOutManager.clearaccountLogoutTask();
	}

	@Override
	public void onStartDownload(VersionInfo versionInfo)
	{
		if (versionInfo != null)
		{
			if (null == mDownloadProgressDialog)
			{
				mDownloadProgressDialog = new ApkDownloadProgressDialog(this, R.style.style_action_sheet_dialog);
			}
			if (!mDownloadProgressDialog.isShowing())
			{
				mDownloadProgressDialog.show();
			}
			if (mDownloadApkBroadcastReceiver == null)
			{
				mDownloadApkBroadcastReceiver = new DownloadApkBroadcastReceiver();
			}
			IntentFilter intentFilter = new IntentFilter(DownloadApkService.PROGRESS_INTENT);
			registerReceiver(mDownloadApkBroadcastReceiver, intentFilter);
			ApkUtil.startDownloadApk(this, versionInfo.getDownloadUrl());
		}
	}

	@Override
	public void onClickSure()
	{
		deleteCacheFiles();
	}

	/**
	 * 得到服务器的版本
	 */
	private void getServerVersion()
	{
		VersionManager.getServerVersionInfos(new IonServerVersionInfosCallback()
		{
			@Override
			public void onServerVersionInfosSuccess(VersionInfo versionInfo)
			{
				if (null != versionInfo)
				{
					if (!TextUtils.isEmpty(versionInfo.getVername()))
					{
						boolean isNew = isNewVersion(versionInfo.getVername());
						if (isNew)
						{
							mNewVersionIcon.setVisibility(View.VISIBLE);
							checkAppVersion(versionInfo);
						}
						else
						{
							mNewVersionIcon.setVisibility(View.GONE);
							ToastUtil.showConfirmToast(ProfileSettingActivity.this, R.string.nin_yet_newest_version_tip);
						}
					}
				}
			}

			@Override
			public void onServerVersionInfosFail(String errorMessage)
			{
				ToastUtil.showAlertToast(ProfileSettingActivity.this, errorMessage);
			}
		});
	}

	/**
	 * 显示删除缓存对话框
	 */
	private void displayDeleteCacheDialog()
	{
		if (null == mAppNoticeDialog)
		{
			mAppNoticeDialog = new AppNoticeDialog(this, R.style.style_action_sheet_dialog);
			mAppNoticeDialog.setOnSureCallback(this);
		}
		if (!mAppNoticeDialog.isShowing())
		{
			mAppNoticeDialog.show();
			mAppNoticeDialog.setSureTitleAndMessage(R.string.clear_cache_message_tip, R.string.app_notice_sure_tip);
		}
	}

	/**
	 * 显示是否有新版本new的标示
	 */
	private void displayVersionNew()
	{
		String versionName = (String) SPUtil.get(this, Constants.VERSION_NAME, "");
		if (!TextUtils.isEmpty(versionName))
		{
			boolean isNew = isNewVersion(versionName);
			if (isNew)
			{
				mNewVersionIcon.setVisibility(View.VISIBLE);
			}
			else
			{
				mNewVersionIcon.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * 设置文件大小
	 */
	private void setFileSize()
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				int fileSize = 0;
				File file = V2GogoApplication.getDiskCacheFile(ProfileSettingActivity.this);
				if (file != null && file.exists())
				{
					File[] files = file.listFiles();
					if (files != null)
					{
						for (File file2 : files)
						{
							fileSize += file2.length();
						}
					}
				}
				final int cacheSize = fileSize;
				ProfileSettingActivity.this.runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						mTextCacheSize.setText(String.format(getString(R.string.cache_size_tip), cacheSize / 1024f / 1024));
					}
				});
			}
		}).start();
	}

	/**
	 * 删除缓存文件
	 */
	private void deleteCacheFiles()
	{
		ImageLoader.getInstance().clearDiskCache();
		mTextCacheSize.setText("0.00M");
	}

	/**
	 * 退出登录
	 */
	private void accountLogout()
	{
		showLoadingDialog(R.string.profile_user_logouting_tip);
		AccountLoginOutManager.accountLogout(new IonAccountLogoutCallback()
		{
			@Override
			public void onAccountLogoutSuccess()
			{
				dismissLoadingDialog();
				// ToastUtil.showInfoToast((Activity)getApplicationContext(),
				// R.string.profile_user_logout_success);
				mBtnLogout.setVisibility(View.GONE);
				forwardHome();
			}

			@Override
			public void onAccountLogoutFail(String errorMessage)
			{
				dismissLoadingDialog();
				ToastUtil.showAlertToast(ProfileSettingActivity.this, errorMessage);
			}
		});
	}

	/**
	 * 返回到首页
	 */
	private void forwardHome()
	{
		Intent intent = new Intent(this, MainTabActivity.class);
		intent.putExtra(MainTabActivity.BACK, MainTabActivity.BACK_HOME);
		startActivity(intent);
		finish();
	}

	/**
	 * 检测程序的版本
	 */
	private void checkAppVersion(VersionInfo versionInfo)
	{
		if (versionInfo.getUpdate() == VersionInfo.FOUSE_UPDATE_VERSION)
		{
			showAppUpdateDialog(versionInfo, true);
		}
		else if (versionInfo.getUpdate() == VersionInfo.HAVE_UPDATE_VERSION)
		{
			showAppUpdateDialog(versionInfo, false);
		}
		else
		{
			ToastUtil.showConfirmToast(this, R.string.nin_yet_newest_version_tip);
		}
	}

	/**
	 * 显示app升级对话框
	 * 
	 * @param versionInfo
	 */
	private void showAppUpdateDialog(VersionInfo versionInfo, boolean isForceUpdate)
	{
		if (mVersionUpdateDialog == null)
		{
			mVersionUpdateDialog = new AppVersionUpdateDialog(this, R.style.style_action_sheet_dialog);
			mVersionUpdateDialog.setOnCallback(this);
		}
		if (!mVersionUpdateDialog.isShowing())
		{
			mVersionUpdateDialog.show();
			mVersionUpdateDialog.setVersionInfos(versionInfo, isForceUpdate);
		}
	}

	/**
	 * 下载进度广播接收器
	 * 
	 * @author houjun
	 */
	private class DownloadApkBroadcastReceiver extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent data)
		{
			if (data != null)
			{
				String action = data.getAction();
				if (DownloadApkService.PROGRESS_INTENT.equals(action))
				{
					int progress = data.getIntExtra(DownloadApkService.PROGRESS, 0);
					if (progress != 100)
					{
						if (mDownloadProgressDialog != null)
						{
							mDownloadProgressDialog.setProgress(progress);
						}
					}
					else
					{
						if (mDownloadProgressDialog != null)
						{
							mDownloadProgressDialog.dismiss();
						}
						mDownloadApkBroadcastReceiver = null;
						ProfileSettingActivity.this.unregisterReceiver(this);
						File path = StorageUtils.getCacheDirectory(context);
						File apkFile = new File(path, "v2gogo.apk");
						String apkPath = apkFile.getAbsolutePath();
						ApkUtil.installApk(context, apkPath);
					}
				}
			}
		}
	}

}
