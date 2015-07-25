package cn.fuego.led.ui.profile;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import cn.fuego.common.util.validate.ValidatorUtil;
import cn.fuego.led.R;
import cn.fuego.led.cache.AppCache;
import cn.fuego.led.constant.IntentCodeConst;
import cn.fuego.led.util.imgcrop.ImgSelectActivity;
import cn.fuego.led.webservice.up.model.base.CustomerJson;
import cn.fuego.led.webservice.up.rest.WebServiceContext;
import cn.fuego.misp.service.MemoryCache;
import cn.fuego.misp.service.http.MispHttpHandler;
import cn.fuego.misp.service.http.MispHttpMessage;
import cn.fuego.misp.ui.common.edit.MispEditParameter;
import cn.fuego.misp.ui.info.MispInfoListActivity;
import cn.fuego.misp.ui.model.CommonItemMeta;
import cn.fuego.misp.ui.util.StrUtil;
import cn.fuego.misp.webservice.json.MispBaseReqJson;

public class UserInfoActivity extends MispInfoListActivity 
{

	public static final Integer HEAD = R.string.info_user_head;
	public static final Integer REALNAME = R.string.info_user_realname;
	public static final Integer COMPANY_NAME = R.string.info_user_company_name;	
	public static final Integer EMAIL = R.string.info_user_email;
	public static final Integer CELLPHONE =R.string.info_user_cellphone;

	private CustomerJson customer;
	private String old_img;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public void initRes()
	{
		this.activityRes.setAvtivityView(R.layout.activity_user_info);		
		
		this.listViewRes.setListView(R.id.user_info_list);
 		this.activityRes.setName(getResources().getString(R.string.title_activity_user_info));
 		this.activityRes.setSaveBtnName(getResources().getString(R.string.misp_save));
		Intent intent= this.getIntent();
		customer = (CustomerJson) intent.getSerializableExtra(IntentCodeConst.DATA_CUSTOMER);
		//保存默认图片
		old_img=customer.getHead_img();
		
		this.getDataList().clear();
		this.getDataList().addAll(getBtnData());
		
	}
	public static void jump(Context context,CustomerJson customer)
	{
		Intent intent = new Intent(context,UserInfoActivity.class);
		intent.putExtra(IntentCodeConst.DATA_CUSTOMER, customer);
		((Activity) context).startActivityForResult(intent,IntentCodeConst.REQUEST_CODE);
	}
	
	@Override
	public void saveOnClick(View v)
	{
		modifyUser();
		
	}

	@Override
	public void backOnClick()
	{
		if(!old_img.equals(customer.getHead_img()))
		{
			deleteImg(customer.getHead_img());
		}
		else
		{
			finish();
		}
		
	}

	private void deleteImg(String head_img)
	{

		MispBaseReqJson req = new MispBaseReqJson();
		req.setObj(head_img);
		WebServiceContext.getInstance().getCustomerRest(new MispHttpHandler(){
			@Override
			public void handle(MispHttpMessage message)
			{
				if(message.isSuccess())
				{
					customer.setHead_img(old_img);
				}
				else
				{
					showMessage(message);
				}
				finish();
			}
		}).deleteFile(req);
	}

	private void modifyUser()
	{
		MispBaseReqJson req = new MispBaseReqJson();
		req.setObj(customer);
		WebServiceContext.getInstance().getCustomerRest(new MispHttpHandler(){
			@Override
			public void handle(MispHttpMessage message)
			{
				if(message.isSuccess())
				{
					AppCache.getInstance().update(customer);
					
					setResult(IntentCodeConst.RESULT_CODE);
					finish();
				}
				else
				{
					showMessage(message);
				}
			}
		}).modifyCustomer(req);
		
	}

	private List<CommonItemMeta> getBtnData()
	{
		List<CommonItemMeta> list = new ArrayList<CommonItemMeta>();

		list.add(new CommonItemMeta(CommonItemMeta.IMG_CONTENT, getResources().getString(HEAD), null,
				MemoryCache.getImageUrl()+customer.getHead_img()));		
		list.add(new CommonItemMeta(CommonItemMeta.BUTTON_TO_EDIT_ITEM,getResources().getString(REALNAME), 
				StrUtil.noNullStr(customer.getReal_name())));		
		list.add(new CommonItemMeta(CommonItemMeta.BUTTON_TO_EDIT_ITEM, getResources().getString(COMPANY_NAME), 
				StrUtil.noNullStr(customer.getCompany_name())));
		list.add(new CommonItemMeta(CommonItemMeta.BUTTON_TO_EDIT_ITEM, getResources().getString(EMAIL), 
				StrUtil.noNullStr(customer.getEmail())));		
		list.add(new CommonItemMeta(CommonItemMeta.TEXT_CONTENT, getResources().getString(CELLPHONE), 
				StrUtil.noNullStr(customer.getCellphone())));
		
		return list;
	}

	@Override
	public void onItemListClick(AdapterView<?> parent, View view, long id,CommonItemMeta item)
	{
		String title =   item.getTitle();
		MispEditParameter data = new MispEditParameter();
		data.setTilteName(title);
		data.setDataKey(title);
		if(getResources().getString(HEAD).equals(title))
		{

			ImgSelectActivity.jump(this, 70, 70, title);
			
		}
		if(getResources().getString(REALNAME).equals(title))
		{
			//data.setPointOut("长度不能大于20");
			data.setDataValue((String) item.getContent());
			MispTextEditActivity.jump(this, data, IntentCodeConst.REQUEST_CODE_EDIT_TEXT);
		}
		
		if(getResources().getString(COMPANY_NAME).equals(title))
		{
			data.setDataValue((String) item.getContent());
			MispTextEditActivity.jump(this, data, IntentCodeConst.REQUEST_CODE_EDIT_TEXT);
		}
		
		if(getResources().getString(EMAIL).equals(title))
		{
			data.setDataValue((String) item.getContent());
			MispTextEditActivity.jump(this, data, IntentCodeConst.REQUEST_CODE_EDIT_TEXT);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode ==IntentCodeConst.RESULT_CODE_EDIT_TEXT)
		{
			if(null != data)
			{
				MispEditParameter result_value = (MispEditParameter) data
						.getSerializableExtra(MispTextEditActivity.JUMP_DATA);

				if (null != result_value)
				{
					if(result_value.getDataKey().equals(getResources().getString(REALNAME)))
					{
						customer.setReal_name(result_value.getDataValue());
					}
					if(result_value.getDataKey().equals(getResources().getString(COMPANY_NAME)))
					{
						customer.setCompany_name(result_value.getDataValue());
					}
					if(result_value.getDataKey().equals(getResources().getString(EMAIL)))
					{
						customer.setEmail(result_value.getDataValue());
					}
				}
				else
				{
					String title =data.getStringExtra("title");
					String urlPath =data.getStringExtra("urlPath");
					String locPath =data.getStringExtra("locPath");
					if(!ValidatorUtil.isEmpty(title))
					{
						if(getResources().getString(HEAD).equals(title))
						{
							customer.setHead_img(urlPath);
						}
					}
				}

				this.refreshList(this.getBtnData());
			}
			

		}
	}

	
}
