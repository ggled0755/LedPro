<?php
// 本类由系统自动生成，仅供测试用途
abstract class EasyUITableAction extends BaseAction 
{
    abstract protected function GetModel();
    
    public function Validator($obj)
    {
        $this->LogWarn("the validator is empty ");
        return SUCCESS;
    }
    private function  GetObj()
    {
        $Req = $this->GetReqObj();
        $obj = $Req->obj;
        return $obj;
    }
    private function  GetObjArray()
    {
    	$Req = $this->GetReqObj();
    	$obj = $Req->obj;
    	return $this->objectToArray($obj);
    }
    public function GetWebPage()
    {
        if(null == $_POST['page'])
        {
            $page['currentPage'] = 0;
            $this->LogWarn("the crrent page is empty");
        }
        else
        {
            $page['currentPage'] = $_POST['page']-1;
        } 
        if(null == $_POST['rows'])
        {
            $page['pageSize'] = 10;
            $this->LogWarn("the page size is empty");
        }
        else
        {
            $page['pageSize'] = $_POST['rows'];
        }
        $this->LogInfo("the current page is ".$page['currentPage'].",the page size is ".$page['pageSize']);
        return $page;
    }
    //获取后台Load查询条件
    public function LoadCondition()
    {
    	return $this->GetCondition();
    }
    //获取APP Load查询条件
    Public function APPLoadCondition()
    {
    	return $this->GetCondition();
    }
    //加载datagrid数据
    public function Load()
    {
        $db =  $this->GetModel();
        $this->LoadPageTable($db);
    }
    public function LoadPageTable($model)
    {
    	//$condition =  array();
    	$ReqType = $this->GetReqType();
    	$this->LogInfo("LoadPageTable,ReqType is ".$ReqType);
    	if(($ReqType == ClientTypeEnum::IOS)||($ReqType == ClientTypeEnum::ANDROID))
    	{
    		//客户端查询条件
    		$condition = $this->APPLoadCondition();
    		$this->LogInfo("condition list is ".json_encode($condition));
    		if(null == $condition['order'])
    		{
    			//加入默认排序条件
    			$order = array();
    			$field = $model->getDbFields();
    			if("id" != $model->getPk()){
    				$order[$model->getPk()] = 'desc';
    			}
    			else{
    				$order[$field[0]] = 'desc';
    				$this->LogInfo("Loading view.. no pk is found.");
    			}
    			$condition['order'] = $order;
    		}
    		//获取分页信息
    		$page = $this->GetAppPage();
    		$condition['limit']['index'] = $page['currentPage']*$page['pageSize'];
    		$condition['limit']['pageSize'] = $page['pageSize'];
    		$this->LogInfo("condition list is ".json_encode($condition));
    		
    		//获取数据
    		$count = $model->where($condition['condition'])->count();
    		$rows = MispCommonService::GetPageData($model, $condition);
    		if(0 == $count)
    		{
    			$rows = array();
    		}
    		$this->LogInfo("query the table ".$model->tableName." count is ".$count);
    		$this->rsp->obj->total = $count;
    		$this->rsp->obj->rows = $rows;
    		$this->ReturnJson();
    	}
    	else
    	{
    		//后台查询条件
    		$condition = $this->LoadCondition();
    		if(null == $condition['order'])
    		{
    			//加入默认排序条件
    			$order = array();
    			$field = $model->getDbFields();
    			if("id" != $model->getPk()){
    				$order[$model->getPk()] = 'desc';
    			}
    			else{
    				$order[$field[0]] = 'desc';
    				$this->LogInfo("Loading view.. no pk is found.");
    			}
    			$condition['order'] = $order;
    		}
    		//获取分页信息
    		$page = $this->GetWebPage();
    		$condition['limit']['index'] = $page['currentPage']*$page['pageSize'];
    		$condition['limit']['pageSize'] = $page['pageSize'];
    		$this->LogInfo("condition is ".json_encode($condition));
    		
    		//获取数据
    		$count = $model->where($condition['condition'])->count();
    		$rows = MispCommonService::GetPageData($model, $condition);
    		if(0 == $count)
    		{
    			$rows = array();
    		}
    		$this->LogInfo("query the table ".$model->tableName." count is ".$count);
    		$data['total'] = $count;
    		$data['rows'] = $rows;
    		$this->ReturnJson($data);
    	}
    	
    }
    //获取后台LoadList查询条件
    public function LoadListCondition()
    {
    	return $this->GetCondition();
    }
    //获取APP LoadList查询条件
    Public function APPLoadListCondition()
    {
    	return $this->GetCondition();
    }
    //加载对象list，tree commbox
    public function LoadList()
    {
    	$db = $this->GetModel();
    	$this->LoadListModel($db);
    }
    public function LoadListModel($model)
    {
    	$condition =  array();
    	$ReqType = $this->GetReqType();
    	$this->LogInfo("LoadList,ReqType is ".$ReqType);
    	$this->LogInfo("Load List ".$model->getTableName()."...");
    	if(($ReqType == ClientTypeEnum::IOS)||($ReqType == ClientTypeEnum::ANDROID))
    	{
    		//客户端查询条件
    		$condition = $this->APPLoadListCondition();
    		$this->LogInfo("condition list is ".json_encode($condition));
    		$objectList = MispCommonService::GetAll($model,$condition);
    		$this->LogInfo($model->getTableName()." list is ".json_encode($objectList));
    		$this->rsp->obj = $objectList;
    		$this->ReturnJson();
    	}
    	else
    	{
    		//后台查询条件
    		$condition = $this->LoadListCondition();
    		$this->LogInfo("condition is ".json_encode($condition));
    		$objectList = MispCommonService::GetAll($model,$condition);
    		$this->LogInfo($model->getTableName()." list is ".json_encode($objectList));
    		$this->ReturnJson($objectList);
    	}
    }
    public function LoadAll()
    {
    	$db = $this->GetModel();
    	$this->LoadAllModel($db);
    }
    //获取后台LoadAll查询条件
    public function LoadAllCondition()
    {
    	return $this->GetCondition();
    }
    //获取APP LoadAll查询条件
    Public function APPLoadAllCondition()
    {
    	return $this->GetCondition();
    }
    public function LoadAllModel($model)
    {
    	$condition =  array();
    	$ReqType = $this->GetReqType();
    	$this->LogInfo("LoadAll,ReqType is ".$ReqType);
    	$this->LogInfo("Load all ".$model->getTableName()."...");
    	if(($ReqType == ClientTypeEnum::IOS)||($ReqType == ClientTypeEnum::ANDROID))
    	{
    		$condition = $this->APPLoadAllCondition();
    	}
    	else 
    	{
    		$condition = $this->LoadAllCondition();
    	}
    	$this->LogInfo("Condition is ".json_encode($condition));
        try
        {
        	$objectList = MispCommonService::GetAll($model,$condition);
        }
        catch(FuegoException $e)
        {
        	$this->rsp->errorCode = $e->getCode();
        	$this->ReturnJson();
        	return;
        }
        $this->rsp->obj = $objectList;
        $this->ReturnJson();  
    }
    public function Show()
    {
        $db = $this->GetModel();
        $this->ShowModel($db);
    }
    public function ShowModel($model)
    {
    	$objID = $this->GetObj();
    	$this->LogInfo("show objID is ".$objID);
    	if(null == $objID)
    	{
    		$this->rsp->obj = array();
    		$this->ReturnJson();
    		return;
    	}
    	$field = $model->getDbFields();
    	if("id" != $model->getPk()){
    		$condition[$model->getPk()] = $objID;
    	}
    	else{
    		$condition[$field[0]] = $objID;
    		$this->LogInfo("Show view.. no pk is found.");
    	}
    	try
    	{
    		$object = MispCommonService::GetUniRecord($model, $condition);
    	}
    	catch(FuegoException $e)
    	{
    		$this->rsp->errorCode = $e->getCode();
    		$this->ReturnJson();
    		return;
    	}
    	$this->rsp->obj = $object;
    	$this->ReturnJson();
    }
    public function Create()
    {
    	$db = $this->GetModel();
    	$this->CreateModel($db);
    	
    }
    //创建对象预处理函数，如果需要对前台提交的对象做一些处理，可以重载此函数
    public function CreateCallForward($obj)
    {
    }
    public function CreateModel($model)
    {
        $this->LogInfo("create ".$model->getTableName()."...");
        $obj = $this->GetCommonData();
        $this->CreateCallForward($obj);
        $result = $this->Validator($obj);
        if(SUCCESS != $result)
        {
            $this->rsp->errorCode = $result;
            $this->LogErr("validator failed when create ".$model->getTableName());
            $this->LogErr("the error is ".$result);
            $this->ReturnJson();
            return;
        }
        //创建对象
        $object = $this->objectToArray($obj);
        $this->LogInfo("Create Obj is ".json_encode($object));
        try
        {
        	$objID= MispCommonService::Create($model, $object);
        	$this->LogInfo("create ".$model->getTableName()." success.");
        }
        catch(FuegoException $e)
        {
        	$this->rsp->errorCode = $e->getCode();
        	$this->LogErr("create ".$model->getTableName()." failed.");
        	$this->ReturnJson();
        	return;
        }
        //获取对象
        $this->LogInfo("get ".$model->getTableName()." objID is ".$objID);
        $condition[$model->getPk()] = $objID;
        try
        {
        	$object = MispCommonService::GetUniRecord($model, $condition);
        	$this->LogInfo("get ".$model->getTableName()." success.");
        }
        catch(FuegoException $e)
        {
        	$this->rsp->errorCode = $e->getCode();
        	$this->ReturnJson();
        	return;
        }
        $this->rsp->obj = $object;
        $this->ReturnJson();
    }
    public function Modify()
    {
    	$db = $this->GetModel();
    	$this->ModifyModel($db);
    }
    //创建对象预处理函数，如果需要对前台提交的对象做一些处理，可以重载此函数
    public function ModifyCallForward($obj)
    {
    }
    public function ModifyModel($model)
    {
        $this->LogInfo("modify");
        $obj = $this->GetCommonData();
        $this->ModifyCallForward($obj);
        $result = $this->Validator($obj);
        if(SUCCESS != $result)
        {
            $this->rsp->errorCode = $result;
            $this->LogErr("validator failed when create ".$model->tableName);
            $this->LogErr("the error is ".$result);
            $this->ReturnJson();
            return;
        }    
        $object = $this->objectToArray($obj);
        $this->LogInfo("Object is to modify, obj is ".json_encode($object));
        try
        {
        	$result = MispCommonService::Modify($model, $object);
        }
        catch(FuegoException $e)
        {
        	$this->rsp->errorCode = $e->getCode();
        }
        $this->rsp->obj = $object;
        $this->ReturnJson();
    }
    public function DeleteList()
    {
    	$db = $this->GetModel();
    	$this->DeleteListModel($db);
    }
    public function DeleteListModel($model)
    {
        $this->LogInfo("delete obj list...");
        $idList = $this->GetCommonData();
        $map[$model->getPk()]=array('in',$idList);
        try
        {
        	$result = MispCommonService::Delete($model, $map);
        }
        catch(FuegoException $e)
        {
        	$this->rsp->errorCode = $e->getCode();
        }
        $this->rsp->errorCode = MispErrorCode::SUCCESS;
        $this->ReturnJson();
    }
    public function Delete()
    {
    	$db = $this->GetModel();
    	$this->DeleteModel($db);
    }
    public function DeleteModel($model)
    {
    	$this->LogInfo("Delete UniRecord...");
    	$id = $this->GetCommonData();
    	$condition[$model->getPk()] = $id;
    	try
    	{
    		$result = MispCommonService::Delete($model, $condition);
    	}
    	catch(FuegoException $e)
    	{
    		$this->rsp->errorCode = $e->getCode();
    	}
    	$this->rsp->errorCode = MispErrorCode::SUCCESS;
    	$this->ReturnJson();
    }
    
}