package com.v2gogo.project.manager.shop;

import org.json.JSONObject;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.v2gogo.project.db.CacheInfo;
import com.v2gogo.project.domain.shop.GoodsListInfo;
import com.v2gogo.project.manager.HttpJsonObjectRequest;
import com.v2gogo.project.manager.HttpJsonObjectRequest.IOnDataReceiveMessageCallback;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.utils.StatusCode;
import com.v2gogo.project.utils.dao.DbService;
import com.v2gogo.project.utils.http.HttpProxy;
import com.v2gogo.project.utils.parse.JsonParser;

public class ShopManager
{

	public static final int FIRST_PAGE = 1;

	public static final int TYPE_ALL = -1;
	public static final int TYPE_NORML_GOODS = 0;
	public static final int TYPE_NORML_SHOP = 1;
	public static final int TYPE_NORML_SECKILL = 2;

	/**
	 * 回去商品列表
	 */
	public static void getShopGoodsList(final int page, final IonGetGoodsListCallback callback)
	{
		String url = ServerUrlConfig.SERVER_URL + "/product/list?pageNo=" + page;
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("getShopGoodsList", Request.Method.GET, url, null,
				new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int result, String message, JSONObject jsonObject)
					{
						if (StatusCode.SUCCESS == result)
						{
							GoodsListInfo goodsListInfo = (GoodsListInfo) JsonParser.parseObject(jsonObject.toString(), GoodsListInfo.class);
							if (goodsListInfo != null && goodsListInfo.getPage() == FIRST_PAGE)
							{
								cacheGoodsInfos(jsonObject);
							}
							if (callback != null)
							{
								callback.onGetGoodsListSuccess(goodsListInfo);
							}
						}
						else
						{
							if (callback != null)
							{
								callback.onGetGoodsListFail(message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (callback != null)
						{
							callback.onGetGoodsListFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 缓存团购商品列表数据
	 */
	private static void cacheGoodsInfos(JSONObject jsonObject)
	{
		CacheInfo cacheInfo = new CacheInfo();
		cacheInfo.setType(CacheInfo.TYPE_SHOP);
		cacheInfo.setResponse(jsonObject.toString());
		DbService.getInstance().insertCacheData(cacheInfo);
	}

	/**
	 * 加载本地团购商品列表数据
	 * 
	 * @return
	 */
	public static GoodsListInfo getAppLocalGoodsListInfos(Context context)
	{
		CacheInfo cacheInfo = DbService.getInstance().getCacheData(CacheInfo.TYPE_SHOP);
		if (null != cacheInfo)
		{
			GoodsListInfo goodsListInfo = (GoodsListInfo) JsonParser.parseObject(cacheInfo.getResponse(), GoodsListInfo.class);
			return goodsListInfo;
		}
		return null;
	}

	/**
	 * 清除请求任务
	 */
	public static void clearGetGoodsListTask()
	{
		HttpProxy.removeRequestTask("getShopGoodsList");
	}

	public interface IonGetGoodsListCallback
	{
		public void onGetGoodsListSuccess(GoodsListInfo goodsListInfo);

		public void onGetGoodsListFail(String errorMessage);
	}
}
