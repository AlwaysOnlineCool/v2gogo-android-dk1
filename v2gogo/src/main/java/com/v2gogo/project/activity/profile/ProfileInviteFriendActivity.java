package com.v2gogo.project.activity.profile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import cn.sharesdk.framework.ShareSDK;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.BaseActivity;
import com.v2gogo.project.activity.account.AccountLoginActivity;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.utils.share.CustomPlatformActionListener;
import com.v2gogo.project.utils.share.ShareUtils;
import com.v2gogo.project.views.dialog.V2gogoShareDialog;
import com.v2gogo.project.views.dialog.V2gogoShareDialog.IonItemClickCallback;
import com.v2gogo.project.views.dialog.V2gogoShareDialog.SHARE_TYPE;

/**
 * 邀请好友
 * 
 * @author houjun
 */
public class ProfileInviteFriendActivity extends BaseActivity implements OnClickListener, IonItemClickCallback
{

	private TextView mTvInviteCode;
	private Button mBtnConfirm;
	private V2gogoShareDialog mV2gogoShareDialog;

	@Override
	public void onInitViews()
	{
		mTvInviteCode = (TextView) findViewById(R.id.profile_invite_friend_activity_invite_code);
		mBtnConfirm = (Button) findViewById(R.id.profile_invite_friend_activity_confirm_invite);
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.profile_invite_friend_activity_layout;
	}

	@Override
	protected void registerListener()
	{
		super.registerListener();
		mBtnConfirm.setOnClickListener(this);
	}

	@Override
	protected void onInitLoadDatas()
	{
		super.onInitLoadDatas();
		ShareSDK.initSDK(this);
		if (V2GogoApplication.getMasterLoginState())
		{
			mTvInviteCode.setVisibility(View.VISIBLE);
			String string = getString(R.string.profile_invite_friend_input_code_tip);
			string = String.format(string, V2GogoApplication.getCurrentMatser().getInvcode());
			SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(string);
			ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(0xff323232);
			spannableStringBuilder.setSpan(foregroundColorSpan, 0, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			foregroundColorSpan = new ForegroundColorSpan(0xffff0f01);
			spannableStringBuilder.setSpan(foregroundColorSpan, 12, string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			mTvInviteCode.setText(spannableStringBuilder);
		}
		else
		{
			mTvInviteCode.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View view)
	{
		if (V2GogoApplication.getMasterLoginState())
		{
			if (null == mV2gogoShareDialog)
			{
				mV2gogoShareDialog = new V2gogoShareDialog(this, R.style.style_action_sheet_dialog);
				mV2gogoShareDialog.setItemClickCallback(this);
			}
			if (!mV2gogoShareDialog.isShowing())
			{
				mV2gogoShareDialog.show();
				mV2gogoShareDialog.setShowMessage();
			}
		}
		else
		{
			AccountLoginActivity.forwardAccountLogin(this);
		}
	}

	@Override
	public void onShareClick(SHARE_TYPE type)
	{
		String tip = getResources().getString(R.string.invite_success_tip);
		String title = String.format(getString(R.string.profile_invite_title_tip), V2GogoApplication.getCurrentMatser().getInvcode());
		String intro = String.format(getString(R.string.profile_invite_intro_tip), V2GogoApplication.getCurrentMatser().getInvcode());
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		String href = "http://www.v2gogo.com/down.html";
		if (type == SHARE_TYPE.SHARE_WEIXIN)
		{
			ShareUtils.share(this, title, intro, href, bitmap, com.v2gogo.project.utils.share.ShareUtils.SHARE_TYPE.SHARE_WEIXIN,
					new CustomPlatformActionListener(this, tip));
		}
		else if (type == SHARE_TYPE.SHARE_MESSAGE)
		{
			intro = intro + href;
			ShareUtils.share(this, title, intro, href, bitmap, com.v2gogo.project.utils.share.ShareUtils.SHARE_TYPE.SHARE_MESSAGE,
					new CustomPlatformActionListener(this, tip));
		}
		else if (type == SHARE_TYPE.SHARE_WENXI_COLLECTIONS)
		{
			ShareUtils.share(this, title, intro, href, bitmap, com.v2gogo.project.utils.share.ShareUtils.SHARE_TYPE.SHARE_WEIXIN_FRIENDS,
					new CustomPlatformActionListener(this, tip));
		}
		else if (type == SHARE_TYPE.SHARE_QQ)
		{
			ShareUtils.share(this, title, intro, href, bitmap, com.v2gogo.project.utils.share.ShareUtils.SHARE_TYPE.SHARE_QQ, new CustomPlatformActionListener(
					this, tip));
		}
		else if (type == SHARE_TYPE.SHARE_QZONE)
		{
			ShareUtils.share(this, title, intro, href, bitmap, com.v2gogo.project.utils.share.ShareUtils.SHARE_TYPE.SHARE_QZONE,
					new CustomPlatformActionListener(this, tip));
		}
	}

	@Override
	public void clearRequestTask()
	{
	}

}
