package com.v2gogo.project.utils.common;

import java.io.File;
import java.io.IOException;

import android.os.Environment;
import android.os.StatFs;

/**
 * sd卡工具类
 * 
 * @author houjun
 */
public class SDCardUtil
{

	private SDCardUtil()
	{
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * sd卡是否可用
	 * 
	 * @return
	 */
	public static boolean isSDCardEnable()
	{
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

	}

	/**
	 * sd卡的挂载路径
	 * 
	 * @return
	 */
	public static String getSDCardPath()
	{
		return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
	}

	/**
	 * 得到sd卡的大小（byte）
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static long getSDCardAllSize()
	{
		if (isSDCardEnable())
		{
			StatFs stat = new StatFs(getSDCardPath());
			long availableBlocks = (long) stat.getAvailableBlocks() - 4;
			long freeBlocks = stat.getAvailableBlocks();
			return freeBlocks * availableBlocks;
		}
		return 0;
	}

	/**
	 * 得到系统的根路径
	 * 
	 * @return
	 */
	public static String getRootDirectoryPath()
	{
		return Environment.getRootDirectory().getAbsolutePath();
	}

	/**
	 * method desc：根据路径判断文件是否存在
	 * 
	 * @param path
	 * @return
	 */
	public static boolean isExists(String path)
	{
		try
		{
			File file = new File(path);
			if (file.exists())
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch (Exception e)
		{
			if (e != null)
			{
				e.printStackTrace();
			}
		}
		return false;
	}

}
