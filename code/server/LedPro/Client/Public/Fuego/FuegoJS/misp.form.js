var misp = misp || {};
misp.form= {};


misp.form.log  = function (message,obj)
{
	var logged = false;
	if(logged)
	{
		if(null != obj)
		{
			alert(JSON.stringify(message)+JSON.stringify(obj));
		}
		else
		{
			alert(JSON.stringify(message));
		}
	}	
};
misp.form.form= {};
misp.form.form.load = function(element,data)
{
	 var domObj = misp.com.getDomObj(element);
	 if(null == domObj)
	 {
		 misp.util.alert("form element is null ");
		 return;
	 }
	 misp.form.log("form data is ",data);
	 if(null != data) {
		 domObj.form('load', data);
	 }
  	 var inputs = domObj.find("*");
		inputs.each(function()
		  {
			 var mispClass = misp.com.getMispClass($(this));
			 if(mispClass == "misp.combobox")
			 { 
				 misp.form.log("match the misp class ",mispClass);
				 misp.form.combobox.load($(this));
			 }
			 else if(mispClass == "misp.radio")
			 {
				 misp.form.log("match the misp class ",mispClass);
				 misp.form.radio.load($(this),data);
			 }
			 else if(mispClass == "misp.img")
			 {
				 var imgDispElement = misp.com.getElementByType("misp.imgDisp",$(this));
				 var imgNameElement = misp.com.getElementByType("misp.imgName",$(this));
				 imgDispElement.attr('src', misp.util.getImgUrl() + imgNameElement.val());
			 }
			 else if(mispClass == "misp.imglist")
			 {
				 misp.form.log("match the misp class ",mispClass);
				 misp.form.imglist.load($(this));
			 }

		   }  
		);
};
//加载form中的combobox
misp.form.combobox= {};
misp.form.combobox.load = function(element,data)
{     
	
	  var domObj = misp.com.getDomObj(element);
	  misp.form.log(data);
  	  if(null == data)
	  {
		  data = misp.com.getOptions(domObj);
	  }
	  if(null != data['url'])
	  {
		  if(data['url'].indexOf("Enum")>-1)
		  {
			  data.queryParams = misp.util.buildFilterJson({typeStr:"equal",firstValue:data.fieldName,attrName:"field_name"});
		  }
		  data['url'] = misp.util.buildUrl(data['url']);
	  }
	  data.panelHeight = data.panelHeight || 180;
	  data.editable = data.editable || false;
	  
      var selectedValue = domObj.val();
 	  data.onLoadSuccess = function()
	  {
		  var options =  $(this).combobox('options');
		  var data = $(this).combobox('getData');
		  var isSelected = false;
		  for(var i=0;i<data.length;i++)
		  {
			  
			  if(selectedValue == data[i][options.valueField])
			  {
 				  isSelected = true;
				  break;
			  }	  
		  }	  
		  if(!isSelected)
		  {
		      if (data.length > 0) 
		      { 
				  $(this).combobox('select',data[0][options.valueField]);
		      }  
		  }	 
		  else
		  {
			  $(this).combobox('select',selectedValue);
		  }	  

	     
	  };
	  
	  domObj.combobox(data);
};
//加载form中的radio
misp.form.radio = {};
misp.form.radio.load = function(element,data)
{
	var radioElement = misp.com.getDomObj(element);
	var inputs = radioElement.find("input");
	var inputName = "";
	inputs.each(function () {
		inputName = $(this).attr("name");
    });
	radioElement.radio('check',data[inputName]);
};
//加载form中的imglist
misp.form.imglist = {};
misp.form.imglist.load = function(element,data)
{
	var imgListElement = misp.com.getDomObj(element);
	misp.form.log(data);
	if(null == data)
	{
		data = misp.com.getOptions(imgListElement);
	}
	var imgNameElement = misp.com.getElementByType("misp.imgName",imgListElement);
	var imgUlElement = misp.com.getElementByType("misp.imgUl",imgListElement);
	var imgNameJson = imgNameElement.textbox('getValue');
	var imgNameList = [];
	if("" != imgNameJson)
	{
		imgNameList = misp.util.jsonToObj(imgNameJson);
	}
	for(var i=0;i<imgNameList.length;i++){
		if("" != imgNameList[i])
		{
			var src = misp.util.getImgUrl() + imgNameList[i];
			if(data['editable'] == false)
			{
				var newElement = "<li style=\"height:110px;\"><img class='content'  src='" + src + "' style=\"width:100px;height:100px;\"></li>";
				imgUlElement.append(newElement);
			}
			else
			{
				var newElement = "<li style=\"height:110px;\"><img class='content'  src='" + src + "' style=\"width:100px;height:100px;\"><img class='button' src='"+swfUploadPath+"/images/fancy_close.png"+"'></li>";
			    imgUlElement.append(newElement);
			    //缩略图处理
			    var imgThumbElement = misp.com.getElementByType("misp.imgThumbName",imgListElement);
				$("img.button").last().bind("click", function(){
			    	var src = $(this).siblings('img').attr('src');
			    	var img = src.replace(misp.util.getImgUrl(),'');
			    	var imgNameJson = imgNameElement.textbox('getValue');
			    	var imgNameList = [];
			    	if("" != imgNameJson)
			    	{
			    		imgNameList = misp.util.jsonToObj(imgNameJson);
			    	}
			    	misp.util.submit({
			    	  data : img,
			    	  url : "MispFile|ImgDelete",
			    	  success:function(obj){
			    		  imgNameList.remove(obj);
			    		  imgNameJson = misp.util.objToJson(imgNameList);
			    		  imgNameElement.textbox('setValue',imgNameJson);
			    		  //缩略图处理
			    		  if(null != imgThumbElement)
			    		  {
			    			  var imgThumbJson = imgThumbElement.textbox('getValue');
			    			  var imgThumbList = [];
			    			  if("" != imgThumbJson)
			    			  {
			    				  imgThumbList = misp.util.jsonToObj(imgThumbJson);
			    			  }
			    			  var imgNames = obj.split(".");
			    			  var thumbObj = imgNames[0] + "_small." + imgNames[1];
			    			  imgThumbList.remove(thumbObj);
			    			  imgThumbJson = misp.util.objToJson(imgThumbList);
			    			  imgThumbElement.textbox('setValue',imgThumbJson);
			    		  }
			    	  }
			    	});
			    	$(this).parent().remove();
			    });
			}
		}
	}
};

