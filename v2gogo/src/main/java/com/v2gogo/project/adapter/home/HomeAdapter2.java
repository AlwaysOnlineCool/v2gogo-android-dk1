package com.v2gogo.project.adapter.home;

import io.vov.vitamio.utils.Log;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.v2gogo.project.InternalLinksTool;
import com.v2gogo.project.R;
import com.v2gogo.project.activity.exchange.ExchangeActivity;
import com.v2gogo.project.activity.exchange.ExchangePrizeDetailsActivity;
import com.v2gogo.project.activity.home.BaseDetailsctivity;
import com.v2gogo.project.activity.home.HomeArticleDetailsActivity;
import com.v2gogo.project.activity.home.HomeConcernActivity;
import com.v2gogo.project.activity.home.subject.HomeSubjectActivity;
import com.v2gogo.project.activity.shop.GroupGoodsDetailsActivity;
import com.v2gogo.project.activity.shop.GroupWebViewActivity;
import com.v2gogo.project.domain.home.HomeInfo;
import com.v2gogo.project.domain.home.PopularizeInfo;
import com.v2gogo.project.domain.home.PopularizeItemInfo;
import com.v2gogo.project.main.image.GlideImageLoader;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.utils.common.ScreenUtil;
import com.v2gogo.project.utils.qiniu.VersionPhotoUrlBuilder;
import com.v2gogo.project.views.expandablelistview.PullExpandableListview;

/**
 * 功能：首页新适配器 优化处理
 * 
 * @ahthor：黄荣星
 * @date:2016-1-13
 * @version::V1.0
 */
public class HomeAdapter2 extends BaseExpandableListAdapter
{

	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private HomeInfo mHomeInfo;
	private PullExpandableListview mPullExpandableListview;

	public HomeAdapter2(Context context, PullExpandableListview expandableListview)
	{
		mContext = context;
		this.mPullExpandableListview = expandableListview;
		mLayoutInflater = LayoutInflater.from(mContext);
	}

	/**
	 * 刷新数据
	 */
	public void reSetDatas(HomeInfo homeInfo)
	{
		mHomeInfo = homeInfo;
		this.notifyDataSetChanged();
		for (int i = 0, size = getGroupCount(); i < size; i++)
		{
			mPullExpandableListview.expandGroup(i);
		}
	}

	//
	// @Override
	// public View getView(int position, View convertView, ViewGroup viewGroup)
	// {
	// ViewHolder viewHolder = null;
	// if (convertView == null)
	// {
	// convertView = mLayoutInflater.inflate(R.layout.home_activity_list_item, null);
	// viewHolder = new ViewHolder(convertView);
	// convertView.setTag(viewHolder);
	// }
	// else
	// {
	// viewHolder = (ViewHolder) convertView.getTag();
	// }
	// PopularizeInfo popularizeInfo = mHomeInfo.getPopularizeInfos().get(position);
	// if (null != popularizeInfo)
	// {
	// viewHolder.mPopularizeLayout.setPopularizeData(mContext, popularizeInfo);
	// }
	// return convertView;
	// }
	//
	// private final class ViewHolder
	// {
	// public PopularizeLayout mPopularizeLayout;
	//
	// public ViewHolder(View view)
	// {
	// mPopularizeLayout = (PopularizeLayout)
	// view.findViewById(R.id.fragment_home_list_item_popularize);
	// }
	// }

	@Override
	public int getGroupCount()
	{
		if (null == mHomeInfo || null == mHomeInfo.getPopularizeInfos())
		{
			return 0;
		}
		return mHomeInfo.getPopularizeInfos().size();
	}

	@Override
	public int getChildrenCount(int groupPosition)
	{
		if (null != mHomeInfo && null != mHomeInfo.getPopularizeInfos())
		{
			PopularizeInfo popularizeInfo = mHomeInfo.getPopularizeInfos().get(groupPosition);
			if (popularizeInfo != null && popularizeInfo.getInfos() != null)
			{
				return popularizeInfo.getInfos().size();
			}
			return 0;
		}
		return 0;
	}

