package com.v2gogo.project.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.domain.VersionInfo;
import com.v2gogo.project.utils.common.DensityUtil;
import com.v2gogo.project.utils.common.ScreenUtil;

/**
 * 版本更新对话框
 * 
 * @author houjun
 */
public class AppVersionUpdateDialog extends Dialog implements android.view.View.OnClickListener
{

	private boolean mIsInitWidth = false;

	private View mContentView;

	private TextView mTvUpdateMessage;

	private Button mBtnUpdate;
	private Button mBtnAfter;

	private boolean isForceUpdate;// 是否强制更新
	private VersionInfo mVersionInfo;
	private IonStartDownloadCallback mCallback;

	public AppVersionUpdateDialog(Context context, boolean cancelable, OnCancelListener cancelListener)
	{
		super(context, cancelable, cancelListener);
		setDialogParams();
	}

	public AppVersionUpdateDialog(Context context, int theme)
	{
		super(context, theme);
		setDialogParams();
	}

	public AppVersionUpdateDialog(Context context)
	{
		super(context);
		setDialogParams();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if (null == mContentView)
		{
			mContentView = LayoutInflater.from(getContext()).inflate(R.layout.app_version_update_dialog_layout, null);
		}
		setContentView(mContentView);
		if (!mIsInitWidth)
		{
			Window dialogWindow = this.getWindow();
			WindowManager.LayoutParams lp = dialogWindow.getAttributes();
			lp.width = ScreenUtil.getScreenWidth(getContext()) - DensityUtil.dp2px(getContext(), 80f);
			dialogWindow.setAttributes(lp);
			mIsInitWidth = true;
		}
		mBtnUpdate = (Button) mContentView.findViewById(R.id.app_version_update_dialog_layout_left_button);
		mBtnAfter = (Button) mContentView.findViewById(R.id.app_version_update_dialog_layout_right_button);
		mTvUpdateMessage = (TextView) mContentView.findViewById(R.id.app_version_update_dialog_layout_message);
		mBtnAfter.setOnClickListener(this);
		mBtnUpdate.setOnClickListener(this);
	}

	@Override
	public void dismiss()
	{
		try
		{
			super.dismiss();
		}
		catch (Exception exception)
		{
		}
	}

	@Override
	public void show()
	{
		try
		{
			super.show();
		}
		catch (Exception exception)
		{
		}
	}

	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.app_version_update_dialog_layout_left_button:
				AppVersionUpdateDialog.this.dismiss();
				if (mCallback != null)
				{
					mCallback.onStartDownload(mVersionInfo);
				}
				break;

			case R.id.app_version_update_dialog_layout_right_button:
				if (isForceUpdate)
				{
					AppVersionUpdateDialog.this.dismiss();
					Intent startMain = new Intent(Intent.ACTION_MAIN);
					startMain.addCategory(Intent.CATEGORY_HOME);
					startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					getContext().startActivity(startMain);
					android.os.Process.killProcess(android.os.Process.myPid());
				}
				else
				{
					AppVersionUpdateDialog.this.dismiss();
				}
				break;

			default:
				break;
		}
	}

	public void setOnCallback(IonStartDownloadCallback mCallback)
	{
		this.mCallback = mCallback;
	}

	/**
	 * 设置对话框属性
	 */
	private void setDialogParams()
	{
		this.setCanceledOnTouchOutside(false);
		this.setCancelable(false);
	}

	/**
	 * 设置版本信息
	 */
	public void setVersionInfos(VersionInfo versionInfo, boolean isForceUpdate)
	{
		mVersionInfo = versionInfo;
		this.isForceUpdate = isForceUpdate;
		if (versionInfo != null)
		{
			mTvUpdateMessage.setText(versionInfo.getText());
			mBtnUpdate.setText(R.string.mashang_update_tip);
			if (this.isForceUpdate)
			{
				mBtnAfter.setText(R.string.exit_app_tip);
			}
			else
			{
				mBtnAfter.setText(R.string.zhanbu_no_update_tip);
			}
		}
	}

	/**
	 * 立即更新apk的数据回调
	 * 
	 * @author houjun
	 */
	public interface IonStartDownloadCallback
	{
		public void onStartDownload(VersionInfo versionInfo);
	}
}
