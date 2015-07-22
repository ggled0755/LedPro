package cn.fuego.misp.webservice.up.rest;


import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import cn.fuego.misp.webservice.json.MispBaseReqJson;
import cn.fuego.misp.webservice.json.MispBaseRspJson;
import cn.fuego.misp.webservice.up.model.LoginRsp;
import cn.fuego.misp.webservice.up.model.ModifyPwdReq;
import cn.fuego.misp.webservice.up.model.ModifyPwdRsp;


/**
 * 
* @ClassName: UserManageService 
* @Description: TODO
* @author Tang Jun
* @date 2014-10-20 上午10:53:45 
*
 */

@Path("/index.php")
@Produces("application/json")  
@Consumes("application/json")  
public interface MispUserManageRest
{
	//APP登录验证
	@POST
	@Path("/Index/Login")
	LoginRsp login(MispBaseReqJson req);
	
	//APP退出
	@POST
	@Path("/Index/Logout")
	LoginRsp logout(MispBaseReqJson req);
	
	//APP修改密码
	@POST
	@Path("/Index/ModifyPassword")
    ModifyPwdRsp modifyPassword(ModifyPwdReq req);
	
	@POST
	@Path("/Index/Register")
	MispBaseRspJson register(MispBaseReqJson req);
	
	@POST
	@Path("/Index/ResetPassword")
	MispBaseRspJson resetPassword(MispBaseReqJson req);
 
	//短信发送验证码
	@POST
	@Path("/MispUtil/SendVerifyCode")
	MispBaseRspJson sendVerifyCode(MispBaseReqJson req);
	

 
}
