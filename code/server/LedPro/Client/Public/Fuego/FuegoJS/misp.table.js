var misp = misp || {};
misp.table= {};
misp.table.callback = misp.table.callback || {};
misp.table.callforward = misp.table.callforward || {};
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
//取消
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
//绑定对象前调函数
misp.table.callforward.bind = function(rows){
	
};
//绑定对象回调函数
misp.table.callback.bind = function(obj){
	
};
//绑定对象
misp.table.bind = function(dataGridPara)
{
	misp.table.log("bind data");
	var tableObj = misp.com.getElementByType("misp.table");
	var tableData = misp.com.getOptions(tableObj);
	if(null == dataGridPara)
	{
		dataGridPara = new Object();
	}
	dataGridPara.gridObj = dataGridPara.gridObj || misp.com.getElementByType("misp.grid",tableObj);
	dataGridPara.actionName = dataGridPara.actionName || tableData.actionName;
	dataGridPara.idField = dataGridPara.idField || tableData.idField;
	dataGridPara.methodName = dataGridPara.methodName || "Bind";
	if(null == dataGridPara.callback)
	{
		dataGridPara.callback = misp.table.callback.bind;
	}
	if(null == dataGridPara.callforward)
	{
		dataGridPara.callforward = misp.table.callforward.bind;
	}
	var rows = dataGridPara.gridObj.datagrid('getChecked');
    if (1 != rows.length)
    {
        misp.util.alert("请选择一条记录");
        return;
    }
    if(false == dataGridPara.callforward(rows))
	{
    	return;
	}
    var id = rows[0][dataGridPara.idField];
	if(rows.length>0)
	{
		dataGridPara.gridObj.datagrid('uncheckAll');
	}
	misp.util.submit({
		data:id,
		url:dataGridPara.actionName + "|" + dataGridPara.methodName,
		success:function(obj){
			dataGridPara.gridObj.datagrid('loadData', { total: 0, rows: [] });
			//重新加载datagrid数据
			dataGridPara.gridObj.datagrid('reload');
			dataGridPara.callback(obj);
	  }
  });
};
//解除绑定对象前调函数
misp.table.callforward.unbind = function(rows){
	
};
//解除绑定对象回调函数
misp.table.callback.unbind = function(obj){
	
};
//绑定对象
misp.table.unbind = function(dataGridPara)
{
	misp.table.log("bind data");
	var tableObj = misp.com.getElementByType("misp.table");
	var tableData = misp.com.getOptions(tableObj);
	if(null == dataGridPara)
	{
		dataGridPara = new Object();
	}
	dataGridPara.gridObj = dataGridPara.gridObj || misp.com.getElementByType("misp.grid",tableObj);
	dataGridPara.actionName = dataGridPara.actionName || tableData.actionName;
	dataGridPara.idField = dataGridPara.idField || tableData.idField;
	dataGridPara.methodName = dataGridPara.methodName || "UnBind";
	if(null == dataGridPara.callback)
	{
		dataGridPara.callback = misp.table.callback.unbind;
	}
	if(null == dataGridPara.callforward)
	{
		dataGridPara.callforward = misp.table.callforward.unbind;
	}
	var rows = dataGridPara.gridObj.datagrid('getChecked');
    if (1 != rows.length)
    {
        misp.util.alert("请选择一条记录");
        return;
    }
    if(false == dataGridPara.callforward(rows))
	{
    	return;
	}
    var id = rows[0][dataGridPara.idField];
	if(rows.length>0)
	{
		dataGridPara.gridObj.datagrid('uncheckAll');
	}
	misp.util.submit({
		data:id,
		url:dataGridPara.actionName + "|" + dataGridPara.methodName,
		success:function(obj){
			dataGridPara.gridObj.datagrid('loadData', { total: 0, rows: [] });
			//重新加载datagrid数据
			dataGridPara.gridObj.datagrid('reload');
			dataGridPara.callback(obj);
	  }
  });
};
//弹出详情Tab页
misp.table.addTab = function(tabPara)
{
	misp.table.log("add tab");
	var tableObj = misp.com.getElementByType("misp.table");
	var tableData = misp.com.getOptions(tableObj);
	if(null == tabPara)
	{
		tabPara = new Object();
	}
	tabPara.gridObj = tabPara.gridObj || misp.com.getElementByType("misp.grid",tableObj);
	tabPara.idField = tabPara.idField || tableData.idField;
	tabPara.detailUrl = tabPara.detailUrl || tableData.detailUrl;
	tabPara.detailTitle = tabPara.detailTitle || tableData.detailTitle;
	
	var rows = tabPara.gridObj.datagrid('getChecked');
    if (1 != rows.length)
    {
        misp.util.alert("请选择一条记录");
        return;
    }
    var tabID = rows[0][tabPara.idField];
    //var tabName = rows[0]['vote_name'];
	if(rows.length>0)
	{
		tabPara.gridObj.datagrid('uncheckAll');
	}
	var homeTabObj = top.$("#pageContent");
	var tabContent = '<iframe scrolling="auto" frameborder="0"  src="' + misp.util.buildPageUrl(tabPara.detailUrl) + '" style="width:100%;height:99%;"></iframe>';
	if (homeTabObj.tabs('exists', tabPara.detailTitle))
	{       												//如果已经打开过这个页面    
		homeTabObj.tabs('select', tabPara.detailTitle);        	//选中该tab页面  													
	    var tab = homeTabObj.tabs('getSelected');    		// 获取选择的面板
	    homeTabObj.tabs('update', {							// 更新选择的面板内容
	        tab: tab,
	        options: {
	            id: tabID,
	            title: tabPara.detailTitle,
	            content: tabContent,
	            closable:true 
	        }
	    });
    } 
    else 
    {
    	homeTabObj.tabs('add', {
    	    id: tabID,
    	    title: tabPara.detailTitle,
    	    content: tabContent,
    	    closable:true    
        });  
    }
};
