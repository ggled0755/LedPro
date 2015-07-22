package cn.fuego.led.ui.project;

import java.util.List;

import test.StubData;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import cn.fuego.led.R;
import cn.fuego.led.ui.base.LedBaseActivity;
import cn.fuego.led.ui.profile.SimpleTreeAdapter;
import cn.fuego.led.util.treeview.TreeListViewAdapter;
import cn.fuego.led.webservice.up.model.base.ProjectJson;
import cn.fuego.led.webservice.up.model.base.ProjectSubfolderJson;
import cn.fuego.misp.ui.model.ListViewResInfo;
import cn.fuego.misp.ui.util.StrUtil;

public class ProjectDetailActivity extends LedBaseActivity
{
	private ListView mTree;

	private TreeListViewAdapter mAdapter;
	private List<ProjectSubfolderJson> datas;
	private ProjectJson project;
	private EditText txt_notes;
	@Override
	public void initRes()
	{
		this.activityRes.setAvtivityView(R.layout.activity_project_detail);
			
		project = (ProjectJson) this.getIntent().getSerializableExtra(ListViewResInfo.SELECT_ITEM);
		if(project!=null)
		{
			this.activityRes.setName(StrUtil.noNullStr(project.getProject_name()));
		}
			
	}
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		mTree  = (ListView) findViewById(R.id.project_detail_list);

		datas = StubData.getSubfolderList();
		try
		{
			mAdapter = new SimpleTreeAdapter<ProjectSubfolderJson>(mTree, this, datas , 2);
			mTree.setAdapter(mAdapter);
		}
		catch (IllegalArgumentException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		txt_notes = (EditText) findViewById(R.id.project_detail_notes);
		
		TextView txt_products = (TextView) findViewById(R.id.project_detail_products);
		txt_products.setText(String.valueOf(project.getTotal_num()));
		
		TextView txt_watt = (TextView) findViewById(R.id.project_detail_watt);
		
		TextView txt_tco = (TextView) findViewById(R.id.project_detail_tco);
		
		
	}


}
