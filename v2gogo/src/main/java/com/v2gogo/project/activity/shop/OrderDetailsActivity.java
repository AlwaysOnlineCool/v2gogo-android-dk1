package com.v2gogo.project.activity.shop;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.BaseActivity;
import com.v2gogo.project.adapter.shop.OrderDetailsAdapter;
import com.v2gogo.project.domain.shop.OrderInfo;
import com.v2gogo.project.manager.shop.OrderManager;
import com.v2gogo.project.manager.shop.OrderManager.IonOrderDetailsCallback;
import com.v2gogo.project.utils.common.DateUtil;
import com.v2gogo.project.utils.common.ToastUtil;
import com.v2gogo.project.views.ProgressLayout;
import com.v2gogo.project.views.ProgressLayout.IonRetryLoadDatasCallback;
import com.v2gogo.project.views.ProgressLayout.State;
import com.v2gogo.project.views.listview.refreshview.OnPullRefreshListener;
import com.v2gogo.project.views.listview.refreshview.PullRefreshListView;

/**
 * 订单详情
 * 
 * @author houjun
 */
public class OrderDetailsActivity extends BaseActivity implements OnPullRefreshListener, IonRetryLoadDatasCallback
{

	public static final String ID = "id";

	private String mId;

	private TextView mTvOrderStatus;
	private TextView mTvOrderNumber;
	private TextView mTvOrderPrice;
	private TextView mTvOrderCreateTime;

	private TextView mTvGoodsPrice;
	private TextView mTvPostPrice;
	private TextView mTvExtraPrice;
	private TextView mTvReceiveName;
	private TextView mTvContactPhone;
	private TextView mTvAddress;
	private TextView mTvPayMethod;

	private ProgressLayout mProgressLayout;
	private PullRefreshListView mPullRefreshListView;

	private OrderDetailsAdapter mOrderDetailsAdapter;

	@Override
	public void onInitViews()
	{
		mProgressLayout = (ProgressLayout) findViewById(R.id.order_details_activity_progress_layout);
		mPullRefreshListView = (PullRefreshListView) findViewById(R.id.order_details_activity_pull_to_refresh_listview);
		mPullRefreshListView.addHeaderView(createHeaderView());
		mPullRefreshListView.addFooterView(createFoooterView());
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.order_details_activity_layout;
	}

	@Override
	protected void registerListener()
	{
		super.registerListener();
		mProgressLayout.setOnTryLoadDatasCallback(this);
		mPullRefreshListView.setOnPullRefreshListener(this);
		mPullRefreshListView.setLoadMoreEnable(false);
	}

	@Override
	protected void onInitLoadDatas()
	{
		super.onInitLoadDatas();
		mOrderDetailsAdapter = new OrderDetailsAdapter(this);
		mPullRefreshListView.setAdapter(mOrderDetailsAdapter);
		if (!TextUtils.isEmpty(mId))
		{
			mProgressLayout.showProgress();
			getOrderDetailsInfo();
		}
	}

	@Override
	protected void onInitIntentData(Intent intent)
	{
		super.onInitIntentData(intent);
		if (null != intent)
		{
			mId = intent.getStringExtra(ID);
		}
	}

	@Override
	public void clearRequestTask()
	{
		OrderManager.clearOrderDetailsTask();
	}

	@Override
	public void onPullDownRefresh(AbsListView pullRefreshView)
	{
		if (!TextUtils.isEmpty(mId))
		{
			getOrderDetailsInfo();
		}
	}

	@Override
	public void onRetryLoadDatas()
	{
		if (!TextUtils.isEmpty(mId))
		{
			getOrderDetailsInfo();
		}
	}

	private View createHeaderView()
	{
		View headerView = LayoutInflater.from(this).inflate(R.layout.order_details_activity_header_layout, null);
		mTvOrderNumber = (TextView) headerView.findViewById(R.id.order_details_activity_header_order_no);
		mTvOrderPrice = (TextView) headerView.findViewById(R.id.order_details_activity_header_order_price);
		mTvOrderStatus = (TextView) headerView.findViewById(R.id.order_details_activity_header_order_status);
		mTvOrderCreateTime = (TextView) headerView.findViewById(R.id.order_details_activity_header_order_create_time);
		return headerView;
	}

	private View createFoooterView()
	{
		View footerView = LayoutInflater.from(this).inflate(R.layout.order_details_activity_footer_layout, null);
		mTvAddress = (TextView) footerView.findViewById(R.id.order_details_activity_footer_address);
		mTvGoodsPrice = (TextView) footerView.findViewById(R.id.order_details_activity_goods_price);
		mTvReceiveName = (TextView) footerView.findViewById(R.id.order_details_activity_footer_receiver);
		mTvPostPrice = (TextView) footerView.findViewById(R.id.order_details_activity_goods_post_price);
		mTvPayMethod = (TextView) footerView.findViewById(R.id.order_details_activity_footer_pay_method);
		mTvExtraPrice = (TextView) footerView.findViewById(R.id.order_details_activity_goods_extra_price);
		mTvContactPhone = (TextView) footerView.findViewById(R.id.order_details_activity_footer_contact_phone);
		return footerView;
	}

	/**
	 * 拉取订单详情的信息
	 */
	private void getOrderDetailsInfo()
	{
		OrderManager.getOrderDetails(mId, new IonOrderDetailsCallback()
		{
			@Override
			public void onOrderDetailsSuccess(OrderInfo info)
			{
				if (info != null)
				{
					mProgressLayout.showContent();
					mTvOrderStatus.setText(info.getOrderStatusString(OrderDetailsActivity.this));
					mTvOrderPrice.setText(info.getOrderTotal() + "");
					mTvOrderCreateTime.setText(DateUtil.convertStringWithTimeStamp(info.getCreateTime()));
					mTvOrderNumber.setText(info.getOrderNo());
					mTvReceiveName.setText(String.format(getString(R.string.order_details_receiver_tip), info.getConsignee()));
					mTvContactPhone.setText(String.format(getString(R.string.order_details_contact_phone_tip), info.getPhone()));
					mTvAddress.setText(String.format(getString(R.string.order_details_post_address_tip), info.getAddress()));
					if (info.getOrderStatus()==1 || info.getOrderStatus()==2)
					{
						switch (info.getPayType())
						{
							case 0:
								mTvPayMethod.setText(getString(R.string.pay_method_tip)+getString(R.string.pay_method_ali));
								break;
							case 1:
								mTvPayMethod.setText(getString(R.string.pay_method_tip)+getString(R.string.pay_method_uni));
								break;
							case 2:
								mTvPayMethod.setText(getString(R.string.pay_method_tip)+getString(R.string.pay_method_weixin));
								break;
						}
					}
					mOrderDetailsAdapter.resetDatas(info.getOrderGoodsInfos());
					mTvPostPrice.setText(String.format(getString(R.string.commit_order_post_price_tip), info.getPostage()));
					mTvGoodsPrice.setText(String.format(getString(R.string.commit_order_goods_price_tip),
							info.getOrderTotal() - info.getPostage() - info.getPoundate()));
					mTvExtraPrice.setText(String.format(getString(R.string.commit_order_extra_price_tip), info.getPoundate()));
					if (mPullRefreshListView.isRefreshing())
					{
						mPullRefreshListView.stopPullRefresh();
					}
				}
				else
				{
					mProgressLayout.showErrorText();
				}
			}

			@Override
			public void onOrderDetailsFail(String errormessage)
			{
				ToastUtil.showAlertToast(OrderDetailsActivity.this, errormessage);
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
}
