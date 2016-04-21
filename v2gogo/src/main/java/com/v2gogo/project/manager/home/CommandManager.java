package com.v2gogo.project.manager.home;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.manager.HttpJsonObjectRequest;
import com.v2gogo.project.manager.HttpJsonObjectRequest.IOnDataReceiveMessageCallback;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.utils.StatusCode;
import com.v2gogo.project.utils.common.MD5Util;
import com.v2gogo.project.utils.http.HttpProxy;

/**
 * 点赞管理器
 * 
 * @author houjun
 */
public class CommandManager
{

	/**
	 * 评论点赞
	 */
	public static void commandComment(final String infoId, final String id, final IoncommandCommentCallback callback)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("cid", id);
		params.put("infoid", infoId == null ? "" : infoId);
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
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("commandComment", Request.Method.PUT,
				ServerUrlConfig.SERVER_URL + "/usercommentsapp/addcommentpraise", params, new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int result,String message, JSONObject jsonObject)
					{
						if (StatusCode.SUCCESS == result)
						{
							if (null != callback)
							{
								callback.oncommandCommentSuccess(id);
							}
						}
						else 
						{
							if (null != callback)
							{
								callback.oncommandCommentFail(message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (null != callback)
						{
							callback.oncommandCommentFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 清除赞评论的请求任务
	 */
	public static void clearCommandCommentTask()
	{
		HttpProxy.removeRequestTask("commandComment");
	}

	/**
	 * 评论点赞数据回调
	 * 
	 * @author houjun
	 */
	public interface IoncommandCommentCallback
	{
		public void oncommandCommentSuccess(String id);

		public void oncommandCommentFail(String errorMessage);
	}

}
