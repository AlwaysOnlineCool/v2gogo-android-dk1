package com.v2gogo.project.manager.upload;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.R.integer;

import com.v2gogo.project.domain.home.theme.UploadErrorInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.utils.StatusCode;
import com.v2gogo.project.utils.common.DateUtil;
import com.v2gogo.project.utils.http.Constants;
import com.v2gogo.project.utils.qiniu.CreateUUid;
import com.v2gogo.project.utils.qiniu.QiNiuUploadManager;
import com.v2gogo.project.utils.qiniu.QiNiuUploadManager.IonUploadCallback;
import com.ypy.eventbus.EventBus;

/**
 * 功能：爆料多媒体上传文件管理类
 * 
 * @ahthor：黄荣星
 * @date:2016-3-31
 * @version::V1.0
 */
public class CommentUploadMultimediaManager
{

	public static final int UPLOAD_FACT_MULTIMEDIA_IMG = 1;
	public static final int UPLOAD_FACT_MULTIMEDIA_VIDEO = 2;
	public static final int UPLOAD_FACT_MULTIMEDIA_VOICE = 3;

	public static void uploadFactMultimediaImg(final File file, final IonFactMultimediaUploadCallback listener)
	{
		Map<String, String> params = new HashMap<String, String>();
		if (V2GogoApplication.getMasterLoginState())
		{
			params.put("x:username", V2GogoApplication.getCurrentMatser().getUsername());
		}
		// upload/fromWeb/image
		String time = DateUtil.getCurrentTimeStr("yyyyMMdd");
		String key = "upload" + File.separator + "fromWeb" + File.separator + "image" + File.separator + time + File.separator + CreateUUid.creatUUID()
				+ ".jpg";
		QiNiuUploadManager.uploadFactMultimedia(key, file, UPLOAD_FACT_MULTIMEDIA_IMG, params, new IonUploadCallback()
		{
			@Override
			public void onUploadProgress(String key, double progress)
			{
				if (listener != null)
				{
					listener.onUploadProgress(progress);
				}
			}

			@Override
			public void onUploadFail(int code, String errorMessage)
			{
				if (listener != null)
				{
					listener.onUploadFail(code, errorMessage);
				}
				// UploadErrorInfo uploadErrorInfo = new UploadErrorInfo();
				// uploadErrorInfo.setCode(code);
				// uploadErrorInfo.setMessage(errorMessage);
				// EventBus.getDefault().post(uploadErrorInfo);
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
							if (listener != null)
							{
								listener.onUploadCallback(key);
							}
						}
						else
						{
							if (listener != null)
							{
								listener.onUploadFail(code, message);
							}
						}
					}
					else
					{
						if (listener != null)
						{
							listener.onUploadFail(statusCode, null);
						}
					}
				}
				else
				{
					if (listener != null)
					{
						listener.onUploadFail(statusCode, null);
					}
				}
			}
		});
	}

	/**
	 * method desc：上传视频
	 * 
	 * @param file
	 * @param listener
	 */
	public static void uploadFactMultimediaVideo(final File file, final IonFactMultimediaUploadCallback listener)
	{
		Map<String, String> params = new HashMap<String, String>();
		if (V2GogoApplication.getMasterLoginState())
		{
			params.put("x:username", V2GogoApplication.getCurrentMatser().getUsername());
		}
		// upload/fromWeb/video
		String time = DateUtil.getCurrentTimeStr("yyyyMMdd");
		String key = "upload" + File.separator + "fromWeb" + File.separator + "video" + File.separator + time + File.separator + CreateUUid.creatUUID()
				+ ".mp4";
		QiNiuUploadManager.uploadFactMultimedia(key, file, UPLOAD_FACT_MULTIMEDIA_VIDEO, params, new IonUploadCallback()
		{
			@Override
			public void onUploadProgress(String key, double progress)
			{
				if (listener != null)
				{
					listener.onUploadProgress(progress);
				}
			}

			@Override
			public void onUploadFail(int code, String errorMessage)
			{
				if (listener != null)
				{
					listener.onUploadFail(code, errorMessage);
				}
				// UploadErrorInfo uploadErrorInfo = new UploadErrorInfo();
				// uploadErrorInfo.setCode(code);
				// uploadErrorInfo.setMessage(errorMessage);
				// EventBus.getDefault().post(uploadErrorInfo);
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
							if (listener != null)
							{
								listener.onUploadCallback(key);
							}
						}
						else
						{
							if (listener != null)
							{
								listener.onUploadFail(code, message);
							}
						}
					}
					else
					{
						if (listener != null)
						{
							listener.onUploadFail(statusCode, null);
						}
					}
				}
				else
				{
					if (listener != null)
					{
						listener.onUploadFail(statusCode, null);
					}
				}
			}
		});
	}

	/**
	 * method desc：上传录音
	 * 
	 * @param file
	 * @param listener
	 */
	public static void uploadFactMultimediaVoice(final File file, final IonFactMultimediaUploadCallback listener)
	{
		Map<String, String> params = new HashMap<String, String>();
		if (V2GogoApplication.getMasterLoginState())
		{
			params.put("x:username", V2GogoApplication.getCurrentMatser().getUsername());
		}
		// upload/fromWeb/video
		String time = DateUtil.getCurrentTimeStr("yyyyMMdd");
		String key = "upload" + File.separator + "fromWeb" + File.separator + "audio" + File.separator + time + File.separator + CreateUUid.creatUUID()
				+ ".mp3";
		QiNiuUploadManager.uploadFactMultimedia(key, file, UPLOAD_FACT_MULTIMEDIA_VOICE, params, new IonUploadCallback()
		{
			@Override
			public void onUploadProgress(String key, double progress)
			{
				if (listener != null)
				{
					listener.onUploadProgress(progress);
				}
			}

			@Override
			public void onUploadFail(int code, String errorMessage)
			{
				if (listener != null)
				{
					listener.onUploadFail(code, errorMessage);
				}
				// UploadErrorInfo uploadErrorInfo = new UploadErrorInfo();
				// uploadErrorInfo.setCode(code);
				// uploadErrorInfo.setMessage(errorMessage);
				// EventBus.getDefault().post(uploadErrorInfo);
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
							if (listener != null)
							{
								listener.onUploadCallback(key);
							}
						}
						else
						{
							if (listener != null)
							{
								listener.onUploadFail(code, message);
							}
						}
					}
					else
					{
						if (listener != null)
						{
							listener.onUploadFail(statusCode, null);
						}
					}
				}
				else
				{
					if (listener != null)
					{
						listener.onUploadFail(statusCode, null);
					}
				}
			}
		});
	}

	public interface IonFactMultimediaUploadCallback
	{
		public void onUploadProgress(double progress);

		public void onUploadCallback(String key);

		public void onUploadFail(int code, String errorMessage);
	}
}
