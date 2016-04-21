package com.v2gogo.project.activity.profile.setting;

import android.text.Html;
import android.text.TextUtils;
import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.BaseActivity;
import com.v2gogo.project.utils.common.ReadAssetsUtils;

/**
 * 免责声明
 * 
 * @author houjun
 */
public class ProfileSettingPayActivity extends BaseActivity
{

	private TextView mTvText;

	@Override
	public void onInitViews()
	{
		mTvText = (TextView) findViewById(R.id.profile_setting_pay_text);
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.profile_setting_pay_activity_layout;
	}

	@Override
	protected void onInitLoadDatas()
	{
		super.onInitLoadDatas();
		String text = ReadAssetsUtils.readAssertResource(this, "user_pay.txt");
		if (!TextUtils.isEmpty(text))
		{
			mTvText.setText(Html.fromHtml(text));
		}
	}

	@Override
	public void clearRequestTask()
	{
	}
}
