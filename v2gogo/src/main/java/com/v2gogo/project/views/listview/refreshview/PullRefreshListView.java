package com.v2gogo.project.views.listview.refreshview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.Scroller;

import com.v2gogo.project.views.listview.NoFageColorListView;

/**
 * 上拉下拉刷新控件
 * 
 * @author houjun
 */
public class PullRefreshListView extends NoFageColorListView
{

	public final static int SCROLL_DURATION_DOWN = 600;
	public final static int SCROLL_DURATION_UP = 400;
	private final static float OFFSET_RADIO = 0.4f;

	private PullRefreshHeader mHeaderView;
	private PullRefreshFooter mFooterView;

	private int mHeaderViewHeight;
	private Scroller mScroller;

	private OnPullRefreshListener onPullRefreshListener;
	private boolean mEnablePullRefresh = false;
	private boolean isRefreshing = false;

	private OnLoadMoreListener mOnLoadMoreListener;

	private boolean mEnableLoadMore = false;

	private boolean isLoagingMore = false;

	private float mLastY = -1;

	private OnScrollListener mOnScrollListener;
	private OnScrollListener mMyOnScrollListener = new OnScrollListener()
	{
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState)
		{
			if (mOnScrollListener != null)
			{
				mOnScrollListener.onScrollStateChanged(view, scrollState);
			}

			if (scrollState == SCROLL_STATE_IDLE)
			{
				if (!isRefreshing && !isLoagingMore && mEnableLoadMore
						&& PullRefreshListView.super.getLastVisiblePosition() == PullRefreshListView.super.getCount() - 1)
				{
					if (mOnLoadMoreListener != null)
					{
						isLoagingMore = true;
						mOnLoadMoreListener.onLoadMore(PullRefreshListView.this);
					}
				}
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
		{
			if (mOnScrollListener != null)
			{
				int visibleCount = visibleItemCount;
				if (PullRefreshListView.super.getLastVisiblePosition() == totalItemCount - 1)
				{
					visibleCount = visibleItemCount - 1;
				}
				mOnScrollListener.onScroll(view, firstVisibleItem, visibleCount, totalItemCount - 1);
			}
		}
	};

	public View getFooterView()
	{
		return mFooterView;
	}

	public int getCount()
	{
		return super.getCount() - 1;
	}

	@Override
	public int getLastVisiblePosition()
	{
		if (super.getLastVisiblePosition() == super.getCount() - 1)
		{
			return super.getLastVisiblePosition() - 1;
		}
		else
		{
			return super.getLastVisiblePosition();
		}
	}

	public PullRefreshListView(Context context)
	{
		super(context);
		initView();
	}

	public PullRefreshListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initView();
	}

	@Override
	protected void onFinishInflate()
	{
		super.onFinishInflate();
		mFooterView = new PullRefreshFooter(getContext());
		addFooterView(mFooterView);
	}

	@Override
	public void setAdapter(ListAdapter adapter)
	{
		super.setAdapter(adapter);
	}

