package com.v2gogo.project.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.v2gogo.project.R;
import com.v2gogo.project.utils.common.DensityUtil;
import com.v2gogo.project.utils.common.ScreenUtil;
import com.v2gogo.project.views.progressbar.NumberProgressBar;

/**
 * apk下载进度对话框
 * 
 * @author houjun
 */
public class ApkDownloadProgressDialog extends Dialog
{

	private View mContentView;
	private boolean isInitWidth = false;
	private NumberProgressBar mNumberProgressBar;

	public ApkDownloadProgressDialog(Context context, boolean cancelable, OnCancelListener cancelListener)
	{
		super(context, cancelable, cancelListener);
		setDialogParams();
	}

	public ApkDownloadProgressDialog(Context context, int theme)
	{
		super(context, theme);
		setDialogParams();
	}

	public ApkDownloadProgressDialog(Context context)
	{
		super(context);
		setDialogParams();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if (mContentView == null)
		{
			mContentView = LayoutInflater.from(getContext()).inflate(R.layout.apk_download_progress_dialog_layout, null);
		}
		setContentView(mContentView);
		if (!isInitWidth)
		{
			Window dialogWindow = this.getWindow();
			WindowManager.LayoutParams lp = dialogWindow.getAttributes();
			lp.width = ScreenUtil.getScreenWidth(getContext()) - DensityUtil.dp2px(getContext(), 80f);
			dialogWindow.setAttributes(lp);
			isInitWidth = true;
		}
		mNumberProgressBar = (NumberProgressBar) mContentView.findViewById(R.id.apk_download_progressbar);
		mNumberProgressBar.setProgressTextColor(0xffff5601);
		mNumberProgressBar.setProgressTextSize(22f);
		mNumberProgressBar.setReachedBarColor(0xffff5601);
		mNumberProgressBar.setReachedBarHeight(9f);
		mNumberProgressBar.setUnreachedBarHeight(9f);
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
	 * 设置进度
	 * 
	 * @param progress
	 */
	public void setProgress(int progress)
	{
		if (mNumberProgressBar != null)
		{
			mNumberProgressBar.setProgress(progress);
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
}
