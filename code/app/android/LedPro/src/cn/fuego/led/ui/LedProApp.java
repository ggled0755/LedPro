package cn.fuego.led.ui;

import java.io.File;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import cn.fuego.common.log.FuegoLog;
import cn.fuego.led.R;
import cn.fuego.led.cache.AppCache;
import cn.fuego.misp.dao.SharedPreUtil;
import cn.fuego.misp.service.MemoryCache;
import cn.smssdk.SMSSDK;

import com.lidroid.xutils.DbUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class LedProApp extends Application
{
	private FuegoLog log = FuegoLog.getLog(getClass());
	
	private  DbUtils db;
	@Override
	public void onCreate()
	{
		// TODO Auto-generated method stub
		super.onCreate();
		
		//初始化数据库
		db = DbUtils.create(this);
		SharedPreUtil.initSharedPreference(getApplicationContext());
		AppCache.getInstance().load();
		loadDB();
		initMemoryCache();
		//initial image cache
		initImageCache();
		
		//init Mob SMS
		SMSSDK.initSDK(this, "8faa74f2ef01", "2c929861d197b945a13fd2d8e9a9e660");

	}
	//初始化MISP-memorycache
	private void initMemoryCache()
	{
		PackageManager packageManager = getPackageManager();
		PackageInfo packInfo;
		try
		{
			packInfo = packageManager.getPackageInfo(getPackageName(),0);
			MemoryCache.setVersionCode(packInfo.versionCode);
			MemoryCache.setVersionName(packInfo.versionName);
			MemoryCache.setDensity(getResources().getDisplayMetrics().density);
		} 
		catch (NameNotFoundException e)
		{
			log.info("can not get the package information");
			e.printStackTrace();
		}

		MemoryCache.setProgramName("LedPro");
		//MemoryCache.setServerIp("192.168.1.105");
		//MemoryCache.setServerPort("7000");	
		MemoryCache.setServerIp("120.25.216.218");
		MemoryCache.setServerPort("88");
	}
	//加载本地数据库
	private void loadDB()
	{
		
	}

	private void initImageCache()
	{
		String appName = getResources().getString(R.string.app_name);
		File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), appName+"/imageloader/Cache");  
		ImageLoaderConfiguration config = new ImageLoaderConfiguration  
			    .Builder(this)  
			    .memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽  
			   // .discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75, null) // Can slow ImageLoader, use it carefully (Better don't use it)/设置缓存的详细信息，最好不要设置这个  
			    .threadPoolSize(30)//线程池内加载的数量  
			    .threadPriority(Thread.NORM_PRIORITY - 2)  
			    .denyCacheImageMultipleSizesInMemory()  
			    .memoryCache(new UsingFreqLimitedMemoryCache(5 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现  
			    .memoryCacheSize(5 * 1024 * 1024)    
			    .discCacheSize(50 * 1024 * 1024)    
			    .discCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密  
			    .tasksProcessingOrder(QueueProcessingType.LIFO)  
			    .discCacheFileCount(500) //缓存的文件数量  
			    .discCache(new UnlimitedDiscCache(cacheDir))//自定义缓存路径  
			    .defaultDisplayImageOptions(DisplayImageOptions.createSimple())  
			    .imageDownloader(new BaseImageDownloader(this, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间  
			    .writeDebugLogs() // Remove for release app  
			    .build();//开始构建  
		ImageLoader.getInstance().init(config);
	}


}
