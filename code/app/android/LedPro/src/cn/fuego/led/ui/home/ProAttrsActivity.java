package cn.fuego.led.ui.home;

import java.util.List;

import org.codehaus.jackson.type.TypeReference;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.RatingBar;
import android.widget.TextView;
import cn.fuego.common.util.validate.ValidatorUtil;
import cn.fuego.led.R;
import cn.fuego.led.ui.base.LedBaseActivity;
import cn.fuego.led.webservice.up.model.base.ProductJson;
import cn.fuego.led.webservice.up.rest.WebServiceContext;
import cn.fuego.misp.service.http.MispHttpHandler;
import cn.fuego.misp.service.http.MispHttpMessage;
import cn.fuego.misp.ui.model.ListViewResInfo;
import cn.fuego.misp.webservice.json.MispBaseReqJson;
import cn.fuego.misp.webservice.json.MispBaseRspJson;
import cn.fuego.misp.webservice.up.model.base.TableMetaJson;

public class ProAttrsActivity extends LedBaseActivity implements OnGroupExpandListener, OnGroupCollapseListener
{

	private ProductJson product;
	private ExpandableListView exList;
	private AttrGroupAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		exList = (ExpandableListView) findViewById(R.id.pro_attrs_exlist);
		adapter = new AttrGroupAdapter(this);
		exList.setAdapter(adapter);
		//setExpandableListViewHeightBasedOnChildren(exList);
		exList.setOnGroupExpandListener(this);
		exList.setOnGroupCollapseListener(this);
		
