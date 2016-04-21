package io.vov.vitamio.activity;

import io.vov.vitamio.R;
import io.vov.vitamio.Vitamio;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

public class InitActivity extends Activity
{
	private UIHandler uiHandler;
	private ProgressBar mProgressBar;
	public static final String FROM_ME = "fromVitamioInitActivity";

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.video_init_layout);
		mProgressBar = (ProgressBar) findViewById(R.id.video_init_params_progressbar);
		uiHandler = new UIHandler(this);
		initVideoParams();
	}


	private void initVideoParams()
	{
		new AsyncTask<Object, Object, Boolean>()
		{
			@Override
			protected Boolean doInBackground(Object... params)
			{
				return Vitamio.initialize(InitActivity.this, getResources().getIdentifier("libarm", "raw", getPackageName()));
			}

			@Override
			protected void onPostExecute(Boolean inited)
			{
				if (inited)
				{
					uiHandler.sendEmptyMessage(0);
				}
			}

		}.execute();
	}

	private static class UIHandler extends Handler
	{
		private WeakReference<Context> mContext;

		public UIHandler(Context c)
		{
			mContext = new WeakReference<Context>(c);
		}

		public void handleMessage(Message msg)
		{
			InitActivity ctx = (InitActivity) mContext.get();
			switch (msg.what)
			{
				case 0:
					ctx.mProgressBar.clearAnimation();
					ctx.mProgressBar.setVisibility(View.GONE);
					Intent src = ctx.getIntent();
					Intent i = new Intent();
					i.setClassName(src.getStringExtra("package"), src.getStringExtra("className"));
					i.setData(src.getData());
					i.putExtras(src);
					i.putExtra(FROM_ME, true);
					ctx.startActivity(i);
					ctx.finish();
					break;
			}
		}
	}
}
