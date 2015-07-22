var misp = misp || {};
misp.file = {};
var swfUploadPath = misp.util.rootUrl() + "/Client/Public/ThirdParty/ImgUpload";
// misp.file 打印函数，默认设置为false，不打印信息
misp.file.log  = function (message,obj)
{
	var logged = false;
	if(logged)
	{
		if(null == obj)
		{
			obj = "";
		}	
		misp.util.log(message,obj,"misp.file");

	}	 
};
//扩展JS Array remove 方法
Array.prototype.remove = function(val) {  
    var index = this.indexOf(val);  
    if (index > -1) {  
        this.splice(index, 1);  
    }  
}; 
//多图片上传，上传成功函数
misp.file.imgListSuccess = function (imgListElement,data){
	//上传图片预览
	misp.file.addImg(imgListElement,data);
	//修改正常图片名称列表
	var imgNameElement = misp.com.getElementByType("misp.imgName",imgListElement);
	var imgNameJson = imgNameElement.textbox('getValue');
	var imgNameList = [];
	if("" != imgNameJson)
	{
		imgNameList = misp.util.jsonToObj(imgNameJson);
	}
	imgNameList.push(data);
	imgNameJson = misp.util.objToJson(imgNameList);
	imgNameElement.textbox('setValue',imgNameJson);
	//修改缩略图名称列表
	var imgThumbElement = misp.com.getElementByType("misp.imgThumbName",imgListElement);
	if(null != imgThumbElement)
	{
		var imgThumbJson = imgThumbElement.textbox('getValue');
		var imgThumbList = [];
		if("" != imgThumbJson)
		{
			imgThumbList = misp.util.jsonToObj(imgThumbJson);
		}
		var imgNames = data.split(".");
		var thumbData = imgNames[0] + "_small." + imgNames[1];
		imgThumbList.push(thumbData);
		imgThumbJson = misp.util.objToJson(imgThumbList);
		imgThumbElement.textbox('setValue',imgThumbJson);
	}
};
//多图片上传，上传成功后预览，绑定删除函数
misp.file.addImg = function (imgListElement,data){
	//上传图片预览
	var src = misp.util.getImgUrl() + data;
    var newElement = "<li style=\"height:110px;\"><img class='content'  src='" + src + "' style=\"width:100px;height:100px;\"><img class='button' src='"+swfUploadPath+"/images/fancy_close.png"+"'></li>";
    var imgUlElement = misp.com.getElementByType("misp.imgUl",imgListElement);
    imgUlElement.append(newElement);
    //原图对象
    var imgNameElement = misp.com.getElementByType("misp.imgName",imgListElement);
    //缩略图对象
    var imgThumbElement = misp.com.getElementByType("misp.imgThumbName",imgListElement);
    $("img.button").last().bind("click", function(){
    	//截取图片名称提交后台删除
    	var src = $(this).siblings('img').attr('src');
    	var img = src.replace(misp.util.getImgUrl(),'');
    	//获取原图名称字符串
    	var imgNameJson = imgNameElement.textbox('getValue');
    	var imgNameList = [];
    	if("" != imgNameJson)
    	{
    		imgNameList = misp.util.jsonToObj(imgNameJson);
    	}
    	misp.util.submit({
    	  data : img,
    	  url : "MispFile|ImgDelete",
    	  success:function(obj){
    		  //删除原图字符串中对应的图片名称
    		  imgNameList.remove(obj);
    		  imgNameJson = misp.util.objToJson(imgNameList);
    		  imgNameElement.textbox('setValue',imgNameJson);
    		  //删除缩略图字符串中对应的图片名称
    		  if(null != imgThumbElement)
    		  {
    			  var imgThumbJson = imgThumbElement.textbox('getValue');
    			  var imgThumbList = [];
    			  if("" != imgThumbJson)
    			  {
    				  imgThumbList = misp.util.jsonToObj(imgThumbJson);
    			  }
    			  var imgNames = obj.split(".");
    			  var thumbObj = imgNames[0] + "_small." + imgNames[1];
    			  imgThumbList.remove(thumbObj);
    			  imgThumbJson = misp.util.objToJson(imgThumbList);
    			  imgThumbElement.textbox('setValue',imgThumbJson);
    		  }
    	  }
    	});
    	$(this).parent().remove();
    });   
};
//通用多图片上传，上传调用函数
misp.file.uploadImgList = function(element){
	var imgListElement = $(element).parents("div[misp-class='misp.imglist']");
	var imgIDElement = misp.com.getElementByType("misp.imgID",imgListElement);
	var imgThumbElement = misp.com.getElementByType("misp.imgThumbName",imgListElement);
	var url;
	if(null != imgThumbElement)
	{
		//带有缩略图的上传
		url = misp.util.buildUrl("MispFile|ImgThumbUpload");
	}
	else
	{
		//不带缩略图的上传
		url = misp.util.buildUrl("MispFile|ImgUpload");
	}
	var imgID = imgIDElement.attr("id");
    $.ajaxFileUpload({
        url:url,					//处理图片脚本
        secureuri :false,
        fileElementId : imgID,		//file控件id
        dataType : 'json',
        success : function (rsp, status){
        	if(misp.util.isSuccess(rsp)){
        		misp.file.imgListSuccess(imgListElement,rsp.obj);
        	}
        	else{
        		misp.util.alert(rsp.errorMsg);
        	}
        },
        error: function(rsp, status, e){
        	misp.util.alert(e);
        }
	})
};
//通用单图片上传，上传调用函数
misp.file.uploadImg = function(element){
	var imgElement = $(element).parents("div[misp-class='misp.img']");
	var data = misp.com.getOptions(imgElement);
	var imgIDElement = misp.com.getElementByType("misp.imgID",imgElement);
	var imgDispElement = misp.com.getElementByType("misp.imgDisp",imgElement);
	var imgNameElement = misp.com.getElementByType("misp.imgName",imgElement);
	var imgNewElement = misp.com.getElementByType("misp.imgNewName",imgElement);
	var imgThumbElement = misp.com.getElementByType("misp.imgThumbName",imgElement);
	var imgNewName = imgNewElement.textbox('getValue');
	var url;
	if("thumb" == data['type'])
	{
		//仅缩略图上传
		url = misp.util.buildUrl("MispFile|ThumbUpload") + "?fileName=" + imgNewName;
	}
	else if("imgthumb" == data['type'])
	{
		//带缩略图上传
		url = misp.util.buildUrl("MispFile|ImgThumbUpload") + "?fileName=" + imgNewName;;
	}
	else
	{
		//原图上传
		url = misp.util.buildUrl("MispFile|ImgUpload") + "?fileName=" + imgNewName;;
	}
	var imgID = imgIDElement.attr("id");
    $.ajaxFileUpload({
        url:url,					//处理图片脚本
        secureuri :false,
        fileElementId : imgID,		//file控件id
        dataType : 'json',
        success : function (rsp, status){
        	if(misp.util.isSuccess(rsp)){
        		imgNameElement.textbox('setValue',rsp.obj);
        		imgNewElement.textbox('setValue',rsp.obj);
        		imgDispElement.attr('src', misp.util.getImgUrl() + rsp.obj);
        		if(null != imgThumbElement)
    			{
        			var imgNames = rsp.obj.split(".");
        			var thumbData = imgNames[0] + "_small." + imgNames[1];
        			imgThumbElement.textbox('setValue',thumbData);
    			}
        	}
        	else{
        		misp.util.alert(rsp.errorMsg);
        	}
        },
        error: function(rsp, status, e){
        	misp.util.alert(e);
        }
	})
};
