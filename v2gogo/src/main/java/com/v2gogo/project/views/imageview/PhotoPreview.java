package com.v2gogo.project.views.imageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.v2gogo.project.R;
import com.v2gogo.project.main.image.DisplayImageOptionsFactory;

/**
 * 图片预览
 * 
 * @author houjun
 */
public class PhotoPreview extends LinearLayout implements OnClickListener
{
	private ProgressBar mProgressBar;
	private GestureImageView ivContent;
	private OnClickListener l;
	

	public PhotoPreview(Context context)
	{
		super(context);
		LayoutInflater.from(context).inflate(R.layout.view_photopreview, this, true);
		mProgressBar = (ProgressBar) findViewById(R.id.iv_content_progressbar);
		ivContent = (GestureImageView) findViewById(R.id.iv_content_vpp);
		ivContent.setOnClickListener(this);
	}

	public PhotoPreview(Context context, AttributeSet attrs)
	{
		this(context);
		LayoutInflater.from(context).inflate(R.layout.view_photopreview, this, true);
		mProgressBar = (ProgressBar) findViewById(R.id.iv_content_progressbar);
		ivContent = (GestureImageView) findViewById(R.id.iv_content_vpp);
		ivContent.setOnClickListener(this);
	}
	

	public GestureImageView getIvContent()
	{
		return ivContent;
	}

	public void loadImage(String path)
	{
		mProgressBar.setVisibility(View.VISIBLE);
		ImageLoader.getInstance().loadImage(path, DisplayImageOptionsFactory.getDefaultDisplayImageOptions(), new SimpleImageLoadingListener()
		{
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage)
			{
				mProgressBar.clearAnimation();
				mProgressBar.setVisibility(View.GONE);
				ivContent.setImageBitmap(loadedImage);
			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason)
			{
				mProgressBar.clearAnimation();
				mProgressBar.setVisibility(View.GONE);
				ivContent.setImageResource(R.drawable.loading_image_default_fe);
			}
		});
	}

	@Override
	public void setOnClickListener(OnClickListener l)
	{
		this.l = l;
	}

	@Override
	public void onClick(View v)
	{
		if (v.getId() == R.id.iv_content_vpp && l != null)
			l.onClick(ivContent);
	};

}
