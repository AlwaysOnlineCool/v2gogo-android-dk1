package com.v2gogo.project.utils.common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 本地持久化操作
 * 
 * @author houjun
 */
public class SPUtil
{

	/**
	 * 本地持久化文件名
	 */
	public static final String FILE_NAME = "v2gogo";

	/**
	 * 持久化数据
	 * 
	 * @param context
	 *            上下文环境
	 * @param key
	 *            对应的key
	 * @param object
	 *            对应的数据
	 */
	public static boolean put(Context context, String key, Object object)
	{
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		if (object != null)
		{
			if (object instanceof String)
			{
				editor.putString(key, (String) object);
			}
			else if (object instanceof Integer)
			{
				editor.putInt(key, (Integer) object);
			}
			else if (object instanceof Boolean)
			{
				editor.putBoolean(key, (Boolean) object);
			}
			else if (object instanceof Float)
			{
				editor.putFloat(key, (Float) object);
			}
			else if (object instanceof Long)
			{
				editor.putLong(key, (Long) object);
			}
			else
			{
				editor.putString(key, object.toString());
			}
			return editor.commit();
		}
		return false;
	}

	/**
	 * 去除持久化数据ֵ
	 * 
	 * @param context
	 *            上下文数据
	 * @param key
	 *            对应的key
	 * @param defaultObject
	 *            默认值ֵ
	 * @return
	 */
	public static Object get(Context context, String key, Object defaultObject)
	{
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		if (defaultObject instanceof String)
		{
			return sp.getString(key, (String) defaultObject);
		}
		else if (defaultObject instanceof Integer)
		{
			return sp.getInt(key, (Integer) defaultObject);
		}
		else if (defaultObject instanceof Boolean)
		{
			return sp.getBoolean(key, (Boolean) defaultObject);
		}
		else if (defaultObject instanceof Float)
		{
			return sp.getFloat(key, (Float) defaultObject);
		}
		else if (defaultObject instanceof Long)
		{
			return sp.getLong(key, (Long) defaultObject);
		}
		return null;
	}

	/**
	 * 移除持久化数据ֵ
	 * 
	 * @param context
	 *            上下文环境
	 * @param key
	 *            对应的key
	 */
	public static void remove(Context context, String key)
	{
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.remove(key);
		editor.commit();
	}

	/**
	 * 清除持久化数据
	 * 
	 * @param context
	 *            上下文环境
	 */
	public static void clear(Context context)
	{
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.clear();
		editor.commit();
	}

	/**
	 * 是否包含相应的key
	 * 
	 * @param context
	 *            上下文
	 * @param key
	 *            对应的key
	 * @return
	 */
	public static boolean contains(Context context, String key)
	{
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		return sp.contains(key);
	}

}
