package com.v2gogo.project.activity.profile;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.v2gogo.project.R;
import com.v2gogo.project.activity.BaseActivity;
import com.v2gogo.project.adapter.profile.ProfileProviceAdaptar;
import com.v2gogo.project.domain.profile.ProvoiceInfo;
import com.v2gogo.project.utils.common.ReadAssetsUtils;
import com.v2gogo.project.views.listview.refreshview.PullRefreshListView;

/**
 * 省会城市选择
 * 
 * @author houjun
 */
public class ProfileProviceActivity extends BaseActivity implements OnItemClickListener
{

	private PullRefreshListView mPullRefreshListView;
	private ProfileProviceAdaptar mProviceAdaptar;
	private List<ProvoiceInfo> mProvoiceInfos;
	private int REQUEST_CODE = 0x200;

	@Override
	public void onInitViews()
	{
		mPullRefreshListView = (PullRefreshListView) findViewById(R.id.profile_provoice_pull_to_refresh_listview);
		mProviceAdaptar = new ProfileProviceAdaptar(this);
		mPullRefreshListView.setAdapter(mProviceAdaptar);
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.profile_provice_activity;
	}

	@Override
	protected void registerListener()
	{
		super.registerListener();
		mPullRefreshListView.setOnItemClickListener(this);
	}

	@Override
	protected void onInitLoadDatas()
	{
		super.onInitLoadDatas();
		String str = ReadAssetsUtils.readAssertResource(this, "city.txt");
		if (null != str)
		{
			mProvoiceInfos = new Gson().fromJson(str, new TypeToken<List<ProvoiceInfo>>()
			{
			}.getType());
			if (mProvoiceInfos != null)
			{
				List<String> strings = new ArrayList<String>();
				for (ProvoiceInfo provoiceInfo : mProvoiceInfos)
				{
					strings.add(provoiceInfo.getState());
				}
				mProviceAdaptar.resetDatas(strings);
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
	{
		ProvoiceInfo provoiceInfo = mProvoiceInfos.get(position - 1);
		if (null != provoiceInfo)
		{
			Intent intent = new Intent(this, ProfileCityActivity.class);
			intent.putStringArrayListExtra(ProfileCityActivity.CITY, (ArrayList<String>) provoiceInfo.getCities());
			startActivityForResult(intent, REQUEST_CODE);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE && null != data)
		{
			Intent intent = new Intent();
			intent.putExtra("city", data.getStringExtra("city"));
			setResult(RESULT_OK, intent);
			finish();
		}
	}

	@Override
	public void clearRequestTask()
	{
	}
}
