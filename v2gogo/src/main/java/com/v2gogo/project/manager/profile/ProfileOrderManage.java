package com.v2gogo.project.manager.profile;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.v2gogo.project.domain.profile.ProfileOrderListInfo;
import com.v2gogo.project.domain.shop.OrderInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.manager.HttpJsonObjectRequest;
import com.v2gogo.project.manager.HttpJsonObjectRequest.IOnDataReceiveMessageCallback;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.utils.StatusCode;
import com.v2gogo.project.utils.common.MD5Util;
import com.v2gogo.project.utils.http.HttpProxy;
import com.v2gogo.project.utils.parse.JsonParser;

/**
 * 个人订单管理器
 * 
 * @author houjun
 */
public class ProfileOrderManage
{

	public static final int FIRST_PAGE = 1;

	/**
	 * 得到个人订单信息
	 * 
	 * @param context
	 * @param page
	 * @param callback
	 */
	public static void getProfileOrderList(int page, final IonProfileOrderListCallback callback)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("pageNo", page + "");
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
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("getProfileOrderList", Request.Method.POST,
				ServerUrlConfig.SERVER_URL + "/orderinfo/list", params, new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int result, String message, JSONObject response)
					{
						if (StatusCode.SUCCESS == result)
						{
							ProfileOrderListInfo profileOrderListInfo = (ProfileOrderListInfo) JsonParser.parseObject(response.toString(),
									ProfileOrderListInfo.class);
							if (callback != null)
							{
								callback.onProfileOrderListSuccess(profileOrderListInfo);
							}
						}
						else
						{
							if (callback != null)
							{
								callback.onProfileOrderFail(message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (null != callback)
						{
							callback.onProfileOrderFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 清除个人订单列表数据任务
	 */
	public static void clearProfileOrderListTask()
	{
		HttpProxy.removeRequestTask("getProfileOrderList");
	}

	public static void deleteProilfeOrder(final OrderInfo orderInfo, final IonDeleteProfileOrderCallback callback)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("oid", orderInfo.getId());
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
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("deleteProilfeOrder", Request.Method.POST,
				ServerUrlConfig.SERVER_URL + "/orderinfo/removeorder", params, new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int result, String message, JSONObject response)
					{
						if (StatusCode.SUCCESS == result)
						{
							if (callback != null)
							{
								callback.onDeleteProfileOrderSuccess(orderInfo);
							}
						}
						else
						{
							callback.onDeleteProfileOrderFail(message);
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (callback != null)
						{
							callback.onDeleteProfileOrderFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	public static void getReceivePeopleInfo(final Context context)
	{

	}

	/**
	 * 删除订单数据回调
	 * 
	 * @author
	 */
	public interface IonDeleteProfileOrderCallback
	{

		public void onDeleteProfileOrderSuccess(OrderInfo orderInfo);

		public void onDeleteProfileOrderFail(String errorMessage);
	}

	/**
	 * 订单列表数据回调
	 * 
	 * @author houjun
	 */
	public interface IonProfileOrderListCallback
	{
		public void onProfileOrderListSuccess(ProfileOrderListInfo profileOrderListInfo);

		public void onProfileOrderFail(String errorMessage);
	}
}
