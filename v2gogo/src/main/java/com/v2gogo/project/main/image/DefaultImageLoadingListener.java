package com.v2gogo.project.main.image;

import android.graphics.Bitmap;
import android.view.View;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 默认的图片加载监听器
 * 
 * @author houjun
 */
public class DefaultImageLoadingListener implements ImageLoadingListener
{

	/**
	 * 加载取消
	 */
	@Override
	public void onLoadingCancelled(String url, View view)
	{

	}

	/**
	 * 加载完成
	 */
	@Override
	public void onLoadingComplete(String url, View view, Bitmap bitmap)
	{

	}

	/**
	 * 加载失败
	 */
	@Override
	public void onLoadingFailed(String url, View view, FailReason arg2)
	{

	}

	/**
	 * 加载开始
	 */
	@Override
	public void onLoadingStarted(String url, View view)
	{

	}
}
