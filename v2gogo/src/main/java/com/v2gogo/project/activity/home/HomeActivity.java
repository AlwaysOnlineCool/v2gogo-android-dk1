package com.v2gogo.project.activity.home;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.v2gogo.project.InternalLinksTool;
import com.v2gogo.project.R;
import com.v2gogo.project.activity.BaseActivity;
import com.v2gogo.project.activity.CoinRankActivity;
import com.v2gogo.project.activity.WebViewActivity;
import com.v2gogo.project.activity.account.AccountLoginActivity;
import com.v2gogo.project.activity.home.subject.HomeSubjectActivity;
import com.v2gogo.project.activity.home.v2gogo.HomeV2gogoActivity;
import com.v2gogo.project.adapter.SliderAdapter;
import com.v2gogo.project.adapter.home.HomeAdapter;
import com.v2gogo.project.adapter.home.HomeHeaderNavAdapter;
import com.v2gogo.project.domain.CoinChangeInfo;
import com.v2gogo.project.domain.SliderInfo;
import com.v2gogo.project.domain.home.ArticeInfo;
import com.v2gogo.project.domain.home.ConcernInfo;
import com.v2gogo.project.domain.home.HomeInfo;
import com.v2gogo.project.domain.home.NavInfo;
import com.v2gogo.project.domain.home.PopularizeInfo;
import com.v2gogo.project.domain.home.PopularizeItemInfo;
import com.v2gogo.project.listener.SimplePageChangeListener;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.manager.HomeManager;
import com.v2gogo.project.manager.HomeManager.IonHomeDataCallback;
import com.v2gogo.project.manager.coin.SignCoinManager;
import com.v2gogo.project.manager.coin.SignCoinManager.IonCoinSignCallback;
import com.v2gogo.project.manager.coin.SignCoinManager.IonLuanchCheckTodaySignCallback;
import com.v2gogo.project.manager.home.PeopleConcernManager;
import com.v2gogo.project.manager.home.PeopleConcernManager.IonPeopleTopicListCallback;
import com.v2gogo.project.utils.common.ScreenUtil;
import com.v2gogo.project.utils.common.ToastUtil;
import com.v2gogo.project.views.ADViewPager;
import com.v2gogo.project.views.DotViews;
import com.v2gogo.project.views.ProgressLayout;
import com.v2gogo.project.views.ProgressLayout.IonRetryLoadDatasCallback;
import com.v2gogo.project.views.ProgressLayout.State;
import com.v2gogo.project.views.dialog.SignGetCoinDialog;
import com.v2gogo.project.views.dialog.SignGetCoinDialog.IonClickGetCoinCallback;
import com.v2gogo.project.views.listview.refreshview.OnLoadMoreListener;
import com.v2gogo.project.views.listview.refreshview.OnPullRefreshListener;
import com.v2gogo.project.views.listview.refreshview.PullRefreshListView;
import com.ypy.eventbus.EventBus;

