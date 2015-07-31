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

import javax.ws.rs.core.NewCookie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import cn.fuego.common.util.validate.ValidatorUtil;
import cn.fuego.led.R;
import cn.fuego.led.webservice.up.model.base.ProductJson;
import cn.fuego.led.webservice.up.model.base.ViewSubfolderJson;

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
	private List<List<ChildModel>> itemList = new ArrayList<List<ChildModel>>();
	//private Map<String,ChildModel> dataSource = new HashMap<String, ChildModel>();
	private ProductJson dataSource;
	public void setDatasource(ProductJson dataSource)
	{
		this.dataSource = dataSource;
		initData();
		this.notifyDataSetChanged();
	}
	
	private void initData()
	{

		
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
		ChildModel child = itemList.get(groupPosition).get(childPosition);
		//自定义样式
		LayoutInflater inflater = LayoutInflater.from(mContext);
	    View layout = inflater.inflate(R.layout.list_item_btn,null);
	    AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

	    layout.setLayoutParams(lp);
	    
	    TextView txt_title = (TextView) layout.findViewById(R.id.item_btn_title);
	    txt_title.setText(child.getName());

	    TextView txt_value = (TextView) layout.findViewById(R.id.item_btn_title);
	    txt_value.setText(child.getValue());
	    
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
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent)
	{
		String name = groupList.get(groupPosition);
		LayoutInflater inflater = LayoutInflater.from(mContext);
	    View layout = inflater.inflate(R.layout.list_item_father_attrs,null);

	    TextView txt_title = (TextView) layout.findViewById(R.id.item_father_attrs_name);
	    txt_title.setText(name);
	    

		return layout;
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

}
