package com.v2gogo.project.activity.profile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RatingBar;
import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.BaseActivity;
import com.v2gogo.project.activity.account.AccountLoginActivity;
import com.v2gogo.project.domain.home.theme.UploadErrorInfo;
import com.v2gogo.project.domain.profile.ProfileInfoItem;
import com.v2gogo.project.domain.profile.ProfileScoreInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.main.image.GlideImageLoader;
import com.v2gogo.project.manager.profile.ProfileScoreUploadPhotoManager;
import com.v2gogo.project.manager.profile.ProfileScoreUploadPhotoManager.IonProfileScoreCallback;
import com.v2gogo.project.manager.profile.ProfileScoreUploadPhotoManager.IonProfileUserScoreToPrizeCallback;
import com.v2gogo.project.utils.common.BitmapCompressUtil;
import com.v2gogo.project.utils.common.SDCardUtil;
import com.v2gogo.project.utils.common.ToastUtil;
import com.v2gogo.project.views.dialog.ProfileActionSheetDialog;
import com.v2gogo.project.views.dialog.ProfileActionSheetDialog.ACTION;
import com.v2gogo.project.views.dialog.ProfileActionSheetDialog.IonActionSheetClickListener;
import com.ypy.eventbus.EventBus;

/**
 * 功能：评分界面
 * 
 * @ahthor：黄荣星
 * @date:2015-12-4
 * @version::V1.0
 */
public class ProfileScoreActivity extends BaseActivity implements OnClickListener, IonActionSheetClickListener
{
	public static final String TARGED_ID = "targed_id";// 商品或者奖品ID
	public static final String COMMENT_TYPE = "comment_type";// 评论类型，对奖品评论：0， 对商品评论：1

	public static final String COMMENT_GROUP_POSITION = "comment_group_position";// 评论的列表索引位置

	private RadioGroup mRadioGroup;
	private RatingBar mRatingBar;
	private EditText mCommentEditText;
	private TextView mWatchTextView;
	private ImageView mImageView1;
	private ImageView mImageView2;
	private ImageView mImageView3;
	private ImageView mImageView4;
	private ImageView mImageView5;
	private ImageView mImageView6;

	// 上传七牛的图片成功回调图片地址
	private String mImageUrl1;
	private String mImageUrl2;
	private String mImageUrl3;
	private String mImageUrl4;
	private String mImageUrl5;
	private String mImageUrl6;

	private Button mPublisButton;// 发布

	private ProfileActionSheetDialog mUploadActionSheetDialog;
	private HashMap<Integer, String> mUploadPathMap;// // 图片上传路径
	protected ProfileScoreInfo mProfileScoreInfo;
	private int mImageViewId;// imageview控件ID
	private int mRate;// 星级数
	private File mFile;
	private String mUploadPath;
	private String mScoreId;// 评分对应的ID
	private String mTargedId;// 根据commentType判断转发（奖品ID、商品ID）
	private int commentType; // 评论类型，对奖品评论：0， 对商品评论：1
	private String mCompressPath;// 压缩图片的路径,根据该路径，图片上传成功后删除图片

	private int groupPosition;// 评论位置

	@Override
	public void clearRequestTask()
	{
		ProfileScoreUploadPhotoManager.clearGetHttpScoreTask();
		ProfileScoreUploadPhotoManager.clearUserScoreToPrizeTask();
	}

	@Override
	public void onInitViews()
	{
		EventBus.getDefault().register(this);

		mRadioGroup = (RadioGroup) findViewById(R.id.feeGroup);
		mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
		mCommentEditText = (EditText) findViewById(R.id.profile_score_edit_content);
		mWatchTextView = (TextView) findViewById(R.id.profile_score_watch_tv);
		mImageView1 = (ImageView) findViewById(R.id.imageview1);
		mImageView2 = (ImageView) findViewById(R.id.imageview2);
		mImageView3 = (ImageView) findViewById(R.id.imageview3);
		mImageView4 = (ImageView) findViewById(R.id.imageview4);
		mImageView5 = (ImageView) findViewById(R.id.imageview5);
		mImageView6 = (ImageView) findViewById(R.id.imageview6);
		mPublisButton = (Button) findViewById(R.id.profile_score_publish_btn);
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.activity_profile_score_layout;
	}

	@Override
	protected void onInitLoadDatas()
	{
		super.onInitLoadDatas();
		getHttpScore();
	}

	@Override
	protected void onInitIntentData(Intent intent)
	{
		super.onInitIntentData(intent);
		mTargedId = intent.getStringExtra(TARGED_ID);
		commentType = intent.getIntExtra(COMMENT_TYPE, 0);
		groupPosition = intent.getIntExtra(COMMENT_GROUP_POSITION, 0);
	}

	/**
	 * method desc：根据选项获取分数和ID
	 */
	private void getScoreAndScoreId(int position)
	{
		if (mProfileScoreInfo.getList() != null && mProfileScoreInfo.getList().get(position) != null)
		{
			ProfileInfoItem scoreItem = mProfileScoreInfo.getList().get(position);
			mScoreId = scoreItem.getId();
			mRate = scoreItem.getScore();
		}
	}

