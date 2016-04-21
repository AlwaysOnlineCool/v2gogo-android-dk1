package com.v2gogo.project.activity.account;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.BaseActivity;
import com.v2gogo.project.db.MatserInfo;
import com.v2gogo.project.manager.account.AccountRegisterManager;
import com.v2gogo.project.manager.account.AccountRegisterManager.IAccountRegisterAccountCallback;
import com.v2gogo.project.utils.common.KeyBoardUtil;
import com.v2gogo.project.utils.common.ToastUtil;

/**
 * 账号注册
 * 
 * @author houjun
 */
public class AccountRegisterActivity extends BaseActivity implements OnClickListener
{

	public static final String CODE = "code";
	public static final String PHONE = "phone";

	private String mCode;
	private String mPhone;

	private EditText mInvCode;
	private EditText mPassword;
	private EditText mConfirmPassord;
	private Button mBtnRegister;

	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.account_register_activity_register:
				registerAccount();
				break;

			default:
				break;
		}
	}

	@Override
	public void onInitViews()
	{
		mPassword = (EditText) findViewById(R.id.account_register_activity_new_pwd);
		mConfirmPassord = (EditText) findViewById(R.id.account_register_activity_confirm_pwd);
		mBtnRegister = (Button) findViewById(R.id.account_register_activity_register);
		mInvCode = (EditText) findViewById(R.id.account_register_code_invcode);
	}

	@Override
	protected void onInitIntentData(Intent intent)
	{
		super.onInitIntentData(intent);
		if (null != intent)
		{
			mCode = intent.getStringExtra(CODE);
			mPhone = intent.getStringExtra(PHONE);
		}
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.account_register_activity_layout;
	}

	@Override
	protected void registerListener()
	{
		super.registerListener();
		mBtnRegister.setOnClickListener(this);
	}

	/**
	 * 注册用户
	 */
	private void registerAccount()
	{
		if (TextUtils.isEmpty(mConfirmPassord.getText().toString()) || TextUtils.isEmpty(mPassword.getText().toString()))
		{
			ToastUtil.showAlertToast(this, R.string.passeord_empty_tip);
			return;
		}
		String passwordString = mPassword.getText().toString().trim();
		String confirmPasswordString = mConfirmPassord.getText().toString().trim();
		if (!passwordString.equals(confirmPasswordString))
		{
			ToastUtil.showAlertToast(this, R.string.passeord_diff_tip);
			return;
		}
		if (passwordString.length() < 6 && passwordString.length() > 16 || confirmPasswordString.length() < 6 || confirmPasswordString.length() > 16)
		{
			ToastUtil.showAlertToast(this, R.string.password_length_tip);
			return;
		}
		if (!TextUtils.isEmpty(mCode) && !TextUtils.isEmpty(mPhone))
		{
			showLoadingDialog(R.string.registering_tip);
			String invCode = mInvCode.getText().toString();
			if (!TextUtils.isEmpty(invCode))
			{
				invCode = invCode.trim();
			}
			else
			{
				invCode = "";
			}
			closeKeyBoard();
			AccountRegisterManager.registerAccount(mPhone, passwordString, mCode, invCode, new IAccountRegisterAccountCallback()
			{
				@Override
				public void onAccountRegisterSuccess(MatserInfo matserInfo)
				{
					setResult(RESULT_OK);
					closeKeyBoard();
					finish();
				}

				@Override
				public void onAccountRegisterFail(String errorMessage)
				{
					dismissLoadingDialog();
					ToastUtil.showAlertToast(AccountRegisterActivity.this, errorMessage);
				}
			});
		}
	}

	/**
	 * 关闭键盘
	 */
	private void closeKeyBoard()
	{
		EditText editText = null;
		if (mConfirmPassord.isFocused())
		{
			editText = mConfirmPassord;
		}
		if (mPassword.isFocused())
		{
			editText = mPassword;
		}
		if (mInvCode.isFocused())
		{
			editText = mInvCode;
		}
		if (null != editText)
		{
			KeyBoardUtil.closeKeybord(editText, this);
		}
	}

	@Override
	public void clearRequestTask()
	{
		closeKeyBoard();
		AccountRegisterManager.clearRegisterAccountTask();
	}
}
