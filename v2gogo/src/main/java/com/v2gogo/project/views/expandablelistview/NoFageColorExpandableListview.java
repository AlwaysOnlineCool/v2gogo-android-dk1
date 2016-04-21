package com.v2gogo.project.views.expandablelistview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ExpandableListView;

/**
 * 换到边缘没有蓝色颜色的ExpandableListView
 * 
 * @author houjun
 */
public class NoFageColorExpandableListview extends ExpandableListView
{

	public NoFageColorExpandableListview(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init();
	}

	public NoFageColorExpandableListview(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	public NoFageColorExpandableListview(Context context)
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
