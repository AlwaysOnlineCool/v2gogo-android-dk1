package com.v2gogo.project.adapter.home;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.domain.home.theme.ThemePhotoCommandUserInfo;
import com.v2gogo.project.main.image.GlideImageLoader;

public class HomeThemePhotoCommandListAdapter extends BaseAdapter
{
	private LayoutInflater mLayoutInflater;
	private List<ThemePhotoCommandUserInfo> mThemePhotoCommandUserInfos;

	public void resetDatas(List<ThemePhotoCommandUserInfo> commandUserInfos)
	{
		mThemePhotoCommandUserInfos = commandUserInfos;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount()
	{
		if (null == mThemePhotoCommandUserInfos)
		{
			return 0;
		}
		return mThemePhotoCommandUserInfos.size();
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
	public View getView(int position, View convertView, ViewGroup viewGroup)
	{
		Context context = viewGroup.getContext();
		ViewHolder viewHolder = null;
		if (convertView == null)
		{
			if (null == mLayoutInflater)
			{
				mLayoutInflater = LayoutInflater.from(context);
			}
			convertView = mLayoutInflater.inflate(R.layout.home_theme_photo_command_list_item, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		bindDatas(position, context, viewHolder);
		return convertView;
	}

	
	/**
	 * 绑定数据
	 */
	private void bindDatas(int position, Context context, ViewHolder viewHolder)
	{
		ThemePhotoCommandUserInfo commandUserInfo =  mThemePhotoCommandUserInfos.get(position%getCount());
		if(commandUserInfo !=null)
		{
			viewHolder.mTextAlias.setText(commandUserInfo.getFullName());
			GlideImageLoader.loadAvatarImageWithFixedSize(context, commandUserInfo.getThumbialUrl(), viewHolder.mAvatar);
		}
	}

	private class ViewHolder
	{
		private ImageView mAvatar;
		private TextView mTextAlias;

		public ViewHolder(View rootView)
		{
             mAvatar =  (ImageView) rootView.findViewById(R.id.home_theme_photo_command_list_item_avatar);
             mTextAlias =  (TextView) rootView.findViewById(R.id.home_theme_photo_command_list_item_alias);
		}
	}
}
