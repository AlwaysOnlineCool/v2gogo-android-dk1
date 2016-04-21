package com.v2gogo.project.utils.common;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

/**
 * 屏幕工具类
 * 
 * @author houjun
 */
public class ScreenUtil
{

	private static int sScreenWidth = 0;
	private static int sStatusHeight = 0;
	private static int sScreenHeight = 0;

	private ScreenUtil()
	{
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * 得到屏幕的宽度
	 * 
	 * @param context
	 *            上下文环境
	 * @return
	 */
	public static int getScreenWidth(Context context)
	{
		if (sScreenWidth == 0)
		{
			WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			DisplayMetrics outMetrics = new DisplayMetrics();
			wm.getDefaultDisplay().getMetrics(outMetrics);
			sScreenWidth = outMetrics.widthPixels;
		}
		return sScreenWidth;
	}

	/**
	 * 得到屏幕的高度
	 * 
	 * @param context
	 *            上下文环境
	 * @return
	 */
	public static int getScreenHeight(Context context)
	{
		if (sScreenHeight == 0)
		{
			WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			DisplayMetrics outMetrics = new DisplayMetrics();
			wm.getDefaultDisplay().getMetrics(outMetrics);
			sScreenHeight = outMetrics.heightPixels;
		}
		return sScreenHeight;
	}

	/**
	 * 得到状态太的高度
	 * 
	 * @param context
	 *            上下文环境
	 * @return
	 */
	public static int getStatusHeight(Context context)
	{
		if (sStatusHeight == 0)
		{
			try
			{
				Class<?> clazz = Class.forName("com.android.internal.R$dimen");
				Object object = clazz.newInstance();
				int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
				sStatusHeight = context.getResources().getDimensionPixelSize(height);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}
		return sStatusHeight;
	}

	/**
	 * 截取屏幕（包含状态栏）
	 * 
	 * @param activity
	 *            截取界面的activity
	 * @return
	 */
	public static Bitmap snapShotWithStatusBar(Activity activity)
	{
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bmp = view.getDrawingCache();
		int width = getScreenWidth(activity);
		int height = getScreenHeight(activity);
		Bitmap bp = null;
		bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
		view.destroyDrawingCache();
		return bp;

	}

	/**
	 * 截取屏幕(不包含状态)
	 * 
	 * @param activity
	 *            截取界面的activity
	 * @return
	 */
	public static Bitmap snapShotWithoutStatusBar(Activity activity)
	{
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bmp = view.getDrawingCache();
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;

		int width = getScreenWidth(activity);
		int height = getScreenHeight(activity);
		Bitmap bp = null;
		bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height - statusBarHeight);
		view.destroyDrawingCache();
		return bp;
	}

}
