package com.v2gogo.project.activity.home.theme;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.AbsListView;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.BaseActivity;
import com.v2gogo.project.adapter.home.HomeThemePhotoCommandListAdapter;
import com.v2gogo.project.domain.home.theme.ThemePhotoCommandListInfo;
import com.v2gogo.project.manager.home.theme.ThemePhotoCommandManager;
import com.v2gogo.project.manager.home.theme.ThemePhotoCommandManager.IonCommandListThemePhotoCallback;
import com.v2gogo.project.utils.common.ToastUtil;
import com.v2gogo.project.utils.http.IntentExtraConstants;
import com.v2gogo.project.views.ProgressLayout;
import com.v2gogo.project.views.ProgressLayout.IonRetryLoadDatasCallback;
import com.v2gogo.project.views.ProgressLayout.State;
import com.v2gogo.project.views.listview.refreshview.OnLoadMoreListener;
import com.v2gogo.project.views.listview.refreshview.OnPullRefreshListener;
import com.v2gogo.project.views.listview.refreshview.PullRefreshListView;

/**
 * 主题图片点赞列表显示
 * 
 * @author houjun
 */
public class HomeThemePhotoCommandListActivity extends BaseActivity implements OnPullRefreshListener, OnLoadMoreListener, IonRetryLoadDatasCallback
{
	private ProgressLayout mProgressLayout;
	private PullRefreshListView mPullRefreshListView;

	private String mPhotoId;
	private ThemePhotoCommandListInfo mPhotoCommandListInfo;
	private HomeThemePhotoCommandListAdapter mPhotoCommandListAdapter;

	@Override
	public void clearRequestTask()
	{
	}

	@Override
	public void onInitViews()
	{
		mProgressLayout = (ProgressLayout) findViewById(R.id.home_theme_photo_comment_list_progress_layout);
		mPullRefreshListView = (PullRefreshListView) findViewById(R.id.home_theme_photo_comment_list_listview);
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.home_theme_photo_command_list_activity_layout;
	}

	@Override
	protected void registerListener()
	{
		super.registerListener();
		mPullRefreshListView.setOnLoadMoreListener(this);
		mPullRefreshListView.setOnPullRefreshListener(this);
		mProgressLayout.setOnTryLoadDatasCallback(this);
		mPullRefreshListView.setLoadMoreEnable(false);
	}

	@Override
	protected void onInitIntentData(Intent intent)
	{
		super.onInitIntentData(intent);
		if (null != intent)
		{
			mPhotoId = intent.getStringExtra(IntentExtraConstants.PID);
		}
	}

	@Override
	protected void onInitLoadDatas()
	{
		super.onInitLoadDatas();
		mPhotoCommandListAdapter = new HomeThemePhotoCommandListAdapter();
		mPullRefreshListView.setAdapter(mPhotoCommandListAdapter);
		mPhotoCommandListInfo = new ThemePhotoCommandListInfo();
		mProgressLayout.showProgress();
		loadDatas(ThemePhotoCommandManager.FIRST_PAGE);
	}

	@Override
	public void onPullDownRefresh(AbsListView pullRefreshView)
	{
		loadDatas(ThemePhotoCommandManager.FIRST_PAGE);
	}

	@Override
	public void onLoadMore(AbsListView pullRefreshView)
	{
		loadDatas(mPhotoCommandListInfo.getPage() + 1);
	}

	@Override
	public void onRetryLoadDatas()
	{
		loadDatas(ThemePhotoCommandManager.FIRST_PAGE);
	}

	/**
	 * 加载数据
	 * 
	 * @param page
	 */
	private void loadDatas(int page)
	{
		if (!TextUtils.isEmpty(mPhotoId))
		{
			ThemePhotoCommandManager.loadCommandUserList(mPhotoId, page, new IonCommandListThemePhotoCallback()
			{
				@Override
				public void onCommandListThemePhotoSuccess(ThemePhotoCommandListInfo themePhotoCommandListInfo)
				{
					mProgressLayout.showContent();
					if (null != themePhotoCommandListInfo)
					{
						boolean isFinish = false;
						boolean isMore = false;
						if (themePhotoCommandListInfo.getPage() == ThemePhotoCommandManager.FIRST_PAGE)
						{
							mPhotoCommandListInfo.clear();
						}
						else
						{
							isMore = true;
						}
						if (themePhotoCommandListInfo.getPageCount() <= themePhotoCommandListInfo.getPage())
						{
							isFinish = true;
						}
						mPhotoCommandListInfo.setPage(themePhotoCommandListInfo.getPage());
						mPhotoCommandListInfo.addAll(themePhotoCommandListInfo);
						if (null != mPullRefreshListView)
						{
							mPhotoCommandListAdapter.resetDatas(mPhotoCommandListInfo.getmCommandUserInfos());
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

				@Override
				public void onCommandListThemePhotoFail(int code, String message)
				{
					ToastUtil.showAlertToast(HomeThemePhotoCommandListActivity.this, message);
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
	}

}
