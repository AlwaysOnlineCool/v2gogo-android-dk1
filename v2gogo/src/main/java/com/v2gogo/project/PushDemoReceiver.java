package com.v2gogo.project;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;

import com.igexin.sdk.PushConsts;
import com.v2gogo.project.activity.MainTabActivity;
import com.v2gogo.project.activity.WebViewActivity;
import com.v2gogo.project.activity.exchange.ExchangeActivity;
import com.v2gogo.project.activity.exchange.ExchangePrizeDetailsActivity;
import com.v2gogo.project.activity.home.BaseDetailsctivity;
import com.v2gogo.project.activity.home.HomeArticleDetailsActivity;
import com.v2gogo.project.activity.home.subject.HomeSubjectActivity;
import com.v2gogo.project.activity.profile.ProfileCommentTabActivity;
import com.v2gogo.project.activity.profile.ProfileEtcQuanActivity;
import com.v2gogo.project.activity.profile.ProfileMessageActivity;
import com.v2gogo.project.activity.profile.ProfilePrizeActivity;
import com.v2gogo.project.activity.shop.GroupGoodsDetailsActivity;
import com.v2gogo.project.activity.shop.GroupWebViewActivity;
import com.v2gogo.project.domain.KickOffInfo;
import com.v2gogo.project.domain.PushInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.utils.common.NotificationUtil;
import com.v2gogo.project.utils.common.SPUtil;
import com.v2gogo.project.utils.http.Constants;
import com.v2gogo.project.utils.parse.JsonParser;
import com.v2gogo.project.views.dialog.AppNoticeDialog;
import com.v2gogo.project.views.dialog.AppNoticeDialog.IonClickSureCallback;

/**
 * 透传消息接收器
 * 
 * @author houjun
 */
public class PushDemoReceiver extends BroadcastReceiver
{

	private final String KICK_OFF = "kickoff";
	private AppNoticeDialog mAppNoticeDialog;

	private Map<String, String> maps = new HashMap<String, String>();

	@Override
	public void onReceive(Context context, Intent intent)
	{
		Bundle bundle = intent.getExtras();
		switch (bundle.getInt(PushConsts.CMD_ACTION))
		{
			case PushConsts.GET_MSG_DATA:
				byte[] payload = bundle.getByteArray("payload");
				if (payload != null)
				{
					String data = new String(payload);
					if (!TextUtils.isEmpty(data))
					{
						if (data.startsWith("{") && data.endsWith("}"))
						{
							if (data.indexOf(KICK_OFF) != -1)
							{
								dealWithKickOff(context, data);
							}
							else
							{
								dealPushInfo(context, data);
							}
						}
					}
				}
				break;

			case PushConsts.GET_CLIENTID:
				String cid = bundle.getString("clientid");
				if (!TextUtils.isEmpty(cid))
				{
					SPUtil.put(context, Constants.CLIENT_ID, cid);
				}
				break;

			default:
				break;
		}
	}

	/**
	 * 处理推送
	 */
	private void dealPushInfo(Context context, String data)
	{
		PushInfo pushInfo = (PushInfo) JsonParser.parseObject(data, PushInfo.class);
		Intent intent = getClassByPushType(context, pushInfo);
		if (null != intent)
		{
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			NotificationUtil.createNotificationMessage(context, pushInfo.getTitle(), pushInfo.getContent(), intent);
		}
	}

	/**
	 * 处理踢下线
	 * 
	 * @param context
	 * @param data
	 */
	private void dealWithKickOff(Context context, String data)
	{
		KickOffInfo kickOffInfo = (KickOffInfo) JsonParser.parseObject(data, KickOffInfo.class);
		if (null != kickOffInfo)
		{
			if (V2GogoApplication.getMasterLoginState())
			{
				if (KICK_OFF.equals(kickOffInfo.getKickOff()) && V2GogoApplication.getCurrentMatser().getUsername().equals(kickOffInfo.getUsername())
						&& kickOffInfo.getLastlogintime() >= V2GogoApplication.getCurrentMatser().getLoginTime())
				{
					if (!maps.containsKey(KICK_OFF) && V2GogoApplication.getMasterLoginState())
					{
						V2GogoApplication.clearMatserInfo(true);
						maps.put(KICK_OFF, KICK_OFF);
						String str = context.getString(R.string.kickoff_tip);
						NotificationUtil.createKickOffMessage(context, context.getString(R.string.app_name), str);
						showKickoffDialog(context);
					}
				}
			}
		}
	}

