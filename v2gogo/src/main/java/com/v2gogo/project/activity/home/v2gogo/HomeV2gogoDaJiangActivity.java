package com.v2gogo.project.activity.home.v2gogo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import cn.sharesdk.framework.ShareSDK;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.BaseActivity;
import com.v2gogo.project.activity.account.AccountLoginActivity;
import com.v2gogo.project.activity.home.HomeArticleDetailsActivity;
import com.v2gogo.project.activity.home.HomeCommentActivity;
import com.v2gogo.project.adapter.home.HomeCommonListAdapter;
import com.v2gogo.project.adapter.home.HomeCommonListAdapter.IonCommandClickListener;
import com.v2gogo.project.domain.home.CommentInfo;
import com.v2gogo.project.domain.home.CommentListInfo;
import com.v2gogo.project.domain.home.InteractionInfo;
import com.v2gogo.project.domain.home.VoteOptionInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.manager.home.CommandManager;
import com.v2gogo.project.manager.home.CommandManager.IoncommandCommentCallback;
import com.v2gogo.project.manager.home.CommentManager;
import com.v2gogo.project.manager.home.CommentManager.IonCommentListCallback;
import com.v2gogo.project.manager.home.CommentManager.IonNewCommentCallback;
import com.v2gogo.project.manager.home.v2gogo.InteractionManager;
import com.v2gogo.project.manager.home.v2gogo.InteractionManager.IonMainInteractionDatasCallback;
import com.v2gogo.project.manager.home.v2gogo.InteractionManager.IonVoteInteractionCallback;
import com.v2gogo.project.manager.profile.ProfileCollectionsManager;
import com.v2gogo.project.manager.profile.ProfileCollectionsManager.IonAddCollectionsCallback;
import com.v2gogo.project.manager.profile.ProfileCommentManager;
import com.v2gogo.project.manager.profile.ProfileCommentManager.IonProfileCommentDeleteCallback;
import com.v2gogo.project.utils.common.KeyBoardUtil;
import com.v2gogo.project.utils.common.SPUtil;
import com.v2gogo.project.utils.common.ScreenUtil;
import com.v2gogo.project.utils.common.StringUtil;
import com.v2gogo.project.utils.common.ToastUtil;
import com.v2gogo.project.utils.share.CustomPlatformActionListener;
import com.v2gogo.project.utils.share.ShareUtils;
import com.v2gogo.project.views.ProgressLayout;
import com.v2gogo.project.views.dialog.ProfileActionSheetDialog;
import com.v2gogo.project.views.dialog.ProfileActionSheetDialog.ACTION;
import com.v2gogo.project.views.dialog.ProfileActionSheetDialog.IonActionSheetClickListener;
import com.v2gogo.project.views.dialog.V2gogoShareDialog;
import com.v2gogo.project.views.dialog.V2gogoShareDialog.IonItemClickCallback;
import com.v2gogo.project.views.dialog.V2gogoShareDialog.SHARE_TYPE;
import com.v2gogo.project.views.dialog.VoteActionSheetDialog;
import com.v2gogo.project.views.dialog.VoteActionSheetDialog.IonVoteActionSheetCallback;
import com.v2gogo.project.views.edittext.LimitNumberEditText;
import com.v2gogo.project.views.expandablelistview.PullExpandableListview;
import com.v2gogo.project.views.listview.refreshview.OnLoadMoreListener;
import com.v2gogo.project.views.listview.refreshview.OnPullRefreshListener;
import com.v2gogo.project.views.logic.VoteOptionDescriptionLayout;
import com.v2gogo.project.views.logic.VoteOptionDescriptionLayout.IonPageLoadFinished;
import com.v2gogo.project.views.logic.VoteOptionNumberLayout;

