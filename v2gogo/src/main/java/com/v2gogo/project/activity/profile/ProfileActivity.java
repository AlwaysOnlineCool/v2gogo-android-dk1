package com.v2gogo.project.activity.profile;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.BaseActivity;
import com.v2gogo.project.activity.WebViewActivity;
import com.v2gogo.project.activity.account.AccountLoginActivity;
import com.v2gogo.project.activity.profile.setting.ProfileSettingActivity;
import com.v2gogo.project.domain.CoinChangeInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.manager.UserInfoManager;
import com.v2gogo.project.manager.profile.ProfileManager;
import com.v2gogo.project.manager.profile.ProfileManager.IonNewTipNoteCallBack;
import com.v2gogo.project.manager.profile.ServicePhoneManager;
import com.v2gogo.project.utils.common.SPUtil;
import com.v2gogo.project.utils.common.ToastUtil;
import com.v2gogo.project.views.listview.refreshview.OnPullRefreshListener;
import com.v2gogo.project.views.listview.refreshview.PullRefreshListView;
import com.v2gogo.project.views.logic.ProfileUserInfoLayout;
import com.ypy.eventbus.EventBus;

/**
 * 个人中心
 * 
 * @author houjun
 */
public class ProfileActivity extends BaseActivity implements OnClickListener, IonNewTipNoteCallBack, OnPullRefreshListener
{

	private ProfileUserInfoLayout mLayoutInfo;
	private PullRefreshListView mPullRefreshListView;

	private RelativeLayout mLayoutOrder;
	private RelativeLayout mLayoutPrize;

	private RelativeLayout mLayoutSetting;
	private RelativeLayout mLayoutMessage;
	private RelativeLayout mLayoutComment;

	private RelativeLayout mLayoutEtcQuan;
	private RelativeLayout mLayoutBussiness;
	private RelativeLayout mLayoutCollection;
	private RelativeLayout mLayoutInviteFriend;
	private RelativeLayout mCallService;

	private ImageView mOrderNew;
	private ImageView mPrizeNew;
	private ImageView mMessageNew;
	private TextView mServicePhone;
	private String mTell;

	@Override
	public void onInitViews()
	{
		mPullRefreshListView = (PullRefreshListView) findViewById(R.id.fragment_profile_pull_to_refresh_listview);
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.profile_activity_layout;
	}

	@Override
	protected boolean isSetting()
	{
		return false;
	}

	@Override
	protected void registerListener()
	{
		super.registerListener();
		EventBus.getDefault().register(this);
		mLayoutInfo.setOnClickListener(this);
		mLayoutOrder.setOnClickListener(this);
		mLayoutPrize.setOnClickListener(this);
		mLayoutSetting.setOnClickListener(this);
		mLayoutMessage.setOnClickListener(this);
		mLayoutEtcQuan.setOnClickListener(this);
		mLayoutComment.setOnClickListener(this);
		mLayoutBussiness.setOnClickListener(this);
		mLayoutCollection.setOnClickListener(this);
		mCallService.setOnClickListener(this);
		mLayoutInviteFriend.setOnClickListener(this);
		mPullRefreshListView.setLoadMoreEnable(false);
		mPullRefreshListView.setOnPullRefreshListener(this);
	}

