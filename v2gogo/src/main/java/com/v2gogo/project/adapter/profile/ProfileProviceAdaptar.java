package com.v2gogo.project.adapter.profile;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.v2gogo.project.R;

public class ProfileProviceAdaptar extends BaseAdapter
{

	private Context mContext;
	private LayoutInflater mLayoutInflater;

	private List<String> mStrings;

	public ProfileProviceAdaptar(Context mContext)
	{
		super();
		this.mContext = mContext;
		mLayoutInflater = LayoutInflater.from(this.mContext);
	}

	public void resetDatas(List<String> strings)
	{
		this.mStrings = strings;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount()
	{
		if (null == mStrings)
		{
			return 0;
		}
		return mStrings.size();
	}

	@Override
	public Object getItem(int arg0)
	{
		return null;
	}

	@Override
	public long getItemId(int arg0)
	{
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2)
	{
		ViewHolder viewHolder = null;
		if (arg1 == null || null == arg1.getTag())
		{
			arg1 = mLayoutInflater.inflate(R.layout.profile_provice_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.mTvPro = (TextView) arg1.findViewById(R.id.profile_pro_text);
			arg1.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) arg1.getTag();
		}
		String str = mStrings.get(arg0);
		if (str != null)
		{
			viewHolder.mTvPro.setText(str);
		}
		return arg1;
	}

	private class ViewHolder
	{
		public TextView mTvPro;
	}

}
