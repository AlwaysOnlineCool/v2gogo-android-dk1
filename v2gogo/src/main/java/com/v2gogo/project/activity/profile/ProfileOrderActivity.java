package com.v2gogo.project.activity.profile;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.BaseActivity;
import com.v2gogo.project.adapter.profile.ProfileOrderAdapter;
import com.v2gogo.project.adapter.profile.ProfileOrderAdapter.IonCancleOrderCallback;
import com.v2gogo.project.adapter.profile.ProfileOrderAdapter.IonFlowOrderCallback;
import com.v2gogo.project.domain.profile.ProfileOrderListInfo;
import com.v2gogo.project.domain.shop.OrderInfo;
import com.v2gogo.project.manager.profile.ProfileOrderManage;
import com.v2gogo.project.manager.profile.ProfileOrderManage.IonDeleteProfileOrderCallback;
import com.v2gogo.project.manager.profile.ProfileOrderManage.IonProfileOrderListCallback;
import com.v2gogo.project.utils.common.ToastUtil;
import com.v2gogo.project.views.ProgressLayout;
import com.v2gogo.project.views.ProgressLayout.IonRetryLoadDatasCallback;
import com.v2gogo.project.views.ProgressLayout.State;
import com.v2gogo.project.views.dialog.AppNoticeDialog;
import com.v2gogo.project.views.expandablelistview.PullExpandableListview;
import com.v2gogo.project.views.listview.refreshview.OnLoadMoreListener;
import com.v2gogo.project.views.listview.refreshview.OnPullRefreshListener;
import com.ypy.eventbus.EventBus;

/**
 * 我的订单
 * 
 * @author houjun
 */
