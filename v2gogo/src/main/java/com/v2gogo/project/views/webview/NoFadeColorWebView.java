package com.v2gogo.project.views.webview;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;

import com.alipay.sdk.app.ac;
import com.v2gogo.project.InternalLinksTool;
import com.v2gogo.project.InternalLinksTool.InternalLinksListerner;
import com.v2gogo.project.R;
import com.v2gogo.project.activity.PhotoPreviewActivity;
import com.v2gogo.project.activity.WebViewActivity;
import com.v2gogo.project.activity.account.AccountLoginActivity;
import com.v2gogo.project.activity.shop.CommitOrderActivity;
import com.v2gogo.project.db.MatserInfo;
import com.v2gogo.project.domain.ShareInfo;
import com.v2gogo.project.domain.SliderInfo;
import com.v2gogo.project.domain.VersionInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.utils.UploadActionSheetDialogUtil;
import com.v2gogo.project.utils.common.AppUtil;
import com.v2gogo.project.utils.common.LogUtil;
import com.v2gogo.project.utils.common.PhotoUtil;
import com.v2gogo.project.utils.common.ToastUtil;
import com.v2gogo.project.utils.common.apk.ApkUtil;
import com.v2gogo.project.utils.common.apk.DownloadApkService;
import com.v2gogo.project.utils.common.apk.StorageUtils;
import com.v2gogo.project.utils.share.CustomPlatformActionListener;
import com.v2gogo.project.utils.share.ShareUtils;
import com.v2gogo.project.views.dialog.ApkDownloadProgressDialog;
import com.v2gogo.project.views.dialog.AppVersionUpdateDialog;
import com.v2gogo.project.views.dialog.AppVersionUpdateDialog.IonStartDownloadCallback;
import com.v2gogo.project.views.dialog.HomeFactChooseDialog;
import com.v2gogo.project.views.dialog.HomeFactChooseDialog.HomeFactACTION;
import com.v2gogo.project.views.dialog.HomeFactChooseDialog.IonActionHomeFactSheetClickListener;
import com.v2gogo.project.views.dialog.ProfileActionSheetDialog;
import com.v2gogo.project.views.dialog.ProfileActionSheetDialog.ACTION;
import com.v2gogo.project.views.dialog.ProfileActionSheetDialog.IonActionSheetClickListener;
import com.v2gogo.project.views.dialog.RecoderDialog;
import com.v2gogo.project.views.dialog.V2gogoShareDialog;
import com.v2gogo.project.views.dialog.V2gogoShareDialog.IonItemClickCallback;
import com.v2gogo.project.views.dialog.V2gogoShareDialog.SHARE_TYPE;

/**
 * 滑到边界不会出现蓝色的webview
 * 
 * @author houjun
 * @param <DownloadApkBroadcastReceiver>
 */
public class NoFadeColorWebView extends WebView implements IonItemClickCallback, IonActionHomeFactSheetClickListener, IonStartDownloadCallback
{
	private ShareInfo mShareInfoWebView;
	private MyProfileActionSheetDialog myProfileActionSheetDialog;
	private V2gogoShareDialog mShareDialog;// 分享对话框
	private RecoderDialog mRecoderDialog;// 录音对话框
	private String mTid;// 主题ID
	private IonPrizeClickCallback prizeClickCallback;
	private IonPrizeClickPraiseCallback prizeClickPraiseCallback;// 点赞按钮回调
	private IonPayCallback mPayCallback;// 支付回调

	private ShareInfo shareInfo;
	private AppVersionUpdateDialog mVersionUpdateDialog;
	private ApkDownloadProgressDialog mDownloadProgressDialog;
	private DownloadApkBroadcastReceiver mDownloadApkBroadcastReceiver;
	private HomeFactChooseDialog mHomeFactChooseDialog;// 上传文件选择项对话框

	public String getmTid()
	{
		return mTid;
	}

