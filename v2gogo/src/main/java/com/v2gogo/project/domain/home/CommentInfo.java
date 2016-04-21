package com.v2gogo.project.domain.home;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.v2gogo.project.utils.common.DateUtil;
import com.v2gogo.project.utils.common.StringUtil;
import com.v2gogo.project.utils.qiniu.VersionPhotoUrlBuilder;

/**
 * 评论实体类
 * 
 * @author houjun
 */
public class CommentInfo implements Serializable
{

	private static final long serialVersionUID = 7879848076731729673L;

	public static final int COMMENT_ARTICE = 0;
	public static final int COMMENT_COMMEMT = 1;
	public static final int COMMENT_VEDIO = 2;

	public static final int SRC_ARTICE_TYPE = 0;
	public static final int SRC_VIDEO_TYPE = 2;

	@SerializedName("id")
	private String mId;

	@SerializedName("type")
	private int mType;

	@SerializedName("srcid")
	private String mSrcid;

	@SerializedName("userid")
	private String mUserid;

	@SerializedName("content")
	private String mContent;

	@SerializedName("fullname")
	private String mUsername;

	@SerializedName("userimg")
	private String mAvatar;

	@SerializedName("createtime")
	private long mCreatetime;

	@SerializedName("praise")
	private int mPraise;

	@SerializedName("coms")
	private int mComs;

	@SerializedName("srccont")
	private String mSrccont;

	@SerializedName("infoid")
	private String mInfoId;

	@SerializedName("praised")
	private boolean mPraised;

	private String mAvatarThumbialUrl;

	private List<CommentReplyInfo> mCommentReplyInfos;

	@SerializedName("imgs")
	private ArrayList<String> mImageUrl;

	private ArrayList<String> mThumbialUrls;
	private ArrayList<String> mRealImageUrls;

	private String mCommentTime;

	public String getSrccont()
	{
		return mSrccont;
	}

	public void setSrccont(String mSrccont)
	{
		this.mSrccont = mSrccont;
	}

	public String getId()
	{
		return mId;
	}

	public void setId(String mId)
	{
		this.mId = mId;
	}

	public int getType()
	{
		return mType;
	}

	public void setType(int mType)
	{
		this.mType = mType;
	}

	public String getSrcid()
	{
		return mSrcid;
	}

	public void setSrcid(String mSrcid)
	{
		this.mSrcid = mSrcid;
	}

	public String getUserid()
	{
		return mUserid;
	}

	public void setUserid(String mUserid)
	{
		this.mUserid = mUserid;
	}

	public String getContent()
	{
		return StringUtil.replaceBlank(mContent);
	}

	public void setContent(String mContent)
	{
		this.mContent = mContent;
	}

	public long getCreatetime()
	{
		return mCreatetime;
	}

	public void setCreatetime(long mCreatetime)
	{
		this.mCreatetime = mCreatetime;
	}

	public int getPraise()
	{
		return mPraise;
	}

	public void setPraise(int mPraise)
	{
		this.mPraise = mPraise;
	}

	public int getComs()
	{
		return mComs;
	}

	public void setComs(int mComs)
	{
		this.mComs = mComs;
	}

	public String getUsername()
	{
		return mUsername;
	}

	public void setUsername(String mUsername)
	{
		this.mUsername = mUsername;
	}

	public String getAvatar()
	{
		return VersionPhotoUrlBuilder.createVersionImageUrl(mAvatar);
	}

	public void setAvatar(String mAvatar)
	{
		this.mAvatar = mAvatar;
	}

	public List<CommentReplyInfo> getCommentReplyInfos()
	{
		return mCommentReplyInfos;
	}

	public void setCommentReplyInfos(List<CommentReplyInfo> mCommentReplyInfos)
	{
		this.mCommentReplyInfos = mCommentReplyInfos;
	}

	public String getInfoId()
	{
		return mInfoId;
	}

	public void setInfoId(String mInfoId)
	{
		this.mInfoId = mInfoId;
	}

	public ArrayList<String> getImageUrl()
	{
		return mImageUrl;
	}

	public void setImageUrl(ArrayList<String> mImageUrl)
	{
		this.mImageUrl = mImageUrl;
	}

	public ArrayList<String> getThumialUrls()
	{
		if (null == mThumbialUrls)
		{
			mThumbialUrls = new ArrayList<String>();
			if (mImageUrl != null)
			{
				for (String str : mImageUrl)
				{
					if (!TextUtils.isEmpty(str))
					{
						String temp = VersionPhotoUrlBuilder.createThumbialUrl(VersionPhotoUrlBuilder.createVersionImageUrl(str));
						mThumbialUrls.add(temp);
					}
				}
			}
		}
		return mThumbialUrls;
	}

	/**
	 * @return
	 */
	public ArrayList<String> getRealImageUrls()
	{
		if (null == mRealImageUrls)
		{
			mRealImageUrls = new ArrayList<String>();
			if (mImageUrl != null)
			{
				for (String str : mImageUrl)
				{
					if (!TextUtils.isEmpty(str))
					{
						String temp = VersionPhotoUrlBuilder.createNoFixedImage(VersionPhotoUrlBuilder.createVersionImageUrl(str));
						mRealImageUrls.add(temp);
					}
				}
			}
		}
		return mRealImageUrls;
	}

	/**
	 * 头像小图
	 * 
	 * @return
	 */
	public String getThumibalAvatar()
	{
		if (null == mAvatarThumbialUrl)
		{
			mAvatarThumbialUrl = VersionPhotoUrlBuilder.createThumbialUserAvatar(getAvatar());
		}
		return mAvatarThumbialUrl;
	}

	/**
	 * 解析盖楼内容
	 */
	public void parseCommentReplyData()
	{
		if (!TextUtils.isEmpty(mSrccont))
		{
			String[] strs = mSrccont.split(";");
			if (strs != null)
			{
				List<CommentReplyInfo> commentReplyInfos = new ArrayList<CommentReplyInfo>(strs.length);
				for (int i = 0, size = strs.length; i < size; i++)
				{
					String str = strs[i];
					String[] strings = str.split(":");
					if (strings != null)
					{
						CommentReplyInfo commentReplyInfo = new CommentReplyInfo();
						try
						{
							commentReplyInfo.setName(strings[0]);
						}
						catch (Exception e)
						{
							commentReplyInfo.setName("");
						}
						try
						{
							commentReplyInfo.setContent(strings[1]);
						}
						catch (Exception e)
						{
							commentReplyInfo.setContent("");
						}
						
						commentReplyInfos.add(commentReplyInfo);
					}
				}
				setCommentReplyInfos(commentReplyInfos);
			}
		}
	}

	public boolean isPraised()
	{
		return mPraised;
	}

	public void setPraised(boolean mPraised)
	{
		this.mPraised = mPraised;
	}

	public String getCommentTime()
	{
		if (null == mCommentTime)
		{
			mCommentTime = DateUtil.convertStringWithTimeStamp(mCreatetime);
		}
		return mCommentTime;
	}

	@Override
	public String toString()
	{
		return "CommentInfo [mId=" + mId + ", mType=" + mType + ", mSrcid=" + mSrcid + ", mUserid=" + mUserid + ", mContent=" + mContent + ", mUsername="
				+ mUsername + ", mAvatar=" + mAvatar + ", mCreatetime=" + mCreatetime + ", mPraise=" + mPraise + ", mComs=" + mComs + ", mSrccont=" + mSrccont
				+ ", mInfoId=" + mInfoId + ", mPraised=" + mPraised + ", mCommentReplyInfos=" + mCommentReplyInfos + ", mImageUrl=" + mImageUrl + "]";
	}

}
