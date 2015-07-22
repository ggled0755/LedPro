package cn.fuego.misp.webservice.up.model;

import cn.fuego.misp.webservice.json.MispBaseRspJson;



/**
 * 
* @ClassName: LoginRsp 
* @Description: TODO
* @author Tang Jun
* @date 2014-10-20 上午10:59:34 
*
 */
public class LoginRsp extends MispBaseRspJson
{
 	private String token;

	public String getToken()
	{
		return token;
	}
	public void setToken(String token)
	{
		this.token = token;
	}

 

}
