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

import com.v2gogo.project.R;
import com.v2gogo.project.activity.MainTabActivity;
import com.v2gogo.project.utils.common.DensityUtil;
import com.v2gogo.project.utils.common.ScreenUtil;

/**
 * 支付失败的对话框
 * 
 * @author houjun
 */
public class PayResultFailDialog extends Dialog implements android.view.View.OnClickListener
{
	private View mContentView;
	private Button mBtnResetPay;
	private Button mBtnContiuneShop;

	private boolean mIsInitWidth = false;
	private IonResetPayCallback mPayCallback;

	public PayResultFailDialog(Context context, boolean cancelable, OnCancelListener cancelListener)
	{
		super(context, cancelable, cancelListener);
		setDialogParams();
	}

	public PayResultFailDialog(Context context, int theme)
	{
		super(context, theme);
		setDialogParams();
	}

	public PayResultFailDialog(Context context)
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
		if (null == mContentView)
		{
			mContentView = LayoutInflater.from(getContext()).inflate(R.layout.pay_result_fail_dialog_layout, null);
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
		mBtnResetPay = (Button) mContentView.findViewById(R.id.pay_result_fail_re_pay);
		mBtnContiuneShop = (Button) mContentView.findViewById(R.id.pay_result_fail_continue_shop_btn);
		mBtnResetPay.setOnClickListener(this);
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

	@Override
	public void onClick(View view)
	{
		if (view.getId() == R.id.pay_result_fail_re_pay)
		{
			PayResultFailDialog.this.dismiss();
			if (null != mPayCallback)
			{
				mPayCallback.onResetPay();
			}
		}
		else if (view.getId() == R.id.pay_result_fail_continue_shop_btn)
		{
			PayResultFailDialog.this.dismiss();
			Intent intent = new Intent(getContext(), MainTabActivity.class);
			intent.putExtra(MainTabActivity.BACK, MainTabActivity.BACK_SHOP);
			getContext().startActivity(intent);
		}
	}

	public void setOnPayCallback(IonResetPayCallback mPayCallback)
	{
		this.mPayCallback = mPayCallback;
	}

	public interface IonResetPayCallback
	{
		public void onResetPay();
	}
}
