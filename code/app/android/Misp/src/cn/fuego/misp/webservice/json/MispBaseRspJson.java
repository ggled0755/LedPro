package cn.fuego.misp.webservice.json;

import java.io.Serializable;

import org.codehaus.jackson.type.TypeReference;

import cn.fuego.common.util.format.JsonConvert;
import cn.fuego.common.util.validate.ValidatorUtil;
import cn.fuego.misp.constant.MispErrorCode;


/**
 * 
* @ClassName: BaseJsonRsp 
* @Description: TODO
* @author Tang Jun
* @date 2014-10-20 上午10:57:45 
*
 */
public class MispBaseRspJson implements Serializable
{

	protected int errorCode = MispErrorCode.SUCCESS;
	protected String errorMsg;
	protected Object obj;
	public int getErrorCode()
	{
		return errorCode;
	}
	public void setErrorCode(int errorCode)
	{
		this.errorCode = errorCode;
	}
	public String getErrorMsg()
	{
		String err = MispErrorCode.getMessageByErrorCode(errorCode);
		if(ValidatorUtil.isEmpty(err))
		{
			return String.valueOf(errorCode);
		}
		return err;
	}
	public void setErrorMsg(String errorMsg)
	{
		this.errorMsg = errorMsg;
	}
	public Object getObj()
	{
		return obj;
	}
	public void setObj(Object obj)
	{
		this.obj = obj;
	}
    public <T> T GetReqCommonField(TypeReference<T> clazz)
    {
         T obj = null;
         obj  = JsonConvert.jsonToObject(JsonConvert.ObjectToJson(this.getObj()),clazz);

        return obj;
    }
    
    public <T> T GetReqCommonField(Class<T> clazz)
    {
         T obj = null;
         obj  = JsonConvert.jsonToObject(JsonConvert.ObjectToJson(this.getObj()),clazz);

        return obj;
    }
    
    public boolean isSucess()
    {
    	if(this.errorCode == MispErrorCode.SUCCESS)
    	{
    		return true;
    	}
    	return false;
    }
	@Override
	public String toString()
	{
		return "MispBaseRspJson [errorCode=" + errorCode + ", errorMsg="
				+ errorMsg + ", obj=" + obj + "]";
	}

	
	
}
