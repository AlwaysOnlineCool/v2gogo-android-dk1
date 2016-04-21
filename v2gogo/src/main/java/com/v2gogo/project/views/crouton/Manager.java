package com.v2gogo.project.views.crouton;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

final class Manager extends Handler
{
	private static final class Messages
	{
		private Messages()
		{
		}

		public static final int DISPLAY_CROUTON = 0xc2007;
		public static final int ADD_CROUTON_TO_VIEW = 0xc20074dd;
		public static final int REMOVE_CROUTON = 0xc2007de1;
	}

	private static Manager INSTANCE;

	private final Queue<Crouton> croutonQueue;

	private Manager()
	{
		croutonQueue = new LinkedBlockingQueue<Crouton>();
	}

	static synchronized Manager getInstance()
	{
		if (null == INSTANCE)
		{
			INSTANCE = new Manager();
		}

		return INSTANCE;
	}

	void add(Crouton crouton)
	{
		croutonQueue.add(crouton);
		displayCrouton();
	}

	private void displayCrouton()
	{
		if (croutonQueue.isEmpty())
		{
			return;
		}

		final Crouton currentCrouton = croutonQueue.peek();

		if (null == currentCrouton.getActivity())
		{
			croutonQueue.poll();
		}

		if (!currentCrouton.isShowing())
		{
			sendMessage(currentCrouton, Messages.ADD_CROUTON_TO_VIEW);
			if (null != currentCrouton.getLifecycleCallback())
			{
				currentCrouton.getLifecycleCallback().onDisplayed();
			}
		}
		else
		{
			sendMessageDelayed(currentCrouton, Messages.DISPLAY_CROUTON, calculateCroutonDuration(currentCrouton));
		}
	}

	private long calculateCroutonDuration(Crouton crouton)
	{
		long croutonDuration = crouton.getConfiguration().durationInMilliseconds;
		croutonDuration += crouton.getInAnimation().getDuration();
		croutonDuration += crouton.getOutAnimation().getDuration();
		return croutonDuration;
	}

	private void sendMessage(Crouton crouton, final int messageId)
	{
		final Message message = obtainMessage(messageId);
		message.obj = crouton;
		sendMessage(message);
	}

	private void sendMessageDelayed(Crouton crouton, final int messageId, final long delay)
	{
		Message message = obtainMessage(messageId);
		message.obj = crouton;
		sendMessageDelayed(message, delay);
	}

	@Override
	public void handleMessage(Message message)
	{
		final Crouton crouton = (Crouton) message.obj;
		if (null == crouton)
		{
			return;
		}
		switch (message.what)
		{
			case Messages.DISPLAY_CROUTON:
			{
				displayCrouton();
				break;
			}

			case Messages.ADD_CROUTON_TO_VIEW:
			{
				addCroutonToView(crouton);
				break;
			}

			case Messages.REMOVE_CROUTON:
			{
				removeCrouton(crouton);
				if (null != crouton.getLifecycleCallback())
				{
					crouton.getLifecycleCallback().onRemoved();
				}
				break;
			}

			default:
			{
				super.handleMessage(message);
				break;
			}
		}
	}

