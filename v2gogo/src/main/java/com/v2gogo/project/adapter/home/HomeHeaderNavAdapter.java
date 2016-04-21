package com.v2gogo.project.adapter.home;

import java.util.List;

import com.v2gogo.project.R;
import com.v2gogo.project.adapter.CustomAdapter;
import com.v2gogo.project.adapter.ViewHolder;
import com.v2gogo.project.domain.home.NavInfo;

/**
 * 功能：首页头部导航适配器
 * 
 * @ahthor：黄荣星
 * @date:2015-11-16
 * @version::V1.0
 */
public class HomeHeaderNavAdapter extends CustomAdapter<NavInfo>
{

	public HomeHeaderNavAdapter(List<NavInfo> list, int itemLayoutId)
	{
		super(list, itemLayoutId);
	}

	@Override
	public void convert(ViewHolder holder, NavInfo bean)
	{
		holder.setText(R.id.home_fragment_shake_item, bean.getName());
		holder.setImageURI(R.id.home_header_nav_img, bean.getThumbImageUrl());
	}

}
