package cn.fuego.misp.ui.list;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import cn.fuego.common.log.FuegoLog;
import cn.fuego.common.util.validate.ValidatorUtil;
import cn.fuego.misp.service.http.MispHttpMessage;
import cn.fuego.misp.ui.base.MispGridView;
import cn.fuego.misp.ui.base.MispHttpActivtiy;
import cn.fuego.misp.ui.model.ListViewResInfo;

public abstract class MispBaseListActivity<E> extends MispHttpActivtiy implements MispListViewInteface,OnItemClickListener
{
	private FuegoLog log = FuegoLog.getLog(getClass());

	private List<E> dataList = new ArrayList<E>();

	private MispListAdapter<E> adapter;

	protected ListViewResInfo listViewRes = new ListViewResInfo();
 	
	private boolean isAdapterForScrollView = false;
	private ListView listView;
	

	public void setAdapterForScrollView()
	{
		this.isAdapterForScrollView = true;
	}


	public List<E> getDataList()
	{
		return dataList;
	}


	public void setDataList(List<E> dataList)
	{
		if(null != dataList)
		{
			this.dataList.addAll(dataList);
		}
	}


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		adapter = new MispListAdapter<E>(this, this,this.listViewRes,this.dataList);
		switch(this.listViewRes.getListType())
		{
			case ListViewResInfo.VIEW_TYPE_LIST:
			{
				 listView = (ListView) findViewById(this.listViewRes.getListView());
				if(null != listView)
				{
					listView.setAdapter(adapter);
					listView.setOnItemClickListener(this);
				}

				break;
			}
		
			case ListViewResInfo.VIEW_TYPE_GRID:
			{
				MispGridView gridView = (MispGridView) findViewById(this.listViewRes.getListView());
				if(null != gridView)
				{
					gridView.setAdapter(adapter);
					gridView.setOnItemClickListener(this);
				}
				break;
	
			}
		}
		this.adapter.notifyDataSetChanged();
		loadSendList();

	}
	

	public abstract void loadSendList();
	
	public void refreshList(List<E> newDataList)
	{
		this.dataList.clear();
		if(!ValidatorUtil.isEmpty(newDataList))
		{
			this.dataList.addAll(newDataList);
		}
		repaint();
	}
	public void repaint()
	{
		if(null != adapter)
		{
			this.adapter.notifyDataSetChanged();
			if(isAdapterForScrollView)
			{
				this.adapterForScrollView();
			}

		}
	}


 

	public abstract List<E> loadListRecv(Object obj);
 
 
	@Override
	public void handle(MispHttpMessage message)
	{
		if (message.isSuccess())
		{
			this.dataList.clear();

			List<E> newData = loadListRecv(message.getMessage().obj);
			if (!ValidatorUtil.isEmpty(newData))
			{
				this.dataList.addAll(newData);
			}

			repaint();

		} else
		{
			log.error("query product failed");
			this.showMessage(message);
		}
	}

	@Override
	final public void onItemClick(AdapterView<?> parent, View view, int position,long id)
	{

		E item = this.adapter.getItem(position);
		onItemListClick(parent,view,id,item);

	}

	private void adapterForScrollView()
	{
    	ListAdapter listAdapter = listView.getAdapter();   
        if (listAdapter == null) 
        {   
            return;   
        }   
   
        int totalHeight = 0;   
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {   
            // listAdapter.getCount()返回数据项的数目   
            View listItem = listAdapter.getView(i, null, listView);   
            // 计算子项View 的宽高   
            listItem.measure(0, 0);    
            // 统计所有子项的总高度   
            totalHeight += listItem.getMeasuredHeight();    
        }   
   
        ViewGroup.LayoutParams params = listView.getLayoutParams();   
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));   
        // listView.getDividerHeight()获取子项间分隔符占用的高度   
        // params.height最后得到整个ListView完整显示需要的高度   
        listView.setLayoutParams(params);  
	}
	
	
	public void onItemListClick(AdapterView<?> parent, View view,long id, E item)
	{
		if(this.listViewRes.getClickActivityClass()==null)
		{
			return;
		}
		Intent intent = new Intent(this,this.listViewRes.getClickActivityClass());
		intent.putExtra(ListViewResInfo.SELECT_ITEM, (Serializable) item);

		if(this.listViewRes.isNoResult())
		{
			this.startActivity(intent);	
		}
		else
		{
			this.startActivityForResult(intent, 1);
		}
		
	}


	public MispListAdapter<E> getAdapter()
	{
		return adapter;
	}


	public void setAdapter(MispListAdapter<E> adapter)
	{
		this.adapter = adapter;
	}
	


}
