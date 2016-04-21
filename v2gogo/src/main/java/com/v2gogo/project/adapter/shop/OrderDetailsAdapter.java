package com.v2gogo.project.adapter.shop;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.domain.shop.OrderGoodsInfo;
import com.v2gogo.project.main.image.GlideImageLoader;

/**
 * @author houjun
 *         订单详情适配器
 */
public class OrderDetailsAdapter extends BaseAdapter
{

	private Context mContext;
	private LayoutInflater mLayoutInflater;

	private List<OrderGoodsInfo> mOrderGoodsInfos;

	public OrderDetailsAdapter(Context context)
	{
		mContext = context;
		mLayoutInflater = LayoutInflater.from(mContext);
	}

	public void resetDatas(List<OrderGoodsInfo> goodsInfos)
	{
		this.mOrderGoodsInfos = goodsInfos;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount()
	{
		if (mOrderGoodsInfos == null)
		{
			return 0;
		}
		return mOrderGoodsInfos.size();
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
			convertView = mLayoutInflater.inflate(R.layout.order_confirm_activity_list_item, null);
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
		OrderGoodsInfo orderGoodsInfo = mOrderGoodsInfos.get(position);
		if (orderGoodsInfo != null)
		{
			viewHolder.mGoodsIntro.setText(orderGoodsInfo.getDescriptions());
			viewHolder.mGoodsName.setText(orderGoodsInfo.getProductName());
			GlideImageLoader.loadImageWithFixedSize(mContext, orderGoodsInfo.getThumbialUrl(), viewHolder.mGoodsThumb);
			viewHolder.mGoodsNum.setText(orderGoodsInfo.getProductPrice() + "\nx" + orderGoodsInfo.getBuyNum());
		}
	}

	private class ViewHolder
	{
		private ImageView mGoodsThumb;
		private TextView mGoodsName;
		private TextView mGoodsIntro;
		private TextView mGoodsNum;

		public ViewHolder(View view)
		{
			mGoodsNum = (TextView) view.findViewById(R.id.order_confirm_goods_list_item_num);
			mGoodsName = (TextView) view.findViewById(R.id.order_confirm_goods_list_item_name);
			mGoodsIntro = (TextView) view.findViewById(R.id.order_confirm_goods_list_item_intro);
			mGoodsThumb = (ImageView) view.findViewById(R.id.order_confirm_goods_list_item_thumb);
		}
	}
}
