package com.v2gogo.project.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.utils.common.DensityUtil;
import com.v2gogo.project.utils.common.ScreenUtil;

/**
 * 功能：打电话对话框
 * 
 * @ahthor：黄荣星
 * @date:2015-10-14
 * @version::V1.0
 */
public class ProfileServicePhoneCallDialog extends Dialog implements android.view.View.OnClickListener
{

	private View mContentView;
	private boolean mIsInitWidth = false;

	private Button mBtnSure;
	private TextView mTvMessage;

	private IonClickSureCallback mSureCallback;

	public ProfileServicePhoneCallDialog(Context context, boolean cancelable, OnCancelListener cancelListener)
	{
		super(context, cancelable, cancelListener);
		setDialogParams();
	}

	public ProfileServicePhoneCallDialog(Context context, int theme)
	{
		super(context, theme);
		setDialogParams();
	}

	public ProfileServicePhoneCallDialog(Context context)
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
			mContentView = LayoutInflater.from(getContext()).inflate(R.layout.app_notice_dialog_layout, null);
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
		mBtnSure = (Button) mContentView.findViewById(R.id.app_notice_dialog_layout_sure);
		mTvMessage = (TextView) mContentView.findViewById(R.id.app_notice_dialog_layout_message);
		mBtnSure.setOnClickListener(this);
	}

	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.app_notice_dialog_layout_sure:
				ProfileServicePhoneCallDialog.this.dismiss();
				if (null != mSureCallback)
				{
					mSureCallback.onClickSure();
				}
				break;

			default:
				break;
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

	/**
	 * 设置对话框属性
	 */
	private void setDialogParams()
	{
		this.setCanceledOnTouchOutside(true);
		this.setCancelable(true);
	}

	public void setSureTitleAndMessage(int message, int sureString)
	{
		if (mBtnSure != null)
		{
			mBtnSure.setText(sureString);
		}
		if (mTvMessage != null)
		{
			mTvMessage.setText(message);
		}
	}

	public void setSureTitleAndMessage(String message, int sureString, int title)
	{
		if (mBtnSure != null)
		{
			mBtnSure.setText(sureString);
		}
		if (mTvMessage != null)
		{
			mTvMessage.setText(message);
		}
	}

	public void setOnSureCallback(IonClickSureCallback mSureCallback)
	{
		this.mSureCallback = mSureCallback;
	}

	public interface IonClickSureCallback
	{
		public void onClickSure();
	}
}
