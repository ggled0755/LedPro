package cn.fuego.misp.constant;



import java.util.ArrayList;
import java.util.List;

 
/** 
* @ClassName: PayOptionEnum 
* @Description: TODO
* @author Aether
* @date 2015-6-24 下午3:39:48 
*  
*/
public enum PayOptionEnum
{	
	ALI_PAY("支付宝支付",0),  
	WEIXIN_PAY("微信支付",1);
	private String strValue;
	private int intValue;
	
	private PayOptionEnum(String strValue, int intValue)
	{
		this.strValue = strValue;
		this.intValue = intValue;
	}
 
	
	public String getStrValue()
	{
		return strValue;
	}


	public void setStrValue(String strValue)
	{
		this.strValue = strValue;
	}


	public int getIntValue()
	{
		return intValue;
	}


	public void setIntValue(int intValue)
	{
		this.intValue = intValue;
	}


	public static PayOptionEnum getEnumByInt(int intValue)
	{
		for (PayOptionEnum c : PayOptionEnum.values())
		{
			if (intValue == c.intValue)
			{
				return c;
			}
		}
		return null;
	}
	public static PayOptionEnum getEnumByStr(String strValue)
	{
		for (PayOptionEnum c : PayOptionEnum.values())
		{
			if (strValue.equals(c.strValue) )
			{
				return c;
			}
		}
		return null;
	}	 
	
	public static List<String> getAllStr()
	{
		List<String> payList = new ArrayList<String>();
		for (PayOptionEnum c : PayOptionEnum.values())
		{
			payList.add(c.strValue);
		}
		return payList;
	}

}