	/**
	 * Adds a {@link Crouton} to the {@link ViewParent} of it's {@link Activity}.
	 * 
	 * @param crouton
	 *            The {@link Crouton} that should be added.
	 */
	private void addCroutonToView(final Crouton crouton)
	{
		if (crouton.isShowing())
		{
			return;
		}
		final View croutonView = crouton.getView();
		if (null == croutonView.getParent())
		{
			ViewGroup.LayoutParams params = croutonView.getLayoutParams();
			if (null == params)
			{
				params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			}
			if (null != crouton.getViewGroup())
			{
				final ViewGroup croutonViewGroup = crouton.getViewGroup();
				if (shouldAddViewWithoutPosition(croutonViewGroup))
				{
					croutonViewGroup.addView(croutonView, params);
				}
				else
				{
					croutonViewGroup.addView(croutonView, 0, params);
				}
			}
			else
			{
				Activity activity = crouton.getActivity();
				if (null == activity || activity.isFinishing())
				{
					return;
				}
				handleTranslucentActionBar((ViewGroup.MarginLayoutParams) params, activity);
				handleActionBarOverlay((ViewGroup.MarginLayoutParams) params, activity);

				activity.addContentView(croutonView, params);
			}
		}
		croutonView.requestLayout();
		ViewTreeObserver observer = croutonView.getViewTreeObserver();
		if (null != observer)
		{
			observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
			{
				@SuppressWarnings("deprecation")
				@Override
				@TargetApi(16)
				public void onGlobalLayout()
				{
					if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
					{
						croutonView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
					}
					else
					{
						croutonView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
					}

					if (crouton.getInAnimation() != null)
					{
						croutonView.startAnimation(crouton.getInAnimation());
						announceForAccessibilityCompat(crouton.getActivity(), crouton.getText());
						if (Configuration.DURATION_INFINITE != crouton.getConfiguration().durationInMilliseconds)
						{
							sendMessageDelayed(crouton, Messages.REMOVE_CROUTON, crouton.getConfiguration().durationInMilliseconds
									+ crouton.getInAnimation().getDuration());
						}
					}
				}
			});
		}
	}

	private boolean shouldAddViewWithoutPosition(ViewGroup croutonViewGroup)
	{
		return croutonViewGroup instanceof FrameLayout || croutonViewGroup instanceof AdapterView || croutonViewGroup instanceof RelativeLayout;
	}

	@TargetApi(19)
	private void handleTranslucentActionBar(ViewGroup.MarginLayoutParams params, Activity activity)
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
		{
			final int flags = activity.getWindow().getAttributes().flags;
			final int translucentStatusFlag = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
			if ((flags & translucentStatusFlag) == translucentStatusFlag)
			{
				setActionBarMargin(params, activity);
			}
		}
	}

	@TargetApi(11)
	private void handleActionBarOverlay(ViewGroup.MarginLayoutParams params, Activity activity)
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		{
			final boolean flags = activity.getWindow().hasFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
			if (flags)
			{
				setActionBarMargin(params, activity);
			}
		}
	}

	private void setActionBarMargin(ViewGroup.MarginLayoutParams params, Activity activity)
	{
		final int actionBarContainerId = Resources.getSystem().getIdentifier("action_bar_container", "id", "android");
		final View actionBarContainer = activity.findViewById(actionBarContainerId);
		if (null != actionBarContainer)
		{
			params.topMargin = actionBarContainer.getBottom();
		}
	}

	protected void removeCrouton(Crouton crouton)
	{
		View croutonView = crouton.getView();
		ViewGroup croutonParentView = (ViewGroup) croutonView.getParent();

		if (null != croutonParentView)
		{
			croutonView.startAnimation(crouton.getOutAnimation());
			Crouton removed = croutonQueue.poll();
			croutonParentView.removeView(croutonView);
			if (null != removed)
			{
				removed.detachActivity();
				removed.detachViewGroup();
				if (null != removed.getLifecycleCallback())
				{
					removed.getLifecycleCallback().onRemoved();
				}
				removed.detachLifecycleCallback();
			}
			sendMessageDelayed(crouton, Messages.DISPLAY_CROUTON, crouton.getOutAnimation().getDuration());
		}
	}

	void removeCroutonImmediately(Crouton crouton)
	{
		if (null != crouton.getActivity() && null != crouton.getView() && null != crouton.getView().getParent())
		{
			((ViewGroup) crouton.getView().getParent()).removeView(crouton.getView());
			removeAllMessagesForCrouton(crouton);
		}
		final Iterator<Crouton> croutonIterator = croutonQueue.iterator();
		while (croutonIterator.hasNext())
		{
			final Crouton c = croutonIterator.next();
			if (c.equals(crouton) && (null != c.getActivity()))
			{
				removeCroutonFromViewParent(crouton);
				removeAllMessagesForCrouton(c);
				croutonIterator.remove();
				break;
			}
		}
	}

	void clearCroutonQueue()
	{
		removeAllMessages();
		for (Crouton crouton : croutonQueue)
		{
			if (null != crouton)
			{
				removeCroutonFromViewParent(crouton);
			}
		}
		croutonQueue.clear();
	}

	void clearCroutonsForActivity(Activity activity)
	{
		Iterator<Crouton> croutonIterator = croutonQueue.iterator();
		while (croutonIterator.hasNext())
		{
			Crouton crouton = croutonIterator.next();
			if ((null != crouton.getActivity()) && crouton.getActivity().equals(activity))
			{
				removeCroutonFromViewParent(crouton);
				removeAllMessagesForCrouton(crouton);
				croutonIterator.remove();
			}
		}
	}

	private void removeCroutonFromViewParent(Crouton crouton)
	{
		if (crouton.isShowing())
		{
			ViewGroup parent = (ViewGroup) crouton.getView().getParent();
			if (null != parent)
			{
				parent.removeView(crouton.getView());
			}
		}
	}

	private void removeAllMessages()
	{
		removeMessages(Messages.ADD_CROUTON_TO_VIEW);
		removeMessages(Messages.DISPLAY_CROUTON);
		removeMessages(Messages.REMOVE_CROUTON);
	}

	private void removeAllMessagesForCrouton(Crouton crouton)
	{
		removeMessages(Messages.ADD_CROUTON_TO_VIEW, crouton);
		removeMessages(Messages.DISPLAY_CROUTON, crouton);
		removeMessages(Messages.REMOVE_CROUTON, crouton);

	}

	@SuppressLint("InlinedApi")
	public static void announceForAccessibilityCompat(Context context, CharSequence text)
	{
		if (Build.VERSION.SDK_INT >= 4)
		{
			AccessibilityManager accessibilityManager = null;
			if (null != context)
			{
				accessibilityManager = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
			}
			if (null == accessibilityManager || !accessibilityManager.isEnabled())
			{
				return;
			}
			final int eventType;
			if (Build.VERSION.SDK_INT < 16)
			{
				eventType = AccessibilityEvent.TYPE_VIEW_FOCUSED;
			}
			else
			{
				eventType = AccessibilityEvent.TYPE_ANNOUNCEMENT;
			}
			final AccessibilityEvent event = AccessibilityEvent.obtain(eventType);
			event.getText().add(text);
			event.setClassName(Manager.class.getName());
			event.setPackageName(context.getPackageName());
			accessibilityManager.sendAccessibilityEvent(event);
		}
	}

	@Override
	public String toString()
	{
		return "Manager{" + "croutonQueue=" + croutonQueue + '}';
	}
}
