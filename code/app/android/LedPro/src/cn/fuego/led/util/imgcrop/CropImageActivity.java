package cn.fuego.led.util.imgcrop;


import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import cn.fuego.common.util.validate.ValidatorUtil;
import cn.fuego.led.R;
import cn.fuego.led.webservice.up.rest.WebServiceContext;
import cn.fuego.misp.constant.MispErrorCode;
import cn.fuego.misp.service.MemoryCache;
import cn.fuego.misp.service.http.MispHttpHandler;
import cn.fuego.misp.service.http.MispHttpMessage;
import cn.fuego.misp.ui.base.MispBaseActivtiy;
import cn.fuego.misp.ui.common.upload.UploadUtil;
import cn.fuego.misp.ui.common.upload.UploadUtil.OnUploadProcessListener;
import cn.fuego.misp.webservice.json.MispBaseReqJson;
import cn.fuego.misp.webservice.json.MispBaseRspJson;


/**
 * 裁剪界面
 *
 */
public class CropImageActivity extends MispBaseActivtiy implements OnClickListener, OnUploadProcessListener{
	
	private CropImageView mImageView;
	private Bitmap mBitmap;
	
	private CropImage mCrop;
	
	private Button mSave;
	private Button mCancel,rotateLeft,rotateRight;
	private String mPath = "CropImageActivity";
	private String TAG = "CropImage";
	public int screenWidth = 0;
	public int screenHeight = 0;
	
	private ProgressBar mProgressBar;
	
	public static final int SHOW_PROGRESS = 2000;

	public static final int REMOVE_PROGRESS = 2001;
	
