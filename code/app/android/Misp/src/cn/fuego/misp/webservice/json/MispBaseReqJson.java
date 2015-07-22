package cn.fuego.misp.webservice.json;

import java.util.List;

import cn.fuego.common.dao.QueryCondition;
import cn.fuego.misp.constant.MISPClientTypeEnum;
import cn.fuego.misp.service.MemoryCache;
import cn.fuego.misp.service.cache.SystemCache;

 
/**
 * 
* @ClassName: BaseJsonReq 
* @Description: TODO
* @author Tang Jun
* @date 2014-10-20 上午10:57:41 
*
 */
public class MispBaseReqJson
{
	protected String app_id = "1";
	protected String token=MemoryCache.getToken();
	protected String client_type= MISPClientTypeEnum.APP_ANDROID.getStrValue();
	protected String client_version;
	
	protected PageJson page;
	
	protected List<QueryCondition> conditionList;
	protected Object obj;

	public String getApp_id()
	{
		return app_id;
	}
	public void setApp_id(String app_id)
	{
		this.app_id = app_id;
	}
	public String getToken()
	{
		return token;
	}
	public void setToken(String token)
	{
		this.token = token;
	}

	public String getClient_type()
	{
		return client_type;
	}
	public void setClient_type(String client_type)
	{
		this.client_type = client_type;
	}
	public String getClient_version()
	{
		return client_version;
	}
	public void setClient_version(String client_version)
	{
		this.client_version = client_version;
	}
	public Object getObj()
	{
		return obj;
	}
	public void setObj(Object obj)
	{
		this.obj = obj;
	}
	public List<QueryCondition> getConditionList()
	{
		return conditionList;
	}
	public void setConditionList(List<QueryCondition> conditionList)
	{
		this.conditionList = conditionList;
	}
 
	public PageJson getPage()
	{
		return page;
	}
	public void setPage(PageJson page)
	{
		this.page = page;
	}
	public void clear()
	{
		this.app_id = "";
		this.client_type = "";
		this.client_version = "";
		this.conditionList = null;
		this.obj = null;
		this.page = null;
		this.token = null;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "MispBaseReqJson [app_id=" + app_id + ", token=" + token
				+ ", client_type=" + client_type + ", client_version="
				+ client_version + ", page=" + page + ", conditionList="
				+ conditionList + ", obj=" + obj + "]";
	}
	
 
 

}