		initView();
	}


	@Override
	public void initRes()
	{
		this.activityRes.setAvtivityView(R.layout.activity_pro_attrs);
		this.activityRes.setName(getString(R.string.title_activity_pro_attrs));
		
		product = (ProductJson) this.getIntent().getSerializableExtra(ListViewResInfo.SELECT_ITEM);
		loadAttrs();
		
	}
	
	private void loadAttrs()
	{
		MispBaseReqJson req = new MispBaseReqJson();
		req.setObj(product.getProduct_id());
		
		WebServiceContext.getInstance().getProductRest(new MispHttpHandler(){
			@Override
			public void handle(MispHttpMessage message)
			{
				if(message.isSuccess())
				{
					MispBaseRspJson rsp = (MispBaseRspJson) message.getMessage().obj;
					List<TableMetaJson> metaList = rsp.GetReqCommonField(new TypeReference<List<TableMetaJson>>(){});
					if(!ValidatorUtil.isEmpty(metaList))
					{
						adapter.setDatasource(metaList);
//						for(int i = 0; i < adapter.getGroupCount(); i++){
//	                        
//							   exList.expandGroup(i);
//							                        
//							}
						//adapterForScrollView();
						setExpandableListViewHeightBasedOnChildren(exList);
						///adapter.notifyDataSetChanged();
					}
					
				}
				else
				{
					showMessage(message);
				}
			}
		}).loadMeta(req);
		
	}


	public static void jump(Context context,ProductJson product)
	{
		Intent i =new Intent();
		i.setClass(context, ProAttrsActivity.class);
		i.putExtra(ListViewResInfo.SELECT_ITEM, product);
		context.startActivity(i);
	}
	
	private void initView()
	{
		TextView txt_name = (TextView) findViewById(R.id.pro_attrs_name);
		txt_name.setText(product.getProduct_name());
		txt_name.setTypeface(ttf_cabin_semibold);
		
		RatingBar rat_score = (RatingBar) findViewById(R.id.pro_attrs_rat);
		rat_score.setRating(product.getProduct_score());
		
		TextView txt_desp = (TextView) findViewById(R.id.pro_attrs_desp);
		txt_desp.setText(product.getProduct_desp());
		txt_desp.setTypeface(ttf_cabin_regular);

		TextView txt_size = (TextView) findViewById(R.id.pro_attrs_size);
		txt_size.setText(product.getProduct_size());
		txt_size.setTypeface(ttf_cabin_regular);

		TextView txt_catg = (TextView) findViewById(R.id.pro_attrs_catg);
		txt_catg.setText(product.getProdcut_catg());
		txt_catg.setTypeface(ttf_cabin_regular);
		
		TextView txt_sub_catg = (TextView) findViewById(R.id.pro_attrs_sub_catg);
		txt_sub_catg.setText(product.getSub_catg());
		txt_sub_catg.setTypeface(ttf_cabin_regular);
		
/*		TextView txt_manu = (TextView) findViewById(R.id.pro_attrs_manufature);
		txt_manu.setText(getString(R.string.text_attr_manufacture)+product.getManufacture());
		txt_manu.setTypeface(ttf_cabin_regular);
		
		TextView txt_warranty = (TextView) findViewById(R.id.pro_attrs_warranty);
		txt_warranty.setText(getString(R.string.text_attr_warranty)+product.getWarranty());
		txt_warranty.setTypeface(ttf_cabin_regular);
		
		TextView txt_lumen = (TextView) findViewById(R.id.pro_attrs_lumen);
		txt_lumen.setText(getString(R.string.text_attr_lumen)+product.getLumen_output());
		txt_lumen.setTypeface(ttf_cabin_regular);
		
		TextView txt_watt = (TextView) findViewById(R.id.pro_attrs_input_watt);
		txt_watt.setText(getString(R.string.text_attr_watt)+product.getInput_watt());
		txt_watt.setTypeface(ttf_cabin_regular);
		
		TextView txt_loc = (TextView) findViewById(R.id.pro_attrs_use_location);
		txt_loc.setText(getString(R.string.text_attr_use_loc)+product.getUse_location());
		txt_loc.setTypeface(ttf_cabin_regular);
		
		TextView txt_base = (TextView) findViewById(R.id.pro_attrs_mounting_base);
		txt_base.setText(getString(R.string.text_attr_mounting_base)+product.getMounting_base());
		txt_base.setTypeface(ttf_cabin_regular);*/
		
	}



	@Override
	public void onGroupCollapse(int groupPosition)
	{
		setExpandableListViewHeightBasedOnChildren(exList);
		
	}



	@Override
	public void onGroupExpand(int groupPosition)
	{
		setExpandableListViewHeightBasedOnChildren(exList);
	
	}


	public static void setExpandableListViewHeightBasedOnChildren(ExpandableListView expandableListView) 
	{ 		// 获取ListView对应的Adapter 		
		AttrGroupAdapter expandableListAdapter = (AttrGroupAdapter) expandableListView.getExpandableListAdapter(); 		
		if (expandableListAdapter == null) 
		{ 			
			// pre-condition 		
			return; 		
		}  		
		int totalHeight = 0; 	
		int num=0; 		
		int groupCount=expandableListAdapter.getGroupCount(),count; 
		num=groupCount; 		
		for(int i=0;i<groupCount;i++) 		
		{ 			
			count=expandableListAdapter.getChildrenCount(i); 	
			View groupListItem;
			groupListItem=expandableListAdapter.getGroupView(i, expandableListView.isGroupExpanded(i), null, expandableListView);
			groupListItem.measure(0, 0); // 计算子项View 的宽高 		
			totalHeight += groupListItem.getMeasuredHeight(); // 统计所有子项的总高度 	
			//Log.i("GroupHeight", "i="+i+"MesuredHeight---"+groupListItem.getMeasuredHeight());
			num=num+count; 	
			
			if(expandableListView.isGroupExpanded(i))
			{			
				for (int j = 0; j<count;  j++)
				{ 				 				
					View listItem; 
					if(j==count-1)
					{
						listItem = expandableListAdapter.getChildView(i, j, true, null, expandableListView);
					}
					else
					{
						listItem = expandableListAdapter.getChildView(i, j, false, null, expandableListView);
					}

					listItem.measure(0,0);
					//Log.i("getHeight", "i="+i+"j="+j+"totalHeight---"+listItem.getHeight()); 

					totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度 
					//Log.i("MesuredHeight", "i="+i+"j="+j+"MesuredHeight---"+listItem.getMeasuredHeight()); 	
				} 
			}


	
		} 			
		ViewGroup.LayoutParams params = expandableListView.getLayoutParams(); 		
		params.height = totalHeight + (expandableListView.getDividerHeight() * (num - 1)); 
		//params.height = totalHeight;
		//Log.i("test", "height:"+params.height); 		
		// listView.getDividerHeight()获取子项间分隔符占用的高度 	
		// params.height最后得到整个ListView完整显示需要的高度 		
		expandableListView.setLayoutParams(params); 	
		expandableListView.requestLayout();
	}



}