	@Override
	protected void onInitLoadDatas()
	{
		super.onInitLoadDatas();
		mPullRefreshListView.addHeaderView(createHeaderView());
		mPullRefreshListView.addFooterView(createFooterView());
		mPullRefreshListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, new String[] {}));
	}

	@Override
	public void onNewTipNoteSuccess(boolean isNewPrize, boolean isNewMessage, boolean isNewOrder)
	{
		displayNewIcons(isNewMessage, isNewOrder, isNewPrize);
		if (mPullRefreshListView != null)
		{
			if (mPullRefreshListView.isRefreshing())
			{
				mPullRefreshListView.stopPullRefresh();
			}
		}
	}

	@Override
	public void onNewTipNoteFail(String errorMessage)
	{
		ToastUtil.showAlertToast(getParent(), errorMessage);
		if (mPullRefreshListView != null)
		{
			if (mPullRefreshListView.isRefreshing())
			{
				mPullRefreshListView.stopPullRefresh();
			}
		}
	}

	@Override
	public void onClick(View view)
	{
		Intent intent = null;
		switch (view.getId())
		{
			case R.id.fragment_profile_header_info_layout:
				if (!V2GogoApplication.getMasterLoginState())
				{
					AccountLoginActivity.forwardAccountLogin(this);
				}
				else
				{
					intent = new Intent(this, ProfileInfoActivity.class);
				}
				break;

			case R.id.fragment_profile_header_order_layout:
				if (!V2GogoApplication.getMasterLoginState())
				{
					AccountLoginActivity.forwardAccountLogin(this);
				}
				else
				{
					intent = new Intent(this, ProfileOrderActivity.class);
				}
				break;

			case R.id.fragment_profile_header_prize_layout:
				if (!V2GogoApplication.getMasterLoginState())
				{
					AccountLoginActivity.forwardAccountLogin(this);
				}
				else
				{
					intent = new Intent(this, ProfilePrizeActivity.class);
				}
				break;

			case R.id.fragment_profile_header_comment_layout:
				if (!V2GogoApplication.getMasterLoginState())
				{
					AccountLoginActivity.forwardAccountLogin(this);
				}
				else
				{
					intent = new Intent(this, ProfileCommentTabActivity.class);
				}
				break;

			case R.id.fragment_profile_header_message_layout:
				if (!V2GogoApplication.getMasterLoginState())
				{
					AccountLoginActivity.forwardAccountLogin(this);
				}
				else
				{
					intent = new Intent(this, ProfileMessageActivity.class);
				}
				break;

			case R.id.fragment_profile_footer_setting_layout:
				intent = new Intent(this, ProfileSettingActivity.class);
				break;

			case R.id.fragment_profile_header_collection_layout:
				if (!V2GogoApplication.getMasterLoginState())
				{
					AccountLoginActivity.forwardAccountLogin(this);
				}
				else
				{
					intent = new Intent(this, ProfileCollectionActivity.class);
				}
				break;
			case R.id.fragment_profile_footer_callservice_layout:// 打电话
				// intent = new Intent(this, WebViewActivity.class);
				// intent.putExtra(WebViewActivity.URL,
				// "http://www.v2gogo.com/customerService.html");
				showCallDialog(mTell, "拨打电话");

				break;

			case R.id.fragment_profile_footer_invite_friend_layout:
				intent = new Intent(this, ProfileInviteFriendActivity.class);
				break;

			case R.id.fragment_profile_footer_bussiness_layout:
				intent = new Intent(this, WebViewActivity.class);
				intent.putExtra(WebViewActivity.URL, "http://www.v2gogo.com/business.html");
				break;

			case R.id.fragment_profile_header_quan_layout:// 我的电子卷
				if (!V2GogoApplication.getMasterLoginState())
				{
					AccountLoginActivity.forwardAccountLogin(this);
				}
				else
				{
					intent = new Intent(this, ProfileEtcQuanActivity.class);
				}
				break;

			default:
				break;
		}
		if (null != intent)
		{
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			this.startActivity(intent);
		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		mLayoutInfo.setMatserInfos(this, V2GogoApplication.getCurrentMatser());
		if (V2GogoApplication.getMasterLoginState())
		{
			loadProfileNewTip();
		}
		else
		{
			mPrizeNew.setVisibility(View.GONE);
			mOrderNew.setVisibility(View.GONE);
			mMessageNew.setVisibility(View.GONE);
			mPullRefreshListView.setOnPullEnable(false);
			if (mPullRefreshListView.isRefreshing())
			{
				mPullRefreshListView.stopPullRefresh();
			}
		}
	}

	@Override
	public void onPullDownRefresh(AbsListView pullRefreshView)
	{
		if (V2GogoApplication.getMasterLoginState())
		{
			loadProfileNewTip();
			UserInfoManager.updateUserInfos();
		}
	}

	@Override
	public void clearRequestTask()
	{
		EventBus.getDefault().unregister(this);
	}

	public void onEventMainThread(CoinChangeInfo changeInfo)
	{
		if (mLayoutInfo != null)
		{
			mLayoutInfo.setMatserInfos(ProfileActivity.this, V2GogoApplication.getCurrentMatser());
		}
		if (mPullRefreshListView != null)
		{
			if (mPullRefreshListView.isRefreshing())
			{
				mPullRefreshListView.stopPullRefresh();
			}
		}
	}

	/**
	 * 用户是否有消息
	 */
	private void loadProfileNewTip()
	{
		ProfileManager.getNewTipNote(new IonNewTipNoteCallBack()
		{
			@Override
			public void onNewTipNoteSuccess(boolean isNewPrize, boolean isNewMessage, boolean isNewOrder)
			{
				displayNewIcons(isNewMessage, isNewOrder, isNewPrize);
				if (mPullRefreshListView != null)
				{
					if (mPullRefreshListView.isRefreshing())
					{
						mPullRefreshListView.stopPullRefresh();
					}
				}
			}

			@Override
			public void onNewTipNoteFail(String errorMessage)
			{
				ToastUtil.showAlertToast(getParent(), errorMessage);
				if (mPullRefreshListView != null)
				{
					if (mPullRefreshListView.isRefreshing())
					{
						mPullRefreshListView.stopPullRefresh();
					}
				}
				isLoginView();
			}
		});
		mPullRefreshListView.setOnPullEnable(true);
	}
	
	
	private void isLoginView()
	{
		if (!V2GogoApplication.getMasterLoginState())
		{
			if (mPullRefreshListView!=null)
			{
				mPrizeNew.setVisibility(View.GONE);
				mOrderNew.setVisibility(View.GONE);
				mMessageNew.setVisibility(View.GONE);
				mPullRefreshListView.setOnPullEnable(false);
				if (mPullRefreshListView.isRefreshing())
				{
					mPullRefreshListView.stopPullRefresh();
				}
			}
			if (mLayoutInfo!=null)
			{
				mLayoutInfo.setMatserInfos(ProfileActivity.this, V2GogoApplication.getCurrentMatser());
			}
		}
	}

	/**
	 * 创建头部视图
	 * 
	 * @return
	 */
	private View createHeaderView()
	{
		View headerView = LayoutInflater.from(this).inflate(R.layout.profile_activity_header, null);
		mOrderNew = (ImageView) headerView.findViewById(R.id.fragment_profile_header_order_new);
		mPrizeNew = (ImageView) headerView.findViewById(R.id.fragment_profile_header_prize_new);
		mMessageNew = (ImageView) headerView.findViewById(R.id.fragment_profile_header_message_new);
		mLayoutOrder = (RelativeLayout) headerView.findViewById(R.id.fragment_profile_header_order_layout);
		mLayoutPrize = (RelativeLayout) headerView.findViewById(R.id.fragment_profile_header_prize_layout);
		mLayoutEtcQuan = (RelativeLayout) headerView.findViewById(R.id.fragment_profile_header_quan_layout);
		mLayoutMessage = (RelativeLayout) headerView.findViewById(R.id.fragment_profile_header_message_layout);
		mLayoutComment = (RelativeLayout) headerView.findViewById(R.id.fragment_profile_header_comment_layout);
		mLayoutInfo = (ProfileUserInfoLayout) headerView.findViewById(R.id.fragment_profile_header_info_layout);
		mLayoutCollection = (RelativeLayout) headerView.findViewById(R.id.fragment_profile_header_collection_layout);
		return headerView;
	}

	/**
	 * 创建页脚视图
	 * 
	 * @return
	 */
	private View createFooterView()
	{
		View footerView = LayoutInflater.from(this).inflate(R.layout.profile_activity_footer, null);
		mLayoutSetting = (RelativeLayout) footerView.findViewById(R.id.fragment_profile_footer_setting_layout);
		mLayoutBussiness = (RelativeLayout) footerView.findViewById(R.id.fragment_profile_footer_bussiness_layout);
		mLayoutInviteFriend = (RelativeLayout) footerView.findViewById(R.id.fragment_profile_footer_invite_friend_layout);
		mCallService = (RelativeLayout) footerView.findViewById(R.id.fragment_profile_footer_callservice_layout);
		mServicePhone = (TextView) footerView.findViewById(R.id.profile_service_phone_value);
		mTell = (String) SPUtil.get(this, ServicePhoneManager.SERVICE_PHONE, "0851-84727618");
		mServicePhone.setText("工作日   9:00 ~ 18:00");
		return footerView;
	}

	private void displayNewIcons(boolean isMessageNew, boolean isOrderNew, boolean isPrizeNew)
	{
		if (!isMessageNew)
		{
			mMessageNew.setVisibility(View.GONE);
		}
		else
		{
			mMessageNew.setVisibility(View.VISIBLE);
		}
		if (isPrizeNew)
		{
			mPrizeNew.setVisibility(View.VISIBLE);
		}
		else
		{
			mPrizeNew.setVisibility(View.GONE);
		}
		if (isOrderNew)
		{
			mOrderNew.setVisibility(View.VISIBLE);
		}
		else
		{
			mOrderNew.setVisibility(View.GONE);
		}
	}
}
