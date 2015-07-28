/**   
* @Title: ViewEvalSum.java 
* @Package cn.fuego.led.webservice.up.model.base 
* @Description: TODO
* @author Aether
* @date 2015-7-28 下午5:19:00 
* @version V1.0   
*/ 
package cn.fuego.led.webservice.up.model.base;

import java.io.Serializable;

/** 
 * @ClassName: ViewEvalSum 
 * @Description: TODO
 * @author Aether
 * @date 2015-7-28 下午5:19:00 
 *  
 */
public class ViewEvalSum implements Serializable
{

	private int eval_sum_id;
	private int eval_obj_type;
	private int eval_obj_id;
	private int eval_type;
	private double eval_sum_value;
	private long eval_count;
	private double eval_avg_value;
	private String eval_type_name;
	public int getEval_sum_id()
	{
		return eval_sum_id;
	}
	public void setEval_sum_id(int eval_sum_id)
	{
		this.eval_sum_id = eval_sum_id;
	}
	public int getEval_obj_type()
	{
		return eval_obj_type;
	}
	public void setEval_obj_type(int eval_obj_type)
	{
		this.eval_obj_type = eval_obj_type;
	}
	public int getEval_obj_id()
	{
		return eval_obj_id;
	}
	public void setEval_obj_id(int eval_obj_id)
	{
		this.eval_obj_id = eval_obj_id;
	}
	public int getEval_type()
	{
		return eval_type;
	}
	public void setEval_type(int eval_type)
	{
		this.eval_type = eval_type;
	}
	public double getEval_sum_value()
	{
		return eval_sum_value;
	}
	public void setEval_sum_value(double eval_sum_value)
	{
		this.eval_sum_value = eval_sum_value;
	}
	public long getEval_count()
	{
		return eval_count;
	}
	public void setEval_count(long eval_count)
	{
		this.eval_count = eval_count;
	}
	public double getEval_avg_value()
	{
		return eval_avg_value;
	}
	public void setEval_avg_value(double eval_avg_value)
	{
		this.eval_avg_value = eval_avg_value;
	}
	public String getEval_type_name()
	{
		return eval_type_name;
	}
	public void setEval_type_name(String eval_type_name)
	{
		this.eval_type_name = eval_type_name;
	}
	
	
}
