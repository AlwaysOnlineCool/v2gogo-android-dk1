package com.v2gogo.project.adapter.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.v2gogo.project.R;
import com.v2gogo.project.domain.home.HomeInfo;
import com.v2gogo.project.domain.home.PopularizeInfo;
import com.v2gogo.project.views.logic.PopularizeLayout;

public class HomeAdapter extends BaseAdapter
{

	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private HomeInfo mHomeInfo;

	public HomeAdapter(Context context)
	{
		mContext = context;
		mLayoutInflater = LayoutInflater.from(mContext);
	}

	/**
	 * 刷新数据
	 */
	public void reSetDatas(HomeInfo homeInfo)
	{
		mHomeInfo = homeInfo;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount()
	{
		if (null == mHomeInfo || null == mHomeInfo.getPopularizeInfos())
		{
			return 0;
		}
		return mHomeInfo.getPopularizeInfos().size();
	}

	@Override
	public Object getItem(int position)
	{
		return mHomeInfo.getPopularizeInfos().get(position);
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
			convertView = mLayoutInflater.inflate(R.layout.home_activity_list_item, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		PopularizeInfo popularizeInfo = mHomeInfo.getPopularizeInfos().get(position);
		if (null != popularizeInfo)
		{
			viewHolder.mPopularizeLayout.setPopularizeData(mContext, popularizeInfo);
		}
		return convertView;
	}

	private final class ViewHolder
	{
		public PopularizeLayout mPopularizeLayout;

		public ViewHolder(View view)
		{
			mPopularizeLayout = (PopularizeLayout) view.findViewById(R.id.fragment_home_list_item_popularize);
		}
	}
}
