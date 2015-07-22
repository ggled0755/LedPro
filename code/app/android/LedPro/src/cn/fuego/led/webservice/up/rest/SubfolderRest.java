/**   
* @Title: SubfolderRest.java 
* @Package cn.fuego.led.webservice.up.rest 
* @Description: TODO
* @author Aether
* @date 2015-7-10 下午8:27:53 
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
 * @ClassName: SubfolderRest 
 * @Description: TODO
 * @author Aether
 * @date 2015-7-10 下午8:27:53 
 *  
 */
@Path("/index.php")
@Produces("application/json")  
@Consumes("application/json") 
public interface SubfolderRest
{

	//该部分接口还存在问题，待改进
	@POST
	@Path("/Subfolder/LoadList")
	MispBaseRspJson loadSubfolder(MispBaseReqJson req);
	
	@POST
	@Path("/Subfolder/Create")
	MispBaseRspJson createSubfolder(MispBaseReqJson req);
	
	@POST
	@Path("/Subfolder/Modify")
	MispBaseRspJson modifySubfolder(MispBaseReqJson req);
	
	@POST
	@Path("/Subfolder/Delete")
	MispBaseRspJson deleteSubfolder(MispBaseReqJson req);
	
	@POST
	@Path("/SubfolderDeatil/Create")
	MispBaseRspJson createSubfolderDetail(MispBaseReqJson req);
	
	@POST
	@Path("/SubfolderDeatil/Modify")
	MispBaseRspJson modifySubfolderDetail(MispBaseReqJson req);
	
	@POST
	@Path("/SubfolderDeatil/Delete")
	MispBaseRspJson deleteSubfolderDetail(MispBaseReqJson req);
	
	
}
