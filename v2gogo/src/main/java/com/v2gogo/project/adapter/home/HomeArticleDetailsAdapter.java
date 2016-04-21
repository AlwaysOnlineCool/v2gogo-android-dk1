package com.v2gogo.project.adapter.home;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.v2gogo.project.R;
import com.v2gogo.project.activity.PhotoPreviewActivity;
import com.v2gogo.project.domain.home.CommentInfo;
import com.v2gogo.project.domain.home.CommentReplyInfo;
import com.v2gogo.project.main.image.DisplayImageOptionsFactory;
import com.v2gogo.project.main.image.GlideImageLoader;
import com.v2gogo.project.utils.common.DensityUtil;
import com.v2gogo.project.utils.common.ScreenUtil;
import com.v2gogo.project.views.expandablelistview.PullExpandableListview;

public class HomeArticleDetailsAdapter extends BaseExpandableListAdapter
{

	private Context mContext;
	private IonMoreClickListener mIonMoreClickListener;
	private IonCommandClick mIonCommandClick;
	private LayoutInflater mLayoutInflater;

	private DisplayImageOptions mDisplayImageOptions;
	private List<CommentInfo> mGroupCommentInfos;
	private PullExpandableListview mPullExpandableListview;

	public HomeArticleDetailsAdapter(Context context, PullExpandableListview pullExpandableListview)
	{
		super();
		this.mContext = context;
		mLayoutInflater = LayoutInflater.from(mContext);
		mPullExpandableListview = pullExpandableListview;
		mDisplayImageOptions =  DisplayImageOptionsFactory.getDefaultDisplayImageOptions();
	}

	public void setOnMoreClickListener(IonMoreClickListener mIonMoreClickListener)
	{
		this.mIonMoreClickListener = mIonMoreClickListener;
	}

	public void setOnCommandClick(IonCommandClick mIonCommandClick)
	{
		this.mIonCommandClick = mIonCommandClick;
	}

	/**
	 * 返回显示的评论列表
	 */
	public List<CommentInfo> getCommentInfos()
	{
		return mGroupCommentInfos;
	}

	/**
	 * 刷新数据
	 */
	public void resetDatas(List<CommentInfo> commentInfos)
	{
		mGroupCommentInfos = commentInfos;
		this.notifyDataSetChanged();
		for (int i = 0; i < getGroupCount(); i++)
		{
			mPullExpandableListview.expandGroup(i);
		}
	}

	@Override
	public long getGroupId(int groupPosition)
	{
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition)
	{
		return childPosition;
	}

	@Override
	public int getGroupCount()
	{
		if (null == mGroupCommentInfos)
		{
			return 0;
		}
		return mGroupCommentInfos.size();
	}

	@Override
	public int getChildrenCount(int groupPosition)
	{
		if (null == mGroupCommentInfos)
		{
			return 0;
		}
		if (null == mGroupCommentInfos.get(groupPosition))
		{
			return 0;
		}
		if (null == mGroupCommentInfos.get(groupPosition).getCommentReplyInfos())
		{
			return 1;
		}
		return mGroupCommentInfos.get(groupPosition).getCommentReplyInfos().size() + 1;
	}

