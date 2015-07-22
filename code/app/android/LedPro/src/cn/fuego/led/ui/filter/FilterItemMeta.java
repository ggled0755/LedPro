/**   
* @Title: FilterItemMeta.java 
* @Package cn.fuego.led.ui.filter 
* @Description: TODO
* @author Aether
* @date 2015-7-20 上午9:28:11 
* @version V1.0   
*/ 
package cn.fuego.led.ui.filter;

import java.io.Serializable;

/** 
 * @ClassName: FilterItemMeta 
 * @Description: TODO
 * @author Aether
 * @date 2015-7-20 上午9:28:11 
 *  
 */
public class FilterItemMeta implements Serializable
{

	private int itemType;//
	private String title;
	private String content;
	private Integer id=0;
	private String minValue;
	private String maxValue;
	private String fieldName;
	private Integer theme=0;
	public int getItemType()
	{
		return itemType;
	}
	public void setItemType(int itemType)
	{
		this.itemType = itemType;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public String getMinValue()
	{
		return minValue;
	}
	public void setMinValue(String minValue)
	{
		this.minValue = minValue;
	}
	public String getMaxValue()
	{
		return maxValue;
	}
	public void setMaxValue(String maxValue)
	{
		this.maxValue = maxValue;
	}
	public String getContent()
	{

		return content;
	}
	public void setContent(String content)
	{
		this.content = content;
	}
	public Integer getId()
	{
		return id;
	}
	public void setId(Integer id)
	{
		this.id = id;
	}
	public String getFieldName()
	{
		return fieldName;
	}
	public void setFieldName(String fieldName)
	{
		this.fieldName = fieldName;
	}
	public Integer getTheme()
	{
		return theme;
	}
	public void setTheme(Integer theme)
	{
		this.theme = theme;
	}
	
	
}
