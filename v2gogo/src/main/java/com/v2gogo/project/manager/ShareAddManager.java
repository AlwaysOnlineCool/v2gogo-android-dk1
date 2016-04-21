package com.v2gogo.project.manager;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.utils.http.HttpProxy;

/**
 * 功能：
 * 
 * @ahthor：黄荣星
 * @date:2015-12-15
 * @version::V1.0
 */
public class ShareAddManager
{
	/**
	 * 文章分享成功新增记录
	 * method desc：
	 * 
	 * @param infoId
	 * @param callback
	 */
	public static void shareArticleAdd(final String infoId)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("infoId", infoId);
		if (V2GogoApplication.getMasterLoginState())
		{
			params.put("userId", V2GogoApplication.getCurrentMatser().getUserid());
		}
		else
		{
			params.put("userId", "");
		}
		final JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("exchangeGoodsByGoodsId", Request.Method.POST,
				ServerUrlConfig.SERVER_URL + "/shareInfo/add", params, null);
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 商品分享成功新增记录
	 * method desc：
	 * 
	 * @param infoId
	 * @param callback
	 */
	public static void shareGoodsAdd(final String productId)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("productId", productId);
		if (V2GogoApplication.getMasterLoginState())
		{
			params.put("userId", V2GogoApplication.getCurrentMatser().getUserid());
		}
		else
		{
			params.put("userId", "");
		}
		final JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("exchangeGoodsByGoodsId", Request.Method.POST,
				ServerUrlConfig.SERVER_URL + "/shareProduct/add", params, null);
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 奖品分享成功新增记录
	 * method desc：
	 * 
	 * @param infoId
	 * @param callback
	 */
	public static void sharePrizeAdd(final String prizeId)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("prizeId", prizeId);
		if (V2GogoApplication.getMasterLoginState())
		{
			params.put("userId", V2GogoApplication.getCurrentMatser().getUserid());
		}
		else
		{
			params.put("userId", "");
		}
		final JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("exchangeGoodsByGoodsId", Request.Method.POST,
				ServerUrlConfig.SERVER_URL + "/sharePrize/add", params, null);
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}
}
