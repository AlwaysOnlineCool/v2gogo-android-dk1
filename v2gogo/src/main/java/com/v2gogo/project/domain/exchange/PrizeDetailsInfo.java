package com.v2gogo.project.domain.exchange;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;
import com.v2gogo.project.utils.common.StringUtil;
import com.v2gogo.project.utils.qiniu.VersionPhotoUrlBuilder;

/**
 * 奖品详情实体类
 * 
 * @author houjun
 */
public class PrizeDetailsInfo implements Serializable
{

	private static final long serialVersionUID = -4542928168155262457L;

	public static final int TYPE_SHAKE = 0;
	public static final int TYPE_PRIZE = 1;

	@SerializedName("name")
	private String mName;

	@SerializedName("id")
	private String mId;

	@SerializedName("intro")
	private String mDescription;

	@SerializedName("sponsor")
	private String mSponsor;

	@SerializedName("sellphone")
	private String mSellerPhone;

	private String mServivePhone;

	@SerializedName("coin")
	private int mNeedCoin;

	@SerializedName("url")
	private String mUrl;

	@SerializedName("ispub")
	private int isPub;

	@SerializedName("supply")
	private int mSupply;

	@SerializedName("used")
	private int mUsed;

	private String mRealImagePhoto;

	private String introLob;

	private long endTime;
	
	private long restTime;//判断是否有倒计时

	

	public long getRestTime()
	{
		return restTime;
	}

	public void setRestTime(long restTime)
	{
		this.restTime = restTime;
	}

	public long getEndTime()
	{
		return endTime;
	}

	public void setEndTime(long endTime)
	{
		this.endTime = endTime;
	}

	public String getName()
	{
		return mName;
	}

	public void setName(String mName)
	{
		this.mName = mName;
	}

	public String getId()
	{
		return mId;
	}

	public void setId(String mId)
	{
		this.mId = mId;
	}

	public String getDescription()
	{
		return StringUtil.replaceBlank(mDescription);
	}

	public void setDescription(String mDescription)
	{
		this.mDescription = mDescription;
	}

	public String getSponsor()
	{
		return mSponsor;
	}

	public void setSponsor(String mSponsor)
	{
		this.mSponsor = mSponsor;
	}

	public String getSellerPhone()
	{
		return mSellerPhone;
	}

	public void setSellerPhone(String mSellerPhone)
	{
		this.mSellerPhone = mSellerPhone;
	}

	public String getServivePhone()
	{
		return mServivePhone;
	}

	public void setServivePhone(String mServivePhone)
	{
		this.mServivePhone = mServivePhone;
	}

	public int getNeedCoin()
	{
		return mNeedCoin;
	}

	public void setNeedCoin(int mNeedCoin)
	{
		this.mNeedCoin = mNeedCoin;
	}

	public String getUrl()
	{
		return VersionPhotoUrlBuilder.createVersionImageUrl(mUrl);
	}

	public void setUrl(String mUrl)
	{
		this.mUrl = mUrl;
	}

	public int IsPub()
	{
		return isPub;
	}

	public void setIsPub(int isPub)
	{
		this.isPub = isPub;
	}

	public int getSupply()
	{
		return mSupply;
	}

	public void setSupply(int mSupply)
	{
		this.mSupply = mSupply;
	}

	public int getUsed()
	{
		return mUsed;
	}

	public void setUsed(int mUsed)
	{
		this.mUsed = mUsed;
	}

	/**
	 * 得到大图路径
	 * 
	 * @return
	 */
	public String getRealPhotoImage()
	{
		if (null == mRealImagePhoto)
		{
			mRealImagePhoto = VersionPhotoUrlBuilder.createNoFixedImage(getUrl());
		}
		return mRealImagePhoto;
	}

	public String getIntroLob()
	{
		return introLob;
	}

	public void setIntroLob(String introLob)
	{
		this.introLob = introLob;
	}

	@Override
	public String toString()
	{
		return "PrizeDetailsInfo [mName=" + mName + ", mId=" + mId + ", mDescription=" + mDescription + ", mSponsor=" + mSponsor + ", mSellerPhone="
				+ mSellerPhone + ", mServivePhone=" + mServivePhone + ", mNeedCoin=" + mNeedCoin + ", mUrl=" + mUrl + ", isPub=" + isPub + ", mSupply="
				+ mSupply + ", mUsed=" + mUsed + "]";
	}

}
