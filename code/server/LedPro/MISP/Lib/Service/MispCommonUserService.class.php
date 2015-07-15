<?php
import("MISP.Util.FuegoLog");
import("MISP.Util.FuegoException");
import("MISP.Constant.MispErrorCode");
import("MISP.Constant.UserTypeEnmu");
import("MISP.Service.MispCommonService");
import("MISP.Service.PrivilegeService");
class MispCommonUserService
{
	static function Create($user)
	{
		$errorCode = MispErrorCode::SUCCESS;
		FuegoLog::getLog()->LogInfo("Create user...");
		//获取用户类型
		$roleDao = MispDaoContext::SystemRole();
		$roleCondition['role_id'] = $user['role_id'];
		$userType = $roleDao->where($roleCondition)->getField('user_type_id');
		//用户信息查重条件
		$condition['user_name'] = $user['user_name'];
		if(UserTypeEnum::CUSTOMER == $userType)		//customer 需要增加Company_id条件
		{
			$condition['company_id'] = $user['company_id'];
		}
		$systemUserDao = MispDaoContext::SystemUser();
		$userID = $systemUserDao->where($condition)->getField('user_id');
		if($userID!=''){
			//用户已存在
			FuegoLog::getLog()->LogWarn("Create user failed, user_name has exist.".json_encode($condition));
			$errorCode = MispErrorCode::USER_EXISTED;
			throw new FuegoException(null,$errorCode);
		}
		FuegoLog::getLog()->LogInfo("User info is ".json_encode($user));
		$result = MispCommonService::Create($systemUserDao, $user);
		FuegoLog::getLog()->LogInfo("Create user success.");
		return $result;
		
	}
	/* (non-PHPdoc)
	 * @see MispUserService::Register()
	 */
	static function CustomerRegister($user)
	{
		$errorCode = MispErrorCode::SUCCESS;
		FuegoLog::getLog()->LogInfo('Create customer...');
		//创建customer记录
		$customerDao = AdminDaoContext::Customer();
		$customer['user_id'] = $user['user_id'];
		$customer['cellphone'] = $user['user_name'];
		$customer['customer_sex'] = SysStatusEnum::CUSTOMER_SEX_MALE;
		$customer['birthday'] = $user['reg_date'];
		$customer['trust_value'] = 5.00;
		$customer['appear_value'] = 5.00;
		$customer['courier_state'] = SysStatusEnum::COURIER_NO_REG;
		$customer['courier_work_status'] = SysStatusEnum::COURIER_WORK_STATUS_CLOSED;
		$customer['seller_state'] = SysStatusEnum::SELLER_NO_REG;
		$customer['company_id'] = 0;	
		FuegoLog::getLog()->LogInfo("customer info is ".json_encode($customer));
		try
		{
			$result = MispCommonService::Create($customerDao, $customer);
			FuegoLog::getLog()->LogInfo("Create customer success.");
			FuegoLog::getLog()->LogInfo("Customer register success.");
		}
		catch(FuegoException $e)
		{
			$errorCode = $e->getCode();
		}
		//创建account记录
		$accountDao = AdminDaoContext::Account();
		$account['user_id'] = $user['user_id'];
		$account['reward'] = 0.00;
		$account['rebate'] = 0.00;
		$account['order_accept'] = 0;
		$account['order_send'] = 0;
		$account['balance'] = 0.00;
		FuegoLog::getLog()->LogInfo("account info is ".json_encode($account));
		try 
		{
			$result = MispCommonService::Create($accountDao, $account);
			FuegoLog::getLog()->LogInfo("Create account success.");
		}
		catch(FuegoException $e)
		{
			$errorCode = $e->getCode();
		}
		return $errorCode;
	}
	static function LoginValidate($user)
	{
		$errorCode = MispErrorCode::SUCCESS;
		FuegoLog::getLog()->LogInfo("Login validate ...");
		$condition['user_name'] = $user['user_name'];
// 		if("" != $user['company_id'])
// 		{
// 			$condition['company_id'] = $user['company_id'];
// 		}
		FuegoLog::getLog()->LogInfo("condition is ".json_encode($condition));
		$userDao= MispDaoContext::SystemUser();
		$userCount = $userDao->where($condition)->count();
		if($userCount==0)		//用户不存在
		{
			FuegoLog::getLog()->LogErr("login failed, the user is not exsit. user name is ".$req->user_name);
			$errorCode = MispErrorCode::USERNAME_OR_PASSWORD_WRONG;
			throw new FuegoException(null,$errorCode);
		}
		$orignalUser = $userDao->where($condition)->find();
		if($user['password'] != $orignalUser['password'])		//验证密码
		{
			FuegoLog::getLog()->LogErr("login failed, the password is wrong. user name is ".$req->user_name);
			$errorCode = MispErrorCode::USERNAME_OR_PASSWORD_WRONG;
			throw new FuegoException(null,$errorCode);
		}
		FuegoLog::getLog()->LogInfo("Login validate success.");
		return $orignalUser;	
	}
	/* (non-PHPdoc)
	 * @see MispUserService::AppLogin()
	 */
	static  function AppLogin($user)
	{
		//创建新的token
		$tokenDao = MispDaoContext::Token();
		$token['token_id'] = DataCreateUtil::GetUUID();
		$token['obj_id'] = $user['user_id'];
		$token['token_type'] = TokenTypeEnum::USER;
		$result = $tokenDao->add($token);
		FuegoLog::getLog()->LogInfo("Create token success, token id is ".json_encode($result).". And token is ".$token['token_name']);
		FuegoLog::getLog()->LogInfo("Customer APPLogin success, the user name is ".$user['user_name']);
		//返回数据
		$data['user'] = $user;
		$data['token'] = $token['token_id'];
		return $data;
	}
	/* (non-PHPdoc)
	 * @see MispUserService::WebLogin()
	 */
	static function WebLogin($user)
	{
		$user['password'] = "";
		$time=30*60;
		setcookie(session_name(),session_id(),time()+$time,"/");
		session_start();                            // 初始化session
		$_SESSION['user'] = $user;
		FuegoLog::getLog()->LogInfo("User WEBLogin success, the user name is ".$user['user_name']);
		return MispErrorCode::SUCCESS;
	}
	/* (non-PHPdoc)
	 * @see MispUserService::ModifyPassword()
	 */
	static public function ModifyPassword($condition, $req)
	{
		FuegoLog::getLog()->LogInfo("Modify password...");
		$errorCode = MispErrorCode::SUCCESS;
		$userDao= MispDaoContext::SystemUser();
		$orginalUser = $userDao->where($condition)->find();
		$orginalPwd = $orginalUser['password'];
		if($orginalPwd != $req->pwdOld)
		{
			FuegoLog::getLog()->LogWarn("Modify password failed,pwdOld is wrong.");
			$errorCode = MispErrorCode::ERROR_OLD_PASSWORD_WORD;
			return $errorCode;
		}
		$orginalUser['password'] = $req->pwdNew;
		try
		{
			$result = MispCommonService::Modify($userDao, $orginalUser);
			FuegoLog::getLog()->LogInfo("Modify password success.");
		}
		catch(FuegoException $e)
		{
			$errorCode = $e->getCode();
		}
		return $errorCode;
	
	}
	static function Modify($user)
	{
		$errorCode = MispErrorCode::SUCCESS;
		FuegoLog::getLog()->LogInfo("modify user...");
		$systemUserDao = MispDaoContext::SystemUser();
		try
		{
			$result = MispCommonService::Modify($systemUserDao, $user);
		}
		catch(FuegoException $e)
		{
			$errorCode = $e->getCode();
		}
		return $errorCode;
	}
	static function Delete($userID)
	{
		$errorCode = MispErrorCode::SUCCESS;
		FuegoLog::getLog()->LogInfo("delete user...");
		$systemUserDao = MispDaoContext::SystemUser();
		$condition['user_id'] = $userID;
		try
		{
			$result = MispCommonService::Delete($systemUserDao, $condition);
		}
		catch(FuegoException $e)
		{
			$errorCode = $e->getCode();
		}
		return $errorCode;
	}	
}

?>