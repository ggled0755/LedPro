package cn.fuego.led.ui.scan;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import cn.fuego.common.contanst.ConditionTypeEnum;
import cn.fuego.common.dao.QueryCondition;
import cn.fuego.common.util.validate.ValidatorUtil;
import cn.fuego.led.R;
import cn.fuego.led.constant.IntentCodeConst;
import cn.fuego.led.ui.base.LedBaseActivity;
import cn.fuego.led.ui.home.ProductDetailActivity;
import cn.fuego.led.webservice.up.model.base.ProductJson;
import cn.fuego.led.webservice.up.rest.WebServiceContext;
import cn.fuego.misp.constant.MispErrorCode;
import cn.fuego.misp.service.http.MispHttpHandler;
import cn.fuego.misp.service.http.MispHttpMessage;
import cn.fuego.misp.webservice.json.MispBaseReqJson;
import cn.fuego.misp.webservice.json.MispBaseRspJson;

public class BarInputActivity extends LedBaseActivity
{

	private EditText txt_input;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		txt_input = (EditText) findViewById(R.id.bar_input_txt);
	}

	@Override
	public void initRes()
	{
		this.activityRes.setAvtivityView(R.layout.activity_bar_input);
		this.activityRes.setName(getString(R.string.title_activity_bar_input));
		this.activityRes.setSaveBtnName(getString(R.string.btn_view));
		this.activityRes.getButtonIDList().add(R.id.bar_input_scan_btn);
		
	}

	@Override
	public void saveOnClick(View v)
	{
		String code = txt_input.getText().toString().trim();
		if(!ValidatorUtil.isEmpty(code))
		{
			serachProByBar(code);
		}
		else
		{
			showMessage(getString(R.string.msg_input_null));
		}
	}

	private void serachProByBar(String code)
	{
		final ProgressDialog pd = ProgressDialog.show(this, null, getString(R.string.progress_msg_loading));
		
		MispBaseReqJson req = new MispBaseReqJson();
		List<QueryCondition> conditionList = new ArrayList<QueryCondition>();
		conditionList.add(new QueryCondition(ConditionTypeEnum.EQUAL, "product_code", code));
		req.setConditionList(conditionList);		
		WebServiceContext.getInstance().getProductRest(new MispHttpHandler(){
			@Override
			public void handle(MispHttpMessage message)
			{
				pd.dismiss();
				if(message.isSuccess())
				{
					MispBaseRspJson rsp = (MispBaseRspJson) message.getMessage().obj;
					ProductJson p = rsp.GetReqCommonField(ProductJson.class);
					if(null!=p)
					{
						ProductDetailActivity.jump(BarInputActivity.this, p);
					}
					else
					{
						showMessage(MispErrorCode.RESULT_NULL);
					}
					
				}
				else
				{
					showMessage(message);
				}
			}
		}).getProduct(req);	
		
	}

	@Override
	public void onClick(View v)
	{
		if(v.getId()==R.id.bar_input_scan_btn)
		{
			Intent i = new Intent();
			i.setClass(this, CaptureActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
			i.putExtra(IntentCodeConst.JUMP_DATA, CaptureActivity.STATE_BAR);
			startActivity(i);
			this.finish();
			
		}
	}

	
}
