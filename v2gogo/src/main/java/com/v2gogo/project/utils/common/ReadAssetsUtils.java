package com.v2gogo.project.utils.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.res.AssetManager;

/**
 * 读取assets下面的文件
 * 
 * @author houjun
 */
public class ReadAssetsUtils
{

	public static String readAssertResource(Context context, String strAssertFileName)
	{
		AssetManager assetManager = context.getAssets();
		String strResponse = "";
		try
		{
			InputStream ims = assetManager.open(strAssertFileName);
			strResponse = getStringFromInputStream(ims);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return strResponse;
	}

	private static String getStringFromInputStream(InputStream a_is)
	{
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		String line;
		try
		{
			br = new BufferedReader(new InputStreamReader(a_is));
			while ((line = br.readLine()) != null)
			{
				sb.append(line);
			}
		}
		catch (IOException e)
		{
		}
		finally
		{
			if (br != null)
			{
				try
				{
					br.close();
				}
				catch (IOException e)
				{
				}
			}
		}
		return sb.toString();
	}
}
