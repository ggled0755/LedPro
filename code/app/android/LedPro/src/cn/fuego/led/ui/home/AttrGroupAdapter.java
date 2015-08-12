/**   
* @Title: AttrGroupAdapter.java 
* @Package cn.fuego.led.ui.home 
* @Description: TODO
* @author Aether
* @date 2015-7-29 下午9:47:12 
* @version V1.0   
*/ 
package cn.fuego.led.ui.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import cn.fuego.common.util.validate.ValidatorUtil;
import cn.fuego.led.R;
import cn.fuego.misp.webservice.up.model.base.TableMetaJson;

/** 
 * @ClassName: AttrGroupAdapter 
 * @Description: TODO
 * @author Aether
 * @date 2015-7-29 下午9:47:12 
 *  
 */
public class AttrGroupAdapter extends BaseExpandableListAdapter
{

	private Context mContext = null;

	private List<String> groupList = new ArrayList<String>();
	private List<List<TableMetaJson>> itemList = new ArrayList<List<TableMetaJson>>();

	private List<TableMetaJson> dataSource = new ArrayList<TableMetaJson>();
	
	public AttrGroupAdapter(Context context)
	{
		this.mContext = context;
	}
	public void setDatasource(List<TableMetaJson> dataSource)
	{
		this.dataSource = dataSource;
		initData();
		this.notifyDataSetChanged();
	}
	
	private void initData()
	{
		Map<String, List<TableMetaJson>> detailMap = new HashMap<String, List<TableMetaJson>>();
		itemList.clear();
		if(!ValidatorUtil.isEmpty(dataSource))
		{
			for(TableMetaJson json : dataSource)
			{
				List<TableMetaJson> detailList = detailMap.get(json.getGroup_name());
				if(null == detailList)
				{
					detailList = new ArrayList<TableMetaJson>();
					detailList.add(json);
					detailMap.put(json.getGroup_name(), detailList);
				}
				else
				{
					detailList.add(json);
				}
				 
			}
			
			groupList = new ArrayList<String>(detailMap.keySet());

	    		for(String group : groupList)
	    		{
	     			itemList.add(detailMap.get(group));
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
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent)
	{
		TableMetaJson child = itemList.get(groupPosition).get(childPosition);
		ChildHolder ch=null;
		//自定义样式
        if(convertView == null)
        {
    		LayoutInflater inflater = LayoutInflater.from(mContext);
    		convertView = inflater.inflate(R.layout.list_item_content, null);
    	   // View layout = inflater.inflate(R.layout.list_item_content,null);
    	    AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
    				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    	    convertView.setLayoutParams(lp);
    	    ch = new ChildHolder();
    	    ch.txt_name=(TextView) convertView.findViewById(R.id.item_content_title);
    	    ch.txt_value =(TextView) convertView.findViewById(R.id.item_content_value);
    	    convertView.setTag(ch);
        }
        else
        {
        	ch =(ChildHolder) convertView.getTag();
        }

	    StringBuffer sb = new StringBuffer();
	    sb.append(child.getLabel_name());
	    if(!ValidatorUtil.isEmpty(child.getUnit_label()))
	    {
	    	sb.append("(");
	    	sb.append(child.getUnit_label());
	    	sb.append(")");
	    	
	    }
	    sb.append(":");
	    ch.txt_name.setText(sb.toString());

	   // TextView txt_value = (TextView) convertView.findViewById(R.id.item_content_value);
	    ch.txt_value.setText(child.getField_value());
	   // Log.i("getHeight", "groupPosition"+groupPosition+"childPosition"+childPosition+"Height---"+layout.getHeight()); 
		return convertView;
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
		return groupList.size();
	}

	@Override
	public long getGroupId(int groupPosition)
	{
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent)
	{
		String name = groupList.get(groupPosition);
		ParentHolder ph =null;
		if(convertView==null)
		{
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.list_item_father_attrs,null);
		    ph = new ParentHolder();
		    ph.txt_title = (TextView) convertView.findViewById(R.id.item_father_attrs_name);
		    convertView.setTag(ph);
		}
		else
		{
			ph = (ParentHolder) convertView.getTag();
		}

	   /// TextView txt_title = (TextView) layout.findViewById(R.id.item_father_attrs_name);
		ph.txt_title.setText(name);
	    

		return convertView;
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
		// TODO Auto-generated method stub
		return false;
	}

	class ParentHolder
	{
		TextView txt_title;
	}
	class ChildHolder
	{
		TextView txt_name;
		TextView txt_value;
	}
}
