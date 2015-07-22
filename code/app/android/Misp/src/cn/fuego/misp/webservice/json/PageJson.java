package cn.fuego.misp.webservice.json;


/**
 * 
* @ClassName: PageJson 
* @Description: TODO
* @author Tang Jun
* @date 2014-10-20 上午10:58:01 
*
 */
public class PageJson
{
	private int pageSize;
	private int currentPage;
	private long count = 0;
	public int getPageSize()
	{
		return pageSize;
	}
	public void setPageSize(int pageSize)
	{
		this.pageSize = pageSize;
	}
	public int getCurrentPage()
	{
		return currentPage;
	}
	public void setCurrentPage(int currentPage)
	{
		this.currentPage = currentPage;
	}
	public long getCount()
	{
		return count;
	}
	public void setCount(long count)
	{
		this.count = count;
	}
	@Override
	public String toString()
	{
		return "PageJson [pageSize=" + pageSize + ", currentPage="
				+ currentPage + ", count=" + count + "]";
	}
	
}
