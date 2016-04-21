package com.v2gogo.project.adapter.home;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.domain.home.ArticeInfo;
import com.v2gogo.project.domain.home.ConcernInfo;
import com.v2gogo.project.main.image.GlideImageLoader;

/**
 * 百姓关注适配器
 * 
 * @author houjun
 */
public class HomeConcernAdapter extends BaseAdapter
{
	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private ConcernInfo mConcernInfo;

	public HomeConcernAdapter(Context context)
	{
		mContext = context;
		mLayoutInflater = LayoutInflater.from(mContext);
	}

	/**
	 * 刷新数据
	 */
	public void resetDatas(ConcernInfo concernInfo)
	{
		this.mConcernInfo = concernInfo;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount()
	{
		if (null == mConcernInfo || null == mConcernInfo.getArticeInfos())
		{
			return 0;
		}
		return mConcernInfo.getArticeInfos().size();
	}

	@Override
	public Object getItem(int position)
	{
		return mConcernInfo.getArticeInfos().get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup)
	{
		ViewHolder viewHolder = null;
		if (convertView == null)
		{
			convertView = mLayoutInflater.inflate(R.layout.home_concern_activity_list_item, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		bindData(position, viewHolder);
		return convertView;
	}

	/**
	 * 绑定数据
	 */
	private void bindData(int position, ViewHolder viewHolder)
	{
		ArticeInfo concernItemInfo = mConcernInfo.getArticeInfos().get(position);
		if (null != concernItemInfo)
		{
			if (concernItemInfo.getVideo() == 1)
			{
				viewHolder.mIvVideo.setVisibility(View.VISIBLE);
			}
			else
			{
				viewHolder.mIvVideo.setVisibility(View.GONE);
			}
			GlideImageLoader.loadImageWithFixedSize(mContext, concernItemInfo.getThumbialUrl(), viewHolder.mArticleThumb);
			viewHolder.mArticleMainTitle.setText(concernItemInfo.getTitle());
			if (TextUtils.isEmpty(concernItemInfo.getTypelabel()))
			{
				viewHolder.mArticeType.setVisibility(View.GONE);
			}
			else
			{
				viewHolder.mArticeType.setVisibility(View.VISIBLE);
				viewHolder.mArticeType.setText(concernItemInfo.getTypelabel());
			}
			viewHolder.mArticleSubTitle.setText(concernItemInfo.getDescription());
			viewHolder.mTime.setText(concernItemInfo.getPublishTimeString());
			viewHolder.mArticeBrower.setText("" + concernItemInfo.getBrower());
		}
	}

	private final class ViewHolder
	{
		public ImageView mArticleThumb;

		public TextView mArticleMainTitle;
		public TextView mArticleSubTitle;
		public TextView mArticeBrower;
		public TextView mArticeType;

		private ImageView mIvVideo;
		private TextView mTime;

		public ViewHolder(View view)
		{
			mArticleThumb = (ImageView) view.findViewById(R.id.activity_concern_article_thumb);
			mIvVideo = (ImageView) view.findViewById(R.id.activity_concern_article_video);
			mArticeType = (TextView) view.findViewById(R.id.activity_concern_article_type);
			mArticleSubTitle = (TextView) view.findViewById(R.id.activity_concern_article_sub_title);
			mArticleMainTitle = (TextView) view.findViewById(R.id.activity_concern_article_main_title);
			mArticeBrower = (TextView) view.findViewById(R.id.activity_concern_article_sub_brower);
			mArticeType = (TextView) view.findViewById(R.id.activity_concern_article_type);
			mTime = (TextView) view.findViewById(R.id.activity_concern_article_time);
		}
	}
}
