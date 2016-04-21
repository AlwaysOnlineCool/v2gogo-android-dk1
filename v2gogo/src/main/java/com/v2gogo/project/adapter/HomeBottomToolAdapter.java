/**    
 * @{#} HomeBottomToolAdapter.java Create on 2015-12-20 下午3:30:13    
 *    
 * Copyright (c) 2013 by BlueSky.    
 *
 *    
 * @author <a href="1084986314@qq.com">BlueSky</a>   
 * @version 1.0    
 */
package com.v2gogo.project.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.domain.ToolInfo;
import com.v2gogo.project.main.image.GlideImageLoader;

/**
 * 功能：
 * 
 * @ahthor：黄荣星
 * @date:2015-12-20
 * @version::V1.0
 */
public class HomeBottomToolAdapter extends BaseAdapter
{
	private List<ToolInfo> datas;
	private Context mContext;
	private LayoutInflater mInflater;

	public HomeBottomToolAdapter(List<ToolInfo> datas, Context mContext)
	{
		super();
		this.datas = datas;
		this.mContext = mContext;
		mInflater = LayoutInflater.from(mContext);
	}

	public void resetDataChanged(List<ToolInfo> datas)
	{
		this.datas = datas;
		notifyDataSetChanged();
	}

	@Override
	public int getCount()
	{
		return datas != null ? datas.size() : 0;
	}

	@Override
	public Object getItem(int positon)
	{
		return datas.get(positon);
	}

	@Override
	public long getItemId(int positon)
	{
		return positon;
	}

	@Override
	public View getView(int positon, View view, ViewGroup group)
	{
		ViewHolder viewHolder;
		if (view == null)
		{
			viewHolder = new ViewHolder();
			view = mInflater.inflate(R.layout.home_bottom_tool_bar, group, false);
			viewHolder.iconImageView = (ImageView) view.findViewById(R.id.activity_tab_main_bottom_home_rb);
			viewHolder.titleTV = (TextView) view.findViewById(R.id.title_tv);

			view.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) view.getTag();
		}
		if (group.getChildCount() == positon)
		{
			viewHolder.titleTV.setTag(positon);
			ToolInfo toolInfo = datas.get(positon);
			viewHolder.titleTV.setText(toolInfo.getToolName());
			if (positon == 0)
			{
				viewHolder.titleTV.setTextColor(0xFFff5a00);
				viewHolder.iconImageView.setColorFilter(0xFFff5a00);
			}
			else
			{
				viewHolder.titleTV.setTextColor(0xFF696969);
				viewHolder.iconImageView.setColorFilter(0xFF696969);
			}
			GlideImageLoader.loadImageWithHomeBottomIcon(mContext, toolInfo.getToolBackImg(), viewHolder.iconImageView);
		}
		return view;
	}

	public class ViewHolder
	{
		ImageView iconImageView;
		TextView titleTV;
	}

}
