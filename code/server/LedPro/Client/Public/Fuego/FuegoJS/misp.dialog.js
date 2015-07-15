var misp = misp || {};
misp.dialog = {};

misp.dialog.log  = function (message,obj)
{
	var logged = false;
	if(logged)
	{
		misp.util.log(message,obj,"misp.dialog");

	}	
};
misp.dialog.load = function (element,data) 
{

	 misp.dialog.log("now load dialog",data);
	 var domObj = misp.com.getDomObj(element);
	 if(null == domObj)
	 {
		 misp.dialog.log("the element is empty");
		 return;
	 }
	 if(null == data)
	 {
		 data = misp.com.getOptions(domObj);
		 misp.table.log("the data is empty",data);
	 }
		
	 if(null == data)
	 {
		 misp.table.log("the data is empty");
		 data = new Object();
	}	
	 data.width = data.width || 600;
	 data.height = data.height || 400;
	 data.closed= false;
	 data.cache= false;
	 data.modal= true;
	 if(undefined != data.href)
	 {
		 data.href = misp.util.buildUrl(data.href);
	 }
	 //打开dialog对话框
	 misp.dialog.log("dialog data is ",data);
	 domObj.dialog(data);
	 domObj.dialog('close');
};

//通用打开普通对话框方法
//dialogPara.pageID 打开dialog的divID，必填参数，如： 'modifyPwd';
//dialogPara.width 打开dialog的宽度，选填参数，默认为 600;
//dialogPara.height 打开dialog的高度，选填参数，默认为 400;
//dialogPara.pageAddr 打开dialog的内容页面地址，必填参数，如：'Index/ModifyPwd.html';
//dialogPara.title 打开dialog的标题，必填参数，如：'员工信息';
misp.dialog.open = function (dialogPara) {

    if (null == dialogPara.width) {
        dialogPara.width = 600;
    }

    if (null == dialogPara.height) {
        dialogPara.height = 400;
    }
    //打开用户编辑窗口
    $(dialogPara.pageID).dialog({
        title: dialogPara.title,
        width: dialogPara.width,
        height: dialogPara.height,
        closed: false,
        cache: false,
        href: misp.util.buildPageUrl(dialogPara.pageAddr),
        modal: true
    });
}
//通用form提交方法
//formPara.formID 页面form的ID，选填参数，默认为'mispForm';
//formPara.url 加载对象信息的url，必填参数，如：'Index|modify';
//formPara.callback 提交成功返回后的回调函数，必填参数，如：'callbackFunction';
misp.dialog.formSubmit = function (formPara) {

    //页面form的ID，选填参数，默认为"#mispForm";
    formPara.formID = formPara.formID || "#mispForm";

    if (null == formPara.callback) {
        formPara.callback = function callback(obj) {
        };
    }
    
    //调用fuegoAjax请求
    misp.util.submit({
    	data:formPara.formID,
    	url:formPara.url,
    	success:function(obj){
    		formPara.callback(obj);
  	  }
    });
}
//通用object提交方法
//formPara.callbackFunction 提交成功返回后的回调函数，选填参数，默认为空;
//formPara.obj 需要提交的对象，必填参数，如：'obj';
//formPara.methodName 加载对象信息的方法名，必填参数，如： 'Login';
//formPara.actionName 加载对象信息的Controller名，必填参数，如：'IndexManage';
misp.dialog.objSubmit = function (formPara) {

    if (null == formPara.callback) {
        formPara.callback = function callback(obj) {
        };
    }
    //alert(JSON.stringify(formPara));
    
    //obj对象转换成json字符串
    var json = misp.util.objToJson(formPara.obj);
    //alert(json);
    //调用fuegoAjax请求
    misp.util.fuegoAjax({
        url: misp.util.buildActionUrl(formPara.actionName, formPara.methodName),
        data: json,
        success: function (rsp) {
            if (misp.util.isSuccess(rsp)) {
                formPara.callback(rsp.obj);
            }
            else {
                misp.util.alert(rsp.errorMsg);
            }
        }
    });
};