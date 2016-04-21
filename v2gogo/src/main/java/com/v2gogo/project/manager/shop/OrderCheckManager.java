package com.v2gogo.project.manager.shop;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.R.integer;

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
 * 订单验证管理器
 * 
 * @author houjun
 */
public class OrderCheckManager
{
	/**
	 * 检查订单的状态
	 * 
	 * @param pinfo
	 * @param callback
	 */
	public static void orderCheckStatus(String pinfo, final IonOrderCheckManagerCallback callback)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("pInfo", pinfo);
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
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("orderCheckStatus", Request.Method.POST,
				ServerUrlConfig.SERVER_URL + "/orderinfo/checkUserOrder", params, new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int code, String message, JSONObject response)
					{
						if (StatusCode.SUCCESS == code)
						{
							if (null != callback)
							{
								callback.onOrderCheckManagerSuccess();
							}
						}
						else
						{
							if (null != callback)
							{
								callback.onOrderCheckManagerFail(message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (null != callback)
						{
							callback.onOrderCheckManagerFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 检查订单数据回调
	 * 
	 * @author houjun
	 */
	public interface IonOrderCheckManagerCallback
	{
		public void onOrderCheckManagerSuccess();

		public void onOrderCheckManagerFail(String errorMessage);
	}
}
