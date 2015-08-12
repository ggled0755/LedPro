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

	//分页加载，带过滤条件搜索
	@POST
	@Path("/Product/GetUnique")
	MispBaseRspJson getProduct(MispBaseReqJson req);
	
	//全部加载
	@POST
	@Path("/Product/LoadAll")
	MispBaseRspJson loadProductAll(MispBaseReqJson req);
	
	//产品评价,多项
	@POST
	@Path("/Eval/CreateList")
	MispBaseRspJson createEvalList(MispBaseReqJson req);
	
	//产品评价
	@POST
	@Path("/Eval/LoadEvalSum")
	MispBaseRspJson loadEvalSum(MispBaseReqJson req);
	
	//产品评价类型
	@POST
	@Path("/EvalType/LoadAll")
	MispBaseRspJson loadEvalType(MispBaseReqJson req);

	//产品过滤条件加载
	@POST
	@Path("/Product/GetFilterMeta")
	MispBaseRspJson loadFilter(MispBaseReqJson req);

	//产品过滤条件详细参数加载
	@POST
	@Path("/Product/GetEnumList")
	MispBaseRspJson loadEnum(MispBaseReqJson req);
	
	//产品详细参数加载
	@POST
	@Path("/Product/MetaShow")
	MispBaseRspJson loadMeta(MispBaseReqJson req);
}
