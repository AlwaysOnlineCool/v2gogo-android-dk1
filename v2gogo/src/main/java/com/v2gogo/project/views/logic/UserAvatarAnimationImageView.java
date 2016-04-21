package com.v2gogo.project.views.logic;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Scroller;

/**
 * 用户头像显示动画控件
 * 
 * @author houjun
 */
public class UserAvatarAnimationImageView extends ImageView implements OnGestureListener
{

	private int m_avatarViewWidth;
	private PointF m_location;
	private Runnable m_runnable;
	private Activity m_activity;

	private boolean isFirst = true;
	private Handler m_handler;
	private boolean isAnimation = false;
	private boolean isNeedClose = false;
	private Bitmap m_bitmap;
	private Matrix m_matrix;
	private OnBackgroundChangedListener m_onBackgroundChangedListener;
	private OnLongPressListener m_onLongPressListener;

	/**
	 * 动画持续时间
	 */
	private static final int ANIMATION_TIME = 250;

	private GestureDetector gestureDetector;

	public void setOnBackgroundChangedListener(OnBackgroundChangedListener onBackgroundChangedListener)
	{
		this.m_onBackgroundChangedListener = onBackgroundChangedListener;
	}

	public UserAvatarAnimationImageView(Context context)
	{
		super(context);
	}

	public UserAvatarAnimationImageView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public void init(Activity context, int avatarViewWidth, PointF location)
	{
		this.m_activity = context;
		this.setScaleType(ScaleType.MATRIX);
		m_handler = new Handler();
		this.m_avatarViewWidth = avatarViewWidth;
		this.m_location = location;
		m_matrix = new Matrix();
		gestureDetector = new GestureDetector(m_activity, this);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		gestureDetector.onTouchEvent(event);
		return true;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom)
	{
		super.onLayout(changed, left, top, right, bottom);
		if (m_runnable != null)
		{
			m_runnable.run();
			m_runnable = null;
		}
	}

	private void startAnimation(PointF fromLocation, PointF toLocation, float fromScale, float toScale, int fromAlpha, int toAlpha)
	{
		if (isAnimation)
		{
			return;
		}
		isAnimation = true;
		final Scroller locationSroller = new Scroller(getContext(), new LinearInterpolator());
		final Scroller scaleSroller = new Scroller(getContext(), new LinearInterpolator());
		locationSroller.startScroll((int) fromLocation.x, (int) fromLocation.y, (int) (toLocation.x - fromLocation.x), (int) (toLocation.y - fromLocation.y),
				ANIMATION_TIME);
		scaleSroller.startScroll((int) (fromScale * 10000), fromAlpha, (int) ((toScale - fromScale) * 10000), toAlpha - fromAlpha, ANIMATION_TIME);
		m_handler.post(new Runnable()
		{
			@Override
			public void run()
			{
				scaleSroller.computeScrollOffset();
				if (locationSroller.computeScrollOffset())
				{
					m_matrix.reset();
					m_matrix.setScale((float) scaleSroller.getCurrX() / 10000, (float) scaleSroller.getCurrX() / 10000);
					m_matrix.postTranslate(locationSroller.getCurrX(), locationSroller.getCurrY());
					setImageMatrix(m_matrix);
					int color = Color.argb(scaleSroller.getCurrY(), 0, 0, 0);
					setBackgroundColor(color);
					if (m_onBackgroundChangedListener != null)
					{
						m_onBackgroundChangedListener.onBackgroundChanged(color);
					}
					m_handler.post(this);
				}
				else
				{
					isAnimation = false;
					if (isNeedClose)
					{
						m_activity.finish();
					}
					else if (m_runnable != null)
					{
						m_runnable.run();
						m_runnable = null;
					}
				}
			}
		});

	}

	public void setImageBitmap(final Bitmap bitmap)
	{
		if (bitmap == null)
		{
			return;
		}
		if (getWidth() == 0 || isAnimation)
		{
			m_runnable = new Runnable()
			{
				@Override
				public void run()
				{
					setImageBitmap(bitmap);
				}
			};
		}
		else
		{
			if (isFirst)
			{
				isFirst = false;
				PointF pointF = new PointF(0, ((float) ((float) getHeight() - (float) getWidth()) / (float) 2));

				float toScale = (float) getWidth() / (float) bitmap.getWidth();
				float fromScale = (float) m_avatarViewWidth / (float) bitmap.getWidth();

				super.setImageBitmap(bitmap);

				m_matrix.reset();

				m_matrix.setScale(fromScale, fromScale);
				m_matrix.postTranslate(m_location.x, m_location.y);
				setImageMatrix(m_matrix);
				m_bitmap = bitmap;
				startAnimation(m_location, pointF, fromScale, toScale, 0, 255);
			}
			else
			{
				m_matrix.reset();

				float scale = (float) getWidth() / (float) bitmap.getWidth();

				m_matrix.setScale(scale, scale);
				m_matrix.postTranslate(0, ((float) (getHeight() - getWidth()) / (float) 2));

				super.setImageBitmap(bitmap);
				m_bitmap = bitmap;
				setImageMatrix(m_matrix);
			}
		}
	}

	/**
	 * 返回显示图片
	 * 
	 * @return
	 */
	public Bitmap getBitmap()
	{
		return m_bitmap;
	}

	@Override
	public boolean onDown(MotionEvent e)
	{
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e)
	{
	}

	public void close()
	{
		if (m_bitmap == null || isAnimation)
		{
			m_activity.finish();
			return;
		}
		isNeedClose = true;
		PointF pointF = new PointF(0, ((float) ((float) getHeight() - (float) getWidth()) / (float) 2));
		float fromScale = (float) getWidth() / (float) m_bitmap.getWidth();
		float toScale = (float) m_avatarViewWidth / (float) m_bitmap.getWidth();
		startAnimation(pointF, m_location, fromScale, toScale, 255, 0);
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e)
	{
		close();
		return true;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
	{
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e)
	{
		if (m_onLongPressListener != null)
		{
			m_onLongPressListener.onLongPress();
		}
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
	{
		return false;
	}

	public void setOnLongPressListener(OnLongPressListener listener)
	{
		m_onLongPressListener = listener;
	}

	public interface OnLongPressListener
	{
		void onLongPress();
	}

	public static interface OnBackgroundChangedListener
	{
		public void onBackgroundChanged(int color);
	}
}
