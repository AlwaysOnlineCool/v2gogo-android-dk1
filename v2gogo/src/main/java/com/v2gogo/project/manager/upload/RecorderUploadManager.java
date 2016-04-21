/**    
 * @{#} RecorderUploadManager.java Create on 2015-12-16 下午11:06:07    
 *    
 * Copyright (c) 2013 by BlueSky.    
 *
 *    
 * @author <a href="1084986314@qq.com">BlueSky</a>   
 * @version 1.0    
 */
package com.v2gogo.project.manager.upload;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.v2gogo.project.domain.home.theme.UploadErrorInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.utils.StatusCode;
import com.v2gogo.project.utils.http.Constants;
import com.v2gogo.project.utils.qiniu.QiNiuUploadManager;
import com.v2gogo.project.utils.qiniu.QiNiuUploadManager.IonUploadCallback;
import com.ypy.eventbus.EventBus;

/**
 * 功能：录音上传管理类
 * 
 * @ahthor：黄荣星
 * @date:2015-12-16
 * @version::V1.0
 */
public class RecorderUploadManager
{

	/**
	 * method desc：上传录音
	 * 
	 * @param scoreId
	 * @param token
	 * @param photo
	 */
	public static void uploadRecorderVoice(String tId, String resourceURL, String voiceLength, final File photo)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("tId", tId);
		params.put("x:resourceURL", resourceURL);
		params.put("x:voiceLength", voiceLength);
		if (V2GogoApplication.getMasterLoginState())
		{
			params.put("x:username", V2GogoApplication.getCurrentMatser().getUsername());
		}
		QiNiuUploadManager.uploadRecorderVoice(tId, photo, "recorder", params, new IonUploadCallback()
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
	 * 获取录音token接口回调
	 * 
	 * @author houjun
	 */
	public static interface IOnUploadVoiceCallback
	{
		public void onSuccess(String uploadToken);

		public void onError(String errorMessage);
	}

}
