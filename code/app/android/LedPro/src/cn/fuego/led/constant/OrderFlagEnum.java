/**   
* @Title: OrderFlagEnum.java 
* @Package cn.fuego.led.constant 
* @Description: TODO
* @author Aether
* @date 2015-7-20 下午8:42:32 
* @version V1.0   
*/ 
package cn.fuego.led.constant;


/** 
 * @ClassName: OrderFlagEnum 
 * @Description: TODO
 * @author Aether
 * @date 2015-7-20 下午8:42:32 
 *  
 */
public enum OrderFlagEnum
{

	NULL("",0),  
	ASC("asc",1),
	DESC("desc",2);
	private String strValue;
	private int intValue;
	
	private OrderFlagEnum(String strValue, int intValue)
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


	public static OrderFlagEnum getEnumByInt(int intValue)
	{
		for (OrderFlagEnum c : OrderFlagEnum.values())
		{
			if (intValue == c.intValue)
			{
				return c;
			}
		}
		return null;
	}
	public static OrderFlagEnum getEnumByStr(String strValue)
	{
		for (OrderFlagEnum c : OrderFlagEnum.values())
		{
			if (strValue.equals(c.strValue) )
			{
				return c;
			}
		}
		return null;
	}	
}
