package com.v2gogo.project.utils.http;

import com.android.volley.toolbox.JsonObjectRequest;
import com.v2gogo.project.main.V2GogoApplication;

/**
 * http请求工具类
 * 
 * @author houjun
 */
public class HttpProxy
{

	/**
	 * 发起返回jsonObject请求
	 * 
	 * @param jsonObjectRequest
	 */
	public static void luanchJsonObjectRequest(JsonObjectRequest jsonObjectRequest)
	{
		V2GogoApplication.getRequestQueue().add(jsonObjectRequest);
	}

	/**
	 * 取消请求任务
	 * 
	 * @param tag
	 */
	public static void removeRequestTask(final String tag)
	{
		V2GogoApplication.getRequestQueue().cancelAll(tag);
	}
}
