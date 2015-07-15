<?php
// 本类由系统自动生成，仅供测试用途
class ProductAction extends EasyUITableAction 
{
	/* (non-PHPdoc)
	 * @see EasyUITableAction::GetTableName()
	 */
	protected function GetModel()
	{
		return AdminDaoContext::Product();
	}
	
	/* (non-PHPdoc)
	 * @see EasyUITableAction::CreateCallForward()
	 */
	public function CreateCallForward($obj)
	{
		$obj->product_score = 5.0;	
	}

	/* (non-PHPdoc)
	 * @see EasyUITableAction::Modify()
	 */
	public function Modify()
	{
		$model = $this->GetModel();
		$req = $this->GetCommonData();
		$condition[$model->getPk()] = $req->product_id;
		//获取对象
		try
		{
			$product = MispCommonService::GetUniRecord($model, $condition);
		}
		catch(FuegoException $e)
		{
			$this->rsp->errorCode = $e->getCode();
			$this->ReturnJson();
			return;
		}
		if($product['product_img_1'] != $req->product_img_1)
		{
			//更新图片后删除原有的图片
			$img = './Client/Public/Fuego/Uploads/'.$product['product_img_1'];
			if (file_exists ( $img )&& is_writable($img)) {
				unlink ( $img );
			}
		}
		parent::Modify();
	}
	/* (non-PHPdoc)
	 * @see EasyUITableAction::Delete()
	 */
	public function Delete()
	{
		$model = $this->GetModel();
		$IDList = $this->GetCommonData();
		$map[$model->getPk()]=array('in',$IDList);
		//获取对象
		try
		{
			$object = MispCommonService::GetUniRecord($model, $map);
		}
		catch(FuegoException $e)
		{
			$this->rsp->errorCode = $e->getCode();
			$this->ReturnJson();
			return;
		}
		//更新图片后删除原有的图片
		$img = './Client/Public/Fuego/Uploads/'.$object['product_img_1'];
		if (file_exists ( $img )&& is_writable($img)) {
			unlink ( $img );
		}
		//删除对象
        try
        {
        	$result = MispCommonService::Delete($model, $map);
        }
        catch(FuegoException $e)
        {
        	$this->rsp->errorCode = $e->getCode();
        }
        $this->ReturnJson();
	}
}
?>