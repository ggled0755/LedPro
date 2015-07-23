package cn.fuego.led.ui.profile;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.type.TypeReference;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ExpandableListView;
import cn.fuego.common.contanst.ConditionTypeEnum;
import cn.fuego.common.dao.QueryCondition;
import cn.fuego.led.R;
import cn.fuego.led.cache.SubfolderCache;
import cn.fuego.led.ui.base.LedBaseActivity;
import cn.fuego.led.webservice.up.model.base.SubfolderJson;
import cn.fuego.led.webservice.up.model.base.ViewSubfolderJson;
import cn.fuego.led.webservice.up.rest.WebServiceContext;
import cn.fuego.misp.service.http.MispHttpHandler;
import cn.fuego.misp.service.http.MispHttpMessage;
import cn.fuego.misp.ui.model.ListViewResInfo;
import cn.fuego.misp.webservice.json.MispBaseReqJson;
import cn.fuego.misp.webservice.json.MispBaseRspJson;

public class SubfolderDetailActivity extends LedBaseActivity
{

	private SdGroupAdapter sdAdapter;
	private SubfolderJson detail;
	private ExpandableListView exListView;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		sdAdapter = new SdGroupAdapter(this);
		exListView = (ExpandableListView) findViewById(R.id.subfolder_detail_exlist);
		
		exListView.setAdapter(sdAdapter);
		
		loadDetailData();
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
	}
	
	public static void jump(Context context,SubfolderJson sd)
	{
		Intent intent = new Intent(context,SubfolderDetailActivity.class);
		intent.putExtra(ListViewResInfo.SELECT_ITEM, sd);		
		context.startActivity(intent);

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
					exListView.expandGroup(0);
				}
				else
				{
					showMessage(message);
				}
			}
		}).loadDetailList(req);
		
	}

}
