package io.vov.vitamio.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

public class CustomSeekBar extends SeekBar
{
	private boolean isSeeek;

	public CustomSeekBar(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public CustomSeekBar(Context context)
	{
		super(context);
	}

	public void setSeekAble(boolean isSeek)
	{
		this.isSeeek = isSeek;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (!isSeeek)
		{
			return false;
		}
		else
		{
			return super.onTouchEvent(event);
		}
	}
}
