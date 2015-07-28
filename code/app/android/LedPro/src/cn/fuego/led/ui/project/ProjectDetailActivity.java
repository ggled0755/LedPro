package cn.fuego.led.ui.project;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.type.TypeReference;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import cn.fuego.common.contanst.ConditionTypeEnum;
import cn.fuego.common.dao.QueryCondition;
import cn.fuego.common.util.format.DateUtil;
import cn.fuego.common.util.validate.ValidatorUtil;
import cn.fuego.led.R;
import cn.fuego.led.cache.SubfolderCache;
import cn.fuego.led.constant.IntentCodeConst;
import cn.fuego.led.ui.base.CreateDialog;
import cn.fuego.led.ui.base.CreateDialog.OnConfirmListener;
import cn.fuego.led.ui.base.LedBaseActivity;
import cn.fuego.led.ui.profile.SimpleTreeAdapter;
import cn.fuego.led.ui.profile.SubfolderDetailActivity;
import cn.fuego.led.util.treeview.Node;
import cn.fuego.led.util.treeview.TreeListViewAdapter;
import cn.fuego.led.util.treeview.TreeListViewAdapter.OnTreeNodeClickListener;
import cn.fuego.led.webservice.up.model.base.ProductJson;
import cn.fuego.led.webservice.up.model.base.ProjectJson;
import cn.fuego.led.webservice.up.model.base.SubfolderDetailJson;
import cn.fuego.led.webservice.up.model.base.SubfolderJson;
import cn.fuego.led.webservice.up.rest.WebServiceContext;
import cn.fuego.misp.service.MemoryCache;
import cn.fuego.misp.service.http.MispHttpHandler;
import cn.fuego.misp.service.http.MispHttpMessage;
import cn.fuego.misp.ui.model.ListViewResInfo;
import cn.fuego.misp.ui.util.StrUtil;
import cn.fuego.misp.webservice.json.MispBaseReqJson;
import cn.fuego.misp.webservice.json.MispBaseRspJson;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class ProjectDetailActivity extends LedBaseActivity
{
/*	//操作方式，0-编辑状态， 对subfolder操作区分
	//0,编辑内部的产品，1-添加产品到指定文件夹
	private int operate_type=0;
	public final int TYPE_EDIT=0;
	public final int TYPE_SELECT=1;*/
	
	private ListView mTree;	
	private List<SubfolderJson> datas =new ArrayList<SubfolderJson>();
	@SuppressWarnings("rawtypes")
	private TreeListViewAdapter mAdapter;
	
	private ProjectJson project;
	private EditText txt_notes;
	
	private ProductJson product;
	private int parent_id=0;
	private boolean addEnable=false;
	
	@Override
	public void initRes()
	{
		this.activityRes.setAvtivityView(R.layout.activity_project_detail);
		this.activityRes.getButtonIDList().add(R.id.project_detail_add_btn);
		this.activityRes.getButtonIDList().add(R.id.project_detail_title);
		
		project = (ProjectJson) this.getIntent().getSerializableExtra(ListViewResInfo.SELECT_ITEM);
		product = (ProductJson) this.getIntent().getSerializableExtra(IntentCodeConst.DATA_PRODUCT);
		if(project!=null)
		{
			this.activityRes.setName(StrUtil.noNullStr(project.getProject_name()));
			
			
		}
		
			
	}
	
	private void loadSubfolder()
	{
		MispBaseReqJson req = new MispBaseReqJson();
		List<QueryCondition> conditionList = new ArrayList<QueryCondition>();
		conditionList.add(new QueryCondition(ConditionTypeEnum.EQUAL, "project_id", project.getProject_id()));
		req.setConditionList(conditionList);
		WebServiceContext.getInstance().getSubfolderRest(new MispHttpHandler(){
			@Override
			public void handle(MispHttpMessage message)
			{
				if(message.isSuccess())
				{
					MispBaseRspJson rsp = (MispBaseRspJson) message.getMessage().obj;
					datas =  rsp.GetReqCommonField(new TypeReference<List<SubfolderJson>>(){});
					if(!ValidatorUtil.isEmpty(datas))
					{
						SubfolderCache.getInstance().setSubfolderList(datas);
						initSubfolderTree();
					}
				}
				else
				{
					showMessage(message);
				}
			}
		}).loadAll(req);
		
	}

	public static void jump(Context context,ProjectJson project,ProductJson product)
	{
		Intent intent = new Intent(context,ProjectDetailActivity.class);
		intent.putExtra(ListViewResInfo.SELECT_ITEM, project);
		intent.putExtra(IntentCodeConst.DATA_PRODUCT, product);
		
		context.startActivity(intent);

	}
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		initSubfolderTree();
		loadSubfolder();
		
		txt_notes = (EditText) findViewById(R.id.project_detail_notes);
		txt_notes.clearFocus();
		
		TextView txt_products = (TextView) findViewById(R.id.project_detail_products);
		txt_products.setText(String.valueOf(project.getTotal_catg())+"/"+String.valueOf(project.getTotal_num()));
		txt_products.requestFocus();
		txt_products.requestFocusFromTouch();
		
		TextView txt_watt = (TextView) findViewById(R.id.project_detail_watt);
		txt_watt.setText(String.valueOf(project.getTotal_watt()));
		
		TextView txt_tco = (TextView) findViewById(R.id.project_detail_tco);
		txt_tco.setText(String.valueOf(project.getTotal_cost()));
		
	}

	@SuppressWarnings("unchecked")
	private void initSubfolderTree()
	{
		mTree  = (ListView) findViewById(R.id.project_detail_list);

		//datas = StubData.getSubfolderList();
		try
		{
			mAdapter = new SimpleTreeAdapter<SubfolderJson>(mTree, this, datas , 2);
			mTree.setAdapter(mAdapter);
			SubfolderCache.getInstance().setAllNode(mAdapter.getAllNodes());
			mAdapter.setOnTreeNodeClickListener(new OnTreeNodeClickListener()
			{

				@Override
				public void onClick(Node node, int position)
				{
					//showMessage(String.valueOf(node.getId()));
					parent_id=node.getId();
					SubfolderCache.getInstance().setTargetID(node.getId());
					if(node.isExpand()&&!addEnable)
					{						
						if(product!=null)
						{
							createSubfolderDetail();
						}
						else
						{
							//showMessage(SubfolderCache.getInstance().getSelIDList().toString());
							SubfolderDetailActivity.jump(ProjectDetailActivity.this, SubfolderCache.getInstance().getSelSd(node.getId()));
							
						}
					}
					
				}


			});
		}
		catch (IllegalArgumentException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		super.onClick(v);
		if(v.getId()==R.id.project_detail_add_btn)
		{
			if(addEnable)
			{
				addEnable=false;
				CreateDialog dialog= new CreateDialog(this, "Add a subfolder for", "Please enter your subfolder name here", new OnConfirmListener()
				{
					
					@Override
					public void confirmCallback(String name)
					{
						createSubfolder(name);
						
					}


				});
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				
				dialog.show();
			}
			else
			{
				addEnable=true;
				showToast(this, "Please select your parent subfolder");
			}
		}
		if(v.getId()==R.id.project_detail_title)
		{
			generatePDF();
		}
	}

	private void generatePDF()
	{
		MispBaseReqJson req = new MispBaseReqJson();
		req.setObj(project.getProject_id());
		
		WebServiceContext.getInstance().getProjectRest(new MispHttpHandler(){
			@Override
			public void handle(MispHttpMessage message)
			{

				if(message.isSuccess())
				{
					MispBaseRspJson rsp = (MispBaseRspJson) message.getMessage().obj;
					String url = rsp.GetReqCommonField(String.class);
					if(!ValidatorUtil.isEmpty(url))
					{
						String webUrl=MemoryCache.getWebContextUrl()+"/Client/Public/Fuego/PDF/"+url;
						HttpUtils http = new HttpUtils();
						String appName = getResources().getString(R.string.app_name);
						File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), appName+"/pdf"); 
						//目标文件加上时间戳
						String target =cacheDir.getAbsolutePath()+"/"+System.currentTimeMillis()+url;

						@SuppressWarnings({ "unused", "rawtypes" })
						HttpHandler handler = http.download(webUrl,
								target,
							    true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
							    true, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
							    new RequestCallBack<File>() {

							        @Override
							        public void onStart() {
							            //testTextView.setText("conn...");
							        }

							        @Override
							        public void onLoading(long total, long current, boolean isUploading) {
							           // testTextView.setText(current + "/" + total);
							        }

							        @Override
							        public void onSuccess(ResponseInfo<File> responseInfo) {
							           // testTextView.setText("downloaded:" + responseInfo.result.getPath());
							        	showMessage("Success");
							        	Intent intent = new Intent("android.intent.action.VIEW");  
							        	intent.addCategory("android.intent.category.DEFAULT");  
							        	intent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);  
							        	Uri uri = Uri.fromFile(responseInfo.result); 
							        	 
							        	intent.setDataAndType (uri, "application/pdf");  
							        	startActivity(intent); 
							        }


							        @Override
							        public void onFailure(HttpException error, String msg) {
							            //testTextView.setText(msg);
							        	Log.e("download", msg, error);
							        	showMessage("Failed");
							        }
							});

					}
					
					
				}
				else
				{
					showMessage(message);
				}
				
			}
		}).createPdf(req);
		
	}

	private void createSubfolder(String name)
	{
		SubfolderJson json = new SubfolderJson();
		json.setProject_id(project.getProject_id());
		json.setSubfolder_name(name);
		json.setSubfolder_parent_id(parent_id);
		
		MispBaseReqJson req = new MispBaseReqJson();
		req.setObj(json);
		
		WebServiceContext.getInstance().getSubfolderRest(new MispHttpHandler(){
			@Override
			public void handle(MispHttpMessage message)
			{
				if(message.isSuccess())
				{
					loadSubfolder();
				}
				else
				{
					showMessage(message);
				}
			}
		}).createSubfolder(req);
		
	}
	
	private void createSubfolderDetail()
	{
		MispBaseReqJson req = new MispBaseReqJson();
		SubfolderDetailJson  json = new SubfolderDetailJson();
		json.setSubfolder_id(parent_id);
		json.setProduct_id(product.getProduct_id());
		req.setObj(json);
		
		WebServiceContext.getInstance().getSubfolderRest(new MispHttpHandler(){
			@Override
			public void handle(MispHttpMessage message)
			{
				if(message.isSuccess())
				{
					showMessage(message);
				}
				else
				{
					showMessage(message);
				}
			}
		}).createSubfolderDetail(req);
		
		
	}
}
