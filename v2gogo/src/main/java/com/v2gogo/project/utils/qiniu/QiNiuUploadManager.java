package com.v2gogo.project.utils.qiniu;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.json.JSONObject;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.utils.common.LogUtil;
import com.v2gogo.project.utils.qiniu.QiNiuUploadToken.IonUploadTokenCallback;

/**
 * 七牛上传文件管理器
 * 
 * @author houjun
 */
public class QiNiuUploadManager
{
	public static final int UPLOAD_STATUS_CODE_SUCCESS = 0;
	public static final int UPLOAD_STATUS_CODE_NET_BROKEN = 1;
	public static final int UPLOAD_STATUS_CODE_NOT_QINIU = 2;
	public static final int UPLOAD_STATUS_CODE_SERVER_ERROR = 3;
	public static final int UPLOAD_STATUS_CODE_YET_CANCELED = 4;
	public static final int UPLOAD_STATUS_FILE_NOT_EXIST = 5;

	public static final Map<String, Boolean> keys = null;

	private static UploadManager sUploadManager = null;
	static
	{
		if (sUploadManager == null)
		{
			synchronized (QiNiuUploadManager.class)
			{
				if (sUploadManager == null)
				{
					sUploadManager = new UploadManager();
				}
			}
		}
	}

	/**
	 * 上传主题图片
	 * 
	 * @param file
	 *            文件
	 * @param preFixx
	 *            指定前缀
	 * @param callback
	 *            上传结果回调
	 */
	public static String uploadSignalPhotoFile(final String tid, final File file, final String preFixx, final Map<String, String> params,
			final IonUploadCallback callback)
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA);
		String date = df.format(new Date());
		String key = preFixx + File.separator + date + File.separator + CreateUUid.creatUUID();
		QiNiuUploadToken.getUploadToken(tid, new IonUploadTokenCallback()
		{
			@Override
			public void onUploadTokenSuccess(String token)
			{
				try
				{
					SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA);
					String date = df.format(new Date());
					String key = preFixx + File.separator + date + File.separator + CreateUUid.creatUUID();
					uploadFile2QiNiu(file, params, callback, token, key);
				}
				catch (Exception exception)
				{
					if (null != callback)
					{
						callback.onUploadFail(UPLOAD_STATUS_FILE_NOT_EXIST, null);
					}
				}
			}

			@Override
			public void onUploadTokenFail(int code, String message)
			{
				if (null != callback)
				{
					callback.onUploadFail(code, message);
				}
			}
		});
		return key;
	}

	/**
	 * 上传用户头像
	 */
	public static String uploadAccountAvatar(final File file, final String preFixx, final Map<String, String> params, final IonUploadCallback callback)
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA);
		String date = df.format(new Date());
		final String key = preFixx + File.separator + date + File.separator + CreateUUid.creatUUID();
		String url = ServerUrlConfig.SERVER_URL + "/userapp/getUploadToken";
		QiNiuUploadToken.getUserUploadToken(url, new IonUploadTokenCallback()
		{
			@Override
			public void onUploadTokenSuccess(String token)
			{
				try
				{
					uploadFile2QiNiu(file, params, callback, token, key);
				}
				catch (Exception exception)
				{
					if (null != callback)
					{
						callback.onUploadFail(UPLOAD_STATUS_FILE_NOT_EXIST, null);
					}
				}
			}

			@Override
			public void onUploadTokenFail(int code, String message)
			{
				if (null != callback)
				{
					callback.onUploadFail(code, message);
				}
			}
		});
		return key;
	}

	/**
	 * 上传评论图片
	 */
	public static String uploadCommentPhoto(final File file, final String preFixx, final Map<String, String> params, final IonUploadCallback callback)
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA);
		String date = df.format(new Date());
		final String key = preFixx + File.separator + date + File.separator + CreateUUid.creatUUID();
		String url = ServerUrlConfig.SERVER_URL + "/vcomments/getUploadToken";
		QiNiuUploadToken.getUserUploadToken(url, new IonUploadTokenCallback()
		{
			@Override
			public void onUploadTokenSuccess(String token)
			{
				try
				{
					uploadFile2QiNiu(file, params, callback, token, key);
				}
				catch (Exception exception)
				{
					if (null != callback)
					{
						callback.onUploadFail(UPLOAD_STATUS_FILE_NOT_EXIST, null);
					}
				}
			}

			@Override
			public void onUploadTokenFail(int code, String message)
			{
				if (null != callback)
				{
					callback.onUploadFail(code, message);
				}
			}
		});
		return key;
	}

	/**
	 * 上传录音
	 */
	public static String uploadRecorderVoice(String tid, final File file, final String preFixx, final Map<String, String> params,
			final IonUploadCallback callback)
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA);
		String date = df.format(new Date());
		final String key = preFixx + File.separator + date + File.separator + CreateUUid.creatUUID();
		QiNiuUploadToken.getUploadVoiceToken(tid, new IonUploadTokenCallback()
		{
			@Override
			public void onUploadTokenSuccess(String token)
			{
				try
				{
					uploadFile2QiNiu(file, params, callback, token, key);
				}
				catch (Exception exception)
				{
					if (null != callback)
					{
						callback.onUploadFail(UPLOAD_STATUS_FILE_NOT_EXIST, null);
					}
				}
			}

			@Override
			public void onUploadTokenFail(int code, String message)
			{
				if (null != callback)
				{
					callback.onUploadFail(code, message);
				}
			}
		});
		return key;
	}

	/**
	 * 上传评分评论图片
	 */
	public static String uploadScoreCommentPhoto(final File file, final String preFixx, String token, final Map<String, String> params,
			final IonUploadCallback callback)
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA);
		String date = df.format(new Date());
		final String key = preFixx + File.separator + date + File.separator + CreateUUid.creatUUID();
		uploadFile2QiNiu(file, params, callback, token, key);
		return key;
	}

	/**
	 * 上传文件到七牛
	 */
	private static void uploadFile2QiNiu(final File file, final Map<String, String> params, final IonUploadCallback callback, String token, String key)
	{
		sUploadManager.put(file, key, token, new UpCompletionHandler()
		{
			@Override
			public void complete(String key, ResponseInfo info, JSONObject jsonObject)
			{
				LogUtil.d("key->" + key + "--->info-->" + info + "--->jsonobject-->" + jsonObject);
				if (null != info)
				{
					int code = -1;
					// 上传成功
					if (info.isOK())
					{
						code = UPLOAD_STATUS_CODE_SUCCESS;
					}
					// 网络断开
					else if (info.isNetworkBroken())
					{
						code = UPLOAD_STATUS_CODE_NET_BROKEN;
					}
					// 服务器内部错误
					else if (info.isServerError())
					{
						code = UPLOAD_STATUS_CODE_SERVER_ERROR;
					}
					// 不是七牛
					else if (info.isNotQiniu())
					{
						code = UPLOAD_STATUS_CODE_NOT_QINIU;
					}
					// 已经取消
					else if (info.isCancelled())
					{
						code = UPLOAD_STATUS_CODE_YET_CANCELED;
					}
					if (null != callback)
					{
						callback.onUploadCallback(code, key, jsonObject);
					}
				}
			}
		}, new UploadOptions(params, "image/jpeg", true, new UpProgressHandler()
		{
			@Override
			public void progress(String key, double percent)
			{
				if (null != callback)
				{
					callback.onUploadProgress(key, percent);
				}
			}
		}, null));
	}

	// =========================爆料上传多媒体========================
	/**
	 * 爆料上传多媒体
	 */
	public static String uploadFactMultimedia(final String key, final File file, int type, final Map<String, String> params, final IonUploadCallback callback)
	{
		QiNiuUploadToken.getUploadFactMultmediaImgToken(type, new IonUploadTokenCallback()
		{
			@Override
			public void onUploadTokenSuccess(String token)
			{
				try
				{
					uploadFile2QiNiu(file, params, callback, token, key);
				}
				catch (Exception exception)
				{
					if (null != callback)
					{
						callback.onUploadFail(UPLOAD_STATUS_FILE_NOT_EXIST, null);
					}
				}
			}

			@Override
			public void onUploadTokenFail(int code, String message)
			{
				if (null != callback)
				{
					callback.onUploadFail(code, message);
				}
			}
		});
		return key;
	}

	public interface IonUploadCallback
	{
		public void onUploadProgress(String key, double progress);

		public void onUploadCallback(int statusCode, String key, JSONObject jsonObject);

		public void onUploadFail(int code, String errorMessage);
	}
}
