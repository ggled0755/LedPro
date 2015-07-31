package cn.fuego.led.ui.setting;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import cn.fuego.common.log.FuegoLog;
import cn.fuego.led.R;
import cn.fuego.led.ui.base.LedBaseActivity;
import cn.fuego.led.webservice.up.rest.WebServiceContext;
import cn.fuego.misp.constant.MispErrorCode;
import cn.fuego.misp.service.MemoryCache;
import cn.fuego.misp.service.http.MispHttpHandler;
import cn.fuego.misp.service.http.MispHttpMessage;
import cn.fuego.misp.ui.util.StrUtil;
import cn.fuego.misp.webservice.json.MispBaseReqJson;
import cn.fuego.misp.webservice.json.MispBaseRspJson;
import cn.fuego.misp.webservice.up.model.base.ClientVersionJson;

public class AboutActivity extends LedBaseActivity
{
	private FuegoLog log = FuegoLog.getLog(getClass());
	public static final String CLIENT_INFO = "client_info";
	
	private ClientVersionJson newVerInfo;
	private ProgressBar progressView;                       //进度条
	private TextView progress_txt;							//进度条显示
    private int progressCount = 0;                               //下载进度  
    private final int DOWNLOAD_ING = 1;                     //标记正在下载  
    private final int DOWNLOAD_OVER = 2;                    //标记下载完成 
    private final int DOWNLOAD_CANCEL = 3;                    //标记下载取消
    private final int DOWNLOAD_ERROR = 0;                   //标记下载失败
    private boolean interceptFlag = false;  //是否取消下载 
    private AlertDialog downloadDialog;    //下载弹出框  

