package com.v2gogo.project.manager;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.v2gogo.project.domain.CoinChangeInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.manager.HttpJsonObjectRequest.IOnDataReceiveMessageCallback;
import com.v2gogo.project.manager.account.AccountLoginManager;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.utils.StatusCode;
import com.v2gogo.project.utils.common.MD5Util;
import com.v2gogo.project.utils.http.HttpProxy;
import com.ypy.eventbus.EventBus;

/**
 * 拉取用户信息
 * 
 * @author houjun
 */
public class UserInfoManager
{

	/**
	 * 同步用户信息
	 */
	public static void updateUserInfos()
	{
		if (!V2GogoApplication.getMasterLoginState())
		{
			return;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", V2GogoApplication.getCurrentMatser().getUsername());
		params.put("token", V2GogoApplication.getCurrentMatser().getToken());
		String signature = MD5Util.getMd5Token(params);
		params.put("signature", signature);
		if (params.containsKey("token"))
		{
			params.remove("token");
		}
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("updateUserInfos", Request.Method.POST,
				ServerUrlConfig.SERVER_URL + "/userapp/getuser", params, new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int code, String message, JSONObject response)
					{
						if (StatusCode.SUCCESS == code)
						{
							AccountLoginManager.dealWithCurrentLoginUser(null, response);
							CoinChangeInfo changeInfo = new CoinChangeInfo();
							EventBus.getDefault().post(changeInfo);
						}
					}

					@Override
					public void onError(String errorMessage)
					{
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}
}