	/**
	 * 设置加载更多监听器
	 * 
	 * @param onLoadMoreListener
	 */
	public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener)
	{
		this.mOnLoadMoreListener = onLoadMoreListener;
		setLoadMoreEnable(true);
	}

	/**
	 * 设置是否可以加载更多
	 * 
	 * @param enable
	 */
	public void setLoadMoreEnable(boolean enable)
	{
		mEnableLoadMore = enable;
		if (!isRefreshing)
		{
			mFooterView.setEnabledLoadMore(enable);
		}
	}

	/**
	 * 设置下拉刷新监听器
	 * 
	 * @param pullRefreshListener
	 */
	public void setOnPullRefreshListener(OnPullRefreshListener pullRefreshListener)
	{
		this.onPullRefreshListener = pullRefreshListener;
		setPullRefreshEnable(true);
	}

	/**
	 * 设置是否可以下拉刷新
	 */
	public void setPullRefreshEnable(boolean enable)
	{
		mEnablePullRefresh = enable;
		if (!mEnablePullRefresh)
		{
			mHeaderView.getContainer().setVisibility(View.INVISIBLE);
		}
		else
		{
			mHeaderView.getContainer().setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 返回是否正在下拉刷新
	 * 
	 * @return
	 */
	public boolean isRefreshing()
	{
		return isRefreshing;
	}

	/**
	 * 是否加载更多
	 * 
	 * @return
	 */
	public boolean isLoadingMore()
	{
		return isLoagingMore;
	}

	public void setOnPullEnable(boolean isable)
	{
		mEnablePullRefresh = isable;
	}

	/**
	 * 停止加载更多
	 */
	public void stopLoadMore()
	{
		if (isLoagingMore)
		{
			isLoagingMore = false;
			if (super.getLastVisiblePosition() == super.getCount() - 1)
			{
				View view = getFooterView();
				if (view != null)
				{
					int top = view.getTop();
					int bottom = getHeight() - getPaddingBottom();
					int dy = bottom - top;
					smoothScrollBy(-dy, SCROLL_DURATION_DOWN);
				}
			}
		}
	}

	/**
	 * 停止
	 */
	public void stopPullRefresh()
	{
		stopPullRefresh(SCROLL_DURATION_UP);
	}

	/**
	 * 停止下拉刷新
	 */
	public void stopPullRefresh(int duration)
	{
		if (isRefreshing)
		{
			isRefreshing = false;
			resetHeaderHeight(duration);
			if (mEnableLoadMore)
			{
				mFooterView.setEnabledLoadMore(true);
			}
			else if (mFooterView.isEnabledLoadMore())
			{
				mFooterView.setEnabledLoadMore(false);
			}
		}
	}

	/**
	 * 开始下拉刷新
	 */
	public void startPullRefresh()
	{
		startPullRefresh(SCROLL_DURATION_DOWN);
	}

	/**
	 * 开始下拉刷新
	 * 
	 * @param 时长
	 */
	public void startPullRefresh(int duration)
	{
		if (!mEnablePullRefresh)
		{
			throw new IllegalStateException("参数异常");
		}
		if (isLoagingMore)
		{
			stopLoadMore();
		}
		if (!isRefreshing)
		{
			setSelection(0);
			isRefreshing = true;
			mHeaderView.setState(PullRefreshHeader.STATE_REFRESHING);
			if (mEnableLoadMore)
			{
				mFooterView.setEnabledLoadMore(false);
			}
			if (onPullRefreshListener != null)
			{
				onPullRefreshListener.onPullDownRefresh(this);
			}
			resetHeaderHeight(duration);
		}
	}

	private void resetHeaderHeight(int duration)
	{
		int finalHeight = 0;
		if (isRefreshing)
		{
			finalHeight = mHeaderViewHeight;
		}
		if (duration != 0)
		{
			startScroller(finalHeight, duration);
		}
		else
		{
			mHeaderView.setVisiableHeight(finalHeight);
		}
	}

	private void startScroller(int finalHeight, int duration)
	{
		int height = mHeaderView.getVisiableHeight();
		mScroller.startScroll(0, height, 0, finalHeight - height, duration);
		invalidate();
	}

	@Override
	public void computeScroll()
	{
		if (isInEditMode())
		{
			return;
		}
		if (mScroller.computeScrollOffset())
		{
			mHeaderView.setVisiableHeight(mScroller.getCurrY());
			postInvalidate();
		}
		super.computeScroll();
	}

	public void initView()
	{
		if (isInEditMode())
		{
			return;
		}
		mScroller = new Scroller(getContext(), new LinearInterpolator());
		mHeaderView = new PullRefreshHeader(getContext());
		mHeaderViewHeight = mHeaderView.getContentHeight();
		addHeaderView(mHeaderView);
		super.setOnScrollListener(mMyOnScrollListener);
	}

	@Override
	public void setOnScrollListener(OnScrollListener l)
	{
		this.mOnScrollListener = l;
	}

	private void updateHeaderHeight(float delta)
	{
		int finalHeight = (int) delta + mHeaderView.getVisiableHeight();
		mHeaderView.setVisiableHeight(finalHeight);
		if (mEnablePullRefresh && !isRefreshing)
		{
			if (mHeaderView.getVisiableHeight() > mHeaderViewHeight)
			{
				mHeaderView.setState(PullRefreshHeader.STATE_RELEASE_REFRESH);
			}
			else
			{
				mHeaderView.setState(PullRefreshHeader.STATE_INIT);
			}
		}
		setSelection(0);
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		if (mEnablePullRefresh && mScroller.isFinished() && !isRefreshing && !isLoagingMore)
		{
			if (mLastY == -1)
			{
				mLastY = ev.getRawY();
			}

			switch (ev.getAction())
			{
				case MotionEvent.ACTION_DOWN:
					mLastY = ev.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					final float deltaY = ev.getRawY() - mLastY;
					mLastY = ev.getRawY();
					if (getFirstVisiblePosition() == 0 && (mHeaderView.getVisiableHeight() > 0 || deltaY > 0))
					{
						updateHeaderHeight(deltaY * OFFSET_RADIO);
					}
					break;

				default:
					mLastY = -1;
					if (getFirstVisiblePosition() == 0)
					{
						if (mHeaderView.getVisiableHeight() > mHeaderViewHeight)
						{
							if (onPullRefreshListener != null)
							{
								isRefreshing = true;
								mHeaderView.setState(PullRefreshHeader.STATE_REFRESHING);
								onPullRefreshListener.onPullDownRefresh(this);
								if (mEnableLoadMore)
								{
									mFooterView.setEnabledLoadMore(false);
								}
							}
						}
						resetHeaderHeight(300);
					}
					break;
			}
		}
		return super.onTouchEvent(ev);
	}
}
