package com.v2gogo.project.activity;

import java.util.List;

import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.exchange.ExchangeActivity;
import com.v2gogo.project.adapter.RankAdapter;
import com.v2gogo.project.domain.CoinChangeInfo;
import com.v2gogo.project.domain.RankInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.main.image.GlideImageLoader;
import com.v2gogo.project.manager.CoinRankManager;
import com.v2gogo.project.manager.CoinRankManager.IongetRankCoinListCallback;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.utils.common.ToastUtil;
import com.v2gogo.project.views.ProgressLayout;
import com.v2gogo.project.views.ProgressLayout.IonRetryLoadDatasCallback;
import com.v2gogo.project.views.ProgressLayout.State;
import com.v2gogo.project.views.listview.refreshview.OnPullRefreshListener;
import com.v2gogo.project.views.listview.refreshview.PullRefreshListView;
import com.ypy.eventbus.EventBus;

/* 
 * 金币排行榜
 * @author houjun
 *
 */
public class CoinRankActivity extends BaseActivity implements OnClickListener, OnPullRefreshListener, IonRetryLoadDatasCallback
{
	private PullRefreshListView mPullRefreshListView;
	private RankAdapter mRankAdapter;

	private Button mBtnGet;
	private Button mBtnExchange;

	private ImageView mAvatar;

	private TextView mWeekCoin;
	private TextView mLeftcoin;
	private TextView mNologin;
	private TextView mtextTextView;
	private TextView mTextNickname;

	private LinearLayout mloginLayout;
	private ProgressLayout mProgressLayout;

	@Override
	public void onInitViews()
	{
		mProgressLayout = (ProgressLayout) findViewById(R.id.coin_rank_progress_layout);
		mPullRefreshListView = (PullRefreshListView) findViewById(R.id.coin_rank_pull_to_refresh_listview);
		initHeaderView();
		displayUserInfos();
	}

	private void initHeaderView()
	{
		View view = getLayoutInflater().inflate(R.layout.rank_activity_head_layout, null);
		mBtnGet = (Button) view.findViewById(R.id.rank_head_get);
		mAvatar = (ImageView) view.findViewById(R.id.rank_head_user_avatar);
		mBtnExchange = (Button) view.findViewById(R.id.rank_head_exchange);
		mTextNickname = (TextView) view.findViewById(R.id.rank_head_nickname);
		mtextTextView = (TextView) view.findViewById(R.id.rank_head_account);
		mWeekCoin = (TextView) view.findViewById(R.id.rank_head_week_coin);
		mloginLayout = (LinearLayout) view.findViewById(R.id.rank_login_layout);
		mLeftcoin = (TextView) view.findViewById(R.id.rank_head_left_coin);
		mNologin = (TextView) view.findViewById(R.id.rank_head_no_login);
		mPullRefreshListView.addHeaderView(view);
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.rank_activity_layout;
	}

	@Override
	protected void registerListener()
	{
		super.registerListener();
		mBtnGet.setOnClickListener(this);
		EventBus.getDefault().register(this);
		mBtnExchange.setOnClickListener(this);
		mProgressLayout.setOnTryLoadDatasCallback(this);
		mPullRefreshListView.setOnPullRefreshListener(this);
	}

	@Override
	protected void onInitLoadDatas()
	{
		super.onInitLoadDatas();
		mRankAdapter = new RankAdapter(this);
		mPullRefreshListView.setAdapter(mRankAdapter);
		getRankCoinList();
		mProgressLayout.showProgress();
	}

	@Override
	public void onClick(View view)
	{
		Intent intent = null;
		switch (view.getId())
		{
			case R.id.rank_head_get:
				intent = new Intent(this, WebViewActivity.class);
				intent.putExtra(WebViewActivity.URL, ServerUrlConfig.SERVER_URL + "/get.html");
				break;

			case R.id.rank_head_exchange:
				intent = new Intent(this, ExchangeActivity.class);
				intent.putExtra(ExchangeActivity.SHOW_BACK, true);
				break;

			default:
				break;
		}
		if (null != intent)
		{
			startActivity(intent);
		}
	}

	@Override
	public void clearRequestTask()
	{
		EventBus.getDefault().unregister(this);
		CoinRankManager.clearGetRankCoinListTask();
	}

	@Override
	public void onPullDownRefresh(AbsListView pullRefreshView)
	{
		getRankCoinList();
	}

	@Override
	public void onRetryLoadDatas()
	{
		getRankCoinList();
	}

	@Override
	protected void onResume()
	{
		if (V2GogoApplication.getMasterLoginState())
		{
			displayCoinInfos();
		}
		super.onResume();
	}

	
	public void onEventMainThread(CoinChangeInfo changeInfo)
	{
		if(null != mLeftcoin)
		{
			displayLeftCoin();
		}
	}

	
	/**
	 * 拉取金币排行榜数据
	 */
	private void getRankCoinList()
	{
		CoinRankManager.getRankCoinList(new IongetRankCoinListCallback()
		{
			@Override
			public void onGetRankCoinListSuccess(List<RankInfo> rankInfos)
			{
				mProgressLayout.showContent();
				if (mPullRefreshListView.isRefreshing())
				{
					mPullRefreshListView.stopPullRefresh();
				}
				if (null != rankInfos)
				{
					mRankAdapter.resetDatas(rankInfos);
				}
			}

			@Override
			public void onGetRankCoinListFail(String errorMessage)
			{
				ToastUtil.showAlertToast(CoinRankActivity.this, errorMessage);
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
				}
			}
		});
	}

	/**
	 * 显示用户信息
	 */
	private void displayUserInfos()
	{
		if (V2GogoApplication.getMasterLoginState())
		{
			mNologin.setVisibility(View.GONE);
			mloginLayout.setVisibility(View.VISIBLE);
			mTextNickname.setText(V2GogoApplication.getCurrentMatser().getFullname());
			mtextTextView.setText(String.format(getString(R.string.coin_account_tip), V2GogoApplication.getCurrentMatser().getUsername()));
			GlideImageLoader.loadAvatarImageWithFixedSize(this, V2GogoApplication.getCurrentMatser().getThumbialAvatar(), mAvatar);
		}
		else
		{
			GlideImageLoader.loadInternalDrawable(this, R.drawable.user_icons_user_orange, mAvatar);
			mloginLayout.setVisibility(View.GONE);
			mNologin.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 显示金币信息
	 */
	private void displayCoinInfos()
	{
		displayLeftCoin();
		String str = String.format(getString(R.string.coin_rank_week_tip), V2GogoApplication.getCurrentMatser().getWeekcoin());
		SpannableStringBuilder stringBuilder = new SpannableStringBuilder(str);
		ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(0xFFF96700);
		stringBuilder.setSpan(foregroundColorSpan, 5, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		mWeekCoin.setText(stringBuilder);
	}

	
	/**
	 *  显示剩余金币
	 */
	private void displayLeftCoin()
	{
		String str = String.format(getString(R.string.coin_rank_now_tip), V2GogoApplication.getCurrentMatser().getCoin());
		SpannableStringBuilder stringBuilder = new SpannableStringBuilder(str);
		ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(0xFFF96700);
		stringBuilder.setSpan(foregroundColorSpan, 5, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		mLeftcoin.setText(stringBuilder);
	}
}
