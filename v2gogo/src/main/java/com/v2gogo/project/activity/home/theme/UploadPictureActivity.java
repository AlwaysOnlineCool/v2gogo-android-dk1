package com.v2gogo.project.activity.home.theme;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.BaseActivity;
import com.v2gogo.project.domain.home.theme.ThemePhotoUploadResultInfo;
import com.v2gogo.project.domain.home.theme.UploadErrorInfo;
import com.v2gogo.project.domain.home.theme.UploadProgressInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.main.image.GlideImageLoader;
import com.v2gogo.project.manager.upload.ThemePhotoUploadManager;
import com.v2gogo.project.utils.common.LogUtil;
import com.v2gogo.project.utils.common.ToastUtil;
import com.v2gogo.project.utils.http.IntentExtraConstants;
import com.v2gogo.project.utils.qiniu.QiNiuUploadManager;
import com.v2gogo.project.views.UploadProgressPopupWindow;
import com.ypy.eventbus.EventBus;

/**
 * 上传图片发布界面
 * 
 * @author houjun
 */
public class UploadPictureActivity extends BaseActivity implements OnClickListener, TextWatcher
{

	private EditText mEditText;
	private ImageView mImageView;
	private Button mBtnUpload;

	private String mPath;
	private String mTid;
	private UploadProgressPopupWindow mProgressPopupWindow;
	private RelativeLayout mRelativeLayout;
	private TextView mWatchTv;// 字数限制显示

	public static final String UPLOAD_SUCCUSS_VIEW = "upload_success_view";// 上传成功页面

	@Override
	public void clearRequestTask()
	{
		EventBus.getDefault().unregister(this);
	}

	@Override
	public void onInitViews()
	{
		mBtnUpload = (Button) findViewById(R.id.home_theme_photo_post_upload_btn);
		mEditText = (EditText) findViewById(R.id.home_theme_photo_post_edit_content);
		mImageView = (ImageView) findViewById(R.id.home_theme_photo_post_upload_image);
		mRelativeLayout = (RelativeLayout) findViewById(R.id.common_app_action_bar);
		mWatchTv = (TextView) findViewById(R.id.upload_pic_watch_tv);
		mEditText.addTextChangedListener(this);

		// 注册EvenBus
		EventBus.getDefault().register(this);
	}

	/**
	 * 接收上传图片成功的消息
	 * 
	 * @param progress
	 */
	public void onEventMainThread(ThemePhotoUploadResultInfo photoUploadResultInfo)
	{
		if (null != photoUploadResultInfo && photoUploadResultInfo.getmThemePhotoInfo() != null)
		{
			photoUploadResultInfo.setFlag(UPLOAD_SUCCUSS_VIEW);
			EventBus.getDefault().post(photoUploadResultInfo);
			finish();
		}
	}

	/**
	 * 进度回调
	 * 
	 * @param progress
	 */
	public void onEventMainThread(UploadProgressInfo uploadProgressInfo)
	{
		displayUploadProgress(uploadProgressInfo);
	}

	/**
	 * 上传失败
	 * 
	 * @param progress
	 */
	public void onEventMainThread(UploadErrorInfo uploadErrorInfo)
	{
		displayUploadErrorTip(uploadErrorInfo);
	}

	/**
	 * 显示上传进度
	 * 
	 * @param uploadProgressInfo
	 */
	private void displayUploadProgress(UploadProgressInfo uploadProgressInfo)
	{
		LogUtil.d("houjun", "uploadProgressInfo->" + uploadProgressInfo);
		if (null != uploadProgressInfo)
		{
			if (uploadProgressInfo.getProgress() != 100)
			{
				if (null == mProgressPopupWindow)
				{
					mProgressPopupWindow = new UploadProgressPopupWindow(this);
				}
				mProgressPopupWindow.show(mRelativeLayout, 0, 0);
				mProgressPopupWindow.setProgress(uploadProgressInfo.getProgress());
			}
			else
			{
				mProgressPopupWindow.dismiss();
			}
		}
	}

	/**
	 * 显示上传失败提示信息
	 * 
	 * @param uploadErrorInfo
	 */
	private void displayCommentsUploadErrorTip(UploadErrorInfo uploadErrorInfo)
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
			if (null != mProgressPopupWindow && mProgressPopupWindow.isShowing())
			{
				mProgressPopupWindow.dismiss();
			}
			ToastUtil.showAlertToast(this, errorMessage);
		}
	}

	@Override
	@TargetApi(11)
	protected void onInitIntentData(Intent intent)
	{
		super.onInitIntentData(intent);
		if (intent != null)
		{
			mPath = intent.getStringExtra(IntentExtraConstants.PATH);
			mTid = intent.getStringExtra(IntentExtraConstants.TID);
		}
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.home_theme_photo_post_activity_layout;
	}

	@Override
	protected void onInitLoadDatas()
	{
		super.onInitLoadDatas();
		GlideImageLoader.loadLocalFileDrawable(this, mPath, mImageView);
	}

	@Override
	protected void registerListener()
	{
		super.registerListener();
		mBtnUpload.setOnClickListener(this);
	}

	@Override
	public void onClick(View view)
	{
		String string = mEditText.getText().toString();
		if (TextUtils.isEmpty(string))
		{
			ToastUtil.showAlertToast(this, R.string.please_input_desc_content);
			return;
		}
		compressBitmap(string);
	}

	/**
	 * 压缩图片
	 * 
	 * @param string
	 */
	private void compressBitmap(String string)
	{
		Bitmap bitmap = BitmapFactory.decodeFile(mPath);
		if (bitmap != null)
		{
			try
			{
				File file = new File(mPath);
				file.deleteOnExit();
				int qur = 40;
				boolean result = bitmap.compress(CompressFormat.JPEG, qur, new FileOutputStream(ThemePhotoUploadManager.FILE_PATH));
				if (!bitmap.isRecycled())
				{
					bitmap.recycle();
				}
				if (result)
				{
					// Intent intent = new Intent(this, HomeThemePhotoTabActivity.class);
					// intent.putExtra(IntentExtraConstants.THEME_PHOTO_DESC, string.trim());
					// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					// startActivity(intent);
					// finish();
					uploadThemePhoto(string, file);
				}
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * 上传主题图片
	 * 
	 * @param intent
	 * @param file
	 */
	private void uploadThemePhoto(String content, File file)
	{
		String username = V2GogoApplication.getCurrentMatser().getUsername();
		ThemePhotoUploadManager.uploadThemePhoto(mTid, file, username, content, mTid);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after)
	{

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count)
	{

	}

	@Override
	public void afterTextChanged(Editable s)
	{
		mWatchTv.setText(s.length() + "/100");
		if (s.length() >= 100)
		{
			ToastUtil.showAlertToast(this, "当前输入已经100字");
		}
	}

}
