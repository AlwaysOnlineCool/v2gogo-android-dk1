package com.v2gogo.project.activity.home.v2gogo;

import java.util.List;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.BaseActivity;
import com.v2gogo.project.activity.WebViewActivity;
import com.v2gogo.project.activity.account.AccountLoginActivity;
import com.v2gogo.project.activity.home.BaseDetailsctivity;
import com.v2gogo.project.activity.home.HomeArticleDetailsActivity;
import com.v2gogo.project.adapter.SliderAdapter;
import com.v2gogo.project.adapter.home.HomeV2gogoAdapter;
import com.v2gogo.project.domain.SliderInfo;
import com.v2gogo.project.domain.home.ArticeInfo;
import com.v2gogo.project.domain.home.V2gogoArticeInfo;
import com.v2gogo.project.listener.SimplePageChangeListener;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.manager.home.v2gogo.V2gogoManager;
import com.v2gogo.project.manager.home.v2gogo.V2gogoManager.IonV2gogoArticeListCallback;
import com.v2gogo.project.utils.common.ScreenUtil;
import com.v2gogo.project.utils.common.ToastUtil;
import com.v2gogo.project.views.ADViewPager;
import com.v2gogo.project.views.DotViews;
import com.v2gogo.project.views.ProgressLayout;
import com.v2gogo.project.views.ProgressLayout.IonRetryLoadDatasCallback;
import com.v2gogo.project.views.ProgressLayout.State;
import com.v2gogo.project.views.listview.refreshview.OnLoadMoreListener;
import com.v2gogo.project.views.listview.refreshview.OnPullRefreshListener;
import com.v2gogo.project.views.listview.refreshview.PullRefreshListView;

/**
 * v2gogo界面 全民秀
 * 
 * @author houjun
 */
