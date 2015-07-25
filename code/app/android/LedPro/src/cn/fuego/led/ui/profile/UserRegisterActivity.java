package cn.fuego.led.ui.profile;

import static cn.smssdk.framework.utils.R.getStringRes;

import java.util.Timer;
import java.util.TimerTask;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.fuego.common.util.validate.ValidatorUtil;
import cn.fuego.led.R;
import cn.fuego.led.cache.AppCache;
import cn.fuego.led.ui.base.LedBaseActivity;
import cn.fuego.led.webservice.up.model.base.CustomerJson;
import cn.fuego.led.webservice.up.rest.WebServiceContext;
import cn.fuego.misp.constant.MispErrorCode;
import cn.fuego.misp.service.http.MispHttpHandler;
import cn.fuego.misp.service.http.MispHttpMessage;
import cn.fuego.misp.ui.util.StrUtil;
import cn.fuego.misp.webservice.json.MispBaseReqJson;
import cn.fuego.misp.webservice.json.MispBaseRspJson;
import cn.fuego.misp.webservice.up.model.UserRegisterReq;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
public class UserRegisterActivity extends LedBaseActivity
{
	private String verifyCode;// 验证码
	private Timer timer;// 计时器
	private Button verifyCodeBtn;
	private TextView verifyCodeView;
	private TextView phoneNumView;
	private TextView passwordView;
	
	public static final int SEND_CYCLE_TIME = 60;

	private int validTime = SEND_CYCLE_TIME;
	
	public static final String OPERTATE_NAME = "operate";
	public static final int REGISTER = 0;
	public static final int FIND_PWD = 1;
	private  int operate = 0;
	
	private long lastCodeTime = 0;
	private String sendPhoneNum;

	private ProgressDialog pd;
	private EditText txt_country;
	@Override
	public void initRes()
	{
		operate = this.getIntent().getIntExtra(OPERTATE_NAME, 0);
		if(operate == REGISTER)
		{
			this.activityRes.setName(getResources().getString(R.string.title_activity_register));
		}
		else
		{
			this.activityRes.setName(getResources().getString(R.string.title_activity_find_pwd));
		}
		this.activityRes.setAvtivityView(R.layout.activity_user_register);
		this.activityRes.getButtonIDList().add(R.id.user_register_btn_verifyCode);
		this.activityRes.getButtonIDList().add(R.id.user_register_btn_submit);
		
		this.activityRes.getButtonIDList().add(R.id.user_register_country);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
 
		verifyCodeBtn = (Button) findViewById(R.id.user_register_btn_verifyCode);
		phoneNumView = (TextView) findViewById(R.id.user_register_txt_phoneNum);
		//默认聚焦
		phoneNumView.requestFocus();
		phoneNumView.requestFocusFromTouch();
		verifyCodeView = (TextView) findViewById(R.id.user_register_txt_verify_code);
		passwordView = (TextView) findViewById(R.id.user_register_txt_password);
		
		txt_country =(EditText) findViewById(R.id.user_register_country);
		txt_country.setText("86");
		//Mob SMS
		EventHandler eh=new EventHandler(){

			@Override
			public void afterEvent(int event, int result, Object data) {
				
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				mobHandler.sendMessage(msg);
			}
			
		};
		SMSSDK.registerEventHandler(eh);
 
	}


	@Override
	public void onClick(View v)
	{
		int id = v.getId();
		switch (id)
		{
		case R.id.user_register_country:
			
			break;
		case R.id.user_register_btn_verifyCode:
			getVerifyCode();
			break;
		case R.id.user_register_btn_submit:
			registerUser();
			break;
		default:
			break;

		}

	}

