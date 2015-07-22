/**   
* @Title: EnumJson.java 
* @Package cn.fuego.led.webservice.up.model.base 
* @Description: TODO
* @author Aether
* @date 2015-7-20 下午12:31:52 
* @version V1.0   
*/ 
package cn.fuego.led.webservice.up.model.base;

import java.io.Serializable;

/** 
 * @ClassName: EnumJson 
 * @Description: TODO
 * @author Aether
 * @date 2015-7-20 下午12:31:52 
 *  
 */
public class EnumJson implements Serializable
{

	private int enum_id;
	private String table_name;
	private String field_name;
	private int enum_sort_id;
	private int enum_key;
	private String enum_value;
	private int company_id;
	public int getEnum_id()
	{
		return enum_id;
	}
	public void setEnum_id(int enum_id)
	{
		this.enum_id = enum_id;
	}
	public String getTable_name()
	{
		return table_name;
	}
	public void setTable_name(String table_name)
	{
		this.table_name = table_name;
	}
	public String getField_name()
	{
		return field_name;
	}
	public void setField_name(String field_name)
	{
		this.field_name = field_name;
	}
	public int getEnum_sort_id()
	{
		return enum_sort_id;
	}
	public void setEnum_sort_id(int enum_sort_id)
	{
		this.enum_sort_id = enum_sort_id;
	}
	public int getEnum_key()
	{
		return enum_key;
	}
	public void setEnum_key(int enum_key)
	{
		this.enum_key = enum_key;
	}
	public String getEnum_value()
	{
		return enum_value;
	}
	public void setEnum_value(String enum_value)
	{
		this.enum_value = enum_value;
	}
	public int getCompany_id()
	{
		return company_id;
	}
	public void setCompany_id(int company_id)
	{
		this.company_id = company_id;
	}
	
	
}
