/**   
* @Title: MispListViewAdapterScroll.java 
* @Package cn.fuego.misp.ui.base 
* @Description: TODO
* @author Aether
* @date 2015-3-21 下午12:27:10 
* @version V1.0   
*/ 
package cn.fuego.misp.ui.base;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/** 
 * @ClassName: MispListViewAdapterScroll 
 * @Description: TODO
 * @author Aether
 * @date 2015-3-21 下午12:27:10 
 *  
 */
public class MispListViewAdaptScroll extends ListView
{

    public MispListViewAdaptScroll(Context context) 
    {
        // TODO Auto-generated method stub
        super(context);
	}
	
	public MispListViewAdaptScroll(Context context, AttributeSet attrs) 
	{
	        // TODO Auto-generated method stub
	        super(context, attrs);
	}
	
	public MispListViewAdaptScroll(Context context, AttributeSet attrs, int defStyle)
	{
	        // TODO Auto-generated method stub
	        super(context, attrs, defStyle);
	}


	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
	    int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, 
	            MeasureSpec.AT_MOST); 
	    super.onMeasure(widthMeasureSpec, expandSpec);  
	}

}
