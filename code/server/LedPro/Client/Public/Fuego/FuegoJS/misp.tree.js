var misp = misp || {};
misp.tree = misp.tree || {};
misp.tree.callback = misp.tree.callback || {};
misp.tree.log = function (message, obj) {
    var logged = false;
    if (logged) {
        misp.util.log(message, obj, "misp.tree");

    }
};
//扩展Tree getLevel 方法
$.extend($.fn.tree.methods, {
	getLevel:function(jq,target){
	var l = $(target).parentsUntil("ul.tree","ul");
	return l.length+1;
	}
});
//扩展Tree LoadFilter方法
//将EasyUI tree 扩展为扁平化Tree，如果加载tree的时候填写了parentField，此方法就会调用递归树来加载树。
//如果没有填写parentField，系统安装EasyUI默认的方式加载tree。
$.fn.tree.defaults.loadFilter = function (data, parent) {
    var opt = $(this).data().tree.options;
    if (opt.parentField) {
        var nodeList = misp.tree.getRootNode(data, opt);
        var treeObj = misp.tree.getTreeObjList(data, nodeList, opt);
        return treeObj;
    }
    return data;
};
misp.tree.load = function (element, data) {
    misp.tree.log("now load tree");
    if (null == data) {
    	misp.tree.log("load with old method", element);
        misp.tree.loadOld(element);
        return;
    }
    var domObj = misp.com.getDomObj(element);
    if (null == domObj) {
        misp.tree.log("the element is null");
        return;
    }
    //if (null == data) {
    //    misp.tree.log("get data from data option");
    //    data = misp.com.getOptions(domObj);
    //}
    data.methodName = data.methodName || 'LoadList';                            //tree默认methodName为“LoadList”
    data.checkbox = data.checkbox || false;                                     //默认不显示勾选框
    var field = new Object;
    if (null != data.field) {
        field = data.field;
    }
    field.idField = field.idField || 'id';                          //此默认值与后台treeModel字段相对应
    field.parentField = field.parentField || 'parent_id';           //此默认值与后台treeModel字段相对应
    field.textField = field.textField || 'text';                    //此默认值与后台treeModel字段相对应
    field.iconClsField = field.iconClsField || 'iconCls';           //此默认值与后台treeModel字段相对应
    field.checkedField = field.checkedField || 'checked';           //此默认值与后台treeModel字段相对应

    data.param = data.param || '';
    

    misp.tree.log("the tree data is ", data);
    domObj.tree({
        lines: true,                                                            //是否显示对齐线
        url: misp.util.buildActionUrl(data.actionName, data.methodName),        //请求后台的url
        checkbox: data.checkbox,                                                //是否显示勾选框
        idField: field.idField,                                                 //id字段名称
        textField: field.textField,                                             //text字段名称
        parentField: field.parentField,                                         //parent字段名称
        iconClsField: field.iconClsField,                                       //iconCls字段名称
        checkedField: field.checkedField,                                       //checked字段名称
        queryParams: field.param                                                //筛选条件
    });
}
//通用新建Tree方法
//treePara.treeGridID 页面treeGridID的ID，选填参数，默认为'mispTreeGrid';
//treePara.methodName 加载数据的方法名，选填参数，默认为 'Load';
//treePara.actionName 加载数据的Controller名，必填参数，如：'AdminManage';
//treePara.idField 对象树id字段的名称，必填内容，如：'menu_id';
//treePara.textFiled 对象树节点内容字段的名称，必填内容，如：'menu_value';
//treePara.parentField 对象树父节点id字段的名称，必填内容，如：'menu_parent_id';
misp.tree.loadOld = function (treePara) {
    treePara.treeID = treePara.treeID || '#mispTree';                      //tree默认id为“mispTree”
    treePara.checkbox = treePara.checkbox || false;                        //默认不显示勾选框

    var field = new Object;
    if (null != treePara.field) {
        field = treePara.field;
    }
    field.idField = field.idField || 'id';                          //此默认值与后台treeModel字段相对应
    field.parentField = field.parentField || 'parent_id';           //此默认值与后台treeModel字段相对应
    field.textField = field.textField || 'text';                    //此默认值与后台treeModel字段相对应
    field.iconClsField = field.iconClsField || 'iconCls';           //此默认值与后台treeModel字段相对应
    field.checkedField = field.checkedField || 'checked';           //此默认值与后台treeModel字段相对应

    treePara.param = treePara.param || '';
    misp.tree.log("the treePara is ", treePara);
    $(treePara.treeID).tree({
        lines: true,
        url: misp.util.buildUrl(treePara.url),
        checkbox: treePara.checkbox,
        idField: field.idField,
        textField: field.textField,
        parentField: field.parentField,
        iconClsField: field.iconClsField,
        checkedField: field.checkedField,
        stateField: field.stateField,
        queryParams: treePara.param
    });
}
misp.tree.getTreeObjList = function (treeData, nodeList, field) {

    var treeObjList = [];
    for (var i = 0; i < nodeList.length; i++) {
        var treeObj = new Object;
        treeObj["id"] = nodeList[i][field.idField];
        treeObj["text"] = nodeList[i][field.textField];
        treeObj["iconCls"] = nodeList[i][field.iconClsField];
        treeObj["checked"] = nodeList[i][field.checkedField];
        //默认树展开到第二级
        if((null == field.stateField)&&(!misp.treeGrid.isRoot(nodeList[i][field.parentField], field.idField, treeData)))
    	{
        	treeObj["state"] = 'closed';
    	}
        else{
        	treeObj["state"] = nodeList[i][field.stateField];
        }
        treeObj["attributes"] = nodeList[i];
        var childList = misp.tree.getChildByParent(treeData, field.parentField, nodeList[i][field.idField]);
        if (0 != childList.length) {
            treeObj["children"] = misp.tree.getTreeObjList(treeData, childList, field);
        }
        else
    	{
        	//末级子节点state设置为'closed'会出现显示bug
        	treeObj["state"] = 'open';
    	}
        treeObjList.push(treeObj);

    }
    return treeObjList;
}
misp.tree.getRootNode = function (treeData, field) {
    var rootNodeList = [];
    for (var i = 0; i < treeData.length; i++) {
        if (misp.treeGrid.isRoot(treeData[i][field.parentField], field.idField, treeData)) {
            rootNodeList.push(treeData[i]);
        }
    }
    return rootNodeList;
}

