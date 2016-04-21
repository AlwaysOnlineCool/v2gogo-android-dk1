package com.v2gogo.project.utils.share;

import android.content.Context;
import android.graphics.Bitmap;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.system.text.ShortMessage;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * 分享工具类
 * 
 * @author houjun
 */
public class ShareUtils
{

	public static enum SHARE_TYPE
	{
		SHARE_WEIXIN, SHARE_WEIXIN_FRIENDS, SHARE_MESSAGE, SHARE_QQ, SHARE_QZONE
	}

	/**
	 * 分享
	 */
	public static void share(Context context, String title, String intro, String href, String imageUrl, SHARE_TYPE type, PlatformActionListener listener)
	{
		Platform platform = getPlatformByShareType(context, type);
		if (platform != null)
		{
			ShareParams shareParams = new ShareParams();
			shareParams.setShareType(Platform.SHARE_TEXT);
			shareParams.setShareType(Platform.SHARE_WEBPAGE);
			shareParams.setTitle(title);
			shareParams.setTitleUrl(href);
			shareParams.setText(intro);
			shareParams.setUrl(href);
			shareParams.setImageUrl(imageUrl);
			platform.setPlatformActionListener(listener);
			platform.share(shareParams);
		}
	}

	/**
	 * 分享
	 */
	public static void share(Context context, String title, String intro, String href, String imageUrl, Bitmap bitmap, SHARE_TYPE type,
			PlatformActionListener listener)
	{
		Platform platform = getPlatformByShareType(context, type);
		if (platform != null)
		{
			ShareParams shareParams = new ShareParams();
			shareParams.setShareType(Platform.SHARE_TEXT);
			shareParams.setShareType(Platform.SHARE_WEBPAGE);
			shareParams.setTitle(title);
			shareParams.setText(intro);
			shareParams.setUrl(href);
			if (null == bitmap)
			{
				shareParams.setImageUrl(imageUrl);
			}
			else
			{
				shareParams.setImageData(bitmap);
			}
			platform.setPlatformActionListener(listener);
			platform.share(shareParams);
		}
	}

	/**
	 * 分享
	 */
	public static void share(Context context, String title, String intro, String href, Bitmap bitmap, SHARE_TYPE type, PlatformActionListener listener)
	{
		Platform platform = getPlatformByShareType(context, type);
		if (platform != null)
		{
			ShareParams shareParams = new ShareParams();
			shareParams.setShareType(Platform.SHARE_TEXT);
			shareParams.setShareType(Platform.SHARE_WEBPAGE);
			shareParams.setTitle(title);
			shareParams.setText(intro);
			shareParams.setUrl(href);
			if (type != SHARE_TYPE.SHARE_MESSAGE)
			{
				shareParams.setImageData(bitmap);
			}
			platform.setPlatformActionListener(listener);
			platform.share(shareParams);
		}
	}

	/**
	 * 得到分享平台
	 */
	private static Platform getPlatformByShareType(Context context, SHARE_TYPE type)
	{
		Platform platform = null;
		if (type == SHARE_TYPE.SHARE_WEIXIN)
		{
			platform = ShareSDK.getPlatform(context, Wechat.NAME);
		}
		else if (type == SHARE_TYPE.SHARE_WEIXIN_FRIENDS)
		{
			platform = ShareSDK.getPlatform(context, WechatMoments.NAME);
		}
		else if (type == SHARE_TYPE.SHARE_MESSAGE)
		{
			platform = ShareSDK.getPlatform(context, ShortMessage.NAME);
		}
		else if (type == SHARE_TYPE.SHARE_QQ)
		{
			platform = ShareSDK.getPlatform(context, QQ.NAME);
		}
		else if (type == SHARE_TYPE.SHARE_QZONE)
		{
			platform = ShareSDK.getPlatform(context, QZone.NAME);
		}
		return platform;
	}
}
