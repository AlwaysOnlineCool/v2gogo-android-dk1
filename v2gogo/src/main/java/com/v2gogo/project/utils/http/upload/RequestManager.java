package com.v2gogo.project.utils.http.upload;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.v2gogo.project.main.V2GogoApplication;

public class RequestManager
{

	private static final String CHARSET_UTF_8 = "UTF-8";

	private static final int TIMEOUT_COUNT = 5 * 1000;

	private static final int RETRY_TIMES = 0;

	private volatile static RequestManager instance = null;

	private RequestQueue mRequestQueue = null;

	public interface RequestListener
	{
		void onRequest();

		void onSuccess(String response, String url, int actionId);

		void onError(String errorMsg, String url, int actionId);
	}

	private RequestManager()
	{
	}

	public static RequestManager getInstance()
	{
		if (null == instance)
		{
			synchronized (RequestManager.class)
			{
				if (null == instance)
				{
					instance = new RequestManager();
				}
			}
		}
		return instance;
	}

	public LoadControler get(String url, RequestListener requestListener, int actionId)
	{
		return this.get(url, requestListener, true, actionId);
	}

	public LoadControler get(String url, RequestListener requestListener, boolean shouldCache, int actionId)
	{
		return this.request(Method.GET, url, null, null, requestListener, shouldCache, TIMEOUT_COUNT, RETRY_TIMES, actionId);
	}

	public LoadControler post(final String url, Object data, final RequestListener requestListener, int actionId)
	{
		return this.post(url, data, requestListener, false, TIMEOUT_COUNT, RETRY_TIMES, actionId);
	}

	public LoadControler post(final String url, Object data, final RequestListener requestListener, boolean shouldCache, int timeoutCount, int retryTimes,
			int actionId)
	{
		return request(Method.POST, url, data, null, requestListener, shouldCache, timeoutCount, retryTimes, actionId);
	}

	public LoadControler request(int method, final String url, Object data, final Map<String, String> headers, final RequestListener requestListener,
			boolean shouldCache, int timeoutCount, int retryTimes, int actionId)
	{
		return this.sendRequest(method, url, data, headers, new LoadListener()
		{
			@Override
			public void onStart()
			{
				requestListener.onRequest();
			}

			@Override
			public void onSuccess(byte[] data, String url, int actionId)
			{
				String parsed = null;
				try
				{
					parsed = new String(data, CHARSET_UTF_8);
				}
				catch (UnsupportedEncodingException e)
				{
					e.printStackTrace();
				}
				requestListener.onSuccess(parsed, url, actionId);
			}

			@Override
			public void onError(String errorMsg, String url, int actionId)
			{
				requestListener.onError(errorMsg, url, actionId);
			}
		}, shouldCache, timeoutCount, retryTimes, actionId);
	}

	public LoadControler sendRequest(int method, final String url, Object data, final Map<String, String> headers, final LoadListener requestListener,
			boolean shouldCache, int timeoutCount, int retryTimes, int actionId)
	{
		if (requestListener == null)
			throw new NullPointerException();

		final ByteArrayLoadControler loadControler = new ByteArrayLoadControler(requestListener, actionId);

		Request<?> request = null;
		if (data != null && data instanceof RequestMap)
		{
			request = new ByteArrayRequest(Method.POST, url, data, loadControler, loadControler);
			request.setShouldCache(false);
		}
		else
		{
			request = new ByteArrayRequest(method, url, data, loadControler, loadControler);
			request.setShouldCache(shouldCache);
		}
		if (headers != null && !headers.isEmpty())
		{
			try
			{
				request.getHeaders().putAll(headers);
			}
			catch (AuthFailureError e)
			{
				e.printStackTrace();
			}
		}
		RetryPolicy retryPolicy = new DefaultRetryPolicy(timeoutCount, retryTimes, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		request.setRetryPolicy(retryPolicy);
		loadControler.bindRequest(request);
		if (this.mRequestQueue == null)
			mRequestQueue = V2GogoApplication.getRequestQueue();
		requestListener.onStart();
		this.mRequestQueue.add(request);
		return loadControler;
	}
}
