package cn.fuego.led.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import cn.fuego.led.R;
import cn.fuego.led.cache.AppCache;
import cn.fuego.led.constant.IntentCodeConst;
import cn.fuego.led.ui.LoginActivity;
import cn.fuego.led.ui.base.LedBaseActivity;
import cn.fuego.led.ui.home.HomeActivity;
import cn.fuego.misp.service.MemoryCache;

public class SettingActivity extends LedBaseActivity
{

	private Button log_btn;
	@Override
	public void initRes()
	{
		this.activityRes.setAvtivityView(R.layout.activity_setting);
		this.activityRes.setName(getResources().getString(R.string.title_activity_setting));
		this.activityRes.getButtonIDList().add(R.id.setting_notifications_btn);
		this.activityRes.getButtonIDList().add(R.id.setting_privacy_btn);
		this.activityRes.getButtonIDList().add(R.id.setting_general_btn);
		this.activityRes.getButtonIDList().add(R.id.setting_about_btn);
		this.activityRes.getButtonIDList().add(R.id.setting_logout_btn);
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		log_btn = (Button) findViewById(R.id.setting_logout_btn);
		if(MemoryCache.isLogined())
		{
			log_btn.setText(getResources().getString(R.string.btn_logout));
		}
		else
		{
			log_btn.setText(getResources().getString(R.string.btn_login));
		}

	}
	public static void jump(Context context)
	{
		Intent intent = new Intent(context,SettingActivity.class);
		context.startActivity(intent);
	
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.setting_logout_btn:
			if(MemoryCache.isLogined())
			{
				AppCache.getInstance().clear();
				HomeActivity.jump(v.getContext());
				finish();
			}
			else
			{
				LoginActivity.jump(this, IntentCodeConst.REQUEST_CODE);
				finish();
			}
			break;
		case R.id.setting_notifications_btn:
			jumpToActivity(this, NotifyActivity.class);
			break;
		case R.id.setting_about_btn:
			jumpToActivity(this, AboutActivity.class);
			break;
		default:
			break;
		}
	}

	
}
