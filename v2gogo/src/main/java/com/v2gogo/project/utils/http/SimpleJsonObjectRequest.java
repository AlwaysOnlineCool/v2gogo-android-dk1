package com.v2gogo.project.utils.http;

import java.util.Map;

import org.json.JSONObject;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.v2gogo.project.utils.common.LogUtil;

/**
 * 自定义JsonObjectRequest
 * 
 * @author houjun
 */
public class SimpleJsonObjectRequest extends JsonObjectRequest
{

	public SimpleJsonObjectRequest(int method, String url, Map<String, String> params, Listener<JSONObject> listener, ErrorListener errorListener)
	{
		super(method, url, (params == null) ? null : createBody(params), listener, errorListener);
	}

	/**
	 * 构造参数数据
	 * 
	 * @param params
	 * @return
	 */
	private static String createBody(Map<String, String> params)
	{
		String result = null;
		if (null != params && params.size() > 0)
		{
			StringBuilder stringBuilder = new StringBuilder();
			for (Map.Entry<String, String> entry : params.entrySet())
			{
				stringBuilder.append(entry.getKey() + "=" + entry.getValue() + "&");
			}
			result = stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1);
		}
		LogUtil.d("传入的请求参数为->>>>>>>>>>>>>>>>>>" + result);
		return result;
	}

	@Override
	public String getBodyContentType()
	{
		return "application/x-www-form-urlencoded; charset=" + getParamsEncoding();
	}
}
