package com.v2gogo.project.adapter.exchange;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.domain.exchange.ExchangeInfo;
import com.v2gogo.project.domain.exchange.PrizeInfo;
import com.v2gogo.project.main.image.GlideImageLoader;
import com.v2gogo.project.utils.common.CountDownUtils;

/**
 * 兑换适配器
 * 
 * @author houjun
 */
public class ExchangeAdapter extends BaseAdapter
{

	private String mLeftTip;
	private String mTotalTip;

	private Context mContext;
	private ExchangeInfo mExchangeInfo;
	private LayoutInflater mLayoutInflater;

	private IonLuanchExchangeListener mIonLuanchExchangeListener;
	private IonItemExchangeClickListener mIonItemExchangeClickListener;

	public ExchangeAdapter(Context context)
	{
		mContext = context;
		mLayoutInflater = LayoutInflater.from(mContext);
		mLeftTip = mContext.getString(R.string.left_prize_num);
		mTotalTip = mContext.getString(R.string.total_prize_num);
	}

	/**
	 * 开始倒计时
	 */
	public void startCountDown(long timeInterval)
	{
		if (mExchangeInfo != null && null != mExchangeInfo.getPrizeInfos() && mExchangeInfo.getPrizeInfos().size() != 0)
		{
			for (PrizeInfo prizeInfo : mExchangeInfo.getPrizeInfos())
			{
				prizeInfo.setRestTime(prizeInfo.getRestTime() - timeInterval - 1000);
			}
			this.notifyDataSetChanged();
		}
	}

	public void setOnLuanchExchangeListener(IonLuanchExchangeListener mIonLuanchExchangeListener)
	{
		this.mIonLuanchExchangeListener = mIonLuanchExchangeListener;
	}

	public void setOnItemExchangeClickListener(IonItemExchangeClickListener mIonItemExchangeClickListener)
	{
		this.mIonItemExchangeClickListener = mIonItemExchangeClickListener;
	}

