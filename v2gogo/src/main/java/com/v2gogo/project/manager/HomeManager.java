package com.v2gogo.project.manager;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.v2gogo.project.db.CacheInfo;
import com.v2gogo.project.domain.home.HomeInfo;
import com.v2gogo.project.manager.HttpJsonObjectRequest.IOnDataReceiveMessageCallback;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.utils.StatusCode;
import com.v2gogo.project.utils.dao.DbService;
import com.v2gogo.project.utils.http.HttpProxy;
import com.v2gogo.project.utils.parse.JsonParser;

/**
 * 首页管理器
 * 
 * @author houjun
 */
public class HomeManager
{

	/**
	 * 拉取首页数据
	 */
	public static void getAppHomeData(final IonHomeDataCallback callback)
	{
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("getAppHomeData", Request.Method.GET,
				ServerUrlConfig.SERVER_URL + "/homeapp/homeDataVersion", null, new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int result, String message, JSONObject jsonObject)
					{
 						if (StatusCode.SUCCESS == result)
						{
							String resultStr = jsonObject.optString("result");
							cacheHomeData(jsonObject);
							HomeInfo homeInfo = (HomeInfo) JsonParser.parseObject(resultStr, HomeInfo.class);
							if (null != callback)
							{
								callback.onHomeDataSuccess(homeInfo);
							}
						}
						else
						{
							if (callback != null)
							{
								callback.onHomeDataFail(message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (callback != null)
						{
							callback.onHomeDataFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

//	/**
//	 * 拉取首页数据
//	 */
//	public static void getAppHomeMoreData(final IonHomeDataCallback callback)
//	{
//		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("getAppHomeData", Request.Method.GET,
//				ServerUrlConfig.SERVER_URL + "/homeapp/homedata", null, new IOnDataReceiveMessageCallback()
//				{
//					@Override
//					public void onSuccess(int result, String message, JSONObject jsonObject)
//					{
//						if (StatusCode.SUCCESS == result)
//						{
//							cacheHomeData(jsonObject);
//							HomeInfo homeInfo = (HomeInfo) JsonParser.parseObject(jsonObject.toString(), HomeInfo.class);
//							if (null != callback)
//							{
//								callback.onHomeDataSuccess(homeInfo);
//							}
//						}
//						else
//						{
//							if (callback != null)
//							{
//								callback.onHomeDataFail(message);
//							}
//						}
//					}
//
//					@Override
//					public void onError(String errorMessage)
//					{
//						if (callback != null)
//						{
//							callback.onHomeDataFail(errorMessage);
//						}
//					}
//				});
//		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
//	}

	/**
	 * 加载本地首页缓存数据
	 * 
	 * @return
	 */
	public static HomeInfo getAppLocalHomeData(Context context)
	{
		CacheInfo cacheInfo = DbService.getInstance().getCacheData(CacheInfo.TYPE_HOME);
		HomeInfo homeInfo = null;
		if (null != cacheInfo)
		{
			try
			{
				JSONObject jsonObject = new JSONObject(cacheInfo.getResponse());
				String resultStr=jsonObject.optString("result");
				homeInfo = (HomeInfo) JsonParser.parseObject(resultStr, HomeInfo.class);
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}
			return homeInfo;
		}
		return null;
	}

	/**
	 * 缓存首页数据
	 */
	private static void cacheHomeData(JSONObject response)
	{
		CacheInfo cacheInfo = new CacheInfo();
		cacheInfo.setResponse(response.toString());
		cacheInfo.setType(CacheInfo.TYPE_HOME);
		DbService.getInstance().insertCacheData(cacheInfo);
	}

	/**
	 * 清除拉取首页数据请求任务
	 */
	public static void clearGetAppHomeDataTask()
	{
		HttpProxy.removeRequestTask("getAppHomeData");
	}

	/**
	 * 首页数据回调
	 * 
	 * @author houjun
	 */
	public interface IonHomeDataCallback
	{
		// 数据返回成功
		public void onHomeDataSuccess(HomeInfo homeInfo);

		// 数据返回失败
		public void onHomeDataFail(String errorMessage);
	}

}
