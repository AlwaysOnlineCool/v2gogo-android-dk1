package com.v2gogo.project.utils.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * MD5加密工具
 * 
 * @author houjun
 */
public class MD5Util
{
	// 全局数组
	private final static String[] strDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	/**
	 * 得到MD5加密的字符串
	 * 
	 * @param strObj
	 *            要加密的字符串
	 * @return
	 */
	public static String getMD5String(String strObj)
	{
		String resultString = null;
		try
		{
			resultString = new String(strObj);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteToString(md.digest(strObj.getBytes()));
		}
		catch (NoSuchAlgorithmException ex)
		{
			ex.printStackTrace();
		}
		return resultString;
	}

	// 加密后解密
	public static String JM(String inStr)
	{
		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++)
		{
			a[i] = (char) (a[i] ^ 't');
		}
		String k = new String(a);
		return k;
	}

	// 返回形式为数字跟字符串
	private static String byteToArrayString(byte bByte)
	{
		int iRet = bByte;
		if (iRet < 0)
		{
			iRet += 256;
		}
		int iD1 = iRet / 16;
		int iD2 = iRet % 16;
		return strDigits[iD1] + strDigits[iD2];
	}

	// 转换字节数组为16进制字串
	private static String byteToString(byte[] bByte)
	{
		StringBuffer sBuffer = new StringBuffer();
		for (int i = 0; i < bByte.length; i++)
		{
			sBuffer.append(byteToArrayString(bByte[i]));
		}
		return sBuffer.toString();
	}

	/**
	 * 得到token的md5值(生成签名的方法)
	 * 
	 * @param params
	 * @return
	 */
	public static String getMd5Token(Map<String, String> params)
	{
		if (params != null)
		{
			TreeMap<String, String> treeMap = new TreeMap<String, String>(params);
			StringBuilder stringBuilder = new StringBuilder();
			for (Entry<String, String> string : treeMap.entrySet())
			{
				stringBuilder.append(string.getKey() + "=" + string.getValue());
			}
			return getMD5String(stringBuilder.toString());
		}
		return null;
	}
}
