package com.v2gogo.project.activity.home.subject;

import java.util.List;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.GridView;
import android.widget.TextView;

import com.v2gogo.project.InternalLinksTool;
import com.v2gogo.project.R;
import com.v2gogo.project.activity.BaseActivity;
import com.v2gogo.project.activity.account.AccountLoginActivity;
import com.v2gogo.project.activity.home.HomeConcernActivity;
import com.v2gogo.project.activity.home.HomeShakeActivity;
import com.v2gogo.project.activity.home.v2gogo.HomeV2gogoActivity;
import com.v2gogo.project.adapter.SliderAdapter;
import com.v2gogo.project.adapter.home.HomeHeaderNavAdapter;
import com.v2gogo.project.adapter.home.SubjectAdapter;
import com.v2gogo.project.domain.SliderInfo;
import com.v2gogo.project.domain.home.NavInfo;
import com.v2gogo.project.domain.home.PopularizeInfo;
import com.v2gogo.project.domain.home.subject.SubjectInfo;
import com.v2gogo.project.domain.home.subject.SubjectMoreArticle;
import com.v2gogo.project.listener.SimplePageChangeListener;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.manager.HomeManager;
import com.v2gogo.project.manager.coin.SignCoinManager;
import com.v2gogo.project.manager.home.subject.SubjectManager;
import com.v2gogo.project.manager.home.subject.SubjectManager.IonSubjectDataCallback;
import com.v2gogo.project.manager.home.subject.SubjectManager.IonSubjectMoreDataCallback;
import com.v2gogo.project.utils.common.ScreenUtil;
import com.v2gogo.project.utils.common.ToastUtil;
import com.v2gogo.project.views.ADViewPager;
import com.v2gogo.project.views.DotViews;
import com.v2gogo.project.views.ProgressLayout;
import com.v2gogo.project.views.ProgressLayout.IonRetryLoadDatasCallback;
import com.v2gogo.project.views.ProgressLayout.State;
import com.v2gogo.project.views.dialog.SignGetCoinDialog;
import com.v2gogo.project.views.expandablelistview.PullExpandableListview;
import com.v2gogo.project.views.listview.refreshview.OnLoadMoreListener;
import com.v2gogo.project.views.listview.refreshview.OnPullRefreshListener;
import com.ypy.eventbus.EventBus;

/**
 * 功能：首页--专题
 * 
 * @ahthor：黄荣星
 * @date:2015-11-19
 * @version::V1.0
 */
public class HomeSubjectActivity extends BaseActivity implements OnPullRefreshListener, OnLoadMoreListener, IonRetryLoadDatasCallback
{
	public SignGetCoinDialog mSignGetCoinDialog;

	public static final String SUBJECT_ID = "subject_id";// 专题ID

	private ProgressLayout mProgressLayout;
	private PullExpandableListview mPullRefreshListView;

	private ADViewPager mAdViewPager;
	private DotViews mDotViews;
	private FrameLayout mFrameLayout;

	private SubjectAdapter mSubjectAdapter;
	private SubjectInfo mSubjectInfo;

	protected PopularizeInfo mPopularizeInfo;

	private int mPage = 1;// 当前页数

	private GridView mHeaderNavGridView;

	private String mSubject_id;

	private TextView mSubjectName;

	@Override
	public void onInitViews()
	{
		initViews();
		initHeaderView();
		setAdapter();
	}

	@Override
	@TargetApi(11)
	protected void onInitIntentData(Intent intent)
	{
		super.onInitIntentData(intent);
		if (intent != null)
		{
			mSubject_id = intent.getStringExtra(SUBJECT_ID);
		}
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.home_subject_activity_layout;
	}

	@Override
	protected void onInitLoadDatas()
	{
		super.onInitLoadDatas();
		mSubjectInfo = new SubjectInfo();
		loadSubjectDatas();
	}

	@Override
	public void registerListener()
	{
		super.registerListener();
		mProgressLayout.setOnTryLoadDatasCallback(this);
		mPullRefreshListView.setOnPullRefreshListener(this);
		mPullRefreshListView.setPullRefreshEnable(true);
		mPullRefreshListView.setOnLoadMoreListener(this);
		mPullRefreshListView.setLoadMoreEnable(false);
	}

	@Override
	public void onPullDownRefresh(AbsListView pullRefreshView)
	{
		loadNetSubjectDatas(mSubject_id);
	}

	// 加载更多
	@Override
	public void onLoadMore(AbsListView pullRefreshView)
	{
		// loadMoreDatas(mPage);
		// SubjectManager.getSubjectMoreArticleList(1,"db25301b5ae24684937e5925b8c32de8",null);
		mPage = mPage + 1;
		getHttpSubjectMoreArticles(mPage);
	}

