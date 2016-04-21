package com.v2gogo.project.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.utils.common.ScreenUtil;

/**
 * 签到领金币
 * 
 * @author houjun
 */
public class SignGetCoinDialog extends Dialog implements android.view.View.OnClickListener
{
	private View mContentView;

	private Button mBtnSign;
	private TextView mTvCoin;
	private TextView mTvDays;
	private TextView mTvConCoin;
	private TextView mTvCoinEx;
	private String mCoinTip;

	private boolean mIsInitWidth = false;
	private IonClickGetCoinCallback mClickGetCoinCallback;

	public SignGetCoinDialog(Context context, int theme)
	{
		super(context, theme);
		setDialogParams();
		mCoinTip = getContext().getString(R.string.coin_tip);
	}

	public SignGetCoinDialog(Context context)
	{
		super(context);
		setDialogParams();
		mCoinTip = getContext().getString(R.string.coin_tip);
	}

	private void setDialogParams()
	{
		Window dialogWindow = getWindow();
		dialogWindow.setWindowAnimations(R.style.style_dialog_aniamtion);
		dialogWindow.setGravity(Gravity.BOTTOM);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if (mContentView == null)
		{
			mContentView = getLayoutInflater().inflate(R.layout.sign_get_coin_dialog_layout, null);
		}
		setContentView(mContentView);
		if (!mIsInitWidth)
		{
			Window dialogWindow = this.getWindow();
			WindowManager.LayoutParams lp = dialogWindow.getAttributes();
			lp.width = ScreenUtil.getScreenWidth(getContext());
			dialogWindow.setAttributes(lp);
			mIsInitWidth = true;
		}
		initViews(mContentView);
		mBtnSign.setOnClickListener(this);
	}

	private void initViews(View view)
	{
		mTvCoin = (TextView) view.findViewById(R.id.sign_coin_text);
		mTvDays = (TextView) view.findViewById(R.id.sign_days_text);
		mBtnSign = (Button) view.findViewById(R.id.sign_get_coin_btn);
		mTvCoinEx = (TextView) view.findViewById(R.id.sign_coin_text_tip);
		mTvConCoin = (TextView) view.findViewById(R.id.sign_coin_con_text_tip);
		String string = getContext().getString(R.string.con_sign_tip);
		SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(string);
		ForegroundColorSpan colorSpan = new ForegroundColorSpan(0xFFF96700);
		spannableStringBuilder.setSpan(colorSpan, 6, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		colorSpan = new ForegroundColorSpan(0xFFF96700);
		spannableStringBuilder.setSpan(colorSpan, 13, string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		mTvConCoin.setText(spannableStringBuilder);
	}

	public void setOnClickGetCoinCallback(IonClickGetCoinCallback mClickGetCoinCallback)
	{
		this.mClickGetCoinCallback = mClickGetCoinCallback;
	}

	/**
	 * 设置数据
	 * 
	 * @param days
	 * @param coin
	 */
	public void setDatas(int days, int coin, int maxCoin)
	{
		mTvCoin.setText("+" + coin);
		mTvCoinEx.setText(coin + mCoinTip);
		mTvDays.setText(String.format(getContext().getString(R.string.sign_days_tip), days));
		String string = getContext().getString(R.string.con_sign_tip);
		String max = String.format(string, maxCoin);
		SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(max);
		ForegroundColorSpan colorSpan = new ForegroundColorSpan(0xFFF96700);
		spannableStringBuilder.setSpan(colorSpan, 6, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		colorSpan = new ForegroundColorSpan(0xFFF96700);
		spannableStringBuilder.setSpan(colorSpan, 13, max.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		mTvConCoin.setText(spannableStringBuilder);
	}

	@Override
	public void onClick(View arg0)
	{
		this.dismiss();
		if (mClickGetCoinCallback != null)
		{
			mClickGetCoinCallback.onGetCoinClick();
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
	 * 点击确定的数据回调
	 * 
	 * @author houjun
	 */
	public interface IonClickGetCoinCallback
	{
		public void onGetCoinClick();
	}

}
