var misp = misp || {};
misp.treement= {};
misp.treement.callback = misp.treement.callback || {};
misp.treement.log = function (message, obj)
{
    var logged = false;
	if(logged)
	{
		if(null == obj)
		{
			obj = "";
		}	
		misp.util.log(message, obj, "misp.treement");

	}	 
};

//table 加载
misp.treement.load = function (element, data)
{
    misp.treement.log("now load treement");
	var domObj = misp.com.getDomObj(element);
	if(null == domObj)
	{
	    misp.treement.log("the element is empty");
		return;
	}
	if(null == data)
	{
	    data = misp.com.getOptions(domObj);
	}
	
	if(null == data)
	{
	    misp.treement.log("the data can not be empty");
		return;
	}	
	
	if(null == data.actionName)
	{
	    misp.treement.log("the action name is empty");
		return;
	}	
	misp.treement.log("the treement data is ", data);
	
	//查找treement 下面的tree
	var toolbarObj = misp.com.getElementByType("misp.toolbar",domObj);
	if(null != toolbarObj)
	{
	     misp.treement.log("load misp toolbar");
		 misp.toolbar.load(toolbarObj);
	}
	var treeObj = misp.com.getElementByType("misp.tree",domObj);
	if(null != treeObj)
	{
		 if((false !== data.isLoad))
		 {
			 misp.treement.log("load misp tree");
			 var treeData = new Object();
			 treeData['actionName'] = data.actionName;
			 treeData['field'] = data.field;
			 misp.tree.load(treeObj, treeData);
			 misp.treement.log("load succes", treeData);
		 }
	}
	var dialogObj = misp.com.getElementByType("misp.treement.dialog");
	if(null != dialogObj)
	{
		 misp.treement.log("load dialog");
		 misp.dialog.load(dialogObj);
	} 
};
misp.treement.create = function (data)
{
    misp.treement.log("misp treement create");
    misp.treement.show("Create", data);
};

