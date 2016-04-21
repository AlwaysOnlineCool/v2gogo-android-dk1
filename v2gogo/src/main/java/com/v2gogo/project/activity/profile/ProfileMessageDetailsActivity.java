package com.v2gogo.project.activity.profile;

import android.content.Intent;
import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.BaseActivity;
import com.v2gogo.project.domain.profile.MessageInfo;
import com.v2gogo.project.utils.common.DateUtil;

/**
 * 消息详情
 * 
 * @author houjun
 */
public class ProfileMessageDetailsActivity extends BaseActivity
{
	public static final String MESSAGE = "message";

	private MessageInfo messageInfo;

	private TextView mMainTitle;
	private TextView mTime;
	private TextView mMessage;

	@Override
	public void onInitViews()
	{
		mMainTitle = (TextView) findViewById(R.id.message_details_activity_main_title);
		mTime = (TextView) findViewById(R.id.message_details_activity_time);
		mMessage = (TextView) findViewById(R.id.message_details_activity_message);
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.profile_message_details_activity;
	}

	@Override
	protected void onInitIntentData(Intent intent)
	{
		super.onInitIntentData(intent);
		if (null != intent)
		{
			messageInfo = (MessageInfo) intent.getSerializableExtra(MESSAGE);
		}
	}

	@Override
	protected void onInitLoadDatas()
	{
		super.onInitLoadDatas();
		if (null != messageInfo)
		{
			mMainTitle.setText(messageInfo.getTitle());
			mMessage.setText(messageInfo.getMsg());
			mTime.setText(DateUtil.convertStringWithTimeStamp(messageInfo.getCreateTime()));
		}
	}

	@Override
	public void clearRequestTask()
	{
	}
}
