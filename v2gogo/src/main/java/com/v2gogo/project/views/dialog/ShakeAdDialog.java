package com.v2gogo.project.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.v2gogo.project.InternalLinksTool;
import com.v2gogo.project.R;
import com.v2gogo.project.activity.WebViewActivity;
import com.v2gogo.project.domain.home.ShakeResultInfo;
import com.v2gogo.project.main.image.GlideImageLoader;
import com.v2gogo.project.utils.common.LogUtil;

/**
 * 摇一摇广告对话框
 * 
 * @author houjun
 */
public class ShakeAdDialog extends Dialog
{

	private View mContentView;
	private ImageButton mIbtnClose;

	private ImageView mGoodThumb;
	private ImageView mShakeResult;

	private TextView mTvGoodName;

	private IonShakeAdResultCallback mShakeAdResultCallback;

	public ShakeAdDialog(Context context, boolean cancelable, OnCancelListener cancelListener)
	{
		super(context, cancelable, cancelListener);
		setDialogParams();
	}

	public ShakeAdDialog(Context context, int theme)
	{
		super(context, theme);
		setDialogParams();
	}

	public ShakeAdDialog(Context context)
	{
		super(context);
		setDialogParams();
	}

	private void setDialogParams()
	{
		Window dialogWindow = getWindow();
		dialogWindow.setWindowAnimations(R.style.style_shake_result_dialog);
		this.setCanceledOnTouchOutside(false);
		this.setCancelable(false);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if (null == mContentView)
		{
			mContentView = LayoutInflater.from(getContext()).inflate(R.layout.shake_ad_result_dialog_layout, null);
		}
		setContentView(mContentView);
		mIbtnClose = (ImageButton) mContentView.findViewById(R.id.shake_ad_result_dialog_layout_btn_close);
		mShakeResult = (ImageView) mContentView.findViewById(R.id.shake_ad_result_dialog_layout_shake_result);
		mTvGoodName = (TextView) mContentView.findViewById(R.id.shake_ad_result_dialog_layout_goods_name);
		mGoodThumb = (ImageView) mContentView.findViewById(R.id.shake_ad_result_dialog_layout_goods_thumb);
	}

	public void setOnShakeAdResultCallback(IonShakeAdResultCallback mShakeAdResultCallback)
	{
		this.mShakeAdResultCallback = mShakeAdResultCallback;
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
			LogUtil.d(exception + "");
		}
		if (null != mShakeAdResultCallback)
		{
			mShakeAdResultCallback.IonShakeColse();
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

	/**
	 * 设置广告数据
	 * 
	 * @param shakeResultInfo
	 */
	public void setAdResultDatas(final ShakeResultInfo shakeResultInfo)
	{
		if (shakeResultInfo != null)
		{
			if (shakeResultInfo.getType() == ShakeResultInfo.SHAKE_RESULT_NO_KAI_JIANG)
			{
				mShakeResult.setImageResource(R.drawable.yaoyaole_icons_huodongweikaishi);
			}
			else if (shakeResultInfo.getType() == ShakeResultInfo.SHAKE_RESULT_NO_GET_ANYTHING)
			{
				mShakeResult.setImageResource(R.drawable.yaoyaole_icons_weizhongjiang);
			}
			if (!TextUtils.isEmpty(shakeResultInfo.getDesc()))
			{
				mTvGoodName.setText(shakeResultInfo.getDesc());
			}
			else
			{
				mTvGoodName.setText(shakeResultInfo.getMessage());
			}
			GlideImageLoader.loadImageWithFixedSize(getContext(), shakeResultInfo.getThumbialUrl(), mGoodThumb);
			registerLisetener(shakeResultInfo);
		}
	}

	/**
	 * 注册点击事件
	 * 
	 * @param shakeResultInfo
	 */
	private void registerLisetener(final ShakeResultInfo shakeResultInfo)
	{
		mGoodThumb.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				String url = shakeResultInfo.getUrl();
				if (!TextUtils.isEmpty(url))
				{
					// 在这里进行内部链接格式处理
					if (url.contains("v2gogo://"))
					{
						try
						{
							Uri uri = Uri.parse(url);
							int type = Integer.valueOf(uri.getQueryParameter("type")).intValue();
							String url_target = uri.getQueryParameter("url");
							String id = uri.getQueryParameter("srcId");
							InternalLinksTool.jump2Activity(getContext(), type, id, url_target, null);
						}
						catch (Exception e)
						{
							LogUtil.d("houjun", "e->" + e.getLocalizedMessage());
							e.printStackTrace();
						}
					}
					else
					{
						Intent intent = new Intent(getContext(), WebViewActivity.class);
						intent.putExtra(WebViewActivity.URL, shakeResultInfo.getUrl());
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						getContext().startActivity(intent);
						ShakeAdDialog.this.dismiss();
					}
				}
			}
		});
		mIbtnClose.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				ShakeAdDialog.this.dismiss();
			}
		});
	}

	/**
	 * 点击关闭
	 * 
	 * @author houjun
	 */
	public interface IonShakeAdResultCallback
	{
		public void IonShakeColse();
	}
}
