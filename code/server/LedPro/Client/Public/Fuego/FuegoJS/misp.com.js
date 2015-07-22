var misp = misp || {};
misp.com = {};


misp.com.log  = function (message,obj)
{
	var logged = false;
	if(logged)
	{
		misp.util.log(message,obj,"misp.com");

	}
};
misp.com.getMispClass= function(element)
{
	  var domObj = misp.com.getDomObj(element);
	  var mispClass = domObj.attr("misp-class");
	  return mispClass;
};
misp.com.getOptions = function(element)
{
	  var domObj = misp.com.getDomObj(element);
	  var data=$.trim(domObj.attr("data-options"));
	  var obj = {};
	  if(data)
	  {
		if(data.substring(0,1)!="{")
		{
			data="{"+data+"}";
		}
		obj =(new Function("return "+data))();
	  }
 
	  return obj;
};
misp.com.getDomObj = function(element)
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
misp.com.getFilterJson = function(element,searchObj)
{
	var json = "";
	try
	{
	    var searchFilterList = [];
	    //获取搜索form中所有的input
	    var inputs = misp.com.getDomObj(element).find("input");
	    //获取searchForm中的input内容。
	    //引用EasyUI格式的input运行后审查元素会发现生成了3个input，所以需要根据filterType属性筛选。
	    //循环这个input数组取值
	    var attrName;
	    var searchFilter = new Object;
	    inputs.each(function () {
	        //alert($(this).val());
	        if (null != $(this).attr("filterType")) {

	            searchFilter.typeStr = $(this).attr("filterType");
	            attrName = $(this).attr("textboxname");
	            searchFilter.attrName = $(this).attr("textboxname");
	        }
	        if (attrName == $(this).attr("name")) {
	            searchFilter.firstValue = $(this).val();
	            if(searchFilter.firstValue !="")
	            {
	            	searchFilterList.push(searchFilter);
		            searchFilter = new Object;
	            }	
	            
	        }
	    });
	    if(null != searchObj)
    	{
	    	searchFilterList.push(searchObj);
    	}
	    json = JSON.stringify(searchFilterList);
	}
	catch(e)
	{
		misp.com.log("get filter error");
	}

    return json;
};
misp.com.getElementByType = function(type,element)
{
	var domObj = null;
	if(null == element)
	{
	    $("*").each(function()
	    {
	    	 var mispClass = misp.com.getMispClass($(this));
	    	 if(null != mispClass)
	    	 {
	    		 misp.com.log("the misp class ",mispClass);
	    	  }	
			 if(mispClass ==type)
			 {
				 misp.com.log("match the misp class ",mispClass);
				 domObj = $(this);
				 return;
			 }	
	     }  
	     );
	}	
	else
	{
		var parentObj = misp.com.getDomObj(element);
		var inputs = parentObj.find("*");

		inputs.each(function()
		  {
			 var mispClass = misp.com.getMispClass($(this));
			 if(null != mispClass)
	    	 {
	    		 misp.com.log("the misp class",mispClass);
	    	  }	
			 if(mispClass ==type)
			 {
				 misp.com.log("match the misp class ",mispClass);
				 domObj = $(this);
				 return;
			 }	 

		   }  
		);
	}	

	return domObj;
};
 
