package com.v2gogo.project.utils.common;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.v2gogo.project.R;

/**
 * 通知栏消息生成器
 * 
 * @author houjun
 */
public class NotificationUtil
{
	public static final String IS_SHOW_PUSH_VOICE = "is_show_push_voice";
	public static final String IS_SHOW_PUSH_NEWS = "is_show_push_news";

	/**
	 * 创建通知栏消息
	 */
	@SuppressWarnings("deprecation")
	public static void createNotificationMessage(final Context context, String title, String content, Intent intent)
	{
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		// 构建一个通知对象(需要传递的参数有三个,分别是图标,标题和 时间)
		Notification notification = new Notification(R.drawable.ic_launcher, content, System.currentTimeMillis());
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(context.getApplicationContext(), title, content, pendingIntent);
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		notification.defaults |= Notification.DEFAULT_SOUND;
		manager.notify((int) (System.currentTimeMillis() * 1.0 / Long.MAX_VALUE) * Integer.MAX_VALUE, notification);
	}

	/**
	 * 创建下线通知栏消息
	 * 
	 * @param context
	 * @param title
	 * @param content
	 */
	@SuppressWarnings("deprecation")
	public static void createKickOffMessage(final Context context, String title, String content)
	{
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		// 构建一个通知对象(需要传递的参数有三个,分别是图标,标题和 时间)
		Notification notification = new Notification(R.drawable.ic_launcher, content, System.currentTimeMillis());
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(context.getApplicationContext(), title, content, pendingIntent);
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		boolean flag = (Boolean) SPUtil.get(context, IS_SHOW_PUSH_VOICE, true);
		if (flag)
		{
			notification.defaults |= Notification.DEFAULT_SOUND;
		}
		manager.notify((int) (System.currentTimeMillis() * 1.0 / Long.MAX_VALUE) * Integer.MAX_VALUE, notification);
	}
}