	/**
	 * method desc：加载更多数据
	 * 
	 * @param page
	 */
	private void getHttpSubjectMoreArticles(int currentPage)
	{
		SubjectManager.getSubjectMoreArticleList(currentPage, mSubject_id, new IonSubjectMoreDataCallback()
		{

			@Override
			public void onSubjectMoreDataSuccess(SubjectMoreArticle moreArticle)
			{
				stopLoadmore();
				if (mSubjectInfo.getMoreArticleInfos() == null)
				{
					mSubjectInfo.setMoreArticleInfos(moreArticle.getInfos());
				}
				else
				{
					mSubjectInfo.getMoreArticleInfos().addAll(moreArticle.getInfos());
				}

				mSubjectAdapter.reSetDatas(mSubjectInfo);
				if (mPullRefreshListView != null)
				{
					boolean Flage = mPage >= moreArticle.getPageCount();
					boolean isFinish = Flage ? true : false;
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

			@Override
			public void onSubjectMoreDataFail(String errorMessage)
			{
				stopLoadmore();
				ToastUtil.showAlertToast(HomeSubjectActivity.this, errorMessage);
			}
		});
	}

	/**
	 * method desc：停止加载更多
	 */
	private void stopLoadmore()
	{
		if (mPullRefreshListView.isLoadingMore())
		{
			mPullRefreshListView.stopLoadMore();
		}
	}

	/**
	 * 拉取网络首页数据
	 */
	private void loadNetSubjectDatas(final String specialId)
	{
		mPage = 1;
		SubjectManager.getHttpSubjectData(specialId, new IonSubjectDataCallback()
		{

			@Override
			public void onSubjectDataSuccess(SubjectInfo subjectInfo)
			{
				mProgressLayout.showContent();
				displayHomeDatas(subjectInfo);
			}

			@Override
			public void onSubjectDataFail(String errorMessage)
			{
				ToastUtil.showAlertToast(HomeSubjectActivity.this, errorMessage);
				SubjectInfo subjectInfo = SubjectManager.getAppLocalSubjectData(HomeSubjectActivity.this);
				if (subjectInfo != null)
				{
					if (subjectInfo.getSpecialId().equals(specialId))
					{
						mProgressLayout.showContent();
						displayHomeDatas(subjectInfo);
					}
					else
					{
						if (mProgressLayout.getState() != State.CONTENT)
						{
							mProgressLayout.showErrorText();
						}
					}
				}
				else
				{
					if (mProgressLayout.getState() != State.CONTENT)
					{
						mProgressLayout.showErrorText();
					}
				}
				if (mPullRefreshListView != null && mPullRefreshListView.isRefreshing())
				{
					mPullRefreshListView.stopPullRefresh();
				}
			}
		});
	}

	/**
	 * 初始化控件
	 */
	private void initViews()
	{
		mProgressLayout = (ProgressLayout) findViewById(R.id.fragment_home_progress_layout);
		mPullRefreshListView = (PullExpandableListview) findViewById(R.id.fragment_home_pull_to_refresh_listview);
		mSubjectName = (TextView) findViewById(R.id.common_app_action_bar_text);
	}

	/**
	 * 初始化头部
	 */
	private void initHeaderView()
	{
		View headerView = getLayoutInflater().inflate(R.layout.home_activity_header, null);
		mFrameLayout = (FrameLayout) headerView.findViewById(R.id.home_activity_framelayout);
		mDotViews = (DotViews) headerView.findViewById(R.id.fragment_home_big_photo_dots);
		mAdViewPager = (ADViewPager) headerView.findViewById(R.id.fragment_home_big_photo_display);
		FrameLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, (int) (ScreenUtil.getScreenWidth(this) * 4 / 8f));
		mAdViewPager.setLayoutParams(layoutParams);
		mHeaderNavGridView = (GridView) headerView.findViewById(R.id.GridView);
		mPullRefreshListView.addHeaderView(headerView, null, false);

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
		HomeManager.clearGetAppHomeDataTask();
		SignCoinManager.clearGetCoinSignTask();
		EventBus.getDefault().unregister(this);
		SignCoinManager.clearLuanchCheckTodaySignTask();
	}

	@Override
	public void onRetryLoadDatas()
	{
		loadNetSubjectDatas(mSubject_id);
	}

	/**
	 * 设置适配器
	 */
	private void setAdapter()
	{
		mSubjectAdapter = new SubjectAdapter(this);
		mPullRefreshListView.setAdapter(mSubjectAdapter);
	}

	/**
	 * method desc：设置Header的Nav导航栏
	 */
	private void setHeaderNavDatas()
	{
		final List<NavInfo> navInfos = mSubjectInfo.getNavs();
		if (null != navInfos && navInfos.size() > 0)
		{
			int size = navInfos.size();
			int column = 5;
			if (size <= 5)
			{
				column = size;
			}
			else
			{
				double temp = (double) size / 2;
				column = (int) Math.ceil(temp);// 向上取整
			}
			mHeaderNavGridView.setNumColumns(column);
			mHeaderNavGridView.setVisibility(View.VISIBLE);
			mHeaderNavGridView.setAdapter(new HomeHeaderNavAdapter(navInfos, R.layout.home_nav_grid_view_item_layout));
			mHeaderNavGridView.setOnItemClickListener(new OnItemClickListener()
			{

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id)
				{
					NavInfo navInfo = navInfos.get(position);

					Intent intent = null;
					switch (navInfo.getContentType())
					{
						case 1:
							clickShake();// 摇一摇
							break;

						case 2:// 百姓关注
							intent = new Intent(HomeSubjectActivity.this, HomeConcernActivity.class);
							break;

						case 3:// 专题
								// intent = new Intent(HomeActivity.this, HomeLiveActivity.class);

							break;

						case 4:// 内链接
								// jump2Activity(HomeSubjectActivity.this, navInfo);
							try
							{
								Uri uri = Uri.parse(navInfo.getUrl());
								String srcId = uri.getQueryParameter("srcId");
								int type = Integer.parseInt(uri.getQueryParameter("type"));
								String url = uri.getQueryParameter("url");
								InternalLinksTool.jump2Activity(HomeSubjectActivity.this, type, srcId, url, null);
							}
							catch (Exception e)
							{
							}
							return;
						case 5:// 外部链接
							InternalLinksTool.forward2Website(HomeSubjectActivity.this, navInfo.getUrl());
							return;
						case 6:// 全民秀
							intent = new Intent(HomeSubjectActivity.this, HomeV2gogoActivity.class);
							break;
						default:
							break;
					}
					if (null != intent)
					{
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
					}

				}
			});
		}
		else
		{
			mHeaderNavGridView.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置大图轮播相关数据
	 */
	private void setSliderDatas()
	{
		List<SliderInfo> sliderInfos = mSubjectInfo.getSliders();
		if (null != sliderInfos && sliderInfos.size() > 0)
		{
			mFrameLayout.setVisibility(View.VISIBLE);
			SliderAdapter sliderAdapter = new SliderAdapter(this, sliderInfos);
			mAdViewPager.setAdapter(sliderAdapter);
			mAdViewPager.setOnPageChangeListener(new SimplePageChangeListener()
			{
				@Override
				public void onPageSelected(int position)
				{
					mDotViews.select(position);
				}
			});
			if (sliderInfos.size() > 1)
			{
				mAdViewPager.play(3 * 1000);
			}
			mDotViews.setSize(sliderInfos.size());
		}
		else
		{
			mFrameLayout.setVisibility(View.GONE);
			if (mAdViewPager.isPlaying())
			{
				mAdViewPager.stop();
			}
			mDotViews.setSize(0);
		}
	}

	/**
	 * 加载专题数据
	 */
	private void loadSubjectDatas()
	{
		mProgressLayout.showProgress();
		loadNetSubjectDatas(mSubject_id);
	}

	/**
	 * 显示首页数据
	 */
	private void displayHomeDatas(SubjectInfo subjectInfo)
	{
		if (null != subjectInfo)
		{
			if (mSubjectName != null)
			{
				mSubjectName.setText(subjectInfo.getName());
			}
			mSubjectInfo.clear();
			mSubjectInfo.addAll(subjectInfo);
			mSubjectInfo.setPage(subjectInfo.getPage());
			mSubjectInfo.setPageCount(subjectInfo.getPageCount());
			setSliderDatas();// 设置首页广告视图
			setHeaderNavDatas();// 设置首页导航栏视图
			mSubjectAdapter.reSetDatas(subjectInfo);

			int count = mPullRefreshListView.getCount();
			for (int i = 0; i < count; i++)
			{
				mPullRefreshListView.expandGroup(i);
			}
			if (mPullRefreshListView.isRefreshing())
			{
				mPullRefreshListView.stopPullRefresh();
			}
			if (subjectInfo.getPageCount() > 1)
			{
				mPullRefreshListView.setLoadMoreEnable(true);
			}
			else
			{
				mPullRefreshListView.setLoadMoreEnable(false);
			}
		}
	}

	/**
	 * 点击摇摇乐
	 */
	private void clickShake()
	{
		if (V2GogoApplication.getMasterLoginState())
		{
			Intent intent = new Intent(this, HomeShakeActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
		else
		{
			AccountLoginActivity.forwardAccountLogin(this);
		}
	}

}
