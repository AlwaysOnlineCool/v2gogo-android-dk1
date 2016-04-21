package com.v2gogo.project.manager;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.v2gogo.project.domain.RankInfo;
import com.v2gogo.project.manager.HttpJsonObjectRequest.IOnDataReceiveMessageCallback;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.utils.StatusCode;
import com.v2gogo.project.utils.http.HttpProxy;

/**
 * 金币排行榜管理器
 * 
 * @author houjun
 */
public class CoinRankManager
{

	/**
	 * 拉取金币排行榜数据
	 * 
	 * @param context
	 * @param callback
	 */
	public static void getRankCoinList(final IongetRankCoinListCallback callback)
	{
		JsonObjectRequest jsonObjectRequest = HttpJsonObjectRequest.createJsonObjectHttpRequest("getRankCoinList", Request.Method.GET,
				ServerUrlConfig.SERVER_URL + "/usercointopapp/getusercointop", null, new IOnDataReceiveMessageCallback()
				{
					@Override
					public void onSuccess(int result,String message, JSONObject jsonObject)
					{
						if (StatusCode.SUCCESS == result)
						{
							JSONArray jsonArray = jsonObject.optJSONArray("list");
							if (null != jsonArray)
							{
								List<RankInfo> rankInfos = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<RankInfo>>()
								{
								}.getType());
								if (null != callback)
								{
									callback.onGetRankCoinListSuccess(rankInfos);
								}
							}
						}
						else 
						{
							if (null != callback)
							{
								callback.onGetRankCoinListFail(message);
							}
						}
					}

					@Override
					public void onError(String errorMessage)
					{
						if (null != callback)
						{
							callback.onGetRankCoinListFail(errorMessage);
						}
					}
				});
		HttpProxy.luanchJsonObjectRequest(jsonObjectRequest);
	}

	/**
	 * 清除拉取金币排行榜数据请求任务
	 */
	public static void clearGetRankCoinListTask()
	{
		HttpProxy.removeRequestTask("getRankCoinList");
	}

	/**
	 * 拉取金币排行榜数据回调接口
	 * 
	 * @author houjun
	 */
	public interface IongetRankCoinListCallback
	{
		public void onGetRankCoinListSuccess(List<RankInfo> rankInfos);

		public void onGetRankCoinListFail(String errorMessage);
	}

}
