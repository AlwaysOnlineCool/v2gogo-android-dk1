package com.v2gogo.project.activity.profile;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.BaseActivity;
import com.v2gogo.project.adapter.profile.ProfileMessageAdapter;
import com.v2gogo.project.adapter.profile.ProfileMessageAdapter.IonStartReadMessageCallback;
import com.v2gogo.project.domain.profile.MessageInfo;
import com.v2gogo.project.domain.profile.ProfileMessageListInfo;
import com.v2gogo.project.manager.profile.ProfileMessageManager;
import com.v2gogo.project.manager.profile.ProfileMessageManager.IonProfileMessageListCallback;
import com.v2gogo.project.manager.profile.ProfileMessageManager.IonProfileReadMessageCallback;
import com.v2gogo.project.utils.common.ToastUtil;
import com.v2gogo.project.views.ProgressLayout;
import com.v2gogo.project.views.ProgressLayout.IonRetryLoadDatasCallback;
import com.v2gogo.project.views.ProgressLayout.State;
import com.v2gogo.project.views.listview.refreshview.OnLoadMoreListener;
import com.v2gogo.project.views.listview.refreshview.OnPullRefreshListener;
import com.v2gogo.project.views.listview.refreshview.PullRefreshListView;

/**
 * 我的消息
 * 
 * @author houjun
 */
