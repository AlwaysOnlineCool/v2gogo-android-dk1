package com.v2gogo.project.views.crouton;

public class Configuration
{
	public static final int DURATION_INFINITE = -1;
	public static final int DURATION_SHORT = 3000;
	public static final int DURATION_LONG = 5000;

	public static final Configuration DEFAULT;

	static
	{
		DEFAULT = new Builder().setDuration(DURATION_SHORT).build();
	}

	final int durationInMilliseconds;
	final int inAnimationResId;
	final int outAnimationResId;

	private Configuration(Builder builder)
	{
		this.durationInMilliseconds = builder.durationInMilliseconds;
		this.inAnimationResId = builder.inAnimationResId;
		this.outAnimationResId = builder.outAnimationResId;
	}

	public static class Builder
	{
		private int durationInMilliseconds = DURATION_SHORT;
		private int inAnimationResId = 0;
		private int outAnimationResId = 0;

		public Builder setDuration(final int duration)
		{
			this.durationInMilliseconds = duration;
			return this;
		}

		public Builder setInAnimation(final int inAnimationResId)
		{
			this.inAnimationResId = inAnimationResId;

			return this;
		}

		public Builder setOutAnimation(final int outAnimationResId)
		{
			this.outAnimationResId = outAnimationResId;
			return this;
		}

		public Configuration build()
		{
			return new Configuration(this);
		}
	}

}
