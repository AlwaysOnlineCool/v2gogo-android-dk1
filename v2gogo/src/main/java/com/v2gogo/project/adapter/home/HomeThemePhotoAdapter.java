package com.v2gogo.project.adapter.home;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.v2gogo.project.R;
import com.v2gogo.project.domain.home.theme.ThemePhotoInfo;
import com.v2gogo.project.views.logic.ThemePhotoItemLayout;
import com.v2gogo.project.views.logic.ThemePhotoItemLayout.IonCommandClickCallback;

public class HomeThemePhotoAdapter extends BaseAdapter
{

	private Context mContext;

	private LayoutInflater mLayoutInflater;
	private List<ThemePhotoInfo> mThemePhotoInfos;

	private IonThemePhotoClickCommandCallback mThemePhotoClickCommandCallback;

	public HomeThemePhotoAdapter(Context context)
	{
		mContext = context;
		mLayoutInflater = LayoutInflater.from(mContext);
		mThemePhotoInfos = new ArrayList<ThemePhotoInfo>();
	}

	public void resetDatas(List<ThemePhotoInfo> photoInfos)
	{
		if (null != photoInfos)
		{
			mThemePhotoInfos.clear();
			mThemePhotoInfos.addAll(photoInfos);
		}
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount()
	{
		if (null == mThemePhotoInfos)
		{
			return 0;
		}
		return (mThemePhotoInfos.size() % 2 == 0 ? mThemePhotoInfos.size() / 2 : mThemePhotoInfos.size() / 2 + 1);
	}

	@Override
	public Object getItem(int position)
	{
		return null;
	}

	@Override
	public long getItemId(int position)
	{
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2)
	{
		ViewHolder viewHolder = null;
		if (convertView == null)
		{
			convertView = mLayoutInflater.inflate(R.layout.home_theme_photo_list_item, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		bindThemePhotoDatas(position, viewHolder);
		return convertView;
	}

	public void setThemePhotoClickCommandCallback(IonThemePhotoClickCommandCallback mThemePhotoClickCommandCallback)
	{
		this.mThemePhotoClickCommandCallback = mThemePhotoClickCommandCallback;
	}

	/**
	 * 绑定主题图片数据
	 */
	private void bindThemePhotoDatas(int position, ViewHolder viewHolder)
	{
		List<ThemePhotoInfo> themePhotoInfos = null;
		final int index = position * 2;
		if (position != getCount() - 1)
		{
			themePhotoInfos = mThemePhotoInfos.subList(index, index + 2);
		}
		else
		{
			themePhotoInfos = mThemePhotoInfos.subList(index, mThemePhotoInfos.size());
		}
		if (null != themePhotoInfos)
		{
			viewHolder.mThemePhotoItemLayout.setThemePhotoDatas(themePhotoInfos);
			viewHolder.mThemePhotoItemLayout.setCommandClickCallback(new IonCommandClickCallback()
			{
				@Override
				public void onCommandClick(ThemePhotoInfo themePhotoInfo)
				{
					if (null != mThemePhotoClickCommandCallback)
					{
						mThemePhotoClickCommandCallback.onThemePhotoClickCommand(themePhotoInfo);
					}
				}
			});
		}
	}

	private class ViewHolder
	{
		private ThemePhotoItemLayout mThemePhotoItemLayout;

		public ViewHolder(View view)
		{
			mThemePhotoItemLayout = (ThemePhotoItemLayout) view.findViewById(R.id.home_theme_photo_item_layout);
		}
	}

	public interface IonThemePhotoClickCommandCallback
	{
		public void onThemePhotoClickCommand(ThemePhotoInfo themePhotoInfo);
	}
}
