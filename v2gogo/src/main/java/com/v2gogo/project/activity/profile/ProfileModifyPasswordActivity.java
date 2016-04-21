package com.v2gogo.project.activity.profile;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.BaseActivity;
import com.v2gogo.project.activity.MainTabActivity;
import com.v2gogo.project.manager.account.AccountPasswordManager;
import com.v2gogo.project.manager.account.AccountPasswordManager.IonmodifyAccountPasswordCallback;
import com.v2gogo.project.utils.common.KeyBoardUtil;
import com.v2gogo.project.utils.common.ToastUtil;
import com.v2gogo.project.views.dialog.AppNoticeDialog;
import com.v2gogo.project.views.dialog.AppNoticeDialog.IonClickSureCallback;

/**
 * 修改密码
 * 
 * @author houjun
 */
public class ProfileModifyPasswordActivity extends BaseActivity implements OnClickListener, IonClickSureCallback
{

	private EditText mEtOldPwd;
	private EditText mEtNewPwd;
	private EditText mEtNewPwdAgagin;
	private Button mBtnConfirmModify;
	private AppNoticeDialog mAppNoticeDialog;

	@Override
	public void onInitViews()
	{
		mEtOldPwd = (EditText) findViewById(R.id.profile_modify_password_activity_old_pwd);
		mEtNewPwd = (EditText) findViewById(R.id.profile_modify_password_activity_new_pwd);
		mEtNewPwdAgagin = (EditText) findViewById(R.id.profile_modify_password_activity_new_pwd_again);
		mBtnConfirmModify = (Button) findViewById(R.id.profile_modify_password_activity_fonfirm_modify);
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.profile_modify_password_activity_layout;
	}

	@Override
	protected void registerListener()
	{
		super.registerListener();
		mBtnConfirmModify.setOnClickListener(this);
	}

	@Override
	public void clearRequestTask()
	{
		AccountPasswordManager.clearModifyAccountPasswordTask();
	}

	@Override
	public void onClickSure()
	{
		Intent intent = new Intent(this, MainTabActivity.class);
		intent.putExtra("isLogin", true);
		intent.putExtra(MainTabActivity.BACK, MainTabActivity.MODIFY_PWD);
		startActivity(intent);
		finish();
	}

	private void closeKeyBoard()
	{
		EditText editText = null;
		if (mEtOldPwd.isFocused())
		{
			editText = mEtOldPwd;
		}
		if (mEtNewPwd.isFocused())
		{
			editText = mEtNewPwd;
		}
		if (mEtNewPwdAgagin.isFocused())
		{
			editText = mEtNewPwdAgagin;
		}
		if (null != editText)
		{
			KeyBoardUtil.closeKeybord(editText, this);
		}
	}

	@Override
	public void onClick(View view)
	{
		String code = mEtOldPwd.getText().toString();
		if (!TextUtils.isEmpty(code))
		{
			code = code.trim();
		}
		else
		{
			ToastUtil.showAlertToast(this, R.string.passeord_empty_tip);
			return;
		}
		String newPwd = mEtNewPwd.getText().toString();
		if (!TextUtils.isEmpty(newPwd))
		{
			newPwd = newPwd.trim();
		}
		else
		{
			ToastUtil.showAlertToast(this, R.string.passeord_empty_tip);
			return;
		}
		String confirmPassword = mEtNewPwdAgagin.getText().toString();
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
		modifyUserPassword(code, newPwd);
	}

	/**
	 * 修改用户密码
	 * 
	 * @param code
	 * @param newPwd
	 */
	private void modifyUserPassword(String code, String newPwd)
	{
		AccountPasswordManager.modifyAccountPassword(code, newPwd, new IonmodifyAccountPasswordCallback()
		{
			@Override
			public void onmodifyAccountPasswordSuccess()
			{
				if (null == mAppNoticeDialog)
				{
					mAppNoticeDialog = new AppNoticeDialog(ProfileModifyPasswordActivity.this, R.style.style_action_sheet_dialog);
					mAppNoticeDialog.setCancelable(false);
					mAppNoticeDialog.setOnSureCallback(ProfileModifyPasswordActivity.this);
					mAppNoticeDialog.setCanceledOnTouchOutside(false);
				}
				if (!mAppNoticeDialog.isShowing())
				{
					mAppNoticeDialog.show();
					mAppNoticeDialog.setSureTitleAndMessage(R.string.modify_password_success_tip, R.string.app_notice_sure_tip);
				}
			}

			@Override
			public void onmodifyAccountPasswordFail(String errorMessage)
			{
				ToastUtil.showAlertToast(ProfileModifyPasswordActivity.this, errorMessage);
			}
		});
	}
}
