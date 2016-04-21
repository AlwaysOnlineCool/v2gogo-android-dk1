package com.v2gogo.project.manager.profile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.v2gogo.project.domain.home.theme.UploadErrorInfo;
import com.v2gogo.project.domain.profile.ProfileScoreInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.manager.HttpJsonObjectRequest;
import com.v2gogo.project.manager.HttpJsonObjectRequest.IOnDataReceiveMessageCallback;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.utils.StatusCode;
import com.v2gogo.project.utils.common.MD5Util;
import com.v2gogo.project.utils.http.Constants;
import com.v2gogo.project.utils.http.HttpProxy;
import com.v2gogo.project.utils.parse.JsonParser;
import com.v2gogo.project.utils.qiniu.QiNiuUploadManager;
import com.v2gogo.project.utils.qiniu.QiNiuUploadManager.IonUploadCallback;
import com.ypy.eventbus.EventBus;

/**
 * 功能：评分评论管理器
 * 
 * @ahthor：黄荣星
 * @date:2015-12-6
 * @version::V1.0
 */
public class ProfileScoreUploadPhotoManager
{

	/**
	 * 上传评论照片
	 */
	public static void uploadScoreCommentPhoto(String scoreId, String token, final File photo)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("x:scoreId", scoreId);
		QiNiuUploadManager.uploadScoreCommentPhoto(photo, "score", token, params, new IonUploadCallback()
		{
			@Override
			public void onUploadProgress(String key, double progress)
			{
			}

			@Override
			public void onUploadFail(int code, String errorMessage)
			{
				UploadErrorInfo uploadErrorInfo = new UploadErrorInfo();
				uploadErrorInfo.setCode(code);
				uploadErrorInfo.setMessage(errorMessage);
				EventBus.getDefault().post(uploadErrorInfo);
			}

			@Override
			public void onUploadCallback(int statusCode, String key, JSONObject jsonObject)
			{
				if (QiNiuUploadManager.UPLOAD_STATUS_CODE_SUCCESS == statusCode)
				{
					if (jsonObject != null)
					{
						int code = jsonObject.optInt(Constants.CODE);
						String message = jsonObject.optString(Constants.MESSAGE);
						if (StatusCode.SUCCESS == code)
						{
							EventBus.getDefault().post(key);
							// JSONObject object = jsonObject.optJSONObject("result");
							// if (null != object)
							// {
							// JSONObject jsonObject2 = object.optJSONObject("comment");
							// if (null != jsonObject2)
							// {
							// CommentInfo commentInfo = (CommentInfo)
							// JsonParser.parseObject(jsonObject2.toString(), CommentInfo.class);
							// EventBus.getDefault().post(commentInfo);
							// }
							// }
						}
						else
						{
							UploadErrorInfo uploadErrorInfo = new UploadErrorInfo();
							uploadErrorInfo.setCode(code);
							uploadErrorInfo.setMessage(message);
							EventBus.getDefault().post(uploadErrorInfo);
						}
					}
					else
					{
						UploadErrorInfo uploadErrorInfo = new UploadErrorInfo();
						uploadErrorInfo.setCode(statusCode);
						uploadErrorInfo.setMessage(null);
						EventBus.getDefault().post(uploadErrorInfo);
					}
				}
				else
				{
					UploadErrorInfo uploadErrorInfo = new UploadErrorInfo();
					uploadErrorInfo.setCode(statusCode);
					uploadErrorInfo.setMessage(null);
					EventBus.getDefault().post(uploadErrorInfo);
				}
			}
		});
	}

	/**
	 * method desc：获取评分
	 * 
	 * @param callback
	 */
	public static void getHttpScore(final IonProfileScoreCallback callback)
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
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("getHttpScore", Request.Method.POST, ServerUrlConfig.SERVER_URL
				+ "/score/list", params, new IOnDataReceiveMessageCallback()
		{
			@Override
			public void onSuccess(int code, String message, JSONObject response)
			{
				if (StatusCode.SUCCESS == code)
				{
					JSONObject jsonObject = response.optJSONObject("result");
					if (jsonObject != null)
					{
						ProfileScoreInfo scoreInfo = (ProfileScoreInfo) JsonParser.parseObject(jsonObject.toString(), ProfileScoreInfo.class);
						if (null != callback)
						{
							callback.onProfileScoreSuccess(scoreInfo);
						}
					}
					else
					{
						if (null != callback)
						{
							callback.onProfileScoreFail(message);
						}
					}
				}
				else
				{
					if (null != callback)
					{
						callback.onProfileScoreFail(message);
					}
				}
			}

			@Override
			public void onError(String errorMessage)
			{
				if (null != callback)
				{
					callback.onProfileScoreFail(errorMessage);
				}
			}
		});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * method desc：用户对奖品评分且评论
	 */
	public static void userScoreToPrize(String scoreId, String targetId, String content, String imgs, int commentType,
			final IonProfileUserScoreToPrizeCallback callback)
	{

		Map<String, String> params = new HashMap<String, String>();
		params.put("scoreId", scoreId);
		params.put("targetId", targetId);
		params.put("content", content);// 评论内容
		params.put("type", "0");// int 评论的type 0：对文章、奖品、商品评论，1：对评论 进行评论
		params.put("img", imgs);// String 图片路径，多张图片以逗号","分割
		params.put("commentType", commentType + "");// String 图片路径，多张图片以逗号","分割
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
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("userScoreToPrize", Request.Method.POST,
				ServerUrlConfig.SERVER_URL + "/score/score", params, new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int code, String message, JSONObject response)
					{
						if (StatusCode.SUCCESS == code)
						{
							if (null != callback)
							{
								callback.onProfileUserScoreToPrizeSuccess(message);
							}
						}
						else if (null != callback)
						{
							callback.onProfileUserScoreToPrizeFail(message);
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (null != callback)
						{
							callback.onProfileUserScoreToPrizeFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * method desc：清除获取评分选项网络请求
	 */
	public static void clearGetHttpScoreTask()
	{
		HttpProxy.removeRequestTask("getHttpScore");
	}

	/**
	 * method desc：：清除用户对奖品评分的网络请求
	 */
	public static void clearUserScoreToPrizeTask()
	{
		HttpProxy.removeRequestTask("userScoreToPrize");
	}

	/**
	 * 获取评分回调
	 * 
	 * @author houjun
	 */
	public interface IonProfileScoreCallback
	{
		public void onProfileScoreSuccess(ProfileScoreInfo info);

		public void onProfileScoreFail(String errormessage);
	}

	/**
	 * 获取评分回调
	 * 
	 * @author houjun
	 */
	public interface IonProfileUserScoreToPrizeCallback
	{
		public void onProfileUserScoreToPrizeSuccess(String message);

		public void onProfileUserScoreToPrizeFail(String errormessage);
	}

}
