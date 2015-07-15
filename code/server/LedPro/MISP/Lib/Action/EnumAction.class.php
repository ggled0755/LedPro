<?php
// 本类由系统自动生成，仅供测试用途
class EnumAction extends EasyUITableAction 
{
	/* (non-PHPdoc)
	 * @see EasyUITableAction::GetTableName()
	 */
	protected function GetModel()
	{
		return MispDaoContext::Enum();
	}
	//加载前台搜索combobox
	public function LoadSearchList()
	{
		$model = $this->GetModel();
		$condition = $this->GetListCondition();
		$this->LogInfo("condition is ".json_encode($condition));
		$defaultItem = array('enum_key'=>'','enum_value'=>'请选择...');
		$defaultList = array();
		array_push($defaultList, $defaultItem);
		$objectList = MispCommonService::GetAll($model,$condition);
		$enumList = array_merge($defaultList, $objectList);
		$this->LogInfo($model->getTableName()." list is ".json_encode($enumList));
		$this->ReturnJson($enumList);
	}
}
?>