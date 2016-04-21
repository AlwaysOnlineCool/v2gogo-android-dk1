package com.v2gogo.project.manager.home.theme;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.v2gogo.project.domain.home.theme.ThemePhotoCommandListInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.manager.HttpJsonObjectRequest;
import com.v2gogo.project.manager.HttpJsonObjectRequest.IOnDataReceiveMessageCallback;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.utils.StatusCode;
import com.v2gogo.project.utils.common.MD5Util;
import com.v2gogo.project.utils.http.HttpProxy;
import com.v2gogo.project.utils.parse.JsonParser;

public class ThemePhotoCommandManager
{

	public static final int FIRST_PAGE = 1;

	/**
	 * 图片点赞
	 * 
	 * @param pid
	 */
	public static void commandThemePhoto(String pid, final IonCommandThemePhotoCallback callback)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("photoId", pid);
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
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("commandThemePhoto", Request.Method.POST,
				ServerUrlConfig.SERVER_URL + "/photo/praisePhoto", params, new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int code, String message, JSONObject response)
					{
						if (null != callback)
						{
							callback.onCommandThemePhoto(code, message);
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (null != callback)
						{
							callback.onCommandThemePhoto(StatusCode.FAIL, errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 图片点赞人员列表
	 */
	public static void loadCommandUserList(String photoId, int page, final IonCommandListThemePhotoCallback callback)
	{
		String url = ServerUrlConfig.SERVER_URL + "/photo/getPhotoPraiseByPage?photoId=" + photoId + "&page=" + page;
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("loadCommandUserList", Request.Method.POST, url, null,
				new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int code, String message, JSONObject response)
					{
						if (StatusCode.SUCCESS == code)
						{
							JSONObject jsonObject = response.optJSONObject("result");
							if (null != jsonObject)
							{
								ThemePhotoCommandListInfo themePhotoCommandListInfo = (ThemePhotoCommandListInfo) JsonParser.parseObject(jsonObject.toString(),
										ThemePhotoCommandListInfo.class);
								if (callback != null)
								{
									callback.onCommandListThemePhotoSuccess(themePhotoCommandListInfo);
								}
							}
						}
						else
						{
							if (null != callback)
							{
								callback.onCommandListThemePhotoFail(code, message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (null != callback)
						{
							callback.onCommandListThemePhotoFail(-1, errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	public interface IonCommandThemePhotoCallback
	{
		public void onCommandThemePhoto(int code, String message);
	}

	public interface IonCommandListThemePhotoCallback
	{
		public void onCommandListThemePhotoFail(int code, String message);

		public void onCommandListThemePhotoSuccess(ThemePhotoCommandListInfo themePhotoCommandListInfo);
	}

}
