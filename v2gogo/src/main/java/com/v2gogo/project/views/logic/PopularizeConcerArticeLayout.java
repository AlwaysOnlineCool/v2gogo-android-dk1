package com.v2gogo.project.views.logic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.home.BaseDetailsctivity;
import com.v2gogo.project.activity.home.HomeArticleDetailsActivity;
import com.v2gogo.project.adapter.home.HomeConcernFootAdapter;
import com.v2gogo.project.domain.home.PopularizeInfo;
import com.v2gogo.project.domain.home.PopularizeItemInfo;

/**
 * 推广底部百姓关注文章显示
 * 
 * @author bluesky
 */
@SuppressLint("InflateParams")
public class PopularizeConcerArticeLayout extends LinearLayout
{
	private ListView mListView;
	private HomeConcernFootAdapter mAdapter;

	public PopularizeConcerArticeLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initViews(context);
	}

	public PopularizeConcerArticeLayout(Context context)
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
		View view = layoutInflater.inflate(R.layout.fragment_home_popularize_article_concer_layout, null);
		loadViews(view);
		this.addView(view);
	}

	/**
	 * 加载控件
	 */
	private void loadViews(View view)
	{
		mListView = (ListView) view.findViewById(R.id.concer_article_listview);
		//设置底部百姓关注文章头部标题
		TextView titleTextView=(TextView) view.findViewById(R.id.fragment_home_popularize_artice_name);
		titleTextView.setText(getResources().getString(R.string.home_all_concern));
		mListView.setAdapter(mAdapter);
	}

	/**
	 * 设置推广数据
	 */
	public void setPopularizeData(final Context context, PopularizeInfo popularizeInfo)
	{
		mAdapter.resetDatas(popularizeInfo);
		registerListener(context, popularizeInfo);
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
