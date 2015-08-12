/**   
* @Title: AdaptScrollExListview.java 
* @Package cn.fuego.led.ui.widget 
* @Description: TODO
* @author Aether
* @date 2015-8-7 下午8:12:25 
* @version V1.0   
*/ 
package cn.fuego.led.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/** 
 * @ClassName: AdaptScrollExListview 
 * @Description: TODO
 * @author Aether
 * @date 2015-8-7 下午8:12:25 
 *  
 */
public class AdaptScrollExListview extends ExpandableListView
{

	public AdaptScrollExListview(Context context, AttributeSet attrs,
			int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,  
        		  
        MeasureSpec.AT_MOST);  
  
        super.onMeasure(widthMeasureSpec, expandSpec);  
	}

	
}
