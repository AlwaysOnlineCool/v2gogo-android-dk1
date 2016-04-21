package com.v2gogo.project.views.listview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

/**
 * 滑到边界不会出现蓝色的listview
 * 
 * @author houjun
 */
public class NoFageColorListView extends ListView
{

	public NoFageColorListView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init();
	}

	public NoFageColorListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	public NoFageColorListView(Context context)
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