misp.treement.modify = function (data)
{
    misp.treement.log("misp treement modify");
    misp.treement.show("Modify", data);
};
misp.treement.callback.create = function(obj){
	
};
misp.treement.callback.modify = function(obj){
	
};
misp.treement.show = function (type, data)
{
    misp.treement.log("show data");
    var treementObj = misp.com.getElementByType("misp.treement");
    var treementData = misp.com.getOptions(treementObj);
    var treeObj = misp.com.getElementByType("misp.tree", treementObj);
    data = data || new Object();
    data.methodName = data.methodName || "Show";

     //这个地方本来应该在table 下面找 dialog但是 easyui 将dialog的内容移除到其他地方了，
     //所以暂时在整个页面进行查找，这样一个页面只能支持一个dialog  
 	var dialogObj = misp.com.getElementByType("misp.treement.dialog");
 	var dialogData = misp.com.getOptions(dialogObj);
    dialogData['methodName'] = type;
    dialogObj.attr("data-options",misp.util.objToJson(dialogData));
    //var formObj = misp.com.getElementByType("misp.form",dialogObj);
    //formObj = $("#mispForm");
   
    if(null == data.callback)
    {
    	 if("Create" == type)
	   	 {
	    		data.callback = misp.treement.callback.create;
	   	 }
    	 else if("Modify" == type)
    	 {
    		data.callback = misp.treement.callback.modify;
    	 }	 
    	 else
    	 {
    		data.callback = function (obj) 
    		{
    		};
    	 }
    }
    var node = treeObj.tree('getSelected');
    
    misp.treement.log("Select node is ",node);
    if (null == node) {
        misp.util.alert("请选择一个节点");
        return;
    }
    else{
    	var nodeLevel = treeObj.tree("getLevel",node.target);
    	if("Modify" == type && nodeLevel<2){
        	misp.util.alert("无法编辑一级节点");
        	return;
        }
    	else {
            var id;
            if ("Create" == type) {
            	misp.temp.node = node;
            } else {
                var parentNode = treeObj.tree('find', node.attributes[treementData.field.parentField]);
                misp.temp.node = parentNode;
                id = node.id;
            }
            dialogObj.dialog('open');
            misp.util.submit({
          	  data:id,
          	  url:treementData.actionName + "|" + data.methodName,
          	  success:function(obj){
          		var formObj = misp.com.getElementByType("misp.form",dialogObj);
          		misp.form.form.load(formObj,obj);
             	data.callback(obj);
          	  }
            });
        }
    }
};
misp.treement.callback.save = function(obj){
	
};
misp.treement.save = function (data)
{
    misp.treement.log("misp treement save");
    var treementObj = misp.com.getElementByType("misp.treement");
    var treementData = misp.com.getOptions(treementObj);
	
    var treeObj = misp.com.getElementByType("misp.tree", treementObj);
	
    if (null == treeObj)
	{
        misp.util.alert("不能找到misp.tree");
		return;
	}	
	
    //这个地方本来应该在table 下面找 dialog但是 easyui 将dialog的内容移除到其他地方了，
    //所以暂时在整个页面进行查找，这样一个页面只能支持一个dialog
    var dialogObj = misp.com.getElementByType("misp.treement.dialog");
	if(null == dialogObj)
	{
	    misp.util.alert("不能找到 misp.treement.dialog");
		return;
	}
	var formObj = misp.com.getElementByType("misp.form",dialogObj);
 
	if(null == data)
	{
		data = misp.com.getOptions(dialogObj);
	}
	if(null == data.callback)
    {
		data.callback = misp.treement.callback.save;
    }
    if (formObj.form('validate') == false) 
    {
    	misp.util.alert("输入不正确");
    	return;
    }
    var req = formObj.serializeObject();
    misp.util.submit({
    	  data:req,
    	  url:treementData.actionName+"|"+data.methodName,
    	  success:function(obj){
    		//重新加载datagrid数据
        	treeObj.tree('reload');
    		//关闭当前对话框
          	dialogObj.dialog('close');
          	data.callback(obj);
    	  }
      });
};
misp.treement.callback.remove = function(obj){
	
};
misp.treement.remove = function(data)
{
	misp.treement.log("misp treement remove");
	var treementObj = misp.com.getElementByType("misp.treement");
    var treementData = misp.com.getOptions(treementObj);
	var treeObj = misp.com.getElementByType("misp.tree",treementObj);
 
	if(null == treeObj)
	{
		misp.util.alert("错误—不能找到treement下面的misp.tree");
	}	
	
	data = data || new Object();
	if(null == data.callback)
    {
		data.callback = misp.treement.callback.remove;
    }
	data.methodName = data.methodName || 'Delete';
	
	var node = treeObj.tree('getSelected');
	misp.treement.log("Select node is ",node);
    if (null == node) {
        misp.util.alert("请选择一个节点");
        return;
    }
    else{
    	var nodeLevel = treeObj.tree("getLevel",node.target);
    	if(nodeLevel<2){
        	misp.util.alert("无法删除一级节点");
        	return;
        }
    	else {
    		//tree 一次只能删除单个节点，这里组成数组只是为了后台接口统一
    		var ids = [];
    		ids.push(node.id);
    		misp.treement.log("the delete id list is ",node.id);
    		
    		$.messager.confirm('确认提示', '确认删除这些记录？', function (r) { 
				if (r){
					misp.util.submit({
			          	  data:ids,
			          	  url:treementData.actionName + "|" + data.methodName,
			          	  success:function(obj){
			          		treeObj.tree('reload');
			          		data.callback(obj);
			          	  }
		            });
	            }
		    });
            
        }
    } 
}
misp.treement.cancel = function ()
{
    misp.treement.log("misp treement cancel");
	var domObj = misp.com.getElementByType("misp.treement.dialog");
	if(null == domObj)
	{
		misp.uitl.alert("不能找到 misp.dialog");
	}	
	domObj.dialog('close');
}

