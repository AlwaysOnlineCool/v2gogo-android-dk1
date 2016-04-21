package com.v2gogo.project.activity.account;

import android.content.Intent;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.BaseActivity;
import com.v2gogo.project.activity.V2gogoUserProtocolActivity;
import com.v2gogo.project.manager.account.AccountVerificationCodeManager;
import com.v2gogo.project.manager.account.AccountVerificationCodeManager.ICheckVerificationCodeCallback;
import com.v2gogo.project.manager.account.AccountVerificationCodeManager.IVerificationCodeCallback;
import com.v2gogo.project.utils.common.KeyBoardUtil;
import com.v2gogo.project.utils.common.StringUtil;
import com.v2gogo.project.utils.common.ToastUtil;

/**
 * 获取验证码
 * 
 * @author houjun
 */
public class AccountVerificationCodeActivity extends BaseActivity implements OnClickListener
{

	private final int REGISTER_REQUEST_CODE = 0x8;
	private final int COUNT_DOWN_TIME = 60 * 1000;

	private String mTip;

	private EditText mAccoutPhoneNo;
	private EditText mAccountCheckCode;
	private Button mAccountNextStep;

	private CheckBox mCheckBox;
	private TextView mTextView;
	private Button mBtnGet;
	private VerificationCodeCountDownTimer mDownTimer;

	@Override
	public void onInitViews()
	{
		mAccountNextStep = (Button) findViewById(R.id.account_verification_code_next_step);
		mTextView = (TextView) findViewById(R.id.account_verification_protocol);
		mTextView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
		mCheckBox = (CheckBox) findViewById(R.id.account_verification_code_check_box);
		mBtnGet = (Button) findViewById(R.id.account_verification_code_get_check_code);
		mAccoutPhoneNo = (EditText) findViewById(R.id.account_verification_code_phone_no);
		mAccountCheckCode = (EditText) findViewById(R.id.account_verification_code_verification_code);
		mCheckBox.setChecked(true);
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.account_verification_code_activity_layout;
	}

	@Override
	protected void registerListener()
	{
		super.registerListener();
		mTextView.setOnClickListener(this);
		mBtnGet.setOnClickListener(this);
		mAccountNextStep.setOnClickListener(this);
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
			case R.id.account_verification_code_next_step:
				verifyCheckCode();
				break;

			case R.id.account_verification_protocol:
				forwardUserPro();
				break;

			case R.id.account_verification_code_get_check_code:
				getCheckCode();
				break;

			default:
				break;
		}
	}

	@Override
	public void onBackPressed()
	{
		setResult(RESULT_CANCELED);
		super.onBackPressed();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REGISTER_REQUEST_CODE && resultCode == RESULT_OK)
		{
			closeKeyBoard();
			setResult(RESULT_OK);
			finish();
		}
	}

	@Override
	public void clearRequestTask()
	{
		closeKeyBoard();
	}

	/**
	 * 关闭键盘
	 */
	private void closeKeyBoard()
	{
		EditText editText = null;
		if (mAccoutPhoneNo.isFocused())
		{
			editText = mAccoutPhoneNo;
		}
		if (mAccountCheckCode.isFocused())
		{
			editText = mAccountCheckCode;
		}
		if (null != editText)
		{
			KeyBoardUtil.closeKeybord(editText, this);
		}
	}

	/**
	 * 验证验证码
	 */
	private void verifyCheckCode()
	{
		if (!mCheckBox.isChecked())
		{
			ToastUtil.showAlertToast(this, R.string.please_read_v2gogo_tip);
		}
		else
		{
			String code = mAccountCheckCode.getText().toString();
			if (TextUtils.isEmpty(code))
			{
				ToastUtil.showAlertToast(this, R.string.input_check_code_tip);
			}
			else
			{
				code = code.trim();
				String phone = mAccoutPhoneNo.getText().toString();
				if (TextUtils.isEmpty(phone))
				{
					ToastUtil.showAlertToast(this, R.string.please_input_check_code_tip);
				}
				else
				{
					phone = phone.trim();
					AccountVerificationCodeManager.checkVerificationCodeRequest(phone, code, new ICheckVerificationCodeCallback()
					{
						@Override
						public void onCheckVerificationCodeSuccess()
						{
							Intent intent = new Intent(AccountVerificationCodeActivity.this, AccountRegisterActivity.class);
							intent.putExtra(AccountRegisterActivity.CODE, mAccountCheckCode.getText().toString() != null ? mAccountCheckCode.getText()
									.toString().trim() : mAccountCheckCode.getText().toString());
							intent.putExtra(AccountRegisterActivity.PHONE, mAccoutPhoneNo.getText().toString() != null ? mAccoutPhoneNo.getText().toString()
									.trim() : mAccoutPhoneNo.getText().toString());
							startActivityForResult(intent, REGISTER_REQUEST_CODE);
							closeKeyBoard();
						}

						@Override
						public void onCheckVerificationCodeFail(String errorMessage)
						{
							ToastUtil.showAlertToast(AccountVerificationCodeActivity.this, errorMessage);
						}
					});
					closeKeyBoard();
				}
			}
		}
	}

	/**
	 * 跳转到用户协议
	 */
	private void forwardUserPro()
	{
		Intent intent;
		intent = new Intent(this, V2gogoUserProtocolActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	/**
	 * 获取验证码
	 */
	private void getCheckCode()
	{
		String phone = mAccoutPhoneNo.getText().toString();
		if (TextUtils.isEmpty(phone))
		{
			ToastUtil.showAlertToast(this, R.string.please_input_check_code_tip);
		}
		else
		{
			phone = phone.trim();
			if (StringUtil.isMobileNO(phone))
			{
				getCheckCode(phone);
				mDownTimer = new VerificationCodeCountDownTimer(COUNT_DOWN_TIME, 1000, mBtnGet);
				mDownTimer.start();
			}
			else
			{
				ToastUtil.showAlertToast(this, R.string.please_input_hefan_phone);
			}
		}
	}

	/**
	 * 得到验证码
	 * 
	 * @param phone
	 */
	private void getCheckCode(String phone)
	{
		AccountVerificationCodeManager.lunachVerificationCodeRequest(phone, new IVerificationCodeCallback()
		{
			@Override
			public void onVerificationCodeSuccess()
			{
				mAccoutPhoneNo.clearFocus();
				mAccountCheckCode.requestFocus();
			}

			@Override
			public void onVerificationCodeFail(String errorMessage)
			{
				ToastUtil.showAlertToast(AccountVerificationCodeActivity.this, errorMessage);
				mBtnGet.setEnabled(true);
				mBtnGet.setText(mTip);
				if (mDownTimer != null)
				{
					mDownTimer.cancel();
				}
			}
		});
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

}