	/**
	 * 显示用户下线的对话框
	 * 
	 * @param context
	 *            上下文
	 */
	@SuppressLint("NewApi")
	private void showKickoffDialog(final Context context)
	{
		if (mAppNoticeDialog == null)
		{
			mAppNoticeDialog = new AppNoticeDialog(context, R.style.style_action_sheet_dialog);
			mAppNoticeDialog.setOnSureCallback(new IonClickSureCallback()
			{
				@Override
				public void onClickSure()
				{
					maps.clear();
					Intent intent = new Intent(context, MainTabActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra(MainTabActivity.BACK, MainTabActivity.KICK_OFF);
					intent.putExtra("isLogin", true);
					context.startActivity(intent);
				}
			});
			mAppNoticeDialog.setCancelable(false);
			mAppNoticeDialog.setCanceledOnTouchOutside(false);
			mAppNoticeDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		}
		if (!mAppNoticeDialog.isShowing())
		{
			mAppNoticeDialog.show();
			mAppNoticeDialog.setSureTitleAndMessage(R.string.kick_off_tip, R.string.app_notice_sure_tip);
		}
	}

	/**
	 * 返回不同intent
	 */
	private Intent getClassByPushType(Context context, PushInfo pushInfo)
	{
		Intent intent = null;
		switch (pushInfo.getType())
		{
			case PushInfo.PUSH_TYPE_DUIHUAN:
				intent = new Intent(context, ExchangeActivity.class);
				intent.putExtra(ExchangeActivity.SHOW_BACK, true);
				break;

			case PushInfo.PUSH_TYPE_INFO:
				intent = new Intent(context, HomeArticleDetailsActivity.class);
				intent.putExtra(BaseDetailsctivity.ID, pushInfo.getId());
				break;

			case PushInfo.PUSH_TYPE_JIANIANHUA:
				if (!V2GogoApplication.getMasterLoginState())
				{
					intent = new Intent(context, MainTabActivity.class);
				}
				else
				{
					intent = new Intent(context, ProfilePrizeActivity.class);
				}
				break;

			case PushInfo.PUSH_TYPE_MESSAGE:
				if (!V2GogoApplication.getMasterLoginState())
				{
					intent = new Intent(context, MainTabActivity.class);
				}
				else
				{
					intent = new Intent(context, ProfileMessageActivity.class);
				}
				break;

			case PushInfo.PUSH_TYPE_PRIZE:
				StringBuilder prize_url = new StringBuilder();
				prize_url.append(ServerUrlConfig.SERVER_URL).append("/prizepaperapp/getExchangePrizeDetail?pid=").append(pushInfo.getId());

				intent = new Intent(context, ExchangePrizeDetailsActivity.class);
				intent.putExtra(ExchangePrizeDetailsActivity.PRIZE_ID, pushInfo.getId());
				intent.putExtra(ExchangePrizeDetailsActivity.PRIZE_URL, prize_url.toString());
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				break;

			case PushInfo.PUSH_TYPE_PRODUCT:
				StringBuilder url = new StringBuilder();
				url.append(ServerUrlConfig.SERVER_URL).append("/product/groupProductDetail?id=").append(pushInfo.getId());

				intent = new Intent(context, GroupGoodsDetailsActivity.class);
				intent.putExtra(GroupGoodsDetailsActivity.GOODS_URL, url.toString());
				break;

			case PushInfo.PUSH_TYPE_TUANGOU:
				intent = new Intent(context, GroupWebViewActivity.class);
				intent.putExtra(GroupWebViewActivity.URL, ServerUrlConfig.SERVER_URL + "/product/index");
				intent.putExtra(GroupWebViewActivity.SHOW_BACK, true);
				break;

			case PushInfo.PUSH_TYPE_URL:
				intent = new Intent(context, WebViewActivity.class);
				intent.putExtra(WebViewActivity.URL, pushInfo.getHref());
				break;

			case PushInfo.PUSH_TYPE_COMMENT:
				if (V2GogoApplication.getMasterLoginState())
				{
					intent = new Intent(context, ProfileCommentTabActivity.class);
				}
				else
				{
					intent = new Intent(context, MainTabActivity.class);
				}
				break;

			case PushInfo.PUSH_TYPE_ETC_QUAN:
				if (V2GogoApplication.getMasterLoginState())
				{
					intent = new Intent(context, ProfileEtcQuanActivity.class);
				}
				else
				{
					intent = new Intent(context, MainTabActivity.class);
				}
				break;
			case PushInfo.PUSH_TYPE_SPECIALTOPIC:// 专题
				intent = new Intent(context, HomeSubjectActivity.class);
				intent.putExtra(HomeSubjectActivity.SUBJECT_ID, pushInfo.getId());
				break;

			default:
				break;
		}
		return intent;
	}
}
