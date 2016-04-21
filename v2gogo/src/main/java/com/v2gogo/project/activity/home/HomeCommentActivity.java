package com.v2gogo.project.activity.home;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.BaseActivity;
import com.v2gogo.project.domain.home.CommentInfo;
import com.v2gogo.project.domain.home.theme.UploadErrorInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.main.image.GlideImageLoader;
import com.v2gogo.project.manager.home.CommentManager;
import com.v2gogo.project.manager.home.CommentManager.IonNewCommentCallback;
import com.v2gogo.project.utils.common.BitmapCompressUtil;
import com.v2gogo.project.utils.common.KeyBoardUtil;
import com.v2gogo.project.utils.common.SDCardUtil;
import com.v2gogo.project.utils.common.ScreenUtil;
import com.v2gogo.project.utils.common.StringUtil;
import com.v2gogo.project.utils.common.ToastUtil;
import com.v2gogo.project.views.dialog.ProfileActionSheetDialog;
import com.v2gogo.project.views.dialog.ProfileActionSheetDialog.ACTION;
import com.v2gogo.project.views.dialog.ProfileActionSheetDialog.IonActionSheetClickListener;
import com.v2gogo.project.views.edittext.LimitedEditText;
import com.ypy.eventbus.EventBus;

/**
 * 发表评论界面
 * 
 * @author houjun
 */
public class HomeCommentActivity extends BaseActivity implements OnClickListener, IonActionSheetClickListener
{
	public static final String SRC_TYPE = "src_type";
	public static final String IS_IMAGE = "is_image";
	public static final String ARTICE_ID = "artice_id";
	public static final String COMMENT_TYPE = "comment_type";

	private CommentInfo mCommentInfo;
	private Button mBtnPublish;
	private ImageView mAddImage;
	private LinearLayout mPhotoContainer;

	private int mType;
	private int mSrcType;
	private boolean mIsImage;
	private String mArticeId;

	private File mFile;
	private String mUploadPath;
	private LimitedEditText mLimitedEditText;
	private ProfileActionSheetDialog mProfileActionSheetDialog;

	@Override
	public void onInitViews()
	{
		mBtnPublish = (Button) findViewById(R.id.home_comment_publish_btn);
		mLimitedEditText = (LimitedEditText) findViewById(R.id.comment_input_text);
		mAddImage = (ImageView) findViewById(R.id.home_comment_photo_add_image);
		mPhotoContainer = (LinearLayout) findViewById(R.id.home_comment_photo_layout_container);
	}

	@Override
	protected void onInitIntentData(Intent intent)
	{
		super.onInitIntentData(intent);
		if (null != intent)
		{
			mArticeId = intent.getStringExtra(ARTICE_ID);
			mIsImage = intent.getBooleanExtra(IS_IMAGE, false);
			mSrcType = intent.getIntExtra(SRC_TYPE, CommentInfo.SRC_ARTICE_TYPE);
			mType = intent.getIntExtra(COMMENT_TYPE, CommentInfo.COMMENT_ARTICE);
		}
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.publish_comment_activity_layout;
	}

