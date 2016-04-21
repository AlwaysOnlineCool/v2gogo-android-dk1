package com.v2gogo.project.activity.home.theme;

import java.io.File;
import java.io.FileOutputStream;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.account.AccountLoginActivity;
import com.v2gogo.project.activity.home.HomeCommentListActivity;
import com.v2gogo.project.adapter.home.HomeThemePhotoAdapter;
import com.v2gogo.project.adapter.home.HomeThemePhotoAdapter.IonThemePhotoClickCommandCallback;
import com.v2gogo.project.domain.ShareInfo;
import com.v2gogo.project.domain.home.CommentInfo;
import com.v2gogo.project.domain.home.theme.ThemePhotoInfo;
import com.v2gogo.project.domain.home.theme.ThemePhotoListInfo;
import com.v2gogo.project.domain.home.theme.ThemePhotoListResultInfo;
import com.v2gogo.project.domain.home.theme.ThemePhotoUploadResultInfo;
import com.v2gogo.project.domain.home.theme.TopicInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.manager.home.PeopleConcernManager;
import com.v2gogo.project.manager.home.theme.ThemePhotoCommandManager;
import com.v2gogo.project.manager.home.theme.ThemePhotoManager;
import com.v2gogo.project.manager.home.theme.ThemePhotoManager.IonThemeTopicCallback;
import com.v2gogo.project.manager.home.theme.ThemePhotoManager.ThemePhotoListCallback;
import com.v2gogo.project.manager.upload.ThemePhotoUploadManager;
import com.v2gogo.project.utils.common.PhotoUtil;
import com.v2gogo.project.utils.common.ToastUtil;
import com.v2gogo.project.utils.http.IntentExtraConstants;
import com.v2gogo.project.views.ProgressLayout;
import com.v2gogo.project.views.ProgressLayout.IonRetryLoadDatasCallback;
import com.v2gogo.project.views.ProgressLayout.State;
import com.v2gogo.project.views.dialog.ProfileActionSheetDialog;
import com.v2gogo.project.views.dialog.ProfileActionSheetDialog.ACTION;
import com.v2gogo.project.views.dialog.ProfileActionSheetDialog.IonActionSheetClickListener;
import com.v2gogo.project.views.listview.refreshview.OnLoadMoreListener;
import com.v2gogo.project.views.listview.refreshview.OnPullRefreshListener;
import com.v2gogo.project.views.listview.refreshview.PullRefreshListView;
import com.ypy.eventbus.EventBus;

/**
 * 主题照片墙
 * 
 * @author houjun
 */
