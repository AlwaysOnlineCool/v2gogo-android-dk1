package com.v2gogo.project.adapter.shop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.domain.shop.GoodsInfo;
import com.v2gogo.project.main.image.GlideImageLoader;

/**
 * 订单确认适配器
 * 
 * @author houjun
 */
public class OrderCommitAdaptar extends BaseAdapter
{
	private Context mContext;
	private LayoutInflater mLayoutInflater;

	private GoodsInfo mGoodsInfo;
	private int mNumber;

	public OrderCommitAdaptar(Context context, int num, GoodsInfo goodsInfos)
	{
		mNumber = num;
		mContext = context;
		mGoodsInfo = goodsInfos;
		mLayoutInflater = LayoutInflater.from(mContext);
	}

	public void setmNumber(int mNumber)
	{
		this.mNumber = mNumber;
	}

	@Override
	public int getCount()
	{
		if (mGoodsInfo == null)
		{
			return 0;
		}
		return 1;
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
		bindDatas(viewHolder);
		return convertView;
	}

	/**
	 * 绑定数据
	 * 
	 * @param viewHolder
	 */
	private void bindDatas(ViewHolder viewHolder)
	{
		if (null != mGoodsInfo)
		{
			GlideImageLoader.loadImageWithFixedSize(mContext, mGoodsInfo.getSrcSmImg(), viewHolder.mGoodsThumb);
			viewHolder.mGoodsName.setText(mGoodsInfo.getProductName());
			viewHolder.mGoodsNum.setText(mGoodsInfo.getV2gogoPrice() + "\nx" + mNumber);
			viewHolder.mGoodsIntro.setText(mGoodsInfo.getDescription());
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
