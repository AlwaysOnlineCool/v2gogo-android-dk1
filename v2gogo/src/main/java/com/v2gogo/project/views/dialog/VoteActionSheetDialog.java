package com.v2gogo.project.views.dialog;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.db.MatserInfo;
import com.v2gogo.project.domain.home.InteractionInfo;
import com.v2gogo.project.domain.home.VoteOptionInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.utils.common.ScreenUtil;
import com.v2gogo.project.views.logic.VoteBotton;

/**
 * 互动赢大奖的actionbar
 * 
 * @author houjun
 */
public class VoteActionSheetDialog extends Dialog implements android.view.View.OnClickListener, OnCheckedChangeListener
{

	private View mContentView;
	private Button mBtnSure;

	private VoteBotton[] mVoteBottons;
	private RadioGroup[] mRadioGroups;

	private boolean mIsInitWidth = false;

	private VoteOptionInfo mVoteOptionInfo;
	private IonVoteActionSheetCallback mIonVoteActionSheetCallback;
	private TextView mMyCoin;
	private TextView mVoteTip;

	public VoteActionSheetDialog(Context context, boolean cancelable, OnCancelListener cancelListener)
	{
		super(context, cancelable, cancelListener);
		setDialogParams();
	}

	public VoteActionSheetDialog(Context context, int theme)
	{
		super(context, theme);
		setDialogParams();
	}

	public VoteActionSheetDialog(Context context)
	{
		super(context);
		setDialogParams();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if (null == mContentView)
		{
			mContentView = getLayoutInflater().inflate(R.layout.vote_action__sheet_dialog_layout, null);
		}
		setContentView(mContentView);
		if (!mIsInitWidth)
		{
			Window dialogWindow = this.getWindow();
			WindowManager.LayoutParams lp = dialogWindow.getAttributes();
			lp.width = ScreenUtil.getScreenWidth(getContext());
			dialogWindow.setAttributes(lp);
			mIsInitWidth = true;
		}
		initViews(mContentView);
	}

	public void setOnVoteActionSheetCallback(IonVoteActionSheetCallback mIonVoteActionSheetCallback)
	{
		this.mIonVoteActionSheetCallback = mIonVoteActionSheetCallback;
	}

	private void initViews(View view)
	{
		mVoteBottons = new VoteBotton[4];
		mRadioGroups = new RadioGroup[2];
		mRadioGroups[0] = (RadioGroup) view.findViewById(R.id.vote_first_layout);
		mRadioGroups[1] = (RadioGroup) view.findViewById(R.id.vote_second_layout);
		mBtnSure = (Button) view.findViewById(R.id.vote_action_sheet_dialog_btn_sure);
		mMyCoin = (TextView) view.findViewById(R.id.vote_action_sheet_dialog_text_coin);
		mVoteTip = (TextView) view.findViewById(R.id.vote_action_sheet_dialog_vote_tip);
		mVoteBottons[0] = (VoteBotton) view.findViewById(R.id.vote_action_sheet_dialog_option_one);
		mVoteBottons[1] = (VoteBotton) view.findViewById(R.id.vote_action_sheet_dialog_option_two);
		mVoteBottons[2] = (VoteBotton) view.findViewById(R.id.vote_action_sheet_dialog_option_three);
		mVoteBottons[3] = (VoteBotton) view.findViewById(R.id.vote_action_sheet_dialog_option_four);
		mBtnSure.setOnClickListener(this);
		mRadioGroups[0].setOnCheckedChangeListener(this);
		mRadioGroups[1].setOnCheckedChangeListener(this);
	}

	private void setDialogParams()
	{
		Window dialogWindow = getWindow();
		dialogWindow.setWindowAnimations(R.style.style_dialog_aniamtion);
		dialogWindow.setGravity(Gravity.BOTTOM);
		this.setCanceledOnTouchOutside(true);
	}

