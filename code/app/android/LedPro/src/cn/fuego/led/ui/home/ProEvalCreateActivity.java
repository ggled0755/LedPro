package cn.fuego.led.ui.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.type.TypeReference;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import cn.fuego.led.R;
import cn.fuego.led.ui.base.LedBaseListActivity;
import cn.fuego.led.ui.widget.OrderButton;
import cn.fuego.led.webservice.up.model.base.EvalJson;
import cn.fuego.led.webservice.up.model.base.EvalTypeJson;
import cn.fuego.led.webservice.up.model.base.ProductJson;
import cn.fuego.led.webservice.up.rest.WebServiceContext;
import cn.fuego.misp.service.http.MispHttpHandler;
import cn.fuego.misp.service.http.MispHttpMessage;
import cn.fuego.misp.ui.model.ListViewResInfo;
import cn.fuego.misp.webservice.json.MispBaseReqJson;
import cn.fuego.misp.webservice.json.MispBaseRspJson;

public class ProEvalCreateActivity extends LedBaseListActivity<EvalTypeJson>
{

	private ProductJson product;
	private Map<Integer,EvalJson> eval_map = new HashMap<Integer, EvalJson>();
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
	}
	@Override
	public void initRes()
	{
		this.activityRes.setAvtivityView(R.layout.activity_pro_eval_create);
		this.activityRes.setSaveBtnName(getResources().getString(R.string.btn_save));
		this.activityRes.setName(getResources().getString(R.string.title_activity_pro_eval_create));
		
		this.listViewRes.setListView(R.id.pro_eval_create_list);
		this.listViewRes.setListItemView(R.layout.list_item_eval_create);
		
		product = (ProductJson) this.getIntent().getSerializableExtra(ListViewResInfo.SELECT_ITEM);
	}

	public static void jump(Context context,ProductJson json)
	{
		Intent i = new Intent();
		i.setClass(context, ProEvalCreateActivity.class);
		i.putExtra(ListViewResInfo.SELECT_ITEM, json);
		context.startActivity(i);
	}
	
	@Override
	public void saveOnClick(View v)
	{
		if(eval_map.size()==0)
		{
			return;
		}
		List<EvalJson> evalList = new ArrayList<EvalJson>();
		for(Integer key :eval_map.keySet())
		{
			evalList.add(eval_map.get(key));
		}

		MispBaseReqJson req = new MispBaseReqJson();
		req.setObj(evalList);
		WebServiceContext.getInstance().getProductRest(new MispHttpHandler(){
			@Override
			public void handle(MispHttpMessage message)
			{
				if(message.isSuccess())
				{
					finish();
				}
				else
				{
					showMessage(message);
				}
			}
		}).createEvalList(req);
	}
	@Override
	public View getListItemView(View view, final EvalTypeJson item)
	{
		if(null==eval_map.get(item.getEval_type_id()))
		{
			EvalJson e = new EvalJson();
			e.setEval_type(item.getEval_type_id());
			e.setEval_obj_id(product.getProduct_id());
			eval_map.put(item.getEval_type_id(), e);

		}
		TextView txt_name = (TextView) view.findViewById(R.id.item_eval_create_name);
		txt_name.setText(item.getEval_type_name());
		
		RatingBar rb_score = (RatingBar) view.findViewById(R.id.item_eval_create_rating);
		rb_score.setRating(0);
		rb_score.setOnRatingBarChangeListener(new OnRatingBarChangeListener()
		{
			
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser)
			{
				
				if(null!=eval_map.get(item.getEval_type_id()))
				{
					eval_map.get(item.getEval_type_id()).setEval_value(rating);
				}			
				
			}
		});

		return view;
	}

	@Override
	public void loadSendList()
	{
		MispBaseReqJson req = new MispBaseReqJson();
		WebServiceContext.getInstance().getProductRest(this).loadEvalType(req);
		
	}

	@Override
	public List<EvalTypeJson> loadListRecv(Object obj)
	{
		MispBaseRspJson rsp = (MispBaseRspJson) obj;
		return rsp.GetReqCommonField(new TypeReference<List<EvalTypeJson>>(){});
	}

	@Override
	public void onOrderStateChanged(OrderButton orderBtn, Integer orderState)
	{
		// TODO Auto-generated method stub
		
	}

}
