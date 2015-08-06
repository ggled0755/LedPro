/**   
* @Title: OrderFlagEnum.java 
* @Package cn.fuego.led.constant 
* @Description: TODO
* @author Aether
* @date 2015-7-20 下午8:42:32 
* @version V1.0   
*/ 
package cn.fuego.led.constant;

import cn.fuego.common.util.validate.ValidatorUtil;

/** 
* @ClassName: FilterTypeEnum 
* @Description: TODO
* @author Aether
* @date 2015-8-6 下午3:05:49 
*  
*/
public enum SeekColorEnum
{

	DEFAULT("default",0),  
	GREEN("green",1),
	ORANGE("orange",2),
	RED("red",3),
	YELLOE("yellow",4);
	
	private String strValue;
	private int intValue;
	
	private SeekColorEnum(String strValue, int intValue)
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


	public static SeekColorEnum getEnumByInt(int intValue)
	{
		for (SeekColorEnum c : SeekColorEnum.values())
		{
			if (intValue == c.intValue)
			{
				return c;
			}
		}
		return null;
	}
	public static SeekColorEnum getEnumByStr(String strValue)
	{
		if(ValidatorUtil.isEmpty(strValue))
		{
			return DEFAULT;
		}
		for (SeekColorEnum c : SeekColorEnum.values())
		{
			if (strValue.equals(c.strValue) )
			{
				return c;
			}
		}
		return null;
	}	
}
