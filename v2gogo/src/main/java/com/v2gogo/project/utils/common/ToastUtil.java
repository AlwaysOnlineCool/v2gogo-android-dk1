package com.v2gogo.project.utils.common;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.v2gogo.project.views.crouton.Crouton;
import com.v2gogo.project.views.crouton.Style;

/**
 * toast工具类
 * 
 * @author houjun
 */
public class ToastUtil
{

	private ToastUtil()
	{
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * Toast显示
	 * 
	 * @param context
	 *            上下文环境
	 * @param message
	 *            显示的信息
	 */
	public static void showAlertToast(Activity activity, CharSequence message)
	{
		if (!TextUtils.isEmpty(message))
		{
			try
			{
				Crouton.makeText(activity, message, Style.ALERT).show();
			}
			catch (Exception e)
			{
			}
		}
	}

	/**
	 * Toast显示
	 * 
	 * @param context
	 *            上下文环境
	 * @param message
	 *            显示的信息
	 */
	public static void showAlertToast(Activity activity, int message)
	{
		try
		{
			Crouton.makeText(activity, message, Style.ALERT).show();
		}
		catch (Exception exception)
		{
		}
	}

	/**
	 * Toast显示
	 * 
	 * @param context
	 *            上下文环境
	 * @param message
	 *            显示的信息
	 */
	public static void showInfoToast(Activity activity, CharSequence message)
	{
		if (!TextUtils.isEmpty(message))
		{
			try
			{
				Crouton.makeText(activity, message, Style.INFO).show();
			}
			catch (Exception e)
			{
			}
		}
	}

	/**
	 * Toast显示
	 * 
	 * @param context
	 *            上下文环境
	 * @param message
	 *            显示的信息
	 */
	public static void showInfoToast(Activity activity, int message)
	{
		try
		{
			Crouton.makeText(activity, message, Style.INFO).show();
		}
		catch (Exception exception)
		{
		}
	}

	/**
	 * Toast显示
	 * 
	 * @param context
	 *            上下文环境
	 * @param message
	 *            显示的信息
	 */
	public static void showConfirmToast(Activity activity, CharSequence message)
	{
		if (!TextUtils.isEmpty(message))
		{
			try
			{
				Crouton.makeText(activity, message, Style.CONFIRM).show();
			}
			catch (Exception exception)
			{
			}
		}
	}

	/**
	 * Toast显示
	 * 
	 * @param context
	 *            上下文环境
	 * @param message
	 *            显示的信息
	 */
	public static void showConfirmToast(Activity activity, int message)
	{
		try
		{
			Crouton.makeText(activity, message, Style.CONFIRM).show();
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 清除toast
	 * 
	 * @param activity
	 */
	public static void clearToastOnActivity(Activity activity)
	{
		Crouton.clearCroutonsForActivity(activity);
	}

	/**
	 * 取消所有toast显示
	 */
	public static void cancelAllToast()
	{
		Crouton.cancelAllCroutons();
	}
}
