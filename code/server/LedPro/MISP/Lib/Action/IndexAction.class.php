<?php
import("MISP.Model.MispServiceContext");
// 本类由系统自动生成，仅供测试用途
class IndexAction extends EasyUITableAction 
{
	
    /* (non-PHPdoc)
	 * @see EasyUITableAction::GetModel()
	 */
	protected function GetModel()
	{
		// TODO Auto-generated method stub
		
	}

	//APP会员注册
    public function Register()
    {
    	$this->LogInfo("Customer register ...");
    	$user = $this->objectToArray($this->GetCommonData());
    	//验证用户名是否重复
    	$condition['user_name'] = $user['user_name'];
    	$userDao= MispDaoContext::SystemUser();
    	$userCount = $userDao->where($condition)->count();
    	if($userCount!=0)		//用户已存在
    	{
    		$this->LogWarn("Register failed, user name has exist, user name is ".$user['user_name']);
    		$this->rsp->errorCode = MispErrorCode::USER_EXISTED;
    		$this->ReturnJson();
    		return;
    	}
    	//创建用户信息
    	$user['role_id'] = RoleEnum::SELLER_CUSTOMER;
    	$user['reg_date'] = date('Y-m-d H:i:s',time());
    	$user['company_id'] = 0;
    	try
    	{
    		$userID = MispCommonUserService::Create($user);
    	}
    	catch(FuegoException $e)
    	{
    		$this->rsp->errorCode = $e->getCode();
    		$this->ReturnJson();
    		return;
    	}
    	//创建会员信息
    	$user['user_id'] = $userID;
    	$registerCode = MispCommonUserService::CustomerRegister($user);
    	if(MispErrorCode::SUCCESS !=$registerCode)
    	{
    		$this->rsp->errorCode = $registerCode;
    		$this->ReturnJson();
    		return;
    	}
    	//获取会员信息
    	$customerDao = AdminDaoContext::Customer();
    	$customerCondition['user_id'] = $userID;
    	try
    	{
    		$customer = MispCommonService::GetUniRecord($customerDao, $customerCondition);
    	}
    	catch(FuegoException $e)
    	{
    		$this->rsp->errorCode = $e->getCode();
    		$this->ReturnJson();
    		return;
    	}
    	$this->rsp->obj = $customer;
    	$this->ReturnJson();
    }
    public function AddRecPerson(){
    	$customer = $this->objectToArray($this->GetCommonData());
    	if(null != $customer['recommend_phone'])
    	{
    		//获取推荐人信息
    		$customerDao = AdminDaoContext::Customer();
    		$condition['cellphone'] = $customer['recommend_phone'];
    		try
    		{
    			$recCustomer = MispCommonService::GetUniRecord($customerDao, $condition);
    			$this->LogInfo("Get recommend customer success, recCustomer is ".json_encode($recCustomer));
    		}
    		catch(FuegoException $e)
    		{
    			$errorCode = $e->getCode();
    		}
    		$customer['recommend_user_id'] = $recCustomer['user_id'];
    		//更新注册用户信息
    		try
    		{
    			$result = MispCommonService::Modify($customerDao, $customer);
    		}
    		catch(FuegoException $e)
    		{
    			$this->rsp->errorCode = $e->getCode();
    		}
    		$this->rsp->errorCode = MispErrorCode::SUCCESS;
    		$this->ReturnJson();
    	}
    }
    //WEB/APP登录验证
    public function Login()
    {
    	$req = $this->GetCommonData();
    	$reqType = $this->GetReqType();
    	$this->LogInfo("User login, Client type is ".$reqType);
    	$user = $this->objectToArray($req);
    	
    	//验证用户与登录密码
    	try
    	{
    		$orignalUser = MispCommonUserService::LoginValidate($user);
    	}
    	catch(FuegoException $e)
    	{
    		$this->rsp->errorCode = $e->getCode();
    		return  $this->ReturnJson();
    	}
    	
    	//APP客户端登陆验证
    	if(($reqType == ClientTypeEnum::IOS)||($reqType == ClientTypeEnum::ANDROID))
    	{
    		//APP端登陆权限验证
    		$this->LogInfo("orignal user is ".json_encode($orignalUser));
    		if(RoleEnum::SELLER_CUSTOMER != $orignalUser['role_id'])
    		{
    			//用户不存在APP登录权限
    			$this->LogWarn("Get role privilege failed. The user don't have WEB login privilege, login failed.");
    			$this->rsp->errorCode = MispErrorCode::USERNAME_OR_PASSWORD_WRONG;
    			$this->ReturnJson();
    			return;
    		}
    		
    		//判断是否已经在其他设备登录
    		$condition['obj_id'] = $orignalUser['user_id'];
    		$condition['token_type'] = TokenTypeEnum::USER;
    		$tokenDao = MispDaoContext::Token();
    		$tokenCount = $tokenDao->where($condition)->count();
    		$this->LogInfo("Token count is ".$tokenCount);
    		if($tokenCount>0)
    		{
    			//用户已在其他设备登录，删除已有token
    			$this->LogInfo("The user has login in other device, user name is ".$orignalUser['user_name']);
    			try
    			{
    				$result = MispCommonService::Delete($tokenDao, $condition);
    				$this->LogInfo("Delete orginal token success.");
    			}
    			catch(FuegoException $e)
    			{
    				$this->LogWarn("Delete orginal token failed.");
    				$this->LogWarn("Customer APPLogin failed");
    				$this->rsp->errorCode = MispErrorCode::ERROR_LOGIN_FAILED;
    				$this->ReturnJson();
    				return;
    			}	
    		}
    		//APP登录成功
    		$data = MispCommonUserService::AppLogin($orignalUser);
    		$this->rsp->obj = $data['user'];
    		$this->rsp->token = $data['token'];
    		$this->ReturnJson();
    	}
    	//WEB端登陆验证
    	if($reqType == ClientTypeEnum::WEB)
    	{
    		//WEB端登陆权限验证
    		$privilegeResult = false;
    		$privilegeList = MispCommonDataService::GetUserPrivilege($orignalUser, PrivilegeEnum::ACCESS_TYPE_LOGIN);    		
    		$this->LogInfo("login privilege is ".json_encode($privilegeList));
    		foreach ($privilegeList as $privilege)
    		{
    			if(PrivilegeEnum::ACCESS_VALUE_WEB_LOGIN == $privilege)
    			{
    				$privilegeResult = true;
    				break;
    			}
    		}
    		if(false ==  $privilegeResult)
    		{ 
    			//用户不存在WEB登录权限
    			$this->LogWarn("Get role privilege failed. The user don't have WEB login privilege, login failed.");
    			$this->rsp->errorCode = MispErrorCode::USERNAME_OR_PASSWORD_WRONG;
    		}
    		else
    		{
    			//WEB登录成功
    			$this->LogInfo("Get user login privilege success. The user have WEB login privilege.");
    			$this->rsp->errorCode = MispCommonUserService::WebLogin($orignalUser);
    		}
    		
    	}
    	$this->ReturnJson();   	
    }
    //WEB退出系统
    public function Logout()
    {
    	$reqType = $this->GetReqType();
    	$this->LogInfo("User Logout, Client type is ".$reqType);
    	$this->LogInfo("WEB logout...");
    	$this->rsp->errorCode = MispErrorCode::SUCCESS;
    	session_destroy();
    	$this->LogInfo("User logout success.");
    	$this->ReturnJson();
    }
    //修改密码
    public function ModifyPassword()
    {
    	$req = $this->GetCommonData();
    	$reqType = $this->GetReqType();
    	$this->LogInfo("Modify password,client type is ".$reqType);
    	if(($reqType == ClientTypeEnum::IOS)||($reqType == ClientTypeEnum::ANDROID))
    	{
    		//修改密码
    		$condition['user_name'] = $req->user_name;
    		$this->rsp->errorCode = MispCommonUserService::ModifyPassword($condition, $req);
    		if($this->rsp->errorCode == MispErrorCode::SUCCESS)
    		{
    			$token = $this->GetToken();
    			$this->rsp->errorCode = MispCommonUserService::AppLogout($token);
    		}
    	}
    	if($reqType == "WEB")
    	{
    		$user = $this->GetLoginUser();
    		$condition['user_name'] = $user['user_name'];
    		$this->rsp->errorCode = MispCommonUserService::ModifyPassword($condition, $req);
    		if($this->rsp->errorCode == MispErrorCode::SUCCESS)
    		{
    			session_destroy();
    		}
    	}
    	$this->ReturnJson();
    }
    //APP会员找回密码
    public function ResetPassword()
    {
    	
    	$req = $this->GetCommonData();
    	$systemUserDao = MispDaoContext::SystemUser();
    	$condition['user_name'] = $req->user_name;
    	$this->LogInfo("Customer reset password , condition info is ".json_encode($condition));
    	$userID = $systemUserDao->where($condition)->getField('user_id');
    	if($userID ==''){
    		$this->LogWarn("Finding password failed, user_name is not exist.".$req->user_name);
    		$this->rsp->errorCode = MispErrorCode::ERROR_USER_NOT_EXISTED;
    		$this->ReturnJson();
    		return;
    	}
    	$user['user_id'] = $userID;
    	$user['password'] = $req->password;
    	try
    	{
    		$result = MispCommonService::Modify($systemUserDao, $user);
    	}
    	catch(FuegoException $e)
    	{
    		$this->rsp->errorCode = $e->getCode();
    	}
    	$this->ReturnJson();
    }
    //WEB加载菜单列表
    public function GetMenuTree()
    {
    	$this->LogInfo("get menu tree");
    	$loginUser = $this->GetLoginUser();
    	//获取用户菜单权限
    	$privilegeList = MispCommonDataService::GetUserPrivilege($loginUser, PrivilegeEnum::ACCESS_TYPE_MENU);
    	//获取菜单列表
    	$menuDao = MispDaoContext::Menu();
    	$map['menu_id']=array('in',$privilegeList);
    	$menuList = $menuDao->where($map)->select();
    	$this->LogInfo("menu tree is ".json_encode($menuList));
    	$this->ReturnJson($menuList);
    }
}