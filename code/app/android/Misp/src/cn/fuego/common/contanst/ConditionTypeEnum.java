/**   
* @Title: QueryCondition.java 
* @Package cn.fuego.misp.contanst 
* @Description: TODO
* @author Tang Jun   
* @date 2014-9-24 下午04:11:10 
* @version V1.0   
*/ 
package cn.fuego.common.contanst;

/** 
 * @ClassName: QueryCondition 
 * @Description: TODO
 * @author Tang Jun
 * @date 2014-9-24 下午04:11:10 
 *  
 */

public enum  ConditionTypeEnum
{
	INCLUDLE("include",1),
	EXCLUDLE("exclude",2), 
	EQUAL("equal",3), 
	NOT_EQUAL("not_equal",4), 
	BIGER("biger",5), 
	BIGER_EQ("biger_eq",6),
	LOWER("lower",7), 
	LOWER_EQ("lower_eq",8),  
	IN("in",10),
	FALSE("false",11),
	DESC_ORDER("desc",12),
	
	ASC_ORDER("asc",13),
	LIKE_LEFT("like_left",14),
	LIKE_RIGHT("like_right",15);
    // 成员变量  
    private String typeName;  
    private int typeValue;
    public static String ALL ="ALL";
    private ConditionTypeEnum(String type,int typeValue)
    {
    	this.typeName = type;
    	this.typeValue = typeValue;
    }
	public String getTypeName()
	{
		return typeName;
	}
	public int getTypeValue()
	{
		return typeValue;
	}
 
	public static ConditionTypeEnum getEnumByStr(String strValue)
	{
		for (ConditionTypeEnum c : ConditionTypeEnum.values())
		{
			if (strValue.equals(c.typeName))
			{
				return c;
			}
		}
		return null;
	} 
  
}
