package com.v2gogo.project.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

public class NoFageColorViewPager extends ViewPager
{

	public NoFageColorViewPager(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	public NoFageColorViewPager(Context context)
	{
		super(context);
		init();
	}

	@SuppressLint("NewApi")
	private void init()
	{
		int version = Build.VERSION.SDK_INT;
		if (version >= Build.VERSION_CODES.GINGERBREAD)
		{
			this.setOverScrollMode(View.OVER_SCROLL_NEVER);
		}
	}

}
