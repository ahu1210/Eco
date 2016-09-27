package com.fare.eco.ui;

import java.io.File;

import android.app.Application;

import com.fare.eco.config.Constants;
import com.fare.eco.externalLibrary.imageload.cache.disc.impl.UnlimitedDiskCache;
import com.fare.eco.externalLibrary.imageload.cache.disc.naming.Md5FileNameGenerator;
import com.fare.eco.externalLibrary.imageload.cache.memory.impl.LruMemoryCache;
import com.fare.eco.externalLibrary.imageload.core.ImageLoader;
import com.fare.eco.externalLibrary.imageload.core.ImageLoaderConfiguration;
import com.fare.eco.externalLibrary.imageload.core.assist.QueueProcessingType;
import com.fare.eco.manager.db.DBManager;
import com.fare.eco.manager.http.RequestManager;

public class MyApplication extends Application {

	private DBManager dbManager;

	@Override
	public void onCreate() {
		super.onCreate();
		dbManager=new DBManager(getApplicationContext());
		dbManager.openDatabase();
		init();
	}

	private void init() {
		RequestManager.init(this);
		initImageLoader();
//		CrashHandler catchHandler = CrashHandler.getInstance();
//      catchHandler.init(getApplicationContext());
	}

	private void initImageLoader() {
		int cacheSize = 2 * 1024 * 1024;
		if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			cacheSize = (int) (Runtime.getRuntime().maxMemory()) / 8;
		}
		File cacheDir = new File(Constants.IMAGE_CACHE_DIR);

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
				.threadPriority(Thread.NORM_PRIORITY - 2) // 线程池数量 3
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new LruMemoryCache(cacheSize)) // LRULimitedMemoryCache
				.diskCacheFileNameGenerator(new Md5FileNameGenerator()) // 缓存到磁盘的文件用md5加密
				.diskCache(new UnlimitedDiskCache(cacheDir))
				.diskCacheSize(50 * 1024 * 1024) // 50M
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs().build();

		ImageLoader.getInstance().init(config);
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		dbManager.closeDatabase();
	}
}