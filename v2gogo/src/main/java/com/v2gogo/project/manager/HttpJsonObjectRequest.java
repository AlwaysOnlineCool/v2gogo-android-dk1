package com.v2gogo.project.manager;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.v2gogo.project.R;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.utils.StatusCode;
import com.v2gogo.project.utils.common.LogUtil;
import com.v2gogo.project.utils.http.Constants;
import com.v2gogo.project.utils.http.SimpleJsonObjectRequest;
import com.v2gogo.project.utils.http.VolleyErrorStringBuilder;

public class HttpJsonObjectRequest
{
	private static final int TIME_OUT = 5 * 1000;

	/**
	 * 创建一个请求队列
	 * 
	 * @param context
	 *            上下文环境
	 * @param method
	 *            请求方法
	 * @param url
	 *            请求url
	 * @param params
	 *            post时，的请求参数
	 * @param callback
	 *            数据回调
	 * @return
	 */
	public static JsonObjectRequest createJsonObjectHttpRequest(final String tag, int method, String url, Map<String, String> params,
			final IOnDataReceiveMessageCallback callback)
	{
		if (params == null)
		{
			params = new HashMap<String, String>();
		}
		final JsonObjectRequest jsonObjectRequest = new SimpleJsonObjectRequest(method, url, params, new Listener<JSONObject>()
		{
			@Override
			public void onResponse(JSONObject response)
			{
				LogUtil.d("返回的数据为---》" + response);
				if (null != response)
				{
					int result = response.optInt(Constants.CODE, StatusCode.FAIL);
					String message = response.optString(Constants.MESSAGE);
					if(StatusCode.CHECK_USER_ERROR == result)
					{
						V2GogoApplication.clearMatserInfo(false);
					}
					if (callback != null)
					{
						callback.onSuccess(result, message, response);
					}
				}
				else
				{
					if (callback != null)
					{
						callback.onError(V2GogoApplication.sIntance.getResources().getString(R.string.network_error_tip));
					}
				}
			}
		}, new ErrorListener()
		{
			@Override
			public void onErrorResponse(VolleyError error)
			{
				LogUtil.d("返回的数据时出错为---》" + error);
				if (callback != null)
				{
					callback.onError(VolleyErrorStringBuilder.createTipStringByError(error));
				}
			}
		});
		jsonObjectRequest.setShouldCache(false);
		jsonObjectRequest.setTag(tag);
		jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		return jsonObjectRequest;
	}


	/**
	 * 数据返回接口
	 * 
	 * @author houjun
	 */
	public static interface IOnDataReceiveMessageCallback
	{
		public void onSuccess(int code, String message, JSONObject response);

		public void onError(String errorMessage);
	}
}
