package com.v2gogo.project.views.crouton;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.utils.common.ScreenUtil;

public final class Crouton
{
	private static final String NULL_PARAMETERS_ARE_NOT_ACCEPTED = "Null parameters are not accepted";
	private static final int IMAGE_ID = 0x100;
	private static final int TEXT_ID = 0x101;
	private final CharSequence text;
	private final Style style;
	private Configuration configuration = null;
	private final View customView;

	private OnClickListener onClickListener;

	private Activity activity;
	private ViewGroup viewGroup;
	private FrameLayout croutonView;
	private Animation inAnimation;
	private Animation outAnimation;
	private LifecycleCallback lifecycleCallback = null;

	private Crouton(Activity activity, CharSequence text, Style style)
	{
		if ((activity == null) || text == null || (style == null))
		{
			throw new IllegalArgumentException(NULL_PARAMETERS_ARE_NOT_ACCEPTED);
		}
		this.activity = activity;
		this.viewGroup = null;
		this.text = text;
		this.style = style;
		this.customView = null;
	}

	private Crouton(Activity activity, CharSequence text, Style style, ViewGroup viewGroup)
	{
		if ((activity == null) || text == null || (style == null))
		{
			throw new IllegalArgumentException(NULL_PARAMETERS_ARE_NOT_ACCEPTED);
		}
		this.activity = activity;
		this.text = text;
		this.style = style;
		this.viewGroup = viewGroup;
		this.customView = null;
	}

	private Crouton(Activity activity, View customView)
	{
		if ((activity == null) || (customView == null))
		{
			throw new IllegalArgumentException(NULL_PARAMETERS_ARE_NOT_ACCEPTED);
		}
		this.activity = activity;
		this.viewGroup = null;
		this.customView = customView;
		this.style = new Style.Builder().build();
		this.text = null;
	}

	private Crouton(Activity activity, View customView, ViewGroup viewGroup)
	{
		this(activity, customView, viewGroup, Configuration.DEFAULT);
	}

	private Crouton(final Activity activity, final View customView, final ViewGroup viewGroup, final Configuration configuration)
	{
		if ((activity == null) || (customView == null))
		{
			throw new IllegalArgumentException(NULL_PARAMETERS_ARE_NOT_ACCEPTED);
		}
		this.activity = activity;
		this.customView = customView;
		this.viewGroup = viewGroup;
		this.style = new Style.Builder().build();
		this.text = null;
		this.configuration = configuration;
	}

	public static Crouton makeText(Activity activity, CharSequence text, Style style)
	{
		return new Crouton(activity, text, style);
	}

	public static Crouton makeText(Activity activity, int textResourceId, Style style)
	{
		return makeText(activity, activity.getString(textResourceId), style);
	}

	public static Crouton make(Activity activity, View customView)
	{
		return new Crouton(activity, customView);
	}

	public static Crouton make(Activity activity, View customView, ViewGroup viewGroup)
	{
		return new Crouton(activity, customView, viewGroup);
	}

	public static Crouton make(Activity activity, View customView, int viewGroupResId)
	{
		return new Crouton(activity, customView, (ViewGroup) activity.findViewById(viewGroupResId));
	}

	public static Crouton make(Activity activity, View customView, int viewGroupResId, final Configuration configuration)
	{
		return new Crouton(activity, customView, (ViewGroup) activity.findViewById(viewGroupResId), configuration);
	}

	public static void showText(Activity activity, CharSequence text, Style style)
	{
		makeText(activity, text, style).show();
	}

	public static void show(Activity activity, View customView)
	{
		make(activity, customView).show();
	}

	public static void show(Activity activity, View customView, int viewGroupResId)
	{
		make(activity, customView, viewGroupResId).show();
	}

	public static void showText(Activity activity, int textResourceId, Style style)
	{
		showText(activity, activity.getString(textResourceId), style);
	}

	public static void hide(Crouton crouton)
	{
		crouton.hide();
	}

	public static void cancelAllCroutons()
	{
		Manager.getInstance().clearCroutonQueue();
	}

	public static void clearCroutonsForActivity(Activity activity)
	{
		Manager.getInstance().clearCroutonsForActivity(activity);
	}

