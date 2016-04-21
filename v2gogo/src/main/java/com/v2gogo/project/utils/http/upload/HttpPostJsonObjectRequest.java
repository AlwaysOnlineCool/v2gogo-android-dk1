package com.v2gogo.project.utils.http.upload;

import org.json.JSONException;
import org.json.JSONObject;

import com.v2gogo.project.utils.StatusCode;
import com.v2gogo.project.utils.common.LogUtil;
import com.v2gogo.project.utils.http.Constants;
import com.v2gogo.project.utils.http.upload.RequestManager.RequestListener;

public class HttpPostJsonObjectRequest
{

	public static void postJsonHttpRequest(String url, RequestMap requestMap, final IonPostJsonHttpRequestCallback callback)
	{
		RequestManager.getInstance().post(url, requestMap, new RequestListener()
		{
			@Override
			public void onSuccess(String response, String url, int actionId)
			{
				LogUtil.d("postJsonHttpRequest->onSuccess->>>>>>>>>" + response);
				try
				{
					JSONObject jsonObject = new JSONObject(response);
					int result = jsonObject.optInt(Constants.CODE, StatusCode.FAIL);
					String message = jsonObject.optString(Constants.MESSAGE);
					if (null != callback)
					{
						callback.IonPostJsonHttpRequestSuccess(result, message, jsonObject);
					}
				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}
			}

			@Override
			public void onRequest()
			{
			}

			@Override
			public void onError(String errorMsg, String url, int actionId)
			{
			}
		}, 1);
	}

	/**
	 * 数据返回数据接口
	 * 
	 * @author houjun
	 */
	public interface IonPostJsonHttpRequestCallback
	{
		public void IonPostJsonHttpRequestSuccess(int result, String message, JSONObject jsonObject);

		public void IonPostJsonHttpRequestFail(String errorMessage);
	}

}
