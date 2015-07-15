var misp = misp || {};
misp.data = misp.data || {};
misp.data.enum = [
	{"enum_id":1,"table_name":"project","field_name":"project_status","enum_sort_id":"","enum_key":"0","enum_value":"进行中"},
	{"enum_id":2,"table_name":"project","field_name":"project_status","enum_sort_id":"","enum_key":"1","enum_value":"已完成"},
	{"enum_id":3,"table_name":"project","field_name":"project_status","enum_sort_id":"","enum_key":"2","enum_value":"已结束"},
	{"enum_id":4,"table_name":"worktime","field_name":"work_type","enum_sort_id":"","enum_key":"0","enum_value":"公摊"},
	{"enum_id":5,"table_name":"worktime","field_name":"work_type","enum_sort_id":"","enum_key":"1","enum_value":"商务谈判"},
	{"enum_id":6,"table_name":"worktime","field_name":"work_type","enum_sort_id":"","enum_key":"2","enum_value":"需求沟通"},
	{"enum_id":7,"table_name":"worktime","field_name":"work_type","enum_sort_id":"","enum_key":"3","enum_value":"开发"},
	{"enum_id":8,"table_name":"worktime","field_name":"work_type","enum_sort_id":"","enum_key":"4","enum_value":"测试"},
	{"enum_id":9,"table_name":"worktime","field_name":"work_type","enum_sort_id":"","enum_key":"5","enum_value":"交付"},
	{"enum_id":10,"table_name":"worktime","field_name":"work_type","enum_sort_id":"","enum_key":"6","enum_value":"维护"},
	{"enum_id":11,"table_name":"project_fee","field_name":"project_fee_type","enum_sort_id":"","enum_key":"0","enum_value":"员工工资"},
	{"enum_id":12,"table_name":"project_fee","field_name":"project_fee_type","enum_sort_id":"","enum_key":"1","enum_value":"办公租金"},
	{"enum_id":13,"table_name":"project_fee","field_name":"project_fee_type","enum_sort_id":"","enum_key":"2","enum_value":"网络通信"},
	{"enum_id":14,"table_name":"project_fee","field_name":"project_fee_status","enum_sort_id":"","enum_key":"0","enum_value":"待审核"},
	{"enum_id":15,"table_name":"project_fee","field_name":"project_fee_status","enum_sort_id":"","enum_key":"1","enum_value":"已审核"},
	{"enum_id":16,"table_name":"project_fee","field_name":"project_fee_status","enum_sort_id":"","enum_key":"2","enum_value":"已拒绝"},
	{"enum_id":17,"table_name":"current_account","field_name":"account_type","enum_sort_id":"","enum_key":"0","enum_value":"现金账户"},
	{"enum_id":18,"table_name":"current_account","field_name":"account_type","enum_sort_id":"","enum_key":"1","enum_value":"对公账户"},
	{"enum_id":19,"table_name":"current_account","field_name":"money_flow_type","enum_sort_id":"","enum_key":"0","enum_value":"收入"},
	{"enum_id":20,"table_name":"current_account","field_name":"money_flow_type","enum_sort_id":"","enum_key":"1","enum_value":"支出"},
	{"enum_id":21,"table_name":"project_fee","field_name":"project_fee_type","enum_sort_id":"","enum_key":"3","enum_value":"财务结算"},
	{"enum_id":22,"table_name":"project_fee","field_name":"project_fee_type","enum_sort_id":"","enum_key":"4","enum_value":"日常耗材"},
	{"enum_id":23,"table_name":"project_fee","field_name":"project_fee_type","enum_sort_id":"","enum_key":"5","enum_value":"公司活动"},
	{"enum_id":24,"table_name":"project_fee","field_name":"project_fee_type","enum_sort_id":"","enum_key":"6","enum_value":"差旅费用"},
	{"enum_id":25,"table_name":"project_fee","field_name":"project_fee_type","enum_sort_id":"","enum_key":"7","enum_value":"业务拓展"},
	{"enum_id":26,"table_name":"project_fee","field_name":"project_fee_type","enum_sort_id":"","enum_key":"8","enum_value":"公关费用"},
	{"enum_id":27,"table_name":"project_fee","field_name":"project_fee_type","enum_sort_id":"","enum_key":"9","enum_value":"设备设施"},
];
misp.data.role = [
	{"role_id":1,"role_name":"超级管理员","role_type_id":0,"role_type_name":"","org_code":"","company_id":0},
	{"role_id":2,"role_name":"普通员工","role_type_id":0,"role_type_name":"","org_code":"","company_id":0}
]; 
misp.data.formatPara = misp.data.formatPara || {};
misp.data.formatConstant = function (val, row) {
	for(var i=0;i<misp.data.enum.length;i++)
	{
		if((misp.data.enum[i].table_name == misp.data.formatPara.table_name)&&(misp.data.enum[i].field_name == misp.data.formatPara.field_name)&&(misp.data.enum[i].enum_key == val))
		{
			return misp.data.enum[i].enum_value;
		}
	}
};
misp.data.formatTime = function (val, row) {
    //如果数据不是/Date(1428855532000)/格式的，如新增一行时数据为空，
    //或者新增一行选择日期后，格式本身就是正确的，返回本身即可
    if ((null == val)||(val.indexOf("Date") < 0)) {
        return val;
    }
    //val原来的时间格式为 /Date(1428855532000)/
    //转换为标准时间格式 Mon Apr 13 2015 00:18:52 GMT+0800
    val = eval('new ' + (val.replace(/\//g, '')));
    //转为JS时间类型数据 方便获取年月日时分秒
    var date = new Date(val);
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var day = date.getDate();
    if (month < 10) {
        month = '0' + month;
    }
    if (day < 10) {
        day = '0' + day;
    }
    var hour = date.getHours();
    if (hour < 10) {
        hour = '0' + hour;
    }
    var minute = date.getMinutes();
    if (minute < 10) {
        minute = '0' + minute;
    }
    var second = date.getSeconds();
    if (second < 10) {
        second = '0' + second;
    }
    return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
};

