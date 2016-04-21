package com.v2gogo.project.activity.home;

import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.BaseActivity;
import com.v2gogo.project.activity.account.AccountLoginActivity;
import com.v2gogo.project.adapter.home.HomeCommonListAdapter;
import com.v2gogo.project.adapter.home.HomeCommonListAdapter.IonCommandClickListener;
import com.v2gogo.project.domain.home.CommentInfo;
import com.v2gogo.project.domain.home.CommentListInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.manager.home.CommandManager;
import com.v2gogo.project.manager.home.CommandManager.IoncommandCommentCallback;
import com.v2gogo.project.manager.home.CommentManager;
import com.v2gogo.project.manager.home.CommentManager.IonCommentListCallback;
import com.v2gogo.project.manager.home.CommentManager.IonNewCommentCallback;
import com.v2gogo.project.manager.home.PeopleConcernManager;
import com.v2gogo.project.manager.profile.ProfileCommentManager;
import com.v2gogo.project.manager.profile.ProfileCommentManager.IonProfileCommentDeleteCallback;
import com.v2gogo.project.utils.common.KeyBoardUtil;
import com.v2gogo.project.utils.common.StringUtil;
import com.v2gogo.project.utils.common.ToastUtil;
import com.v2gogo.project.views.ProgressLayout;
import com.v2gogo.project.views.ProgressLayout.IonRetryLoadDatasCallback;
import com.v2gogo.project.views.ProgressLayout.State;
import com.v2gogo.project.views.dialog.ProfileActionSheetDialog;
import com.v2gogo.project.views.dialog.ProfileActionSheetDialog.ACTION;
import com.v2gogo.project.views.dialog.ProfileActionSheetDialog.IonActionSheetClickListener;
import com.v2gogo.project.views.edittext.LimitNumberEditText;
import com.v2gogo.project.views.expandablelistview.PullExpandableListview;
import com.v2gogo.project.views.listview.refreshview.OnLoadMoreListener;
import com.v2gogo.project.views.listview.refreshview.OnPullRefreshListener;