    /* 下载保存路径 */  
    private String mSavePath; 
    private File apkFile;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		TextView txt_info = (TextView) findViewById(R.id.about_info);
		StringBuffer sb = new StringBuffer();
		sb.append(getString(R.string.app_name));
		sb.append("   V ");
		sb.append(MemoryCache.getVersionName());
		txt_info.setText(sb.toString());
	}

	@Override
	public void initRes()
	{
		this.activityRes.setAvtivityView(R.layout.activity_about);
		this.activityRes.setName(getString(R.string.title_setting_about));
		this.activityRes.getButtonIDList().add(R.id.about_update_btn);
		
	}

	@Override
	public void onClick(View v)
	{
		if(v.getId()==R.id.about_update_btn)
		{
			checkVersion();
		}
	}

	private void checkVersion()
	{
		MispBaseReqJson req = new MispBaseReqJson();
		WebServiceContext.getInstance().getMispSystemManageRest(new MispHttpHandler(){
			@Override
			public void handle(MispHttpMessage message)
			{
				if(message.isSuccess())
				{
					MispBaseRspJson rsp = (MispBaseRspJson) message.getMessage().obj;
					newVerInfo = rsp.GetReqCommonField(ClientVersionJson.class);
					if(null!=newVerInfo&&newVerInfo.getVersion_code()>MemoryCache.getVersionCode())
					{
						showMessage("start download");
						//doNewVersionUpdate(); // 更新新版本 
						downFile(MemoryCache.getAppDownloadUrl()+newVerInfo.getApk_url());
					}
					else
					{
						showMessage("yours is already latest");
						//notNewVersionShow(); // 提示当前为最新版本  
					}
				}
				else
				{
					showMessage(message);
				}
			}
		}).getAppVersion(req);
		
	}

	private void notNewVersionShow() {  
	    int verCode = MemoryCache.getVersionCode();
	    String verName = MemoryCache.getVersionName();
	    StringBuffer sb = new StringBuffer();  
	    sb.append("当前版本:");  
	    sb.append(verName);    	
	    sb.append(",\n已是最新版,无需更新!");  
	    Dialog dialog = new AlertDialog.Builder(this).setTitle("软件更新")  
	            .setMessage(sb.toString())// 设置内容  
	            .setPositiveButton("确定",// 设置确定按钮  
	                    new DialogInterface.OnClickListener() {  
	                        @Override  
	                        public void onClick(DialogInterface dialog,  
	                                int which) {  
	                            finish();  
	                        }  
	                    }).create();// 创建  
	    // 显示对话框  
	    dialog.show();  
	}  
	private void doNewVersionUpdate() 
	{
	    int verCode = MemoryCache.getVersionCode();
	    String verName = MemoryCache.getVersionName();
	    StringBuffer sb = new StringBuffer();
	    sb.append("当前版本:");
	    sb.append(verName);

	    sb.append("\n发现新版本");
	    sb.append(newVerInfo.getVersion_name());
	    sb.append("\n");
	    sb.append(StrUtil.noNullStr(newVerInfo.getVersion_name()));
	    Dialog dialog = new AlertDialog.Builder(this)
	            .setTitle("软件更新")
	            .setMessage(sb.toString())
	            // 设置内容
	            .setPositiveButton("更新",// 设置确定按钮
	                    new DialogInterface.OnClickListener() { 
	 
	                        @Override
	                        public void onClick(DialogInterface dialog,
	                                int which) {

	                            downFile(MemoryCache.getWebContextUrl()+newVerInfo.getApk_url());
	                        } 
	 
	                    })
	            .setNegativeButton("暂不更新",
	                    new DialogInterface.OnClickListener() { 
	                        public void onClick(DialogInterface dialog,
	                                int whichButton) {
	                            // 点击"取消"按钮之后退出程序
	                        	dialog.dismiss();
	                        }
	                    }).create();//创建
	    // 显示对话框        
	    dialog.show();
	}
	//更新UI的handler  
	private Handler mhandler = new Handler() 
	{                          
		  
        @Override  
        public void handleMessage(Message msg) {  
            super.handleMessage(msg); 
           
            switch (msg.what) {  
            case DOWNLOAD_ING:  
                // 更新进度条  
            	progressView.setProgress(progressCount);  
            	progress_txt.setText("下载进度："+String.valueOf(progressCount)+"%");
                break;  
            case DOWNLOAD_OVER:    //安装   
            	closeDialog();
                startInstall();           
                break; 
            case DOWNLOAD_ERROR:
            	closeDialog();

            	showMessage(MispErrorCode.ERROR_UPDATE_VERSION_FAILED);
            	break;
            case DOWNLOAD_CANCEL:
            	closeDialog();

             	break;
            default:  
                break;  
            }  
            
        }  
  
    } ;
    private void closeDialog()
    {
    	if(null != downloadDialog)
    	{
    		downloadDialog.cancel();
    	}
		
		downloadDialog = null;
    }
    
	public void downFile(final String path)
	{
		android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this); 
		builder.setTitle("Downloading……");
		LayoutInflater inflater = LayoutInflater.from(this); 
		View view = inflater.inflate(R.layout.upgrade_apk, null);
		progress_txt = (TextView) view.findViewById(R.id.upgrade_count_txt);
		progress_txt.setText("Progress：0%");
		progressView = (ProgressBar) view.findViewById(R.id.uprade_progress_count);
		builder.setView(view);
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
                dialog.dismiss();  
                interceptFlag = true; 
				
			}
		});
		 downloadDialog = builder.create();  
		 downloadDialog.setCanceledOnTouchOutside(false);
		 downloadDialog.setCancelable(false);
	     downloadDialog.show(); 

		new Thread()
		{
			public void run()
			{
				
                // 创建连接
             
				try
				{
					
				     //判断SD卡是否存在
				     if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
				     {
				    	 String sdpath = Environment.getExternalStorageDirectory() + "/";
				    	// mSavePath = sdpath + getResources().getString(R.string.app_name);//创建文件名 
				    	 mSavePath = sdpath ;
					     URL url = new URL(path);
					     HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					     conn.connect();
				    	 File file = new File(mSavePath);  
			             // 判断文件目录是否存在  
			             if (!file.exists())  
			             {  
			                 file.mkdir();  
			             } 
			            apkFile = new File(mSavePath, newVerInfo.getApk_name());  
		                FileOutputStream fileOutputStream = new FileOutputStream(apkFile); 
		                

						long length = conn.getContentLength();
						InputStream is = conn.getInputStream();
/*						FileOutputStream fileOutputStream = null;
						File file = new File(
								Environment.getExternalStorageDirectory(),
								"/"+newVerInfo.getApkName());
						fileOutputStream = new FileOutputStream(file);*/
						if (is != null)
						{
							byte[] buf = new byte[1024];
							int ch = 0;
							int count = 0;
							while (((ch = is.read(buf)) != -1))
							{
								if(interceptFlag)
								{
									mhandler.sendEmptyMessage(DOWNLOAD_CANCEL);
									interceptFlag=false;
									return;

								}

								count += ch;
								progressCount = (int) (((float)count/length)*100);
								mhandler.sendEmptyMessage(DOWNLOAD_ING); 

								fileOutputStream.write(buf, 0, ch);
							}
							

						}
						fileOutputStream.flush();
						if (fileOutputStream != null)
						{
							fileOutputStream.close();
						}
						mhandler.sendEmptyMessage(DOWNLOAD_OVER);
					     
				     }
				     else
				     {
				    	 log.info("SD not existed!");
				    	 Toast.makeText(AboutActivity.this, "SD card not existed!", Toast.LENGTH_LONG).show();
				    	 
				     }

			 

				} catch (Exception e)
				{
					log.error("update failed",e);
					mhandler.sendEmptyMessage(DOWNLOAD_ERROR);
				}  
			}

		}.start();

	}

	void startInstall()
	{
		mhandler.post(new Runnable()
		{
			public void run()
			{
				install();
			}
		});

	}

	void install()
	{
  
		File apkfile = new File(mSavePath, newVerInfo.getApk_name());  
        if (!apkfile.exists())  
        {  
            return;  
        } 
        log.info("打开apk路径"+Uri.parse("file://" + apkfile.toString()));
        Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive"); 
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
		this.startActivity(intent);
		
	}


	public static boolean isNewVision(ClientVersionJson version)
	{
        int vercode = MemoryCache.getVersionCode();
        if (null != version && version.getVersion_code() > vercode) 
        {  
        	return true;
        }
        return false;
	}


//	//本页面屏蔽返回
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event)
//	{
//		if(keyCode == KeyEvent.KEYCODE_BACK)
//		{      
//			return  true;
//		}  
//		return super.onKeyDown(keyCode, event);
//	}
}
