<?php
// 本类由系统自动生成，仅供测试用途
class RoleAction extends EasyUITableAction 
{
	/* (non-PHPdoc)
	 * @see EasyUITableAction::GetTableName()
	 */
	protected function GetModel()
	{
		return MispDaoContext::SystemRole();	
	}
	public function LoadSearchList()
	{
		$model = $this->GetModel();
		$condition = $this->GetListCondition();
		$this->LogInfo("condition is ".json_encode($condition));
		$defaultItem = array('role_id'=>'','role_name'=>'请选择...');
		$defaultList = array();
		array_push($defaultList, $defaultItem);
		$objectList = MispCommonService::GetAll($model,$condition);
		$enumList = array_merge($defaultList, $objectList);
		$this->LogInfo($model->getTableName()." list is ".json_encode($enumList));
		$this->ReturnJson($enumList);
	}
}
?>