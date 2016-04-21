package com.v2gogo.project.manager.profile;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.v2gogo.project.domain.profile.ProfileEtcOrderListInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.manager.HttpJsonObjectRequest;
import com.v2gogo.project.manager.HttpJsonObjectRequest.IOnDataReceiveMessageCallback;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.utils.StatusCode;
import com.v2gogo.project.utils.common.MD5Util;
import com.v2gogo.project.utils.http.HttpProxy;
import com.v2gogo.project.utils.parse.JsonParser;

/**
 * 个人电子券管理器
 * 
 * @author houjun
 */
public class ProfileEtcQuanManager
{

	public static final int FIRST_PAGE = 1;

	/**
	 * 得到个人电子券列表
	 */
	public static void getProfileEtcQuanList(final int page, final IonProfileEtcQuanListCallback callback)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("pageNo", page + "");
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
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("getProfileEtcQuanList", Request.Method.POST,
				ServerUrlConfig.SERVER_URL + "/electronicOrder/list", params, new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int code, String message, JSONObject response)
					{
						if (StatusCode.SUCCESS == code)
						{
							if (null != callback)
							{
								JSONObject jsonObject = response.optJSONObject("result");
								if (jsonObject != null)
								{
									ProfileEtcOrderListInfo profileEtcOrderListInfo = (ProfileEtcOrderListInfo) JsonParser.parseObject(jsonObject.toString(),
											ProfileEtcOrderListInfo.class);
									if (null != callback)
									{
										callback.onProfileEtcQuanListSuccess(profileEtcOrderListInfo);
									}
								}
							}
						}
						else
						{
							if (null != callback)
							{
								callback.onProfileEtcQuanListFail(code, message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (callback != null)
						{
							callback.onProfileEtcQuanListFail(-1, errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 得到个人电子券列表
	 */
	public static void getProfileEtcQuan(String replayCode, final String id, final IonProfileEtcQuanCallback callback)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		params.put("replayCode", replayCode);
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
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("getProfileEtcQuan", Request.Method.POST,
				ServerUrlConfig.SERVER_URL + "/electronicOrder/validate", params, new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int code, String message, JSONObject response)
					{
						if (StatusCode.SUCCESS == code)
						{
							if (callback != null)
							{
								callback.onProfileEtcQuanSuccess(id,message);
							}
						}
						else
						{
							if (null != callback)
							{
								callback.onProfileEtcQuanFail(code, message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (callback != null)
						{
							callback.onProfileEtcQuanFail(-1, errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 清除列表请求任务
	 */
	public static void clearEtcListTask()
	{
		HttpProxy.removeRequestTask("getProfileEtcQuanList");
	}

	/**
	 * 删除领取的请求任务
	 */
	public static void clearEtcGetTask()
	{
		HttpProxy.removeRequestTask("getProfileEtcQuan");
	}

	public interface IonProfileEtcQuanListCallback
	{
		public void onProfileEtcQuanListSuccess(ProfileEtcOrderListInfo profileEtcOrderListInfo);

		public void onProfileEtcQuanListFail(int code, String message);
	}

	public interface IonProfileEtcQuanCallback
	{
		public void onProfileEtcQuanSuccess(String id,String message);

		public void onProfileEtcQuanFail(int code, String message);
	}
}
