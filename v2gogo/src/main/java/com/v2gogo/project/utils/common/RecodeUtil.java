package com.v2gogo.project.utils.common;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;

/**
 * 功能：录音工具类
 * 
 * @ahthor：黄荣星
 * @date:2015-10-4
 * @version::V1.0
 */
public class RecodeUtil
{
	public static final int RECORD_AUDIO = 10;

	/**
	 * method desc：跳向录音
	 */
	public static void forwordToRecode(Activity activity)
	{
		Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
		activity.startActivityForResult(intent, RECORD_AUDIO);
	}

}
