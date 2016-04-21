package com.v2gogo.project.activity.account;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.BaseActivity;
import com.v2gogo.project.db.MatserInfo;
import com.v2gogo.project.manager.account.AccountLoginManager;
import com.v2gogo.project.manager.account.AccountLoginManager.IAccountLoginCallback;
import com.v2gogo.project.utils.common.KeyBoardUtil;
import com.v2gogo.project.utils.common.SPUtil;
import com.v2gogo.project.utils.common.ToastUtil;
import com.v2gogo.project.utils.http.Constants;

/**
 * 账号登录
 * 
 * @author houjun
 */
public class AccountLoginActivity extends BaseActivity implements OnClickListener
{

	private final int REGISTER_REQUEST_CODE = 0X7;

	private Button mAccountLogin;
	private Button mBtnForgetPassword;
	private Button mBtnRegisterAccount;

	private EditText mAccountLoginName;
	private EditText mAccountLoginPassord;

	@Override
	public void onInitViews()
	{
		mAccountLogin = (Button) findViewById(R.id.login_activity_account_login);
		mAccountLoginName = (EditText) findViewById(R.id.login_activity_account_name);
		mBtnForgetPassword = (Button) findViewById(R.id.login_activity_forget_password);
		mBtnRegisterAccount = (Button) findViewById(R.id.login_activity_register_account);
		mAccountLoginPassord = (EditText) findViewById(R.id.login_activity_account_password);
		mAccountLoginName.setText((String) (SPUtil.get(this, Constants.USER_NAME, "")));
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.account_login_activity_layout;
	}

	@Override
	protected void registerListener()
	{
		super.registerListener();
		mAccountLogin.setOnClickListener(this);
		mBtnForgetPassword.setOnClickListener(this);
		mBtnRegisterAccount.setOnClickListener(this);
	}

	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.login_activity_account_login:
				if (!checkInpurParams())
				{
					showLoadingDialog(R.string.logining_tip);
					String userName = mAccountLoginName.getText().toString().trim();
					String userPassword = mAccountLoginPassord.getText().toString().trim();
					AccountLoginManager.accountLogin(userName, userPassword, false, new IAccountLoginCallback()
					{
						@Override
						public void onAccountLoginSuccess(MatserInfo masterInfo)
						{
							dismissLoadingDialog();
							finish();
						}

						@Override
						public void onAccountLoginFail(String erroMessage)
						{
							dismissLoadingDialog();
							ToastUtil.showAlertToast(AccountLoginActivity.this, erroMessage);
						}
					});
					closeKeyBoard();
				}
				break;

			case R.id.login_activity_register_account:
				Intent intent = new Intent(this, AccountVerificationCodeActivity.class);
				startActivityForResult(intent, REGISTER_REQUEST_CODE);
				closeKeyBoard();
				break;

			case R.id.login_activity_forget_password:
				Intent intent1 = new Intent(this, AccountForgetPasswordActivity.class);
				startActivity(intent1);
				closeKeyBoard();
				break;

			default:
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int arg1, Intent arg2)
	{
		super.onActivityResult(requestCode, arg1, arg2);
		if (requestCode == REGISTER_REQUEST_CODE && arg1 == RESULT_OK)
		{
			closeKeyBoard();
			finish();
		}
	}

	/**
	 * 关闭键盘
	 */
	private void closeKeyBoard()
	{
		KeyBoardUtil.closeKeybord(mAccountLoginName.isFocused() ? mAccountLoginName : mAccountLoginPassord, this);
	}

	/**
	 * 检查输入参数
	 * 
	 * @return 是否合法
	 */
	private boolean checkInpurParams()
	{
		return TextUtils.isEmpty(mAccountLoginName.getText().toString()) || TextUtils.isEmpty(mAccountLoginPassord.getText().toString());
	}

	/**
	 * 跳转用户登录
	 */
	public static void forwardAccountLogin(Context context)
	{
		Intent intent = new Intent(context, AccountLoginActivity.class);
		context.startActivity(intent);
	}

	@Override
	public void clearRequestTask()
	{
		AccountLoginManager.clearAccountLoginTask();
		closeKeyBoard();
	}

}
