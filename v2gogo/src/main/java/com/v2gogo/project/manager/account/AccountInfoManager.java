package com.v2gogo.project.manager.account;

import java.io.File;
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
import com.v2gogo.project.manager.upload.AccountAvatarUploadManager;
import com.v2gogo.project.utils.StatusCode;
import com.v2gogo.project.utils.common.MD5Util;
import com.v2gogo.project.utils.http.HttpProxy;
import com.v2gogo.project.utils.parse.JsonParser;

/**
 * 用户信息管理类
 * 
 * @author houjun
 */
public class AccountInfoManager
{
	/**
	 * 修改用户昵称
	 */
	public static void lunachModifyProfileNikename(String nickname, String username, final IonModifyProfileCallback callback)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("fullname", nickname);
		params.put("username", username);
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
		JsonObjectRequest objectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("lunachModifyProfileNikename", Request.Method.POST,
				ServerUrlConfig.SERVER_URL + "/userapp/androidupdateusertxt", params, new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int result, String message, JSONObject response)
					{
						if (StatusCode.SUCCESS == result)
						{
							if (response != null)
							{
								JSONObject object = response.optJSONObject("user");
								if (null != object)
								{
									MatserInfo masterInfo = (MatserInfo) JsonParser.parseObject(object.toString(), MatserInfo.class);
									if (V2GogoApplication.getMasterLoginState())
									{
										V2GogoApplication.getCurrentMatser().setFullname(masterInfo.getFullname());
										V2GogoApplication.updateMatser();
									}
									if (null != callback)
									{
										callback.onModifyProfileSuccess(masterInfo);
									}
								}
							}
						}
						else
						{
							if (callback != null)
							{
								callback.onModifyProfileFail(message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (callback != null)
						{
							callback.onModifyProfileFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(objectRequest);
	}

	/**
	 * 修改用户性别
	 */
	public static void lunachModifyProfileGender(int gender, String username, final IonModifyProfileCallback callback)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("sex", gender + "");
		params.put("username", username);
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
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("lunachModifyProfileGender", Request.Method.POST,
				ServerUrlConfig.SERVER_URL + "/userapp/androidupdateusertxt", params, new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int code, String message, JSONObject response)
					{
						if (StatusCode.SUCCESS == code)
						{
							if (response != null)
							{
								JSONObject object = response.optJSONObject("user");
								if (null != object)
								{
									MatserInfo masterInfo = (MatserInfo) JsonParser.parseObject(object.toString(), MatserInfo.class);
									if (V2GogoApplication.getMasterLoginState())
									{
										V2GogoApplication.getCurrentMatser().setSex(masterInfo.getSex());
										V2GogoApplication.updateMatser();
									}
									if (null != callback)
									{
										callback.onModifyProfileSuccess(masterInfo);
									}
								}
							}
						}
						else
						{
							if (callback != null)
							{
								callback.onModifyProfileFail(message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (callback != null)
						{
							callback.onModifyProfileFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 修改用户所在地
	 */
	public static void lunachModifyProfileCity(String city, String username, final IonModifyProfileCallback callback)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("city", city);
		params.put("username", username);
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
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("lunachModifyProfileGender", Request.Method.POST,
				ServerUrlConfig.SERVER_URL + "/userapp/androidupdateusertxt", params, new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int code, String message, JSONObject response)
					{
						if (StatusCode.SUCCESS == code)
						{
							if (response != null)
							{
								JSONObject object = response.optJSONObject("user");
								if (null != object)
								{
									MatserInfo masterInfo = (MatserInfo) JsonParser.parseObject(object.toString(), MatserInfo.class);
									if (V2GogoApplication.getMasterLoginState())
									{
										V2GogoApplication.getCurrentMatser().setCity(masterInfo.getCity());
										V2GogoApplication.updateMatser();
									}
									if (null != callback)
									{
										callback.onModifyProfileSuccess(masterInfo);
									}
								}
							}
						}
						else
						{
							if (callback != null)
							{
								callback.onModifyProfileFail(message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (callback != null)
						{
							callback.onModifyProfileFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 修改用户头像
	 * 
	 * @param context
	 *            上下文环境
	 * @param filePath
	 *            头像本地路径
	 * @param mUserId
	 *            用户id
	 * @param callback
	 *            上传头像结果数据回调
	 */
	public static void lunachModifyProfileAvatar(final File avatar, final String username)
	{
		AccountAvatarUploadManager.modifyAccountAvatar(avatar, username);
	}

	
	/**
	 * 取消修改用户城市的请求任务
	 */
	public static void clearModifyProfileCityTask()
	{
		HttpProxy.removeRequestTask("lunachModifyProfileCity");
	}

	/**
	 * 取消修改性别的请求任务
	 */
	public static void clearModifyProfileGenderTask()
	{
		HttpProxy.removeRequestTask("lunachModifyProfileGender");
	}

	/**
	 * 移除修改昵称的请求任务
	 */
	public static void clearodifyProfileNikenameTask()
	{
		HttpProxy.removeRequestTask("lunachModifyProfileNikename");
	}

	/**
	 * 用户信息回调接口
	 * 
	 * @author houjun
	 */
	public interface IonModifyProfileCallback
	{
		// 修改成功
		public void onModifyProfileSuccess(MatserInfo masterInfo);

		// 修改失败
		public void onModifyProfileFail(String errorMessage);
	}

}
