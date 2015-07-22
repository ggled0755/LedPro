package cn.fuego.misp.ui.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class MispListActivity<E> extends MispBaseListActivity<E> 

{
	 
 
 
	@Override
	public int getItemViewType(Object item)
	{
		// TODO Auto-generated method stub
		return 0;
	}
	public int getItemTypeCount()
	{
		return 1;
	}
	


 	
	public abstract View getListItemView(View view, E item);

	
	@Override
	final public View getView(LayoutInflater inflater,View convertView, ViewGroup parent, Object item)
	{
		View view = inflater.inflate(this.listViewRes.getListItemView(), null);
		return getListItemView(view,(E)item);
	}

 
}
