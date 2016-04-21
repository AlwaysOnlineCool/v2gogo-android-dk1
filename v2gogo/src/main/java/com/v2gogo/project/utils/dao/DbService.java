package com.v2gogo.project.utils.dao;

import java.util.List;

import android.database.sqlite.SQLiteDatabase;

import com.v2gogo.project.db.CacheInfo;
import com.v2gogo.project.db.dao.CacheInfoDao;
import com.v2gogo.project.db.dao.CacheInfoDao.Properties;
import com.v2gogo.project.db.dao.DaoSession;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.utils.common.LogUtil;

import de.greenrobot.dao.query.DeleteQuery;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * 数据库操作工具类
 * 
 * @author houjun
 */
public class DbService
{

	private static DbService instance;
	private CacheInfoDao mCacheInfoDao;

	private DbService()
	{

	}

	public static DbService getInstance()
	{
		if (instance == null)
		{
			instance = new DbService();
			DaoSession daoSession = V2GogoApplication.getDaoSession();
			instance.mCacheInfoDao = daoSession.getCacheInfoDao();
		}
		return instance;
	}

	/**
	 * 插入缓存数据
	 * 
	 * @param cacheInfo
	 *            缓存数据
	 */
	public void insertCacheData(CacheInfo cacheInfo)
	{
		if (null == cacheInfo)
		{
			return;
		}
		if (LogUtil.isDebug)
		{
			LogUtil.d("insertCacheData->" + cacheInfo.getResponse());
		}
		SQLiteDatabase database = mCacheInfoDao.getDatabase();
		if (database != null)
		{
			database.beginTransaction();
			try
			{
				deleteCacheData(cacheInfo.getType());
				mCacheInfoDao.insert(cacheInfo);
				database.setTransactionSuccessful();
			}
			finally
			{
				database.endTransaction();
			}
		}
	}

	/**
	 * 取出缓存数据
	 * 
	 * @param type
	 *            相应数据对应的类型
	 * @return
	 */
	public CacheInfo getCacheData(int type)
	{
		QueryBuilder<CacheInfo> queryBuilder = mCacheInfoDao.queryBuilder();
		queryBuilder.where(Properties.Type.eq(type));
		List<CacheInfo> list = queryBuilder.list();
		if (LogUtil.isDebug)
		{
			LogUtil.d("getCacheData->" + list);
		}
		if (null != list && list.size() > 0)
		{
			return list.get(0);
		}
		return null;
	}

	/**
	 * 删除缓存数据
	 * 
	 * @param type
	 *            相应数据对应的类型
	 */
	private void deleteCacheData(int type)
	{
		QueryBuilder<CacheInfo> queryBuilder = mCacheInfoDao.queryBuilder();
		DeleteQuery<CacheInfo> deleteQueryBuilder = queryBuilder.where(Properties.Type.eq(type)).buildDelete();
		deleteQueryBuilder.executeDeleteWithoutDetachingEntities();
	}

}
