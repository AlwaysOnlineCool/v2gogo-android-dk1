package com.v2gogo.project.utils.common;

import android.content.Context;
import android.util.TypedValue;

/**
 * 单位转化工具类
 * 
 * @author houjun
 */
public class DensityUtil
{

	private DensityUtil()
	{
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * dp转px
	 * 
	 * @param context
	 *            上下文环境
	 * @param val
	 *            要转化的值ת�ֵ
	 * @return
	 */
	public static int dp2px(Context context, float dpVal)
	{
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources().getDisplayMetrics());
	}

	/**
	 * sp转px
	 * 
	 * @param context
	 *            上下文环境
	 * @param val
	 *            要转化的值
	 * @return
	 */
	public static int sp2px(Context context, float spVal)
	{
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, context.getResources().getDisplayMetrics());
	}

	/**
	 * px转dp
	 * 
	 * @param context
	 *            上下文环境
	 * @param pxVal
	 *            要转化的值ֵ
	 * @return
	 */
	public static float px2dp(Context context, float pxVal)
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return (pxVal / scale);
	}

	/**
	 * px转sp
	 * 
	 * @param context
	 *            上下文环境
	 * @param pxVal
	 *            要转化的值ֵ
	 * @return
	 */
	public static float px2sp(Context context, float pxVal)
	{
		return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
	}

}
