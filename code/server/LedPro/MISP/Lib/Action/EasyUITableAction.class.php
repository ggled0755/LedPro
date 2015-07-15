<?php
// 本类由系统自动生成，仅供测试用途
import("MISP.Action.BaseAction");
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
    public function GetTableCondition()
    {
    	$searchFilter = array();
    	if(null == $_POST['filter'])
    	{
    		$this->LogWarn("Filter condition is empty");
    	}
    	else
    	{
    		$PostStr = stripslashes($_POST['filter']);
    		$this->LogInfo("Filter condition is ".$PostStr);
    		$filterList = json_decode($PostStr);
    		$searchFilter = $this->ConvertToCondition($filterList);
    	}
    	return $searchFilter;
    }
    public function GetPage()
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
    //加载datagrid数据
    public function Load()
    {
    	$order = array(); 
        $db =  $this->GetModel();
        $field = $db->getDbFields();
        if("id" != $db->getPk()){
        	$order[$db->getPk()] = 'desc';
        }
        else{
        	$order[$field[0]] = 'desc';
        	$this->LogInfo("Loading view.. no pk is found.");
        }
        $this->LoadPageTable($db,$order);
    }
    public function LoadPageTable($model,$order)
    {
    	$condition =  array();
    	$ReqType = $this->GetReqType();
    	$this->LogInfo("LoadPageTable,ReqType is ".$ReqType);
    	if(($ReqType == ClientTypeEnum::IOS)||($ReqType == ClientTypeEnum::ANDROID))
    	{
    		//客户端查询条件
    		$condition = $this->GetCondition();
    		$this->LogInfo("condition list is ".json_encode($condition));
    		//获取分页信息
    		$page = $this->GetPageData();
    		$index = $page['currentPage']*$page['pageSize'];
    		//获取数据
    		$count = $model->where($condition)->count();
    		$rows = $model->where($condition)->order($order)->limit($index,$page['pageSize'])->select();
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
    		$condition = $this->GetTableCondition();
    		$this->LogInfo("condition is ".json_encode($condition));
    		//获取分页信息
    		$page = $this->GetPage();
    		$index = $page['currentPage']*$page['pageSize'];
    		//获取数据
    		$count = $model->where($condition)->count();
    		$rows = $model->where($condition)->order($order)->limit($index,$page['pageSize'])->select();
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
    public function GetListCondition()
    {
    	$searchFilter = array();
    	if(null == $_POST['filter'])
    	{
    		$this->LogWarn("Filter condition is empty");
    	}
    	else
    	{
    		$PostStr = stripslashes($_POST['filter']);
    		$this->LogInfo("Filter condition is ".$PostStr);
    		$filterList = json_decode($PostStr);
    		$searchFilter = $this->ConvertToCondition($filterList);
    	}
    	return $searchFilter;
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
    		$condition = $this->GetCondition();
    		$this->LogInfo("condition list is ".json_encode($condition));
    		$objectList = MispCommonService::GetAll($model,$condition);
    		$this->LogInfo($model->getTableName()." list is ".json_encode($objectList));
    		$this->rsp->obj = $objectList;
    		$this->ReturnJson();
    	}
    	else
    	{
    		//后台查询条件
    		$condition = $this->GetListCondition();
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
    public function GetAllCondition()
    {
    	$condition = array();
    	return $condition;
    }
    public function LoadAllModel($model)
    {
    	$this->LogInfo("Load all ".$model->getTableName()."...");
    	$condition = $this->GetAllCondition();
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
    //上传图片
    public function ImgUpload($fileName=null)
    {
    	$this->LogInfo("Upload image...");
    	//导入图片上传类
    	import("ORG.Net.UploadFile");
    	//实例化上传类
    	$upload = new UploadFile();
    	$upload->maxSize = 4145728;		//138px*152px
    	//设置文件上传类型
    	$upload->allowExts = array('jpg','gif','png','jpeg');
    	//设置文件上传位置
    	$upload->savePath = "./Client/Public/Fuego/Uploads/";	//根目录下的Public文件夹
    	//设置文件上传名(按照时间)
    	//$upload->saveRule = "time";
    	if (!$upload->upload()){
    		//上传图片错误
    		$this->LogWarn("Upload image failed.".$upload->getErrorMsg());
    		$this->rsp->errorCode =MispErrorCode::UPLOAD_IMG_FAILED;
    		$this->rsp->errorMsg = $upload->getErrorMsg();
    		echo json_encode($this->rsp);
    		return;
    	}else{
    		//上传成功，获取上传信息
    		$this->LogInfo("Upload image success.");
    		$info = $upload->getUploadFileInfo();
    		//删除原有图片
    		if(null != $fileName)
    		{
    			$this->LogInfo("Delete old file, old file name is ".$fileName);
    			$img = './Client/Public/Fuego/Uploads/'.$fileName;
    			if (file_exists ( $img )&& is_writable($img)) {
    				unlink ( $img );
    			}
    		}
    	}
    	$this->rsp->errorCode = MispErrorCode::SUCCESS;
    	$this->rsp->obj = $info[0]['savename'];
    	echo json_encode($this->rsp);
    	exit;
    }
    //删除图片
    public function ImgDelete()
    {
    	$req = $this->GetCommonData();
    	$this->LogInfo("Delete img ..., img name is ".$req);
		//更新图片后删除原有的图片
		$img = './Client/Public/Fuego/Uploads/'.$req;
		if (file_exists ( $img )&& is_writable($img)) {
			unlink ( $img );
			$this->LogInfo("Delete img success.");
		}
		$this->rsp->obj = $req;
		$this->ReturnJson();
    }
    //上传音频
    public function VoiceUpload()
    {
    	$this->LogInfo("Upload voice...");
    	//导入图片上传类
    	import("ORG.Net.UploadFile");
    	//实例化上传类
    	$upload = new UploadFile();
    	//$upload->maxSize = 4145728;		//138px*152px
    	//设置文件上传类型
    	$upload->allowExts = array('mp3','MP3');
    	ini_set('post_max_size', '100M');
    	//设置文件上传位置
    	$upload->savePath = "./Client/Public/Fuego/Uploads/";	//根目录下的Public文件夹
    	//设置文件上传名(按照时间)
    	//$upload->saveRule = "time";
    	if (!$upload->upload()){
    		//上传图片错误
    		$this->LogWarn("Upload voice failed.".$upload->getErrorMsg());
    		$this->rsp->errorCode =MispErrorCode::UPLOAD_IMG_FAILED;
    		$this->rsp->errorMsg = $upload->getErrorMsg();
    		echo json_encode($this->rsp);
    		return;
    	}else{
    		//上传成功，获取上传信息
    		$this->LogInfo("Upload voice success.");
    		$info = $upload->getUploadFileInfo();
    	}
    	$this->rsp->errorCode = MispErrorCode::SUCCESS;
    	$this->rsp->obj = $info[0]['savename'];
    	echo json_encode($this->rsp);
    	exit;
    }
    
}