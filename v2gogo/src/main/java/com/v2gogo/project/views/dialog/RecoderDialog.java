package com.v2gogo.project.views.dialog;

import java.io.File;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.v2gogo.project.R;
import com.v2gogo.project.manager.upload.RecorderUploadManager;
import com.v2gogo.project.utils.common.LogUtil;
import com.v2gogo.project.utils.common.MyRecorderUtil;
import com.v2gogo.project.utils.common.NetUtil;
import com.v2gogo.project.utils.common.SDCardUtil;
import com.v2gogo.project.utils.common.ScreenUtil;
import com.v2gogo.project.utils.common.ToastUtil;

/**
 * 分享对话框
 * 
 * @author houjun
 */
public class RecoderDialog extends Dialog implements android.view.View.OnClickListener
{

	private View mContentView;
	private IonUploadRecorderClickCallback mRecoderClickCallback;
	private boolean mIsInitWidth;

	protected Button mRecordBtn;// 录音按钮
	private ImageView iv_record;
	private RelativeLayout mRecordLayout;
	private MyRecorderUtil mRecorder = null;
	private String mFileName;

	private static int RECORD_ING = 1; // 正在录音
	private static int RECODE_ED = 2; // 完成录音
	private static int RECODE_STATE = 0; // 录音的状态

	private String mTid;// 主题ID

	public RecoderDialog(Context context, boolean cancelable, OnCancelListener cancelListener)
	{
		super(context, cancelable, cancelListener);
		setDialogParams();
	}

	public RecoderDialog(Context context, int theme)
	{
		super(context, theme);
		setDialogParams();
	}

