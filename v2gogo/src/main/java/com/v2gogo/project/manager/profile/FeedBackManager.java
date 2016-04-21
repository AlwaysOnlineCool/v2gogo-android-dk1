package com.v2gogo.project.manager.profile;

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
 * 意见反馈管理器
 * 
 * @author houjun
 */
public class FeedBackManager
{

	/**
	 * 意见反馈
	 */
	public static void uploadFeedback(String content, final IonuploadFeedbackCallback callback)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("content", content);
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
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("uploadFeedback", Request.Method.POST,
				ServerUrlConfig.SERVER_URL + "/vuserideaapp/adduseridea", params, new IOnDataReceiveMessageCallback()
				{

					@Override
					public void onSuccess(int result, String message, JSONObject jsonObject)
					{
						if (StatusCode.SUCCESS == result)
						{
							if (null != callback)
							{
								callback.onuploadFeedbackSuccess();
							}
						}
						else
						{
							if (callback != null)
							{
								callback.onuploadFeedbackFail(message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (null != callback)
						{
							callback.onuploadFeedbackFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);

	}

	public static void clearUploadFeedbackTask()
	{
		HttpProxy.removeRequestTask("uploadFeedback");
	}

	public interface IonuploadFeedbackCallback
	{
		public void onuploadFeedbackSuccess();

		public void onuploadFeedbackFail(String errorMessage);
	}

}
