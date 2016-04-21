package com.v2gogo.project.views.logic;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.home.theme.HomeThemePhotoDetailsActivity;
import com.v2gogo.project.domain.home.theme.ThemePhotoInfo;
import com.v2gogo.project.main.image.GlideImageLoader;
import com.v2gogo.project.utils.common.DensityUtil;
import com.v2gogo.project.utils.common.ScreenUtil;
import com.v2gogo.project.utils.http.IntentExtraConstants;

/**
 * 晒照片的item
 * 
 * @author houjun
 */
public class ThemePhotoItemLayout extends LinearLayout
{

	private TextView[] mTextAlias;
	private TextView[] mTextDate;
	private TextView[] mTextSn;
	private TextView[] mTextDesc;
	private TextView[] mTextCommand;

	private ImageView[] mAvatars;
	private ImageView[] mPhotoImage;

	private LinearLayout[] mLinearLayouts;

	private IonCommandClickCallback mCommandClickCallback;

	public ThemePhotoItemLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initViews(context);
	}

	public ThemePhotoItemLayout(Context context)
	{
		super(context);
		initViews(context);
	}

	private void initViews(Context context)
	{
		View view = LayoutInflater.from(context).inflate(R.layout.theme_photo_item_layout, null);
		LinearLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		view.setLayoutParams(layoutParams);

		mTextSn = new TextView[2];
		mTextDate = new TextView[2];
		mTextDesc = new TextView[2];
		mTextAlias = new TextView[2];
		mTextCommand = new TextView[2];

		mAvatars = new ImageView[2];
		mPhotoImage = new ImageView[2];
		mLinearLayouts = new LinearLayout[2];

		mTextSn[0] = (TextView) view.findViewById(R.id.home_theme_photo_sn_one);
		mTextSn[1] = (TextView) view.findViewById(R.id.home_theme_photo_sn_two);

		mTextDesc[0] = (TextView) view.findViewById(R.id.home_theme_photo_description_one);
		mTextDesc[1] = (TextView) view.findViewById(R.id.home_theme_photo_description_two);
		mTextCommand[0] = (TextView) view.findViewById(R.id.home_theme_photo_command_one);
		mTextCommand[1] = (TextView) view.findViewById(R.id.home_theme_photo_command_two);

		mTextAlias[0] = (TextView) view.findViewById(R.id.home_theme_photo_user_alias_one);
		mTextAlias[1] = (TextView) view.findViewById(R.id.home_theme_photo_user_alias_two);
		mTextDate[0] = (TextView) view.findViewById(R.id.home_theme_photo_publish_date_one);
		mTextDate[1] = (TextView) view.findViewById(R.id.home_theme_photo_publish_date_two);

		mAvatars[0] = (ImageView) view.findViewById(R.id.home_theme_photo_user_avatar_one);
		mAvatars[1] = (ImageView) view.findViewById(R.id.home_theme_photo_user_avatar_two);
		mPhotoImage[0] = (ImageView) view.findViewById(R.id.home_theme_photo_image_one);
		mPhotoImage[1] = (ImageView) view.findViewById(R.id.home_theme_photo_image_two);

		mLinearLayouts[0] = (LinearLayout) view.findViewById(R.id.home_theme_photo_layout_one);
		mLinearLayouts[1] = (LinearLayout) view.findViewById(R.id.home_theme_photo_layout_two);

		int width = (int) ((ScreenUtil.getScreenWidth(context) - DensityUtil.dp2px(context, 18f)) / 2f);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width);
		params.topMargin = 8;

		mPhotoImage[0].setLayoutParams(params);
		mPhotoImage[1].setLayoutParams(params);

		initVisibity();

		this.removeAllViews();
		this.addView(view);
	}

	public void initVisibity()
	{
		mAvatars[0].setImageBitmap(null);
		mAvatars[1].setImageBitmap(null);
		mPhotoImage[0].setImageBitmap(null);
		mPhotoImage[1].setImageBitmap(null);

		mLinearLayouts[0].setVisibility(View.INVISIBLE);
		mLinearLayouts[1].setVisibility(View.INVISIBLE);
	}

	public void setCommandClickCallback(IonCommandClickCallback mCommandClickCallback)
	{
		this.mCommandClickCallback = mCommandClickCallback;
	}

	/**
	 * 设置主题图片数据
	 * 
	 * @param themePhotoInfos
	 */
	public void setThemePhotoDatas(List<ThemePhotoInfo> themePhotoInfos)
	{
		initVisibity();
		if (null == themePhotoInfos)
		{
			return;
		}
		for (int i = 0; i < themePhotoInfos.size() && i < 2; i++)
		{
			final ThemePhotoInfo themePhotoInfo = themePhotoInfos.get(i);
			if (null != themePhotoInfo)
			{
				mLinearLayouts[i].setVisibility(View.VISIBLE);
				mTextSn[i].setText("No:" + themePhotoInfo.getResourceNo());
				mTextDate[i].setText(themePhotoInfo.getFriendlyTime());
				mTextAlias[i].setText(themePhotoInfo.getFullName());
				Drawable drawable = null;
				if (themePhotoInfo.isPraise())
				{
					mTextCommand[i].setTextColor(0xffff6f01);
					drawable = getResources().getDrawable(R.drawable.ic_action_theme_command_pressed);
					drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				}
				else
				{
					mTextCommand[i].setTextColor(0xff878787);
					drawable = getResources().getDrawable(R.drawable.ic_action_theme_command);
					drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				}
				mTextCommand[i].setCompoundDrawables(drawable, null, null, null);
				mTextCommand[i].setText(themePhotoInfo.getPraiseNum() + "");
				mTextDesc[i].setText(themePhotoInfo.getPhotoDescription());
				GlideImageLoader.loadImageWithFixedSize(getContext(), themePhotoInfo.getThumbialPhotoImage(), mPhotoImage[i]);
				GlideImageLoader.loadAvatarImageWithFixedSize(getContext(), themePhotoInfo.getAvatarThumbialUrl(), mAvatars[i]);
				registerListener(i, themePhotoInfo);
			}
		}
	}

	/**
	 * 注册监听
	 * 
	 * @param i
	 * @param themePhotoInfo
	 */
	private void registerListener(int i, final ThemePhotoInfo themePhotoInfo)
	{
		mLinearLayouts[i].setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				forward2Details(themePhotoInfo);
			}
		});
		mTextCommand[i].setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if (mCommandClickCallback != null)
				{
					mCommandClickCallback.onCommandClick(themePhotoInfo);
				}
			}
		});
	}

	/**
	 * 查看主题详情
	 * 
	 * @param themePhotoInfo
	 */
	private void forward2Details(final ThemePhotoInfo themePhotoInfo)
	{
		Intent intent = new Intent(getContext(), HomeThemePhotoDetailsActivity.class);
		intent.putExtra(IntentExtraConstants.PID, themePhotoInfo.getId());
		intent.putExtra(IntentExtraConstants.WIDTH, themePhotoInfo.getImgWidth());
		intent.putExtra(IntentExtraConstants.HEIGHT, themePhotoInfo.getImgHeight());
		intent.putExtra(IntentExtraConstants.TID, themePhotoInfo.gettId());
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		getContext().startActivity(intent);
	}

	public interface IonCommandClickCallback
	{
		public void onCommandClick(ThemePhotoInfo themePhotoInfo);
	}
}
