/**   
* @Title: MispComDataAccessReqJson.java 
* @Package cn.fuego.misp.webservice.json 
* @Description: TODO
* @author Tang Jun   
* @date 2015-5-22 上午9:16:49 
* @version V1.0   
*/ 
package cn.fuego.misp.webservice.json;

 /** 
 * @ClassName: MispComDataAccessReqJson 
 * @Description: TODO
 * @author Tang Jun
 * @date 2015-5-22 上午9:16:49 
 *  
 */
public class MispComDataAccessReqJson extends MispBaseReqJson
{
	private String table_name;

	public String getTable_name()
	{
		return table_name;
	}

	public void setTable_name(String table_name)
	{
		this.table_name = table_name;
	}

 
}
