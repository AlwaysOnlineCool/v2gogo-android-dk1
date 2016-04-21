package com.v2gogo.project.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.v2gogo.project.InternalLinksTool;
import com.v2gogo.project.R;
import com.v2gogo.project.activity.WebViewActivity;
import com.v2gogo.project.activity.account.AccountLoginActivity;
import com.v2gogo.project.activity.exchange.ExchangePrizeDetailsActivity;
import com.v2gogo.project.activity.home.BaseDetailsctivity;
import com.v2gogo.project.activity.home.HomeArticleDetailsActivity;
import com.v2gogo.project.activity.home.theme.HomeThemePhotoTabActivity;
import com.v2gogo.project.activity.shop.GroupGoodsDetailsActivity;
import com.v2gogo.project.domain.SliderInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.main.image.GlideImageLoader;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.utils.http.IntentExtraConstants;

/**
 * 大图轮播适配器
 * 
 * @author houjun
 */
public class SliderAdapter extends BaseAdapter
{

	private Context mContext;
	private List<SliderInfo> mSliderInfos;
	private LayoutInflater mLayoutInflater;

	public SliderAdapter(Context context, List<SliderInfo> sliderInfos)
	{
		super();
		this.mContext = context;
		this.mSliderInfos = sliderInfos;
		mLayoutInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount()
	{
		if (null == mSliderInfos)
		{
			return 0;
		}
		return mSliderInfos.size();
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
		if (null == convertView || null == convertView.getTag())
		{
			convertView = mLayoutInflater.inflate(R.layout.common_slider_layout_item, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		bindData(position, viewHolder);
		return convertView;
	}

	private class ViewHolder
	{
		public ImageView imageView;
		public TextView textview;

		public ViewHolder(View view)
		{
			imageView = (ImageView) view.findViewById(R.id.common_slider_imageview);
			textview = (TextView) view.findViewById(R.id.common_slider_textview);
		}
	}

	/**
	 * 绑定数据
	 */
	private void bindData(int position, ViewHolder viewHolder)
	{
		final SliderInfo sliderInfo = mSliderInfos.get(position);
		if (null != sliderInfo)
		{
			viewHolder.textview.setText(sliderInfo.getTitle());
			GlideImageLoader.loadImageWithFixedSize(mContext, sliderInfo.getThumbialUrl(), viewHolder.imageView);
			viewHolder.imageView.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					// jump2Activity(sliderInfo);
					InternalLinksTool.jump2Activity(mContext, sliderInfo.getSrctype(), sliderInfo.getSrcid(), sliderInfo.getHref(), null);
				}
			});
		}
	}

}
