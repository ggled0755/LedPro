var misp = misp || {};
misp.editorGrid = {};
misp.editorGrid.editRow = null;
//通用Datagrid加载方法
//dataGridPara.gridID 页面datagird的ID，选填参数，默认为'mispGrid';
//dataGridPara.toolbarID 页面toolbar的ID，选填参数，默认为 'toolbar';
//dataGridPara.methodName 加载数据的方法名，选填参数，默认为 'Load';
//dataGridPara.createMethod 新增数据的方法名，选填参数，默认为 'Create';
//dataGridPara.modifyMethod 新增数据的方法名，选填参数，默认为 'Modify';
//dataGridPara.actionName 加载数据的Controller名，必填参数，如：'AdminManage';
//dataGridPara.idName 对象id字段的名称，必填内容，如：'user_id';
misp.editorGrid.load = function (dataGridPara) {

    if (null == dataGridPara.gridID) {
        dataGridPara.gridID = 'mispGrid';
    }
    dataGridPara.gridID = '#' + dataGridPara.gridID;

    if (null == dataGridPara.toolbarID) {
        dataGridPara.toolbarID = 'mispToolbar';
    }
    dataGridPara.toolbarID = '#' + dataGridPara.toolbarID;

    if (null == dataGridPara.methodName) {
        dataGridPara.methodName = 'Load';
    }

    if (null == dataGridPara.createMethod) {
        dataGridPara.createMethod = 'Create';
    }

    if (null == dataGridPara.modifyMethod) {
        dataGridPara.modifyMethod = 'Modify';
    }

    $(dataGridPara.gridID).datagrid({
        border: false,                                                                  //是否显示pannel边框
        fit: true,                                                                      //撑满页面布局     
        method: 'post',                                                                 //提交方式
        url: misp.util.buildActionUrl(dataGridPara.actionName, dataGridPara.methodName),          //数据加载地址
        pagination: true,                                                               //是否显示底部页码栏
        pageSize: 10,                                                                   //默认一页显示10条记录
        pageList: [10, 20, 30, 40, 50],                                                 //页面显示记录数选项
        striped: true,                                                                  //是否条纹显示
        fitColumns: true,                               //列始终撑满页面，且不出现滚动条，在列较少时设置为true，列较多时设置为false
        nowrap: false,                                  //设置为true时，数据内容不换行，设置为false显示不下的表格会自动换行
        idField: dataGridPara.idName,                   //实现不同页之间选择的数据都有效
        queryParams: '',                                //查询条件参数
        toolbar: dataGridPara.toolbarID,                //toobar
        onAfterEdit: function (rowIndex, rowData, changes) {

            //判断是新增还是编辑
            //也可以使用$(dataGridPara.gridID).datagrid('getChanges', inserted); 可以获取到插入的行
            //使用$(dataGridPara.gridID).datagrid('getChanges', updated); 可以获取到修改的行，修改完之后需要调用acceptChanges
            $(dataGridPara.gridID).datagrid('selectRow', rowIndex);         //选中正在编辑的行
            var row = $(dataGridPara.gridID).datagrid('getSelected');       //获取此行中的ID
            if (null != row[dataGridPara.idName]) {
                dataGridPara.submitMethod = dataGridPara.modifyMethod;
                rowData[dataGridPara.idName] = row[dataGridPara.idName];    //解决不同浏览器下的兼容问题，firefox没有开启editor的字段没有提交
            }
            else {
                dataGridPara.submitMethod = dataGridPara.createMethod;
            }
            //清空编辑行标
            misp.editorGrid.editRow = null;
            var json = misp.util.objToJson(rowData);
            $(dataGridPara.gridID).datagrid('unselectAll');
            //alert(json);
            //调用fuegoAjax请求
            misp.util.fuegoAjax({
                url: misp.util.buildActionUrl(dataGridPara.actionName, dataGridPara.submitMethod),
                data: json,
                success: function (rsp) {
                    if (misp.util.isSuccess(rsp)) {
                        //重新加载datagrid数据
                        $(dataGridPara.gridID).datagrid('reload');
                    }
                    else {
                        misp.util.alert(rsp.errorMsg);
                    }
                }
            });
        }
    });
}
//通用增加一行方法
//
misp.editorGrid.create = function (dataGridPara) {

    if (null == dataGridPara.gridID) {
        dataGridPara.gridID = 'mispGrid';
    }
    dataGridPara.gridID = '#' + dataGridPara.gridID;

    if (null != misp.editorGrid.editRow) {
        //如果在新增之前有处于编辑状态的行，再点击新增时，相当于点击了保存按钮
        //$(dataGridPara.gridID).datagrid('endEdit', misp.editorGrid.editRow);
        //如果在新增之前有处于编辑状态的行，再点击新增时，相当于点击了取消按钮
        misp.editorGrid.cancel();
    }
    else{
        misp.editorGrid.editRow = 0;
        $(dataGridPara.gridID).datagrid('insertRow', {
            index: misp.editorGrid.editRow,	    // index start with 0
            row: dataGridPara.row
        });
        $(dataGridPara.gridID).datagrid('beginEdit', misp.editorGrid.editRow);
    }

}
//通用编辑一行方法
//
misp.editorGrid.modify = function (dataGridPara) {

    if (null == dataGridPara) {
        var dataGridPara = new Object;
        dataGridPara.gridID = 'mispGrid';
    }
    if (null == dataGridPara.gridID) {
        dataGridPara.gridID = 'mispGrid';
    }
    dataGridPara.gridID = '#' + dataGridPara.gridID;

    var rows = $(dataGridPara.gridID).datagrid('getSelections');
    if (1 != rows.length) {
        misp.util.alert("请选择一条记录");
    }
    else {
        if (null != misp.editorGrid.editRow) {
            //如果在编辑之前有处于编辑状态的行，再点击编辑时，相当于点击了保存按钮
            //$(dataGridPara.gridID).datagrid('endEdit', misp.editorGrid.editRow);
            //如果在编辑之前有处于编辑状态的行，再点击编辑时，相当于点击了取消按钮
            misp.editorGrid.cancel();
        }
        else{
            misp.editorGrid.editRow = $(dataGridPara.gridID).datagrid('getRowIndex', rows[0]);
            $(dataGridPara.gridID).datagrid('beginEdit', misp.editorGrid.editRow);
            $(dataGridPara.gridID).datagrid('unselectAll');
        }
    }
}
//通用取消方法
//
misp.editorGrid.cancel = function (dataGridPara) {

    if (null == dataGridPara) {
        var dataGridPara = new Object;
        dataGridPara.gridID = 'mispGrid';
    }
    if (null == dataGridPara.gridID) {
        dataGridPara.gridID = 'mispGrid';
    }
    dataGridPara.gridID = '#' + dataGridPara.gridID;

    misp.editorGrid.editRow = null;
    
    $(dataGridPara.gridID).datagrid('rejectChanges');
    $(dataGridPara.gridID).datagrid('unselectAll');
}
//通用保存方法
//
misp.editorGrid.save = function (dataGridPara) {

    if (null == dataGridPara) {
        var dataGridPara = new Object;
        dataGridPara.gridID = 'mispGrid';
    }
    if (null == dataGridPara.gridID) {
        dataGridPara.gridID = 'mispGrid';
    }
    dataGridPara.gridID = '#' + dataGridPara.gridID;
    //保存时取消所有编辑状态
    $(dataGridPara.gridID).datagrid('unselectAll');
    if (null != misp.editorGrid.editRow) {
        $(dataGridPara.gridID).datagrid('endEdit', misp.editorGrid.editRow);
    }
}
//editor扩展datetimebox
$.extend($.fn.datagrid.defaults.editors, {
    datetimebox: {
        init: function (container, options) {
            var editor = $('<input />').appendTo(container);
            options.editable = false;
            editor.datetimebox(options);
            return editor;
        },
        getValue: function (target) {
            return $(target).datetimebox('getValue');
        },
        setValue: function (target, value) {
            $(target).datetimebox('setValue',value);
        },
        resize: function (target, width) {
            $(target).datetimebox('resize', width);
        },
        destory: function (target) {
            $(target).datetimebox('destory');
        }
    }
});
//datagrid扩展增加、删除editor属性
//调用方法举例
//$("#mispGrid").datagrid('addEditor',{
//          field:'password',
//          editor:{type:'validatebox',options:{required:true}}
// });
//$("#mispGrid").datagrid('removeEditor',['password','user_id']);
$.extend($.fn.datagrid.methods, {
    addEditor: function (jq, param) {
        if (param instanceof Array) {
            $.each(param, function (index, item) {
                var e = $(jq).datagrid('getColumnOption', item.field);
                e.editor = item.editor;
            });
        } else {
            var e = $(jq).datagrid('getColumnOption', param.field);
            e.editor = param.editor;
        }
    },
    removeEditor: function (jq, param) {
        if (param instanceof Array) {
            $.each(param, function (index, item) {
                var e = $(jq).datagrid('getColumnOption', item);
                e.editor = {};
            });
        } else {
            var e = $(jq).datagrid('getColumnOption', param);
            e.editor = {};
        }
    }
});