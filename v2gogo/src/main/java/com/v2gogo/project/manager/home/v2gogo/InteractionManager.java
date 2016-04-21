package com.v2gogo.project.manager.home.v2gogo;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.v2gogo.project.domain.home.InteractionInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.manager.HttpJsonObjectRequest;
import com.v2gogo.project.manager.HttpJsonObjectRequest.IOnDataReceiveMessageCallback;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.utils.StatusCode;
import com.v2gogo.project.utils.common.MD5Util;
import com.v2gogo.project.utils.http.HttpProxy;
import com.v2gogo.project.utils.parse.JsonParser;

/**
 * 互动赢大奖管理器
 * 
 * @author houjun
 */
public class InteractionManager
{
	public static void getMainInteractionDatas(final IonMainInteractionDatasCallback callback)
	{
		String username = V2GogoApplication.getMasterLoginState() ? V2GogoApplication.getCurrentMatser().getUsername() : "";
		String url = ServerUrlConfig.SERVER_URL + "/v2gogointeractiveapp/getv2gogointeractive?username=" + username;
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("getMainInteractionDatas", Request.Method.GET, url, null,
				new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int result, String message, JSONObject jsonObject)
					{
						if (StatusCode.SUCCESS == result)
						{
							boolean isPraise = jsonObject.optBoolean("isPraise");
							JSONObject object = jsonObject.optJSONObject("info");
							if (null != object)
							{
								InteractionInfo interactionInfo = (InteractionInfo) JsonParser.parseObject(object.toString(), InteractionInfo.class);
								interactionInfo.setYetVoted(isPraise);
								if (null != callback)
								{
									callback.onMainInteractionDatasSuccess(interactionInfo);
								}
							}
							else
							{
								if (null != callback)
								{
									callback.onMainInteractionDatasFail(null);
								}
							}
						}
						else
						{
							if (null != callback)
							{
								callback.onMainInteractionDatasFail(message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (null != callback)
						{
							callback.onMainInteractionDatasFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 互动投票
	 */
	public static void voteInteractionById(final String articeId, final String optionId, String username, final int voteNum,
			final IonVoteInteractionCallback callback)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", optionId);
		params.put("username", username);
		params.put("vote", voteNum + "");
		params.put("infoid", articeId == null ? "" : articeId);
		if (V2GogoApplication.getMasterLoginState())
		{
			params.put("token", V2GogoApplication.getCurrentMatser().getToken());
			String signature = MD5Util.getMd5Token(params);
			params.put("signature", signature);
		}
		if (params.containsKey("token"))
		{
			params.remove("token");
		}
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("voteInteractionById", Request.Method.POST,
				ServerUrlConfig.SERVER_URL + "/v2gogointeractiveapp/v2gogovoteoption", params, new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int result, String message, JSONObject jsonObject)
					{
						if (StatusCode.SUCCESS == result)
						{
							int coin = jsonObject.optInt("coin");
							if (V2GogoApplication.getMasterLoginState())
							{
								V2GogoApplication.getCurrentMatser().setCoin(coin);
								V2GogoApplication.updateMatser();
							}
							if (callback != null)
							{
								callback.onVoteInteractionSuccess(coin, optionId, voteNum);
							}
						}
						else
						{
							if (null != callback)
							{
								callback.onVoteInteractionFail(message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (null != callback)
						{
							callback.onVoteInteractionFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 清除互动赢大奖的数据请求任务
	 */
	public static void clearGetMainInteractionDatasTask()
	{
		HttpProxy.removeRequestTask("getMainInteractionDatas");
	}

	/**
	 * 清除投票的请求任务
	 */
	public static void clearvoteInteractionByIdTask()
	{
		HttpProxy.removeRequestTask("voteInteractionById");
	}

	/**
	 * 获取互动赢大奖投票数据回调
	 * 
	 * @author houjun
	 */
	public interface IonVoteInteractionCallback
	{
		/**
		 * 获取数据成功
		 */
		public void onVoteInteractionSuccess(int coin, String optionId, int number);

		public void onVoteInteractionFail(String errorMessage);
	}

	/**
	 * 获取互动赢大奖数据回调接口
	 * 
	 * @author houjun
	 */
	public interface IonMainInteractionDatasCallback
	{
		/**
		 * 获取数据成功
		 */
		public void onMainInteractionDatasSuccess(InteractionInfo interactionInfo);

		public void onMainInteractionDatasFail(String errorMessage);
	}
}
