package com.v2gogo.project.domain.shop;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

/**
 * 商品详情实体类
 * 
 * @author houjun
 */
public class GoodsDetailsInfo implements Serializable
{

	private static final long serialVersionUID = -2928517695603289126L;

	@SerializedName("product")
	private GoodsInfo mGoodsInfo;

	public GoodsInfo getGoodsInfo()
	{
		return mGoodsInfo;
	}

	public void setGoodsInfo(GoodsInfo mGoodsInfo)
	{
		this.mGoodsInfo = mGoodsInfo;
	}

	@Override
	public String toString()
	{
		return "GoodsDetailsInfo [mGoodsInfo=" + mGoodsInfo + "]";
	}

}
