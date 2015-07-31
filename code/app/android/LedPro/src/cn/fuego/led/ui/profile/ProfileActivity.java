package cn.fuego.led.ui.profile;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.type.TypeReference;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import cn.fuego.common.contanst.ConditionTypeEnum;
import cn.fuego.common.dao.QueryCondition;
import cn.fuego.common.util.format.DateUtil;
import cn.fuego.common.util.validate.ValidatorUtil;
import cn.fuego.led.R;
import cn.fuego.led.cache.AppCache;
import cn.fuego.led.cache.ProjectCache;
import cn.fuego.led.constant.IntentCodeConst;
import cn.fuego.led.ui.base.CreateDialog;
import cn.fuego.led.ui.base.CreateDialog.OnConfirmListener;
import cn.fuego.led.ui.base.LedBaseListActivity;
import cn.fuego.led.ui.project.ProjectDetailActivity;
import cn.fuego.led.ui.widget.OrderButton;
import cn.fuego.led.webservice.up.model.base.ProjectJson;
import cn.fuego.led.webservice.up.rest.WebServiceContext;
import cn.fuego.misp.service.MemoryCache;
import cn.fuego.misp.service.http.MispHttpHandler;
import cn.fuego.misp.service.http.MispHttpMessage;
import cn.fuego.misp.ui.util.LoadImageUtil;
import cn.fuego.misp.ui.widget.RoundImageView;
import cn.fuego.misp.webservice.json.MispBaseReqJson;
import cn.fuego.misp.webservice.json.MispBaseRspJson;

public class ProfileActivity extends LedBaseListActivity<ProjectJson> implements OnEditorActionListener, TextWatcher, OnItemLongClickListener
{

	private TextView txt_name;
	private TextView txt_company;

	private RoundImageView round_head;
	private View default_head;
	private List<QueryCondition> conditionList = new ArrayList<QueryCondition>();
	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		txt_name = (TextView) findViewById(R.id.profile_username);	
		txt_company = (TextView) findViewById(R.id.profile_company_name);
		default_head = findViewById(R.id.profile_head_to_edit);
		round_head = (RoundImageView) findViewById(R.id.profile_head);		

		initUserInfo();
		search_input.setOnEditorActionListener(this);
		search_input.addTextChangedListener(this);
		
