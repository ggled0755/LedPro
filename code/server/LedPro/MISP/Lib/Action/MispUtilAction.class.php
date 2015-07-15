<?php
// 本类由系统自动生成，仅供测试用途
class MispUtilAction extends EasyUITableAction
{
    
    protected function GetModel(){
    }
    /**
     * 发送注册验证码
     */
    public function SendVerifyCode(){
    	$this->LogInfo("Send verify code...");
    	$obj = $this->GetCommonData();
    	//发送验证码
    	$phoneNum = $obj;
    	$content = ShortMessage::getRandNum(4);
    	$text1 = "您的验证码为：";
    	$text2 = "，您正在使用宅客帮帮手机客户端，请在10分钟内验证。";
    	$message = $text1.$content.$text2."【快客洗涤】";
    	$result = ShortMessage::SendMessage($phoneNum,$message);
    	$xmlObj = simplexml_load_string($result);
    	if($xmlObj->DESCRIPTION == "SUCCESS")
    	{
    		$this->LogInfo("Send verify code success, verify code is ".$content);
    		$this->rsp->errorCode = MispErrorCode::SUCCESS;
    		$this->rsp->obj = $content;
    		$this->ReturnJson();
    		return;
    	}
    	else
    	{
    		$this->LogInfo("Send verify code failed.");
    		$this->rsp->errorCode = SEND_MESSAGE_FAILED;
    		$this->ReturnJson();
    		return;
    	}
    }
	/**
	 * 发送短信
	 */
	public function SendMsg(){
		$this->LogInfo("Send verify code...");
		$obj = $this->GetCommonData();	
		//发送验证码
		$this->LogInfo("Obj is ".$obj);
		
		$phoneNum = $obj->phone;
		$content = $obj->content;
		$sign = $obj->sign;
		$result = ShortMessage::SendMessage($phoneNum,$content,$sign);
		$xmlObj = simplexml_load_string($result);
		$this->LogInfo("is ".$result);
		if($xmlObj->RetCode == "Sucess")
		{
			$this->LogInfo("Send verify code success, verify code is ".$content);
			$data['obj'] = $content;
			$this->ReturnJson($data);
			return;
		}
		else
		{
			$this->LogInfo("Send verify code failed.");
			$this->errorCode = SEND_MESSAGE_FAILED;
			$this->ReturnJson();
			return;
		}
	}
}
?>