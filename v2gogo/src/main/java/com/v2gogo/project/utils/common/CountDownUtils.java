package com.v2gogo.project.utils.common;

import android.os.CountDownTimer;

/**
 * 倒计时的时间计算
 * 
 * @author houjun
 */
public class CountDownUtils
{
	private final static long yearLevelValue = 365 * 24 * 60 * 60 * 1;
	private final static long monthLevelValue = 30 * 24 * 60 * 60 * 1;
	private final static long dayLevelValue = 24 * 60 * 60 * 1;
	private final static long hourLevelValue = 60 * 60 * 1;
	private final static long minuteLevelValue = 60 * 1;
	private final static long secondLevelValue = 1;

	public static String getDifference(long period)
	{
		String result = null;
		int year = getYear(period);
		int month = getMonth(period - year * yearLevelValue);
		int day = getDay(period - year * yearLevelValue - month * monthLevelValue);
		int hour = getHour(period - year * yearLevelValue - month * monthLevelValue - day * dayLevelValue);
		int minute = getMinute(period - year * yearLevelValue - month * monthLevelValue - day * dayLevelValue - hour * hourLevelValue);
		int second = getSecond(period - year * yearLevelValue - month * monthLevelValue - day * dayLevelValue - hour * hourLevelValue - minute
				* minuteLevelValue);
		result = hour + ":" + minute + ":" + second;
		return result;
	}

	/*
	 * 毫秒转化
	 */
	public static String formatTime(long ms)
	{

		int ss = 1000;
		int mi = ss * 60;
		int hh = mi * 60;
		int dd = hh * 24;

		long day = ms / dd;
		long hour = (ms - day * dd) / hh;
		long minute = (ms - day * dd - hour * hh) / mi;
		long second = (ms - day * dd - hour * hh - minute * mi) / ss;
		long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

		String strDay = day < 10 ? "0" + day : "" + day; // 天
		String strHour = hour < 10 ? "0" + hour : "" + hour;// 小时
		String strMinute = minute < 10 ? "0" + minute : "" + minute;// 分钟
		String strSecond = second < 10 ? "0" + second : "" + second;// 秒
		String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;// 毫秒
		strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : "" + strMilliSecond;

		if (strDay.equals("00"))
		{
			if (strHour.equals("00"))
			{
				if (strMilliSecond.equals("00"))
				{
					return strSecond + "秒" + "后开抢";
				}
				else
				{
					return strMinute + "分钟 " + strSecond + "秒" + "后开抢";
				}
			}
			else
			{
				return strHour + "小时" + strMinute + "分钟 " + strSecond + "秒" + "后开抢";
			}
		}
		else
		{
			return strDay + "天" + strHour + "小时" + strMinute + "分钟 " + strSecond + "秒" + "后开抢";
		}
	}

	private static int getYear(long period)
	{
		return (int) (period / yearLevelValue);
	}

	private static int getMonth(long period)
	{
		return (int) (period / monthLevelValue);
	}

	private static int getDay(long period)
	{
		return (int) (period / dayLevelValue);
	}

	private static int getHour(long period)
	{
		return (int) (period / hourLevelValue);
	}

	private static int getMinute(long period)
	{
		return (int) (period / minuteLevelValue);
	}

	private static int getSecond(long period)
	{
		return (int) (period / secondLevelValue);
	}
}
