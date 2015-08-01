/**   
* @Title: HostChkListener.java 
* @Package cn.fuego.led.ui.base 
* @Description: TODO
* @author Aether
* @date 2015-7-13 下午9:13:43 
* @version V1.0   
*/ 
package cn.fuego.led.ui.base;

import android.app.ActivityManager;
import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import cn.fuego.led.R;
import cn.fuego.led.ui.profile.ProfileActivity;
import cn.fuego.led.ui.widget.OrderButton;

/** 
 * @ClassName: HostChkListener 
 * @Description: TODO
 * @author Aether
 * @date 2015-7-13 下午9:13:43 
 *  
 */
public class HostChkListener implements OnCheckedChangeListener
{

	private static View bottom_up;
	private static View bottom_down;
	private static Context mContext;
	private static HeightChanngeCallback hCallback;
    public interface HeightChanngeCallback
    {
        public void onHeightChanged(CompoundButton buttonView);
    }
	public static void initView(Context context,View view_up,View view_down,HeightChanngeCallback heightChangeCallBack)
	{
		mContext =context;
		bottom_up = view_up;
		bottom_down = view_down;
		hCallback=heightChangeCallBack;
	}
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
	{

		
		hCallback.onHeightChanged(buttonView);
		//get current task
		ActivityManager activityManager=(ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);  
		String runningActivity=activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
		
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
			if(ProfileActivity.class.getName().equals(runningActivity))
			{
				return;
			}
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

}
