package com.v2gogo.project.views.dialog;

import io.vov.vitamio.utils.Log;
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
 * 功能：爆料弹框
 * 
 * @ahthor：黄荣星
 * @date:2016-3-30
 * @version::V1.0
 */
public class HomeFactChooseDialog extends Dialog implements android.view.View.OnClickListener
{
	public static enum HomeFactACTION
	{
		FIRST_ITEM, SECOND_ITEM, THIRD_ITEM, FOURTH_ITEM, FIFTH_ITEM, CANCEL
	}

	private View mContentView;
	private boolean mIsInitWidth = false;
	private TextView mTvTip;
	private Button mBtnCancel;
	private Button mBtnFirstItem;
	private Button mBtnSecondItem;
	private Button mBtnThirdItem;
	private Button mBtnFourthItem;
	private Button mBtnFifthItem;

	private int subType = -1;

	private IonActionHomeFactSheetClickListener mSheetClickListener;

	public HomeFactChooseDialog(Context context, boolean cancelable, OnCancelListener cancelListener)
	{
		super(context, cancelable, cancelListener);
		setDialogParams();
	}
	//2016-4-13  新增参数subType用于多媒体上传时判断显示弹出框按钮个数  author: AlwaysOnline
	public HomeFactChooseDialog(Context context, int theme, int subType)
	{
		super(context, theme);
		this.subType = subType;
		setDialogParams();
	}

	public HomeFactChooseDialog(Context context)
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
			mContentView = getLayoutInflater().inflate(R.layout.home_fact_action_sheet_dialog_layout, null);
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

	public void setSheetClickListener(IonActionHomeFactSheetClickListener mSheetClickListener)
	{
		this.mSheetClickListener = mSheetClickListener;
	}

	private void initViews(View view)
	{
		mTvTip = (TextView) view.findViewById(R.id.profile_action_sheet_dialog_tip);
		mBtnCancel = (Button) view.findViewById(R.id.profile_action_sheet_dialog_cancle_item);
		mBtnFirstItem = (Button) view.findViewById(R.id.profile_action_sheet_dialog_first_item);
		mBtnSecondItem = (Button) view.findViewById(R.id.profile_action_sheet_dialog_second_item);
		mBtnThirdItem = (Button) view.findViewById(R.id.profile_action_sheet_dialog_third_item);
		mBtnFourthItem = (Button) view.findViewById(R.id.profile_action_sheet_dialog_fourth_item);
		mBtnFifthItem = (Button) view.findViewById(R.id.profile_action_sheet_dialog_fifth_item);

		/*2016-4-13  新增参数subType用于多媒体上传时判断显示弹出框按钮个数  author: AlwaysOnline
		 * 101--148行
		 * 为新增多媒体上传时弹出框现实个数判断
		 * */
		if (subType != -1)
		{
			HomeFactACTION action = null;
			mBtnFirstItem.setVisibility(View.GONE);
			mBtnSecondItem.setVisibility(View.GONE);
			mBtnThirdItem.setVisibility(View.GONE);
			mBtnFourthItem.setVisibility(View.GONE);
			mBtnFifthItem.setVisibility(View.GONE);
			int flag = 0;
			if ((subType & 1 << 0) != 0)
			{
				mBtnFirstItem.setVisibility(View.VISIBLE);
				action = HomeFactACTION.FIRST_ITEM;
				flag = flag + 1;
			}
			if ((subType & 1 << 1) != 0)
			{
				mBtnSecondItem.setVisibility(View.VISIBLE);
				action = HomeFactACTION.SECOND_ITEM;
				flag = flag + 1;
			}
			if ((subType & 1 << 2) != 0)
			{
				mBtnThirdItem.setVisibility(View.VISIBLE);
				action = HomeFactACTION.THIRD_ITEM;
				flag = flag + 1;
			}
			if ((subType & 1 << 3) != 0)
			{
				mBtnFourthItem.setVisibility(View.VISIBLE);
				action = HomeFactACTION.FOURTH_ITEM;
				flag = flag + 1;
			}
			if ((subType & 1 << 4) != 0)
			{
				mBtnFifthItem.setVisibility(View.VISIBLE);
				action = HomeFactACTION.FIFTH_ITEM;
				flag = flag + 1;
			}
			if (mSheetClickListener != null && flag == 1)
			{
				mSheetClickListener.onClickItemListener(action, this);
				this.dismiss();
			}
		}
		registerListener();
	}

