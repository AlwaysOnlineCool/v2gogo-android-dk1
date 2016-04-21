package com.v2gogo.project.utils.http;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.v2gogo.project.R;
import com.v2gogo.project.main.V2GogoApplication;

/**
 * 根据不同错误，显示不同友好的文字
 * 
 * @author houjun
 */
public class VolleyErrorStringBuilder
{
	public static String createTipStringByError(VolleyError error)
	{
		if (null != error)
		{
			if (error instanceof TimeoutError)
			{
				return V2GogoApplication.sIntance.getResources().getString(R.string.timeout_error_tip);
			}
			else if (error instanceof ServerError)
			{
				return V2GogoApplication.sIntance.getResources().getString(R.string.server_error_tip);

			}
			else if (error instanceof NetworkError)
			{
				return V2GogoApplication.sIntance.getResources().getString(R.string.network_error_tip);
			}
			else if (error instanceof ParseError)
			{
				return V2GogoApplication.sIntance.getResources().getString(R.string.server_error_tip);
			}
			else if (error instanceof AuthFailureError)
			{
				return V2GogoApplication.sIntance.getResources().getString(R.string.auth_error_tip);
			}
		}
		else
		{
			return V2GogoApplication.sIntance.getResources().getString(R.string.network_error_tip);
		}
		return "";
	}
}
