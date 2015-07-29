package cn.fuego.led.ui.scan;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.fuego.common.contanst.ConditionTypeEnum;
import cn.fuego.common.dao.QueryCondition;
import cn.fuego.common.util.validate.ValidatorUtil;
import cn.fuego.led.R;
import cn.fuego.led.ui.base.LedBaseActivity;
import cn.fuego.led.ui.home.ProductDetailActivity;
import cn.fuego.led.webservice.up.model.base.ProductJson;
import cn.fuego.led.webservice.up.rest.WebServiceContext;
import cn.fuego.misp.service.http.MispHttpHandler;
import cn.fuego.misp.service.http.MispHttpMessage;
import cn.fuego.misp.webservice.json.MispBaseReqJson;
import cn.fuego.misp.webservice.json.MispBaseRspJson;

public class ResultActivity extends LedBaseActivity
{

	private ImageView barcodeImageView;

	private TextView resultTextView;
	private Bitmap barcodeBitmap;
	private String barcodeFormat;
	private String decodeDate;
	private CharSequence metadataText;
	private String resultString;
	private Bundle bundle;

	private ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		mGetIntentData();
		setView();

	}
	@Override
	public void initRes()
	{
		this.activityRes.setAvtivityView(R.layout.activity_result);
		this.activityRes.setName(getString(R.string.title_activity_result));
		this.activityRes.setSaveBtnName(getString(R.string.btn_view));
	}
	
	@Override
	public void saveOnClick(View v)
	{
		String result = resultTextView.getText().toString().trim();
		if(!ValidatorUtil.isEmpty(result))
		{
			loadProduct(result);
		}
		else
		{
			showMessage(getString(R.string.msg_input_null));
		}
	}
	private void loadProduct(String result)
	{
		pd = ProgressDialog.show(this, null, getString(R.string.progress_msg_loading));
		
		MispBaseReqJson req = new MispBaseReqJson();
		List<QueryCondition> conditionList = new ArrayList<QueryCondition>();
		conditionList.add(new QueryCondition(ConditionTypeEnum.EQUAL, "product_code", result));
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
						ProductDetailActivity.jump(ResultActivity.this, p);
					}
					
				}
				else
				{
					showMessage(message);
				}
			}
		}).getProduct(req);
		
	}
	private void setView() 
	{
		barcodeImageView.setImageBitmap(barcodeBitmap);

		resultTextView.setText(resultString);
	}

	private void mGetIntentData() {
		bundle = new Bundle();
		bundle = this.getIntent().getExtras();
		barcodeBitmap = bundle.getParcelable("bitmap");
		barcodeFormat = bundle.getString("barcodeFormat");
		decodeDate = bundle.getString("decodeDate");
		metadataText = bundle.getCharSequence("metadataText");
		resultString = bundle.getString("resultString");
	}

	private void initView() {
		barcodeImageView = (ImageView) findViewById(R.id.barcode_image_view);
		resultTextView = (TextView) findViewById(R.id.contents_text_view);

	}

	public void backCapture(View view) {
		runBack();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			runBack();
		}
		return false;
	}

	public void runBack() {
		Intent intent = new Intent(ResultActivity.this, CaptureActivity.class);
		startActivity(intent);
		ResultActivity.this.finish();
	}


}
