package cn.fuego.led.webservice.up.model.base;

import java.io.Serializable;

import cn.fuego.common.util.format.DateUtil;
import cn.fuego.led.cache.AppCache;

public class EvalJson implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int eval_id;
	private int eval_obj_type;//能源消耗，产品质量，用户评价等
	private int eval_obj_id;
	private int eval_type;
	private float eval_value;
	private String eval_content;
	private int user_id=AppCache.getInstance().getUser().getUser_id();
	private String eval_time=DateUtil.getCurrentDateTimeStr();
	
	public int getEval_id()
	{
		return eval_id;
	}
	public void setEval_id(int eval_id)
	{
		this.eval_id = eval_id;
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
	public float getEval_value()
	{
		return eval_value;
	}
	public void setEval_value(float eval_value)
	{
		this.eval_value = eval_value;
	}
	public String getEval_content()
	{
		return eval_content;
	}
	public void setEval_content(String eval_content)
	{
		this.eval_content = eval_content;
	}
	public int getUser_id()
	{
		return user_id;
	}
	public void setUser_id(int user_id)
	{
		this.user_id = user_id;
	}
	public String getEval_time()
	{
		return eval_time;
	}
	public void setEval_time(String eval_time)
	{
		this.eval_time = eval_time;
	}
	public int getEval_type()
	{
		return eval_type;
	}
	public void setEval_type(int eval_type)
	{
		this.eval_type = eval_type;
	}
	

	
}
