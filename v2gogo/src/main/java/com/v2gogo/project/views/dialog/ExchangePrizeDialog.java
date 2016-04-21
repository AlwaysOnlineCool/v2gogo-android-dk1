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
 * 奖品兑换的对话框
 * 
 * @author houjun
 */
public class ExchangePrizeDialog extends Dialog implements android.view.View.OnClickListener
{

	private View mContentView;
	private boolean mIsInitWidth = false;

	private Button mBtnLeft;
	private Button mBtnRight;

	private TextView mTvMessage;

	private String mId;
	private String mReceiveId;

	private IonSureClickPrizeCallback mClickPrizeCallback;

	public ExchangePrizeDialog(Context context, boolean cancelable, OnCancelListener cancelListener)
	{
		super(context, cancelable, cancelListener);
		setDialogParams();
	}

	public ExchangePrizeDialog(Context context, int theme)
	{
		super(context, theme);
		setDialogParams();
	}

	public ExchangePrizeDialog(Context context)
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
			mContentView = LayoutInflater.from(getContext()).inflate(R.layout.exchange_prize_dialog_layout, null);
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
		mBtnLeft = (Button) mContentView.findViewById(R.id.exchange_prize_left_button);
		mBtnRight = (Button) mContentView.findViewById(R.id.exchange_prize_right_btn);
		mTvMessage = (TextView) mContentView.findViewById(R.id.exchange_prize_message_textview);
		mBtnRight.setOnClickListener(this);
		mBtnLeft.setOnClickListener(this);
	}

	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.exchange_prize_left_button:
				ExchangePrizeDialog.this.dismiss();
				if (mClickPrizeCallback != null)
				{
					mClickPrizeCallback.onSureClickPrize(mId, mReceiveId);
				}
				break;

			case R.id.exchange_prize_right_btn:
				ExchangePrizeDialog.this.dismiss();
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

	public void setOnClickPrizeCallback(IonSureClickPrizeCallback mClickPrizeCallback)
	{
		this.mClickPrizeCallback = mClickPrizeCallback;
	}

	private void setDialogParams()
	{
		this.setCancelable(true);
		this.setCanceledOnTouchOutside(true);
	}

	/**
	 * 设置message
	 * 
	 * @param message
	 */
	public void setMessage(int message)
	{
		if (mTvMessage != null)
		{
			mTvMessage.setText(message);
		}
	}

	/**
	 * @param prizeInfo
	 */
	public void setPrizeInfosId(String id, String receiveId)
	{
		mId = id;
		mReceiveId = receiveId;
	}

	public interface IonSureClickPrizeCallback
	{
		public void onSureClickPrize(String id, String receiveId);
	}
}
