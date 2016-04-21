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
 * 用户头像上传管理器
 * 
 * @author houjun
 */
public class AccountAvatarUploadManager
{

	/**
	 * 修改用户头像
	 */
	public static void modifyAccountAvatar(final File avatar, final String username)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("x:username", username);
		QiNiuUploadManager.uploadAccountAvatar(avatar, "headPortrait", params, new IonUploadCallback()
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
								String str = object.optString("img");
								if (V2GogoApplication.getMasterLoginState())
								{
									V2GogoApplication.getCurrentMatser().setImg(str);
									V2GogoApplication.updateMatser();
								}
								EventBus.getDefault().post(V2GogoApplication.getCurrentMatser());
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
