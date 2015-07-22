package cn.fuego.led.ui.filter;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.type.TypeReference;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.TextView;
import cn.fuego.common.contanst.ConditionTypeEnum;
import cn.fuego.common.dao.QueryCondition;
import cn.fuego.led.R;
import cn.fuego.led.R.id;
import cn.fuego.led.R.layout;
import cn.fuego.led.constant.IntentCodeConst;
import cn.fuego.led.webservice.up.model.base.EnumJson;
import cn.fuego.led.webservice.up.rest.WebServiceContext;
import cn.fuego.misp.ui.list.MispListActivity;
import cn.fuego.misp.ui.model.ListViewResInfo;
import cn.fuego.misp.webservice.json.MispBaseReqJson;
import cn.fuego.misp.webservice.json.MispBaseRspJson;

public class FilterListActivity extends MispListActivity<EnumJson>
{

	private FilterItemMeta selectItem;
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
	}
	@Override
	public void initRes()
	{
		this.activityRes.setAvtivityView(R.layout.activity_filter_list);
		this.listViewRes.setListView(R.id.filter_list_content);
		this.listViewRes.setListItemView(R.layout.list_item_result);
		
		selectItem = (FilterItemMeta) this.getIntent().getSerializableExtra(ListViewResInfo.SELECT_ITEM);
		if(selectItem!=null)
		{
			this.activityRes.setName(selectItem.getTitle());
		}
	}
	@Override
	public View getListItemView(View view, EnumJson item)
	{
		TextView txt_name = (TextView) view.findViewById(R.id.item_result_title);
		txt_name.setText(item.getEnum_value());
		
		return view;
	}

	@Override
	public void loadSendList()
	{
		MispBaseReqJson req = new MispBaseReqJson();
		List<QueryCondition> conditionList =  new ArrayList<QueryCondition>();
		conditionList.add(new QueryCondition(ConditionTypeEnum.EQUAL, "field_name", selectItem.getFieldName()));
		req.setConditionList(conditionList);

		WebServiceContext.getInstance().getEnumRest(this).loadList(req);
		
	}

	@Override
	public List<EnumJson> loadListRecv(Object obj)
	{
		MispBaseRspJson rsp = (MispBaseRspJson) obj;
		
		return rsp.GetReqCommonField(new TypeReference<List<EnumJson>>(){});
	}
	@Override
	public void onItemListClick(AdapterView<?> parent, View view, long id,	EnumJson item)
	{
		FilterDataCache.getInstance().update(item.getField_name(),item.getEnum_value());
		setResult(IntentCodeConst.RESULT_CODE_ITEM_CONFIRM);
		this.finish();
	}


}
