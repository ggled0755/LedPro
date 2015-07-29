package cn.fuego.led.ui.scan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import cn.fuego.common.util.validate.ValidatorUtil;
import cn.fuego.led.R;
import cn.fuego.led.constant.IntentCodeConst;
import cn.fuego.led.ui.base.LedBaseActivity;

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
		// TODO Auto-generated method stub
		
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
