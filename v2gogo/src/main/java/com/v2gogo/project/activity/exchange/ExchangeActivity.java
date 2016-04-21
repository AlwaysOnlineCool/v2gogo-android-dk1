package com.v2gogo.project.activity.exchange;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.BaseActivity;
import com.v2gogo.project.activity.account.AccountLoginActivity;
import com.v2gogo.project.adapter.exchange.ExchangeAdapter;
import com.v2gogo.project.adapter.exchange.ExchangeAdapter.IonItemExchangeClickListener;
import com.v2gogo.project.adapter.exchange.ExchangeAdapter.IonLuanchExchangeListener;
import com.v2gogo.project.domain.exchange.ExchangeInfo;
import com.v2gogo.project.domain.exchange.PrizeInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.manager.ExchangeManager;
import com.v2gogo.project.manager.ExchangeManager.IonExchangeCallback;
import com.v2gogo.project.manager.ExchangeManager.IonExchangeListCallback;
import com.v2gogo.project.manager.HttpJsonObjectRequest.IOnDataReceiveMessageCallback;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.utils.StatusCode;
import com.v2gogo.project.utils.common.ToastUtil;
import com.v2gogo.project.views.ProgressLayout;
import com.v2gogo.project.views.ProgressLayout.IonRetryLoadDatasCallback;
import com.v2gogo.project.views.ProgressLayout.State;
import com.v2gogo.project.views.dialog.AppNoticeDialog;
import com.v2gogo.project.views.dialog.ExchangePrizeDialog;
import com.v2gogo.project.views.dialog.ExchangePrizeDialog.IonSureClickPrizeCallback;
import com.v2gogo.project.views.listview.refreshview.OnLoadMoreListener;
import com.v2gogo.project.views.listview.refreshview.OnPullRefreshListener;
import com.v2gogo.project.views.listview.refreshview.PullRefreshListView;

/**
 * 兑换专区
 * 
 * @author houjun
 */
