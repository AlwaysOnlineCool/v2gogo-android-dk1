package com.v2gogo.project.activity.profile.setting;

import android.content.Intent;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.BaseActivity;
import com.v2gogo.project.utils.common.AppUtil;

/**
 * 关于我们
 * 
 * @author houjun
 */
public class AboutUsActivity extends BaseActivity implements OnClickListener
{

	private TextView mVersionText;
	private TextView mServicePhone;
	private TextView mProductTetx;

	@Override
	public void onInitViews()
	{
		mVersionText = (TextView) findViewById(R.id.about_us_version_text);
		mServicePhone = (TextView) findViewById(R.id.about_us_service_phone_text);
		mProductTetx = (TextView) findViewById(R.id.about_us_service_product_text);
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.about_us_activity_layout;
	}

	@Override
	protected void onInitLoadDatas()
	{
		super.onInitLoadDatas();
		mVersionText.setText(String.format(getString(R.string.about_us_version_tip), AppUtil.getVersionName(this)));
		String str = String.format(getString(R.string.about_service_phone_tip), "0851-84727618");
		SpannableStringBuilder stringBuilder = new SpannableStringBuilder(str);
		ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(0xFFF96700);
		stringBuilder.setSpan(foregroundColorSpan, 5, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		mServicePhone.setText(stringBuilder);
	}

	@Override
	protected void registerListener()
	{
		super.registerListener();
		mProductTetx.setOnClickListener(this);
		mServicePhone.setOnClickListener(this);
	}

	@Override
	public void onClick(View view)
	{
		Intent intent = null;
		switch (view.getId())
		{
			case R.id.about_us_service_product_text:
				intent = new Intent(this, ProductDescriptionActivity.class);
				break;

			case R.id.about_us_service_phone_text:
				intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:0851-84727618"));
				break;

			default:
				break;
		}
		if (null != intent)
		{
			startActivity(intent);
		}
	}

	@Override
	public void clearRequestTask()
	{
	}
}
