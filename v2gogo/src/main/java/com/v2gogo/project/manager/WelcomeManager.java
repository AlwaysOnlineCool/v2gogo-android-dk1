package com.v2gogo.project.manager;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.v2gogo.project.db.CacheInfo;
import com.v2gogo.project.domain.WelcomeInfo;
import com.v2gogo.project.manager.HttpJsonObjectRequest.IOnDataReceiveMessageCallback;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.utils.StatusCode;
import com.v2gogo.project.utils.common.LogUtil;
import com.v2gogo.project.utils.dao.DbService;
import com.v2gogo.project.utils.http.HttpProxy;
import com.v2gogo.project.utils.parse.JsonParser;

/**
 * 加载管理器(是否有最新图片，图片信息)
 * 
 * @author houjun
 */
public class WelcomeManager
{

	/**
	 * 拉取应用程序启动信息
	 */
	public static void getAppLoadingImage(final IOnAppLoadingCallback callback)
	{
		// String url = ServerUrlConfig.SERVER_URL + "/welcomeapp/getwelcome?ios=0";
		String url = ServerUrlConfig.SERVER_URL + "/welcomeapp/getWelcomeVersion?ios=0";
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("getAppLoadingImage", Request.Method.GET, url, null,
				new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int code, String message, JSONObject response)
					{
						LogUtil.e("hrx", "response:" + response.toString());
						if (code == StatusCode.SUCCESS)
						{
							String resultStr = response.optString("result");
							WelcomeInfo welcomeInfo = (WelcomeInfo) JsonParser.parseObject(resultStr, WelcomeInfo.class);
							cacheWelcomeDatas(response);
							if (null != callback)
							{
								callback.onAppLoadingSuccesss(welcomeInfo);
							}
						}
						else
						{
							if (null != callback)
							{
								callback.onAppLoadingFail();
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (null != callback)
						{
							callback.onAppLoadingFail();
						}
					}

				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 缓存本地数据
	 */
	private static void cacheWelcomeDatas(JSONObject response)
	{
		CacheInfo cacheInfo = new CacheInfo();
		cacheInfo.setType(CacheInfo.TYPE_WELCOME);
		cacheInfo.setResponse(response.toString());
		DbService.getInstance().insertCacheData(cacheInfo);
	}

	/**
	 * 得到本地缓存的数据
	 * 
	 * @return
	 */
	public static WelcomeInfo getAppLocalLoadingImage(Context context)
	{
		CacheInfo cacheInfo = DbService.getInstance().getCacheData(CacheInfo.TYPE_WELCOME);
		if (null != cacheInfo)
		{
			String resultStr = null;
			try
			{
				JSONObject jsonObject = new JSONObject(cacheInfo.getResponse());
				resultStr = jsonObject.optString("result");
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}
			if (resultStr == null)
			{
				return null;
			}
			WelcomeInfo welcomeInfo = (WelcomeInfo) JsonParser.parseObject(resultStr, WelcomeInfo.class);
			return welcomeInfo;
		}
		return null;
	}

	/**
	 * 取消引导页的请求任务
	 */
	public static void clearGetAppLoadingImageTask()
	{
		HttpProxy.removeRequestTask("getAppLoadingImage");
	}

	/**
	 * 拉取应用启动信息数据回调
	 * 
	 * @author houjun
	 */
	public interface IOnAppLoadingCallback
	{
		// 加载启动信息成功
		public void onAppLoadingSuccesss(WelcomeInfo welcomeInfo);

		// 加载启动信息失败
		public void onAppLoadingFail();
	}

}
