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
import com.v2gogo.project.activity.MainTabActivity;
import com.v2gogo.project.activity.profile.ProfileOrderActivity;
import com.v2gogo.project.utils.common.DensityUtil;
import com.v2gogo.project.utils.common.ScreenUtil;

/**
 * 支付成功的对话框
 * 
 * @author houjun
 */
public class PayResultSuccessDialog extends Dialog implements android.view.View.OnClickListener
{
	private View mContentView;
	private Button mBtnLookOrder;
	private Button mBtnContiuneShop;

	private TextView mTextTitle;
	private TextView mTextMessage;

	private boolean mIsInitWidth = false;

	public PayResultSuccessDialog(Context context, boolean cancelable, OnCancelListener cancelListener)
	{
		super(context, cancelable, cancelListener);
		setDialogParams();
	}

	public PayResultSuccessDialog(Context context, int theme)
	{
		super(context, theme);
		setDialogParams();
	}

	public PayResultSuccessDialog(Context context)
	{
		super(context);
		setDialogParams();
	}

	private void setDialogParams()
	{
		this.setCanceledOnTouchOutside(true);
		this.setCancelable(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if (mContentView == null)
		{
			mContentView = LayoutInflater.from(getContext()).inflate(R.layout.pay_result_success_dialog_layout, null);
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
		mTextTitle = (TextView) mContentView.findViewById(R.id.pay_result_success_title);
		mTextMessage = (TextView) mContentView.findViewById(R.id.pay_result_success_message);
		mBtnLookOrder = (Button) mContentView.findViewById(R.id.pay_result_success_look_order_btn);
		mBtnContiuneShop = (Button) mContentView.findViewById(R.id.pay_result_success_continue_shop_btn);
		mBtnLookOrder.setOnClickListener(this);
		mBtnContiuneShop.setOnClickListener(this);
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
	 * 设置提示文字
	 * 
	 * @param title
	 * @param message
	 */
	public void setTitleMessage(String title, String message)
	{
		if (mTextMessage != null)
		{
			mTextMessage.setText(message);
		}
		if (mTextTitle != null)
		{
			mTextTitle.setText(title);
		}
	}

	@Override
	public void onClick(View view)
	{
		if (view.getId() == R.id.pay_result_success_look_order_btn)
		{
			Intent intent = new Intent(getContext(), ProfileOrderActivity.class);
			getContext().startActivity(intent);
			PayResultSuccessDialog.this.dismiss();
		}
		else if (view.getId() == R.id.pay_result_success_continue_shop_btn)
		{
			Intent intent = new Intent(getContext(), MainTabActivity.class);
			intent.putExtra(MainTabActivity.BACK, MainTabActivity.BACK_SHOP);
			getContext().startActivity(intent);
			PayResultSuccessDialog.this.dismiss();
		}
	}
}
