package com.v2gogo.project.manager.upload;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.v2gogo.project.domain.home.CommentInfo;
import com.v2gogo.project.domain.home.theme.UploadErrorInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.utils.StatusCode;
import com.v2gogo.project.utils.http.Constants;
import com.v2gogo.project.utils.parse.JsonParser;
import com.v2gogo.project.utils.qiniu.QiNiuUploadManager;
import com.v2gogo.project.utils.qiniu.QiNiuUploadManager.IonUploadCallback;
import com.ypy.eventbus.EventBus;

/**
 * 评论上传管理器
 * 
 * @author houjun
 */
public class CommentUploadPhotoManager
{

	/**
	 * 上传评论照片
	 */
	public static void uploadCommentPhoto(String srcId, String infoId, int type, String content, String srccont, final File photo)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("x:srcId", srcId);
		params.put("x:type", type + "");
		params.put("x:infoId", infoId);
		params.put("x:content", content);
		params.put("x:srcCont", srccont);
		params.put("x:username", V2GogoApplication.getMasterLoginState() ? V2GogoApplication.getCurrentMatser().getUsername() : "");
		QiNiuUploadManager.uploadCommentPhoto(photo, "comment", params, new IonUploadCallback()
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
							JSONObject object = jsonObject.optJSONObject("result");
							if (null != object)
							{
								JSONObject jsonObject2 = object.optJSONObject("comment");
								if (null != jsonObject2)
								{
									CommentInfo commentInfo = (CommentInfo) JsonParser.parseObject(jsonObject2.toString(), CommentInfo.class);
									EventBus.getDefault().post(commentInfo);
								}
							}
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
}
