package cn.fuego.led.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;
import cn.fuego.common.util.validate.ValidatorUtil;
import cn.fuego.led.R;
import cn.fuego.led.cache.AppCache;
import cn.fuego.led.ui.base.LedBaseActivity;
import cn.fuego.led.ui.profile.UserRegisterActivity;
import cn.fuego.led.webservice.up.model.base.CustomerJson;
import cn.fuego.led.webservice.up.rest.WebServiceContext;
import cn.fuego.misp.constant.MispErrorCode;
import cn.fuego.misp.service.MemoryCache;
import cn.fuego.misp.service.http.MispHttpHandler;
import cn.fuego.misp.service.http.MispHttpMessage;
import cn.fuego.misp.ui.util.StrUtil;
import cn.fuego.misp.webservice.json.MispBaseReqJson;
import cn.fuego.misp.webservice.json.MispBaseRspJson;
import cn.fuego.misp.webservice.up.model.LoginRsp;
import cn.fuego.misp.webservice.up.model.base.UserJson;

public class LoginActivity extends LedBaseActivity
{

    private EditText textName,textPwd;
    private ProgressDialog proDialog;
	public static String JUMP_SOURCE = "jump_source";
	private Class clazz;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		textName = (EditText) findViewById(R.id.login_username);
		textPwd =(EditText) findViewById(R.id.login_pwd);
	}



	@Override
	public void initRes()
	{
		this.activityRes.setAvtivityView(R.layout.activity_login);

		clazz = (Class) this.getIntent().getSerializableExtra(JUMP_SOURCE);
		this.activityRes.getButtonIDList().add(R.id.login_btn);
		this.activityRes.getButtonIDList().add(R.id.login_find_pwd);
		this.activityRes.getButtonIDList().add(R.id.login_register);
		
	}
	
	public static void jump(Context context,Class clazz,int code)
	{
		AppCache.getInstance().clear();
		Intent intent = new Intent(context,LoginActivity.class);
		intent.putExtra(LoginActivity.JUMP_SOURCE, clazz);
		((Activity) context).startActivityForResult(intent,code);
	}
	public static void jump(Fragment fragment,int code)
	{
		AppCache.getInstance().clear();

 		Intent intent = new Intent();
 		intent.setClass(fragment.getActivity(), LoginActivity.class);
 		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
 		fragment.startActivityForResult(intent,code);
	}

	public static void jump(Activity activity,int code)
	{
		AppCache.getInstance().clear();

 		Intent intent = new Intent();
 		intent.setClass(activity, LoginActivity.class);
  		activity.startActivityForResult(intent,code);

  	}


	@Override
	public void onClick(View v)
	{
		
		switch(v.getId())
		{
			case R.id.login_btn:
				String userName = textName.getText().toString().trim();
				String password = textPwd.getText().toString().trim();
				checkLogin(userName,password);
		
				break;
			case R.id.login_find_pwd:
/*				Intent i = new Intent();
				i.putExtra(UserRegisterActivity.OPERTATE_NAME, UserRegisterActivity.FIND_PWD);
				i.setClass(this, UserRegisterActivity.class);
		        this.startActivity(i);*/
				break;
			case R.id.login_register:
				Intent i = new Intent();
				i.putExtra(UserRegisterActivity.OPERTATE_NAME, UserRegisterActivity.REGISTER);
				i.setClass(this, UserRegisterActivity.class);
		        this.startActivity(i);
		        break;
		        
		}
		
	}

 
	private void checkLogin(String userName,String password)
	{

		if(!ValidatorUtil.isEmpty(userName)&&!ValidatorUtil.isEmpty(password))
		{
			proDialog =ProgressDialog.show(this, null, "Check Login……");
			MispBaseReqJson req = new MispBaseReqJson();
			UserJson json= new UserJson();
			json.setUser_name(userName);
			json.setPassword(StrUtil.MD5(password));
			req.setObj(json);
	   
			WebServiceContext.getInstance().getMispUserManageRest(new MispHttpHandler(){
				@Override
				public void handle(MispHttpMessage message)
				{
					proDialog.dismiss();
					if(message.isSuccess())
					{
						LoginRsp rsp = (LoginRsp) message.getMessage().obj;
						if(!ValidatorUtil.isEmpty(rsp.getToken())&& null != rsp.getObj())
						{
							MemoryCache.setToken(rsp.getToken());
							loadCustomer(rsp.GetReqCommonField(UserJson.class));
						}
						else
						{
							showMessage(MispErrorCode.ERROR_LOGIN_FAILED);
						}
					}
					else
					{
						showMessage(message);
					}
				}
			}).login(req);
		}
		else
		{
			showMessage(getResources().getString(R.string.msg_input_null));
		}

	}



	private void loadCustomer(final UserJson user)
	{

		MispBaseReqJson req = new MispBaseReqJson();
		req.setObj(user.getUser_id());
		WebServiceContext.getInstance().getCustomerRest(new MispHttpHandler(){
			@Override
			public void handle(MispHttpMessage message)
			{
				if(message.isSuccess())
				{
					MispBaseRspJson rsp = (MispBaseRspJson) message.getMessage().obj;
					CustomerJson customer = rsp.GetReqCommonField(CustomerJson.class);

					loginSuccess(MemoryCache.getToken(),user,customer);
					
					finish();
					
				}
				else
				{
					showMessage(message);
				}
			}
		}).getCustomer(req);

	}
	
	private void loginSuccess(String token,UserJson user,CustomerJson customer)
	{
 		AppCache.getInstance().update(token,user, customer);
 		jumpToSource();
		this.finish();
	}

	private void jumpToSource()
	{
		
		if(null != clazz)
		{

			if(!Fragment.class.isAssignableFrom(clazz))
			{
				jumpToActivity(LoginActivity.this, clazz);
			}

 
		}
		

	}
}
