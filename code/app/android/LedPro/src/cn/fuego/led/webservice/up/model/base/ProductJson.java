/**   
* @Title: ProductJson.java 
* @Package cn.fuego.led.webservice.up.model.base 
* @Description: TODO
* @author Aether
* @date 2015-7-7 上午11:34:27 
* @version V1.0   
*/ 
package cn.fuego.led.webservice.up.model.base;

import java.io.Serializable;

/** 
 * @ClassName: ProductJson 
 * @Description: TODO
 * @author Aether
 * @date 2015-7-7 上午11:34:27 
 *  
 */
public class ProductJson implements Serializable
{

	private int product_id;
	private String product_name;
	private String product_code;
	private String product_desp;
	private float product_score;  
	private float product_price;
	private String prodcut_catg; 
	private String use_location;     
	private String mounting_base;    
	private String sub_catg;		  
	private String product_size;
	private String lumen_output;
	private String input_watt;
	
	private String manufacture;
	private String warranty;
	private String certification;
	private String product_img;
	private String product_img_sm1;
	private String product_img_sm2;
	private String product_img_sm3;
	private String product_qr_code;
	private String shop_id;
	private String create_time;
	private int create_user_id;
	private String related_product_ids;
	public int getProduct_id()
	{
		return product_id;
	}
	public void setProduct_id(int product_id)
	{
		this.product_id = product_id;
	}
	public String getProduct_name()
	{
		return product_name;
	}
	public void setProduct_name(String product_name)
	{
		this.product_name = product_name;
	}
	public String getProduct_code()
	{
		return product_code;
	}
	public void setProduct_code(String product_code)
	{
		this.product_code = product_code;
	}
	public String getProduct_desp()
	{
		return product_desp;
	}
	public void setProduct_desp(String product_desp)
	{
		this.product_desp = product_desp;
	}
	public float getProduct_score()
	{
		return product_score;
	}
	public void setProduct_score(float product_score)
	{
		this.product_score = product_score;
	}
	public String getProdcut_catg()
	{
		return prodcut_catg;
	}
	public void setProdcut_catg(String prodcut_catg)
	{
		this.prodcut_catg = prodcut_catg;
	}
	public String getUse_location()
	{
		return use_location;
	}
	public void setUse_location(String use_location)
	{
		this.use_location = use_location;
	}
	public String getMounting_base()
	{
		return mounting_base;
	}
	public void setMounting_base(String mounting_base)
	{
		this.mounting_base = mounting_base;
	}
	public String getSub_catg()
	{
		return sub_catg;
	}
	public void setSub_catg(String sub_catg)
	{
		this.sub_catg = sub_catg;
	}
	public String getProduct_size()
	{
		return product_size;
	}
	public void setProduct_size(String product_size)
	{
		this.product_size = product_size;
	}
	public String getLumen_output()
	{
		return lumen_output;
	}
	public void setLumen_output(String lumen_output)
	{
		this.lumen_output = lumen_output;
	}
	public String getInput_watt()
	{
		return input_watt;
	}
	public void setInput_watt(String input_watt)
	{
		this.input_watt = input_watt;
	}
	public String getManufacture()
	{
		return manufacture;
	}
	public void setManufacture(String manufacture)
	{
		this.manufacture = manufacture;
	}
	public String getWarranty()
	{
		return warranty;
	}
	public void setWarranty(String warranty)
	{
		this.warranty = warranty;
	}
	public String getCertification()
	{
		return certification;
	}
	public void setCertification(String certification)
	{
		this.certification = certification;
	}
	public String getProduct_img()
	{
		return product_img;
	}
	public void setProduct_img(String product_img)
	{
		this.product_img = product_img;
	}
	public String getProduct_img_sm1()
	{
		return product_img_sm1;
	}
	public void setProduct_img_sm1(String product_img_sm1)
	{
		this.product_img_sm1 = product_img_sm1;
	}
	public String getProduct_img_sm2()
	{
		return product_img_sm2;
	}
	public void setProduct_img_sm2(String product_img_sm2)
	{
		this.product_img_sm2 = product_img_sm2;
	}
	public String getProduct_img_sm3()
	{
		return product_img_sm3;
	}
	public void setProduct_img_sm3(String product_img_sm3)
	{
		this.product_img_sm3 = product_img_sm3;
	}
	public String getProduct_qr_code()
	{
		return product_qr_code;
	}
	public void setProduct_qr_code(String product_qr_code)
	{
		this.product_qr_code = product_qr_code;
	}
	public String getShop_id()
	{
		return shop_id;
	}
	public void setShop_id(String shop_id)
	{
		this.shop_id = shop_id;
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
	public String getRelated_product_ids()
	{
		return related_product_ids;
	}
	public void setRelated_product_ids(String related_product_ids)
	{
		this.related_product_ids = related_product_ids;
	}
	public float getProduct_price()
	{
		return product_price;
	}
	public void setProduct_price(float product_price)
	{
		this.product_price = product_price;
	}
	
	

}
