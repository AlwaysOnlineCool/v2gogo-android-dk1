package com.v2gogo.project.activity.home.theme;

import cn.sharesdk.framework.ShareSDK;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.BaseActivity;
import com.v2gogo.project.domain.ShareInfo;
import com.v2gogo.project.utils.share.CustomPlatformActionListener;
import com.v2gogo.project.utils.share.ShareUtils;
import com.v2gogo.project.views.dialog.V2gogoShareDialog;
import com.v2gogo.project.views.dialog.V2gogoShareDialog.IonItemClickCallback;
import com.v2gogo.project.views.dialog.V2gogoShareDialog.SHARE_TYPE;

/**
 * 需要分享的activity的基类
 * 
 * @author houjun
 */
public abstract class BaseShareActivity extends BaseActivity
{

	private V2gogoShareDialog mShareDialog;

	/**
	 * 得到分享的参数
	 * 
	 * @return
	 */
	public abstract ShareInfo getShareInfo();

	@Override
	public void clearRequestTask()
	{
	}

	@Override
	public void onInitViews()
	{

	}

	@Override
	public int getCurrentLayoutId()
	{
		return 0;
	}

	/**
	 * 显示分享的对话框
	 */
	public void showShareDialog()
	{
		if (null == mShareDialog)
		{
			mShareDialog = new V2gogoShareDialog(this, R.style.style_action_sheet_dialog);
			mShareDialog.setItemClickCallback(new IonItemClickCallback()
			{
				@Override
				public void onShareClick(SHARE_TYPE type)
				{
					shareContent(type);
				}
			});
		}
		if (!mShareDialog.isShowing())
		{
			mShareDialog.show();
		}
	}

	/**
	 * 分享内容
	 */
	private void shareContent(SHARE_TYPE type)
	{
		ShareInfo shareInfo = getShareInfo();
		com.v2gogo.project.utils.share.ShareUtils.SHARE_TYPE shareType = com.v2gogo.project.utils.share.ShareUtils.SHARE_TYPE.SHARE_WEIXIN;
		if (shareInfo != null)
		{
			ShareSDK.initSDK(this.getApplicationContext());
			String tip = getResources().getString(R.string.share_success_tip);
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
			ShareUtils.share(this, shareInfo.getTitle(), shareInfo.getDescription(), shareInfo.getHref(), shareInfo.getImageUrl(), shareInfo.getBitmap(),
					shareType, new CustomPlatformActionListener(this, tip));
		}
	}
}
