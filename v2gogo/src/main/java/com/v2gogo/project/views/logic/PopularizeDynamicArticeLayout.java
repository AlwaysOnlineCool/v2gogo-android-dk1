package com.v2gogo.project.views.logic;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.home.BaseDetailsctivity;
import com.v2gogo.project.activity.home.HomeArticleDetailsActivity;
import com.v2gogo.project.activity.home.HomeConcernActivity;
import com.v2gogo.project.adapter.home.HomeConcernFootAdapter;
import com.v2gogo.project.domain.home.PopularizeInfo;
import com.v2gogo.project.domain.home.PopularizeItemInfo;
import com.v2gogo.project.views.listview.MyListView;

/**
 * 推广文章显示
 * 
 * @author houjun
 */
public class PopularizeDynamicArticeLayout extends LinearLayout
{

	private MyListView mListView;
	private TextView mTvName;
	private RelativeLayout mMoreLayout;
	private HomeConcernFootAdapter mAdapter;

	public PopularizeDynamicArticeLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initViews(context);
	}

	public PopularizeDynamicArticeLayout(Context context)
	{
		super(context);
		initViews(context);
	}

	/**
	 * 初始化控件
	 */
	private void initViews(Context context)
	{
		mAdapter = new HomeConcernFootAdapter(context);
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View view = layoutInflater.inflate(R.layout.fragment_home_popularize_dynamic_article__listview_layout, null);
		loadViews(view);
		this.addView(view);
	}

	/**
	 * 加载控件
	 */
	private void loadViews(View view)
	{

		mListView = (MyListView) view.findViewById(R.id.dynamic_article_listview);
		mTvName = (TextView) view.findViewById(R.id.fragment_home_popularize_artice_name);
		mMoreLayout = (RelativeLayout) view.findViewById(R.id.fragment_home_artice_more_layout);

		mListView.setAdapter(mAdapter);

	}

	/**
	 * 设置推广数据
	 */
	public void setPopularizeData(final Context context, PopularizeInfo popularizeInfo)
	{
		registerMoreClickListener(context);
		registerListener(context, popularizeInfo);
		mTvName.setText(popularizeInfo.getName());
		mAdapter.resetDatas(popularizeInfo);
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
	private void registerListener(final Context context, final PopularizeInfo popularizeInfo)
	{
		mListView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				PopularizeItemInfo itemInfo = popularizeInfo.getInfos().get(position);
				Intent intent = new Intent(context, HomeArticleDetailsActivity.class);
				intent.putExtra(BaseDetailsctivity.ID, itemInfo.getInfoid());
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(intent);
			}

		});
	}

}
