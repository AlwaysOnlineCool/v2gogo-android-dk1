package com.v2gogo.project.utils.qiniu;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.manager.HttpJsonObjectRequest;
import com.v2gogo.project.manager.HttpJsonObjectRequest.IOnDataReceiveMessageCallback;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.manager.upload.CommentUploadMultimediaManager;
import com.v2gogo.project.utils.StatusCode;
import com.v2gogo.project.utils.common.MD5Util;
import com.v2gogo.project.utils.http.HttpProxy;

public class QiNiuUploadToken
{

	/**
	 * 获取主题图片上传文件token
	 * 
	 * @param callback
	 *            数据回调
	 */
	public static void getUploadToken(final String tid, final IonUploadTokenCallback callback)
	{
		String rand = Math.random() + "";
		String username = V2GogoApplication.getMasterLoginState() ? V2GogoApplication.getCurrentMatser().getUsername() : "";
		Map<String, String> params = new HashMap<String, String>();
		if (V2GogoApplication.getMasterLoginState())
		{
			params.put("username", V2GogoApplication.getCurrentMatser().getUsername());
			params.put("token", V2GogoApplication.getCurrentMatser().getToken());
			params.put("rand", rand);
			params.put("tId", tid);
		}
		String signature = MD5Util.getMd5Token(params);
		String url = ServerUrlConfig.SERVER_URL + "/topic/getUploadToken?username=" + username + "&signature=" + signature + "&rand=" + rand + "&tId=" + tid;
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("getUploadToken", Request.Method.GET, url, null,
				new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int code, String message, JSONObject response)
					{
						parseTokenData(callback, code, message, response);
					}

					@Override
					public void onError(String errorMessage)
					{
						if (callback != null)
						{
							callback.onUploadTokenFail(StatusCode.FAIL, errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 获取主题图片上传文件token
	 * 
	 * @param callback
	 *            数据回调
	 */
	public static void getUploadVoiceToken(final String tid, final IonUploadTokenCallback callback)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("tId", tid);
		params.put("username", V2GogoApplication.getCurrentMatser().getUsername());
		final JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("exchangeGoodsByGoodsId", Request.Method.POST,
				ServerUrlConfig.SERVER_URL + "/topic/getUploadVoiceToken", params, new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int code, String message, JSONObject response)
					{
						parseVoiceTokenData(callback, code, message, response);
					}

					@Override
					public void onError(String errorMessage)
					{
						if (callback != null)
						{
							callback.onUploadTokenFail(StatusCode.FAIL, errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 获取爆料上传多媒体：图片token
	 * 
	 * @param callback
	 *            数据回调
	 */
	public static void getUploadFactMultmediaImgToken(int type, final IonUploadTokenCallback callback)
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
		// 获取地址
		String url = getUrl(type);
		final JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("getUploadImgToken", Request.Method.POST, url, params,
				new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int code, String message, JSONObject response)
					{
						parseFactMultimediaTokenData(callback, code, message, response);
					}

					@Override
					public void onError(String errorMessage)
					{
						if (callback != null)
						{
							callback.onUploadTokenFail(StatusCode.FAIL, errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * method desc：根据上传类型获取接口地址
	 * 
	 * @param type
	 * @return
	 */
	private static String getUrl(int type)
	{
		String url = "";
		switch (type)
		{
			case CommentUploadMultimediaManager.UPLOAD_FACT_MULTIMEDIA_IMG:
				url = ServerUrlConfig.SERVER_URL + "/common/getUploadImgToken";
				break;
			case CommentUploadMultimediaManager.UPLOAD_FACT_MULTIMEDIA_VIDEO:
				url = ServerUrlConfig.SERVER_URL + "/common/getUploadVideoToken";
				break;
			case CommentUploadMultimediaManager.UPLOAD_FACT_MULTIMEDIA_VOICE:
				url = ServerUrlConfig.SERVER_URL + "/common/getUploadVoiceToken";
				break;
		}
		return url;
	}

	/**
	 * 获取爆料上传多媒体：视频token
	 * 
	 * @param callback
	 *            数据回调
	 */
	public static void getUploadFactMultmediaVideoToken(final IonUploadTokenCallback callback)
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
		final JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("getUploadVideoToken", Request.Method.POST,
				ServerUrlConfig.SERVER_URL + "/common/getUploadVideoToken", params, new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int code, String message, JSONObject response)
					{
						// parseVoiceTokenData(callback, code, message, response);
					}

					@Override
					public void onError(String errorMessage)
					{
						if (callback != null)
						{
							callback.onUploadTokenFail(StatusCode.FAIL, errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 获取爆料上传多媒体：视频token
	 * 
	 * @param callback
	 *            数据回调
	 */
	public static void getUploadFactMultmediaVoiceToken(final IonUploadTokenCallback callback)
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
		final JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("getUploadVoiceToken", Request.Method.POST,
				ServerUrlConfig.SERVER_URL + "/common/getUploadVoiceToken", params, new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int code, String message, JSONObject response)
					{
						// parseVoiceTokenData(callback, code, message, response);
					}

					@Override
					public void onError(String errorMessage)
					{
						if (callback != null)
						{
							callback.onUploadTokenFail(StatusCode.FAIL, errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 获取一般上传token
	 * 
	 * @param callback
	 *            数据回调
	 */
	public static void getUserUploadToken(String url, final IonUploadTokenCallback callback)
	{
		Map<String, String> params = new HashMap<String, String>();
		String username = V2GogoApplication.getMasterLoginState() ? V2GogoApplication.getCurrentMatser().getUsername() : "";
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
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("getUploadToken", Request.Method.POST, url, params,
				new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int code, String message, JSONObject response)
					{
						parseTokenData(callback, code, message, response);
					}

					@Override
					public void onError(String errorMessage)
					{
						if (callback != null)
						{
							callback.onUploadTokenFail(StatusCode.FAIL, errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 解析token数据
	 */
	private static void parseTokenData(final IonUploadTokenCallback callback, int code, String message, JSONObject response)
	{
		if (StatusCode.SUCCESS == code)
		{
			String token = null;
			JSONObject jsonObject = response.optJSONObject("result");
			if (jsonObject != null)
			{
				token = jsonObject.optString("uploadImgToken");
				if (null != callback)
				{
					callback.onUploadTokenSuccess(token);
				}
			}
			else
			{
				if (callback != null)
				{
					callback.onUploadTokenFail(code, message);
				}
			}
		}
		else
		{
			if (callback != null)
			{
				callback.onUploadTokenFail(code, message);
			}
		}
	}

	/**
	 * 解析token数据
	 */
	private static void parseVoiceTokenData(final IonUploadTokenCallback callback, int code, String message, JSONObject response)
	{
		if (StatusCode.SUCCESS == code)
		{
			String token = null;
			JSONObject jsonObject = response.optJSONObject("result");
			if (jsonObject != null)
			{
				token = jsonObject.optString("uploadVoiceToken");
				if (null != callback)
				{
					callback.onUploadTokenSuccess(token);
				}
			}
			else
			{
				if (callback != null)
				{
					callback.onUploadTokenFail(code, message);
				}
			}
		}
		else
		{
			if (callback != null)
			{
				callback.onUploadTokenFail(code, message);
			}
		}
	}

	/**
	 * 解析token数据
	 */
	private static void parseFactMultimediaTokenData(final IonUploadTokenCallback callback, int code, String message, JSONObject response)
	{
		if (StatusCode.SUCCESS == code)
		{
			String token = null;
			JSONObject jsonObject = response.optJSONObject("result");
			if (jsonObject != null)
			{
				token = jsonObject.optString("token");
				if (null != callback)
				{
					callback.onUploadTokenSuccess(token);
				}
			}
			else
			{
				if (callback != null)
				{
					callback.onUploadTokenFail(code, message);
				}
			}
		}
		else
		{
			if (callback != null)
			{
				callback.onUploadTokenFail(code, message);
			}
		}
	}

	/**
	 * 上传token数据回调
	 * 
	 * @author houjun
	 */
	public interface IonUploadTokenCallback
	{
		public void onUploadTokenSuccess(String token);

		public void onUploadTokenFail(int code, String message);
	}
}
