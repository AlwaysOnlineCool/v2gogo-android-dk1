package com.v2gogo.project.manager.profile;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.v2gogo.project.domain.profile.ProfileMessageListInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.manager.HttpJsonObjectRequest;
import com.v2gogo.project.manager.HttpJsonObjectRequest.IOnDataReceiveMessageCallback;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.utils.StatusCode;
import com.v2gogo.project.utils.common.MD5Util;
import com.v2gogo.project.utils.http.HttpProxy;
import com.v2gogo.project.utils.parse.JsonParser;

/**
 * 我的消息管理器
 * 
 * @author houjun
 */
public class ProfileMessageManager
{

	public static final int FIRST_PAGE = 1;

	public static final int SIGNAL_MESSAGE = 0;
	public static final int MESSAGE_ALL = 1;

	public static void getProfileMessageList(int page, final IonProfileMessageListCallback callback)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("page", page + "");
		params.put("pageSize", 20 + "");
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
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("getProfileMessageList", Request.Method.POST,
				ServerUrlConfig.SERVER_URL + "/vusermessageapp/msglist", params, new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int result, String message, JSONObject jsonObject)
					{
						if (StatusCode.SUCCESS == result)
						{
							ProfileMessageListInfo messageListInfo = (ProfileMessageListInfo) JsonParser.parseObject(jsonObject.toString(),
									ProfileMessageListInfo.class);
							if (null != callback)
							{
								callback.onProfileMessageListSuccess(messageListInfo);
							}
						}
						else
						{
							if (callback != null)
							{
								callback.onProfileMessageListFail(message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (null != callback)
						{
							callback.onProfileMessageListFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 读取消息
	 */
	public static void readProfileMessage(int all, final String id, final IonProfileReadMessageCallback callback)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		params.put("all", all + "");
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
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("readProfileMessage", Request.Method.POST,
				ServerUrlConfig.SERVER_URL + "/vusermessageapp/readymsg", params, new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int result, String message, JSONObject jsonObject)
					{
						if (StatusCode.SUCCESS == result)
						{
							if (null != callback)
							{
								callback.onProfileReadMessageSuccess(id);
							}
						}
						else
						{
							if (callback != null)
							{
								callback.onProfileReadMessageFail(message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (null != callback)
						{
							callback.onProfileReadMessageFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 清除我的消息列表的请求任务
	 */
	public static void clearGetProfileMessageListTask()
	{
		HttpProxy.removeRequestTask("getProfileMessageList");
	}

	/**
	 * 清除已读的请求任务
	 */
	public static void clearReadProfileMessageTask()
	{
		HttpProxy.removeRequestTask("readProfileMessage");
	}

	/**
	 * 个人消息列表数据回调
	 * 
	 * @author houjun
	 */
	public interface IonProfileReadMessageCallback
	{
		public void onProfileReadMessageSuccess(String id);

		public void onProfileReadMessageFail(String errorMessage);
	}

	/**
	 * 个人消息列表数据回调
	 * 
	 * @author houjun
	 */
	public interface IonProfileMessageListCallback
	{
		public void onProfileMessageListSuccess(ProfileMessageListInfo messageListInfo);

		public void onProfileMessageListFail(String errorMessage);
	}

}
