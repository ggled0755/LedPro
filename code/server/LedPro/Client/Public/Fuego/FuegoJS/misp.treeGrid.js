var misp = misp || {};
misp.treeGrid = {};
//通用新建TreeGrid方法
//treeGridPara.treeGridID 页面treeGridID的ID，选填参数，默认为'mispTreeGrid';
//treeGridPara.toolbarID 页面toolbar的ID，选填参数，默认为 'mispToolbar';
//treeGridPara.methodName 加载数据的方法名，选填参数，默认为 'Load';
//treeGridPara.actionName 加载数据的Controller名，必填参数，如：'AdminManage';
//treeGridPara.idField 对象树id字段的名称，必填内容，如：'menu_id';
//treeGridPara.treeField 对象树节点内容字段的名称，必填内容，如：'menu_value';
//treeGridPara.parentField 对象树父节点id字段的名称，必填内容，如：'menu_parent_id';
misp.treeGrid.load = function (treeGridPara) {

    treeGridPara.treeGridID = '#' + (treeGridPara.treeGridID || 'mispTreeGrid');          //treeGrid默认id为“mispTreeGrid”
    treeGridPara.toolbarID = '#' + (treeGridPara.toolbarID || 'mispToolbar');             //treeGrid默认toolbarID为“mispToolbar”
    treeGridPara.methodName = treeGridPara.methodName || 'Load';                          //treeGrid默认methodName为“Load”
    treeGridPara.iconCls = treeGridPara.iconCls || 'iconCls';                          //
    //alert(JSON.stringify(treeGridPara));
    $(treeGridPara.treeGridID).treegrid({
        singleSelect: false,                                                            //允许多行选中
        lines: true,                                                                     //是否显示树标线
        border: false,                                                                  //是否显示pannel边框
        fit: true,                                                                      //撑满页面布局     
        url: misp.util.buildActionUrl(treeGridPara.actionName, treeGridPara.methodName),          //数据加载地址
        pagination: true,                                                               //是否显示底部页码栏
        pageSize: 10,                                                                   //默认一页显示10条记录
        pageList: [10, 20, 30, 40, 50],                                                 //页面显示记录数选项
        striped: true,                                                                  //是否条纹显示
        fitColumns: true,                               //列始终撑满页面，且不出现滚动条，在列较少时设置为true，列较多时设置为false
        nowrap: false,                                  //设置为true时，数据内容不换行，设置为false显示不下的表格会自动换行
        queryParams: '',                                //查询条件参数
        toolbar: treeGridPara.toolbarID,                //toobar
        idField: treeGridPara.idField,
        treeField: treeGridPara.treeField,
        loadFilter: function (data) {
            var dataLines = data.rows;
            for (var i = 0 ; i < dataLines.length; i++) {
                dataLines[i]["iconCls"] = dataLines[i][treeGridPara.iconCls];
                if (misp.treeGrid.isRoot(dataLines[i][treeGridPara.parentField], treeGridPara.idField, dataLines)) {
                    dataLines[i]["_parentId"] = null;
                }
                else {
                    dataLines[i]["_parentId"] = dataLines[i][treeGridPara.parentField];
                }
            }
            data.rows = dataLines;
            //alert(JSON.stringify(data.rows));
            return data;
        }
    });
};
misp.treeGrid.isRoot = function(parentId,idField,dataList)
{
    for(var i=0;i<dataList.length;i++)
    {
        if(parentId == dataList[i][idField])
        {
            return false;
        }	
    }	
    return true;
};
//通用查询方法
//searchPara.gridID 页面datagird的ID，选填参数，默认为'mispGrid';
//searchPara.formID 页面查询form的ID，选填参数，默认为'searchForm';
misp.treeGrid.search = function (searchPara) {

    if (null == searchPara) {
        var searchPara = new Object;
        searchPara.formID = 'searchForm';
        searchPara.gridID = 'mispTreeGrid';
    }

    if (null == searchPara.formID) {
        searchPara.formID = 'searchForm';
    }
    searchPara.formID = '#' + searchPara.formID;

    if (null == searchPara.gridID) {
        searchPara.gridID = 'mispTreeGrid';
    }
    searchPara.gridID = '#' + searchPara.gridID;
    //alert(JSON.stringify(searchPara));
    if ($(searchPara.formID).form('validate') == false) 
    {
        return;
    }
    var searchFilterList = [];
    //获取searchForm中的input内容。
    //引用EasyUI格式的input运行后审查元素会发现生成了3个input，所以需要根据name判空。
    $('form :input').each(function () {
        var searchFilter = new Object;
        if (null != $(this).attr("textboxname"))
        {
            var filterType = $(this).attr("filterType");
            if(filterType != null && filterType!='')
            {
                searchFilter.typeStr = filterType;
                searchFilter.firstValue = $(this).val();
                searchFilter.attrName = $(this).attr("textboxname");
                searchFilterList.push(searchFilter);
            }

        }
      
    });
    var json = JSON.stringify(searchFilterList);
    alert(json);
    var param = {"filter" : json};
    $(searchPara.gridID).treegrid('loadData', { total: 0, rows: [] });
    $(searchPara.gridID).treegrid('load', param);
};
//通用新增、编辑打开对话框方法
//dialogPara.dialogID 打开dialog的divID，选填参数，默认为 'mispShow';
//dialogPara.methodName 加载对象信息的方法名，选填参数，默认为 'Show';
//dialogPara.gridID 页面datagird的ID，选填参数，默认为'mispGrid';
//dialogPara.formID 页面form的ID，选填参数，默认为'mispForm';
//dialogPara.createCallbackFunction 新增回调函数，打开新增窗口时对form的一些操作；
//dialogPara.modifyCallbackFunction 编辑回调函数，打开编辑窗口时对form的一些操作；
//dialogPara.actionName 加载对象信息的Controller名，必填参数，如：'AdminManage';
//dialogPara.idName 对象id字段的名称，必填内容，如：'user_id';
misp.treeGrid.show = function (dialogPara) {

    if (null == dialogPara.dialogID) {
        dialogPara.dialogID = 'mispShow';
    }
    dialogPara.dialogID = '#' + dialogPara.dialogID;

    if (null == dialogPara.methodName) {
        dialogPara.methodName = 'Show';
    }

    if (null == dialogPara.gridID) {
        dialogPara.gridID = 'mispTreeGrid';
    }
    dialogPara.gridID = '#' + dialogPara.gridID;

    if (null == dialogPara.formID) {
        dialogPara.formID = 'mispForm';
    }
    dialogPara.formID = '#' + dialogPara.formID;

    if (null == dialogPara.createCallbackFunction) {
        dialogPara.createCallbackFunction = function createCallbackFunction(obj) {
        };
    }

    if (null == dialogPara.modifyCallbackFunction) {
        dialogPara.modifyCallbackFunction = function modifyCallbackFunction(obj) {
        };
    }
    // alert(JSON.stringify(dialogPara));

    var rows = $(dialogPara.gridID).treegrid('getSelections');
    if ((1 != rows.length) && ("Modify" == dialogPara.type)) {
        misp.util.alert("请选择一条记录");
    }
    else {
        var id=null;
        if ("Modify" == dialogPara.type) {
            id = rows[0][dialogPara.idName];  
        }
        var json = misp.util.objToJson(id);
        //打开用户编辑窗口
        $(dialogPara.dialogID).dialog('open');
        //调用fuegoAjax请求
        misp.util.fuegoAjax({
            url: misp.util.buildActionUrl(dialogPara.actionName, dialogPara.methodName),
            data: json,
            success: function (rsp) {
                if (misp.util.isSuccess(rsp)) {
                    //alert(JSON.stringify(rsp.obj));
                    $(dialogPara.formID).form('load', rsp.obj);
                    if ("Modify" == dialogPara.type) {
                        dialogPara.modifyCallbackFunction(rsp.obj);
                    }
                    else {
                        dialogPara.createCallbackFunction(rsp.obj);
                    }  
                }
                else {
                    misp.util.alert(rsp.errorMsg);
                }
            }
        });
    }
  
};
//通用新增、编辑form提交方法
//formPara.dialogID 打开dialog的divID，选填参数，默认为 'showPage';
//formPara.formID 页面form的ID，选填参数，默认为'mispForm';
//formPara.createMethod 创建对象的方法名，选填参数，默认为'Create';
//formPara.modifyMethod 创建对象的方法名，选填参数，默认为'Modify';
//formPara.actionName 加载对象信息的Controller名，必填参数，如：'AdminManage';
//formPara.idName 对象id字段的名称，必填内容，如：'user_id';
misp.treeGrid.save = function (formPara) {

    if (null == formPara.dialogID) {
        formPara.dialogID = 'mispShow';
    }
    formPara.dialogID = '#' + formPara.dialogID;

    if (null == formPara.formID) {
        formPara.formID = 'mispForm';
    }
    formPara.formID = '#' + formPara.formID;

    if (null == formPara.gridID) {
        formPara.gridID = 'mispTreeGrid';
    }
    formPara.gridID = '#' + formPara.gridID;

    formPara.idName = '#' + formPara.idName;
    //alert($(formPara.idName).val());
    if (0 != $(formPara.idName).val()) {             //对象id字段名，需要修改，0是页面上初始化的值。
        if (null == formPara.modifyMethod) {
            formPara.modifyMethod = 'Modify';
        }
        formPara.methodName = formPara.modifyMethod;
    }
    else {
        if (null == formPara.createMethod) {
            formPara.createMethod = 'Create';
        }
        formPara.methodName = formPara.createMethod;
    }
    //alert(JSON.stringify(formPara));
    if ($(formPara.formID).form('validate') == false) 
    {
  		
        return;
    }
    var json = misp.util.formToJson(formPara.formID);
    //alert(json);
    //调用fuegoAjax请求
    misp.util.fuegoAjax({
        url: misp.util.buildActionUrl(formPara.actionName, formPara.methodName),
        data: json,
        success: function (rsp) {
            if (misp.util.isSuccess(rsp)) {
                //关闭当前对话框
                $(formPara.dialogID).dialog('close');
                //重新加载datagrid数据
                $(formPara.gridID).treegrid('reload');
            }
            else {
                misp.util.alert(rsp.errorMsg);
            }
        }
    });
};

