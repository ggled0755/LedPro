/**   
* @Title: ShowImgUtil.java 
* @Package cn.fuego.led.util.imgcrop 
* @Description: TODO
* @author Aether
* @date 2015-7-25 上午9:13:31 
* @version V1.0   
*/ 
package cn.fuego.led.util.imgcrop;

import java.io.File;
import java.util.Date;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.util.Log;
import android.widget.Toast;
import cn.fuego.led.R;

/** 
 * @ClassName: ShowImgUtil 
 * @Description: TODO
 * @author Aether
 * @date 2015-7-25 上午9:13:31 
 *  
 */
public class ShowImgUtil extends Activity
{

	private Context mContext;
	//private Activity mActivity;
	private int size_w=70,size_h=70;
	private static ShowImgUtil instance;
	
	public static final String IMAGE_PATH ="ImgCache";
	private static String localTempImageFileName = "";
	public static final int FLAG_CHOOSE_IMG = 5;
	public static final int FLAG_CHOOSE_PHONE = 6;
	public static final int FLAG_MODIFY_FINISH = 7;
	public static final File FILE_SDCARD = Environment
            .getExternalStorageDirectory();
	public static final File FILE_LOCAL = new File(FILE_SDCARD,IMAGE_PATH);
	public static final File FILE_PIC_SCREENSHOT = new File(FILE_LOCAL,
            "images/screenshots");	
	private String selectedTitle="";//区别某一项目
	private ImgSelectCallback imgCallbcak;
	private String locPath;
	
	public synchronized static ShowImgUtil getInstance()
	{
		if(null == instance)
		{
			instance = new ShowImgUtil();
		}
		return instance;
		
	}
	/**
	 * 回调函数。返回本地路径和网络路径
	* @ClassName: imgSelectCallback 
	* @Description: TODO
	* @author Aether
	* @date 2015-7-25 上午9:37:25 
	*
	 */
    public interface ImgSelectCallback
    {
        public void imgComplete(String title,String localPath,String urlPath);
    }

	/**
	 * 初始化，传入输出图片尺寸信息
	 * @param context
	 * @param width
	 * @param height
	 */
	public void init(Context context,int width,int height)
	{
		this.size_w=width;
		this.size_h=height;
		this.mContext=context;
		

	}
	public void show(String title,ImgSelectCallback callback)
	{
		this.selectedTitle = title;
		this.imgCallbcak = callback;
		
		ImgSelectDialog modifyAvatarDialog = new ImgSelectDialog(mContext)
		{
			//选择本地相册
			@Override
			public void doGoToImg() {
				this.dismiss();
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_PICK);
				intent.setType("image/*");
				ShowImgUtil.this.startActivityForResult(intent, FLAG_CHOOSE_IMG);
			}
			
			//选择相机拍照
			@Override
			public void doGoToPhone() {
				this.dismiss();
				String status = Environment.getExternalStorageState();
				if (status.equals(Environment.MEDIA_MOUNTED)) {
					try {
						localTempImageFileName = "";
						localTempImageFileName = String.valueOf((new Date())
								.getTime()) + ".png";
						File filePath = FILE_PIC_SCREENSHOT;
						if (!filePath.exists()) {
							filePath.mkdirs();
						}
						Intent intent = new Intent(
								android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
						File f = new File(filePath, localTempImageFileName);
						// localTempImgDir和localTempImageFileName是自己定义的名字
						Uri u = Uri.fromFile(f);
						intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
						intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
						ShowImgUtil.this.startActivityForResult(intent, FLAG_CHOOSE_PHONE);
					} catch (ActivityNotFoundException e) {
						Log.w(getClass().getSimpleName(), "Activity Not Found");
					}
				}
			}
		};
		AlignmentSpan span = new AlignmentSpan.Standard(
				Layout.Alignment.ALIGN_CENTER);
		AbsoluteSizeSpan span_size = new AbsoluteSizeSpan(25, true);
		SpannableStringBuilder spannable = new SpannableStringBuilder();
		String dTitle = mContext.getString(R.string.gl_choose_title);
		spannable.append(dTitle);
		spannable.setSpan(span, 0, dTitle.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannable.setSpan(span_size, 0, dTitle.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		modifyAvatarDialog.setTitle(spannable);
		modifyAvatarDialog.show();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == FLAG_CHOOSE_IMG && resultCode == RESULT_OK) 
		{
			if (data != null) {
				Uri uri = data.getData();
				if (!TextUtils.isEmpty(uri.getAuthority())) {
					Cursor cursor = getContentResolver().query(uri,
							new String[] { MediaStore.Images.Media.DATA },
							null, null, null);
					if (null == cursor) {
						Toast.makeText(this, "图片没找到", 0).show();
						return;
					}
					cursor.moveToFirst();
					String path = cursor.getString(cursor
							.getColumnIndex(MediaStore.Images.Media.DATA));
					cursor.close();
					Log.i("","path=" + path);
					locPath =path;
					Intent intent = new Intent(this, CropImageActivity.class);
					intent.putExtra("title", selectedTitle);
					intent.putExtra("path", path);
					intent.putExtra("width", this.size_w);
					intent.putExtra("height", this.size_h);
					startActivityForResult(intent, FLAG_MODIFY_FINISH);
				} else {
					Log.i("","path=" + uri.getPath());
					locPath=uri.getPath();
					Intent intent = new Intent(this, CropImageActivity.class);
					intent.putExtra("title", selectedTitle);
					intent.putExtra("path", uri.getPath());
					intent.putExtra("width", this.size_w);
					intent.putExtra("height", this.size_h);
					
					startActivityForResult(intent, FLAG_MODIFY_FINISH);
				}
			}
		} else if (requestCode == FLAG_CHOOSE_PHONE && resultCode == RESULT_OK) {
			File f = new File(FILE_PIC_SCREENSHOT,localTempImageFileName);
			locPath=f.getAbsolutePath();
			Intent intent = new Intent(this, CropImageActivity.class);
			intent.putExtra("title", selectedTitle);
			intent.putExtra("path", f.getAbsolutePath());
			intent.putExtra("width", this.size_w);
			intent.putExtra("height", this.size_h);			
			startActivityForResult(intent, FLAG_MODIFY_FINISH);
		}else if (requestCode == FLAG_MODIFY_FINISH && resultCode == RESULT_OK) {
			if (data != null) {
				final String path = data.getStringExtra("path");
				Log.i("", "截取到的图片路径是 = " + path);
				String title = data.getStringExtra("title");
				imgCallbcak.imgComplete(title,locPath, path);
			}
		}
	}
	
	
}
