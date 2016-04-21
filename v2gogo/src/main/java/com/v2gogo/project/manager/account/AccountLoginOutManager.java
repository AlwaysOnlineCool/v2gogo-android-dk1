package com.v2gogo.project.manager.account;

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
import com.v2gogo.project.utils.common.SPUtil;
import com.v2gogo.project.utils.http.HttpProxy;

/**
 * 用户退出管理器
 * 
 * @author houjun
 */
public class AccountLoginOutManager
{

	/**
	 * 用户退出登录
	 */
	public static void accountLogout(final IonAccountLogoutCallback callback)
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
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("accountLogout", Request.Method.POST,
				ServerUrlConfig.SERVER_URL + "/userapp/logout", params, new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int result, String message, JSONObject jsonObject)
					{
						if (StatusCode.SUCCESS == result)
						{
							SPUtil.remove(V2GogoApplication.sIntance, "collections");
							V2GogoApplication.clearMatserInfo(true);
							if (null != callback)
							{
								callback.onAccountLogoutSuccess();
							}
						}
						else
						{
							if (callback != null)
							{
								callback.onAccountLogoutFail(message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (null != callback)
						{
							callback.onAccountLogoutFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 取消用户注销的请求任务
	 */
	public static void clearaccountLogoutTask()
	{
		HttpProxy.removeRequestTask("accountLogout");
	}

	/**
	 * 用户退出登录结果回调接口
	 * 
	 * @author houjun
	 */
	public interface IonAccountLogoutCallback
	{
		// 用户退出登录成功
		public void onAccountLogoutSuccess();

		// 用户退出登录失败
		public void onAccountLogoutFail(String errorMessage);

	}
}
