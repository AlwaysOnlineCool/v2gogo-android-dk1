package com.v2gogo.project.views.logic;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.v2gogo.project.activity.home.theme.HomeThemePhotoCommandListActivity;
import com.v2gogo.project.domain.home.theme.ThemePhotoCommandUserInfo;
import com.v2gogo.project.main.image.GlideImageLoader;
import com.v2gogo.project.utils.common.DensityUtil;
import com.v2gogo.project.utils.common.ScreenUtil;
import com.v2gogo.project.utils.http.IntentExtraConstants;

/**
 * 点赞头像列表
 * 
 * @author houjun
 */
public class ThemePhotoAvatarLayout extends LinearLayout
{

	private LinearLayout mAvatarContainer;
	private LinearLayout.LayoutParams mLayoutParams;

	public ThemePhotoAvatarLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initViews(context);
	}

	public ThemePhotoAvatarLayout(Context context)
	{
		super(context);
		initViews(context);
	}

	private void initViews(Context context)
	{
		mAvatarContainer = new LinearLayout(context);
		mAvatarContainer.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		mAvatarContainer.setLayoutParams(layoutParams);
		this.removeAllViews();
		this.addView(mAvatarContainer);
	}

	
	/**
	 * 设置头像列表
	 * @param commandUserInfos
	 */
	public void setCommandUserListDatas(final List<ThemePhotoCommandUserInfo> commandUserInfos,final String photoId)
	{
		if(commandUserInfos == null || commandUserInfos.size() ==0)
		{
			return;
		}
		int width  = 0;
		if(null == mLayoutParams)
		{
			width =  DensityUtil.dp2px(getContext().getApplicationContext(), 40f);
			mLayoutParams =  new LayoutParams(width, width);
		}
		mAvatarContainer.removeAllViews();
		int leftMargin =  DensityUtil.dp2px(getContext(), 10f);
		int size =  (ScreenUtil.getScreenWidth(getContext().getApplicationContext())-DensityUtil.dp2px(getContext().getApplicationContext(), 20))/(width+leftMargin);
		if(size >=commandUserInfos.size())
		{
			size =  commandUserInfos.size();
		}
		for(int i =0;i<size;i++)
		{
			ThemePhotoCommandUserInfo themePhotoCommandUserInfo =  commandUserInfos.get(i);
			if(themePhotoCommandUserInfo !=null)
			{
				ImageView imageView =  new ImageView(getContext());
				imageView.setScaleType(ScaleType.CENTER_CROP);
				if(i == 0)
				{
					mLayoutParams.leftMargin =  0;
				}
				else
				{
					mLayoutParams.leftMargin =  leftMargin;
				}
				imageView.setLayoutParams(mLayoutParams);
				mAvatarContainer.addView(imageView);
				GlideImageLoader.loadAvatarImageWithFixedSize(getContext(), themePhotoCommandUserInfo.getThumbialUrl(), imageView);
				imageView.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View view)
					{
						Intent intent =  new Intent(getContext(), HomeThemePhotoCommandListActivity.class);
						intent.putExtra(IntentExtraConstants.PID, photoId);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						getContext().startActivity(intent);
					}
				});
			}
		}
	}
}