//扩展comboTree LoadFilter方法 ，参数说明参考tree扩展loadFilter方法
$.fn.combotree.defaults.loadFilter = function (data, parent) {
  var opt = $(this).data().tree.options;
  if (opt.parentField) {
      var nodeList = misp.tree.getRootNode(data, opt);
      var treeObj = misp.tree.getTreeObjList(data, nodeList, opt);
      return treeObj;
  }
  return data;
};
misp.form.combotree= {};
//通用加载combotree 方法，参数说明参考treeload方法
misp.form.combotree.load = function (element,data) {
	var domObj = misp.com.getDomObj(element);
	misp.form.log(data);
	if(null == data)
	{
		data = misp.com.getOptions(domObj);
	}
	if(null != data.url)
	{
		data.url = misp.util.buildUrl(data.url);
	}
	  data.panelHeight = data.panelHeight || 180;
	  data.editable = data.editable || false;
	  data.checkbox = data.checkbox || false;                        //默认不显示勾选框
	  data.idField = data.idField || 'id';                          //此默认值与后台treeModel字段相对应
	  data.parentField = data.parentField || 'parent_id';           //此默认值与后台treeModel字段相对应
	  data.textField = data.textField || 'text';                    //此默认值与后台treeModel字段相对应
	  data.iconClsField = data.iconClsField || 'iconCls';           //此默认值与后台treeModel字段相对应
	  data.checkedField = data.checkedField || 'checked';           //此默认值与后台treeModel字段相对应
	  data.queryParams = data.queryParams || '';
	  
	  var selectedValue = domObj.val();
	  data.onLoadSuccess = function()
	  {
		  var tree = domObj.combotree('tree');			//获取tree对象
		  var selectID;
		  if("" != selectedValue)
		  {
			  selectID = selectedValue;
		  }
		  else
		  {
			  var rootData = tree.tree('getRoots');
			  selectID = rootData[0]["id"];
		  }
		  domObj.combotree('setValue', selectID); 
	  };
	  domObj.combotree(data);
};
