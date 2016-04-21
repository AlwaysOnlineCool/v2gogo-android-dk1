package com.v2gogo.project.activity.home;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.v2gogo.project.R;
import com.v2gogo.project.activity.BaseActivity;
import com.v2gogo.project.domain.CoinChangeInfo;
import com.v2gogo.project.domain.home.ShakeAdInfo;
import com.v2gogo.project.domain.home.ShakeResultInfo;
import com.v2gogo.project.hardware.ShakeDetector;
import com.v2gogo.project.hardware.ShakeDetector.OnShakeListener;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.main.image.DisplayImageOptionsFactory;
import com.v2gogo.project.manager.HttpJsonObjectRequest.IOnDataReceiveMessageCallback;
import com.v2gogo.project.manager.home.ShakeManager;
import com.v2gogo.project.manager.home.ShakeManager.IonShakeCallback;
import com.v2gogo.project.utils.common.ToastUtil;
import com.v2gogo.project.utils.parse.JsonParser;
import com.v2gogo.project.views.dialog.ShakeAdDialog;
import com.v2gogo.project.views.dialog.ShakeAdDialog.IonShakeAdResultCallback;
import com.v2gogo.project.views.dialog.ShakeResultDialog;
import com.v2gogo.project.views.dialog.ShakeResultDialog.IonShakeResultCallback;
import com.ypy.eventbus.EventBus;

/**
 * 摇摇乐主界面
 * 
 * @author houjun
 */

