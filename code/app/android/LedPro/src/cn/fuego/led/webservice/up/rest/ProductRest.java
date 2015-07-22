/**   
* @Title: ProductRest.java 
* @Package cn.fuego.led.webservice.up.rest 
* @Description: TODO
* @author Aether
* @date 2015-7-10 下午8:04:24 
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
 * @ClassName: ProductRest 
 * @Description: TODO
 * @author Aether
 * @date 2015-7-10 下午8:04:24 
 *  
 */
@Path("/index.php")
@Produces("application/json")  
@Consumes("application/json") 

public interface ProductRest
{

	//分页加载，带过滤条件搜索
	@POST
	@Path("/Product/Load")
	MispBaseRspJson loadProduct(MispBaseReqJson req);
	
	//产品评价
	@POST
	@Path("/Eval/Create")
	MispBaseRspJson createEval(MispBaseReqJson req);
}
