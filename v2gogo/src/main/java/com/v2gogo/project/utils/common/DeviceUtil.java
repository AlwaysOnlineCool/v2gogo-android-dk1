package com.v2gogo.project.utils.common;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * 获取设备的唯一标示
 * 
 * @author houjun
 */
public class DeviceUtil
{

	/**
	 * 获取设备的唯一标示
	 * 
	 * @param context
	 * @return
	 */
	public static String getImei(final Context context)
	{
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();
	}

}
