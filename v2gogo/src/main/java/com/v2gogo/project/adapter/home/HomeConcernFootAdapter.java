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
import com.v2gogo.project.domain.home.PopularizeInfo;
import com.v2gogo.project.domain.home.PopularizeItemInfo;
import com.v2gogo.project.main.image.GlideImageLoader;

/**
 * 首页底部百姓关注适配器
 * 
 * @author houjun
 */
public class HomeConcernFootAdapter extends BaseAdapter
{
	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private PopularizeInfo mPopularizeInfo;

	public HomeConcernFootAdapter(Context context)
	{
		mContext = context;
		mLayoutInflater = LayoutInflater.from(mContext);
	}

	/**
	 * 刷新数据
	 */
	public void resetDatas(PopularizeInfo popularizeInfo)
	{
		this.mPopularizeInfo = popularizeInfo;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount()
	{
		if (null == mPopularizeInfo || null == mPopularizeInfo.getInfos())
		{
			return 0;
		}
		return mPopularizeInfo.getInfos().size();
	}

	@Override
	public Object getItem(int position)
	{
		return mPopularizeInfo.getInfos().get(position);
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
		PopularizeItemInfo popularizeItemInfo = mPopularizeInfo.getInfos().get(position);
		if (null != popularizeItemInfo)
		{
			if (popularizeItemInfo.getVideo() == 1)
			{
				viewHolder.mIvVideo.setVisibility(View.VISIBLE);
			}
			else
			{
				viewHolder.mIvVideo.setVisibility(View.GONE);
			}
			GlideImageLoader.loadImageWithFixedSize(mContext, popularizeItemInfo.getThumbialUrl(), viewHolder.mArticleThumb);
			viewHolder.mArticleMainTitle.setText(popularizeItemInfo.getTittle());
			if (TextUtils.isEmpty(popularizeItemInfo.getTypelabel()))
			{
				viewHolder.mArticeType.setVisibility(View.GONE);
			}
			else
			{
				viewHolder.mArticeType.setVisibility(View.VISIBLE);
				viewHolder.mArticeType.setText(popularizeItemInfo.getTypelabel());
			}
			viewHolder.mArticleSubTitle.setText(popularizeItemInfo.getIntro());
			viewHolder.mTime.setText(popularizeItemInfo.getPublishedTimeString());
			viewHolder.mArticeBrower.setText("" + popularizeItemInfo.getBrowser());

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
