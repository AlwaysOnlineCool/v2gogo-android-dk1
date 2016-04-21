package com.v2gogo.project.views.gridview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

public class MyGridView extends GridView
{

	/**
	 * @param context
	 */
	public MyGridView(Context context)
	{
		super(context, null);
	}

	public MyGridView(Context context, AttributeSet attrs)
	{
		super(context, attrs, 0);
	}

	public MyGridView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int expandSpec = heightMeasureSpec;
		expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);// 注意这里,这里的意思是直接测量出GridView的高度
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{

		if (ev.getAction() == MotionEvent.ACTION_MOVE)
		{
			return true;
		}
		return super.dispatchTouchEvent(ev);
	}

}
