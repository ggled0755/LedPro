/**   
* @Title: LedBaseActivity.java 
* @Package cn.fuego.led.ui.base 
* @Description: TODO
* @author Aether
* @date 2015-7-7 下午8:51:55 
* @version V1.0   
*/ 
package cn.fuego.led.ui.base;


import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import cn.fuego.led.R;
import cn.fuego.led.ui.home.HomeActivity;
import cn.fuego.led.ui.profile.ProfileActivity;
import cn.fuego.led.ui.scan.CaptureActivity;
import cn.fuego.led.ui.setting.SettingActivity;
import cn.fuego.misp.ui.base.MispBaseActivtiy;

/** 
 * @ClassName: LedBaseActivity 
 * @Description: TODO
 * @author Aether
 * @date 2015-7-7 下午8:51:55 
 *  
 */
public abstract class LedBaseActivity extends MispBaseActivtiy
{
	private View bottom_up;
	private View bottom_down;
	public Typeface ttf_cabin_bold;
	public Typeface ttf_cabin_semibold;
	public Typeface ttf_cabin_regular;
	public Typeface ttf_Helvetica_Neue;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        //将字体文件保存在assets/fonts/目录下，创建Typeface对象
		ttf_cabin_bold = Typeface.createFromAsset(getAssets(), "fonts/Cabin-Bold.ttf");
		ttf_cabin_semibold = Typeface.createFromAsset(getAssets(), "fonts/Cabin-SemiBold.ttf");
		ttf_cabin_regular = Typeface.createFromAsset(getAssets(), "fonts/Cabin-Regular.otf");
		ttf_Helvetica_Neue = Typeface.createFromAsset(getAssets(), "fonts/Helvetica-Neue.ttf");
		
		bottom_up = findViewById(R.id.bottom_search_view);
		bottom_down = findViewById(R.id.bottom_tab_view);
		if(this.getTitleView()!=null)
		{
			this.getTitleView().setTypeface(ttf_cabin_bold);
		}
	
		CheckBox chk_tab= (CheckBox) findViewById(R.id.host_tab_chk);
		if(chk_tab!=null)
		{
			chk_tab.setOnCheckedChangeListener(chkListener);
		}		
		CheckBox chk_serch = (CheckBox) findViewById(R.id.host_search_chk);
		if(chk_serch!=null)
		{
			chk_serch.setOnCheckedChangeListener(chkListener);
		}				
		
		Button home_btn = (Button) this.findViewById(R.id.bottom_home_btn);
		if(home_btn!=null)
		{
			home_btn.setOnClickListener(tabBarClick);
		}			
		Button scan_btn = (Button) this.findViewById(R.id.bottom_scan_btn);
		if(scan_btn!=null)
		{
			scan_btn.setOnClickListener(tabBarClick);
		}		
		Button profile_btn = (Button) this.findViewById(R.id.bottom_profile_btn);
		if(profile_btn!=null)
		{
			profile_btn.setOnClickListener(tabBarClick);
		}
		Button setting_btn = (Button) this.findViewById(R.id.bottom_setting_btn);
		if(setting_btn!=null)
		{
			setting_btn.setOnClickListener(tabBarClick);
		}	
		
	}
	OnCheckedChangeListener chkListener = new OnCheckedChangeListener()
	{
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		{
			if(buttonView.getId()==R.id.host_tab_chk)
			{
				if(isChecked)
				{
					bottom_down.setVisibility(View.VISIBLE);
				}
				else
				{
					bottom_down.setVisibility(View.GONE);
				}
			}
			if(buttonView.getId()==R.id.host_search_chk)
			{
				if(isChecked)
				{
					bottom_up.setVisibility(View.VISIBLE);
				}
				else
				{
					bottom_up.setVisibility(View.GONE);
				}
			}
			
		}
	};
	OnClickListener tabBarClick = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			//get current task
			ActivityManager activityManager=(ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);  
			String runningActivity=activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
		   
			switch (v.getId())
			{
			case R.id.bottom_home_btn:
				if(HomeActivity.class.getName().equals(runningActivity))
				{
					return;
				}
				HomeActivity.jump(v.getContext());
				finish();
				break;
			case R.id.bottom_scan_btn:
				CaptureActivity.jump(v.getContext(), CaptureActivity.STATE_QR);
				break;
			case R.id.bottom_profile_btn:
				if(ProfileActivity.class.getName().equals(runningActivity))
				{
					return;
				}
				ProfileActivity.jump(v.getContext());
				finish();
				break;
			case R.id.bottom_setting_btn:
				SettingActivity.jump(v.getContext());
				break;
				default:break;
				
			}
			
		}
	};
	
	
}
