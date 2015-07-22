<?php
import("MISP.Util.FuegoLog");
import("MISP.Constant.MispErrorCode");
import("MISP.Util.FuegoException");
class MispCommonService
{
	static function Create($model,$object)
	{
		$errorCode = MispErrorCode::SUCCESS;
		$result = $model->add($object);			//$result获取到的是新创建对象的ID
		if(false == $result)
		{
			FuegoLog::getLog()->LogErr("create data failed.the table is ".$model->getTableName());
			$errorCode = MispErrorCode::DB_CREATE_ERROR;
			throw new FuegoException(null,$errorCode);
		}
		return $result;
	}
	static function CreateList($model,$objList)
	{
		$errorCode = MispErrorCode::SUCCESS;
		$result = $model->addAll($objList);			//$result获取到的是新创建对象的ID
		if(false == $result)
		{
			FuegoLog::getLog()->LogErr("create data failed.the table is ".$model->getTableName());
			$errorCode = MispErrorCode::DB_CREATE_ERROR;
			throw new FuegoException(null,$errorCode);
		}
		return $result;
	
	}
	static function Modify($model,$object)
	{
		$errorCode = MispErrorCode::SUCCESS;
		$result = $model->save($object);
		if(false === $result)
		{
			FuegoLog::getLog()->LogErr("modify data failed.the table is ".$model->getTableName());
			$errorCode = MispErrorCode::DB_MODIFY_ERROR;
			throw new FuegoException(null,$errorCode);
		}
		return $result;
	}
	static function ModifyField($model,$ID,$field,$value)
	{
		$errorCode = MispErrorCode::SUCCESS;
		$condition[$model->getPk()]=$ID;
		$result = $model->where($condition)->setField($field,$value);
		if(false === $result)
		{
			FuegoLog::getLog()->LogErr("modify field data failed.the table is ".$model->getTableName());
			$errorCode = MispErrorCode::DB_MODIFY_ERROR;
		}
		return $errorCode;
	}
	static function ModifyListField($model,$IDList,$field,$value)
	{
		$errorCode = MispErrorCode::SUCCESS;
		$map[$model->getPk()]=array('in',$IDList);
		$result = $model->where($map)->setField($field,$value);
		if(false === $result)
		{
			FuegoLog::getLog()->LogErr("modify field data failed.the table is ".$model->getTableName());
			$errorCode = MispErrorCode::DB_MODIFY_ERROR;
		}
		return $errorCode;
	}
	static function Delete($model,$condition)
	{
		$errorCode = MispErrorCode::SUCCESS;
		$result = $model->where($condition)->delete();
		if(false === $result)
		{
			FuegoLog::getLog()->LogErr("delete data failed.the table is ".$model->getTableName());
			$errorCode = MispErrorCode::DB_DELETE_ERROR;
			throw new FuegoException(null,$errorCode);
		}
		return $result;
	}
	static function GetAll($model,$condition)
	{
		$errorCode = MispErrorCode::SUCCESS;
		$result = $model->where($condition)->select();
		if(false === $result)
		{
			FuegoLog::getLog()->LogErr("get data failed.the table is ".$model->getTableName());
			$errorCode = MispErrorCode::DB_GET_ERROR;
			throw new FuegoException(null,$errorCode);
		}
		return $result;
	}
	static function GetPageData($model,$condition)
	{
		$errorCode = MispErrorCode::SUCCESS;
		$result = $model->where($condition['condition'])->order($condition['order'])->limit($condition['limit']['index'],$condition['limit']['pageSize'])->select();
		if(false === $result)
		{
			FuegoLog::getLog()->LogErr("get page data failed.the table is ".$model->getTableName());
			$errorCode = MispErrorCode::DB_GET_ERROR;
			throw new FuegoException(null,$errorCode);
		}
		return $result;
	}
	static function GetUniRecord($model,$condition)
	{
		$errorCode = MispErrorCode::SUCCESS;
		$result = $model->where($condition)->find();
		if(false === $result)
		{
			FuegoLog::getLog()->LogErr("get data failed.the table is ".$model->getTableName());
			$errorCode = MispErrorCode::DB_GET_ERROR;
			throw new FuegoException(null,$errorCode);
		}
		return $result;
	}
	static function GetField($model,$condition,$field)
	{
		$errorCode = MispErrorCode::SUCCESS;
		$result = $model->where($condition)->getField($field);
		if(false === $result)
		{
			FuegoLog::getLog()->LogErr("get data failed.the table is ".$model->getTableName());
			$errorCode = MispErrorCode::DB_GET_ERROR;
			throw new FuegoException(null,$errorCode);
		}
		return $result;
	}
	static function GetFieldList($model,$condition,$field)
	{
		$errorCode = MispErrorCode::SUCCESS;
		$result = $model->where($condition)->getField($field,true);
		if(false === $result)
		{
			FuegoLog::getLog()->LogErr("get data failed.the table is ".$model->getTableName());
			$errorCode = MispErrorCode::DB_GET_ERROR;
			throw new FuegoException(null,$errorCode);
		}
		return $result;
	}
	
}

?>