public abstract class BaseCommentListActivity extends BaseActivity implements OnGroupClickListener, OnTouchListener, OnChildClickListener, OnClickListener,
		IonCommandClickListener, OnPullRefreshListener, OnLoadMoreListener, IonActionSheetClickListener, IonRetryLoadDatasCallback
{

	private Button mBtnPublishComment;
	private ProgressLayout mProgressLayout;
	private LimitNumberEditText mLimitNumberEditText;
	private PullExpandableListview mExpandableListView;
	private HomeCommonListAdapter mCommonListAdapter;

	private ProfileActionSheetDialog mActionSheetDialog;

	protected CommentListInfo mCommentListInfo;
	private CommentInfo mClickCommentInfo;

	protected int mSrcType = 0;
	private String mArticeId = null;

	public abstract String getCommentListType();

	@Override
	public void onInitViews()
	{
		mBtnPublishComment = (Button) findViewById(R.id.base_comment_list_publish_btn);
		mLimitNumberEditText = (LimitNumberEditText) findViewById(R.id.base_comment_list_input);
		mLimitNumberEditText.requestFocus();
		mProgressLayout = (ProgressLayout) findViewById(R.id.base_comment_list_progress_layout);
		mExpandableListView = (PullExpandableListview) findViewById(R.id.base_comment_list_expandable_listview);
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.base_comment_list_activity_layout;
	}

	@Override
	protected void onInitLoadDatas()
	{
		super.onInitLoadDatas();
		mProgressLayout.showProgress();
		mCommentListInfo = new CommentListInfo();
		mSrcType = ((HomeCommentListActivity) getParent()).mSrcType;
		mArticeId = ((HomeCommentListActivity) getParent()).mArticeId;
		mCommonListAdapter = new HomeCommonListAdapter(this, mExpandableListView, false);
		mCommonListAdapter.setOnCommandClickListener(this);
		mExpandableListView.setAdapter(mCommonListAdapter);
		if (CommentManager.HOT_COMMENT_LIST_KEYWORD.equals(getCommentListType()))
		{
			loadHotestComments(CommentManager.FIRST_PAGE);
		}
		else if (CommentManager.NEWEST_COMMENT_LIST_KEYWORD.equals(getCommentListType()))
		{
			loadNewestComments(CommentManager.FIRST_PAGE);
		}
	}

	@Override
	protected void registerListener()
	{
		super.registerListener();
		mProgressLayout.setOnTryLoadDatasCallback(this);
		mExpandableListView.setOnPullRefreshListener(this);
		mExpandableListView.setOnLoadMoreListener(this);
		mExpandableListView.setLoadMoreEnable(false);
		mExpandableListView.setOnGroupClickListener(this);
		mExpandableListView.setOnChildClickListener(this);
		mBtnPublishComment.setOnClickListener(this);
	}

	@Override
	protected boolean isSetting()
	{
		return false;
	}

	@Override
	public void onPullDownRefresh(AbsListView pullRefreshView)
	{
		if (CommentManager.HOT_COMMENT_LIST_KEYWORD.equals(getCommentListType()))
		{
			loadHotestComments(CommentManager.FIRST_PAGE);
		}
		else if (CommentManager.NEWEST_COMMENT_LIST_KEYWORD.equals(getCommentListType()))
		{
			loadNewestComments(CommentManager.FIRST_PAGE);
		}
	}

	@Override
	public void onLoadMore(AbsListView pullRefreshView)
	{
		if (CommentManager.HOT_COMMENT_LIST_KEYWORD.equals(getCommentListType()))
		{
			loadHotestComments(mCommentListInfo.getCurrentPage() + 1);
		}
		else if (CommentManager.NEWEST_COMMENT_LIST_KEYWORD.equals(getCommentListType()))
		{
			loadNewestComments(mCommentListInfo.getCurrentPage() + 1);
		}
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		KeyBoardUtil.closeKeybord(mLimitNumberEditText, this);
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
	public void onRetryLoadDatas()
	{
		if (CommentManager.HOT_COMMENT_LIST_KEYWORD.equals(getCommentListType()))
		{
			loadHotestComments(CommentManager.FIRST_PAGE);
		}
		else if (CommentManager.NEWEST_COMMENT_LIST_KEYWORD.equals(getCommentListType()))
		{
			loadNewestComments(CommentManager.FIRST_PAGE);
		}
	}

	@Override
	public boolean onTouch(View view, MotionEvent event)
	{
		switch (event.getAction())
		{
			case MotionEvent.ACTION_DOWN:

				break;
			case MotionEvent.ACTION_MOVE:

				break;
			case MotionEvent.ACTION_UP:

				break;
		}
		return false;
	}

	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.base_comment_list_publish_btn:// 发布
				publishCommentOrReply();
				break;
		}
	}

	@Override
	public void onCommandClick(boolean isReply, CommentInfo commentInfo)
	{
		if (!isReply)
		{
			startCommendComment(commentInfo);
		}
	}

	@Override
	public void onClickListener(ACTION action, ProfileActionSheetDialog profileActionSheetDialog)
	{
		if (action == ACTION.FIRST_ITEM)
		{
			deleteUserComment();
		}
	}

	@Override
	protected void onPressBack()
	{
		super.onPressBack();
		CommentManager.clearNewCommentTask();
		CommentManager.clearGetCommentListTask();
		KeyBoardUtil.closeKeybord(mLimitNumberEditText, this);
	}

	@Override
	public void clearRequestTask()
	{
		CommentManager.clearNewCommentTask();
		CommentManager.clearGetCommentListTask();
		KeyBoardUtil.closeKeybord(mLimitNumberEditText, this);
	}

	/**
	 * 赞评论
	 * 
	 * @param commentInfo
	 */
	private void startCommendComment(CommentInfo commentInfo)
	{
		if (null != commentInfo)
		{
			if (V2GogoApplication.getMasterLoginState())
			{
				if (commentInfo.isPraised())
				{
					ToastUtil.showAlertToast(getParent(), R.string.nin_yet_command_tip);
				}
				else
				{
					commentInfo.setPraised(true);
					commentInfo.setPraise(commentInfo.getPraise() + 1);
					mCommonListAdapter.resetDatas(mCommentListInfo.getCommentInfos());
					CommandManager.commandComment(commentInfo.getInfoId(), commentInfo.getId(), new IoncommandCommentCallback()
					{
						@Override
						public void oncommandCommentSuccess(String id)
						{
						}

						@Override
						public void oncommandCommentFail(String errorMessage)
						{
							ToastUtil.showAlertToast(getParent(), errorMessage);
						}
					});
				}
			}
			else
			{
				AccountLoginActivity.forwardAccountLogin(this);
			}
		}
	}

	/**
	 * 处理删除评论成功
	 * 
	 * @param id
	 */
	private void dealWithDeleteCommentsSuccess(CommentInfo commentInfo)
	{
		if (null != mCommentListInfo && null != mCommentListInfo.getCommentInfos() && commentInfo != null)
		{
			((HomeCommentListActivity) getParent()).mDeleteCommentInfos.add(commentInfo.getId());
			mCommentListInfo.getCommentInfos().remove(commentInfo);
			mCommonListAdapter.resetDatas(mCommentListInfo.getCommentInfos());
		}
		mClickCommentInfo = null;
	}

	/**
	 * 发起评论或发起回复
	 */
	private void publishCommentOrReply()
	{
		if (!V2GogoApplication.getMasterLoginState())
		{
			AccountLoginActivity.forwardAccountLogin(this);
		}
		else
		{
			if (mClickCommentInfo == null)
			{
				publishComment();
			}
			else
			{
				commentReplyInfo();
			}
			mLimitNumberEditText.setText("");
			mLimitNumberEditText.setHint(R.string.comment_list_input_tip);
		}
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
				mClickCommentInfo = commentInfo;
				String userId = null;
				if (V2GogoApplication.getMasterLoginState())
				{
					userId = V2GogoApplication.getCurrentMatser().getUserid();
				}
				if (commentInfo.getUserid().equals(userId))
				{
					closeKeyboard(false);
					deleteUserSelfComments();
				}
				else
				{
					displayReplyOthersKeyboard(commentInfo);
				}
			}
		}
	}

	/**
	 * 发起删除评论的请求
	 */
	private void deleteUserComment()
	{
		if (null != mClickCommentInfo)
		{
			if (V2GogoApplication.getMasterLoginState())
			{
				ProfileCommentManager.deleteProfileCommentById(mClickCommentInfo, V2GogoApplication.getCurrentMatser().getUsername(),
						new IonProfileCommentDeleteCallback()
						{

							@Override
							public void onProfileCommentDeleteSuccess(CommentInfo commentInfo)
							{
								dealWithDeleteCommentsSuccess(commentInfo);
							}

							@Override
							public void onProfileCommentDeleteFail(String errorMessage)
							{
								mClickCommentInfo = null;
								ToastUtil.showAlertToast(getParent(), errorMessage);
							}
						});
			}
			else
			{
				AccountLoginActivity.forwardAccountLogin(this);
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
		String str = getString(R.string.comment_reply_tip);
		str = String.format(str, commentInfo.getUsername());
		mLimitNumberEditText.setHint(str);
		KeyBoardUtil.openKeybord(mLimitNumberEditText, this);
	}

	/**
	 * 删除用户自己的评论
	 */
	private void deleteUserSelfComments()
	{
		if (null == mActionSheetDialog)
		{
			mActionSheetDialog = new ProfileActionSheetDialog(this, R.style.style_action_sheet_dialog);
			mActionSheetDialog.setSheetClickListener(this);
		}
		if (!mActionSheetDialog.isShowing())
		{
			mActionSheetDialog.show();
			mActionSheetDialog.setTips(getString(R.string.you_sure_delete_comment_tip), getString(R.string.delete_comment_tip), null);
		}
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
		if (null != mExpandableListView)
		{
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
		}
	}

	/**
	 * 直接评论
	 */
	private void publishComment()
	{
		String content = mLimitNumberEditText.getText().toString();
		if (TextUtils.isEmpty(content))
		{
			ToastUtil.showAlertToast(getParent(), R.string.comment_content_no_empty_tip);
			return;
		}
		else
		{
			content = content.trim();
			mBtnPublishComment.setEnabled(false);
			content = StringUtil.replaceBlank(content);
			KeyBoardUtil.closeKeybord(mLimitNumberEditText, this);
			IonNewCommentCallback callback = new IonNewCommentCallback()
			{
				@Override
				public void onNewCommentSuccess(CommentInfo commentInfo)
				{
					mClickCommentInfo = null;
					mBtnPublishComment.setEnabled(true);
					dealNewCommentSuccess(commentInfo);
				}

				@Override
				public void onNewCommentFail(String errorMessage)
				{
					mClickCommentInfo = null;
					mBtnPublishComment.setEnabled(true);
					ToastUtil.showAlertToast(getParent(), errorMessage);
				}
			};
			if (mSrcType == CommentInfo.SRC_ARTICE_TYPE)
			{
				CommentManager.publishNewCommentWithNoImage("", mArticeId, CommentInfo.COMMENT_ARTICE, CommentInfo.SRC_ARTICE_TYPE, content, "", callback);
			}
			else if (mSrcType == CommentInfo.SRC_VIDEO_TYPE)
			{
				CommentManager.publishNewCommentWithNoImage("", mArticeId, CommentInfo.COMMENT_VEDIO, CommentInfo.SRC_VIDEO_TYPE, content, "", callback);
			}
		}
	}

	/**
	 * 回复评论
	 */
	private void commentReplyInfo()
	{
		if (null != mClickCommentInfo)
		{
			String content = mLimitNumberEditText.getText().toString();
			if (TextUtils.isEmpty(content))
			{
				ToastUtil.showAlertToast(getParent(), R.string.comment_content_no_empty_tip);
			}
			else
			{
				content = content.trim();
				closeKeyboard(false);
				String srcCount = "";
				mBtnPublishComment.setEnabled(false);
				content = StringUtil.replaceBlank(content);
				if (TextUtils.isEmpty(mClickCommentInfo.getSrccont()))
				{
					srcCount = mClickCommentInfo.getUsername() + ":" + mClickCommentInfo.getContent();
				}
				else
				{
					srcCount = mClickCommentInfo.getSrccont() + ";" + mClickCommentInfo.getUsername() + ":" + mClickCommentInfo.getContent();
				}
				IonNewCommentCallback callback = new IonNewCommentCallback()
				{
					@Override
					public void onNewCommentSuccess(CommentInfo commentInfo)
					{
						mClickCommentInfo = null;
						mBtnPublishComment.setEnabled(true);
						dealNewCommentSuccess(commentInfo);
					}

					@Override
					public void onNewCommentFail(String errorMessage)
					{
						mClickCommentInfo = null;
						mBtnPublishComment.setEnabled(true);
						ToastUtil.showAlertToast(getParent(), errorMessage);
					}
				};
				if (mSrcType == CommentInfo.SRC_VIDEO_TYPE)
				{
					CommentManager.publishNewCommentWithNoImage(mClickCommentInfo.getId(), mArticeId, CommentInfo.COMMENT_COMMEMT, CommentInfo.SRC_VIDEO_TYPE,
							content, srcCount, callback);
				}
				else
				{
					CommentManager.publishNewCommentWithNoImage(mClickCommentInfo.getId(), mArticeId, CommentInfo.COMMENT_COMMEMT, CommentInfo.SRC_ARTICE_TYPE,
							content, srcCount, callback);
				}
			}
		}
	}

	/**
	 * 评论成功后的处理
	 * 
	 * @param commentInfo
	 */
	private void dealNewCommentSuccess(CommentInfo commentInfo)
	{
		ToastUtil.showConfirmToast(getParent(), R.string.publish_comment_success);
		if (V2GogoApplication.getMasterLoginState())
		{
			commentInfo.setAvatar(V2GogoApplication.getCurrentMatser().getThumbialAvatar());
		}
		if (!TextUtils.isEmpty(commentInfo.getSrccont()))
		{
			commentInfo.parseCommentReplyData();
		}
		((HomeCommentListActivity) getParent()).mAddCommentInfos.add(0, commentInfo);
		if (CommentManager.NEWEST_COMMENT_LIST_KEYWORD.equals(getCommentListType()))
		{
			mCommentListInfo.addTop(commentInfo);
			mCommonListAdapter.resetDatas(mCommentListInfo.getCommentInfos());
		}
	}

	/**
	 * 关闭键盘
	 */
	private void closeKeyboard(boolean isClear)
	{
		if (null != mClickCommentInfo)
		{
			if (isClear)
			{
				mClickCommentInfo = null;
			}
		}
		KeyBoardUtil.closeKeybord(mLimitNumberEditText, this);
		mLimitNumberEditText.setText("");
		mLimitNumberEditText.setHint(R.string.comment_list_input_tip);
	}

	/**
	 * 加载最新评论
	 * 
	 * @param page
	 */
	private void loadNewestComments(int page)
	{
		CommentManager.getNewestCommentList(mArticeId, page, new IonCommentListCallback()
		{
			@Override
			public void onCommentListSuccess(CommentListInfo commentListInfo)
			{
				mProgressLayout.showContent();
				if (commentListInfo != null)
				{
					displayCommentDatas(commentListInfo);
				}
			}

			@Override
			public void onCommentListFail(String errorMessage)
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
	 * 加载最热评论
	 * 
	 * @param page
	 */
	private void loadHotestComments(int page)
	{
		CommentManager.getHotCommentList(mArticeId, page, new IonCommentListCallback()
		{
			@Override
			public void onCommentListSuccess(CommentListInfo commentListInfo)
			{
				mProgressLayout.showContent();
				if (commentListInfo != null)
				{
					displayCommentDatas(commentListInfo);
				}
			}

			@Override
			public void onCommentListFail(String errorMessage)
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

}
