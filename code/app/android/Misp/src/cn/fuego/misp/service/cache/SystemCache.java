/**   
 * @Title: SystemMenuCache.java 
 * @Package cn.fuego.misp.service.cache 
 * @Description: TODO
 * @author Tang Jun   
 * @date 2014-3-25 下午11:13:07 
 * @version V1.0   
 */
package cn.fuego.misp.service.cache;

import cn.fuego.common.log.FuegoLog;

/**
 * @ClassName: SystemMenuCache
 * @Description: TODO
 * @author Tang Jun
 * @date 2014-3-25 下午11:13:07
 * 
 */

public class SystemCache
{
 
	private String app_id;
	private String token;
	private String client_type;
	private String client_version;
	
	private FuegoLog log = FuegoLog.getLog(SystemCache.class);

	public static SystemCache instance = new SystemCache();
 
	private SystemCache()
	{
		this.reload();

	}

	public static synchronized SystemCache getInstance()
	{
		if (null == instance)
		{
			instance = new SystemCache();
		}
		return instance;
	}
	
	public void initToken(String token)
	{
		this.token = token;
	}
	public void initClientTypen(String client_type)
	{
		this.client_type = client_type;
	}
	public void initClientVersion(String client_version)
	{
		this.client_version = client_version;
	}
	public void initAppID(String app_id)
	{
		this.app_id = app_id;
	}
	

	public void reload()
	{
 		 
		 

	}
	

	public String getApp_id()
	{
		return app_id;
	}

	public String getClient_type()
	{
		return client_type;
	}

	public String getClient_version()
	{
		return client_version;
	}

	public String getToken()
	{
		return token;
	}

 


 
 
}
