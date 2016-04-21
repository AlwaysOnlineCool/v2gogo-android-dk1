package com.v2gogo.project.views.logic;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.home.BaseDetailsctivity;
import com.v2gogo.project.activity.home.HomeArticleDetailsActivity;
import com.v2gogo.project.activity.home.HomeConcernActivity;
import com.v2gogo.project.domain.home.PopularizeInfo;
import com.v2gogo.project.domain.home.PopularizeItemInfo;
import com.v2gogo.project.main.image.GlideImageLoader;

/**
 * 推广文章显示
 * 
 * @author houjun
 */
public class PopularizeArticeLayout extends LinearLayout
{

	private final int MAX_SIZE = 3;

	private TextView mTvName;
	private RelativeLayout mMoreLayout;

	private TextView[] mTvTitles;
	private TextView[] mTvBrowers;
	private TextView[] mTvDescriptions;
	private TextView[] mTvDate;
	private TextView[] mTvTye;

	private ImageView[] mImageThumbs;
	private ImageView[] mIvVideos;
	private RelativeLayout[] mRelativeLayouts;

	public PopularizeArticeLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initViews(context);
	}

	public PopularizeArticeLayout(Context context)
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
		View view = layoutInflater.inflate(R.layout.fragment_home_popularize_article_layout, null);
		loadViews(view);
		this.addView(view);
	}

	/**
	 * 加载控件
	 */
	private void loadViews(View view)
	{
		mTvTye = new TextView[MAX_SIZE];
		mTvDate = new TextView[MAX_SIZE];
		mTvTitles = new TextView[MAX_SIZE];
		mTvBrowers = new TextView[MAX_SIZE];
		mIvVideos = new ImageView[MAX_SIZE];
		mImageThumbs = new ImageView[MAX_SIZE];
		mTvDescriptions = new TextView[MAX_SIZE];
		mRelativeLayouts = new RelativeLayout[MAX_SIZE];

		mTvName = (TextView) view.findViewById(R.id.fragment_home_popularize_artice_name);
		mMoreLayout = (RelativeLayout) view.findViewById(R.id.fragment_home_artice_more_layout);

		mTvTitles[0] = (TextView) view.findViewById(R.id.fragment_home_popularize_artice_one_title);
		mTvTitles[1] = (TextView) view.findViewById(R.id.fragment_home_popularize_artice_two_title);
		mTvTitles[2] = (TextView) view.findViewById(R.id.fragment_home_popularize_artice_three_title);

		mTvTye[0] = (TextView) view.findViewById(R.id.fragment_home_popularize_artice_one_detail_type);
		mTvTye[1] = (TextView) view.findViewById(R.id.fragment_home_popularize_artice_two_detail_type);
		mTvTye[2] = (TextView) view.findViewById(R.id.fragment_home_popularize_artice_three_detail_type);

		mTvDate[0] = (TextView) view.findViewById(R.id.fragment_home_popularize_artice_one_date);
		mTvDate[1] = (TextView) view.findViewById(R.id.fragment_home_popularize_artice_two_date);
		mTvDate[2] = (TextView) view.findViewById(R.id.fragment_home_popularize_artice_three_date);

		mTvBrowers[0] = (TextView) view.findViewById(R.id.fragment_home_popularize_artice_one_brower);
		mTvBrowers[1] = (TextView) view.findViewById(R.id.fragment_home_popularize_artice_two_brower);
		mTvBrowers[2] = (TextView) view.findViewById(R.id.fragment_home_popularize_artice_three_brower);

		mTvDescriptions[0] = (TextView) view.findViewById(R.id.fragment_home_popularize_artice_one_desr);
		mTvDescriptions[1] = (TextView) view.findViewById(R.id.fragment_home_popularize_artice_two_desr);
		mTvDescriptions[2] = (TextView) view.findViewById(R.id.fragment_home_popularize_artice_three_desr);

		mImageThumbs[0] = (ImageView) view.findViewById(R.id.fragment_home_popularize_artice_one_thumb);
		mImageThumbs[1] = (ImageView) view.findViewById(R.id.fragment_home_popularize_artice_two_thumb);
		mImageThumbs[2] = (ImageView) view.findViewById(R.id.fragment_home_popularize_artice_three_thumb);

		mIvVideos[0] = (ImageView) view.findViewById(R.id.fragment_home_popularize_artice_one_video);
		mIvVideos[1] = (ImageView) view.findViewById(R.id.fragment_home_popularize_artice_two_video);
		mIvVideos[2] = (ImageView) view.findViewById(R.id.fragment_home_popularize_artice_three_video);

		mRelativeLayouts[0] = (RelativeLayout) view.findViewById(R.id.fragment_home_popularize_artice_one_layout);
		mRelativeLayouts[1] = (RelativeLayout) view.findViewById(R.id.fragment_home_popularize_artice_two_layout);
		mRelativeLayouts[2] = (RelativeLayout) view.findViewById(R.id.fragment_home_popularize_artice_three_layout);

		initVisibity();
	}

	/**
	 * 设置推广数据
	 */
	public void setPopularizeData(final Context context, PopularizeInfo popularizeInfo)
	{
		initVisibity();
		registerMoreClickListener(context);
		mTvName.setText(popularizeInfo.getName());
		List<PopularizeItemInfo> popularizeItemInfos = popularizeInfo.getInfos();
		if (null != popularizeItemInfos && popularizeItemInfos.size() > 0)
		{
			for (int i = 0, size = popularizeItemInfos.size(); i < size; i++)
			{
				PopularizeItemInfo popularizeItemInfo = popularizeItemInfos.get(i);
				if (null != popularizeItemInfo)
				{
					mRelativeLayouts[i % MAX_SIZE].setVisibility(View.VISIBLE);
					registerListener(context, i % MAX_SIZE, popularizeItemInfo);
					mTvTitles[i % MAX_SIZE].setText(popularizeItemInfo.getTittle());
					mTvDescriptions[i % MAX_SIZE].setText(popularizeItemInfo.getIntro());
					mTvDate[i % MAX_SIZE].setText(popularizeItemInfo.getPublishedTimeString());
					mTvBrowers[i % MAX_SIZE].setText("" + popularizeItemInfo.getBrowser());
					if (TextUtils.isEmpty(popularizeItemInfo.getTypelabel()))
					{
						mTvTye[i % MAX_SIZE].setVisibility(View.GONE);
					}
					else
					{
						mTvTye[i % MAX_SIZE].setVisibility(View.VISIBLE);
						mTvTye[i % MAX_SIZE].setText(popularizeItemInfo.getTypelabel());
					}
					if (popularizeItemInfo.getVideo() == 0)
					{
						mIvVideos[i % MAX_SIZE].setVisibility(View.GONE);
					}
					else
					{
						mIvVideos[i % MAX_SIZE].setVisibility(View.VISIBLE);
					}
					GlideImageLoader.loadImageWithFixedSize(context, popularizeItemInfo.getThumbialUrl(), mImageThumbs[i % MAX_SIZE]);
				}
			}
		}
	}

	/**
	 * 初始化控件
	 */
	private void initVisibity()
	{
		mRelativeLayouts[0].setVisibility(View.GONE);
		mRelativeLayouts[1].setVisibility(View.GONE);
		mRelativeLayouts[2].setVisibility(View.GONE);
	}

	/**
	 * 注册更多监听
	 */
	private void registerMoreClickListener(final Context context)
	{
		mMoreLayout.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Intent intent = new Intent(context, HomeConcernActivity.class);
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
		mRelativeLayouts[i].setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Intent intent = new Intent(context, HomeArticleDetailsActivity.class);
				intent.putExtra(BaseDetailsctivity.ID, popularizeItemInfo.getInfoid());
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
