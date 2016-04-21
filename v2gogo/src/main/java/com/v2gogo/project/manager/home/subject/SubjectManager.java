package com.v2gogo.project.manager.home.subject;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.v2gogo.project.db.CacheInfo;
import com.v2gogo.project.domain.home.subject.SubjectInfo;
import com.v2gogo.project.domain.home.subject.SubjectMoreArticle;
import com.v2gogo.project.manager.HttpJsonObjectRequest;
import com.v2gogo.project.manager.HttpJsonObjectRequest.IOnDataReceiveMessageCallback;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.utils.StatusCode;
import com.v2gogo.project.utils.dao.DbService;
import com.v2gogo.project.utils.http.HttpProxy;
import com.v2gogo.project.utils.parse.JsonParser;

/**
 * 功能：专题网络管理器
 * 
 * @ahthor：黄荣星
 * @date:2015-11-17
 * @version::V1.0
 */
public class SubjectManager
{

	/**
	 * 拉去专题网络数据
	 */
	public static void getHttpSubjectData(final String specialId, final IonSubjectDataCallback callback)
	{
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("getHttpSubjectData", Request.Method.GET,
				ServerUrlConfig.SERVER_URL + "/specialtopic/stopicData?specialId=" + specialId, null, new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int result, String message, JSONObject jsonObject)
					{
						if (StatusCode.SUCCESS == result)
						{
							String resultStr = jsonObject.optString("result");
							SubjectInfo subjectInfo = (SubjectInfo) JsonParser.parseObject(resultStr, SubjectInfo.class);
							subjectInfo.setSpecialId(specialId);

							Gson gson = new Gson();
							String jsonObjectNew = gson.toJson(subjectInfo);
							cacheSubjectData(jsonObjectNew);

							if (null != callback)
							{
								callback.onSubjectDataSuccess(subjectInfo);
							}
						}
						else
						{
							if (callback != null)
							{
								callback.onSubjectDataFail(message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (callback != null)
						{
							callback.onSubjectDataFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 加载本地首页缓存数据
	 * 
	 * @return
	 */
	public static SubjectInfo getAppLocalSubjectData(Context context)
	{
		CacheInfo cacheInfo = DbService.getInstance().getCacheData(CacheInfo.TYPE_HOME_SUBJECT);
		SubjectInfo subjectInfo = null;
		if (null != cacheInfo)
		{
			try
			{
				subjectInfo = (SubjectInfo) JsonParser.parseObject(cacheInfo.getResponse(), SubjectInfo.class);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return subjectInfo;
		}
		return null;
	}

	/**
	 * 缓存专题数据
	 */
	private static void cacheSubjectData(String response)
	{
		CacheInfo cacheInfo = new CacheInfo();
		cacheInfo.setResponse(response);
		cacheInfo.setType(CacheInfo.TYPE_HOME_SUBJECT);
		DbService.getInstance().insertCacheData(cacheInfo);
	}

	/**
	 * 清除拉取专题数据请求任务
	 */
	public static void clearGetSubjectDataTask()
	{
		HttpProxy.removeRequestTask("getHttpSubjectData");
	}

	/**
	 * 拉去专题更多文章数据
	 */
	public static void getSubjectMoreArticleList(int currentPage, String specialId, final IonSubjectMoreDataCallback callback)
	{
		String url = ServerUrlConfig.SERVER_URL + "/specialtopic/stopicArticle?page=" + currentPage + "&specialId=" + specialId;
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("getSubjectMoreArticleList", Request.Method.GET, url, null,
				new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int result, String message, JSONObject jsonObject)
					{
						if (StatusCode.SUCCESS == result)
						{
							String resultStr = jsonObject.optString("result");
							SubjectMoreArticle moreArticle = (SubjectMoreArticle) JsonParser.parseObject(resultStr, SubjectMoreArticle.class);
							// cachePeopleConcernDatas(jsonObject, concernInfo);
							if (null != callback)
							{
								callback.onSubjectMoreDataSuccess(moreArticle);
							}
						}
						else
						{
							if (null != callback)
							{
								callback.onSubjectMoreDataFail(message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (null != callback)
						{
							callback.onSubjectMoreDataFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 清除专题更多文章列表请求任务
	 */
	public static void clearGetSubjectMoreArticleList()
	{
		HttpProxy.removeRequestTask("getSubjectMoreArticleList");
	}

	/**
	 * 专题数据回调
	 */
	public interface IonSubjectDataCallback
	{
		// 数据返回成功
		public void onSubjectDataSuccess(SubjectInfo subjectInfo);

		// 数据返回失败
		public void onSubjectDataFail(String errorMessage);
	}

	/**
	 * 专题数据回调
	 */
	public interface IonSubjectMoreDataCallback
	{
		// 数据返回成功
		public void onSubjectMoreDataSuccess(SubjectMoreArticle moreArticle);

		// 数据返回失败
		public void onSubjectMoreDataFail(String errorMessage);
	}

}
