package com.v2gogo.project.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.domain.exchange.PrizeInfo;
import com.v2gogo.project.utils.common.KeyBoardUtil;
import com.v2gogo.project.utils.common.ScreenUtil;

/**
 * 领取金币的对话框
 * 
 * @author houjun
 */
public class ProfileGetCodeSheetCode extends Dialog implements android.view.View.OnClickListener
{

	public static enum ACTION
	{
		SURE, CANCEL
	}

	private View mContentView;
	private TextView mTvTip;
	private Button mBtnSure;
	private Button mBtnCancel;
	private EditText mInput;
	private PrizeInfo mPrizeInfo;

	private boolean mIsInitWidth = false;
	private IonActionSheetClickListener mSheetClickListener;

	public ProfileGetCodeSheetCode(Context context, boolean cancelable, OnCancelListener cancelListener)
	{
		super(context, cancelable, cancelListener);
		setDialogParams();
	}

	public ProfileGetCodeSheetCode(Context context, int theme)
	{
		super(context, theme);
		setDialogParams();
	}

	public ProfileGetCodeSheetCode(Context context)
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
			mContentView = getLayoutInflater().inflate(R.layout.profile_action_sheet_edit_dialog_layout, null);
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
	}

	public void setSheetClickListener(IonActionSheetClickListener mSheetClickListener)
	{
		this.mSheetClickListener = mSheetClickListener;
	}

	private void initViews(View view)
	{
		mBtnCancel = (Button) view.findViewById(R.id.profile_action_sheet_edit_dialog_cancle);
		mBtnSure = (Button) view.findViewById(R.id.profile_action_sheet_edit_dialog_sure);
		mInput = (EditText) view.findViewById(R.id.profile_action_sheet_edit_dialog_input);
		mTvTip = (TextView) view.findViewById(R.id.profile_action_sheet_edit_dialog_tip);
		mTvTip.setText(R.string.profile_prize_please_input_code_tip);
		registerListener();
	}

	private void registerListener()
	{
		mBtnCancel.setOnClickListener(this);
		mBtnSure.setOnClickListener(this);
	}

	private void setDialogParams()
	{
		Window dialogWindow = getWindow();
		dialogWindow.setWindowAnimations(R.style.style_dialog_aniamtion);
		dialogWindow.setGravity(Gravity.BOTTOM);
		this.setCanceledOnTouchOutside(true);
	}

	@Override
	public void show()
	{
		try
		{
			super.show();
			if (mInput != null)
			{
				mInput.setText("");
			}
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

	@Override
	public void onClick(View view)
	{
		ACTION action = null;
		switch (view.getId())
		{
			case R.id.profile_action_sheet_edit_dialog_sure:
				action = ACTION.SURE;
				break;

			case R.id.profile_action_sheet_edit_dialog_cancle:
				action = ACTION.CANCEL;
				break;

			default:
				break;
		}
		if (mSheetClickListener != null)
		{
			closeKeyBoard();
			this.dismiss();
			mSheetClickListener.onActionSheetClick(action, mInput.getText().toString(), mPrizeInfo, this);
		}
	}

	/**
	 * 设置展位文字
	 * 
	 * @param hint
	 */
	public void setDatas(String hint, PrizeInfo prizeInfo)
	{
		mInput.setHint(hint);
		mPrizeInfo = prizeInfo;
	}

	/**
	 * 关闭键盘
	 */
	public void closeKeyBoard()
	{
		if (mInput != null)
		{
			KeyBoardUtil.closeKeybord(mInput, getContext());
		}
	}

	/**
	 * 点击时间回调
	 * 
	 * @author houjun
	 */
	public interface IonActionSheetClickListener
	{

		public void onActionSheetClick(ACTION action, String string, PrizeInfo mPrizeInfo, ProfileGetCodeSheetCode profileGetCodeSheetCode);
	}
}
