package com.v2gogo.project.manager.home;

import org.json.JSONObject;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.v2gogo.project.db.CacheInfo;
import com.v2gogo.project.domain.home.ConcernInfo;
import com.v2gogo.project.manager.HttpJsonObjectRequest;
import com.v2gogo.project.manager.HttpJsonObjectRequest.IOnDataReceiveMessageCallback;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.utils.StatusCode;
import com.v2gogo.project.utils.dao.DbService;
import com.v2gogo.project.utils.http.HttpProxy;
import com.v2gogo.project.utils.parse.JsonParser;

/**
 * 百姓关注管理器
 * 
 * @author houjun
 */
public class PeopleConcernManager
{

	public static final int FIRST_PAGE = 1;

	/**
	 * 拉取百姓关注的话题列表
	 */
	public static void getPeopleTopicList(final int currentPage, final IonPeopleTopicListCallback topicListCallback)
	{
		String url = ServerUrlConfig.SERVER_URL + "/info/getbxgzinfos?page=" + currentPage;
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("getPeopleTopicList", Request.Method.GET, url, null,
				new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int result,String message, JSONObject jsonObject)
					{
						if (StatusCode.SUCCESS == result)
						{
							ConcernInfo concernInfo = (ConcernInfo) JsonParser.parseObject(jsonObject.toString(), ConcernInfo.class);
							cachePeopleConcernDatas(jsonObject, concernInfo);
							if (null != topicListCallback)
							{
								topicListCallback.onPeopleTopicListSuccess(concernInfo);
							}
						}
						else 
						{
							if (null != topicListCallback)
							{
								topicListCallback.onPeopleTopicListFail(message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (null != topicListCallback)
						{
							topicListCallback.onPeopleTopicListFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 清除百姓关注的文章列表请求任务
	 */
	public static void clearGetPeopleTopicList()
	{
		HttpProxy.removeRequestTask("getPeopleTopicList");
	}

	/**
	 * 缓存百姓关注数据
	 * 
	 * @param context
	 * @param jsonObject
	 * @param concernInfo
	 */
	private static void cachePeopleConcernDatas(JSONObject jsonObject, ConcernInfo concernInfo)
	{
		if (concernInfo != null)
		{
			if (concernInfo.getCurrentPage() == FIRST_PAGE)
			{
				CacheInfo cacheInfo = new CacheInfo();
				cacheInfo.setResponse(jsonObject.toString());
				cacheInfo.setType(CacheInfo.TYPE_PEOPLE_CONCERN);
				DbService.getInstance().insertCacheData(cacheInfo);
			}
		}
	}

	/**
	 * 得到本地缓存数据
	 */
	public static ConcernInfo getLocalPeopleConcernDatas(Context context)
	{
		CacheInfo cacheInfo = DbService.getInstance().getCacheData(CacheInfo.TYPE_PEOPLE_CONCERN);
		if (null != cacheInfo)
		{
			ConcernInfo concernInfo = (ConcernInfo) JsonParser.parseObject(cacheInfo.getResponse(), ConcernInfo.class);
			return concernInfo;
		}
		return null;
	}

	/**
	 * 话题数据接口
	 * 
	 * @author houjun
	 */
	public interface IonPeopleTopicListCallback
	{
		public void onPeopleTopicListSuccess(ConcernInfo concernInfo);

		public void onPeopleTopicListFail(String errorMessage);
	}
}
