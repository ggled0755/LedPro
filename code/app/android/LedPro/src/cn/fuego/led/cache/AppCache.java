package cn.fuego.led.cache;



import cn.fuego.common.log.FuegoLog;
import cn.fuego.common.util.validate.ValidatorUtil;
import cn.fuego.led.dao.ConfigInfo;
import cn.fuego.led.webservice.up.model.base.CustomerJson;
import cn.fuego.led.webservice.up.rest.WebServiceContext;
import cn.fuego.misp.dao.SharedPreUtil;
import cn.fuego.misp.service.MemoryCache;
import cn.fuego.misp.service.http.MispHttpHandler;
import cn.fuego.misp.service.http.MispHttpMessage;
import cn.fuego.misp.webservice.json.MispBaseReqJson;
import cn.fuego.misp.webservice.json.MispBaseRspJson;
import cn.fuego.misp.webservice.up.model.base.CompanyJson;
import cn.fuego.misp.webservice.up.model.base.UserJson;

public class AppCache
{

	public static String PAY_NOTIFY_URL = MemoryCache.getWebContextUrl() + "/index.php/AlipayNotify/GetNotify";

	private FuegoLog log = FuegoLog.getLog(getClass());
	private UserJson user;
	private CustomerJson customer;
	private CompanyJson company;
	private static AppCache instance;
	private  int company_id = 1;
 
	
	public static final String USER_CACHE="user";
	public static final String CUSTOMER_CACHE="customer";
	public static final String TOKEN_CACHE="token";
	
	
	private boolean firstStarted = true;
	private boolean started = false;

	public static final String CONFIG = "config"; //存储一些个人配置信息
	private ConfigInfo cfgInfo;
	
	public boolean isFirstStarted()
	{
		String startStr = (String) SharedPreUtil.getInstance().get("isFirstStarted");
		if(!ValidatorUtil.isEmpty(startStr))
		{
			firstStarted = Boolean.valueOf(startStr);
		}
		
		return firstStarted;
	}

	public void setFirstStarted(boolean firstStarted)
	{
		this.firstStarted = firstStarted;
		SharedPreUtil.getInstance().put("isFirstStarted", String.valueOf(firstStarted));

	}

	public boolean isStarted()
	{
		return started;
	}

	public void setStarted(boolean started)
	{
		this.started = started;
	}

	public CompanyJson getCompany()
	{
		return company;
	}

	public int getCompany_id()
	{
		return company_id;
	}
 
 
 

	private AppCache()
	{
		 company_id = 1;
		 
		  
	}
	
	public synchronized static AppCache getInstance()
	{
		if(null == instance)
		{
			instance = new AppCache();
		}
		return instance;
		
	}
	
	public void clear()
	{
		MemoryCache.setToken(null);
		user = null;
		customer = null;
		SharedPreUtil.getInstance().delete(USER_CACHE);
		SharedPreUtil.getInstance().delete(CUSTOMER_CACHE);
		SharedPreUtil.getInstance().delete(TOKEN_CACHE);
				
	}

	public CustomerJson getCustomer()
	{
		return customer;
	}
	public void update(CustomerJson customer)
	{
		if(customer!=null)
		{
			this.customer = customer;
			SharedPreUtil.getInstance().put(CUSTOMER_CACHE, customer);
			load();
		}

	}
	public void update(String token,UserJson user,CustomerJson customer)
	{
 
		SharedPreUtil.getInstance().put(USER_CACHE, user);
		SharedPreUtil.getInstance().put(CUSTOMER_CACHE, customer);
		SharedPreUtil.getInstance().put(TOKEN_CACHE,token);
		load();

	}

	public void load()
	{
		MemoryCache.setToken((String) SharedPreUtil.getInstance().get(TOKEN_CACHE));
		this.user =  (UserJson) SharedPreUtil.getInstance().get(USER_CACHE);
		this.customer = (CustomerJson) SharedPreUtil.getInstance().get(CUSTOMER_CACHE);
		this.cfgInfo = (ConfigInfo) SharedPreUtil.getInstance().get(CONFIG);
	
	}
	public void updateConfig(ConfigInfo config)
	{
		SharedPreUtil.getInstance().put(CONFIG, config);
	}
	public UserJson getUser()
	{
		return user;
	}

 
	public void loadCustomer()
	{
		MispBaseReqJson req = new MispBaseReqJson();
		req.setObj(AppCache.getInstance().getUser().getUser_id());
		WebServiceContext.getInstance().getCustomerRest(new MispHttpHandler(){
			@Override
			public void handle(MispHttpMessage message)
			{
				if(message.isSuccess())
				{
					MispBaseRspJson rsp = (MispBaseRspJson) message.getMessage().obj;
					CustomerJson json = rsp.GetReqCommonField(CustomerJson.class);
					if(json!=null)
					{
						customer=json;
						update(json);
						
					}
				}
				else
				{
					log.error("can not get the customer information");
				}
			}
		}).getCustomer(req);
		
	}

	public ConfigInfo getCfgInfo()
	{
		return cfgInfo;
	}

	public void setCfgInfo(ConfigInfo cfgInfo)
	{
		this.cfgInfo = cfgInfo;
	}

	 
}
