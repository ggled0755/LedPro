/**   
* @Title: ViewSubfolderJson.java 
* @Package cn.fuego.led.webservice.up.model.base 
* @Description: TODO
* @author Aether
* @date 2015-7-22 下午8:32:54 
* @version V1.0   
*/ 
package cn.fuego.led.webservice.up.model.base;

import java.io.Serializable;

/** 
 * @ClassName: ViewSubfolderJson 
 * @Description: TODO
 * @author Aether
 * @date 2015-7-22 下午8:32:54 
 *  
 */
public class ViewSubfolderJson implements Serializable
{
	private int subfolder_detail_id;
	private int subfolder_id;
	private int product_id;
	private int product_num;
	private int project_id;
	private String subfolder_name;
	private int subfolder_parent_id;
	private String subfolder_note;
	private int subfolder_level;
	private String product_name;
	public int getSubfolder_detail_id()
	{
		return subfolder_detail_id;
	}
	public void setSubfolder_detail_id(int subfolder_detail_id)
	{
		this.subfolder_detail_id = subfolder_detail_id;
	}
	public int getSubfolder_id()
	{
		return subfolder_id;
	}
	public void setSubfolder_id(int subfolder_id)
	{
		this.subfolder_id = subfolder_id;
	}
	public int getProduct_id()
	{
		return product_id;
	}
	public void setProduct_id(int product_id)
	{
		this.product_id = product_id;
	}
	public int getProduct_num()
	{
		return product_num;
	}
	public void setProduct_num(int product_num)
	{
		this.product_num = product_num;
	}
	public int getProject_id()
	{
		return project_id;
	}
	public void setProject_id(int project_id)
	{
		this.project_id = project_id;
	}
	public String getSubfolder_name()
	{
		return subfolder_name;
	}
	public void setSubfolder_name(String subfolder_name)
	{
		this.subfolder_name = subfolder_name;
	}
	public int getSubfolder_parent_id()
	{
		return subfolder_parent_id;
	}
	public void setSubfolder_parent_id(int subfolder_parent_id)
	{
		this.subfolder_parent_id = subfolder_parent_id;
	}
	public String getSubfolder_note()
	{
		return subfolder_note;
	}
	public void setSubfolder_note(String subfolder_note)
	{
		this.subfolder_note = subfolder_note;
	}
	public int getSubfolder_level()
	{
		return subfolder_level;
	}
	public void setSubfolder_level(int subfolder_level)
	{
		this.subfolder_level = subfolder_level;
	}
	public String getProduct_name()
	{
		return product_name;
	}
	public void setProduct_name(String product_name)
	{
		this.product_name = product_name;
	}
	@Override
	public String toString()
	{
		return "ViewSubfolderJson [subfolder_detail_id=" + subfolder_detail_id
				+ ", subfolder_id=" + subfolder_id + ", product_id="
				+ product_id + ", product_num=" + product_num + ", project_id="
				+ project_id + ", subfolder_name=" + subfolder_name
				+ ", subfolder_parent_id=" + subfolder_parent_id
				+ ", subfolder_note=" + subfolder_note + ", subfolder_level="
				+ subfolder_level + ", product_name=" + product_name + "]";
	}
	
	
	
}
