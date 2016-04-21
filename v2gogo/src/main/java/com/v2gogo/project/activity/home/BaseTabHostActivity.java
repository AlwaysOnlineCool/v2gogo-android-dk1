package com.v2gogo.project.activity.home;

import android.annotation.TargetApi;
import android.app.TabActivity;
import android.os.Build;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.v2gogo.project.R;
import com.v2gogo.project.hardware.SystemBarTintManager;

@SuppressWarnings("deprecation")
public class BaseTabHostActivity extends TabActivity
{

	/**
	 * 设置状态栏的颜色
	 */
	protected void setStatusBarbg()
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
		{
			setTranslucentStatus(true);
		}
		SystemBarTintManager systemBarTintManager = new SystemBarTintManager(this);
		systemBarTintManager.setStatusBarTintEnabled(true);
		systemBarTintManager.setStatusBarTintResource(R.color.status_bar_color);
	}

	@TargetApi(19)
	private void setTranslucentStatus(boolean on)
	{
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on)
		{
			winParams.flags |= bits;
		}
		else
		{
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	protected void setActionBarHeight()
	{
		RelativeLayout actionBarRelativeLayout = (RelativeLayout) findViewById(R.id.common_app_action_bar);
		if (null != actionBarRelativeLayout)
		{
			ViewGroup.LayoutParams layoutParams = null;
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
			{
				layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.action_bar_height_ex));
			}
			else
			{
				layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.action_bar_height));
			}
			actionBarRelativeLayout.setLayoutParams(layoutParams);
		}
	}
}
