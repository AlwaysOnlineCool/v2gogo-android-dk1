package com.v2gogo.project.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.tandong.swichlayout.BaseAnimViewS;
import com.tandong.swichlayout.BaseEffects;
import com.tandong.swichlayout.SwitchLayout;
import com.v2gogo.project.R;
import com.v2gogo.project.utils.common.MD5Util;
import com.v2gogo.project.utils.common.ToastUtil;
import com.v2gogo.project.views.DotViews;
import com.v2gogo.project.views.NoFageColorViewPager;
import com.v2gogo.project.views.imageview.PhotoPreview;

/**
 * 图片预览
 * 
 * @author houjun
 */
public class PhotoPreviewActivity extends BaseActivity implements OnPageChangeListener, OnClickListener
{

	public static final String INDEX = "index";
	public static final String PATHS = "paths";

	private List<String> mPaths;

	private DotViews mDotViews;
	private NoFageColorViewPager mFageColorViewPager;
	private ImageView mBackImageView;
	private ImageView mDownloadPicImgeView;

	private PhotoPreview mCurrentImageView;
	private String mCurrentImagePath;
	private TextView mCurrentTv;

	private PagerAdapter mPagerAdapter = new PagerAdapter()
	{
		@Override
		public int getCount()
		{
			if (mPaths == null)
			{
				return 0;
			}
			else
			{
				return mPaths.size();
			}
		}

		@Override
		public View instantiateItem(final ViewGroup container, final int position)
		{
			PhotoPreview photoPreview = new PhotoPreview(getApplicationContext());
			((ViewPager) container).addView(photoPreview);
			photoPreview.loadImage(mPaths.get(position));
			photoPreview.setOnClickListener(photoItemClickListener);
			return photoPreview;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object)
		{
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object)
		{
			return view == object;
		}

		public void setPrimaryItem(ViewGroup container, int position, Object object)
		{
			mCurrentImageView = (PhotoPreview) object;
		};

	};

	private OnClickListener photoItemClickListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			outPhotoPrivew();
		}
	};

	@Override
	public void onPageScrollStateChanged(int arg0)
	{

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2)
	{

	}

	@Override
	public void onPageSelected(int position)
	{
		mDotViews.select(position);
		setCurrentPositonTv(position);
	}

	@Override
	public void clearRequestTask()
	{
	}

	@Override
	public void onInitViews()
	{
		mBackImageView = (ImageView) findViewById(R.id.article_back_img);
		mDownloadPicImgeView = (ImageView) findViewById(R.id.article_download_pic_iv);
		mCurrentTv = (TextView) findViewById(R.id.article_photopic_tv);
		mBackImageView.setOnClickListener(this);
		mDownloadPicImgeView.setOnClickListener(this);

		mPaths = getIntent().getStringArrayListExtra(PATHS);
		mDotViews = (DotViews) findViewById(R.id.activity_photo_preview_dots);
		mFageColorViewPager = (NoFageColorViewPager) findViewById(R.id.activity_photo_preview_viewpager);
		mFageColorViewPager.setAdapter(mPagerAdapter);
		mFageColorViewPager.setOnPageChangeListener(this);
		int index = getIntent().getIntExtra(INDEX, 0);
		mFageColorViewPager.setCurrentItem(index);

		setCurrentPositonTv(index);

		mDotViews.setSize(mPaths.size());
		mDotViews.select(index);
		mDotViews.setVisibility(View.GONE);
		
		setEnterAnim();
	}

	private void setCurrentPositonTv(int index)
	{
		mCurrentImagePath = mPaths.get(index);
		mCurrentTv.setText((index + 1) + "/" + mPaths.size());
	}

	/**
	 * 设置进入动画
	 */
	private void setEnterAnim()
	{
		try
		{
			BaseAnimViewS.animDuration = 400;
			SwitchLayout.animDuration = 400;
			SwitchLayout.longAnimDuration = 800;
			BaseAnimViewS.longAnimDuration = 800;
			SwitchLayout.ScaleBig(this, false, null);
		}
		catch (Exception e)
		{
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
		{
			outPhotoPrivew();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.activity_photopreview_layout;
	}

	@Override
	public void onClick(View arg0)
	{
		switch (arg0.getId())
		{
			case R.id.article_back_img:
				outPhotoPrivew();
				break;
			case R.id.article_download_pic_iv:
				saveChoicePic(mCurrentImagePath);
				break;

			default:
				break;
		}
	}

	/**
	 * 退出图片浏览
	 */
	private void outPhotoPrivew()
	{
		try
		{
			SwitchLayout.ScaleSmall(this, true, BaseEffects.acceInter);
		}
		catch (Exception exception)
		{
		}
	}

	/**
	 * @param path
	 *            图片的网络地址
	 */
	private void saveChoicePic(String path)
	{
		Bitmap bitmap = ((BitmapDrawable) mCurrentImageView.getIvContent().getDrawable()).getBitmap();
		saveMyBitmap(path, bitmap);
	}

	private void saveMyBitmap(String imgPath, Bitmap mBitmap)
	{
		// File f = new File("/sdcard/" + bitName + ".png");
		String fileName = MD5Util.getMD5String(imgPath);
		File f = StorageUtils.getOwnCacheDirectory(this, "v2gogo/" + fileName + ".jpg");
		if (f.exists())
		{
			f.delete();
		}
		FileOutputStream fOut = null;
		try
		{
			fOut = new FileOutputStream(f);
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
			fOut.flush();
			fOut.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		ToastUtil.showConfirmToast(this, "已保存到SD卡v2gogo文件夹下");
	}
}
