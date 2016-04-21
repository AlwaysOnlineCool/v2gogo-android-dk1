package com.v2gogo.project.utils.parse;

import java.lang.reflect.Type;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.v2gogo.project.utils.common.LogUtil;

/**
 * json解析
 * 
 * @author houjun
 */
public class JsonParser
{

	/**
	 * 将json字符串转化为对象
	 * 
	 * @param jsonString
	 *            json字符串
	 * @param clazz
	 *            对应的类名
	 * @return 实体对象
	 */
	public static <T> Object parseObject(String jsonString, Class<T> clazz)
	{
		Gson gson = new Gson();
		return gson.fromJson(jsonString, clazz);
	}

	/**
	 * 解析json数组
	 * 
	 * @param jsonString
	 *            json字符串
	 * @param type
	 * @return
	 */
	public static Object parseObjectList(String jsonString, Type type)
	{
		if (LogUtil.isDebug)
		{
			if (TextUtils.isEmpty(jsonString))
			{
				throw new IllegalArgumentException("解析的字符串不能为空");
			}
		}
		Gson gson = new Gson();
		return gson.fromJson(jsonString, type);
	}
}
