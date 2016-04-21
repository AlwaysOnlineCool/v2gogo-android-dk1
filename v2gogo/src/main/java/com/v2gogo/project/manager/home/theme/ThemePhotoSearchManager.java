package com.v2gogo.project.manager.home.theme;

import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.v2gogo.project.domain.home.theme.ThemePhotoInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.manager.HttpJsonObjectRequest;
import com.v2gogo.project.manager.HttpJsonObjectRequest.IOnDataReceiveMessageCallback;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.utils.StatusCode;
import com.v2gogo.project.utils.http.HttpProxy;
import com.v2gogo.project.utils.parse.JsonParser;

/**
 * 主题图片
 * 
 * @author houjun
 */
public class ThemePhotoSearchManager
{

	/**
	 * 根据图片编号搜索图片
	 * 
	 * @param photoNo
	 */
	public static void searchThemePhotoBySn(String photoNo, String tid, final IonSearchThemePhotoCallback callback)
	{
		String username = V2GogoApplication.getMasterLoginState() ? V2GogoApplication.getCurrentMatser().getUsername() : "";
		String url = ServerUrlConfig.SERVER_URL+"/photo/searchPhotoByNo?photoNo=" + photoNo + "&username=" + username + "&tId=" + tid;
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("searchThemePhotoBySn", Request.Method.GET, url, null,
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
								jsonObject = jsonObject.optJSONObject("photo");
								if (jsonObject != null)
								{
									ThemePhotoInfo themePhotoInfo = (ThemePhotoInfo) JsonParser.parseObject(jsonObject.toString(), ThemePhotoInfo.class);
									if (null != callback)
									{
										callback.onSearchThemePhotoSuccess(themePhotoInfo);
									}
								}
							}
						}
						else
						{
							if (null != callback)
							{
								callback.onSearchThemePhotoFail(code, message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{

						if (null != callback)
						{
							callback.onSearchThemePhotoFail(StatusCode.FAIL, errorMessage);
						}

					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);

	}

	/**
	 * 根据图片id搜索图片
	 * 
	 * @param photoNo
	 */
	public static void searchThemePhotoById(String id, String tid, final IonSearchThemePhotoCallback callback)
	{
		String username = V2GogoApplication.getMasterLoginState() ? V2GogoApplication.getCurrentMatser().getUsername() : "";
		String url = ServerUrlConfig.SERVER_URL+"/photo/getPhotoById?photoId=" + id + "&username=" + username + "&tId=" + tid;
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("searchThemePhotoById", Request.Method.GET, url, null,
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
								jsonObject = jsonObject.optJSONObject("photo");
								if (jsonObject != null)
								{
									ThemePhotoInfo themePhotoInfo = (ThemePhotoInfo) JsonParser.parseObject(jsonObject.toString(), ThemePhotoInfo.class);
									if (null != callback)
									{
										callback.onSearchThemePhotoSuccess(themePhotoInfo);
									}
								}
							}
						}
						else
						{
							if (null != callback)
							{
								callback.onSearchThemePhotoFail(code, message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{

						if (null != callback)
						{
							callback.onSearchThemePhotoFail(StatusCode.FAIL, errorMessage);
						}

					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 清除根据编号搜索的任务
	 */
	public static void clearSearchThemePhotoBySnTask()
	{
		HttpProxy.removeRequestTask("searchThemePhotoBySn");
	}

	/**
	 * 清除根据编号搜索的任务
	 */
	public static void clearSearchThemePhotoByIdTask()
	{
		HttpProxy.removeRequestTask("searchThemePhotoById");
	}

	public interface IonSearchThemePhotoCallback
	{
		public void onSearchThemePhotoSuccess(ThemePhotoInfo themePhotoInfo);

		public void onSearchThemePhotoFail(int code, String errorMessage);
	}

}
