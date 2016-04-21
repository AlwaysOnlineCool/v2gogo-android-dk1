package com.v2gogo.project.main;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.v2gogo.project.db.MatserInfo;
import com.v2gogo.project.db.dao.DaoMaster;
import com.v2gogo.project.db.dao.DaoMaster.OpenHelper;
import com.v2gogo.project.db.dao.DaoSession;
import com.v2gogo.project.main.image.GlideImageLoader;
import com.v2gogo.project.utils.common.SPUtil;
import com.v2gogo.project.utils.http.Constants;
import com.v2gogo.project.utils.parse.JsonParser;

/**
 * 整个应用程序
 * 
 * @author houjun
 */
public class V2GogoApplication extends Application
{
	private static DaoMaster sDaoMaster;
	private static DaoSession sDaoSession;
	public static V2GogoApplication sIntance;
	private static RequestQueue sRequestQueue;
	private static MatserInfo sMasterInfo;
	public String mNewApkLoadUrl;

	/**
	 * 应用程序启动
	 */
	@Override
	public void onCreate()
	{
		super.onCreate();
		sIntance = this;
		init(sIntance);
	}
	
	public static V2GogoApplication getsIntance()
	{
		return sIntance;
	}

	@Override
	public void onLowMemory()
	{
		super.onLowMemory();
		if (getPackageName().equals(getMainProcessName(sIntance)))
		{
			GlideImageLoader.onLowMemory(sIntance);
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void onTrimMemory(int level)
	{
		super.onTrimMemory(level);
		if (getPackageName().equals(getMainProcessName(sIntance)))
		{
			GlideImageLoader.onTrimMemory(sIntance, level);
		}
	}

	@Override
	public void onTerminate()
	{
		super.onTerminate();
		if (getPackageName().equals(getMainProcessName(sIntance)))
		{
			GlideImageLoader.onDestory(sIntance);
		}
	}

	/**
	 * 初始化相关数据
	 */
	private void init(Context context)
	{
		if (getPackageName().equals(getMainProcessName(context)))
		{
			if (!ImageLoader.getInstance().isInited())
			{
				initImageLoader(context);
			}
			GlideImageLoader.initGlideImageLoader(context);
//			LogUtil.isDebug = false;
//			VolleyLog.DEBUG = false;
//			L.writeDebugLogs(false);
//			L.writeLogs(false);
		}
	}

	/**
	 * 得到主进程的名称
	 * 
	 * @return
	 */
	private String getMainProcessName(Context context)
	{
		String processName = null;
		int pid = android.os.Process.myPid();
		ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses())
		{
			if (appProcess.pid == pid)
			{
				processName = appProcess.processName;
				break;
			}
		}
		return processName;
	}

	/**
	 * 初始化图片加载器
	 * 
	 * @param context
	 *            上下文
	 */
	@SuppressWarnings("deprecation")
	private void initImageLoader(Context context)
	{
		int maxSize = (int) ((int) Runtime.getRuntime().maxMemory() / 6f);
		ImageLoaderConfiguration imageLoaderConfiguration = new ImageLoaderConfiguration.Builder(context).threadPoolSize(3)
				.threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory().memoryCache(new WeakMemoryCache())
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple()).tasksProcessingOrder(QueueProcessingType.LIFO)
				.discCache(new UnlimitedDiskCache(getDiskCacheFile(context))).discCacheFileNameGenerator(new HashCodeFileNameGenerator())
				.memoryCacheSize(maxSize).imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)).writeDebugLogs().build();
		ImageLoader.getInstance().init(imageLoaderConfiguration);
	}

	public static DaoMaster getDaoMaster(Context context)
	{
		if (sDaoMaster == null)
		{
			OpenHelper helper = new DaoMaster.DevOpenHelper(context, "v2gogo", null);
			sDaoMaster = new DaoMaster(helper.getWritableDatabase());
		}
		return sDaoMaster;
	}

	/**
	 * 取得DaoSession
	 * 
	 * @param context
	 * @return
	 */
	public static DaoSession getDaoSession()
	{
		if (sDaoSession == null)
		{
			if (sDaoMaster == null)
			{
				sDaoMaster = getDaoMaster(sIntance);
			}
			sDaoSession = sDaoMaster.newSession();
		}
		return sDaoSession;
	}

	/**
	 * 得到请求队列
	 */
	public static RequestQueue getRequestQueue()
	{
		if (null == sRequestQueue)
		{
			synchronized (V2GogoApplication.class)
			{
				if (null == sRequestQueue)
				{
					sRequestQueue = Volley.newRequestQueue(sIntance);
				}
			}
		}
		return sRequestQueue;
	}

	/**
	 * 初始化用户信息
	 */
	private static void initMasterInfo()
	{
		String user = (String) SPUtil.get(sIntance, Constants.USER, "");
		if (!TextUtils.isEmpty(user))
		{
			sMasterInfo = (MatserInfo) JsonParser.parseObject(user, MatserInfo.class);
		}
	}

	public static void setCurrentMaster(MatserInfo matserInfo)
	{
		sMasterInfo = matserInfo;
	}

	/**
	 * 得到当前用户
	 * 
	 * @return
	 */
	public static MatserInfo getCurrentMatser()
	{
		if (sMasterInfo != null)
		{
			return sMasterInfo;
		}
		else
		{
			initMasterInfo();
			return sMasterInfo;
		}
	}

	/**
	 * 判断用户已登录
	 * 
	 * @return
	 */
	public static boolean getMasterLoginState()
	{
		return SPUtil.contains(sIntance, Constants.USER) && !TextUtils.isEmpty((String) (SPUtil.get(sIntance, Constants.USER, "")));
	}

	/**
	 * 清除用户消息
	 * 
	 * @param isRetian
	 */
	public static void clearMatserInfo(boolean isRetian)
	{
		if (!isRetian)
		{
			SPUtil.remove(sIntance, Constants.USER_NAME);
		}
		sMasterInfo = null;
		SPUtil.remove(sIntance, Constants.USER);
		SPUtil.remove(sIntance, Constants.USER_PASS);
	}

	/**
	 * 更新用户信息
	 */
	public static void updateMatser()
	{
		if (sMasterInfo != null)
		{
			String user = new Gson().toJson(sMasterInfo);
			SPUtil.put(sIntance, Constants.USER, user);
		}
	}

	/**
	 * 得到缓存文件
	 */
	public static File getDiskCacheFile(Context context)
	{
		return StorageUtils.getOwnCacheDirectory(context, "v2gogo/Cache");
	}
}
