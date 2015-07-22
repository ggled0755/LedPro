package cn.fuego.misp.webservice.up.model;

import cn.fuego.misp.webservice.json.MispBaseRspJson;
import cn.fuego.misp.webservice.up.model.base.ClientVersionJson;

public class GetClientVersionRsp extends MispBaseRspJson
{
	private ClientVersionJson obj;

	public ClientVersionJson getObj()
	{
		return obj;
	}

	public void setObj(ClientVersionJson obj)
	{
		this.obj = obj;
	}
	

}
