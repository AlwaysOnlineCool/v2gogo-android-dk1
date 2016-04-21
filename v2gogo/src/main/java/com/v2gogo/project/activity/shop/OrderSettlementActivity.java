package com.v2gogo.project.activity.shop;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;
import com.v2gogo.project.R;
import com.v2gogo.project.activity.BaseActivity;
import com.v2gogo.project.domain.shop.OrderGoodsInfo;
import com.v2gogo.project.domain.shop.OrderInfo;
import com.v2gogo.project.manager.HttpJsonObjectRequest.IOnDataReceiveMessageCallback;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.manager.shop.OrderCheckManager;
import com.v2gogo.project.manager.shop.OrderCheckManager.IonOrderCheckManagerCallback;
import com.v2gogo.project.manager.shop.OrderManager;
import com.v2gogo.project.manager.shop.OrderManager.IonGetPayMethodCallback;
import com.v2gogo.project.manager.shop.PayMethod;
import com.v2gogo.project.manager.union.UnionConfig;
import com.v2gogo.project.manager.union.UnionPayManager;
import com.v2gogo.project.utils.StatusCode;
import com.v2gogo.project.utils.common.DateUtil;
import com.v2gogo.project.utils.common.MD5Util;
import com.v2gogo.project.utils.common.ToastUtil;
import com.v2gogo.project.utils.pay.OrderBuildUtil;
import com.v2gogo.project.utils.pay.PayResult;
import com.v2gogo.project.views.dialog.PayResultFailDialog;
import com.v2gogo.project.views.dialog.PayResultFailDialog.IonResetPayCallback;
import com.v2gogo.project.views.dialog.PayResultSuccessDialog;
import com.ypy.eventbus.EventBus;

/**
 * 订单结算界面
 * 
 * @author houjun
 */
public class OrderSettlementActivity extends BaseActivity implements OnClickListener, IonResetPayCallback
{
	private static final int SDK_PAY_FLAG = 1;

	public static final String ORDER = "order";

	private Button mBtnPay;

	private TextView mTvOrderNum;
	private TextView mTvOrderState;
	private TextView mTvOrderDate;
	private TextView mTvOrderPrice;

	private RadioGroup mRadioGroup;

	private PayResultFailDialog mPayResultFailDialog;
	private PayResultSuccessDialog mPayResultSuccessDialog;

	private OrderInfo mOrderInfo;
	private int mCheckedId = 0;
	private int mPayType = 0;// 0：支付宝[默认]，1：银联2:微信支付

