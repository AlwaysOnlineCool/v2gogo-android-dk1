package com.v2gogo.project.domain.home;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.v2gogo.project.utils.qiniu.VersionPhotoUrlBuilder;

public class InteractionInfo implements Serializable
{

	private static final long serialVersionUID = 2972362725352238608L;

	public static final int STATUS_END = 3;// 互动结束
	public static final int STATUS_PREPARE = 1;// 互动未开始
	public static final int STATUS_PROCESS = 2;// 互动中

	public static final int COMMENT_UPLOAD_PHOTO = 1;// 互动可以上传图片
	public static final int COMMENT_NO_UPLOAD_PHOTO = 0;// 互动不可以上传图片

	@SerializedName("id")
	private String mId;

	@SerializedName("content")
	private String mDescription;

	@SerializedName("options")
	private List<VoteOptionInfo> mVoteOptionInfos;

	@SerializedName("votecoin")
	private int mVoteCoin;

	@SerializedName("status")
	private int mStatus;

	@SerializedName("iscom")
	private int mIsComm;

	@SerializedName("notice")
	private String mNotice;

	@SerializedName("conclusion")
	private String mConclusion;

	@SerializedName("title")
	private String mTitle;

	@SerializedName("href")
	private String mHref;

	@SerializedName("img")
	private String mImage;

	@SerializedName("intro")
	private String mIntro;

	@SerializedName("isPraise")
	private boolean isYetVoted;

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
		return mDescription;
	}

	public void setDescription(String mDescription)
	{
		this.mDescription = mDescription;
	}

	public List<VoteOptionInfo> getmoteOptionInfos()
	{
		return mVoteOptionInfos;
	}

	public void setVoteOptionInfos(List<VoteOptionInfo> mVoteOptionInfos)
	{
		this.mVoteOptionInfos = mVoteOptionInfos;
	}

	public int getVoteCoin()
	{
		return mVoteCoin;
	}

	public void setVoteCoin(int mVoteCoin)
	{
		this.mVoteCoin = mVoteCoin;
	}

	public int getStatus()
	{
		return mStatus;
	}

	public void setStatus(int mStatus)
	{
		this.mStatus = mStatus;
	}

	public int isComm()
	{
		return mIsComm;
	}

	public void setIsComm(int mIsComm)
	{
		this.mIsComm = mIsComm;
	}

	public String getNotice()
	{
		return mNotice;
	}

	public void setNotice(String mNotice)
	{
		this.mNotice = mNotice;
	}

	public String getConclusion()
	{
		return mConclusion;
	}

	public void setConclusion(String mConclusion)
	{
		this.mConclusion = mConclusion;
	}

	public String getTitle()
	{
		return mTitle;
	}

	public void setTitle(String mTitle)
	{
		this.mTitle = mTitle;
	}

	public String getHref()
	{
		return mHref;
	}

	public void setHref(String mHref)
	{
		this.mHref = mHref;
	}

	public String getImage()
	{
		return VersionPhotoUrlBuilder.createVersionImageUrl(mImage);
	}

	public void setImage(String mImage)
	{
		this.mImage = mImage;
	}

	public String getIntro()
	{
		return mIntro;
	}

	public void setIntro(String mIntro)
	{
		this.mIntro = mIntro;
	}

	public boolean isYetVoted()
	{
		return isYetVoted;
	}

	public void setYetVoted(boolean isYetVoted)
	{
		this.isYetVoted = isYetVoted;
	}

	@Override
	public String toString()
	{
		return "InteractionInfo [mId=" + mId + ", mDescription=" + mDescription + ", mVoteOptionInfos=" + mVoteOptionInfos + ", mVoteCoin=" + mVoteCoin
				+ ", mStatus=" + mStatus + ", mIsComm=" + mIsComm + ", mNotice=" + mNotice + ", mConclusion=" + mConclusion + ", mTitle=" + mTitle + ", mHref="
				+ mHref + ", mImage=" + mImage + ", mIntro=" + mIntro + ", isYetVoted=" + isYetVoted + "]";
	}

}
