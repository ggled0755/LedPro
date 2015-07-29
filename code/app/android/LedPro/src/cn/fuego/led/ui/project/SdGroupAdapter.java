/**   
* @Title: SdGroupAdapter.java 
* @Package cn.fuego.led.ui.profile 
* @Description: TODO
* @author Aether
* @date 2015-7-22 下午9:45:10 
* @version V1.0   
*/ 
package cn.fuego.led.ui.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import cn.fuego.common.util.validate.ValidatorUtil;
import cn.fuego.led.R;
import cn.fuego.led.ui.widget.RecAddAndSubView;
import cn.fuego.led.ui.widget.RecAddAndSubView.OnNumChangeListener;
import cn.fuego.led.webservice.up.model.base.ViewSubfolderJson;
import cn.fuego.led.webservice.up.rest.WebServiceContext;
import cn.fuego.misp.constant.MispErrorCode;
import cn.fuego.misp.service.http.MispHttpHandler;
import cn.fuego.misp.service.http.MispHttpMessage;
import cn.fuego.misp.webservice.json.MispBaseReqJson;

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
	
	private List<ViewSubfolderJson> dataSource=new ArrayList<ViewSubfolderJson>();

	private ProgressDialog pd;
	//存储选中项目
	private Map<Integer,List<Integer>> selMap = new HashMap<Integer, List<Integer>>();
	private List<Integer> idList;
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
		if(!ValidatorUtil.isEmpty(dataSource))
		{
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
	public View getChildView(final int groupPosition, final int childPosition,boolean isLastChild, View convertView, ViewGroup parent)
	{
		
		final ViewSubfolderJson detail = itemList.get(groupPosition).get(childPosition);
		//自定义样式
		LayoutInflater inflater = LayoutInflater.from(mContext);
	    View layout = inflater.inflate(R.layout.list_item_sd_child,null);
	    AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

	    layout.setLayoutParams(lp);
	    
	    TextView txt_title = (TextView) layout.findViewById(R.id.item_sd_child_title);
	    txt_title.setText(detail.getProduct_name());

	    CheckBox chk_child = (CheckBox) layout.findViewById(R.id.item_sd_child_chk);
	    chk_child.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if(isChecked)
				{
					if(null!=selMap.get(groupPosition)&&!ValidatorUtil.isEmpty(selMap.get(groupPosition)))
					{
						selMap.get(groupPosition).add(detail.getSubfolder_detail_id());
					}
					else
					{
						List<Integer> temp = new ArrayList<Integer>();
						temp.add(detail.getSubfolder_detail_id());
						selMap.put(groupPosition, temp);
					}
					
				}
				else
				{
					selMap.get(groupPosition).remove(detail.getSubfolder_detail_id());

				}
				
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
				detail.setProduct_num(num);
				modifyNum(detail,groupPosition,childPosition);
				
			}


		});
		return layout;
	}
	//修改产品数量
	private void modifyNum(final ViewSubfolderJson detail, final int groupPosition, final int childPosition)
	{
		pd = ProgressDialog.show(mContext, null, "Processing……");
		
		MispBaseReqJson req = new MispBaseReqJson();
		req.setObj(detail);
		WebServiceContext.getInstance().getSubfolderRest(new MispHttpHandler(){
			@Override
			public void handle(MispHttpMessage message)
			{
				pd.dismiss();
				if(message.isSuccess())
				{
					itemList.get(groupPosition).get(childPosition).setProduct_num(detail.getProduct_num());
				}
				else
				{
					Toast.makeText(mContext, MispErrorCode.getMessageByErrorCode(message.getErrorCode()) , Toast.LENGTH_SHORT).show();
				}
				notifyDataSetChanged();

			}
		}).modifySubfolderDetail(req);
	}	
	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded,View convertView, ViewGroup parent)
	{
		String name = groupList.get(groupPosition);
		LayoutInflater inflater = LayoutInflater.from(mContext);
	    View layout = inflater.inflate(R.layout.list_item_sd_father,null);

	    TextView txt_title = (TextView) layout.findViewById(R.id.item_sd_father_title);
	    txt_title.setText(name);
	    
	    Button btn_delete = (Button) layout.findViewById(R.id.item_sd_father_delete_btn);
	    btn_delete.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				removeItems(groupPosition);
				
			}
		});
		return layout;
	}
	//删除子项目
	protected void removeItems(final int groupPosition)
	{
		if(!selMap.containsKey(groupPosition))
		{
			Toast.makeText(mContext, "Please select at least one child item", Toast.LENGTH_LONG).show();
		}
		else
		{
			pd = ProgressDialog.show(mContext, null, "Processing……");
			idList = new ArrayList<Integer>();
			idList =selMap.get(groupPosition);

			
			MispBaseReqJson req = new MispBaseReqJson();
			req.setObj(idList);
			
			WebServiceContext.getInstance().getSubfolderRest(new MispHttpHandler(){
				@Override
				public void handle(MispHttpMessage message)
				{
					pd.dismiss();
					if(message.isSuccess())
					{
						//删除代码较为复杂，建议改善，注意grouplist 的remove
						for(int i=0;i<itemList.size();i++)
						{
							int count =itemList.get(i).size();
							for(int j=0;j<itemList.get(i).size();j++)
							{
								if(idList.contains(itemList.get(i).get(j).getSubfolder_detail_id()))
								{
									itemList.get(i).remove(j);
									j--;
									count--;
								}
								
							}
							if(count==0)
							{
								itemList.remove(i);
								groupList.remove(i);
								i--;
								
							}
							
						}
						notifyDataSetChanged();


					}
					else
					{
						Toast.makeText(mContext, "Please select at least one child item", Toast.LENGTH_LONG).show();
					}
				}
			}).deleteDetailList(req);
		}
		
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
