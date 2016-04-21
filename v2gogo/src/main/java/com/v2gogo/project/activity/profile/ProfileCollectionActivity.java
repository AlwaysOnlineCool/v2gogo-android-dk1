package com.v2gogo.project.activity.profile;

import android.content.Intent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.BaseActivity;
import com.v2gogo.project.activity.home.BaseDetailsctivity;
import com.v2gogo.project.activity.home.HomeArticleDetailsActivity;
import com.v2gogo.project.activity.shop.GroupGoodsDetailsActivity;
import com.v2gogo.project.adapter.profile.ProfileCollectionsAdapter;
import com.v2gogo.project.domain.profile.CollectionsInfo;
import com.v2gogo.project.domain.profile.CollectionsListInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.manager.profile.ProfileCollectionsManager;
import com.v2gogo.project.manager.profile.ProfileCollectionsManager.IonCancelCollectionsCallback;
import com.v2gogo.project.manager.profile.ProfileCollectionsManager.IonProfileCollectionsListCallback;
import com.v2gogo.project.manager.profile.ProfileMessageManager;
import com.v2gogo.project.utils.common.ToastUtil;
import com.v2gogo.project.views.ProgressLayout;
import com.v2gogo.project.views.ProgressLayout.IonRetryLoadDatasCallback;
import com.v2gogo.project.views.ProgressLayout.State;
import com.v2gogo.project.views.dialog.ProfileActionSheetDialog;
import com.v2gogo.project.views.dialog.ProfileActionSheetDialog.ACTION;
import com.v2gogo.project.views.dialog.ProfileActionSheetDialog.IonActionSheetClickListener;
import com.v2gogo.project.views.listview.refreshview.OnLoadMoreListener;
import com.v2gogo.project.views.listview.refreshview.OnPullRefreshListener;
import com.v2gogo.project.views.listview.refreshview.PullRefreshListView;

/**
 * 我的收藏
 * 
 * @author houjun
 */