	@Override
	protected void onInitLoadDatas()
	{
		super.onInitLoadDatas();
		if (mIsImage)
		{
			mPhotoContainer.setVisibility(View.VISIBLE);
			int width = (int) (ScreenUtil.getScreenWidth(this) / 4f);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, width);
			mAddImage.setLayoutParams(layoutParams);
		}
		else
		{
			mPhotoContainer.setVisibility(View.GONE);
		}
	}

	@Override
	protected void registerListener()
	{
		super.registerListener();
		mAddImage.setOnClickListener(this);
		EventBus.getDefault().register(this);
		mBtnPublish.setOnClickListener(this);
	}

	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.home_comment_publish_btn:
				publishComment();
				break;

			case R.id.home_comment_photo_add_image:
				showSelectPhotoDialog();
				break;

			default:
				break;
		}
	}

	@Override
	public void onClickListener(ACTION action, ProfileActionSheetDialog profileActionSheetDialog)
	{
		if (action == ACTION.FIRST_ITEM)
		{
			forward2Camera();
		}
		else if (action == ACTION.SECOND_ITEM)
		{
			forward2Album();
		}
	}

	@Override
	protected void getAlbumPath(String albumPath)
	{
		super.getAlbumPath(albumPath);
		mUploadPath = albumPath;
		GlideImageLoader.loadLocalFileDrawable(this, mUploadPath, mAddImage);
	}

	@Override
	protected void getCameraPath(String cameraPath)
	{
		super.getCameraPath(cameraPath);
		mUploadPath = cameraPath;
		GlideImageLoader.loadLocalFileDrawable(this, mUploadPath, mAddImage);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_MOVE)
		{
			KeyBoardUtil.closeKeybord(mLimitedEditText.getInputEditText(), this);
		}
		return super.onTouchEvent(event);
	}

	@Override
	public void finish()
	{
		Intent intent = new Intent();
		int resultCode = RESULT_OK;
		if (null == mCommentInfo)
		{
			resultCode = RESULT_CANCELED;
		}
		else
		{
			intent.putExtra(HomeArticleDetailsActivity.COMMENT, mCommentInfo);
		}
		setResult(resultCode, intent);
		KeyBoardUtil.closeKeybord(mLimitedEditText.getInputEditText(), this);
		super.finish();
	}

	@Override
	public void clearRequestTask()
	{
		CommentManager.clearNewCommentTask();
		EventBus.getDefault().unregister(this);
	}

	/**
	 * 显示选择图片上传对话框
	 */
	private void showSelectPhotoDialog()
	{
		if (null == mProfileActionSheetDialog)
		{
			mProfileActionSheetDialog = new ProfileActionSheetDialog(this, R.style.style_action_sheet_dialog);
			mProfileActionSheetDialog.setSheetClickListener(this);
		}
		if (!mProfileActionSheetDialog.isShowing())
		{
			mProfileActionSheetDialog.show();
			mProfileActionSheetDialog.setTips(getString(R.string.upload_photo_tip_ex));
		}
	}

	/**
	 * 上传失败
	 * 
	 * @param progress
	 */
	public void onEventMainThread(UploadErrorInfo uploadErrorInfo)
	{
		dismissLoadingDialog();
		ToastUtil.showAlertToast(HomeCommentActivity.this, R.string.profile_modify_avatar_fail);
	}

	/**
	 * 上传失败
	 * 
	 * @param progress
	 */
	public void onEventMainThread(CommentInfo commentInfo)
	{
		commentSuccess(commentInfo);
	}

	/**
	 * 发布评论
	 */
	private void publishComment()
	{
		if (null != mArticeId)
		{
			String content = mLimitedEditText.getText();
			if (TextUtils.isEmpty(content))
			{
				content = content.trim();
				ToastUtil.showAlertToast(this, R.string.comment_content_no_empty_tip);
			}
			else
			{
				mBtnPublish.setEnabled(false);
				content = StringUtil.replaceBlank(content);
				showLoadingDialog(R.string.comment_content_uploading_tip);
				KeyBoardUtil.closeKeybord(mLimitedEditText.getInputEditText(), this);
				if (mIsImage && !TextUtils.isEmpty(mUploadPath))
				{
					mFile = compressBitmap(mUploadPath);
					CommentManager.publishNewCommentWithImage("", mArticeId, mType, mSrcType, content, "", mFile);
				}
				else
				{
					CommentManager.publishNewCommentWithNoImage("", mArticeId, mType, mSrcType, content, "", new IonNewCommentCallback()
					{
						@Override
						public void onNewCommentSuccess(CommentInfo commentInfo)
						{
							commentSuccess(commentInfo);
						}

						@Override
						public void onNewCommentFail(String errorMessage)
						{
							commentFail(errorMessage);
						}
					});
				}
			}
		}
	}

	/**
	 * 压缩图片
	 * 
	 * @return
	 */
	private File compressBitmap(String path)
	{
		File file = null;
		Bitmap bitmap = BitmapCompressUtil.getimage(path);
		if (bitmap != null)
		{
			try
			{
				file = new File(SDCardUtil.getSDCardPath() + "v2gogo" + File.separator + System.currentTimeMillis() + ".jpg");
				boolean result = bitmap.compress(Bitmap.CompressFormat.JPEG, 50, new FileOutputStream(file));
				if (!result)
				{
					file.delete();
					file = null;
				}
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * 评论失败
	 * 
	 * @param errorMessage
	 */
	private void commentFail(String errorMessage)
	{
		mBtnPublish.setEnabled(true);
		ToastUtil.showAlertToast(HomeCommentActivity.this, errorMessage);
		dismissLoadingDialog();
	}

	/**
	 * 评论成功
	 * 
	 * @param commentInfo
	 */
	private void commentSuccess(CommentInfo commentInfo)
	{
		deleteFile();
		mBtnPublish.setEnabled(true);
		ToastUtil.showConfirmToast(HomeCommentActivity.this, R.string.publish_comment_success);
		if (null != commentInfo)
		{
			if (V2GogoApplication.getMasterLoginState())
			{
				commentInfo.setAvatar(V2GogoApplication.getCurrentMatser().getThumbialAvatar());
			}
			mCommentInfo = commentInfo;
		}
		dismissLoadingDialog();
		finish();
	}

	/**
	 * 删除文件
	 */
	private void deleteFile()
	{
		if (!TextUtils.isEmpty(mUploadPath))
		{
			File file = new File(mUploadPath);
			if (file.exists())
			{
				file.delete();
			}
			if (null != mFile && mFile.exists())
			{
				mFile.delete();
			}
		}
	}
}
