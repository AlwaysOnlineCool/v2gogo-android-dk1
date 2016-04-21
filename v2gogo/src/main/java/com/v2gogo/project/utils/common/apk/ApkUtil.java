package com.v2gogo.project.utils.common.apk;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * apk安装相关
 * 
 * @author houjun
 */
public class ApkUtil
{
	/**
	 * 开始下载apk
	 */
	public static void startDownloadApk(Activity activity, String url)
	{
		Intent intent = new Intent(activity.getApplicationContext(), DownloadApkService.class);
		intent.putExtra(DownloadApkService.APK_URL, url);
		activity.startService(intent);
	}

	/**
	 * 开始下载apk
	 */
	public static void startDownloadApk(Context context, String url)
	{
		Intent intent = new Intent(context.getApplicationContext(), DownloadApkService.class);
		intent.putExtra(DownloadApkService.APK_URL, url);
		context.startService(intent);
	}

	/**
	 * 安装apk
	 */
	public static void installApk(Context context, String filePath)
	{
		Intent intent = new Intent(Intent.ACTION_VIEW);
		File file = new File(filePath);
		if (file != null && file.length() > 0 && file.exists() && file.isFile())
		{
			intent.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}
	}
}
