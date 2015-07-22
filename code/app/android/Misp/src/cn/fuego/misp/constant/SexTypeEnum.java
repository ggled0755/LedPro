/**   
* @Title: SexTypeEnum.java 
* @Package cn.fuego.misp.constant 
* @Description: TODO
* @author Aether
* @date 2015-6-24 下午3:32:31 
* @version V1.0   
*/ 
package cn.fuego.misp.constant;

import java.util.ArrayList;
import java.util.List;

/** 
 * @ClassName: SexTypeEnum 
 * @Description: TODO
 * @author Aether
 * @date 2015-6-24 下午3:32:31 
 *  
 */
public enum SexTypeEnum
{
	MAN("男",0),  
	WOMAN("女",1);
	private String strValue;
	private int intValue;
	
	private SexTypeEnum(String strValue, int intValue)
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


	public static SexTypeEnum getEnumByInt(int intValue)
	{
		for (SexTypeEnum c : SexTypeEnum.values())
		{
			if (intValue == c.intValue)
			{
				return c;
			}
		}
		return null;
	}
	public static SexTypeEnum getEnumByStr(String strValue)
	{
		for (SexTypeEnum c : SexTypeEnum.values())
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
		List<String> sexList = new ArrayList<String>();
		for (SexTypeEnum c : SexTypeEnum.values())
		{
			sexList.add(c.strValue);
		}
		return sexList;
	}

}
