package cn.fuego.led.webservice.up.rest;



import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import cn.fuego.common.log.FuegoLog;
import cn.fuego.misp.service.MemoryCache;
import cn.fuego.misp.service.http.HttpListener;
import cn.fuego.misp.service.http.MispProxyFactory;
import cn.fuego.misp.webservice.up.rest.MispSystemManageRest;
import cn.fuego.misp.webservice.up.rest.MispUserManageRest;

public class WebServiceContext
{
	private FuegoLog log = FuegoLog.getLog(getClass());

	private static WebServiceContext instance;

	public static String hostURL = MemoryCache.getWebContextUrl();
	
	private WebServiceContext()
	{
		log.info("the host and base url is "+hostURL);

	}

	public static synchronized WebServiceContext getInstance()
	{
		if (null == instance)
		{
			instance = new WebServiceContext();
		}
		return instance;
	}
 
	private HttpClient getHttpClient()
	{
		HttpClient httpClient = new DefaultHttpClient();
	 
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
 
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
		return httpClient;

	}

	public MispUserManageRest getMispUserManageRest(HttpListener handler)
	{
	 
		MispUserManageRest rest = MispProxyFactory.create(hostURL,MispUserManageRest.class, getHttpClient(),handler);

		return rest;
	}
	public MispSystemManageRest getMispSystemManageRest(HttpListener handler)
	{
	 
		MispSystemManageRest rest = MispProxyFactory.create( hostURL,MispSystemManageRest.class, getHttpClient(),handler);

		return rest;
	}
	 

	public CustomerRest getCustomerRest(HttpListener handler)
	{
	 
		CustomerRest rest = MispProxyFactory.create( hostURL,CustomerRest.class, getHttpClient(),handler);

		return rest;
	}

	public ProductRest getProductRest(HttpListener handler)
	{
	 
		ProductRest rest = MispProxyFactory.create( hostURL,ProductRest.class, getHttpClient(),handler);

		return rest;
	}

	public EnumRest getEnumRest(HttpListener handler)
	{
	 
		EnumRest rest = MispProxyFactory.create( hostURL,EnumRest.class, getHttpClient(),handler);

		return rest;
	}
	
	public ProjectRest getProjectRest(HttpListener handler)
	{
	 
		ProjectRest rest = MispProxyFactory.create( hostURL,ProjectRest.class, getHttpClient(),handler);

		return rest;
	}
}
