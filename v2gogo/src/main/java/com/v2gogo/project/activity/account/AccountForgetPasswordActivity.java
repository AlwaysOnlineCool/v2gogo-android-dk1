package com.v2gogo.project.activity.account;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.BaseActivity;
import com.v2gogo.project.manager.account.AccountPasswordManager;
import com.v2gogo.project.manager.account.AccountPasswordManager.IonAccountPasswordCheckCode;
import com.v2gogo.project.manager.account.AccountPasswordManager.IonforgetAccountPasswordCallback;
import com.v2gogo.project.utils.common.KeyBoardUtil;
import com.v2gogo.project.utils.common.ToastUtil;
import com.v2gogo.project.views.dialog.AppNoticeDialog;
import com.v2gogo.project.views.dialog.AppNoticeDialog.IonClickSureCallback;

/**
 * 忘记密码
 * 
 * @author houjun
 */
public class AccountForgetPasswordActivity extends BaseActivity implements OnClickListener, IonClickSureCallback
{

	private final int COUNT_DOWN_TIME = 60 * 1000;

	private EditText mPhone;
	private EditText mCode;
	private EditText mPassword;
	private EditText mConfirm;
	private Button mBtn;
	private Button mBtnModify;

	private String mTip;
	private String mPhoneString;
	private VerificationCodeCountDownTimer mCountDownTimer;

	private AppNoticeDialog mAppNoticeDialog;

	@Override
	public void onInitViews()
	{
		mPhone = (EditText) findViewById(R.id.account_forget_password_activity_username);
		mCode = (EditText) findViewById(R.id.account_forget_password_input_check_code);
		mConfirm = (EditText) findViewById(R.id.account_forget_password_input_confirm_pwd);
		mPassword = (EditText) findViewById(R.id.account_forget_password_input_new_pwd);
		mPhone = (EditText) findViewById(R.id.account_forget_password_activity_username);
		mBtn = (Button) findViewById(R.id.account_forget_password_get_check_code);
		mBtnModify = (Button) findViewById(R.id.account_forget_password_modify_pwd);
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.account_forget_password_activity_layout;
	}

	@Override
	protected void registerListener()
	{
		super.registerListener();
		mBtn.setOnClickListener(this);
		mBtnModify.setOnClickListener(this);
	}

	@Override
	protected void onInitLoadDatas()
	{
		super.onInitLoadDatas();
		mTip = getString(R.string.get_check_code_tip);
	}

	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.account_forget_password_get_check_code:
				getCheckCode();
				break;

			case R.id.account_forget_password_modify_pwd:
				forgetPassword();
				break;

