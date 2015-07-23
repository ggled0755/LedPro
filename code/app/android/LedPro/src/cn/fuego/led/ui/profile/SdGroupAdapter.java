/**   
* @Title: SdGroupAdapter.java 
* @Package cn.fuego.led.ui.profile 
* @Description: TODO
* @author Aether
* @date 2015-7-22 下午9:45:10 
* @version V1.0   
*/ 
package cn.fuego.led.ui.profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import cn.fuego.led.R;
import cn.fuego.led.ui.widget.RecAddAndSubView;
import cn.fuego.led.ui.widget.RecAddAndSubView.OnNumChangeListener;
import cn.fuego.led.webservice.up.model.base.ViewSubfolderJson;

/** 
 * @ClassName: SdGroupAdapter 
 * @Description: TODO
 * @author Aether
 * @date 2015-7-22 下午9:45:10 
 *  
 */
public class SdGroupAdapter extends BaseExpandableListAdapter
{
	private Context mContext = null;

	private List<String> groupList = new ArrayList<String>();

	private List<List<ViewSubfolderJson>> itemList = new ArrayList<List<ViewSubfolderJson>>();
	
	private List<ViewSubfolderJson> dataSource;

	
	public SdGroupAdapter(Context context)
	{
		this.mContext = context;
	}
	public void setDatasource(List<ViewSubfolderJson> dataSource)
	{
		this.dataSource = dataSource;
		initData();
		this.notifyDataSetChanged();
	}

	private void initData()
	{
		Map<String, List<ViewSubfolderJson>> detailMap = new HashMap<String, List<ViewSubfolderJson>>();
		itemList.clear();
		for(ViewSubfolderJson json : dataSource)
		{
			List<ViewSubfolderJson> detailList = detailMap.get(json.getSubfolder_name());
			if(null == detailList)
			{
				detailList = new ArrayList<ViewSubfolderJson>();
				detailList.add(json);
				detailMap.put(json.getSubfolder_name(), detailList);
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
	@Override
	public Object getChild(int groupPosition, int childPosition)
	{
		
		return itemList.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition)
	{
		
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,boolean isLastChild, View convertView, ViewGroup parent)
	{
		
		final ViewSubfolderJson detail = itemList.get(groupPosition).get(childPosition);
		//自定义样式
		LayoutInflater inflater = LayoutInflater.from(mContext);
	    View layout = inflater.inflate(R.layout.list_item_sd_child,null);
	    AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

	    layout.setLayoutParams(lp);
	    //lp.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
	    
	    TextView txt_title = (TextView) layout.findViewById(R.id.item_sd_child_title);
	    txt_title.setText(detail.getProduct_name());

	    CheckBox chk_child = (CheckBox) layout.findViewById(R.id.item_sd_child_chk);
	    chk_child.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				Toast.makeText(mContext, detail.toString(), Toast.LENGTH_LONG).show();
				
			}
		});
	    
	    RecAddAndSubView add_sub = (RecAddAndSubView) layout.findViewById(R.id.item_sd_child_num_btn);
	    add_sub.setAutoChangeNumber(true);
	    add_sub.setNum(detail.getProduct_num());
	    add_sub.setOnNumChangeListener(new OnNumChangeListener()
		{
			
			@Override
			public void onNumChange(View view, int stype, int num)
			{
				Toast.makeText(mContext, String.valueOf(num), Toast.LENGTH_LONG).show();
				
			}
		});
		return layout;
	}
	
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,View convertView, ViewGroup parent)
	{
		String name = groupList.get(groupPosition);
		LayoutInflater inflater = LayoutInflater.from(mContext);
	    View layout = inflater.inflate(R.layout.list_item_sd_father,null);

	    TextView txt_title = (TextView) layout.findViewById(R.id.item_sd_father_title);
	    txt_title.setText(name);
	    		
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
		return groupList.size();
	}

	@Override
	public long getGroupId(int groupPosition)
	{
		// TODO Auto-generated method stub
		return groupPosition;
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