	@Override
	protected void registerListener()
	{
		super.registerListener();
		mPublisButton.setOnClickListener(this);
		mImageView1.setOnClickListener(this);
		mImageView2.setOnClickListener(this);
		mImageView3.setOnClickListener(this);
		mImageView4.setOnClickListener(this);
		mImageView5.setOnClickListener(this);
		mImageView6.setOnClickListener(this);
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(RadioGroup arg0, int checkId)
			{
				switch (checkId)
				{
					case R.id.feerb1:// 差
						getScoreAndScoreId(0);
						break;
					case R.id.feerb2:// 一般
						getScoreAndScoreId(1);
						break;
					case R.id.feerb3:// 满意
						getScoreAndScoreId(2);
						break;
					case R.id.feerb4:// 很满意
						getScoreAndScoreId(3);
						break;
					case R.id.feerb5:// 强烈推荐
						getScoreAndScoreId(4);
						break;
				}
				mRatingBar.setVisibility(View.VISIBLE);
				mRatingBar.setNumStars(mRate);
				mRatingBar.setRating(mRate);
			}
		});
		mCommentEditText.addTextChangedListener(new TextWatcher()
		{

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
			{

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
			{

			}

			@Override
			public void afterTextChanged(Editable s)
			{
				mWatchTextView.setText(s.length() + "/200");
				if (s.length() >= 200)
				{
					ToastUtil.showAlertToast(ProfileScoreActivity.this, "当前输入已经200字");
				}
			}
		});
	}

	@Override
	public void onClick(View arg0)
	{
		switch (arg0.getId())
		{
			case R.id.profile_score_publish_btn:
				publishComment();
				break;
			default:
				mImageViewId = arg0.getId();// 获取当前点击ID
				showUploadDialog();
				break;
		}
	}

	/**
	 * method desc：发布评论
	 */
	private void publishComment()
	{
		if (mRate == 0)
		{
			ToastUtil.showAlertToast(this, "您没有进行评分");
			return;
		}
		String commentContent = mCommentEditText.getText() + "";
		if (TextUtils.isEmpty(commentContent))
		{
			ToastUtil.showAlertToast(this, "您还没输入评论内容");
			return;
		}
		String urls = builderImageUrls();
		// if (TextUtils.isEmpty(urls))
		// {
		// ToastUtil.showAlertToast(this, "请选择上传图片");
		// return;
		// }
		httpPublishComment(commentContent, urls);
	}

	/**
	 * method desc：构造图片路径
	 * 
	 * @return
	 */
	private String builderImageUrls()
	{
		StringBuilder builder = new StringBuilder();
		if (!TextUtils.isEmpty(mImageUrl1))
		{
			builder.append(mImageUrl1).append(",");
		}
		if (!TextUtils.isEmpty(mImageUrl2))
		{
			builder.append(mImageUrl2).append(",");
		}
		if (!TextUtils.isEmpty(mImageUrl3))
		{
			builder.append(mImageUrl3).append(",");
		}
		if (!TextUtils.isEmpty(mImageUrl4))
		{
			builder.append(mImageUrl4).append(",");
		}
		if (!TextUtils.isEmpty(mImageUrl5))
		{
			builder.append(mImageUrl5 + ",");
		}
		if (!TextUtils.isEmpty(mImageUrl6))
		{
			builder.append(mImageUrl6);
		}
		return builder.toString();
	}

	private void httpPublishComment(String commentContent, String urls)
	{
		showLoadingDialog("评分中...");
		ProfileScoreUploadPhotoManager.userScoreToPrize(mScoreId, mTargedId, commentContent, urls, commentType, new IonProfileUserScoreToPrizeCallback()
		{

			@Override
			public void onProfileUserScoreToPrizeSuccess(String message)
			{
				dismissLoadingDialog();
				ToastUtil.showConfirmToast(ProfileScoreActivity.this, message);

				EventBus.getDefault().post(groupPosition);
				finish();
			}

			@Override
			public void onProfileUserScoreToPrizeFail(String errormessage)
			{
				dismissLoadingDialog();
				ToastUtil.showAlertToast(ProfileScoreActivity.this, errormessage);
			}
		});
	}

	/**
	 * 获取评分选项
	 * method desc：
	 */
	private void getHttpScore()
	{
		showLoadingDialog("加载中...");
		ProfileScoreUploadPhotoManager.getHttpScore(new IonProfileScoreCallback()
		{
			@Override
			public void onProfileScoreSuccess(ProfileScoreInfo info)
			{
				dismissLoadingDialog();
				mProfileScoreInfo = info;
			}

			@Override
			public void onProfileScoreFail(String errormessage)
			{
				dismissLoadingDialog();
				ToastUtil.showAlertToast(ProfileScoreActivity.this, errormessage);
			}
		});
	}

	// ===================
	/**
	 * 显示上传的对话框
	 */
	private void showUploadDialog()
	{
		if (mUploadActionSheetDialog == null)
		{
			mUploadActionSheetDialog = new ProfileActionSheetDialog(this, R.style.style_action_sheet_dialog);
			mUploadActionSheetDialog.setSheetClickListener(this);
		}
		if (!mUploadActionSheetDialog.isShowing())
		{
			mUploadActionSheetDialog.show();
			mUploadActionSheetDialog.setTips(getString(R.string.please_select_you_theme_photo));
		}
	}

	@Override
	public void onClickListener(ACTION action, ProfileActionSheetDialog profileActionSheetDialog)
	{
		if (action == ACTION.FIRST_ITEM)
		{
			if (!V2GogoApplication.getMasterLoginState())
			{
				AccountLoginActivity.forwardAccountLogin(this);
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
				AccountLoginActivity.forwardAccountLogin(this);
			}
			else
			{
				forward2Album();
			}
		}
	}

	@Override
	protected void getAlbumPath(String albumPath)
	{
		super.getAlbumPath(albumPath);
		if (null != albumPath)
		{
			// PhotoUtil.forward2Crop(this, Uri.fromFile(new File(albumPath)));
			uploadImage(albumPath);// 上传图片
			// bindImageView(albumPath);
		}
	}

	/**
	 * method desc：
	 * 
	 * @param albumPath
	 *            本地图片地址
	 * @param key
	 */
	private void bindImageView(String albumPath, String key)
	{
		if (mUploadPathMap == null)
		{
			mUploadPathMap = new HashMap<Integer, String>();
		}
		if (mUploadPathMap.containsKey(mImageViewId))
		{
			mUploadPathMap.remove(mImageViewId);
			mUploadPathMap.put(mImageViewId, albumPath);
		}
		else
		{
			mUploadPathMap.put(mImageViewId, albumPath);
		}
		switch (mImageViewId)
		{
			case R.id.imageview1:
				mImageUrl1 = key;
				GlideImageLoader.loadLocalFileDrawable(this, albumPath, mImageView1);
				setWidthAndHeight(mImageView1);
				mImageView2.setVisibility(View.VISIBLE);
				break;
			case R.id.imageview2:
				mImageUrl2 = key;
				GlideImageLoader.loadLocalFileDrawable(this, albumPath, mImageView2);
				setWidthAndHeight(mImageView2);
				mImageView3.setVisibility(View.VISIBLE);
				break;
			case R.id.imageview3:
				mImageUrl3 = key;
				GlideImageLoader.loadLocalFileDrawable(this, albumPath, mImageView3);
				setWidthAndHeight(mImageView3);
				mImageView4.setVisibility(View.VISIBLE);
				setWidthAndHeight(mImageView4);
				break;
			case R.id.imageview4:
				mImageUrl4 = key;
				GlideImageLoader.loadLocalFileDrawable(this, albumPath, mImageView4);
				setWidthAndHeight(mImageView4);
				mImageView5.setVisibility(View.VISIBLE);
				break;
			case R.id.imageview5:
				mImageUrl5 = key;
				GlideImageLoader.loadLocalFileDrawable(this, albumPath, mImageView5);
				setWidthAndHeight(mImageView5);
				mImageView6.setVisibility(View.VISIBLE);
				break;
			case R.id.imageview6:
				mImageUrl6 = key;
				GlideImageLoader.loadLocalFileDrawable(this, albumPath, mImageView6);
				setWidthAndHeight(mImageView6);
				ToastUtil.showAlertToast(this, "最多能上传6张照片");
				break;
		}
	}

	private void setWidthAndHeight(View view)
	{
		int width = view.getWidth();
		LayoutParams params = (LayoutParams) view.getLayoutParams();
		params.height = width;
		view.setLayoutParams(params);
	}

	/**
	 * method desc：上传图片
	 * 
	 * @param albumPath
	 */
	private void uploadImage(String albumPath)
	{
		showLoadingDialog("上传中...");
		mUploadPath = albumPath;
		mFile = compressBitmap(albumPath);
		ProfileScoreUploadPhotoManager.uploadScoreCommentPhoto(mScoreId, mProfileScoreInfo.getUploadToken(), mFile);
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
				mCompressPath = SDCardUtil.getSDCardPath() + "v2gogo" + File.separator + System.currentTimeMillis() + ".jpg";
				file = new File(mCompressPath);
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
	 * 删除文件
	 */
	private void deleteFile()
	{
		if (!TextUtils.isEmpty(mCompressPath))
		{
			File file = new File(mCompressPath);
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

	@Override
	protected void getCameraPath(String cameraPath)
	{
		super.getCameraPath(cameraPath);
		if (cameraPath != null)
		{
			// PhotoUtil.cameraCropImageUri(this, Uri.fromFile(new File(cameraPath)));
			// bindImageView(cameraPath);
			uploadImage(cameraPath);
		}
	}

	// ===========================eventbus=================================
	public void onEventMainThread(UploadErrorInfo uploadErrorInfo)
	{
		dismissLoadingDialog();
		ToastUtil.showAlertToast(this, "上传图像失败");
	}

	/**
	 * method desc：EventBus 回调 图片上传成功后的回调
	 * 
	 * @param key
	 *            图片路径
	 */
	public void onEventMainThread(String key)
	{
		dismissLoadingDialog();
		bindImageView(mUploadPath, key);
		deleteFile();// 删除压缩路径的图片
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

}
