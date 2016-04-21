package com.v2gogo.project.utils.qiniu;

import android.os.Build;
import android.text.TextUtils;

import com.v2gogo.project.manager.config.PhotoServerUrlConfig;
import com.v2gogo.project.utils.common.LogUtil;

public class VersionPhotoUrlBuilder
{

	/**
	 * 小图路径加工器(80DIP)
	 * 
	 * @param url
	 * @return
	 */
	public static String createThumbialUrl(String url)
	{
		String photoUrl = null;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
		{
			photoUrl = url + PhotoServerUrlConfig.PHOTO_IMAGE_SUFFIX_FOR_LIST_THUMB_FOR_WEBP;
		}
		else
		{
			photoUrl = url + PhotoServerUrlConfig.PHOTO_IMAGE_SUFFIX_FOR_LIST_THUMB_FOR_JPG;
		}
		LogUtil.d("houjun", "createThumbialUrl->photoUrl->" + photoUrl);
		return photoUrl;
	}

	/**
	 * 小图路径加工器(1/2)
	 * 
	 * @param url
	 * @return
	 */
	public static String createThumbialUrlByHalf(String url)
	{
		String photoUrl = null;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
		{
			photoUrl = url + PhotoServerUrlConfig.PHOTO_IMAGE_SUFFIX_FOR_LIST_THUMB_FOR_WEBP_HALF;
		}
		else
		{
			photoUrl = url + PhotoServerUrlConfig.PHOTO_IMAGE_SUFFIX_FOR_LIST_THUMB_FOR_JPG_HALF;
		}
		LogUtil.d("houjun", "createThumbialUrlByHalf->photoUrl->" + photoUrl);
		return photoUrl;
	}

	/**
	 * 小图路径加工器(3/8)
	 * 
	 * @param url
	 * @return
	 */
	public static String createThumbialUrlByTE(String url)
	{
		String photoUrl = null;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
		{
			photoUrl = url + PhotoServerUrlConfig.PHOTO_IMAGE_SUFFIX_FOR_LIST_THUMB_FOR_WEBP_TE;
		}
		else
		{
			photoUrl = url + PhotoServerUrlConfig.PHOTO_IMAGE_SUFFIX_FOR_LIST_THUMB_FOR_JPG_TE;
		}
		LogUtil.d("houjun", "createThumbialUrlByTE->photoUrl->" + photoUrl);
		return photoUrl;
	}

	/**
	 * 小图路径加工器(3/8)
	 * 
	 * @param url
	 * @return
	 */
	public static String createThumbialUserAvatar(String url)
	{
		String photoUrl = null;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
		{
			photoUrl = url + PhotoServerUrlConfig.PHOTO_IMAGE_SUFFIX_FOR_LIST_THUMB_FOR_WEBP_AVATAR;
		}
		else
		{
			photoUrl = url + PhotoServerUrlConfig.PHOTO_IMAGE_SUFFIX_FOR_LIST_THUMB_FOR_JPG_AVATAR;
		}
		LogUtil.d("houjun", "createThumbialUserAvatar->photoUrl->" + photoUrl);
		return photoUrl;
	}

	/**
	 * 小图路径加工器(3/8)
	 * 
	 * @param url
	 * @return
	 */
	public static String createNoFixedImage(String url)
	{
		String photoUrl = null;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
		{
			photoUrl = url + PhotoServerUrlConfig.PHOTO_IMAGE_NO_FIXED_SIZE_WEBP;
		}
		else
		{
			photoUrl = url + PhotoServerUrlConfig.PHOTO_IMAGE_NO_FIXED_SIZE_JPG;
		}
		LogUtil.d("houjun", "createNoFixedImage->photoUrl->" + photoUrl);
		return photoUrl;
	}

	/**
	 * 首页底部菜单小图路径 46dp/46dp
	 * 
	 * @param url
	 * @return
	 */
	public static String createHomeBottomToolBar(String url)
	{
		String photoUrl = null;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
		{
			photoUrl = url + PhotoServerUrlConfig.PHOTO_IMAGE_SUFFIX_BOTTOM_TOOL_WEAP_BAR;
		}
		else
		{
			photoUrl = url + PhotoServerUrlConfig.PHOTO_IMAGE_SUFFIX_BOTTOM_TOOL_JPG_BAR;
		}
		LogUtil.d("houjun", "createNoFixedImage->photoUrl->" + photoUrl);
		return photoUrl;
	}

	/**
	 * 首页底部菜单小图路径 46dp/46dp
	 * 
	 * @param url
	 * @return
	 */
	public static String createShakeImage(String url)
	{
		String photoUrl = null;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
		{
			photoUrl = url + PhotoServerUrlConfig.PHOTO_IMAGE_SUFFIX_FOR_SHAKE_WEBP;
		}
		else
		{
			photoUrl = url + PhotoServerUrlConfig.PHOTO_IMAGE_SUFFIX_FOR_SHAKE_JPG;
		}
		LogUtil.d("houjun", "createNoFixedImage->photoUrl->" + photoUrl);
		return photoUrl;
	}

	/**
	 * 返回版本的图片url
	 * 
	 * @param url
	 * @return
	 */
	public static String createVersionImageUrl(String url)
	{
		if (!TextUtils.isEmpty(url))
		{
			if (url.contains(PhotoServerUrlConfig.PHOTO_SERVER_URL))
			{
				return url;
			}
			else
			{
				return PhotoServerUrlConfig.PHOTO_SERVER_URL + url;
			}
		}
		else
		{
			return url;
		}
	}
}
