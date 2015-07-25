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
	private String real_name;
	private String nickname;
	private String head_img;
	private String cellphone;
	private String email;
	private String company_name;
	private int customer_sex=0;
	private String birthday;
	
	public int getUser_id()
	{
		return user_id;
	}
	public void setUser_id(int user_id)
	{
		this.user_id = user_id;
	}
	public String getReal_name()
	{
		return real_name;
	}
	public void setReal_name(String real_name)
	{
		this.real_name = real_name;
	}
	public String getNickname()
	{
		return nickname;
	}
	public void setNickname(String nickname)
	{
		this.nickname = nickname;
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
	public int getCustomer_sex()
	{
		return customer_sex;
	}
	public void setCustomer_sex(int customer_sex)
	{
		this.customer_sex = customer_sex;
	}
	public String getBirthday()
	{
		return birthday;
	}
	public void setBirthday(String birthday)
	{
		this.birthday = birthday;
	}

	
	
}
