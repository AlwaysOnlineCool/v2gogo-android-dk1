package com.v2gogo.project.activity.profile;

import android.view.View;
import android.widget.AbsListView;
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
import com.v2gogo.project.manager.home.PeopleConcernManager;
import com.v2gogo.project.manager.profile.ProfileCommentManager;
import com.v2gogo.project.manager.profile.ProfileCommentManager.IonProfileCommentDeleteCallback;
import com.v2gogo.project.manager.profile.ProfileCommentManager.IonProfileCommentListCallback;
import com.v2gogo.project.utils.common.ToastUtil;
import com.v2gogo.project.views.ProgressLayout;
import com.v2gogo.project.views.ProgressLayout.IonRetryLoadDatasCallback;
import com.v2gogo.project.views.ProgressLayout.State;
import com.v2gogo.project.views.dialog.ProfileActionSheetDialog;
import com.v2gogo.project.views.dialog.ProfileActionSheetDialog.ACTION;
import com.v2gogo.project.views.dialog.ProfileActionSheetDialog.IonActionSheetClickListener;
import com.v2gogo.project.views.expandablelistview.PullExpandableListview;
import com.v2gogo.project.views.listview.refreshview.OnLoadMoreListener;
import com.v2gogo.project.views.listview.refreshview.OnPullRefreshListener;

/**
 * 我的评论
 * 
 * @author houjun
 */
public class ProfileCommentActivity extends BaseActivity implements OnGroupClickListener, OnChildClickListener, IonActionSheetClickListener,
		IonCommandClickListener, OnPullRefreshListener, OnLoadMoreListener, IonRetryLoadDatasCallback
{
	private ProgressLayout mProgressLayout;
	private PullExpandableListview mExpandableListView;

	private HomeCommonListAdapter mCommonListAdapter;
	private CommentListInfo mCommentListInfo;

	private ProfileActionSheetDialog mProfileActionSheetDialog;
	private CommentInfo mCommentInfo;

	@Override
	public void onInitViews()
	{
		mProgressLayout = (ProgressLayout) findViewById(R.id.profile_comment_list_progress_layout);
		mExpandableListView = (PullExpandableListview) findViewById(R.id.profile_comment_list_expandable_listview);
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.profile_comment_activity_layout;
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
		mCommonListAdapter = new HomeCommonListAdapter(this, mExpandableListView,false);
		mCommonListAdapter.setOnCommandClickListener(this);
		mExpandableListView.setAdapter(mCommonListAdapter);
		loadProfileCommentList(CommentManager.FIRST_PAGE);
	}

	@Override
	protected void registerListener()
	{
		super.registerListener();
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
		if (mCommentListInfo != null && null != mCommentListInfo.getCommentInfos())
		{
			CommentInfo commentInfo = mCommentListInfo.getCommentInfos().get(position);
			if (commentInfo != null)
			{
				clickItem(commentInfo);
			}
		}
		return true;
	}

	@Override
	public boolean onChildClick(ExpandableListView arg0, View arg1, int position, int arg3, long arg4)
	{
		return onGroupClick(arg0, arg1, position, arg4);
	}

	@Override
	public void onClickListener(ACTION action, ProfileActionSheetDialog profileActionSheetDialog)
	{
		if (action == ACTION.FIRST_ITEM)
		{
			if (null != mCommentInfo && V2GogoApplication.getMasterLoginState())
			{
				deleteProfileComments();
			}
		}
	}

	private void clickItem(CommentInfo commentInfo)
	{
		mCommentInfo = commentInfo;
		if (null == mProfileActionSheetDialog)
		{
			mProfileActionSheetDialog = new ProfileActionSheetDialog(this, R.style.style_action_sheet_dialog);
			mProfileActionSheetDialog.setSheetClickListener(this);
		}
		if (!mProfileActionSheetDialog.isShowing())
		{
			mProfileActionSheetDialog.show();
			mProfileActionSheetDialog.setTips(getString(R.string.you_sure_delete_comment_tip), getString(R.string.delete_comment_tip), null);
		}
	}

	@Override
	public void onCommandClick(boolean isReplay,CommentInfo commentInfo)
	{
		if(!isReplay)
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
						commandComment(commentInfo);
					}
				}
				else
				{
					AccountLoginActivity.forwardAccountLogin(this);
				}
			}
		}
	}

	@Override
	public void onRetryLoadDatas()
	{
		loadProfileCommentList(ProfileCommentManager.FIRST_PAGE);
	}

	/**
	 * 删除用户评论
	 */
	private void deleteProfileComments()
	{
		ProfileCommentManager.deleteProfileCommentById(mCommentInfo, V2GogoApplication.getCurrentMatser().getUsername(), new IonProfileCommentDeleteCallback()
		{
			@Override
			public void onProfileCommentDeleteSuccess(CommentInfo commentInfo)
			{
				mCommentInfo = null;
				if (null != mCommentListInfo && null != mCommentListInfo.getCommentInfos())
				{
					mCommentListInfo.getCommentInfos().remove(commentInfo);
					mCommonListAdapter.resetDatas(mCommentListInfo.getCommentInfos());
				}
			}

			@Override
			public void onProfileCommentDeleteFail(String errorMessage)
			{
				mCommentInfo = null;
				ToastUtil.showAlertToast(getParent(), errorMessage);
			}
		});
	}

	/**
	 * 点击赞
	 * 
	 * @param commentInfo
	 */
	private void commandComment(CommentInfo commentInfo)
	{
		CommandManager.commandComment(commentInfo.getInfoId(), commentInfo.getId(), new IoncommandCommentCallback()
		{
			@Override
			public void oncommandCommentSuccess(String id)
			{
				if (null != mCommentListInfo && null != mCommentListInfo.getCommentInfos())
				{
					boolean isHave = false;
					for (CommentInfo commentInfo : mCommentListInfo.getCommentInfos())
					{
						if (null != commentInfo)
						{
							if (id.equals(commentInfo.getId()))
							{
								commentInfo.setPraised(true);
								commentInfo.setPraise(commentInfo.getPraise() + 1);
								isHave = true;
								break;
							}
						}
					}
					if (isHave)
					{
						mCommonListAdapter.resetDatas(mCommentListInfo.getCommentInfos());
					}
				}
			}

			@Override
			public void oncommandCommentFail(String errorMessage)
			{
				ToastUtil.showAlertToast(getParent(), errorMessage);
			}
		});
	}

	/**
	 * 拉取个人评论列表数据
	 * 
	 * @param page
	 */
	private void loadProfileCommentList(int page)
	{
		ProfileCommentManager.getProfileCommentList(page, new IonProfileCommentListCallback()
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

	@Override
	public void clearRequestTask()
	{
		ProfileCommentManager.clearDeleteProfileCommentById();
		ProfileCommentManager.clearGetProfileCommentList();
	}
}
