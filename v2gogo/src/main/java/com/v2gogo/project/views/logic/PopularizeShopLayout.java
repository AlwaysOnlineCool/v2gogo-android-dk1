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
import com.v2gogo.project.activity.shop.GroupWebViewActivity;
import com.v2gogo.project.domain.home.PopularizeInfo;
import com.v2gogo.project.domain.home.PopularizeItemInfo;
import com.v2gogo.project.main.image.GlideImageLoader;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.utils.common.ScreenUtil;

/**
 * 推广团购显示
 * 
 * @author houjun
 */
public class PopularizeShopLayout extends LinearLayout
{

	private final int MAX_SIZE = 3;

	private TextView mTvName;
	private RelativeLayout mTvMore;

	private ImageView[] mImageThumbs;

	public PopularizeShopLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initViews(context);
	}

	public PopularizeShopLayout(Context context)
	{
		super(context);
		initViews(context);
	}

	/**
	 * 初始化控件
	 */
	private void initViews(Context context)
	{
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View view = layoutInflater.inflate(R.layout.fragment_home_popularize_shop_layout, null);
		loadViews(view, context);
		this.addView(view);
	}

	/**
	 * 加载控件
	 */
	private void loadViews(View view, Context context)
	{
		mImageThumbs = new ImageView[MAX_SIZE];
		mTvName = (TextView) view.findViewById(R.id.fragment_home_popularize_shop_name);
		mTvMore = (RelativeLayout) view.findViewById(R.id.fragment_home_popularize_shop_more);
		mImageThumbs[2] = (ImageView) view.findViewById(R.id.fragment_home_popularize_shop_three_image);
		mImageThumbs[0] = (ImageView) view.findViewById(R.id.fragment_home_popularize_shop_first_image);
		mImageThumbs[1] = (ImageView) view.findViewById(R.id.fragment_home_popularize_shop_second_image);
		int screenWidth = ScreenUtil.getScreenWidth(context);
		LinearLayout.LayoutParams layoutParams = new LayoutParams((int) (screenWidth / 2f), (int) (screenWidth / 2f));
		mImageThumbs[0].setLayoutParams(layoutParams);
		layoutParams = new LayoutParams((int) (screenWidth / 2f), (int) (screenWidth / 4f));
		mImageThumbs[1].setLayoutParams(layoutParams);
		mImageThumbs[2].setLayoutParams(layoutParams);
		setVisity();
	}

	/**
	 * 设置推广数据
	 */
	public void setPopularizeData(final Context context, final PopularizeInfo popularizeInfo)
	{
		setVisity();
		registerMoreClickListener(context);
		mTvName.setText(popularizeInfo.getName());
		List<PopularizeItemInfo> popularizeItemInfos = popularizeInfo.getInfos();
		if (null != popularizeItemInfos && popularizeItemInfos.size() > 0)
		{
			for (int i = 0, size = popularizeItemInfos.size(); i < size; i++)
			{
				final PopularizeItemInfo popularizeItemInfo = popularizeItemInfos.get(i);
				if (null != popularizeItemInfo)
				{
					mImageThumbs[i % MAX_SIZE].setVisibility(View.VISIBLE);
					GlideImageLoader.loadImageWithFixedSize(context, popularizeItemInfo.getThumbialUrl(), mImageThumbs[i % MAX_SIZE]);
					registerListener(context, i % MAX_SIZE, popularizeItemInfo);
				}
				else
				{
					mImageThumbs[i % MAX_SIZE].setImageBitmap(null);
				}
			}
		}
	}

	private void setVisity()
	{
		mImageThumbs[0].setVisibility(View.GONE);
		mImageThumbs[1].setVisibility(View.GONE);
		mImageThumbs[2].setVisibility(View.GONE);
	}

	/**
	 * 注册更多监听
	 */
	private void registerMoreClickListener(final Context context)
	{
		mTvMore.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Intent intent = new Intent(context, GroupWebViewActivity.class);
				intent.putExtra(GroupWebViewActivity.URL, ServerUrlConfig.SERVER_URL + "/product/index");
				intent.putExtra(GroupWebViewActivity.SHOW_BACK, true);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(intent);
			}
		});
	}

	/**
	 * 注册监听
	 */
	private void registerListener(final Context context, int i, final PopularizeItemInfo popularizeItemInfo)
	{
		mImageThumbs[i].setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				StringBuilder url = new StringBuilder();
				url.append(ServerUrlConfig.SERVER_URL).append("/product/groupProductDetail?id=").append(popularizeItemInfo.getInfoid());

				Intent intent = new Intent(context, GroupGoodsDetailsActivity.class);
				intent.putExtra(GroupGoodsDetailsActivity.GOODS_URL, url.toString());
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(intent);
			}
		});
	}

	@Override
	public void setVisibility(int visibility)
	{
		super.setVisibility(visibility);
		if (visibility == View.GONE)
		{
			mImageThumbs[0].setImageBitmap(null);
			mImageThumbs[1].setImageBitmap(null);
			mImageThumbs[2].setImageBitmap(null);
		}
	}
}
