package com.v2gogo.project.manager.home;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.v2gogo.project.domain.home.ShakeResultInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.manager.HttpJsonObjectRequest;
import com.v2gogo.project.manager.HttpJsonObjectRequest.IOnDataReceiveMessageCallback;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.utils.common.MD5Util;
import com.v2gogo.project.utils.http.HttpProxy;
import com.v2gogo.project.utils.parse.JsonParser;

/**
 * 摇一摇管理器
 * 
 * @author houjun
 */
public class ShakeManager
{

	/**
	 * 发起摇一摇请求
	 * 
	 * @param context
	 * @param userId
	 */
	public static void luanchShakeRequest(final IonShakeCallback callback)
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
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("luanchShakeRequest", Request.Method.POST,
				ServerUrlConfig.SERVER_URL + "/shake", params, new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int result, String message, JSONObject jsonObject)
					{
						if (jsonObject != null)
						{
							ShakeResultInfo shakeResultInfo = (ShakeResultInfo) JsonParser.parseObject(jsonObject.toString(), ShakeResultInfo.class);
							if (null != callback)
							{
								callback.onShakeSuccess(shakeResultInfo);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (null != callback)
						{
							callback.onShakeFail(errorMessage);
						}
					}
				});
		jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(3500, 0, 1f));
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 发起摇一摇请求
	 * 
	 * @param context
	 * @param userId
	 */
	public static void getShakeAds(final IOnDataReceiveMessageCallback callback)
	{
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("getShakeAds", Request.Method.POST, ServerUrlConfig.SERVER_URL
				+ "/yaopriseapp/shakeAds", null, callback);
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 清除摇一摇请求任务
	 */
	public static void clearLuanchShakeRequestTask()
	{
		HttpProxy.removeRequestTask("luanchShakeRequest");
	}

	/**
	 * 摇一摇数据回调
	 * 
	 * @author houjun
	 */
	public interface IonShakeCallback
	{
		public void onShakeSuccess(ShakeResultInfo shakeResultInfo);

		public void onShakeFail(String errorMessage);
	}

}
