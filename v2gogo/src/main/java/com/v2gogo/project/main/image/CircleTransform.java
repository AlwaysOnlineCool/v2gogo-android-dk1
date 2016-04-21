package com.v2gogo.project.main.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * 图片圆形Transform
 * 
 * @author houjun
 */
public class CircleTransform extends BitmapTransformation
{

	public CircleTransform(Context context)
	{
		super(context.getApplicationContext());
	}

	@Override
	public String getId()
	{
		return getClass().getName();
	}

	@Override
	protected Bitmap transform(BitmapPool pool, Bitmap source, int arg2, int arg3)
	{
		return circleCrop(pool, source);
	}

	private static Bitmap circleCrop(BitmapPool pool, Bitmap source)
	{
		if (source == null)
			return null;

		int size = Math.min(source.getWidth(), source.getHeight());
		int x = (source.getWidth() - size) / 2;
		int y = (source.getHeight() - size) / 2;

		Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

		Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
		if (result == null)
		{
			result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
		}

		Canvas canvas = new Canvas(result);
		Paint paint = new Paint();
		paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
		paint.setAntiAlias(true);
		float r = size / 2f;
		canvas.drawCircle(r, r, r, paint);

		int borderWidth = 0;
		int borderColor = 0x00ffffff;

		final Paint paint2 = new Paint();
		paint2.setAntiAlias(true);
		paint2.setColor(borderColor);
		paint2.setStrokeWidth(borderWidth);
		paint2.setStyle(Paint.Style.STROKE);
		canvas.drawCircle(source.getWidth() / 2, source.getHeight() / 2, (float) (source.getWidth() / 2 - Math.ceil(borderWidth / 2)), paint2);
		return result;
	}
}
