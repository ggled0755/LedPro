/**   
* @Title: CustomTextview.java 
* @Package cn.fuego.led.ui.widget 
* @Description: TODO
* @author Aether
* @date 2015-7-30 上午9:53:32 
* @version V1.0   
*/ 
package cn.fuego.led.ui.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/** 
 * @ClassName: CustomTextview 
 * @Description: TODO
 * @author Aether
 * @date 2015-7-30 上午9:53:32 
 *  
 */
public class CustomTextview extends TextView
{

	public CustomTextview(Context context)
	{
		super(context);
		// TODO Auto-generated constructor stub
	}
	public CustomTextview(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public CustomTextview(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void setTypeface(Typeface tf, int style)
	{
        switch (style)
		{
		case Typeface.NORMAL:
			
			break;

		default:
			break;
		}
		if (style == Typeface.BOLD) 
        {
            super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/YourCustomFont_Bold.ttf"));
        } 
        else
        {
           super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/YourCustomFont.ttf"));
        }
	}
	
	




/*	public void setTypeface(Typeface tf, int style) {
         if (style == Typeface.BOLD) {
              super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/YourCustomFont_Bold.ttf"));
          } else {
             super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/YourCustomFont.ttf"));
          }
    }*/
}
