package com.v2gogo.project.views.dialog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.R.integer;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.v2gogo.project.R;
import com.v2gogo.project.dk.ChangeAddressDialog;
import com.v2gogo.project.dk.ChangeAddressDialog.OnAddressCListener;
import com.v2gogo.project.domain.shop.ReceiverInfos;
import com.v2gogo.project.utils.common.KeyBoardUtil;
import com.v2gogo.project.utils.common.ScreenUtil;
import com.v2gogo.project.utils.common.ToastUtil;
import com.v2gogo.project.views.logic.OrderCommitAddressEditLayout;

public class CommitOrderAddressEditDialog extends Dialog implements android.view.View.OnClickListener
{
	public static enum ACTION
	{
		SURE, CANCEL
	}

	private View mContentView;

	private TextView mTvTip;
	private Button mBtnSure;
	private Button mBtnCancel;
	private EditText mInputName;
	private EditText mInputPhone;
	private EditText mInputAddress;
	private EditText mChangeAddress;

	private ReceiverInfos receiverInfos;
	private Activity activity;
	private String afterAddress;

	private boolean mIsInitWidth = false;

	private IonCommitOrderAddressEditCallback mOrderAddressEditCallback;

	private Context context;

	public CommitOrderAddressEditDialog(Context context, boolean cancelable, OnCancelListener cancelListener)
	{
		super(context, cancelable, cancelListener);
		this.context = context;
		setDialogParams();
	}

	public CommitOrderAddressEditDialog(Context context, int theme)
	{
		super(context, theme);
		this.context = context;
		setDialogParams();
	}

	public CommitOrderAddressEditDialog(Activity activity, Context context, int theme, ReceiverInfos receiverInfos)
	{
		super(context, theme);
		this.context = context;
		this.receiverInfos = receiverInfos;
		this.activity = activity;
		setDialogParams();
	}

