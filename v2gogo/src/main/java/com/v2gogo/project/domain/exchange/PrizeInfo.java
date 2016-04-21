package com.v2gogo.project.domain.exchange;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;
import com.v2gogo.project.utils.common.DateUtil;
import com.v2gogo.project.utils.common.StringUtil;
import com.v2gogo.project.utils.qiniu.VersionPhotoUrlBuilder;

/**
 * 奖品实体类
 * 
 * @author houjun
 */
public class PrizeInfo implements Serializable
{

	private static final long serialVersionUID = -4044404405153776170L;

	public static final int SHAKE = 0;
	public static final int NORMAL = 1;

	public static final int RECEIVE_TYPE_ZHIJIE = 0;
	public static final int RECEIVE_TYPE_XIANCHANG = 1;
	public static final int RECEIVE_TYPE_POST = 2;

	private long restTime;
	private long createtime;
	private long endTime;
	private long beginConvert; // 倒计时

	private String createTimeString;

	@SerializedName("status")
	private int mStatus;

	@SerializedName("coin")
	private int mCoin;

	@SerializedName("supply")
	private int mSupply;

	@SerializedName("allsupply")
	private int mAllSupply;

	@SerializedName("type")
	private int mType;

	@SerializedName("url")
	private String mThumb;

	@SerializedName("title")
	private String mTitle;

	@SerializedName("srcid")
	private String mSrcId;

	@SerializedName("intro")
	private String mDescription;

	@SerializedName("id")
	private String mId;

	@SerializedName("serverphone")
	private String mServicePhone;

	@SerializedName("ispub")
	private int mIsPub;

	@SerializedName("prizepaperid")
	private String mPrizepaperid;

	@SerializedName("receivetype")
	private int receiveType;
	private int endFlag;

	private String replayCode;// 领取码

	private int isComment;// 0：未评价 1：已评价

	private int prizepaperType;// 0：个人 1：众筹

	private String gid;// 奖品分组ID

	public long getBeginConvert()
	{
		return beginConvert;
	}

	public void setBeginConvert(long beginConvert)
	{
		this.beginConvert = beginConvert;
	}

	public long getEndTime()
	{

		return endTime;
	}

	public String getEndTimeString()
	{
		String endTimeStr = "";
		if (endTime != 0)
		{
			endTimeStr = DateUtil.convertStringWithTimeStamp(endTime);
		}
		return endTimeStr;
	}

	public void setEndTime(long endTime)
	{
		this.endTime = endTime;
	}

	private String mThumbialPhotoImage;

	public int getCoin()
	{
		return mCoin;
	}

	public void setCoin(int mCoin)
	{
		this.mCoin = mCoin;
	}

	public int getSupply()
	{
		return mSupply;
	}

	public void setSupply(int mSupply)
	{
		this.mSupply = mSupply;
	}

	public int getAllSupply()
	{
		return mAllSupply;
	}

	public void setAllSupply(int mAllSupply)
	{
		this.mAllSupply = mAllSupply;
	}

	public int getType()
	{
		return mType;
	}

	public void setType(int mType)
	{
		this.mType = mType;
	}

	public String getImageUrl()
	{
		return VersionPhotoUrlBuilder.createVersionImageUrl(mThumb);
	}

	public void setThumb(String mThumb)
	{
		this.mThumb = mThumb;
	}

	public String getTitle()
	{
		return mTitle;
	}

	public void setTitle(String mTitle)
	{
		this.mTitle = mTitle;
	}

	public String getSrcId()
	{
		return mSrcId;
	}

	public void setSrcId(String mSrcId)
	{
		this.mSrcId = mSrcId;
	}

	public String getDescription()
	{
		return StringUtil.replaceBlank(mDescription);
	}

	public void setDescription(String mDescription)
	{
		this.mDescription = mDescription;
	}

	public String getId()
	{
		return mId;
	}

	public void setId(String mId)
	{
		this.mId = mId;
	}

	public String getServicePhone()
	{
		return mServicePhone;
	}

	public void setServicePhone(String mServicePhone)
	{
		this.mServicePhone = mServicePhone;
	}

	public int getIsPub()
	{
		return mIsPub;
	}

	public void setIsPub(int mIsPub)
	{
		this.mIsPub = mIsPub;
	}

	public int getStatus()
	{
		return mStatus;
	}

	public void setStatus(int mStatus)
	{
		this.mStatus = mStatus;
	}

	public String getPrizepaperid()
	{
		return mPrizepaperid;
	}

	public void setPrizepaperid(String mPrizepaperid)
	{
		this.mPrizepaperid = mPrizepaperid;
	}

	public int getReceiveType()
	{
		return receiveType;
	}

	public void setReceiveType(int receiveType)
	{
		this.receiveType = receiveType;
	}

	public long getCreatetime()
	{
		return createtime;
	}

	public void setCreatetime(long createtime)
	{
		this.createtime = createtime;
	}

	public String getCreateTimeString()
	{
		if (createtime != 0)
		{
			createTimeString = DateUtil.convertStringWithTimeStamp(createtime);
		}
		return createTimeString;
	}

	public int getEndFlag()
	{
		return endFlag;
	}

	public void setEndFlag(int endFlag)
	{
		this.endFlag = endFlag;
	}

	public long getRestTime()
	{
		return restTime;
	}

	public void setRestTime(long restTime)
	{
		this.restTime = restTime;
	}

	/**
	 * 得到图片的小图路径
	 * 
	 * @return
	 */
	public String getThumbibalPhotoImage()
	{
		if (null == mThumbialPhotoImage)
		{
			mThumbialPhotoImage = VersionPhotoUrlBuilder.createThumbialUrl(getImageUrl());
		}
		return mThumbialPhotoImage;
	}

	public String getReplayCode()
	{
		return replayCode;
	}

	public void setReplayCode(String replayCode)
	{
		this.replayCode = replayCode;
	}

	public int getIsComment()
	{
		return isComment;
	}

	public void setIsComment(int isComment)
	{
		this.isComment = isComment;
	}

	public int getPrizepaperType()
	{
		return prizepaperType;
	}

	public void setPrizepaperType(int prizepaperType)
	{
		this.prizepaperType = prizepaperType;
	}

	public String getGid()
	{
		return gid;
	}

	public void setGid(String gid)
	{
		this.gid = gid;
	}

	@Override
	public String toString()
	{
		return "PrizeInfo [mCoin=" + mCoin + ", mSupply=" + mSupply + ", mAllSupply=" + mAllSupply + ", mType=" + mType + ", mThumb=" + mThumb + ", mTitle="
				+ mTitle + ", mSrcId=" + mSrcId + ", mDescription=" + mDescription + ", mId=" + mId + ", mServicePhone=" + mServicePhone + ", mIsPub=" + mIsPub
				+ "]";
	}

}
