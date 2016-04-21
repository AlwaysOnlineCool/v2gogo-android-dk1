package com.v2gogo.project.activity.home;

import java.util.List;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.BaseActivity;
import com.v2gogo.project.activity.home.subject.HomeSubjectActivity;
import com.v2gogo.project.adapter.SliderAdapter;
import com.v2gogo.project.adapter.home.HomeConcernAdapter;
import com.v2gogo.project.domain.SliderInfo;
import com.v2gogo.project.domain.home.ArticeInfo;
import com.v2gogo.project.domain.home.ConcernInfo;
import com.v2gogo.project.listener.SimplePageChangeListener;
import com.v2gogo.project.manager.home.PeopleConcernManager;
import com.v2gogo.project.manager.home.PeopleConcernManager.IonPeopleTopicListCallback;
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
 * 百姓关注
 * 
 * @author houjun
 */
public class HomeConcernActivity extends BaseActivity implements OnItemClickListener, OnPullRefreshListener, OnLoadMoreListener, IonRetryLoadDatasCallback
{
	private ConcernInfo mConcernInfo;

	private DotViews mDotViews;
	private ADViewPager mAdViewPager;

	private ProgressLayout mProgressLayout;
	private PullRefreshListView mPullRefreshListView;
	private FrameLayout mFrameLayout;

	private HomeConcernAdapter mHomeConcernAdapter;

	@Override
	public void onInitViews()
	{
		mProgressLayout = (ProgressLayout) findViewById(R.id.activity_focus_progress_layout);
		mPullRefreshListView = (PullRefreshListView) findViewById(R.id.activity_focus_pull_to_refresh_listview);
		initHeaderView();
		mHomeConcernAdapter = new HomeConcernAdapter(this);
		mPullRefreshListView.setAdapter(mHomeConcernAdapter);
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.home_concern_activity_layout;
	}

	@Override
	protected void onInitLoadDatas()
	{
		super.onInitLoadDatas();
		mConcernInfo = new ConcernInfo();
		ConcernInfo concernInfo = PeopleConcernManager.getLocalPeopleConcernDatas(this);
		if (concernInfo == null || concernInfo.isEmpty())
		{
			mProgressLayout.showProgress();
		}
		displaySliderDatas(concernInfo);
		displayConcernDatas(concernInfo);
		loadConcernList(PeopleConcernManager.FIRST_PAGE);
	}

	@Override
	protected void registerListener()
	{
		super.registerListener();
		mProgressLayout.setOnTryLoadDatasCallback(this);
		mPullRefreshListView.setOnLoadMoreListener(this);
		mPullRefreshListView.setOnPullRefreshListener(this);
		mPullRefreshListView.setLoadMoreEnable(false);
		mPullRefreshListView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
	{
		if (null != mConcernInfo && null != mConcernInfo.getArticeInfos() && position >= 2)
		{
			ArticeInfo articeInfo = mConcernInfo.getArticeInfos().get((position - 2) % mHomeConcernAdapter.getCount());
			if (null != articeInfo)
			{
				if (!TextUtils.isEmpty(articeInfo.getHref()))// 跳专题
				{
					Intent intent = new Intent(this, HomeSubjectActivity.class);
					intent.putExtra(HomeSubjectActivity.SUBJECT_ID, articeInfo.getHref());
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				}
				else
				{
					Intent intent = new Intent(this, HomeArticleDetailsActivity.class);
					intent.putExtra(BaseDetailsctivity.ID, articeInfo.getId());
					startActivity(intent);
				}
			}
		}
	}

	@Override
	public void onPullDownRefresh(AbsListView pullRefreshView)
	{
		loadConcernList(PeopleConcernManager.FIRST_PAGE);
	}

	@Override
	public void onLoadMore(AbsListView pullRefreshView)
	{
		loadConcernList(mConcernInfo.getCurrentPage() + 1);
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
	public void clearRequestTask()
	{
		PeopleConcernManager.clearGetPeopleTopicList();
	}

	@Override
	public void onRetryLoadDatas()
	{
		loadConcernList(PeopleConcernManager.FIRST_PAGE);
	}

	/**
	 * 百姓关注列表
	 * 
	 * @param page
	 */
	private void loadConcernList(int page)
	{
		PeopleConcernManager.getPeopleTopicList(page, new IonPeopleTopicListCallback()
		{
			@Override
			public void onPeopleTopicListSuccess(ConcernInfo concernInfo)
			{
				mProgressLayout.showContent();
				displayConcernDatas(concernInfo);
				displaySliderDatas(concernInfo);
			}

			@Override
			public void onPeopleTopicListFail(String errorMessage)
			{
				ToastUtil.showAlertToast(HomeConcernActivity.this, errorMessage);
				if (mProgressLayout.getState() != State.CONTENT)
				{
					mProgressLayout.showErrorText();
				}
				else
				{
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
	 * 初始化头部视图
	 */
	private void initHeaderView()
	{
		View headerView = LayoutInflater.from(this).inflate(R.layout.home_concern_activity_header_layout, null);
		mFrameLayout = (FrameLayout) headerView.findViewById(R.id.home_concern_activity_header_framelayout);
		mDotViews = (DotViews) headerView.findViewById(R.id.home_people_concern_header_big_photo_dots);
		mAdViewPager = (ADViewPager) headerView.findViewById(R.id.home_people_concern_header_big_photo_display);
		FrameLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, (int) (ScreenUtil.getScreenWidth(this) * 1 / 2f));
		mAdViewPager.setLayoutParams(layoutParams);
		mPullRefreshListView.addHeaderView(headerView);
	}

	/**
	 * 显示关注数据
	 */
	private void displayConcernDatas(ConcernInfo concernInfo)
	{
		if (null != concernInfo)
		{
			boolean isFinish = false;
			boolean isMore = false;
			if (concernInfo.getCurrentPage() == PeopleConcernManager.FIRST_PAGE)
			{
				mConcernInfo.clear();
			}
			else
			{
				isMore = true;
			}
			if (concernInfo.getCount() <= concernInfo.getCurrentPage())
			{
				isFinish = true;
			}
			mConcernInfo.setCurrentPage(concernInfo.getCurrentPage());
			mConcernInfo.addAll(concernInfo);
			if (null != mPullRefreshListView)
			{
				mHomeConcernAdapter.resetDatas(mConcernInfo);
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
	}

	/**
	 * 显示轮播数据
	 * 
	 * @param concernInfo
	 */
	private void displaySliderDatas(ConcernInfo concernInfo)
	{
		if (null != concernInfo && null != concernInfo.getSliderInfos() && concernInfo.getSliderInfos().size() > 0)
		{
			mFrameLayout.setVisibility(View.VISIBLE);
			List<SliderInfo> sliderInfos = concernInfo.getSliderInfos();
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
			if (mConcernInfo.getCurrentPage() <= 1)
			{
				mFrameLayout.setVisibility(View.GONE);
				if (mAdViewPager.isPlaying())
				{
					mAdViewPager.stop();
				}
				mDotViews.setSize(0);
			}
		}
	}
}
