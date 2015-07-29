package cn.fuego.led.ui.project;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.type.TypeReference;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.TextView;
import cn.fuego.common.contanst.ConditionTypeEnum;
import cn.fuego.common.dao.QueryCondition;
import cn.fuego.led.R;
import cn.fuego.led.cache.SubfolderCache;
import cn.fuego.led.ui.base.LedBaseActivity;
import cn.fuego.led.ui.base.ModifyDialog;
import cn.fuego.led.ui.base.ModifyDialog.OnModifyConfirmListener;
import cn.fuego.led.webservice.up.model.base.SubfolderJson;
import cn.fuego.led.webservice.up.model.base.ViewSubfolderJson;
import cn.fuego.led.webservice.up.rest.WebServiceContext;
import cn.fuego.misp.service.http.MispHttpHandler;
import cn.fuego.misp.service.http.MispHttpMessage;
import cn.fuego.misp.ui.model.ListViewResInfo;
import cn.fuego.misp.ui.util.StrUtil;
import cn.fuego.misp.webservice.json.MispBaseReqJson;
import cn.fuego.misp.webservice.json.MispBaseRspJson;

public class SubfolderDetailActivity extends LedBaseActivity
{

	private SdGroupAdapter sdAdapter;
	private SubfolderJson detail;
	private ExpandableListView exListView;
	private TextView txt_notes,txt_title;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		sdAdapter = new SdGroupAdapter(this);
		exListView = (ExpandableListView) findViewById(R.id.subfolder_detail_exlist);		
		exListView.setAdapter(sdAdapter);
		
		txt_notes = (TextView) findViewById(R.id.subfolder_detail_notes);
		txt_title = (TextView) findViewById(R.id.misp_title_name);
		initView();
		
		loadDetailData();
	}

	private void initView()
	{
		txt_title.setText(detail.getSubfolder_name());
		txt_notes.setText(StrUtil.noNullStr(detail.getSubfolder_note()));
		
	}

	@Override
	public void initRes()
	{
		this.activityRes.setAvtivityView(R.layout.activity_subfolder_detail);
		detail = (SubfolderJson) this.getIntent().getSerializableExtra(ListViewResInfo.SELECT_ITEM);
		if(detail!=null)
		{
			this.activityRes.setName(detail.getSubfolder_name());
		}
		this.activityRes.getButtonIDList().add(R.id.subfolder_detail_title);
	}
	
	public static void jump(Context context,SubfolderJson sd)
	{
		Intent intent = new Intent(context,SubfolderDetailActivity.class);
		intent.putExtra(ListViewResInfo.SELECT_ITEM, sd);		
		context.startActivity(intent);

	}
	
	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		super.onClick(v);
		if(v.getId()==R.id.subfolder_detail_title)
		{
			ModifyDialog dialog =new ModifyDialog(this, detail.getSubfolder_name(), 
					StrUtil.noNullStr(detail.getSubfolder_note()));
			dialog.setConfirmListener(new OnModifyConfirmListener()
			{
				
				@Override
				public void confirmCallback(String name, String note)
				{
					detail.setSubfolder_name(name);
					detail.setSubfolder_note(note);
					modifySfd();
					
				}
			});
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			
			dialog.show();
		}
	}



	protected void modifySfd()
	{
		MispBaseReqJson req = new MispBaseReqJson();
		req.setObj(detail);
		WebServiceContext.getInstance().getSubfolderRest(new MispHttpHandler(){
			@Override
			public void handle(MispHttpMessage message)
			{
				if(message.isSuccess())
				{
					initView();
					SubfolderCache.getInstance().updateSf(detail);
					SubfolderCache.getInstance().setChange(true);
				}
				else
				{
					showMessage(message);
				}
			}
		}).modifySubfolder(req);
		
	}

	private void loadDetailData()
	{
		MispBaseReqJson req = new MispBaseReqJson();
		List<QueryCondition> conditionList = new ArrayList<QueryCondition>();
		conditionList.add(new QueryCondition(ConditionTypeEnum.IN, "subfolder_id", SubfolderCache.getInstance().getSelIDList()));
		req.setConditionList(conditionList);
		
		WebServiceContext.getInstance().getSubfolderRest(new MispHttpHandler(){
			@Override
			public void handle(MispHttpMessage message)
			{
				if(message.isSuccess())
				{
					MispBaseRspJson rsp = (MispBaseRspJson) message.getMessage().obj;
					
					List<ViewSubfolderJson> dataSource = rsp.GetReqCommonField(new TypeReference<List<ViewSubfolderJson>>(){});
					sdAdapter.setDatasource(dataSource);
			        for (int i=0; i<sdAdapter.getGroupCount(); i++) 
			        {
			        	exListView.expandGroup(i);
			        }
				}
				else
				{
					showMessage(message);
				}
			}
		}).loadDetailList(req);
		
	}

}
