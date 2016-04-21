package com.v2gogo.project.manager.home.theme;

import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.v2gogo.project.domain.home.theme.ThemePhotoListInfo;
import com.v2gogo.project.domain.home.theme.TopicInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.manager.HttpJsonObjectRequest;
import com.v2gogo.project.manager.HttpJsonObjectRequest.IOnDataReceiveMessageCallback;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.utils.StatusCode;
import com.v2gogo.project.utils.http.HttpProxy;
import com.v2gogo.project.utils.parse.JsonParser;

/**
 * 主题图片管理器
 * 
 * @author houjun
 */
public class ThemePhotoManager
{
	public static final int FIRST_PAGE = 1;

	/**
	 * 拉取最新主题图片列表数据
	 * 
	 * @param page
	 *            当前页
	 * @param callback
	 *            数据回调
	 */
	public static void getNewestThemePhotoList(final int page, final String tid, final String timestamp, final ThemePhotoListCallback callback)
	{
		String username = V2GogoApplication.getMasterLoginState() ? V2GogoApplication.getCurrentMatser().getUsername() : "";
		String url = ServerUrlConfig.SERVER_URL+"/photo/getNewestPhotoList?timestamp=" + timestamp + "&page=" + page + "&username=" + username + "&tId=" + tid;
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("getNewestThemePhotoList", Request.Method.GET, url, null,
				new IOnDataReceiveMessageCallback()
				{

					@Override
					public void onSuccess(int code, String message, JSONObject response)
					{
						if (StatusCode.SUCCESS == code)
						{
							ThemePhotoListInfo themePhotoListInfo = (ThemePhotoListInfo) JsonParser.parseObject(response.toString(), ThemePhotoListInfo.class);
							if (null != callback)
							{
								callback.themePhotoListSuccess(themePhotoListInfo);
							}
						}
						else
						{
							if (null != callback)
							{
								callback.themePhotoListFail(code, message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (null != callback)
						{
							callback.themePhotoListFail(StatusCode.FAIL, errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 拉取最新主题图片列表数据
	 * 
	 * @param page
	 *            当前页
	 * @param callback
	 *            数据回调
	 */
	public static void getHotestThemePhotoList(final int page, final String tid, final ThemePhotoListCallback callback)
	{
		String username = V2GogoApplication.getMasterLoginState() ? V2GogoApplication.getCurrentMatser().getUsername() : "";
		String url = ServerUrlConfig.SERVER_URL+"/photo/getHotestPhotoList?page=" + page + "&username=" + username + "&tId=" + tid;
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("getHotestThemePhotoList", Request.Method.GET, url, null,
				new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int code, String message, JSONObject response)
					{
						if (StatusCode.SUCCESS == code)
						{
							ThemePhotoListInfo themePhotoListInfo = (ThemePhotoListInfo) JsonParser.parseObject(response.toString(), ThemePhotoListInfo.class);
							if (null != callback)
							{
								callback.themePhotoListSuccess(themePhotoListInfo);
							}
						}
						else
						{
							if (null != callback)
							{
								callback.themePhotoListFail(code, message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (null != callback)
						{
							callback.themePhotoListFail(StatusCode.FAIL, errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 获取主题相关的信息
	 * 
	 * @param tid
	 */
	public static void loadThemePhotoTopic(String tid, final IonThemeTopicCallback callback)
	{
		String url = ServerUrlConfig.SERVER_URL+"/topic/getPhotoTopic?tId=" + tid;
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("loadThemePhotoTopic", Request.Method.GET, url, null,
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
								jsonObject = jsonObject.optJSONObject("topic");
								if (jsonObject != null)
								{
									TopicInfo topicInfo = (TopicInfo) JsonParser.parseObject(jsonObject.toString(), TopicInfo.class);
									if (callback != null)
									{
										callback.onThemeTopicSuccess(topicInfo);
									}
								}
							}
						}
						else
						{
							if (callback != null)
							{
								callback.onThemeTopicFail(code, message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (callback != null)
						{
							callback.onThemeTopicFail(StatusCode.FAIL, errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 清除最新拉取主题图片列表数据任务
	 */
	public static void clearNewestThemePhotoListTask()
	{
		HttpProxy.removeRequestTask("getNewestThemePhotoList");
	}

	/**
	 * 清除最热拉取主题图片列表数据任务
	 */
	public static void clearHotestThemePhotoListTask()
	{
		HttpProxy.removeRequestTask("getHotestThemePhotoList");
	}

	/**
	 * 清除主题信息任务
	 */
	public static void clearoadThemePhotoTopic()
	{
		HttpProxy.removeRequestTask("loadThemePhotoTopic");
	}

	public interface ThemePhotoListCallback
	{
		public void themePhotoListSuccess(ThemePhotoListInfo themePhotoListInfo);

		public void themePhotoListFail(int code, String message);
	}

	public interface IonThemeTopicCallback
	{
		public void onThemeTopicSuccess(TopicInfo topicInfo);

		public void onThemeTopicFail(int code, String message);
	}
}
