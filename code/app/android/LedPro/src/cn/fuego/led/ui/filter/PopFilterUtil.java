/**   
* @Title: PopFilterUtil.java 
* @Package cn.fuego.led.ui.base 
* @Description: TODO
* @author Aether
* @date 2015-7-13 下午9:45:04 
* @version V1.0   
*/ 
package cn.fuego.led.ui.filter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;
import cn.fuego.led.R;
import cn.fuego.led.ui.widget.RangeSeekBar;
import cn.fuego.led.ui.widget.RangeSeekBar.OnRangeSeekBarChangeListener;
import cn.fuego.misp.ui.base.MispListViewAdaptScroll;
import cn.fuego.misp.ui.model.ListViewResInfo;


/** 
 * @ClassName: PopFilterUtil 
 * @Description: TODO
 * @author Aether
 * @date 2015-7-13 下午9:45:04 
 *  
 */
public class PopFilterUtil implements OnRangeSeekBarChangeListener<Float>, OnItemClickListener
{
	private static PopFilterUtil instance;
	private PopupWindow popView;

	private Context mContext;
	private OnPopCallback mCallback;
	
	private PopFilterUtil() 
	{
	}
	/**
	 * 单例模式获取
	 * @return
	 */
	public static PopFilterUtil getInstance() 
	{
		if (null == instance)
		{
			instance = new PopFilterUtil();
		}
		return instance;

	}
	
	//main
	public void showPop(Context context,final View parent,OnPopCallback callback)
	{
		mContext=context;
		mCallback=callback;
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
        View view = layoutInflater.inflate(R.layout.pop_filter, null);
        
        MispListViewAdaptScroll filterList = (MispListViewAdaptScroll) view.findViewById(R.id.pop_filter_list);
        
		Button cancel_btn = (Button) view.findViewById(R.id.pop_filter_cancel_btn);
		cancel_btn.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				popView.dismiss();
				mCallback.onPopClick(0);
				
			}
		});
		
		Button confirm_btn = (Button) view.findViewById(R.id.pop_filter_confirm_btn);
		confirm_btn.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				popView.dismiss();
				mCallback.onPopClick(1);
				
			}
		});
		
		//seekbar
        ViewGroup lumen_root = (ViewGroup) view.findViewById(R.id.filter_seek_lm);
        RangeSeekBar<Float> lumen_seek;
        lumen_seek= new RangeSeekBar<Float>(1f, 100f, context,3);
        lumen_root.addView(lumen_seek);
		if(popView==null)
		{
			
            popView = new PopupWindow(view, (int) (wm.getDefaultDisplay().getWidth()*0.8), LayoutParams.MATCH_PARENT, true);            
            popView.setAnimationStyle(R.style.popAnimFade);
            popView.setOutsideTouchable(true);
            ColorDrawable dw = new ColorDrawable(0x90000000);  
            popView.setBackgroundDrawable(dw);
            FilterDataCache.getInstance().initData(context);

		}
		 popView.showAtLocation(parent, Gravity.RIGHT, 0, 0);
        //设置背景变暗
		final Window w=((Activity) context).getWindow();
        final WindowManager.LayoutParams lp= w.getAttributes();
        lp.alpha = 0.4f;
        w.setAttributes(lp); 

		// 监听popwindow消失事件，并对radioGroup清零
		popView.setOnDismissListener(new OnDismissListener()
		{
			@Override
			public void onDismiss()
			{

				lp.alpha = 1f;
				w.setAttributes(lp);
				//popView = null;
			}
		});
		
		lumen_seek.setOnRangeSeekBarChangeListener(this);
		filterList.setAdapter(new DistinctAdapter(context, FilterDataCache.getInstance().getData()));
		filterList.setOnItemClickListener(this);
		
	}
	
	@Override
	public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar,Float minValue, Float maxValue)
	{
		Toast.makeText(mContext, "minValue："+minValue+"maxValue:"+maxValue, Toast.LENGTH_LONG).show();
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		FilterItemMeta selItem=(FilterItemMeta) parent.getAdapter().getItem(position);
		if(selItem!=null)
		{
			load(selItem);
		}
		
		
		
	}

	private void load(FilterItemMeta selItem)
	{
		Intent i = new Intent();
		i.setClass(mContext, FilterListActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.putExtra(ListViewResInfo.SELECT_ITEM, selItem);
		mContext.startActivity(i);
	}

	//定义回调接口
    public interface OnPopCallback
    {
        public void onPopClick(Integer btnState);
    }

	
	
}