misp.tree.getChildByParent = function (treeData, parentField, parentID) {
    var childNode = [];
    for (var i = 0; i < treeData.length; i++) {
        if (treeData[i][parentField] == parentID) {
            childNode.push(treeData[i]);
        }
    }
    return childNode;
};
misp.tree.callback.create = function(obj){
	
};
misp.tree.callback.modify = function(obj){
	
};
//通用打开新增、编辑树节点对话框方法
//dialogPara.dialogID 打开dialog的divID，选填参数，默认为 'mispShow';
//dialogPara.treeID 页面tree的ID，选填参数，默认为'mispTree';
//dialogPara.formID 页面form的ID，选填参数，默认为'mispForm';
//dialogPara.parentNameField 页面parentName的ID，选填参数，默认为'parent_name';
//dialogPara.methodName 请求后台methodName，选填参数，默认为'Show';
//dialogPara.createCallback 通用新增回调函数，打开新增窗口后对dialog的一些操作，选填参数，默认为空;
//dialogPara.modifyCallback 通用新增回调函数，打开编辑窗口后对dialog的一些操作，选填参数，默认为空;
//dialogPara.actionName 请求后台methodName，必填参数，如:'MenuManage';
//dialogPara.parentIDField 对象parentID字段ID，必填内容，如：'menu_parent_id';
misp.tree.show = function (type,data) {
	
	var dialogObj = misp.com.getElementByType("misp.dialog");
	if (null == data) {
        misp.tree.log("get data from data option");
        data = misp.com.getOptions(dialogObj);
    }
    data.treeID = data.treeID || "#mispTree";
    data.formID = data.formID || "#mispForm";
    
    data['methodName'] = type;
    dialogObj.attr("data-options",misp.util.objToJson(data));
    
    if(null == data.callback)
    {
    	 if("Create" == type)
	   	 {
	    		data.callback = misp.tree.callback.create;
	   	 }
    	 else if("Modify" == type)
    	 {
    		data.callback = misp.tree.callback.modify;
    	 }	 
    	 else
    	 {
    		data.callback = function (obj) 
    		{
    		};
    	 }
    }
    var node = $(data.treeID).tree('getSelected');
    
    misp.tree.log("Select node is ",node);
    if (null == node) {
        misp.util.alert("请选择一个节点");
        return;
    }
    else{
    	var nodeLevel = $(data.treeID).tree("getLevel",node.target);
    	if("Modify" == type && nodeLevel<2){
        	misp.util.alert("无法编辑一级节点");
        	return;
        }
    	else {
            var id;
            if ("Create" == type) {
            	misp.temp.obj = node;
            } else {
                var parentNode = $(data.treeID).tree('find', node.attributes[data.parentField]);
                misp.temp.obj = parentNode;
                id = node.id;
            }
            dialogObj.dialog('open');
            misp.util.submit({
          	  data:id,
          	  url:data.actionName+"|Show",
          	  success:function(obj){
          		  $(data.formID).form('load',obj);
             	  data.callback(obj);
          	  }
            });
        }
    }
    
}
//通用新增、编辑form提交方法
//formPara.dialogID 打开dialog的divID，选填参数，默认为 'showPage';
//formPara.treeID 打开tree的divID，选填参数，默认为 'mispTree';
//formPara.formID 页面form的ID，选填参数，默认为'mispForm';
//formPara.createMethod 创建对象的方法名，选填参数，默认为'Create';
//formPara.modifyMethod 创建对象的方法名，选填参数，默认为'Modify';
//formPara.actionName 加载对象信息的Controller名，必填参数，如：'AdminManage';
//formPara.idField 对象id字段的名称，必填内容，如：'user_id';
misp.tree.save = function (formPara) {

    formPara.dialogID = '#' + (formPara.dialogID || 'mispShow');
    formPara.formID = '#' + (formPara.formID || 'mispForm');
    formPara.treeID = '#' + (formPara.treeID || 'mispTree');

    formPara.idField = '#' + formPara.idField;

    if (0 != $(formPara.idField).val()) {             //对象id字段名，需要修改，0是页面上初始化的值。
        formPara.modifyMethod = formPara.modifyMethod || 'Modify';
        formPara.methodName = formPara.modifyMethod;
    }
    else {
        formPara.createMethod = formPara.createMethod || 'Create';
        formPara.methodName = formPara.createMethod;
    }
    alert(JSON.stringify(formPara));
    if ($(formPara.formID).form('validate') == false) return;
    var json = misp.util.formToJson(formPara.formID);
    //alert(json);
    //调用fuegoAjax请求
    misp.util.fuegoAjax({
        url: misp.util.buildActionUrl(formPara.actionName, formPara.methodName),
        data: json,
        success: function (rsp) {
            if (misp.util.isSuccess(rsp)) {
                alert("ok");
                //关闭当前对话框
                $(formPara.dialogID).dialog('close');
                alert("dalogclose");
                //重新加载datagrid数据
                $(formPara.treeID).tree('reload');
                alert("treereload");
            }
            else {
                misp.util.alert(rsp.errorMsg);
            }
        }
    });
}
//通用删除方法
//deletePara.treeID 页面tree的ID，选填参数，默认为'mispTree';
//deletePara.methodName 加载对象信息的方法名，选填参数，默认为 'Delete';
//deletePara.actionName 加载对象信息的Controller名，必填参数，如：'AdminManage';
misp.tree.delete = function (deletePara) {

    deletePara.treeID = '#' + (deletePara.treeID || 'mispTree');
    deletePara.methodName = deletePara.methodName || 'Delete';

    var node = $(deletePara.treeID).tree('getSelected');
    alert(node);
    if (node.length < 1) {
        misp.util.alert("请至少选择一个节点");
    }
    else {
        var children = $(deletePara.treeID).tree('getChildren', node.target);
        if (children != "") {
            misp.util.alert("请先删除子节点");
        } else {
            var ids = [];
            ids.push(node.id);
            var json = misp.util.objToJson(ids);
            //alert(json);
            $.messager.confirm('确认提示', '确认删除这些记录？', function (r) {
                if (r) {
                    //取消已选择的行，解决EasyUI bug
                    //$(deletePara.gridID).datagrid('uncheckAll');
                    //调用fuegoAjax请求
                    misp.util.fuegoAjax({
                        url: misp.util.buildActionUrl(deletePara.actionName, deletePara.methodName),
                        data: json,
                        success: function (rsp) {
                            if (misp.util.isSuccess(rsp)) {
                                $(deletePara.treeID).tree('reload');
                            }
                            misp.util.alert(rsp.errorMsg);
                        }
                    });
                }
            });
        }

    }
}
//通用打开编辑权限对话框方法
//dialogPara.dialogID 打开dialog的divID，选填参数，默认为 'mispTreeShow';
//dialogPara.gridID 页面datagird的ID，选填参数，默认为'mispGrid';
//dialogPara.callback 通用回调函数，打开新增窗口后对tree的一些操作；
//dialogPara.idField 对象id字段的名称，必填内容，如：'user_id';
misp.tree.showTree = function (dialogPara) {

    dialogPara.dialogID = '#' + (dialogPara.dialogID || 'mispTreeShow');              //dialog默认id为“mispTreeShow”
    dialogPara.gridID = '#' + (dialogPara.gridID || 'mispGrid');                      //datagird默认id为“mispGrid”
    dialogPara.masterField = '#' + (dialogPara.masterField || 'master_value');        //datagird默认id为“mispGrid”
    if (null == dialogPara.callback) {
        dialogPara.callback = function callback(obj) {
        };
    }
    //alert(JSON.stringify(dialogPara));
    var rows = $(dialogPara.gridID).datagrid('getChecked');
    if (1 != rows.length) {
        misp.util.alert("请选择一条记录");
    }
    else {
        //打开权限编辑窗口
        $(dialogPara.masterField).textbox('setValue', rows[0][dialogPara.idField]);
        $(dialogPara.dialogID).dialog('open');
        dialogPara.callback(rows[0][dialogPara.idField]);
    }
}
//通用保存勾选Tree节点方法
//treePara.treeID 页面treeID的ID，选填参数，默认为'mispTree';
//treePara.dialogID 页面dialog的ID，选填参数，默认为'mispTreeShow';
//treePara.methodName 加载数据的方法名，选填参数，默认为 'SaveCheckedTree';
//treePara.typeFiled 对象树节点内容字段的名称，必填内容，如：'menu_value';
//treePara.masterField 对象树父节点id字段的名称，必填内容，如：'master_value';
//treePara.actionName 加载数据的Controller名，必填内容，如：'MenuManage';
//treePara.idField 对象树id字段的名称，必填内容，如：'menu_id';
misp.tree.saveChecked = function (treePara) {
    treePara.treeID = '#' + (treePara.treeID || 'mispTree');                    //tree默认id为“mispTree
    treePara.dialogID = '#' + (treePara.dialogID || 'mispTreeShow');            //dialog默认id为“mispTreeShow”
    treePara.masterField = '#' + (treePara.masterField || 'master_value');      //masterField默认id为“master_value”
    treePara.methodName = treePara.methodName || 'SaveCheckedTree';             //methodName默认为“SaveCheckedTree”

    var nodes = $(treePara.treeID).tree('getChecked');	                        // get checked nodes
    var checkedNodes = [];
    for (var i = 0 ; i < nodes.length; i++) {
        var nodeObj = {};
        nodeObj[treePara.idField] = nodes[i]['id'];
        if (treePara.typeField != null) {
            nodeObj[treePara.typeField] = nodes[i][treePara.typeField];
        }
        checkedNodes.push(nodeObj);
    }
    var parentObj = $(treePara.masterField).textbox('getValue');
    var json = misp.util.objToJson(checkedNodes, parentObj);
    //alert(json);
    misp.util.fuegoAjax({
        url: misp.util.buildActionUrl(treePara.actionName, treePara.methodName),
        data: json,
        success: function (rsp) {
            if (misp.util.isSuccess(rsp)) {
                //关闭当前对话框
                $(treePara.dialogID).dialog('close');
            }
            misp.util.alert(rsp.errorMsg);
        }
    });

}