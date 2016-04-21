package com.v2gogo.project.activity.exchange;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsoluteLayout;
import android.widget.ImageButton;

import cn.sharesdk.framework.ShareSDK;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.BaseActivity;
import com.v2gogo.project.activity.account.AccountLoginActivity;
import com.v2gogo.project.domain.ShareInfo;
import com.v2gogo.project.domain.exchange.PrizeDetailsInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.manager.ExchangeManager;
import com.v2gogo.project.manager.ShareAddManager;
import com.v2gogo.project.manager.ExchangeManager.IonExchangeCallback;
import com.v2gogo.project.manager.home.CommandManager;
import com.v2gogo.project.manager.home.CommandManager.IoncommandCommentCallback;
import com.v2gogo.project.utils.common.ToastUtil;
import com.v2gogo.project.utils.share.CustomPlatformActionListener;
import com.v2gogo.project.utils.share.ShareUtils;
import com.v2gogo.project.views.dialog.AppNoticeDialog;
import com.v2gogo.project.views.dialog.ExchangePrizeDialog;
import com.v2gogo.project.views.dialog.ExchangePrizeDialog.IonSureClickPrizeCallback;
import com.v2gogo.project.views.dialog.V2gogoShareDialog;
import com.v2gogo.project.views.dialog.V2gogoShareDialog.IonItemClickCallback;
import com.v2gogo.project.views.dialog.V2gogoShareDialog.SHARE_TYPE;
import com.v2gogo.project.views.webview.NoFadeColorWebView.IonPrizeClickCallback;
import com.v2gogo.project.views.webview.NoFadeColorWebView.IonPrizeClickPraiseCallback;
import com.v2gogo.project.views.webview.ProgressWebView;
import com.v2gogo.project.views.webview.ProgressWebView.IonReceiveTitleCallback;

