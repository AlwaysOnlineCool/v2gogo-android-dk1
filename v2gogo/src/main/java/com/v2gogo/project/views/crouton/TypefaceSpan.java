package com.v2gogo.project.views.crouton;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.util.LruCache;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

public class TypefaceSpan extends MetricAffectingSpan
{
	private static LruCache<String, Typeface> sTypefaceCache = new LruCache<String, Typeface>(5);

	private Typeface mTypeface;

	public TypefaceSpan(Context context, String typefaceName)
	{
		mTypeface = sTypefaceCache.get(typefaceName);
		if (mTypeface == null)
		{
			mTypeface = Typeface.createFromAsset(context.getApplicationContext().getAssets(), String.format("%s", typefaceName));
			sTypefaceCache.put(typefaceName, mTypeface);
		}
	}

	@Override
	public void updateMeasureState(TextPaint p)
	{
		p.setTypeface(mTypeface);
	}

	@Override
	public void updateDrawState(TextPaint tp)
	{
		tp.setTypeface(mTypeface);
	}
}