public class ProfileOrderActivity extends BaseActivity implements OnPullRefreshListener, OnLoadMoreListener, OnGroupClickListener, IonCancleOrderCallback,
		IonFlowOrderCallback, IonRetryLoadDatasCallback
{

	private ProfileOrderAdapter mOrderAdapter;

	private ProfileOrderListInfo mProfileOrderListInfo;

	private ProgressLayout mProgressLayout;
	private PullExpandableListview mPullExpandableListview;

	private AppNoticeDialog mAppNoticeDialog;

	@Override
	public void onInitViews()
	{
		EventBus.getDefault().register(this);

		mProgressLayout = (ProgressLayout) findViewById(R.id.profile_order_progress_layout);
		mPullExpandableListview = (PullExpandableListview) findViewById(R.id.profile_order_pull_to_refresh_expandablelistview);
	}

	public void onEventMainThread(int groupPosition)
	{
		if (mOrderAdapter != null)
		{
			OrderInfo orderInfo = (OrderInfo) mOrderAdapter.getGroup(groupPosition);
			orderInfo.setIsComment(OrderInfo.ORDER_STATUS_IS_YES_COMMENT);
			mOrderAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.profile_order_activity_layout;
	}

	@Override
	protected void registerListener()
	{
		super.registerListener();
		mOrderAdapter.setOnCancleOrderCallback(this);
		mOrderAdapter.setOnFlowOrderCallback(this);
		mPullExpandableListview.setOnPullRefreshListener(this);
		mProgressLayout.setOnTryLoadDatasCallback(this);
		mPullExpandableListview.setOnLoadMoreListener(this);
		mPullExpandableListview.setOnGroupClickListener(this);
		mPullExpandableListview.setLoadMoreEnable(false);
	}

	@Override
	protected void onInitLoadDatas()
	{
		super.onInitLoadDatas();
		mProgressLayout.showProgress();
		mOrderAdapter = new ProfileOrderAdapter(this, mPullExpandableListview);
		mPullExpandableListview.setAdapter(mOrderAdapter);
		mProfileOrderListInfo = new ProfileOrderListInfo();
		getProfileOrderList(ProfileOrderManage.FIRST_PAGE);
	}

	@Override
	public void clearRequestTask()
	{
		ProfileOrderManage.clearProfileOrderListTask();
	}

	@Override
	public void onPullDownRefresh(AbsListView pullRefreshView)
	{
		getProfileOrderList(ProfileOrderManage.FIRST_PAGE);
	}

	@Override
	public void onLoadMore(AbsListView pullRefreshView)
	{
		getProfileOrderList(mProfileOrderListInfo.getPage() + 1);
	}

	@Override
	public boolean onGroupClick(ExpandableListView arg0, View arg1, int groupView, long arg3)
	{
		return true;
	}

	@Override
	public void onCancleOrder(OrderInfo orderInfo)
	{
		if (orderInfo != null)
		{
			ProfileOrderManage.deleteProilfeOrder(orderInfo, new IonDeleteProfileOrderCallback()
			{
				@Override
				public void onDeleteProfileOrderSuccess(OrderInfo orderInfo)
				{
					if (orderInfo != null)
					{
						boolean isHas = mProfileOrderListInfo.removeOrder(orderInfo);
						if (isHas)
						{
							mOrderAdapter.resetDatas(mProfileOrderListInfo.getOrderInfos());
						}
					}
				}

				@Override
				public void onDeleteProfileOrderFail(String errorMessage)
				{
					ToastUtil.showAlertToast(ProfileOrderActivity.this, errorMessage);
				}
			});
		}
	}

	@Override
	public void onFlowOrder(OrderInfo orderInfo)
	{
		if (null != orderInfo)
		{
			showFlowDialog(orderInfo);
		}
	}

	@Override
	public void onRetryLoadDatas()
	{
		getProfileOrderList(ProfileOrderManage.FIRST_PAGE);
	}

	/**
	 * 个人订单列表数据
	 * 
	 * @param page
	 */
	private void getProfileOrderList(int page)
	{
		ProfileOrderManage.getProfileOrderList(page, new IonProfileOrderListCallback()
		{

			@Override
			public void onProfileOrderListSuccess(ProfileOrderListInfo profileOrderListInfo)
			{
				displayProfileOrderListDatas(profileOrderListInfo);
			}

			@Override
			public void onProfileOrderFail(String errorMessage)
			{
				ToastUtil.showAlertToast(ProfileOrderActivity.this, errorMessage);
				if (mProgressLayout.getState() != State.CONTENT)
				{
					mProgressLayout.showErrorText();
				}
				else
				{
					if (mPullExpandableListview.isRefreshing())
					{
						mPullExpandableListview.stopLoadMore();
					}
					if (mPullExpandableListview.isLoadingMore())
					{
						mPullExpandableListview.stopLoadMore();
					}
				}
			}
		});
	}

	/**
	 * 显示个人订单列表数据
	 * 
	 * @param profileOrderListInfo
	 */
	private void displayProfileOrderListDatas(ProfileOrderListInfo profileOrderListInfo)
	{
		if (profileOrderListInfo != null)
		{
			boolean isFinish = false;
			boolean isMore = false;
			if (profileOrderListInfo.getPage() == ProfileOrderManage.FIRST_PAGE)
			{
				mProfileOrderListInfo.clear();
			}
			else
			{
				isMore = true;
			}
			if (profileOrderListInfo.getCount() <= profileOrderListInfo.getPage())
			{
				isFinish = true;
			}
			mProfileOrderListInfo.setPage(profileOrderListInfo.getPage());
			mProfileOrderListInfo.addAll(profileOrderListInfo);
			mOrderAdapter.resetDatas(mProfileOrderListInfo.getOrderInfos());
			if (isMore)
			{
				mPullExpandableListview.stopLoadMore();
			}
			else
			{
				mPullExpandableListview.stopPullRefresh();
			}
			if (isFinish)
			{
				mPullExpandableListview.setLoadMoreEnable(false);
			}
			else
			{
				mPullExpandableListview.setLoadMoreEnable(true);
			}
			if (mOrderAdapter.getGroupCount() == 0)
			{
				mProgressLayout.showEmptyText();
			}
			else
			{
				mProgressLayout.showContent();
			}
		}
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	/**
	 * 显示物流信息对话框
	 * 
	 * @param orderInfo
	 */
	private void showFlowDialog(OrderInfo orderInfo)
	{
		if (null == mAppNoticeDialog)
		{
			mAppNoticeDialog = new AppNoticeDialog(this, R.style.style_action_sheet_dialog);
		}
		if (!mAppNoticeDialog.isShowing())
		{
			mAppNoticeDialog.show();
			mAppNoticeDialog.setSureTitleAndMessage(orderInfo.getExpressCompary() + "\n" + orderInfo.getExpressNo(), R.string.app_notice_sure_tip,
					R.string.profile_order_object_flow_tip);
		}
	}
}
