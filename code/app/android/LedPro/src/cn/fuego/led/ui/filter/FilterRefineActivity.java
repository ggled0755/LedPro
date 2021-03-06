package cn.fuego.led.ui.filter;

import java.util.List;

import org.codehaus.jackson.type.TypeReference;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import cn.fuego.common.util.validate.ValidatorUtil;
import cn.fuego.led.R;
import cn.fuego.led.constant.FilterTypeEnum;
import cn.fuego.led.constant.IntentCodeConst;
import cn.fuego.led.ui.base.LedBaseActivity;
import cn.fuego.led.webservice.up.rest.WebServiceContext;
import cn.fuego.misp.service.http.MispHttpHandler;
import cn.fuego.misp.service.http.MispHttpMessage;
import cn.fuego.misp.ui.base.MispListViewAdaptScroll;
import cn.fuego.misp.ui.model.ListViewResInfo;
import cn.fuego.misp.webservice.json.MispBaseReqJson;
import cn.fuego.misp.webservice.json.MispBaseRspJson;
import cn.fuego.misp.webservice.up.model.base.EnumJson;
import cn.fuego.misp.webservice.up.model.base.TableMetaJson;

public class FilterRefineActivity extends LedBaseActivity implements OnItemClickListener
{

	private MispListViewAdaptScroll listview;
	private DistinctAdapter mAdapter ;
	private TableMetaJson selItem;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);;
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.width=(int) (wm.getDefaultDisplay().getWidth()*0.8);
		params.height = LayoutParams.MATCH_PARENT;
		params.gravity=Gravity.RIGHT;
		getWindow().setAttributes(params);
		
		listview= (MispListViewAdaptScroll) findViewById(R.id.filter_refine_list);
		mAdapter = new DistinctAdapter(this);
		if(FilterDataCache.getInstance().getData().size()==0)
		{
			loadFilter();
		}
		else
		{
			mAdapter.setDatasource(FilterDataCache.getInstance().getData());
		}
		
		listview.setAdapter(mAdapter);
		listview.setOnItemClickListener(this);

		
	}


	@Override
	public void initRes()
	{
		this.activityRes.setAvtivityView(R.layout.activity_filter_refine);
		this.activityRes.setName(getResources().getString(R.string.title_activity_filter_refine));

		this.activityRes.getButtonIDList().add(R.id.filter_refine_reset_btn);
	}


	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		super.onClick(v);
		if(v.getId()==R.id.filter_refine_reset_btn)
		{
			showMessage("Reset conditions");
			FilterDataCache.getInstance().resetData();
			mAdapter.notifyDataSetChanged();

		}
	}


	@Override
	public void saveOnClick(View v)
	{

		setResult(IntentCodeConst.RESULT_CODE_FILTER_SERACH);
		this.finish();
	}

	private void loadFilter()
	{
		MispBaseReqJson req = new MispBaseReqJson();
		
		WebServiceContext.getInstance().getProductRest(new MispHttpHandler(){
			@Override
			public void handle(MispHttpMessage message)
			{
				if(message.isSuccess())
				{
					MispBaseRspJson rsp = (MispBaseRspJson) message.getMessage().obj;
					List<TableMetaJson> filterList = rsp.GetReqCommonField(new TypeReference<List<TableMetaJson>>(){});
					if(!ValidatorUtil.isEmpty(filterList))
					{
						FilterDataCache.getInstance().setData(filterList);
						FilterDataCache.getInstance().updateDefaultMata(rsp.GetReqCommonField(new TypeReference<List<TableMetaJson>>(){}));						
						mAdapter.setDatasource(FilterDataCache.getInstance().getData());

					}

				}

			}
		}).loadFilter(req);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		selItem=(TableMetaJson) parent.getAdapter().getItem(position);
		if(selItem!=null)
		{
			if(selItem.getField_type()==FilterTypeEnum.SELECT.getIntValue())
			{
				Intent i = new Intent();
				i.setClass(this, FilterListActivity.class);
				i.putExtra(ListViewResInfo.SELECT_ITEM, selItem);
				startActivityForResult(i, IntentCodeConst.REQUEST_CODE);
			}

			if(selItem.getField_type()==FilterTypeEnum.GROUP.getIntValue())
			{
				Intent i = new Intent();
				i.setClass(this, FilterGroupActivity.class);
				i.putExtra(ListViewResInfo.SELECT_ITEM, selItem);
				startActivityForResult(i, IntentCodeConst.REQUEST_CODE);
			}
		}

		
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode==IntentCodeConst.RESULT_CODE_ITEM_CONFIRM)
		{			
			mAdapter.setDatasource(FilterDataCache.getInstance().getData());
		}
		else if(resultCode==IntentCodeConst.RESULT_CODE_GROUP)
		{
			if(null!=data)
			{
				EnumJson p= (EnumJson) data.getSerializableExtra(IntentCodeConst.DATA_PARENT);
				EnumJson c= (EnumJson) data.getSerializableExtra(IntentCodeConst.DATA_CHILD);
				selItem.setField_value(p.getEnum_value()+FilterDataCache.GROUP_SEPRATOR+c.getEnum_value());
				selItem.setField_enum_key(c.getEnum_key());
				FilterDataCache.getInstance().update(selItem);
				mAdapter.setDatasource(FilterDataCache.getInstance().getData());
			}
		}
		
	}

}
