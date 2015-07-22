/**   
* @Title: MispApplyStatusEnum.java 
* @Package cn.fuego.misp.constant 
* @Description: TODO
* @author Aether
* @date 2015-6-25 上午11:25:29 
* @version V1.0   
*/ 
package cn.fuego.misp.constant;


/** 
 * @ClassName: MispApplyStatusEnum 
 * @Description: TODO
 * @author Aether
 * @date 2015-6-25 上午11:25:29 
 *  
 */
public enum MispApplyStatusEnum
{

	APPROVING("待审核",0),
	AGREED("已通过",1),
	REFUSED("已拒绝",2);
	private String strValue;
	private int intValue;
	
	private MispApplyStatusEnum(String strValue, int intValue)
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


	public static MispApplyStatusEnum getEnumByInt(int intValue)
	{
		for (MispApplyStatusEnum c : MispApplyStatusEnum.values())
		{
			if (intValue == c.intValue)
			{
				return c;
			}
		}
		return null;
	}
	public static MispApplyStatusEnum getEnumByStr(String strValue)
	{
		for (MispApplyStatusEnum c : MispApplyStatusEnum.values())
		{
			if (strValue.equals(c.strValue) )
			{
				return c;
			}
		}
		return null;
	}	
}
