package com.v2gogo.project.manager.account;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.v2gogo.project.db.MatserInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.manager.HttpJsonObjectRequest;
import com.v2gogo.project.manager.HttpJsonObjectRequest.IOnDataReceiveMessageCallback;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.utils.StatusCode;
import com.v2gogo.project.utils.common.AppUtil;
import com.v2gogo.project.utils.common.MD5Util;
import com.v2gogo.project.utils.common.SPUtil;
import com.v2gogo.project.utils.http.Constants;
import com.v2gogo.project.utils.http.HttpProxy;
import com.v2gogo.project.utils.parse.JsonParser;

/**
 * 账号登录管理器
 * 
 * @author houjun
 */
public class AccountLoginManager
{
	/**
	 * 账号登录
	 * 
	 * @param account
	 *            账号
	 * @param password
	 *            密码
	 * @param callback
	 *            登录接口回调
	 */
	public static void accountLogin(String account, final String password, boolean isAuto, final IAccountLoginCallback callback)
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put("ios", 0 + "");
		map.put("username", account);
		if (isAuto)
		{
			map.put("autologin", 1 + "");
		}
		else
		{
			map.put("autologin", 0 + "");
		}
		map.put("userpass", MD5Util.getMD5String(password));
		map.put("appVersion", AppUtil.getVersionCode(V2GogoApplication.sIntance) + "");
		map.put("devicetoken", (String) (SPUtil.get(V2GogoApplication.sIntance, Constants.CLIENT_ID, "")));
		SPUtil.remove(V2GogoApplication.sIntance, "collections");
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("accountLogin", Request.Method.POST, ServerUrlConfig.SERVER_URL
				+ "/userapp/login", map, new IOnDataReceiveMessageCallback()
		{
			@Override
			public void onSuccess(int result, String message, JSONObject jsonObject)
			{
				if (StatusCode.SUCCESS == result)
				{
					MatserInfo masterInfo = dealWithCurrentLoginUser(password, jsonObject);
					if (null != callback)
					{
						callback.onAccountLoginSuccess(masterInfo);
					}
				}
				else
				{
					if (callback != null)
					{
						callback.onAccountLoginFail(message);
					}
				}
			}

			@Override
			public void onError(String errorMessage)
			{
				if (null != callback)
				{
					callback.onAccountLoginFail(errorMessage);
				}
			}
		});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 处理当前登录用户
	 * 
	 * @param response
	 * @return
	 */
	public static MatserInfo dealWithCurrentLoginUser(String pasword, JSONObject response)
	{
		MatserInfo masterInfo = new MatserInfo();
		JSONObject jsonObject = response.optJSONObject("user");
		if (null != jsonObject)
		{
			masterInfo = (MatserInfo) JsonParser.parseObject(jsonObject.toString(), MatserInfo.class);

			if (V2GogoApplication.getCurrentMatser() == null)
			{
				V2GogoApplication.setCurrentMaster(masterInfo);
			}
			else
			{
				V2GogoApplication.setCurrentMaster(masterInfo);
			}
			V2GogoApplication.updateMatser();
			SPUtil.put(V2GogoApplication.sIntance, Constants.USER_NAME, masterInfo.getUsername());
			if (!TextUtils.isEmpty(pasword))
			{
				SPUtil.put(V2GogoApplication.sIntance, Constants.USER_PASS, pasword);
			}
		}
		return masterInfo;
	}

	/**
	 * 取消用户登录请求任务
	 */
	public static void clearAccountLoginTask()
	{
		HttpProxy.removeRequestTask("accountLogin");
	}

	/**
	 * 账号登录接口
	 * 
	 * @author houjun
	 */
	public interface IAccountLoginCallback
	{
		/**
		 * 账号登录成功
		 */
		public void onAccountLoginSuccess(MatserInfo masterInfo);

		/**
		 * 账号登录失败
		 */
		public void onAccountLoginFail(String erroMessage);
	}
}