			default:
				break;
		}
	}

	@Override
	public void onClickSure()
	{
		finish();
	}

	private void forgetPassword()
	{
		String code = mCode.getText().toString();
		if (!TextUtils.isEmpty(code) && TextUtils.isDigitsOnly(code.trim()))
		{
			code = code.trim();
		}
		else
		{
			ToastUtil.showAlertToast(this, R.string.please_input_check_code_tip);
			return;
		}
		String newPwd = mPassword.getText().toString();
		if (!TextUtils.isEmpty(newPwd))
		{
			newPwd = newPwd.trim();
		}
		else
		{
			ToastUtil.showAlertToast(this, R.string.passeord_empty_tip);
			return;
		}
		String confirmPassword = mConfirm.getText().toString();
		if (!TextUtils.isEmpty(confirmPassword))
		{
			confirmPassword = confirmPassword.trim();
		}
		else
		{
			ToastUtil.showAlertToast(this, R.string.passeord_empty_tip);
			return;
		}
		if (!confirmPassword.equals(newPwd))
		{
			ToastUtil.showAlertToast(this, R.string.passeord_diff_tip);
			return;
		}
		if (newPwd.length() < 6 && newPwd.length() > 16 || confirmPassword.length() < 6 || confirmPassword.length() > 16)
		{
			ToastUtil.showAlertToast(this, R.string.password_length_tip);
			return;
		}
		closeKeyBoard();
		forgetAccountPassword(code, newPwd);
	}

	/**
	 * 忘记密码
	 * 
	 * @param code
	 * @param newPwd
	 */
	private void forgetAccountPassword(String code, String newPwd)
	{
		AccountPasswordManager.forgetAccountPassword(code, mPhoneString, newPwd, new IonforgetAccountPasswordCallback()
		{
			@Override
			public void onforgetAccountPasswordSuccess()
			{
				if (mAppNoticeDialog == null)
				{
					mAppNoticeDialog = new AppNoticeDialog(AccountForgetPasswordActivity.this, R.style.style_action_sheet_dialog);
					mAppNoticeDialog.setCancelable(false);
					mAppNoticeDialog.setOnSureCallback(AccountForgetPasswordActivity.this);
					mAppNoticeDialog.setCanceledOnTouchOutside(false);
				}
				if (!mAppNoticeDialog.isShowing())
				{
					mAppNoticeDialog.show();
					mAppNoticeDialog.setSureTitleAndMessage(R.string.reset_pwd_success_tip, R.string.app_notice_sure_tip);
				}
			}

			@Override
			public void onforgetAccountPasswordFail(String errorMessage)
			{
				ToastUtil.showAlertToast(AccountForgetPasswordActivity.this, errorMessage);
			}
		});
	}

	/**
	 * 获取验证码
	 */
	private void getCheckCode()
	{
		String phone = mPhone.getText().toString();
		if (!TextUtils.isEmpty(phone))
		{
			mPhoneString = phone.trim();
			if (TextUtils.isDigitsOnly(mPhoneString) && mPhoneString.length() == 11)
			{
				closeKeyBoard();
				AccountPasswordManager.forgetAccountPasswordCheckCode(mPhoneString, new IonAccountPasswordCheckCode()
				{
					@Override
					public void onAccountPasswordCheckCodeSuccess()
					{
						mPhone.clearFocus();
						mCode.requestFocus();
					}

					@Override
					public void onAccountPasswordCheckCodeFail(String errorMessage)
					{
						mBtn.setEnabled(true);
						mBtn.setText(mTip);
						mCountDownTimer.cancel();
						ToastUtil.showAlertToast(AccountForgetPasswordActivity.this, errorMessage);
					}
				});
				mCountDownTimer = new VerificationCodeCountDownTimer(COUNT_DOWN_TIME, 1000, mBtn);
				mCountDownTimer.start();
			}
			else
			{
				ToastUtil.showAlertToast(this, R.string.please_input_you_phone_tip);
			}
		}
		else
		{
			ToastUtil.showAlertToast(this, R.string.please_input_you_phone_tip);
		}
	}

	private void closeKeyBoard()
	{
		EditText editText = null;
		if (mPassword.isFocused())
		{
			editText = mPassword;
		}
		else if (mPhone.isFocused())
		{
			editText = mPhone;
		}
		else if (mConfirm.isFocused())
		{
			editText = mConfirm;
		}
		else if (mCode.isFocused())
		{
			editText = mCode;
		}
		if (editText != null)
		{
			KeyBoardUtil.closeKeybord(editText, this);
		}
	}

	/**
	 * 获取验证码倒计时
	 * 
	 * @author houjun
	 */
	private final class VerificationCodeCountDownTimer extends CountDownTimer
	{
		private Button mBtn;

		public VerificationCodeCountDownTimer(long millisInFuture, long countDownInterval, Button button)
		{
			this(millisInFuture, countDownInterval);
			mBtn = button;
			mBtn.setEnabled(false);
		}

		private VerificationCodeCountDownTimer(long millisInFuture, long countDownInterval)
		{
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish()
		{
			mBtn.setEnabled(true);
			mBtn.setText(mTip);
			this.cancel();
		}

		@Override
		public void onTick(long millisInFutureLeft)
		{
			if (null != mBtn)
			{
				mBtn.setText(mTip + "(" + millisInFutureLeft / 1000 + ")");
			}
		}
	}

	@Override
	public void clearRequestTask()
	{
		closeKeyBoard();
		if (mCountDownTimer != null)
		{
			mCountDownTimer.cancel();
		}
		AccountPasswordManager.clearForgetAccountPasswordCheckCode();
	}
}
