var misp = misp || {};
misp.table= {};
misp.table.callback = misp.table.callback || {};
misp.table.log  = function (message,obj)
{
	var logged = false;
	if(logged)
	{
		if(null == obj)
		{
			obj = "";
		}	
		misp.util.log(message,obj,"misp.table");

	}	 
};

//table 加载
misp.table.load = function (element,data) 
{
	 misp.table.log("now load table");
	var domObj = misp.com.getDomObj(element);
	if(null == domObj)
	{
		misp.table.log("the element is empty");
		return;
	}
	if(null == data)
	{
	    data = misp.com.getOptions(domObj);
	}
	
	if(null == data)
	{
		misp.table.log("the data can not be empty");
		return;
	}	
	
	if(null == data.actionName)
	{
		misp.table.log("the action name is empty");
		return;
	}	
	misp.table.log("the table data is ",data);
	//查找table 下面的searchForm
	var searchObj = misp.com.getElementByType("misp.search",domObj);
	if(null != searchObj)
	{
		misp.table.log("load misp searchForm");
		misp.form.form.load(searchObj);
	}
	var toolbarID = "mispToolbar";
	//查找table 下面的toolbar
	var toolbarObj = misp.com.getElementByType("misp.toolbar",domObj);
	if(null != toolbarObj)
	{
		toolbarObj.attr("id",toolbarID);
		misp.table.log("load misp toolbar");
		misp.toolbar.load(toolbarObj);
	}
	//查找table 下面的grid
	var gridObj = misp.com.getElementByType("misp.grid",domObj);
	if(null != gridObj)
	{
		 if((false !== data.isLoad))
		 {
			 misp.table.log("load misp grid");
			 var gridData = new Object();
			 gridData['url'] = misp.util.buildActionUrl(data.actionName,"Load");
			 gridData['toolbar'] = "#"+toolbarID;
			 misp.table.log("load succes",gridData);
			 misp.grid.load(gridObj,gridData);
			 misp.table.log("load succes",gridData);
		 }
	}
	//查找页面中的table.dialog
	var dialogObj = misp.com.getElementByType("misp.table.dialog");
	if(null != dialogObj)
	{
		 misp.treement.log("load dialog");
		 misp.dialog.load(dialogObj);
	}
};
misp.table.search  = function()
{
	misp.table.log("misp.table.search");
	var tableObj = misp.com.getElementByType("misp.table");
	var tableData = misp.com.getOptions(tableObj);
	
	var gridObj = misp.com.getElementByType("misp.grid",tableObj);
	var searchObj = misp.com.getElementByType("misp.search",tableObj);

	if(null == gridObj)
	{
		misp.util.alert("不能找到misp.grid");
		return;
	}	
	 
	var json = "";
	if(null == searchObj)
	{
		misp.table.log("search form is not exist");
		 
	}
	else
	{
		json= misp.com.getFilterJson(searchObj);
	}	
	misp.table.log("search data is ",json);
    var param = {"filter" : json};
    gridObj.datagrid('loadData', { total: 0, rows: [] });
    gridObj.datagrid('load', param);	
};
misp.table.create = function(data)
{
	misp.table.log("misp table create");
	misp.table.show("Create",data);
};

