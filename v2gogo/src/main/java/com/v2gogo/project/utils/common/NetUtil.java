package com.v2gogo.project.utils.common;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络判断工具类
 * 
 * @author houjun
 */
public class NetUtil
{

	private NetUtil()
	{
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * 网络是否连接
	 * 
	 * @param context
	 *            上下文
	 * @return
	 */
	public static boolean isConnected(Context context)
	{

		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (null != connectivity)
		{

			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (null != info && info.isConnected())
			{
				if (info.getState() == NetworkInfo.State.CONNECTED)
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 是否是wifi
	 */
	public static boolean isWifi(Context context)
	{
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (cm == null)
			return false;
		return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;

	}

	/**
	 * 打开网络设置
	 */
	public static void openSetting(Activity activity)
	{
		Intent intent = new Intent("/");
		ComponentName cm = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
		intent.setComponent(cm);
		intent.setAction("android.intent.action.VIEW");
		activity.startActivityForResult(intent, 0);
	}

}
