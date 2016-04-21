/**    
 * @{#} WelfareActivity.java Create on 2016-3-10 下午8:12:00    
 *    
 * Copyright (c) 2013 by BlueSky.    
 *
 *    
 * @author <a href="1084986314@qq.com">BlueSky</a>   
 * @version 1.0    
 */
package com.v2gogo.project.activity.home.welfare;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.json.JSONObject;

import android.annotation.TargetApi;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.v2gogo.project.R;
import com.v2gogo.project.activity.BaseActivity;
import com.v2gogo.project.activity.shop.OrderSettlementActivity;
import com.v2gogo.project.manager.HttpJsonObjectRequest.IOnDataReceiveMessageCallback;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.manager.shop.OrderManager;
import com.v2gogo.project.manager.shop.OrderManager.IonGetPayMethodCallback;
import com.v2gogo.project.manager.shop.PayMethod;
import com.v2gogo.project.manager.union.UnionPayManager;
import com.v2gogo.project.utils.StatusCode;
import com.v2gogo.project.utils.common.MD5Util;
import com.v2gogo.project.utils.common.ToastUtil;

/**
 * 功能：公益支付
 * 
 * @ahthor：黄荣星
 * @date:2016-3-10
 * @version::V1.0
 */
public class WelfareActivity extends BaseActivity implements OnClickListener
{

	private RadioGroup mRadioGroup;
	private int mCheckedId;
	private String projectId;
	private String amount;
	private int mPayType;
	private IWXAPI mApi;// 微信支付API
	private Button mBtnPay;

	@Override
	public void clearRequestTask()
	{

	}

	@Override
	public void onInitViews()
	{
		mApi = WXAPIFactory.createWXAPI(this, null);// 微信api对象
		mRadioGroup = (RadioGroup) findViewById(R.id.order_settlment_online_pay_radiogroup);
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId)
			{
				mCheckedId = checkedId;
			}
		});
		mBtnPay = (Button) findViewById(R.id.order_settlment_pay_order);
		mBtnPay.setOnClickListener(this);

	}

	@Override
	protected void onInitLoadDatas()
	{
		super.onInitLoadDatas();
		getPayMethodDatas();
	}

	@Override
	@TargetApi(11)
	protected void onInitIntentData(Intent intent)
	{
		super.onInitIntentData(intent);
		projectId = intent.getStringExtra("projectId");
		amount = intent.getStringExtra("amount");

	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.welfare_layout;
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
				ToastUtil.showAlertToast(WelfareActivity.this, errormessage);
			}
		});
	}

	@Override
	public void onClick(View view)
	{
		switch (mCheckedId)
		{
			case 0:// 支付宝
				mPayType = 0;
				pay(mPayType + "");
				break;
			case 1:// 银联
				mPayType = 1;
				pay(mPayType + "");
				break;
			case 2:// 微信
				mPayType = 2;
				pay(mPayType + "");
				break;
			default:
				break;
		}
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
	 * 支付
	 * 
	 * @param payType
	 */
	private void pay(String payType)
	{
		UnionPayManager.getLovePayTransactionNo(projectId, payType, amount, new IOnDataReceiveMessageCallback()
		{

			@Override
			public void onSuccess(int code, String message, JSONObject response)
			{
				if (StatusCode.SUCCESS == code)// 成功
				{
					if (mPayType == 1)// 银联支付
					{
						// 流水交易号
						// JSONObject jsonObject = response.optJSONObject("result");
						// String tn = jsonObject.optString("tn", "");
						// startUnionPay(tn);
					}
					else if (mPayType == 2)// 微信支付
					{
						// 流水交易号
						JSONObject jsonObject = response.optJSONObject("result");
						String prepayId = jsonObject.optString("prepayid", "");
						startWeixinPay(prepayId);
					}
				}
				else
				{// 失败
					ToastUtil.showAlertToast(WelfareActivity.this, message);
				}
			}

			@Override
			public void onError(String errorMessage)
			{
				ToastUtil.showAlertToast(WelfareActivity.this, errorMessage);
			}
		});
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

}
