package com.v2gogo.project.activity.profile;

import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.BaseActivity;
import com.v2gogo.project.db.MatserInfo;
import com.v2gogo.project.domain.home.theme.UploadErrorInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.main.image.GlideImageLoader;
import com.v2gogo.project.manager.account.AccountInfoManager;
import com.v2gogo.project.manager.account.AccountInfoManager.IonModifyProfileCallback;
import com.v2gogo.project.manager.upload.AccountAvatarUploadManager;
import com.v2gogo.project.utils.common.BitmapCompressUtil;
import com.v2gogo.project.utils.common.PhotoUtil;
import com.v2gogo.project.utils.common.ToastUtil;
import com.v2gogo.project.views.dialog.ProfileActionSheetDialog;
import com.v2gogo.project.views.dialog.ProfileActionSheetDialog.ACTION;
import com.v2gogo.project.views.dialog.ProfileActionSheetDialog.IonActionSheetClickListener;
import com.v2gogo.project.views.dialog.ProfileActionSheetEditDialog;
import com.ypy.eventbus.EventBus;

public class ProfileInfoActivity extends BaseActivity implements OnClickListener, IonActionSheetClickListener,
		ProfileActionSheetEditDialog.IonActionSheetClickListener
{
	private final int REQUEST_CODE = 0X201;

	private RelativeLayout mAvatarLayout;
	private RelativeLayout mNikenameLayout;
	private RelativeLayout mGenderLayout;
	private RelativeLayout mAddressLayout;

	private File mFile;
	private ImageView mAvatar;
	private TextView mTextNinkname;
	private TextView mTextGender;
	private TextView mGogoNumber;
	private TextView mAddress;

	private ProfileActionSheetEditDialog mNinknameEditDialog;
	private ProfileActionSheetDialog mAvatarActionSheetDialog;
	private ProfileActionSheetDialog mGenderActionSheetDialog;

	@Override
	public void onInitViews()
	{
		mGogoNumber = (TextView) findViewById(R.id.profile_info_user_gogo);
		mAddress = (TextView) findViewById(R.id.profile_info_user_address);
		mTextGender = (TextView) findViewById(R.id.profile_info_user_gender);
		mAvatar = (ImageView) findViewById(R.id.profile_info_user_avatar_image);
		mTextNinkname = (TextView) findViewById(R.id.profile_info_user_nikename);
		mAvatarLayout = (RelativeLayout) findViewById(R.id.profile_info_user_avatar_layout);
		mNikenameLayout = (RelativeLayout) findViewById(R.id.profile_info_user_nikename_layout);
		mGenderLayout = (RelativeLayout) findViewById(R.id.profile_info_user_gender_layout);
		mAddressLayout = (RelativeLayout) findViewById(R.id.profile_info_user_address_layout);
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.profile_info_activity_layout;
	}

	@Override
	protected void registerListener()
	{
		super.registerListener();
		EventBus.getDefault().register(this);
		mGenderLayout.setOnClickListener(this);
		mAvatarLayout.setOnClickListener(this);
		mAddressLayout.setOnClickListener(this);
		mNikenameLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.profile_info_user_avatar_layout:
				showModifyAvatarDialog();
				break;

			case R.id.profile_info_user_nikename_layout:
				showModifyNicknameDialog();
				break;

			case R.id.profile_info_user_gender_layout:
				showModifyGenderDialog();
				break;

			case R.id.profile_info_user_address_layout:
				Intent intent = new Intent(this, ProfileProviceActivity.class);
				startActivityForResult(intent, REQUEST_CODE);
				break;

			default:
				break;
		}
	}

	@Override
	public void onClickListener(ACTION action, ProfileActionSheetDialog profileActionSheetDialog)
	{
		if (profileActionSheetDialog == mAvatarActionSheetDialog)
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
		else
		{
			modifyProfileGender(action);
		}
	}

	@Override
	public void onClickListener(ProfileActionSheetEditDialog.ACTION action, String text, ProfileActionSheetEditDialog profileActionSheetDialog)
	{
		modifyProfileNickname(action, text);
	}

	@Override
	protected void getAlbumPath(String albumPath)
	{
		super.getAlbumPath(albumPath);
		if (null != albumPath)
		{
			PhotoUtil.forward2Crop(this, Uri.fromFile(new File(albumPath)));
		}
	}

	@Override
	protected void getCameraPath(String cameraPath)
	{
		super.getCameraPath(cameraPath);
		if (cameraPath != null)
		{
			PhotoUtil.cameraCropImageUri(this, Uri.fromFile(new File(cameraPath)));
		}
	}

	@Override
	protected void getCompressPath(Bitmap bitmap)
	{
		modifyAvatar(bitmap);
	}

	/**
	 * 修改头像
	 * 
	 * @param bitmap
	 */
	private void modifyAvatar(Bitmap bitmap)
	{
		if (V2GogoApplication.getMasterLoginState())
		{
			Bitmap compressBitmap = BitmapCompressUtil.comp(bitmap);
			if (compressBitmap != null)
			{
				String path = PhotoUtil.getAvatarPath();
				mFile = new File(path);
				if (!mFile.exists())
				{
					mFile.exists();
				}
				try
				{
					boolean result = compressBitmap.compress(CompressFormat.JPEG, 70, new FileOutputStream(mFile));
					if (!compressBitmap.isRecycled())
					{
						compressBitmap.recycle();
					}
					if (!bitmap.isRecycled())
					{
						bitmap.recycle();
					}
					if (result)
					{
						showLoadingDialog(R.string.profile_modify_avatar_uploading);
						AccountAvatarUploadManager.modifyAccountAvatar(mFile, V2GogoApplication.getCurrentMatser().getUsername());
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
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
		ToastUtil.showAlertToast(ProfileInfoActivity.this, R.string.profile_modify_avatar_fail);
	}

	/**
	 * 上传成功
	 * 
	 * @param progress
	 */
	public void onEventMainThread(MatserInfo matserInfo)
	{
		dismissLoadingDialog();
		ToastUtil.showConfirmToast(ProfileInfoActivity.this, R.string.profile_modify_avatar_success);
		if (null != matserInfo)
		{
			displayProfileInfo(matserInfo);
		}
		else
		{
			GlideImageLoader.loadInternalDrawable(ProfileInfoActivity.this, R.drawable.user_icons_user_orange, mAvatar);
		}
	}

	@Override
	protected void onInitLoadDatas()
	{
		super.onInitLoadDatas();
		if (V2GogoApplication.getMasterLoginState())
		{
			displayProfileInfo(V2GogoApplication.getCurrentMatser());
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (null != data && requestCode == REQUEST_CODE && resultCode == RESULT_OK)
		{
			String city = data.getStringExtra("city");
			if (null != city && V2GogoApplication.getMasterLoginState())
			{
				AccountInfoManager.lunachModifyProfileCity(city, V2GogoApplication.getCurrentMatser().getUsername(), new CustomOnModifyProfileCallback(this));
			}
		}
	}

	/**
	 * 显示修改性别
	 */
	private void showModifyGenderDialog()
	{
		if (null == mGenderActionSheetDialog)
		{
			mGenderActionSheetDialog = new ProfileActionSheetDialog(this, R.style.style_action_sheet_dialog);
			mGenderActionSheetDialog.setSheetClickListener(this);
		}
		if (!mGenderActionSheetDialog.isShowing())
		{
			mGenderActionSheetDialog.show();
			mGenderActionSheetDialog.setTips(R.string.please_selection_sex_tip, R.string.profile_info_gender_man, R.string.profile_info_gender_felman);
		}
	}

	/**
	 * 显示修改用户昵称对话框
	 */
	private void showModifyNicknameDialog()
	{
		if (null == mNinknameEditDialog)
		{
			mNinknameEditDialog = new ProfileActionSheetEditDialog(this, R.style.style_action_sheet_dialog);
			mNinknameEditDialog.setSheetClickListener(this);
		}
		if (!mNinknameEditDialog.isShowing())
		{
			mNinknameEditDialog.show();
		}
	}

	/**
	 * 显示修改用户头像对话框
	 */
	private void showModifyAvatarDialog()
	{
		if (mAvatarActionSheetDialog == null)
		{
			mAvatarActionSheetDialog = new ProfileActionSheetDialog(this, R.style.style_action_sheet_dialog);
			mAvatarActionSheetDialog.setSheetClickListener(this);
		}
		if (!mAvatarActionSheetDialog.isShowing())
		{
			mAvatarActionSheetDialog.show();
		}
	}

	/**
	 * 显示用户信息
	 */
	private void displayProfileInfo(MatserInfo masterInfo)
	{
		if (null != mFile && mFile.exists())
		{
			mFile.delete();
		}
		GlideImageLoader.loadAvatarImageWithFixedSize(this, masterInfo.getThumbialAvatar(), mAvatar);
		mTextGender.setText(masterInfo.getSex() == 0 ? getString(R.string.profile_info_gender_man) : getString(R.string.profile_info_gender_felman));
		mTextNinkname.setText(masterInfo.getFullname());
		mGogoNumber.setText(masterInfo.getUsername());
		mAddress.setText(masterInfo.getCity());
	}

	/**
	 * 修改用户性别
	 * 
	 * @param action
	 */
	private void modifyProfileGender(ACTION action)
	{
		int gender = 0;
		if (action == ACTION.FIRST_ITEM)
		{
			gender = 0;
		}
		else if (action == ACTION.SECOND_ITEM)
		{
			gender = 1;
		}
		if (V2GogoApplication.getMasterLoginState())
		{
			MatserInfo masterInfo = V2GogoApplication.getCurrentMatser();
			if (masterInfo.getSex() != gender)
			{
				AccountInfoManager.lunachModifyProfileGender(gender, masterInfo.getUsername(), new CustomOnModifyProfileCallback(this));
			}
		}
	}

	/**
	 * 修改用户昵称
	 */
	private void modifyProfileNickname(com.v2gogo.project.views.dialog.ProfileActionSheetEditDialog.ACTION action, String text)
	{
		if (action == ProfileActionSheetEditDialog.ACTION.SURE)
		{
			if (V2GogoApplication.getMasterLoginState())
			{
				MatserInfo masterInfo = V2GogoApplication.getCurrentMatser();
				if (!TextUtils.isEmpty(text) && !text.equals(masterInfo.getFullname()))
				{
					AccountInfoManager.lunachModifyProfileNikename(text.trim(), masterInfo.getUsername(), new CustomOnModifyProfileCallback(this));
				}
			}
		}
	}

	@Override
	public void clearRequestTask()
	{
		EventBus.getDefault().unregister(this);
		AccountInfoManager.clearModifyProfileCityTask();
		AccountInfoManager.clearModifyProfileGenderTask();
		AccountInfoManager.clearodifyProfileNikenameTask();
	}

	private class CustomOnModifyProfileCallback implements IonModifyProfileCallback
	{
		private Activity context;
		public CustomOnModifyProfileCallback(Activity context)
		{
			super();
			this.context = context;
		}

		@Override
		public void onModifyProfileSuccess(MatserInfo masterInfo)
		{
			ToastUtil.showConfirmToast(context, R.string.profile_modify_success);
			if (null != masterInfo)
			{
				displayProfileInfo(masterInfo);
			}
			else
			{
				mAvatar.setImageResource(R.drawable.user_icons_user_orange);
			}
		}

		@Override
		public void onModifyProfileFail(String errorMessage)
		{
			ToastUtil.showAlertToast(context, errorMessage);
		}
	}
}