public class ProfileCollectionActivity extends BaseActivity implements OnLoadMoreListener, OnPullRefreshListener, OnItemClickListener,
		IonActionSheetClickListener, OnItemLongClickListener, IonRetryLoadDatasCallback
{

	private ProgressLayout mProgressLayout;

	private CollectionsListInfo mCollectionsListInfo;
	private PullRefreshListView mPullRefreshListView;
	private ProfileCollectionsAdapter mCollectionsAdapter;
	private ProfileActionSheetDialog mProfileActionSheetDialog;

	private CollectionsInfo mCollectionsInfo;

	@Override
	public void onInitViews()
	{
		mProgressLayout = (ProgressLayout) findViewById(R.id.profile_collections_progress_layout);
		mPullRefreshListView = (PullRefreshListView) findViewById(R.id.profile_collections_pull_to_refresh_listview);
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.profile_collection_activity_layout;
	}

	@Override
	protected void onInitLoadDatas()
	{
		super.onInitLoadDatas();
		mProgressLayout.showProgress();
		mCollectionsListInfo = new CollectionsListInfo();
		mCollectionsAdapter = new ProfileCollectionsAdapter(this);
		mPullRefreshListView.setAdapter(mCollectionsAdapter);
		loadProfileCollectionsList(ProfileCollectionsManager.FIRST_PAGE);
	}

	@Override
	protected void registerListener()
	{
		super.registerListener();
		mPullRefreshListView.setOnLoadMoreListener(this);
		mPullRefreshListView.setLoadMoreEnable(false);
		mProgressLayout.setOnTryLoadDatasCallback(this);
		mPullRefreshListView.setOnPullRefreshListener(this);
		mPullRefreshListView.setOnItemClickListener(this);
		mPullRefreshListView.setOnItemLongClickListener(this);
	}

	@Override
	public void onLoadMore(AbsListView pullRefreshView)
	{
		loadProfileCollectionsList(mCollectionsListInfo.getCurrentPage() + 1);
	}

	@Override
	public void onPullDownRefresh(AbsListView pullRefreshView)
	{
		loadProfileCollectionsList(ProfileCollectionsManager.FIRST_PAGE);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
	{
		if (null != mCollectionsListInfo && null != mCollectionsListInfo.getCollectionsInfos() && position >= 1)
		{
			CollectionsInfo collectionsInfo = mCollectionsListInfo.getCollectionsInfos().get(position - 1);
			if (null != collectionsInfo)
			{
				Intent intent = null;
				switch (collectionsInfo.getType())
				{
					case CollectionsInfo.TYPE_ARTICE:
						intent = new Intent(this, HomeArticleDetailsActivity.class);
						intent.putExtra(BaseDetailsctivity.ID, collectionsInfo.getSrcId());
						startActivity(intent);
						break;

					case CollectionsInfo.TYPE_GOODS:
						StringBuilder url = new StringBuilder();
						url.append(ServerUrlConfig.SERVER_URL).append("/product/groupProductDetail?id=").append(collectionsInfo.getSrcId());

						intent = new Intent(this, GroupGoodsDetailsActivity.class);
						intent.putExtra(GroupGoodsDetailsActivity.GOODS_URL, url.toString());
						startActivity(intent);
						break;

					default:
						break;
				}
			}
		}
	}

	@Override
	public void onClickListener(ACTION action, ProfileActionSheetDialog profileActionSheetDialog)
	{
		if (action == ACTION.FIRST_ITEM)
		{
			if (null != mCollectionsInfo && V2GogoApplication.getMasterLoginState())
			{
				cancelProfileCollections();
			}
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3)
	{
		if (null != mCollectionsListInfo && null != mCollectionsListInfo.getCollectionsInfos() && position >= 1)
		{
			if (null == mProfileActionSheetDialog)
			{
				mProfileActionSheetDialog = new ProfileActionSheetDialog(this, R.style.style_action_sheet_dialog);
				mProfileActionSheetDialog.setSheetClickListener(this);
			}
			if (!mProfileActionSheetDialog.isShowing())
			{
				mProfileActionSheetDialog.show();
				mCollectionsInfo = mCollectionsListInfo.getCollectionsInfos().get(position - 1);
				mProfileActionSheetDialog.setTips(getString(R.string.you_sure_cancel_collections_tip), getString(R.string.cancel_collections_tip), null);
			}
		}
		return true;
	}

	@Override
	public void clearRequestTask()
	{
		ProfileCollectionsManager.clearCancleCollectionsTask();
		ProfileCollectionsManager.clearProfileCollectionsTask();
	}

	@Override
	public void onRetryLoadDatas()
	{
		loadProfileCollectionsList(ProfileCollectionsManager.FIRST_PAGE);
	}

	/**
	 * 取消用户收藏
	 */
	private void cancelProfileCollections()
	{
		ProfileCollectionsManager.cancelCollectionsById(mCollectionsInfo.getId(), V2GogoApplication.getCurrentMatser().getUsername(),
				new IonCancelCollectionsCallback()
				{
					@Override
					public void onCancelCollectionsSuccess(String id)
					{
						mCollectionsInfo = null;
						ToastUtil.showConfirmToast(ProfileCollectionActivity.this, R.string.cancel_collections_success_tip);
						if (null != mCollectionsListInfo && null != mCollectionsListInfo.getCollectionsInfos())
						{
							CollectionsInfo collectionsInfoEx = null;
							for (CollectionsInfo collectionsInfo : mCollectionsListInfo.getCollectionsInfos())
							{
								if (null != collectionsInfo)
								{
									if (id.equals(collectionsInfo.getId()))
									{
										collectionsInfoEx = collectionsInfo;
										break;
									}
								}
							}
							if (null != collectionsInfoEx)
							{
								mCollectionsListInfo.getCollectionsInfos().remove(collectionsInfoEx);
								mCollectionsAdapter.resetDatas(mCollectionsListInfo.getCollectionsInfos());
							}
						}
					}

					@Override
					public void onCancelCollectionsFail(String errorMessage)
					{
						mCollectionsInfo = null;
						ToastUtil.showAlertToast(ProfileCollectionActivity.this, errorMessage);
					}
				});
	}

	/**
	 * 拉取收藏列表
	 * 
	 * @param page
	 */
	private void loadProfileCollectionsList(int page)
	{
		ProfileCollectionsManager.getProfileCollectionsList(page, new IonProfileCollectionsListCallback()
		{
			@Override
			public void onProfileCollectionsListSuccess(CollectionsListInfo collectionsListInfo)
			{
				displayCollectionsDatas(collectionsListInfo);
			}

			@Override
			public void onProfileCollectionsListFail(String errorMessage)
			{
				ToastUtil.showAlertToast(ProfileCollectionActivity.this, errorMessage);
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
	 * 显示收藏数据
	 * 
	 * @param collectionsListInfo
	 */
	private void displayCollectionsDatas(CollectionsListInfo collectionsListInfo)
	{
		if (null != collectionsListInfo)
		{
			boolean isFinish = false;
			boolean isMore = false;
			if (collectionsListInfo.getCurrentPage() == ProfileMessageManager.FIRST_PAGE)
			{
				mCollectionsListInfo.clear();
			}
			else
			{
				isMore = true;
			}
			if (collectionsListInfo.getCount() <= collectionsListInfo.getCurrentPage())
			{
				isFinish = true;
			}
			mCollectionsListInfo.setCurrentPage(collectionsListInfo.getCurrentPage());
			mCollectionsListInfo.addAll(collectionsListInfo);
			mCollectionsAdapter.resetDatas(mCollectionsListInfo.getCollectionsInfos());
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
			if (mCollectionsAdapter.getCount() == 0)
			{
				mProgressLayout.showEmptyText();
			}
			else
			{
				mProgressLayout.showContent();
			}
		}
	}
}
