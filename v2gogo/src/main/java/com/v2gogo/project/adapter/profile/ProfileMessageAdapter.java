package com.v2gogo.project.adapter.profile;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.domain.profile.MessageInfo;

/**
 * 消息适配器
 * 
 * @author houjun
 */
public class ProfileMessageAdapter extends BaseAdapter
{

	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private List<MessageInfo> mMessageInfos;
	private IonStartReadMessageCallback mIonStartReadMessageCallback;

	public ProfileMessageAdapter(Context context)
	{
		mContext = context;
		mLayoutInflater = LayoutInflater.from(mContext);
	}

	public void setOnStartReadMessageCallback(IonStartReadMessageCallback mIonStartReadMessageCallback)
	{
		this.mIonStartReadMessageCallback = mIonStartReadMessageCallback;
	}

	/**
	 * 刷新数据
	 * 
	 * @param messageInfos
	 */
	public void resetDatas(List<MessageInfo> messageInfos)
	{
		mMessageInfos = messageInfos;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount()
	{
		if (null == mMessageInfos)
		{
			return 0;
		}
		return mMessageInfos.size();
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
		if (null == convertView || null == convertView.getTag())
		{
			convertView = mLayoutInflater.inflate(R.layout.profile_message_activity_list_item, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		bindDatas(position, viewHolder);
		return convertView;
	}

	/**
	 * 绑定数据
	 */
	private void bindDatas(int position, ViewHolder viewHolder)
	{
		final MessageInfo messageInfo = mMessageInfos.get(position);
		if (null != messageInfo)
		{
			viewHolder.mTvContent.setText(messageInfo.getTitle());
			viewHolder.mBtnYetRead.setTextColor(0xFFFFFFFF);
			if (MessageInfo.YET_READ == messageInfo.getRead())
			{
				viewHolder.mBtnYetRead.setText(R.string.profile_message_yet_read);
				viewHolder.mBtnYetRead.setTextColor(0xFF696969);
				viewHolder.mBtnYetRead.setBackgroundResource(R.drawable.selector_common_btn_drawable);
			}
			else if (MessageInfo.NO_READ == messageInfo.getRead())
			{
				viewHolder.mBtnYetRead.setText(R.string.profile_message_no_read);
				viewHolder.mBtnYetRead.setBackgroundResource(R.drawable.selector_common_org_btn_drawable);
			}
			viewHolder.mBtnYetRead.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View arg0)
				{
					if (null != mIonStartReadMessageCallback)
					{
						mIonStartReadMessageCallback.onStartReadMessage(messageInfo);
					}
				}
			});
			viewHolder.mCreatetime.setText(messageInfo.getmMessageTime());
		}
	}

	private final class ViewHolder
	{
		private TextView mTvContent;
		private Button mBtnYetRead;
		private TextView mCreatetime;

		public ViewHolder(View view)
		{
			mTvContent = (TextView) view.findViewById(R.id.profile_message_text_content);
			mBtnYetRead = (Button) view.findViewById(R.id.profile_message_btn_yt_read);
			mCreatetime = (TextView) view.findViewById(R.id.profile_message_create_time);
		}
	}

	/**
	 * 读取消息
	 * 
	 * @author houjun
	 */
	public interface IonStartReadMessageCallback
	{
		public void onStartReadMessage(MessageInfo messageInfo);
	}
}
