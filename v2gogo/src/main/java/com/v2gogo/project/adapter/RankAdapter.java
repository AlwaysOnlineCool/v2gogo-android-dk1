package com.v2gogo.project.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.domain.RankInfo;
import com.v2gogo.project.main.image.GlideImageLoader;

public class RankAdapter extends BaseAdapter
{

	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private List<RankInfo> mRankInfos;

	public RankAdapter(Context mContext)
	{
		super();
		this.mContext = mContext;
		mLayoutInflater = LayoutInflater.from(this.mContext);
	}

	/**
	 * 刷新数据
	 */
	public void resetDatas(List<RankInfo> rankInfos)
	{
		this.mRankInfos = rankInfos;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount()
	{
		if (null == mRankInfos)
		{
			return 0;
		}
		return mRankInfos.size();
	}

	@Override
	public Object getItem(int posiotion)
	{
		return null;
	}

	@Override
	public long getItemId(int posiotion)
	{
		return 0;
	}

	@Override
	public View getView(int posiotion, View convertView, ViewGroup arg2)
	{
		ViewHolder viewHolder = null;
		if (null == convertView || null == convertView.getTag())
		{
			convertView = mLayoutInflater.inflate(R.layout.rank_activity_list_item, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		bindDatas(posiotion, viewHolder);
		return convertView;
	}

	/**
	 * 绑定数据
	 */
	@SuppressWarnings("deprecation")
	private void bindDatas(int posiotion, final ViewHolder viewHolder)
	{
		final RankInfo rankInfo = mRankInfos.get(posiotion);
		if (null != rankInfo)
		{
			if (posiotion == 0 || posiotion == 1 || posiotion == 2)
			{
				if (posiotion == 0)
				{
					viewHolder.mRank.setBackgroundResource(R.drawable.jinbi_icons_no1);
				}
				else if (posiotion == 1)
				{
					viewHolder.mRank.setBackgroundResource(R.drawable.jinbi_icons_no2);
				}
				else
				{
					viewHolder.mRank.setBackgroundResource(R.drawable.jinbi_icons_no3);
				}
				viewHolder.mRank.setText("");
			}
			else
			{
				viewHolder.mRank.setText(posiotion + 1 + "");
				viewHolder.mRank.setBackgroundDrawable(null);
			}
			displayUserAvatar(viewHolder, rankInfo);
			viewHolder.mCoin.setText(rankInfo.getCoin() + "");
			viewHolder.mUsername.setText(rankInfo.getNinkname());
		}
	}

	/**
	 * 显示用户头像
	 * 
	 * @param viewHolder
	 * @param rankInfo
	 */
	private void displayUserAvatar(final ViewHolder viewHolder, final RankInfo rankInfo)
	{
		GlideImageLoader.loadAvatarImageWithFixedSize(mContext, rankInfo.getAvatar(), viewHolder.mAvatar);
	}

	private class ViewHolder
	{
		public TextView mRank;
		public TextView mUsername;
		public ImageView mAvatar;
		public TextView mCoin;

		public ViewHolder(View view)
		{
			mRank = (TextView) view.findViewById(R.id.rank_activity_list_item_rank);
			mUsername = (TextView) view.findViewById(R.id.rank_activity_list_item_username);
			mCoin = (TextView) view.findViewById(R.id.rank_activity_list_item_coin);
			mAvatar = (ImageView) view.findViewById(R.id.rank_activity_list_item_avatar);
		}
	}
}
