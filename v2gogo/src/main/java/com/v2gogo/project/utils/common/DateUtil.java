package com.v2gogo.project.utils.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.sharesdk.framework.statistics.NewAppReceiver;

/**
 * 时间转化工具
 * 
 * @author houjun
 */
public class DateUtil
{

	/**
	 * 计算当前时间点距离明天的时间差值
	 * 
	 * @param currentTime
	 * @return
	 */
	public static long getOffestTime(long currentTime)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		long offest = -1;
		long end = time2TimeStamp(format.format(new Date()) + " 23:59:59");
		offest = end - currentTime;
		return offest;
	}

	/**
	 * 根据时间戳转化为字符串
	 */
	public static String convertStringWithTimeStamp(long timeStamp)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
		String date = sdf.format(new Date(timeStamp));
		return date;
	}

	/**
	 * 根据时间戳转化为字符串
	 */
	public static String convertStringWithTimeStampWithoutHour(long timeStamp)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
		String date = sdf.format(new Date(timeStamp));
		return date;
	}

	/**
	 * 转化为时间戳
	 */
	public static long time2TimeStamp(String dataTime)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		long distance = -1;
		try
		{
			distance = dateFormat.parse(dataTime).getTime();
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		return distance;
	}

	/**
	 * 显示友好时间
	 * 
	 * @param timestape
	 * @return
	 */
	public static String getTimeDiff(long timestape)
	{

		StringBuffer sb = new StringBuffer();
		long time = System.currentTimeMillis() - (timestape);

		long mill = (long) Math.ceil(time / 1000);// 秒前
		long minute = (long) Math.ceil(time / 60 / 1000.0f);// 分钟前
		long hour = (long) Math.ceil(time / 60 / 60 / 1000.0f);// 小时
		long day = (long) Math.ceil(time / 24 / 60 / 60 / 1000.0f);// 天前
		if (day - 1 > 0)
		{
			if (day - 1 >= 15)
			{
				sb.append("15天");
			}
			else
			{
				sb.append((day - 1) + "天");
			}
		}
		else if (hour - 1 > 0)
		{
			if (hour >= 24)
			{
				sb.append("1天");
			}
			else
			{
				sb.append((hour - 1) + "小时");
			}
		}
		else if (minute - 1 > 0)
		{
			if (minute == 60)
			{
				sb.append("1小时");
			}
			else
			{
				sb.append((minute - 1) + "分钟");
			}
		}
		else if (mill - 1 > 0)
		{
			if (mill == 60)
			{
				sb.append("1分钟");
			}
			else
			{
				sb.append((mill - 1) + "秒");
			}
		}
		else
		{
			sb.append("刚刚");
		}
		if (!sb.toString().equals("刚刚"))
		{
			sb.append("前");
		}
		return sb.toString();
	}

	public static String getCurrentTimeStr(String format)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date());
	}
}
