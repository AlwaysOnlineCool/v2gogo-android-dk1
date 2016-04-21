package com.v2gogo.project.utils.common;

import android.util.Log;

/**
 * 日志打印工具
 * 
 * @author houjun
 */
public class LogUtil
{
	private LogUtil()
	{
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	private static final String TAG = "v2gogo";
	public static boolean isDebug = false;

	public static void i(String msg)
	{
		if (isDebug)
			Log.i(TAG, msg);
	}

	public static void d(String msg)
	{
		if (isDebug)
			Log.d(TAG, msg);
	}

	public static void e(String msg)
	{
		if (isDebug)
			Log.e(TAG, msg);
	}

	public static void v(String msg)
	{
		if (isDebug)
			Log.v(TAG, msg);
	}

	public static void i(String tag, String msg)
	{
		if (isDebug)
			Log.i(TAG, msg);
	}

	public static void d(String tag, String msg)
	{
		if (isDebug)
			Log.d(tag, msg);
	}

	public static void e(String tag, String msg)
	{
		if (isDebug)
			Log.e(tag, msg);
	}

	public static void v(String tag, String msg)
	{
		if (isDebug)
			Log.v(tag, msg);
	}

}