public class ExchangePrizeDetailsActivity extends BaseActivity implements IonReceiveTitleCallback, IonItemClickCallback, OnClickListener,
		IonSureClickPrizeCallback, IonPrizeClickCallback, IonPrizeClickPraiseCallback
{

	public static final String PRIZE_ID = "prize_id";
	public static final String PRIZE_URL = "prize_url";
	public static final String IS_SHOW = "is_show";
	public static final String PRIZEINFO = "prizeinfo";

	private String mPrizeId;
	private ProgressWebView mProgressWebview;
	private AbsoluteLayout mAbsoluteLayout;
	private AppNoticeDialog mAppNoticeDialog;
	private ExchangePrizeDialog mExchangePrizeDialog;
	private PrizeDetailsInfo mPrizeDetailsInfo;
	private String mUrl;// 链接地址

	private ImageButton mShareImgBtn;
	private V2gogoShareDialog mShareDialog;
	private ShareInfo mShareInfo;

	@Override
	public void onInitViews()
	{
		mShareImgBtn = (ImageButton) findViewById(R.id.commen_webview_share);
		mProgressWebview = (ProgressWebView) findViewById(R.id.exchange_detaile_activity_webview);
		mAbsoluteLayout = (AbsoluteLayout) findViewById(R.id.exchange_details_activity_container);
		mProgressWebview.setOnReceiveTitleCallback(this);
	}

	/**
	 * method desc：弹出分享对话框
	 */
	public void share()
	{
		if (null == mShareDialog)
		{
			mShareDialog = new V2gogoShareDialog(this, R.style.style_action_sheet_dialog);
			mShareDialog.setItemClickCallback(this);
		}
		if (!mShareDialog.isShowing())
		{
			mShareDialog.show();
		}
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.exchange_prize_details_activity_layout;
	}

	@Override
	protected void onInitIntentData(Intent intent)
	{
		super.onInitIntentData(intent);
		if (null != intent)
		{
			mPrizeId = intent.getStringExtra(PRIZE_ID);
			mUrl = intent.getStringExtra(PRIZE_URL);
		}
	}

	@Override
	protected void onInitLoadDatas()
	{
		super.onInitLoadDatas();
		mProgressWebview.loadUrl(mUrl);

		ShareSDK.initSDK(this);
		mShareInfo = new ShareInfo();
		mShareInfo.setHref(mUrl);
		if (!TextUtils.isEmpty(mUrl))
		{
			try
			{
				Uri uri = Uri.parse(mUrl);
				String targetId = uri.getQueryParameter("pid");
				if (TextUtils.isEmpty(targetId))
				{
					targetId = uri.getQueryParameter("id");
				}
				mShareInfo.setTargedId(targetId);
			}
			catch (Exception e)
			{
			}
		}
	}

	@Override
	protected boolean isNotifyIntent()
	{
		return true;
	}

	@Override
	protected void registerListener()
	{
		super.registerListener();
		mProgressWebview.setPrizeClickCallback(this);// 设置立即兑换回调
		mProgressWebview.setPrizeClickPraiseCallback(this);// 设置点赞回调

		mShareImgBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				share();
			}
		});
	}

	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.exchange_prize_details_exchange:
				exchangePrize();
				break;

			default:
				break;
		}
	}

	@Override
	public void onSureClickPrize(String id, String receiveId)
	{
		if (!TextUtils.isEmpty(id))
		{
			ExchangeManager.exchangeGoodsByGoodsId(id, V2GogoApplication.getCurrentMatser().getUsername(), new IonExchangeCallback()
			{
				@Override
				public void onExchangeSuccess()
				{
					if (mPrizeDetailsInfo != null)
					{
					}
					showExchangeSuccessDialog();
				}

				@Override
				public void onExchangeFail(String errorMessage)
				{
					ToastUtil.showAlertToast(ExchangePrizeDetailsActivity.this, errorMessage);
				}
			});
		}
	}

	/**
	 * 兑换奖品
	 */
	private void exchangePrize()
	{
		if (null != mPrizeId)
		{
			if (V2GogoApplication.getMasterLoginState())
			{
				showExchangeDialog(mPrizeId);
			}
			else
			{
				AccountLoginActivity.forwardAccountLogin(this);
			}
		}
	}

	/**
	 * 显示兑换成功对话框
	 */
	private void showExchangeSuccessDialog()
	{
		if (null == mAppNoticeDialog)
		{
			mAppNoticeDialog = new AppNoticeDialog(this, R.style.style_action_sheet_dialog);
		}
		if (!mAppNoticeDialog.isShowing())
		{
			mAppNoticeDialog.show();
			mAppNoticeDialog.setSureTitleAndMessage(R.string.exchange_prize_success, R.string.app_notice_sure_tip);
		}
	}

	/**
	 * 显示兑换对话框
	 */
	private void showExchangeDialog(final String id)
	{
		if (null == mExchangePrizeDialog)
		{
			mExchangePrizeDialog = new ExchangePrizeDialog(this, R.style.style_action_sheet_dialog);
			mExchangePrizeDialog.setOnClickPrizeCallback(this);
		}
		if (!mExchangePrizeDialog.isShowing())
		{
			mExchangePrizeDialog.show();
			mExchangePrizeDialog.setMessage(R.string.exchange_prize_tip);
			mExchangePrizeDialog.setPrizeInfosId(id, null);
		}
	}

	@Override
	protected void onResume()
	{
		mProgressWebview.resumeWebview();
		super.onResume();
	}

	@Override
	protected void onPause()
	{
		mProgressWebview.pauseWebview();
		super.onPause();
	}

	@Override
	protected void onDestroy()
	{
		mAbsoluteLayout.removeView(mProgressWebview);
		mProgressWebview.destoryWebview();
		super.onDestroy();
	}

	@Override
	public void clearRequestTask()
	{

	}

	/**
	 * 立即兑换回调方法
	 */
	@Override
	public void getExchangePrizeId(String pid)
	{
		exchangePrize();
	}

	/**
	 * 点赞回调
	 */
	@Override
	public void getPrizePraiseId(String cid)
	{
		if (V2GogoApplication.getMasterLoginState())
		{
			CommandManager.commandComment(null, cid, new IoncommandCommentCallback()
			{
				@Override
				public void oncommandCommentSuccess(String id)
				{
					ToastUtil.showConfirmToast(ExchangePrizeDetailsActivity.this, "点赞成功");
				}

				@Override
				public void oncommandCommentFail(String errorMessage)
				{
					ToastUtil.showAlertToast(ExchangePrizeDetailsActivity.this, errorMessage);
				}
			});
		}
		else
		{
			AccountLoginActivity.forwardAccountLogin(this);
		}
	}

	@Override
	public void onShareClick(SHARE_TYPE type)
	{
		String tip = getResources().getString(R.string.share_success_tip);
		com.v2gogo.project.utils.share.ShareUtils.SHARE_TYPE shareType = com.v2gogo.project.utils.share.ShareUtils.SHARE_TYPE.SHARE_WEIXIN;
		if (type == SHARE_TYPE.SHARE_WENXI_COLLECTIONS)
		{
			shareType = com.v2gogo.project.utils.share.ShareUtils.SHARE_TYPE.SHARE_WEIXIN_FRIENDS;
		}
		else if (type == SHARE_TYPE.SHARE_QQ)
		{
			shareType = com.v2gogo.project.utils.share.ShareUtils.SHARE_TYPE.SHARE_QQ;
		}
		else if (type == SHARE_TYPE.SHARE_QZONE)
		{
			shareType = com.v2gogo.project.utils.share.ShareUtils.SHARE_TYPE.SHARE_QZONE;
		}
		else if (type == SHARE_TYPE.SHARE_MESSAGE)
		{
			shareType = com.v2gogo.project.utils.share.ShareUtils.SHARE_TYPE.SHARE_MESSAGE;
		}
		ShareUtils.share(this, mShareInfo.getTitle(), mShareInfo.getDescription(), mShareInfo.getHref(), mShareInfo.getImageUrl(), shareType,
				new CustomPlatformActionListener(this, tip));

		// 分享奖品记录
		ShareAddManager.sharePrizeAdd(mShareInfo.getTargedId());
	}

	@Override
	public void onReceiveTitle(String title)
	{
		mShareInfo.setTitle(title);
		mShareInfo.setDescription(title);
	}

}
