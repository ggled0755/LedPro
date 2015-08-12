/**   
* @Title: SortButton.java 
* @Package cn.fuego.led.ui.widget 
* @Description: TODO
* @author Aether
* @date 2015-7-18 下午5:45:28 
* @version V1.0   
*/ 
package cn.fuego.led.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cn.fuego.led.R;

/** 
 * @ClassName: SortButton 
 * @Description: TODO
 * @author Aether
 * @date 2015-7-18 下午5:45:28 
 *  
 */
public class OrderButton extends LinearLayout implements OnClickListener
{
	private Context mContext; 
	private ImageView img_left;
	private ImageView img_right;
	private Integer[] dir_list={R.drawable.icon_order_null,R.drawable.icon_order_up,R.drawable.icon_order_down};
	private int order_state =0;
	private OnOrderClickCallback orderCallback;
	private LinearLayout btn;
	public OrderButton(Context context)
	{
		super(context);
		mContext=context;
	}


	public OrderButton(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		mContext=context;
	}
	public OrderButton(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		mContext=context;
	
	}



	public void initUI(int imgRes,OnOrderClickCallback callback)
	{
		
		this.orderCallback=callback;
		btn = new LinearLayout(mContext);
		btn.setOnClickListener(this);
		img_left = new ImageView(mContext);
		img_left.setImageDrawable(getResources().getDrawable(imgRes));
		LayoutParams mParams = new LayoutParams(50, 50);
		img_left.setLayoutParams(mParams);
		
		img_right = new ImageView(mContext);
		img_right.setImageDrawable(getResources().getDrawable(dir_list[order_state]));
		LayoutParams p1 =new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		p1.setMargins(20, 0, 20, 0);
		img_right.setLayoutParams(p1);
		btn.addView(img_left);
		btn.addView(img_right);

		setGravity(Gravity.CENTER);
		addView(btn);
	}


	@Override
	public void onClick(View v)
	{
		if(v==btn)
		{
			order_state++;
			if(order_state>2)
			{
				order_state=1;
			}
			img_right.setImageDrawable(getResources().getDrawable(dir_list[order_state]));

			if(orderCallback!=null)
			{
				orderCallback.onOrderStateChanged(this,order_state);
				
			}
		}
		
	}
	
    public interface OnOrderClickCallback
    {
        public void onOrderStateChanged(OrderButton orderBtn,Integer orderState);
    }


	public OnOrderClickCallback getOrderCallback()
	{
		return orderCallback;
	}


	public void setOrderCallback(OnOrderClickCallback orderCallback)
	{
		this.orderCallback = orderCallback;
	}

    public void clearState()
    {
    	order_state=0;
    	img_right.setImageDrawable(getResources().getDrawable(dir_list[order_state]));
    }
}