	public void cancel()
	{
		Manager manager = Manager.getInstance();
		manager.removeCroutonImmediately(this);
	}

	public void show()
	{
		Manager.getInstance().add(this);
	}

	public Animation getInAnimation()
	{
		if ((null == this.inAnimation) && (null != this.activity))
		{
			if (getConfiguration().inAnimationResId > 0)
			{
				this.inAnimation = AnimationUtils.loadAnimation(getActivity(), getConfiguration().inAnimationResId);
			}
			else
			{
				measureCroutonView();
				this.inAnimation = DefaultAnimationsBuilder.buildDefaultSlideInDownAnimation(getView());
			}
		}
		return inAnimation;
	}

	public Animation getOutAnimation()
	{
		if ((null == this.outAnimation) && (null != this.activity))
		{
			if (getConfiguration().outAnimationResId > 0)
			{
				this.outAnimation = AnimationUtils.loadAnimation(getActivity(), getConfiguration().outAnimationResId);
			}
			else
			{
				this.outAnimation = DefaultAnimationsBuilder.buildDefaultSlideOutUpAnimation(getView());
			}
		}
		return outAnimation;
	}

	public void setLifecycleCallback(LifecycleCallback lifecycleCallback)
	{
		this.lifecycleCallback = lifecycleCallback;
	}

	public void hide()
	{
		Manager.getInstance().removeCrouton(this);
	}

	public Crouton setOnClickListener(OnClickListener onClickListener)
	{
		this.onClickListener = onClickListener;
		return this;
	}

	public Crouton setConfiguration(final Configuration configuration)
	{
		this.configuration = configuration;
		return this;
	}

	boolean isShowing()
	{
		return (null != activity) && (isCroutonViewNotNull() || isCustomViewNotNull());
	}

	private boolean isCroutonViewNotNull()
	{
		return (null != croutonView) && (null != croutonView.getParent());
	}

	private boolean isCustomViewNotNull()
	{
		return (null != customView) && (null != customView.getParent());
	}

	void detachActivity()
	{
		activity = null;
	}

	void detachViewGroup()
	{
		viewGroup = null;
	}

	void detachLifecycleCallback()
	{
		lifecycleCallback = null;
	}

	LifecycleCallback getLifecycleCallback()
	{
		return lifecycleCallback;
	}

	Style getStyle()
	{
		return style;
	}

	Configuration getConfiguration()
	{
		if (null == configuration)
		{
			configuration = getStyle().configuration;
		}
		return configuration;
	}

	Activity getActivity()
	{
		return activity;
	}

	ViewGroup getViewGroup()
	{
		return viewGroup;
	}

	CharSequence getText()
	{
		return text;
	}

	View getView()
	{
		if (null != this.customView)
		{
			return this.customView;
		}
		if (null == this.croutonView)
		{
			initializeCroutonView();
		}
		return croutonView;
	}

