/**   
* @Title: CustomerManageRest.java 
* @Package cn.fuego.helpbuy.webservice.up.rest 
* @Description: TODO
* @author Aether
* @date 2015-5-28 上午10:42:41 
* @version V1.0   
*/ 
package cn.fuego.led.webservice.up.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import cn.fuego.misp.webservice.json.MispBaseReqJson;
import cn.fuego.misp.webservice.json.MispBaseRspJson;

/** 
 * @ClassName: CustomerManageRest 
 * @Description: TODO
 * @author Aether
 * @date 2015-5-28 上午10:42:41 
 *  
 */
@Path("/index.php")
@Produces("application/json")  
@Consumes("application/json") 
public interface CustomerRest
{
	

	//修改用户信息
	@POST
	@Path("/Customer/Modify")
	MispBaseRspJson modifyCustomer(MispBaseReqJson req);
	
	//获取用户信息
	@POST
	@Path("/Customer/Show")
	MispBaseRspJson getCustomer(MispBaseReqJson req);	

	
	//删除图片
	@POST
	@Path("/MispFile/ImgDelete")
	MispBaseRspJson deleteFile(MispBaseReqJson req);	
	
}
