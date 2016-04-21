package com.v2gogo.project.activity.profile;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.BaseActivity;
import com.v2gogo.project.activity.WebViewActivity;
import com.v2gogo.project.activity.exchange.ExchangePrizeDetailsActivity;
import com.v2gogo.project.adapter.profile.ProfilePrizeAdapter;
import com.v2gogo.project.adapter.profile.ProfilePrizeAdapter.IonClickButton;
import com.v2gogo.project.domain.exchange.PrizeInfo;
import com.v2gogo.project.domain.profile.ProfilePrizeListInfo;
import com.v2gogo.project.domain.shop.OrderInfo;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.manager.profile.ProfileMessageManager;
import com.v2gogo.project.manager.profile.ProfilePrizeManager;
import com.v2gogo.project.manager.profile.ProfilePrizeManager.IonGetCrowdFundingCallback;
import com.v2gogo.project.manager.profile.ProfilePrizeManager.IonGetPrizeGoodsCallback;
import com.v2gogo.project.manager.profile.ProfilePrizeManager.IonProfilePrizeListCallback;
import com.v2gogo.project.utils.common.ToastUtil;
import com.v2gogo.project.views.ProgressLayout;
import com.v2gogo.project.views.ProgressLayout.IonRetryLoadDatasCallback;
import com.v2gogo.project.views.ProgressLayout.State;
import com.v2gogo.project.views.dialog.AppNoticeDialog;
import com.v2gogo.project.views.dialog.ExchangePrizeDialog;
import com.v2gogo.project.views.dialog.ExchangePrizeDialog.IonSureClickPrizeCallback;
import com.v2gogo.project.views.dialog.ProfileGetCodeSheetCode;
import com.v2gogo.project.views.dialog.ProfileGetCodeSheetCode.ACTION;
import com.v2gogo.project.views.dialog.ProfileGetCodeSheetCode.IonActionSheetClickListener;
import com.v2gogo.project.views.dialog.ProfilePrizePostActionSheet;
import com.v2gogo.project.views.dialog.ProfilePrizePostActionSheet.IonPostPrizeActionSheetClickListener;
import com.v2gogo.project.views.listview.refreshview.OnLoadMoreListener;
import com.v2gogo.project.views.listview.refreshview.OnPullRefreshListener;
import com.v2gogo.project.views.listview.refreshview.PullRefreshListView;
import com.ypy.eventbus.EventBus;

/**
 * 我的奖品
 * 
 * @author houjun
 */
