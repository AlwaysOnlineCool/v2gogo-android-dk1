package com.v2gogo.project.views.logic;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.shop.GroupGoodsDetailsActivity;
import com.v2gogo.project.domain.shop.GoodsInfo;
import com.v2gogo.project.main.image.GlideImageLoader;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.utils.common.DensityUtil;
import com.v2gogo.project.utils.common.ScreenUtil;

public class ShopGridViewItemLayout extends LinearLayout
{

	private LinearLayout[] mLinearLayouts;
	private ImageView[] mImageViews;
	private TextView[] mGoodsPrices;
	private TextView[] mGoodsNames;
	private TextView[] mGoodsSell;

	private TextView[] mGoodsYetCompleteds;

	public ShopGridViewItemLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initView(context);
	}

	public ShopGridViewItemLayout(Context context)
	{
		super(context);
		initView(context);
	}

	/**
	 * 初始化视图
	 */
	private void initView(Context context)
	{
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View view = layoutInflater.inflate(R.layout.shop_gridview_item_layout, null);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		view.setLayoutParams(layoutParams);

		mGoodsSell = new TextView[2];
		mGoodsNames = new TextView[2];
		mImageViews = new ImageView[2];
		mGoodsPrices = new TextView[2];
		mLinearLayouts = new LinearLayout[2];
		mGoodsYetCompleteds = new TextView[2];
		mLinearLayouts[0] = (LinearLayout) view.findViewById(R.id.shop_gridview_item_goods_layout_one);
		mLinearLayouts[1] = (LinearLayout) view.findViewById(R.id.shop_gridview_item_goods_layout_two);

		mGoodsNames[0] = (TextView) view.findViewById(R.id.shop_gridview_item_goods_name_one);
		mGoodsNames[1] = (TextView) view.findViewById(R.id.shop_gridview_item_goods_name_two);
		mGoodsSell[0] = (TextView) view.findViewById(R.id.shop_gridview_item_goods_sell_one);
		mGoodsSell[1] = (TextView) view.findViewById(R.id.shop_gridview_item_goods_sell_two);
		mImageViews[0] = (ImageView) view.findViewById(R.id.shop_gridview_item_goods_thumb_one);
		mImageViews[1] = (ImageView) view.findViewById(R.id.shop_gridview_item_goods_thumb_two);
		mGoodsPrices[0] = (TextView) view.findViewById(R.id.shop_gridview_item_goods_price_one);
		mGoodsPrices[1] = (TextView) view.findViewById(R.id.shop_gridview_item_goods_price_two);

		mGoodsYetCompleteds[0] = (TextView) view.findViewById(R.id.shop_gridview_item_goods_yet_completed_one);
		mGoodsYetCompleteds[1] = (TextView) view.findViewById(R.id.shop_gridview_item_goods_yet_completed_two);

		int width = (int) ((ScreenUtil.getScreenWidth(context.getApplicationContext()) - DensityUtil.dp2px(context.getApplicationContext(), 3 * 6)) / 2f);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, width);

		mImageViews[0].setLayoutParams(params);
		mImageViews[1].setLayoutParams(params);

		mGoodsYetCompleteds[0].setLayoutParams(params);
		mGoodsYetCompleteds[1].setLayoutParams(params);

		this.addView(view);
		setViewGone();
	}

	private void setViewGone()
	{
		for (int i = 0; i < 2; i++)
		{
			mLinearLayouts[i].setVisibility(View.INVISIBLE);
			mImageViews[i].setImageBitmap(null);
		}
	}

	/**
	 * 设置商品数据
	 * 
	 * @param goodsInfos
	 *            商品信息
	 */
	public void setShopDatas(List<GoodsInfo> goodsInfos, String yetSellTip)
	{
		setViewGone();
		if (null == goodsInfos || 0 == goodsInfos.size())
		{
			return;
		}
		for (int i = 0; i < goodsInfos.size(); i++)
		{
			final GoodsInfo goodsInfo = goodsInfos.get(i);
			if (null != goodsInfo)
			{
				mLinearLayouts[i].setVisibility(View.VISIBLE);
				GlideImageLoader.loadImageWithFixedSize(getContext(), goodsInfo.getSrcSmImg(), mImageViews[i]);
				mGoodsNames[i].setText(goodsInfo.getProductName());
				mGoodsPrices[i].setText("￥" + goodsInfo.getV2gogoPrice());
				mGoodsSell[i].setText(String.format(yetSellTip, goodsInfo.getSalesVolume()));
				if (goodsInfo.getStock() <= 0)
				{
					mGoodsYetCompleteds[i].setVisibility(View.VISIBLE);
				}
				else
				{
					mGoodsYetCompleteds[i].setVisibility(View.GONE);
				}
				mLinearLayouts[i].setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View view)
					{
						jump2GoodsDetails(goodsInfo.getId());
					}
				});
			}
		}
	}

	/**
	 * 跳转到商品详情
	 * 
	 * @param id
	 */
	private void jump2GoodsDetails(String id)
	{
		StringBuilder url = new StringBuilder();
		url.append(ServerUrlConfig.SERVER_URL).append("/product/groupProductDetail?id=").append(id);

		Intent intent = new Intent(getContext(), GroupGoodsDetailsActivity.class);
		intent.putExtra(GroupGoodsDetailsActivity.GOODS_URL, url.toString());
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		getContext().startActivity(intent);
	}
}
