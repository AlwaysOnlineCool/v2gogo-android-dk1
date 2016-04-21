package com.v2gogo.project.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.utils.common.ScreenUtil;

/**
 * 分享对话框
 * 
 * @author houjun
 */
public class V2gogoShareDialog extends Dialog implements android.view.View.OnClickListener
{

	public static enum SHARE_TYPE
	{
		SHARE_WEIXIN, SHARE_WENXI_COLLECTIONS, SHARE_MESSAGE, SHARE_QQ, SHARE_QZONE
	}

	private View mContentView;

	private boolean mIsInitWidth = false;
	private IonItemClickCallback mItemClickCallback;

	private TextView mTvTitle;
	private TextView mMessage;
	private TextView mShareWeixin;
	private TextView mShareWeiXinCollections;
	private TextView mQQ;
	private TextView mQzone;

	private Button mBtnCancel;

	public V2gogoShareDialog(Context context, boolean cancelable, OnCancelListener cancelListener)
	{
		super(context, cancelable, cancelListener);
		setDialogParams();
	}

	public V2gogoShareDialog(Context context, int theme)
	{
		super(context, theme);
		setDialogParams();
	}

	public V2gogoShareDialog(Context context)
	{
		super(context);
		setDialogParams();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if (null == mContentView)
		{
			mContentView = getLayoutInflater().inflate(R.layout.share_layout, null);
		}
		setContentView(mContentView);
		if (!mIsInitWidth)
		{
			Window dialogWindow = this.getWindow();
			WindowManager.LayoutParams lp = dialogWindow.getAttributes();
			lp.width = ScreenUtil.getScreenWidth(getContext());
			dialogWindow.setAttributes(lp);
			mIsInitWidth = true;
		}
		mMessage = (TextView) mContentView.findViewById(R.id.share_to_message);
		mShareWeixin = (TextView) mContentView.findViewById(R.id.share_to_weixin);
		mTvTitle = (TextView) mContentView.findViewById(R.id.share_layout_title);
		mShareWeiXinCollections = (TextView) mContentView.findViewById(R.id.share_to_weixin_friend);
		mBtnCancel = (Button) mContentView.findViewById(R.id.share_cancel);
		mQQ = (TextView) mContentView.findViewById(R.id.share_to_qq);
		mQzone = (TextView) mContentView.findViewById(R.id.share_to_qzone);
		registerListener();
	}

	@Override
	public void show()
	{
		try
		{
			super.show();
		}
		catch (Exception exception)
		{
		}
	}

	@Override
	public void dismiss()
	{
		try
		{
			super.dismiss();
		}
		catch (Exception exception)
		{
		}
	}

	private void setDialogParams()
	{
		Window dialogWindow = getWindow();
		dialogWindow.setWindowAnimations(R.style.style_dialog_aniamtion);
		dialogWindow.setGravity(Gravity.BOTTOM);
		this.setCanceledOnTouchOutside(true);
	}

	public void setItemClickCallback(IonItemClickCallback mItemClickCallback)
	{
		this.mItemClickCallback = mItemClickCallback;
	}

	public void setShowMessage()
	{
		mMessage.setVisibility(View.VISIBLE);
		mTvTitle.setText(R.string.profile_invite_tip);
	}

	private void registerListener()
	{
		mMessage.setOnClickListener(this);
		mBtnCancel.setOnClickListener(this);
		mShareWeixin.setOnClickListener(this);
		mShareWeiXinCollections.setOnClickListener(this);
		mQQ.setOnClickListener(this);
		mQzone.setOnClickListener(this);
	}

	/**
	 * 分享数据回调
	 * 
	 * @author houjun
	 */
	public interface IonItemClickCallback
	{
		public void onShareClick(SHARE_TYPE type);
	}

	@Override
	public void onClick(View view)
	{
		SHARE_TYPE shareType = null;
		switch (view.getId())
		{
			case R.id.share_to_weixin:
				shareType = SHARE_TYPE.SHARE_WEIXIN;
				if (null != mItemClickCallback)
				{
					this.dismiss();
					mItemClickCallback.onShareClick(shareType);
				}
				break;

			case R.id.share_to_weixin_friend:
				shareType = SHARE_TYPE.SHARE_WENXI_COLLECTIONS;
				if (null != mItemClickCallback)
				{
					this.dismiss();
					mItemClickCallback.onShareClick(shareType);
				}
				break;

			case R.id.share_to_message:
				shareType = SHARE_TYPE.SHARE_MESSAGE;
				if (null != mItemClickCallback)
				{
					this.dismiss();
					mItemClickCallback.onShareClick(shareType);
				}
				break;

			case R.id.share_to_qq:
				shareType = SHARE_TYPE.SHARE_QQ;
				if (null != mItemClickCallback)
				{
					this.dismiss();
					mItemClickCallback.onShareClick(shareType);
				}
				break;
			case R.id.share_to_qzone:
				shareType = SHARE_TYPE.SHARE_QZONE;
				if (null != mItemClickCallback)
				{
					this.dismiss();
					mItemClickCallback.onShareClick(shareType);
				}
				break;

			case R.id.share_cancel:
				this.dismiss();
				break;

			default:
				break;
		}
	}
}