	@Override
	public Object getGroup(int groupPosition)
	{
		return mHomeInfo.getPopularizeInfos().get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition)
	{
		return mHomeInfo.getPopularizeInfos().get(groupPosition).getInfos().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition)
	{
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition)
	{
		return childPosition;
	}

	@Override
	public boolean hasStableIds()
	{
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
	{
		GroupViewHolder groupViewHolder = null;
		if (convertView == null)
		{
			convertView = mLayoutInflater.inflate(R.layout.home_expandable_group_layout, null);
			groupViewHolder = new GroupViewHolder(convertView);
			convertView.setTag(groupViewHolder);
		}
		else
		{
			groupViewHolder = (GroupViewHolder) convertView.getTag();
		}
		bindGroupDatas(groupPosition, groupViewHolder, convertView);
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
	{
		PopularizeInfo popularizeInfo = (PopularizeInfo) getGroup(groupPosition);
		if (popularizeInfo != null)
		{
			PopularizeItemInfo itemInfo = (PopularizeItemInfo) getChild(groupPosition, childPosition);
			switch (popularizeInfo.getSrctype())
			{
				case PopularizeInfo.TYPE_POPULARIZE_ARTICE:
					convertView = getArticleItemVIew(itemInfo, childPosition, convertView);
					break;
				case PopularizeInfo.TYPE_POPULARIZE_GOODS:
					convertView = getShopItemVIew(itemInfo, childPosition, convertView);
					break;
				case PopularizeInfo.TYPE_POPULARIZE_PRIZE:
					convertView = getPrizeItemVIew(itemInfo, childPosition, convertView);
					break;
				case PopularizeInfo.TYPE_POPULARIZE_WEBSITE:
					convertView = getCustomItemVIew(itemInfo, popularizeInfo.getSrctype(), childPosition, convertView);
					break;
				case PopularizeInfo.TYPE_POPULARIZE_SUBJECT:
					convertView = getCustomItemVIew(itemInfo, popularizeInfo.getSrctype(), childPosition, convertView);
					break;
				case PopularizeInfo.TYPE_POPULARIZE_CONCER_FOOT:
					convertView = getArticleItemVIew(itemInfo, childPosition, convertView);
					break;

				default:
					break;
			}
		}
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition)
	{
		return false;
	}

	/**
	 * group视图
	 * 
	 * @author houjun
	 */
	private class GroupViewHolder
	{
		private TextView nameTv;
		private TextView moreTv;
		private RelativeLayout mMoreLayout;

		public GroupViewHolder(View groupView)
		{
			nameTv = (TextView) groupView.findViewById(R.id.fragment_home_popularize_artice_name);
			moreTv = (TextView) groupView.findViewById(R.id.expandable_group_more_id);
			mMoreLayout = (RelativeLayout) groupView.findViewById(R.id.fragment_home_artice_more_layout);
		}
	}

	/**
	 * method desc：
	 * 
	 * @param groupPosition
	 * @param groupViewHolder
	 * @param convertView
	 */
	private void bindGroupDatas(int groupPosition, GroupViewHolder groupViewHolder, View convertView)
	{
		final PopularizeInfo popularizeInfo = (PopularizeInfo) getGroup(groupPosition);
		if (popularizeInfo != null)
		{
			groupViewHolder.nameTv.setText(popularizeInfo.getName());
			if (popularizeInfo.getSrctype() == PopularizeInfo.TYPE_POPULARIZE_ARTICE)
			{
				groupViewHolder.moreTv.setVisibility(View.VISIBLE);
			}
			else
			{
				groupViewHolder.moreTv.setVisibility(View.GONE);
			}
			groupViewHolder.mMoreLayout.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					switch (popularizeInfo.getSrctype())
					{
						case PopularizeInfo.TYPE_POPULARIZE_ARTICE:
							Intent intent = new Intent(mContext, HomeConcernActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							mContext.startActivity(intent);
							break;
						case PopularizeInfo.TYPE_POPULARIZE_GOODS:
							Intent intent1 = new Intent(mContext, GroupWebViewActivity.class);
							intent1.putExtra(GroupWebViewActivity.URL, ServerUrlConfig.SERVER_URL + "/product/index");
							intent1.putExtra(GroupWebViewActivity.SHOW_BACK, true);
							intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							mContext.startActivity(intent1);
							break;
						case PopularizeInfo.TYPE_POPULARIZE_PRIZE:
							Intent intent2 = new Intent(mContext, ExchangeActivity.class);
							intent2.putExtra(ExchangeActivity.SHOW_BACK, true);
							mContext.startActivity(intent2);
							break;
						case PopularizeInfo.TYPE_POPULARIZE_WEBSITE:

							break;
						case PopularizeInfo.TYPE_POPULARIZE_SUBJECT:

							break;

						default:
							break;
					}
				}
			});
		}
	}

