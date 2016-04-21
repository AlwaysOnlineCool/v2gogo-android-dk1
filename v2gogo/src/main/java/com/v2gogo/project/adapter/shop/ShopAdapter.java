package com.v2gogo.project.adapter.shop;

import java.util.List;

import android.content.Context;
import android.graphics.Paint;
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
 * 每日团购适配器
 * 
 * @author houjun
 */
public class ShopAdapter extends BaseAdapter
{

	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private List<GoodsInfo> mGoodsInfos;
	private String mYetSelltip;

	public ShopAdapter(Context context)
	{
		mContext = context;
		mLayoutInflater = LayoutInflater.from(mContext);
		mYetSelltip = mContext.getString(R.string.goods_details_sell_tip);
	}

	public void resetDatas(List<GoodsInfo> mGoodsInfos)
	{
		this.mGoodsInfos = mGoodsInfos;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount()
	{
		if (null == mGoodsInfos)
		{
			return 0;
		}
		return mGoodsInfos.size();
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
		if (null == convertView || null == convertView.getTag())
		{
			convertView = mLayoutInflater.inflate(R.layout.shop_activity_list_item, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		GoodsInfo goodsInfo = mGoodsInfos.get(position);
		if (null != goodsInfo)
		{
			bindDatas(mContext, goodsInfo, viewHolder);
		}
		return convertView;
	}

	private void bindDatas(Context context, GoodsInfo goodsInfo, ViewHolder viewHolder)
	{
		GlideImageLoader.loadImageWithFixedSize(context, goodsInfo.getSrcSmImg(), viewHolder.mIvGoodThumb);
		viewHolder.mTvGoodsName.setText(goodsInfo.getProductName());
		viewHolder.mTvGoodsDescription.setText(goodsInfo.getDescription());
		viewHolder.mTvMarketPrice.setText("￥" + goodsInfo.getOriginalPrice());
		viewHolder.mTvGogoPrice.setText("￥" + goodsInfo.getV2gogoPrice());
		viewHolder.mTvYetSell.setText(String.format(mYetSelltip, goodsInfo.getSalesVolume()));
		if (goodsInfo.getStock() <= 0)
		{
			viewHolder.mIvYetCompleted.setVisibility(View.VISIBLE);
		}
		else
		{
			viewHolder.mIvYetCompleted.setVisibility(View.GONE);
		}
	}

	public final class ViewHolder
	{
		public ImageView mIvGoodThumb;
		public TextView mIvYetCompleted;
		public TextView mTvGoodsName;
		public TextView mTvGoodsDescription;
		private TextView mTvMarketPrice;
		private TextView mTvGogoPrice;
		private TextView mTvYetSell;

		public ViewHolder(View view)
		{
			mTvMarketPrice = (TextView) view.findViewById(R.id.home_every_day_goods_marketprice);
			mTvGoodsName = (TextView) view.findViewById(R.id.home_every_day_shop_goods_name);
			mIvGoodThumb = (ImageView) view.findViewById(R.id.home_every_day_shop_goods_thumb);
			mTvGogoPrice = (TextView) view.findViewById(R.id.home_every_day_goods_gogo_price);
			mTvYetSell = (TextView) view.findViewById(R.id.home_every_day_goods_yet_sell);
			mTvGoodsDescription = (TextView) view.findViewById(R.id.home_every_day_shop_goods_description);
			mIvYetCompleted = (TextView) view.findViewById(R.id.home_every_day_shop_goods_yet_completed);
			mTvMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
		}
	}
}
