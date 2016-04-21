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
 * 个人管理器
 * 
 * @author houjun
 */
public class ProfileManager
{

	/**
	 * 是否有最新的提示
	 * 
	 * @param context
	 */
	public static void getNewTipNote(final IonNewTipNoteCallBack callback)
	{
		Map<String, String> params = new HashMap<String, String>();
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
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("getNewTipNote", Request.Method.POST,
				ServerUrlConfig.SERVER_URL + "/usercenterapp/getusernewstatus", params, new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int result, String message, JSONObject jsonObject)
					{
						if (StatusCode.SUCCESS == result)
						{
							boolean isMessageNew = jsonObject.optBoolean("newmsg");
							boolean isOrderNew = jsonObject.optBoolean("newporder");
							boolean isPrizeNew = jsonObject.optBoolean("newprize");
							if (null != callback)
							{
								callback.onNewTipNoteSuccess(isPrizeNew, isMessageNew, isOrderNew);
							}
						}
						else
						{
							if (callback != null)
							{
								callback.onNewTipNoteFail(message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (null != callback)
						{
							callback.onNewTipNoteFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * @author houjun
	 */
	public interface IonNewTipNoteCallBack
	{
		public void onNewTipNoteSuccess(boolean isNewPrize, boolean isNewMessage, boolean isNewOrder);

		public void onNewTipNoteFail(String errorMessage);
	}

}
