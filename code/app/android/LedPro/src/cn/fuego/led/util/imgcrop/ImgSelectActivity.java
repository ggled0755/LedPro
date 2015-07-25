package cn.fuego.led.util.imgcrop;

import java.io.File;
import java.util.Date;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;
import cn.fuego.led.R;
import cn.fuego.led.constant.IntentCodeConst;
import cn.fuego.led.ui.base.LedBaseActivity;

public class ImgSelectActivity extends LedBaseActivity
{
	private int size_w,size_h;
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
	private String locPath;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_img_select);
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);;
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.width=(int) (wm.getDefaultDisplay().getWidth()*0.8);
		params.height = LayoutParams.WRAP_CONTENT;
		params.gravity=Gravity.CENTER;
		getWindow().setAttributes(params);
	}

	@Override
	public void initRes()
	{
		this.activityRes.setAvtivityView(R.layout.activity_img_select);
		
		this.activityRes.getButtonIDList().add(R.id.gl_choose_img);
		this.activityRes.getButtonIDList().add(R.id.gl_choose_phone);
		this.activityRes.getButtonIDList().add(R.id.gl_choose_cancel);
		
		size_w = this.getIntent().getIntExtra("out_w", 70);
		size_h = this.getIntent().getIntExtra("out_h", 70);
		selectedTitle = this.getIntent().getStringExtra("sel_t");
		
	}
	public static void jump(Context context,int width,int height,String title)
	{
		Intent intent = new Intent(context,ImgSelectActivity.class);
		intent.putExtra("out_w", width);
		intent.putExtra("out_h", height);
		intent.putExtra("sel_t", title);
		((Activity) context).startActivityForResult(intent,IntentCodeConst.REQUEST_CODE);
	}
	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId())
		{
		case R.id.gl_choose_img:
			doGoToImg();
			break;
		case R.id.gl_choose_phone:
			doGoToPhone();
			break;
		case R.id.gl_choose_cancel:
			this.finish();
			break;
		default:
			break;
		}
	}
	
	private void doGoToImg()
	{
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, FLAG_CHOOSE_IMG);
		
	}
	
	private void doGoToPhone()
	{
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
				startActivityForResult(intent, FLAG_CHOOSE_PHONE);
			} catch (ActivityNotFoundException e) {
				Log.w(getClass().getSimpleName(), "Activity Not Found");
			}
		}
		
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
						Toast.makeText(this, "photo not found!", 0).show();
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

				Intent i = new Intent();
				i.putExtra("title", title);
				i.putExtra("urlPath", path);
				i.putExtra("locPath", locPath);
				setResult(IntentCodeConst.RESULT_CODE_EDIT_TEXT, i);
				this.finish();
			}
		}
	}


	
	
}
