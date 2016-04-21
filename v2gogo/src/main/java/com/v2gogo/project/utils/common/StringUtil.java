package com.v2gogo.project.utils.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

/**
 * 字符串工具类
 * 
 * @author houjun
 */
public class StringUtil
{

	/**
	 * 去除字符串的字符
	 * 
	 * @param str
	 * @return
	 */
	public static String replaceBlank(String str)
	{
		String dest = "";
		if (!TextUtils.isEmpty(str))
		{
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	/**
	 * method desc：手机号码验证 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
	 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
	 * 
	 * @param mobiles
	 *            手机号码
	 * @return true验证通过，false 不通过
	 */
	public static boolean isMobileNO(String mobiles)
	{
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(145)|(147)|(17[0-9])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}
}
