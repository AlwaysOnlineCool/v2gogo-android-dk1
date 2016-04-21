package com.v2gogo.project.adapter.home;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.v2gogo.project.InternalLinksTool;
import com.v2gogo.project.R;
import com.v2gogo.project.activity.WebViewActivity;
import com.v2gogo.project.activity.account.AccountLoginActivity;
import com.v2gogo.project.activity.home.BaseDetailsctivity;
import com.v2gogo.project.activity.home.HomeArticleDetailsActivity;
import com.v2gogo.project.domain.home.subject.AdInfo;
import com.v2gogo.project.domain.home.subject.SubjectArticle;
import com.v2gogo.project.domain.home.subject.SubjectInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.main.image.GlideImageLoader;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.utils.common.LogUtil;
import com.v2gogo.project.utils.common.ScreenUtil;
import com.v2gogo.project.utils.qiniu.VersionPhotoUrlBuilder;

/**
 * 功能：专题适配器
 * 
 * @ahthor：黄荣星
 * @date:2015-11-23
 * @version::V1.0
 */
public class SubjectAdapter extends BaseExpandableListAdapter
{

	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private SubjectInfo mSubjectInfo;

	public SubjectAdapter(Context context)
	{
		mContext = context;
		mLayoutInflater = LayoutInflater.from(mContext);
	}

	/**
	 * 刷新数据
	 */
	public void reSetDatas(SubjectInfo subjectInfo)
	{
		mSubjectInfo = subjectInfo;
		this.notifyDataSetChanged();
	}

	@Override
	public int getGroupCount()
	{
		return mSubjectInfo != null ? mSubjectInfo.getGroups().size() : 0;
	}

	@Override
	public int getChildrenCount(int groupPosition)
	{
		int count = 0;
		switch (groupPosition)
		{
			case 0:
				if (mSubjectInfo.getInfos() != null)
				{
					count = mSubjectInfo.getInfos().size();
				}
				break;
			case 1:
				if (mSubjectInfo.getAds() != null)
				{
					count = mSubjectInfo.getAds().size();
				}
				break;
			case 2:
				if (mSubjectInfo.getMoreArticleInfos() != null)
				{
					count = mSubjectInfo.getMoreArticleInfos().size();
				}
				break;
		}
		return count;
	}

	@Override
	public Object getGroup(int groupPosition)
	{
		return mSubjectInfo.getGroups().get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition)
	{
		return null;
	}

	@Override
	public long getGroupId(int groupPosition)
	{
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition)
	{
		return 0;
	}

	@Override
	public boolean hasStableIds()
	{
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
	{
		ViewGroupHolder viewHolder = null;
		if (convertView == null)
		{
			viewHolder = new ViewGroupHolder();
			convertView = mLayoutInflater.inflate(R.layout.home_subject_expandlistview_group_layout, null);
			// viewHolder.titleTextView = (TextView)
			// convertView.findViewById(R.id.fragment_home_popularize_artice_name);

			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewGroupHolder) convertView.getTag();
		}
		// viewHolder.titleTextView.setText(mSubjectInfo.getGroups().get(groupPosition));

		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
	{
		switch (groupPosition)
		{
			case 0:// 文章列表
				convertView = getArticleItemView(groupPosition, childPosition, convertView);
				break;
			case 1:// 广告
				convertView = getAdItemView(childPosition, convertView);
				break;
			case 2:// 广告
				convertView = getArticleItemView(groupPosition, childPosition, convertView);
				break;
		}
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition)
	{
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * method desc：专题广告Item视图
	 * 
	 * @param childPosition
	 * @param convertView
	 * @return
	 */
	private View getAdItemView(int childPosition, View convertView)
	{
		ViewAdHolder viewAdHolder = null;
		if (convertView == null || convertView.getTag(R.layout.home_subject_group_ad_layout) == null)
		{
			convertView = mLayoutInflater.inflate(R.layout.home_subject_group_ad_layout, null);
			viewAdHolder = new ViewAdHolder(convertView);
			convertView.setTag(R.layout.home_subject_group_ad_layout);
		}
		else
		{
			viewAdHolder = (ViewAdHolder) convertView.getTag(R.layout.home_subject_group_ad_layout);
		}
		bindAdData(childPosition, viewAdHolder);
		return convertView;
	}

	/**
	 * 跳转到外部链接
	 */
	private void forward2Website(String url, Context context)
	{
		Intent intent = null;
		if (url.contains(ServerUrlConfig.SERVER_URL))
		{
			if (V2GogoApplication.getMasterLoginState())
			{
				intent = new Intent(context, WebViewActivity.class);
				intent.putExtra(WebViewActivity.URL, url);
				context.startActivity(intent);
			}
			else
			{
				AccountLoginActivity.forwardAccountLogin(context);
			}
		}
		else
		{
			intent = new Intent(context, WebViewActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra(WebViewActivity.URL, url);
			context.startActivity(intent);
		}
	}

	/**
	 * method desc：绑定广告数据
	 */
	private void bindAdData(int postion, ViewAdHolder viewHolder)
	{
		final AdInfo adInfo = mSubjectInfo.getAds().get(postion);
		if (adInfo != null)
		{
			viewHolder.adNameTextView.setText(adInfo.getAdvtName());
			GlideImageLoader.loadImageWithFixedSize(mContext, VersionPhotoUrlBuilder.createThumbialUrlByTE(adInfo.getImg()), viewHolder.adImageView);
			viewHolder.adImageView.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					handlerWithUrl(adInfo.getUrl());
				}
			});
		}
	}

