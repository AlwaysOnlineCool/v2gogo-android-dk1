package com.v2gogo.project.activity.shop;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsoluteLayout;
import android.widget.ImageButton;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.BaseActivity;
import com.v2gogo.project.activity.account.AccountLoginActivity;
import com.v2gogo.project.domain.ShareInfo;
import com.v2gogo.project.domain.shop.CartItemInfo;
import com.v2gogo.project.domain.shop.GoodsDetailsInfo;
import com.v2gogo.project.domain.shop.GoodsInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.manager.ShareAddManager;
import com.v2gogo.project.manager.profile.ProfileCollectionsManager;
import com.v2gogo.project.manager.profile.ProfileCollectionsManager.IonAddCollectionsCallback;
import com.v2gogo.project.manager.shop.GoodsDetailsManager;
import com.v2gogo.project.utils.common.ToastUtil;
import com.v2gogo.project.utils.share.CustomPlatformActionListener;
import com.v2gogo.project.utils.share.ShareUtils;
import com.v2gogo.project.views.ProgressLayout.IonRetryLoadDatasCallback;
import com.v2gogo.project.views.dialog.BuyGoodsActionSheet;
import com.v2gogo.project.views.dialog.BuyGoodsActionSheet.IonSelectNumCallback;
import com.v2gogo.project.views.dialog.V2gogoShareDialog;
import com.v2gogo.project.views.dialog.V2gogoShareDialog.IonItemClickCallback;
import com.v2gogo.project.views.dialog.V2gogoShareDialog.SHARE_TYPE;
import com.v2gogo.project.views.logic.ActionBarPopupWindow;
import com.v2gogo.project.views.logic.ActionBarPopupWindow.IonClickItemListener;
import com.v2gogo.project.views.webview.ProgressWebView;
import com.v2gogo.project.views.webview.ProgressWebView.IonReceiveTitleCallback;

/**
 * 商品详情
 * 修改：bluesky 2015-12-08
 * 
 * @author houjun
 */