public class HomeActivity extends BaseActivity implements OnPullRefreshListener, OnLoadMoreListener, OnClickListener, IonClickGetCoinCallback,
		IonRetryLoadDatasCallback
{
	public SignGetCoinDialog mSignGetCoinDialog;

	private ProgressLayout mProgressLayout;
	private PullRefreshListView mPullRefreshListView;

	private ADViewPager mAdViewPager;
	private DotViews mDotViews;
	private FrameLayout mFrameLayout;

	private ImageButton mBtnSign;
	private TextView mTvLeftCoin;
	private ImageButton mBtnTipOff;

	// private TextView mTvLive;
	// private TextView mTvShank;
	// private TextView mTvV2gogo;
	// private TextView mTvCommonConcern;

	private HomeAdapter mHomeAdapter;
	private HomeInfo mHomeInfo;

	protected PopularizeInfo mPopularizeInfo;

	private int mPage = 0;// 当前页数

	private GridView mHeaderNavGridView;

	@Override
	public void onInitViews()
	{
		initViews();
		initHeaderView();
		setAdapter();
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.home_activity_layout;
	}

	@Override
	protected boolean isSetting()
	{
		return false;
	}

	@Override
	protected void onInitLoadDatas()
	{
		super.onInitLoadDatas();
		mHomeInfo = new HomeInfo();
		loadHomeDatas();
	}

	@Override
	public void registerListener()
	{
		mBtnSign.setOnClickListener(this);
		mBtnTipOff.setOnClickListener(this);
		EventBus.getDefault().register(this);
		mTvLeftCoin.setOnClickListener(this);
		mPullRefreshListView.setLoadMoreEnable(true);
		mProgressLayout.setOnTryLoadDatasCallback(this);
		mPullRefreshListView.setPullRefreshEnable(true);
		mPullRefreshListView.setOnPullRefreshListener(this);
		mPullRefreshListView.setOnLoadMoreListener(this);
	}

	@Override
	public void onPullDownRefresh(AbsListView pullRefreshView)
	{
		loadNetHomeDatas();
	}

	// 加载更多
	@Override
	public void onLoadMore(AbsListView pullRefreshView)
	{
		mPage = mPage + 1;
		loadMoreDatas(mPage);
	}

	/**
	 * method desc：加载更多数据
	 * 
	 * @param page
	 */
	private void loadMoreDatas(int page)
	{
		PeopleConcernManager.getPeopleTopicList(page, new IonPeopleTopicListCallback()
		{
			@Override
			public void onPeopleTopicListSuccess(ConcernInfo concernInfo)
			{
				stopLoadmore();
				mProgressLayout.showContent();
				ArrayList<ArticeInfo> list = (ArrayList<ArticeInfo>) concernInfo.getArticeInfos();
				ArrayList<PopularizeItemInfo> popularizeItemInfos = new ArrayList<PopularizeItemInfo>();

				for (ArticeInfo articeInfo : list)
				{
					PopularizeItemInfo itemInfo = new PopularizeItemInfo();
					itemInfo.setTittle(articeInfo.getTitle());
					itemInfo.setIntro(articeInfo.getDescription());
					itemInfo.setPublishedTimeString(articeInfo.getPublishTimeString());
					itemInfo.setBrowser(articeInfo.getBrower());
					itemInfo.setTypelabel(articeInfo.getTypelabel());
					itemInfo.setUrl(articeInfo.getThumb());
					itemInfo.setInfoid(articeInfo.getId());
					itemInfo.setHref(articeInfo.getHref());
					itemInfo.setInfotype(articeInfo.getType());
					itemInfo.setVideo(articeInfo.getVideo());

					popularizeItemInfos.add(itemInfo);
				}
				if (mPopularizeInfo == null)
				{
					mPopularizeInfo = new PopularizeInfo();
					mPopularizeInfo.setName("百姓关注");
					mPopularizeInfo.setSrctype(1001);
					mPopularizeInfo.setInfos(popularizeItemInfos);
					mHomeInfo.addLoadMoreAll(mPopularizeInfo);
				}
				else
				{
					int size = mHomeInfo.getPopularizeInfos().size();
					mPopularizeInfo = mHomeInfo.getPopularizeInfos().get(size - 1);
					mPopularizeInfo.getInfos().addAll(popularizeItemInfos);
				}
				mHomeAdapter.reSetDatas(mHomeInfo);
				if (mPullRefreshListView != null)
				{
					boolean Flage = concernInfo.getCount() <= mPage;
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
			public void onPeopleTopicListFail(String errorMessage)
			{
				stopLoadmore();
				ToastUtil.showAlertToast(HomeActivity.this, errorMessage);
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

	@Override
	public void onClick(View view)
	{
		Intent intent = null;
		switch (view.getId())
		{
			case R.id.fragment_home_btn_sign:
				clickSign();
				break;

			case R.id.fragment_home_tv_left_coin:
				if (!V2GogoApplication.getMasterLoginState())
				{
					AccountLoginActivity.forwardAccountLogin(this);
				}
				else
				{
					intent = new Intent(this, CoinRankActivity.class);
				}
				break;

			case R.id.fragment_home_ibtn_boaliao:
				clickTipOff();
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

	/**
	 * 拉取网络首页数据
	 */
	private void loadNetHomeDatas()
	{
		mPage = 0;// 加载首页数据。页数重置默认
		HomeManager.getAppHomeData(new IonHomeDataCallback()
		{
			@Override
			public void onHomeDataSuccess(HomeInfo homeInfo)
			{
				mProgressLayout.showContent();
				displayHomeDatas(homeInfo);
				if (homeInfo != null && homeInfo.getmToolInfos() != null)
				{
					EventBus.getDefault().post(homeInfo.getmToolInfos());
				}
			}

			@Override
			public void onHomeDataFail(String errorMessage)
			{
				ToastUtil.showAlertToast(getParent(), errorMessage);
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
				}
			}
		});
	}

	/**
	 * 点击签到
	 */
	private void clickSign()
	{
		if (!V2GogoApplication.getMasterLoginState())
		{
			AccountLoginActivity.forwardAccountLogin(this);
		}
		else
		{
			SignCoinManager.luanchCheckTodaySign(new IonLuanchCheckTodaySignCallback()
			{

				@Override
				public void onLuanchCheckTodaySignSuccess(int maxSign, int coin, int day)
				{
					showSignDialog(day, coin, maxSign);
				}

				@Override
				public void onLuanchCheckTodaySignFail(String errorMessage)
				{
					ToastUtil.showAlertToast(getParent(), errorMessage);
				}
			});
		}
	}

	/**
	 * 初始化控件
	 */
	private void initViews()
	{
		mBtnSign = (ImageButton) findViewById(R.id.fragment_home_btn_sign);
		mTvLeftCoin = (TextView) findViewById(R.id.fragment_home_tv_left_coin);
		mBtnTipOff = (ImageButton) findViewById(R.id.fragment_home_ibtn_boaliao);
		mProgressLayout = (ProgressLayout) findViewById(R.id.fragment_home_progress_layout);
		mPullRefreshListView = (PullRefreshListView) findViewById(R.id.fragment_home_pull_to_refresh_listview);
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
		if (mTvLeftCoin != null)
		{
			mTvLeftCoin.setText(V2GogoApplication.getMasterLoginState() ? V2GogoApplication.getCurrentMatser().getCoin() + "" : "0");
		}
	}

	@Override
	public void onGetCoinClick()
	{
		SignCoinManager.getCoinSign(new IonCoinSignCallback()
		{
			@Override
			public void IonCoinSignSuccess(int coin)
			{
				if (mTvLeftCoin != null)
				{
					mTvLeftCoin.setText(V2GogoApplication.getMasterLoginState() ? V2GogoApplication.getCurrentMatser().getCoin() + "" : "0");
				}
			}

			@Override
			public void IonCoinSignFail(String errorMessage)
			{
				ToastUtil.showAlertToast(getParent(), errorMessage);
			}
		});
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
		loadNetHomeDatas();
	}

	public void onEventMainThread(CoinChangeInfo changeInfo)
	{
		if (mTvLeftCoin != null)
		{
			mTvLeftCoin.setText(V2GogoApplication.getMasterLoginState() ? V2GogoApplication.getCurrentMatser().getCoin() + "" : "0");
		}
	}

	/**
	 * 设置适配器
	 */
	private void setAdapter()
	{
		mHomeAdapter = new HomeAdapter(this);
		mPullRefreshListView.setAdapter(mHomeAdapter);
	}

	/**
	 * method desc：设置Header的Nav导航栏
	 */
	private void setHeaderNavDatas()
	{
		List<NavInfo> navInfos = mHomeInfo.getmNavInfos();
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
			mHeaderNavGridView.setAdapter(new HomeHeaderNavAdapter(navInfos, R.layout.home_nav_grid_view_item_layout));
			mHeaderNavGridView.setOnItemClickListener(new OnItemClickListener()
			{

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id)
				{
					NavInfo navInfo = mHomeInfo.getmNavInfos().get(position);

					Intent intent = null;
					switch (navInfo.getContentType())
					{
						case 1:
							clickShake();// 摇一摇
							break;

						case 2:// 百姓关注
							intent = new Intent(HomeActivity.this, HomeConcernActivity.class);
							break;

						case 3:// 专题
								// intent = new Intent(HomeActivity.this, HomeLiveActivity.class);
							intent = new Intent(HomeActivity.this, HomeSubjectActivity.class);
							intent.putExtra(HomeSubjectActivity.SUBJECT_ID, navInfo.getUrl());
							break;

						case 4:// 内链接
							try
							{
								Uri uri = Uri.parse(navInfo.getUrl());
								int type = Integer.parseInt(uri.getQueryParameter("type"));
								String url = uri.getQueryParameter("url");
								String srcId = uri.getQueryParameter("srcId");
								InternalLinksTool.jump2Activity(HomeActivity.this, type, srcId, url, null);
							}
							catch (Exception e)
							{
							}
							return;
						case 5:// 外部链接
							InternalLinksTool.forward2Website(HomeActivity.this, navInfo.getUrl());
							return;
						case 6:// 全民秀
							intent = new Intent(HomeActivity.this, HomeV2gogoActivity.class);
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
	}

	/**
	 * 设置大图轮播相关数据
	 */
	private void setSliderDatas()
	{
		List<SliderInfo> sliderInfos = mHomeInfo.getSliderInfos();
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
	 * 加载首页数据
	 */
	private void loadHomeDatas()
	{
		HomeInfo homeInfo = HomeManager.getAppLocalHomeData(this);
		if (homeInfo == null || homeInfo.isEmpty())
		{
			mProgressLayout.showProgress();
		}
		displayHomeDatas(homeInfo);
		loadNetHomeDatas();
	}

	/**
	 * 显示签到对话框
	 */
	private void showSignDialog(int day, int coin, int maxCoin)
	{
		if (V2GogoApplication.getMasterLoginState())
		{
			if (null == mSignGetCoinDialog)
			{
				mSignGetCoinDialog = new SignGetCoinDialog(this, R.style.style_action_sheet_dialog);
				mSignGetCoinDialog.setOnClickGetCoinCallback(this);
			}
			if (!mSignGetCoinDialog.isShowing())
			{
				mSignGetCoinDialog.show();
				mSignGetCoinDialog.setDatas(day, coin, maxCoin);
			}
		}
	}

	/**
	 * 显示首页数据
	 */
	private void displayHomeDatas(HomeInfo homeInfo)
	{
		if (null != homeInfo)
		{
			mPopularizeInfo = null;
			mHomeInfo.clear();
			mHomeInfo.addAll(homeInfo);
			setSliderDatas();// 设置首页广告视图
			setHeaderNavDatas();// 设置首页导航栏视图
			mHomeAdapter.reSetDatas(mHomeInfo);
			if (mPullRefreshListView.isRefreshing())
			{
				mPullRefreshListView.stopPullRefresh();
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

	/**
	 * 点击爆料
	 */
	private void clickTipOff()
	{
		if (!V2GogoApplication.getMasterLoginState())
		{
			AccountLoginActivity.forwardAccountLogin(this);
		}
		else
		{
			Intent intent = new Intent(this, WebViewActivity.class);
			String url = "https://www.jinshuju.net/f/mi5AfU?from=singlemessage&isappinstalled=0";
			intent.putExtra(WebViewActivity.URL, url);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	}

}
