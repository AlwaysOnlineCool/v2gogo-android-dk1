package com.v2gogo.project.activity.home.theme;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.TargetApi;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.BaseActivity;
import com.v2gogo.project.adapter.home.HomeThemePhotoAdapter;
import com.v2gogo.project.adapter.home.HomeThemePhotoAdapter.IonThemePhotoClickCommandCallback;
import com.v2gogo.project.domain.home.theme.ThemePhotoInfo;
import com.v2gogo.project.manager.home.theme.ThemePhotoCommandManager;
import com.v2gogo.project.manager.home.theme.ThemePhotoSearchManager;
import com.v2gogo.project.manager.home.theme.ThemePhotoSearchManager.IonSearchThemePhotoCallback;
import com.v2gogo.project.utils.common.KeyBoardUtil;
import com.v2gogo.project.utils.common.ToastUtil;
import com.v2gogo.project.utils.http.IntentExtraConstants;
import com.v2gogo.project.views.ProgressLayout;
import com.v2gogo.project.views.listview.refreshview.PullRefreshListView;

/**
 * 主题列表搜索
 * 
 * @author houjun
 */
public class HomeThemePhotoSearchActivity extends BaseActivity implements OnEditorActionListener, OnClickListener, IonThemePhotoClickCommandCallback
{

	private Button mBtnSearch;
	private EditText mEditText;

	private ProgressLayout mProgressLayout;
	private PullRefreshListView mRefreshListView;

	private HomeThemePhotoAdapter mThemePhotoAdapter;
	private String mTid;

	@Override
	public void clearRequestTask()
	{
		ThemePhotoSearchManager.clearSearchThemePhotoBySnTask();
	}

	@Override
	@TargetApi(11)
	protected void onInitIntentData(Intent intent)
	{
		super.onInitIntentData(intent);
		if (intent != null)
		{
			mTid = intent.getStringExtra(IntentExtraConstants.TID);
		}
	}

	@Override
	public void onInitViews()
	{
		mEditText = (EditText) findViewById(R.id.home_theme_photo_search_input);
		mBtnSearch = (Button) findViewById(R.id.home_theme_photo_cancel_search_btn);
		mProgressLayout = (ProgressLayout) findViewById(R.id.home_theme_photo_search_progress_layout);
		mRefreshListView = (PullRefreshListView) findViewById(R.id.home_theme_photo_search_pull_list_view);
		displaySearchKeyBoard();
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.home_theme_photo_search_activity_layout;
	}

	@Override
	protected void registerListener()
	{
		super.registerListener();
		mBtnSearch.setOnClickListener(this);
		mEditText.setOnEditorActionListener(this);
		mThemePhotoAdapter.setThemePhotoClickCommandCallback(this);
	}

	@Override
	protected void onPause()
	{
		KeyBoardUtil.closeKeybord(mEditText, this);
		super.onPause();
	}

	@Override
	protected void onInitLoadDatas()
	{
		super.onInitLoadDatas();
		mThemePhotoAdapter = new HomeThemePhotoAdapter(this);
		mRefreshListView.setAdapter(mThemePhotoAdapter);
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
	{
		if (actionId == EditorInfo.IME_ACTION_SEARCH)
		{
			KeyBoardUtil.closeKeybord(mEditText, HomeThemePhotoSearchActivity.this);
			String photoNo = mEditText.getText().toString();
			if (TextUtils.isEmpty(photoNo))
			{
				ToastUtil.showAlertToast(HomeThemePhotoSearchActivity.this, R.string.search_content_is_empty_tip);
			}
			else
			{
				mProgressLayout.showProgress();
				photoNo = photoNo.replace("No:", "").replace("no:", "").replace("No：", "").replace("no：", "").trim();
				searchThemePhoto(photoNo);
			}
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View view)
	{
		finish();
	}

	@Override
	public void onThemePhotoClickCommand(ThemePhotoInfo themePhotoInfo)
	{
		if (null != themePhotoInfo)
		{
			if (themePhotoInfo.isPraise())
			{
				ToastUtil.showAlertToast(getParent(), R.string.you_yet_command_theme_photo);
			}
			else
			{
				themePhotoInfo.setPraise(true);
				themePhotoInfo.setPraiseNum(themePhotoInfo.getPraiseNum() + 1);
				mThemePhotoAdapter.notifyDataSetChanged();
				ThemePhotoCommandManager.commandThemePhoto(themePhotoInfo.getId(), null);
			}
		}
	}

	/**
	 * 搜索主题图片
	 * 
	 * @param photoNo
	 */
	private void searchThemePhoto(String photoNo)
	{
		if (!TextUtils.isEmpty(mTid))
		{
			ThemePhotoSearchManager.searchThemePhotoBySn(photoNo, mTid, new IonSearchThemePhotoCallback()
			{
				@Override
				public void onSearchThemePhotoSuccess(ThemePhotoInfo themePhotoInfo)
				{
					mProgressLayout.showContent();
					if (null != themePhotoInfo)
					{
						List<ThemePhotoInfo> themePhotoInfos = new ArrayList<ThemePhotoInfo>();
						themePhotoInfos.add(themePhotoInfo);
						mThemePhotoAdapter.resetDatas(themePhotoInfos);
					}
				}

				@Override
				public void onSearchThemePhotoFail(int code, String errorMessage)
				{
					ToastUtil.showAlertToast(HomeThemePhotoSearchActivity.this, errorMessage);
					mProgressLayout.showErrorText(errorMessage);
				}
			});
		}
	}

	/**
	 * 显示搜索键盘
	 */
	private void displaySearchKeyBoard()
	{
		mEditText.setFocusable(true);
		mEditText.setFocusableInTouchMode(true);
		mEditText.requestFocus();
		new Timer().schedule(new TimerTask()
		{
			public void run()
			{
				runOnUiThread(new Runnable()
				{
					public void run()
					{
						KeyBoardUtil.openKeybord(mEditText, HomeThemePhotoSearchActivity.this);
					}
				});
			}

		}, 200);
	}
}
