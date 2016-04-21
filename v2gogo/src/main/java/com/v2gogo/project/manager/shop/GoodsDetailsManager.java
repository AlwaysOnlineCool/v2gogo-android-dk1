package com.v2gogo.project.manager.shop;

import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.v2gogo.project.domain.shop.GoodsDetailsInfo;
import com.v2gogo.project.manager.HttpJsonObjectRequest;
import com.v2gogo.project.manager.HttpJsonObjectRequest.IOnDataReceiveMessageCallback;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.utils.StatusCode;
import com.v2gogo.project.utils.http.HttpProxy;
import com.v2gogo.project.utils.parse.JsonParser;

/**
 * 商品详情管理器
 * 
 * @author houjun
 */
public class GoodsDetailsManager
{

	/**
	 * 拉取商品详情
	 * 
	 * @param context
	 *            商品详情
	 * @param goodsId
	 *            商品id
	 */
	public static void getGoodsDetailsInfosNew(String goodsId, final IonGoodsDetailsInfosCallback callback)
	{
		// String url = ServerUrlConfig.SERVER_URL + "/product/productinfo?id=" + goodsId;
		String url = ServerUrlConfig.SERVER_URL + "/product/simpleProductInfo?id=" + goodsId;
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("getGoodsDetailsInfos", Request.Method.GET, url, null,
				new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int code, String message, JSONObject response)
					{
						if (StatusCode.SUCCESS == code)
						{
							GoodsDetailsInfo goodsDetailsInfo = (GoodsDetailsInfo) JsonParser.parseObject(response.toString(), GoodsDetailsInfo.class);
							if (callback != null)
							{
								callback.onGoodsDetailsInfosSuccess(goodsDetailsInfo);
							}
						}
						else
						{
							if (null != callback)
							{
								callback.onGoodsDetailsInfosFail(message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (null != callback)
						{
							callback.onGoodsDetailsInfosFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	public static void clearGoodsDetailsInfosTask()
	{
		HttpProxy.removeRequestTask("getGoodsDetailsInfos");
	}

	/**
	 * 商品详情数据回调
	 * 
	 * @author houjun
	 */
	public interface IonGoodsDetailsInfosCallback
	{
		public void onGoodsDetailsInfosSuccess(GoodsDetailsInfo goodsDetailsInfo);

		public void onGoodsDetailsInfosFail(String errorMessage);
	}
}
