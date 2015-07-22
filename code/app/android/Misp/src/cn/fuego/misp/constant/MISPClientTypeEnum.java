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
* @ClassName: MISPClientTypeEnum 
* @Description: TODO
* @author Aether
* @date 2015-5-18 下午5:37:02 
*  
*/
public enum MISPClientTypeEnum
{
	WEB_BS(0,"WEB"),
	WEB_CS(1,"CS"),
	WEB_SUB_SYS(2,"SUB_SYS"),
	APP_ANDROID(3,"ANDROID"),
	APP_IOS(4,"IOS");
	
	/**
	 * @param intValue
	 * @param strValue
	 */
	private MISPClientTypeEnum(int intValue, String strValue)
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
	public static MISPClientTypeEnum getEnumByInt(int intValue)
	{
		for (MISPClientTypeEnum c : MISPClientTypeEnum.values())
		{
			if (intValue == c.intValue)
			{
				return c;
			}
		}
		return null;
	}	
}