	/**
	 * method desc：判断是否显示“更多文章按钮”
	 * 
	 * @param srcType
	 * @return
	 */
	private boolean isMoreVisibility(int srcType)
	{
		boolean isMore = false;
		switch (srcType)
		{
			case PopularizeInfo.TYPE_POPULARIZE_ARTICE:
				isMore = true;
				break;
			case PopularizeInfo.TYPE_POPULARIZE_GOODS:
				isMore = true;
				break;
			case PopularizeInfo.TYPE_POPULARIZE_PRIZE:
				isMore = true;
				break;
			case PopularizeInfo.TYPE_POPULARIZE_WEBSITE:
				isMore = false;
				break;
			case PopularizeInfo.TYPE_POPULARIZE_SUBJECT:
				isMore = false;
				break;
			default:
				break;
		}
		return isMore;
	}

	/**
	 * method desc：
	 * 
	 * @param childPosition
	 * @param convertView
	 * @return
	 */
	private View getArticleItemVIew(PopularizeItemInfo itemInfo, int childPosition, View convertView)
	{
		ViewArticleHolder viewHolder = null;
		if (convertView == null || convertView.getTag(R.layout.home_concern_activity_list_item) == null)
		{
			convertView = mLayoutInflater.inflate(R.layout.home_concern_activity_list_item, null);
			viewHolder = new ViewArticleHolder(convertView);
			convertView.setTag(R.layout.home_concern_activity_list_item);
		}
		else
		{
			viewHolder = (ViewArticleHolder) convertView.getTag();
		}

		bindArticleData(itemInfo, viewHolder);
		return convertView;
	}

	private final class ViewArticleHolder
	{
		public ImageView mArticleThumb;

		public TextView mArticleMainTitle;
		public TextView mArticleSubTitle;
		public TextView mArticeBrower;
		public TextView mArticeType;

		private ImageView mIvVideo;
		private TextView mTime;

		private View mView;

