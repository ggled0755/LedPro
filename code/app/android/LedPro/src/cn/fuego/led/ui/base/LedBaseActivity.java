/**   
* @Title: LedBaseActivity.java 
* @Package cn.fuego.led.ui.base 
* @Description: TODO
* @author Aether
* @date 2015-7-7 下午8:51:55 
* @version V1.0   
*/ 
package cn.fuego.led.ui.base;


import android.graphics.Typeface;
import android.os.Bundle;
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
		
	}
	
	
}
