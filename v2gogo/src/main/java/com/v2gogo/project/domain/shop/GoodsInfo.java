package com.v2gogo.project.domain.shop;

import java.io.Serializable;

import com.v2gogo.project.utils.qiniu.VersionPhotoUrlBuilder;

/**
 * 商品实体类
 * 
 * @author houjun
 */
public class GoodsInfo implements Serializable
{

	private static final long serialVersionUID = 9157387028305903586L;

	public static enum GOODS_STATUS
	{
		NEW_ADD, SHEN_HEI, PUBLISGED
	}

	public static final int SUPPORT_PAY_METHOD_ALL = 0;
	public static final int SUPPORT_PAY_METHOD_ONLINE = 1;
	public static final int SUPPORT_PAY_METHOD_ARRIVE = 2;

	private float v2gogoPrice;// 商品价格
	private float postage;// 邮费
	private float originalPrice;
	private float poundage;// 手续费

	private String srcSmImg;// 小图
	private String srcImg;// 大图
	private String detailedInfo;// 图文
	private String id;
	private String unit;// 单位
	private String descriptions;
	private String typeName;// 商品类型
	private String productName;// 商品名称
	private String mThumbialUrl;

	private int status;
	private int postNum;// 邮寄数量
	private int salesVolume;// 累积销量
	private int stock;// 库存

	private int payMethodSupport;

	private long saveTime;// 上架时间

	public float getV2gogoPrice()
	{
		return v2gogoPrice;
	}

	public void setV2gogoPrice(float v2gogoPrice)
	{
		this.v2gogoPrice = v2gogoPrice;
	}

	public String getSrcSmImg()
	{
		if (null == mThumbialUrl)
		{
			mThumbialUrl = VersionPhotoUrlBuilder.createThumbialUrl(VersionPhotoUrlBuilder.createVersionImageUrl(srcSmImg));
		}
		return mThumbialUrl;
	}

	public void setSrcSmImg(String srcSmImg)
	{
		this.srcSmImg = srcSmImg;
	}

	public String getSrcImg()
	{
		return VersionPhotoUrlBuilder.createNoFixedImage(VersionPhotoUrlBuilder.createVersionImageUrl(srcImg));
	}

	public void setSrcImg(String srcImg)
	{
		this.srcImg = srcImg;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public int getPostNum()
	{
		return postNum;
	}

	public void setPostNum(int postNum)
	{
		this.postNum = postNum;
	}

	public int getSalesVolume()
	{
		return salesVolume;
	}

	public void setSalesVolume(int salesVolume)
	{
		this.salesVolume = salesVolume;
	}

	public String getDetailedInfo()
	{
		return detailedInfo;
	}

	public void setDetailedInfo(String detailedInfo)
	{
		this.detailedInfo = detailedInfo;
	}

	public float getPostage()
	{
		return postage;
	}

	public void setPostage(float postage)
	{
		this.postage = postage;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getUnit()
	{
		return unit;
	}

	public void setUnit(String unit)
	{
		this.unit = unit;
	}

	public int getStock()
	{
		return stock;
	}

	public void setStock(int stock)
	{
		this.stock = stock;
	}

	public long getSaveTime()
	{
		return saveTime;
	}

	public void setSaveTime(long saveTime)
	{
		this.saveTime = saveTime;
	}

	public float getOriginalPrice()
	{
		return originalPrice;
	}

	public void setOriginalPrice(float originalPrice)
	{
		this.originalPrice = originalPrice;
	}

	public float getPoundage()
	{
		return poundage;
	}

	public void setPoundage(float poundage)
	{
		this.poundage = poundage;
	}

	public String getDescription()
	{
		return descriptions;
	}

	public void setDescription(String description)
	{
		this.descriptions = description;
	}

	public String getTypeName()
	{
		return typeName;
	}

	public void setTypeId(String typeId)
	{
		this.typeName = typeId;
	}

	public String getProductName()
	{
		return productName;
	}

	public void setProductName(String productName)
	{
		this.productName = productName;
	}

	public int getPayMethodSupport()
	{
		return payMethodSupport;
	}

	public void setPayMethodSupport(int payMethodSupport)
	{
		this.payMethodSupport = payMethodSupport;
	}

	@Override
	public String toString()
	{
		return "GoodsInfo [v2gogoPrice=" + v2gogoPrice + ", postage=" + postage + ", originalPrice=" + originalPrice + ", poundage=" + poundage + ", srcSmImg="
				+ srcSmImg + ", srcImg=" + srcImg + ", detailedInfo=" + detailedInfo + ", id=" + id + ", unit=" + unit + ", descriptions=" + descriptions
				+ ", typeName=" + typeName + ", productName=" + productName + ", status=" + status + ", postNum=" + postNum + ", salesVolume=" + salesVolume
				+ ", stock=" + stock + ", payMethodSupport=" + payMethodSupport + ", saveTime=" + saveTime + "]";
	}

}