		public ViewArticleHolder(View view)
		{
			this.mView = view;
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
	 * 绑定数据
	 */
	private void bindArticleData(final PopularizeItemInfo itemInfo, ViewArticleHolder viewHolder)
	{
		if (null != itemInfo)
		{
			if (itemInfo.getVideo() == 1)
			{
				viewHolder.mIvVideo.setVisibility(View.VISIBLE);
			}
			else
			{
				viewHolder.mIvVideo.setVisibility(View.GONE);
			}
			GlideImageLoader.loadImageWithFixedSize(mContext, itemInfo.getThumbialUrl(), viewHolder.mArticleThumb);
			viewHolder.mArticleMainTitle.setText(itemInfo.getTittle());
			if (TextUtils.isEmpty(itemInfo.getTypelabel()))
			{
				viewHolder.mArticeType.setVisibility(View.GONE);
			}
			else
			{
				viewHolder.mArticeType.setVisibility(View.VISIBLE);
				viewHolder.mArticeType.setText(itemInfo.getTypelabel());
			}
			viewHolder.mArticleSubTitle.setText(itemInfo.getIntro());
			viewHolder.mTime.setText(itemInfo.getPublishedTimeString());
			viewHolder.mArticeBrower.setText("" + itemInfo.getBrowser());

			viewHolder.mView.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					Log.d("ddd", "itemInfo.getHref() = " + itemInfo.getInfotype());
					if (!TextUtils.isEmpty(itemInfo.getHref()) && !itemInfo.getHref().contains("/common/info?"))
					{
						
						Intent intent = new Intent(mContext, HomeSubjectActivity.class);
						intent.putExtra(HomeSubjectActivity.SUBJECT_ID, itemInfo.getHref());
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						mContext.startActivity(intent);
					}
					else
					{
						Intent intent = new Intent(mContext, HomeArticleDetailsActivity.class);
						intent.putExtra(BaseDetailsctivity.ID, itemInfo.getInfoid());
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						mContext.startActivity(intent);
					}
				}
			});

		}
	}

	// ========================商品视图=======================
	/**
	 * method desc：
	 * 
	 * @param childPosition
	 * @param convertView
	 * @return
	 */
	private View getShopItemVIew(PopularizeItemInfo itemInfo, int childPosition, View convertView)
	{
		ViewShopHolder viewHolder = null;
		if (convertView == null || convertView.getTag(R.layout.home_expand_group_shop_layout) == null)
		{
			convertView = mLayoutInflater.inflate(R.layout.home_expand_group_shop_layout, null);
			viewHolder = new ViewShopHolder(convertView);
			convertView.setTag(R.layout.home_expand_group_shop_layout);
		}
		else
		{
			viewHolder = (ViewShopHolder) convertView.getTag();
		}
		bindShopData(childPosition, itemInfo, viewHolder);
		return convertView;
	}

	private final class ViewShopHolder
	{
		private final int MAX_SIZE = 3;

		public ImageView[] mImageThumbs;

		public ViewShopHolder(View view)
		{
			mImageThumbs = new ImageView[MAX_SIZE];
			mImageThumbs[2] = (ImageView) view.findViewById(R.id.fragment_home_popularize_shop_three_image);
			mImageThumbs[0] = (ImageView) view.findViewById(R.id.fragment_home_popularize_shop_first_image);
			mImageThumbs[1] = (ImageView) view.findViewById(R.id.fragment_home_popularize_shop_second_image);
			int screenWidth = ScreenUtil.getScreenWidth(mContext);
			LinearLayout.LayoutParams layoutParams = new LayoutParams((int) (screenWidth / 2f), (int) (screenWidth / 2f));
			mImageThumbs[0].setLayoutParams(layoutParams);
			layoutParams = new LayoutParams((int) (screenWidth / 2f), (int) (screenWidth / 4f));
			mImageThumbs[1].setLayoutParams(layoutParams);
			mImageThumbs[2].setLayoutParams(layoutParams);
			setVisity();
		}

		private void setVisity()
		{
			mImageThumbs[0].setVisibility(View.GONE);
			mImageThumbs[1].setVisibility(View.GONE);
			mImageThumbs[2].setVisibility(View.GONE);
		}
	}

	/**
	 * method desc：
	 * 
	 * @param itemInfo
	 * @param viewHolder
	 */
	private void bindShopData(int position, final PopularizeItemInfo itemInfo, ViewShopHolder viewHolder)
	{

		ImageView imageView = viewHolder.mImageThumbs[position % viewHolder.MAX_SIZE];
		if (null != itemInfo)
		{
			imageView.setVisibility(View.VISIBLE);
			GlideImageLoader.loadImageWithFixedSize(mContext, itemInfo.getThumbialUrl(), imageView);
			viewHolder.mImageThumbs[position].setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					StringBuilder url = new StringBuilder();
					url.append(ServerUrlConfig.SERVER_URL).append("/product/groupProductDetail?id=").append(itemInfo.getInfoid());

					Intent intent = new Intent(mContext, GroupGoodsDetailsActivity.class);
					intent.putExtra(GroupGoodsDetailsActivity.GOODS_URL, url.toString());
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					mContext.startActivity(intent);
				}
			});
		}
		else
		{
			imageView.setImageBitmap(null);
		}
	}

	// ========================商品视图=======================
	// ========================奖品视图=======================
	/**
	 * method desc：
	 * 
	 * @param childPosition
	 * @param convertView
	 * @return
	 */
	private View getPrizeItemVIew(PopularizeItemInfo itemInfo, int childPosition, View convertView)
	{
		ViewPrizeHolder viewHolder = null;
		if (convertView == null || convertView.getTag(R.layout.home_expand_group_prize_layout) == null)
		{
			convertView = mLayoutInflater.inflate(R.layout.home_expand_group_prize_layout, null);
			viewHolder = new ViewPrizeHolder(convertView);
			convertView.setTag(R.layout.home_expand_group_prize_layout);
		}
		else
		{
			viewHolder = (ViewPrizeHolder) convertView.getTag();
		}
		bindPrizeData(childPosition, itemInfo, viewHolder);
		return convertView;
	}

	private final class ViewPrizeHolder
	{
		private final int MAX_SIZE = 4;

		public ImageView[] mImageThumbs;

		public ViewPrizeHolder(View view)
		{
			mImageThumbs = new ImageView[MAX_SIZE];
			mImageThumbs[2] = (ImageView) view.findViewById(R.id.fragment_home_popularize_exchange_three_image);
			mImageThumbs[0] = (ImageView) view.findViewById(R.id.fragment_home_popularize_exchange_first_image);
			mImageThumbs[1] = (ImageView) view.findViewById(R.id.fragment_home_popularize_exchange_second_image);
			mImageThumbs[3] = (ImageView) view.findViewById(R.id.fragment_home_popularize_exchange_four_image);
			int screenWidth = ScreenUtil.getScreenWidth(mContext);
			LinearLayout.LayoutParams layoutParams = new LayoutParams((int) (screenWidth / 4f), (int) (screenWidth / 4f));
			mImageThumbs[0].setLayoutParams(layoutParams);
			mImageThumbs[1].setLayoutParams(layoutParams);
			mImageThumbs[2].setLayoutParams(layoutParams);
			mImageThumbs[3].setLayoutParams(layoutParams);
			setVisibity();
		}

		private void setVisibity()
		{
			mImageThumbs[0].setVisibility(View.GONE);
			mImageThumbs[1].setVisibility(View.GONE);
			mImageThumbs[2].setVisibility(View.GONE);
			mImageThumbs[3].setVisibility(View.GONE);
		}
	}

	/**
	 * method desc：
	 * 
	 * @param itemInfo
	 * @param viewHolder
	 */
	private void bindPrizeData(int position, final PopularizeItemInfo itemInfo, ViewPrizeHolder viewHolder)
	{

		ImageView imageView = viewHolder.mImageThumbs[position % viewHolder.MAX_SIZE];
		if (null != itemInfo)
		{
			imageView.setVisibility(View.VISIBLE);
			GlideImageLoader.loadImageWithFixedSize(mContext, itemInfo.getThumbialUrl(), imageView);
			viewHolder.mImageThumbs[position].setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					String id = itemInfo.getInfoid();
					StringBuilder url = new StringBuilder();
					url.append(ServerUrlConfig.SERVER_URL).append("/prizepaperapp/getExchangePrizeDetail?pid=").append(id);

					Intent intent = new Intent(mContext, ExchangePrizeDetailsActivity.class);
					intent.putExtra(ExchangePrizeDetailsActivity.PRIZE_ID, id);
					intent.putExtra(ExchangePrizeDetailsActivity.PRIZE_URL, url.toString());
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					mContext.startActivity(intent);
				}
			});
		}
		else
		{
			imageView.setImageBitmap(null);
		}
	}

	// ========================奖品视图=======================
	// ========================自定义、专题视图=======================
	/**
	 * method desc：
	 * 
	 * @param childPosition
	 * @param convertView
	 * @return
	 */
	private View getCustomItemVIew(PopularizeItemInfo itemInfo, int srcType, int childPosition, View convertView)
	{
		ViewCustomHolder viewHolder = null;
		if (convertView == null || convertView.getTag(R.layout.home_expand_group_custome_layout) == null)
		{
			convertView = mLayoutInflater.inflate(R.layout.home_expand_group_custome_layout, null);
			viewHolder = new ViewCustomHolder(convertView);
			convertView.setTag(R.layout.home_expand_group_custome_layout);
		}
		else
		{
			viewHolder = (ViewCustomHolder) convertView.getTag();
		}
		bindCustomData(childPosition, srcType, itemInfo, viewHolder);
		return convertView;
	}

	private final class ViewCustomHolder
	{
		public ImageView mImageThumb;

		public ViewCustomHolder(View view)
		{
			mImageThumb = (ImageView) view.findViewById(R.id.fragment_home_popularize_custom_image);
			int height = (int) (ScreenUtil.getScreenWidth(mContext) * 3 / 8f);
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ScreenUtil.getScreenWidth(mContext), height);
			mImageThumb.setLayoutParams(layoutParams);
		}
	}

	/**
	 * method desc：
	 * 
	 * @param itemInfo
	 * @param viewHolder
	 */
	private void bindCustomData(int position, final int srcType, final PopularizeItemInfo itemInfo, ViewCustomHolder viewHolder)
	{
		if (null != itemInfo)
		{
			GlideImageLoader.loadImageWithFixedSize(mContext, VersionPhotoUrlBuilder.createThumbialUrlByTE(itemInfo.getUrl()), viewHolder.mImageThumb);
			viewHolder.mImageThumb.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					if (srcType == PopularizeInfo.TYPE_POPULARIZE_WEBSITE)
					{
						InternalLinksTool.jump2Activity(mContext, itemInfo.getSrctype(), itemInfo.getInfoid(), itemInfo.getHref(), null);
					}
					else if (srcType == PopularizeInfo.TYPE_POPULARIZE_SUBJECT)
					{
						Intent intent = new Intent(mContext, HomeSubjectActivity.class);
						intent.putExtra(HomeSubjectActivity.SUBJECT_ID, itemInfo.getHref());
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						mContext.startActivity(intent);
					}
				}

			});
		}
	}

	// ========================自定义视图=======================
	// ========================专题视图=======================
	/**
	 * method desc：
	 * 
	 * @param childPosition
	 * @param convertView
	 * @return
	 */
	private View getSubjectItemVIew(PopularizeItemInfo itemInfo, int srcType, int childPosition, View convertView)
	{
		ViewCustomHolder viewHolder = null;
		if (convertView == null)
		{
			convertView = mLayoutInflater.inflate(R.layout.home_expand_group_custome_layout, null);
			viewHolder = new ViewCustomHolder(convertView);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewCustomHolder) convertView.getTag();
		}
		bindCustomData(childPosition, srcType, itemInfo, viewHolder);
		return convertView;
	}

	private final class ViewSubjectHolder
	{
		public ImageView mImageThumb;

		public ViewSubjectHolder(View view)
		{
			mImageThumb = (ImageView) view.findViewById(R.id.fragment_home_popularize_custom_image);
			int height = (int) (ScreenUtil.getScreenWidth(mContext) * 3 / 8f);
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ScreenUtil.getScreenWidth(mContext), height);
			mImageThumb.setLayoutParams(layoutParams);
		}
	}

	/**
	 * method desc：
	 * 
	 * @param itemInfo
	 * @param viewHolder
	 */
	private void bindSubjectData(int position, final int srcType, final PopularizeItemInfo itemInfo, ViewCustomHolder viewHolder)
	{
		if (null != itemInfo)
		{
			GlideImageLoader.loadImageWithFixedSize(mContext, VersionPhotoUrlBuilder.createThumbialUrlByTE(itemInfo.getUrl()), viewHolder.mImageThumb);
			viewHolder.mImageThumb.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					if (srcType == PopularizeInfo.TYPE_POPULARIZE_WEBSITE)
					{
						InternalLinksTool.jump2Activity(mContext, itemInfo.getSrctype(), itemInfo.getInfoid(), itemInfo.getHref(), null);
					}
					else if (srcType == PopularizeInfo.TYPE_POPULARIZE_SUBJECT)
					{
						Intent intent = new Intent(mContext, HomeSubjectActivity.class);
						intent.putExtra(HomeSubjectActivity.SUBJECT_ID, itemInfo.getHref());
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						mContext.startActivity(intent);
					}
				}

			});
		}
	}

	// ========================自定义视图=======================
}
