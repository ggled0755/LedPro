/**   
* @Title: SubfolderJson.java 
* @Package cn.fuego.led.webservice.up.model.base 
* @Description: TODO
* @author Aether
* @date 2015-7-22 下午4:15:54 
* @version V1.0   
*/ 
package cn.fuego.led.webservice.up.model.base;

import java.io.Serializable;

import cn.fuego.led.util.treeview.TreeNodeId;
import cn.fuego.led.util.treeview.TreeNodeLabel;
import cn.fuego.led.util.treeview.TreeNodePid;

/** 
 * @ClassName: SubfolderJson 
 * @Description: TODO
 * @author Aether
 * @date 2015-7-22 下午4:15:54 
 *  
 */
public class SubfolderJson implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@TreeNodeId
	private int subfolder_id;
	private int project_id;
	@TreeNodeLabel
	private String subfolder_name;
	@TreeNodePid
	private int subfolder_parent_id;
	private String subfolder_note;
	private int subfolder_level;

	
	public int getSubfolder_id()
	{
		return subfolder_id;
	}
	public void setSubfolder_id(int subfolder_id)
	{
		this.subfolder_id = subfolder_id;
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

	
}