	public RecoderDialog(Context context)
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
			mContentView = getLayoutInflater().inflate(R.layout.recoder_layout, null);
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
		initVoiceView(mContentView);
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
			exception.printStackTrace();
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
		this.setCanceledOnTouchOutside(false);
		this.setCancelable(true);
	}

	public void setItemClickCallback(IonUploadRecorderClickCallback mItemClickCallback)
	{
		this.mRecoderClickCallback = mItemClickCallback;
	}

	private void registerListener()
	{
		mRecordBtn.setOnClickListener(this);
	}

	/**
	 * 分享数据回调
	 * 
	 * @author houjun
	 */
	public interface IonUploadRecorderClickCallback
	{
		public void onUploadRecoderClick();
	}

	@Override
	public void onClick(View view)
	{
		if (null != mRecoderClickCallback)
		{
			this.dismiss();
			mRecoderClickCallback.onUploadRecoderClick();
		}
	}

	private void updateDisplay(double signalEMA)
	{
		switch ((int) signalEMA)
		{
			case 0:
			case 1:
			case 2:
				iv_record.setImageResource(R.drawable.chat_icon_voice1);
				break;
			case 3:
			case 4:
			case 5:
				iv_record.setImageResource(R.drawable.chat_icon_voice2);
				break;
			case 6:
			case 7:
			case 8:
				iv_record.setImageResource(R.drawable.chat_icon_voice3);
				break;
			case 9:
			case 10:
			case 11:
				iv_record.setImageResource(R.drawable.chat_icon_voice4);
				break;
			case 12:
			case 13:
			case 14:
				iv_record.setImageResource(R.drawable.chat_icon_voice5);
				break;
			default:
				iv_record.setImageResource(R.drawable.chat_icon_voice6);
				break;
		}
	}

	Handler mHandler = new Handler();
	private Runnable mPollTask = new Runnable()
	{
		public void run()
		{
			if (mRecorder != null)
			{
				double amp = mRecorder.getAmplitude() / 2700.0;
				updateDisplay(amp);
				mHandler.postDelayed(mPollTask, 300);
			}
		}
	};

	float startY = 0;// 开始位置

	/**
	 * method desc：开始录音
	 */
	private void initVoiceView(View view)
	{
		mRecordBtn = (Button) view.findViewById(R.id.btn_speak);
		iv_record = (ImageView) view.findViewById(R.id.iv_record);
		mRecordLayout = (RelativeLayout) view.findViewById(R.id.layout_record);

		mRecordBtn.setOnTouchListener(new OnTouchListener()
		{
			long beforeTime;
			long afterTime;
			int timeDistance;

			@SuppressLint("NewApi")
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				switch (event.getAction())
				{
					case MotionEvent.ACTION_DOWN:
						if (!NetUtil.isConnected(getContext()))
						{

							ToastUtil.showAlertToast(getOwnerActivity(), getContext().getString(R.string.network_error_tip));
							break;
						}
						// 如果当前不是正在录音状态，开始录音
						if (RECODE_STATE != RECORD_ING)
						{
							startY = event.getY();// 记录其实Y轴坐标
							RECODE_STATE = RECORD_ING;
							beforeTime = System.currentTimeMillis();
							try
							{
								mRecordLayout.setVisibility(View.VISIBLE);
							}
							catch (Exception e)
							{
							}
							// 显示录音情况
							// 开始录音
							startVoice();
							mHandler.postDelayed(mPollTask, 300);
						}
						break;
					case MotionEvent.ACTION_UP:
						mRecordLayout.setVisibility(View.INVISIBLE);
						// mHandler.removeCallbacks(mSleepTask);
						mHandler.removeCallbacks(mPollTask);
						iv_record.setImageResource(R.drawable.chat_icon_voice1);
						// 如果是正在录音
						if (RECODE_STATE == RECORD_ING)
						{
							RECODE_STATE = RECODE_ED;

							afterTime = System.currentTimeMillis();
							System.out.println(timeDistance + "声音录制时间");

							// 停止录音
							stopVoice();

							if (!SDCardUtil.isExists(mFileName))
							{
								break;
							}
							if (((afterTime - beforeTime) / 1000) < 1)
							{
								Toast.makeText(getContext(), getContext().getString(R.string.voice_short), Toast.LENGTH_SHORT).show();
								try
								{
									File file = new File(mFileName + "");
									if (file.isFile())
									{
										file.delete();
									}
								}
								catch (Exception e)
								{
									e.printStackTrace();
								}
								break;
							}
							else
							{

								ToastUtil.showAlertToast(getOwnerActivity(), "这里上传录音文件");
								// 上传录音文件
//								RecorderUploadManager.uploadRecorderVoice(mTid, resourceURL, voiceLength, photo);
							}

						}
						break;
					case MotionEvent.ACTION_MOVE:
						LogUtil.e("getY():" + event.getY() + " recordBtn.getY():" + mRecordBtn.getY() + " is:" + (event.getY() < mRecordBtn.getY()));
						LogUtil.e("滑动距离：" + Math.abs(startY - event.getY()));
						if (Math.abs(startY - event.getY()) > 200)
						{
							ToastUtil.showAlertToast(getOwnerActivity(), "取消语言上传");
							mRecordLayout.setVisibility(View.INVISIBLE);
							// mHandler.removeCallbacks(mSleepTask);
							mHandler.removeCallbacks(mPollTask);
							iv_record.setImageResource(R.drawable.chat_icon_voice1);
							// 如果是正在录音
							if (RECODE_STATE == RECORD_ING)
							{
								RECODE_STATE = RECODE_ED;
								afterTime = System.currentTimeMillis();
								System.out.println(timeDistance + "声音录制时间");
								// 停止录音
								stopVoice();
							}
						}
					default:
						break;
				}
				return false;
			}
		});
		// initVoiceAnimRes();
	}

	/** 开始录音 */
	private void startVoice()
	{
		// 设置录音保存路径
		mFileName = SDCardUtil.getSDCardPath() + UUID.randomUUID().toString() + ".amr";
		mRecorder = new MyRecorderUtil(mFileName);
		mRecorder.start();
	}

	/** 停止录音 */
	private void stopVoice()
	{
		if (mRecorder != null)
		{
			mRecorder.stop();
		}
	}
}
