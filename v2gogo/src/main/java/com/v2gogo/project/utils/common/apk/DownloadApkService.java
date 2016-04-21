package com.v2gogo.project.utils.common.apk;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

/**
 * 下载apk的service
 * 
 * @author houjun
 */
public class DownloadApkService extends IntentService
{
	public static final String APK_URL = "apk_url";
	public static final String PROGRESS = "progress";
	public static final String PROGRESS_INTENT = "progress_intent";

	private final int BUFFER_SIZE = 10 * 1024;
	private final String TAG = "houjun";
	private Intent mProgressIntent;

	public DownloadApkService()
	{
		super("DownloadService");
	}

	@Override
	protected void onHandleIntent(Intent intent)
	{
		String urlStr = intent.getStringExtra(APK_URL);
		if (!TextUtils.isEmpty(urlStr))
		{
			InputStream in = null;
			FileOutputStream out = null;
			try
			{
				URL url = new URL(urlStr);
				HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

				urlConnection.setRequestMethod("GET");
				urlConnection.setDoOutput(false);
				urlConnection.setConnectTimeout(10 * 1000);
				urlConnection.setReadTimeout(10 * 1000);
				urlConnection.setRequestProperty("Connection", "Keep-Alive");
				urlConnection.setRequestProperty("Charset", "UTF-8");
				urlConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");

				urlConnection.connect();
				long bytetotal = urlConnection.getContentLength();
				long bytesum = 0;
				int byteread = 0;
				in = urlConnection.getInputStream();
				File dir = StorageUtils.getCacheDirectory(this);
				File apkFile = new File(dir, "v2gogo.apk");
				if (apkFile.isFile() && apkFile.exists() && apkFile.length() > 0)
				{
					apkFile.delete();
				}
				out = new FileOutputStream(apkFile);
				byte[] buffer = new byte[BUFFER_SIZE];
				int oldProgress = 0;
				while ((byteread = in.read(buffer)) != -1)
				{
					bytesum += byteread;
					out.write(buffer, 0, byteread);
					int progress = (int) (bytesum * 100L / bytetotal);
					if (progress != oldProgress)
					{
						updateProgress(progress);
					}
					oldProgress = progress;
				}
				if (bytesum == bytetotal)
				{
					updateProgress(100);
					stopSelf();
				}
			}
			catch (Exception e)
			{
				Log.e(TAG, "download apk file error", e);
			}
			finally
			{
				if (out != null)
				{
					try
					{
						out.close();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
				if (in != null)
				{
					try
					{
						in.close();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 进度更新
	 * 
	 * @param progress
	 */
	private void updateProgress(int progress)
	{
		if (null == mProgressIntent)
		{
			mProgressIntent = new Intent(PROGRESS_INTENT);
		}
		mProgressIntent.putExtra(PROGRESS, progress);
		sendBroadcast(mProgressIntent);
	}
}
