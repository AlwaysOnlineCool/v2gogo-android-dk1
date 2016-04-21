package com.v2gogo.project.utils.common;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * 图片选择
 * 
 * @author
 */
public class PhotoUtil
{

	public static final int ALUMB_REQUEST_CODE = 0x5;
	public static final int CAREMA_REQUEST_CODE = 0x6;
	public static final int CROP_REQUEST_CODE = 0x07;
	public static final int CAREAM_CROP_REQUEST_CODE = 0X08;
	// 爆料返回标志定义
	public static final int FACT_VIDEO_REQUEST_CODE = 9;// 视频录制
	public static final int FACT_VIDEO_ALUMB_REQUEST_CODE = 10;// 视频选择
	public static final int FACT_IMG_ALUMB_REQUEST_CODE = 11;// 图片选择
	public static final int FACT_CAREMA_IMG_REQUEST_CODE = 12;// 拍照
	public static final int FACT_VOICE_REQUEST_CODE = 13;// 录制语音

	private static final String AVATAR = "avatar.jpg";// 图片
	private static final String VIDEO = "video.mp4";// 视频格式
	private static final String VOICE = "voice.mp3";// 音频格式
	public static String fileName = "";

	/**
	 * 跳转到相册
	 * 
	 * @param activity
	 */
	public static void forword2Alumb(Activity activity)
	{
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		if (AppUtil.isIntentExist(activity, intent))
		{
			activity.startActivityForResult(intent, ALUMB_REQUEST_CODE);
		}
	}

	/**
	 * 照转到拍照
	 * 
	 * @param activity
	 */
	public static void forwrd2Camera(Activity activity)
	{
		File file = new File(SDCardUtil.getSDCardPath(), AVATAR);
		if (file.exists())
		{
			file.delete();
		}
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		if (AppUtil.isIntentExist(activity, intent))
		{
			activity.startActivityForResult(intent, CAREMA_REQUEST_CODE);
		}
	}

	/**
	 * 爆料跳转到相册
	 * 
	 * @param activity
	 */
	public static void forword2FactAlumb(Activity activity)
	{
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		if (AppUtil.isIntentExist(activity, intent))
		{
			activity.startActivityForResult(intent, FACT_IMG_ALUMB_REQUEST_CODE);
		}
	}

	/**
	 * 跳转到视频选择
	 * 
	 * @param activity
	 */
	public static void forword2VideoAlumb(Activity activity)
	{
		Intent intent = new Intent();
		intent.setType("video/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		if (AppUtil.isIntentExist(activity, intent))
		{
			activity.startActivityForResult(intent, FACT_VIDEO_ALUMB_REQUEST_CODE);
		}
	}

	/**
	 * 爆料照转到拍照
	 * 
	 * @param activity
	 */
	public static void forwrd2FactCamera(Activity activity)
	{
		File file = new File(SDCardUtil.getSDCardPath(), AVATAR);
		if (file.exists())
		{
			file.delete();
		}
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		if (AppUtil.isIntentExist(activity, intent))
		{
			activity.startActivityForResult(intent, FACT_CAREMA_IMG_REQUEST_CODE);
		}
	}

	/**
	 * 照转到录制视频
	 * 
	 * @param activity
	 */
	public static void forwrd2FactVideo(Activity activity)
	{
		File file = new File(SDCardUtil.getSDCardPath(), VIDEO);
		if (file.exists())
		{
			file.delete();
		}
		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		if (AppUtil.isIntentExist(activity, intent))
		{
			activity.startActivityForResult(intent, FACT_VIDEO_REQUEST_CODE);
		}
	}

	/**
	 * 照转到录制语音
	 * 
	 * @param activity
	 */
	public static void forwrd2FactVoice(Activity activity)
	{
		File file = new File(SDCardUtil.getSDCardPath(), VOICE);
		if (file.exists())
		{
			file.delete();
		}
		Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
		// intent.setType(MediaStore.AUDIO_AMR); //String AUDIO_AMR = "audio/amr";
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		if (AppUtil.isIntentExist(activity, intent))
		{
			activity.startActivityForResult(intent, FACT_VOICE_REQUEST_CODE);
		}

	}

	/**
	 * 跳转到图片剪切
	 * 
	 * @param activity
	 * @param filePath
	 */
	public static void forward2Crop(Activity activity, Uri uri)
	{
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 320);
		intent.putExtra("outputY", 320);
		intent.putExtra("outputFormat", "JPEG");
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("return-data", true);
		if (AppUtil.isIntentExist(activity, intent))
		{
			activity.startActivityForResult(intent, CROP_REQUEST_CODE);
		}
	}

	public static void cameraCropImageUri(Activity activity, Uri uri)
	{
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/jpeg");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 320);
		intent.putExtra("outputY", 320);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true);
		if (AppUtil.isIntentExist(activity, intent))
		{
			activity.startActivityForResult(intent, CAREAM_CROP_REQUEST_CODE);
		}
	}

	/**
	 * 返回拍照临时文件路径
	 * 
	 * @return
	 */
	public static String getAvatarPath()
	{
		File file = new File(SDCardUtil.getSDCardPath(), AVATAR);
		return file.getAbsolutePath();
	}

	/**
	 * 返回视频压缩文件路径
	 * 
	 * @return
	 */
	public static String getFactVideoPath()
	{
		File file = new File(SDCardUtil.getSDCardPath(), VIDEO);
		return file.getAbsolutePath();
	}

}
