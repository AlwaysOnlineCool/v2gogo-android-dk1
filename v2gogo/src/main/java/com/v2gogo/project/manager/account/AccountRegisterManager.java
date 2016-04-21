package com.v2gogo.project.manager.account;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

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

/**
 * 用户注册管理器
 * 
 * @author houjun
 */
public class AccountRegisterManager
{

	/**
	 * 账号注册
	 * 
	 * @param account
	 *            账号
	 * @param password
	 *            密码
	 * @param phoneNO
	 *            电话
	 * @param callback
	 *            注册结果回调
	 */
	public static void registerAccount(final String account, final String password, String code, String invcode, final IAccountRegisterAccountCallback callback)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("ios", 0 + "");
		params.put("code", code);
		params.put("invcode", invcode);
		params.put("username", account);
		params.put("userpass", MD5Util.getMD5String(password));
		params.put("appVersion", AppUtil.getVersionCode(V2GogoApplication.sIntance) + "");
		params.put("devicetoken", (String) (SPUtil.get(V2GogoApplication.sIntance, Constants.CLIENT_ID, "")));
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("registerAccount", Request.Method.POST,
				ServerUrlConfig.SERVER_URL + "/userapp/register", params, new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int result, String message, JSONObject jsonObject)
					{
						if (StatusCode.SUCCESS == result)
						{
							MatserInfo masterInfo = AccountLoginManager.dealWithCurrentLoginUser(password, jsonObject);
							if (null != callback)
							{
								callback.onAccountRegisterSuccess(masterInfo);
							}
						}
						else
						{
							if (null != callback)
							{
								callback.onAccountRegisterFail(message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (null != callback)
						{
							callback.onAccountRegisterFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 清除注册请求
	 */
	public static void clearRegisterAccountTask()
	{
		HttpProxy.removeRequestTask("registerAccount");
	}

	/**
	 * 注册返回结果回调接口
	 * 
	 * @author houjun
	 */
	public interface IAccountRegisterAccountCallback
	{
		// 注册成功
		public void onAccountRegisterSuccess(MatserInfo matserInfo);

		// 注册失败
		public void onAccountRegisterFail(String errorMessage);
	}

}
