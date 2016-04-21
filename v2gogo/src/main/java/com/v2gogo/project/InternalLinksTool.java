package com.v2gogo.project;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.v2gogo.project.activity.WebViewActivity;
import com.v2gogo.project.activity.account.AccountLoginActivity;
import com.v2gogo.project.activity.exchange.ExchangeActivity;
import com.v2gogo.project.activity.exchange.ExchangePrizeDetailsActivity;
import com.v2gogo.project.activity.home.BaseDetailsctivity;
import com.v2gogo.project.activity.home.HomeArticleDetailsActivity;
import com.v2gogo.project.activity.home.theme.HomeThemePhotoTabActivity;
import com.v2gogo.project.activity.profile.ProfileOrderActivity;
import com.v2gogo.project.activity.shop.CommitOrderActivity;
import com.v2gogo.project.activity.shop.GroupGoodsDetailsActivity;
import com.v2gogo.project.activity.shop.GroupProductListWebViewActivity;
import com.v2gogo.project.activity.shop.GroupProductTypeWebViewActivity;
import com.v2gogo.project.domain.SliderInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.utils.common.LogUtil;
import com.v2gogo.project.utils.http.IntentExtraConstants;

/**
 * 功能：内部链接统一处理工具类
 * 
 * @ahthor：黄荣星
 * @date:2015-12-9
 * @version::V1.0
 */
public class InternalLinksTool
{
	/**
	 * （1）、文章详细（已实现）:
	 * v2gogo://?type=0&srcId=fc0b3962329e4d9887874ff81070a8cc
	 * （2）、商品详细（已实现）:
	 * v2gogo://?type=2&srcId=fc0b3962329e4d9887874ff81070a8cc
	 * （3）、兑换奖品详细（已实现）:
	 * v2gogo://?type=1&srcId=fc0b3962329e4d9887874ff81070a8cc
	 * （4）、原生晒照片列表（已实现）:
	 * v2gogo://?type=4&srcId=fc0b3962329e4d9887874ff81070a8cc
	 * （5）、团购列表（未实现）:
	 * v2gogo://?type=3&srcId=
	 * （6）、兑换列表（未实现）:
	 * v2gogo://?type=5&srcId=
	 * （7）、分享（未实现）:
	 * v2gogo://?type=5&title=晒照片分享&url=http://test-api.v2gogo.com/topic/getTopic?tId=48d96f7407
	 * ae43c3937d520b8a58f5b8
	 * （8）、上传图片（未实现）
	 * v2gogo://?type=7&srcId=
	 * （9）、上传声音（未实现）
	 * v2gogo://?type=8&srcId=
	 */

	/**
	 * 页面跳转
	 */
	/*
	 * public static void jump2Activity(final Context context, int type, String id, String url)
	 * {
	 * if (type == 0 || type == 1 || type == 2 || type == 4)
	 * {
	 * if (TextUtils.isEmpty(id))
	 * {
	 * return;
	 * }
	 * }
	 * Intent intent = null;
	 * switch (type)
	 * {
	 * // 文章详细
	 * case SliderInfo.SRC_TYPE_INFO:
	 * intent = new Intent(context, HomeArticleDetailsActivity.class);
	 * intent.putExtra(BaseDetailsctivity.ID, id);
	 * context.startActivity(intent);
	 * break;
	 * // 兑换奖品详细
	 * case SliderInfo.SRC_TYPE_PRIZE:
	 * intent = new Intent(context, ExchangePrizeDetailsActivity.class);
	 * intent.putExtra(ExchangePrizeDetailsActivity.PRIZE_ID, id);
	 * context.startActivity(intent);
	 * break;
	 * // 商品详细
	 * case SliderInfo.SRC_TYPE_PRODUCT:
	 * intent = new Intent(context, GroupGoodsDetailsActivity.class);
	 * intent.putExtra(GroupGoodsDetailsActivity.GOODS_URL, url);
	 * intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 * context.startActivity(intent);
	 * break;
	 * // 团购列表-3
	 * case SliderInfo.SRC_TYPE_SHOP:
	 * intent = new Intent(context, ShopActivity.class);
	 * context.startActivity(intent);
	 * break;
	 * // 原生晒照片列表
	 * case SliderInfo.SRC_TYPE_THEME:
	 * intent = new Intent(context, HomeThemePhotoTabActivity.class);
	 * intent.putExtra(IntentExtraConstants.TID, id);
	 * intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 * context.startActivity(intent);
	 * break;
	 * // 兑换列表-5
	 * case SliderInfo.SRC_TYPE_EXCHANGE:
	 * intent = new Intent(context, ExchangeActivity.class);
	 * intent.putExtra(ExchangeActivity.SHOW_BACK, true);
	 * intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 * context.startActivity(intent);
	 * break;
	 * // 外部链接
	 * case SliderInfo.SRC_TYPE_WEBSITE:
	 * intent = forward2Website(context, url, intent);
	 * break;
	 * default:
	 * break;
	 * }
	 * if (null != intent)
	 * {
	 * intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 * context.startActivity(intent);
	 * }
	 * }
	 */

