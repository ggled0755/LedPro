package cn.fuego.misp.ui.common.upload;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import cn.fuego.misp.R;
import cn.fuego.misp.constant.MispCommonIDName;
import cn.fuego.misp.constant.MispErrorCode;
import cn.fuego.misp.service.MemoryCache;
import cn.fuego.misp.service.http.MispHttpHandler;
import cn.fuego.misp.service.http.MispHttpMessage;
import cn.fuego.misp.ui.base.MispBaseActivtiy;
import cn.fuego.misp.ui.common.upload.UploadUtil.OnUploadProcessListener;
import cn.fuego.misp.ui.util.ImgCompressUtil;
import cn.fuego.misp.ui.util.LoadImageUtil;
import cn.fuego.misp.webservice.json.MispBaseRspJson;

public class MispUploadImgActivity extends MispBaseActivtiy implements OnUploadProcessListener
{

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
	//请求服务器uri
	private static String requestURL = MemoryCache.getUploadUrl();
 	private ImageView imageView;
 
	private ProgressBar progressView;
	private TextView progress_txt;							//进度条显示
	private AlertDialog downloadDialog;    //下载弹出框  
	private int totalSize,processSize;
 	private String picPath = null;
	//private ProgressDialog progressDialog;
	private View submit_btn_area;
	
	@Override
	public void initRes()
	{
		this.activityRes.setAvtivityView(R.layout.misp_upload_img);
		this.activityRes.getButtonIDList().add(R.id.upload_img_camera);
		this.activityRes.getButtonIDList().add(R.id.upload_img_submitbtn);
		
	}
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

        imageView = (ImageView) findViewById(R.id.upload_imgview_area);
        submit_btn_area = findViewById(R.id.submit_btn_area);
        submit_btn_area.setVisibility(View.VISIBLE);
        progressView = (ProgressBar) findViewById(R.id.upload_img_progressBar);
        
        
       /// android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this); 
		//builder.setTitle("正在上传图片，请稍后……");
		//LayoutInflater inflater = LayoutInflater.from(this); 
		//View view = inflater.inflate(MispCommonIDName.layout_misp_download_file, null);
		//progress_txt = (TextView) view.findViewById(MispCommonIDName.misp_upgrade_count_txt);
		//progress_txt.setText("正在上传图片，请稍后……");
		//progressView = (ProgressBar) view.findViewById(MispCommonIDName.misp_uprade_progress_count);

		//builder.setView(view);
		//downloadDialog = builder.create();  
		//downloadDialog.setCanceledOnTouchOutside(false);
        
    }
    
	public static void jump(Activity activity, int requestCode)
	{
		Intent intent = new Intent(activity,MispUploadImgActivity.class);
		activity.startActivityForResult(intent, requestCode);
	}
	 
	public void onClick(View v) {
		
		 if(v.getId() ==  R.id.upload_img_submitbtn)
		 {
				this.waitDailog.show();
				if(picPath!=null)
				{		
					Toast.makeText(this, "正在处理图片", Toast.LENGTH_LONG).show();
					picPath=ImgCompressUtil.getInstance().copressImg(this, picPath, "SmartHome/compress/");
					
					if(picPath!=null)
					{
						toUploadFile();
					}

	 			}else{
					Toast.makeText(this, "图片不存在", Toast.LENGTH_LONG).show();
				}
		 }
		 else  if(v.getId() ==  R.id.upload_img_camera)
		 {
				Intent intent1 = new Intent(this,SelectPicActivity.class);
				startActivityForResult(intent1, TO_SELECT_PHOTO);
		 }
	 
	}
    private void closeDialog()
    {
    	if(null != downloadDialog)
    	{
    		downloadDialog.cancel();
    	}
		
		downloadDialog = null;
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{

		if(resultCode==Activity.RESULT_OK && requestCode == TO_SELECT_PHOTO)
		{
 			//imageView.setImageBitmap(null);
 			picPath = data.getStringExtra(SelectPicActivity.KEY_PHOTO_PATH);
 			LoadImageUtil.getInstance().loadImage(imageView, "file://"+picPath);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	 
	public void onUploadDone(MispBaseRspJson rsp)
	{
		//progressDialog.dismiss();
		MispHttpMessage msg = new MispHttpMessage();
		msg.getMessage().what = UPLOAD_FILE_DONE;  
 		msg.getMessage().obj = rsp;
		handler.sendMessage(msg);
	}
	
	private void toUploadFile()
	{
		String fileKey = "upload";
		UploadUtil uploadUtil = UploadUtil.getInstance();;
		uploadUtil.setOnUploadProcessListener(this);   		

		Map<String, String> params = new HashMap<String, String>();
		params.put("orderId", "111");
		uploadUtil.uploadFile( picPath,fileKey, requestURL,params);
	}

	private MispHttpHandler handler = new MispHttpHandler(){
		@Override
		public void handle(MispHttpMessage msg) {
			switch (msg.getMessage().what) {
		
			case UPLOAD_INIT_PROCESS:
				//progressBar.setMax(msg.getMessage().arg1);
				//downloadDialog.show(); 
				//totalSize=msg.getMessage().arg1;
				progressView.setMax(msg.getMessage().arg1);
				//progress_txt.setText(String.valueOf(totalSize));
				break;
			case UPLOAD_IN_PROCESS:
				progressView.setProgress(msg.getMessage().arg1);
				//processSize=msg.getMessage().arg1;
				//progress_txt.setText(String.valueOf(processSize));
/*
				new Thread(new Runnable()
				{
					
					@Override
					public void run()
					{
						progressView.setProgress(processSize);
						
					}
				});*/
				//progressView.setProgress((int) (((float)processSize/totalSize)*100));
				
				///progressCount = (int) (((float)processSize/totalSize)*100);
				//progress_txt.setText(processSize/totalSize);
				break;
			case UPLOAD_FILE_DONE:
				msg.getMessage().what = MispErrorCode.SUCCESS;
				waitDailog.dismiss();
				if(msg.isSuccess())
				{
					closeDialog();
					MispBaseRspJson json = (MispBaseRspJson) msg.getMessage().obj;
			        Intent intent = new Intent();
			        intent.putExtra(RETURN_DATA, json);
			        intent.putExtra("picPath", picPath);//本地图片地址
			        setResult(0, intent);

					finish();
					
				}
				showMessage(msg);
 				
 				break;
			default:
				break;
			}
 		}
		
	};

	 
	public void onUploadProcess(int uploadSize) 
	{
		MispHttpMessage msg = new MispHttpMessage();
		msg.getMessage().what = UPLOAD_IN_PROCESS;
		msg.getMessage().arg1 = uploadSize;
		handler.sendMessage(msg);
	}
	 
	public void initUpload(int fileSize) 
	{
		MispHttpMessage msg = new MispHttpMessage();
		msg.getMessage().what = UPLOAD_INIT_PROCESS;
		msg.getMessage().arg1 = fileSize;
	  
		handler.sendMessage(msg);
	}


}
