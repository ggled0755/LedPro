var misp = misp || {};
misp.util = {};
misp.req = misp.req || {};
misp.req.client_type = "WEB";
misp.temp =  misp.temp || {};
//不同平台地址拼写方式不一样，在平台间移植时需要更改
misp.util.platform = "PHP";  //"JAVA"或 "PHP"

misp.util.log  = function (message,obj,location)
{
	try
	{
		if(null != location)
		{
			alert(JSON.stringify(message)+":"+JSON.stringify(obj) + ",the location is " + location);
		}	
		else if(null != obj)
		{
			alert(JSON.stringify(message)+":"+JSON.stringify(obj));
		}
		else
		{
			alert(JSON.stringify(message));
		}
	}
	catch(e)
	{
		misp.table.log("log error,can not convert",e);
	}
};
misp.util.rootUrl = function()
{
    //获取当前网址，如： http://localhost:8080/Tmall/index.jsp 
    var curWwwPath = window.document.location.href;

    //获取主机地址之后的目录如：/Tmall/index.jsp 
    var pathName = window.document.location.pathname;
    var pos = curWwwPath.indexOf(pathName);

    //获取主机地址，如： http://localhost:8080 
    var localhostPath = curWwwPath.substring(0, pos);
    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    //获取带"/"的项目名，如：/Tmall
    if ("JAVA" == misp.util.platform) {
        
        return localhostPath + projectName ;
    }
    else if ("ASP.NET" == misp.util.platform) {
        return localhostPath ;
    }
    else if ("PHP" == misp.util.platform) {
        return localhostPath + projectName;
    }
};
misp.util.buildUrl = function (url)
{
	if(url.indexOf("html")>0)
	{
		return misp.util.buildPageUrl(url);
	}
	else if(url.indexOf("|")>0)
	{
		var strs= new Array(); //定义一数组 
		strs=url.split("|");
		return misp.util.buildActionUrl(strs[0],strs[1]);
	}
	return misp.util.rootUrl() + url; 
};
misp.util.buildActionUrl = function (actionName, methodName, packageName) {
 
    //获取带"/"的项目名，如：/Tmall
    var rootUrl = misp.util.rootUrl();
    if ("JAVA" == misp.util.platform) {
         return rootUrl + "/" + actionName + "!" + methodName + ".action";
    }
    else if ("ASP.NET" == misp.util.platform) {
        return rootUrl + '/' + actionName + '/' + methodName;
    }
    else if ("PHP" == misp.util.platform) {
    	
    	if(packageName)
		{
    		return rootUrl + "/" + packageName + "/" + actionName + "/" + methodName;
		}
    	else
		{
    		return rootUrl + "/index.php/" + actionName + "/" + methodName;
		}
        
    }
    
};
//不同平台地址拼写方式不一样，在平台间移植时需要更改
misp.util.buildPageUrl = function (pageAddr,packageName) {
	var rootUrl = misp.util.rootUrl();
    //获取当前网址，如： http://localhost:8080/Tmall/index.jsp 
    var curWwwPath = window.document.location.href;

    //获取主机地址之后的目录如：/Tmall/index.jsp 
    var pathName = window.document.location.pathname;
    var pos = curWwwPath.indexOf(pathName);
    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    //获取主机地址，如： http://localhost:8080 
    var localhostPath = curWwwPath.substring(0, pos);
    if ("JAVA" == misp.util.platform) {
        //获取带"/"的项目名，如：/Tmall
        return localhostPath + projectName + "/Client/Views/" + pageAddr;
    }
    else if ("ASP.NET" == misp.util.platform) {
        return localhostPath + '/Client/Views/' + pageAddr;
    }
    else if ("PHP" == misp.util.platform) {
    	return localhostPath + projectName + "/Client/Views/" + pageAddr;
    }
};
misp.util.getImgUrl = function(){
	var rootUrl = misp.util.rootUrl();
	return rootUrl + "/Client/Public/Fuego/Uploads/";
}
misp.util.submit = function(data)
{
	if((null != data.data)&&(Object.prototype.toString.call(data.data) === "[object String]")&&(data.data.indexOf("#")>=0))
	{
		if ($(data.data).form('validate') == false) 
	    {
			misp.util.log("输入验证失败");
	    	return;
	    }
		misp.req.obj = $(data.data).serializeObject();			
	}
	else{
		misp.req.obj = data.data;
	}

    var json = misp.util.objToJson(misp.req);
    data.url = misp.util.buildUrl(data.url);
    data.data = json;
    var callback = data.success;
    data.success = function(rsp)
    {
         if (misp.util.isSuccess(rsp)) {
        	callback(rsp.obj);
        }
        else {
            misp.util.alert(rsp.errorMsg);
        }
    	
    };
    misp.util.fuegoAjax(data);
}
//通用Ajax函数
misp.util.fuegoAjax = function (ajaxPara) {
	try
	{
		if(ajaxPara.wait)
		{
			showWaitDialog();
		}
		var contentType = "";
	    if ("JAVA" == misp.util.platform) {
	        contentType = "text/html";
	    }
        $.ajax({
            type: "post",
            url: ajaxPara.url,
            contentType: contentType,
            data: ajaxPara.data,
            dataType: "json",
            success:function (data)
            {
        		if(ajaxPara.wait)
            	{
            		removeWaitDialog();
            	}	
            	ajaxPara.success(data);
            },
            error: function (error) {
        		if(ajaxPara.wait)
            	{
            		removeWaitDialog();
            	}	
                misp.util.alert(misp.util.objToJson(error));
                misp.util.alert("当前ajax操作出错！");
            }
        });
	}
	catch(e)
	{
		alert(e);
	}
};
//判断是否返回成功函数
misp.util.isSuccess = function (obj) {
    var result = false;
    if (obj.errorCode == 0) {
        result = true;
    }
    return result;
};
//通用提示窗口
misp.util.alert = function (text) {
    $.messager.alert('操作提示', text);
};
//
misp.util.objToJson = function (obj)
{
	return JSON.stringify(obj);
};
misp.util.jsonToObj = function (json)
{
	try
	{
		var obj = JSON.parse(json);
	}
	catch(e)
	{
		misp.util.log("json convert error", json);
		
	}
	return obj;
};
$.fn.serializeObject = function () {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function () {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};
misp.util.getDomObj = function(element)
{
	  var domObj;
	  if((typeof element=='string')&&element.constructor==String)
	  {
		  domObj = $(element); 
	  }
	  else
	  {
		  domObj = element;
	  }	 
	  return domObj;
};

misp.util.buildReqObj = function(obj1,obj2){
	var object1 = new Object;
	var object2 = new Object;
	if((Object.prototype.toString.call(obj1) === "[object String]")&&(obj1.indexOf("#")>=0))
	{
		object1 = $(obj1).serializeObject();			
	}
	else{
		object1 = obj1;
	}
	if((Object.prototype.toString.call(obj2) === "[object String]")&&(obj2.indexOf("#")>=0))
	{
		object2 = $(obj2).serializeObject();			
	}
	else{
		object2 = obj2;
	}
	for(var key in object2){
		object1[key] = object2[key];
	}
	misp.req.obj = object1;
};
misp.util.fileUpload = function(filePara){
	var fileName = $(filePara.newFile).textbox('getValue');
	var url = misp.util.buildUrl("Index|ImgUpload") + "?fileName=" + fileName;
    $.ajaxFileUpload({
        url:url,//处理图片脚本
        secureuri :false,
        fileElementId : filePara.fileID,//file控件id
        dataType : 'json',
        success : function (data, status){
        	if(misp.util.isSuccess(data)){
        		$(filePara.fileName).textbox('setValue',data.obj);
        		$(filePara.newFile).textbox('setValue',data.obj);
        		$(filePara.fileImg).attr('src', misp.util.getImgUrl() + data.obj);
        	}
        	else{
        		misp.util.alert(data.errorMsg);
        	}
        },
        error: function(data, status, e){
        	misp.util.alert(e);
        }
	})
};
misp.util.buildFilterJson = function(filterPara){
	var searchFilterList = [];
    searchFilterList.push(filterPara);
    var json = misp.util.objToJson(searchFilterList);
    var param = { "filter": json };
    return param;
};
function showWaitDialog()
{
	 $("<div class=\"datagrid-mask\"></div>")
	 .css({display:"block",width:"100%",height:$(window).height()})
	 .appendTo("body");
	 $("<div class=\"datagrid-mask-msg\" ></div>")
	 .html("正在处理数据,请稍后...")
	 .appendTo("body")
	 .css({display:"block","left":"45%","font-size":"14px","font-family":"微软雅黑",}); 
}
function removeWaitDialog()
{
   $("body").find("div.datagrid-mask-msg").remove();
   $("body").find("div.datagrid-mask").remove();
}
