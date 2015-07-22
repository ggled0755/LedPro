package cn.fuego.misp.service.http;

import android.os.Message;
import cn.fuego.misp.constant.MispErrorCode;
import cn.fuego.misp.webservice.json.MispBaseRspJson;

public class MispHttpMessage 
{
	private Message message = new Message();

	public Message getMessage()
	{
		return message;
	}

	public void setMessage(Message message)
	{
		this.message = message;
	}
	
	public boolean isSuccess()
	{
		if(MispErrorCode.SUCCESS != message.what)
		{
			return false;
		}
		if(MispErrorCode.SUCCESS != getErrorCode())
		{
			return false;
		}
		return true;
		 
	}
	
	public boolean isNetSuccess()
	{
		if(MispErrorCode.SUCCESS != message.what)
		{
			return false;
		}
		return true;
	}

	public int getErrorCode()
	{
		MispBaseRspJson rsp = (MispBaseRspJson) message.obj;
		if(null != rsp)
		{
			return rsp.getErrorCode();
			
		}
		return 0;//MISPErrorMessageConst.NET_FAIL;
	}
	

}
