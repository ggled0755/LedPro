/**   
* @Title: EnumRest.java 
* @Package cn.fuego.led.webservice.up.rest 
* @Description: TODO
* @author Aether
* @date 2015-7-18 下午5:35:59 
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
 * @ClassName: EnumRest 
 * @Description: TODO
 * @author Aether
 * @date 2015-7-18 下午5:35:59 
 *  
 */
@Path("/index.php")
@Produces("application/json")  
@Consumes("application/json") 

public interface EnumRest
{
	//加载全部，带过滤条件搜索
	@POST
	@Path("/Enum/LoadList")
	MispBaseRspJson loadList(MispBaseReqJson req);

}
