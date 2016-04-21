package com.v2gogo.project.adapter.profile;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.domain.shop.EtcOrderInfo;
import com.v2gogo.project.main.image.GlideImageLoader;
import com.v2gogo.project.utils.common.DateUtil;

/**
 * 电子券的适配器
 * 
 * @author houjun
 */
public class ProfileEtcQuanAdapter extends BaseAdapter
{
	private LayoutInflater mLayoutInflater;

	private IonClickButton mIonClickButton;
	private List<EtcOrderInfo> mEtcOrderInfos;

	public void resetDatas(List<EtcOrderInfo> etcOrderInfos)
	{
		mEtcOrderInfos = etcOrderInfos;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount()
	{
		if (null == mEtcOrderInfos)
		{
			return 0;
		}
		return mEtcOrderInfos.size();
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
		ViewHolder viewHolder = null;
		if (convertView == null)
		{
			if (null == mLayoutInflater)
			{
				mLayoutInflater = LayoutInflater.from(viewGroup.getContext());
			}
			convertView = mLayoutInflater.inflate(R.layout.profile_etc_quan_activity_list_item, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		bindDatas(position, viewGroup, viewHolder);
		return convertView;
	}

	public void setOnClickButton(IonClickButton mIonClickButton)
	{
		this.mIonClickButton = mIonClickButton;
	}

	/**
	 * 绑定数据
	 */
	private void bindDatas(int position, ViewGroup viewGroup, ViewHolder viewHolder)
	{
		final EtcOrderInfo etcOrderInfo = mEtcOrderInfos.get(position);
		if (null != etcOrderInfo)
		{
			viewHolder.mQuanDescription.setText(etcOrderInfo.getDescriptions());
			viewHolder.mQuanName.setText(etcOrderInfo.getProductName());
			viewHolder.mTime.setText(String.format("过期时间:%s", DateUtil.convertStringWithTimeStamp(etcOrderInfo.getCreateTime())));
			if (etcOrderInfo.getReplayStatus() == EtcOrderInfo.ETC_YET_NOT_USE)
			{
				viewHolder.mBtnStatus.setTextColor(0xFFFFFFFF);
				viewHolder.mBtnStatus.setEnabled(true);
				viewHolder.mOverTime.setVisibility(View.GONE);
				viewHolder.mBtnStatus.setText(R.string.profile_prize_yet_get_tip_use);
				viewHolder.mBtnStatus.setBackgroundResource(R.drawable.selector_common_org_btn_drawable);
			}
			else
			{
				viewHolder.mBtnStatus.setTextColor(0xFF696969);
				viewHolder.mBtnStatus.setEnabled(false);
				viewHolder.mOverTime.setVisibility(View.VISIBLE);
				viewHolder.mBtnStatus.setText(R.string.profile_prize_yet_get_tip_used);
				viewHolder.mBtnStatus.setBackgroundResource(R.drawable.selector_common_btn_drawable);
			}
			GlideImageLoader.loadImageWithFixedSize(viewGroup.getContext(), etcOrderInfo.getImg(), viewHolder.mQuanThumb);
			viewHolder.mBtnStatus.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					if (null != mIonClickButton)
					{
						mIonClickButton.onClickButton(etcOrderInfo);
					}
				}
			});
		}
	}

	private class ViewHolder
	{
		public TextView mTime;
		public Button mBtnStatus;
		public TextView mQuanName;
		public ImageView mQuanThumb;
		private TextView mOverTime;
		public TextView mQuanDescription;

		public ViewHolder(View rootView)
		{
			mQuanThumb = (ImageView) rootView.findViewById(R.id.profile_etc_quan_list_item_thumb);
			mQuanName = (TextView) rootView.findViewById(R.id.profile_etc_quan_list_item_main_title);
			mQuanDescription = (TextView) rootView.findViewById(R.id.profile_etc_quan_list_item_sub_title);
			mBtnStatus = (Button) rootView.findViewById(R.id.profile_etc_quan_list_item_ibtn_exchange);
			mOverTime = (TextView) rootView.findViewById(R.id.profile_etc_quan_list_yet_completed);
			mTime = (TextView) rootView.findViewById(R.id.profile_etc_quan_list_item_date_time);
		}
	}

	/**
	 * 点击回调
	 * 
	 * @author houjun
	 */
	public interface IonClickButton
	{
		public void onClickButton(EtcOrderInfo etcOrderInfo);
	}

}