public class HomeV2gogoActivity extends BaseActivity implements OnClickListener, OnItemClickListener, OnPullRefreshListener, OnLoadMoreListener,
		IonRetryLoadDatasCallback
{

	private V2gogoArticeInfo mV2gogoArticeInfo;

	private ProgressLayout mProgressLayout;
	private PullRefreshListView mPullRefreshListView;

	private TextView mTvJiang;
	private TextView mTvRegistration;

	private DotViews mDotViews;
	private ADViewPager mAdViewPager;
	private HomeV2gogoAdapter mHomeV2gogoAdapter;
	private FrameLayout mFrameLayout;

	@Override
	public void onInitViews()
	{
		mProgressLayout = (ProgressLayout) findViewById(R.id.v2gogo_activity_progress_layout);
		mPullRefreshListView = (PullRefreshListView) findViewById(R.id.v2gogo_activity_pull_to_refresh_listview);
		initHeaderView();
		mHomeV2gogoAdapter = new HomeV2gogoAdapter(this);
		mPullRefreshListView.setAdapter(mHomeV2gogoAdapter);

	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.v2gogo_activity_layout;
	}

	@Override
	protected void onInitLoadDatas()
	{
		super.onInitLoadDatas();
		mProgressLayout.showProgress();
		mV2gogoArticeInfo = new V2gogoArticeInfo();
		loadLoaclDatas();
		loadV2gogoList(V2gogoManager.FIRST_PAGE);
	}

	@Override
	protected void registerListener()
	{
		super.registerListener();
		mTvJiang.setOnClickListener(this);
		mTvRegistration.setOnClickListener(this);
		mProgressLayout.setOnTryLoadDatasCallback(this);
		mPullRefreshListView.setOnItemClickListener(this);
		mPullRefreshListView.setOnPullRefreshListener(this);
		mPullRefreshListView.setOnLoadMoreListener(this);
		mPullRefreshListView.setLoadMoreEnable(false);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
	{
		if (null != mV2gogoArticeInfo && null != mV2gogoArticeInfo.getArticeInfos() && position >= 2)
		{
			ArticeInfo articeInfo = mV2gogoArticeInfo.getArticeInfos().get((position - 2) % mHomeV2gogoAdapter.getCount());
			if (null != articeInfo)
			{
				Intent intent = new Intent(this, HomeArticleDetailsActivity.class);
				intent.putExtra(BaseDetailsctivity.ID, articeInfo.getId());
				startActivity(intent);
			}
		}
	}

	@Override
	public void onPullDownRefresh(AbsListView pullRefreshView)
	{
		loadV2gogoList(V2gogoManager.FIRST_PAGE);
	}

	@Override
	public void onLoadMore(AbsListView pullRefreshView)
	{
		loadV2gogoList(mV2gogoArticeInfo.getCurrentPage() + 1);
	}

	@Override
	public void onClick(View view)
	{
		Intent intent = null;
		switch (view.getId())
		{

			case R.id.home_v2gogo_header_jiang_item:
				intent = new Intent(this, HomeV2gogoDaJiangActivity.class);
				break;

			case R.id.home_v2gogo_header_registration_item:
				if (!V2GogoApplication.getMasterLoginState())
				{
					AccountLoginActivity.forwardAccountLogin(this);
					break;
				}
				intent = new Intent(this, WebViewActivity.class);
				intent.putExtra(WebViewActivity.URL, "https://jinshuju.net/f/iB3aI9");
				break;

			default:
				break;
		}
		if (null != intent)
		{
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	}

	@Override
	public void clearRequestTask()
	{
		V2gogoManager.clearGetV2gogoArticeListTask();
	}

	@Override
	public void onRetryLoadDatas()
	{
		loadV2gogoList(V2gogoManager.FIRST_PAGE);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		if (!mAdViewPager.isPlaying() && null != mAdViewPager.getAdapter() && mAdViewPager.getAdapter().getCount() > 1)
		{
			mAdViewPager.play(3 * 1000);
		}
	}

	@Override
	public void onPause()
	{
		if (mAdViewPager.isPlaying())
		{
			mAdViewPager.stop();
		}
		super.onPause();
	}

	/**
	 * 拉取v2gogo列表数据
	 * 
	 * @param page
	 */
	private void loadV2gogoList(int page)
	{
		V2gogoManager.getV2gogoArticeList(page, new IonV2gogoArticeListCallback()
		{
			@Override
			public void onV2gogoArticeListSuccess(V2gogoArticeInfo v2gogoArticeInfo)
			{
				mProgressLayout.showContent();
				displayArticeDatas(v2gogoArticeInfo);
				displaySliderDatas(v2gogoArticeInfo);
			}

			@Override
			public void onV2gogoArticeListFail(String errorMessage)
			{
				if (mProgressLayout.getState() != State.CONTENT)
				{
					mProgressLayout.showErrorText();
				}
				else
				{
					ToastUtil.showAlertToast(HomeV2gogoActivity.this, errorMessage);
					if (mPullRefreshListView.isRefreshing())
					{
						mPullRefreshListView.stopPullRefresh();
					}
					if (mPullRefreshListView.isLoadingMore())
					{
						mPullRefreshListView.stopLoadMore();
					}
				}
			}
		});
	}

	/**
	 * 初始化头部
	 */
	private void initHeaderView()
	{
		View headerView = LayoutInflater.from(this).inflate(R.layout.v2gogo_activity_header_layout, null);
		mTvJiang = (TextView) headerView.findViewById(R.id.home_v2gogo_header_jiang_item);
		mTvRegistration = (TextView) headerView.findViewById(R.id.home_v2gogo_header_registration_item);
		mDotViews = (DotViews) headerView.findViewById(R.id.home_v2gogo_interaction_header_big_photo_dots);
		mAdViewPager = (ADViewPager) headerView.findViewById(R.id.home_v2gogo_interaction_header_big_photo_display);
		mFrameLayout = (FrameLayout) headerView.findViewById(R.id.home_v2gogo_interaction_header_framelayout);
		mFrameLayout.setVisibility(View.GONE);
		FrameLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, (int) (ScreenUtil.getScreenWidth(this) * 1f / 2));
		mAdViewPager.setLayoutParams(layoutParams);
		mPullRefreshListView.addHeaderView(headerView);
	}

	/**
	 * 显示文章数据
	 * 
	 * @param v2gogoArticeInfo
	 */
	private void displayArticeDatas(V2gogoArticeInfo v2gogoArticeInfo)
	{
		if (null != v2gogoArticeInfo)
		{
			boolean isFinish = false;
			boolean isMore = false;
			if (v2gogoArticeInfo.getCurrentPage() == V2gogoManager.FIRST_PAGE)
			{
				mV2gogoArticeInfo.clear();
			}
			else
			{
				isMore = true;
			}
			if (v2gogoArticeInfo.getCount() <= v2gogoArticeInfo.getCurrentPage())
			{
				isFinish = true;
			}
			mV2gogoArticeInfo.setCurrentPage(v2gogoArticeInfo.getCurrentPage());
			mV2gogoArticeInfo.addAll(v2gogoArticeInfo);
			mHomeV2gogoAdapter.resetDatas(mV2gogoArticeInfo.getArticeInfos());
			if (isMore)
			{
				mPullRefreshListView.stopLoadMore();
			}
			else
			{
				mPullRefreshListView.stopPullRefresh();
			}
			if (isFinish)
			{
				mPullRefreshListView.setLoadMoreEnable(false);
			}
			else
			{
				mPullRefreshListView.setLoadMoreEnable(true);
			}
		}
	}

	/**
	 * 显示大图轮播
	 * 
	 * @param v2gogoArticeInfo
	 */
	private void displaySliderDatas(V2gogoArticeInfo v2gogoArticeInfo)
	{
		if (null != v2gogoArticeInfo && null != v2gogoArticeInfo.getSliderInfos() && v2gogoArticeInfo.getSliderInfos().size() > 0)
		{
			mFrameLayout.setVisibility(View.VISIBLE);
			List<SliderInfo> sliderInfos = v2gogoArticeInfo.getSliderInfos();
			SliderAdapter sliderAdapter = new SliderAdapter(this, sliderInfos);
			mAdViewPager.setAdapter(sliderAdapter);
			mDotViews.setSize(sliderInfos.size());
			if (sliderInfos.size() > 1)
			{
				mAdViewPager.play(3 * 1000);
			}
			mAdViewPager.setOnPageChangeListener(new SimplePageChangeListener()
			{
				@Override
				public void onPageSelected(int position)
				{
					super.onPageSelected(position);
					mDotViews.select(position);
				}
			});
		}
		else
		{
			if (mV2gogoArticeInfo.getCurrentPage() <= 1)
			{
				mFrameLayout.setVisibility(View.GONE);
				if (mAdViewPager.isPlaying())
				{
					mAdViewPager.stop();
					mDotViews.setSize(0);
				}
			}
		}
	}

	/**
	 * 加载缓存数据
	 */
	private void loadLoaclDatas()
	{
		V2gogoArticeInfo v2gogoArticeInfo = V2gogoManager.getLocalV2gogoArticeListDatas(this);
		displayArticeDatas(v2gogoArticeInfo);
		displaySliderDatas(v2gogoArticeInfo);
	}

}
