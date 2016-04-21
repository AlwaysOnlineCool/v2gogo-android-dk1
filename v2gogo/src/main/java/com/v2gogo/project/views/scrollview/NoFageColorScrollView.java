package com.v2gogo.project.views.scrollview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

/**
 * 滑到边界不显示蓝色的ScrollView
 * 
 * @author houjun
 */
public class NoFageColorScrollView extends ScrollView
{

	public NoFageColorScrollView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init();
	}

	public NoFageColorScrollView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	public NoFageColorScrollView(Context context)
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
