package com.v2gogo.project.views;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.v2gogo.project.R;
import com.v2gogo.project.utils.common.DensityUtil;
import com.v2gogo.project.utils.common.ScreenUtil;
import com.v2gogo.project.views.progressbar.NumberProgressBar;

public class UploadProgressPopupWindow extends PopupWindow
{

	private NumberProgressBar mNumberProgressBar;

	public UploadProgressPopupWindow(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(context);
	}

	public UploadProgressPopupWindow(Context context)
	{
		super(context);
		init(context);
	}

	@SuppressWarnings("deprecation")
	private void init(Context context)
	{
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View view = layoutInflater.inflate(R.layout.upload_progress_popup_window_layout, null);
		mNumberProgressBar = (NumberProgressBar) view.findViewById(R.id.upload_progress_pupop_window_progress);
		this.setWidth(ScreenUtil.getScreenWidth(context));
		this.setHeight(DensityUtil.dp2px(context, 36f));
		this.setContentView(view);
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		this.setBackgroundDrawable(new BitmapDrawable());
		mNumberProgressBar.setProgressTextSize(24f);
		mNumberProgressBar.setReachedBarHeight(5f);
		mNumberProgressBar.setUnreachedBarHeight(5f);
		mNumberProgressBar.setUnreachedBarColor(0xffffffff);
	}

	public void show(View view, int offestX, int offestY)
	{
		try
		{
			this.showAsDropDown(view, offestX, offestX);
		}
		catch (Exception exception)
		{
		}
	}

	@Override
	public void dismiss()
	{
		try
		{
			super.dismiss();
		}
		catch (Exception exception)
		{
		}
	}

	public void setProgress(int progress)
	{
		mNumberProgressBar.setProgress(progress);
	}
}
