package cn.fuego.led.ui;


import android.os.Bundle;
import android.os.CountDownTimer;
import cn.fuego.led.R;
import cn.fuego.led.ui.home.HomeActivity;
import cn.fuego.misp.service.MemoryCache;
import cn.fuego.misp.ui.base.MispBaseActivtiy;

public class StartActivity extends MispBaseActivtiy 
{

	public static boolean isForeground = false;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(MemoryCache.isLogined())
		{
			//AppCache.getInstance().loadCustomer();
		}	
 		new CountDownTimer(2000, 1000)
		{

			@Override
			public void onTick(long millisUntilFinished)
			{
			}

			@Override
			public void onFinish()
			{
 
				startMain();

				  
			}


		}.start();

		
	}
 
	private void startMain()
	{
		//MainTabbarActivity.jump(this, MainTabbarActivity.class, 1);
		jumpToActivity(this, HomeActivity.class);

		@SuppressWarnings("deprecation")
		int VERSION = Integer.parseInt(android.os.Build.VERSION.SDK);
		if (VERSION >= 5)
		{
			StartActivity.this.overridePendingTransition(
					R.anim.alpha_in, R.anim.alpha_out);
		}
		finish();
	}

	@Override
	public void initRes()
	{
		//this.activityRes.getButtonIDList().add(R.id.welcome_go_home);
		this.activityRes.setAvtivityView(R.layout.activity_start);
		
	}


}