public class HomeV2gogoDaJiangActivity extends BaseActivity implements OnCheckedChangeListener, OnClickListener, IonVoteActionSheetCallback,
		IonPageLoadFinished, OnPullRefreshListener, IonItemClickCallback, OnLoadMoreListener, IonCommandClickListener, OnGroupClickListener,
		OnChildClickListener, IonActionSheetClickListener
{

	private final int COMMENT_REQUEST_CODE = 0x12;

	private final int NEWEST_POSITION = 0;
	private final int HOT_POSITION = 1;
	private int mCurrentPosition = 0;

	private ProgressLayout mProgressLayout;
	private PullExpandableListview mPullRefreshListView;
	private HomeCommonListAdapter mHomeCommonListAdapter;

	private ImageButton mBtnCollections;
	private Button mBtnComment;
	private Button mBtnVote;
	private ImageButton mShare;
	private ImageView mLine;
	private TextView mTextNotice;
	private RadioGroup mRadioGroup;
	private FrameLayout mCommentLayout;

	private LinearLayout mInputLayout;
	private Button mBtnPublishComments;
	private LinearLayout mCommonLayout;
	private LimitNumberEditText mLimitNumberEditText;

	private CommentInfo mClickCommentInfo;
	private CommentListInfo mHotCommentListInfo;
	private CommentListInfo mNewestCommentListInfo;

	private V2gogoShareDialog mV2gogoShareDialog;
	private ProfileActionSheetDialog mActionSheetDialog;
	private VoteActionSheetDialog mVoteActionSheetDialog;
	private VoteOptionNumberLayout mVoteOptionNumberLayout;
	private VoteOptionDescriptionLayout mVoteOptionDescriptionLayout;
	private InteractionInfo mInteractionInfo;
	private float mHalfScreenWidth;

	@Override
	public void onInitViews()
	{
		mShare = (ImageButton) findViewById(R.id.activity_v2gogo_interaction_layout_share);
		mBtnVote = (Button) findViewById(R.id.activity_v2gogo_interaction_layout_vote);
		mInputLayout = (LinearLayout) findViewById(R.id.activity_v2gogo_interaction_input_layout);
		mBtnPublishComments = (Button) findViewById(R.id.activity_v2gogo_interaction_publish_btn);
		mCommonLayout = (LinearLayout) findViewById(R.id.activity_v2gogo_interaction_common_layout);
		mBtnComment = (Button) findViewById(R.id.activity_v2gogo_interaction_layout_comment);
		mProgressLayout = (ProgressLayout) findViewById(R.id.activity_v2gogo_interaction_progress_layout);
		mLimitNumberEditText = (LimitNumberEditText) findViewById(R.id.activity_v2gogo_interaction_input);
		mBtnCollections = (ImageButton) findViewById(R.id.activity_v2gogo_interaction_layout_collections);
		mPullRefreshListView = (PullExpandableListview) findViewById(R.id.activity_v2gogo_interaction_pull_to_refresh_listview);
		initHeaderView();
		setAdapter();
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.home_interaction_activity_layout;
	}

	@Override
	protected void onInitLoadDatas()
	{
		super.onInitLoadDatas();
		ShareSDK.initSDK(this);
		mProgressLayout.showProgress();
		mHotCommentListInfo = new CommentListInfo();
		mNewestCommentListInfo = new CommentListInfo();
		loadMainInteraction();
	}

	@Override
	protected void registerListener()
	{
		super.registerListener();
		mShare.setOnClickListener(this);
		mBtnVote.setOnClickListener(this);
		mBtnComment.setOnClickListener(this);
		mBtnCollections.setOnClickListener(this);
		mRadioGroup.setOnCheckedChangeListener(this);
		mBtnPublishComments.setOnClickListener(this);
		mVoteOptionDescriptionLayout.setOnPageLoadFinished(this);
		mPullRefreshListView.setOnPullRefreshListener(this);
		mPullRefreshListView.setOnGroupClickListener(this);
		mPullRefreshListView.setOnChildClickListener(this);
		mPullRefreshListView.setOnLoadMoreListener(this);
		mPullRefreshListView.setLoadMoreEnable(false);
	}

	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.activity_v2gogo_interaction_layout_vote:
				clickVote();
				break;

			case R.id.activity_v2gogo_interaction_layout_comment:
				clickComment();
				break;

			case R.id.activity_v2gogo_interaction_layout_share:
				showShareDialog();
				break;

			case R.id.activity_v2gogo_interaction_publish_btn:
				commentReplyInfo();
				break;

			case R.id.activity_v2gogo_interaction_layout_collections:
				addCollections();
				break;

			default:
				break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int checkId)
	{
		Animation animation = null;
		switch (checkId)
		{
			case R.id.home_interaction_activity_header_hotest_rb:
				if (mCurrentPosition == NEWEST_POSITION)
				{
					animation = new TranslateAnimation(0, mHalfScreenWidth, 0, 0);
				}
				mCurrentPosition = HOT_POSITION;
				break;

			case R.id.home_interaction_activity_header_newest_rb:
				if (mCurrentPosition == HOT_POSITION)
				{
					animation = new TranslateAnimation(mHalfScreenWidth, 0, 0, 0);
				}
				mCurrentPosition = NEWEST_POSITION;
				break;

			default:
				break;
		}
		if (null != animation)
		{
			animation.setDuration(250);
			animation.setInterpolator(new AccelerateInterpolator());
			animation.setFillEnabled(true);
			animation.setFillAfter(true);
			mLine.startAnimation(animation);
		}
		if (mCurrentPosition == NEWEST_POSITION)
		{
			if (null != mInteractionInfo)
			{
				loadNewestComments(CommentManager.FIRST_PAGE);
			}
		}
		else
		{
			if (null != mInteractionInfo)
			{
				loadHotestComments(CommentManager.FIRST_PAGE);
			}
		}
	}

	@Override
	public void onPageLoadFinished()
	{
		if (null != mInteractionInfo && mInteractionInfo.getStatus() != InteractionInfo.STATUS_PREPARE)
		{
			if (mCurrentPosition == NEWEST_POSITION)
			{
				if (null != mInteractionInfo)
				{
					loadNewestComments(CommentManager.FIRST_PAGE);
				}
			}
			else
			{
				if (null != mInteractionInfo)
				{
					loadHotestComments(CommentManager.FIRST_PAGE);
				}
			}
		}
	}

	@Override
	public void onVoteActionSheetCallback(VoteOptionInfo voteOptionInfo)
	{
		if (V2GogoApplication.getMasterLoginState())
		{
			int leftCoin = V2GogoApplication.getCurrentMatser().getCoin();
			if (null != mInteractionInfo)
			{
				if (leftCoin < mInteractionInfo.getVoteCoin())
				{
					ToastUtil.showAlertToast(this, R.string.you_have_en_coin);
					return;
				}
				if (null == voteOptionInfo)
				{
					ToastUtil.showAlertToast(this, R.string.you_selcted_option_tip);
					return;
				}
				vote(voteOptionInfo);
			}
		}
	}

	/**
	 * 投票
	 * 
	 * @param voteOptionInfo
	 */
	private void vote(VoteOptionInfo voteOptionInfo)
	{
		InteractionManager.voteInteractionById(voteOptionInfo.getSrcId(), voteOptionInfo.getId(), V2GogoApplication.getCurrentMatser().getUsername(), 1,
				new IonVoteInteractionCallback()
				{
					@Override
					public void onVoteInteractionSuccess(int coin, String optionId, int number)
					{
						ToastUtil.showConfirmToast(HomeV2gogoDaJiangActivity.this, R.string.vote_success_tip);
						if (mInteractionInfo != null)
						{
							mInteractionInfo.setYetVoted(true);
						}
						dealVoteSuccess(optionId, number);
					}

					@Override
					public void onVoteInteractionFail(String errorMessage)
					{
						ToastUtil.showAlertToast(HomeV2gogoDaJiangActivity.this, errorMessage);
					}
				});
	}

	/**
	 * 拉取数据
	 */
	private void loadMainInteraction()
	{
		InteractionManager.getMainInteractionDatas(new IonMainInteractionDatasCallback()
		{
			@Override
			public void onMainInteractionDatasSuccess(InteractionInfo interactionInfo)
			{
				mProgressLayout.showContent();
				dealWithInteractionDatas(interactionInfo);
			}

			@Override
			public void onMainInteractionDatasFail(String errorMessage)
			{
				mProgressLayout.showContent();
				ToastUtil.showAlertToast(HomeV2gogoDaJiangActivity.this, errorMessage);
				dealRefreahListview();
			}
		});
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		mVoteOptionDescriptionLayout.onResume();
	}

	@Override
	protected void onPause()
	{
		KeyBoardUtil.closeKeybord(mLimitNumberEditText, this);
		mVoteOptionDescriptionLayout.onPause();
		super.onPause();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == COMMENT_REQUEST_CODE && resultCode == RESULT_OK && null != data)
		{
			CommentInfo commentInfo = (CommentInfo) data.getSerializableExtra(HomeArticleDetailsActivity.COMMENT);
			if (null != commentInfo)
			{
				if (mCurrentPosition == NEWEST_POSITION)
				{
					mNewestCommentListInfo.addTop(commentInfo);
					mHomeCommonListAdapter.resetDatas(mNewestCommentListInfo.getCommentInfos());
				}
			}
		}
	}

	@Override
	public void onPullDownRefresh(AbsListView pullRefreshView)
	{
		mLimitNumberEditText.clearFocus();
		mInputLayout.setVisibility(View.GONE);
		mCommonLayout.setVisibility(View.VISIBLE);
		KeyBoardUtil.closeKeybord(mLimitNumberEditText, this);
		loadMainInteraction();
		mVoteOptionDescriptionLayout.hide();
	}

	@Override
	public void onShareClick(SHARE_TYPE type)
	{
		if (mInteractionInfo != null)
		{
			String title = mInteractionInfo.getTitle();
			String intro = mInteractionInfo.getIntro();
			String href = ServerUrlConfig.SERVER_URL + mInteractionInfo.getHref();
			String image = mInteractionInfo.getImage();
			if (type == SHARE_TYPE.SHARE_WEIXIN)
			{
				ShareUtils.share(this, title, intro, href, image, com.v2gogo.project.utils.share.ShareUtils.SHARE_TYPE.SHARE_WEIXIN,
						new CustomPlatformActionListener(this, null));
			}
			else if (type == SHARE_TYPE.SHARE_WENXI_COLLECTIONS)
			{
				ShareUtils.share(this, title, intro, href, image, com.v2gogo.project.utils.share.ShareUtils.SHARE_TYPE.SHARE_WEIXIN_FRIENDS,
						new CustomPlatformActionListener(this, null));
			}
		}
	}

	@Override
	public void onLoadMore(AbsListView pullRefreshView)
	{
		mLimitNumberEditText.clearFocus();
		mInputLayout.setVisibility(View.GONE);
		mCommonLayout.setVisibility(View.VISIBLE);
		KeyBoardUtil.closeKeybord(mLimitNumberEditText, this);
		if (mCurrentPosition == NEWEST_POSITION && null != mInteractionInfo)
		{
			loadNewestComments(mNewestCommentListInfo.getCurrentPage() + 1);
		}
		else if (mCurrentPosition == HOT_POSITION && null != mInteractionInfo)
		{
			loadHotestComments(mNewestCommentListInfo.getCurrentPage() + 1);
		}
	}

	@Override
	public void onCommandClick(boolean isReply,CommentInfo commentInfo)
	{
		if(!isReply)
		{
			if (null != commentInfo)
			{
				if (V2GogoApplication.getMasterLoginState())
				{
					if (commentInfo.isPraised())
					{
						ToastUtil.showAlertToast(this, R.string.nin_yet_command_tip);
					}
					else
					{
						commentInfo.setPraised(true);
						commentInfo.setPraise(commentInfo.getPraise() + 1);
						mHomeCommonListAdapter.resetDatas();
						CommandManager.commandComment(mInteractionInfo == null ? null : mInteractionInfo.getId(), commentInfo.getId(),
								new IoncommandCommentCallback()
						{
							@Override
							public void oncommandCommentSuccess(String id)
							{
							}
							
							@Override
							public void oncommandCommentFail(String errorMessage)
							{
								ToastUtil.showAlertToast(HomeV2gogoDaJiangActivity.this, errorMessage);
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
	}

	@Override
	public void clearRequestTask()
	{
		mVoteOptionDescriptionLayout.destory();
		InteractionManager.clearGetMainInteractionDatasTask();
		InteractionManager.clearvoteInteractionByIdTask();
		CommentManager.clearGetCommentListTask();
		CommentManager.clearNewCommentTask();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK && mInputLayout.getVisibility() == View.VISIBLE)
		{
			mInputLayout.setVisibility(View.GONE);
			mCommonLayout.setVisibility(View.VISIBLE);
			KeyBoardUtil.closeKeybord(mLimitNumberEditText, this);
			mLimitNumberEditText.clearFocus();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onGroupClick(ExpandableListView arg0, View arg1, int position, long arg3)
	{
		if (!V2GogoApplication.getMasterLoginState())
		{
			AccountLoginActivity.forwardAccountLogin(this);
		}
		else
		{
			clickItem(position);
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
			deleteUserComment();
		}
	}

	/**
	 * 添加收藏
	 */
	private void addCollections()
	{

		if (!V2GogoApplication.getMasterLoginState())
		{
			AccountLoginActivity.forwardAccountLogin(HomeV2gogoDaJiangActivity.this);
		}
		else
		{
			if (mInteractionInfo != null)
			{
				ProfileCollectionsManager.AddCollectionsById(mInteractionInfo.getId(), ProfileCollectionsManager.COLLECTION_TYPE_INFO,
						new IonAddCollectionsCallback()
						{
							@Override
							public void onAddCollectionsSuccess()
							{
								if (null != mBtnCollections)
								{
									mBtnCollections.setImageResource(R.drawable.ic_action_favor_on_normal);
								}
								ToastUtil.showConfirmToast(HomeV2gogoDaJiangActivity.this, R.string.add_collections_tip);
							}

							@Override
							public void onAddCollectionsFail(String errorMessage)
							{
								ToastUtil.showAlertToast(HomeV2gogoDaJiangActivity.this, errorMessage);
							}
						});
			}
		}
	}

	/**
	 * 加载最新评论
	 * 
	 * @param page
	 */
	private void loadNewestComments(int page)
	{
		CommentManager.getNewestCommentList(mInteractionInfo.getId(), page, new IonCommentListCallback()
		{
			@Override
			public void onCommentListSuccess(CommentListInfo commentListInfo)
			{
				displayCommentDatas(commentListInfo);
			}

			@Override
			public void onCommentListFail(String errorMessage)
			{
				ToastUtil.showAlertToast(HomeV2gogoDaJiangActivity.this, errorMessage);
				if (mPullRefreshListView.isRefreshing())
				{
					mPullRefreshListView.stopPullRefresh();
				}
				if (mPullRefreshListView.isLoadingMore())
				{
					mPullRefreshListView.stopLoadMore();
				}
			}
		});
	}

	/**
	 * 加载最新评论
	 * 
	 * @param page
	 */
	private void loadHotestComments(int page)
	{
		CommentManager.getHotCommentList(mInteractionInfo.getId(), page, new IonCommentListCallback()
		{
			@Override
			public void onCommentListSuccess(CommentListInfo commentListInfo)
			{
				displayCommentDatas(commentListInfo);
			}

			@Override
			public void onCommentListFail(String errorMessage)
			{
				ToastUtil.showAlertToast(HomeV2gogoDaJiangActivity.this, errorMessage);
				if (mPullRefreshListView.isRefreshing())
				{
					mPullRefreshListView.stopPullRefresh();
				}
				if (mPullRefreshListView.isLoadingMore())
				{
					mPullRefreshListView.stopLoadMore();
				}
			}
		});
	}

	/**
	 * 显示评论数据
	 * 
	 * @param commentListInfo
	 */
	private void displayCommentDatas(CommentListInfo commentListInfo)
	{
		if (null != commentListInfo)
		{
			CommentListInfo commentListInfo2 = null;
			if (CommentManager.HOT_COMMENT_LIST_KEYWORD.equals(commentListInfo.getType()))
			{
				commentListInfo2 = mHotCommentListInfo;
			}
			else
			{
				commentListInfo2 = mNewestCommentListInfo;
			}
			if (null != commentListInfo2)
			{
				boolean isFinish = false;
				boolean isMore = false;
				if (commentListInfo.getCurrentPage() == CommentManager.FIRST_PAGE)
				{
					commentListInfo2.clear();
				}
				else
				{
					isMore = true;
				}
				if (commentListInfo.getCount() <= commentListInfo.getCurrentPage())
				{
					isFinish = true;
				}
				commentListInfo2.setCurrentPage(commentListInfo.getCurrentPage());
				commentListInfo2.addAll(commentListInfo);
				mHomeCommonListAdapter.resetDatas(commentListInfo2.getCommentInfos());
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
			}
		}
	}

	/**
	 * 显示互动信息
	 * 
	 * @param interactionInfo
	 */
	private void dealWithInteractionDatas(InteractionInfo interactionInfo)
	{
		if (null != interactionInfo)
		{
			mInteractionInfo = interactionInfo;
			String collections = (String) SPUtil.get(V2GogoApplication.sIntance, "collections", "");
			if (collections.contains(mInteractionInfo.getId() + ",") && null != mBtnCollections)
			{
				mBtnCollections.setImageResource(R.drawable.ic_action_favor_on_normal);
			}
			mVoteOptionDescriptionLayout.setHtmlDatas(interactionInfo.getDescription());
			if (null != mInteractionInfo.getmoteOptionInfos())
			{
				int number = 0;
				for (VoteOptionInfo voteOptionInfo : mInteractionInfo.getmoteOptionInfos())
				{
					number += voteOptionInfo.getPraise();
				}
				mVoteOptionNumberLayout.setVoteNumbers(number);
			}
			if (mInteractionInfo.getStatus() == InteractionInfo.STATUS_PREPARE || mInteractionInfo.getStatus() == InteractionInfo.STATUS_END)
			{
				if (mInteractionInfo.getStatus() == InteractionInfo.STATUS_PREPARE)
				{
					mTextNotice.setText(mInteractionInfo.getNotice());
				}
				else
				{
					mTextNotice.setText(mInteractionInfo.getConclusion());
				}
				mTextNotice.setVisibility(View.VISIBLE);
				if (mInteractionInfo.getStatus() == InteractionInfo.STATUS_PREPARE)
				{
					mCommentLayout.setVisibility(View.GONE);
					if (mHotCommentListInfo.getCommentInfos() != null)
					{
						mHotCommentListInfo.getCommentInfos().clear();
					}
					if (mNewestCommentListInfo.getCommentInfos() != null)
					{
						mNewestCommentListInfo.getCommentInfos().clear();
					}
					if (mCurrentPosition == HOT_POSITION)
					{
						mHomeCommonListAdapter.resetDatas(mHotCommentListInfo.getCommentInfos());
					}
					else
					{
						mHomeCommonListAdapter.resetDatas(mNewestCommentListInfo.getCommentInfos());
					}
				}
				else
				{
					mCommentLayout.setVisibility(View.VISIBLE);
				}
			}
			else
			{
				mCommentLayout.setVisibility(View.VISIBLE);
				mTextNotice.setVisibility(View.GONE);
			}
		}
		else
		{
			mVoteOptionNumberLayout.setVoteNumbers(0);
		}
		dealRefreahListview();
	}

	private void dealRefreahListview()
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

	/**
	 * 显示分享对话框
	 */
	private void showShareDialog()
	{
		if (null == mV2gogoShareDialog)
		{
			mV2gogoShareDialog = new V2gogoShareDialog(this, R.style.style_action_sheet_dialog);
			mV2gogoShareDialog.setItemClickCallback(this);
		}
		if (!mV2gogoShareDialog.isShowing())
		{
			mV2gogoShareDialog.show();
		}
	}

	/**
	 * 发起删除评论的请求
	 */
	private void deleteUserComment()
	{
		if (null != mClickCommentInfo)
		{
			ProfileCommentManager.deleteProfileCommentById(mClickCommentInfo, V2GogoApplication.getCurrentMatser().getUsername(),
					new IonProfileCommentDeleteCallback()
					{
						@Override
						public void onProfileCommentDeleteSuccess(CommentInfo commentInfo)
						{
							if (null != mHomeCommonListAdapter && null != mHomeCommonListAdapter.getCommentInfos() && commentInfo != null)
							{
								mHomeCommonListAdapter.getCommentInfos().remove(commentInfo);
								mHomeCommonListAdapter.resetDatas(mHomeCommonListAdapter.getCommentInfos());
							}
							mClickCommentInfo = null;
						}

						@Override
						public void onProfileCommentDeleteFail(String errorMessage)
						{
							ToastUtil.showAlertToast(HomeV2gogoDaJiangActivity.this, errorMessage);
						}
					});
		}
	}

	/**
	 * 初始化滑块大小与位置
	 */
	private void initLinePositionSize()
	{
		mHalfScreenWidth = ScreenUtil.getScreenWidth(this) / 2f;
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.common_ui_popup_titleline);
		float scaleX = mHalfScreenWidth / bitmap.getWidth();
		Matrix matrix = new Matrix();
		matrix.postScale(scaleX, 0.5f);
		mLine.setImageMatrix(matrix);
		if (!bitmap.isRecycled())
		{
			bitmap.recycle();
		}
	}

	/**
	 * 初始化头部
	 */
	private void initHeaderView()
	{
		View headerView = LayoutInflater.from(this).inflate(R.layout.home_interaction_activity_header_layout, null);
		mLine = (ImageView) headerView.findViewById(R.id.home_interaction_activity_header_line);
		mRadioGroup = (RadioGroup) headerView.findViewById(R.id.home_interaction_activity_header_rg);
		mTextNotice = (TextView) headerView.findViewById(R.id.home_interaction_activity_header_notice);
		mCommentLayout = (FrameLayout) headerView.findViewById(R.id.home_interaction_activity_header_commemt_oparation);
		mVoteOptionNumberLayout = (VoteOptionNumberLayout) headerView.findViewById(R.id.home_interaction_activity_heade_number_layout);
		mVoteOptionDescriptionLayout = (VoteOptionDescriptionLayout) headerView.findViewById(R.id.home_interaction_activity_heade_description_layout);
		mPullRefreshListView.addHeaderView(headerView, null, true);
		initLinePositionSize();
	}

	/**
	 * 点击投票
	 */
	private void clickVote()
	{
		if (mInteractionInfo != null)
		{
			if (mInteractionInfo.getStatus() == InteractionInfo.STATUS_PROCESS)
			{
				if (V2GogoApplication.getMasterLoginState())
				{
					if (mInteractionInfo.isYetVoted())
					{
						ToastUtil.showAlertToast(this, R.string.nin_yet_vote_tip);
					}
					else
					{
						displayVoteDialog();
					}
				}
				else
				{
					AccountLoginActivity.forwardAccountLogin(this);
				}
			}
			else if (mInteractionInfo.getStatus() == InteractionInfo.STATUS_END)
			{
				ToastUtil.showAlertToast(this, R.string.vote_yet_end_tip);
			}
			else if (mInteractionInfo.getStatus() == InteractionInfo.STATUS_PREPARE)
			{
				ToastUtil.showAlertToast(this, R.string.vote_not_start_tip);
			}
		}
	}

	/**
	 * 显示投票对话框
	 */
	private void displayVoteDialog()
	{
		if (null == mVoteActionSheetDialog)
		{
			mVoteActionSheetDialog = new VoteActionSheetDialog(this, R.style.style_action_sheet_dialog);
			mVoteActionSheetDialog.setOnVoteActionSheetCallback(this);
		}
		if (!mVoteActionSheetDialog.isShowing())
		{
			mVoteActionSheetDialog.show();
			mVoteActionSheetDialog.setActionDatas(mInteractionInfo);
			mVoteActionSheetDialog.setOptionInfos(mInteractionInfo.getmoteOptionInfos());
		}
	}

	/**
	 * 点击评论
	 */
	private void clickComment()
	{
		if (null != mInteractionInfo)
		{
			if (mInteractionInfo.getStatus() == InteractionInfo.STATUS_PREPARE)
			{
				ToastUtil.showAlertToast(this, R.string.vote_not_start_tip);
			}
			else
			{
				if (V2GogoApplication.getMasterLoginState())
				{
					Intent intent = new Intent(this, HomeCommentActivity.class);
					intent.putExtra(HomeCommentActivity.ARTICE_ID, mInteractionInfo.getId());
					intent.putExtra(HomeCommentActivity.COMMENT_TYPE, CommentInfo.COMMENT_ARTICE);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivityForResult(intent, COMMENT_REQUEST_CODE);
				}
				else
				{
					AccountLoginActivity.forwardAccountLogin(this);
				}
			}
		}
	}

	/**
	 * 设置适配器
	 */
	private void setAdapter()
	{
		mHomeCommonListAdapter = new HomeCommonListAdapter(this, mPullRefreshListView,false);
		mPullRefreshListView.setAdapter(mHomeCommonListAdapter);
		mHomeCommonListAdapter.setOnCommandClickListener(this);
	}

	/**
	 * 处理投票数据成功之后的逻辑
	 */
	private void dealVoteSuccess(String optionId, int voteNum)
	{
		if (null != mInteractionInfo && null != mInteractionInfo.getmoteOptionInfos())
		{
			for (VoteOptionInfo voteOptionInfo : mInteractionInfo.getmoteOptionInfos())
			{
				if (null != voteOptionInfo)
				{
					if (optionId.equals(voteOptionInfo.getId()))
					{
						voteOptionInfo.setmPraise(voteOptionInfo.getPraise() + voteNum);
						break;
					}
				}
			}
			mVoteOptionNumberLayout.setVoteNumbers(mVoteOptionNumberLayout.getVoteNumber() + voteNum);
		}
	}

	/**
	 * 点击item
	 * 
	 * @param position
	 */
	private void clickItem(int position)
	{
		if (null != mHomeCommonListAdapter.getCommentInfos() && position <= mHomeCommonListAdapter.getGroupCount()
				&& null != mHomeCommonListAdapter.getCommentInfos().get(position))
		{
			CommentInfo commentInfo = mHomeCommonListAdapter.getCommentInfos().get(position);
			if (commentInfo != null)
			{
				mClickCommentInfo = commentInfo;
				String userId = V2GogoApplication.getCurrentMatser().getUserid();
				if (commentInfo.getUserid().equals(userId))
				{
					deleteUserSelfComments();
				}
				else
				{
					mInputLayout.setVisibility(View.VISIBLE);
					mCommonLayout.setVisibility(View.GONE);
					displayReplyOthersKeyboard(commentInfo);
				}
			}
		}
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
	 * 显示回复别人的键盘
	 * 
	 * @param commentInfo
	 */
	private void displayReplyOthersKeyboard(CommentInfo commentInfo)
	{
		String str = getString(R.string.comment_reply_tip);
		str = String.format(str, commentInfo.getUsername());
		mLimitNumberEditText.setHint(str);
		mLimitNumberEditText.requestFocus();
		KeyBoardUtil.openKeybord(mLimitNumberEditText, this);
	}

	/**
	 * 回复评论
	 */
	private void commentReplyInfo()
	{
		if (null != mClickCommentInfo && null != mInteractionInfo && mInteractionInfo.getStatus() != InteractionInfo.STATUS_PREPARE)
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
				mBtnPublishComments.setEnabled(false);
				content = StringUtil.replaceBlank(content);
				if (TextUtils.isEmpty(mClickCommentInfo.getSrccont()))
				{
					srcCount = mClickCommentInfo.getUsername() + ":" + mClickCommentInfo.getContent();
				}
				else
				{
					srcCount = mClickCommentInfo.getSrccont() + ";" + mClickCommentInfo.getUsername() + ":" + mClickCommentInfo.getContent();
				}
				CommentManager.publishNewCommentWithNoImage(mClickCommentInfo.getId(), mInteractionInfo.getId(), CommentInfo.COMMENT_COMMEMT,
						CommentInfo.SRC_ARTICE_TYPE, content, srcCount, new IonNewCommentCallback()
						{
							@Override
							public void onNewCommentSuccess(CommentInfo commentInfo)
							{
								commentSuccess(commentInfo);
							}

							@Override
							public void onNewCommentFail(String errorMessage)
							{
								commentFail(errorMessage);
							}

						});
				mLimitNumberEditText.setText("");
				KeyBoardUtil.closeKeybord(mLimitNumberEditText, this);
			}
		}
	}

	/**
	 * 评论失败
	 * 
	 * @param errorMessage
	 */
	private void commentFail(String errorMessage)
	{
		mClickCommentInfo = null;
		mBtnPublishComments.setEnabled(true);
		mLimitNumberEditText.clearFocus();
		mInputLayout.setVisibility(View.GONE);
		mCommonLayout.setVisibility(View.VISIBLE);
		ToastUtil.showAlertToast(HomeV2gogoDaJiangActivity.this, errorMessage);
	}

	/**
	 * 评论成功
	 * 
	 * @param commentInfo
	 */
	private void commentSuccess(CommentInfo commentInfo)
	{
		mClickCommentInfo = null;
		mBtnPublishComments.setEnabled(true);
		mLimitNumberEditText.clearFocus();
		mInputLayout.setVisibility(View.GONE);
		mCommonLayout.setVisibility(View.VISIBLE);
		if (mCurrentPosition == NEWEST_POSITION)
		{
			if (V2GogoApplication.getMasterLoginState())
			{
				commentInfo.setAvatar(V2GogoApplication.getCurrentMatser().getThumbialAvatar());
			}
			if (!TextUtils.isEmpty(commentInfo.getSrccont()))
			{
				commentInfo.parseCommentReplyData();
			}
			mNewestCommentListInfo.addTop(commentInfo);
			mHomeCommonListAdapter.resetDatas(mNewestCommentListInfo.getCommentInfos());
		}
		ToastUtil.showConfirmToast(HomeV2gogoDaJiangActivity.this, R.string.publish_comment_success);
	}
}
