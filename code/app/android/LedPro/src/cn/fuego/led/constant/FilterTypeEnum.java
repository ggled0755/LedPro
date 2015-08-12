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
* @ClassName: FilterTypeEnum 
* @Description: TODO
* @author Aether
* @date 2015-8-6 下午3:05:49 
*  
*/
public enum FilterTypeEnum
{

	INTUT("include",0),  
	SELECT("equal",1),
	GROUP("in",2),
	SEEK("between",3);
	private String strValue;
	private int intValue;
	
	private FilterTypeEnum(String strValue, int intValue)
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


	public static FilterTypeEnum getEnumByInt(int intValue)
	{
		for (FilterTypeEnum c : FilterTypeEnum.values())
		{
			if (intValue == c.intValue)
			{
				return c;
			}
		}
		return null;
	}
	public static FilterTypeEnum getEnumByStr(String strValue)
	{
		for (FilterTypeEnum c : FilterTypeEnum.values())
		{
			if (strValue.equals(c.strValue) )
			{
				return c;
			}
		}
		return null;
	}	
}
