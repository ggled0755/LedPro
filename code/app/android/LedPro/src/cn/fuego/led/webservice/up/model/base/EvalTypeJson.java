/**   
* @Title: EvalTypeJson.java 
* @Package cn.fuego.led.webservice.up.model.base 
* @Description: TODO
* @author Aether
* @date 2015-7-28 下午12:27:35 
* @version V1.0   
*/ 
package cn.fuego.led.webservice.up.model.base;

import java.io.Serializable;

/** 
 * @ClassName: EvalTypeJson 
 * @Description: TODO
 * @author Aether
 * @date 2015-7-28 下午12:27:35 
 *  
 */
public class EvalTypeJson implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int eval_type_id;
	private String eval_type_name;
	private int eval_obj_type;
	public int getEval_type_id()
	{
		return eval_type_id;
	}
	public void setEval_type_id(int eval_type_id)
	{
		this.eval_type_id = eval_type_id;
	}
	public String getEval_type_name()
	{
		return eval_type_name;
	}
	public void setEval_type_name(String eval_type_name)
	{
		this.eval_type_name = eval_type_name;
	}
	public int getEval_obj_type()
	{
		return eval_obj_type;
	}
	public void setEval_obj_type(int eval_obj_type)
	{
		this.eval_obj_type = eval_obj_type;
	}
	
	

}