	private void measureCroutonView()
	{
		View view = getView();
		int widthSpec;
		if (null != viewGroup)
		{
			widthSpec = View.MeasureSpec.makeMeasureSpec(viewGroup.getMeasuredWidth(), View.MeasureSpec.AT_MOST);
		}
		else
		{
			widthSpec = View.MeasureSpec.makeMeasureSpec(activity.getWindow().getDecorView().getMeasuredWidth(), View.MeasureSpec.AT_MOST);
		}
		view.measure(widthSpec, View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
	}

	private void initializeCroutonView()
	{
		Resources resources = this.activity.getResources();
		this.croutonView = initializeCroutonViewGroup(resources);
		RelativeLayout contentView = initializeContentView(resources);
		this.croutonView.addView(contentView);
	}

	@SuppressWarnings("deprecation")
	private FrameLayout initializeCroutonViewGroup(Resources resources)
	{
		FrameLayout croutonView = new FrameLayout(this.activity);
		if (null != onClickListener)
		{
			croutonView.setOnClickListener(onClickListener);
		}
		LayoutParams layoutParams = null;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
		{
			layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, resources.getDimensionPixelSize(R.dimen.action_bar_height_ex));
			layoutParams.topMargin = ScreenUtil.getStatusHeight(this.activity);
		}
		else
		{
			layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, resources.getDimensionPixelSize(R.dimen.action_bar_height));
			layoutParams.topMargin = 0;
		}
		croutonView.setLayoutParams(layoutParams);
		if (this.style.backgroundColorValue != Style.NOT_SET)
		{
			croutonView.setBackgroundColor(this.style.backgroundColorValue);
		}
		else
		{
			croutonView.setBackgroundColor(resources.getColor(this.style.backgroundColorResourceId));
		}
		if (this.style.backgroundDrawableResourceId != 0)
		{
			Bitmap background = BitmapFactory.decodeResource(resources, this.style.backgroundDrawableResourceId);
			BitmapDrawable drawable = new BitmapDrawable(resources, background);
			if (this.style.isTileEnabled)
			{
				drawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
			}
			croutonView.setBackgroundDrawable(drawable);
		}
		return croutonView;
	}

	@SuppressLint("NewApi")
	private RelativeLayout initializeContentView(final Resources resources)
	{
		RelativeLayout contentView = new RelativeLayout(this.activity);
		contentView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
		int padding = this.style.paddingInPixels;
		if (this.style.paddingDimensionResId > 0)
		{
			padding = resources.getDimensionPixelSize(this.style.paddingDimensionResId);
		}
		contentView.setPadding(padding, padding, padding, padding);
		ImageView image = null;
		if ((null != this.style.imageDrawable) || (0 != this.style.imageResId))
		{
			image = initializeImageView();
			contentView.addView(image, image.getLayoutParams());
		}
		TextView text = initializeTextView(resources);
		RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		if (null != image)
		{
			textParams.addRule(RelativeLayout.RIGHT_OF, image.getId());
		}
		if ((this.style.gravity & Gravity.CENTER) != 0)
		{
			textParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		}
		else if ((this.style.gravity & Gravity.CENTER_VERTICAL) != 0)
		{
			textParams.addRule(RelativeLayout.CENTER_VERTICAL);
		}
		else if ((this.style.gravity & Gravity.CENTER_HORIZONTAL) != 0)
		{
			textParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		}
		contentView.addView(text, textParams);
		return contentView;
	}

	private TextView initializeTextView(final Resources resources)
	{
		TextView text = new TextView(this.activity);
		text.setId(TEXT_ID);
		if (this.style.fontName != null)
		{
			setTextWithCustomFont(text, this.style.fontName);
		}
		else if (this.style.fontNameResId != 0)
		{
			setTextWithCustomFont(text, resources.getString(this.style.fontNameResId));
		}
		else
		{
			text.setText(this.text);
		}
		text.setTypeface(Typeface.DEFAULT);
		text.setGravity(this.style.gravity);
		if (this.style.textColorValue != Style.NOT_SET)
		{
			text.setTextColor(this.style.textColorValue);
		}
		else if (this.style.textColorResourceId != 0)
		{
			text.setTextColor(resources.getColor(this.style.textColorResourceId));
		}
		if (this.style.textSize != 0)
		{
			text.setTextSize(TypedValue.COMPLEX_UNIT_SP, this.style.textSize);
		}
		else
		{
			text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
		}
		return text;
	}

	private void setTextWithCustomFont(TextView text, String fontName)
	{
		if (this.text != null)
		{
			SpannableString s = new SpannableString(this.text);
			s.setSpan(new TypefaceSpan(text.getContext(), fontName), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			text.setText(s);
		}
	}

	private ImageView initializeImageView()
	{
		ImageView image;
		image = new ImageView(this.activity);
		image.setId(IMAGE_ID);
		image.setAdjustViewBounds(true);
		image.setScaleType(this.style.imageScaleType);
		if (null != this.style.imageDrawable)
		{
			image.setImageDrawable(this.style.imageDrawable);
		}
		if (this.style.imageResId != 0)
		{
			image.setImageResource(this.style.imageResId);
		}
		RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		imageParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		imageParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		image.setLayoutParams(imageParams);
		return image;
	}
}