	public void setActionDatas(InteractionInfo interactionInfo)
	{
		int leftCoin = 0;
		MatserInfo masterInfo = V2GogoApplication.getCurrentMatser();
		if (masterInfo != null)
		{
			leftCoin = masterInfo.getCoin();
		}
		String str = String.format(getContext().getResources().getString(R.string.vote_my_coin_tip), leftCoin);
		SpannableStringBuilder stringBuilder = new SpannableStringBuilder(str);
		ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(0xFFF96700);
		stringBuilder.setSpan(foregroundColorSpan, 5, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		mMyCoin.setText(stringBuilder);
		mVoteTip.setText(String.format(getContext().getString(R.string.vote_coin_cast_tip), interactionInfo.getVoteCoin()));
	}

	/**
	 * 设置选项数据
	 * 
	 * @param voteOptionInfos
	 */
	public void setOptionInfos(List<VoteOptionInfo> voteOptionInfos)
	{
		initVisibity();
		if (null != voteOptionInfos)
		{
			for (int i = 0, size = voteOptionInfos.size(); i < size; i++)
			{
				if (size <= 4)
				{
					if (size <= 2)
					{
						mRadioGroups[0].setVisibility(View.VISIBLE);
						mRadioGroups[1].setVisibility(View.GONE);
					}
					else
					{
						mRadioGroups[0].setVisibility(View.VISIBLE);
						mRadioGroups[1].setVisibility(View.VISIBLE);
					}
					VoteOptionInfo voteOptionInfo = voteOptionInfos.get(i);
					mVoteBottons[i].setVisibility(View.VISIBLE);
					mVoteBottons[i].setVoteOptionInfo(voteOptionInfo);
				}
			}
		}
	}

	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.vote_action_sheet_dialog_btn_sure:
				this.dismiss();
				if (null != mIonVoteActionSheetCallback)
				{
					mIonVoteActionSheetCallback.onVoteActionSheetCallback(mVoteOptionInfo);
				}
				break;

			default:
				break;
		}
	}

	@Override
	public void show()
	{
		try
		{
			super.show();
		}
		catch (Exception exception)
		{
		}
	}

	@Override
	public void dismiss()
	{
		try
		{
			super.dismiss();
		}
		catch (Exception exception)
		{
		}
	}

	private void initVisibity()
	{
		mVoteBottons[0].setVisibility(View.INVISIBLE);
		mVoteBottons[2].setVisibility(View.INVISIBLE);
		mRadioGroups[0].setVisibility(View.GONE);
		mRadioGroups[1].setVisibility(View.GONE);
		mVoteBottons[1].setVisibility(View.INVISIBLE);
		mVoteBottons[3].setVisibility(View.INVISIBLE);
	}

	/**
	 * 确定回调接口
	 * 
	 * @author houjun
	 */
	public interface IonVoteActionSheetCallback
	{
		public void onVoteActionSheetCallback(VoteOptionInfo voteOptionInfo);
	}

	@Override
	public void onCheckedChanged(RadioGroup radioGroup, int checkId)
	{
		if (radioGroup == mRadioGroups[0])
		{
			if (checkId == R.id.vote_action_sheet_dialog_option_one || checkId == R.id.vote_action_sheet_dialog_option_two)
			{
				mRadioGroups[1].clearCheck();
				radioGroup.check(checkId);
				if (checkId == R.id.vote_action_sheet_dialog_option_one)
				{
					mVoteOptionInfo = mVoteBottons[0].getVoteOptionInfo();
				}
				else
				{
					mVoteOptionInfo = mVoteBottons[1].getVoteOptionInfo();
				}
			}
		}
		else if (radioGroup == mRadioGroups[1])
		{
			if (checkId == R.id.vote_action_sheet_dialog_option_three || checkId == R.id.vote_action_sheet_dialog_option_four)
			{
				mRadioGroups[0].clearCheck();
				radioGroup.check(checkId);
				if (checkId == R.id.vote_action_sheet_dialog_option_three)
				{
					mVoteOptionInfo = mVoteBottons[2].getVoteOptionInfo();
				}
				else
				{
					mVoteOptionInfo = mVoteBottons[3].getVoteOptionInfo();
				}
			}
		}
	}
}
