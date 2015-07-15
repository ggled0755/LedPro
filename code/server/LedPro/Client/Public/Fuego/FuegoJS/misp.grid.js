var misp = misp || {};
misp.grid= {};

misp.grid.log  = function (message,obj)
{
	var logged = false;
	if(logged)
	{
		misp.util.log(message,obj,"misp.grid");

	}	
};
//通用新建Datagrid方法
//dataGridPara.gridID 页面datagird的ID，选填参数，默认为'mispGrid';
//dataGridPara.toolbarID 页面toolbar的ID，选填参数，默认为 'toolbar';
//dataGridPara.methodName 加载数据的方法名，选填参数，默认为 'Load';
//dataGridPara.actionName 加载数据的Controller名，必填参数，如：'AdminManage';
//dataGridPara.idName 对象id字段的名称，必填内容，如：'user_id';
misp.grid.load = function (element,data)
{
	misp.grid.log("now load grid");
	if(null == data)
	{
		misp.grid.loadOld(element);
		misp.grid.log("load with old method",element);
		return;
	}	
	var domObj = misp.com.getDomObj(element);
	if(null == domObj)
	{
		misp.grid.log("the element is null");
		return;
	}	
	if(null == data)
	{
		misp.grid.log("get data from data option");
		data = misp.com.getOptions(domObj);
	}
	
	misp.grid.log("the grid data is ",data);	
	domObj.datagrid({
        border: false,                                                                  //是否显示pannel边框
        fit: true,                                                                      //撑满页面布局     
        method: 'post',                                                                 //提交方式
        url: data.url,          														//数据加载地址
        pagination: true,                                                               //是否显示底部页码栏
        pageSize: 10,                                                                   //默认一页显示10条记录
        pageList: [10, 20, 30, 40, 50],                                                 //页面显示记录数选项
        striped: true,                                                                  //是否条纹显示
        fitColumns: false,                               //列始终撑满页面，且不出现滚动条，在列较少时设置为true，列较多时设置为false
        nowrap: false,                                  //设置为true时，数据内容不换行，设置为false显示不下的表格会自动换行
        idField: data.idField,                   //实现不同页之间选择的数据都有效
        queryParams: '',                                //查询条件参数
        toolbar: data.toolbar,                //toobar
    });
};
misp.grid.loadOld = function (dataGridPara) {
	dataGridPara.gridID = dataGridPara.gridID || "#mispGrid";
	dataGridPara.toolbarID = dataGridPara.toolbarID || "#mispToolbar";
	dataGridPara.methodName = dataGridPara.methodName || "Load";
	dataGridPara.param = dataGridPara.param || "";
     $(dataGridPara.gridID).datagrid({
        border: false,                                                                  //是否显示pannel边框
        fit: true,                                                                      //撑满页面布局     
        method: 'post',                                                                 //提交方式
        url: misp.util.buildUrl(dataGridPara.actionName+"|"+dataGridPara.methodName),   //数据加载地址
        pagination: true,                                                               //是否显示底部页码栏
        pageSize: 10,                                                                   //默认一页显示10条记录
        pageList: [10, 20, 30, 40, 50],                                                 //页面显示记录数选项
        striped: true,                                                                  //是否条纹显示
        fitColumns: false,                               //列始终撑满页面，且不出现滚动条，在列较少时设置为true，列较多时设置为false
        nowrap: false,                                  //设置为true时，数据内容不换行，设置为false显示不下的表格会自动换行
        idField: dataGridPara.idName,                   //实现不同页之间选择的数据都有效
        queryParams: dataGridPara.param,                                //查询条件参数
        toolbar: dataGridPara.toolbarID,                //toobar
    });
};
