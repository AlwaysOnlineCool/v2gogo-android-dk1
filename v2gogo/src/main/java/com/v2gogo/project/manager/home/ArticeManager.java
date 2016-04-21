package com.v2gogo.project.manager.home;

import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.v2gogo.project.domain.home.ArticleDetailsInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.manager.HttpJsonObjectRequest;
import com.v2gogo.project.manager.HttpJsonObjectRequest.IOnDataReceiveMessageCallback;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.utils.StatusCode;
import com.v2gogo.project.utils.common.AppUtil;
import com.v2gogo.project.utils.http.HttpProxy;
import com.v2gogo.project.utils.parse.JsonParser;

/**
 * 文章管理器
 * 
 * @author houjun
 */
public class ArticeManager
{

	/**
	 * 拉取文章详情
	 */
	public static void getArticeDetails(String id, final IonArticeDetailsCallback callback)
	{
		String url = ServerUrlConfig.SERVER_URL + "/info/getinfo?id=" + id + "&version=" + AppUtil.getVersionCode(V2GogoApplication.sIntance);
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("getArticeDetails", Request.Method.GET, url, null,
				new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int result, String message,JSONObject jsonObject)
					{
						if (StatusCode.SUCCESS == result)
						{
							ArticleDetailsInfo articleDetailsInfo = (ArticleDetailsInfo) JsonParser.parseObject(jsonObject.toString(), ArticleDetailsInfo.class);
							if (null != callback)
							{
								callback.onArticeDetailsSuccess(articleDetailsInfo);
							}
						}
						else 
						{
							if (null != callback)
							{
								callback.onArticeDetailsFail(message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (null != callback)
						{
							callback.onArticeDetailsFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 清除文章详情的请求任务
	 */
	public static void clearGetArticeDetailsTask()
	{
		HttpProxy.removeRequestTask("getArticeDetails");
	}

	/**
	 * 文章详情数据回调
	 * 
	 * @author houjun
	 */
	public interface IonArticeDetailsCallback
	{
		public void onArticeDetailsSuccess(ArticleDetailsInfo articleDetailsInfo);

		public void onArticeDetailsFail(String errorString);
	}

}
