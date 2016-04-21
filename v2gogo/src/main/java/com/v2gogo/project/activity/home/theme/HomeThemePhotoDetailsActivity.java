package com.v2gogo.project.activity.home.theme;

import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.account.AccountLoginActivity;
import com.v2gogo.project.activity.home.BaseDetailsctivity;
import com.v2gogo.project.activity.home.HomeCommentActivity;
import com.v2gogo.project.adapter.home.HomeCommonListAdapter;
import com.v2gogo.project.adapter.home.HomeCommonListAdapter.IonCommandClickListener;
import com.v2gogo.project.domain.ShareInfo;
import com.v2gogo.project.domain.home.CommentInfo;
import com.v2gogo.project.domain.home.CommentListInfo;
import com.v2gogo.project.domain.home.theme.ThemePhotoCommandUserInfo;
import com.v2gogo.project.domain.home.theme.ThemePhotoInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.main.image.GlideImageLoader;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.manager.home.CommandManager;
import com.v2gogo.project.manager.home.CommandManager.IoncommandCommentCallback;
import com.v2gogo.project.manager.home.CommentManager;
import com.v2gogo.project.manager.home.CommentManager.IonCommentListCallback;
import com.v2gogo.project.manager.home.CommentManager.IonNewCommentCallback;
import com.v2gogo.project.manager.home.PeopleConcernManager;
import com.v2gogo.project.manager.home.theme.ThemePhotoCommandManager;
import com.v2gogo.project.manager.home.theme.ThemePhotoCommandManager.IonCommandThemePhotoCallback;
import com.v2gogo.project.manager.home.theme.ThemePhotoSearchManager;
import com.v2gogo.project.manager.home.theme.ThemePhotoSearchManager.IonSearchThemePhotoCallback;
import com.v2gogo.project.manager.profile.ProfileCommentManager;
import com.v2gogo.project.manager.profile.ProfileCommentManager.IonProfileCommentDeleteCallback;
import com.v2gogo.project.utils.StatusCode;
import com.v2gogo.project.utils.common.DensityUtil;
import com.v2gogo.project.utils.common.KeyBoardUtil;
import com.v2gogo.project.utils.common.ScreenUtil;
import com.v2gogo.project.utils.common.StringUtil;
import com.v2gogo.project.utils.common.ToastUtil;
import com.v2gogo.project.utils.http.IntentExtraConstants;
import com.v2gogo.project.views.ProgressLayout;
import com.v2gogo.project.views.dialog.ProfileActionSheetDialog;
import com.v2gogo.project.views.dialog.ProfileActionSheetDialog.ACTION;
import com.v2gogo.project.views.dialog.ProfileActionSheetDialog.IonActionSheetClickListener;
import com.v2gogo.project.views.edittext.LimitNumberEditText;
import com.v2gogo.project.views.expandablelistview.PullExpandableListview;
import com.v2gogo.project.views.listview.refreshview.OnLoadMoreListener;
import com.v2gogo.project.views.listview.refreshview.OnPullRefreshListener;
import com.v2gogo.project.views.logic.ThemePhotoAvatarLayout;

/**
 * 主题图片详情页面
 * 
 * @author houjun
 */
