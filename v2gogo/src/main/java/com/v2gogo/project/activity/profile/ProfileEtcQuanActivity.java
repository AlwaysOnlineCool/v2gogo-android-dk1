package com.v2gogo.project.activity.profile;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.BaseActivity;
import com.v2gogo.project.activity.shop.GroupGoodsDetailsActivity;
import com.v2gogo.project.adapter.profile.ProfileEtcQuanAdapter;
import com.v2gogo.project.adapter.profile.ProfileEtcQuanAdapter.IonClickButton;
import com.v2gogo.project.domain.exchange.PrizeInfo;
import com.v2gogo.project.domain.profile.ProfileEtcOrderListInfo;
import com.v2gogo.project.domain.shop.EtcOrderInfo;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.manager.profile.ProfileEtcQuanManager;
import com.v2gogo.project.manager.profile.ProfileEtcQuanManager.IonProfileEtcQuanCallback;
import com.v2gogo.project.manager.profile.ProfileEtcQuanManager.IonProfileEtcQuanListCallback;
import com.v2gogo.project.utils.common.ToastUtil;
import com.v2gogo.project.views.ProgressLayout;
import com.v2gogo.project.views.ProgressLayout.IonRetryLoadDatasCallback;
import com.v2gogo.project.views.ProgressLayout.State;
import com.v2gogo.project.views.dialog.ProfileGetCodeSheetCode;
import com.v2gogo.project.views.dialog.ProfileGetCodeSheetCode.ACTION;
import com.v2gogo.project.views.dialog.ProfileGetCodeSheetCode.IonActionSheetClickListener;
import com.v2gogo.project.views.listview.refreshview.OnLoadMoreListener;
import com.v2gogo.project.views.listview.refreshview.OnPullRefreshListener;
import com.v2gogo.project.views.listview.refreshview.PullRefreshListView;

/**
 * 我的电子券
 * 
 * @author houjun
 */
