package cn.fuego.led.ui.project;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.type.TypeReference;

import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import cn.fuego.common.contanst.ConditionTypeEnum;
import cn.fuego.common.dao.QueryCondition;
import cn.fuego.common.util.validate.ValidatorUtil;
import cn.fuego.led.R;
import cn.fuego.led.cache.ProjectCache;
import cn.fuego.led.cache.SubfolderCache;
import cn.fuego.led.constant.IntentCodeConst;
import cn.fuego.led.ui.base.CreateDialog;
import cn.fuego.led.ui.base.CreateDialog.OnConfirmListener;
import cn.fuego.led.ui.base.ExportPdfDialog;
import cn.fuego.led.ui.base.ExportPdfDialog.OnExConfirmListener;
import cn.fuego.led.ui.base.LedBaseActivity;
import cn.fuego.led.ui.base.ModifyDialog;
import cn.fuego.led.ui.base.ModifyDialog.OnModifyConfirmListener;
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
import com.nostra13.universalimageloader.utils.StorageUtils;

public class ProjectDetailActivity extends LedBaseActivity
{

	private ListView mTree;	
	private List<SubfolderJson> datas =new ArrayList<SubfolderJson>();
	@SuppressWarnings("rawtypes")
	private TreeListViewAdapter mAdapter;
	
	private ProjectJson project;
	private TextView txt_notes,txt_title,txt_products,txt_watt,txt_tco;
	
	private ProductJson product;
	private int parent_id=0;
	private boolean addEnable=false;
	
