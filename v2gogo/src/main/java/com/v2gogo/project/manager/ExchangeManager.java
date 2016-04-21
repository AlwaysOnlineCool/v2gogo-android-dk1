package com.v2gogo.project.manager;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.v2gogo.project.db.CacheInfo;
import com.v2gogo.project.domain.exchange.ExchangeInfo;
import com.v2gogo.project.domain.exchange.PrizeDetailsInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.manager.HttpJsonObjectRequest.IOnDataReceiveMessageCallback;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.utils.StatusCode;
import com.v2gogo.project.utils.common.MD5Util;
import com.v2gogo.project.utils.dao.DbService;
import com.v2gogo.project.utils.http.HttpProxy;
import com.v2gogo.project.utils.parse.JsonParser;

/**
 * 兑换专区管理器
 * 
 * @author houjun
 */
public class ExchangeManager
{
	public static final int FIRST_PAGE = 1;

	/**
	 * 拉取兑换商品列表
	 * 
	 * @param context
	 *            上下文环境
	 * @param currentPage
	 *            拉取数据的页码数(第一页为1，顺序叠加)
	 * @param callback
	 *            数据返回回调接口
	 */
	public static void getExchangeGoodsList(final int currentPage, final IonExchangeListCallback callback)
	{
		String url = ServerUrlConfig.SERVER_URL + "/prizepaperapp/publist?page=" + currentPage;
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("getExchangeGoodsList", Request.Method.GET, url, null,
				new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int result, String message, JSONObject jsonObject)
					{
						if (StatusCode.SUCCESS == result)
						{
							ExchangeInfo exchangeInfo = (ExchangeInfo) JsonParser.parseObject(jsonObject.toString(), ExchangeInfo.class);
							if (exchangeInfo.getCurrentPage() == FIRST_PAGE)
							{
								cacheExchangeDatas(jsonObject);
							}
							if (null != callback)
							{
								callback.onExchangeListSuccess(exchangeInfo);
							}
						}
						else
						{
							if (null != callback)
							{
								callback.onExchangeListFail(message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (null != callback)
						{
							callback.onExchangeListFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 兑换专区立即兑换
	 * 
	 * @param context
	 *            上下文环境
	 * @param srcId
	 *            奖品id
	 * @param userId
	 *            兑换用户id
	 * @param callback
	 *            兑换结果数据回调
	 */
	public static void exchangeGoodsByGoodsId(final String srcId, final String username, final IonExchangeCallback callback)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("pid", srcId);
		if (V2GogoApplication.getMasterLoginState())
		{
			params.put("username", V2GogoApplication.getCurrentMatser().getUsername());
			params.put("token", V2GogoApplication.getCurrentMatser().getToken());
			String signature = MD5Util.getMd5Token(params);
			params.put("signature", signature);
		}
		if (params.containsKey("token"))
		{
			params.remove("token");
		}
		final JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("exchangeGoodsByGoodsId", Request.Method.POST,
				ServerUrlConfig.SERVER_URL + "/userprizeapp/convertpaper", params, new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int result, String message, JSONObject jsonObject)
					{
						if (StatusCode.SUCCESS == result)
						{
							int coin = jsonObject.optInt("coin");
							if (V2GogoApplication.getMasterLoginState())
							{
								V2GogoApplication.getCurrentMatser().setCoin(coin);
								V2GogoApplication.updateMatser();
							}
							if (null != callback)
							{
								callback.onExchangeSuccess();
							}
						}
						else
						{
							if (null != callback)
							{
								callback.onExchangeFail(message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (null != callback)
						{
							callback.onExchangeFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 兑换专区立即兑换
	 * 
	 * @param context
	 *            上下文环境
	 * @param srcId
	 *            奖品id
	 * @param userId
	 *            兑换用户id
	 * @param callback
	 *            兑换结果数据回调
	 */
	public static void convertpaper(final String id, IOnDataReceiveMessageCallback callback)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("pid", id);
		if (V2GogoApplication.getMasterLoginState())
		{
			params.put("username", V2GogoApplication.getCurrentMatser().getUsername());
			params.put("token", V2GogoApplication.getCurrentMatser().getToken());
			String signature = MD5Util.getMd5Token(params);
			params.put("signature", signature);
		}
		if (params.containsKey("token"))
		{
			params.remove("token");
		}
		final JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("getcrowdfunding", Request.Method.POST,
				ServerUrlConfig.SERVER_URL + "/userprizeapp/convertpaper", params, callback);
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 拉取兑换奖品详情
	 * 
	 * @param context
	 *            上下文环境
	 * @param prizeId
	 *            奖品id
	 * @param callback
	 *            详情数据回调接口
	 */
	public static void getExchangePrizeDatilesById(String prizeId, final IonExchangePrizeDetailsCallback callback)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("pid", prizeId);
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("getExchangePrizeDatilesById", Request.Method.POST,
				ServerUrlConfig.SERVER_URL + "/prizepaperapp/getprizeinfo", params, new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int result, String message, JSONObject jsonObject)
					{
						if (StatusCode.SUCCESS == result)
						{
							JSONObject object = jsonObject.optJSONObject("prize");
							if (null != jsonObject)
							{
								PrizeDetailsInfo prizeDetailsInfo = (PrizeDetailsInfo) JsonParser.parseObject(object.toString(), PrizeDetailsInfo.class);
								if (null != callback)
								{
									callback.onExchangePrizeDetails(prizeDetailsInfo);
								}
							}
						}
						else
						{
							if (null != callback)
							{
								callback.onExchangePrizeFail(message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (null != callback)
						{
							callback.onExchangePrizeFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 获取缓存的兑换商品列表
	 * 
	 * @param context
	 * @return
	 */
	public static ExchangeInfo getLocalExchangeGoodsList(Context context)
	{
		CacheInfo cacheInfo = DbService.getInstance().getCacheData(CacheInfo.TYPE_EXCHANGE);
		if (null != cacheInfo)
		{
			ExchangeInfo exchangeInfo = (ExchangeInfo) JsonParser.parseObject(cacheInfo.getResponse(), ExchangeInfo.class);
			return exchangeInfo;
		}
		return null;
	}

	/**
	 * 缓存兑换专区数据
	 */
	private static void cacheExchangeDatas(JSONObject response)
	{
		CacheInfo cacheInfo = new CacheInfo();
		cacheInfo.setType(CacheInfo.TYPE_EXCHANGE);
		cacheInfo.setResponse(response.toString());
		DbService.getInstance().insertCacheData(cacheInfo);
	}

	/**
	 * 取消兑换列表请求任务
	 */
	public static void clearExchangeGoodsListTask()
	{
		HttpProxy.removeRequestTask("getExchangeGoodsList");
	}

	/**
	 * 取消奖品详情请求任务
	 */
	public static void clearExchangePrizeDatilesByIdTask()
	{
		HttpProxy.removeRequestTask("getExchangePrizeDatilesById");
	}

	/**
	 * 取消兑换奖品请求任务
	 */
	public static void clearExchangeGoodsByGoodsIdTask()
	{
		HttpProxy.removeRequestTask("exchangeGoodsByGoodsId");
	}

	/**
	 * 兑换专区列表数据回调
	 * 
	 * @author houjun
	 */
	public interface IonExchangeListCallback
	{
		public void onExchangeListSuccess(ExchangeInfo exchangeInfo);

		public void onExchangeListFail(String errorMessage);
	}

	/**
	 * 立即兑换数据回调
	 * 
	 * @author houjun
	 */
	public interface IonExchangeCallback
	{
		public void onExchangeSuccess();

		public void onExchangeFail(String errorMessage);
	}

	/**
	 * 奖品详情数据回调
	 * 
	 * @author houjun
	 */
	public interface IonExchangePrizeDetailsCallback
	{
		public void onExchangePrizeDetails(PrizeDetailsInfo prizeDetailsInfo);

		public void onExchangePrizeFail(String errorMessage);
	}
}
