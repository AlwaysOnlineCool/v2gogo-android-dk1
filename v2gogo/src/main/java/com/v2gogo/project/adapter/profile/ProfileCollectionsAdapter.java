package com.v2gogo.project.adapter.profile;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.domain.profile.CollectionsInfo;
import com.v2gogo.project.main.image.GlideImageLoader;

/**
 * 我的收藏适配器
 * 
 * @author houjun
 */
public class ProfileCollectionsAdapter extends BaseAdapter
{

	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private List<CollectionsInfo> mCollectionsInfos;

	public ProfileCollectionsAdapter(Context context)
	{
		mContext = context;
		mLayoutInflater = LayoutInflater.from(mContext);
	}

	/**
	 * 刷新数据
	 */
	public void resetDatas(List<CollectionsInfo> collectionsInfos)
	{
		this.mCollectionsInfos = collectionsInfos;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount()
	{
		if (null == mCollectionsInfos)
		{
			return 0;
		}
		return mCollectionsInfos.size();
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
	public View getView(int position, View convertView, ViewGroup arg2)
	{
		ViewHolder viewHolder = null;
		if (convertView == null)
		{
			convertView = mLayoutInflater.inflate(R.layout.profile_collections_list_item, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		bindDatas(position, viewHolder);
		return convertView;
	}

	/**
	 * 绑定数据
	 */
	private void bindDatas(int position, ViewHolder viewHolder)
	{
		CollectionsInfo collectionsInfo = mCollectionsInfos.get(position);
		if (null != collectionsInfo)
		{
			viewHolder.mTitle.setText(collectionsInfo.getTitle());
			viewHolder.mDescription.setText(collectionsInfo.getDescription());
			viewHolder.mTime.setText(collectionsInfo.getCreateTimeString());
			GlideImageLoader.loadImageWithFixedSize(mContext, collectionsInfo.getThumbialUrl(), viewHolder.mImageView);
		}
	}

	private class ViewHolder
	{
		private TextView mTitle;
		private TextView mDescription;
		private TextView mTime;
		private ImageView mImageView;

		public ViewHolder(View rootView)
		{
			mTitle = (TextView) rootView.findViewById(R.id.profile_collections_liet_item_main_title);
			mTime = (TextView) rootView.findViewById(R.id.profile_collections_liet_item_publish_date);
			mDescription = (TextView) rootView.findViewById(R.id.profile_collections_liet_item_sub_title);
			mImageView = (ImageView) rootView.findViewById(R.id.profile_collections_liet_item_thumb);
		}
	}

}
