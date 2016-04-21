package com.v2gogo.project.adapter.profile;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.profile.ProfileScoreActivity;
import com.v2gogo.project.activity.shop.GroupGoodsDetailsActivity;
import com.v2gogo.project.activity.shop.OrderDetailsActivity;
import com.v2gogo.project.activity.shop.OrderSettlementActivity;
import com.v2gogo.project.domain.shop.OrderGoodsInfo;
import com.v2gogo.project.domain.shop.OrderInfo;
import com.v2gogo.project.main.image.GlideImageLoader;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.views.expandablelistview.PullExpandableListview;

/**
 * 我的订单适配器
 * 
 * @author houjun
 */
public class ProfileOrderAdapter extends BaseExpandableListAdapter
{

	private Context mContext;
	private List<OrderInfo> mOrderInfos;
	private LayoutInflater mLayoutInflater;

	private PullExpandableListview mPullExpandableListview;

	private IonFlowOrderCallback mFlowOrderCallback;
	private IonCancleOrderCallback mCancleOrderCallback;

	public ProfileOrderAdapter(Context context, PullExpandableListview pullExpandableListview)
	{
		mContext = context;
		mLayoutInflater = LayoutInflater.from(mContext);
		this.mPullExpandableListview = pullExpandableListview;
	}

	/**
	 * 刷新数据
	 * 
	 * @param orderInfos
	 */
	public void resetDatas(List<OrderInfo> orderInfos)
	{
		mOrderInfos = orderInfos;
		this.notifyDataSetChanged();
		for (int i = 0, size = getGroupCount(); i < size; i++)
		{
			mPullExpandableListview.expandGroup(i);
		}
	}

	@Override
	public Object getChild(int groupPosition, int childPosition)
	{
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition)
	{
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean lastChind, View convertView, ViewGroup arg4)
	{
		if (lastChind)
		{
			LastChindViewHolder lastChindViewHolder = null;
			if (convertView == null || convertView.getTag(R.layout.profile_order_list_last_chind_layout) == null)
			{
				convertView = mLayoutInflater.inflate(R.layout.profile_order_list_last_chind_layout, null);
				lastChindViewHolder = new LastChindViewHolder(convertView);
				convertView.setTag(R.layout.profile_order_list_last_chind_layout, lastChindViewHolder);
			}
			else
			{
				lastChindViewHolder = (LastChindViewHolder) convertView.getTag(R.layout.profile_order_list_last_chind_layout);
			}
			bindLastChindDatas(groupPosition, lastChindViewHolder, convertView);
		}
		else
		{
			ChindViewHolder chindViewHolder = null;
			if (convertView == null || convertView.getTag(R.layout.profile_order_list_chind_layout) == null)
			{
				convertView = mLayoutInflater.inflate(R.layout.profile_order_list_chind_layout, null);
				chindViewHolder = new ChindViewHolder(convertView);
				convertView.setTag(R.layout.profile_order_list_chind_layout);
			}
			else
			{
				chindViewHolder = (ChindViewHolder) convertView.getTag(R.layout.profile_order_list_chind_layout);
			}
			bindChindDats(groupPosition, childPosition, chindViewHolder, convertView);
		}
		return convertView;
	}

