package cn.fuego.led.ui.project;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.type.TypeReference;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import cn.fuego.common.contanst.ConditionTypeEnum;
import cn.fuego.common.dao.QueryCondition;
import cn.fuego.common.util.format.DateUtil;
import cn.fuego.led.R;
import cn.fuego.led.cache.AppCache;
import cn.fuego.led.ui.base.CreateDialog;
import cn.fuego.led.ui.base.CreateDialog.OnConfirmListener;
import cn.fuego.led.ui.base.LedBaseListActivity;
import cn.fuego.led.ui.widget.OrderButton;
import cn.fuego.led.webservice.up.model.base.ProductJson;
import cn.fuego.led.webservice.up.model.base.ProjectJson;
import cn.fuego.led.webservice.up.rest.WebServiceContext;
import cn.fuego.misp.service.http.MispHttpHandler;
import cn.fuego.misp.service.http.MispHttpMessage;
import cn.fuego.misp.ui.model.ListViewResInfo;
import cn.fuego.misp.webservice.json.MispBaseReqJson;
import cn.fuego.misp.webservice.json.MispBaseRspJson;

public class ProjectListActivity extends LedBaseListActivity<ProjectJson>
{
	private ProductJson product;
	private ProgressDialog pd;
	private int selId=0;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		View add_btn = findViewById(R.id.project_list_add_btn);
		add_btn.setOnClickListener(this);

	}
	
	@Override
	public void initRes()
	{
		this.activityRes.setAvtivityView(R.layout.activity_project_list);
		this.activityRes.setName(getString(R.string.title_activity_project_list));
		
		
		this.listViewRes.setListView(R.id.project_list_content);
		this.listViewRes.setListItemView(R.layout.list_item_project_sel);
		this.setAdapterForScrollView();
		
		product =(ProductJson) this.getIntent().getSerializableExtra(ListViewResInfo.SELECT_ITEM);
		
	}
	
	public static void jump(Context context,ProductJson product)
	{
		Intent intent = new Intent(context,ProjectListActivity.class);
		intent.putExtra(ListViewResInfo.SELECT_ITEM, product);
		context.startActivity(intent);

	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		super.onClick(v);
		if(v.getId()==R.id.project_list_add_btn)
		{
			CreateDialog dialog= new CreateDialog(this, "Create a new project", "Please enter your project name here", new OnConfirmListener()
			{
				
				@Override
				public void confirmCallback(String name)
				{
					createProject(name);
					
				}
			});
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			
			dialog.show();
		}
	}
	private void createProject(String name)
	{
		MispBaseReqJson req =new MispBaseReqJson();
		ProjectJson json = new ProjectJson();
		json.setProject_name(name);
		json.setCreate_user_id(AppCache.getInstance().getUser().getUser_id());
		json.setCreate_time(DateUtil.getCurrentDateTimeStr());
		req.setObj(json);
		WebServiceContext.getInstance().getProjectRest(new MispHttpHandler(){
			@Override
			public void handle(MispHttpMessage message)
			{
				if(message.isSuccess())
				{
					loadSendList();
				}
				else
				{
					showMessage(message);
				}
			}
		}).createProject(req);
		
	}
	@Override
	public View getListItemView(View view, final ProjectJson item)
	{
		CheckBox chk = (CheckBox) view.findViewById(R.id.item_project_sel_chk);
		if(item.getProject_id()!=selId)
		{
			chk.setChecked(false);
		}
		chk.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if(isChecked)
				{
					selId=item.getProject_id();
					repaint();
					selProject(item);
				}				
				
			}


		});
		TextView txt_name = (TextView) view.findViewById(R.id.item_project_sel_name);
		txt_name.setText(item.getProject_name());
		
		return view;
	}
	
	private void selProject(ProjectJson item)
	{
		ProjectDetailActivity.jump(this, item, product);
		
	}
	@Override
	public void loadSendList()
	{
		pd = ProgressDialog.show(this, null, getString(R.string.progress_msg_loading));
		MispBaseReqJson req = new MispBaseReqJson();
		List<QueryCondition> conditionList=new ArrayList<QueryCondition>();
		conditionList.add(new QueryCondition(ConditionTypeEnum.EQUAL, "create_user_id", AppCache.getInstance().getUser().getUser_id()));
		req.setConditionList(conditionList);
		WebServiceContext.getInstance().getProjectRest(this).loadAll(req);
				
		
	}

	@Override
	public List<ProjectJson> loadListRecv(Object obj)
	{
		if(pd!=null&&pd.isShowing())
		{
			pd.dismiss();
		}
		MispBaseRspJson rsp = (MispBaseRspJson) obj;
		
		return rsp.GetReqCommonField(new TypeReference<List<ProjectJson>>(){});
	}

	@Override
	public void onOrderStateChanged(OrderButton orderBtn, Integer orderState)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHeightChanged(CompoundButton buttonView)
	{
		// TODO Auto-generated method stub
		
	}


}
