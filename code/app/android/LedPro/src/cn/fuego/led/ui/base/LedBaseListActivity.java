/**   
* @Title: LedBaseListActivity.java 
* @Package cn.fuego.led.ui.base 
* @Description: TODO
* @author Aether
* @date 2015-7-6 下午9:30:37 
* @version V1.0   
*/ 
package cn.fuego.led.ui.base;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import cn.fuego.led.R;
import cn.fuego.led.constant.IntentCodeConst;
import cn.fuego.led.ui.base.HostChkListener.HeightChanngeCallback;
import cn.fuego.led.ui.filter.FilterRefineActivity;
import cn.fuego.led.ui.widget.OrderButton;
import cn.fuego.led.ui.widget.OrderButton.OnOrderClickCallback;
import cn.fuego.misp.ui.list.MispListActivity;

/** 
 * @ClassName: LedBaseListActivity 
 * @Description: TODO
 * @author Aether
 * @date 2015-7-6 下午9:30:37 
 *  
 */
public abstract class LedBaseListActivity<E> extends MispListActivity<E> implements OnOrderClickCallback, HeightChanngeCallback
{

	public Typeface ttf_cabin_bold;
	public Typeface ttf_cabin_semibold;
	public Typeface ttf_cabin_regular;
	public Typeface ttf_Helvetica_Neue;
	private TabbarClickListener tabBarClick =new TabbarClickListener();
	private HostChkListener chkListener = new HostChkListener();
	
	public List<Integer> btnList = new ArrayList<Integer>();
	
	private View search_preview;
	public EditText search_input;
	protected Button search_clear_btn;
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
		
		View bottom_up = findViewById(R.id.bottom_search_view);
		View bottom_down = findViewById(R.id.bottom_tab_view);
		if(bottom_up!=null&&bottom_down!=null)
		{
			HostChkListener.initView(this, bottom_up, bottom_down,this);
		}
		
		
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
		
		
		//过滤条件的filter
		initOrder();
		
		
	}



	private void initOrder()
	{
		OrderButton pf_btn = (OrderButton) findViewById(R.id.order_pf_btn);
		if(pf_btn!=null)
		{
			pf_btn.initUI(R.drawable.btn_order_pf, this);
			btnList.add(R.id.order_pf_btn);
		}
		
		OrderButton like_btn = (OrderButton) findViewById(R.id.order_like_btn);
		if(like_btn!=null)
		{
			like_btn.initUI(R.drawable.btn_order_like, this);
			btnList.add(R.id.order_like_btn);
		}
		
		OrderButton price_btn = (OrderButton) findViewById(R.id.order_price_btn);
		if(price_btn!=null)
		{
			price_btn.initUI(R.drawable.btn_order_price, this);
			btnList.add(R.id.order_price_btn);
		}
		
		Button filter_btn = (Button) findViewById(R.id.sort_filter_btn);
		if(filter_btn!=null)
		{
			filter_btn.setOnClickListener(this);
		}		
		
		search_preview = findViewById(R.id.host_search_preview);
		if(search_preview!=null)
		{
			search_preview.setOnClickListener(this);
		}
		
		
		search_input = (EditText) findViewById(R.id.host_search_input);
		//search_input.clearFocus();
		
		search_clear_btn = (Button) findViewById(R.id.host_search_clear_btn);
		if(search_clear_btn!=null)
		{
			search_clear_btn.setOnClickListener(this);
		}
		
	}



	@Override
	public void onClick(View v)
	{
		if(v.getId()==R.id.sort_filter_btn)
		{
			//PopFilterUtil.getInstance().showPop(this, v, this);//方法已经废弃
			Intent i = new Intent();
			i.setClass(this, FilterRefineActivity.class);
			startActivityForResult(i, IntentCodeConst.REQUEST_CODE);
		}
		if(v.getId()==R.id.host_search_preview)
		{
			search_preview.setVisibility(View.GONE);
			search_input.setVisibility(View.VISIBLE);
			search_input.setFocusable(true);
			search_input.setFocusableInTouchMode(true);
			//先设置焦点再获取焦点
			search_input.requestFocus();
			search_input.requestFocusFromTouch();
			search_input.setInputType(InputType.TYPE_CLASS_TEXT); 

			
		}
		if(v.getId()==R.id.host_search_clear_btn)
		{
			search_input.setText("");  
			search_clear_btn.setVisibility(View.GONE); 
		}
	}


}
