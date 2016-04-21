package com.v2gogo.project.domain.shop;

import java.io.Serializable;

import com.v2gogo.project.utils.qiniu.VersionPhotoUrlBuilder;

/**
 * 电子券实体类
 * 
 * @author houjun
 */
public class EtcOrderInfo implements Serializable
{
	private static final long serialVersionUID = 3807485890963514762L;

	public static final int ETC_YET_NOT_USE = 0;
	public static final int ETC_YET_USED = 1;

	private String id;
	private String orderId;
	private String userId;
	private String productId;
	private String productName;
	
	private int buyNum;
	private int replayStatus;
	private float productPrice;
	private float productTotal;
	
	private String img;
	private String descriptions;
	private String replayCode;
	private long createTime;

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

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
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
		return VersionPhotoUrlBuilder.createThumbialUrl(VersionPhotoUrlBuilder.createVersionImageUrl(img));
	}

	public void setImg(String img)
	{
		this.img = img;
	}

	public String getDescriptions()
	{
		return descriptions;
	}

	public void setDescriptions(String descriptions)
	{
		this.descriptions = descriptions;
	}

	public String getReplayCode()
	{
		return replayCode;
	}

	public void setReplayCode(String replayCode)
	{
		this.replayCode = replayCode;
	}

	public long getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(long createTime)
	{
		this.createTime = createTime;
	}

	public int getReplayStatus()
	{
		return replayStatus;
	}

	public void setReplayStatus(int replayStatus)
	{
		this.replayStatus = replayStatus;
	}

	@Override
	public String toString()
	{
		return "EtcOrderInfo [id=" + id + ", orderId=" + orderId + ", userId=" + userId + ", productId=" + productId + ", productName=" + productName
				+ ", buyNum=" + buyNum + ", productPrice=" + productPrice + ", productTotal=" + productTotal + ", img=" + img + ", descriptions="
				+ descriptions + ", replayCode=" + replayCode + ", createTime=" + createTime + "]";
	}

}
