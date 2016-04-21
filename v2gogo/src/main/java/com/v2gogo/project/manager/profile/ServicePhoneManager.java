package com.v2gogo.project.manager.profile;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.manager.HttpJsonObjectRequest;
import com.v2gogo.project.manager.HttpJsonObjectRequest.IOnDataReceiveMessageCallback;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.utils.StatusCode;
import com.v2gogo.project.utils.common.MD5Util;
import com.v2gogo.project.utils.common.SPUtil;
import com.v2gogo.project.utils.http.HttpProxy;

/**
 * 客服电话
 */
public class ServicePhoneManager
{

	public static final String SERVICE_PHONE = "service_phone";

	/**
	 * 获取客户电话
	 */
	public static void getServicePhone(final Context context)
	{
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("getServicePhone", Request.Method.POST,
				ServerUrlConfig.SERVER_URL + "/common/customerPhone", null, new IOnDataReceiveMessageCallback()
				{

					@Override
					public void onSuccess(int result, String message, JSONObject jsonObject)
					{
						if (StatusCode.SUCCESS == result)
						{
							String servicePhoneStr = jsonObject.optString("customerPhone");
							if (!TextUtils.isEmpty(servicePhoneStr))
							{
								SPUtil.put(context, SERVICE_PHONE, servicePhoneStr);
							}
						}
						else
						{
							Log.e("v2gogo", "获取客户电话错误：" + message);
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						Log.e("v2gogo", "获取客户电话错误：" + errorMessage);
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);

	}

}