public class HomeShakeActivity extends BaseActivity implements OnShakeListener, OnClickListener, AnimationListener, IonShakeResultCallback,
		IonShakeAdResultCallback
{
	private boolean mIsEnable = true;

	private TextView mLeftCoin;

	private ImageView mHand;
	private RelativeLayout mRelativeLayout;

	private Animation mAnimation;
	private ShakeDetector mShakeDetector;
	private ShakeResultInfo mShakeResultInfo;

	private ShakeAdDialog mShakeAdDialog;
	private ShakeResultDialog mShakeResultDialog;

	private SoundPool mSoundPool;
	private Map<Integer, Integer> resId;

	private ImageView inside_imageview;
	private ImageView circle_imageview;

	@Override
	@SuppressWarnings("deprecation")
	public void onInitViews()
	{
		mLeftCoin = (TextView) findViewById(R.id.home_shake_left_coin);
		mHand = (ImageView) findViewById(R.id.home_shake_hand_imageview);
		mRelativeLayout = (RelativeLayout) findViewById(R.id.home_shake_container_layout);
		DisplayImageOptions displayImageOptions = DisplayImageOptionsFactory.getDrawableDisplayImageOptions();
		Bitmap bitmap = ImageLoader.getInstance().loadImageSync("drawable://" + R.drawable.yaoyaole_bg, displayImageOptions);
		mRelativeLayout.setBackgroundDrawable(new BitmapDrawable(bitmap));

		inside_imageview = (ImageView) findViewById(R.id.home_shake_inside_imageview);
		circle_imageview = (ImageView) findViewById(R.id.home_shake_circle_imageview);
	}

	@Override
	protected void onInitLoadDatas()
	{
		super.onInitLoadDatas();
		initSound();
		if (V2GogoApplication.getMasterLoginState())
		{
			mLeftCoin.setText(V2GogoApplication.getCurrentMatser().getCoin() + "");
		}
		mShakeDetector = new ShakeDetector(this);
		getNetShakeAds();
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.home_shake_activity_layout;
	}

	@Override
	protected void registerListener()
	{
		super.registerListener();
		EventBus.getDefault().register(this);
		mShakeDetector.registerOnShakeListener(this);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		if (null != mShakeDetector)
		{
			try
			{
				mShakeDetector.start();
			}
			catch (UnsupportedOperationException exception)
			{
				ToastUtil.showAlertToast(this, R.string.shake_fail_tip);
			}
		}
	}

	@Override
	protected void onPause()
	{
		if (null != mShakeDetector)
		{
			mShakeDetector.stop();
		}
		super.onPause();
	}

	@Override
	public void onShake()
	{
		if (null == mAnimation)
		{
			mAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_yao_yi_yao_hand);
			mAnimation.setFillEnabled(true);
			mAnimation.setFillAfter(false);
			mAnimation.setAnimationListener(this);
		}
		if (mShakeAdDialog != null && mShakeAdDialog.isShowing())
		{
			mShakeAdDialog.dismiss();
		}
		if (mIsEnable)
		{
			mHand.startAnimation(mAnimation);
			if (V2GogoApplication.getMasterLoginState())
			{
				ShakeManager.luanchShakeRequest(new IonShakeCallback()
				{
					@Override
					public void onShakeSuccess(ShakeResultInfo shakeResultInfo)
					{
						mShakeResultInfo = shakeResultInfo;
					}

					@Override
					public void onShakeFail(String errorMessage)
					{
						mShakeResultInfo = null;
						ToastUtil.showAlertToast(HomeShakeActivity.this, errorMessage);
					}
				});
				mIsEnable = false;
			}
		}
	}

	/**
	 * method desc：网络获取摇一摇广告
	 */
	private void getNetShakeAds()
	{
		ShakeManager.getShakeAds(new IOnDataReceiveMessageCallback()
		{

			@SuppressLint("NewApi")
			@Override
			public void onSuccess(int code, String message, JSONObject response)
			{
				if (response != null)
				{
					try
					{
						String result = response.optString("result");
						JSONObject jsonObject = new JSONObject(result);
						String resultStr = jsonObject.optString("list");
						Type type = new TypeToken<ArrayList<ShakeAdInfo>>()
						{
						}.getType();
						@SuppressWarnings("unchecked")
						ArrayList<ShakeAdInfo> ads = (ArrayList<ShakeAdInfo>) JsonParser.parseObjectList(resultStr, type);
						for (ShakeAdInfo shakeAdInfo : ads)
						{
							DisplayImageOptions options = DisplayImageOptionsFactory.getShakeAdsDisplayImageOptions();
							switch (shakeAdInfo.getType())
							{
								case 14:
									ImageLoader.getInstance().loadImage(shakeAdInfo.getImg(), options, new ImageLoadingListener()
									{
										@Override
										public void onLoadingStarted(String arg0, View arg1)
										{

										}

										@Override
										public void onLoadingFailed(String arg0, View arg1, FailReason arg2)
										{

										}

										@Override
										public void onLoadingComplete(String arg0, View arg1, Bitmap bitmap)
										{
											mRelativeLayout.setBackground(new BitmapDrawable(null, bitmap));
										}

										@Override
										public void onLoadingCancelled(String arg0, View arg1)
										{

										}
									});
									break;
								case 15:
									ImageLoader.getInstance().loadImage(shakeAdInfo.getImg(), options, new ImageLoadingListener()
									{
										@Override
										public void onLoadingStarted(String arg0, View arg1)
										{

										}

										@Override
										public void onLoadingFailed(String arg0, View arg1, FailReason arg2)
										{

										}

										@Override
										public void onLoadingComplete(String arg0, View arg1, Bitmap bitmap)
										{
											// mRelativeLayout.setBackground(new
											// BitmapDrawable(null, bitmap));
											inside_imageview.setImageBitmap(bitmap);
										}

										@Override
										public void onLoadingCancelled(String arg0, View arg1)
										{

										}
									});
									break;
								case 16:
									ImageLoader.getInstance().loadImage(shakeAdInfo.getImg(), options, new ImageLoadingListener()
									{
										@Override
										public void onLoadingStarted(String arg0, View arg1)
										{

										}

										@Override
										public void onLoadingFailed(String arg0, View arg1, FailReason arg2)
										{

										}

										@Override
										public void onLoadingComplete(String arg0, View arg1, Bitmap bitmap)
										{
											// mRelativeLayout.setBackground(new
											// BitmapDrawable(null, bitmap));
											mHand.setImageBitmap(bitmap);
										}

										@Override
										public void onLoadingCancelled(String arg0, View arg1)
										{

										}
									});
									break;
								case 17:
									ImageLoader.getInstance().loadImage(shakeAdInfo.getImg(), options, new ImageLoadingListener()
									{
										@Override
										public void onLoadingStarted(String arg0, View arg1)
										{

										}

										@Override
										public void onLoadingFailed(String arg0, View arg1, FailReason arg2)
										{

										}

										@Override
										public void onLoadingComplete(String arg0, View arg1, Bitmap bitmap)
										{
											circle_imageview.setImageBitmap(bitmap);
										}

										@Override
										public void onLoadingCancelled(String arg0, View arg1)
										{

										}
									});
									break;
							}
						}
					}
					catch (JSONException e)
					{
						e.printStackTrace();
					}
				}

			}

			@Override
			public void onError(String errorMessage)
			{
				ToastUtil.showAlertToast(HomeShakeActivity.this, errorMessage);
			}
		});
	}

	@Override
	public void onClick(View view)
	{
		finish();
	}

	@Override
	public void onAnimationEnd(Animation animation)
	{
		if (mShakeResultInfo == null)
		{
			mIsEnable = true;
			return;
		}
		displayShakeResult();
	}

	@Override
	public void onAnimationRepeat(Animation animation)
	{
	}

	@Override
	public void onAnimationStart(Animation animation)
	{
		playSounds(1, 1);
		mShakeResultInfo = null;
	}

	@Override
	public void onShakeResultClose()
	{
		mIsEnable = true;
		updateUserCoin();
	}

	@Override
	public void clearRequestTask()
	{
		if (null != mShakeDetector)
		{
			mShakeDetector.stop();
		}
		if (null != mSoundPool)
		{
			mSoundPool.release();
		}
		resId.clear();
		EventBus.getDefault().unregister(this);
		ShakeManager.clearLuanchShakeRequestTask();
	}

	@Override
	public void IonShakeColse()
	{
		mIsEnable = true;
	}

	public void onEventMainThread(CoinChangeInfo changeInfo)
	{
		if (mLeftCoin != null)
		{
			mLeftCoin.setText(V2GogoApplication.getCurrentMatser().getCoin() + "");
		}
	}

	/**
	 * 更新用户金币
	 */
	private void updateUserCoin()
	{
		if (mShakeResultInfo != null && mShakeResultInfo.getType() == ShakeResultInfo.SHAKE_RESULT_GET_COIN)
		{
			if (V2GogoApplication.getMasterLoginState())
			{
				V2GogoApplication.getCurrentMatser().setCoin(V2GogoApplication.getCurrentMatser().getCoin() + mShakeResultInfo.getCoin());
				V2GogoApplication.getCurrentMatser().setAllcoin(V2GogoApplication.getCurrentMatser().getAllcoin() + mShakeResultInfo.getCoin());
				V2GogoApplication.getCurrentMatser().setWeekcoin(V2GogoApplication.getCurrentMatser().getWeekcoin() + mShakeResultInfo.getCoin());
				mLeftCoin.setText(V2GogoApplication.getCurrentMatser().getCoin() + "");
				V2GogoApplication.updateMatser();
			}
		}
	}

	/**
	 * 显示摇一摇结果
	 */
	private void displayShakeResult()
	{
		if (null != mShakeResultInfo)
		{
			if (mSoundPool != null)
			{
				mSoundPool.stop(resId.get(1));
			}
			if (mShakeResultInfo.getType() == ShakeResultInfo.SHAKE_RESULT_GET_COIN || mShakeResultInfo.getType() == ShakeResultInfo.SHAKE_RESULT_GET_PRIZE)
			{
				displayGetShakeResult();
			}
			else
			{
				displayNoGetShakeResult();
			}
		}
	}

	/**
	 * 显示未中奖结果
	 */
	private void displayNoGetShakeResult()
	{
		playSounds(resId.get(2), 1);
		// 没有广告
		if (TextUtils.isEmpty(mShakeResultInfo.getImg()))
		{
			if (null == mShakeResultDialog)
			{
				mShakeResultDialog = new ShakeResultDialog(this, R.style.style_action_sheet_dialog);
				mShakeResultDialog.setShakeResultCallback(this);
			}
			mShakeResultDialog.show();
			mShakeResultDialog.setShakeResult(mShakeResultInfo);
		}
		// 有广告
		else
		{
			if (null == mShakeAdDialog)
			{
				mShakeAdDialog = new ShakeAdDialog(this, R.style.style_action_sheet_dialog);
				mShakeAdDialog.setOnShakeAdResultCallback(this);
			}
			mShakeAdDialog.show();
			mShakeAdDialog.setAdResultDatas(mShakeResultInfo);
		}
	}

	/**
	 * 显示得奖结果
	 */
	private void displayGetShakeResult()
	{
		playSounds(resId.get(2), 1);
		if (null == mShakeResultDialog)
		{
			mShakeResultDialog = new ShakeResultDialog(this, R.style.style_action_sheet_dialog);
			mShakeResultDialog.setShakeResultCallback(this);
		}
		mShakeResultDialog.show();
		mShakeResultDialog.setShakeResult(mShakeResultInfo);
	}

	/**
	 * 播放音效
	 */
	private void playSounds(int sound, int number)
	{
		if (mSoundPool != null)
		{
			mSoundPool.play(resId.get(sound), (float) 0.8, (float) 0.8, 1, number, 1);
		}
	}

	/**
	 * 初始化音效
	 */
	@SuppressLint("UseSparseArrays")
	private void initSound()
	{
		mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
		resId = new HashMap<Integer, Integer>();
		resId.put(1, mSoundPool.load(this, R.raw.shake_sound_male, 1));
		resId.put(2, mSoundPool.load(this, R.raw.shake_match, 1));
	}

}