public class HomeThemePhotoDetailsActivity extends BaseShareActivity implements OnClickListener, OnGroupClickListener, OnPullRefreshListener,
		OnLoadMoreListener, IonCommandClickListener, IonActionSheetClickListener, OnChildClickListener
{
	private final int REQUEST_CODE_PUBLISH_COMMENT = 0X101;

	private String mId;
	private String mTid;

	private int mWidth;
	private int mHeight;

	private ImageView mHeaderUserAvatar;
	private ImageView mHeaderPhotoImage;

	private TextView mHeaderFullname;
	private TextView mHeaderUploadTime;
	private TextView mHeaderPhotoSn;
	private TextView mHeaderPhotoDesc;
	private TextView mHeaderCommandText;

	private ImageButton mHeaderClickCommand;
	private ThemePhotoAvatarLayout mPhotoAvatarLayout;

	private ImageButton mIbtnCommand;
	private ImageButton mIbtnShare;
	private Button mBtnWriteComment;

	private LinearLayout mInputLayout;
	private Button mBtnPublishComments;
	private LinearLayout mCommonLayout;
	private LimitNumberEditText mLimitNumberEditText;

	private ProgressLayout mProgressLayout;
	private ProfileActionSheetDialog mActionSheetDialog;
	private PullExpandableListview mPullExpandableListview;

	private CommentInfo mClickCommentInfo;
	private ThemePhotoInfo mThemePhotoInfo;
	private CommentListInfo mCommentListInfo;
	private HomeCommonListAdapter mCommonListAdapter;

	@Override
	public void clearRequestTask()
	{
		CommentManager.clearNewCommentTask();
		ThemePhotoSearchManager.clearSearchThemePhotoByIdTask();
	}

	@Override
	public void onInitViews()
	{
		mIbtnShare = (ImageButton) findViewById(R.id.home_theme_photo_deatils_layout_share);
		mBtnWriteComment = (Button) findViewById(R.id.home_theme_photo_deatils_layout_write);
		mIbtnCommand = (ImageButton) findViewById(R.id.home_theme_photo_deatils_layout_command);
		mBtnPublishComments = (Button) findViewById(R.id.home_theme_photo_details_publish_btn);
		mInputLayout = (LinearLayout) findViewById(R.id.home_theme_photo_details_input_layout);
		mCommonLayout = (LinearLayout) findViewById(R.id.home_theme_photo_details_common_layout);
		mProgressLayout = (ProgressLayout) findViewById(R.id.home_theme_photo_details_progress_layout);
		mLimitNumberEditText = (LimitNumberEditText) findViewById(R.id.home_theme_photo_details_input);
		mPullExpandableListview = (PullExpandableListview) findViewById(R.id.home_theme_photo_details_pull_expandable_list_view);
		initListHeaderView();
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.home_theme_photo_details_activity_layout;
	}

	@Override
	public ShareInfo getShareInfo()
	{
		if (mThemePhotoInfo != null)
		{
			ShareInfo shareInfo = new ShareInfo();
			shareInfo.setTitle(mThemePhotoInfo.getResourceNo());
			shareInfo.setImageUrl(mThemePhotoInfo.getPhotoImg());
			shareInfo.setDescription(mThemePhotoInfo.getPhotoDescription());
			shareInfo.setHref(ServerUrlConfig.SERVER_URL + mThemePhotoInfo.getShareAddress());
			return shareInfo;
		}
		return null;
	}

	@Override
	protected void onInitIntentData(Intent intent)
	{
		super.onInitIntentData(intent);
		if (null != intent)
		{
			mId = intent.getStringExtra(IntentExtraConstants.PID);
			mTid = intent.getStringExtra(IntentExtraConstants.TID);
			mWidth = intent.getIntExtra(IntentExtraConstants.WIDTH, 1);
			mHeight = intent.getIntExtra(IntentExtraConstants.HEIGHT, 1);
			if (mWidth == 0 || mHeight == 0)
			{
				mWidth = 1;
				mHeight = 1;
			}
		}
	}

	@Override
	protected void registerListener()
	{
		super.registerListener();
		mIbtnShare.setOnClickListener(this);
		mIbtnCommand.setOnClickListener(this);
		mBtnWriteComment.setOnClickListener(this);
		mHeaderClickCommand.setOnClickListener(this);
		mBtnPublishComments.setOnClickListener(this);
		mCommonListAdapter.setOnCommandClickListener(this);
		mPullExpandableListview.setOnLoadMoreListener(this);
		mPullExpandableListview.setOnGroupClickListener(this);
		mPullExpandableListview.setOnChildClickListener(this);
		mPullExpandableListview.setOnPullRefreshListener(this);
		mPullExpandableListview.setLoadMoreEnable(false);
	}

	@Override
	protected void onInitLoadDatas()
	{
		super.onInitLoadDatas();
		mCommentListInfo = new CommentListInfo();
		mCommonListAdapter = new HomeCommonListAdapter(this, mPullExpandableListview, false);
		mPullExpandableListview.setAdapter(mCommonListAdapter);
		if (!TextUtils.isEmpty(mId))
		{
			mProgressLayout.showProgress();
			loadThemePhotoDetailsDatas(mId);
			loadNewestComments(CommentManager.FIRST_PAGE);
		}
	}

	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.home_theme_photo_deatils_layout_share:
				showShareDialog();
				break;

			case R.id.home_theme_photo_deatils_layout_command:
			case R.id.home_theme_photo_details_header_photo_command:
				commandThemePhoto();
				break;

			case R.id.home_theme_photo_deatils_layout_write:
				forwardWriteComment();
				break;

			case R.id.home_theme_photo_details_publish_btn:
				commentReplyInfo();
				break;

			default:
				break;
		}
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
	protected void onPause()
	{
		KeyBoardUtil.closeKeybord(mLimitNumberEditText, this);
		super.onPause();
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
	public void onPullDownRefresh(AbsListView pullRefreshView)
	{
		if (!TextUtils.isEmpty(mId))
		{
			loadThemePhotoDetailsDatas(mId);
			loadNewestComments(CommentManager.FIRST_PAGE);
		}
	}

	@Override
	public void onLoadMore(AbsListView pullRefreshView)
	{
		if (!TextUtils.isEmpty(mId))
		{
			loadThemePhotoDetailsDatas(mId);
			loadNewestComments(mCommentListInfo.getCurrentPage() + 1);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_PUBLISH_COMMENT && resultCode == RESULT_OK)
		{
			if (data == null)
			{
				return;
			}
			CommentInfo commentInfo = (CommentInfo) data.getSerializableExtra(BaseDetailsctivity.COMMENT);
			if (null != commentInfo)
			{
				if (!TextUtils.isEmpty(commentInfo.getSrccont()))
				{
					commentInfo.parseCommentReplyData();
				}
				mCommentListInfo.addTop(commentInfo);
				mCommonListAdapter.resetDatas(mCommentListInfo.getCommentInfos());
			}
		}
	}

	@Override
	public void onCommandClick(boolean isReply, CommentInfo commentInfo)
	{
		if (!isReply)
		{
			commandComment(commentInfo);
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

	/**
	 * 回复评论
	 */
	private void commentReplyInfo()
	{
		if (null != mClickCommentInfo && !TextUtils.isEmpty(mId))
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
				CommentManager.publishNewCommentWithNoImage(mClickCommentInfo.getId(), mId, CommentInfo.COMMENT_COMMEMT, CommentInfo.SRC_ARTICE_TYPE, content,
						srcCount, new IonNewCommentCallback()
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
		ToastUtil.showAlertToast(HomeThemePhotoDetailsActivity.this, errorMessage);
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

		if (V2GogoApplication.getMasterLoginState())
		{
			commentInfo.setAvatar(V2GogoApplication.getCurrentMatser().getThumbialAvatar());
		}
		if (!TextUtils.isEmpty(commentInfo.getSrccont()))
		{
			commentInfo.parseCommentReplyData();
		}
		mCommentListInfo.addTop(commentInfo);
		mCommonListAdapter.resetDatas(mCommentListInfo.getCommentInfos());
		ToastUtil.showConfirmToast(HomeThemePhotoDetailsActivity.this, R.string.publish_comment_success);
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
							if (null != mCommonListAdapter && null != mCommonListAdapter.getCommentInfos() && commentInfo != null)
							{
								mCommonListAdapter.getCommentInfos().remove(commentInfo);
								mCommonListAdapter.resetDatas(mCommonListAdapter.getCommentInfos());
							}
							mClickCommentInfo = null;
						}

						@Override
						public void onProfileCommentDeleteFail(String errorMessage)
						{
							ToastUtil.showAlertToast(HomeThemePhotoDetailsActivity.this, errorMessage);
						}
					});
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
	 * 赞评论
	 * 
	 * @param commentInfo
	 */
	private void commandComment(CommentInfo commentInfo)
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
					CommandManager.commandComment(commentInfo.getInfoId(), commentInfo.getId(), new IoncommandCommentCallback()
					{
						@Override
						public void oncommandCommentSuccess(String id)
						{
						}

						@Override
						public void oncommandCommentFail(String errorMessage)
						{
							ToastUtil.showAlertToast(HomeThemePhotoDetailsActivity.this, errorMessage);
						}
					});
					mCommonListAdapter.resetDatas(mCommentListInfo.getCommentInfos());
				}
			}
			else
			{
				AccountLoginActivity.forwardAccountLogin(this);
			}
		}
	}

	/**
	 * 赞主题图片
	 */
	private void commandThemePhoto()
	{
		if (!TextUtils.isEmpty(mId))
		{
			if (!V2GogoApplication.getMasterLoginState())
			{
				AccountLoginActivity.forwardAccountLogin(this);
			}
			else
			{
				if (mThemePhotoInfo != null)
				{
					if (mThemePhotoInfo.isPraise())
					{
						ToastUtil.showAlertToast(HomeThemePhotoDetailsActivity.this, R.string.you_yet_command_theme_photo);
					}
					else
					{
						ThemePhotoCommandManager.commandThemePhoto(mId, new IonCommandThemePhotoCallback()
						{
							@Override
							public void onCommandThemePhoto(int code, String message)
							{
								if (StatusCode.SUCCESS == code)
								{
									showCommandDatas();
								}
								else
								{
									ToastUtil.showAlertToast(HomeThemePhotoDetailsActivity.this, message);
								}
							}
						});
					}
				}
			}
		}
	}

	/**
	 * 显示点赞成功的数据
	 */
	private void showCommandDatas()
	{
		if (mThemePhotoInfo != null)
		{
			mThemePhotoInfo.setPraise(true);
			mThemePhotoInfo.setPraiseNum(mThemePhotoInfo.getPraiseNum() + 1);
			String number = String.format(getString(R.string.how_much_yet_command), mThemePhotoInfo.getPraiseNum());
			mHeaderCommandText.setText(number);
			ThemePhotoCommandUserInfo themePhotoCommandUserInfo = new ThemePhotoCommandUserInfo();
			themePhotoCommandUserInfo.setFullName(V2GogoApplication.getCurrentMatser().getFullname());
			themePhotoCommandUserInfo.setHeadImg(V2GogoApplication.getCurrentMatser().getThumbialAvatar());
			mThemePhotoInfo.addTop(themePhotoCommandUserInfo);
			mPhotoAvatarLayout.setCommandUserListDatas(mThemePhotoInfo.getCommandUserInfos(), mThemePhotoInfo.getId());
		}
		mHeaderClickCommand.setBackgroundResource(R.drawable.theme_photo_command_bg_pressed);
		mHeaderClickCommand.setImageResource(R.drawable.theme_command_pressed);
		mIbtnCommand.setImageResource(R.drawable.ic_action_theme_photo_command_pressed);
	}

	/**
	 * 搜索主题图片
	 * 
	 * @param photoNo
	 */
	private void loadThemePhotoDetailsDatas(String id)
	{
		ThemePhotoSearchManager.searchThemePhotoById(id, mTid, new IonSearchThemePhotoCallback()
		{
			@Override
			public void onSearchThemePhotoSuccess(ThemePhotoInfo themePhotoInfo)
			{
				mProgressLayout.showContent();
				mThemePhotoInfo = themePhotoInfo;
				displayThemePhotoDatas(themePhotoInfo);
			}

			@Override
			public void onSearchThemePhotoFail(int code, String errorMessage)
			{
				mProgressLayout.showContent();
				ToastUtil.showAlertToast(HomeThemePhotoDetailsActivity.this, errorMessage);
			}
		});
	}

	/**
	 * 显示主题图片的数据
	 */
	private void displayThemePhotoDatas(ThemePhotoInfo themePhotoInfo)
	{
		if (null != themePhotoInfo)
		{
			mHeaderFullname.setText(themePhotoInfo.getFullName());
			mHeaderPhotoSn.setText("No:" + themePhotoInfo.getResourceNo());
			mHeaderUploadTime.setText(themePhotoInfo.getFriendlyTime());
			mHeaderPhotoDesc.setText(themePhotoInfo.getPhotoDescription());
			GlideImageLoader.loadImageWithFixedSize(this, themePhotoInfo.getRealPhotoImage(), mHeaderPhotoImage);
			GlideImageLoader.loadAvatarImageWithFixedSize(this, themePhotoInfo.getHeadImg(), mHeaderUserAvatar);
			if (themePhotoInfo.isPraise())
			{
				mHeaderClickCommand.setBackgroundResource(R.drawable.theme_photo_command_bg_pressed);
				mHeaderClickCommand.setImageResource(R.drawable.theme_command_pressed);
				mIbtnCommand.setImageResource(R.drawable.ic_action_theme_photo_command_pressed);
			}
			else
			{
				mHeaderClickCommand.setBackgroundResource(R.drawable.theme_photo_command_bg);
				mHeaderClickCommand.setImageResource(R.drawable.theme_command);
				mIbtnCommand.setImageResource(R.drawable.ic_action_theme_photo_command);
			}
			if (themePhotoInfo.getPraiseNum() == 0)
			{
				mHeaderCommandText.setText(R.string.how_much_no_yet_command);
			}
			else
			{
				String number = String.format(getString(R.string.how_much_yet_command), themePhotoInfo.getPraiseNum());
				mHeaderCommandText.setText(number);
			}
			mPhotoAvatarLayout.setCommandUserListDatas(themePhotoInfo.getCommandUserInfos(), themePhotoInfo.getId());
		}
	}

	/**
	 * 初始化头部视图
	 */
	private void initListHeaderView()
	{
		View headerView = LayoutInflater.from(this).inflate(R.layout.home_theme_photo_details_activity_header_layout, null);

		mHeaderPhotoSn = (TextView) headerView.findViewById(R.id.home_theme_photo_details_header_sn);
		mHeaderFullname = (TextView) headerView.findViewById(R.id.home_theme_photo_details_header_fullname);
		mHeaderUploadTime = (TextView) headerView.findViewById(R.id.home_theme_photo_details_header_upload_time);
		mHeaderCommandText = (TextView) headerView.findViewById(R.id.home_theme_photo_details_header_command_num);
		mHeaderPhotoDesc = (TextView) headerView.findViewById(R.id.home_theme_photo_details_header_photo_description);

		mHeaderPhotoImage = (ImageView) headerView.findViewById(R.id.home_theme_photo_details_header_image);
		mHeaderUserAvatar = (ImageView) headerView.findViewById(R.id.home_theme_photo_details_header_user_avatar);
		mHeaderClickCommand = (ImageButton) headerView.findViewById(R.id.home_theme_photo_details_header_photo_command);

		mPhotoAvatarLayout = (ThemePhotoAvatarLayout) headerView.findViewById(R.id.home_theme_photo_details_header_avatar_layout);

		float scale = mHeight * 1.0f / mWidth;
		int width = ScreenUtil.getScreenWidth(this) - DensityUtil.dp2px(this, 20f);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, (int) (width * scale));
		layoutParams.topMargin = 15;
		mHeaderPhotoImage.setLayoutParams(layoutParams);
		mPullExpandableListview.addHeaderView(headerView);
	}

	/**
	 * 写评论
	 */
	private void forwardWriteComment()
	{
		if (!TextUtils.isEmpty(mId))
		{
			Intent intent = new Intent(this, HomeCommentActivity.class);
			intent.putExtra(HomeCommentActivity.ARTICE_ID, mId);
			intent.putExtra(HomeCommentActivity.IS_IMAGE, false);
			intent.putExtra(HomeCommentActivity.COMMENT_TYPE, 0);
			intent.putExtra(HomeCommentActivity.SRC_TYPE, CommentInfo.SRC_ARTICE_TYPE);
			startActivityForResult(intent, REQUEST_CODE_PUBLISH_COMMENT);
		}
	}

	/**
	 * 加载最新评论
	 */
	private void loadNewestComments(int page)
	{
		CommentManager.getNewestCommentList(mId, page, new IonCommentListCallback()
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
				mProgressLayout.showContent();
				ToastUtil.showAlertToast(HomeThemePhotoDetailsActivity.this, errorMessage);
				if (mPullExpandableListview.isRefreshing())
				{
					mPullExpandableListview.stopPullRefresh();
				}
				if (mPullExpandableListview.isLoadingMore())
				{
					mPullExpandableListview.stopLoadMore();
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
			mPullExpandableListview.stopLoadMore();
		}
		else
		{
			mPullExpandableListview.stopPullRefresh();
		}
		if (isFinish)
		{
			mPullExpandableListview.setLoadMoreEnable(false);
		}
		else
		{
			mPullExpandableListview.setLoadMoreEnable(true);
		}
	}
}
