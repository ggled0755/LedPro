/**   
* @Title: ConfigInfo.java 
* @Package cn.fuego.uush.dao 
* @Description: TODO
* @author Aether
* @date 2015-7-27 上午10:25:01 
* @version V1.0   
*/ 
package cn.fuego.led.dao;

import java.io.Serializable;

/** 
 * @ClassName: ConfigInfo 
 * @Description: TODO
 * @author Aether
 * @date 2015-7-27 上午10:25:01 
 *  
 */
public class ConfigInfo implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private boolean needNotify=false;

	public boolean isNeedNotify()
	{
		return needNotify;
	}

	public void setNeedNotify(boolean needNotify)
	{
		this.needNotify = needNotify;
	} 
	
	


}
