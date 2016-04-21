package com.v2gogo.project.views.logic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.utils.common.DensityUtil;
import com.v2gogo.project.utils.common.ScreenUtil;

public class VoteOptionNumberLayout extends LinearLayout
{
	private TextView[] mTextViews;
	private int mNumber = 0;

	@SuppressLint("NewApi")
	public VoteOptionNumberLayout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init(context);
	}

	public VoteOptionNumberLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(context);
	}

	public VoteOptionNumberLayout(Context context)
	{
		super(context);
		init(context);
	}

	private void init(Context context)
	{
		View view = LayoutInflater.from(context).inflate(R.layout.vote_option_number_layout, null);
		LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.vote_number_layout_container);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ScreenUtil.getScreenWidth(context) - DensityUtil.dp2px(context, 10f) * 2,
				DensityUtil.dp2px(context, 38f));
		layoutParams.topMargin = DensityUtil.dp2px(context, 8f);
		linearLayout.setLayoutParams(layoutParams);
		mTextViews = new TextView[6];
		mTextViews[0] = (TextView) view.findViewById(R.id.vote_number_text6);
		mTextViews[1] = (TextView) view.findViewById(R.id.vote_number_text5);
		mTextViews[2] = (TextView) view.findViewById(R.id.vote_number_text4);
		mTextViews[3] = (TextView) view.findViewById(R.id.vote_number_text3);
		mTextViews[4] = (TextView) view.findViewById(R.id.vote_number_text2);
		mTextViews[5] = (TextView) view.findViewById(R.id.vote_number_text1);
		this.addView(view);
		setVoteNumbers(0);
	}

	/**
	 * 设置数量
	 * 
	 * @param number
	 */
	public void setVoteNumbers(int number)
	{
		mNumber = number;
		if (number <= 100000)
		{
			String str = String.valueOf(number);
			int length = str.length();
			for (int i = 0; i < 6; i++)
			{
				if (i < length)
				{
					mTextViews[i].setText(str.charAt(length - 1 - i) + "");
					mTextViews[i].setTextColor(0xffff6701);
				}
				else
				{
					mTextViews[i].setTextColor(0xff696969);
					mTextViews[i].setText("0");
				}
			}
		}
	}

	/**
	 * 返回数字
	 * 
	 * @return
	 */
	public int getVoteNumber()
	{
		return mNumber;
	}

}
