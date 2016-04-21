package com.v2gogo.project.activity.profile.setting;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.BaseActivity;
import com.v2gogo.project.manager.profile.FeedBackManager;
import com.v2gogo.project.manager.profile.FeedBackManager.IonuploadFeedbackCallback;
import com.v2gogo.project.utils.common.KeyBoardUtil;
import com.v2gogo.project.utils.common.ToastUtil;

/**
 * 意见反馈
 * 
 * @author houjun
 */
public class FeedbackActivity extends BaseActivity implements OnClickListener
{

	private EditText mEditText;
	private Button mBtn;

	@Override
	public void onInitViews()
	{
		mBtn = (Button) findViewById(R.id.feedback_activity_publish_btn);
		mEditText = (EditText) findViewById(R.id.feedback_activity_edittext_content);
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.feedback_activity_layout;
	}

	@Override
	protected void registerListener()
	{
		super.registerListener();
		mBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0)
	{
		String content = mEditText.getText().toString();
		if (!TextUtils.isEmpty(content))
		{
			content = content.trim();
			KeyBoardUtil.closeKeybord(mEditText, this);
			FeedBackManager.uploadFeedback(content, new IonuploadFeedbackCallback()
			{
				@Override
				public void onuploadFeedbackSuccess()
				{
					ToastUtil.showConfirmToast(FeedbackActivity.this, R.string.feedback_upload_success);
				}

				@Override
				public void onuploadFeedbackFail(String errorMessage)
				{
					ToastUtil.showAlertToast(FeedbackActivity.this, errorMessage);
				}
			});
		}
		else
		{
			ToastUtil.showAlertToast(this, R.string.input_no_empty_tip);
		}
	}

	@Override
	public void clearRequestTask()
	{
		FeedBackManager.clearUploadFeedbackTask();
		KeyBoardUtil.closeKeybord(mEditText, this);
	}
}