	/**
	 * 更新数据
	 */
	public void resetDatas(ExchangeInfo exchangeInfo)
	{
		mExchangeInfo = exchangeInfo;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount()
	{
		if (null == mExchangeInfo || null == mExchangeInfo.getPrizeInfos() || mExchangeInfo.getPrizeInfos().size() == 0)
		{
			return 0;
		}
		return mExchangeInfo.getPrizeInfos().size();
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
			convertView = mLayoutInflater.inflate(R.layout.exchange_activity_list_item, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		bindDatas(viewHolder, position, convertView);
		return convertView;
	}

	/**
	 * 绑定数据
	 */
	private void bindDatas(ViewHolder viewHolder, int position, View view)
	{
		PrizeInfo prizeInfo = mExchangeInfo.getPrizeInfos().get(position);
		if (null != prizeInfo)
		{
			displayCountDown(viewHolder, prizeInfo);
			registerListener(viewHolder, prizeInfo, view);
			viewHolder.mMainTitle.setText(prizeInfo.getTitle());
			viewHolder.mSubTitle.setText(prizeInfo.getDescription());
			viewHolder.mNeedGoldNum.setText(prizeInfo.getCoin() + "");
			viewHolder.mLeftNum.setText(String.format(mLeftTip, prizeInfo.getSupply()));
			viewHolder.mTotleNum.setText(String.format(mTotalTip, prizeInfo.getAllSupply()));
			GlideImageLoader.loadImageWithFixedSize(mContext, prizeInfo.getThumbibalPhotoImage(), viewHolder.mImageView);
		}
	}

	/**
	 * 显示倒计时逻辑
	 */
	private void displayCountDown(ViewHolder viewHolder, PrizeInfo prizeInfo)
	{
		if (prizeInfo.getIsPub() == PrizeInfo.SHAKE)
		{
			viewHolder.mCountDown.setVisibility(View.GONE);
			viewHolder.mBtnExchange.setVisibility(View.VISIBLE);
			viewHolder.mBtnExchange.setBackgroundResource(R.drawable.icons_label_red);
			viewHolder.mBtnExchange.setText(R.string.today_shake_tip);
			viewHolder.mBtnExchange.setTextColor(0xffffffff);
		}
		else if (prizeInfo.getIsPub() == PrizeInfo.NORMAL)
		{
			// 已经开启兑换
			if (prizeInfo.getRestTime() <= 0)
			{
				viewHolder.mCountDown.setVisibility(View.GONE);
				viewHolder.mBtnExchange.setVisibility(View.VISIBLE);
				if (prizeInfo.getEndFlag() == 0)
				{
					if (prizeInfo.getPrizepaperType() == 1)
					{
						viewHolder.mBtnExchange.setTextColor(0xFFF96700);
						viewHolder.mBtnExchange.setBackgroundResource(R.drawable.btn_common_greyselected_selected);
						viewHolder.mBtnExchange.setText(R.string.fragment_exchange_raise_tip);
					}
					else
					{
						viewHolder.mBtnExchange.setTextColor(0xFFF96700);
						viewHolder.mBtnExchange.setBackgroundResource(R.drawable.btn_common_greyselected_selected);
						viewHolder.mBtnExchange.setText(R.string.fragment_exchange_exchange_tip);
					}
				}
				else
				{
					viewHolder.mBtnExchange.setTextColor(0xFFF96700);
					viewHolder.mBtnExchange.setBackgroundResource(R.drawable.btn_common_greyselected_selected);
					viewHolder.mBtnExchange.setText(R.string.fragment_stop_exchange_tip);
				}
			}
			else
			{
				viewHolder.mCountDown.setVisibility(View.VISIBLE);
				viewHolder.mBtnExchange.setVisibility(View.GONE);

				// long time = prizeInfo.getRestTime() - 1;// 单位秒
				// viewHolder.mCountDown.setText(CountDownUtils.formatTime(Math.abs(time * 1000)));
				// prizeInfo.setRestTime(time);
				viewHolder.mCountDown.setText(CountDownUtils.formatTime(Math.abs(prizeInfo.getRestTime() * 1000)));
			}
		}
	}

	/**
	 * 注册监听
	 */
	private void registerListener(final ViewHolder viewHolder, final PrizeInfo prizeInfo, View view)
	{
		viewHolder.mBtnExchange.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if (null != mIonLuanchExchangeListener)
				{
					mIonLuanchExchangeListener.onLuanchExchangeStart(prizeInfo);
				}
			}
		});
		view.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if (null != mIonItemExchangeClickListener)
				{
					mIonItemExchangeClickListener.onItemExchangeClick(prizeInfo);
				}
			}
		});

	}

	private final class ViewHolder
	{
		public ImageView mImageView;

		public TextView mMainTitle;
		public TextView mSubTitle;
		public TextView mTotleNum;
		public TextView mLeftNum;
		public TextView mNeedGoldNum;
		private TextView mCountDown;
		public Button mBtnExchange;

		public ViewHolder(View view)
		{
			mImageView = (ImageView) view.findViewById(R.id.fragment_exchange_good_thumb);
			mLeftNum = (TextView) view.findViewById(R.id.fragment_exchange_good_left_num);
			mSubTitle = (TextView) view.findViewById(R.id.fragment_exchange_good_sub_title);
			mTotleNum = (TextView) view.findViewById(R.id.fragment_exchange_good_totle_num);
			mBtnExchange = (Button) view.findViewById(R.id.fragment_exchange_ibtn_exchange);
			mMainTitle = (TextView) view.findViewById(R.id.fragment_exchange_good_main_title);
			mCountDown = (TextView) view.findViewById(R.id.fragment_exchange_count_down_time);
			mNeedGoldNum = (TextView) view.findViewById(R.id.fragment_exchange_good_exchange_gold);
		}
	}

	/**
	 * 立即兑换的接口
	 * 
	 * @author houjun
	 */
	public interface IonLuanchExchangeListener
	{
		public void onLuanchExchangeStart(PrizeInfo prizeInfo);
	}

	/**
	 * 每一个item点击事件
	 * 
	 * @author houjun
	 */
	public interface IonItemExchangeClickListener
	{
		public void onItemExchangeClick(PrizeInfo prizeInfo);
	}
}
