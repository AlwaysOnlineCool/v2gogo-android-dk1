package com.v2gogo.project.domain.shop;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * 购物车实体类
 * 
 * @author houjun
 */
public class CartInfo implements Serializable
{

	private static final long serialVersionUID = 6334019162041437419L;

	@SerializedName("result")
	public String mResult;

	public List<CartItemInfo> mCartItemInfos;

	public String getResult()
	{
		return mResult;
	}

	public void setResult(String mResult)
	{
		this.mResult = mResult;
	}

	public List<CartItemInfo> getCartItemInfos()
	{
		return mCartItemInfos;
	}

	public void setCartItemInfos(List<CartItemInfo> mCartItemInfos)
	{
		this.mCartItemInfos = mCartItemInfos;
	}

	@Override
	public String toString()
	{
		return "CartInfo [mResult=" + mResult + ", mCartItemInfos=" + mCartItemInfos + "]";
	}

}
