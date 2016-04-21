package com.v2gogo.project.views.logic;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.v2gogo.project.InternalLinksTool;
import com.v2gogo.project.R;
import com.v2gogo.project.activity.WebViewActivity;
import com.v2gogo.project.activity.account.AccountLoginActivity;
import com.v2gogo.project.activity.exchange.ExchangePrizeDetailsActivity;
import com.v2gogo.project.activity.home.BaseDetailsctivity;
import com.v2gogo.project.activity.home.HomeActivity;
import com.v2gogo.project.activity.home.HomeArticleDetailsActivity;
import com.v2gogo.project.activity.home.subject.HomeSubjectActivity;
import com.v2gogo.project.activity.home.theme.HomeThemePhotoTabActivity;
import com.v2gogo.project.activity.shop.GroupGoodsDetailsActivity;
import com.v2gogo.project.domain.SliderInfo;
import com.v2gogo.project.domain.home.PopularizeInfo;
import com.v2gogo.project.domain.home.PopularizeItemInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.main.image.GlideImageLoader;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.utils.common.ScreenUtil;
import com.v2gogo.project.utils.http.IntentExtraConstants;
import com.v2gogo.project.utils.qiniu.VersionPhotoUrlBuilder;

/**
 * 推广团购显示
 * 
 * @author houjun
 */
public class PopularizeCustomLayout extends LinearLayout implements OnClickListener
{

	private TextView mTextName;
	private ImageView mImageThumb;

	public PopularizeCustomLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initViews(context);
	}

	public PopularizeCustomLayout(Context context)
	{
		super(context);
		initViews(context);
	}

	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{

			default:
				break;
		}
	}

	/**
	 * 初始化控件
	 */
	private void initViews(Context context)
	{
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View view = layoutInflater.inflate(R.layout.fragment_home_popularize_custom_layout, null);
		loadViews(view, context);
		this.addView(view);
	}

	/**
	 * 加载控件
	 */
	private void loadViews(View view, Context context)
	{
		mTextName = (TextView) view.findViewById(R.id.fragment_home_popularize_custom_name);
		mImageThumb = (ImageView) view.findViewById(R.id.fragment_home_popularize_custom_image);
		int height = (int) (ScreenUtil.getScreenWidth(context) * 3 / 8f);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ScreenUtil.getScreenWidth(context), height);
		mImageThumb.setLayoutParams(layoutParams);
	}

	/**
	 * 设置推广数据
	 */
	public void setPopularizeData(final Context context, final PopularizeInfo popularizeInfo)
	{
		mTextName.setText(popularizeInfo.getName());
		List<PopularizeItemInfo> popularizeItemInfos = popularizeInfo.getInfos();
		if (null != popularizeItemInfos && popularizeItemInfos.size() > 0)
		{
			final PopularizeItemInfo popularizeItemInfo = popularizeItemInfos.get(0);
			if (null != popularizeItemInfo)
			{
				GlideImageLoader.loadImageWithFixedSize(context, VersionPhotoUrlBuilder.createThumbialUrlByTE(popularizeItemInfo.getUrl()), mImageThumb);
				mImageThumb.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View view)
					{
						if (popularizeInfo.getSrctype() == PopularizeInfo.TYPE_POPULARIZE_WEBSITE)
						{
							InternalLinksTool.jump2Activity(context, popularizeItemInfo.getSrctype(), popularizeItemInfo.getInfoid(),
									popularizeItemInfo.getHref(), null);
						}
						else if (popularizeInfo.getSrctype() == PopularizeInfo.TYPE_POPULARIZE_SUBJECT)
						{
							Intent intent = new Intent(context, HomeSubjectActivity.class);
							intent.putExtra(HomeSubjectActivity.SUBJECT_ID, popularizeItemInfo.getHref());
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							context.startActivity(intent);
						}
					}

				});
			}
		}
	}

	@Override
	public void setVisibility(int visibility)
	{
		super.setVisibility(visibility);
		if (visibility == View.GONE)
		{
			mImageThumb.setImageBitmap(null);
		}
	}
}
