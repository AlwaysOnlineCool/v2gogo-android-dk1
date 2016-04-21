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
import com.v2gogo.project.utils.http.HttpProxy;

/**
 * 用户密码管理器
 * 
 * @author houjun
 */
public class AccountPasswordManager
{
	/**
	 * 修改密码
	 * 
	 * @param context
	 *            上下文环境
	 * @param oldPwd
	 *            旧密码
	 * @param newPwd
	 *            新密码
	 * @param callback
	 */
	public static void modifyAccountPassword(String oldPwd, final String newPwd, final IonmodifyAccountPasswordCallback callback)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("newpass", MD5Util.getMD5String(newPwd));
		params.put("oldpass", MD5Util.getMD5String(oldPwd));
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
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("modifyAccountPassword", Request.Method.POST,
				ServerUrlConfig.SERVER_URL + "/userapp/updateuserpass", params, new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int result, String message, JSONObject jsonObject)
					{
						if (StatusCode.SUCCESS == result)
						{
							if (null != callback)
							{
								callback.onmodifyAccountPasswordSuccess();
							}
							if (V2GogoApplication.getMasterLoginState())
							{
								V2GogoApplication.clearMatserInfo(true);
							}
						}
						else
						{
							if (callback != null)
							{
								callback.onmodifyAccountPasswordFail(message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (null != callback)
						{
							callback.onmodifyAccountPasswordFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 忘记密码
	 * 
	 * @param context
	 * @param callback
	 */
	public static void forgetAccountPassword(final String code, String phone, String newPwd, final IonforgetAccountPasswordCallback callback)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("code", code);
		params.put("username", phone);
		params.put("userpass", MD5Util.getMD5String(newPwd));
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("forgetAccountPassword", Request.Method.POST,
				ServerUrlConfig.SERVER_URL + "/userapp/resetuserpass", params, new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int result, String message, JSONObject jsonObject)
					{
						if (StatusCode.SUCCESS == result)
						{
							if (V2GogoApplication.getMasterLoginState())
							{
								V2GogoApplication.clearMatserInfo(true);
							}
							if (callback != null)
							{
								callback.onforgetAccountPasswordSuccess();
							}
						}
						else
						{
							if (callback != null)
							{
								callback.onforgetAccountPasswordFail(message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (null != callback)
						{
							callback.onforgetAccountPasswordFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 忘记密码(验证码)
	 * 
	 * @param context
	 * @param callback
	 */
	public static void forgetAccountPasswordCheckCode(String phone, final IonAccountPasswordCheckCode callback)
	{
		String url = ServerUrlConfig.SERVER_URL + "/valicode/getlosepassvalicode?username=" + phone;
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("forgetAccountPasswordCheckCode", Request.Method.GET, url,
				null, new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int result, String message, JSONObject jsonObject)
					{
						// 获取验证码成功
						if (StatusCode.SUCCESS == result)
						{
							if (callback != null)
							{
								callback.onAccountPasswordCheckCodeSuccess();
							}
						}
						else
						{
							if (callback != null)
							{
								callback.onAccountPasswordCheckCodeFail(message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (null != callback)
						{
							callback.onAccountPasswordCheckCodeFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 清除用户修改密码的请求任务
	 */
	public static void clearModifyAccountPasswordTask()
	{
		HttpProxy.removeRequestTask("modifyAccountPassword");
	}

	/**
	 * 取消忘记密码验证码的请求任务
	 */
	public static void clearForgetAccountPasswordCheckCode()
	{
		HttpProxy.removeRequestTask("forgetAccountPasswordCheckCode");
	}

	/**
	 * 忘记密码数据回调接口
	 * 
	 * @author houjun
	 */
	public interface IonmodifyAccountPasswordCallback
	{
		public void onmodifyAccountPasswordSuccess();

		public void onmodifyAccountPasswordFail(String errorMessage);
	}

	/**
	 * 忘记密码数据回调接口
	 * 
	 * @author houjun
	 */
	public interface IonforgetAccountPasswordCallback
	{
		public void onforgetAccountPasswordSuccess();

		public void onforgetAccountPasswordFail(String errorMessage);
	}

	/**
	 * 忘记密码数据回调接口
	 * 
	 * @author houjun
	 */
	public interface IonAccountPasswordCheckCode
	{
		public void onAccountPasswordCheckCodeSuccess();

		public void onAccountPasswordCheckCodeFail(String errorMessage);
	}
}
