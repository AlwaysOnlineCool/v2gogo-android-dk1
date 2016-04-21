package com.v2gogo.project.adapter.home;

import java.util.List;

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
import com.v2gogo.project.main.image.GlideImageLoader;

/**
 * 微兔gogo适配器
 * 
 * @author houjun
 */
public class HomeV2gogoAdapter extends BaseAdapter
{

	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private List<ArticeInfo> mArticeInfos;

	public HomeV2gogoAdapter(Context context)
	{
		mContext = context;
		mLayoutInflater = LayoutInflater.from(mContext);
	}

	/**
	 * 刷新数据
	 * 
	 * @param articeInfos
	 */
	public void resetDatas(List<ArticeInfo> articeInfos)
	{
		mArticeInfos = articeInfos;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount()
	{
		if (null == mArticeInfos || mArticeInfos.size() == 0)
		{
			return 0;
		}
		return mArticeInfos.size();
	}

	@Override
	public Object getItem(int position)
	{
		return null;
	}

	@Override
	public long getItemId(int position)
	{
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup)
	{
		ViewHolder viewHolder = null;
		if (convertView == null)
		{
			convertView = mLayoutInflater.inflate(R.layout.home_v2gogo_activity_list_item, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		ArticeInfo articeInfo = mArticeInfos.get(position);
		if (null != articeInfo)
		{
			GlideImageLoader.loadImageWithFixedSize(mContext, articeInfo.getThumbialUrl(), viewHolder.mArticleThumb);
			if (articeInfo.getVideo() == 0)
			{
				viewHolder.mIvVideo.setVisibility(View.GONE);
			}
			else
			{
				viewHolder.mIvVideo.setVisibility(View.VISIBLE);
			}
			viewHolder.mArticleMainTitle.setText(articeInfo.getTitle());
			viewHolder.mArticleSubTitle.setText(articeInfo.getDescription());
			viewHolder.mArticleBrowerNum.setText("" + articeInfo.getBrower());
			if (TextUtils.isEmpty(articeInfo.getTypelabel()))
			{
				viewHolder.mArticeType.setVisibility(View.GONE);
			}
			else
			{
				viewHolder.mArticeType.setVisibility(View.VISIBLE);
				viewHolder.mArticeType.setText(articeInfo.getTypelabel());
			}
			viewHolder.mArticlePublishDate.setText(articeInfo.getPublishTimeString());
		}
		return convertView;
	}

	private final class ViewHolder
	{
		public ImageView mArticleThumb;
		private ImageView mIvVideo;

		public TextView mArticeType;
		public TextView mArticleMainTitle;
		public TextView mArticleSubTitle;
		public TextView mArticlePublishDate;
		public TextView mArticleBrowerNum;

		public ViewHolder(View view)
		{
			mArticleThumb = (ImageView) view.findViewById(R.id.activity_v2gogo_article_thumb);
			mIvVideo = (ImageView) view.findViewById(R.id.activity_v2gogo_article_video);

			mArticeType = (TextView) view.findViewById(R.id.activity_v2gogo_article_type);
			mArticleSubTitle = (TextView) view.findViewById(R.id.activity_v2gogo_article_sub_title);
			mArticleMainTitle = (TextView) view.findViewById(R.id.activity_v2gogo_article_main_title);
			mArticlePublishDate = (TextView) view.findViewById(R.id.activity_v2gogo_article_publish_date);
			mArticleBrowerNum = (TextView) view.findViewById(R.id.activity_v2gogo_article_brower);
		}
	}
}
