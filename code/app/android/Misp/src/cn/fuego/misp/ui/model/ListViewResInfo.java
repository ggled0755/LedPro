package cn.fuego.misp.ui.model;

public class ListViewResInfo
{
	public static final String SELECT_ITEM = "SELECT_ITEM";
	public static final int VIEW_TYPE_GRID = 2;
	public static final int VIEW_TYPE_LIST = 1;

	private int listType = VIEW_TYPE_LIST;
	private int listView=0;
	private int listItemView=0;
	/**
	 * 作用域：MispListActivity
	 * 点击后跳转的目标Class
	 */
	private Class clickActivityClass;
	
	private boolean noResult = true;
	
	public int getListView()
	{
		return listView;
	}
	public void setListView(int listView)
	{
		this.listView = listView;
	}
	public int getListItemView()
	{
		return listItemView;
	}
	public void setListItemView(int listItemView)
	{
		this.listItemView = listItemView;
	}
	public Class getClickActivityClass()
	{
		return clickActivityClass;
	}
	public void setClickActivityClass(Class clickActivityClass)
	{
		this.clickActivityClass = clickActivityClass;
	}
	public int getListType()
	{
		return listType;
	}
	public void setListType(int listType)
	{
		this.listType = listType;
	}
	public boolean isNoResult()
	{
		return noResult;
	}
	public void setNoResult(boolean noResult)
	{
		this.noResult = noResult;
	}
 
	
}
