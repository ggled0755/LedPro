<?php
import("MISP.Util.FuegoLog");
import("MISP.Constant.PrivilegeEnum");
import("MISP.Constant.CompanyEnum");
import("MISP.Model.MispDaoContext");
class MispCommonDataService
{
    //获取用户权限
    static function GetUserPrivilege($user,$obj_type)
    {
    	FuegoLog::getLog()->LogInfo("Get role privilege...");
    	//判断公司状态
//     	$companyCondition['company_id'] = $user['company_id'];
//     	$companyDao = MispDaoContext::Company();
//     	$companyStatus = $companyDao->where($companyCondition)->getField('company_status');
//     	if(CompanyEnum::STATUS_FREEZE == $companyStatus)
//     	{
//     		FuegoLog::getLog()->LogInfo("Get role privilege failed. The company is freezed, company id is ".$user['company_id']);
//     		return false;
//     	}
		
		$privilegeDao = MispDaoContext::Privilege();
		$allPriValueList = array();
		//获取角色权限
    	$loginCondition['master_value'] = $user['role_id'];
    	$loginCondition['master_type'] = PrivilegeEnum::MASTER_TYPE_ROLE;
    	$loginCondition['access_obj_type'] = $obj_type;
    	$rolePriValueList = $privilegeDao->where($loginCondition)->getField('access_obj_value',true);
    	foreach ($rolePriValueList as $rolePri)
    	{
    		FuegoLog::getLog()->LogInfo("role pri is ".$rolePri);
    		array_push($allPriValueList, $rolePri);
    	}
    	FuegoLog::getLog()->LogInfo("Role privilege is ".json_encode($rolePriValueList));
    	//获取用户权限
    	FuegoLog::getLog()->LogInfo("Get user privilege...");
    	$loginCondition['master_value'] = $user['user_id'];
    	$loginCondition['master_type'] = PrivilegeEnum::MASTER_TYPE_USER;
    	$loginCondition['access_obj_type'] = $obj_type;
    	$userPriValueList = $privilegeDao->where($loginCondition)->getField('access_obj_value',true);
    	FuegoLog::getLog()->LogInfo("User privilege is ".json_encode($userPriValueList));
    	foreach ($userPriValueList as $userPri)
    	{
    		FuegoLog::getLog()->LogInfo("user pri is ".$userPri);
    		array_push($allPriValueList, $userPri);
    	}
    	
    	FuegoLog::getLog()->LogInfo("All user privilege is ".json_encode($allPriValueList));
    	return $allPriValueList;
    }
    //账户变化日志
    static function AccountLog($account_log)
    {
    	FuegoLog::getLog()->LogInfo("Create account log...");
    	$condition['user_id'] = $account_log['user_id'];
    	//获取用户信息
    	$userDao = MispDaoContext::SystemUser();
    	try
    	{
    		$user = MispCommonService::GetUniRecord($userDao, $condition);
    	}
    	catch(FuegoException $e)
    	{
    		FuegoLog::getLog()->LogErr("get user info failed.");
    		return MispErrorCode::ACCOUNT_OPERATE_LOG_FAILED;
    	}
    	//获取账户余额信息
    	$accountDao = AdminDaoContext::Account();
    	try
    	{
    		$account = MispCommonService::GetUniRecord($accountDao, $condition);
    	}
    	catch(FuegoException $e)
    	{
    		FuegoLog::getLog()->LogErr("get account info failed.");
    		return MispErrorCode::ACCOUNT_OPERATE_LOG_FAILED;
    	}
    	$account_log['user_name'] = $user['user_name'];
    	$account_log['rear_balance'] = $account['balance'];
    	$account_log['operate_time'] = date('Y-m-d H:i:s',time());
    	if(AdminEnum::ACCOUNT_OPERATE_FLAG_INCREASE == $account_log['operate_flag'])
    	{
    		$account_log['pre_balance'] = $account_log['rear_balance'] - $account_log['balance_change'];
    	}
    	else if(AdminEnum::ACCOUNT_OPERATE_FLAG_DECREASE == $account_log['operate_flag'])
    	{
    		$account_log['pre_balance'] = $account_log['rear_balance'] + $account_log['balance_change'];
    	}
    	//创建账户操作日志
    	$accountLogDao = AdminDaoContext::AccountLog();
    	try
    	{
    		$objID= MispCommonService::Create($accountLogDao, $account_log);
    	}
    	catch(FuegoException $e)
    	{
    		FuegoLog::getLog()->LogErr("create account log failed.");
    		return MispErrorCode::ACCOUNT_OPERATE_LOG_FAILED;
    	}
    	return MispErrorCode::SUCCESS;
    	 
    }
	
}

?>