package cn.fuego.led.webservice.up.model.base;

import java.io.Serializable;

/** 
* @ClassName: CustomerJson 
* @Description: TODO
* @author Aether
* @date 2015-5-27 下午7:46:03 
*  
*/
public class CustomerJson implements Serializable,Cloneable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int user_id;
	private int company_id=0;
	private String nick_name;
	private String head_img;
	private String cellphone;
	private String email;
	private String company_name;
	
	public int getUser_id()
	{
		return user_id;
	}
	public void setUser_id(int user_id)
	{
		this.user_id = user_id;
	}
	public int getCompany_id()
	{
		return company_id;
	}
	public void setCompany_id(int company_id)
	{
		this.company_id = company_id;
	}
	public String getNick_name()
	{
		return nick_name;
	}
	public void setNick_name(String nick_name)
	{
		this.nick_name = nick_name;
	}
	public String getHead_img()
	{
		return head_img;
	}
	public void setHead_img(String head_img)
	{
		this.head_img = head_img;
	}
	public String getCellphone()
	{
		return cellphone;
	}
	public void setCellphone(String cellphone)
	{
		this.cellphone = cellphone;
	}
	public String getEmail()
	{
		return email;
	}
	public void setEmail(String email)
	{
		this.email = email;
	}
	public String getCompany_name()
	{
		return company_name;
	}
	public void setCompany_name(String company_name)
	{
		this.company_name = company_name;
	}



	
}
