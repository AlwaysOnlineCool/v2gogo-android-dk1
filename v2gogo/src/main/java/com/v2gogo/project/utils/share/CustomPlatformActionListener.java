package com.v2gogo.project.utils.share;

import java.util.HashMap;

import android.app.Activity;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.wechat.utils.WechatClientNotExistException;
import cn.sharesdk.wechat.utils.WechatTimelineNotSupportedException;

import com.v2gogo.project.R;
import com.v2gogo.project.utils.common.ToastUtil;

public class CustomPlatformActionListener implements PlatformActionListener
{
	public static final int SHARE_ARTICLE_FLAG = 1;// 文章
	public static final int SHARE_PRIZE_FLAG = 2;// 奖品
	public static final int SHARE_GOODS_FLAG = 3;// 商品

	private Activity mContext;
	private String tip;

	public CustomPlatformActionListener(Activity mContext, String tip)
	{
		super();
		this.mContext = mContext;
		this.tip = tip;
	}

	public CustomPlatformActionListener(Activity mContext, String tip, String targedId, int flag)
	{
		super();
		this.mContext = mContext;
		this.tip = tip;
	}

	@Override
	public void onCancel(Platform arg0, int arg1)
	{
		ToastUtil.showAlertToast(mContext, "取消分享");
	}

	@Override
	public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2)
	{
		ToastUtil.showConfirmToast(mContext, tip);
	}

	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2)
	{
		dealWithError(arg2, mContext);
	}

	/**
	 * 处理失败
	 */
	public void dealWithError(Throwable throwable, Activity context)
	{
		String failtext = null;
		if (throwable instanceof WechatClientNotExistException)
		{
			failtext = context.getResources().getString(R.string.wechat_client_inavailable);
		}
		else if (throwable instanceof WechatTimelineNotSupportedException)
		{
			failtext = context.getResources().getString(R.string.wechat_client_inavailable);
		}
		ToastUtil.showAlertToast(context, failtext);
	}
}
