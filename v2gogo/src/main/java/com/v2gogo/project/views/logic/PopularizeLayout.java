package com.v2gogo.project.views.logic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.v2gogo.project.R;
import com.v2gogo.project.domain.home.PopularizeInfo;

/**
 * 推广显示
 * 
 * @author houjun
 */
public class PopularizeLayout extends LinearLayout
{

	private PopularizeShopLayout mPopularizeShopLayout;
	private PopularizeCustomLayout mPopularizeCustomLayout;
	private PopularizeDynamicArticeLayout mPopularizeDynamicArticeLayout;
	private PopularizeExchangeLayout mPopularizeExchangeLayout;
	private PopularizeConcerArticeLayout mPopularizeConcerFootLayout;

	@SuppressLint("NewApi")
	public PopularizeLayout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public PopularizeLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initViews(context);
	}

	public PopularizeLayout(Context context)
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
		View view = layoutInflater.inflate(R.layout.common_popularize_layout, null);
		loadViews(view);
		this.addView(view);
	}

	/**
	 * 加载控件
	 * 
	 * @param view
	 */
	private void loadViews(View view)
	{
		mPopularizeShopLayout = (PopularizeShopLayout) view.findViewById(R.id.common_popularize_shop_layout);
		mPopularizeCustomLayout = (PopularizeCustomLayout) view.findViewById(R.id.common_popularize_custom_layout);
		mPopularizeDynamicArticeLayout = (PopularizeDynamicArticeLayout) view.findViewById(R.id.common_popularize_artice_layout);
		mPopularizeExchangeLayout = (PopularizeExchangeLayout) view.findViewById(R.id.common_popularize_exchange_layout);
		mPopularizeConcerFootLayout = (PopularizeConcerArticeLayout) view.findViewById(R.id.common_popularize_concer_foot_layout);
	}

	/**
	 * 设置推广显示数据
	 */
	public void setPopularizeData(Context context, PopularizeInfo popularizeInfo)
	{
		switch (popularizeInfo.getSrctype())
		{
			case PopularizeInfo.TYPE_POPULARIZE_ARTICE:
				setPopularizeArticeData(context, popularizeInfo);
				break;

			case PopularizeInfo.TYPE_POPULARIZE_GOODS:
				setPopularizeShopData(context, popularizeInfo);
				break;

			case PopularizeInfo.TYPE_POPULARIZE_PRIZE:
				setPopularizePrizeData(context, popularizeInfo);
				break;

			case PopularizeInfo.TYPE_POPULARIZE_WEBSITE:
				setPopularizeCustomData(context, popularizeInfo);
				break;
			case PopularizeInfo.TYPE_POPULARIZE_SUBJECT:
				setPopularizeCustomData(context, popularizeInfo);
				break;
			case PopularizeInfo.TYPE_POPULARIZE_CONCER_FOOT:// 底部文章
				setPopularizeConcerFootData(context, popularizeInfo);
				break;
			default:
				break;
		}
	}

	/**
	 * 设置推广自定义数据
	 */
	private void setPopularizeCustomData(Context context, PopularizeInfo popularizeInfo)
	{
		mPopularizeShopLayout.setVisibility(View.GONE);
		mPopularizeDynamicArticeLayout.setVisibility(View.GONE);
		mPopularizeExchangeLayout.setVisibility(View.GONE);
		mPopularizeConcerFootLayout.setVisibility(View.GONE);
		if (!popularizeInfo.isEmpty())
		{
			mPopularizeCustomLayout.setVisibility(View.VISIBLE);
			mPopularizeCustomLayout.setPopularizeData(context, popularizeInfo);
		}
		else
		{
			mPopularizeCustomLayout.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置推广奖品数据
	 */
	private void setPopularizePrizeData(Context context, PopularizeInfo popularizeInfo)
	{
		mPopularizeShopLayout.setVisibility(View.GONE);
		mPopularizeDynamicArticeLayout.setVisibility(View.GONE);
		mPopularizeCustomLayout.setVisibility(View.GONE);
		mPopularizeConcerFootLayout.setVisibility(View.GONE);
		if (!popularizeInfo.isEmpty())
		{
			mPopularizeExchangeLayout.setVisibility(View.VISIBLE);
			mPopularizeExchangeLayout.setPopularizeData(context, popularizeInfo);
		}
		else
		{
			mPopularizeExchangeLayout.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置推广商品数据
	 */
	private void setPopularizeShopData(Context context, PopularizeInfo popularizeInfo)
	{
		mPopularizeExchangeLayout.setVisibility(View.GONE);
		mPopularizeCustomLayout.setVisibility(View.GONE);
		mPopularizeDynamicArticeLayout.setVisibility(View.GONE);
		mPopularizeConcerFootLayout.setVisibility(View.GONE);
		if (!popularizeInfo.isEmpty())
		{
			mPopularizeShopLayout.setVisibility(View.VISIBLE);
			mPopularizeShopLayout.setPopularizeData(context, popularizeInfo);
		}
		else
		{
			mPopularizeShopLayout.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置推广文章数据
	 */
	private void setPopularizeArticeData(Context context, PopularizeInfo popularizeInfo)
	{
		mPopularizeShopLayout.setVisibility(View.GONE);
		mPopularizeExchangeLayout.setVisibility(View.GONE);
		mPopularizeCustomLayout.setVisibility(View.GONE);
		mPopularizeConcerFootLayout.setVisibility(View.GONE);
		if (!popularizeInfo.isEmpty())
		{
			mPopularizeDynamicArticeLayout.setVisibility(View.VISIBLE);
			mPopularizeDynamicArticeLayout.setPopularizeData(context, popularizeInfo);
		}
		else
		{
			mPopularizeDynamicArticeLayout.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置底部百姓关注推广文章数据
	 */
	private void setPopularizeConcerFootData(Context context, PopularizeInfo popularizeInfo)
	{
		mPopularizeShopLayout.setVisibility(View.GONE);
		mPopularizeExchangeLayout.setVisibility(View.GONE);
		mPopularizeCustomLayout.setVisibility(View.GONE);
		mPopularizeDynamicArticeLayout.setVisibility(View.GONE);
		if (!popularizeInfo.isEmpty())
		{
			mPopularizeConcerFootLayout.setVisibility(View.VISIBLE);
			mPopularizeConcerFootLayout.setPopularizeData(context, popularizeInfo);
		}
		else
		{
			mPopularizeConcerFootLayout.setVisibility(View.GONE);
		}
	}
}