public class ProfilePrizeActivity extends BaseActivity implements OnLoadMoreListener, OnPullRefreshListener, IonClickButton, OnItemClickListener,
		IonRetryLoadDatasCallback, IonActionSheetClickListener, IonPostPrizeActionSheetClickListener, OnClickListener, IonSureClickPrizeCallback
{

	private Button mBtnGet;
	private ProgressLayout mProgressLayout;
	private ProfilePrizeAdapter mPrizeAdapter;
	private PullRefreshListView mPullRefreshListView;
	private ProfilePrizeListInfo mProfilePrizeListInfo;

	private ProfileGetCodeSheetCode mProfileGetCodeSheetCode;
	private ProfilePrizePostActionSheet mProfilePrizePostActionSheet;

	private AppNoticeDialog mAppNoticeDialog;
	private ExchangePrizeDialog mExchangePrizeDialog;

	@Override
	public void onInitViews()
	{
		EventBus.getDefault().register(this);

		mBtnGet = (Button) findViewById(R.id.profile_prize_get_button);
		mProgressLayout = (ProgressLayout) findViewById(R.id.profile_prize_progress_layout);
		mPullRefreshListView = (PullRefreshListView) findViewById(R.id.profile_prize_pull_to_refresh_listview);
	}

	public void onEventMainThread(int position)
	{
		if (mPrizeAdapter != null)
		{
			PrizeInfo prizeInfo = (PrizeInfo) mPrizeAdapter.getItem(position);
			prizeInfo.setIsComment(OrderInfo.ORDER_STATUS_IS_YES_COMMENT);
			mPrizeAdapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.profile_prize_activity_layout;
	}

	@Override
	protected void onInitLoadDatas()
	{
		super.onInitLoadDatas();
		mProgressLayout.showProgress();
		mPrizeAdapter = new ProfilePrizeAdapter(this);
		mPullRefreshListView.setAdapter(mPrizeAdapter);
		mPrizeAdapter.setOnClickButton(this);
		mProfilePrizeListInfo = new ProfilePrizeListInfo();
		loadProfilePrizeList(ProfilePrizeManager.FIRST_PAGE);

	}

	@Override
	protected void registerListener()
	{
		super.registerListener();
		mBtnGet.setOnClickListener(this);
		mPullRefreshListView.setOnPullRefreshListener(this);
		mPullRefreshListView.setOnLoadMoreListener(this);
		mPullRefreshListView.setLoadMoreEnable(false);
		mPullRefreshListView.setOnItemClickListener(this);
		mProgressLayout.setOnTryLoadDatasCallback(this);
	}

	@Override
	protected boolean isNotifyIntent()
	{
		return true;
	}

	@Override
	public void onLoadMore(AbsListView pullRefreshView)
	{
		loadProfilePrizeList(mProfilePrizeListInfo.getCurrentPage() + 1);
	}

	@Override
	public void onPullDownRefresh(AbsListView pullRefreshView)
	{
		loadProfilePrizeList(ProfilePrizeManager.FIRST_PAGE);
	}

	@Override
	public void onClickButton(PrizeInfo prizeInfo)
	{
		if (null != prizeInfo && prizeInfo.getStatus() == 0)
		{
			if (prizeInfo.getPrizepaperType() == 1)// 众筹
			{
				getCrowdFunding(prizeInfo);
			}
			else
			{
				if (prizeInfo.getReceiveType() == PrizeInfo.RECEIVE_TYPE_ZHIJIE)
				{
					showGetPrizeConfirmDialog(prizeInfo);
				}
				else if (prizeInfo.getReceiveType() == PrizeInfo.RECEIVE_TYPE_XIANCHANG)
				{
					showGetGoodsDialog(prizeInfo);
				}
				else if (prizeInfo.getReceiveType() == PrizeInfo.RECEIVE_TYPE_POST)
				{
					showPostGoodsDialog(prizeInfo);
				}
			}
		}
	}

	private void getCrowdFunding(PrizeInfo prizeInfo)
	{
		if (prizeInfo != null)
		{
			ProfilePrizeManager.getcrowdfunding(prizeInfo.getPrizepaperid(), new IonGetCrowdFundingCallback()
			{
				@Override
				public void onGetCrowdFundingSuccess(String resultInfo)
				{
					showGetCrowdFundingResult(resultInfo);
				}

				@Override
				public void onGetCrowdFundingFail(String errorMessage)
				{
					ToastUtil.showAlertToast(ProfilePrizeActivity.this, errorMessage);
				}
			});
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
	{
		forwardPrizeDetails(position);
		// forwardH5(position);
	}

	@Override
	public void clearRequestTask()
	{
		ProfilePrizeManager.clearGetPrizeGoodsTask();
		ProfilePrizeManager.clearGetProfilePrizeListTask();
	}

	@Override
	public void onRetryLoadDatas()
	{
		loadProfilePrizeList(ProfilePrizeManager.FIRST_PAGE);
	}

	@Override
	public void onClick(View view)
	{
		forwardPrizeDescription();
	}

	@Override
	public void onActionSheetClick(ACTION action, String text, PrizeInfo prizeInfo, ProfileGetCodeSheetCode profileActionSheetDialog)
	{
		getArrivePrize(action, text, prizeInfo);
	}

	@Override
	public void onPostPrizeActionListener(com.v2gogo.project.views.dialog.ProfilePrizePostActionSheet.ACTION action, String name, String phone, String address,
			PrizeInfo prizeInfo, ProfilePrizePostActionSheet profilePrizePostSheetCode)
	{
		getPostPrize(action, name, phone, address, prizeInfo);
	}

	@Override
	public void onSureClickPrize(String id, String receiveId)
	{
		if (!TextUtils.isEmpty(id))
		{
			getProfilePrize("", "", "", id, Integer.parseInt(receiveId));
		}
	}

	/**
	 * 拉取我的奖品数据
	 * 
	 * @param page
	 */
	private void loadProfilePrizeList(int page)
	{
		ProfilePrizeManager.getProfilePrizeList(page, new IonProfilePrizeListCallback()
		{
			@Override
			public void onProfilePrizeListSuccess(ProfilePrizeListInfo profilePrizeListInfo)
			{
				displayUserPrizeDatas(profilePrizeListInfo);
			}

			@Override
			public void onProfilePrizeListFail(String errorMessage)
			{
				ToastUtil.showAlertToast(ProfilePrizeActivity.this, errorMessage);
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
	 * 跳转到奖品详情
	 * 
	 * @param position
	 */
	private void forwardPrizeDetails(int position)
	{
		if (mProfilePrizeListInfo != null && mProfilePrizeListInfo.getPrizeInfos() != null && position >= 1)
		{
			PrizeInfo prizeInfo = mProfilePrizeListInfo.getPrizeInfos().get(position - 1);
			String id = prizeInfo.getId();
			StringBuilder url = new StringBuilder();
			url.append(ServerUrlConfig.SERVER_URL).append("/prizepaperapp/getprizedetail?pid=").append(id);

			Intent intent = new Intent(this, ExchangePrizeDetailsActivity.class);
			intent.putExtra(ExchangePrizeDetailsActivity.PRIZE_ID, id);
			intent.putExtra(ExchangePrizeDetailsActivity.PRIZE_URL, url.toString());
			startActivity(intent);
		}
	}

	/**
	 * 跳转到奖品说明
	 */
	private void forwardPrizeDescription()
	{
		Intent intent = new Intent(this, WebViewActivity.class);
		intent.putExtra(WebViewActivity.URL, "http://www.v2gogo.com/get.html");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	/**
	 * 领取现场领取奖品
	 */
	private void getArrivePrize(ACTION action, String text, PrizeInfo prizeInfo)
	{
		if (action == ACTION.SURE)
		{
			if (TextUtils.isEmpty(text))
			{
				ToastUtil.showAlertToast(this, R.string.delete_prize_tip);
			}
			else
			{
				if (TextUtils.isEmpty(prizeInfo.getReplayCode()))
				{
					ToastUtil.showAlertToast(this, "服务器返回领取码为空!");
					return;
				}
				if (text.trim().equals(prizeInfo.getReplayCode().trim()))
				{
					if (prizeInfo != null)
					{
						getProfilePrize("", "", "", prizeInfo.getId(), prizeInfo.getReceiveType());
					}
				}
				else
				{
					ToastUtil.showAlertToast(this, R.string.profile_prize_you_input_code_error_tip);
				}
			}
		}
	}

	/**
	 * 领取邮寄奖品
	 */
	private void getPostPrize(com.v2gogo.project.views.dialog.ProfilePrizePostActionSheet.ACTION action, String name, String phone, String address,
			PrizeInfo prizeInfo)
	{
		if (action == com.v2gogo.project.views.dialog.ProfilePrizePostActionSheet.ACTION.SURE)
		{
			if (TextUtils.isEmpty(name))
			{
				ToastUtil.showAlertToast(this, R.string.please_input_get_username_tip);
				return;
			}
			if (TextUtils.isEmpty(phone))
			{
				ToastUtil.showAlertToast(this, R.string.please_input_get_phone_tip);
				return;
			}
			if (TextUtils.isEmpty(address))
			{
				ToastUtil.showAlertToast(this, R.string.please_input_get_address_tip);
				return;
			}
			else
			{
				if (null != prizeInfo)
				{
					getProfilePrize(name, phone, address, prizeInfo.getId(), prizeInfo.getReceiveType());
				}
			}
		}
	}

	/**
	 * 领取奖品
	 */
	private void getProfilePrize(String name, String phone, String address, String id, int type)
	{
		ProfilePrizeManager.getPrizeGoods(id, name.trim(), phone.trim(), address.trim(), type, new IonGetPrizeGoodsCallback()
		{
			@Override
			public void onGetPrizeGoodsSuccess(String id, int receiveType)
			{
				dealGetPrizeSuccess(id, receiveType);
			}

			@Override
			public void onGetPrizeGoodsFail(String errorMessage)
			{
				ToastUtil.showAlertToast(ProfilePrizeActivity.this, errorMessage);
			}
		});
	}

	/**
	 * 处理领取奖品成功
	 * 
	 * @param id
	 * @param receiveType
	 */
	private void dealGetPrizeSuccess(String id, int receiveType)
	{
		if (mProfilePrizeListInfo != null && mProfilePrizeListInfo.getPrizeInfos() != null)
		{
			boolean isresult = false;
			for (PrizeInfo info : mProfilePrizeListInfo.getPrizeInfos())
			{
				if (info != null)
				{
					if (id.equals(info.getId()))
					{
						isresult = true;
						info.setStatus(1);
						break;
					}
				}
			}
			if (isresult)
			{
				int resId = 0;
				if (receiveType == PrizeInfo.RECEIVE_TYPE_POST)
				{
					resId = R.string.profile_prize_prize_3_three_arrive_tip;
				}
				else if (receiveType == PrizeInfo.RECEIVE_TYPE_XIANCHANG)
				{
					resId = R.string.profile_prize_you_get_prize_success_tip;
				}
				else if (receiveType == PrizeInfo.RECEIVE_TYPE_ZHIJIE)
				{
					resId = R.string.profile_prize_send_you_number_tip;
				}
				if (resId != 0)
				{
					if (mAppNoticeDialog == null)
					{
						mAppNoticeDialog = new AppNoticeDialog(this, R.style.style_action_sheet_dialog);
					}
					if (!mAppNoticeDialog.isShowing())
					{
						mAppNoticeDialog.show();
						mAppNoticeDialog.setSureTitleAndMessage(resId, R.string.app_notice_sure_tip);
					}
				}
				mPrizeAdapter.resetDatas(mProfilePrizeListInfo.getPrizeInfos());
			}
		}
	}

	private void showGetCrowdFundingResult(String result)
	{
		if (mAppNoticeDialog == null)
		{
			mAppNoticeDialog = new AppNoticeDialog(this, R.style.style_action_sheet_dialog);
			mAppNoticeDialog.setCancelable(false);
			mAppNoticeDialog.setCanceledOnTouchOutside(false);
		}
		if (!mAppNoticeDialog.isShowing())
		{
			mAppNoticeDialog.show();
			mAppNoticeDialog.setSureTipAndMessage(result, getString(R.string.app_notice_sure_tip));
		}
	}

	/**
	 * 显示领取虚拟奖品的对话框
	 * 
	 * @param prizeInfo
	 */
	private void showGetPrizeConfirmDialog(final PrizeInfo prizeInfo)
	{
		if (null == mExchangePrizeDialog)
		{
			mExchangePrizeDialog = new ExchangePrizeDialog(this, R.style.style_action_sheet_dialog);
			mExchangePrizeDialog.setOnClickPrizeCallback(this);
		}
		if (!mExchangePrizeDialog.isShowing())
		{
			mExchangePrizeDialog.show();
			mExchangePrizeDialog.setMessage(R.string.you_sure_get_prize_tip);
			mExchangePrizeDialog.setPrizeInfosId(prizeInfo.getId(), prizeInfo.getReceiveType() + "");
		}
	}

	/**
	 * 显示领取的对话框
	 * 
	 * @param prizeInfo
	 */
	private void showGetGoodsDialog(final PrizeInfo prizeInfo)
	{
		if (null == mProfileGetCodeSheetCode)
		{
			mProfileGetCodeSheetCode = new ProfileGetCodeSheetCode(this, R.style.style_action_sheet_dialog);
			mProfileGetCodeSheetCode.setSheetClickListener(this);
		}
		if (!mProfileGetCodeSheetCode.isShowing())
		{
			mProfileGetCodeSheetCode.show();
			mProfileGetCodeSheetCode.setDatas(getString(R.string.delete_prize_tip), prizeInfo);
		}
	}

	/**
	 * 显示邮寄的对话框
	 * 
	 * @param prizeInfo
	 */
	private void showPostGoodsDialog(final PrizeInfo prizeInfo)
	{
		if (null == mProfilePrizePostActionSheet)
		{
			mProfilePrizePostActionSheet = new ProfilePrizePostActionSheet(this, R.style.style_action_sheet_dialog);
			mProfilePrizePostActionSheet.setSheetClickListener(this);
		}
		if (!mProfilePrizePostActionSheet.isShowing())
		{
			mProfilePrizePostActionSheet.show();
			mProfilePrizePostActionSheet.setDatas(prizeInfo);
		}
	}

	/**
	 * 显示用户奖品信息
	 * 
	 * @param profilePrizeListInfo
	 */
	private void displayUserPrizeDatas(ProfilePrizeListInfo profilePrizeListInfo)
	{
		if (null != profilePrizeListInfo)
		{
			boolean isFinish = false;
			boolean isMore = false;
			if (profilePrizeListInfo.getCurrentPage() == ProfileMessageManager.FIRST_PAGE)
			{
				mProfilePrizeListInfo.clear();
			}
			else
			{
				isMore = true;
			}
			if (profilePrizeListInfo.getCount() <= profilePrizeListInfo.getCurrentPage())
			{
				isFinish = true;
			}
			mProfilePrizeListInfo.setCurrentPage(profilePrizeListInfo.getCurrentPage());
			mProfilePrizeListInfo.addAll(profilePrizeListInfo);
			mPrizeAdapter.resetDatas(mProfilePrizeListInfo.getPrizeInfos());
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
			if (mPrizeAdapter.getCount() == 0)
			{
				mProgressLayout.showEmptyText();
			}
			else
			{
				mProgressLayout.showContent();
			}
		}
	}

}
