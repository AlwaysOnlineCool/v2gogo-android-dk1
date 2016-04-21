package com.v2gogo.project.activity.shop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.BaseActivity;
import com.v2gogo.project.adapter.shop.OrderCommitAdaptar;
import com.v2gogo.project.domain.shop.CartItemInfo;
import com.v2gogo.project.domain.shop.GoodsDetailsInfo;
import com.v2gogo.project.domain.shop.GoodsInfo;
import com.v2gogo.project.domain.shop.OrderInfo;
import com.v2gogo.project.domain.shop.ReceiverInfos;
import com.v2gogo.project.manager.shop.GoodsDetailsManager;
import com.v2gogo.project.manager.shop.GoodsDetailsManager.IonGoodsDetailsInfosCallback;
import com.v2gogo.project.manager.shop.OrderManager;
import com.v2gogo.project.manager.shop.OrderManager.IonBuilderOrderCallback;
import com.v2gogo.project.manager.shop.OrderManager.IonUserNewestAddressCallback;
import com.v2gogo.project.utils.common.ToastUtil;
import com.v2gogo.project.views.dialog.CommitOrderAddressEditDialog;
import com.v2gogo.project.views.dialog.CommitOrderAddressEditDialog.ACTION;
import com.v2gogo.project.views.dialog.CommitOrderAddressEditDialog.IonCommitOrderAddressEditCallback;
import com.v2gogo.project.views.dialog.PayResultSuccessDialog;
import com.v2gogo.project.views.listview.refreshview.PullRefreshListView;
import com.v2gogo.project.views.logic.CartSteper;
import com.v2gogo.project.views.logic.CartSteper.Action;
import com.v2gogo.project.views.logic.CartSteper.IonChangedClickListener;
import com.v2gogo.project.views.logic.OrderCommitAddressEditLayout;
import com.v2gogo.project.views.logic.OrderCommitAddressEditLayout.IonEditAddressCallback;

/**
 * 提交订单界面
 * 
 * @author houjun
 */
