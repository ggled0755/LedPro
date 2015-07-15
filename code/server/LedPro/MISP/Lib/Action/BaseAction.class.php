<?php
// 本类由系统自动生成，仅供测试用途
require_once './Public/PHPInclude/IncludeMisp.php';
class MispBaseRspJson
{
	public $errorCode = 0;
	public $errorMsg;
	public $obj;
	public function __construct($errorCode)
	{

		$this->errorCode = $errorCode;

	}
}
class BaseAction extends Action 
{
	 protected $rsp;
	 private  $reqLogFlag = true;
// 	 public function __construct()
// 	 {
	 
// 	 	$this->rsp = new MispBaseRspJson();
	 
// 	 }
	 public function LogErr($log)
	 {
	   Log::write($log);
	 }
	 public function LogWarn($log)
	 {
	 	Log::write($log,Log::WARN);
	 }
	 public function LogInfo($log)
	 {
	 	Log::write($log,Log::INFO);
	 }
	 //Session统一验证
	 public function _initialize(){
	 	$this->rsp = new MispBaseRspJson();
	 	$this->rsp->errorCode = MispErrorCode::SUCCESS;
	 	$indexURL = "Index/index";
	 	$loginURL = "Index/Login";
	 	$logoutURL = "Index/Logout";
	 	$redirectURL = "Index/redirectPage";
	 	
	 	$reqURL = $_SERVER["REQUEST_URI"];
	 	if((ClientTypeEnum::WEB == $this->GetReqType())||(""== $this->GetReqType()))
	 	{
	 		$this->LogInfo("Session validator...".$this->GetReqType());
	 		if(strpos($reqURL, $redirectURL)||strpos($reqURL, $indexURL)||strpos($reqURL, $loginURL)||strpos($reqURL, $logoutURL))
	 		{
	 			$this->LogInfo("Session validator login Page");
	 			return;
	 		}
	 		if($_SESSION['user']['user_name'] == null)
	 		{
	 			$this->LogInfo("Session is out ...,Jump to login page");
	 			session_destroy();
	 			header("location: http://".$_SERVER['HTTP_HOST']."/LedPro/index.php/Index/redirectPage.html");
	 		}
	 		else 
	 		{
	 			$time=30*60;
	 			setcookie(session_name(),session_id(),time()+$time,"/");
	 		}
	 	}
	 }
	 
	 private function GetReqJson()
	 {
	   $req = file_get_contents("php://input");
	   if($this->reqLogFlag)
	   {
	   	$this->LogInfo('the url is '.$_SERVER["REQUEST_URI"] );
	   	$this->LogInfo('request is '.$req);
	   	$this->reqLogFlag=false;
	   	 
	   }
	   return $req;
	 }
	 
