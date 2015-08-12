/**   
* @Title: FilterGroupAdapter.java 
* @Package cn.fuego.led.ui.filter 
* @Description: TODO
* @author Aether
* @date 2015-8-7 上午9:39:44 
* @version V1.0   
*/ 
package cn.fuego.led.ui.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import cn.fuego.common.util.validate.ValidatorUtil;
import cn.fuego.led.R;
import cn.fuego.misp.webservice.up.model.base.EnumJson;

/** 
 * @ClassName: FilterGroupAdapter 
 * @Description: TODO
 * @author Aether
 * @date 2015-8-7 上午9:39:44 
 *  
 */
public class FilterGroupAdapter extends BaseExpandableListAdapter
{
	private Context mContext = null;
	private List<EnumJson> groupList = new ArrayList<EnumJson>();
	private List<List<EnumJson>> itemList = new ArrayList<List<EnumJson>>();
	private List<EnumJson> dataSource=new ArrayList<EnumJson>();
	//存储选中项目
	private Map<String,List<EnumJson>> selMap = new HashMap<String, List<EnumJson>>();
	
	public FilterGroupAdapter(Context context)
	{
		this.mContext = context;
	}
	
	public void setDatasource(List<EnumJson> dataSource)
	{
		this.dataSource = dataSource;
		initData();
		this.notifyDataSetChanged();
	}
	//初始化数据
	public void initData()
	{
		Map<Integer, List<EnumJson>> detailMap = new HashMap<Integer, List<EnumJson>>();
		if(!ValidatorUtil.isEmpty(dataSource))
		{
			for(EnumJson json : dataSource)
			{
				
				if(json.getEnum_parent_key()==0)
				{
					groupList.add(json);
				}
				else
				{
					List<EnumJson> detailList = detailMap.get(json.getEnum_parent_key());
					if(null == detailList)
					{
						detailList = new ArrayList<EnumJson>();
						detailList.add(json);
						detailMap.put(json.getEnum_parent_key(), detailList);
					}
					else
					{
						detailList.add(json);
					}
					 
				}
			}
			
			itemList.clear();
			for(EnumJson e:groupList)
			{				
				if(null!=detailMap.get(e.getEnum_key()))
				{
					itemList.add(detailMap.get(e.getEnum_key()));
				}
				
			}

		}
		
	}	
	@Override
	public Object getChild(int groupPosition, int childPosition)
	{
		// TODO Auto-generated method stub
		return itemList.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition)
	{
		// TODO Auto-generated method stub
		return itemList.get(groupPosition).get(childPosition).getEnum_key();
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent)
	{
		EnumJson child = (EnumJson) getChild(groupPosition, childPosition);
		LayoutInflater inflater = LayoutInflater.from(mContext);
	    View layout = inflater.inflate(R.layout.list_item_group_child,null);
	    
		TextView txt_label = (TextView) layout.findViewById(R.id.item_group_child_label);
		txt_label.setText(child.getEnum_value());
		
		CheckBox chk = (CheckBox) layout.findViewById(R.id.item_group_child_chk);

	    return layout;

	}

	@Override
	public int getChildrenCount(int groupPosition)
	{
		// TODO Auto-generated method stub
		return itemList.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition)
	{
		// TODO Auto-generated method stub
		return groupList.get(groupPosition);
	}

	@Override
	public int getGroupCount()
	{
		// TODO Auto-generated method stub
		return itemList.size();
	}

	@Override
	public long getGroupId(int groupPosition)
	{
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent)
	{
		LayoutInflater inflater = LayoutInflater.from(mContext);
	    View layout = inflater.inflate(R.layout.list_item_group_father,null);
	    
		TextView txt_label = (TextView) layout.findViewById(R.id.item_group_father_label);
		txt_label.setText(groupList.get(groupPosition).getEnum_value());
		
		CheckBox chk = (CheckBox) layout.findViewById(R.id.item_group_father_chk);

	    return layout;
	}
	
	//全选和全不选子项
	protected void selChildren(int groupPosition)
	{
/*		if(null!=selMap.get(getGroup(groupPosition)))
		{
			if(selMap.get(getGroup(groupPosition))
			{
				selMap.get(getGroup(groupPosition).
			}
		}
		else
		{

			selMap.put((String)getGroup(groupPosition), itemList.get(groupPosition));
		}*/
		
	}

	@Override
	public boolean hasStableIds()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition)
	{
		// 如果不为true，则不能点击child项
		return true;
	}



}
