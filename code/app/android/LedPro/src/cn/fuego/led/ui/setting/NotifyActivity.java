package cn.fuego.led.ui.setting;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import cn.fuego.led.R;
import cn.fuego.led.cache.AppCache;
import cn.fuego.led.dao.ConfigInfo;
import cn.fuego.led.ui.base.LedBaseActivity;

public class NotifyActivity extends LedBaseActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		CheckBox chk = (CheckBox) findViewById(R.id.notify_chk);
		if(null!=AppCache.getInstance().getCfgInfo())
		{
			chk.setChecked(AppCache.getInstance().getCfgInfo().isNeedNotify());
		}
		
		chk.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				ConfigInfo config = AppCache.getInstance().getCfgInfo();
				if(null==config)
				{
					config= new ConfigInfo();
				}
				config.setNeedNotify(isChecked);
				AppCache.getInstance().updateConfig(config);
				
			}
		});
	}

	@Override
	public void initRes()
	{
		this.activityRes.setAvtivityView(R.layout.activity_notify);
		this.activityRes.setName(getString(R.string.title_setting_notifications));
		
	}
}