misp.table.modify = function(data)
{
	misp.table.log("misp table modify");
	misp.table.show("Modify",data);
};
misp.table.callback.create = function(obj){
	
};
misp.table.callback.modify = function(obj){
	
};
misp.table.show = function(type,data)
{
	misp.table.log("show data");
	var tableObj = misp.com.getElementByType("misp.table");
	var tableData = misp.com.getOptions(tableObj);
	
	var gridObj = misp.com.getElementByType("misp.grid",tableObj);

	var rows = gridObj.datagrid('getChecked');
    if ((1 != rows.length)  && ("Modify" == type))
    {
        misp.util.alert("请选择一条记录");
        return;
    }

     if(null == data)
     {
    	 data = new Object();
     }
     
     if(null == data.callback)
     {
     	 if("Create" == type)
    	 {
     		data.callback = misp.table.callback.create;
    	 }
     	 else if("Modify" == type)
     	 {
     		data.callback = misp.table.callback.modify;
     	 }	 
     	 else
     	 {
     		data.callback = function (obj) 
    		{
            };
     	 }
     }
 
     data.methodName = data.methodName || "Show";

     //这个地方本来应该在table 下面找 dialog但是 easyui 将dialog的内容移除到其他地方了，
     //所以暂时在整个页面进行查找，这样一个页面只能支持一个dialog
     var dialogObj = misp.com.getElementByType("misp.table.dialog");
     var dialogData = misp.com.getOptions(dialogObj);
     dialogData['methodName'] = type;
     dialogObj.attr("data-options",misp.util.objToJson(dialogData));
     //var formObj = misp.com.getElementByType("misp.form",dialogObj);
     dialogObj.dialog('open');
     
      var id = null;
      if ("Modify" == type) {
            id = rows[0][tableData.idField];    
       }
      if(rows.length>0)
	  {
    	  gridObj.datagrid('uncheckAll');
	  }
      misp.util.submit({
    	  data:id,
    	  url:tableData.actionName+"|"+data.methodName,
    	  success:function(obj){
    		  var formObj = misp.com.getElementByType("misp.form",dialogObj);
    		  misp.form.form.load(formObj,obj);
           	  data.callback(obj);
    	  }
      });
};
misp.table.callback.save = function(obj){
	
};
misp.table.save = function (data) 
{
	misp.table.log("misp table save");
	var tableObj = misp.com.getElementByType("misp.table");
	var tableData = misp.com.getOptions(tableObj);
	
	var gridObj = misp.com.getElementByType("misp.grid",tableObj);
	
	if(null == gridObj)
	{
		misp.util.alert("不能找到misp.grid");
		return;
	}	
	
    //这个地方本来应该在table 下面找 dialog但是 easyui 将dialog的内容移除到其他地方了，
    //所以暂时在整个页面进行查找，这样一个页面只能支持一个dialog
    var dialogObj = misp.com.getElementByType("misp.table.dialog");
	if(null == dialogObj)
	{
		misp.util.alert("不能找到 misp.table.dialog");
		return;
	}
	var formObj = misp.com.getElementByType("misp.form",dialogObj);
 
	if(null == data)
	{
		data = misp.com.getOptions(dialogObj);
	}
	if(null == data.callback)
    {
		data.callback = misp.table.callback.save;
    }
    if ("Modify"==data.methodName) {
    	data.methodName = 'Modify';
    }
    else{
    	data.methodName = 'Create';
    }
    if (formObj.form('validate') == false) 
    {
    	misp.util.alert("输入不正确");
    	return;
    }
    var req = formObj.serializeObject();
    misp.util.submit({
    	  data:req,
    	  url:tableData.actionName+"|"+data.methodName,
    	  success:function(obj){
    		  gridObj.datagrid('loadData', { total: 0, rows: [] });
              //重新加载datagrid数据
          	  gridObj.datagrid('reload');
          	  dialogObj.dialog('close');
          	  data.callback(obj);
    	  }
      });
};
//批量删除
misp.table.removeList = function(data)
{
	misp.table.log("misp table remove");
	var tableObj = misp.com.getElementByType("misp.table");
	var tableData = misp.com.getOptions(tableObj);
	
	var gridObj = misp.com.getElementByType("misp.grid",tableObj);
 
	if(null == gridObj)
	{
		misp.util.alert("错误—不能找到table下面的misp.grid");
	}	
	
	if(null == data)
	{
		data = new Object();
	}	
    if (null == data.methodName) {
    	data.methodName = 'DeleteList';
	}

    var rows = gridObj.datagrid('getChecked');
	if (rows.length < 1) 
	{
	    misp.util.alert("请至少选择一条记录");
	    return;
	}
    var ids = [];
	for (var i = 0 ; i < rows.length; i++) {
	    ids.push(rows[i][tableData.idField]);
	}
	$.messager.confirm('确认提示', '确认删除这些记录？', function (r) { 
		if (r) {
	        //取消已选择的行，解决EasyUI bug
			gridObj.datagrid('uncheckAll');
			misp.util.submit({
		    	  data:ids,
		    	  url:tableData.actionName+"|"+data.methodName,
		    	  success:function(obj){
		    		  gridObj.datagrid('loadData', { total: 0, rows: [] });
                      //重新加载datagrid数据
                  	  gridObj.datagrid('reload');
		    	  }
		      });
        }
    });
	   
};
//删除单条
misp.table.remove = function(data)
{
	misp.table.log("misp table remove");
	var tableObj = misp.com.getElementByType("misp.table");
	var tableData = misp.com.getOptions(tableObj);
	
	var gridObj = misp.com.getElementByType("misp.grid",tableObj);
 
	if(null == gridObj)
	{
		misp.util.alert("错误—不能找到table下面的misp.grid");
	}	
	
	if(null == data)
	{
		data = new Object();
	}	
    if (null == data.methodName) {
    	data.methodName = 'Delete';
	}

    var rows = gridObj.datagrid('getChecked');
	if (rows.length != 1) 
	{
	    misp.util.alert("请选择一条记录");
	    return;
	}
    var id = null;
	id = rows[0][tableData.idField];
	$.messager.confirm('确认提示', '确认删除这条记录？', function (r) { 
		if (r) {
	        //取消已选择的行，解决EasyUI bug
			gridObj.datagrid('uncheckAll');
			misp.util.submit({
		    	  data:id,
		    	  url:tableData.actionName+"|"+data.methodName,
		    	  success:function(obj){
		    		  gridObj.datagrid('loadData', { total: 0, rows: [] });
                      //重新加载datagrid数据
                  	  gridObj.datagrid('reload');
		    	  }
		      });
        }
    });
	   
};
misp.table.cancel = function()
{
	misp.table.log("misp table cancel");
	var domObj = misp.com.getElementByType("misp.table.dialog");
	if(null == domObj)
	{
		misp.uitl.alert("不能找到 misp.dialog");
	}	
	domObj.dialog('close');
};

