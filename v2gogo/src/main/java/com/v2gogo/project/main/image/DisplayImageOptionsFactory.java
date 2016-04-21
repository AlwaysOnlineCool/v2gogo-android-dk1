package com.v2gogo.project.main.image;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.v2gogo.project.R;

/**
 * 图片显示选项工厂类
 * 
 * @author houjun
 */
public class DisplayImageOptionsFactory
{

	private static final int defaultImageId = R.drawable.loading_image_default_fe;
	private static final int emptyImageId = R.drawable.loading_image_default_fe;
	private static final int errorImageId = R.drawable.loading_image_default_fe;

	private DisplayImageOptionsFactory()
	{
		throw new UnsupportedOperationException("can't init");
	}

	/**
	 * 得到一个默认图片显示选项
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static DisplayImageOptions getDefaultDisplayImageOptions()
	{
		DisplayImageOptions options = new DisplayImageOptions.Builder().showImageForEmptyUri(emptyImageId).showImageOnFail(errorImageId)
				.showImageOnLoading(defaultImageId).cacheInMemory(true).cacheOnDisc(true).considerExifParams(true)
				// 是否考虑JPEG图像EXIF参数（旋转，翻转）
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
				.resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
				.displayer(new FadeInBitmapDisplayer(100, true, false, false)).build();// 构建完成
		return options;
	}

	/**
	 * 得到一个默认图片显示选项
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static DisplayImageOptions getUserAvatarImageOptions()
	{
		DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true).considerExifParams(true)
		// 是否考虑JPEG图像EXIF参数（旋转，翻转）
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
				.resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
				.displayer(new FadeInBitmapDisplayer(100, true, false, false)).build();// 构建完成
		return options;
	}

	/**
	 * 得到一个默认图片显示选项
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static DisplayImageOptions getSliderDisplayImageOptions()
	{
		DisplayImageOptions options = new DisplayImageOptions.Builder().showImageForEmptyUri(emptyImageId).showImageOnFail(errorImageId)
				.showImageOnLoading(defaultImageId).cacheInMemory(true).cacheOnDisc(true).considerExifParams(true)
				// 是否考虑JPEG图像EXIF参数（旋转，翻转）
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
				.resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
				.displayer(new FadeInBitmapDisplayer(100, true, false, false)).build();// 构建完成
		return options;
	}

	/**
	 * 得到一个默认图片显示选项
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static DisplayImageOptions getShakeAdsDisplayImageOptions()
	{
		DisplayImageOptions options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.yaoyaole_bg).showImageOnFail(R.drawable.yaoyaole_bg)
				.showImageOnLoading(R.drawable.yaoyaole_bg).cacheInMemory(true).cacheOnDisc(true).considerExifParams(true)
				// 是否考虑JPEG图像EXIF参数（旋转，翻转）
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
				.resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
				.displayer(new FadeInBitmapDisplayer(100, true, false, false)).build();// 构建完成
		return options;
	}

	/**
	 * 得到轮播图片显示选项
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static DisplayImageOptions getAvatarDisplayImageOptions(int radius)
	{
		DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.user_icons_user_orange)
				.showImageForEmptyUri(R.drawable.user_icons_user_orange).showImageOnFail(R.drawable.user_icons_user_orange).cacheInMemory(true)
				.cacheOnDisc(false).considerExifParams(true)
				// 是否考虑JPEG图像EXIF参数（旋转，翻转）
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
				.resetViewBeforeLoading(false)// 设置图片在下载前是否重置，复位
				.displayer(new FadeInBitmapDisplayer(100, true, false, false)).displayer(new RoundedBitmapDisplayer(radius)).build();// 构建完成
		return options;
	}

	/**
	 * 加载本地资源文件的DisplayImageOptions
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static DisplayImageOptions getDrawableDisplayImageOptions()
	{
		DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(false).considerExifParams(true)
		// 是否考虑JPEG图像EXIF参数（旋转，翻转）
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
				.resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
				.displayer(new FadeInBitmapDisplayer(0));// 是否图片加载好后渐入的动画时间
		return builder.build();
	}

	@SuppressWarnings("deprecation")
	public static DisplayImageOptions getDefaultDisplayImageOptionsWithNoPhoto(boolean isCacheDisk)
	{
		DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(isCacheDisk).considerExifParams(true)
		// 是否考虑JPEG图像EXIF参数（旋转，翻转）
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
				.resetViewBeforeLoading(false)// 设置图片在下载前是否重置，复位
				.displayer(new FadeInBitmapDisplayer(0));// 是否图片加载好后渐入的动画时间
		return builder.build();
	}
}
