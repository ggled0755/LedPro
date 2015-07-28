/**   
* @Title: ImageGalleryAdapter.java 
* @Package cn.fuego.mechef.ui.base 
* @Description: TODO
* @author Aether
* @date 2015-7-15 下午5:58:02 
* @version V1.0   
*/ 
package cn.fuego.led.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import cn.fuego.led.R;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/** 
 * @ClassName: ImageGalleryAdapter 
 * @Description: TODO
 * @author Aether
 * @date 2015-7-15 下午5:58:02 
 *  
 */
public class ImageGalleryAdapter extends BaseAdapter
{
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private String[] imageUrls;
	private DisplayImageOptions options;
	private Context mContext;

	public void init(Context context,String[] urls)
	{
		mContext = context;
	    imageUrls = urls;
	    options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.loading)
		.showImageForEmptyUri(R.drawable.loading_image)
		.showImageOnFail(R.drawable.load_image_failed)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();


	}
	
	@Override
	public int getCount()
	{
		return imageUrls.length;
	}

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ImageView imageView = (ImageView) convertView;
		if (imageView == null) 
		{
			LayoutInflater inflater = LayoutInflater.from(mContext);
			imageView = (ImageView)  inflater.inflate(
					R.layout.item_gallery_image, parent, false);
		}

		imageLoader.displayImage(imageUrls[position], imageView, options);
		return imageView;
	}

}
