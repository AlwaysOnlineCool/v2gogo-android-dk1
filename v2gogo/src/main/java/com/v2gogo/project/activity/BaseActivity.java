package com.v2gogo.project.activity;

import java.io.File;
import java.io.FileNotFoundException;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.domain.home.theme.UploadErrorInfo;
import com.v2gogo.project.hardware.SystemBarTintManager;
import com.v2gogo.project.utils.common.AppUtil;
import com.v2gogo.project.utils.common.LogUtil;
import com.v2gogo.project.utils.common.PhotoUtil;
import com.v2gogo.project.utils.common.ToastUtil;
import com.v2gogo.project.utils.common.VersionAlbumUtil;
import com.v2gogo.project.utils.qiniu.QiNiuUploadManager;
import com.v2gogo.project.views.dialog.AppNoticeDialog;
import com.v2gogo.project.views.dialog.AppNoticeDialog.IonClickSureCallback;
import com.v2gogo.project.views.dialog.CustomMaterialDialog;
import com.v2gogo.project.views.listview.refreshview.OnPullRefreshListener;
import com.v2gogo.project.views.listview.refreshview.PullRefreshListView;
import com.v2gogo.project.views.progressbar.CustomMaterialProgress;

/**
 * activity基类
 * 
 * @author houjun
 */
public abstract class BaseActivity extends Activity
{

	private TextView mDialogTip;
	protected ImageButton mBackBtn;
	private CustomMaterialDialog mLoadingCustomMaterialDialog;
	private AppNoticeDialog mAppNoticeDialog;
	protected PullRefreshListView mPullRefreshListView;

	/**
	 * 清除请求任务
	 */
	public abstract void clearRequestTask();

	/**
	 * 初始化控件
	 */
	public abstract void onInitViews();

	/**
	 * 得到当前布局id
	 * 
	 * @return
	 */
	public abstract int getCurrentLayoutId();

	/**
	 * 点击返回按钮的事件处理
	 */
	protected void onPressBack()
	{
		this.finish();
	}

	/**
	 * 加载网络或本地数据
	 */
	protected void onInitLoadDatas()
	{
	};

	protected void onPullDownRefresh2(AbsListView pullRefreshView)
	{

	}

	/**
	 * 是否是推送进入的界面
	 * 
	 * @return
	 */
	protected boolean isNotifyIntent()
	{
		return false;
	}

	/**
	 * 初始化其他页面传入的数据
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	protected void onInitIntentData(Intent intent)
	{
		if (Build.VERSION.SDK_INT >= VERSION_CODES.ICE_CREAM_SANDWICH)
		{
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
		}
	}

	/**
	 * 是否设置状态栏
	 * 
	 * @return
	 */
	protected boolean isSetting()
	{
		return true;
	}

	/**
	 * 得到相册图片的路径
	 * 
	 * @param albumPath
	 */
	protected void getAlbumPath(String albumPath)
	{
	}

	/**
	 * 得到拍照的图片
	 * 
	 * @param CameraPath
	 */
	protected void getCameraPath(String CameraPath)
	{
	}

	/**
	 * 得到图片压缩后的路径
	 * 
	 * @param albumPath
	 */
	protected void getCompressPath(Bitmap bitmap)
	{
	}

	/**
	 * 得到爆料拍照的图片
	 * 
	 * @param CameraPath
	 */
	protected void getFactCameraPath(Bitmap bitmap)
	{
	}

	/**
	 * 得到爆料相册图片的路径
	 * 
	 * @param albumPath
	 */
	protected void getFactAlbumPath(String albumPath)
	{
	}

	/**
	 * 得到爆料视频的路径
	 * 
	 * @param albumPath
	 */
	protected void getFactVideoAlbumPath(String albumPath)
	{
	}

	/**
	 * 得到爆料录音路径
	 * 
	 * @param albumPath
	 */
	protected void getFactVoicePath(String voicePath)
	{
		
	}

