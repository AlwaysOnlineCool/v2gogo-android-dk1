package com.v2gogo.project.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.utils.common.ScreenUtil;

/**
 * 用户头像选择对话框
 * 
 * @author
 */
public class ProfileActionSheetDialog extends Dialog implements android.view.View.OnClickListener
{
	public static enum ACTION
	{
		FIRST_ITEM, SECOND_ITEM, CANCEL
	}

	private View mContentView;
	private boolean mIsInitWidth = false;
	private TextView mTvTip;
	private Button mBtnCancel;
	private Button mBtnFirstItem;
	private Button mBtnSecondItem;

	private IonActionSheetClickListener mSheetClickListener;

	public ProfileActionSheetDialog(Context context, boolean cancelable, OnCancelListener cancelListener)
	{
		super(context, cancelable, cancelListener);
		setDialogParams();
	}

	public ProfileActionSheetDialog(Context context, int theme)
	{
		super(context, theme);
		setDialogParams();
	}

	public ProfileActionSheetDialog(Context context)
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
			mContentView = getLayoutInflater().inflate(R.layout.profile_action_sheet_dialog_layout, null);
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
		mTvTip = (TextView) view.findViewById(R.id.profile_action_sheet_dialog_tip);
		mBtnCancel = (Button) view.findViewById(R.id.profile_action_sheet_dialog_cancle_item);
		mBtnFirstItem = (Button) view.findViewById(R.id.profile_action_sheet_dialog_first_item);
		mBtnSecondItem = (Button) view.findViewById(R.id.profile_action_sheet_dialog_second_item);
		registerListener();
	}

	private void registerListener()
	{
		mBtnCancel.setOnClickListener(this);
		mBtnFirstItem.setOnClickListener(this);
		mBtnSecondItem.setOnClickListener(this);
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

	/**
	 * 设置显示文字
	 * 
	 * @param firstTips
	 * @param secondTips
	 */
	public void setTips(String tip, String firstTips, String secondTips)
	{
		mTvTip.setText(tip);
		if (TextUtils.isEmpty(firstTips))
		{
			mBtnFirstItem.setVisibility(View.GONE);
		}
		else
		{
			mBtnFirstItem.setVisibility(View.VISIBLE);
			mBtnFirstItem.setText(firstTips);
		}
		if (TextUtils.isEmpty(secondTips))
		{
			mBtnSecondItem.setVisibility(View.GONE);
		}
		else
		{
			mBtnSecondItem.setVisibility(View.VISIBLE);
			mBtnSecondItem.setText(secondTips);
		}
	}

	/**
	 * 设置显示文字
	 * 
	 * @param firstTips
	 * @param secondTips
	 */
	public void setTips(String tip)
	{
		mTvTip.setText(tip);
	}

	/**
	 * 设置显示文字
	 * 
	 * @param firstTips
	 * @param secondTips
	 */
	public void setTips(int tip, int firstTips, int secondTips)
	{
		String firstTip = getContext().getString(firstTips);
		String secondTip = getContext().getString(secondTips);
		String tipString = getContext().getString(tip);
		setTips(tipString, firstTip, secondTip);
	}

	/**
	 * 点击时间回调
	 * 
	 * @author houjun
	 */
	public interface IonActionSheetClickListener
	{
		public void onClickListener(ACTION action, ProfileActionSheetDialog profileActionSheetDialog);
	}

	@Override
	public void onClick(View view)
	{
		ACTION action = null;
		switch (view.getId())
		{
			case R.id.profile_action_sheet_dialog_cancle_item:
				action = ACTION.CANCEL;
				break;

			case R.id.profile_action_sheet_dialog_first_item:
				action = ACTION.FIRST_ITEM;
				break;

			case R.id.profile_action_sheet_dialog_second_item:
				action = ACTION.SECOND_ITEM;
				break;

			default:
				break;
		}
		if (mSheetClickListener != null)
		{
			mSheetClickListener.onClickListener(action, this);
			this.dismiss();
		}
	}
}
