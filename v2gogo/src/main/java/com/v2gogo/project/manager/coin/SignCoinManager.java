package com.v2gogo.project.manager.coin;

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
 * 签到领金币管理器
 * 
 * @author houjun
 */
public class SignCoinManager
{
	/**
	 * 验证今天是否可以签到
	 * 
	 * @param context
	 */
	public static void luanchCheckTodaySign(final IonLuanchCheckTodaySignCallback callback)
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
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("luanchCheckTodaySign", Request.Method.POST,
				ServerUrlConfig.SERVER_URL + "/usersign/checkusersign", params, new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int result, String message, JSONObject jsonObject)
					{
						if (StatusCode.UNSIGN == result)
						{
							JSONObject sign = jsonObject.optJSONObject("vsign");
							int maxCoin = jsonObject.optInt("maxcoin");
							int coin = sign.optInt("coin");
							int day = sign.optInt("signsum");
							if (callback != null)
							{
								callback.onLuanchCheckTodaySignSuccess(maxCoin, coin, day);
							}
						}
						else
						{
							if (callback != null)
							{
								callback.onLuanchCheckTodaySignFail(message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (callback != null)
						{
							callback.onLuanchCheckTodaySignFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 验证今天是否可以签到
	 * 
	 * @param context
	 */
	public static void getCoinSign(final IonCoinSignCallback callback)
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
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("getCoinSign", Request.Method.POST, ServerUrlConfig.SERVER_URL
				+ "/usersign/sign", params, new IOnDataReceiveMessageCallback()
		{
			@Override
			public void onSuccess(int result, String message, JSONObject jsonObject)
			{
				if (StatusCode.SUCCESS == result)
				{
					JSONObject object = jsonObject.optJSONObject("sign");
					if (null != object)
					{
						int coin = object.optInt("coin");
						if (V2GogoApplication.getMasterLoginState())
						{
							V2GogoApplication.getCurrentMatser().setCoin(V2GogoApplication.getCurrentMatser().getCoin() + coin);
							V2GogoApplication.getCurrentMatser().setAllcoin(V2GogoApplication.getCurrentMatser().getAllcoin() + coin);
							V2GogoApplication.getCurrentMatser().setWeekcoin(V2GogoApplication.getCurrentMatser().getWeekcoin() + coin);
							V2GogoApplication.updateMatser();
						}
						if (null != callback)
						{
							callback.IonCoinSignSuccess(coin);
						}
					}
				}
				else
				{
					if (callback != null)
					{
						callback.IonCoinSignFail(message);
					}
				}
			}

			@Override
			public void onError(String errorMessage)
			{
				if (null != callback)
				{
					callback.IonCoinSignFail(errorMessage);
				}
			}
		});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 清除领取金币的任务
	 */
	public static void clearGetCoinSignTask()
	{
		HttpProxy.removeRequestTask("getCoinSign");
	}

	/**
	 * 清除验证今天是否可以签到的任务
	 */
	public static void clearLuanchCheckTodaySignTask()
	{
		HttpProxy.removeRequestTask("luanchCheckTodaySign");
	}

	/**
	 * 签到领金币回调
	 * 
	 * @author houjun
	 */
	public interface IonCoinSignCallback
	{
		public void IonCoinSignSuccess(int coin);

		public void IonCoinSignFail(String errorMessage);
	}

	/**
	 * 是否可以签到的数据回调
	 * 
	 * @author houjun
	 */
	public interface IonLuanchCheckTodaySignCallback
	{
		public void onLuanchCheckTodaySignSuccess(int maxSign, int coin, int day);

		public void onLuanchCheckTodaySignFail(String errorMessage);
	}
}