	/**
	 * 控件注册监听
	 */
	protected void registerListener()
	{
		if (null != mBackBtn)
		{
			mBackBtn.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					onPressBack();
				}
			});
		}
	}

	@Override
	protected void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		onInitIntentData(getIntent());
		setContentView(getCurrentLayoutId());
		initBackBtn();
		initPullRefresh();
		onInitViews();
		onInitLoadDatas();
		registerListener();
		setStatusBarbg();
		// setActionBarHeight();
	}

	@Override
	public void finish()
	{
		clearRequestTask();
		dismissLoadingDialog();
		if (isNotifyIntent() && !AppUtil.isMainIntentExist(this))
		{
			Intent intent = new Intent(this, MainTabActivity.class);
			startActivity(intent);
		}
		super.finish();
	}

	@Override
	protected void onDestroy()
	{
		ToastUtil.cancelAllToast();
		super.onDestroy();
	}

	@TargetApi(19)
	private void setTranslucentStatus(boolean on)
	{
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on)
		{
			winParams.flags |= bits;
		}
		else
		{
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == PhotoUtil.ALUMB_REQUEST_CODE)
		{
			if (null != data && resultCode == RESULT_OK)
			{
				Uri uri = data.getData();
				if (uri != null)
				{
					String path = VersionAlbumUtil.getPath(this, uri);
					if (path != null)
					{
						getAlbumPath(path);
					}
				}
			}
		}
		else if (requestCode == PhotoUtil.CAREMA_REQUEST_CODE)
		{
			if (resultCode == RESULT_OK)
			{
				getCameraPath(PhotoUtil.getAvatarPath());
			}
		}
		else if (requestCode == PhotoUtil.FACT_IMG_ALUMB_REQUEST_CODE)// 爆料图片选择
		{
			if (null != data && resultCode == RESULT_OK)
			{
				Uri uri = data.getData();
				if (uri != null)
				{
					String path = VersionAlbumUtil.getPath(this, uri);
					if (path != null)
					{
						getFactAlbumPath(path);
					}
				}
			}
		}
		else if (requestCode == PhotoUtil.FACT_VIDEO_ALUMB_REQUEST_CODE)// 爆料视频选择
		{
			if (null != data && resultCode == RESULT_OK)
			{
				Uri uri = data.getData();
				if (uri != null)
				{
					String path = VersionAlbumUtil.getPath(this, uri);
					if (path != null)
					{
						getFactVideoAlbumPath(path);
					}
				}
			}
		}
		else if (requestCode == PhotoUtil.FACT_CAREMA_IMG_REQUEST_CODE)// 爆料拍照
		{

			if (resultCode == RESULT_OK || data != null)
			{
				Bitmap bitmap = decodeUriAsBitmap(Uri.fromFile(new File(PhotoUtil.getAvatarPath())));
				if (null != bitmap)
				{
					getFactCameraPath(bitmap);
				}
			}
		}
		else if (requestCode == PhotoUtil.FACT_VIDEO_REQUEST_CODE)// 爆料拍视频
		{

			if (resultCode == RESULT_OK || data != null)
			{
				Uri uri = data.getData();
				if (uri != null)
				{
					String path = VersionAlbumUtil.getPath(this, uri);
					if (path != null)
					{
						getFactVideoAlbumPath(path);
					}
				}
			}
		}
		else if (requestCode == PhotoUtil.FACT_VOICE_REQUEST_CODE)// 爆料录制语音
		{

			if (resultCode == RESULT_OK || data != null)
			{
				Uri uri = data.getData();
				if (uri != null)
				{
					String path = VersionAlbumUtil.getPath(this, uri);
					if (path != null)
					{
					getFactVoicePath(path);
					}
				}
			}
		}
		else if (requestCode == PhotoUtil.CROP_REQUEST_CODE)
		{
			if (resultCode == RESULT_OK)
			{
				if (null != data && null != data.getExtras())
				{
					Bitmap bitmap = data.getExtras().getParcelable("data");
					if (null != bitmap)
					{
						getCompressPath(bitmap);
					}
				}
			}
		}
		else if (requestCode == PhotoUtil.CAREAM_CROP_REQUEST_CODE)
		{
			if (resultCode == RESULT_OK || data != null)
			{
				Bitmap bitmap = decodeUriAsBitmap(Uri.fromFile(new File(PhotoUtil.getAvatarPath())));
				if (null != bitmap)
				{
					getCompressPath(bitmap);
				}
			}
		}
	}

	/**
	 * 跳转到相册
	 */
	public void forward2Album()
	{
		PhotoUtil.forword2Alumb(this);
	}

	/**
	 * 跳转到拍照
	 */
	public void forward2Camera()
	{
		PhotoUtil.forwrd2Camera(this);
	}

	private Bitmap decodeUriAsBitmap(Uri uri)
	{
		Bitmap bitmap = null;
		try
		{
			bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

	/**
	 * 显示加载对话框
	 * 
	 * @param tip
	 */
	public void showLoadingDialog(int tipId)
	{
		showLoadingDialog(getString(tipId));
	}

	/**
	 * 加载对话框消失
	 */
	public void dismissLoadingDialog()
	{
		try
		{
			if (null != mLoadingCustomMaterialDialog)
			{
				mLoadingCustomMaterialDialog.dismiss();
			}
		}
		catch (Exception exception)
		{
		}
	}

	/**
	 * 设置状态栏的颜色
	 */
	private void setStatusBarbg()
	{
		if (isSetting())
		{
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
			{
				setTranslucentStatus(true);
			}
			SystemBarTintManager systemBarTintManager = new SystemBarTintManager(this);
			systemBarTintManager.setStatusBarTintEnabled(true);
			systemBarTintManager.setStatusBarTintResource(R.color.status_bar_color);
		}
	}

	/**
	 * 设置action的高度
	 */
	private void setActionBarHeight()
	{
		RelativeLayout actionBarRelativeLayout = (RelativeLayout) findViewById(R.id.common_app_action_bar);
		if (null != actionBarRelativeLayout)
		{
			LinearLayout.LayoutParams layoutParams = null;
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
			{
				layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.action_bar_height_ex));
			}
			else
			{
				layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.action_bar_height));
			}
			actionBarRelativeLayout.setLayoutParams(layoutParams);
		}
	}

	/**
	 * 显示加载对话框
	 * 
	 * @param tip
	 */
	public void showLoadingDialog(String tip)
	{
		if (null == mLoadingCustomMaterialDialog)
		{
			mLoadingCustomMaterialDialog = new CustomMaterialDialog(this);
			View view = LayoutInflater.from(this).inflate(R.layout.common_loading_dialog_layout, null);
			mDialogTip = (TextView) view.findViewById(R.id.common_loading_dialog_tip);
			CustomMaterialProgress materialProgress = (CustomMaterialProgress) view.findViewById(R.id.common_loading_dialog_progress);
			materialProgress.spin();
			materialProgress.setBarColor(0xffee6600);
			mLoadingCustomMaterialDialog.setContentView(view);
		}
		mDialogTip.setText(tip);
		mLoadingCustomMaterialDialog.show();
	}

	private void initBackBtn()
	{
		mBackBtn = (ImageButton) findViewById(R.id.common_app_action_bar_back);
	}

	private void initPullRefresh()
	{
		mPullRefreshListView = (PullRefreshListView) findViewById(R.id.fragment_profile_pull_to_refresh_listview);
		if (mPullRefreshListView != null)
		{
			mPullRefreshListView.setPullRefreshEnable(true);
			mPullRefreshListView.setOnPullRefreshListener(new OnPullRefreshListener()
			{
				@Override
				public void onPullDownRefresh(AbsListView pullRefreshView)
				{
					onPullDownRefresh2(pullRefreshView);
				}
			});
		}
	}

	/**
	 * method desc：弹出打电话对话框
	 */
	protected void showCallDialog(final String message, String sureMsg)
	{
		if (null == mAppNoticeDialog)
		{
			mAppNoticeDialog = new AppNoticeDialog(this, R.style.style_action_sheet_dialog);
			mAppNoticeDialog.setOnSureCallback(new IonClickSureCallback()
			{
				@Override
				public void onClickSure()
				{
					Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + message));
					startActivity(intent);
				}
			});
		}
		if (!mAppNoticeDialog.isShowing())
		{
			mAppNoticeDialog.show();
			mAppNoticeDialog.setSureTipAndMessage(message, sureMsg);
		}
	}

	/**
	 * 显示上传失败提示信息
	 * 
	 * @param uploadErrorInfo
	 */
	protected void displayUploadErrorTip(UploadErrorInfo uploadErrorInfo)
	{
		LogUtil.d("houjun", "uploadErrorInfo->" + uploadErrorInfo);
		if (null != uploadErrorInfo)
		{
			String errorMessage = null;
			if (uploadErrorInfo.getCode() == QiNiuUploadManager.UPLOAD_STATUS_CODE_NET_BROKEN)
			{
				errorMessage = getString(R.string.you_network_yet_broke_tip);
			}
			else if (uploadErrorInfo.getCode() == QiNiuUploadManager.UPLOAD_STATUS_CODE_NOT_QINIU)
			{
				errorMessage = getString(R.string.you_is_not_qiniu_tip);
			}
			else if (uploadErrorInfo.getCode() == QiNiuUploadManager.UPLOAD_STATUS_CODE_SERVER_ERROR)
			{
				errorMessage = getString(R.string.you_server_error_tip);
			}
			else if (uploadErrorInfo.getCode() == QiNiuUploadManager.UPLOAD_STATUS_CODE_YET_CANCELED)
			{
				errorMessage = getString(R.string.you_photo_yet_cancel_tip);
			}
			else
			{
				errorMessage = uploadErrorInfo.getMessage();
			}
			dismissLoadingDialog();
			ToastUtil.showAlertToast(this, errorMessage);
		}
	}

	protected boolean isNewVersion(String versionName)
	{
		float newestVersion = Float.valueOf(versionName);
		int localVersion = AppUtil.getVersionCode(this);
		boolean isNew = false;
		if (newestVersion > localVersion)
		{
			isNew = true;
		}
		return isNew;
	}
}
