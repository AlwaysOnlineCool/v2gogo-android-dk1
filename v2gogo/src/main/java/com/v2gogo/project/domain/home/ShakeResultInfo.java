package com.v2gogo.project.domain.home;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;
import com.v2gogo.project.utils.qiniu.VersionPhotoUrlBuilder;

/**
 * 摇一摇结果实体类
 * 
 * @author houjun
 */
public class ShakeResultInfo implements Serializable
{

	private static final long serialVersionUID = 6002148295269067597L;

	public static final int SHAKE_RESULT_NO_KAI_JIANG = -1;// 未开奖
	public static final int SHAKE_RESULT_NO_GET_ANYTHING = 0;// 未中奖
	public static final int SHAKE_RESULT_GET_COIN = 1;// 中金币
	public static final int SHAKE_RESULT_GET_PRIZE = 2;// 实物

	@SerializedName("type")
	private int mType;

	@SerializedName("coin")
	private int mCoin;

	@SerializedName("prize")
	private ShakePrizeInfo mShakePrizeInfo;

	@SerializedName("msg")
	private String mMessage;

	@SerializedName("desc")
	private String desc;

	@SerializedName("img")
	private String img;

	@SerializedName("url")
	private String url;

	private String mThumbialUrl;

	public int getType()
	{
		return mType;
	}

	public void setType(int mType)
	{
		this.mType = mType;
	}

	public int getCoin()
	{
		return mCoin;
	}

	public void setCoin(int mCoin)
	{
		this.mCoin = mCoin;
	}

	public ShakePrizeInfo getShakePrizeInfo()
	{
		return mShakePrizeInfo;
	}

	public void setShakePrizeInfo(ShakePrizeInfo mShakePrizeInfo)
	{
		this.mShakePrizeInfo = mShakePrizeInfo;
	}

	public int getmType()
	{
		return mType;
	}

	public void setmType(int mType)
	{
		this.mType = mType;
	}

	public int getmCoin()
	{
		return mCoin;
	}

	public void setmCoin(int mCoin)
	{
		this.mCoin = mCoin;
	}

	public ShakePrizeInfo getmShakePrizeInfo()
	{
		return mShakePrizeInfo;
	}

	public void setmShakePrizeInfo(ShakePrizeInfo mShakePrizeInfo)
	{
		this.mShakePrizeInfo = mShakePrizeInfo;
	}

	public String getMessage()
	{
		return mMessage;
	}

	public void setMessage(String mMessage)
	{
		this.mMessage = mMessage;
	}

	public String getDesc()
	{
		return desc;
	}

	public void setDesc(String desc)
	{
		this.desc = desc;
	}

	public String getImg()
	{
		return VersionPhotoUrlBuilder.createVersionImageUrl(img);
	}

	public void setImg(String img)
	{
		this.img = img;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
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
		return "ShakeResultInfo [mType=" + mType + ", mCoin=" + mCoin + ", mShakePrizeInfo=" + mShakePrizeInfo + ", mMessage=" + mMessage + ", desc=" + desc
				+ ", img=" + img + ", url=" + url + "]";
	}

}
