package com.v2gogo.project.views.crouton;

import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.widget.ImageView;

public class Style
{

	public static final int NOT_SET = -1;

	public static final int holoRedLight = 0xffef4136;
	public static final int holoGreenLight = 0xff1d953f;
	public static final int holoBlueLight = 0xff2a5caa;

	public static final Style ALERT;
	public static final Style CONFIRM;
	public static final Style INFO;

	static
	{
		ALERT = new Builder().setBackgroundColorValue(holoRedLight).build();
		CONFIRM = new Builder().setBackgroundColorValue(holoGreenLight).build();
		INFO = new Builder().setBackgroundColorValue(holoBlueLight).build();
	}

	final Configuration configuration;
	final int backgroundColorResourceId;
	final int backgroundDrawableResourceId;
	final int backgroundColorValue;
	final boolean isTileEnabled;
	final int textColorResourceId;
	final int textColorValue;
	final int gravity;
	final Drawable imageDrawable;
	final int imageResId;
	final ImageView.ScaleType imageScaleType;
	final int textSize;
	final int paddingInPixels;
	final int paddingDimensionResId;
	final String fontName;
	final int fontNameResId;

	private Style(final Builder builder)
	{
		this.configuration = builder.configuration;
		this.backgroundColorResourceId = builder.backgroundColorResourceId;
		this.backgroundDrawableResourceId = builder.backgroundDrawableResourceId;
		this.isTileEnabled = builder.isTileEnabled;
		this.textColorResourceId = builder.textColorResourceId;
		this.textColorValue = builder.textColorValue;
		this.gravity = builder.gravity;
		this.imageDrawable = builder.imageDrawable;
		this.textSize = builder.textSize;
		this.imageResId = builder.imageResId;
		this.imageScaleType = builder.imageScaleType;
		this.paddingInPixels = builder.paddingInPixels;
		this.paddingDimensionResId = builder.paddingDimensionResId;
		this.backgroundColorValue = builder.backgroundColorValue;
		this.fontName = builder.fontName;
		this.fontNameResId = builder.fontNameResId;
	}

	public static class Builder
	{
		private Configuration configuration;
		private int backgroundColorValue;
		private int backgroundColorResourceId;
		private int backgroundDrawableResourceId;
		private boolean isTileEnabled;
		private int textColorResourceId;
		private int textColorValue;
		private int gravity;
		private Drawable imageDrawable;
		private int textSize;
		private int imageResId;
		private ImageView.ScaleType imageScaleType;
		private int paddingInPixels;
		private int paddingDimensionResId;
		private String fontName;
		private int fontNameResId;

		public Builder()
		{
			configuration = Configuration.DEFAULT;
			paddingInPixels = 10;
			backgroundColorResourceId = 0;
			backgroundDrawableResourceId = 0;
			backgroundColorValue = NOT_SET;
			isTileEnabled = false;
			textColorResourceId = android.R.color.white;
			textColorValue = NOT_SET;
			gravity = Gravity.CENTER;
			imageDrawable = null;
			imageResId = 0;
			imageScaleType = ImageView.ScaleType.FIT_XY;
			fontName = null;
			fontNameResId = 0;
		}

		public Builder(final Style baseStyle)
		{
			configuration = baseStyle.configuration;
			backgroundColorValue = baseStyle.backgroundColorValue;
			backgroundColorResourceId = baseStyle.backgroundColorResourceId;
			backgroundDrawableResourceId = baseStyle.backgroundDrawableResourceId;
			isTileEnabled = baseStyle.isTileEnabled;
			textColorResourceId = baseStyle.textColorResourceId;
			textColorValue = baseStyle.textColorValue;
			gravity = baseStyle.gravity;
			imageDrawable = baseStyle.imageDrawable;
			textSize = baseStyle.textSize;
			imageResId = baseStyle.imageResId;
			imageScaleType = baseStyle.imageScaleType;
			paddingInPixels = baseStyle.paddingInPixels;
			paddingDimensionResId = baseStyle.paddingDimensionResId;
			fontName = baseStyle.fontName;
			fontNameResId = baseStyle.fontNameResId;
		}

		public Builder setConfiguration(Configuration configuration)
		{
			this.configuration = configuration;
			return this;
		}

		public Builder setBackgroundColor(int backgroundColorResourceId)
		{
			this.backgroundColorResourceId = backgroundColorResourceId;

			return this;
		}

		public Builder setBackgroundColorValue(int backgroundColorValue)
		{
			this.backgroundColorValue = backgroundColorValue;
			return this;
		}

		public Builder setBackgroundDrawable(int backgroundDrawableResourceId)
		{
			this.backgroundDrawableResourceId = backgroundDrawableResourceId;

			return this;
		}

		public Builder setTileEnabled(boolean isTileEnabled)
		{
			this.isTileEnabled = isTileEnabled;

			return this;
		}

		public Builder setTextColor(int textColor)
		{
			this.textColorResourceId = textColor;

			return this;
		}

		public Builder setTextColorValue(int textColorValue)
		{
			this.textColorValue = textColorValue;
			return this;
		}

		public Builder setGravity(int gravity)
		{
			this.gravity = gravity;

			return this;
		}

		public Builder setImageDrawable(Drawable imageDrawable)
		{
			this.imageDrawable = imageDrawable;

			return this;
		}

		public Builder setImageResource(int imageResId)
		{
			this.imageResId = imageResId;

			return this;
		}

		public Builder setTextSize(int textSize)
		{
			this.textSize = textSize;
			return this;
		}

		public Builder setImageScaleType(ImageView.ScaleType imageScaleType)
		{
			this.imageScaleType = imageScaleType;
			return this;
		}

		public Builder setPaddingInPixels(int padding)
		{
			this.paddingInPixels = padding;
			return this;
		}

		public Builder setPaddingDimensionResId(int paddingResId)
		{
			this.paddingDimensionResId = paddingResId;
			return this;
		}

		public Builder setFontName(String fontName)
		{
			this.fontName = fontName;
			return this;
		}

		public Builder setFontNameResId(int fontNameResId)
		{
			this.fontNameResId = fontNameResId;
			return this;
		}

		public Style build()
		{
			return new Style(this);
		}
	}

}