//通用删除方法
//deletePara.gridID 页面datagird的ID，选填参数，默认为'mispGrid';
//deletePara.methodName 加载对象信息的方法名，选填参数，默认为 'Delete';
//deletePara.actionName 加载对象信息的Controller名，必填参数，如：'AdminManage';
//deletePara.idName 对象id字段的名称，必填内容，如：'user_id';
misp.treeGrid.delete = function (deletePara) {
    //alert("OKK");
    if (null == deletePara.gridID) {
        deletePara.gridID = 'mispTreeGrid';
    }
    deletePara.gridID = '#' + deletePara.gridID;

    if (null == deletePara.methodName) {
        deletePara.methodName = 'Delete';
    }
  
    var rows = $(deletePara.gridID).treegrid('getSelections');
    // var rows = $(deletePara.gridID).datagrid('getChecked');
  
    if (rows.length < 1) {
        misp.util.alert("请至少选择一条记录");
    }
    else {
        var ids = [];
        for (var i = 0 ; i < rows.length; i++) {
            ids.push(rows[i][deletePara.idName]);
        }
        var json = misp.util.objToJson(ids);
        $.messager.confirm('确认提示', '确认删除这些记录？', function (r) {
            if (r) {
                //取消已选择的行，解决EasyUI bug
                $(deletePara.gridID).datagrid('uncheckAll');
                //调用fuegoAjax请求
                misp.util.fuegoAjax({
                    url: misp.util.buildActionUrl(deletePara.actionName, deletePara.methodName),
                    data: json,
                    success: function (rsp) {
                        if (misp.util.isSuccess(rsp)) {
                            //删除前台显示数据
                            //var rowIndex = $(deletePara.gridID).datagrid('getRowIndex', row);
                            //$(deletePara.gridID).datagrid('deleteRow', rowIndex);
                            //解决最后一条记录无法删除BUG
                            $(deletePara.gridID).treegrid('loadData', { total: 0, rows: [] });
                            //重新加载datagrid数据
                            $(deletePara.gridID).treegrid('reload');
                            // $("#mispTreeGrid").treegrid('reload');

                        }
                        misp.util.alert(rsp.errorMsg);
                    }
                });
            };
        });
    };
};