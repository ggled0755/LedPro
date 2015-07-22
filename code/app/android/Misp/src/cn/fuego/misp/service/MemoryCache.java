package cn.fuego.misp.service;

import cn.fuego.common.util.validate.ValidatorUtil;



public class MemoryCache
{
	private static String token;
	
	private static String version = "0.1";

	private static String serverIp = "192.168.0.105"; //本地2
	//private static String serverIp = "101.200.232.17"; //服务器
	private static String programName="";
	private static String serverPort= "7000";
	
	private static float density = 1;
	 
 	private static String versionNname;
	private static int versionCode;

	public static String getVersionNname()
	{
		return versionNname;
	}

	public static void setVersionNname(String versionNname)
	{
		MemoryCache.versionNname = versionNname;
	}

	public static int getVersionCode()
	{
		return versionCode;
	}

	public static void  setVersionCode(int versionCode)
	{
		MemoryCache.versionCode = versionCode;
	}
	public static float getDensity()
	{
		return density;
	}

	public static void setDensity(float density)
	{
		MemoryCache.density = density;
	}
 

	public static String getToken()
	{
		return token;
	}
	
	public static boolean isLogined()
	{
		if(ValidatorUtil.isEmpty(token))
		{
			return false;
		}
		return true;
	}

	public static void setToken(String token)
	{
		MemoryCache.token = token;
	}

	public static String getVersion()
	{
		return version;
	}

 

	public static String getServerIp()
	{
		return serverIp;
	}

 

	public static String getServerPort()
	{
		return serverPort;
	}
	
	public static String getHostUrl()
	{
		return "http://"+MemoryCache.getServerIp()+":"+MemoryCache.getServerPort();
	}
	public static String getWebContextUrl()
	{
		return getHostUrl()+"/"+programName;
	}
	public static String getImageUrl()
	{
		return MemoryCache.getWebContextUrl() + "/Client/Public/Fuego/Uploads/";
	}
	public static String getUploadUrl()
	{
		return MemoryCache.getWebContextUrl() + "/index.php/Customer/ImgUpload";
	}
	public static String getRestUrl()
	{
		return getWebContextUrl()+"/rest";
		
	}
 

}
