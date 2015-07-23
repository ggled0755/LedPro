/**   
* @Title: Subfolder_detail.java 
* @Package cn.fuego.led.webservice.up.model.base 
* @Description: TODO
* @author Aether
* @date 2015-7-9 下午3:42:34 
* @version V1.0   
*/ 
package cn.fuego.led.webservice.up.model.base;

import java.io.Serializable;

/** 
 * @ClassName: Subfolder_detail 
 * @Description: TODO
 * @author Aether
 * @date 2015-7-9 下午3:42:34 
 *  
 */
public class SubfolderDetailJson implements Serializable
{

	private int subfolder_detail_id;
	private int subfolder_id;
	private int product_id;
	private int product_num;
	
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
	
	
	
}
