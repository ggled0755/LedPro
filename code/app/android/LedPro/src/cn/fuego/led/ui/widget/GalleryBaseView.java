/**   
* @Title: GalleryBaseView.java 
* @Package cn.fuego.mechef.ui.widget 
* @Description: TODO
* @author Aether
* @date 2015-7-15 下午9:06:09 
* @version V1.0   
*/ 
package cn.fuego.led.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.LinearLayout;

/** 
 * @ClassName: GalleryBaseView 
 * @Description: TODO
 * @author Aether
 * @date 2015-7-15 下午9:06:09 
 *  
 */
@SuppressWarnings("deprecation")
public class GalleryBaseView extends LinearLayout 
{


    private Context mContext; 
    private Gallery mGallery; 
    private ImageGalleryAdapter galleryAdapter = new ImageGalleryAdapter();
    private ItemClickCallback mCallback;
    
	public GalleryBaseView(Context context)
	{
		super(context);
		mContext = context;
	}
	
	public GalleryBaseView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		mContext = context;
	}

	public GalleryBaseView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		mContext = context;
	}

	public void initUI(String[] imageUrls,ItemClickCallback callback)
	{
		this.mCallback = callback;
		
        mGallery = new Gallery(mContext, null); 
        LayoutParams params2 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, (float) 0.4);
        params2.setMargins(5, 0, 5, 0);
        mGallery.setLayoutParams(params2);
        mGallery.setSpacing(2);

        

        //适配器初始化
		galleryAdapter.init(mContext,imageUrls);
		mGallery.setAdapter(galleryAdapter);
		mGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mCallback.onItemClick(position);
			}
		});

        if(imageUrls.length>1)
        {
        	mGallery.setSelection(1);
        }
        setGravity(Gravity.CENTER);

        addView(mGallery);

	}


}
