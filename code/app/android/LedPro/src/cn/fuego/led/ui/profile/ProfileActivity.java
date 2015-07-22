package cn.fuego.led.ui.profile;

import java.util.List;

import test.StubData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.fuego.led.R;
import cn.fuego.led.ui.base.LedBaseListActivity;
import cn.fuego.led.ui.project.ProjectDetailActivity;
import cn.fuego.led.ui.widget.OrderButton;
import cn.fuego.led.webservice.up.model.base.ProjectJson;
import cn.fuego.misp.ui.util.LoadImageUtil;

public class ProfileActivity extends LedBaseListActivity<ProjectJson>
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	@Override
	public void initRes()
	{
		this.activityRes.setAvtivityView(R.layout.activity_profile);
		this.listViewRes.setListView(R.id.profile_list);
		this.listViewRes.setListItemView(R.layout.list_item_profile_project);
		//this.listViewRes.setListItemView(R.layout.list_item_home_product);
		this.listViewRes.setClickActivityClass(ProjectDetailActivity.class);
		
		this.setAdapterForScrollView();
		
		this.setDataList(StubData.getProjectList());
	}
	public static void jump(Context context)
	{
		Intent intent = new Intent(context,ProfileActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent);
	
	}
	@Override
	public View getListItemView(View view, ProjectJson item)
	{
		ImageView img_product = (ImageView) view.findViewById(R.id.item_project_img);
		//使用测试页面
		LoadImageUtil.getInstance().loadImage(img_product, R.drawable.project_1);
		//button 需要实现
		Button btn_email = (Button) view.findViewById(R.id.item_project_email_btn);
		Button btn_edit = (Button) view.findViewById(R.id.item_project_edit_btn);
		
		TextView txt_name = (TextView) view.findViewById(R.id.item_project_name);
		txt_name.setText(item.getProject_name());
		
		TextView txt_notes = (TextView) view.findViewById(R.id.item_project_notes);
		txt_notes.setText(item.getProject_note());
		
		TextView txt_num = (TextView) view.findViewById(R.id.item_project_product_num);
		StringBuffer sb1= new StringBuffer();
		sb1.append(item.getTotal_catg());
		sb1.append("/");
		sb1.append(item.getTotal_num());
		sb1.append("  ");
		sb1.append("Products");
		
		txt_num.setText(sb1.toString());
		
		TextView txt_watt = (TextView) view.findViewById(R.id.item_project_watt);
		txt_watt.setText(item.getTotal_watt()+"  Watt/Month");
		
		TextView txt_tco = (TextView) view.findViewById(R.id.item_project_tco);
		txt_tco.setText("$"+item.getTotal_cost()+"  TCO");
		
		return view;
	}

	@Override
	public void loadSendList()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ProjectJson> loadListRecv(Object obj)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onOrderStateChanged(OrderButton orderBtn, Integer orderState)
	{
		// TODO Auto-generated method stub
		
	}



}