	public CommitOrderAddressEditDialog(Context context)
	{
		super(context);
		this.context = context;
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

	@Override
	public void onClick(View view)
	{
		ACTION action = null;
		switch (view.getId())
		{
			case R.id.profile_action_sheet_edit_dialog_sure:
				action = ACTION.SURE;
				// 收件人姓名非空判断
				if (TextUtils.isEmpty(mInputName.getText().toString()))
				{
					ToastUtil.showAlertToast(activity, R.string.commit_order_commit_receiver_name_tip);
					return;
				}
				// 收件人电话号码非空判断
				if (TextUtils.isEmpty(mInputPhone.getText().toString()))
				{
					ToastUtil.showAlertToast(activity, R.string.commit_order_commit_receiver_phone_tip);
					return;
				}
				// 收件人电话号码合法性判断
				if (!isMobileNO())
				{
					ToastUtil.showAlertToast(activity, R.string.commit_order_commit_receiver_phoneRight_tip);
					return;
				}
				// 收件人地址非空判断
				if (TextUtils.isEmpty(mInputAddress.getText().toString()))
				{
					ToastUtil.showAlertToast(activity, R.string.commit_order_commit_receiver_address_tip);
					return;
				}

				if (TextUtils.isEmpty(mChangeAddress.getText().toString()))
				{
					ToastUtil.showAlertToast(activity, "请选择收货地区");
					return;
				}
				break;

			case R.id.profile_action_sheet_edit_dialog_cancle:
				action = ACTION.CANCEL;
				break;

			default:
				break;
		}
		if (mOrderAddressEditCallback != null)
		{
			closeKeyBoard();
			this.dismiss();
			mOrderAddressEditCallback.onCommitOrderAddress(action, mInputName.getText().toString(), mInputPhone.getText().toString(), mChangeAddress.getText()
					+ mInputAddress.getText().toString(), this);
		}
	}

	public boolean isMobileNO()
	{

		Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");

		Matcher m = p.matcher(mInputPhone.getText().toString());

		// System.out.println(m.matches() + "---");

		return m.matches();
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

	public void setOnOrderAddressEdit(IonCommitOrderAddressEditCallback mOrderAddressEditCallback)
	{
		this.mOrderAddressEditCallback = mOrderAddressEditCallback;
	}

	private void initViews(View view)
	{
		mBtnCancel = (Button) view.findViewById(R.id.profile_action_sheet_edit_dialog_cancle);
		mBtnSure = (Button) view.findViewById(R.id.profile_action_sheet_edit_dialog_sure);
		mInputName = (EditText) view.findViewById(R.id.get_prize_post_input_name);
		mChangeAddress = (EditText) view.findViewById(R.id.get_prize_post_change_address);
		mInputAddress = (EditText) view.findViewById(R.id.get_prize_post_input_address);
		mInputPhone = (EditText) view.findViewById(R.id.get_prize_post_input_phonenumber);
		mInputName.setHint(R.string.commit_order_commit_receiver_name_tip);
		mInputPhone.setHint(R.string.commit_order_commit_receiver_phone_tip);
		mInputAddress.setHint(R.string.commit_order_commit_receiver_address_tip);
		mTvTip = (TextView) view.findViewById(R.id.get_prize_post_tip);
		mTvTip.setText(R.string.commit_order_receive_title_tip);

		if (receiverInfos != null)
		{
			mInputName.setText(receiverInfos.getConsignee());
			mInputPhone.setText(receiverInfos.getPhone());
			String address = receiverInfos.getAddress();
			int index;
			String detailsAddress;
			if (address.contains("区"))
			{
				index = receiverInfos.getAddress().indexOf("区");
				detailsAddress = address.substring(index + 1, address.length());
				afterAddress = address.substring(0, index + 1);
			}
			else if (address.contains("县"))
			{
				index = receiverInfos.getAddress().indexOf("县");
				detailsAddress = address.substring(index + 1, address.length());
				afterAddress = address.substring(0, index + 1);
			}
			else
			{
				index = receiverInfos.getAddress().lastIndexOf("市");
				detailsAddress = address.substring(index + 1, address.length());
				afterAddress = address.substring(0, index + 1);
			}

			if (detailsAddress != null && afterAddress != null)
			{
				mChangeAddress.setText(afterAddress);
				mInputAddress.setText(detailsAddress);
			}

		}
		// 2016-4-15 添加省市区选择弹出框 author : AlwaysOnline
		mChangeAddress.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				ChangeAddressDialog mChangeAddressDialog = new ChangeAddressDialog(context);
				String afAddress = mChangeAddress.getText().toString();
				String pAddress;
				String cAddress;
				String aAddress;
				if (afAddress != null)
				{
					int pIndex = afAddress.indexOf("省");
					int cIndex = afAddress.indexOf("市");
					if (cIndex > 5 || cIndex < 0)
					{
						 cIndex = afAddress.lastIndexOf("州");
					}
					pAddress = afAddress.substring(0, pIndex + 1);
					cAddress = afAddress.substring(pIndex + 1, cIndex + 1);
					aAddress = afAddress.substring(cIndex + 1, afAddress.length());
					mChangeAddressDialog.setAddress(pAddress, cAddress, aAddress);
				}
				mChangeAddressDialog.show();
				mChangeAddressDialog.setAddresskListener(new OnAddressCListener()
				{

					@Override
					public void onClick(String province, String city, String area)
					{
						// TODO Auto-generated method stub
						// Toast.makeText(context, province + "-" + city + "-" + area,
						// Toast.LENGTH_LONG).show();
						mChangeAddress.setText(province + city + area);
					}
				});
			}
		});
		registerListener();
	}

	/**
	 * 地址输入框监听回调
	 */
	class EditChangedListener implements TextWatcher
	{

		@Override
		public void afterTextChanged(Editable arg0)
		{

		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
		{

		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
		{

		}

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
	public interface IonCommitOrderAddressEditCallback
	{
		public void onCommitOrderAddress(ACTION action, String name, String phone, String address, CommitOrderAddressEditDialog orderAddressEditDialog);
	}

}
