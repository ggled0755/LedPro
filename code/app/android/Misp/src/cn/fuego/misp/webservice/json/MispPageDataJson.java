package cn.fuego.misp.webservice.json;

import java.util.List;


/**
 * 
* @ClassName: PageJson 
* @Description: TODO
* @author Tang Jun
* @date 2014-10-20 上午10:58:01 
*
 */
public class MispPageDataJson<E>
{
	private List<E> rows;
	private int total;
	public List<E> getRows()
	{
		return rows;
	}
	public void setRows(List<E> rows)
	{
		this.rows = rows;
	}
	public int getTotal()
	{
		return total;
	}
	public void setTotal(int total)
	{
		this.total = total;
	}
	
	 
}
