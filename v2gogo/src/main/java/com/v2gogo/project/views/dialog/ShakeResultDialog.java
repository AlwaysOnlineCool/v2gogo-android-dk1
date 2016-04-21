package com.v2gogo.project.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.home.HomeShakeActivity;
import com.v2gogo.project.activity.profile.ProfilePrizeActivity;
import com.v2gogo.project.domain.home.ShakePrizeInfo;
import com.v2gogo.project.domain.home.ShakeResultInfo;
import com.v2gogo.project.main.image.GlideImageLoader;
import com.v2gogo.project.views.dialog.ShakeRealNoticeDialog.IonClickDialogButton;

public class ShakeResultDialog extends Dialog
{
	private View mContentView;
	private ImageButton mIbtnClose;
	private ImageView mGoodThumb;
	private TextView mTvGoodName;
	private ImageView mIvShakeState;

	private IonShakeResultCallback mShakeResultCallback;
	private ShakeRealNoticeDialog mRealNoticeDialog;

	public ShakeResultDialog(Context context, boolean cancelable, OnCancelListener cancelListener)
	{
		super(context, cancelable, cancelListener);
		setDialogParams();
	}

	public ShakeResultDialog(HomeShakeActivity homeShakeActivity, int theme)
	{
		super(homeShakeActivity, theme);
		setDialogParams();
	}

	public ShakeResultDialog(Context context)
	{
		super(context);
		setDialogParams();
	}

	public void setShakeResultCallback(IonShakeResultCallback mShakeResultCallback)
	{
		this.mShakeResultCallback = mShakeResultCallback;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if (mContentView == null)
		{
			mContentView = LayoutInflater.from(getContext()).inflate(R.layout.shake_prize_result_dialog_layout, null);
		}
		setContentView(mContentView);
		mIvShakeState = (ImageView) mContentView.findViewById(R.id.imageView1);
		mGoodThumb = (ImageView) mContentView.findViewById(R.id.shake_result_dialog_layout_goods_thumb);
		mTvGoodName = (TextView) mContentView.findViewById(R.id.shake_result_dialog_layout_goods_name);
		mIbtnClose = (ImageButton) mContentView.findViewById(R.id.shake_result_dialog_layout_btn_close);
	}

	@Override
	public void dismiss()
	{
		try
		{
			if (mGoodThumb != null)
			{
				mGoodThumb.setImageBitmap(null);
			}
			if (mIvShakeState != null)
			{
				mIvShakeState.setImageBitmap(null);
			}
			super.dismiss();
		}
		catch (Exception exception)
		{
		}
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

	private void setDialogParams()
	{
		Window dialogWindow = getWindow();
		dialogWindow.setWindowAnimations(R.style.style_shake_result_dialog);
		this.setCanceledOnTouchOutside(false);
		this.setCancelable(false);
	}

	/**
	 * 设置摇一摇结果
	 * 
	 * @param shakeResultInfo
	 */
	public void setShakeResult(final ShakeResultInfo shakeResultInfo)
	{
		if (shakeResultInfo != null)
		{
			if (shakeResultInfo.getType() == ShakeResultInfo.SHAKE_RESULT_NO_KAI_JIANG)
			{
				showNokaiJiangResult(shakeResultInfo);
			}
			else if (shakeResultInfo.getType() == ShakeResultInfo.SHAKE_RESULT_NO_GET_ANYTHING)
			{
				showNoGetAnythingResult(shakeResultInfo);
			}
			else if (shakeResultInfo.getType() == ShakeResultInfo.SHAKE_RESULT_GET_COIN)
			{
				displayLargeCoinResult(shakeResultInfo);
			}
			else if (shakeResultInfo.getType() == ShakeResultInfo.SHAKE_RESULT_GET_PRIZE)
			{
				diaplayRealPrizeResult(shakeResultInfo);
			}
			registerListener(shakeResultInfo);
		}
	}

	/**
	 * 注册监听
	 * 
	 * @param shakeResultInfo
	 */
	private void registerListener(final ShakeResultInfo shakeResultInfo)
	{
		mIbtnClose.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				ShakeResultDialog.this.dismiss();
				if (shakeResultInfo.getType() == ShakeResultInfo.SHAKE_RESULT_GET_PRIZE)
				{
					showRealPrizeDialog(getContext());
				}
				else
				{
					if (null != mShakeResultCallback)
					{
						mShakeResultCallback.onShakeResultClose();
					}
				}
			}
		});
	}

	/**
	 * 显示实物大奖
	 * 
	 * @param context
	 */
	private void showRealPrizeDialog(final Context context)
	{
		if (null == mRealNoticeDialog)
		{
			mRealNoticeDialog = new ShakeRealNoticeDialog(context, R.style.style_action_sheet_dialog);
			mRealNoticeDialog.setOnClickDialogButton(new IonClickDialogButton()
			{
				@Override
				public void onClickDialog(boolean isLeft)
				{
					if (null != mShakeResultCallback)
					{
						mShakeResultCallback.onShakeResultClose();
					}
					if (isLeft)
					{
						Intent intent = new Intent(getContext(), ProfilePrizeActivity.class);
						getContext().startActivity(intent);
					}
				}
			});
		}
		if (!mRealNoticeDialog.isShowing())
		{
			mRealNoticeDialog.show();
		}
	}

	/**
	 * 显示得到实物奖
	 * 
	 * @param shakeResultInfo
	 */
	private void diaplayRealPrizeResult(ShakeResultInfo shakeResultInfo)
	{
		ShakePrizeInfo shakePrizeInfo = shakeResultInfo.getmShakePrizeInfo();
		if (null != shakePrizeInfo)
		{
			GlideImageLoader.loadImageWithFixedSize(getContext(), shakePrizeInfo.getThumialUrl(), mGoodThumb);
			mTvGoodName.setText(shakeResultInfo.getMessage());
			mIvShakeState.setImageResource(R.drawable.yaoyaole_icons_yaoyaole);
		}
	}

	/**
	 * 显示大金币
	 * 
	 * @param shakeResultInfo
	 */
	private void displayLargeCoinResult(ShakeResultInfo shakeResultInfo)
	{
		mGoodThumb.setImageResource(R.drawable.yaoyaole_manycoins);
		mTvGoodName.setText(shakeResultInfo.getMessage());
		mIvShakeState.setImageResource(R.drawable.yaoyaole_icons_yaoyaole);
	}

	/**
	 * 显示没有得到奖品
	 */
	private void showNoGetAnythingResult(ShakeResultInfo shakeResultInfo)
	{
		mGoodThumb.setImageResource(R.drawable.yaoyaole_pic_none);
		mTvGoodName.setText(shakeResultInfo.getMessage());
		mIvShakeState.setImageResource(R.drawable.yaoyaole_icons_weizhongjiang);
	}

	/**
	 * 显示没有开奖
	 * 
	 * @param prizeInfos
	 */
	private void showNokaiJiangResult(ShakeResultInfo shakeResultInfo)
	{
		mGoodThumb.setImageResource(R.drawable.yaoyaole_pic_notstart);
		mTvGoodName.setText(shakeResultInfo.getMessage());
		mIvShakeState.setImageResource(R.drawable.yaoyaole_icons_huodongweikaishi);
	}

	/**
	 * 设置回调
	 * 
	 * @author houjun
	 */
	public interface IonShakeResultCallback
	{
		public void onShakeResultClose();
	}
}
