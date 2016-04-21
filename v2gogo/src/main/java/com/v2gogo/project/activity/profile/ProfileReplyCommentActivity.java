package com.v2gogo.project.activity.profile;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.LinearLayout;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.BaseActivity;
import com.v2gogo.project.activity.home.BaseDetailsctivity;
import com.v2gogo.project.activity.home.HomeArticleDetailsActivity;
import com.v2gogo.project.adapter.home.HomeCommonListAdapter;
import com.v2gogo.project.adapter.home.HomeCommonListAdapter.IonCommandClickListener;
import com.v2gogo.project.domain.home.CommentInfo;
import com.v2gogo.project.domain.home.CommentListInfo;
import com.v2gogo.project.manager.home.CommentManager;
import com.v2gogo.project.manager.home.CommentManager.IonNewCommentCallback;
import com.v2gogo.project.manager.home.PeopleConcernManager;
import com.v2gogo.project.manager.profile.ProfileCommentManager;
import com.v2gogo.project.manager.profile.ProfileCommentManager.IonProfileCommentListCallback;
import com.v2gogo.project.utils.common.KeyBoardUtil;
import com.v2gogo.project.utils.common.StringUtil;
import com.v2gogo.project.utils.common.ToastUtil;
import com.v2gogo.project.views.ProgressLayout;
import com.v2gogo.project.views.ProgressLayout.IonRetryLoadDatasCallback;
import com.v2gogo.project.views.ProgressLayout.State;
import com.v2gogo.project.views.edittext.LimitNumberEditText;
import com.v2gogo.project.views.expandablelistview.PullExpandableListview;
import com.v2gogo.project.views.listview.refreshview.OnLoadMoreListener;
import com.v2gogo.project.views.listview.refreshview.OnPullRefreshListener;

/**
 * 回复我的
 * 
 * @author houjun
 */