public abstract class BaseThemePhotoListActivity extends BaseShareActivity implements OnPullRefreshListener, OnLoadMoreListener, IonRetryLoadDatasCallback,
		OnClickListener, IonActionSheetClickListener, IonThemePhotoClickCommandCallback
{

	protected abstract boolean isLoadNewest();

	private ProgressLayout mProgressLayout;
	private PullRefreshListView mPullRefreshListView;

	private HomeThemePhotoAdapter mHomeThemePhotoAdapter;

	private ProfileActionSheetDialog mUploadActionSheetDialog;

	private TopicInfo mTopicInfo;
	private ThemePhotoListInfo mThemePhotoListInfo;

	private View mLine;
	private Button mBtnJoin;

	private TextView mTvCommentNum;
	private ImageButton mIbtnShare;
	private ImageButton mIbtnSearch;
	private ImageButton mIbtnComment;
	private RelativeLayout mJoinLayout;

	private String mTid;

	@Override
	public void clearRequestTask()
	{
		ThemePhotoManager.clearoadThemePhotoTopic();
		if (isLoadNewest())
		{
			EventBus.getDefault().unregister(this);
			ThemePhotoManager.clearNewestThemePhotoListTask();
		}
		else
		{
			ThemePhotoManager.clearHotestThemePhotoListTask();
		}
	}

	@Override
	public void onInitViews()
	{
		mLine = findViewById(R.id.base_theme_photo_layout_line);
		mBtnJoin = (Button) findViewById(R.id.base_theme_photo_layout_join);
		mIbtnShare = (ImageButton) findViewById(R.id.base_theme_photo_layout_share);
		mIbtnSearch = (ImageButton) findViewById(R.id.base_theme_photo_layout_search);
		mIbtnComment = (ImageButton) findViewById(R.id.base_theme_photo_layout_comment);
		mTvCommentNum = (TextView) findViewById(R.id.base_theme_photo_layout_comment_num);
		mProgressLayout = (ProgressLayout) findViewById(R.id.home_theme_photo_progress_layout);
		mJoinLayout = (RelativeLayout) findViewById(R.id.base_theme_photo_layout_join_layout);
		mPullRefreshListView = (PullRefreshListView) findViewById(R.id.base_theme_photo_pull_to_refresh_listview);
		mTvCommentNum.setTextSize(9f);
	}

	@Override
	protected void onInitLoadDatas()
	{
		super.onInitLoadDatas();
		mProgressLayout.showProgress();
		mThemePhotoListInfo = new ThemePhotoListInfo();
		mTid = ((HomeThemePhotoTabActivity) getParent()).mTid;
		mHomeThemePhotoAdapter = new HomeThemePhotoAdapter(this);
		mPullRefreshListView.setAdapter(mHomeThemePhotoAdapter);
		loadPhotoInfoDatas();
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.base_theme_photo_activity_layout;
	}

	@Override
	protected void registerListener()
	{
		super.registerListener();
		mBtnJoin.setOnClickListener(this);
		mIbtnShare.setOnClickListener(this);
		mIbtnSearch.setOnClickListener(this);
		mIbtnComment.setOnClickListener(this);
		mProgressLayout.setOnTryLoadDatasCallback(this);
		mPullRefreshListView.setOnLoadMoreListener(this);
		mPullRefreshListView.setOnPullRefreshListener(this);
		if (isLoadNewest())
		{
			EventBus.getDefault().register(this);
		}
		mHomeThemePhotoAdapter.setThemePhotoClickCommandCallback(this);
		mPullRefreshListView.setLoadMoreEnable(false);
	}

	@Override
	public ShareInfo getShareInfo()
	{
		if (null != mTopicInfo)
		{
			ShareInfo shareInfo = new ShareInfo();
			shareInfo.setTitle(mTopicInfo.gettTitle());
			shareInfo.setDescription(mTopicInfo.getIntro());
			shareInfo.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
			shareInfo.setHref(ServerUrlConfig.SERVER_URL + mTopicInfo.getShareAddress());
			return shareInfo;
		}
		return null;
	}

	@Override
	protected boolean isSetting()
	{
		return false;
	}

	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.base_theme_photo_layout_search:
				forward2Search();
				break;

			case R.id.base_theme_photo_layout_share:
				showShareDialog();
				break;

			case R.id.base_theme_photo_layout_join:
				showUploadDialog();
				break;

			case R.id.base_theme_photo_layout_comment:
				forwardCommentList();
				break;

			default:
				break;
		}

	}

	@Override
	public void onPullDownRefresh(AbsListView pullRefreshView)
	{
		loadPhotoInfoDatas();
	}

	@Override
	public void onLoadMore(AbsListView pullRefreshView)
	{
		loadThemePhotoList(mThemePhotoListInfo.getCurrentPage() + 1, mThemePhotoListInfo.getLastTimestamp());
	}

	@Override
	public void onRetryLoadDatas()
	{
		loadPhotoInfoDatas();
	}

	@Override
	public void onClickListener(ACTION action, ProfileActionSheetDialog profileActionSheetDialog)
	{
		if (action == ACTION.FIRST_ITEM)
		{
			if (!V2GogoApplication.getMasterLoginState())
			{
				AccountLoginActivity.forwardAccountLogin(this);
			}
			else
			{
				forward2Camera();
			}
		}
		else if (action == ACTION.SECOND_ITEM)
		{
			if (!V2GogoApplication.getMasterLoginState())
			{
				AccountLoginActivity.forwardAccountLogin(this);
			}
			else
			{
				forward2Album();
			}
		}
	}

	@Override
	public void onThemePhotoClickCommand(ThemePhotoInfo themePhotoInfo)
	{
		if (null != themePhotoInfo && null != mTopicInfo)
		{
			if (mTopicInfo.getStatus() == TopicInfo.TOPIC_STATUS_YET_END)
			{
				ToastUtil.showAlertToast(getParent(), R.string.topic_activity_yet_end);
			}
			else
			{
				if (themePhotoInfo.isPraise())
				{
					ToastUtil.showAlertToast(getParent(), R.string.you_yet_command_theme_photo);
				}
				else
				{
					themePhotoInfo.setPraise(true);
					themePhotoInfo.setPraiseNum(themePhotoInfo.getPraiseNum() + 1);
					mHomeThemePhotoAdapter.notifyDataSetChanged();
					ThemePhotoCommandManager.commandThemePhoto(themePhotoInfo.getId(), null);
				}
			}
		}
	}

	@Override
	protected void getCompressPath(Bitmap bitmap)
	{
		super.getCompressPath(bitmap);
		if (null != bitmap)
		{
			try
			{
				boolean result = bitmap.compress(CompressFormat.JPEG, 100, new FileOutputStream(ThemePhotoUploadManager.FILE_PATH));
				if (!bitmap.isRecycled())
				{
					bitmap.recycle();
				}
				if (result)
				{
					forward2Upload(ThemePhotoUploadManager.FILE_PATH);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * 进度回调
	 * 
	 * @param progress
	 */
	public void onEventMainThread(ThemePhotoUploadResultInfo photoUploadResultInfo)
	{
		if (null != photoUploadResultInfo && photoUploadResultInfo.getmThemePhotoInfo() != null)
		{
			if (photoUploadResultInfo.getFlag() != null && photoUploadResultInfo.getFlag().equals(UploadPictureActivity.UPLOAD_SUCCUSS_VIEW))
			{
				mThemePhotoListInfo.addTop(photoUploadResultInfo.getmThemePhotoInfo());
				mHomeThemePhotoAdapter.resetDatas(mThemePhotoListInfo.getThemePhotoListResultInfo().getThemePhotoInfos());
			}
		}
	}

	/**
	 * 加载数据
	 */
	private void loadPhotoInfoDatas()
	{
		loadThemeTopic();
		loadThemePhotoList(ThemePhotoManager.FIRST_PAGE, "0");
	}

	/**
	 * 跳转到上传
	 * 
	 * @param resultPhotoPath
	 */
	private void forward2Upload(String resultPhotoPath)
	{
		// Intent intent = new Intent(this, HomeThemePhotoPostActivity.class);
		Intent intent = new Intent(this, UploadPictureActivity.class);
		intent.putExtra(IntentExtraConstants.PATH, resultPhotoPath);
		intent.putExtra(IntentExtraConstants.TID, mTid);
//		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	@Override
	protected void getAlbumPath(String albumPath)
	{
		super.getAlbumPath(albumPath);
		if (null != albumPath)
		{
			PhotoUtil.forward2Crop(this, Uri.fromFile(new File(albumPath)));
		}
	}

	@Override
	protected void getCameraPath(String cameraPath)
	{
		super.getCameraPath(cameraPath);
		if (cameraPath != null)
		{
			PhotoUtil.cameraCropImageUri(this, Uri.fromFile(new File(cameraPath)));
		}
	}

	/**
	 * 拉取主题信息
	 */
	private void loadThemeTopic()
	{
		if (!TextUtils.isEmpty(mTid))
		{
			ThemePhotoManager.loadThemePhotoTopic(mTid, new IonThemeTopicCallback()
			{
				@Override
				public void onThemeTopicSuccess(TopicInfo topicInfo)
				{
					mTopicInfo = topicInfo;
					if (null != topicInfo)
					{
						if (topicInfo.isAudit())
						{
							mLine.setVisibility(View.VISIBLE);
							mJoinLayout.setVisibility(View.VISIBLE);
						}
						else
						{
							mLine.setVisibility(View.GONE);
							mJoinLayout.setVisibility(View.GONE);
						}
						if (topicInfo.getComments() == 0)
						{
							mTvCommentNum.setVisibility(View.GONE);
						}
						else
						{
							mTvCommentNum.setVisibility(View.VISIBLE);
							mTvCommentNum.setText(topicInfo.getComments() + "");
						}
						((HomeThemePhotoTabActivity) getParent()).mUrl = topicInfo.getPolicy();
						((HomeThemePhotoTabActivity) getParent()).setTitleText(topicInfo.gettTitle());
					}
				}

				@Override
				public void onThemeTopicFail(int code, String message)
				{
					ToastUtil.showAlertToast(getParent(), message);
				}
			});
		}
	}

	/**
	 * 跳转至搜索
	 */
	private void forward2Search()
	{
		Intent intent = new Intent(this, HomeThemePhotoSearchActivity.class);
		intent.putExtra(IntentExtraConstants.TID, mTid);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	/**
	 * 跳转到评论列表
	 */
	private void forwardCommentList()
	{
		if (!TextUtils.isEmpty(mTid))
		{
			Intent intent = new Intent(this, HomeCommentListActivity.class);
			intent.putExtra(HomeCommentListActivity.ARTICE_ID, mTid);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra(HomeCommentListActivity.SRC_TYPE, CommentInfo.SRC_ARTICE_TYPE);
			startActivity(intent);
		}
	}

	/**
	 * 显示上传的对话框
	 */
	private void showUploadDialog()
	{
		if (mUploadActionSheetDialog == null)
		{
			mUploadActionSheetDialog = new ProfileActionSheetDialog(this, R.style.style_action_sheet_dialog);
			mUploadActionSheetDialog.setSheetClickListener(this);
		}
		if (!mUploadActionSheetDialog.isShowing())
		{
			mUploadActionSheetDialog.show();
			mUploadActionSheetDialog.setTips(getString(R.string.please_select_you_theme_photo));
		}
	}

	/**
	 * 获取主题图片列表
	 * 
	 * @param page
	 */
	public void loadThemePhotoList(int page, String timestamp)
	{
		if (TextUtils.isEmpty(mTid))
		{
			return;
		}
		if (isLoadNewest())
		{
			ThemePhotoManager.getNewestThemePhotoList(page, mTid, timestamp, new ThemePhotoListCallback()
			{
				@Override
				public void themePhotoListSuccess(ThemePhotoListInfo themePhotoListInfo)
				{
					displayThemePhotoDatas(themePhotoListInfo);
				}

				@Override
				public void themePhotoListFail(int code, String message)
				{
					displayLoadError(message);
				}
			});
		}
		else
		{
			ThemePhotoManager.getHotestThemePhotoList(page, mTid, new ThemePhotoListCallback()
			{
				@Override
				public void themePhotoListSuccess(ThemePhotoListInfo themePhotoListInfo)
				{
					displayThemePhotoDatas(themePhotoListInfo);
				}

				@Override
				public void themePhotoListFail(int code, String message)
				{
					displayLoadError(message);
				}
			});
		}
	}

	/**
	 * 显示加载失败的提示信息
	 * 
	 * @param message
	 */
	private void displayLoadError(String message)
	{
		ToastUtil.showAlertToast(getParent(), message);
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

	/**
	 * 显示主题照片数据
	 * 
	 * @param themePhotoListInfo
	 */
	private void displayThemePhotoDatas(ThemePhotoListInfo themePhotoListInfo)
	{
		mProgressLayout.showContent();
		if (null != themePhotoListInfo)
		{
			ThemePhotoListResultInfo themePhotoListResultInfo = themePhotoListInfo.getThemePhotoListResultInfo();
			if (themePhotoListResultInfo != null)
			{
				boolean isFinish = false;
				boolean isMore = false;
				if (themePhotoListInfo.getThemePhotoListResultInfo().getPage() == PeopleConcernManager.FIRST_PAGE)
				{
					mThemePhotoListInfo.clear();
				}
				else
				{
					isMore = true;
				}
				if (themePhotoListResultInfo.getPageCount() <= themePhotoListResultInfo.getPage())
				{
					isFinish = true;
				}
				mThemePhotoListInfo.setCurrentPage(themePhotoListResultInfo.getPage());
				mThemePhotoListInfo.addAll(themePhotoListInfo);
				if (null != mPullRefreshListView)
				{
					mHomeThemePhotoAdapter.resetDatas(mThemePhotoListInfo.getThemePhotoListResultInfo().getThemePhotoInfos());
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
	}
}