	//去上传文件
	protected static final int TO_UPLOAD_FILE = 1;  
	//上传文件响应
	protected static final int UPLOAD_FILE_DONE = 2;  
	//选择文件
	public static final int TO_SELECT_PHOTO = 3;
	//上传初始化
	private static final int UPLOAD_INIT_PROCESS = 4;
	//上传中
	private static final int UPLOAD_IN_PROCESS = 5;
	//返回后的图片地址
	private String saveImgUrl="";
	//区别某一项目
	private String mtitle="";
	//设置输出图片大小，例如头像和中型图片
	private int mWidth=70;
	private int mHeight=70;
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case SHOW_PROGRESS:
				mProgressBar.setVisibility(View.VISIBLE);
				break;
			case REMOVE_PROGRESS:
				mHandler.removeMessages(SHOW_PROGRESS);
				mProgressBar.setVisibility(View.INVISIBLE);
				break;
			}

		}
	};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gl_modify_avatar);
        
        init();
    }
    @Override
    protected void onStop(){
    	super.onStop();
    	if(mBitmap!=null){
    		mBitmap=null;
    	}
    }
    
    private void init()
    {
    	getWindowWH();
    	mPath = getIntent().getStringExtra("path");
    	mtitle = getIntent().getStringExtra("title");
    	
    	mWidth = getIntent().getIntExtra("width", 70);
    	mHeight = getIntent().getIntExtra("height", 70);
    	
    	Log.i(TAG, "得到的图片的路径是 = " + mPath);
        mImageView = (CropImageView) findViewById(R.id.gl_modify_avatar_image);
        mSave = (Button) this.findViewById(R.id.gl_modify_avatar_save);
        mCancel = (Button) this.findViewById(R.id.gl_modify_avatar_cancel);
        rotateLeft = (Button) this.findViewById(R.id.gl_modify_avatar_rotate_left);
        rotateRight = (Button) this.findViewById(R.id.gl_modify_avatar_rotate_right);
        mSave.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        rotateLeft.setOnClickListener(this);
        rotateRight.setOnClickListener(this);
        try{
            mBitmap = createBitmap(mPath,screenWidth,screenHeight);
            if(mBitmap==null){
            	Toast.makeText(CropImageActivity.this, "没有找到图片", 0).show();
    			finish();
            }else{
            	resetImageView(mBitmap);
            }
        }catch (Exception e) {
        	Toast.makeText(CropImageActivity.this, "没有找到图片", 0).show();
			finish();
		}
        addProgressbar();       
    }
    /**
     * 获取屏幕的高和宽
     */
    private void getWindowWH(){
		DisplayMetrics dm=new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth=dm.widthPixels;
		screenHeight=dm.heightPixels;
	}
    private void resetImageView(Bitmap b){
    	 mImageView.clear();
    	 mImageView.setImageBitmap(b);
         mImageView.setImageBitmapResetBase(b, true);
         mCrop = new CropImage(this, mImageView,mHandler,mWidth,mHeight);
         mCrop.crop(b);
    }
    
    public void onClick(View v)
    {
    	switch (v.getId())
    	{
    	case R.id.gl_modify_avatar_cancel:
//    		mCrop.cropCancel();
    		if(!ValidatorUtil.isEmpty(saveImgUrl))
    		{
    			
    			confirmCancel();
    		}
    		else finish();
    		
    		break;
    	case R.id.gl_modify_avatar_save:
    		String path = mCrop.saveToLocal(mCrop.cropAndSave());
    		Log.i(TAG, "截取后图片的路径是 = " + path);
    		//保存开始异步上传
    		uploadImg(path);	
    		break;
    	case R.id.gl_modify_avatar_rotate_left:
    		mCrop.startRotate(270.f);
    		break;
    	case R.id.gl_modify_avatar_rotate_right:
    		mCrop.startRotate(90.f);
    		break;
    		
    	}
    }
    private void confirmCancel()
	{
 		new AlertDialog.Builder(this)    
        .setTitle("确认提示").setMessage("是否取消本次操作?") 
                .setPositiveButton("确定", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						 
						deleteImg(saveImgUrl);
					} 
				})
                .setNegativeButton("取消", null)
                .setCancelable(false)
                .show();
		
	}
	//上传图片
	private void uploadImg(String path)
	{
		String fileKey = "upload";
		UploadUtil uploadUtil = UploadUtil.getInstance();;
		uploadUtil.setOnUploadProcessListener(this);   		

		Map<String, String> params = new HashMap<String, String>();
		params.put("orderId", "111");
		
		uploadUtil.uploadFile(path,fileKey,MemoryCache.getUploadUrl(),params);   	
		
	}
	//删除图片
    private void deleteImg(String url)
	{
		MispBaseReqJson req = new MispBaseReqJson();
		req.setObj(url);
		WebServiceContext.getInstance().getCustomerRest(new MispHttpHandler(){
			@Override
			public void handle(MispHttpMessage message)
			{
				if(message.isSuccess())
				{
					saveImgUrl="";
				}
				else
				{
					showMessage(message);
				}
				finish();
			}
		}).deleteFile(req);
		
	}
	protected void addProgressbar() {
		mProgressBar = new ProgressBar(this);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		addContentView(mProgressBar, params);
		mProgressBar.setVisibility(View.INVISIBLE);
	}
    
    public Bitmap createBitmap(String path,int w,int h){
    	try{
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			// 这里是整个方法的关键，inJustDecodeBounds设为true时将不为图片分配内存。
			BitmapFactory.decodeFile(path, opts);
			int srcWidth = opts.outWidth;// 获取图片的原始宽度
			int srcHeight = opts.outHeight;// 获取图片原始高度
			int destWidth = 0;
			int destHeight = 0;
			// 缩放的比例
			double ratio = 0.0;
			if (srcWidth < w || srcHeight < h) {
				ratio = 0.0;
				destWidth = srcWidth;
				destHeight = srcHeight;
			} else if (srcWidth > srcHeight) {// 按比例计算缩放后的图片大小，maxLength是长或宽允许的最大长度
				ratio = (double) srcWidth / w;
				destWidth = w;
				destHeight = (int) (srcHeight / ratio);
			} else {
				ratio = (double) srcHeight / h;
				destHeight = h;
				destWidth = (int) (srcWidth / ratio);
			}
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			// 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
			newOpts.inSampleSize = (int) ratio + 1;
			// inJustDecodeBounds设为false表示把图片读进内存中
			newOpts.inJustDecodeBounds = false;
			// 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
			newOpts.outHeight = destHeight;
			newOpts.outWidth = destWidth;
			// 获取缩放后图片
			return BitmapFactory.decodeFile(path, newOpts);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
    }
    private MispHttpHandler handler = new MispHttpHandler(){
		@Override
		public void handle(MispHttpMessage msg) {
			switch (msg.getMessage().what) {
		
			case UPLOAD_INIT_PROCESS:
			    //progressView.setMax(msg.getMessage().arg1);
				waitDailog.show();
				break;
			case UPLOAD_IN_PROCESS:
				//progressView.setProgress(msg.getMessage().arg1);

				break;
			case UPLOAD_FILE_DONE:
				msg.getMessage().what = MispErrorCode.SUCCESS;
				waitDailog.dismiss();
				showMessage(msg);
				if(msg.isSuccess())
				{
					MispBaseRspJson json = (MispBaseRspJson) msg.getMessage().obj;
					String imgUrl= json.GetReqCommonField(String.class);
					saveImgUrl= imgUrl;
			        Intent intent = new Intent();
		    		intent.putExtra("path", imgUrl);
		    		intent.putExtra("title", mtitle);
		    		setResult(RESULT_OK, intent);
					finish();
					
				}
 				break;
			default:
				break;
			}
 		}
		
	};

	@Override
	public void onUploadDone(MispBaseRspJson rsp)
	{
		MispHttpMessage msg = new MispHttpMessage();
		msg.getMessage().what = UPLOAD_FILE_DONE;  
 		msg.getMessage().obj = rsp;
		handler.sendMessage(msg);
		
	}
	@Override
	public void onUploadProcess(int uploadSize)
	{
		MispHttpMessage msg = new MispHttpMessage();
		msg.getMessage().what = UPLOAD_IN_PROCESS;
		msg.getMessage().arg1 = uploadSize;
		handler.sendMessage(msg);
		
	}
	@Override
	public void initUpload(int fileSize)
	{
		MispHttpMessage msg = new MispHttpMessage();
		msg.getMessage().what = UPLOAD_INIT_PROCESS;
		msg.getMessage().arg1 = fileSize;
	  
		handler.sendMessage(msg);
		
	}
	@Override
	public void initRes()
	{
		// TODO Auto-generated method stub
		
	}
   
}