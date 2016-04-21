package com.v2gogo.project.activity.profile.setting;

import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.BaseActivity;
import com.v2gogo.project.utils.common.ReadAssetsUtils;

/**
 * 产品介绍
 * 
 * @author houjun
 */
public class ProductDescriptionActivity extends BaseActivity
{

	private TextView mText;

	@Override
	public void onInitViews()
	{
		mText = (TextView) findViewById(R.id.product_desc_layout_text);
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.product_description_activity_layout;
	}

	@Override
	protected void onInitLoadDatas()
	{
		super.onInitLoadDatas();
		String text = ReadAssetsUtils.readAssertResource(this, "product_desc.txt");
		mText.setText(text);
	}

	@Override
	public void clearRequestTask()
	{
	}
}
