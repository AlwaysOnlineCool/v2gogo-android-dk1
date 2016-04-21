package com.v2gogo.project.manager.profile;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.v2gogo.project.domain.profile.CollectionsListInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.manager.HttpJsonObjectRequest;
import com.v2gogo.project.manager.HttpJsonObjectRequest.IOnDataReceiveMessageCallback;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.utils.StatusCode;
import com.v2gogo.project.utils.common.MD5Util;
import com.v2gogo.project.utils.common.SPUtil;
import com.v2gogo.project.utils.http.HttpProxy;
import com.v2gogo.project.utils.parse.JsonParser;

/**
 * 我的收藏管理器
 * 
 * @author houjun
 */
public class ProfileCollectionsManager
{

	public static final int FIRST_PAGE = 1;
	// 文章
	public static final int COLLECTION_TYPE_INFO = 0;
	// 商品
	public static final int COLLECTION_TYPE_PRODUCT = 1;
	// 奖品
	public static final int COLLECTION_TYPE_PRIZE = 2;

	/**
	 * 得到我的收藏列表
	 */
	public static void getProfileCollectionsList(int page, final IonProfileCollectionsListCallback callback)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("page", page + "");
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
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("getProfileCollectionsList", Request.Method.POST,
				ServerUrlConfig.SERVER_URL + "/usercollectionapp/list", params, new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int result, String message, JSONObject jsonObject)
					{
						if (StatusCode.SUCCESS == result)
						{
							CollectionsListInfo collectionsListInfo = (CollectionsListInfo) JsonParser.parseObject(jsonObject.toString(),
									CollectionsListInfo.class);
							if (null != callback)
							{
								callback.onProfileCollectionsListSuccess(collectionsListInfo);
							}
						}
						else
						{
							if (callback != null)
							{
								callback.onProfileCollectionsListFail(message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (null != callback)
						{
							callback.onProfileCollectionsListFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 添加收藏
	 */
	public static void AddCollectionsById(final String id, int type, final IonAddCollectionsCallback callback)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("srcid", id);
		params.put("type", type + "");
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
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("AddCollectionsById", Request.Method.POST,
				ServerUrlConfig.SERVER_URL + "/usercollectionapp/add", params, new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int result, String message, JSONObject jsonObject)
					{
						if (StatusCode.SUCCESS == result)
						{
							String collectionsString = (String) SPUtil.get(V2GogoApplication.sIntance, "collections", "");
							SPUtil.put(V2GogoApplication.sIntance, "collections", collectionsString + (id + ","));
							if (callback != null)
							{
								callback.onAddCollectionsSuccess();
							}
						}
						else
						{
							if (callback != null)
							{
								callback.onAddCollectionsFail(message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (callback != null)
						{
							callback.onAddCollectionsFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 取消收藏
	 */
	public static void cancelCollectionsById(final String id, String username, final IonCancelCollectionsCallback callback)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		params.put("username", username);
		if (V2GogoApplication.getMasterLoginState())
		{
			params.put("token", V2GogoApplication.getCurrentMatser().getToken());
			String signature = MD5Util.getMd5Token(params);
			params.put("signature", signature);
		}
		if (params.containsKey("token"))
		{
			params.remove("token");
		}
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("cancelCollectionsById", Request.Method.POST,
				ServerUrlConfig.SERVER_URL + "/usercollectionapp/delete", params, new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int result, String message, JSONObject jsonObject)
					{
						if (StatusCode.SUCCESS == result)
						{
							String collectionsString = (String) SPUtil.get(V2GogoApplication.sIntance, "collections", "");
							if (collectionsString.contains(id + ","))
							{
								collectionsString = collectionsString.replace((id + ","), "");
								SPUtil.put(V2GogoApplication.sIntance, "collections", collectionsString);
							}
							if (callback != null)
							{
								callback.onCancelCollectionsSuccess(id);
							}
						}
						else
						{
							if (callback != null)
							{
								callback.onCancelCollectionsFail(message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (callback != null)
						{
							callback.onCancelCollectionsFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 清除收藏列表的请求任务
	 */
	public static void clearProfileCollectionsTask()
	{
		HttpProxy.removeRequestTask("getProfileCollectionsList");
	}

	/**
	 * 清除取消收藏的请求任务
	 */
	public static void clearCancleCollectionsTask()
	{
		HttpProxy.removeRequestTask("cancelCollectionsById");
	}

	/**
	 * 清除添加收藏的请求任务
	 */
	public static void clearAddCollectionsTask()
	{
		HttpProxy.removeRequestTask("AddCollectionsById");
	}

	/**
	 * 取消收藏数据回调
	 * 
	 * @author houjun
	 */
	public interface IonCancelCollectionsCallback
	{
		public void onCancelCollectionsSuccess(String id);

		public void onCancelCollectionsFail(String errorMessage);
	}

	/**
	 * 添加收藏数据回调
	 * 
	 * @author houjun
	 */
	public interface IonAddCollectionsCallback
	{
		public void onAddCollectionsSuccess();

		public void onAddCollectionsFail(String errorMessage);
	}

	/**
	 * 我的收藏列表数据返回回调
	 * 
	 * @author houjun
	 */
	public interface IonProfileCollectionsListCallback
	{
		public void onProfileCollectionsListSuccess(CollectionsListInfo collectionsListInfo);

		public void onProfileCollectionsListFail(String errorMessage);
	}
}
