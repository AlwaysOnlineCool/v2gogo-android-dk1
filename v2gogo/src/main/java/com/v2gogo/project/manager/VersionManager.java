package com.v2gogo.project.manager;

import org.json.JSONObject;

import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.v2gogo.project.domain.VersionInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.manager.HttpJsonObjectRequest.IOnDataReceiveMessageCallback;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.utils.StatusCode;
import com.v2gogo.project.utils.common.SPUtil;
import com.v2gogo.project.utils.http.Constants;
import com.v2gogo.project.utils.http.HttpProxy;
import com.v2gogo.project.utils.parse.JsonParser;

public class VersionManager
{

	/**
	 * 获取版本信息
	 * 
	 * @param context
	 *            上下文环境
	 * @param callback
	 *            数据回调
	 */
	public static void getServerVersionInfos(final IonServerVersionInfosCallback callback)
	{
		String url = ServerUrlConfig.SERVER_URL + "/welcomeapp/getnowversion?ios=0";
		JsonObjectRequest objectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("getServerVersionInfos", Request.Method.GET, url, null,
				new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int code, String message,JSONObject response)
					{
						if (StatusCode.SUCCESS == code)
						{
							JSONObject jsonObject = response.optJSONObject("version");
							if (jsonObject != null)
							{
								VersionInfo versionInfo = (VersionInfo) JsonParser.parseObject(jsonObject.toString(), VersionInfo.class);
								if (versionInfo != null && !TextUtils.isEmpty(versionInfo.getVername()))
								{
									SPUtil.put(V2GogoApplication.sIntance, Constants.VERSION_NAME, versionInfo.getVername());
								}
								if (callback != null)
								{
									callback.onServerVersionInfosSuccess(versionInfo);
								}
							}
						}
						else
						{
							if (callback != null)
							{
								callback.onServerVersionInfosFail(message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (callback != null)
						{
							callback.onServerVersionInfosFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(objectRequest);
	}

	/**
	 * 清除请求任务
	 */
	public static void clearServerVersionInfosTask()
	{
		HttpProxy.removeRequestTask("getServerVersionInfos");
	}

	/**
	 * 获取版本信息的数据回调
	 * 
	 * @author
	 */
	public interface IonServerVersionInfosCallback
	{
		public void onServerVersionInfosSuccess(VersionInfo versionInfo);

		public void onServerVersionInfosFail(String errorMessage);
	}
}
