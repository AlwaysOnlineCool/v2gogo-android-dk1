package com.v2gogo.project.manager.profile;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.v2gogo.project.domain.home.CommentInfo;
import com.v2gogo.project.domain.home.CommentListInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.manager.HttpJsonObjectRequest;
import com.v2gogo.project.manager.HttpJsonObjectRequest.IOnDataReceiveMessageCallback;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.utils.StatusCode;
import com.v2gogo.project.utils.common.MD5Util;
import com.v2gogo.project.utils.http.HttpProxy;
import com.v2gogo.project.utils.parse.JsonParser;

/**
 * 个人评论管理器
 * 
 * @author houjun
 */
public class ProfileCommentManager
{

	public static final int FIRST_PAGE = 1;

	/**
	 * 拉取个人评论列表
	 */
	public static void getProfileCommentList(int page, final IonProfileCommentListCallback callback)
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
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("getProfileCommentList", Request.Method.POST,
				ServerUrlConfig.SERVER_URL + "/usercommentsapp/list", params, new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int result, String message, JSONObject jsonObject)
					{
						if (StatusCode.SUCCESS == result)
						{
							CommentListInfo commentListInfo = (CommentListInfo) JsonParser.parseObject(jsonObject.toString(), CommentListInfo.class);
							parseCommentReplyDatas(commentListInfo);
							if (null != callback)
							{
								callback.onProfileCommentListSuccess(commentListInfo);
							}
						}
						else
						{
							if (callback != null)
							{
								callback.onProfileCommentListFail(message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (null != callback)
						{
							callback.onProfileCommentListFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 获取回复我的评论列表数据
	 * 
	 * @param page
	 * @param callback
	 */
	public static void getReplyProfileCommentList(int page, final IonProfileCommentListCallback callback)
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
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("getReplyProfileCommentList", Request.Method.POST,
				ServerUrlConfig.SERVER_URL + "/usercommentsapp/outherCommentlist", params, new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int result, String message, JSONObject jsonObject)
					{
						if (StatusCode.SUCCESS == result)
						{
							CommentListInfo commentListInfo = (CommentListInfo) JsonParser.parseObject(jsonObject.toString(), CommentListInfo.class);
							parseCommentReplyDatas(commentListInfo);
							if (null != callback)
							{
								callback.onProfileCommentListSuccess(commentListInfo);
							}
						}
						else
						{
							if (callback != null)
							{
								callback.onProfileCommentListFail(message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (null != callback)
						{
							callback.onProfileCommentListFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 删除评论
	 */
	public static void deleteProfileCommentById(final CommentInfo commentInfo, String username, final IonProfileCommentDeleteCallback callback)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", username);
		params.put("cid", commentInfo.getId());
		params.put("infoid", commentInfo.getInfoId());
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
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("deleteProfileCommentById", Request.Method.POST,
				ServerUrlConfig.SERVER_URL + "/usercommentsapp/delete", params, new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int result, String message, JSONObject jsonObject)
					{
						if (StatusCode.SUCCESS == result)
						{
							if (null != callback)
							{
								callback.onProfileCommentDeleteSuccess(commentInfo);
							}
						}
						else
						{
							if (null != callback)
							{
								callback.onProfileCommentDeleteFail(message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (null != callback)
						{
							callback.onProfileCommentDeleteFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 清除我的评论列表的请求任务
	 */
	public static void clearGetProfileCommentList()
	{
		HttpProxy.removeRequestTask("getProfileCommentList");
	}

	/**
	 * 清除删除评论的请求任务
	 */
	public static void clearDeleteProfileCommentById()
	{
		HttpProxy.removeRequestTask("deleteProfileCommentById");
	}

	/**
	 * 解析数据
	 * 
	 * @param commentListInfo
	 */
	private static void parseCommentReplyDatas(CommentListInfo commentListInfo)
	{
		if (null != commentListInfo && null != commentListInfo.getCommentInfos())
		{
			for (CommentInfo commentInfo : commentListInfo.getCommentInfos())
			{
				if (null != commentInfo)
				{
					commentInfo.parseCommentReplyData();
				}
			}
		}
	}

	/**
	 * 评论列表数据回调
	 * 
	 * @author houjun
	 */
	public interface IonProfileCommentListCallback
	{
		public void onProfileCommentListSuccess(CommentListInfo commentListInfo);

		public void onProfileCommentListFail(String errorMessage);
	}

	/**
	 * 评论删除数据回调
	 * 
	 * @author houjun
	 */
	public interface IonProfileCommentDeleteCallback
	{
		public void onProfileCommentDeleteSuccess(CommentInfo commentInfo);

		public void onProfileCommentDeleteFail(String errorMessage);
	}

}
