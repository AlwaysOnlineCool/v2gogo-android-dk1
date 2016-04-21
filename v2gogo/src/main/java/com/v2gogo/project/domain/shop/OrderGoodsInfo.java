package com.v2gogo.project.domain.shop;

import java.io.Serializable;

import com.v2gogo.project.utils.qiniu.VersionPhotoUrlBuilder;

/**
 * 订单里面商品信息
 * 
 * @author houjun
 */
public class OrderGoodsInfo implements Serializable
{

	private static final long serialVersionUID = -552368026648450334L;

	private int buyNum;
	private float productPrice;
	private float productTotal;

	private String img;
	private String id;
	private String orderId;
	private String productId;
	private String productName;
	private String descriptions;
	private String mThumbialUrl;

	public int getBuyNum()
	{
		return buyNum;
	}

	public void setBuyNum(int buyNum)
	{
		this.buyNum = buyNum;
	}

	public float getProductPrice()
	{
		return productPrice;
	}

	public void setProductPrice(float productPrice)
	{
		this.productPrice = productPrice;
	}

	public float getProductTotal()
	{
		return productTotal;
	}

	public void setProductTotal(float productTotal)
	{
		this.productTotal = productTotal;
	}

	public String getImg()
	{
		return VersionPhotoUrlBuilder.createVersionImageUrl(img);
	}

	public void setImg(String img)
	{
		this.img = img;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getOrderId()
	{
		return orderId;
	}

	public void setOrderId(String orderId)
	{
		this.orderId = orderId;
	}

	public String getProductId()
	{
		return productId;
	}

	public void setProductId(String productId)
	{
		this.productId = productId;
	}

	public String getProductName()
	{
		return productName;
	}

	public void setProductName(String productName)
	{
		this.productName = productName;
	}

	public String getDescriptions()
	{
		return descriptions;
	}

	public void setDescriptions(String descriptions)
	{
		this.descriptions = descriptions;
	}

	public String getThumbialUrl()
	{
		if (null == mThumbialUrl)
		{
			mThumbialUrl = VersionPhotoUrlBuilder.createThumbialUrl(getImg());
		}
		return mThumbialUrl;
	}

	@Override
	public String toString()
	{
		return "OrderGoodsInfo [buyNum=" + buyNum + ", productPrice=" + productPrice + ", productTotal=" + productTotal + ", img=" + img + ", id=" + id
				+ ", orderId=" + orderId + ", productId=" + productId + ", productName=" + productName + ", descriptions=" + descriptions + "]";
	}

}
