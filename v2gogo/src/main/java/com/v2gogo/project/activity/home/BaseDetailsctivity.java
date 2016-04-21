package com.v2gogo.project.activity.home;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.widget.AbsListView;
import android.widget.AbsoluteLayout;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import cn.sharesdk.framework.ShareSDK;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.BaseActivity;
import com.v2gogo.project.activity.account.AccountLoginActivity;
import com.v2gogo.project.activity.home.HomeArticleDetailsActivity.MyChromeClient;
import com.v2gogo.project.activity.home.HomeArticleDetailsActivity.MyWebviewCient;
import com.v2gogo.project.adapter.home.HomeArticleDetailsAdapter;
import com.v2gogo.project.adapter.home.HomeArticleDetailsAdapter.IonCommandClick;
import com.v2gogo.project.adapter.home.HomeArticleDetailsAdapter.IonMoreClickListener;
import com.v2gogo.project.domain.ShareInfo;
import com.v2gogo.project.domain.home.ArticleDetailsInfo;
import com.v2gogo.project.domain.home.CommentInfo;
import com.v2gogo.project.domain.home.CommentListInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.manager.ShareAddManager;
import com.v2gogo.project.manager.home.CommandManager;
import com.v2gogo.project.manager.home.CommandManager.IoncommandCommentCallback;
import com.v2gogo.project.manager.home.CommentManager;
import com.v2gogo.project.manager.home.CommentManager.IonCommentListCallback;
import com.v2gogo.project.manager.home.PeopleConcernManager;
import com.v2gogo.project.manager.profile.ProfileCollectionsManager;
import com.v2gogo.project.manager.profile.ProfileCollectionsManager.IonAddCollectionsCallback;
import com.v2gogo.project.utils.common.SPUtil;
import com.v2gogo.project.utils.common.ToastUtil;
import com.v2gogo.project.utils.share.CustomPlatformActionListener;
import com.v2gogo.project.utils.share.ShareUtils;
import com.v2gogo.project.views.ProgressLayout;
import com.v2gogo.project.views.dialog.V2gogoShareDialog;
import com.v2gogo.project.views.dialog.V2gogoShareDialog.IonItemClickCallback;
import com.v2gogo.project.views.dialog.V2gogoShareDialog.SHARE_TYPE;
import com.v2gogo.project.views.expandablelistview.PullExpandableListview;
import com.v2gogo.project.views.listview.refreshview.OnPullRefreshListener;
import com.v2gogo.project.views.logic.DetailsBottomOparationLayout;
import com.v2gogo.project.views.logic.DetailsBottomOparationLayout.ACTION;
import com.v2gogo.project.views.logic.DetailsBottomOparationLayout.IonClickOperationListener;
import com.v2gogo.project.views.webview.JsWebview;
import com.ypy.eventbus.EventBus;

/**
 * 有评论列表的详情界面
 * 
 * @author houjun
 */
