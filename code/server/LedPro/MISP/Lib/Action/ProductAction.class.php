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
	 * @see EasyUITableAction::LoadCondition()
	 */
// 	public function LoadCondition()
// 	{
// 		$searchCondition = array();
// 		//获取查询条件
// 		$tableCondition['product_id'] = "2";
// 		$this->LogInfo("tableCondition is ".json_encode($tableCondition));
// 		//前台查询条件
// 		$filterCondition = parent::LoadCondition();
// 		if(null != $filterCondition['condition'])
// 		{
// 			$searchCondition = $filterCondition['condition'];
// 		}
// 		$filterCondition['condition'] = array_merge($tableCondition, $searchCondition);
// 		$this->LogInfo("LoadCondition filterCondition is ".json_encode($filterCondition));
// 		return $filterCondition;	
// 	}
	public function CreatePdf()
	{
		Vendor("TCPdf.tcpdf");
		//实例化
		$pdf = new TCPDF('P', 'mm', 'A4', true, 'UTF-8', false);
		
		// 设置文档信息
		$pdf->SetCreator('Helloweba');
		$pdf->SetAuthor('yueguangguang');
		$pdf->SetTitle('Welcome to helloweba.com!');
		$pdf->SetSubject('TCPDF Tutorial');
		$pdf->SetKeywords('TCPDF, PDF, PHP');
		
		// 设置页眉和页脚信息
		$pdf->SetHeaderData('', 0, '','Product Facts Project',
				array(0,64,255), array(0,64,128));
		$pdf->setFooterData(array(0,64,0), array(0,64,128));
		
		// 设置页眉和页脚字体
		$pdf->setHeaderFont(Array('timesb', '', '18'));
		$pdf->setFooterFont(Array('helvetica', '', '8'));
		
		// 设置默认等宽字体
		$pdf->SetDefaultMonospacedFont('courier');
		
		// 设置间距
		$pdf->SetMargins(15, 27, 15);
		$pdf->SetHeaderMargin(5);
		$pdf->SetFooterMargin(10);
		
		// 设置分页
		$pdf->SetAutoPageBreak(TRUE, 25);
		
		// set image scale factor
		$pdf->setImageScale(1.25);
		
		// set default font subsetting mode
		$pdf->setFontSubsetting(true);
		

		$pdf->AddPage();		//创建一页pdf
		//项目名称
		$pdf->SetFont('timesb', '', 18);		//设置字体
		$projectName = 'Project 111';
		$pdf->MultiCell(0, 0, $projectName, 0, 'C', false, '', '', '', true, 0, false, true, 0, '', false);
		//Notes
		$this->CreateTitle($pdf,'Notes:');
		$this->CreateDetail($pdf, 'Please enter project notes here.');
		//Summary
		$this->CreateTitle($pdf,'Summary:');
		$this->CreateDetail($pdf, '5/14   Products',100);
		$this->CreateDetail($pdf, '1000   Watt/Month',100);
		$this->CreateDetail($pdf, '$200   Total cost of ownership TCO',100);
		//$pdf->Write(0,$projectName,'', 0, 'L', true, 0, false, false, 0);
		$pdf->Image('./Client/Public/Fuego/img/ic_launcher.jpg', 140, 62, 45, 30, 'JPG', '', '', false, 150);
		//Subfolder
		$this->CreateTitle($pdf,'Subfolder:');
	
		//输出PDF
		//默认是I：在浏览器中打开，D：下载，F：在服务器生成pdf ，S：只返回pdf的字符串
		//这里的路径必须是绝对路径
		$pdf->Output('D:\xampps\htdocs\LedPro\Client\Public\Fuego\Uploads\t11.pdf', 'I');
	}
	private function CreateTitle($pdf,$text)
	{
		$pdf->Ln('', false);
		$pdf->Ln(5, false);
		$pdf->SetFont('timesb', '', 14);
		$pdf->MultiCell(0, 0, $text, 0, 'L', false, '', '', '', true, 0, false, true, 0, '', false);
		
	}
	private function CreateDetail($pdf,$text,$w=0,$h=0)
	{
		$pdf->Ln('', false);
		$pdf->Ln(2, false);
		$pdf->SetFont('timesb', '', 11);
		$pdf->MultiCell($w, $h, $text, 0, 'L', false, '', '', '', true, 0, false, true, 0, '', false);
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