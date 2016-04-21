package com.v2gogo.project.views.logic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.v2gogo.project.R;

/**
 * 购物车中步进控件
 * 
 * @author houjun
 */
public class CartSteper extends LinearLayout implements OnClickListener
{

	public static enum Action
	{
		REDUCE, ADD
	}

	private int mStep = 1;
	private int mMinValue = 1;
	private int mMaxValue = Integer.MAX_VALUE;

	private TextView mTvContent;
	private ImageButton mIbtnAdd;
	private ImageButton mIbtnReduce;

	private IonChangedClickListener mChangedClickListener;

	@SuppressLint("NewApi")
	public CartSteper(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init(context);
	}

	public CartSteper(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(context);
	}

	public CartSteper(Context context)
	{
		super(context);
		init(context);
	}

	public int getMaxValue()
	{
		return mMaxValue;
	}

	public void setMaxValue(int mMaxValue)
	{
		this.mMaxValue = mMaxValue;
	}

	public int getMinValue()
	{
		return mMinValue;
	}

	public void setMinValue(int mMinValue)
	{
		this.mMinValue = mMinValue;
	}

	public int getStep()
	{
		return mStep;
	}

	public void setStep(int mStep)
	{
		this.mStep = mStep;
	}

	public void setChangedClickListener(IonChangedClickListener mChangedClickListener)
	{
		this.mChangedClickListener = mChangedClickListener;
	}

	private void init(Context context)
	{
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View view = layoutInflater.inflate(R.layout.custom_steper_layout, null);
		initViews(view);
		LinearLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		view.setLayoutParams(layoutParams);
		this.addView(view);
	}

	private void initViews(View view)
	{
		mIbtnAdd = (ImageButton) view.findViewById(R.id.common_stepper_add_ibtn);
		mTvContent = (TextView) view.findViewById(R.id.common_stepper_content_tv);
		mIbtnReduce = (ImageButton) view.findViewById(R.id.common_stepper_reduce_ibtn);
		mTvContent.setText(mMinValue + "");
		registerListener();
	}

	/**
	 * 设置图素
	 */
	public void setStepperDrawable(int addResId, int reduceResId)
	{
		mIbtnAdd.setBackgroundResource(addResId);
		mIbtnReduce.setBackgroundResource(reduceResId);
	}

	/**
	 * 设置图素
	 */
	@SuppressWarnings("deprecation")
	public void setStepperDrawable(Drawable addResDrawable, Drawable reduceResDrawable)
	{
		mIbtnAdd.setBackgroundDrawable(addResDrawable);
		mIbtnReduce.setBackgroundDrawable(reduceResDrawable);
	}

	private void registerListener()
	{
		mIbtnAdd.setOnClickListener(this);
		mIbtnReduce.setOnClickListener(this);
	}

	/**
	 * 设置步进内容
	 */
	public void setStepperContent(int stepper)
	{
		if (stepper < mMinValue)
		{
			mIbtnReduce.setEnabled(false);
			return;
		}
		if (stepper > mMaxValue)
		{
			mIbtnAdd.setEnabled(false);
			return;
		}
		mTvContent.setText(stepper + "");
		mIbtnAdd.setEnabled(true);
		mIbtnReduce.setEnabled(true);
	}

	/**
	 * 得到步进
	 * 
	 * @return
	 */
	public int getStepperContent()
	{
		return Integer.parseInt(mTvContent.getText().toString().trim());
	}

	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.common_stepper_add_ibtn:
				setStepperContent(getStepperContent() + mStep);
				if (null != mChangedClickListener)
				{
					mChangedClickListener.onChangedAction(Action.ADD, getStepperContent());
				}
				break;

			case R.id.common_stepper_reduce_ibtn:
				setStepperContent(getStepperContent() - mStep);
				if (null != mChangedClickListener)
				{
					mChangedClickListener.onChangedAction(Action.REDUCE, getStepperContent());
				}
				break;

			default:
				break;
		}
	}

	/**
	 * 点击事件监听
	 * 
	 * @author houjun
	 */
	public interface IonChangedClickListener
	{
		public void onChangedAction(Action action, int value);
	}
}