public abstract class BaseDetailsctivity extends BaseActivity implements IonClickOperationListener, IonItemClickCallback, IonCommandClick,
		IonMoreClickListener, OnGroupClickListener, OnChildClickListener, OnPullRefreshListener
{
	public static final String ID = "id";

	public static final String COMMENT = "comment";
	public static final String ADD_COMMENT = "add_comment";
	public static final String DELETE_COMMENT = "delete_comment";

	private final int REQUEST_CODE_COMMENT_LIST = 0X100;
	protected final int REQUEST_CODE_PUBLISH_COMMENT = 0X101;
	protected int mTotalComment;// 评论总数

	/**
	 * 加载头部视图
	 */
	public abstract View getHeaderView();

	/**
	 * 评论是否可以发图片
	 * 
	 * @return
	 */
	public abstract boolean isPublishImage();

	/**
	 * 得到分享的参数
	 * 
	 * @return
	 */
	public abstract ShareInfo getShareInfo();

	/**
	 * 加载数据
	 */
	public abstract void loadNetData();

	public abstract int getSrcType();

	public abstract int getType();

	protected String mId;

	protected ProgressLayout mProgressLayout;
	private PullExpandableListview mExpandableListview;
	protected DetailsBottomOparationLayout mBottomOparationLayout;

	private HomeArticleDetailsAdapter mArticleDetailsAdapter;

	private V2gogoShareDialog mShareDialog;
	private CommentListInfo mCommentListInfo;
	
	
	private JsWebview mJsWebview;
	private AbsoluteLayout mAbsoluteLayout;
	private ArticleDetailsInfo mArticleDetailsInfo;

	private WebChromeClient chromeClient = null;
	private WebChromeClient.CustomViewCallback myCallBack = null;

	@Override
	public void clearRequestTask()
	{
		CommentManager.clearGetCommentListTask();
	}

	@Override
	public void onInitViews()
	{
		EventBus.getDefault().register(this);
		mProgressLayout = (ProgressLayout) findViewById(R.id.common_details_activity_progress_layout);
		mExpandableListview = (PullExpandableListview) findViewById(R.id.common_details_activity_expandablelistView);
		mBottomOparationLayout = (DetailsBottomOparationLayout) findViewById(R.id.common_details_activity_buttom_operation_layout);
		mExpandableListview.addHeaderView(getHeaderView());
	}
	
	@SuppressLint("NewApi")
	public class MyWebviewCient extends WebViewClient
	{
		@Override
		public WebResourceResponse shouldInterceptRequest(WebView view, String url)
		{
			WebResourceResponse response = null;
			response = super.shouldInterceptRequest(view, url);
			return response;
		}
	}

	public class MyChromeClient extends WebChromeClient
	{

		@Override
		public void onShowCustomView(View view, CustomViewCallback callback)
		{
			Log.d("ddd", "onShowCustomView");
			
		}

		@Override
		public void onHideCustomView()
		{
			Log.d("ddd", "onHideCustomView");
		}

		@Override
		public boolean onConsoleMessage(ConsoleMessage consoleMessage)
		{
			// TODO Auto-generated method stub
			Log.d("ZR", consoleMessage.message() + " at " + consoleMessage.sourceId() + ":" + consoleMessage.lineNumber());
			return super.onConsoleMessage(consoleMessage);
		}
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.details_activity_layout;
	}

	@Override
	protected void onInitIntentData(Intent intent)
	{
		super.onInitIntentData(intent);
		if (intent != null)
		{
			mId = intent.getStringExtra(ID);
		}
	}

	@Override
	protected void onInitLoadDatas()
	{
		super.onInitLoadDatas();
		ShareSDK.initSDK(this);
		if (mId != null)
		{
			loadNetData();
			mProgressLayout.showProgress();
		}
		String collections = (String) SPUtil.get(V2GogoApplication.sIntance, "collections", "");
		if (collections.contains(mId + ","))
		{
			mBottomOparationLayout.setYetCollections();
		}
		mCommentListInfo = new CommentListInfo();
		mArticleDetailsAdapter = new HomeArticleDetailsAdapter(this, mExpandableListview);
		mArticleDetailsAdapter.setOnCommandClick(this);
		mArticleDetailsAdapter.setOnMoreClickListener(this);
		mExpandableListview.setAdapter(mArticleDetailsAdapter);
	}

	@Override
	protected void registerListener()
	{
		super.registerListener();
		mExpandableListview.setOnGroupClickListener(this);
		mExpandableListview.setOnChildClickListener(this);
		mExpandableListview.setOnPullRefreshListener(this);
		mExpandableListview.setLoadMoreEnable(false);
		mBottomOparationLayout.setOnClickOperationListener(this);
	}

	@Override
	public void onCommandClick(CommentInfo commentInfo)
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
							ToastUtil.showAlertToast(BaseDetailsctivity.this, errorMessage);
						}
					});
					mArticleDetailsAdapter.resetDatas(mCommentListInfo.getCommentInfos());
				}
			}
			else
			{
				AccountLoginActivity.forwardAccountLogin(this);
			}
		}
	}

	@Override
	public void onMoreClick(int position)
	{
		Intent intent = new Intent(this, HomeCommentListActivity.class);
		intent.putExtra(HomeCommentListActivity.ARTICE_ID, mId);
		intent.putExtra(HomeCommentListActivity.SRC_TYPE, getSrcType());
		startActivityForResult(intent, REQUEST_CODE_COMMENT_LIST);
	}

	@Override
	public void onClickOperation(ACTION name, View view)
	{
		if (name == ACTION.SHARE)
		{
			if (null == mShareDialog)
			{
				mShareDialog = new V2gogoShareDialog(this, R.style.style_action_sheet_dialog);
				mShareDialog.setItemClickCallback(this);
			}
			if (!mShareDialog.isShowing())
			{
				mShareDialog.show();
			}
		}
		else if (name == ACTION.WRITE_COMMENT)
		{
			if (V2GogoApplication.getMasterLoginState())
			{
				forwardWriteComment();
			}
			else
			{
				AccountLoginActivity.forwardAccountLogin(this);
			}
		}
		else if (name == ACTION.COMMENT)
		{
			forwardCommentList();
		}
		else if (name == ACTION.COLLECTIONS)
		{
			if (!V2GogoApplication.getMasterLoginState())
			{
				AccountLoginActivity.forwardAccountLogin(BaseDetailsctivity.this);
			}
			else
			{
				if (null != mId)
				{
					addCollections();
				}
			}
		}
	}

	@Override
	public void onShareClick(SHARE_TYPE type)
	{
		ShareInfo shareInfo = getShareInfo();
		com.v2gogo.project.utils.share.ShareUtils.SHARE_TYPE shareType = com.v2gogo.project.utils.share.ShareUtils.SHARE_TYPE.SHARE_WEIXIN;
		if (shareInfo != null)
		{
			String tip = getResources().getString(R.string.share_success_tip);
			if (type == SHARE_TYPE.SHARE_WENXI_COLLECTIONS)
			{
				shareType = com.v2gogo.project.utils.share.ShareUtils.SHARE_TYPE.SHARE_WEIXIN_FRIENDS;
			}
			else if (type == SHARE_TYPE.SHARE_QQ)
			{
				shareType = com.v2gogo.project.utils.share.ShareUtils.SHARE_TYPE.SHARE_QQ;
			}
			else if (type == SHARE_TYPE.SHARE_QZONE)
			{
				shareType = com.v2gogo.project.utils.share.ShareUtils.SHARE_TYPE.SHARE_QZONE;
			}
			else if (type == SHARE_TYPE.SHARE_MESSAGE)
			{
				shareType = com.v2gogo.project.utils.share.ShareUtils.SHARE_TYPE.SHARE_MESSAGE;
			}
			ShareUtils.share(this, shareInfo.getTitle(), shareInfo.getDescription(), shareInfo.getHref(), shareInfo.getImageUrl(), shareType,
					new CustomPlatformActionListener(this, tip, shareInfo.getTargedId(), CustomPlatformActionListener.SHARE_ARTICLE_FLAG));

			// 分享记录
			ShareAddManager.shareArticleAdd(shareInfo.getTargedId());
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK)
		{
			if (requestCode == REQUEST_CODE_COMMENT_LIST)
			{
				dealWithCommentListDatas(data);
			}
			else if (requestCode == REQUEST_CODE_PUBLISH_COMMENT)
			{
				refreshCommentDatas(data);
			}
		}
	}

	@Override
	public boolean onGroupClick(ExpandableListView arg0, View arg1, int position, long arg3)
	{
		forwardCommentList();
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
		if (null != mId)
		{
			loadNewestComments();
		}
	}

	/**
	 * 写评论
	 */
	private void forwardWriteComment()
	{
		Intent intent = new Intent(this, HomeCommentActivity.class);
		intent.putExtra(HomeCommentActivity.ARTICE_ID, mId);
		intent.putExtra(HomeCommentActivity.IS_IMAGE, isPublishImage());
		intent.putExtra(HomeCommentActivity.COMMENT_TYPE, getType());
		intent.putExtra(HomeCommentActivity.SRC_TYPE, getSrcType());
		startActivityForResult(intent, REQUEST_CODE_PUBLISH_COMMENT);
	}

	/**
	 * 跳转到评论列表
	 */
	private void forwardCommentList()
	{
		Intent intent = new Intent(this, HomeCommentListActivity.class);
		intent.putExtra(HomeCommentListActivity.ARTICE_ID, mId);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra(HomeCommentListActivity.SRC_TYPE, getSrcType());
		startActivityForResult(intent, REQUEST_CODE_COMMENT_LIST);
	}

	/**
	 * 添加收藏
	 */
	private void addCollections()
	{
		ProfileCollectionsManager.AddCollectionsById(mId, ProfileCollectionsManager.COLLECTION_TYPE_INFO, new IonAddCollectionsCallback()
		{
			@Override
			public void onAddCollectionsSuccess()
			{
				if (null != mBottomOparationLayout)
				{
					mBottomOparationLayout.setYetCollections();
				}
				ToastUtil.showConfirmToast(BaseDetailsctivity.this, R.string.add_collections_tip);
			}

			@Override
			public void onAddCollectionsFail(String errorMessage)
			{
				ToastUtil.showAlertToast(BaseDetailsctivity.this, errorMessage);
			}
		});
	}

	/**
	 * 加载最新评论
	 */
	private void loadNewestComments()
	{
		CommentManager.getNewestCommentList(mId, CommentManager.FIRST_PAGE, new IonCommentListCallback()
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
				ToastUtil.showAlertToast(BaseDetailsctivity.this, errorMessage);
				if (mExpandableListview.isRefreshing())
				{
					mExpandableListview.stopPullRefresh();
				}
				if (mExpandableListview.isLoadingMore())
				{
					mExpandableListview.stopLoadMore();
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
		mArticleDetailsAdapter.resetDatas(mCommentListInfo.getCommentInfos());
		if (isMore)
		{
			mExpandableListview.stopLoadMore();
		}
		else
		{
			mExpandableListview.stopPullRefresh();
		}
		if (isFinish)
		{
			mExpandableListview.setLoadMoreEnable(false);
		}
		else
		{
			mExpandableListview.setLoadMoreEnable(false);
		}
	}

	/**
	 * 加载首页评论数据
	 */
	public void loadFirstPageComments()
	{
		if (mId != null)
		{
			loadNewestComments();
		}
	}

	/**
	 * 刷新评论数据
	 * 
	 * @param data
	 */
	private void refreshCommentDatas(Intent data)
	{
		if (data == null)
		{
			return;
		}
		CommentInfo commentInfo = (CommentInfo) data.getSerializableExtra(COMMENT);
		if (null != commentInfo)
		{
			if (!TextUtils.isEmpty(commentInfo.getSrccont()))
			{
				commentInfo.parseCommentReplyData();
			}
			mCommentListInfo.addTop(commentInfo);
			mArticleDetailsAdapter.resetDatas(mCommentListInfo.getCommentInfos());
			if (mBottomOparationLayout != null)
			{
				mTotalComment = mTotalComment + 1;
				mBottomOparationLayout.setCommentNum(mTotalComment);
			}
		}
	}

	/**
	 * 从评论列表的返回处理
	 * 
	 * @param requestCode
	 * @param data
	 */
	@SuppressWarnings("unchecked")
	private void dealWithCommentListDatas(Intent data)
	{
		if (data != null)
		{
			ArrayList<CommentInfo> addCommentInfos = (ArrayList<CommentInfo>) data.getSerializableExtra(ADD_COMMENT);
			boolean isChange = false;
			if (null != addCommentInfos && addCommentInfos.size() > 0)
			{
				isChange = true;
				for (CommentInfo commentInfo : addCommentInfos)
				{
					if (null != commentInfo)
					{
						mCommentListInfo.addTop(commentInfo);
						mTotalComment = mTotalComment + 1;
					}
				}
			}
			ArrayList<String> strings = (ArrayList<String>) data.getSerializableExtra(DELETE_COMMENT);
			if (null != strings && strings.size() > 0)
			{
				isChange = true;
				ArrayList<CommentInfo> commentInfos = new ArrayList<CommentInfo>();
				for (String str : strings)
				{
					if (mCommentListInfo.getCommentInfos() != null)
					{
						for (CommentInfo info : mCommentListInfo.getCommentInfos())
						{
							if (str.equals(info.getId()))
							{
								commentInfos.add(info);
							}
						}
					}
				}
				if (commentInfos.size() > 0 && null != mCommentListInfo.getCommentInfos())
				{
					for (CommentInfo commentInfo : commentInfos)
					{
						mCommentListInfo.getCommentInfos().remove(commentInfo);
						mTotalComment = mTotalComment - 1;
					}
				}
			}
			if (isChange)
			{
				mArticleDetailsAdapter.resetDatas(mCommentListInfo.getCommentInfos());
				// 处理底部评论数
				if (mBottomOparationLayout != null)
				{
					mBottomOparationLayout.setCommentNum(mTotalComment);
				}
			}
		}
	}
}
