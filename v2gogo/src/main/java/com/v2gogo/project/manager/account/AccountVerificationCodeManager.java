package com.v2gogo.project.manager.account;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.v2gogo.project.manager.HttpJsonObjectRequest;
import com.v2gogo.project.manager.HttpJsonObjectRequest.IOnDataReceiveMessageCallback;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.utils.StatusCode;
import com.v2gogo.project.utils.http.HttpProxy;

/**
 * 用户验证码管理器
 * 
 * @author houjun
 */
public class AccountVerificationCodeManager
{
	/**
	 * 获取验证码
	 */
	public static void lunachVerificationCodeRequest(final String phoneNum, final IVerificationCodeCallback callback)
	{
		String url = ServerUrlConfig.SERVER_URL + "/valicode/getvalicode?username=" + phoneNum;
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("lunachVerificationCodeRequest", Request.Method.GET, url, null,
				new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int result, String message, JSONObject jsonObject)
					{
						// 获取验证码成功
						if (StatusCode.SUCCESS == result)
						{
							if (callback != null)
							{
								callback.onVerificationCodeSuccess();
							}
						}
						else
						{
							if (callback != null)
							{
								callback.onVerificationCodeFail(message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (callback != null)
						{
							callback.onVerificationCodeFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 验证验证码
	 */
	public static void checkVerificationCodeRequest(String phone, String code, final ICheckVerificationCodeCallback callback)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("code", code);
		params.put("username", phone);
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("checkVerificationCodeRequest", Request.Method.POST,
				ServerUrlConfig.SERVER_URL + "/valicode/checkvalicode", params, new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int result, String message, JSONObject jsonObject)
					{
						if (StatusCode.SUCCESS == result)
						{
							if (null != callback)
							{
								callback.onCheckVerificationCodeSuccess();
							}
						}
						else
						{
							if (null != callback)
							{
								callback.onCheckVerificationCodeFail(message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (null != callback)
						{
							callback.onCheckVerificationCodeFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 验证验证码返回回调接口
	 * 
	 * @author houjun
	 */
	public interface ICheckVerificationCodeCallback
	{
		// 验证码验证成功
		public void onCheckVerificationCodeSuccess();

		// 验证码验证失败
		public void onCheckVerificationCodeFail(String errorMessage);
	}

	/**
	 * 验证码返回回调接口
	 * 
	 * @author houjun
	 */
	public interface IVerificationCodeCallback
	{
		// 验证码成功
		public void onVerificationCodeSuccess();

		// 验证码失败
		public void onVerificationCodeFail(String errorMessage);
	}

}
