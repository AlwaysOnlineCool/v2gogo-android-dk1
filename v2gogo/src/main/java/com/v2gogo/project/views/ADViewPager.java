package com.v2gogo.project.views;

import java.lang.reflect.Field;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.BaseAdapter;
import android.widget.Scroller;

import com.v2gogo.project.utils.common.LogUtil;

/**
 * 大图轮播显示器
 * 
 * @author houjun
 */
public class ADViewPager extends ViewPager
{
	private OnPageChangeListener listener;
	private BaseAdapter baseAdapter;

	private int base = 10000;
	private int delayMillis;
	private Handler handler;

	private boolean isPlaying = false;
	private boolean isScrolling = false;
	private boolean isCanScroll = true;
	private ScrollerCustomDuration mScroller = null;

	private Runnable runnable = new Runnable()
	{
		@Override
		public void run()
		{
			if (isPlaying)
			{
				handler.postDelayed(runnable, delayMillis);
				LogUtil.d("isplaying>>>>>>");
				if (baseAdapter != null && !isScrolling)
				{
					ADViewPager.this.setCurrentItem(ADViewPager.this.getCurrentItem() + 1);
				}
			}
		}
	};

	public ADViewPager(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(context);
	}

	public ADViewPager(Context context)
	{
		super(context);
		init(context);
	}

	private void init(Context context)
	{
		if (Build.VERSION.SDK_INT >= 9)
		{
			this.setOverScrollMode(View.OVER_SCROLL_NEVER);
		}
		handler = new Handler();
		try
		{
			Class<?> viewpager = ViewPager.class;
			Field scroller = viewpager.getDeclaredField("mScroller");
			scroller.setAccessible(true);
			mScroller = new ScrollerCustomDuration(getContext(), new AccelerateDecelerateInterpolator());
			scroller.set(this, mScroller);
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 开始循环播放
	 * 
	 * @param delayMillis
	 */
	public void play(int delayMillis)
	{
		this.delayMillis = delayMillis;
		handler.removeCallbacks(runnable);
		isPlaying = true;
		handler.postDelayed(runnable, delayMillis);
	}

	/**
	 * 是否在播放中
	 * 
	 * @return
	 */
	public boolean isPlaying()
	{
		return isPlaying;
	}

	/**
	 * 停止播放
	 */
	public void stop()
	{
		isPlaying = false;
		handler.removeCallbacks(runnable);
	}

	public int getCount()
	{
		return baseAdapter.getCount();
	}

	public void setScanScroll(boolean isCanScroll)
	{
		this.isCanScroll = isCanScroll;
	}

	@Override
	public void scrollTo(int x, int y)
	{
		if (isCanScroll)
		{
			super.scrollTo(x, y);
		}
	}

	/**
	 * 设置当前位置
	 */
	public void setCurrentItem(int currentItem)
	{
		if (baseAdapter != null)
		{
			int superCurrentItem = super.getCurrentItem();
			int childItem = getCurrentItem();
			super.setCurrentItem(superCurrentItem + (currentItem - childItem));
		}
		else
		{
			super.setCurrentItem(currentItem);
		}
	}

	/**
	 * 得到当前位置
	 */
	public int getCurrentItem()
	{
		if (baseAdapter != null && baseAdapter.getCount() != 0)
		{
			return ((super.getCurrentItem() - baseAdapter.getCount() * base / 2) % baseAdapter.getCount() + baseAdapter.getCount()) % baseAdapter.getCount();
		}
		else
		{
			return super.getCurrentItem();
		}
	}

	@Override
	public void setAdapter(PagerAdapter pagerAdapter)
	{
		this.listener = null;
		this.baseAdapter = null;
		super.setAdapter(pagerAdapter);
	}

	public void setAdapter(BaseAdapter baseAdapter)
	{
		if (isPlaying)
		{
			stop();
		}
		this.baseAdapter = baseAdapter;
		super.setAdapter(new MyAdapter(baseAdapter));
		super.setCurrentItem(baseAdapter.getCount() * base / 2);
		super.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	private class MyAdapter extends PagerAdapter
	{
		private View cacheView;
		private int count;
		private BaseAdapter baseAdapter;

		public MyAdapter(BaseAdapter baseAdapter)
		{
			this.baseAdapter = baseAdapter;
		}

		private View getCacheView()
		{
			View view = cacheView;
			cacheView = null;
			return view;
		}

		@Override
		public int getCount()
		{
			count = baseAdapter.getCount();
			return count * base;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1)
		{
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View container, int position, Object object)
		{
			View view = (View) object;
			((ViewPager) container).removeView(view);
			cacheView = view;
		}

		@Override
		public Object instantiateItem(View container, int position)
		{
			View cacheView = getCacheView();
			cacheView = baseAdapter.getView(((position - count * base / 2) % count + count) % count, cacheView, null);
			((ViewPager) container).addView(cacheView);
			return cacheView;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0)
	{
		ViewParent viewParent = getParent();
		switch (arg0.getAction())
		{
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				if (viewParent != null)
				{
					viewParent.requestDisallowInterceptTouchEvent(false);
				}
				break;

			case MotionEvent.ACTION_MOVE:
			case MotionEvent.ACTION_DOWN:
				if (viewParent != null)
				{
					viewParent.requestDisallowInterceptTouchEvent(true);
				}
				break;

			default:
				break;
		}
		return super.onTouchEvent(arg0);
	}

	private class MyOnPageChangeListener implements OnPageChangeListener
	{
		@Override
		public void onPageScrollStateChanged(int arg0)
		{
			if (baseAdapter != null)
			{
				if (listener != null)
				{
					listener.onPageScrollStateChanged(arg0);
				}
			}
			switch (arg0)
			{
				case SCROLL_STATE_IDLE:
					isScrolling = false;
					break;
				default:
					isScrolling = true;
					break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2)
		{
			if (listener != null && baseAdapter != null)
			{
				listener.onPageScrolled(
						((arg0 - baseAdapter.getCount() * base / 2) % baseAdapter.getCount() + baseAdapter.getCount()) % baseAdapter.getCount(), arg1, arg2);
			}
		}

		@Override
		public void onPageSelected(int arg0)
		{
			if (listener != null && baseAdapter != null)
			{
				listener.onPageSelected(((arg0 - baseAdapter.getCount() * base / 2) % baseAdapter.getCount() + baseAdapter.getCount()) % baseAdapter.getCount());
			}
		}
	}

	@Override
	public void setOnPageChangeListener(OnPageChangeListener listener)
	{
		if (baseAdapter != null)
		{
			this.listener = listener;
		}
		else
		{
			super.setOnPageChangeListener(listener);
		}
	}

	public class ScrollerCustomDuration extends Scroller
	{

		public ScrollerCustomDuration(Context context)
		{
			super(context);
		}

		public ScrollerCustomDuration(Context context, Interpolator interpolator)
		{
			super(context, interpolator);
		}

		@Override
		public void startScroll(int startX, int startY, int dx, int dy, int duration)
		{
			super.startScroll(startX, startY, dx, dy, 300);
		}
	}

}
