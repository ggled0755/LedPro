/**   
* @Title: MISPClientVersionEnum.java 
* @Package cn.fuego.misp.constant 
* @Description: TODO
* @author Aether
* @date 2015-1-16 下午9:54:29 
* @version V1.0   
*/ 
package cn.fuego.misp.constant;


/** 
 * @ClassName: MISPClientVersionEnum 
 * @Description: TODO
 * @author Aether
 * @date 2015-1-16 下午9:54:29 
 *  
 */
public enum MISPClientVersionEnum
{
	VERSION_OLD(0,"ANDRIOD"),
	VERSION_NEW(1,"WEB");
	
	/**
	 * @param intValue
	 * @param strValue
	 */
	private MISPClientVersionEnum(int intValue, String strValue)
	{
		this.intValue = intValue;
		this.strValue = strValue;
	}
	private int intValue;
	private String strValue;
	public int getIntValue()
	{
		return intValue;
	}
	public String getStrValue()
	{
		return strValue;
	}
	public static MISPClientVersionEnum getEnumByInt(int intValue)
	{
		for (MISPClientVersionEnum c : MISPClientVersionEnum.values())
		{
			if (intValue == c.intValue)
			{
				return c;
			}
		}
		return null;
	}	
}
