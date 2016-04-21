package com.v2gogo.project.adapter.profile;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.profile.ProfileScoreActivity;
import com.v2gogo.project.domain.exchange.PrizeInfo;
import com.v2gogo.project.main.image.GlideImageLoader;

/**
 * 我的奖品适配器
 * 
 * @author houjun
 */
public class ProfilePrizeAdapter extends BaseAdapter
{

	private Context mContext;
	private IonClickButton mIonClickButton;
	private LayoutInflater mLayoutInflater;
	private List<PrizeInfo> mPrizeInfos;

	public ProfilePrizeAdapter(Context mContext)
	{
		this.mContext = mContext;
		mLayoutInflater = LayoutInflater.from(this.mContext);
	}

	public void setOnClickButton(IonClickButton mIonClickButton)
	{
		this.mIonClickButton = mIonClickButton;
	}

	public void resetDatas(List<PrizeInfo> infos)
	{
		mPrizeInfos = infos;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount()
	{
		if (null == mPrizeInfos)
		{
			return 0;
		}
		return mPrizeInfos.size();
	}

	@Override
	public Object getItem(int position)
	{
		return mPrizeInfos.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2)
	{
		ViewHolder holder = null;
		if (convertView == null)
		{
			convertView = mLayoutInflater.inflate(R.layout.profile_prize_activity_list_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		bindDatas(position, holder);
		return convertView;
	}

	/**
	 * 绑定数据
	 */
	private void bindDatas(final int position, ViewHolder holder)
	{
		final PrizeInfo prizeInfo = mPrizeInfos.get(position);
		if (null != prizeInfo)
		{
			GlideImageLoader.loadImageWithFixedSize(mContext, prizeInfo.getThumbibalPhotoImage(), holder.mPrizeThumb);
			holder.mPrizeName.setText(prizeInfo.getTitle());
			holder.mPrizeDescription.setText(prizeInfo.getDescription());
			holder.mTime.setText(String.format("过期时间:%S", prizeInfo.getEndTimeString()));
			holder.mBtnWriteStatus.setVisibility(View.GONE);
			if (prizeInfo.getEndFlag() != 0 && prizeInfo.getStatus() == 0)
			{
				holder.mBtnStatus.setVisibility(View.GONE);
				holder.mYetOverTime.setVisibility(View.VISIBLE);
			}
			else
			{
				holder.mYetOverTime.setVisibility(View.GONE);
				holder.mBtnStatus.setVisibility(View.VISIBLE);
				if (prizeInfo.getStatus() == 0)
				{
					holder.mBtnStatus.setTextColor(0xFFFFFFFF);
					if (prizeInfo.getPrizepaperType() == 1)
					{
						holder.mBtnStatus.setText(R.string.profile_przie_zhong_chou_result_tip);
					}
					else
					{
						if (prizeInfo.getReceiveType() == PrizeInfo.RECEIVE_TYPE_ZHIJIE)
						{
							holder.mBtnStatus.setText(R.string.profile_przie_zhijie_tip);
						}
						else if (prizeInfo.getReceiveType() == PrizeInfo.RECEIVE_TYPE_XIANCHANG)
						{
							holder.mBtnStatus.setText(R.string.profile_przie_xianchang_tip);
						}
						else if (prizeInfo.getReceiveType() == PrizeInfo.RECEIVE_TYPE_POST)
						{
							holder.mBtnStatus.setText(R.string.profile_prize_post_tip);
						}
					}
					holder.mBtnStatus.setBackgroundResource(R.drawable.selector_common_org_btn_drawable);
				}
				else if (prizeInfo.getStatus() == 1)
				{
					holder.mBtnStatus.setTextColor(0xFF696969);
					holder.mBtnStatus.setText(R.string.profile_prize_yet_get_tip_ex);
					holder.mBtnStatus.setBackgroundResource(R.drawable.selector_common_btn_drawable);

					if (prizeInfo.getIsComment() == 0)
					{
						holder.mBtnWriteStatus.setVisibility(View.VISIBLE);
					}
					else
					{
						holder.mBtnWriteStatus.setVisibility(View.GONE);
					}
				}
			}
			holder.mBtnStatus.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View arg0)
				{
					if (null != mIonClickButton)
					{
						mIonClickButton.onClickButton(prizeInfo);
					}
				}
			});
			holder.mBtnWriteStatus.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View arg0)
				{
					Intent intent = new Intent(mContext, ProfileScoreActivity.class);
					intent.putExtra(ProfileScoreActivity.TARGED_ID, prizeInfo.getPrizepaperid());
					intent.putExtra(ProfileScoreActivity.COMMENT_TYPE, 0);
					intent.putExtra(ProfileScoreActivity.COMMENT_GROUP_POSITION, position);
					mContext.startActivity(intent);
				}
			});
		}
	}

	private class ViewHolder
	{
		public ImageView mPrizeThumb;
		public TextView mPrizeName;
		public TextView mPrizeDescription;
		private TextView mYetOverTime;
		public TextView mTime;
		public Button mBtnStatus;
		public Button mBtnWriteStatus;

		public ViewHolder(View rootView)
		{
			mPrizeThumb = (ImageView) rootView.findViewById(R.id.profile_prize_list_item_thumb);
			mPrizeName = (TextView) rootView.findViewById(R.id.profile_prize_list_item_main_title);
			mYetOverTime = (TextView) rootView.findViewById(R.id.profile_prize_list_yet_completed);
			mPrizeDescription = (TextView) rootView.findViewById(R.id.profile_prize_list_item_sub_title);
			mBtnStatus = (Button) rootView.findViewById(R.id.profile_prize_list_item_ibtn_exchange);
			mTime = (TextView) rootView.findViewById(R.id.profile_prize_list_item_date_time);
			mBtnWriteStatus = (Button) rootView.findViewById(R.id.profile_prize_list_item_write_ibtn_exchange);
		}
	}

	/**
	 * 点击回调
	 * 
	 * @author houjun
	 */
	public interface IonClickButton
	{
		public void onClickButton(PrizeInfo prizeInfo);
	}

}