	private void handlerWithUrl(String url)
	{
		if (TextUtils.isEmpty(url))
		{
			return;
		}
		else
		{
			url = url.trim();
			LogUtil.d("houjun", "url->" + url);
			if (url.contains("v2gogo://"))
			{
				try
				{
					Uri uri = Uri.parse(url);
					int type = Integer.valueOf(uri.getQueryParameter("type")).intValue();
					String url_target = uri.getQueryParameter("url");
					String id = uri.getQueryParameter("srcId");
					InternalLinksTool.jump2Activity(mContext, type, id, url_target, null);
				}
				catch (Exception e)
				{
					LogUtil.d("houjun", "e->" + e.getLocalizedMessage());
					e.printStackTrace();
				}
			}
			else
			{
				forward2Website(url, mContext);
			}
		}
	}

	/**
	 * method desc：文章列表Item视图
	 * 
	 * @param position
	 * @param convertView
	 * @return
	 */
	private View getArticleItemView(int groupposition, int position, View convertView)
	{
		ViewArticleHolder viewArticleHolder = null;
		if (convertView == null || convertView.getTag(R.layout.home_concern_activity_list_item) == null)
		{
			convertView = mLayoutInflater.inflate(R.layout.home_concern_activity_list_item, null);
			viewArticleHolder = new ViewArticleHolder(convertView);
			convertView.setTag(R.layout.home_concern_activity_list_item);
		}
		else
		{
			viewArticleHolder = (ViewArticleHolder) convertView.getTag(R.layout.home_concern_activity_list_item);
		}

		bindData(groupposition, position, viewArticleHolder, convertView);

		return convertView;
	}

	/**
	 * 绑定数据
	 */
	private void bindData(int groupposition, int position, ViewArticleHolder viewHolder, View view)
	{
		SubjectArticle subjectArticle = null;
		if (groupposition == 2)// 更多文章
		{
			subjectArticle = mSubjectInfo.getMoreArticleInfos().get(position);
		}
		else
		{
			subjectArticle = mSubjectInfo.getInfos().get(position);
		}
		if (null != subjectArticle)
		{
			// 获取文章ID
			final String infoId = subjectArticle.getInfoId();

			viewHolder.mIvVideo.setVisibility(View.GONE);
			GlideImageLoader.loadImageWithFixedSize(mContext, subjectArticle.getThumbialUrl(), viewHolder.mArticleThumb);
			viewHolder.mArticleMainTitle.setText(subjectArticle.getTitle());
			if (TextUtils.isEmpty(subjectArticle.getTypelabel()))
			{
				viewHolder.mArticeType.setVisibility(View.GONE);
			}
			else
			{
				viewHolder.mArticeType.setVisibility(View.VISIBLE);
				viewHolder.mArticeType.setText(subjectArticle.getTypelabel());
			}
			viewHolder.mArticleSubTitle.setText(subjectArticle.getIntro());
			viewHolder.mTime.setText(subjectArticle.getPublishedTimeString());
			viewHolder.mArticeBrower.setText("" + subjectArticle.getBrowser());

			view.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					Intent intent = new Intent(mContext, HomeArticleDetailsActivity.class);
					intent.putExtra(BaseDetailsctivity.ID, infoId);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					mContext.startActivity(intent);
				}
			});
		}
	}

	private static class ViewGroupHolder
	{
		TextView titleTextView;
	}

	private class ViewArticleHolder
	{
		public ImageView mArticleThumb;

		public TextView mArticleMainTitle;
		public TextView mArticleSubTitle;
		public TextView mArticeBrower;
		public TextView mArticeType;

		private ImageView mIvVideo;
		private TextView mTime;

		public ViewArticleHolder(View view)
		{
			mArticleThumb = (ImageView) view.findViewById(R.id.activity_concern_article_thumb);
			mIvVideo = (ImageView) view.findViewById(R.id.activity_concern_article_video);
			mArticeType = (TextView) view.findViewById(R.id.activity_concern_article_type);
			mArticleSubTitle = (TextView) view.findViewById(R.id.activity_concern_article_sub_title);
			mArticleMainTitle = (TextView) view.findViewById(R.id.activity_concern_article_main_title);
			mArticeBrower = (TextView) view.findViewById(R.id.activity_concern_article_sub_brower);
			mArticeType = (TextView) view.findViewById(R.id.activity_concern_article_type);
			mTime = (TextView) view.findViewById(R.id.activity_concern_article_time);
		}
	}

	/**
	 * 广告
	 */
	private class ViewAdHolder
	{
		public ImageView adImageView;
		public TextView adNameTextView;

		public ViewAdHolder(View view)
		{
			adImageView = (ImageView) view.findViewById(R.id.subject_ad_image);
			int height = (int) (ScreenUtil.getScreenWidth(mContext) * 3 / 8f);
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ScreenUtil.getScreenWidth(mContext), height);
			adImageView.setLayoutParams(layoutParams);
			adNameTextView = (TextView) view.findViewById(R.id.subject_ad_name);
		}
	}

}
