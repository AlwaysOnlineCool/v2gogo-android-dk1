package com.v2gogo.project.domain.shop;

import java.io.Serializable;

/**
 * 购物车每一栏的实体类
 * 
 * @author houjun
 */
public class CartItemInfo implements Serializable
{
	private static final long serialVersionUID = -9000438392061639656L;

	public static final int PAY_METHOD_ARRIVE = 0;
	public static final int PAY_METHOD_ONLINE = 1;

	private int mBuyNum;
	private int mPayMethod;
	private int mSupportPayMehod;

	private GoodsInfo mGoodsInfo;

	private float mPostPrice = 0.0f;
	private float mOrderPrice = 0.0f;
	private float mExtraPrice = 0.0f;
	private float mGoodPrice = 0.0f;

	public GoodsInfo getGoodsInfo()
	{
		return mGoodsInfo;
	}

	public void setGoodsInfo(GoodsInfo mGoodsInfo)
	{
		this.mGoodsInfo = mGoodsInfo;
	}

	public int getBuyNum()
	{
		return mBuyNum;
	}

	public void setBuyNum(int mBuyNum)
	{
		this.mBuyNum = mBuyNum;
	}

	public int getPayMethod()
	{
		return mPayMethod;
	}

	public void setPayMethod(int payMethod)
	{
		mPayMethod = payMethod;
	}

	public void setSupportPayMethod(int supportPayMethod)
	{
		mSupportPayMehod = supportPayMethod;
		if (mSupportPayMehod == GoodsInfo.SUPPORT_PAY_METHOD_ALL)
		{
			mPayMethod = PAY_METHOD_ONLINE;
		}
		else if (mSupportPayMehod == GoodsInfo.SUPPORT_PAY_METHOD_ARRIVE)
		{
			mPayMethod = PAY_METHOD_ARRIVE;
		}
		else if (mSupportPayMehod == GoodsInfo.SUPPORT_PAY_METHOD_ONLINE)
		{
			mPayMethod = PAY_METHOD_ONLINE;
		}
	}

	public int getSupportPayMethod()
	{
		return mSupportPayMehod;
	}

	/**
	 * 得到商品金额
	 * 
	 * @return
	 */
	public float getGoodPrice()
	{
		if (mGoodsInfo != null)
		{
			mGoodPrice = mBuyNum * mGoodsInfo.getV2gogoPrice();
		}
		return mGoodPrice;
	}

	/**
	 * 得到商品邮费
	 * 
	 * @return
	 */
	public float getPostPrice()
	{
		if (mGoodsInfo != null)
		{
			if (mGoodsInfo.getPostage() == 0 || mGoodsInfo.getPostage() == 0.0f || mGoodsInfo.getPostage() == 0.00f)
			{
				mPostPrice = 0.0f;
			}
			else
			{
				mPostPrice = mBuyNum % mGoodsInfo.getPostNum() == 0 ? mBuyNum / mGoodsInfo.getPostNum() * mGoodsInfo.getPostage() : (mBuyNum
						/ mGoodsInfo.getPostNum() + 1)
						* mGoodsInfo.getPostage();
			}
		}
		return mPostPrice;
	}

	/**
	 * 得到订单金额
	 * 
	 * @return
	 */
	public float getOrderPrice()
	{
		if (mPayMethod == 0)
		{
			mOrderPrice = getGoodPrice() + getPostPrice() + getExtraPrice();
		}
		else
		{
			mOrderPrice = getGoodPrice() + getPostPrice();
		}
		return mOrderPrice;
	}

	/**
	 * 得到手续费
	 * 
	 * @return
	 */
	public float getExtraPrice()
	{
		if (mGoodsInfo != null)
		{
			this.mExtraPrice = mGoodsInfo.getPoundage();
		}
		return mExtraPrice;
	}

	@Override
	public String toString()
	{
		return "CartItemInfo [mBuyNum=" + mBuyNum + ", mPayMethod=" + mPayMethod + ", mSupportPayMehod=" + mSupportPayMehod + ", mGoodsInfo=" + mGoodsInfo
				+ ", mPostPrice=" + mPostPrice + ", mOrderPrice=" + mOrderPrice + ", mExtraPrice=" + mExtraPrice + ", mGoodPrice=" + mGoodPrice + "]";
	}

}