	private void registerUser()
	{
		String countryCode = txt_country.getText().toString().trim();
		if(ValidatorUtil.isEmpty(countryCode))
		{
			showMessage("Please enter country code");
			return;
		}
		String phoneNum = phoneNumView.getText().toString().trim();
		if(!ValidatorUtil.isMobile(phoneNum))
		{
			this.showMessage(MispErrorCode.ERROR_PHONE_INVALID);
			return;
		}

		String code = verifyCodeView.getText().toString().trim();
		if(!isVerifyCodeValid(code))
		{
			//this.showMessage(MispErrorCode.ERROR_VERIFY_CODE_INVALID);
			showMessage("Please enter ths verify code");
			return;
		}
		
		if(!phoneNum.equals(this.sendPhoneNum))
		{
			
			showMessage("Please enter ths same phone num");
			return;
		}
		
		String password = passwordView.getText().toString().trim();
		if(ValidatorUtil.isEmpty(password))
		{
			this.showMessage(MispErrorCode.ERROR_PASSWORD_IS_EMPTY);
			return;
		}
		if(password.length()<6 || password.length()>20)
		{
			this.showMessage("请输入6-20位新密码");
			return;
		}
		MispBaseReqJson req = new MispBaseReqJson();
		UserRegisterReq regInfo = new UserRegisterReq();
		regInfo.setUser_name(phoneNum);
		regInfo.setPassword(StrUtil.MD5(password));
		regInfo.setVerify_code(code);
		regInfo.setCountry_code(countryCode);
		req.setObj(regInfo);
		pd = ProgressDialog.show(this, null, getResources().getString(R.string.progress_msg_processing));
		if(REGISTER == operate)
		{
			WebServiceContext.getInstance().getMispUserManageRest(new MispHttpHandler(){
				@Override
				public void handle(MispHttpMessage message)
				{
					if(pd!=null&&pd.isShowing())
					{
						pd.dismiss();
					}
					if(message.isSuccess())
					{
						MispBaseRspJson rsp = (MispBaseRspJson) message.getMessage().obj;
						
						CustomerJson customer= rsp.GetReqCommonField(CustomerJson.class);
						AppCache.getInstance().update(customer);
						finish();
			
					}
					else
					{
						showMessage(message);
					}
				}
			}).register(req);
		}
		else
		{
			WebServiceContext.getInstance().getMispUserManageRest(new MispHttpHandler(){
				@Override
				public void handle(MispHttpMessage message)
				{
					if(pd!=null&&pd.isShowing())
					{
						pd.dismiss();
					}
					if(message.isSuccess())
					{
						finish();
					}
					else
					{
						showMessage(message);
					}
				}
			}).resetPassword(req);
		}
		
	}

	private void getVerifyCode()
	{
		String countryCode = txt_country.getText().toString().trim();
		if(ValidatorUtil.isEmpty(countryCode))
		{
			showMessage("Please enter country code");
			return;
		}
		String phoneNum = phoneNumView.getText().toString().trim();
		
		if(!ValidatorUtil.isMobile(phoneNum))
		{
			this.showMessage(MispErrorCode.ERROR_PHONE_INVALID);
			return;
		}
		sendPhoneNum=phoneNum;
		pd = ProgressDialog.show(this, null, getResources().getString(R.string.progress_msg_processing));
		//Mob get code
		SMSSDK.getVerificationCode(countryCode,phoneNum);
		//开启定时器
		lastCodeTime = System.currentTimeMillis();
		startVerifyTimer();	

	}
	
	private void startVerifyTimer()
	{
		validTime = SEND_CYCLE_TIME;
		verifyCodeBtn.setEnabled(false);
		timer = new Timer();
		timer.schedule(new TimerTask()
		{

			@Override
			public void run()
			{
				handler.sendEmptyMessage(validTime--);
			}
		}, 0, 1000);
	}

	private boolean isVerifyCodeValid(String code)
	{
		if(ValidatorUtil.isEmpty(code))
		{
			return false;
			
		}
/*		if(!code.equals(this.verifyCode))
		{
			return true;
		}
		long nowTime = System.currentTimeMillis();
		if((nowTime - lastCodeTime)>10*60*1000)
		{
			return true;
		}*/
		return true;
	}
	private Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			if (msg.what == 0)
			{
				verifyCodeBtn.setEnabled(true);
				verifyCodeBtn.setText(getResources().getString(R.string.btn_get_code));
				//verifyCode = null;
				timer.cancel();
			} else
			{
 				verifyCodeBtn.setText(msg.what+"s");
			}
		};
	};

	@Override
	public void onDestroy()
	{
		if (timer != null)
			timer.cancel();
		SMSSDK.unregisterAllEventHandler();
		super.onDestroy();
		
	}
	//Mob handler
	Handler mobHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int event = msg.arg1;
			int result = msg.arg2;
			Object data = msg.obj;
			Log.e("event", "event="+event);
			if (result == SMSSDK.RESULT_COMPLETE) {
				//短信注册成功后，返回MainActivity,然后提示新好友
				if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
					//Toast.makeText(getApplicationContext(), "提交验证码成功", Toast.LENGTH_SHORT).show();

				} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){//验证码已经发送
					if(pd!=null&&pd.isShowing())
					{
						pd.dismiss();
					}
					showMessage(getResources().getString(R.string.msg_send_code_success));
				
					
				}else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){//返回支持发送验证码的国家列表
					//Toast.makeText(getApplicationContext(), "获取国家列表成功", Toast.LENGTH_SHORT).show();
					//countryTextView.setText(data.toString());
					
				}
			} else {
				((Throwable) data).printStackTrace();
				int resId = getStringRes(UserRegisterActivity.this, "smssdk_network_error");
				Toast.makeText(UserRegisterActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
				if (resId > 0) {
					Toast.makeText(UserRegisterActivity.this, resId, Toast.LENGTH_SHORT).show();
				}
			}
			
		}
		
	};

}
