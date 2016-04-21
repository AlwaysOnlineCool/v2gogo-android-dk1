package com.v2gogo.project.utils.common;

import android.os.CountDownTimer;

public class MyCountDownTime extends CountDownTimer
{
	private OnCountDownTimeListener listener;
	
	

	public void setCountDownTimeListener(OnCountDownTimeListener listener)
	{
		this.listener = listener;
	}

	public MyCountDownTime(long millisInFuture, long countDownInterval)
	{
		super(millisInFuture, countDownInterval);
	}

	@Override
	public void onTick(long millisUntilFinished)
	{
		if (listener != null)
		{
			listener.onTick(millisUntilFinished);
		}
	}

	@Override
	public void onFinish()
	{
		if (listener != null)
		{
			listener.onFinish();
		}
	}

	/**
	 * 功能：倒计时接口
	 * 
	 * @ahthor：黄荣星
	 * @date:2015-9-23
	 * @version::V1.0
	 */
	public interface OnCountDownTimeListener
	{
		void onTick(long millisTime);

		void onFinish();
	}

}