	private void bindChindDats(int groupPosition, int childPosition, ChindViewHolder chindViewHolder, View convertView)
	{
		final OrderInfo orderInfo = mOrderInfos.get(groupPosition);
		if (orderInfo != null)
		{
			if (null != orderInfo.getOrderGoodsInfos())
			{
				final OrderGoodsInfo orderGoodsInfo = orderInfo.getOrderGoodsInfos().get(childPosition);
				if (orderGoodsInfo != null)
				{
					GlideImageLoader.loadImageWithFixedSize(mContext, orderGoodsInfo.getThumbialUrl(), chindViewHolder.mGoodsThumb);
					chindViewHolder.mGoodsName.setText(orderGoodsInfo.getProductName());
					chindViewHolder.mGoodsDescription.setText(orderGoodsInfo.getDescriptions());
					chindViewHolder.mGoodsPrice.setText("¥ " + orderGoodsInfo.getProductPrice());
					convertView.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View view)
						{
							String id = orderGoodsInfo.getProductId();
							StringBuilder url = new StringBuilder();
							url.append(ServerUrlConfig.SERVER_URL).append("/product/groupProductDetail?id=").append(id);

							Intent intent = new Intent(mContext, GroupGoodsDetailsActivity.class);
							intent.putExtra(GroupGoodsDetailsActivity.GOODS_URL, url.toString());
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							mContext.startActivity(intent);
						}
					});
				}
			}
		}
	}

	@Override
	public int getChildrenCount(int groupPosition)
	{
		if (null == mOrderInfos || null == mOrderInfos.get(groupPosition) || null == mOrderInfos.get(groupPosition).getOrderGoodsInfos())
		{
			return 0;
		}
		return mOrderInfos.get(groupPosition).getOrderGoodsInfos().size() + 1;
	}

	@Override
	public Object getGroup(int groupPosition)
	{
		return mOrderInfos.get(groupPosition);
	}

	@Override
	public int getGroupCount()
	{
		if (mOrderInfos == null)
		{
			return 0;
		}
		return mOrderInfos.size();
	}

	@Override
	public long getGroupId(int groupPosition)
	{
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpand, View convertView, ViewGroup arg3)
	{
		GroupViewHolder groupViewHolder = null;
		if (convertView == null)
		{
			convertView = mLayoutInflater.inflate(R.layout.profile_order_list_group_layout, null);
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
	public boolean hasStableIds()
	{
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1)
	{
		return false;
	}

	/**
	 * 绑定lastChind数据
	 */
	private void bindLastChindDatas(final int groupPosition, LastChindViewHolder lastChindViewHolder, View view)
	{
		final OrderInfo orderInfo = mOrderInfos.get(groupPosition);
		if (null != orderInfo)
		{
			lastChindViewHolder.mOrderPrice.setText(orderInfo.getOrderTotal() + "");
			lastChindViewHolder.mOrderStatus.setText(orderInfo.getOrderStatusString(mContext));
			view.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					Intent intent = new Intent(mContext, OrderDetailsActivity.class);
					intent.putExtra(OrderDetailsActivity.ID, orderInfo.getId());
					mContext.startActivity(intent);
				}
			});
			lastChindViewHolder.mWriteBtn.setVisibility(View.GONE);
			lastChindViewHolder.mBtn.setVisibility(View.GONE);
			if (orderInfo.getOrderStatus() == OrderInfo.ORDER_STATUS_YET_COMPLETED)
			{
				lastChindViewHolder.mBtn.setVisibility(View.VISIBLE);
				lastChindViewHolder.mBtn.setText(R.string.profile_order_order_details_write);

				lastChindViewHolder.mWriteBtn.setVisibility(View.VISIBLE);
				lastChindViewHolder.mWriteBtn.setText(R.string.profile_order_object_flow_tip);

				if (orderInfo.getIsComment() == 0)
				{
					lastChindViewHolder.mBtn.setVisibility(View.VISIBLE);
				}
				else
				{
					lastChindViewHolder.mBtn.setVisibility(View.GONE);
				}
			}
			else if (orderInfo.getOrderStatus() == OrderInfo.ORDER_STATUS_NO_PAY)
			{
				lastChindViewHolder.mWriteBtn.setVisibility(View.VISIBLE);
				lastChindViewHolder.mWriteBtn.setText(R.string.profile_order_cancel_order_tip);

				lastChindViewHolder.mBtn.setVisibility(View.VISIBLE);
				lastChindViewHolder.mBtn.setText(R.string.profile_order_now_pay_tip);
			}
			lastChindViewHolder.mBtn.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					if (orderInfo.getOrderStatus() == OrderInfo.ORDER_STATUS_YET_COMPLETED)
					{
						if (orderInfo.getOrderGoodsInfos() != null && orderInfo.getOrderGoodsInfos().get(0) != null)
						{
							String productId = orderInfo.getOrderGoodsInfos().get(0).getProductId();

							Intent intent = new Intent(mContext, ProfileScoreActivity.class);
							intent.putExtra(ProfileScoreActivity.TARGED_ID, productId);
							intent.putExtra(ProfileScoreActivity.COMMENT_TYPE, 1);
							intent.putExtra(ProfileScoreActivity.COMMENT_GROUP_POSITION, groupPosition);
							mContext.startActivity(intent);
						}
					}
					else if (orderInfo.getOrderStatus() == OrderInfo.ORDER_STATUS_NO_PAY)
					{
						Intent intent = new Intent(mContext, OrderSettlementActivity.class);
						intent.putExtra(OrderSettlementActivity.ORDER, orderInfo);
						mContext.startActivity(intent);
					}
				}
			});
			lastChindViewHolder.mWriteBtn.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if (orderInfo.getOrderStatus() == OrderInfo.ORDER_STATUS_NO_PAY)
					{
						if (mCancleOrderCallback != null)
						{
							mCancleOrderCallback.onCancleOrder(orderInfo);
						}
					}
					else if (orderInfo.getOrderStatus() == OrderInfo.ORDER_STATUS_YET_COMPLETED)
					{
						if (mFlowOrderCallback != null)
						{
							mFlowOrderCallback.onFlowOrder(orderInfo);
						}
					}
				}
			});
		}
	}

	/**
	 * 绑定group数据
	 */
	private void bindGroupDatas(int groupPosition, GroupViewHolder groupViewHolder, View view)
	{
		final OrderInfo orderInfo = mOrderInfos.get(groupPosition);
		if (orderInfo != null)
		{
			groupViewHolder.mOrderNo.setText(orderInfo.getOrderNo());
			groupViewHolder.mOrderTime.setText(orderInfo.getCreateTimeString());
			view.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					Intent intent = new Intent(mContext, OrderDetailsActivity.class);
					intent.putExtra(OrderDetailsActivity.ID, orderInfo.getId());
					mContext.startActivity(intent);
				}
			});
			// if (orderInfo.getOrderStatus() == OrderInfo.ORDER_STATUS_YET_COMPLETED)
			// {
			// groupViewHolder.mBtn.setVisibility(View.VISIBLE);
			// groupViewHolder.mBtn.setText(R.string.profile_order_order_details_tip);
			// }
			// else if (orderInfo.getOrderStatus() == OrderInfo.ORDER_STATUS_NO_PAY)
			// {
			// groupViewHolder.mBtn.setVisibility(View.VISIBLE);
			// groupViewHolder.mBtn.setText(R.string.profile_order_order_details_tip);
			// }
			// else if (orderInfo.getOrderStatus() == OrderInfo.ORDER_STATUS_NO_SEND)
			// {
			// groupViewHolder.mBtn.setVisibility(View.VISIBLE);
			// groupViewHolder.mBtn.setText(R.string.profile_order_order_details_tip);
			// }
			groupViewHolder.mBtn.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					clickBtn(orderInfo);
				}
			});
		}
	}

	private void clickBtn(final OrderInfo orderInfo)
	{
		Intent intent = new Intent(mContext, OrderDetailsActivity.class);
		intent.putExtra(OrderDetailsActivity.ID, orderInfo.getId());
		mContext.startActivity(intent);
	}

	public void setOnFlowOrderCallback(IonFlowOrderCallback mFlowOrderCallback)
	{
		this.mFlowOrderCallback = mFlowOrderCallback;
	}

	public void setOnCancleOrderCallback(IonCancleOrderCallback mCancleOrderCallback)
	{
		this.mCancleOrderCallback = mCancleOrderCallback;
	}

	/**
	 * 头部页面视图
	 * 
	 * @author houjun
	 */
	private class GroupViewHolder
	{
		private TextView mOrderNo;
		private TextView mOrderTime;
		private Button mBtn;

		public GroupViewHolder(View groupView)
		{
			mBtn = (Button) groupView.findViewById(R.id.profile_order_group_btn);
			mOrderNo = (TextView) groupView.findViewById(R.id.profile_order_group_order_no);
			mOrderTime = (TextView) groupView.findViewById(R.id.profile_order_group_order_time);
		}
	}

	/**
	 * 最后一个last视图
	 * 
	 * @author houjun
	 */
	private class LastChindViewHolder
	{
		private Button mBtn;
		private Button mWriteBtn;
		private TextView mOrderPrice;
		private TextView mOrderStatus;

		public LastChindViewHolder(View lastChildView)
		{
			mBtn = (Button) lastChildView.findViewById(R.id.profile_order_last_chind_btn);
			mWriteBtn = (Button) lastChildView.findViewById(R.id.profile_order_last_chind_write_btn);
			mOrderPrice = (TextView) lastChildView.findViewById(R.id.profile_order_last_chind_order_price);
			mOrderStatus = (TextView) lastChildView.findViewById(R.id.profile_order_last_chind_order_status);
		}
	}

	private class ChindViewHolder
	{
		private TextView mGoodsPrice;
		private ImageView mGoodsThumb;
		private TextView mGoodsName;
		private TextView mGoodsDescription;

		public ChindViewHolder(View chindView)
		{
			mGoodsPrice = (TextView) chindView.findViewById(R.id.profile_order_chind_gogo_price);
			mGoodsThumb = (ImageView) chindView.findViewById(R.id.profile_order_chind_goods_thumb);
			mGoodsName = (TextView) chindView.findViewById(R.id.profile_order_chind_goods_name);
			mGoodsDescription = (TextView) chindView.findViewById(R.id.profile_order_chind_goods_description);
		}
	}

	public interface IonCancleOrderCallback
	{
		public void onCancleOrder(OrderInfo orderInfo);
	}

	public interface IonFlowOrderCallback
	{
		public void onFlowOrder(OrderInfo orderInfo);
	}
}
