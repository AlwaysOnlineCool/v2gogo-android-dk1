package com.v2gogo.project.views.logic;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.db.MatserInfo;
import com.v2gogo.project.main.image.GlideImageLoader;

public class ProfileUserInfoLayout extends LinearLayout
{
	private ImageView mIvAvatar;
	private TextView mTvNickname;
	private TextView mTvAccount;
	private TextView mTvTotalCoin;
	private TextView mTvLeftCoin;
	private TextView mTvRegisterLayout;
	private LinearLayout mLinearLayout;

	public ProfileUserInfoLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(context);
	}

	public ProfileUserInfoLayout(Context context)
	{
		super(context);
		init(context);
	}

	private void init(Context context)
	{
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View view = layoutInflater.inflate(R.layout.activity_user_info_layout, null);
		mIvAvatar = (ImageView) view.findViewById(R.id.fragment_profile_avatar);
		mTvAccount = (TextView) view.findViewById(R.id.fragment_profile_login_no);
		mTvLeftCoin = (TextView) view.findViewById(R.id.fragment_profile_left_coin);
		mTvTotalCoin = (TextView) view.findViewById(R.id.fragment_profile_total_coin);
		mTvNickname = (TextView) view.findViewById(R.id.fragment_profile_login_nikename);
		mTvRegisterLayout = (TextView) view.findViewById(R.id.login_layout);
		mLinearLayout = (LinearLayout) view.findViewById(R.id.master_layout);
		LinearLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		view.setLayoutParams(layoutParams);
		this.addView(view);
	}

	/**
	 * 设置信息
	 */
	public void setMatserInfos(Context context, MatserInfo masterInfo)
	{
		int leftCoin = 0;
		int totalCoin = 0;
		if (null != masterInfo)
		{
			mTvRegisterLayout.setVisibility(View.GONE);
			mLinearLayout.setVisibility(View.VISIBLE);
			leftCoin = masterInfo.getCoin();
			totalCoin = masterInfo.getAllcoin();
			GlideImageLoader.loadAvatarImageWithFixedSize(context, masterInfo.getThumbialAvatar(), mIvAvatar);
			mTvAccount.setText(String.format(String.format(context.getResources().getString(R.string.profile_user_account), masterInfo.getUsername())));
			mTvNickname.setText(masterInfo.getFullname());
			String totalString = String.format(context.getResources().getString(R.string.profile_user_total_coin), totalCoin);
			SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(totalString);
			ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(0xffa2a2a5);
			spannableStringBuilder.setSpan(foregroundColorSpan, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			ForegroundColorSpan colorSpan = new ForegroundColorSpan(0xFFF96700);
			spannableStringBuilder.setSpan(colorSpan, 6, totalString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			mTvTotalCoin.setText(spannableStringBuilder);
			String leftString = String.format(context.getResources().getString(R.string.profile_user_left_coin), leftCoin);
			spannableStringBuilder = new SpannableStringBuilder(leftString);
			spannableStringBuilder.setSpan(foregroundColorSpan, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			spannableStringBuilder.setSpan(colorSpan, 6, leftString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			mTvLeftCoin.setText(spannableStringBuilder);
		}
		else
		{
			mTvRegisterLayout.setVisibility(View.VISIBLE);
			mLinearLayout.setVisibility(View.GONE);
			GlideImageLoader.loadInternalDrawable(context, R.drawable.user_icons_user_orange, mIvAvatar);
		}
	}
}
