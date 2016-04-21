package com.v2gogo.project.adapter.shop;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.v2gogo.project.R;
import com.v2gogo.project.domain.shop.GoodsInfo;
import com.v2gogo.project.views.logic.ShopGridViewItemLayout;

public class ShopGridAdapter extends BaseAdapter
{

	private Context mContext;
	private String mYetSelltip;
	private LayoutInflater mLayoutInflater;
	private List<GoodsInfo> mGoodsInfos;

	public ShopGridAdapter(Context context)
	{
		mContext = context;
		mLayoutInflater = LayoutInflater.from(mContext);
		mGoodsInfos = new ArrayList<GoodsInfo>();
		mYetSelltip = mContext.getString(R.string.goods_details_sell_tip);
	}

	@Override
	public int getCount()
	{
		if (null == mGoodsInfos)
		{
			return 0;
		}
		return mGoodsInfos.size() % 2 == 0 ? mGoodsInfos.size() / 2 : mGoodsInfos.size() / 2 + 1;
	}

	public void resetDatas(List<GoodsInfo> goodsInfos)
	{
		if (null != goodsInfos)
		{
			mGoodsInfos.clear();
			mGoodsInfos.addAll(goodsInfos);
		}
		this.notifyDataSetChanged();
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
			convertView = mLayoutInflater.inflate(R.layout.shop_grid_list_item, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final int index = position * 2;
		List<GoodsInfo> goodsInfos = null;
		if(position != getCount()-1)
		{
			goodsInfos = mGoodsInfos.subList(index, index+2);
		}
		else
		{
			goodsInfos =  mGoodsInfos.subList(index, mGoodsInfos.size());
		}
		viewHolder.mGridViewItemLayout.setShopDatas(goodsInfos, mYetSelltip);
		return convertView;
	}

	private class ViewHolder
	{
		private ShopGridViewItemLayout mGridViewItemLayout;

		public ViewHolder(View view)
		{
			mGridViewItemLayout = (ShopGridViewItemLayout) view.findViewById(R.id.shop_grid_list_item_container);
		}
	}

}
