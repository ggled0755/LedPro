/**   
* @Title: ProjectJson.java 
* @Package cn.fuego.led.webservice.up.model.base 
* @Description: TODO
* @author Aether
* @date 2015-7-8 下午5:56:36 
* @version V1.0   
*/ 
package cn.fuego.led.webservice.up.model.base;

import java.io.Serializable;

/** 
 * @ClassName: ProjectJson 
 * @Description: TODO
 * @author Aether
 * @date 2015-7-8 下午5:56:36 
 *  
 */
public class ProjectJson implements Serializable
{

	private int project_id;	
	private String project_name;
	private String project_note;
	private String project_img;
	
	private int total_catg;
	private int total_num;
	private float total_watt;
	private float total_cost;

	private String create_time;
	private int create_user_id;
	public int getProject_id()
	{
		return project_id;
	}
	public void setProject_id(int project_id)
	{
		this.project_id = project_id;
	}
	public String getProject_name()
	{
		return project_name;
	}
	public void setProject_name(String project_name)
	{
		this.project_name = project_name;
	}
	public String getProject_note()
	{
		return project_note;
	}
	public void setProject_note(String project_note)
	{
		this.project_note = project_note;
	}
	public String getProject_img()
	{
		return project_img;
	}
	public void setProject_img(String project_img)
	{
		this.project_img = project_img;
	}
	public int getTotal_catg()
	{
		return total_catg;
	}
	public void setTotal_catg(int total_catg)
	{
		this.total_catg = total_catg;
	}
	public int getTotal_num()
	{
		return total_num;
	}
	public void setTotal_num(int total_num)
	{
		this.total_num = total_num;
	}
	public float getTotal_watt()
	{
		return total_watt;
	}
	public void setTotal_watt(float total_watt)
	{
		this.total_watt = total_watt;
	}
	public float getTotal_cost()
	{
		return total_cost;
	}
	public void setTotal_cost(float total_cost)
	{
		this.total_cost = total_cost;
	}
	public String getCreate_time()
	{
		return create_time;
	}
	public void setCreate_time(String create_time)
	{
		this.create_time = create_time;
	}
	public int getCreate_user_id()
	{
		return create_user_id;
	}
	public void setCreate_user_id(int create_user_id)
	{
		this.create_user_id = create_user_id;
	}


	
}
