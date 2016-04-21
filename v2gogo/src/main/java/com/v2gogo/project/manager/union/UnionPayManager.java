package com.v2gogo.project.manager.union;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.manager.HttpJsonObjectRequest;
import com.v2gogo.project.manager.HttpJsonObjectRequest.IOnDataReceiveMessageCallback;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.utils.StatusCode;
import com.v2gogo.project.utils.common.MD5Util;
import com.v2gogo.project.utils.http.HttpProxy;

/**
 * 银联支付管理器
 * 
 * @author bluesky
 */
public class UnionPayManager
{
	/**
	 * method desc：
	 * 
	 * @param pinfo
	 *            必须 JSON 商品集合
	 * @param orderId
	 *            订单号
	 * @param payType
	 *            0：支付宝[默认]，1：银联
	 * @param callback
	 *            回调接口
	 */
	public static void getTransactionNo(String pinfo, String orderId, String payType, IOnDataReceiveMessageCallback callback)
	{ // 获取基础性的参数
		// Map<String, String> params = getBaseParamsMap();
		Map<String, String> params = new HashMap<String, String>();
		// 构造参数
		params.put("pInfo", pinfo);
		params.put("orderId", orderId);
		params.put("payType", payType);

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

		// 地址
		String url = ServerUrlConfig.SERVER_URL + "/unionpay/getTransactionNo";
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("getTransactionNo", Request.Method.POST, url, params, callback);
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * method desc：
	 * 
	 * @param pinfo
	 * @param orderId
	 * @param payType
	 * @param callback
	 */
	public static void getLovePayTransactionNo(String projectId, String payType, String amount, IOnDataReceiveMessageCallback callback)
	{ // 获取基础性的参数
		// Map<String, String> params = getBaseParamsMap();
		Map<String, String> params = new HashMap<String, String>();
		// 构造参数
		params.put("projectId", projectId);
		params.put("payType", payType);
		params.put("appIp", "");
		params.put("isAPP", "true");
		params.put("amount", amount);

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

		// 地址
		String url = ServerUrlConfig.SERVER_URL + "/pay/lovePay";
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("getLovePayTransactionNo", Request.Method.POST, url, params,
				callback);
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	private static Map<String, String> getBaseParamsMap()
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
		return params;
	}

}