public class CommitOrderActivity extends BaseActivity implements OnClickListener, OnCheckedChangeListener, IonCommitOrderAddressEditCallback,
		IonEditAddressCallback
{

	public static final String ORDER_ITEM_INFO = "order_item_info";
	public static final String GOODS_ID = "goods_id";// 商品ID

	private PullRefreshListView mPullRefreshListView;
	private OrderCommitAddressEditLayout mOrderCommitAddressEditLayout;

	private TextView mPostPrice;
	private TextView mGoodsPrice;

	private TextView mExtraPriceTip;
	private TextView mOnlinePriceTip;

	private TextView mTvTotalPrice;

	private Button mBtnPay;

	private RadioGroup mRadioGroup;

	private RadioButton mOnlineBtn;
	private RadioButton mArriveBtn;

	// private CartItemInfo mCartItemInfo;
	private ReceiverInfos mReceiverInfos;

	private PayResultSuccessDialog mPayResultSuccessDialog;
	private CommitOrderAddressEditDialog mCommitOrderAddressEditDialog;

	private String mGoodsId;

	private int mBuyNum = 1;// 购买数量，默认1
	protected GoodsInfo mGoodsInfo;
	private CartSteper mCartSteper;
	private OrderCommitAdaptar mAdaptar;
	private float mAllProductPrice;// 商品总价

	@Override
	public void clearRequestTask()
	{
		OrderManager.clearBuildOrderTask();
		OrderManager.clearUserNewestAddressTask();
	}

	@Override
	public void onInitViews()
	{
		mBtnPay = (Button) findViewById(R.id.order_confirm_commit_order_btn);
		mTvTotalPrice = (TextView) findViewById(R.id.order_confirm_commit_total_price);
		mPullRefreshListView = (PullRefreshListView) findViewById(R.id.order_confirm_pull_to_refresh_listview);
		mPullRefreshListView.addHeaderView(getHeaderView());
		mPullRefreshListView.addFooterView(getFooterView());
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.order_confirm_activity_layout;
	}

	@Override
	protected void onInitLoadDatas()
	{
		super.onInitLoadDatas();
		getGoodsDetails();// 获取商品详情

	}

	@Override
	protected void onInitIntentData(Intent intent)
	{
		super.onInitIntentData(intent);
		if (null != intent)
		{
			// mCartItemInfo = (CartItemInfo) intent.getSerializableExtra(ORDER_ITEM_INFO);
			mGoodsId = intent.getStringExtra(GOODS_ID);
		}
	}

	@Override
	protected void registerListener()
	{
		super.registerListener();
		mBtnPay.setOnClickListener(this);
		mPullRefreshListView.setLoadMoreEnable(false);
		mOrderCommitAddressEditLayout.setOnAddressCallback(this);
	}

	@Override
	public void onClick(View view)
	{
		if (view == mBtnPay)
		{
			commitOrderClick();
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int checkId)
	{
		switch (checkId)
		{
			case R.id.order_confirm_activity_footer_rb_arrive:
				setOrderPrice(CartItemInfo.PAY_METHOD_ARRIVE);
				break;

			case R.id.order_confirm_activity_footer_rb_online:
				setOrderPrice(CartItemInfo.PAY_METHOD_ONLINE);
				break;

			default:
				break;
		}
	}

	/**
	 * 拉取商品详情
	 */
	private void getGoodsDetails()
	{
		GoodsDetailsManager.getGoodsDetailsInfosNew(mGoodsId, new IonGoodsDetailsInfosCallback()
		{
			@Override
			public void onGoodsDetailsInfosSuccess(GoodsDetailsInfo goodsDetailsInfo)
			{
				if (goodsDetailsInfo != null && goodsDetailsInfo.getGoodsInfo() != null)
				{
					mGoodsInfo = goodsDetailsInfo.getGoodsInfo();
					displayData(mGoodsInfo);
				}
			}

			@Override
			public void onGoodsDetailsInfosFail(String errorMessage)
			{
				ToastUtil.showAlertToast(CommitOrderActivity.this, errorMessage);
			}
		});
	}

	/**
	 * method desc：
	 * 
	 * @param goodsInfo
	 */
	protected void displayData(GoodsInfo goodsInfo)
	{
		if (goodsInfo != null)
		{
			setAdapter(goodsInfo);
			mAllProductPrice = mBuyNum * goodsInfo.getV2gogoPrice();// 商品总价
			setOrderPrice(CartItemInfo.PAY_METHOD_ONLINE);
			mPostPrice.setText(String.format(getString(R.string.commit_order_post_price_tip), goodsInfo.getPostage()));
			mGoodsPrice.setText(String.format(getString(R.string.commit_order_goods_price_tip), mAllProductPrice));
			if (goodsInfo.getPayMethodSupport() == GoodsInfo.SUPPORT_PAY_METHOD_ALL)
			{
				mExtraPriceTip.setVisibility(View.VISIBLE);
				mOnlinePriceTip.setVisibility(View.VISIBLE);
				mRadioGroup.setOnCheckedChangeListener(this);
				mOnlineBtn.setVisibility(View.VISIBLE);
				mArriveBtn.setVisibility(View.VISIBLE);
				mRadioGroup.check(R.id.order_confirm_activity_footer_rb_online);

			}
			else if (goodsInfo.getPayMethodSupport() == GoodsInfo.SUPPORT_PAY_METHOD_ARRIVE)
			{
				mExtraPriceTip.setVisibility(View.VISIBLE);
				mOnlinePriceTip.setVisibility(View.GONE);
				mOnlineBtn.setVisibility(View.GONE);
				mArriveBtn.setVisibility(View.VISIBLE);
				mRadioGroup.setOnCheckedChangeListener(null);
				mRadioGroup.check(R.id.order_confirm_activity_footer_rb_arrive);
			}
			else if (goodsInfo.getPayMethodSupport() == GoodsInfo.SUPPORT_PAY_METHOD_ONLINE)
			{
				mExtraPriceTip.setVisibility(View.GONE);
				mOnlinePriceTip.setVisibility(View.VISIBLE);
				mOnlineBtn.setVisibility(View.VISIBLE);
				mArriveBtn.setVisibility(View.GONE);
				mRadioGroup.setOnCheckedChangeListener(null);
				mRadioGroup.check(R.id.order_confirm_activity_footer_rb_online);
			}
			mExtraPriceTip.setText(String.format(getString(R.string.commit_order_extra_price_tip), goodsInfo.getPostage()));
			loadUserNewestAddress();
		}
	}

	/**
	 * 跳转到在线支付的界面
	 * 
	 * @param info
	 */
	private void forwardOnlinePay(OrderInfo info)
	{
		Intent intent = new Intent(this, OrderSettlementActivity.class);
		intent.putExtra(OrderSettlementActivity.ORDER, info);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	@Override
	public void onCommitOrderAddress(com.v2gogo.project.views.dialog.CommitOrderAddressEditDialog.ACTION action, String name, String phone, String address,
			CommitOrderAddressEditDialog orderAddressEditDialog)
	{
		if (action == ACTION.SURE)
		{
//			if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(address))
//			{
				mOrderCommitAddressEditLayout.setAddRessInfos(name, phone, address);
			//}
		}
	}

	@Override
	public void onEditAddress()
	{
		showReceiveInfoDialog();
	}

	/**
	 * 拉取最新的用户地址
	 */
	private void loadUserNewestAddress()
	{
		OrderManager.getUserNewestAddress(new IonUserNewestAddressCallback()
		{
			@Override
			public void onUserNewestAddressSuccess(ReceiverInfos receiverInfos)
			{
				if (receiverInfos != null)
				{
					mReceiverInfos = receiverInfos;
					mOrderCommitAddressEditLayout.setAddRessInfos(receiverInfos.getConsignee(), receiverInfos.getPhone(), receiverInfos.getAddress());
				}
				else
				{
					mOrderCommitAddressEditLayout.setAddRessInfos(null, null, null);
				}
			}

			@Override
			public void onUserNewestAddressFail(String errormessage)
			{
				ToastUtil.showAlertToast(CommitOrderActivity.this, errormessage);
				mOrderCommitAddressEditLayout.setAddRessInfos(null, null, null);
			}
		});
	}

	/**
	 * 设置适配器
	 */
	public void setAdapter(GoodsInfo goodsInfo)
	{
		mAdaptar = new OrderCommitAdaptar(this, mBuyNum, goodsInfo);
		mPullRefreshListView.setAdapter(mAdaptar);
	}

	/**
	 * 显示货到付款提交订单成功的对话框
	 */
	private void showArriveOrderDialog()
	{
		if (null == mPayResultSuccessDialog)
		{
			mPayResultSuccessDialog = new PayResultSuccessDialog(this, R.style.style_action_sheet_dialog);
		}
		if (!mPayResultSuccessDialog.isShowing())
		{
			mPayResultSuccessDialog.show();
			mPayResultSuccessDialog.setTitleMessage(getString(R.string.commit_order_commit_success_title_tip),
					getString(R.string.commit_order_commit_success_message_tip));
		}
	}

	/**
	 * 设置订单金额
	 */
	private void setOrderPrice(int payMethod)
	{
		if (mGoodsInfo != null)
		{
			mGoodsInfo.setPayMethodSupport(payMethod);
			mTvTotalPrice.setText(String.format(getString(R.string.commit_order_order_total_tip), getOrderPrice(payMethod)));
		}
	}

	/**
	 * 得到订单金额
	 * 
	 * @return
	 */
	public float getOrderPrice(int peyMethod)
	{
		float mOrderPrice = 0.0f;
		if (mGoodsInfo != null)
		{
			if (peyMethod == 0)
			{
				mOrderPrice = mAllProductPrice + mGoodsInfo.getPostage() + mGoodsInfo.getPoundage();
			}
			else
			{
				mOrderPrice = mAllProductPrice + mGoodsInfo.getPostage();
			}
		}
		return mOrderPrice;
	}

	/**
	 * 显示编辑收货人信息对话框
	 */
	private void showReceiveInfoDialog()
	{
		if (null == mCommitOrderAddressEditDialog)
		{
			mCommitOrderAddressEditDialog = new CommitOrderAddressEditDialog(CommitOrderActivity.this,this, R.style.style_action_sheet_dialog, mReceiverInfos);
			mCommitOrderAddressEditDialog.setOnOrderAddressEdit(this);
		}
		if (!mCommitOrderAddressEditDialog.isShowing())
		{
			mCommitOrderAddressEditDialog.show();
		}
	}

	/**
	 * 生成头部视图
	 * 
	 * @return
	 */
	private View getHeaderView()
	{
		View headView = LayoutInflater.from(this).inflate(R.layout.order_confirm_activity_header_layout, null);
		mOrderCommitAddressEditLayout = (OrderCommitAddressEditLayout) headView.findViewById(R.id.order_commit_activity_header_address_edit_layout);
		return headView;
	}

	/**
	 * 生成footer视图
	 * 
	 * @return
	 */
	private View getFooterView()
	{
		View footerView = LayoutInflater.from(this).inflate(R.layout.order_confirm_activity_footer_layout, null);
		mExtraPriceTip = (TextView) footerView.findViewById(R.id.order_confirm_activity_footer_arrive_price_tip);
		mOnlinePriceTip = (TextView) footerView.findViewById(R.id.order_confirm_activity_footer_online_price_tip);
		mRadioGroup = (RadioGroup) footerView.findViewById(R.id.order_confirm_activity_footer_radiogroup);
		mOnlineBtn = (RadioButton) footerView.findViewById(R.id.order_confirm_activity_footer_rb_online);
		mArriveBtn = (RadioButton) footerView.findViewById(R.id.order_confirm_activity_footer_rb_arrive);
		mGoodsPrice = (TextView) footerView.findViewById(R.id.order_commit_activity_goods_price);
		mPostPrice = (TextView) footerView.findViewById(R.id.order_commit_activity_goods_post_price);
		mCartSteper = (CartSteper) footerView.findViewById(R.id.buy_goods_sheet_edit_dialog_cartsteper);
		mBuyNum = mCartSteper.getStepperContent();
		mCartSteper.setChangedClickListener(new IonChangedClickListener()
		{
			@Override
			public void onChangedAction(Action action, int value)
			{
				setGoodsPrice();
			}
		});
		return footerView;
	}

	/**
	 * method desc：
	 */
	protected void setGoodsPrice()
	{
		mBuyNum = mCartSteper.getStepperContent();
		if (mAdaptar != null && mGoodsInfo != null)
		{
			// 商品合计
			mAllProductPrice = mBuyNum * mGoodsInfo.getV2gogoPrice();// 数量*单价
			mGoodsPrice.setText(String.format(getString(R.string.commit_order_goods_price_tip), mAllProductPrice));

			// 设置订单总价
			setOrderPrice(mGoodsInfo.getPayMethodSupport());

			mAdaptar.setmNumber(mBuyNum);
			mAdaptar.notifyDataSetChanged();
		}
	}

	/**
	 * 点击生成订单
	 */
	private void commitOrderClick()
	{
		//收件人姓名非空判断
		if (mOrderCommitAddressEditLayout.isEmptyName())
		{
			ToastUtil.showAlertToast(this, R.string.commit_order_commit_receiver_name_tip);
			return;
		}
		//收件人电话号码非空判断
		if (mOrderCommitAddressEditLayout.isEmptyPhone())
		{
			ToastUtil.showAlertToast(this, R.string.commit_order_commit_receiver_phone_tip);
			return;
		}
		//收件人电话号码合法性判断
		if (!mOrderCommitAddressEditLayout.isMobileNO())
		{
			ToastUtil.showAlertToast(this, R.string.commit_order_commit_receiver_phoneRight_tip);
			return;
		}
		//收件人地址非空判断
		if (mOrderCommitAddressEditLayout.isEmptyAddress())
		{
			ToastUtil.showAlertToast(this, R.string.commit_order_commit_receiver_address_tip);
			return;
		}
		commitOrder(mOrderCommitAddressEditLayout.getPhone(), mOrderCommitAddressEditLayout.getName(), mOrderCommitAddressEditLayout.getAddress());
	}

	/**
	 * 提交订单
	 * 
	 * @param phone
	 *            、
	 *            收货人的电话
	 * @param name
	 *            收货人的姓名
	 * @param address
	 *            收货人的地址
	 */
	private void commitOrder(String phone, String name, String address)
	{
		if (mGoodsInfo != null)
		{
			try
			{
				JSONArray jsonArray = new JSONArray();
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("pid", mGoodsInfo.getId());
				jsonObject.put("psup", mBuyNum);
				jsonArray.put(jsonObject);
				String did = "";
				if (mReceiverInfos != null)
				{
					did = mReceiverInfos.getId();
				}
				showLoadingDialog(getString(R.string.commit_order_commiting_order_tip));
				OrderManager.buildOrder(name, jsonArray.toString(), phone, mGoodsInfo.getPayMethodSupport(), address,
						getOrderPrice(mGoodsInfo.getPayMethodSupport()), did, new IonBuilderOrderCallback()
						{
							@Override
							public void onBuilderOrderSuccess(OrderInfo info)
							{
								dismissLoadingDialog();
								if (info != null)
								{
									if (info.getPayMethod() == CartItemInfo.PAY_METHOD_ARRIVE)
									{
										showArriveOrderDialog();
									}
									else if (info.getPayMethod() == CartItemInfo.PAY_METHOD_ONLINE)
									{
										forwardOnlinePay(info);
									}
								}
							}

							@Override
							public void onBuilderOrderFail(String errormessage)
							{
								dismissLoadingDialog();
								ToastUtil.showAlertToast(CommitOrderActivity.this, errormessage);
							}
						});
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}
		}
	}
}
