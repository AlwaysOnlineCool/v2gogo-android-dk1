package com.v2gogo.project.manager.home.v2gogo;

import org.json.JSONObject;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.v2gogo.project.db.CacheInfo;
import com.v2gogo.project.domain.home.V2gogoArticeInfo;
import com.v2gogo.project.manager.HttpJsonObjectRequest;
import com.v2gogo.project.manager.HttpJsonObjectRequest.IOnDataReceiveMessageCallback;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.utils.StatusCode;
import com.v2gogo.project.utils.dao.DbService;
import com.v2gogo.project.utils.http.HttpProxy;
import com.v2gogo.project.utils.parse.JsonParser;

public class V2gogoManager
{

	public static final int FIRST_PAGE = 1;

	/**
	 * 拉取v2gogo文章列表
	 * 
	 * @param context
	 * @param page
	 */
	public static void getV2gogoArticeList(int page, final IonV2gogoArticeListCallback callback)
	{
		String url = ServerUrlConfig.SERVER_URL + "/v2gogoinfomationapp/v2gogolist?page=" + page;
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("getV2gogoArticeList", Request.Method.GET, url, null,
				new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int result, String message,JSONObject jsonObject)
					{
						if (StatusCode.SUCCESS == result)
						{
							V2gogoArticeInfo articeInfo = (V2gogoArticeInfo) JsonParser.parseObject(jsonObject.toString(), V2gogoArticeInfo.class);
							if (articeInfo != null)
							{
								if (articeInfo.getCurrentPage() == FIRST_PAGE)
								{
									cacheV2gogoArticeListDatas(jsonObject);
								}
							}
							if (callback != null)
							{
								callback.onV2gogoArticeListSuccess(articeInfo);
							}
						}
						else
						{
							if (callback != null)
							{
								callback.onV2gogoArticeListFail(message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (null != errorMessage)
						{
							callback.onV2gogoArticeListFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 缓存v2gogo文章列表数据
	 */
	private static void cacheV2gogoArticeListDatas(JSONObject jsonObject)
	{
		CacheInfo cacheInfo = new CacheInfo();
		cacheInfo.setType(CacheInfo.TYPE_V2GOGO_REAL_INTERACTION);
		cacheInfo.setResponse(jsonObject.toString());
		DbService.getInstance().insertCacheData(cacheInfo);
	}

	/**
	 * 得到本地缓存数据
	 * 
	 * @return
	 */
	public static V2gogoArticeInfo getLocalV2gogoArticeListDatas(Context context)
	{
		CacheInfo cacheInfo = DbService.getInstance().getCacheData(CacheInfo.TYPE_V2GOGO_REAL_INTERACTION);
		if (null != cacheInfo)
		{
			V2gogoArticeInfo v2gogoArticeInfo = (V2gogoArticeInfo) JsonParser.parseObject(cacheInfo.getResponse(), V2gogoArticeInfo.class);
			return v2gogoArticeInfo;
		}
		return null;
	}

	/**
	 * 清除v2gogo文章列表的清除任务
	 */
	public static void clearGetV2gogoArticeListTask()
	{
		HttpProxy.removeRequestTask("getV2gogoArticeList");
	}

	/**
	 * v2gogo文章列表数据回调
	 * 
	 * @author houjun
	 */
	public interface IonV2gogoArticeListCallback
	{
		public void onV2gogoArticeListSuccess(V2gogoArticeInfo v2gogoArticeInfo);

		public void onV2gogoArticeListFail(String errorMessage);
	}

}
