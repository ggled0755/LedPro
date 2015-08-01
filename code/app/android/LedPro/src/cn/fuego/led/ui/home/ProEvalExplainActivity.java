package cn.fuego.led.ui.home;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.type.TypeReference;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RatingBar;
import android.widget.TextView;
import cn.fuego.common.contanst.ConditionTypeEnum;
import cn.fuego.common.dao.QueryCondition;
import cn.fuego.led.R;
import cn.fuego.led.ui.base.LedBaseListActivity;
import cn.fuego.led.ui.widget.OrderButton;
import cn.fuego.led.webservice.up.model.base.ProductJson;
import cn.fuego.led.webservice.up.model.base.ViewEvalSum;
import cn.fuego.led.webservice.up.rest.WebServiceContext;
import cn.fuego.misp.ui.model.ListViewResInfo;
import cn.fuego.misp.webservice.json.MispBaseReqJson;
import cn.fuego.misp.webservice.json.MispBaseRspJson;

public class ProEvalExplainActivity extends LedBaseListActivity<ViewEvalSum>
{

	private ProductJson product;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		TextView txt_name = (TextView) findViewById(R.id.product_eval_name);
		RatingBar rb_eval = (RatingBar) findViewById(R.id.product_eval_rating);
		if(null!=product)
		{
			txt_name.setText(product.getProduct_name());
			rb_eval.setRating(product.getProduct_score());
		}


	}

	@Override
	public void initRes()
	{
		this.activityRes.setAvtivityView(R.layout.activity_product_eval);
		this.activityRes.setName(getResources().getString(R.string.title_activity_product_eval));
		this.listViewRes.setListView(R.id.product_eval_list);
		this.listViewRes.setListItemView(R.layout.list_item_btn);
		
		product =(ProductJson) this.getIntent().getSerializableExtra(ListViewResInfo.SELECT_ITEM);
		
	}

	public static void jump(Context context,ProductJson json)
	{
		Intent i = new Intent();
		i.setClass(context, ProEvalExplainActivity.class);
		i.putExtra(ListViewResInfo.SELECT_ITEM, json);
		context.startActivity(i);
	}

	@Override
	public View getListItemView(View view, ViewEvalSum item)
	{
		TextView txt_name= (TextView) view.findViewById(R.id.item_btn_title);
		txt_name.setText(item.getEval_type_name());
		
		TextView txt_value = (TextView) view.findViewById(R.id.item_btn_value);
		txt_value.setText(String.valueOf(item.getEval_avg_value()));
		
		return view;
	}
	@Override
	public void loadSendList()
	{
		MispBaseReqJson req = new MispBaseReqJson();
		List<QueryCondition> conditionList = new ArrayList<QueryCondition>();
		conditionList.add(new QueryCondition(ConditionTypeEnum.EQUAL, "eval_obj_id", product.getProduct_id()));
		req.setConditionList(conditionList);
		
		WebServiceContext.getInstance().getProductRest(this).loadEvalSum(req);
		
	}
	@Override
	public List<ViewEvalSum> loadListRecv(Object obj)
	{
		MispBaseRspJson rsp = (MispBaseRspJson) obj;
		
		return rsp.GetReqCommonField(new TypeReference<List<ViewEvalSum>>(){});
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
