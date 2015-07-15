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
//多图片上传，上次成功函数
misp.file.imgSuccess = function (imgListElement,data){
	misp.file.addImg(imgListElement,data);
	var imgNameElement = misp.com.getElementByType("misp.imgName",imgListElement);
	var imgName = imgNameElement.textbox('getValue');
	if(imgName == ''){
		imgNameElement.textbox('setValue',data);
	}else{
		imgNameElement.textbox('setValue',imgName + "|" + data);
	}	
};
//多图片上传，上传成功后预览，绑定删除函数
misp.file.addImg = function (imgListElement,data){
	var src = misp.util.getImgUrl() + data;
    var newElement = "<li><img class='content'  src='" + src + "' style=\"width:100px;height:100px;\"><img class='button' src='"+swfUploadPath+"/images/fancy_close.png"+"'></li>";
    var imgUlElement = misp.com.getElementByType("misp.imgUl",imgListElement);
    var imgNameElement = misp.com.getElementByType("misp.imgName",imgListElement); 
    imgUlElement.append(newElement);
    $("img.button").last().bind("click", function(){
    	var src = $(this).siblings('img').attr('src');
    	var img = src.replace(misp.util.getImgUrl(),'');
    	var imgName = imgNameElement.textbox('getValue');
    	misp.util.submit({
    	  data : img,
    	  url : "Index|ImgDelete",
    	  success:function(obj){
    		  var lastImgName;
			if(imgName.indexOf("|")>0)
			{
				lastImgName = imgName.replace("|"+obj,'');
			}
			else
			{
				lastImgName = imgName.replace(obj,'');
			}
			imgNameElement.textbox('setValue',lastImgName);
    	  }
    	});
    	$(this).parent().remove();
    });   
};
//通用多图片上传，上传调用函数
//filePara.fileID 上传文件的input框ID，必填参数，如：'up0';
//filePara.fileName 文件名提交input，name值与数据库字段对应；必填参数，如： 'product_img';
misp.file.imgUpload = function(element){
	var imgListElement = $(element).parents("div[misp-class='misp.imglist']");
	var imgIDElement = misp.com.getElementByType("misp.imgID",imgListElement);
	var imgID = imgIDElement.attr("id");
	var url = misp.util.buildUrl("Index|ImgUpload");
    $.ajaxFileUpload({
        url:url,					//处理图片脚本
        secureuri :false,
        fileElementId : imgID,		//file控件id
        dataType : 'json',
        success : function (rsp, status){
        	if(misp.util.isSuccess(rsp)){
        		misp.file.imgSuccess(imgListElement,rsp.obj);
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