	private void registerListener()
	{
		mBtnCancel.setOnClickListener(this);
		mBtnFirstItem.setOnClickListener(this);
		mBtnSecondItem.setOnClickListener(this);
		mBtnThirdItem.setOnClickListener(this);
		mBtnFourthItem.setOnClickListener(this);
		mBtnFifthItem.setOnClickListener(this);
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
	 * 第一项显示文字 及是否可见
	 * 
	 * @param tips
	 * @param isVisible
	 */
	public void setFirstTips(String tips, boolean isVisible)
	{
		mBtnFirstItem.setText(tips);
		if (isVisible)
		{
			mBtnFirstItem.setVisibility(View.VISIBLE);
		}
		else
		{
			mBtnFirstItem.setVisibility(View.GONE);
		}
	}

	/**
	 * 第二项显示文字 及是否可见
	 * 
	 * @param tips
	 * @param isVisible
	 */
	public void setSecondTips(String tips, boolean isVisible)
	{
		mBtnSecondItem.setText(tips);
		if (isVisible)
		{
			mBtnSecondItem.setVisibility(View.VISIBLE);
		}
		else
		{
			mBtnSecondItem.setVisibility(View.GONE);
		}
	}

	/**
	 * 第三项显示文字 及是否可见
	 * 
	 * @param tips
	 * @param isVisible
	 */
	public void setThirdTips(String tips, boolean isVisible)
	{
		mBtnThirdItem.setText(tips);
		if (isVisible)
		{
			mBtnThirdItem.setVisibility(View.VISIBLE);
		}
		else
		{
			mBtnThirdItem.setVisibility(View.GONE);
		}
	}

	/**
	 * 第四项显示文字 及是否可见
	 * 
	 * @param tips
	 * @param isVisible
	 */
	public void setFourthTips(String tips, boolean isVisible)
	{
		mBtnFourthItem.setText(tips);
		if (isVisible)
		{
			mBtnFourthItem.setVisibility(View.VISIBLE);
		}
		else
		{
			mBtnFourthItem.setVisibility(View.GONE);
		}
	}

	/**
	 * 第四项显示文字 及是否可见
	 * 
	 * @param tips
	 * @param isVisible
	 */
	public void setFifthTips(String tips, boolean isVisible)
	{
		mBtnFifthItem.setText(tips);
		if (isVisible)
		{
			mBtnFifthItem.setVisibility(View.VISIBLE);
		}
		else
		{
			mBtnFifthItem.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置显示文字
	 * 
	 * @param firstTips
	 * @param secondTips
	 */
	public void setTitleTips(String tip)
	{
		mTvTip.setText(tip);
	}

	/**
	 * 点击时间回调
	 * 
	 * @author houjun
	 */
	public interface IonActionHomeFactSheetClickListener
	{
		public void onClickItemListener(HomeFactACTION action, HomeFactChooseDialog profileActionSheetDialog);
	}

	@Override
	public void onClick(View view)
	{
		HomeFactACTION action = null;
		switch (view.getId())
		{
			case R.id.profile_action_sheet_dialog_cancle_item:
				action = HomeFactACTION.CANCEL;
				break;

			case R.id.profile_action_sheet_dialog_first_item:
				action = HomeFactACTION.FIRST_ITEM;
				break;

			case R.id.profile_action_sheet_dialog_second_item:
				action = HomeFactACTION.SECOND_ITEM;
				break;
			case R.id.profile_action_sheet_dialog_third_item:
				action = HomeFactACTION.THIRD_ITEM;
				break;
			case R.id.profile_action_sheet_dialog_fourth_item:
				action = HomeFactACTION.FOURTH_ITEM;
				break;
			case R.id.profile_action_sheet_dialog_fifth_item:
				action = HomeFactACTION.FIFTH_ITEM;
				break;

			default:
				break;
		}
		if (mSheetClickListener != null)
		{
			mSheetClickListener.onClickItemListener(action, this);
			this.dismiss();
		}
	}
}
