/**   
* @Title: ProjectRest.java 
* @Package cn.fuego.led.webservice.up.rest 
* @Description: TODO
* @author Aether
* @date 2015-7-10 下午8:55:10 
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
 * @ClassName: ProjectRest 
 * @Description: TODO
 * @author Aether
 * @date 2015-7-10 下午8:55:10 
 *  
 */
@Path("/index.php")
@Produces("application/json")  
@Consumes("application/json")

public interface ProjectRest
{
	
	@POST
	@Path("/Project/LoadAll")
	MispBaseRspJson loadAll(MispBaseReqJson req);
	
	@POST
	@Path("/Project/Create")
	MispBaseRspJson createProject(MispBaseReqJson req);
	
	@POST
	@Path("/Project/Delete")
	MispBaseRspJson deleteProject(MispBaseReqJson req);
	
	@POST
	@Path("/Project/Modify")
	MispBaseRspJson modifyProject(MispBaseReqJson req);
	
	//创建PDF文件
	@POST
	@Path("/Project/CreatePdf")
	MispBaseRspJson createPdf(MispBaseReqJson req);
	
}
