package com.v2gogo.project.utils;

import android.content.Context;

import com.v2gogo.project.R;
import com.v2gogo.project.views.dialog.ProfileActionSheetDialog;
import com.v2gogo.project.views.dialog.ProfileActionSheetDialog.IonActionSheetClickListener;

public class UploadActionSheetDialogUtil
{
	/**
	 * 显示上传的对话框
	 */
	public static void showUploadDialog(Context context, IonActionSheetClickListener listener)
	{

		ProfileActionSheetDialog mUploadActionSheetDialog = new ProfileActionSheetDialog(context, R.style.style_action_sheet_dialog);
		mUploadActionSheetDialog.setSheetClickListener(listener);
		if (!mUploadActionSheetDialog.isShowing())
		{
			mUploadActionSheetDialog.show();
			mUploadActionSheetDialog.setTips(context.getString(R.string.please_select_you_theme_photo));
		}
	}
}
