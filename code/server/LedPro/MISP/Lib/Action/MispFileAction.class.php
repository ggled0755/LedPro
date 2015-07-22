<?php
// 本类由系统自动生成，仅供测试用途
class MispFileAction extends EasyUITableAction
{
    
    protected function GetModel(){
    }
    //图片上传接口，不生成缩略图
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
    //图片上传接口，生成缩略图，删除原图
    public function ThumbUpload($fileName=null)
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
    	//设置需要生成缩略图，仅对图像文件有效
    	$upload->thumb = true;
    	//设置需要生成缩略图的文件前缀
    	$upload->thumbPrefix = "";
    	$upload->thumbSuffix = SysStringEnum::SMALL_IMG_SUFFIX;
    	//设置缩略图最大宽度
    	$upload->thumbMaxWidth = 100;
    	//设置缩略图最大高度
    	$upload->thumbMaxHeight = 100;
    	//删除原图
    	$upload->thumbRemoveOrigin = true;
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
    	$imgName = $info[0]['savename'];
    	$fileNames = explode('.',$imgName);
    	$fileNameThumb = $fileNames[0].SysStringEnum::SMALL_IMG_SUFFIX.".".$fileNames[1];
    	$this->rsp->errorCode = MispErrorCode::SUCCESS;
    	$this->rsp->obj = $fileNameThumb;
    	echo json_encode($this->rsp);
    	exit;
    }
    //图片上传接口，生成缩略图，同时保存原图
    public function ImgThumbUpload($fileName=null)
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
    	//设置需要生成缩略图，仅对图像文件有效
    	$upload->thumb = true;
    	//设置需要生成缩略图的文件前缀
    	$upload->thumbPrefix = "";
    	$upload->thumbSuffix = SysStringEnum::SMALL_IMG_SUFFIX;
    	//设置缩略图最大宽度
    	$upload->thumbMaxWidth = 100;
    	//设置缩略图最大高度
    	$upload->thumbMaxHeight = 100;
    	//删除原图
    	$upload->thumbRemoveOrigin = false;
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
    			$fileNames = explode('.',$fileName);
    			$fileNameThumb = $fileNames[0].SysStringEnum::SMALL_IMG_SUFFIX.".".$fileNames[1];
    			$imgThumb = './Client/Public/Fuego/Uploads/'.$fileNameThumb;
    			if (file_exists ( $imgThumb )&& is_writable($imgThumb)) {
    				unlink ( $imgThumb );
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
    	$fileName = $this->GetCommonData();
    	$this->LogInfo("Delete img ..., img name is ".$fileName);
    	//更新图片后删除原有的图片
    	$img = './Client/Public/Fuego/Uploads/'.$fileName;
    	if (file_exists ( $img )&& is_writable($img)) {
    		unlink ( $img );
    		$this->LogInfo("Delete img success.");
    	}
    	$fileNames = explode('.',$fileName);
    	$fileNameThumb = $fileNames[0].SysStringEnum::SMALL_IMG_SUFFIX.".".$fileNames[1];
    	$imgThumb = './Client/Public/Fuego/Uploads/'.$fileNameThumb;
    	if (file_exists ( $imgThumb )&& is_writable($imgThumb)) {
    		unlink ( $imgThumb );
    	}
    	$this->rsp->obj = $fileName;
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
?>