/**   
* @Title: TabbarClickListener.java 
* @Package cn.fuego.led.ui.base 
* @Description: TODO
* @author Aether
* @date 2015-7-13 下午9:06:22 
* @version V1.0   
*/ 
package cn.fuego.led.ui.base;

import cn.fuego.led.R;
import cn.fuego.led.ui.home.HomeActivity;
import cn.fuego.led.ui.profile.ProfileActivity;
import cn.fuego.led.ui.scan.CaptureActivity;
import cn.fuego.led.ui.setting.SettingActivity;
import android.app.ActivityManager;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

/** 
 * @ClassName: TabbarClickListener 
 * @Description: TODO
 * @author Aether
 * @date 2015-7-13 下午9:06:22 
 *  
 */
public class TabbarClickListener implements OnClickListener
{

	private Context mContext;
	
	@Override
	public void onClick(View v)
	{
		mContext = v.getContext();
		//get current task
		ActivityManager activityManager=(ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);  
		String runningActivity=activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
	   
		switch (v.getId())
		{
		case R.id.bottom_home_btn:
			if(HomeActivity.class.getName().equals(runningActivity))
			{
				return;
			}
			HomeActivity.jump(v.getContext());
			
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

			break;
		case R.id.bottom_setting_btn:
			SettingActivity.jump(v.getContext());
			break;
			default:break;
			
		}
		
	}

}
