package com.v2gogo.project.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.v2gogo.project.R;
import com.v2gogo.project.utils.common.DensityUtil;
import com.v2gogo.project.utils.common.ScreenUtil;

/**
 * 摇一摇中大奖的中奖对话框
 * 
 * @author houjun
 */
public class ShakeRealNoticeDialog extends Dialog implements android.view.View.OnClickListener
{
	private View mContentView;

	private Button mLeftButton;
	private Button mRightButon;

	private boolean mIsInitWidth = false;
	private IonClickDialogButton mClickDialogButton;

	public ShakeRealNoticeDialog(Context context, boolean cancelable, OnCancelListener cancelListener)
	{
		super(context, cancelable, cancelListener);
		setDialogParams();
	}

	public ShakeRealNoticeDialog(Context context, int theme)
	{
		super(context, theme);
		setDialogParams();
	}

	public ShakeRealNoticeDialog(Context context)
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
			mContentView = LayoutInflater.from(getContext()).inflate(R.layout.shake_real_notice_dialog_layout, null);
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
		mLeftButton = (Button) mContentView.findViewById(R.id.shake_real_notice_layout_left_button);
		mRightButon = (Button) mContentView.findViewById(R.id.shake_real_notice_layout_right_button);
		mLeftButton.setOnClickListener(this);
		mRightButon.setOnClickListener(this);
	}

	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.shake_real_notice_layout_left_button:
				dismiss();
				if (mClickDialogButton != null)
				{
					mClickDialogButton.onClickDialog(true);
				}
				break;

			case R.id.shake_real_notice_layout_right_button:
				dismiss();
				if (mClickDialogButton != null)
				{
					mClickDialogButton.onClickDialog(false);
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
		this.setCanceledOnTouchOutside(false);
		this.setCancelable(false);
	}

	public void setOnClickDialogButton(IonClickDialogButton mClickDialogButton)
	{
		this.mClickDialogButton = mClickDialogButton;
	}

	public interface IonClickDialogButton
	{
		public void onClickDialog(boolean isLeft);
	}
}
