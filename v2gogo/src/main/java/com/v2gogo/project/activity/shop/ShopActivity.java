package com.v2gogo.project.activity.shop;

import java.util.List;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.BaseActivity;
import com.v2gogo.project.adapter.SliderAdapter;
import com.v2gogo.project.adapter.shop.ShopAdapter;
import com.v2gogo.project.adapter.shop.ShopGridAdapter;
import com.v2gogo.project.domain.SliderInfo;
import com.v2gogo.project.domain.shop.GoodsListInfo;
import com.v2gogo.project.listener.SimplePageChangeListener;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.manager.shop.ShopManager;
import com.v2gogo.project.manager.shop.ShopManager.IonGetGoodsListCallback;
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
 * 团购
 * 
 * @author houjun
 */
public class ShopActivity extends BaseActivity implements OnPullRefreshListener, OnLoadMoreListener, OnItemClickListener, IonRetryLoadDatasCallback,
		OnClickListener
{

	public static final String SHOW_BACK = "show_back";

	private DotViews mDotViews;
	private ADViewPager mAdViewPager;
	private FrameLayout mFrameLayout;
	private ImageButton mSwitchBtn;

	private ProgressLayout mProgressLayout;
	private PullRefreshListView mPullRefreshListView;

	private ShopAdapter mShopAdapter;
	private ShopGridAdapter mShopGridAdapter;
	private GoodsListInfo mGoodsListInfo;

	private boolean mIsShowBack;
	private boolean mIsDisplayGrid = false;

	@Override
	public void onInitViews()
	{
		mSwitchBtn = (ImageButton) findViewById(R.id.home_everyday_shop_switch_display);
		mSwitchBtn.setEnabled(false);
		mProgressLayout = (ProgressLayout) findViewById(R.id.home_everyday_shop_progress_layout);
		mPullRefreshListView = (PullRefreshListView) findViewById(R.id.home_everyday_shop_pull_to_refresh_listview);
		initHeaderView();
		displayBackBtn();
	}

	private void initHeaderView()
	{
		View headerView = LayoutInflater.from(this).inflate(R.layout.shop_activity_header_layout, null);
		mDotViews = (DotViews) headerView.findViewById(R.id.shop_activity_header_big_photo_dots);
		mFrameLayout = (FrameLayout) headerView.findViewById(R.id.shop_activity_header_framelayout);
		mAdViewPager = (ADViewPager) headerView.findViewById(R.id.shop_activity_header_big_photo_display);
		int height = (int) (ScreenUtil.getScreenWidth(this) * 1.0f / 2);
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ScreenUtil.getScreenWidth(this), height);
		mAdViewPager.setLayoutParams(layoutParams);
		mFrameLayout.setVisibility(View.GONE);
		mPullRefreshListView.addHeaderView(headerView);
		mSwitchBtn.setEnabled(true);
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.shop_activity_layout;
	}

	@Override
	protected void onInitIntentData(Intent intent)
	{
		super.onInitIntentData(intent);
		if (null != intent)
		{
			mIsShowBack = intent.getBooleanExtra(SHOW_BACK, false);
		}
	}

	@Override
	protected boolean isSetting()
	{
		if (mIsShowBack)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	protected void onInitLoadDatas()
	{
		super.onInitLoadDatas();
		mGoodsListInfo = new GoodsListInfo();
		mShopAdapter = new ShopAdapter(this);
		mPullRefreshListView.setAdapter(mShopAdapter);
		GoodsListInfo goodsListInfo = ShopManager.getAppLocalGoodsListInfos(this);
		if (goodsListInfo == null || goodsListInfo.isEmpty())
		{
			mProgressLayout.showProgress();
		}
		displaySliders(goodsListInfo);
		displayGoodsListDatas(goodsListInfo);
		getShopGoodsList(ShopManager.FIRST_PAGE);
	}

	@Override
	protected boolean isNotifyIntent()
	{
		return true;
	}

	@Override
	protected void registerListener()
	{
		super.registerListener();
		mSwitchBtn.setOnClickListener(this);
		mPullRefreshListView.setOnLoadMoreListener(this);
		mPullRefreshListView.setLoadMoreEnable(false);
		mProgressLayout.setOnTryLoadDatasCallback(this);
		mPullRefreshListView.setOnItemClickListener(this);
		mPullRefreshListView.setOnPullRefreshListener(this);
	}

	@Override
	public void onPullDownRefresh(AbsListView pullRefreshView)
	{
		getShopGoodsList(ShopManager.FIRST_PAGE);
	}

	@Override
	public void onLoadMore(AbsListView pullRefreshView)
	{
		getShopGoodsList(mGoodsListInfo.getPage() + 1);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
	{
		if (null != mGoodsListInfo && position >= 2)
		{
			if (!mIsDisplayGrid)
			{
				String id = mGoodsListInfo.getGoodsInfos().get(position - 2).getId();
				StringBuilder url = new StringBuilder();
				url.append(ServerUrlConfig.SERVER_URL).append("/product/groupProductDetail?id=").append(id);

				Intent intent = new Intent(this, GroupGoodsDetailsActivity.class);
				intent.putExtra(GroupGoodsDetailsActivity.GOODS_URL, url.toString());
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				this.startActivity(intent);

			}
		}
	}

	@Override
	public void clearRequestTask()
	{
		ShopManager.clearGetGoodsListTask();
	}

	@Override
	public void onRetryLoadDatas()
	{
		getShopGoodsList(ShopManager.FIRST_PAGE);
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

	@Override
	public void onClick(View view)
	{
		if (!mIsDisplayGrid)
		{
			if (null == mShopGridAdapter)
			{
				mShopGridAdapter = new ShopGridAdapter(this);
			}
			mPullRefreshListView.setAdapter(mShopGridAdapter);
			mShopGridAdapter.resetDatas(mGoodsListInfo.getGoodsInfos());
			mSwitchBtn.setImageResource(R.drawable.shop_list_lis_icon);
		}
		else
		{
			mPullRefreshListView.setAdapter(mShopAdapter);
			mShopAdapter.resetDatas(mGoodsListInfo.getGoodsInfos());
			mSwitchBtn.setImageResource(R.drawable.shop_list_grid_icon);
		}
		mIsDisplayGrid = !mIsDisplayGrid;
	}

	/**
	 * 拉取团购数据
	 * 
	 * @param page
	 */
	private void getShopGoodsList(int page)
	{
		ShopManager.getShopGoodsList(page, new IonGetGoodsListCallback()
		{
			@Override
			public void onGetGoodsListSuccess(GoodsListInfo goodsListInfo)
			{
				mProgressLayout.showContent();
				displayGoodsListDatas(goodsListInfo);
				displaySliders(goodsListInfo);
			}

			@Override
			public void onGetGoodsListFail(String errorMessage)
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
					if (mPullRefreshListView.isLoadingMore())
					{
						mPullRefreshListView.stopLoadMore();
					}
				}
			}
		});
	}

	/**
	 * 显示返回按钮
	 */
	private void displayBackBtn()
	{
		if (null != mBackBtn)
		{
			if (mIsShowBack)
			{
				mBackBtn.setVisibility(View.VISIBLE);
			}
			else
			{
				mBackBtn.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * 显示大图轮播
	 * 
	 * @param goodsListInfo
	 */
	private void displaySliders(GoodsListInfo goodsListInfo)
	{
		if (null != goodsListInfo && null != goodsListInfo.getSliderInfos() && goodsListInfo.getSliderInfos().size() > 0)
		{
			mFrameLayout.setVisibility(View.VISIBLE);
			List<SliderInfo> sliderInfos = goodsListInfo.getSliderInfos();
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
			mFrameLayout.setVisibility(View.GONE);
			if (mAdViewPager.isPlaying())
			{
				mAdViewPager.stop();
			}
			mDotViews.setSize(0);
		}
	}

	/**
	 * 显示商品列表数据
	 * 
	 * @param goodsListInfo
	 */
	private void displayGoodsListDatas(GoodsListInfo goodsListInfo)
	{
		if (null != goodsListInfo)
		{
			boolean isFinish = false;
			if (goodsListInfo.getPage() == ShopManager.FIRST_PAGE)
			{
				mGoodsListInfo.clear();
			}
			if (goodsListInfo.getCount() <= goodsListInfo.getPage())
			{
				isFinish = true;
			}
			mGoodsListInfo.setPage(goodsListInfo.getPage());
			mGoodsListInfo.addAll(goodsListInfo);
			if (mIsDisplayGrid)
			{
				if (null != mShopGridAdapter)
				{
					mShopGridAdapter.resetDatas(mGoodsListInfo.getGoodsInfos());
				}
			}
			else
			{
				mShopAdapter.resetDatas(mGoodsListInfo.getGoodsInfos());
			}
			if (mPullRefreshListView.isLoadingMore())
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