	public NoFadeColorWebView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init();
	}

	public NoFadeColorWebView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	public NoFadeColorWebView(Context context)
	{
		super(context);
		init();
	}

	@SuppressLint("NewApi")
	private void init()
	{
		int version = Build.VERSION.SDK_INT;
		if (version >= Build.VERSION_CODES.GINGERBREAD)
		{
			this.setOverScrollMode(View.OVER_SCROLL_NEVER);
		}
		try
		{
			this.setSaveEnabled(false);
		}
		catch (Exception exception)
		{
		}
		myProfileActionSheetDialog = new MyProfileActionSheetDialog();
	}

	public void pauseWebview()
	{
		try
		{
			WebView.class.getMethod("onPause").invoke(this);
			this.pauseTimers();
		}
		catch (Exception e)
		{
		}
	}

	public void resumeWebview()
	{
		try
		{
			WebView.class.getMethod("onResume").invoke(this);
			this.resumeTimers();
		}
		catch (Exception e)
		{
		}
	}

	public void destoryWebview()
	{
		try
		{
			this.destroy();
		}
		catch (Exception e)
		{
		}
	}

	@Override
	public void loadUrl(String url)
	{

		if (url != null && url.contains("javascript:"))
		{
			super.loadUrl(url);
		}
		else if (url != null && url.contains("#username"))
		{
			String urlString = url;
			String str = V2GogoApplication.getMasterLoginState() ? V2GogoApplication.getCurrentMatser().getUsername() : "";

			if (url.contains("?"))
			{
				urlString = url + "&username=" + str;
			}
			else
			{
				urlString = url + "?username=" + str;
			}
			Map<String, String> params = new HashMap<String, String>();
			params.put("Agent", "fromV2gogo");
			super.loadUrl(urlString, params);
		}
		else
		{
			super.loadUrl(url);
		}
	}

	/**
	 * 注入js函数监听
	 */
	public void addUserInfoListner()
	{
		MatserInfo masterInfo = V2GogoApplication.getCurrentMatser();
		if (masterInfo == null)
		{
			masterInfo = new MatserInfo();
		}

		String v2gogoapp = "window.Device={v2gogoapp:{'userId':%s,'username':%s,'version':%s,'os':'Android'}}";
		v2gogoapp = String.format(v2gogoapp, masterInfo.getUserid() != null ? "'" + masterInfo.getUserid() + "'" : null, masterInfo.getUsername() != null ? "'"
				+ masterInfo.getUsername() + "'" : null, AppUtil.getVersionCode(getContext()));
		this.loadUrl("javascript:" + v2gogoapp + ";");
	}

	/**
	 * 注册图片点击
	 * 
	 * @param context
	 */
	public void registerImageListenerClick(Context context)
	{
		this.addJavascriptInterface(new JavascriptInterface(context), "AndroidJSListener");
	}

	public class v2gogoapp
	{
		private MatserInfo masterInfo = V2GogoApplication.getCurrentMatser();

		public String userId = masterInfo != null ? masterInfo.getUserid() : null;
		public String username = masterInfo != null ? masterInfo.getUsername() : null;
		public int version = AppUtil.getVersionCode(getContext());
		public String os = "Android";

		@android.webkit.JavascriptInterface
		public String getOs()
		{
			LogUtil.e("os:" + os);
			return os;
		}

	}

	/**
	 * js调用通道
	 * 
	 * @author houjun
	 */
	public class JavascriptInterface
	{
		private Context context;
		private ArrayList<String> paths;

		public JavascriptInterface(Context context)
		{
			this.context = context;
			this.paths = new ArrayList<String>();
		}

		@android.webkit.JavascriptInterface
		public void getImages(String path)
		{
			LogUtil.e("path:" + path);
			paths.add(path);
		}

		@android.webkit.JavascriptInterface
		public void UploadImageTo7NiuServer(String themeId)
		{
			LogUtil.e("path:" + themeId);
			mTid = themeId;
			UploadActionSheetDialogUtil.showUploadDialog(getContext(), myProfileActionSheetDialog);
		}

		@android.webkit.JavascriptInterface
		public void exchangePrize(String pid)
		{
			if (prizeClickCallback != null)
			{
				prizeClickCallback.getExchangePrizeId(pid);
			}
		}

		@android.webkit.JavascriptInterface
		public void praiseComment(String cid)
		{
			LogUtil.e(cid);
			if (prizeClickPraiseCallback != null)
			{
				prizeClickPraiseCallback.getPrizePraiseId(cid);
			}
		}

		@android.webkit.JavascriptInterface
		public void UploadMessageSuccessToast(String jsonStr)
		{
			LogUtil.e("jsonStr:" + jsonStr);
			try
			{
				JSONObject jsonObject = new JSONObject(jsonStr);
				String codeStr = jsonObject.optString("code");
				String message = jsonObject.optString("message");
				if (codeStr != null && codeStr.equals("0"))
				{
					ToastUtil.showConfirmToast((Activity) getContext(), message);
				}
				else
				{
					ToastUtil.showAlertToast((Activity) getContext(), message);
				}
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}

		}

		@android.webkit.JavascriptInterface
		public void onShareInfo(String data)
		{
			if (!TextUtils.isEmpty(data))
			{
				shareInfo = new ShareInfo();
				try
				{
					JSONObject jsonObject = new JSONObject(data);
					String title = jsonObject.optString("title", "");
					String desc = jsonObject.optString("desc", "");
					String url = jsonObject.optString("url", "");
					String imgUrl = jsonObject.optString("imgUrl", "");

					shareInfo.setTitle(title);
					shareInfo.setDescription(desc);
					shareInfo.setHref(url);
					shareInfo.setImageUrl(imgUrl);
				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}

			}

		}

		@android.webkit.JavascriptInterface
		public void openImage(final String path, String href)
		{
			if (href != null && href != "undefined" && href.length() > 0)
			{
				forward2Website(href);
			}
			else
			{
				((Activity) context).runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						LogUtil.d("houjun", "openImage");
						Intent intent = new Intent(context, PhotoPreviewActivity.class);
						int index = paths.indexOf(path);
						intent.putExtra(PhotoPreviewActivity.INDEX, index);
						intent.putStringArrayListExtra(PhotoPreviewActivity.PATHS, paths);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						context.startActivity(intent);
					}
				});
			}
		}
	}

	public void setPrizeClickCallback(IonPrizeClickCallback prizeClickCallback)
	{
		this.prizeClickCallback = prizeClickCallback;
	}

	public void setPrizeClickPraiseCallback(IonPrizeClickPraiseCallback prizeClickPraiseCallback)
	{
		this.prizeClickPraiseCallback = prizeClickPraiseCallback;
	}

	/**
	 * method desc：
	 * 页面跳转
	 * 
	 * @param type
	 *            类型
	 * @param info
	 *            标志
	 * @param originalData
	 *            原始json格式数据
	 */
	/*
	 * public void jump2Activity(int type, String info, String originalData)
	 * {
	 * if (type == 0 || type == 1 || type == 2 || type == 4)
	 * {
	 * if (TextUtils.isEmpty(info))
	 * {
	 * return;
	 * }
	 * }
	 * Intent intent = null;
	 * switch (type)
	 * {
	 * // 文章详细
	 * case SliderInfo.SRC_TYPE_INFO:
	 * intent = new Intent(getContext(), HomeArticleDetailsActivity.class);
	 * intent.putExtra(BaseDetailsctivity.ID, info);
	 * getContext().startActivity(intent);
	 * break;
	 * // 兑换奖品详细
	 * case SliderInfo.SRC_TYPE_PRIZE:
	 * StringBuilder url = new StringBuilder();
	 * url.append(ServerUrlConfig.SERVER_URL).append("/prizepaperapp/getExchangePrizeDetail?pid=").
	 * append(info);
	 * intent = new Intent(getContext(), ExchangePrizeDetailsActivity.class);
	 * intent.putExtra(ExchangePrizeDetailsActivity.PRIZE_ID, info);
	 * intent.putExtra(ExchangePrizeDetailsActivity.PRIZE_URL, url.toString());
	 * intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 * getContext().startActivity(intent);
	 * break;
	 * // 商品详细
	 * case SliderInfo.SRC_TYPE_PRODUCT:
	 * intent = new Intent(getContext(), GroupGoodsDetailsActivity.class);
	 * intent.putExtra(GroupGoodsDetailsActivity.GOODS_URL, info);
	 * intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 * getContext().startActivity(intent);
	 * break;
	 * // 团购列表-3
	 * case SliderInfo.SRC_TYPE_SHOP:
	 * intent = new Intent(getContext(), GroupProductListWebViewActivity.class);
	 * intent.putExtra(GroupProductListWebViewActivity.URL, info);
	 * getContext().startActivity(intent);
	 * break;
	 * // 原生晒照片列表
	 * case SliderInfo.SRC_TYPE_THEME:
	 * intent = new Intent(getContext(), HomeThemePhotoTabActivity.class);
	 * intent.putExtra(IntentExtraConstants.TID, info);
	 * intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 * getContext().startActivity(intent);
	 * break;
	 * // 兑换列表-5
	 * case SliderInfo.SRC_TYPE_EXCHANGE:
	 * intent = new Intent(getContext(), ExchangeActivity.class);
	 * intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 * getContext().startActivity(intent);
	 * break;
	 * // 分享-6
	 * case SliderInfo.SRC_TYPE_SHARED:
	 * if (originalData != null)
	 * {
	 * mShareInfoWebView = getShareInfo(originalData);
	 * share();
	 * }
	 * break;
	 * // 上传图片-7
	 * case SliderInfo.SRC_TYPE_UPLOAD_PIC:
	 * UploadActionSheetDialogUtil.showUploadDialog(getContext(), myProfileActionSheetDialog);
	 * break;
	 * // 上传声音-8
	 * case SliderInfo.SRC_TYPE_UPLOAD_VOICE:
	 * try
	 * {
	 * Uri uri = Uri.parse(originalData);
	 * String srcId = uri.getQueryParameter("srcId");
	 * showRecoderDialog(srcId);
	 * }
	 * catch (Exception e)
	 * {
	 * }
	 * break;
	 * // 外部链接
	 * case SliderInfo.SRC_TYPE_WEBSITE:
	 * forward2Website(info);
	 * break;
	 * // 商品类型
	 * case SliderInfo.SRC_TYPE_PRODUCT_TYPE:
	 * intent = new Intent(getContext(), GroupProductTypeWebViewActivity.class);
	 * intent.putExtra(GroupProductTypeWebViewActivity.URL, info);
	 * intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 * getContext().startActivity(intent);
	 * break;
	 * // 订单查询
	 * case SliderInfo.SRC_TYPE_ORDER_lIST:
	 * intent = new Intent(getContext(), ProfileOrderActivity.class);
	 * getContext().startActivity(intent);
	 * break;
	 * // 立即购买
	 * case SliderInfo.SRC_TYPE_BUY_NOW:
	 * try
	 * {
	 * if (!V2GogoApplication.getMasterLoginState())
	 * {
	 * AccountLoginActivity.forwardAccountLogin(getContext());
	 * }
	 * else
	 * {
	 * Uri uri = Uri.parse(originalData);
	 * String srcId = uri.getQueryParameter("srcId");
	 * intent = new Intent(getContext(), CommitOrderActivity.class);
	 * intent.putExtra(CommitOrderActivity.GOODS_ID, srcId);
	 * getContext().startActivity(intent);
	 * }
	 * }
	 * catch (Exception e)
	 * {
	 * }
	 * break;
	 * default:
	 * break;
	 * }
	 * }
	 */
	// 2016-4-13 新增参数subType用于多媒体上传时判断显示弹出框按钮个数 author: AlwaysOnline
	protected void jump2ActivityNew(final int type, String id, final String url, final String originalDataUrl)
	{
		InternalLinksTool.jump2Activity(getContext(), type, id, url, new InternalLinksListerner()
		{
			@Override
			public void onInternalLinks(String originalData)
			{
				switch (type)
				{
				// 分享-6
					case SliderInfo.SRC_TYPE_SHARED:
						mShareInfoWebView = getShareInfo(originalDataUrl);
						share();
						break;
					// 上传图片-7
					case SliderInfo.SRC_TYPE_UPLOAD_PIC:
						UploadActionSheetDialogUtil.showUploadDialog(getContext(), myProfileActionSheetDialog);
						break;
					// 上传声音-8
					case SliderInfo.SRC_TYPE_UPLOAD_VOICE:
						try
						{
							Uri uri = Uri.parse(originalData);
							String srcId = uri.getQueryParameter("srcId");
							showRecoderDialog(srcId);
						}
						catch (Exception e)
						{
						}
						break;
					// 外部链接
					case SliderInfo.SRC_TYPE_WEBSITE:
						forward2Website(url);
						break;
					// 公益
					case SliderInfo.SRC_TYPE_BUY_lOVE:
						try
						{
							if (!V2GogoApplication.getMasterLoginState())
							{
								AccountLoginActivity.forwardAccountLogin(getContext());
							}
							else
							{
								Uri uri = Uri.parse(originalDataUrl);
								String projectId = uri.getQueryParameter("projectId");
								String amount = uri.getQueryParameter("amount");
								String subtype = uri.getQueryParameter("subtype");
								if (mPayCallback != null)
								{
									mPayCallback.onPay(projectId, amount, subtype);// subType=121
																					// 微信支付
								}
								// Intent intent = new Intent(getContext(), WelfareActivity.class);
								// intent.putExtra("projectId", projectId);
								// intent.putExtra("amount", amount);
								// getContext().startActivity(intent);
							}
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
						break;
					// 升级
					case SliderInfo.SRC_TYPE_UPDATE:
						VersionInfo versionInfo = new VersionInfo();
						versionInfo.setText("你需要升级到新版本才能捐款！");
						versionInfo.setDownloadUrl(V2GogoApplication.getsIntance().mNewApkLoadUrl);
						showAppUpdateDialog(versionInfo, false);
						break;
					// 升级
					case SliderInfo.SRC_TYPE_UPLOADE_FILE:// 上传多媒体文件

						if (!V2GogoApplication.getMasterLoginState())
						{
							AccountLoginActivity.forwardAccountLogin(getContext());
						}
						else
						{
							Uri uri = Uri.parse(originalDataUrl);
							int subtype = Integer.parseInt(uri.getQueryParameter("subtype"));
							showUploadFileDialog(subtype);
						}
						break;
					
				}
			}
		});
	}

	/**
	 * 显示app升级对话框
	 * 
	 * @param versionInfo
	 */
	private void showAppUpdateDialog(VersionInfo versionInfo, boolean isForceUpdate)
	{
		if (mVersionUpdateDialog == null)
		{
			mVersionUpdateDialog = new AppVersionUpdateDialog(getContext(), R.style.style_action_sheet_dialog);
			mVersionUpdateDialog.setOnCallback(this);
		}
		if (!mVersionUpdateDialog.isShowing())
		{
			mVersionUpdateDialog.show();
			mVersionUpdateDialog.setVersionInfos(versionInfo, isForceUpdate);
		}
	}

	/**
	 * 显示上传文件对话框
	 */
	// 2016-4-13 新增参数subType用于多媒体上传时判断显示弹出框按钮个数 author: AlwaysOnline
	private void showUploadFileDialog(int subType)
	{
		mHomeFactChooseDialog = new HomeFactChooseDialog(getContext(), R.style.style_action_sheet_dialog, subType);
		mHomeFactChooseDialog.setSheetClickListener(this);
		if (!mHomeFactChooseDialog.isShowing())
		{
			mHomeFactChooseDialog.show();
		}
	}

	/**
	 * method desc：弹出分享对话框
	 */
	public void share()
	{
		if (null == mShareDialog)
		{
			mShareDialog = new V2gogoShareDialog(getContext(), R.style.style_action_sheet_dialog);
			mShareDialog.setItemClickCallback(this);
		}
		if (!mShareDialog.isShowing())
		{
			mShareDialog.show();
		}
	}

	/**
	 * method desc：弹出录音对话框
	 */
	public void showRecoderDialog(String tid)
	{
		if (null == mRecoderDialog)
		{
			mRecoderDialog = new RecoderDialog(getContext(), R.style.style_action_sheet_dialog);
			// mRecoderDialog.setOnShowListener(this);
		}
		if (!mRecoderDialog.isShowing())
		{
			mRecoderDialog.show();
		}
	}

	protected ShareInfo getShareInfo(String originalJsonData)
	{
		// Uri uri = Uri.parse(originalJsonData);
		// String type = uri.getQueryParameter("type");
		// String srcId = uri.getQueryParameter("srcId");

		ShareInfo shareInfo = new ShareInfo();
		// JSONObject jsonObject = new JSONObject(originalJsonData);
		// String title = jsonObject.optString("title");
		// String url = jsonObject.optString("url");
		// String desc = jsonObject.optString("desc");
		// String imgUrl = jsonObject.optString("imgUrl");

		Uri uri = Uri.parse(originalJsonData);
		String title = uri.getQueryParameter("title");
		String url = uri.getQueryParameter("url");
		String desc = uri.getQueryParameter("desc");
		String imgUrl = uri.getQueryParameter("imgUrl");

		shareInfo.setTitle(title);
		shareInfo.setHref(url);
		shareInfo.setDescription(desc);
		shareInfo.setImageUrl(imgUrl);

		return shareInfo;
	}

	/**
	 * 跳转到外部链接
	 */
	private void forward2Website(String info)
	{
		Intent intent = null;
		if (info.contains(ServerUrlConfig.SERVER_URL))
		{
			if (V2GogoApplication.getMasterLoginState())
			{
				intent = new Intent(getContext(), WebViewActivity.class);
				intent.putExtra(WebViewActivity.URL, info);
				getContext().startActivity(intent);
			}
			else
			{
				AccountLoginActivity.forwardAccountLogin(getContext());
			}
		}
		else
		{
			intent = new Intent(getContext(), WebViewActivity.class);
			intent.putExtra(WebViewActivity.URL, info);
			getContext().startActivity(intent);
		}
	}

	@Override
	public void clearHistory()
	{
	}

	@Override
	public void clearCache(boolean includeDiskFiles)
	{
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
		ShareUtils.share(getContext(), mShareInfoWebView.getTitle(), mShareInfoWebView.getDescription(), mShareInfoWebView.getHref(),
				mShareInfoWebView.getImageUrl(), shareType, new CustomPlatformActionListener((Activity) getContext(), tip));
	}

	private class MyProfileActionSheetDialog implements IonActionSheetClickListener
	{

		@Override
		public void onClickListener(ACTION action, ProfileActionSheetDialog profileActionSheetDialog)
		{

			if (action == ACTION.FIRST_ITEM)
			{
				if (!V2GogoApplication.getMasterLoginState())
				{
					AccountLoginActivity.forwardAccountLogin(getContext());
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
					AccountLoginActivity.forwardAccountLogin(getContext());
				}
				else
				{
					forward2Album();
				}
			}

		}

	}

	private void forward2Album()
	{
		PhotoUtil.forword2Alumb((Activity) getContext());
	}

	private void forward2Camera()
	{
		PhotoUtil.forwrd2Camera((Activity) getContext());
	}

	// ***************跳转到照相机或者照片begin*****************

	private void forword2FactAlumb()
	{
		PhotoUtil.forword2FactAlumb((Activity) getContext());
	}

	private void forward2FactCamera()
	{
		PhotoUtil.forwrd2FactCamera((Activity) getContext());
	}

	private void forward2FactVideo()
	{
		PhotoUtil.forwrd2FactVideo((Activity) getContext());
	}

	private void forword2FactVideoAlumb()
	{
		PhotoUtil.forword2VideoAlumb((Activity) getContext());
	}

	private void forward2FactVoice()
	{
		PhotoUtil.forwrd2FactVoice((Activity) getContext());
	}

	public ShareInfo getShareInfo()
	{
		return shareInfo;
	}

	// set method

	public void setOnPayCallback(IonPayCallback mPayCallback)
	{
		this.mPayCallback = mPayCallback;
	}

	// ==================接口定义begin=======================
	/**
	 * @author houjun
	 */
	public interface IonPrizeClickCallback
	{
		public void getExchangePrizeId(String pid);
	}

	/**
	 * 点击H5立即点赞按钮，H5和原生交互， H5传入的评论ID。
	 * 
	 * @author houjun
	 */
	public interface IonPrizeClickPraiseCallback
	{
		public void getPrizePraiseId(String cid);
	}

	/**
	 * 支付回调
	 * 
	 * @author houjun
	 */
	public interface IonPayCallback
	{
		public void onPay(String ProductId, String buyNum, String subType);
	}

	// ==================接口定义end=======================

	@Override
	public void onStartDownload(VersionInfo versionInfo)
	{
		dowloadApkProgress(versionInfo.getDownloadUrl());
	}

	private void dowloadApkProgress(String loadurl)
	{
		if (null == mDownloadProgressDialog)
		{
			mDownloadProgressDialog = new ApkDownloadProgressDialog(getContext(), R.style.style_action_sheet_dialog);
		}
		if (!mDownloadProgressDialog.isShowing())
		{
			mDownloadProgressDialog.show();
		}
		if (mDownloadApkBroadcastReceiver == null)
		{
			mDownloadApkBroadcastReceiver = new DownloadApkBroadcastReceiver();
		}
		IntentFilter intentFilter = new IntentFilter(DownloadApkService.PROGRESS_INTENT);
		getContext().registerReceiver(mDownloadApkBroadcastReceiver, intentFilter);
		ApkUtil.startDownloadApk(getContext(), loadurl);
	}

	/**
	 * 下载进度广播接收器
	 * 
	 * @author houjun
	 */
	private class DownloadApkBroadcastReceiver extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent data)
		{
			if (data != null)
			{
				String action = data.getAction();
				if (DownloadApkService.PROGRESS_INTENT.equals(action))
				{
					int progress = data.getIntExtra(DownloadApkService.PROGRESS, 0);
					if (progress != 100)
					{
						if (mDownloadProgressDialog != null)
						{
							mDownloadProgressDialog.setProgress(progress);
						}
					}
					else
					{
						if (mDownloadProgressDialog != null)
						{
							mDownloadProgressDialog.dismiss();
						}
						mDownloadApkBroadcastReceiver = null;
						getContext().unregisterReceiver(this);
						File path = StorageUtils.getCacheDirectory(context);
						File apkFile = new File(path, "v2gogo.apk");
						String apkPath = apkFile.getAbsolutePath();
						ApkUtil.installApk(context, apkPath);
					}
				}
			}
		}
	}

	/**
	 * 多媒体上传文件回调函数
	 */

	@Override
	public void onClickItemListener(HomeFactACTION action, HomeFactChooseDialog profileActionSheetDialog)
	{
		if (action == HomeFactACTION.FIRST_ITEM)// 相片拍照
		{
			forward2FactCamera();
		}
		else if (action == HomeFactACTION.SECOND_ITEM)// 相片选择
		{
			forword2FactAlumb();
		}
		else if (action == HomeFactACTION.THIRD_ITEM)// 视频录制
		{
			forward2FactVideo();
		}
		else if (action == HomeFactACTION.FOURTH_ITEM)// 视频选择
		{
			forword2FactVideoAlumb();
		}
		else if (action == HomeFactACTION.FIFTH_ITEM)// 语音
		{
			forward2FactVoice();
		}
	}
}