public class ProfileMessageActivity extends BaseActivity implements OnClickListener, OnPullRefreshListener, OnLoadMoreListener, IonStartReadMessageCallback,
		OnItemClickListener, IonRetryLoadDatasCallback
{

	private Button mBtnRead;
	private ProfileMessageListInfo mMessageListInfo;

	private ProgressLayout mProgressLayout;
	private PullRefreshListView mPullRefreshListView;
	private ProfileMessageAdapter mProfileMessageAdapter;

	@Override
	public void onInitViews()
	{
		mBtnRead = (Button) findViewById(R.id.profile_message_all_read_btn);
		mProgressLayout = (ProgressLayout) findViewById(R.id.profile_message_progress_layout);
		mPullRefreshListView = (PullRefreshListView) findViewById(R.id.profile_message_pull_to_refresh_listview);
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.profile_message_activity_layout;
	}

	@Override
	protected void registerListener()
	{
		super.registerListener();
		mBtnRead.setOnClickListener(this);
		mPullRefreshListView.setOnPullRefreshListener(this);
		mPullRefreshListView.setOnLoadMoreListener(this);
		mPullRefreshListView.setLoadMoreEnable(false);
		mPullRefreshListView.setOnItemClickListener(this);
		mProgressLayout.setOnTryLoadDatasCallback(this);
	}

	@Override
	protected void onInitLoadDatas()
	{
		super.onInitLoadDatas();
		mProgressLayout.showProgress();
		mMessageListInfo = new ProfileMessageListInfo();
		mProfileMessageAdapter = new ProfileMessageAdapter(this);
		mProfileMessageAdapter.setOnStartReadMessageCallback(this);
		mPullRefreshListView.setAdapter(mProfileMessageAdapter);
		getProfileMessageList(ProfileMessageManager.FIRST_PAGE);
	}

	@Override
	protected boolean isNotifyIntent()
	{
		return true;
	}

	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.profile_message_all_read_btn:
				readProfileMessage("", ProfileMessageManager.MESSAGE_ALL);
				break;

			default:
				break;
		}
	}

	@Override
	public void onPullDownRefresh(AbsListView pullRefreshView)
	{
		getProfileMessageList(ProfileMessageManager.FIRST_PAGE);
	}

	@Override
	public void onLoadMore(AbsListView pullRefreshView)
	{
		getProfileMessageList(mMessageListInfo.getCurrentPage() + 1);
	}

	/**
	 * 个人消息列表
	 * 
	 * @param page
	 */
	private void getProfileMessageList(int page)
	{
		ProfileMessageManager.getProfileMessageList(page, new IonProfileMessageListCallback()
		{
			@Override
			public void onProfileMessageListSuccess(ProfileMessageListInfo messageListInfo)
			{
				displayMessageDatas(messageListInfo);
			}

			@Override
			public void onProfileMessageListFail(String errorMessage)
			{
				ToastUtil.showAlertToast(ProfileMessageActivity.this, errorMessage);
				if (mProgressLayout.getState() != State.CONTENT)
				{
					mProgressLayout.showErrorText();
				}
				else
				{
					if (mPullRefreshListView.isRefreshing())
					{
						mPullRefreshListView.stopPullRefresh();
					}
					if (mPullRefreshListView.isLoadingMore())
					{
						mPullRefreshListView.stopLoadMore();
					}
				}
			}
		});
	}

	/**
	 * 显示消息数据
	 */
	private void displayMessageDatas(ProfileMessageListInfo messageListInfo)
	{
		if (null != messageListInfo)
		{
			boolean isFinish = false;
			boolean isMore = false;
			if (messageListInfo.getCurrentPage() == ProfileMessageManager.FIRST_PAGE)
			{
				mMessageListInfo.clear();
			}
			else
			{
				isMore = true;
			}
			if (messageListInfo.getCount() <= messageListInfo.getCurrentPage())
			{
				isFinish = true;
			}
			mMessageListInfo.setCurrentPage(messageListInfo.getCurrentPage());
			mMessageListInfo.addAll(messageListInfo);
			mProfileMessageAdapter.resetDatas(mMessageListInfo.getMessageInfos());
			if (isMore)
			{
				mPullRefreshListView.stopLoadMore();
			}
			else
			{
				mPullRefreshListView.stopPullRefresh();
			}
			if (isFinish)
			{
				mPullRefreshListView.setLoadMoreEnable(false);
			}
			else
			{
				mPullRefreshListView.setLoadMoreEnable(true);
			}
			if (mProfileMessageAdapter.getCount() == 0)
			{
				mProgressLayout.showEmptyText();
			}
			else
			{
				mProgressLayout.showContent();
			}
		}
	}

	@Override
	public void onStartReadMessage(MessageInfo messageInfo)
	{
		if (null != messageInfo)
		{
			if (messageInfo.getRead() == MessageInfo.NO_READ)
			{
				readProfileMessage(messageInfo.getId(), ProfileMessageManager.SIGNAL_MESSAGE);
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
	{
		if (position >= 1)
		{
			if (null != mMessageListInfo && mMessageListInfo.getMessageInfos() != null && mMessageListInfo.getMessageInfos().size() > 0)
			{
				MessageInfo messageInfo = mMessageListInfo.getMessageInfos().get(position - 1);
				if (messageInfo != null)
				{
					if (messageInfo.getRead() == MessageInfo.NO_READ)
					{
						readProfileMessage(messageInfo.getId(), ProfileMessageManager.SIGNAL_MESSAGE);
					}
					Intent intent = new Intent(this, ProfileMessageDetailsActivity.class);
					intent.putExtra(ProfileMessageDetailsActivity.MESSAGE, messageInfo);
					startActivity(intent);
				}
			}
		}
	}

	/**
	 * 读取个人消息
	 */
	private void readProfileMessage(String id, int type)
	{
		ProfileMessageManager.readProfileMessage(type, id, new IonProfileReadMessageCallback()
		{
			@Override
			public void onProfileReadMessageSuccess(String id)
			{
				if (null != mMessageListInfo && null != mMessageListInfo.getMessageInfos())
				{
					if (TextUtils.isEmpty(id))
					{
						for (MessageInfo messageInfo : mMessageListInfo.getMessageInfos())
						{
							if (null != messageInfo && messageInfo.getRead() == MessageInfo.NO_READ)
							{
								messageInfo.setRead(MessageInfo.YET_READ);
							}
						}
					}
					else
					{
						for (MessageInfo messageInfo : mMessageListInfo.getMessageInfos())
						{
							if (null != messageInfo && messageInfo.getId().equals(id))
							{
								messageInfo.setRead(MessageInfo.YET_READ);
								break;
							}
						}
					}
					mProfileMessageAdapter.resetDatas(mMessageListInfo.getMessageInfos());
				}

			}

			@Override
			public void onProfileReadMessageFail(String errorMessage)
			{
				ToastUtil.showAlertToast(ProfileMessageActivity.this, errorMessage);
			}
		});
	}

	@Override
	public void clearRequestTask()
	{
		ProfileMessageManager.clearReadProfileMessageTask();
		ProfileMessageManager.clearGetProfileMessageListTask();
	}

	@Override
	public void onRetryLoadDatas()
	{
		getProfileMessageList(ProfileMessageManager.FIRST_PAGE);
	}
}
