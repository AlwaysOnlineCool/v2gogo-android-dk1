package com.v2gogo.project.manager.home;

import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.v2gogo.project.domain.LiveVideoInfo;
import com.v2gogo.project.manager.HttpJsonObjectRequest;
import com.v2gogo.project.manager.HttpJsonObjectRequest.IOnDataReceiveMessageCallback;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.utils.StatusCode;
import com.v2gogo.project.utils.http.HttpProxy;
import com.v2gogo.project.utils.parse.JsonParser;

/**
 * 视频直播管理器
 * 
 * @author houjun
 */
public class HomeLiveManager
{

	/**
	 * 得到直播视频列表
	 */
	public static void getHomeLiveList(final IonHomeLiveListCallback callback)
	{
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("getHomeLiveList", Request.Method.GET,
				ServerUrlConfig.SERVER_URL + "/videobroadcast/list", null, new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int code, String message, JSONObject response)
					{
						if (StatusCode.SUCCESS == code)
						{
							JSONObject jsonObject = response.optJSONObject("video");
							if (null != jsonObject)
							{
								LiveVideoInfo liveVideoInfo = (LiveVideoInfo) JsonParser.parseObject(jsonObject.toString(), LiveVideoInfo.class);
								if (callback != null)
								{
									callback.onHomeLiveSuccess(liveVideoInfo);
								}
							}
							else
							{
								if (callback != null)
								{
									callback.onHomeLiveFail(null);
								}
							}
						}
						else
						{
							if (callback != null)
							{
								callback.onHomeLiveFail(message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (callback != null)
						{
							callback.onHomeLiveFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 数据回调
	 * 
	 * @author houjun
	 */
	public interface IonHomeLiveListCallback
	{
		public void onHomeLiveSuccess(LiveVideoInfo liveVideoInfo);

		public void onHomeLiveFail(String errorMessage);
	}

}