	private IWXAPI mApi;// 微信支付API

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case SDK_PAY_FLAG:
					PayResult payResult = new PayResult((String) msg.obj);
					// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
					String resultStatus = payResult.getResultStatus();
					if (TextUtils.equals(resultStatus, "9000"))
					{
						handlerPaySuccess();
					}
					else
					{
						// 判断resultStatus 为非“9000”则代表可能支付失败
						// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
						if (TextUtils.equals(resultStatus, "8000"))
						{
							Toast.makeText(OrderSettlementActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
						}
						else
						{
							handlerPayFailed();
						}
					}
					break;

				default:
					break;
			}
		};
	};

	@Override
	public void clearRequestTask()
	{

	}

	@Override
	public void onInitViews()
	{
		EventBus.getDefault().register(this);

		mApi = WXAPIFactory.createWXAPI(this, null);// 微信api对象
		mBtnPay = (Button) findViewById(R.id.order_settlment_pay_order);
		mTvOrderState = (TextView) findViewById(R.id.order_details_activity_header_order_status);
		mTvOrderNum = (TextView) findViewById(R.id.order_details_activity_header_order_no);
		mTvOrderPrice = (TextView) findViewById(R.id.order_details_activity_header_order_price);
		mTvOrderDate = (TextView) findViewById(R.id.order_details_activity_header_order_create_time);

		mRadioGroup = (RadioGroup) findViewById(R.id.order_settlment_online_pay_radiogroup);
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId)
			{
				mCheckedId = checkedId;
			}
		});
	}

	@Override
	protected void onInitLoadDatas()
	{
		super.onInitLoadDatas();
		if (mOrderInfo != null)
		{
			mTvOrderDate.setText(DateUtil.convertStringWithTimeStampWithoutHour(mOrderInfo.getCreateTime()));
			mTvOrderNum.setText(mOrderInfo.getOrderNo());
			mTvOrderPrice.setText(mOrderInfo.getOrderTotal() + "");
			mTvOrderState.setText(mOrderInfo.getOrderStatusString(this));

			getPayMethodDatas();
		}
	}

	@Override
	protected void onInitIntentData(Intent intent)
	{
		super.onInitIntentData(intent);
		if (null != intent)
		{
			mOrderInfo = (OrderInfo) intent.getSerializableExtra(ORDER);
		}
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.order_settlement_activity_layout;
	}

	@Override
	protected void registerListener()
	{
		super.registerListener();
		mBtnPay.setOnClickListener(this);
	}

	@Override
	public void onResetPay()
	{
		payUnion(mPayType + "");
	}

	@Override
	public void onClick(View view)
	{
		switch (mCheckedId)
		{
			case 0:// 支付宝
				mPayType = 0;
				payUnion(mPayType + "");
				break;
			case 1:// 银联
				mPayType = 1;
				payUnion(mPayType + "");
				break;
			case 2:// 微信
				mPayType = 2;
				payUnion(mPayType + "");
				break;
			default:
				break;
		}
	}

	/**
	 * method desc：获取支付方式
	 */
	private void getPayMethodDatas()
	{
		showLoadingDialog("加载中...");
		OrderManager.getPayMethodData(new IonGetPayMethodCallback()
		{
			@Override
			public void onGetPayMethodSuccess(ArrayList<PayMethod> datas)
			{
				dismissLoadingDialog();
				displayPayMethodView(datas);
			}

			@Override
			public void onGetPayMethodFail(String errormessage)
			{
				dismissLoadingDialog();
				ToastUtil.showAlertToast(OrderSettlementActivity.this, errormessage);
			}
		});
	}

	/**
	 * method desc：展示支付方式View
	 * 
	 * @param datas
	 */
	protected void displayPayMethodView(ArrayList<PayMethod> datas)
	{
		try
		{

			if (mRadioGroup != null && mRadioGroup.getChildCount() > 0)
			{
				mRadioGroup.removeAllViews();
			}
			for (int i = 0, len = datas.size(); i < len; i++)
			{
				PayMethod payMethod = datas.get(i);
				RadioButton mRadioButton = (RadioButton) LayoutInflater.from(this).inflate(R.layout.pay_method_layout, null);
				mRadioButton.setId(Integer.parseInt(payMethod.getId()));
				mRadioButton.setText(payMethod.getPayName());
				if (i == 0)// 默认设置
				{
					mRadioButton.setChecked(true);
					mCheckedId = Integer.parseInt(payMethod.getId());
				}

				mRadioGroup.addView(mRadioButton);
			}
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
	}

	/**
	 * method desc：构造产品信息
	 * 
	 * @return 返回json格式字符串
	 */
	private String buildPinfo()
	{
		String pInfo = "";
		try
		{
			JSONArray jsonArray = new JSONArray();
			for (OrderGoodsInfo orderGoodsInfo : mOrderInfo.getOrderGoodsInfos())
			{
				if (null != orderGoodsInfo)
				{
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("productId", orderGoodsInfo.getProductId());
					jsonObject.put("buyNum", orderGoodsInfo.getBuyNum());
					jsonArray.put(jsonObject);
				}
			}
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("pInfo", jsonArray);

			pInfo = jsonObj.toString();
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		return pInfo;
	}

	/**
	 * method desc：开始银联支付
	 */
	private void startUnionPay(String tn)
	{
		String spId = null;
		String sysProvider = null;
		UPPayAssistEx.startPayByJAR(this, PayActivity.class, spId, sysProvider, tn, UnionConfig.USE_TESTT_MODE);
	}

	/**
	 * method desc：随机字符串
	 * 
	 * @param length
	 * @return
	 */
	public static String getRandomString(int length)
	{ // length表示生成字符串的长度
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++)
		{
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	/**
	 * 微信支付签名算法sign
	 * 
	 * @param characterEncoding
	 * @param parameters
	 * @return
	 */
	public String createSign(SortedMap<Object, Object> parameters)
	{
		StringBuffer sb = new StringBuffer();
		Set es = parameters.entrySet();// 所有参与传参的参数按照accsii排序（升序）
		Iterator it = es.iterator();
		while (it.hasNext())
		{
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			Object v = entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k))
			{
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + ServerUrlConfig.PAY_WEIXIN_MIYAO);
		// sb.append("key=wjzpgz2015D52DFdsf256546DSF23564");
		String sign = MD5Util.getMD5String(sb.toString()).toUpperCase();
		return sign;
	}

	/**
	 * method desc：开始银联支付
	 */
	private void startWeixinPay(String prepayId)
	{
		mApi.registerApp(ServerUrlConfig.PAY_WEIXIN_APPID);

		String nonceStr = getRandomString(32);
		String timeStamp = (int) (System.currentTimeMillis() / 1000) + "";

		SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
		parameters.put("appid", ServerUrlConfig.PAY_WEIXIN_APPID);
		parameters.put("partnerid", ServerUrlConfig.PAY_WEIXIN_PARTERID);
		parameters.put("prepayid", prepayId);
		parameters.put("package", "Sign=WXPay");
		parameters.put("noncestr", nonceStr);
		parameters.put("timestamp", timeStamp);

		String sign = createSign(parameters);

		PayReq request = new PayReq();
		request.appId = ServerUrlConfig.PAY_WEIXIN_APPID;
		request.partnerId = ServerUrlConfig.PAY_WEIXIN_PARTERID;
		request.prepayId = prepayId;
		request.packageValue = "Sign=WXPay";
		request.nonceStr = nonceStr;
		request.timeStamp = timeStamp;
		request.sign = sign;

		mApi.sendReq(request);
	}

	/**
	 * method desc：银联支付
	 */
	private void payUnion(String payType)
	{
		String orderId = "";
		String pinfo = "";
		if (mOrderInfo != null)
		{
			orderId = mOrderInfo.getOrderNo();
			pinfo = buildPinfo();
		}

		UnionPayManager.getTransactionNo(pinfo, orderId, payType, new IOnDataReceiveMessageCallback()
		{

			@Override
			public void onSuccess(int code, String message, JSONObject response)
			{
				if (StatusCode.SUCCESS == code)// 成功
				{
					if (mPayType == 1)// 银联支付
					{
						// 流水交易号
						JSONObject jsonObject = response.optJSONObject("result");
						String tn = jsonObject.optString("tn", "");
						startUnionPay(tn);
					}
					else if (mPayType == 2)// 微信支付
					{
						// 流水交易号
						JSONObject jsonObject = response.optJSONObject("result");
						String prepayId = jsonObject.optString("prepayid", "");
						startWeixinPay(prepayId);
					}
					else
					{
						startPay();// 支付宝
					}
				}
				else
				{// 失败
					ToastUtil.showAlertToast(OrderSettlementActivity.this, message);
				}
			}

			@Override
			public void onError(String errorMessage)
			{
				ToastUtil.showAlertToast(OrderSettlementActivity.this, errorMessage);
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null)
		{
			return;
		}
		String str = data.getExtras().getString("pay_result");
		if (str.equalsIgnoreCase("R_SUCCESS") || str.equalsIgnoreCase("SUCCESS"))
		{
			handlerPaySuccess();// 支付成功处理
		}
		else if (str.equalsIgnoreCase("R_FAIL") || str.equalsIgnoreCase("FAIL"))
		{
			handlerPayFailed();
		}
		else if (str.equalsIgnoreCase("R_CANCEL") || str.equalsIgnoreCase("CANCEL"))
		{
			handlerPayFailed();
		}
	}

	/**
	 * method desc：
	 */
	private void handlerCancled()
	{
		ToastUtil.showAlertToast(this, "取消支付");
	}

	/**
	 * 支付失败处理
	 * method desc：
	 */
	private void handlerPayFailed()
	{
		if (null == mPayResultFailDialog)
		{
			mPayResultFailDialog = new PayResultFailDialog(OrderSettlementActivity.this, R.style.style_action_sheet_dialog);
			mPayResultFailDialog.setOnPayCallback(OrderSettlementActivity.this);
			mPayResultFailDialog.setCanceledOnTouchOutside(false);
		}
		if (!mPayResultFailDialog.isShowing())
		{
			mPayResultFailDialog.show();
		}
	}

	private void handlerPaySuccess()
	{
		// 支付成功
		if (null == mPayResultSuccessDialog)
		{
			mPayResultSuccessDialog = new PayResultSuccessDialog(OrderSettlementActivity.this, R.style.style_action_sheet_dialog);
			mPayResultSuccessDialog.setCanceledOnTouchOutside(false);
		}
		if (!mPayResultSuccessDialog.isShowing())
		{
			mPayResultSuccessDialog.show();
		}
	}

	/**
	 * 支付订单
	 */
	private void payOrder()
	{
		if (mOrderInfo != null)
		{
			if (null != mOrderInfo.getOrderGoodsInfos() && mOrderInfo.getOrderGoodsInfos().size() > 0)
			{
				try
				{
					String pInfo = null;
					JSONArray jsonArray = new JSONArray();
					for (OrderGoodsInfo orderGoodsInfo : mOrderInfo.getOrderGoodsInfos())
					{
						if (null != orderGoodsInfo)
						{
							JSONObject jsonObject = new JSONObject();
							jsonObject.put("productId", orderGoodsInfo.getId());
							jsonObject.put("buyNum", orderGoodsInfo.getBuyNum());
							jsonArray.put(jsonObject);
						}
					}
					pInfo = jsonArray.toString();
					OrderCheckManager.orderCheckStatus(pInfo, new IonOrderCheckManagerCallback()
					{
						@Override
						public void onOrderCheckManagerSuccess()
						{
							startPay();
						}

						@Override
						public void onOrderCheckManagerFail(String errorMessage)
						{
							ToastUtil.showAlertToast(OrderSettlementActivity.this, errorMessage);
						}
					});
				}
				catch (Exception exception)
				{
				}
			}
		}
	}

	/**
	 * 开始支付
	 */
	private void startPay()
	{
		if (mOrderInfo != null)
		{
			String goodsName = "goodsname";
			String goodsIntro = "goodsdescription";
			if (null != mOrderInfo.getOrderGoodsInfos() && mOrderInfo.getOrderGoodsInfos().size() > 0)
			{
				OrderGoodsInfo orderGoodsInfo = mOrderInfo.getOrderGoodsInfos().get(0);
				goodsName = orderGoodsInfo.getProductName();
				goodsIntro = orderGoodsInfo.getDescriptions();
			}
			PayRunnable payRunnable = new PayRunnable(goodsName, mOrderInfo.getOrderNo(), goodsIntro, mOrderInfo.getOrderTotal() + "");
			new Thread(payRunnable).start();
		}
	}

	/**
	 * 支付任务
	 * 
	 * @author houjun
	 */
	private class PayRunnable implements Runnable
	{
		private String mGoodsName;
		private String mOrderNo;
		private String mGoodsDescription;
		private String mPrice;

		public PayRunnable(String mGoodsName, String mOrderNo, String mGoodsDescription, String mPrice)
		{
			super();
			this.mGoodsName = mGoodsName;
			this.mOrderNo = mOrderNo;
			this.mGoodsDescription = mGoodsDescription;
			this.mPrice = mPrice;
		}

		@Override
		public void run()
		{
			PayTask alipay = new PayTask(OrderSettlementActivity.this);
			String payInfo = OrderBuildUtil.getSignOrderInfo(mGoodsName, mGoodsDescription, mPrice, mOrderNo);
			String result = alipay.pay(payInfo);
			Message msg = new Message();
			msg.what = SDK_PAY_FLAG;
			msg.obj = result;
			mHandler.sendMessage(msg);
		}
	}

	public void onEventMainThread(BaseResp resp)
	{
		if (resp != null)
		{
			switch (resp.errCode)
			{
				case 0:// 成功
					handlerPaySuccess();
					break;
				case -1:// 错误
					handlerPayFailed();
					break;
				case -2:// 用户取消
					handlerPayFailed();
					break;
			}
		}
	}
}
