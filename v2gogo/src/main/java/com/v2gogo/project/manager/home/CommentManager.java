package com.v2gogo.project.manager.home;

import java.io.File;
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
import com.v2gogo.project.manager.upload.CommentUploadPhotoManager;
import com.v2gogo.project.utils.StatusCode;
import com.v2gogo.project.utils.common.MD5Util;
import com.v2gogo.project.utils.http.HttpProxy;
import com.v2gogo.project.utils.parse.JsonParser;

/**
 * 评论管理器
 * 
 * @author houjun
 */
public class CommentManager
{

	public static final int FIRST_PAGE = 1;

	public static final String HOT_COMMENT_LIST_KEYWORD = "hot_comment_list_keyword";
	public static final String NEWEST_COMMENT_LIST_KEYWORD = "newest_comment_list_keyword";

	/**
	 * 获取热门评论列表
	 */
	public static void getHotCommentList(String srcId, int currentPage, final IonCommentListCallback callback)
	{
		String username = V2GogoApplication.getMasterLoginState() ? V2GogoApplication.getCurrentMatser().getUsername() : "";
		String url = ServerUrlConfig.SERVER_URL + "/vcomments/gethotvcomments?id=" + srcId + "&page=" + currentPage + "&username=" + username;
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("getHotCommentList", Request.Method.GET, url, null,
				new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int result, String message, JSONObject jsonObject)
					{
						dealCommentListDatas(callback, HOT_COMMENT_LIST_KEYWORD, message, result, jsonObject);
					}

					@Override
					public void onError(String errorMessage)
					{
						if (null != callback)
						{
							callback.onCommentListFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 获取最新评论列表
	 */
	public static void getNewestCommentList(String srcId, int currentPage, final IonCommentListCallback callback)
	{
		String username = V2GogoApplication.getMasterLoginState() ? V2GogoApplication.getCurrentMatser().getUsername() : "";
		String url = ServerUrlConfig.SERVER_URL + "/vcomments/getnewvcomments?id=" + srcId + "&page=" + currentPage + "&username=" + username;
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("getNewestCommentList", Request.Method.GET, url, null,
				new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int result, String message, JSONObject jsonObject)
					{
						dealCommentListDatas(callback, NEWEST_COMMENT_LIST_KEYWORD, message, result, jsonObject);
					}

					@Override
					public void onError(String errorMessage)
					{
						if (null != callback)
						{
							callback.onCommentListFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	// 发布有图片评论
	public static void publishNewCommentWithImage(String srcId, String infoId, int type, int srcType, String content, String srccont, final File photo)
	{
		content = content.replace(";", "；");
		content = content.replace(":", "：");
		CommentUploadPhotoManager.uploadCommentPhoto(srcId, infoId, type, content, srccont, photo);
	}

	// 发布没有图片评论
	public static void publishNewCommentWithNoImage(String srcId, String infoId, int type, int srcType, String content, String srccont,
			final IonNewCommentCallback callback)
	{
		
		content = content.replace(";", "；");
		content = content.replace(":", "：");
		Map<String, String> params = new HashMap<String, String>();
		content =buildParams(srcId, infoId, type,content, srccont, params);
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("publishNewCommentWithNoImage", Request.Method.POST,
				ServerUrlConfig.SERVER_URL + "/vcomments/addComment", params, new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int result, String message, JSONObject jsonObject)
					{
						if (StatusCode.SUCCESS == result)
						{
							JSONObject object = jsonObject.optJSONObject("result");
							if (null != object)
							{
								JSONObject jsonObject2 = object.optJSONObject("comment");
								if (null != jsonObject2)
								{
									CommentInfo commentInfo = (CommentInfo) JsonParser.parseObject(jsonObject2.toString(), CommentInfo.class);
									if (null != callback)
									{
										callback.onNewCommentSuccess(commentInfo);
									}
								}
								else
								{
									if (null != callback)
									{
										callback.onNewCommentFail(null);
									}
								}
							}
							else
							{
								if (null != callback)
								{
									callback.onNewCommentFail(null);
								}
							}
						}
						else
						{
							if (callback != null)
							{
								callback.onNewCommentFail(message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (null != callback)
						{
							callback.onNewCommentFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 建立请求参数
	 */
	private static String buildParams(String srcId, String infoId, int type,String content, String srccont, Map<String, String> params)
	{
		params.put("srcId", srcId);
		params.put("type", type + "");
		params.put("infoId", infoId);
		params.put("content", content);
		params.put("srcCont", srccont);
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
		content = content.replace(":", "：").replace(";", "；").replace("%", "％").replace("*", "＊").replace("?", "？");
		return content;
	}

	/**
	 * 处理评论列表数据
	 */
	private static void dealCommentListDatas(final IonCommentListCallback callback, String tag, String message, int result, JSONObject jsonObject)
	{
		if (StatusCode.SUCCESS == result)
		{
			CommentListInfo commentListInfo = (CommentListInfo) JsonParser.parseObject(jsonObject.toString(), CommentListInfo.class);
			parseCommentReplyDatas(commentListInfo);
			commentListInfo.setType(tag);
			if (null != callback)
			{
				callback.onCommentListSuccess(commentListInfo);
			}
		}
		else
		{
			if (null != callback)
			{
				callback.onCommentListFail(message);
			}
		}
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
	 * 清除评论列表的请求任务
	 */
	public static void clearGetCommentListTask()
	{
		HttpProxy.removeRequestTask("getCommentList");
	}

	/**
	 * 删除评论的任务
	 */
	public static void clearNewCommentTask()
	{
		HttpProxy.removeRequestTask("publishNewCommentWithNoImage");
	}

	/**
	 * 评论列表数据回调
	 * 
	 * @author houjun
	 */
	public interface IonCommentListCallback
	{
		public void onCommentListSuccess(CommentListInfo commentListInfo);

		public void onCommentListFail(String errorMessage);
	}

	/**
	 * 发布评论数据回调
	 * 
	 * @author houjun
	 */
	public interface IonNewCommentCallback
	{
		public void onNewCommentSuccess(CommentInfo commentInfo);

		public void onNewCommentFail(String errorMessage);
	}
}
