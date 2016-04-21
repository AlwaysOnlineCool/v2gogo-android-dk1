package com.v2gogo.project.manager.shop;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.reflect.TypeToken;
import com.v2gogo.project.domain.shop.OrderInfo;
import com.v2gogo.project.domain.shop.ReceiverInfos;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.manager.HttpJsonObjectRequest;
import com.v2gogo.project.manager.HttpJsonObjectRequest.IOnDataReceiveMessageCallback;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.utils.StatusCode;
import com.v2gogo.project.utils.common.MD5Util;
import com.v2gogo.project.utils.http.HttpProxy;
import com.v2gogo.project.utils.parse.JsonParser;

/**
 * 订单管理器
 * 
 * @author houjun
 */
public class OrderManager
{

	/**
	 * 创建订单
	 */
	public static void buildOrder(String consignee, String pinfo, String phone, int payMethod, String address, float orderTotal, String did,
			final IonBuilderOrderCallback callback)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("phone", phone);
		params.put("consignee", consignee);
		params.put("payMethod", payMethod + "");
		params.put("address", address);
		params.put("orderTotal", orderTotal + "");
		params.put("did", did);
		params.put("pinfo", pinfo);
		if (V2GogoApplication.getMasterLoginState())
		{
			params.put("username", V2GogoApplication.getCurrentMatser().getUsername());
			params.put("token", V2GogoApplication.getCurrentMatser().getToken());
			String signature = MD5Util.getMd5Token(params);
			params.put("signature", signature);
		}
		if (params.containsKey("token"))
		{
			params.remove("token");
		}
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("buildOrder", Request.Method.PUT, ServerUrlConfig.SERVER_URL
				+ "/orderinfo/saveuserorder", params, new IOnDataReceiveMessageCallback()
		{
			@Override
			public void onSuccess(int code, String message, JSONObject response)
			{
				if (StatusCode.SUCCESS == code)
				{
					JSONObject jsonObject = response.optJSONObject("order");
					if (jsonObject != null)
					{
						OrderInfo orderInfo = (OrderInfo) JsonParser.parseObject(jsonObject.toString(), OrderInfo.class);
						if (null != callback)
						{
							callback.onBuilderOrderSuccess(orderInfo);
						}
					}
					else
					{
						if (null != callback)
						{
							callback.onBuilderOrderFail(null);
						}
					}
				}
				else
				{
					if (null != callback)
					{
						callback.onBuilderOrderFail(message);
					}
				}
			}

			@Override
			public void onError(String errorMessage)
			{
				if (null != callback)
				{
					callback.onBuilderOrderFail(errorMessage);
				}
			}
		});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 得到用户最新的地址信息
	 * 
	 * @param context
	 */
	public static void getUserNewestAddress(final IonUserNewestAddressCallback callback)
	{
		Map<String, String> params = new HashMap<String, String>();
		if (V2GogoApplication.getMasterLoginState())
		{
			params.put("username", V2GogoApplication.getCurrentMatser().getUsername());
			params.put("token", V2GogoApplication.getCurrentMatser().getToken());
			String signature = MD5Util.getMd5Token(params);
			params.put("signature", signature);
		}
		if (params.containsKey("token"))
		{
			params.remove("token");
		}
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("getUserNewestAddress", Request.Method.POST,
				ServerUrlConfig.SERVER_URL + "/orderinfo/getuseraddress", params, new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int code, String message, JSONObject response)
					{
						if (StatusCode.SUCCESS == code)
						{
							JSONObject jsonObject = response.optJSONObject("delivery");
							if (jsonObject != null)
							{
								ReceiverInfos receiverInfos = (ReceiverInfos) JsonParser.parseObject(jsonObject.toString(), ReceiverInfos.class);
								if (null != callback)
								{
									callback.onUserNewestAddressSuccess(receiverInfos);
								}
							}
							else
							{
								if (callback != null)
								{
									callback.onUserNewestAddressFail(null);
								}
							}
						}
						else
						{
							if (callback != null)
							{
								callback.onUserNewestAddressFail(message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (null != callback)
						{
							callback.onUserNewestAddressFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 拉取订单详情
	 */
	public static void getOrderDetails(String orderId, final IonOrderDetailsCallback callback)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("oid", orderId);
		if (V2GogoApplication.getMasterLoginState())
		{
			params.put("username", V2GogoApplication.getCurrentMatser().getUsername());
			params.put("token", V2GogoApplication.getCurrentMatser().getToken());
			String signature = MD5Util.getMd5Token(params);
			params.put("signature", signature);
		}
		if (params.containsKey("token"))
		{
			params.remove("token");
		}
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("getOrderDetails", Request.Method.POST,
				ServerUrlConfig.SERVER_URL + "/orderinfo/getorderinfo", params, new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int code, String message, JSONObject response)
					{
						if (StatusCode.SUCCESS == code)
						{
							JSONObject jsonObject = response.optJSONObject("order");
							if (null != jsonObject)
							{
								OrderInfo orderInfo = (OrderInfo) JsonParser.parseObject(jsonObject.toString(), OrderInfo.class);
								if (callback != null)
								{
									callback.onOrderDetailsSuccess(orderInfo);
								}
							}
						}
						else
						{
							if (callback != null)
							{
								callback.onOrderDetailsFail(message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (callback != null)
						{
							callback.onOrderDetailsFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	public static void getPayMethodData(final IonGetPayMethodCallback callback)
	{
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("getPayMethodData", Request.Method.POST,
				ServerUrlConfig.SERVER_URL + "/payType/list", null, new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int code, String message, JSONObject response)
					{
						if (StatusCode.SUCCESS == code)
						{
							JSONObject jsonObject = response.optJSONObject("result");
							if (jsonObject != null)
							{
								String PayMethods = jsonObject.optString("list", "");
								if (!TextUtils.isEmpty(PayMethods))
								{
									Type type = new TypeToken<ArrayList<PayMethod>>()
									{
									}.getType();
									@SuppressWarnings("unchecked")
									ArrayList<PayMethod> datas = (ArrayList<PayMethod>) JsonParser.parseObjectList(PayMethods, type);
									if (null != callback)
									{
										callback.onGetPayMethodSuccess(datas);
									}
								}
							}
							else
							{
								if (callback != null)
								{
									callback.onGetPayMethodFail(message);
								}
							}
						}
						else
						{
							if (callback != null)
							{
								callback.onGetPayMethodFail(message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (null != callback)
						{
							callback.onGetPayMethodFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 清除订单详情的请求任务
	 */
	public static void clearOrderDetailsTask()
	{
		HttpProxy.removeRequestTask("getOrderDetails");
	}

	/**
	 * 清除用户最新地址的请求任务
	 */
	public static void clearUserNewestAddressTask()
	{
		HttpProxy.removeRequestTask("getUserNewestAddress");
	}

	/**
	 * 清除生成订单的请求任务
	 */
	public static void clearBuildOrderTask()
	{
		HttpProxy.removeRequestTask("buildOrder");
	}

	/**
	 * 用户最新信息的数据回调
	 * 
	 * @author houjun
	 */
	public interface IonUserNewestAddressCallback
	{
		public void onUserNewestAddressSuccess(ReceiverInfos receiverInfos);

		public void onUserNewestAddressFail(String errormessage);
	}

	/**
	 * 创建订单数据回调
	 * 
	 * @author houjun
	 */
	public interface IonBuilderOrderCallback
	{
		public void onBuilderOrderSuccess(OrderInfo info);

		public void onBuilderOrderFail(String errormessage);
	}

	/**
	 * 订单详情数据回调
	 * 
	 * @author houjun
	 */
	public interface IonOrderDetailsCallback
	{
		public void onOrderDetailsSuccess(OrderInfo info);

		public void onOrderDetailsFail(String errormessage);
	}

	/**
	 * 获取支付方式回调
	 * 
	 * @author houjun
	 */
	public interface IonGetPayMethodCallback
	{
		public void onGetPayMethodSuccess(ArrayList<PayMethod> datas);

		public void onGetPayMethodFail(String errormessage);
	}
}
