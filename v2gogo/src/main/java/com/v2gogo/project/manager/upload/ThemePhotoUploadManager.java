package com.v2gogo.project.manager.upload;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.os.Environment;

import com.v2gogo.project.domain.home.theme.ThemePhotoUploadResultInfo;
import com.v2gogo.project.domain.home.theme.UploadErrorInfo;
import com.v2gogo.project.domain.home.theme.UploadProgressInfo;
import com.v2gogo.project.utils.StatusCode;
import com.v2gogo.project.utils.http.Constants;
import com.v2gogo.project.utils.parse.JsonParser;
import com.v2gogo.project.utils.qiniu.QiNiuUploadManager;
import com.v2gogo.project.utils.qiniu.QiNiuUploadManager.IonUploadCallback;
import com.ypy.eventbus.EventBus;

/**
 * 主题照片上传管理器
 * 
 * @author houjun
 */
public class ThemePhotoUploadManager
{

	public static final String FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/v2gogo/theme";

	/**
	 * 上传主题图片
	 * 
	 * @param file
	 */
	public static void uploadThemePhoto(String tid, File file, String userName, String photoDescription, String tId)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("x:username", userName);
		params.put("x:photoDescription", photoDescription);
		params.put("x:tId", tId);
		QiNiuUploadManager.uploadSignalPhotoFile(tid, file, "photo", params, new IonUploadCallback()
		{
			@Override
			public void onUploadCallback(int statusCode, String key, JSONObject jsonObject)
			{
				if (statusCode == QiNiuUploadManager.UPLOAD_STATUS_CODE_SUCCESS)
				{
					if (null != jsonObject)
					{
						int code = jsonObject.optInt(Constants.CODE);
						String message = jsonObject.optString(Constants.MESSAGE);
						if (StatusCode.SUCCESS == code)
						{
							JSONObject object = jsonObject.optJSONObject("result");
							if (null != object)
							{
								ThemePhotoUploadResultInfo photoUploadResultInfo = (ThemePhotoUploadResultInfo) JsonParser.parseObject(object.toString(),
										ThemePhotoUploadResultInfo.class);
								EventBus.getDefault().post(photoUploadResultInfo);
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
				}
				else
				{
					UploadErrorInfo uploadErrorInfo = new UploadErrorInfo();
					uploadErrorInfo.setCode(statusCode);
					uploadErrorInfo.setMessage(null);
					EventBus.getDefault().post(uploadErrorInfo);
				}
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
			public void onUploadProgress(String key, double progress)
			{
				UploadProgressInfo uploadProgressInfo = new UploadProgressInfo();
				uploadProgressInfo.setKey(key);
				uploadProgressInfo.setProgress((int) (progress * 100));
				EventBus.getDefault().post(uploadProgressInfo);
			}
		});
	}
}
