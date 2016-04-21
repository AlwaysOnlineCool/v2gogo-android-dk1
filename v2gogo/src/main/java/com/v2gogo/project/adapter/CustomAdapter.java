package com.v2gogo.project.adapter;

import java.util.List;

import com.v2gogo.project.utils.common.LogUtil;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Adapter基类
 */
public abstract class CustomAdapter<T> extends BaseAdapter
{
	protected List<T> mList;
	protected int layoutId;

	public CustomAdapter(List<T> list, int itemLayoutId)
	{
		this.mList = list;
		this.layoutId = itemLayoutId;
	}

	public int getCount()
	{
		return mList.size();
	}

	public T getItem(int position)
	{
		return mList.get(position);
	}

	public long getItemId(int position)
	{
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = ViewHolder.get(position, convertView, parent, layoutId);
		convert(holder, getItem(position));
		return holder.getConvertView();
	}

	public abstract void convert(ViewHolder holder, T bean);
}