public class ProfileReplyCommentActivity extends BaseActivity implements OnGroupClickListener, OnChildClickListener, IonCommandClickListener,
		OnPullRefreshListener, OnLoadMoreListener, IonRetryLoadDatasCallback, OnClickListener
{
	private Button mBtnPublishComment;
	private ProgressLayout mProgressLayout;
	private LinearLayout mInputLinearLayout;
	private LimitNumberEditText mLimitNumberEditText;
	private PullExpandableListview mExpandableListView;

	private HomeCommonListAdapter mCommonListAdapter;
	private CommentListInfo mCommentListInfo;

	private CommentInfo mCommentInfo;

	@Override
	public void onInitViews()
	{
		mBtnPublishComment = (Button) findViewById(R.id.profile_comment_list_reply_publish_btn);
		mInputLinearLayout = (LinearLayout) findViewById(R.id.profile_comment_list_reply_input);
		mProgressLayout = (ProgressLayout) findViewById(R.id.profile_comment_reply_list_progress_layout);
		mLimitNumberEditText = (LimitNumberEditText) findViewById(R.id.profile_comment_list_reply_input_edit);
		mExpandableListView = (PullExpandableListview) findViewById(R.id.profile_comment_reply_list_expandable_listview);
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.profile_comment_activity_reply_layout;
	}

	@Override
	protected boolean isSetting()
	{
		return false;
	}

	@Override
	protected void onInitLoadDatas()
	{
		super.onInitLoadDatas();
		mProgressLayout.showProgress();
		mCommentListInfo = new CommentListInfo();
		mCommonListAdapter = new HomeCommonListAdapter(this, mExpandableListView, true);
		mCommonListAdapter.setOnCommandClickListener(this);
		mExpandableListView.setAdapter(mCommonListAdapter);
		loadProfileCommentList(CommentManager.FIRST_PAGE);
	}

	@Override
	protected void registerListener()
	{
		super.registerListener();
		mBtnPublishComment.setOnClickListener(this);
		mExpandableListView.setOnPullRefreshListener(this);
		mExpandableListView.setOnLoadMoreListener(this);
		mExpandableListView.setLoadMoreEnable(false);
		mExpandableListView.setOnGroupClickListener(this);
		mExpandableListView.setOnChildClickListener(this);
		mProgressLayout.setOnTryLoadDatasCallback(this);
	}

	@Override
	public void onPullDownRefresh(AbsListView pullRefreshView)
	{
		loadProfileCommentList(ProfileCommentManager.FIRST_PAGE);
	}

	@Override
	public void onLoadMore(AbsListView pullRefreshView)
	{
		loadProfileCommentList(mCommentListInfo.getCurrentPage() + 1);
	}

	@Override
	public boolean onGroupClick(ExpandableListView arg0, View arg1, int position, long arg3)
	{
		clickItem(position);
		return true;
	}

	@Override
	public boolean onChildClick(ExpandableListView arg0, View arg1, int position, int arg3, long arg4)
	{
		return onGroupClick(arg0, arg1, position, arg4);
	}

	@Override
	public void onCommandClick(boolean isReply, CommentInfo commentInfo)
	{
		if (isReply)
		{
			mCommentInfo = commentInfo;
			displayReplyOthersKeyboard(commentInfo);
		}
	}

	@Override
	public void onRetryLoadDatas()
	{
		loadProfileCommentList(ProfileCommentManager.FIRST_PAGE);
	}

	@Override
	protected void onPause()
	{
		KeyBoardUtil.closeKeybord(mLimitNumberEditText, this);
		super.onPause();
	}

	@Override
	public void clearRequestTask()
	{
		ProfileCommentManager.clearGetProfileCommentList();
	}

	@Override
	public void onClick(View view)
	{
		commentReplyInfo();
	}

	/**
	 * 点击item
	 * 
	 * @param position
	 */
	private void clickItem(int position)
	{
		if (null != mCommonListAdapter.getCommentInfos() && position <= mCommonListAdapter.getGroupCount()
				&& null != mCommonListAdapter.getCommentInfos().get(position))
		{
			CommentInfo commentInfo = mCommonListAdapter.getCommentInfos().get(position);
			if (commentInfo != null)
			{
				Intent intent = new Intent(this, HomeArticleDetailsActivity.class);
				intent.putExtra(BaseDetailsctivity.ID, commentInfo.getInfoId());
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		}
	}

	/**
	 * 回复评论
	 */
	private void commentReplyInfo()
	{
		if (null != mCommentInfo)
		{
			String content = mLimitNumberEditText.getText().toString();
			if (TextUtils.isEmpty(content))
			{
				ToastUtil.showAlertToast(getParent(), R.string.comment_content_no_empty_tip);
			}
			else
			{
				content = content.trim();
				String srcCount = "";
				mBtnPublishComment.setEnabled(false);
				content = StringUtil.replaceBlank(content);
				KeyBoardUtil.closeKeybord(mLimitNumberEditText, this);
				if (TextUtils.isEmpty(mCommentInfo.getSrccont()))
				{
					srcCount = mCommentInfo.getUsername() + ":" + mCommentInfo.getContent();
				}
				else
				{
					srcCount = mCommentInfo.getSrccont() + ";" + mCommentInfo.getUsername() + ":" + mCommentInfo.getContent();
				}
				IonNewCommentCallback callback = new IonNewCommentCallback()
				{
					@Override
					public void onNewCommentSuccess(CommentInfo commentInfo)
					{
						mCommentInfo = null;
						mBtnPublishComment.setEnabled(true);
						mInputLinearLayout.setVisibility(View.GONE);
						ToastUtil.showConfirmToast(getParent(), R.string.publish_comment_success);
					}

					@Override
					public void onNewCommentFail(String errorMessage)
					{
						mCommentInfo = null;
						mBtnPublishComment.setEnabled(true);
						mInputLinearLayout.setVisibility(View.GONE);
						ToastUtil.showAlertToast(getParent(), errorMessage);
					}
				};
				CommentManager.publishNewCommentWithNoImage(mCommentInfo.getId(), mCommentInfo.getInfoId(), CommentInfo.COMMENT_COMMEMT,
						CommentInfo.SRC_ARTICE_TYPE, content, srcCount, callback);
			}
		}
	}

	/**
	 * 显示回复别人的键盘
	 * 
	 * @param commentInfo
	 */
	private void displayReplyOthersKeyboard(CommentInfo commentInfo)
	{
		mInputLinearLayout.setVisibility(View.VISIBLE);
		String str = getString(R.string.comment_reply_tip);
		str = String.format(str, commentInfo.getUsername());
		mLimitNumberEditText.setHint(str);
		KeyBoardUtil.openKeybord(mLimitNumberEditText, this);
	}

	/**
	 * 拉取个人评论列表数据
	 * 
	 * @param page
	 */
	private void loadProfileCommentList(int page)
	{
		ProfileCommentManager.getReplyProfileCommentList(page, new IonProfileCommentListCallback()
		{
			@Override
			public void onProfileCommentListSuccess(CommentListInfo commentListInfo)
			{
				if (null != commentListInfo)
				{
					displayCommentDatas(commentListInfo);
				}
			}

			@Override
			public void onProfileCommentListFail(String errorMessage)
			{
				ToastUtil.showAlertToast(getParent(), errorMessage);
				if (mProgressLayout.getState() != State.CONTENT)
				{
					mProgressLayout.showErrorText();
				}
				else
				{
					if (mExpandableListView.isRefreshing())
					{
						mExpandableListView.stopPullRefresh();
					}
					if (mExpandableListView.isLoadingMore())
					{
						mExpandableListView.stopLoadMore();
					}
				}
			}
		});
	}

	/**
	 * 显示评论数据
	 * 
	 * @param commentListInfo
	 */
	private void displayCommentDatas(CommentListInfo tempCommentListInfo)
	{
		boolean isFinish = false;
		boolean isMore = false;
		if (tempCommentListInfo.getCurrentPage() == PeopleConcernManager.FIRST_PAGE)
		{
			mCommentListInfo.clear();
		}
		else
		{
			isMore = true;
		}
		if (tempCommentListInfo.getCount() <= tempCommentListInfo.getCurrentPage())
		{
			isFinish = true;
		}
		mCommentListInfo.setCurrentPage(tempCommentListInfo.getCurrentPage());
		mCommentListInfo.addAll(tempCommentListInfo);
		mCommonListAdapter.resetDatas(mCommentListInfo.getCommentInfos());
		if (isMore)
		{
			mExpandableListView.stopLoadMore();
		}
		else
		{
			mExpandableListView.stopPullRefresh();
		}
		if (isFinish)
		{
			mExpandableListView.setLoadMoreEnable(false);
		}
		else
		{
			mExpandableListView.setLoadMoreEnable(true);
		}
		if (mCommonListAdapter.getGroupCount() == 0)
		{
			mProgressLayout.showEmptyText();
		}
		else
		{
			mProgressLayout.showContent();
		}
	}
}
