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

import com.v2gogo.project.R;
import com.v2gogo.project.domain.exchange.PrizeInfo;
import com.v2gogo.project.utils.common.KeyBoardUtil;
import com.v2gogo.project.utils.common.ScreenUtil;

/**
 * 领取金币的对话框
 * 
 * @author houjun
 */
public class ProfilePrizePostActionSheet extends Dialog implements android.view.View.OnClickListener
{

	public static enum ACTION
	{
		SURE, CANCEL
	}

	private View mContentView;
	private Button mBtnSure;
	private Button mBtnCancel;
	private EditText mInputName;
	private EditText mInputPhone;
	private EditText mInputAddress;

	private PrizeInfo mPrizeInfo;

	private boolean mIsInitWidth = false;
	private IonPostPrizeActionSheetClickListener mSheetClickListener;

	public ProfilePrizePostActionSheet(Context context, boolean cancelable, OnCancelListener cancelListener)
	{
		super(context, cancelable, cancelListener);
		setDialogParams();
	}

	public ProfilePrizePostActionSheet(Context context, int theme)
	{
		super(context, theme);
		setDialogParams();
	}

	public ProfilePrizePostActionSheet(Context context)
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
			mContentView = getLayoutInflater().inflate(R.layout.get_prize_post_layout, null);
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

	public void setSheetClickListener(IonPostPrizeActionSheetClickListener mSheetClickListener)
	{
		this.mSheetClickListener = mSheetClickListener;
	}

	private void initViews(View view)
	{
		mBtnCancel = (Button) view.findViewById(R.id.profile_action_sheet_edit_dialog_cancle);
		mBtnSure = (Button) view.findViewById(R.id.profile_action_sheet_edit_dialog_sure);
		mInputName = (EditText) view.findViewById(R.id.get_prize_post_input_name);
		mInputAddress = (EditText) view.findViewById(R.id.get_prize_post_input_address);
		mInputPhone = (EditText) view.findViewById(R.id.get_prize_post_input_phonenumber);
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
			if (mInputName != null)
			{
				mInputName.setText("");
				mInputName.setHint(R.string.please_input_get_username_tip);
			}
			if (mInputAddress != null)
			{
				mInputAddress.setText("");
				mInputAddress.setHint(R.string.please_input_get_address_tip);
			}
			if (mInputPhone != null)
			{
				mInputPhone.setText("");
				mInputPhone.setHint(R.string.please_input_get_phone_tip);
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
				this.dismiss();
				break;

			case R.id.profile_action_sheet_edit_dialog_cancle:
				action = ACTION.CANCEL;
				this.dismiss();
				break;

			default:
				break;
		}
		if (mSheetClickListener != null)
		{
			closeKeyBoard();
			mSheetClickListener.onPostPrizeActionListener(action, mInputName.getText().toString(), mInputPhone.getText().toString(), mInputAddress.getText()
					.toString(), mPrizeInfo, this);
		}
	}

	/**
	 * 设置展位文字
	 * 
	 * @param hint
	 */
	public void setDatas(PrizeInfo prizeInfo)
	{
		mPrizeInfo = prizeInfo;
	}

	/**
	 * 关闭键盘
	 */
	public void closeKeyBoard()
	{
		EditText editText = null;
		if (mInputAddress.isFocused())
		{
			editText = mInputAddress;
		}
		if (mInputName.isFocused())
		{
			editText = mInputName;
		}
		if (mInputPhone.isFocused())
		{
			editText = mInputPhone;
		}
		if (editText != null)
		{
			KeyBoardUtil.closeKeybord(editText, getContext());
		}
	}

	/**
	 * 点击时间回调
	 * 
	 * @author houjun
	 */
	public interface IonPostPrizeActionSheetClickListener
	{
		public void onPostPrizeActionListener(ACTION action, String name, String phone, String address, PrizeInfo prizeInfo,
				ProfilePrizePostActionSheet profilePrizePostSheetCode);
	}
}
