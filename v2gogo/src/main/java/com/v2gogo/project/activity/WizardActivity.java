package com.v2gogo.project.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.v2gogo.project.R;
import com.v2gogo.project.adapter.WizardAdapter;
import com.v2gogo.project.main.image.GlideImageLoader;
import com.v2gogo.project.utils.common.SPUtil;
import com.v2gogo.project.utils.http.Constants;
import com.v2gogo.project.views.NoFageColorViewPager;

/**
 * 用户引导界面
 * 
 * @author houjun
 */
public class WizardActivity extends BaseActivity implements OnTouchListener, OnPageChangeListener
{

	private List<View> views;

	private int currentIndex;
	private int lastX = 0;

	private int[] resIds = new int[] { R.drawable.loading1, R.drawable.loading2, R.drawable.loading3 };
	private NoFageColorViewPager mNoFageColorViewPager;
	private WizardAdapter mWizardAdapter;

	@Override
	public void onInitViews()
	{
		mNoFageColorViewPager = (NoFageColorViewPager) findViewById(R.id.wizard_conatiner);
		views = new ArrayList<View>();
		LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
		for (int i = 0; i < resIds.length; i++)
		{
			ImageView iv = new ImageView(this);
			iv.setLayoutParams(mParams);
			GlideImageLoader.loadInternalDrawable(this, resIds[i], iv);
			views.add(iv);
		}
		mWizardAdapter = new WizardAdapter(views);
		mNoFageColorViewPager.setOnTouchListener(this);
		mNoFageColorViewPager.setAdapter(mWizardAdapter);
		mNoFageColorViewPager.setOnPageChangeListener(this);
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.wizard_activity_layout;
	}

	@Override
	protected void onInitLoadDatas()
	{
		super.onInitLoadDatas();
		SPUtil.put(this, Constants.IS_FIRST, false);
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent event)
	{
		switch (event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				lastX = (int) event.getX();
				break;

			case MotionEvent.ACTION_MOVE:
				if ((lastX - event.getX()) > 50 && (currentIndex == views.size() - 1))
				{
					Intent intent = new Intent(this, MainTabActivity.class);
					startActivity(intent);
					finish();
				}
				break;

			default:
				break;
		}
		return false;
	}

	@Override
	public void onPageScrollStateChanged(int arg0)
	{
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2)
	{
	}

	@Override
	public void onPageSelected(int position)
	{
		currentIndex = position;
	}

	@Override
	public void clearRequestTask()
	{
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
