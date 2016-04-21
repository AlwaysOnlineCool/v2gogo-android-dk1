package com.v2gogo.project.activity;

import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.utils.common.ReadAssetsUtils;

/**
 * v2gogo用户协议
 * 
 * @author houjun
 */
public class V2gogoUserProtocolActivity extends BaseActivity
{

	private TextView mProtocolText;

	@Override
	public void onInitViews()
	{
		mProtocolText = (TextView) findViewById(R.id.v2gogo_user_protocol_text);
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.v2gogo_user_protocol_activity_layout;
	}

	@Override
	protected void onInitLoadDatas()
	{
		super.onInitLoadDatas();
		String text = ReadAssetsUtils.readAssertResource(this, "important_protocol_chs_for_v2gogo.txt");
		mProtocolText.setText(text);
	}

	@Override
	public void clearRequestTask()
	{
	}

}