	 public function GetReqObj()
	 {
	   $json = $this->GetReqJson();
	   $req = json_decode($json);
	   return $req;
	 }
	 public function GetReqType()
	 {
	 	$json = $this->GetReqJson();
	 	$req = json_decode($json);
	 	return $req->client_type;
	 }
	 public function GetToken()
	 {
	 	$json = $this->GetReqJson();
	 	$req = json_decode($json);
	 	return $req->token;
	 }
	 public function ConvertToCondition($filterList)
	 {
	 	$searchFilter= array();
	 	$conditiontype = array();
	 	foreach($filterList as $filter)
	 	{
	 		$this->LogInfo("typeStr is ".$filter->typeStr);
	 		$this->LogInfo("first value is ".$filter->firstValue);
	 		if("" !== $filter->firstValue)
	 		{
	 			$condition = array();
	 			switch ($filter->typeStr){
	 				case MispFilterEnum::FILTER_INCLUDE :
	 					$keyValue = '%'.$filter->firstValue.'%';
	 					if(null==$conditiontype[$filter->attrName])
	 					{
	 						$searchFilter[$filter->attrName] = array('like',$keyValue);
	 						$conditiontype[$filter->attrName] = "1";
	 					}
	 					else
	 					{
	 						$condition[$filter->attrName] = array('like',$keyValue);
	 						$condition['_logic'] = "AND";
	 						$searchFilter['_complex'] = $condition;
	 					}
	 						
	 					break;
	 				case MispFilterEnum::FILTER_EQUAL :
	 					if(null==$conditiontype[$filter->attrName])
	 					{
	 						$searchFilter[$filter->attrName] = $filter->firstValue;
	 						$conditiontype[$filter->attrName] = "1";
	 					}
	 					else
	 					{
	 						$condition[$filter->attrName] = $filter->firstValue;
	 						$condition['_logic'] = "AND";
	 						$searchFilter['_complex'] = $condition;
	 					}
	 					break;
	 				case MispFilterEnum::FILTER_BIGGER :
	 					if(null==$conditiontype[$filter->attrName])
	 					{
	 						$searchFilter[$filter->attrName] = array('EGT',$filter->firstValue);
	 						$conditiontype[$filter->attrName] = "1";
	 					}
	 					else
	 					{
	 						$condition[$filter->attrName] = array('EGT',$filter->firstValue);
	 						$condition['_logic'] = "AND";
	 						$searchFilter['_complex'] = $condition;
	 					}
	 					break;
	 				case MispFilterEnum::FILTER_LOWER :
	 					if(null==$conditiontype[$filter->attrName])
	 					{
	 						$searchFilter[$filter->attrName] = array('ELT',$filter->firstValue);
	 						$conditiontype[$filter->attrName] = "1";
	 					}
	 					else
	 					{
	 						$condition[$filter->attrName] = array('ELT',$filter->firstValue);
	 						$condition['_logic'] = "AND";
	 						$searchFilter['_complex'] = $condition;
	 					}
	 					break;
 					case MispFilterEnum::FILTER_IN :
 						if(null==$conditiontype[$filter->attrName])
 						{
 							$searchFilter[$filter->attrName] = array('in',$filter->firstValue);
 							$conditiontype[$filter->attrName] = "1";
 						}
 						else
 						{
 							$condition[$filter->attrName] = array('in',$filter->firstValue);
 							$condition['_logic'] = "AND";
 							$searchFilter['_complex'] = $condition;
 						}
 						break;
					case MispFilterEnum::FILTER_NOT_EQUAL :
						if(null==$conditiontype[$filter->attrName])
						{
							$searchFilter[$filter->attrName] = array('neq',$filter->firstValue);
							$conditiontype[$filter->attrName] = "1";
						}
						else
						{
							$condition[$filter->attrName] = array('neq',$filter->firstValue);
							$condition['_logic'] = "AND";
							$searchFilter['_complex'] = $condition;
						}
						break;			
	 			}
	 		}
	 		
	 	}
	 	return $searchFilter;
	 }
	 public function GetCondition()
	 {
	 	$json = $this->GetReqJson();
	 	$req = json_decode($json);
	 	$searchFilter = $this->ConvertToCondition($req->conditionList);
	 	return $searchFilter;
	 }
	 public function GetReqAppID()
	 {
	 	$json = $this->GetReqJson();
	 	$req = json_decode($json); 
	 	return $req->app_id;
	 }
	 public function GetPageData()
	 {
	 	$json = $this->GetReqJson();
	 	$req = json_decode($json);
	 	$page = null;
	 	if(null === $req->page->currentPage)
	 	{
	 		$page['currentPage'] = 0;
	 		$this->LogWarn("the crrent page is empty");
	 	}
	 	else
	 	{
	 		$page['currentPage'] = $req->page->currentPage -1;
	 	}
	 	if(null === $req->page->pageSize)
	 	{
	 		$page['pageSize'] = 10;
	 		$this->LogWarn("the page size is empty");
	 	}
	 	else
	 	{
	 		$page['pageSize'] = $req->page->pageSize;
	 	}
	 	$this->LogInfo("the current page is ".$page['currentPage'].",the page size is ".$page['pageSize']);
	 	return $page;
	 }
	 public function GetCommonData()
	 {
	 	$json = $this->GetReqJson();
	 	$req = json_decode($json);
	 	return $req->obj;
	 }
	 public function GetLoginUser()
	 {
	 	return $_SESSION['user'];
	 }
	 public function DoAuth()
	 {
	   $this->LogInfo("User login validator.DoAuth...");
	   $req =  $this->GetReqObj();
	   $condition['token_name'] = $req->token;
	   $tokenDao = MispDaoContext::Token();
	   $tokenCount = $tokenDao->where($condition)->count();
	   if(0 == $tokenCount)
	   {
	   	    $this->LogWarn("DoAuth failed, user login invalid.");
	   		$this->rsp->errorCode = MispErrorCode::ERROR_LOGIN_INVALID;
	   		$this->ReturnJson();
	   }
	   $this->LogInfo("DoAuth success.");
	 }
	 
 
	 public  function  ReturnJson($data=null)
	 {
	 	$json = "";
	 	$clientType = $this->GetReqType();
 	 	if(null == $data)
	 	{
	 		
	 		if((ClientTypeEnum::ANDROID == $clientType)||(ClientTypeEnum::IOS == $clientType))
	 		{
	 			$this->rsp->errorMsg = null;
	 		}
	 		else
	 		{
	 			if(!ValidatorUtil::IsEmpty($this->rsp->PrivateErrorMsg))
	 			{
	 				$this->rsp->errorMsg = $this->rsp->PrivateErrorMsg;
	 			}
	 			else
	 			{
	 				$this->rsp->errorMsg = $this->GetErrorMsg();
	 			}
	 		}
	 		$json = json_encode($this->rsp);
 	 	}
	 	else
	 	{
	 		$json = json_encode($data);
	 		
	 	}
	     $this->LogInfo('the url is '.$_SERVER["REQUEST_URI"] );
	     $this->LogInfo('response is '.$json);
	     header('Content-Type:application/json;charset=utf-8');
	     echo $json;
	     exit;
	 }
	 
	 public  function  GetErrorMsg()
	 {
	 	$ini_array = parse_ini_file("./MISP/Lang/MispErrorMessage.ini");
	    return $ini_array[$this->rsp->errorCode];
	 }
	 
	 function objectToArray($array) {
	 	if(is_object($array)) {
	 		$array = (array)$array;
	 	} if(is_array($array)) {
	 		foreach($array as $key=>$value) {
	 			$array[$key] = $this->objectToArray($value);
	 		}
	 	}
	 	return $array;
	 }

	 
   function uuid($prefix = '')  
   {  
    $chars = md5(uniqid(mt_rand(), true));  
    $uuid  = substr($chars,0,8) . '-';  
    $uuid .= substr($chars,8,4) . '-';  
    $uuid .= substr($chars,12,4) . '-';  
    $uuid .= substr($chars,16,4) . '-';  
    $uuid .= substr($chars,20,12);  
    return $prefix . $uuid;  
  }  


	
}