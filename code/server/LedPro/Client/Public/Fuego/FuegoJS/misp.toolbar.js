var misp = misp || {};
misp.toolbar= {}; 

misp.toolbar.log  = function (message,obj)
{
	var logged = false;
	if(logged)
	{
		misp.util.log(message,obj);

	}	
};
misp.toolbar.load=function(element,data)
{
	
    var pp = top.$('#pageContent').tabs('getSelected'); 
    var tab = pp.panel('options');
    
    if(null == data)
    {
    	data =  new Object();
    }	
    if(null == data.url)
    {
    	data.url = misp.util.buildActionUrl('sys/UserManage', 'LoadButton');
    }	
    misp.toolbar.log("load tool bar"); 
   misp.util.fuegoAjax({
       url: data.url,
       data:  misp.util.objToJson(tab.id),
       success: function (rsp) {
    	  
           if (misp.util.isSuccess(rsp)) {
        	
        	   misp.toolbar.addButton(element,rsp.obj);
           }
           else {
               misp.util.alert(rsp.errorMsg);
           }
       }
   }); 
 
	}
misp.toolbar.addButton =  function (element,buttonList)
{
	misp.toolbar.log("the button data is ",buttonList);
	var td= misp.com.getDomObj(element);
	var content ="";
	for(var i=0; i<buttonList.length; i++)
	{

	content += "<a class=\"" +buttonList[i].css+"\" iconCls=\""+buttonList[i].icon+"\" onclick=\""+buttonList[i].method+"\">"+buttonList[i].value+"</a>";

	}
	misp.toolbar.log("the content is  "+content);
	 
	td.append(content);	
	$.parser.parse(td);

}