	@Override
	public Object getGroup(int groupPosition)
	{
		if (null == mGroupCommentInfos)
		{
			return null;
		}
		return mGroupCommentInfos.get(groupPosition);
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
	{
		GroupViewHolder groupViewHolder = null;
		if (null == convertView || null == convertView.getTag(R.layout.home_artice_topic_group_layout))
		{
			convertView = mLayoutInflater.inflate(R.layout.home_artice_topic_group_layout, null);
			groupViewHolder = new GroupViewHolder(convertView);
			convertView.setTag(R.layout.home_artice_topic_group_layout, groupViewHolder);
		}
		else
		{
			groupViewHolder = (GroupViewHolder) convertView.getTag(R.layout.home_artice_topic_group_layout);
		}
		bindCommentDatas(groupPosition, groupViewHolder);
		return convertView;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition)
	{
		return null;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
	{
		if (!isLastChild)
		{
			ChildViewHolder childViewHolder = null;
			if (null == convertView || null == convertView.getTag(R.layout.home_common_list_child_layout))
			{
				convertView = mLayoutInflater.inflate(R.layout.home_common_list_child_layout, null);
				childViewHolder = new ChildViewHolder(convertView);
				convertView.setTag(R.layout.home_common_list_child_layout, childViewHolder);
			}
			else
			{
				childViewHolder = (ChildViewHolder) convertView.getTag(R.layout.home_common_list_child_layout);
			}
			setChildDatas(groupPosition, childPosition, childViewHolder);
		}
		else
		{
			LastChildViewHolder lastChildViewHolder = null;
			if (null == convertView || null == convertView.getTag(R.layout.home_common_list_last_child_layout))
			{
				convertView = mLayoutInflater.inflate(R.layout.home_common_list_last_child_layout, null);
				lastChildViewHolder = new LastChildViewHolder(convertView);
				convertView.setTag(R.layout.home_common_list_last_child_layout, lastChildViewHolder);
			}
			else
			{
				lastChildViewHolder = (LastChildViewHolder) convertView.getTag(R.layout.home_common_list_last_child_layout);
			}
			displayLastChildDatas(groupPosition, lastChildViewHolder);
		}
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition)
	{
		return true;
	}

	public boolean hasStableIds()
	{
		return true;
	}

	/**
	 * 绑定评论数据
	 */
	private void bindCommentDatas(final int groupPosition, final GroupViewHolder groupViewHolder)
	{
		if (groupPosition == 0)
		{
			groupViewHolder.mMoreLayout.setVisibility(View.VISIBLE);
			groupViewHolder.mTextType.setText(R.string.home_v2gogo_interaction_comment_newest);
		}
		else
		{
			groupViewHolder.mTextType.setText("");
			groupViewHolder.mMoreLayout.setVisibility(View.GONE);
		}
		final CommentInfo commentInfo = mGroupCommentInfos.get(groupPosition);
		if (null != commentInfo)
		{
			groupViewHolder.mLikeNumber.setText(commentInfo.getPraise() + "");
			groupViewHolder.mUserNickname.setText(commentInfo.getUsername());
			displayCommentUserAvatar(groupViewHolder, commentInfo);
			groupViewHolder.mCreateTime.setText(commentInfo.getCommentTime());
			displayCommandBtn(groupViewHolder, commentInfo);
		}
		groupViewHolder.mTextMore.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if (null != mIonMoreClickListener)
				{
					mIonMoreClickListener.onMoreClick(groupPosition);
				}
			}
		});
	}

	private void displayCommandBtn(final GroupViewHolder groupViewHolder, final CommentInfo commentInfo)
	{
		if (commentInfo.isPraised())
		{
			groupViewHolder.mLikeNumber.setTextColor(0xFFF96700);
			groupViewHolder.mBtnPraised.setBackgroundResource(R.drawable.commentary_btn_dianzan_selected);
		}
		else
		{
			groupViewHolder.mLikeNumber.setTextColor(0xff696969);
			groupViewHolder.mBtnPraised.setBackgroundResource(R.drawable.commentary_btn_dianzan);
		}
		groupViewHolder.mRelativeLayout.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if (null != mIonCommandClick)
				{
					mIonCommandClick.onCommandClick(commentInfo);
				}
			}
		});
	}

	private void displayCommentUserAvatar(final GroupViewHolder groupViewHolder, final CommentInfo commentInfo)
	{
		GlideImageLoader.loadAvatarImageWithFixedSize(mContext, commentInfo.getThumibalAvatar(), groupViewHolder.mUserAvatar);
	}

	/**
	 * 显示最后一个child视图的数据
	 */
	private void displayLastChildDatas(int groupPosition, LastChildViewHolder lastChildViewHolder)
	{
		final CommentInfo commentInfo = mGroupCommentInfos.get(groupPosition);
		if (null != commentInfo)
		{
			for (int i = 0; i < 4; i++)
			{
				lastChildViewHolder.mThumbs[i].setVisibility(View.GONE);
				lastChildViewHolder.mThumbs[i].setImageBitmap(null);
			}
			if (null == commentInfo.getThumialUrls() || 0 == commentInfo.getThumialUrls().size())
			{
				lastChildViewHolder.mImageLayout.setVisibility(View.GONE);
			}
			else
			{
				lastChildViewHolder.mImageLayout.setVisibility(View.VISIBLE);
				for (int i = 0, size = commentInfo.getThumialUrls().size(); i < size; i++)
				{
					final int index = i % 4;
					lastChildViewHolder.mThumbs[index].setVisibility(View.VISIBLE);
					lastChildViewHolder.mThumbs[index].setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View view)
						{
							Intent intent = new Intent(mContext, PhotoPreviewActivity.class);
							intent.putExtra(PhotoPreviewActivity.INDEX, index);
							intent.putStringArrayListExtra(PhotoPreviewActivity.PATHS, commentInfo.getRealImageUrls());
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							mContext.startActivity(intent);
						}
					});
					ImageLoader.getInstance().displayImage(commentInfo.getThumialUrls().get(index), lastChildViewHolder.mThumbs[index], mDisplayImageOptions);
				}
			}
			if (TextUtils.isEmpty(commentInfo.getContent()))
			{
				lastChildViewHolder.mTextContent.setVisibility(View.GONE);
			}
			else
			{
				lastChildViewHolder.mTextContent.setVisibility(View.VISIBLE);
				lastChildViewHolder.mTextContent.setText(commentInfo.getContent());
			}
		}
	}

	/**
	 * 显示子视图数据（不是最后一个）
	 */
	private void setChildDatas(int groupPosition, int childPosition, ChildViewHolder childViewHolder)
	{
		CommentInfo commentInfo = mGroupCommentInfos.get(groupPosition);
		if (null != commentInfo)
		{
			List<CommentReplyInfo> commentReplyInfos = commentInfo.getCommentReplyInfos();
			if (null != commentReplyInfos)
			{
				CommentReplyInfo commentReplyInfo = commentReplyInfos.get(childPosition);
				if (null != commentReplyInfo)
				{
					childViewHolder.mChildNinkname.setText(commentReplyInfo.getName());
					childViewHolder.mChildContent.setText(commentReplyInfo.getContent());
					childViewHolder.mChildFlower.setText("" + (childPosition + 1));
				}
			}
		}
	}

	/**
	 * group视图缓存
	 * 
	 * @author houjun
	 */
	private class GroupViewHolder
	{
		public RelativeLayout mRelativeLayout;
		public RelativeLayout mMoreLayout;
		public ImageView mUserAvatar;
		public TextView mUserNickname;
		public TextView mCreateTime;
		private TextView mLikeNumber;
		private TextView mTextType;
		private TextView mTextMore;
		private Button mBtnPraised;

		public GroupViewHolder(View rootView)
		{
			mTextMore = (TextView) rootView.findViewById(R.id.home_artice_topic_group_more);

			mMoreLayout = (RelativeLayout) rootView.findViewById(R.id.artice_deatils_more_layout);
			mTextType = (TextView) rootView.findViewById(R.id.home_artice_topic_group_type);
			mBtnPraised = (Button) rootView.findViewById(R.id.home_artice_topic_group_is_praised);
			mCreateTime = (TextView) rootView.findViewById(R.id.home_artice_topic_group_create_date);
			mLikeNumber = (TextView) rootView.findViewById(R.id.home_artice_topic_group_support_num);
			mUserAvatar = (ImageView) rootView.findViewById(R.id.home_artice_topic_group_user_avatar);
			mUserNickname = (TextView) rootView.findViewById(R.id.home_artice_topic_group_user_nickname);
			mRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.home_artice_topic_group_command_layout);
		}
	}

	/**
	 * child视图缓存
	 * 
	 * @author houjun
	 */
	private class ChildViewHolder
	{
		public TextView mChildFlower;
		public TextView mChildNinkname;
		public TextView mChildContent;

		public ChildViewHolder(View view)
		{
			mChildFlower = (TextView) view.findViewById(R.id.home_comment_list_chind_flower);
			mChildContent = (TextView) view.findViewById(R.id.home_comment_list_chind_comment_content);
			mChildNinkname = (TextView) view.findViewById(R.id.home_comment_list_chind_user_nickname);
		}
	}

	/**
	 * 最后一个child视图缓存
	 * 
	 * @author houjun
	 */
	private class LastChildViewHolder
	{
		public ImageView[] mThumbs;
		public TextView mTextContent;
		public LinearLayout mImageLayout;

		public LastChildViewHolder(View view)
		{
			mThumbs = new ImageView[4];
			mTextContent = (TextView) view.findViewById(R.id.home_comment_list_last_chind_comment);
			mThumbs[0] = (ImageView) view.findViewById(R.id.home_comment_list_last_child_image_one);
			mThumbs[1] = (ImageView) view.findViewById(R.id.home_comment_list_last_child_image_two);
			mThumbs[2] = (ImageView) view.findViewById(R.id.home_comment_list_last_child_image_three);
			mThumbs[3] = (ImageView) view.findViewById(R.id.home_comment_list_last_child_image_four);
			mImageLayout = (LinearLayout) view.findViewById(R.id.home_comment_list_last_child_image_layout);
			float contentWidth = ScreenUtil.getScreenWidth(mContext) - DensityUtil.dp2px(mContext, 62.25f);
			float itemWidth = (contentWidth - 3 * 7) / 4f;
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) itemWidth, (int) itemWidth);
			layoutParams.leftMargin = 0;
			mThumbs[0].setLayoutParams(layoutParams);
			layoutParams.leftMargin = 7;
			mThumbs[1].setLayoutParams(layoutParams);
			mThumbs[2].setLayoutParams(layoutParams);
			mThumbs[3].setLayoutParams(layoutParams);
		}
	}

	/**
	 * 更多点击
	 * 
	 * @author houjun
	 */
	public interface IonMoreClickListener
	{
		public void onMoreClick(int position);
	}

	/**
	 * 点赞数据回调
	 * 
	 * @author houjun
	 */
	public interface IonCommandClick
	{
		public void onCommandClick(CommentInfo commentInfo);
	}

}
