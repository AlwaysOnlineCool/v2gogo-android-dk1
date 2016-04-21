package com.v2gogo.project.views.logic;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioButton;

import com.v2gogo.project.domain.home.VoteOptionInfo;

/**
 * 投票button
 * 
 * @author
 */
public class VoteBotton extends RadioButton
{

	private VoteOptionInfo mVoteOptionInfo;

	public VoteBotton(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		this.setSingleLine(true);
		this.setEllipsize(TruncateAt.END);
		this.setButtonDrawable(null);
	}

	public VoteBotton(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.setSingleLine(true);
		this.setEllipsize(TruncateAt.END);
		this.setButtonDrawable(null);
	}

	public VoteBotton(Context context)
	{
		super(context);
		this.setSingleLine(true);
		this.setEllipsize(TruncateAt.END);
		this.setButtonDrawable(null);
	}

	public VoteOptionInfo getVoteOptionInfo()
	{
		return mVoteOptionInfo;
	}

	public void setVoteOptionInfo(VoteOptionInfo mVoteOptionInfo)
	{
		this.mVoteOptionInfo = mVoteOptionInfo;
		if (mVoteOptionInfo == null)
		{
			this.setVisibility(View.GONE);
		}
		else
		{
			this.setVisibility(View.VISIBLE);
			this.setText(mVoteOptionInfo.getName());
		}
	}

	@Override
	public String toString()
	{
		return "VoteBotton [mVoteOptionInfo=" + mVoteOptionInfo + "]";
	}

}
