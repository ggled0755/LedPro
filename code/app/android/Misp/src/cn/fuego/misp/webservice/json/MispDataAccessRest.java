/**   
* @Title: MispDataAccessService.java 
* @Package cn.fuego.misp.service 
* @Description: TODO
* @author Tang Jun   
* @date 2015-5-22 上午9:21:37 
* @version V1.0   
*/ 
package cn.fuego.misp.webservice.json;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

 /** 
 * @ClassName: MispDataAccessService 
 * @Description: TODO
 * @author Tang Jun
 * @date 2015-5-22 上午9:21:37 
 *  
 */
@Path("/basic")
@Produces("application/json")  
@Consumes("application/json")  
public interface MispDataAccessRest
{

	 @POST
	 @Path("/ComDataAccess!Create.action")
	 MispBaseRspJson Create(MispComDataAccessReqJson req);
	 
	 @POST
	 @Path("/ComDataAccess!Modify.action")
	 MispBaseRspJson Modify(MispComDataAccessReqJson req);
	 
	 @POST
	 @Path("/ComDataAccess!Delete.action")
	 MispBaseRspJson Delete(MispComDataAccessReqJson req);
 
}