public class ProfileEtcQuanActivity extends BaseActivity implements IonRetryLoadDatasCallback, OnLoadMoreListener, OnPullRefreshListener, OnItemClickListener,
		IonActionSheetClickListener, IonClickButton
{

	private ProgressLayout mProgressLayout;
	private PullRefreshListView mPullRefreshListView;

	private ProfileEtcQuanAdapter mProfileEtcQuanAdapter;
	private ProfileEtcOrderListInfo mProfileEtcOrderListInfo;
	private ProfileGetCodeSheetCode mProfileGetCodeSheetCode;
	private EtcOrderInfo mEtcOrderInfo;

	@Override
	public void clearRequestTask()
	{
		ProfileEtcQuanManager.clearEtcGetTask();
		ProfileEtcQuanManager.clearEtcListTask();
	}

	@Override
	public void onInitViews()
	{
		mProgressLayout = (ProgressLayout) findViewById(R.id.profile_quan_progress_layout);
		mPullRefreshListView = (PullRefreshListView) findViewById(R.id.profile_quan_pull_to_refresh_listview);
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.profile_etc_qua_activity_layout;
	}

	@Override
	protected void onInitLoadDatas()
	{
		super.onInitLoadDatas();
		mProgressLayout.showProgress();
		mProfileEtcOrderListInfo = new ProfileEtcOrderListInfo();
		mProfileEtcQuanAdapter = new ProfileEtcQuanAdapter();
		mProfileEtcQuanAdapter.setOnClickButton(this);
		mPullRefreshListView.setAdapter(mProfileEtcQuanAdapter);
		loadProfileEtcList(ProfileEtcQuanManager.FIRST_PAGE);
	}

	@Override
	protected void registerListener()
	{
		super.registerListener();
		mProgressLayout.setOnTryLoadDatasCallback(this);
		mPullRefreshListView.setOnLoadMoreListener(this);
		mPullRefreshListView.setOnItemClickListener(this);
		mPullRefreshListView.setOnPullRefreshListener(this);
		mPullRefreshListView.setLoadMoreEnable(false);
	}

	@Override
	public void onRetryLoadDatas()
	{
		loadProfileEtcList(ProfileEtcQuanManager.FIRST_PAGE);
	}

	@Override
	public void onLoadMore(AbsListView pullRefreshView)
	{
		loadProfileEtcList(mProfileEtcOrderListInfo.getCurrentPage() + 1);
	}

	@Override
	public void onPullDownRefresh(AbsListView pullRefreshView)
	{
		loadProfileEtcList(ProfileEtcQuanManager.FIRST_PAGE);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
	{
		if (null != mProfileEtcOrderListInfo && null != mProfileEtcOrderListInfo.getEtcOrderInfos() && position >= 1)
		{
			EtcOrderInfo etcOrderInfo = mProfileEtcOrderListInfo.getEtcOrderInfos().get(position - 1);
			if (null != etcOrderInfo)
			{
				forwardPrizeDetails(etcOrderInfo.getProductId());
			}
		}
	}

	@Override
	public void onActionSheetClick(ACTION action, String string, PrizeInfo mPrizeInfo, ProfileGetCodeSheetCode profileGetCodeSheetCode)
	{
		getEtcOrder(action, string, mPrizeInfo);
	}

	@Override
	public void onClickButton(EtcOrderInfo etcOrderInfo)
	{
		mEtcOrderInfo = etcOrderInfo;
		showGetGoodsDialog();
	}

	/**
	 * 领取电子券
	 */
	private void getEtcOrder(ACTION action, String string, PrizeInfo aPrizeInfo)
	{
		if (action == ACTION.SURE)
		{
			if (TextUtils.isEmpty(string))
			{
				ToastUtil.showAlertToast(this, R.string.profile_et_you_input_code_empty_tip);
			}
			else
			{
				if (null != mEtcOrderInfo)
				{
					if (string.trim().equals(mEtcOrderInfo.getReplayCode().trim()))
					{
						ProfileEtcQuanManager.getProfileEtcQuan(string.trim(), mEtcOrderInfo.getId(), new IonProfileEtcQuanCallback()
						{
							@Override
							public void onProfileEtcQuanSuccess(String id, String message)
							{
								ToastUtil.showConfirmToast(ProfileEtcQuanActivity.this, message);
								dealGetDatas(id);
							}

							@Override
							public void onProfileEtcQuanFail(int code, String message)
							{
								ToastUtil.showAlertToast(ProfileEtcQuanActivity.this, message);
							}
						});
						mEtcOrderInfo = null;
					}
					else
					{
						ToastUtil.showAlertToast(this, R.string.profile_et_you_input_code_error_tip);
					}
				}
			}
		}
	}

	/**
	 * 处理领取数据
	 * 
	 * @param id
	 */
	private void dealGetDatas(String id)
	{
		if (null != mProfileEtcOrderListInfo && null != mProfileEtcOrderListInfo.getEtcOrderInfos() && mProfileEtcOrderListInfo.getEtcOrderInfos().size() > 0)
		{
			boolean isExist = false;
			for (EtcOrderInfo etcOrderInfo : mProfileEtcOrderListInfo.getEtcOrderInfos())
			{
				if (etcOrderInfo != null)
				{
					if (id.equals(etcOrderInfo.getId()))
					{
						isExist = true;
						etcOrderInfo.setReplayStatus(EtcOrderInfo.ETC_YET_USED);
						break;
					}
				}
			}
			if (isExist)
			{
				mProfileEtcQuanAdapter.resetDatas(mProfileEtcOrderListInfo.getEtcOrderInfos());
			}
		}
	}

	/**
	 * 得到用户电子券
	 * 
	 * @param page
	 */
	private void loadProfileEtcList(int page)
	{
		ProfileEtcQuanManager.getProfileEtcQuanList(page, new IonProfileEtcQuanListCallback()
		{
			@Override
			public void onProfileEtcQuanListSuccess(ProfileEtcOrderListInfo profileEtcOrderListInfo)
			{
				displayProfileEtcDatas(profileEtcOrderListInfo);
			}

			@Override
			public void onProfileEtcQuanListFail(int code, String message)
			{
				displayFail();
				ToastUtil.showAlertToast(ProfileEtcQuanActivity.this, message);
			}
		});
	}

	/**
	 * 显示失败
	 */
	private void displayFail()
	{
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

	/**
	 * 显示电子券的数据
	 */
	private void displayProfileEtcDatas(ProfileEtcOrderListInfo profileEtcOrderListInfo)
	{
		if (null != profileEtcOrderListInfo)
		{
			boolean isFinish = false;
			boolean isMore = false;
			if (profileEtcOrderListInfo.getCurrentPage() == ProfileEtcQuanManager.FIRST_PAGE)
			{
				mProfileEtcOrderListInfo.clear();
			}
			else
			{
				isMore = true;
			}
			if (profileEtcOrderListInfo.getCount() <= profileEtcOrderListInfo.getCurrentPage())
			{
				isFinish = true;
			}
			mProfileEtcOrderListInfo.setCurrentPage(profileEtcOrderListInfo.getCurrentPage());
			mProfileEtcOrderListInfo.addAll(profileEtcOrderListInfo);
			mProfileEtcQuanAdapter.resetDatas(mProfileEtcOrderListInfo.getEtcOrderInfos());
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
			if (mProfileEtcQuanAdapter.getCount() == 0)
			{
				mProgressLayout.showEmptyText();
			}
			else
			{
				mProgressLayout.showContent();
			}
		}
	}

	/**
	 * 跳转到奖品详情
	 * 
	 * @param position
	 */
	private void forwardPrizeDetails(String id)
	{
		StringBuilder url = new StringBuilder();
		url.append(ServerUrlConfig.SERVER_URL).append("/product/groupProductDetail?id=").append(id);

		Intent intent = new Intent(this, GroupGoodsDetailsActivity.class);
		intent.putExtra(GroupGoodsDetailsActivity.GOODS_URL, url.toString());
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(intent);
	}

	/**
	 * 显示领取的对话框
	 * 
	 * @param prizeInfo
	 */
	private void showGetGoodsDialog()
	{
		if (null == mProfileGetCodeSheetCode)
		{
			mProfileGetCodeSheetCode = new ProfileGetCodeSheetCode(this, R.style.style_action_sheet_dialog);
			mProfileGetCodeSheetCode.setSheetClickListener(this);
		}
		if (!mProfileGetCodeSheetCode.isShowing())
		{
			mProfileGetCodeSheetCode.show();
			mProfileGetCodeSheetCode.setDatas(getString(R.string.profile_et_you_input_code_empty_tip), null);
		}
	}
}
