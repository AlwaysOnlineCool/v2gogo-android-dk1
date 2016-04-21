package com.v2gogo.project.utils.common;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;

import com.v2gogo.project.activity.MainTabActivity;

/**
 * 应用程序工具类
 * 
 * @author houjun
 */
public class AppUtil
{

	private AppUtil()
	{
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * 得到应用名称
	 * 
	 * @param context
	 *            上下文环境
	 * @return
	 */
	public static String getAppName(Context context)
	{
		try
		{
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			int labelRes = packageInfo.applicationInfo.labelRes;
			return context.getResources().getString(labelRes);
		}
		catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 得到版本信息
	 * 
	 * @param context
	 *            上下文环境
	 * @return
	 */
	public static String getVersionName(Context context)
	{
		try
		{
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionName;
		}
		catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 得到版本号
	 * 
	 * @param context
	 *            上下文环境
	 * @return
	 */
	public static int getVersionCode(Context context)
	{
		try
		{
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		}
		catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 判断intent时候存在
	 */
	public static boolean isIntentExist(Context a_oContext, Intent a_oIntent)
	{
		boolean isExist = false;
		PackageManager packageManager = a_oContext.getPackageManager();
		List<ResolveInfo> componentList = packageManager.queryIntentActivities(a_oIntent, PackageManager.MATCH_DEFAULT_ONLY);
		if (componentList.size() > 0)
		{
			isExist = true;
		}
		return isExist;
	}

	/**
	 * 判断主程序是否已经启动
	 * 
	 * @param a_oContext
	 * @return
	 */
	public static boolean isMainIntentExist(Context a_oContext)
	{
		boolean result = false;
		Intent mainIntent = new Intent(a_oContext, MainTabActivity.class);
		ActivityManager am = (ActivityManager) a_oContext.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> appTask = am.getRunningTasks(1);
		if (appTask.size() > 0 && appTask.get(0).baseActivity.equals(mainIntent.getComponent()))
		{
			result = true;
		}
		return result;
	}
}
