package cn.fuego.common.util;

import java.io.UnsupportedEncodingException;

import cn.fuego.common.log.FuegoLog;
import cn.fuego.common.util.file.PropertyReader;

public class SystemConfigInfo
{
    private static final FuegoLog log = FuegoLog.getLog(SystemConfigInfo.class);

	public static String getSystemRootPath()
	{
		String path = SystemConfigInfo.class.getResource("/").getPath();
		try
		{
			path = java.net.URLDecoder.decode(path,"utf-8");
		}
		catch (UnsupportedEncodingException e)
		{
			log.error("decode failed",e);
		}  
		return path;
	}
	
	public static String getConfigItem(String itemName)
	{
		return  PropertyReader.getInstance().getPropertyByName(itemName);
	}

	public static String getProductName()
	{
		return PropertyReader.getInstance().getPropertyByName(SystemConfigNameConst.PRODUCT_NAME);
	}
	
	public static String getDefaultPassword()
	{
		return PropertyReader.getInstance().getPropertyByName(SystemConfigNameConst.USER_DEFAULT_PASSWOARD);
	}
	public static String getTemplatePath()
	{
		return  getSystemRootPath() + PropertyReader.getInstance().getPropertyByName(SystemConfigNameConst.TEMPLATE_PATH);
		
	}
	public static String getMessageUserID()
	{
		return PropertyReader.getInstance().getPropertyByName(SystemConfigNameConst.MESSAGE_USER_ID);

	}

	public static String getMessageAccount()
	{
		return PropertyReader.getInstance().getPropertyByName(SystemConfigNameConst.MESSAGE_ACCOUNT);

	}
	
	public static String getMessagePassword()
	{
		return PropertyReader.getInstance().getPropertyByName(SystemConfigNameConst.MESSAGE_PASSWORD);

	}
	public static String getDevicePort()
	{
		return PropertyReader.getInstance().getPropertyByName(SystemConfigNameConst.DEVICE_PORT);
	}
	
	
	public static int getHeartBeatTime()
	{
		String timeOut = PropertyReader.getInstance().getPropertyByName("HEART_BEAT_TIME");
		int timeSecond = Integer.valueOf(timeOut);
		return timeSecond;

	}
	public static int getDeviceTimeOut()
	{
		String timeOut = PropertyReader.getInstance().getPropertyByName("DEVICE_TIME_OUT");
		int timeSecond = Integer.valueOf(timeOut);
		return timeSecond;

	}
	public static String getServerPort()
	{
		return PropertyReader.getInstance().getPropertyByName("SERVER_PORT");
	}
 
}
