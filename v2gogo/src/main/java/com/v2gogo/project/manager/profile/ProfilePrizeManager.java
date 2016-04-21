package com.v2gogo.project.manager.profile;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.v2gogo.project.domain.profile.ProfilePrizeListInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.manager.HttpJsonObjectRequest;
import com.v2gogo.project.manager.HttpJsonObjectRequest.IOnDataReceiveMessageCallback;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.utils.StatusCode;
import com.v2gogo.project.utils.common.MD5Util;
import com.v2gogo.project.utils.http.HttpProxy;
import com.v2gogo.project.utils.parse.JsonParser;

/**
 * 我的奖品列表管理器
 * 
 * @author houjun
 */
public class ProfilePrizeManager
{
	public static final int FIRST_PAGE = 1;

	/**
	 * 我的奖品列表
	 */
	public static void getProfilePrizeList(int page, final IonProfilePrizeListCallback callback)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("page", page + "");
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
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("getProfilePrizeList", Request.Method.POST,
				ServerUrlConfig.SERVER_URL + "/userprizeapp/list", params, new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int result, String message, JSONObject jsonObject)
					{
						if (StatusCode.SUCCESS == result)
						{
							ProfilePrizeListInfo profilePrizeListInfo = (ProfilePrizeListInfo) JsonParser.parseObject(jsonObject.toString(),
									ProfilePrizeListInfo.class);
							if (null != callback)
							{
								callback.onProfilePrizeListSuccess(profilePrizeListInfo);
							}
						}
						else
						{
							if (null != callback)
							{
								callback.onProfilePrizeListFail(message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (null != callback)
						{
							callback.onProfilePrizeListFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);

	}

	/**
	 * 查看众筹结果
	 * 
	 * @param pid
	 * @param callback
	 */
	public static void getcrowdfunding(final String pid, final IonGetCrowdFundingCallback callback)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("pid", pid);
		final JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("getcrowdfunding", Request.Method.POST,
				ServerUrlConfig.SERVER_URL + "/prizepaperapp/getcrowdfunding", params, new IOnDataReceiveMessageCallback()
				{

					@Override
					public void onSuccess(int result, String message, JSONObject response)
					{

						if (StatusCode.SUCCESS == result)
						{
							if (null != callback)
							{
								JSONObject jsonObject = response.optJSONObject("result");
								if (jsonObject != null)
								{
									String resultInfo = jsonObject.optString("resultInfo", "");
									callback.onGetCrowdFundingSuccess(resultInfo);
								}
							}
						}
						else
						{
							if (null != callback)
							{
								callback.onGetCrowdFundingFail(message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (null != callback)
						{
							callback.onGetCrowdFundingFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 领取奖品
	 */
	public static void getPrizeGoods(final String id, final String username, final String phone, final String address, final int receiveType,
			final IonGetPrizeGoodsCallback callback)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("pid", id);
		params.put("name", username);
		params.put("phone", phone);
		params.put("address", address);
		params.put("receivetype", receiveType + "");
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
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("getPrizeGoods", Request.Method.POST,
				ServerUrlConfig.SERVER_URL + "/userprizeapp/receiveprize", params, new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int result, String message, JSONObject jsonObject)
					{
						if (StatusCode.SUCCESS == result)
						{
							if (null != callback)
							{
								callback.onGetPrizeGoodsSuccess(id, receiveType);
							}
						}
						else
						{
							if (null != callback)
							{
								callback.onGetPrizeGoodsFail(message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (null != callback)
						{
							callback.onGetPrizeGoodsFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 清除领取奖品的请求任务
	 */
	public static void clearGetPrizeGoodsTask()
	{
		HttpProxy.removeRequestTask("getPrizeGoods");
	}

	/**
	 * 清除我的奖品的请求任务
	 */
	public static void clearGetProfilePrizeListTask()
	{
		HttpProxy.removeRequestTask("getProfilePrizeList");
	}

	/**
	 * 领取奖品数据回调
	 * 
	 * @author
	 */
	public interface IonGetPrizeGoodsCallback
	{
		public void onGetPrizeGoodsSuccess(String id, int receiveType);

		public void onGetPrizeGoodsFail(String errorMessage);
	}

	/**
	 * 查看众筹数据回调
	 * 
	 * @author
	 */
	public interface IonGetCrowdFundingCallback
	{
		public void onGetCrowdFundingSuccess(String resultInfo);

		public void onGetCrowdFundingFail(String errorMessage);
	}

	/**
	 * 奖品列表数据回调
	 * 
	 * @author
	 */
	public interface IonProfilePrizeListCallback
	{
		public void onProfilePrizeListSuccess(ProfilePrizeListInfo profilePrizeListInfo);

		public void onProfilePrizeListFail(String errorMessage);
	}
}
