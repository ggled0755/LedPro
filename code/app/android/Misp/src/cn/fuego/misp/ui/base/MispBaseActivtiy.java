package cn.fuego.misp.ui.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cn.fuego.common.log.FuegoLog;
import cn.fuego.common.util.validate.ValidatorUtil;
import cn.fuego.misp.constant.MispCommonIDName;
import cn.fuego.misp.constant.MispErrorCode;
import cn.fuego.misp.service.http.MispHttpMessage;
import cn.fuego.misp.ui.dailog.MispWaitDailog;
import cn.fuego.misp.ui.model.ActivityResInfo;

public abstract class MispBaseActivtiy extends Activity implements OnClickListener
{
	public static String RETURN_DATA = "returnData";
	private FuegoLog log = FuegoLog.getLog(MispBaseActivtiy.class);
 	private TextView titleView;
	private Button saveButton;
	protected MispWaitDailog waitDailog;
	public abstract void initRes();
	protected static void jumpToActivity(Context context,Class clazz)
	{
		Intent intent = new Intent(context,clazz);
 
		context.startActivity(intent);
	}
	
	public void backOnClick()
	{
		this.finish();
	}
	
	public void saveOnClick(View v)
	{
		this.finish();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{  		
		
		super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		waitDailog = new MispWaitDailog(this);  
		//点击外面不消失
		waitDailog.setCanceledOnTouchOutside(false);
		initRes();
		// TODO Auto-generated method stub
 		if(0 != activityRes.getAvtivityView())
 		{
 			setContentView(activityRes.getAvtivityView());
 		}
		initBackButton();
		//setup title
		initTitle();	
		initActivityButton();

		
	}
	
	private void initActivityButton()
	{
		 
		if(!ValidatorUtil.isEmpty(this.activityRes.getButtonIDList()))
		{
			for(Integer id :this.activityRes.getButtonIDList() )
			{
				View btn =    findViewById(id);
				if(null != btn)
				{
					btn.setOnClickListener(this);
 				}
				else
				{
					log.warn("the button id is not exist in the view, the id is "+id);
				}
			}
		}
	}

	private void initTitle()
	{
		TextView title =(TextView) findViewById(activityRes.getTitleTextView());
		if(null != title)
		{
			title.setText(activityRes.getName());
		}
		else
		{
			titleView = (TextView) findViewById(MispCommonIDName.misp_title_name);
			if(null != titleView)
			{
				titleView.setText(this.activityRes.getName());
			}
		}
		saveButton = (Button) findViewById(MispCommonIDName.misp_tilte_save);
		if(null != saveButton)
		{
			if(!ValidatorUtil.isEmpty(activityRes.getSaveBtnName()))
			{
				saveButton.setText(activityRes.getSaveBtnName());
			}
			saveButton.setVisibility(View.VISIBLE);
			saveButton.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					saveOnClick(v);
					
				}
			});
		}


		
		
	}
	
	
	
	public Button getSaveButton()
	{
		return saveButton;
	}

	private void initBackButton()
	{
		View button = findViewById(activityRes.getBackBtn());
		if(null == button)
		{
			 button = findViewById(MispCommonIDName.misp_title_back);
		}
		if(null != button)
		{
			 
			OnClickListener l = new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					backOnClick();
				}
			};
			button.setOnClickListener(l);
		}
	}
 
	public ActivityResInfo activityRes = new ActivityResInfo();

	
	public void showMessage(MispHttpMessage message)
	{
		showMessage(message.getErrorCode());	
	}
	
	public void showMessage(int errorCode)
	{
		showMessage(MispErrorCode.getMessageByErrorCode(errorCode));
	}
	
	public void showMessage(String message)
	{
		Toast toast;
		toast = Toast.makeText(getApplicationContext(), message , Toast.LENGTH_SHORT);
		toast.show();
	}
	public int getScreenWidth()
	{
		DisplayMetrics metric = new DisplayMetrics();
	    getWindowManager().getDefaultDisplay().getMetrics(metric);
	    int width = metric.widthPixels;     // 屏幕宽度（像素）
 	    
	    return width;
	}
	public int getActivityHeight()
	{
		DisplayMetrics metric = new DisplayMetrics();
	    this.getWindowManager().getDefaultDisplay().getMetrics(metric);
 	    int height = metric.heightPixels;
 	    height = height-getStatusHeight(this);
	    return height;
	}
	public int getStatusHeight(Activity activity){
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight){
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
            } catch (Exception e) {
                e.printStackTrace();
            }  
        }
        return statusHeight;
    }
	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		
	}
	public TextView getTitleView()
	{
		return titleView;
	}
 
    public void showToast(Context mContext,String Msg)
    {
    	Toast.makeText(mContext, Msg, Toast.LENGTH_SHORT).show();
    }
    public void showToast(Context mContext,MispHttpMessage message)
    {

	    Toast.makeText(mContext, MispErrorCode.getMessageByErrorCode(message.getErrorCode()), Toast.LENGTH_SHORT).show();
    }

}
