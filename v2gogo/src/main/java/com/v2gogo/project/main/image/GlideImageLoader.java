package com.v2gogo.project.main.image;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.v2gogo.project.R;
import com.v2gogo.project.main.V2GogoApplication;

/**
 * 加载图片
 * 
 * @author houjun
 */
public class GlideImageLoader
{

	/**
	 * imageview(固定大小的imageview)
	 * 
	 * @param context
	 *            上下文
	 * @param url
	 *            图片url
	 * @param imageView
	 *            显示图片
	 */
	public static void loadImageWithFixedSize(Context context, String url, ImageView imageView)
	{
		Glide.with(context).load(url).centerCrop().dontAnimate().placeholder(R.drawable.loading_image_default_fe).error(R.drawable.loading_image_default_fe)
				.fallback(R.drawable.loading_image_default_fe).into(imageView);
	}

	/**
	 * 加载app的drawable资源
	 * 
	 * @param context
	 *            上下文
	 * @param resId
	 *            资源标示
	 * @param imageView
	 *            显示图片控件
	 */
	public static void loadInternalDrawable(Context context, int resId, ImageView imageView)
	{
		Glide.with(context).load(resId).centerCrop().dontAnimate().diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
	}

	/**
	 * 加载sd卡上的图片资源
	 * 
	 * @param context
	 *            上下文
	 * @param path
	 *            文件路径
	 * @param imageView
	 *            显示图片控件
	 */
	public static void loadLocalFileDrawable(Context context, String path, ImageView imageView)
	{
		Glide.with(context).load(path).centerCrop().dontAnimate().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
	}

	/**
	 * imageview(不固定大小的imageview)
	 * 
	 * @param context
	 *            上下文
	 * @param url
	 *            图片url
	 * @param imageView
	 *            显示图片
	 */
	public static void loadImageWithNoFixedSize(Context context, String url, ImageView imageView)
	{
		Glide.with(context).load(url).centerCrop().dontAnimate().placeholder(R.drawable.loading_image_default_fe).error(R.drawable.loading_image_default_fe)
				.fallback(R.drawable.loading_image_default_fe).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
	}

	public static void loadImageWithCircle(Context context, String url, ImageView imageView)
	{
		Glide.with(context).load(url).centerCrop().dontAnimate().placeholder(R.drawable.home_btn_yaoyiyao).error(R.drawable.home_btn_yaoyiyao)
				.fallback(R.drawable.home_btn_yaoyiyao).diskCacheStrategy(DiskCacheStrategy.ALL).transform(new CircleTransform(context)).into(imageView);
	}

	/**
	 * imageview(不固定大小的imageview)
	 * 
	 * @param context
	 *            上下文
	 * @param url
	 *            图片url
	 * @param imageView
	 *            显示图片
	 */
	public static void loadAvatarImageWithFixedSize(Context context, String url, ImageView imageView)
	{
		Glide.with(context).load(url).centerCrop().dontAnimate().placeholder(R.drawable.user_icons_user_orange).error(R.drawable.user_icons_user_orange)
				.fallback(R.drawable.user_icons_user_orange).transform(new CircleTransform(context)).into(imageView);
	}

	/**
	 * imageview(不固定大小的imageview)
	 * 
	 * @param context
	 *            上下文
	 * @param url
	 *            图片url
	 * @param imageView
	 *            显示图片
	 */
	public static void loadImageWithHomeBottomIcon(Context context, String url, ImageView imageView)
	{
		Glide.with(context).load(url).centerCrop().dontAnimate().placeholder(R.drawable.tab_home).error(R.drawable.tab_home).fallback(R.drawable.tab_home)
				.diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
	}

	@SuppressWarnings("deprecation")
	public static void initGlideImageLoader(Context context)
	{
		if (!Glide.isSetup())
		{
			GlideBuilder glideBuilder = new GlideBuilder(context);
			glideBuilder.setDiskCache(DiskLruCacheWrapper.get(V2GogoApplication.getDiskCacheFile(context), Integer.MAX_VALUE));
			Glide.setup(glideBuilder);
		}
	}

	/**
	 * 内存警告
	 * 
	 * @param context
	 * @param level
	 */
	public static void onTrimMemory(Context context, int level)
	{
		Glide.with(context).onTrimMemory(level);
	}

	/**
	 * 内存警告
	 * 
	 * @param context
	 * @param level
	 */
	public static void onLowMemory(Context context)
	{
		Glide.with(context).onLowMemory();
	}

	public static void onDestory(Context context)
	{
		Glide.with(context).onDestroy();
	}
}
