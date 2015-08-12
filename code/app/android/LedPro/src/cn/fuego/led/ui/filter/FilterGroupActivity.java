package cn.fuego.led.ui.filter;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.type.TypeReference;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import cn.fuego.common.contanst.ConditionTypeEnum;
import cn.fuego.common.dao.QueryCondition;
import cn.fuego.common.util.validate.ValidatorUtil;
import cn.fuego.led.R;
import cn.fuego.led.constant.IntentCodeConst;
import cn.fuego.led.ui.base.LedBaseActivity;
import cn.fuego.led.webservice.up.rest.WebServiceContext;
import cn.fuego.misp.service.http.MispHttpHandler;
import cn.fuego.misp.service.http.MispHttpMessage;
import cn.fuego.misp.ui.model.ListViewResInfo;
import cn.fuego.misp.webservice.json.MispBaseReqJson;
import cn.fuego.misp.webservice.json.MispBaseRspJson;
import cn.fuego.misp.webservice.up.model.base.EnumJson;
import cn.fuego.misp.webservice.up.model.base.TableMetaJson;

public class FilterGroupActivity extends LedBaseActivity implements OnChildClickListener
{

	private TableMetaJson selectItem ;
	private FilterGroupAdapter fgAdapter;

	private ExpandableListView exListView;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//setContentView(layoutResID)
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);;
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.width=(int) (wm.getDefaultDisplay().getWidth()*0.8);
		params.height = LayoutParams.MATCH_PARENT;
		params.gravity=Gravity.RIGHT;
		getWindow().setAttributes(params);
		
		exListView = (ExpandableListView) findViewById(R.id.filter_group_exlist);
		fgAdapter = new FilterGroupAdapter(this);
		exListView.setAdapter(fgAdapter);
		exListView.setOnChildClickListener(this);

	}

	@Override
	public void initRes()
	{
		this.activityRes.setAvtivityView(R.layout.activity_filter_group);
		
		selectItem = (TableMetaJson) this.getIntent().getSerializableExtra(ListViewResInfo.SELECT_ITEM);
		if(selectItem!=null)
		{
			this.activityRes.setName(selectItem.getLabel_name());
		}
		loadGroup();
	}

	private void loadGroup()
	{
		MispBaseReqJson req = new MispBaseReqJson();
		List<QueryCondition> conditionList = new ArrayList<QueryCondition>();
		conditionList.add(new QueryCondition(ConditionTypeEnum.EQUAL, "field_name", selectItem.getField_name()));
		req.setConditionList(conditionList);
		WebServiceContext.getInstance().getProductRest(new MispHttpHandler(){
			@Override
			public void handle(MispHttpMessage message)
			{
				if(message.isSuccess())
				{
					MispBaseRspJson rsp = (MispBaseRspJson) message.getMessage().obj;
					List<EnumJson> metaList = rsp.GetReqCommonField(new TypeReference<List<EnumJson>>(){});
					if(!ValidatorUtil.isEmpty(metaList))
					{
						fgAdapter.setDatasource(metaList);
					}
					
				}
				else
				{
					showMessage(message);
				}
			}
		}).loadEnum(req);
		
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,int groupPosition, int childPosition, long id)
	{
		//showMessage(0);
		EnumJson parentEnum = (EnumJson) fgAdapter.getGroup(groupPosition);
		EnumJson childEnum = (EnumJson) fgAdapter.getChild(groupPosition, childPosition);
		Intent i = new Intent();
		i.putExtra(IntentCodeConst.DATA_PARENT, parentEnum);
		i.putExtra(IntentCodeConst.DATA_CHILD, childEnum);
		setResult(IntentCodeConst.RESULT_CODE_GROUP, i);
		this.finish();
		return false;
	}
}
