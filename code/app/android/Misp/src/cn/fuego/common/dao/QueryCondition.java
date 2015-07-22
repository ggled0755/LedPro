/**   
* @Title: QueryCondition.java 
* @Package cn.fuego.misp.domain 
* @Description: TODO
* @author Tang Jun   
* @date 2014-9-24 下午04:31:29 
* @version V1.0   
*/ 
package cn.fuego.common.dao;

import cn.fuego.common.contanst.ConditionTypeEnum;

/** 
 * @ClassName: QueryCondition 
 * @Description: TODO
 * @author Tang Jun
 * @date 2014-9-24 下午04:31:29 
 *  
 */

public class QueryCondition
{
	private ConditionTypeEnum conditionType;
	private String attrName;
	private Object firstValue;
	
	private String typeStr;
  
	public QueryCondition()
	{
		super();
   	}
	public QueryCondition(ConditionTypeEnum conditionType)
	{
		super();
		this.conditionType = conditionType;
  	}
	public QueryCondition(ConditionTypeEnum conditionType, String attrName)
	{
		super();
		this.conditionType = conditionType;
		this.attrName = attrName;
 	}
	
	public QueryCondition(ConditionTypeEnum conditionType, String attrName, Object firstValue)
	{
		super();
		this.conditionType = conditionType;
		this.attrName = attrName;
		this.firstValue = firstValue;
	}
	 
 
	public String getAttrName()
	{
		return attrName;
	}
	public void setAttrName(String attrName)
	{
		this.attrName = attrName;
	}
	public ConditionTypeEnum getOperate()
	{
		return conditionType;
	}
	public void setOperate(ConditionTypeEnum operate)
	{
		this.conditionType = operate;
	}

 
	public Object getFirstValue()
	{
		return firstValue;
	}
	public void setFirstValue(Object firstValue)
	{
		this.firstValue = firstValue;
	}
	
	
	public String getTypeStr()
	{
		if(null != this.conditionType)
		{
			typeStr = this.conditionType.getTypeName();

		}
		return typeStr;
	}
	public void setTypeStr(String typeStr)
	{
		this.typeStr = typeStr;
		this.conditionType = ConditionTypeEnum.getEnumByStr(typeStr);
	}
	@Override
	public String toString()
	{
		return "QueryCondition [conditionType=" + conditionType + ", attrName="
				+ attrName + ", firstValue=" + firstValue + "]";
	}
 

}
