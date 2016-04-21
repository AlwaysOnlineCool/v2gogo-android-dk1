package com.v2gogo.project.activity.home.theme;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.BaseActivity;
import com.v2gogo.project.main.image.GlideImageLoader;
import com.v2gogo.project.manager.upload.ThemePhotoUploadManager;
import com.v2gogo.project.utils.common.ToastUtil;
import com.v2gogo.project.utils.http.IntentExtraConstants;

/**
 * 主题图片发布界面
 * 
 * @author houjun
 */
public class HomeThemePhotoPostActivity extends BaseActivity implements OnClickListener
{

	private EditText mEditText;
	private ImageView mImageView;
	private Button mBtnUpload;

	private String mPath;

	@Override
	public void clearRequestTask()
	{
	}

	@Override
	public void onInitViews()
	{
		mBtnUpload = (Button) findViewById(R.id.home_theme_photo_post_upload_btn);
		mEditText = (EditText) findViewById(R.id.home_theme_photo_post_edit_content);
		mImageView = (ImageView) findViewById(R.id.home_theme_photo_post_upload_image);
	}

	@Override
	@TargetApi(11)
	protected void onInitIntentData(Intent intent)
	{
		super.onInitIntentData(intent);
		if (intent != null)
		{
			mPath = intent.getStringExtra(IntentExtraConstants.PATH);
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
					Intent intent = new Intent(this, HomeThemePhotoTabActivity.class);
					intent.putExtra(IntentExtraConstants.THEME_PHOTO_DESC, string.trim());
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					finish();
				}
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
		}
	}

}
