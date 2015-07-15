var misp = misp || {};
misp.parser = {};


misp.parser.log  = function (message,obj)
{
	var logged = false;
	if(logged)
	{
		misp.util.log(message,obj);

	}	
};
misp.parser.parse = function(id)
{
	if(null == id ||  ""==id)
	{
		misp.parser.log("parse all");
		misp.parser.parseAll();
	}
	else
	{
	    $(id).each(function()
	    	    {
	    	    	 misp.parser.dispatch($(this));
	    	    }  
	    );
	}
	

};
misp.parser.parseAll= function()
{
	$("[misp-class]").each(function()
	    {
	    	misp.parser.dispatch($(this));
	    }  
    );
    
};


misp.parser.dispatch = function(element)
{
    var mispClass = misp.com.getMispClass(element);
    
	var obj = misp.com.getOptions(element);
	if(null != mispClass)
	{
		misp.parser.log(mispClass);
	}	
    if(mispClass == "misp.combobox")
    {
    	misp.parser.log("parse combobox");
    	misp.form.combobox.load(element,obj);
    }
    else if(mispClass == "misp.table")
    {
    	misp.parser.log("parse table");
    	misp.table.load(element,obj);
    }
    //else if(mispClass == "misp.toolbar")
    //{
    	//misp.parser.log("parse toolbar");
    	//misp.toolbar.load(element,obj);
    //}
    else if (mispClass == "misp.treement")
    {
        misp.parser.log("parse treement");
        misp.treement.load(element, obj);
    }
};
 