public class ExchangeActivity extends BaseActivity implements OnLoadMoreListener, IonLuanchExchangeListener, OnPullRefreshListener,
		IonItemExchangeClickListener, IonRetryLoadDatasCallback, IonSureClickPrizeCallback, OnScrollListener
{
	public static final String SHOW_BACK = "show_back";
	private boolean mIsShowBack = false;

	private ExchangeInfo mExchangeInfo;
	private ExchangeAdapter mExchangeAdapter;

	private ProgressLayout mProgressLayout;
	private PullRefreshListView mPullRefreshListView;

	private AppNoticeDialog mAppNoticeDialog;
	private ExchangePrizeDialog mExchangePrizeDialog;
	private boolean isScroll;// 列表是否在滚动
	private PrizeInfo mPrizeInfo;

	@Override
	public void onInitViews()
	{
		mProgressLayout = (ProgressLayout) findViewById(R.id.fragment_exchange_progress_layout);
		mPullRefreshListView = (PullRefreshListView) findViewById(R.id.fragment_exchange_pull_to_refresh_listview);
		mExchangeAdapter = new ExchangeAdapter(this);
		mPullRefreshListView.setAdapter(mExchangeAdapter);

		displayBackBtn();
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.exchange_activity_layout;
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
	protected void registerListener()
	{
		super.registerListener();
		mProgressLayout.setOnTryLoadDatasCallback(this);
		mPullRefreshListView.setOnLoadMoreListener(this);
		mExchangeAdapter.setOnLuanchExchangeListener(this);
		mPullRefreshListView.setOnPullRefreshListener(this);
		mExchangeAdapter.setOnItemExchangeClickListener(this);
		mPullRefreshListView.setLoadMoreEnable(false);
		mPullRefreshListView.setOnScrollListener(this);
	}

	@Override
	protected void onInitLoadDatas()
	{
		super.onInitLoadDatas();
		mExchangeInfo = new ExchangeInfo();
		ExchangeInfo exchangeInfo = ExchangeManager.getLocalExchangeGoodsList(this);
		if (exchangeInfo == null || exchangeInfo.isEmpty())
		{
			mProgressLayout.showProgress();
		}
		displayExchangeDatas(exchangeInfo);
		getExhangeList(ExchangeManager.FIRST_PAGE);

	}

	private Handler mHandler = new Handler();
	private Runnable runnable = new Runnable()
	{
		@Override
		public void run()
		{
			// LogUtil.e(Thread.currentThread().getName());
			if (!isScroll)// 不滚动的时候更新UI
			{
				mExchangeAdapter.notifyDataSetChanged();
			}
			mHandler.postDelayed(this, 1000);
			caculateResetTime();
			if (!isHasDownTime())
			{
				stopDownTimer();
			}
		}

		// 计算倒计时的时间
		private void caculateResetTime()
		{
			for (PrizeInfo prizeInfo : mExchangeInfo.getPrizeInfos())
			{
				if (prizeInfo.getRestTime() > 0)
				{
					prizeInfo.setRestTime(prizeInfo.getRestTime() - 1);
				}
			}
		}
	};

	// 列表是否有倒计时 有：true 否：false
	private boolean isHasDownTime()
	{
		for (PrizeInfo prizeInfo : mExchangeInfo.getPrizeInfos())
		{
			if (prizeInfo.getRestTime() > 0)
			{
				return true;
			}
		}
		return false;
	}

	// 启动倒计时线程
	private void startDowntTimer()
	{
		mHandler.postDelayed(runnable, 1000);
	}

	// 停止倒计时
	private void stopDownTimer()
	{
		if (mHandler != null)
		{
			mHandler.removeCallbacks(runnable);
		}
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
	protected boolean isNotifyIntent()
	{
		return true;
	}

	@Override
	public void onPullDownRefresh(AbsListView pullRefreshView)
	{
		stopDownTimer();
		getExhangeList(ExchangeManager.FIRST_PAGE);
	}

	@Override
	public void onLuanchExchangeStart(PrizeInfo prizeInfo)
	{
		if (null != prizeInfo)
		{
			if (PrizeInfo.NORMAL == prizeInfo.getIsPub())
			{
				if (V2GogoApplication.getMasterLoginState())
				{
					if (prizeInfo.getPrizepaperType() == 1)
					{
						showExchangeDialog(prizeInfo, R.string.exchange_zhongchou_tip);
						mPrizeInfo = prizeInfo;
					}
					else
					{
						showExchangeDialog(prizeInfo, R.string.exchange_prize_tip);
						mPrizeInfo = prizeInfo;
					}
				}
				else
				{
					AccountLoginActivity.forwardAccountLogin(this);
				}
			}
		}
	}

	@Override
	public void onItemExchangeClick(PrizeInfo prizeInfo)
	{
		if (null != prizeInfo)
		{
			String id = prizeInfo.getId();
			StringBuilder url = new StringBuilder();
			url.append(ServerUrlConfig.SERVER_URL).append("/prizepaperapp/getExchangePrizeDetail?pid=").append(id);

			Intent intent = new Intent(this, ExchangePrizeDetailsActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra(ExchangePrizeDetailsActivity.PRIZE_ID, id);
			intent.putExtra(ExchangePrizeDetailsActivity.PRIZE_URL, url.toString());
			startActivity(intent);
		}
	}

	@Override
	public void onLoadMore(AbsListView pullRefreshView)
	{
		stopDownTimer();
		getExhangeList(mExchangeInfo.getCurrentPage() + 1);
	}

	@Override
	public void clearRequestTask()
	{
		ExchangeManager.clearExchangeGoodsListTask();
		ExchangeManager.clearExchangeGoodsByGoodsIdTask();
	}

	@Override
	public void onRetryLoadDatas()
	{
		getExhangeList(ExchangeManager.FIRST_PAGE);
	}

	@Override
	public void onSureClickPrize(String id, String receiveId)
	{
		if (mPrizeInfo != null && mPrizeInfo.getPrizepaperType() == 1)// 众筹
		{
			raiseProject();
		}
		else
		{
			if (!TextUtils.isEmpty(id))
			{
				ExchangeManager.exchangeGoodsByGoodsId(id, V2GogoApplication.getCurrentMatser().getUsername(), new IonExchangeCallback()
				{
					@Override
					public void onExchangeSuccess()
					{
						mProgressLayout.showContent();
						if (mPrizeInfo != null)
						{
							int supplyCount = mPrizeInfo.getSupply() - 1;
							mPrizeInfo.setSupply(supplyCount >= 0 ? supplyCount : 0);
							mExchangeAdapter.notifyDataSetChanged();
						}
						showExchangeSuccessDialog();
					}

					@Override
					public void onExchangeFail(String errorMessage)
					{
						ToastUtil.showAlertToast(getParent(), errorMessage);
					}
				});
			}
		}
	}

	/**
	 * 项目众筹
	 */
	private void raiseProject()
	{
		ExchangeManager.convertpaper(mPrizeInfo.getId(), new IOnDataReceiveMessageCallback()
		{

			@Override
			public void onSuccess(int code, String message, JSONObject response)
			{
				if (StatusCode.SUCCESS == code)
				{
					mProgressLayout.showContent();
					if (mPrizeInfo != null)
					{
						int supplyCount = mPrizeInfo.getSupply() - 1;
						mPrizeInfo.setSupply(supplyCount >= 0 ? supplyCount : 0);
						mExchangeAdapter.notifyDataSetChanged();
					}
					showExchangeSuccessDialog();
				}
				else
				{
					ToastUtil.showAlertToast(getParent(), message);
				}
			}

			@Override
			public void onError(String errorMessage)
			{
				ToastUtil.showAlertToast(getParent(), errorMessage);
			}
		});
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		stopDownTimer();
	}

	/**
	 * 兑换奖品列表
	 * 
	 * @param page
	 */
	private void getExhangeList(int page)
	{
		ExchangeManager.getExchangeGoodsList(page, new IonExchangeListCallback()
		{
			@Override
			public void onExchangeListSuccess(ExchangeInfo exchangeInfo)
			{
				mProgressLayout.showContent();
				displayExchangeDatas(exchangeInfo);
				startDowntTimer();
			}

			@Override
			public void onExchangeListFail(String errorMessage)
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
	 * 显示兑换成功对话框
	 */
	private void showExchangeSuccessDialog()
	{
		if (null == mAppNoticeDialog)
		{
			mAppNoticeDialog = new AppNoticeDialog(this, R.style.style_action_sheet_dialog);
		}
		if (!mAppNoticeDialog.isShowing())
		{
			mAppNoticeDialog.show();
			mAppNoticeDialog.setSureTitleAndMessage(R.string.exchange_prize_success, R.string.app_notice_sure_tip);
		}
	}

	/**
	 * 显示兑换对话框
	 */
	private void showExchangeDialog(final PrizeInfo prizeInfo, int messageTip)
	{
		if (null == mExchangePrizeDialog)
		{
			mExchangePrizeDialog = new ExchangePrizeDialog(this, R.style.style_action_sheet_dialog);
			mExchangePrizeDialog.setOnClickPrizeCallback(this);
		}
		if (!mExchangePrizeDialog.isShowing())
		{
			mExchangePrizeDialog.show();
			mExchangePrizeDialog.setMessage(messageTip);
			mExchangePrizeDialog.setPrizeInfosId(prizeInfo.getId(), null);
		}
	}

	/**
	 * 显示兑换数据
	 */
	private void displayExchangeDatas(ExchangeInfo exchangeInfo)
	{
		if (null != exchangeInfo)
		{
			boolean isFinish = false;
			boolean isMore = false;
			if (exchangeInfo.getCurrentPage() == ExchangeManager.FIRST_PAGE)
			{
				mExchangeInfo.clear();
			}
			else
			{
				isMore = true;
			}
			if (exchangeInfo.getCount() <= exchangeInfo.getCurrentPage())
			{
				isFinish = true;
			}
			mExchangeInfo.setCurrentPage(exchangeInfo.getCurrentPage());
			mExchangeInfo.addAll(exchangeInfo);
			mExchangeAdapter.resetDatas(mExchangeInfo);
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

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState)
	{
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE)
		{
			isScroll = false;
		}
		else
		{
			isScroll = true;
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
	{
		// TODO Auto-generated method stub

	}

}
