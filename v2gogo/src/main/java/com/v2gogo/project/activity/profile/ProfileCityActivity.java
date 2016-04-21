package com.v2gogo.project.activity.profile;

import java.util.List;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.BaseActivity;
import com.v2gogo.project.adapter.profile.ProfileProviceAdaptar;
import com.v2gogo.project.views.listview.refreshview.PullRefreshListView;

/**
 * 省会城市选择
 * 
 * @author houjun
 */
public class ProfileCityActivity extends BaseActivity implements OnItemClickListener
{

	public static final String CITY = "city";

	private PullRefreshListView mPullRefreshListView;
	private ProfileProviceAdaptar mProviceAdaptar;

	private List<String> citys;

	@Override
	public void onInitViews()
	{
		mPullRefreshListView = (PullRefreshListView) findViewById(R.id.profile_city_pull_to_refresh_listview);
		mProviceAdaptar = new ProfileProviceAdaptar(this);
		mPullRefreshListView.setAdapter(mProviceAdaptar);
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.profile_city_activity;
	}

	@Override
	protected void onInitIntentData(Intent intent)
	{
		super.onInitIntentData(intent);
		if (null != intent)
		{
			citys = intent.getStringArrayListExtra(CITY);
		}
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
		if (null != citys)
		{
			mProviceAdaptar.resetDatas(citys);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
	{
		if (null != citys)
		{
			Intent intent = new Intent();
			intent.putExtra("city", citys.get(position - 1));
			setResult(RESULT_OK, intent);
			finish();
		}
	}

	@Override
	public void clearRequestTask()
	{
	}
}