	/**
	 * method desc：
	 * 页面跳转
	 * 
	 * @param type
	 *            类型
	 * @param info
	 *            标志
	 * @param originalData
	 *            原始json格式数据
	 */
	// public static void jump2Activity(final Context context, String originalData,
	// InternalLinksListerner listerner)
	public static void jump2Activity(final Context context, int type, String id, String originalData, InternalLinksListerner listerner)
	{
		LogUtil.e("type:" + type + " id:" + id + " url:" + originalData);
		if (type == 0 || type == 1 || type == 4)
		{
			if (TextUtils.isEmpty(id))
			{
				return;
			}
		}
		Intent intent = null;
		switch (type)
		{
		// 文章详细
			case SliderInfo.SRC_TYPE_INFO:
				intent = new Intent(context, HomeArticleDetailsActivity.class);
				intent.putExtra(BaseDetailsctivity.ID, id);
				context.startActivity(intent);
				break;

			// 兑换奖品详细
			case SliderInfo.SRC_TYPE_PRIZE:
				StringBuilder url = new StringBuilder();
				url.append(ServerUrlConfig.SERVER_URL).append("/prizepaperapp/getExchangePrizeDetail?pid=").append(id);

				intent = new Intent(context, ExchangePrizeDetailsActivity.class);
				intent.putExtra(ExchangePrizeDetailsActivity.PRIZE_ID, id);
				intent.putExtra(ExchangePrizeDetailsActivity.PRIZE_URL, url.toString());
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(intent);
				break;

			// 商品详细
			case SliderInfo.SRC_TYPE_PRODUCT:

				intent = new Intent(context, GroupGoodsDetailsActivity.class);
				intent.putExtra(GroupGoodsDetailsActivity.GOODS_URL, originalData);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(intent);
				break;
			// 团购列表-3
			case SliderInfo.SRC_TYPE_SHOP:
				intent = new Intent(context, GroupProductListWebViewActivity.class);
				intent.putExtra(GroupProductListWebViewActivity.URL, originalData);
				context.startActivity(intent);
				break;

			// 原生晒照片列表-4
			case SliderInfo.SRC_TYPE_THEME:
				intent = new Intent(context, HomeThemePhotoTabActivity.class);
				intent.putExtra(IntentExtraConstants.TID, id);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(intent);
				break;
			// 兑换列表-5
			case SliderInfo.SRC_TYPE_EXCHANGE:
				intent = new Intent(context, ExchangeActivity.class);
				intent.putExtra(ExchangeActivity.SHOW_BACK, true);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(intent);
				break;
			// 分享-6
			case SliderInfo.SRC_TYPE_SHARED:
				if (originalData != null && listerner != null)
				{
					listerner.onInternalLinks(originalData);
				}

				break;
			// 上传图片-7
			case SliderInfo.SRC_TYPE_UPLOAD_PIC:
				if (listerner != null)
				{
					listerner.onInternalLinks(originalData);
				}
				break;
			// 上传声音-8
			case SliderInfo.SRC_TYPE_UPLOAD_VOICE:
				if (listerner != null)
				{
					listerner.onInternalLinks(originalData);
				}
				break;
			// 外部链接
			case SliderInfo.SRC_TYPE_WEBSITE:
				forward2Website(context, originalData);
				break;
			// 商品类型
			case SliderInfo.SRC_TYPE_PRODUCT_TYPE:

				intent = new Intent(context, GroupProductTypeWebViewActivity.class);
				intent.putExtra(GroupProductTypeWebViewActivity.URL, originalData);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(intent);
				break;
			// 订单查询
			case SliderInfo.SRC_TYPE_ORDER_lIST:
				if (!V2GogoApplication.getMasterLoginState())
				{
					AccountLoginActivity.forwardAccountLogin(context);
				}
				else
				{
					intent = new Intent(context, ProfileOrderActivity.class);
					context.startActivity(intent);
				}
				break;
			// 立即购买
			case SliderInfo.SRC_TYPE_BUY_NOW:
				try
				{
					if (!V2GogoApplication.getMasterLoginState())
					{
						AccountLoginActivity.forwardAccountLogin(context);
					}
					else
					{
						intent = new Intent(context, CommitOrderActivity.class);
						intent.putExtra(CommitOrderActivity.GOODS_ID, id);
						context.startActivity(intent);
					}
				}
				catch (Exception e)
				{
				}
				break;
			// 立即购买
			case SliderInfo.SRC_TYPE_BUY_lOVE:
				if (listerner != null)
				{
					listerner.onInternalLinks(originalData);
				}
				break;
			// 升级
			case SliderInfo.SRC_TYPE_UPDATE:
				if (listerner != null)
				{
					listerner.onInternalLinks(originalData);
				}
				break;
				// 升级
			case SliderInfo.SRC_TYPE_UPLOADE_FILE:
				if (listerner != null)
				{
					listerner.onInternalLinks(originalData);
				}
				break;
			default:
				break;
		}
	}

	public interface InternalLinksListerner
	{
		public void onInternalLinks(String originalData);
	}

	// /**
	// * 跳转到外部链接
	// */
	// public static Intent forward2Website(Context context, String url, Intent intent)
	// {
	// if (url.contains(ServerUrlConfig.SERVER_URL))
	// {
	// if (!V2GogoApplication.getMasterLoginState())
	// {
	// AccountLoginActivity.forwardAccountLogin(context);
	// }
	// else
	// {
	// intent = new Intent(context, WebViewActivity.class);
	// intent.putExtra(WebViewActivity.URL, url);
	// }
	// }
	// else
	// {
	// intent = new Intent(context, WebViewActivity.class);
	// intent.putExtra(WebViewActivity.URL, url);
	// }
	// return intent;
	// }

	/**
	 * 跳转到外部链接
	 */
	public static void forward2Website(Context context, String info)
	{
		if (TextUtils.isEmpty(info))
		{
			return;
		}
		Intent intent = null;
		if (info.contains("#login"))
		{
			if (V2GogoApplication.getMasterLoginState())
			{
				intent = new Intent(context, WebViewActivity.class);
				intent.putExtra(WebViewActivity.URL, info);
				context.startActivity(intent);
			}
			else
			{
				AccountLoginActivity.forwardAccountLogin(context);
			}
		}
		else
		{
			intent = new Intent(context, WebViewActivity.class);
			intent.putExtra(WebViewActivity.URL, info);
			context.startActivity(intent);
		}

	}

}