		ListView main_list = (ListView) findViewById(R.id.profile_list);
		main_list.setOnItemLongClickListener(this);
	}
	
	private void initUserInfo()
	{
		if(ValidatorUtil.isEmpty(AppCache.getInstance().getCustomer().getHead_img()))
		{
			round_head.setVisibility(View.GONE);
			default_head.setVisibility(View.VISIBLE);
		}
		else
		{
			LoadImageUtil.getInstance().loadImage(round_head, MemoryCache.getImageUrl()+AppCache.getInstance().getCustomer().getHead_img());
		}
		txt_name.setText(AppCache.getInstance().getCustomer().getReal_name());
		txt_name.setTypeface(ttf_cabin_semibold);
		txt_company.setText(AppCache.getInstance().getCustomer().getCompany_name());
		txt_name.setTypeface(ttf_cabin_regular);
	}
	@Override
	public void initRes()
	{
		this.activityRes.setAvtivityView(R.layout.activity_profile);
		this.activityRes.getButtonIDList().add(R.id.profile_login_view);
		this.activityRes.getButtonIDList().add(R.id.profile_add_project_btn);
		this.listViewRes.setListView(R.id.profile_list);
		this.listViewRes.setListItemView(R.layout.list_item_profile_project);
		this.listViewRes.setClickActivityClass(ProjectDetailActivity.class);
		
		this.setAdapterForScrollView();
	}
	
	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		super.onClick(v);
		if(v.getId()==R.id.profile_login_view)
		{
			UserInfoActivity.jump(this, AppCache.getInstance().getCustomer());
		}
		if(v.getId()==R.id.profile_add_project_btn)
		{
			CreateDialog dialog= new CreateDialog(this, "Create a new project", "Please enter your project name here", new OnConfirmListener()
			{
				
				@Override
				public void confirmCallback(String name)
				{
					createProject(name);
					
				}
			});
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			
			dialog.show();
		}
	}
	private void createProject(String name)
	{
		MispBaseReqJson req =new MispBaseReqJson();
		ProjectJson json = new ProjectJson();
		json.setProject_name(name);
		json.setCreate_user_id(AppCache.getInstance().getUser().getUser_id());
		json.setCreate_time(DateUtil.getCurrentDateTimeStr());
		req.setObj(json);
		WebServiceContext.getInstance().getProjectRest(new MispHttpHandler(){
			@Override
			public void handle(MispHttpMessage message)
			{
				if(message.isSuccess())
				{
					loadSendList();
				}
				else
				{
					showMessage(message);
				}
			}
		}).createProject(req);
		
	}

	public static void jump(Context context)
	{
		Intent intent = new Intent(context,ProfileActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent);
	
	}
	
	@Override
	public View getListItemView(View view, ProjectJson item)
	{
		ImageView img_product = (ImageView) view.findViewById(R.id.item_project_img);
		//使用测试页面

		LoadImageUtil.getInstance().loadImage(img_product, R.drawable.project_1);
		//button 需要实现
		Button btn_email = (Button) view.findViewById(R.id.item_project_email_btn);
		Button btn_edit = (Button) view.findViewById(R.id.item_project_edit_btn);
		
		TextView txt_name = (TextView) view.findViewById(R.id.item_project_name);
		txt_name.setText(item.getProject_name());
		txt_name.setTypeface(ttf_cabin_semibold);
		
		TextView txt_notes = (TextView) view.findViewById(R.id.item_project_notes);
		txt_notes.setText(item.getProject_note());
		txt_notes.setTypeface(ttf_cabin_regular);
		
		TextView txt_num = (TextView) view.findViewById(R.id.item_project_product_num);
		StringBuffer sb1= new StringBuffer();
		sb1.append(item.getTotal_catg());
		sb1.append("/");
		sb1.append(item.getTotal_num());
		sb1.append("  ");
		sb1.append("Products");
		
		txt_num.setText(sb1.toString());
		txt_num.setTypeface(ttf_cabin_regular);
		
		TextView txt_watt = (TextView) view.findViewById(R.id.item_project_watt);
		txt_watt.setText(item.getTotal_watt()+"  Watt/Month");
		txt_watt.setTypeface(ttf_cabin_regular);
		
		TextView txt_tco = (TextView) view.findViewById(R.id.item_project_tco);
		txt_tco.setText("$"+item.getTotal_cost()+"  TCO");
		txt_tco.setTypeface(ttf_cabin_regular);
		
		return view;
	}

	@Override
	public void loadSendList()
	{
		pd = ProgressDialog.show(this, null, getString(R.string.progress_msg_loading));
		pd.setCancelable(true);
		MispBaseReqJson req = new MispBaseReqJson();

		conditionList.add(new QueryCondition(ConditionTypeEnum.EQUAL, "create_user_id", AppCache.getInstance().getUser().getUser_id()));
		req.setConditionList(conditionList);
		
		WebServiceContext.getInstance().getProjectRest(this).loadAll(req);
		
	}

	@Override
	public List<ProjectJson> loadListRecv(Object obj)
	{
		pd.dismiss();
		MispBaseRspJson rsp = (MispBaseRspJson) obj;
		List<ProjectJson> result =rsp.GetReqCommonField(new TypeReference<List<ProjectJson>>(){});
		ProjectCache.getInstance().setProjectList(result);
		
		return result;
	}
	@Override
	public void showMessage(MispHttpMessage message)
	{
		// TODO Auto-generated method stub
		super.showMessage(message);
		if(!message.isNetSuccess())
		{
			pd.dismiss();
		}
	}
	@Override
	public void onOrderStateChanged(OrderButton orderBtn, Integer orderState)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==IntentCodeConst.RESULT_CODE)
		{
			initUserInfo();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{      
			return  true;
		}  

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onRestart()
	{
		// TODO Auto-generated method stub
		super.onRestart();
		if(ProjectCache.getInstance().isChanged())
		{
			ProjectCache.getInstance().setChanged(false);
			refreshList(ProjectCache.getInstance().getProjectList());
		}
	}

	@Override
	public void afterTextChanged(Editable s)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count)
	{
		int textLength = search_input.getText().length();  
        if (textLength > 0) 
        {  
        	search_clear_btn.setVisibility(View.VISIBLE);  
        } 
        else
        {  
        	search_clear_btn.setVisibility(View.GONE);  
        }  
		
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
	{
		/*判断是否是“GO”键*/  
        if(actionId == EditorInfo.IME_ACTION_SEARCH)
        {  
            /*隐藏软键盘*/  
    		String keyword=search_input.getText().toString().trim();
        	//showMessage(keyword);
    		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if(inputMethodManager.isActive()){  
                inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);  
            }  
            conditionList.clear();
        	conditionList.add(new QueryCondition(ConditionTypeEnum.INCLUDLE, "project_name", keyword));
        	loadSendList();

            return true;  
        }  
        return false;  
	}


	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int position,
			long id)
	{
		 Dialog alertDialog = new AlertDialog.Builder(this). 
	                setTitle("Info."). 
	                setMessage("Are you sure to delete this project?"). 
	                setPositiveButton("Confirm", new DialogInterface.OnClickListener() { 
	                     
	                    @Override 
	                    public void onClick(DialogInterface dialog, int which) { 
	                        // TODO Auto-generated method stub 
	                    	deleteProject(position);
	                    } 
	                    
	                }). 
	                setNegativeButton("Cancel", new DialogInterface.OnClickListener() { 
	                     
	                    @Override 
	                    public void onClick(DialogInterface dialog, int which) { 
	                    	dialog.dismiss();
	                    } 
	                }).create();
		 alertDialog.show();
		return true;
	}

	protected void deleteProject(final int position)
	{
		MispBaseReqJson req = new MispBaseReqJson();
		req.setObj(getDataList().get(position).getProject_id());
		WebServiceContext.getInstance().getProjectRest(new MispHttpHandler(){
			@Override
			public void handle(MispHttpMessage message)
			{
				if(message.isSuccess())
				{
					getDataList().remove(position);
					repaint();
					showMessage(message);
				}
				else
				{
					showMessage(message);
				}
			}
		}).deleteProject(req);
		
	}



}