	private ProgressDialog pd;
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
		txt_title = (TextView) findViewById(R.id.misp_title_name);
		txt_notes = (TextView) findViewById(R.id.project_detail_notes);
		txt_notes.setTypeface(ttf_cabin_regular);
		txt_products = (TextView) findViewById(R.id.project_detail_products);
		txt_watt = (TextView) findViewById(R.id.project_detail_watt);
		txt_tco = (TextView) findViewById(R.id.project_detail_tco);
		initView();
		initTypeface();
		
	}
	//初始化标题字体
	private void initTypeface()
	{
		TextView title_notes= (TextView) findViewById(R.id.project_detail_title_notes);
		title_notes.setTypeface(ttf_cabin_semibold);
		TextView title_summary= (TextView) findViewById(R.id.project_detail_title_summary);
		title_summary.setTypeface(ttf_cabin_semibold);
		TextView title_sub= (TextView) findViewById(R.id.project_detail_title_subfolder);
		title_sub.setTypeface(ttf_cabin_semibold);
		
		txt_products.setTypeface(ttf_cabin_regular);
		txt_watt.setTypeface(ttf_cabin_regular);
		txt_tco.setTypeface(ttf_cabin_regular);
	}
	private void initView()
	{
		txt_title.setText(project.getProject_name());
		txt_notes.setText(StrUtil.noNullStr(project.getProject_note()));
		txt_products.setText(String.valueOf(project.getTotal_catg())+"/"+String.valueOf(project.getTotal_num()));
		txt_watt.setText(String.valueOf(project.getTotal_watt()));
		txt_tco.setText("$"+String.valueOf(project.getTotal_cost()));
	}
	@Override
	public void saveOnClick(View v)
	{
		pd = ProgressDialog.show(this, null, getString(R.string.progress_msg_processing));
		MispBaseReqJson req = new MispBaseReqJson();
		req.setObj(project.getProject_id());
		WebServiceContext.getInstance().getProjectRest(new MispHttpHandler(){
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
					String url = rsp.GetReqCommonField(String.class);
					if(!ValidatorUtil.isEmpty(url))
					{
						selOperDialog(url);
					}
				}
				else
				{
					showMessage(message);
				}
			}
		}).createPdf(req);
	}

	private void selOperDialog(final String url)
	{
		String webUrl=MemoryCache.getWebContextUrl()+"/Client/Public/Fuego/PDF/"+url;
		ExportPdfDialog dialog = new ExportPdfDialog(this, project.getProject_name(), webUrl);
		dialog.setConfirmListener(new OnExConfirmListener()
		{
			
			@Override
			public void otherCallbak(String content)
			{
				copyUrl(content);
				
			}
			
			@Override
			public void confirmCallback(String content)
			{

				downloadPDF(content,url);

				
			}
		});
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		dialog.show();
		
	}

	@SuppressWarnings("deprecation")
	protected void copyUrl(String content)
	{
		// 得到剪贴板管理器  
		ClipboardManager cmb = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);  
		cmb.setText(content);  
		showMessage("copy to clipbord");
		
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
					if(!addEnable)
					{
						if(node.isLeaf()||!node.isExpand())
						{						
							if(product!=null)
							{
								createSubfolderDetail();
							}
							else
							{
								SubfolderDetailActivity.jump(ProjectDetailActivity.this,
										SubfolderCache.getInstance().getSelSd(node.getId()),node);
								
							}
						}
					}
					else
					{
						//showMessage("press add subfolder button to cancel");
						showToast(ProjectDetailActivity.this, "press add subfolder button to cancel");
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
			if(!addEnable)
			{
				addEnable=true;
				createSubfolder();
			}
			else
			{
				addEnable=false;
			}
			
		}
		if(v.getId()==R.id.project_detail_title)
		{
			modifyProject(project.getProject_name(),StrUtil.noNullStr(project.getProject_note()));
		}
	}
	
	private void createSubfolder()
	{
		CreateDialog dialog= new CreateDialog(this, "Add a subfolder for "+StrUtil.noNullStr(SubfolderCache.getInstance().getSelSd(parent_id).getSubfolder_name()), 
				"Please enter your subfolder name here", new OnConfirmListener()
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
	//修改project信息，包含name 和notes
	private void modifyProject(String name,String note)
	{
		ModifyDialog dialog = new ModifyDialog(this, name, note);
		dialog.setConfirmListener(new OnModifyConfirmListener()
		{

			@Override
			public void confirmCallback(String name, String note)
			{
				project.setProject_name(name);
				project.setProject_note(note);
				MispBaseReqJson req = new MispBaseReqJson();
				req.setObj(project);
				WebServiceContext.getInstance().getProjectRest(new MispHttpHandler(){
					@Override
					public void handle(MispHttpMessage message)
					{
						if(message.isSuccess())
						{
							initView();
							ProjectCache.getInstance().updatePro(project);
							ProjectCache.getInstance().setChanged(true);
						}
						else
						{
							showMessage(message);
						}
					}
				}).modifyProject(req);
				
			}

		});
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		dialog.show();
		
	}
	//下载pdf
	private void downloadPDF(String webUrl,String fileName)
	{

		HttpUtils http = new HttpUtils();
		String appName = getResources().getString(R.string.app_name);
		File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), appName+"/pdf"); 
		//目标文件加上时间戳
		String target =cacheDir.getAbsolutePath()+"/"+System.currentTimeMillis()+fileName;
		@SuppressWarnings({ "unused", "rawtypes" })
		HttpHandler handler = http.download(webUrl,
				target,
			    true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
			    true, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
			    new RequestCallBack<File>() {

			        @Override
			        public void onStart() {
			        	pd = ProgressDialog.show(ProjectDetailActivity.this, null, getString(R.string.progress_msg_processing));
			        }

			        @Override
			        public void onLoading(long total, long current, boolean isUploading) {
			           // testTextView.setText(current + "/" + total);
			        }

			        @Override
			        public void onSuccess(ResponseInfo<File> responseInfo) {
					if(pd!=null&&pd.isShowing())
						{
							pd.dismiss();
						}
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
						if(pd!=null&&pd.isShowing())
						{
							pd.dismiss();
						}
			        	Log.e("download", msg, error);
			        	showMessage("Failed");
			        }
			});

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
					finish();
				}
				else
				{
					showMessage(message);
				}
			}
		}).createSubfolderDetail(req);
		
		
	}
	@Override
	protected void onRestart()
	{
		// TODO Auto-generated method stub
		super.onRestart();
		if(SubfolderCache.getInstance().isChange())
		{
			SubfolderCache.getInstance().setChange(false);
			datas =SubfolderCache.getInstance().getSubfolderList();
			initSubfolderTree();
		}
		if(ProjectCache.getInstance().isChanged())
		{
			project = ProjectCache.getInstance().getProjectMap().get(project.getProject_id());
			initView();
		}
	}
	
	
}