public class GroupGoodsDetailsActivity extends BaseActivity implements OnClickListener, IonItemClickCallback, IonReceiveTitleCallback,
		IonRetryLoadDatasCallback, IonSelectNumCallback
{

	// public static final String GOODS_ID = "goods_id";
	public static final String GOODS_URL = "goods_url";// 商品链接地址

	private ProgressWebView mProgressWebView;
	private String mGoodsId;

	private GoodsDetailsInfo mGoodsDetailsInfo;
	private AbsoluteLayout mAbsoluteLayout;
	private ActionBarPopupWindow mBarPopupWindow;
	private BuyGoodsActionSheet mBuyGoodsActionSheet;

	private ImageButton mIbtnMore;
	private String mUrl;

	private V2gogoShareDialog mShareDialog;
	private ShareInfo mShareInfo;

	@Override
	public void onInitViews()
	{
		mProgressWebView = (ProgressWebView) findViewById(R.id.webview_activity_webview);
		mAbsoluteLayout = (AbsoluteLayout) findViewById(R.id.webview_activity_webview_container);

		mIbtnMore = (ImageButton) findViewById(R.id.goods_details_layout_btn_more);

	}

	// private void initHeaderView()
	// {
	// View view = LayoutInflater.from(this).inflate(R.layout.webview, null);
	// mProgressWebView = (ProgressWebView) view.findViewById(R.id.webview_activity_webview);
	// mAbsoluteLayout = (AbsoluteLayout)
	// view.findViewById(R.id.webview_activity_webview_container);
	// if (mPullRefreshListView != null)
	// {
	// mPullRefreshListView.addHeaderView(view);
	// mPullRefreshListView.setAdapter(new ArrayAdapter<String>(this,
	// android.R.layout.simple_list_item_2, new String[] {}));
	// }
	// }

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.goods_details_activity_layout;
	}

	@Override
	protected void onInitLoadDatas()
	{
		super.onInitLoadDatas();
		mProgressWebView.loadUrl(mUrl);
		mGoodsDetailsInfo = new GoodsDetailsInfo();
	}

	@Override
	protected boolean isNotifyIntent()
	{
		return true;
	}

	@Override
	protected void registerListener()
	{
		super.registerListener();
		mProgressWebView.setOnReceiveTitleCallback(this);
		mIbtnMore.setOnClickListener(this);
	}

	@Override
	protected void onInitIntentData(Intent intent)
	{
		super.onInitIntentData(intent);
		if (null != intent)
		{
			mUrl = intent.getStringExtra(GOODS_URL);// 商品详情地址
			mShareInfo = new ShareInfo();
			mShareInfo.setHref(mUrl);
			if (!TextUtils.isEmpty(mUrl))
			{
				try
				{
					Uri uri = Uri.parse(mUrl);
					mGoodsId = uri.getQueryParameter("id");
					mShareInfo.setTargedId(mGoodsId);
				}
				catch (Exception e)
				{
				}
			}
		}
	}

	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.goods_details_layout_btn_more:
				clikeMore();
				break;

			default:
				break;
		}
	}

	@Override
	public void clearRequestTask()
	{
		GoodsDetailsManager.clearGoodsDetailsInfosTask();
		mAbsoluteLayout.removeView(mProgressWebView);
		mProgressWebView.destoryWebview();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		mProgressWebView.resumeWebview();
	}

	@Override
	protected void onPause()
	{
		mProgressWebView.pauseWebview();
		super.onPause();
	}

	@Override
	public void onRetryLoadDatas()
	{
	}

	@Override
	public void onYetSelectNum(int goodsNum, GoodsInfo goodsInfo)
	{
		if (null != goodsInfo)
		{
			CartItemInfo cartItemInfo = new CartItemInfo();
			cartItemInfo.setBuyNum(goodsNum);
			cartItemInfo.setGoodsInfo(goodsInfo);
			cartItemInfo.setSupportPayMethod(goodsInfo.getPayMethodSupport());
			Intent intent = new Intent(this, CommitOrderActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra(CommitOrderActivity.ORDER_ITEM_INFO, cartItemInfo);
			startActivity(intent);
		}
	}

	/**
	 * 点击购买
	 */
	private void clickBuy()
	{
		if (V2GogoApplication.getMasterLoginState())
		{
			if (null != mGoodsDetailsInfo && null != mGoodsDetailsInfo.getGoodsInfo())
			{
				if (mGoodsDetailsInfo.getGoodsInfo().getStatus() == 2)
				{
					if (mBuyGoodsActionSheet == null)
					{
						mBuyGoodsActionSheet = new BuyGoodsActionSheet(this, R.style.style_action_sheet_dialog);
						mBuyGoodsActionSheet.setOnSelectNumCallback(this);
					}
					if (!mBuyGoodsActionSheet.isShowing())
					{
						mBuyGoodsActionSheet.show();
						mBuyGoodsActionSheet.setGoodsData(mGoodsDetailsInfo.getGoodsInfo());
					}
				}
				else
				{
					ToastUtil.showAlertToast(this, R.string.goods_details_yet_no_buy_tip);
				}
			}
		}
		else
		{
			AccountLoginActivity.forwardAccountLogin(this);
		}
	}

	/**
	 * method desc：弹出分享对话框
	 */
	public void share()
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

	/**
	 * 点击更多
	 */
	private void clikeMore()
	{
		if (null == mBarPopupWindow)
		{
			mBarPopupWindow = new ActionBarPopupWindow(this);
			mBarPopupWindow.setClickItemListener(new IonClickItemListener()
			{
				@Override
				public void onPopupWindowClick(int position)
				{
					if (position == 1)// 商品分类
					{
						share();
					}
					if (position == 2)
					{
						if (!V2GogoApplication.getMasterLoginState())
						{
							AccountLoginActivity.forwardAccountLogin(GroupGoodsDetailsActivity.this);
						}
						if (null != mGoodsId)
						{
							ProfileCollectionsManager.AddCollectionsById(mGoodsId, ProfileCollectionsManager.COLLECTION_TYPE_PRODUCT,
									new IonAddCollectionsCallback()
									{
										@Override
										public void onAddCollectionsSuccess()
										{
											ToastUtil.showConfirmToast(GroupGoodsDetailsActivity.this, R.string.add_collections_tip);
										}

										@Override
										public void onAddCollectionsFail(String errorMessage)
										{
											ToastUtil.showAlertToast(GroupGoodsDetailsActivity.this, errorMessage);
										}
									});
						}
					}
				}
			});
		}
		if (!mBarPopupWindow.isShowing())
		{
			mBarPopupWindow.show(mIbtnMore, 0, 0);
		}
	}

	@Override
	public void onShareClick(SHARE_TYPE type)
	{

		String tip = getResources().getString(R.string.share_success_tip);
		com.v2gogo.project.utils.share.ShareUtils.SHARE_TYPE shareType = com.v2gogo.project.utils.share.ShareUtils.SHARE_TYPE.SHARE_WEIXIN;
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
		ShareUtils.share(this, mShareInfo.getTitle(), mShareInfo.getDescription(), mShareInfo.getHref(), mShareInfo.getImageUrl(), shareType,
				new CustomPlatformActionListener(this, tip));

		// 分享商品记录
		ShareAddManager.shareGoodsAdd(mShareInfo.getTargedId());
	}

	@Override
	public void onReceiveTitle(String title)
	{
		mShareInfo.setTitle(title);
		mShareInfo.setDescription(title);
	}

}
