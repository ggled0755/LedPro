package cn.fuego.led.ui.home;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.type.TypeReference;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import cn.fuego.common.contanst.ConditionTypeEnum;
import cn.fuego.common.dao.QueryCondition;
import cn.fuego.common.util.format.JsonConvert;
import cn.fuego.common.util.validate.ValidatorUtil;
import cn.fuego.led.R;
import cn.fuego.led.constant.IntentCodeConst;
import cn.fuego.led.ui.LoginActivity;
import cn.fuego.led.ui.base.LedBaseActivity;
import cn.fuego.led.ui.project.ProjectListActivity;
import cn.fuego.led.ui.widget.GalleryBaseView;
import cn.fuego.led.ui.widget.ItemClickCallback;
import cn.fuego.led.webservice.up.model.base.ProductJson;
import cn.fuego.led.webservice.up.rest.WebServiceContext;
import cn.fuego.misp.service.MemoryCache;
import cn.fuego.misp.service.http.MispHttpHandler;
import cn.fuego.misp.service.http.MispHttpMessage;
import cn.fuego.misp.ui.model.ListViewResInfo;
import cn.fuego.misp.ui.util.LoadImageUtil;
import cn.fuego.misp.webservice.json.MispBaseReqJson;
import cn.fuego.misp.webservice.json.MispBaseRspJson;

public class ProductDetailActivity extends LedBaseActivity
{

	private ProductJson product;
	private GalleryBaseView gallery_product;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initView();
		loadRelatedItems();
	}
	
	private void loadRelatedItems()
	{
		String ids=product.getRelated_product_ids();
		//String[] ids = product.getRelated_product_ids().;
		//List<Integer> relate_list=JsonConvert.jsonToObject(ids, new TypeReference<List<Integer>>(){});
		List<String> relate_list=JsonConvert.jsonToObject(ids, new TypeReference<List<String>>(){});
		if(!ValidatorUtil.isEmpty(relate_list))
		{
			MispBaseReqJson req = new MispBaseReqJson();
			List<QueryCondition> conditionList = new ArrayList<QueryCondition>();
			conditionList.add(new QueryCondition(ConditionTypeEnum.IN, "product_id", relate_list));
			req.setConditionList(conditionList);
			
			WebServiceContext.getInstance().getProductRest(new MispHttpHandler(){
				@Override
				public void handle(MispHttpMessage message)
				{
					if(message.isSuccess())
					{
						MispBaseRspJson rsp  = (MispBaseRspJson) message.getMessage().obj;
						List<ProductJson> productList =rsp.GetReqCommonField(new TypeReference<List<ProductJson>>(){});
						if(!ValidatorUtil.isEmpty(productList))
						{
							displayGallery(productList);
						}
						
						
					}
					else
					{
						showMessage(message);
					}
				}

			}).loadProductAll(req);
		}
		
	}

	private void displayGallery(List<ProductJson> productList)
	{
		
		List<String> strList = new ArrayList<String>();
		for(ProductJson p:productList)
		{
			strList.add(MemoryCache.getImageUrl()+p.getProduct_img());
		}
		gallery_product.initUI(strList.toArray(new String[strList.size()]) , new ItemClickCallback()
		{
			
			@Override
			public void onItemClick(Integer position)
			{
				//ProductDetailActivity.jumpToActivity(context, clazz);
	
			}
		});
		
	}
	@Override
	public void initRes()
	{
		this.activityRes.setAvtivityView(R.layout.activity_product_detail);
		product = (ProductJson) this.getIntent().getSerializableExtra(ListViewResInfo.SELECT_ITEM);
		if(null!=product)
		{
			this.activityRes.setName(product.getProduct_name());
		}
		this.activityRes.getButtonIDList().add(R.id.product_detail_add_btn);
		this.activityRes.getButtonIDList().add(R.id.product_detail_re_btn);
		this.activityRes.getButtonIDList().add(R.id.product_detail_fl_btn);
		this.activityRes.getButtonIDList().add(R.id.product_detail_score_view);
	}
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.product_detail_add_btn:
			if(MemoryCache.isLogined())
			{
				ProjectListActivity.jump(this, product);
			}
			else
			{
				//LoginActivity.jump(this, ProductDetailActivity.class, IntentCodeConst.REQUEST_CODE);
				LoginActivity.jump(this, IntentCodeConst.REQUEST_CODE);
			}
			break;
		case R.id.product_detail_re_btn:
			ProEvalExplainActivity.jump(this, product);
			break;
		case R.id.product_detail_fl_btn:
			
			break;	
		case R.id.product_detail_score_view:
			ProEvalCreateActivity.jump(this, product);
			break;
		default:
			break;
		}

	}

	private void initView()
	{
		ImageView img_big = (ImageView) findViewById(R.id.product_detail_img);
		if(!ValidatorUtil.isEmpty(product.getProduct_img()))
		{
			LoadImageUtil.getInstance().loadImage(img_big, MemoryCache.getImageUrl()+product.getProduct_img());
		}
		ImageView img_sm1 = (ImageView) findViewById(R.id.product_detail_img_sm1);
		if(!ValidatorUtil.isEmpty(product.getProduct_img_sm1()))
		{
			LoadImageUtil.getInstance().loadImage(img_sm1, MemoryCache.getImageUrl()+product.getProduct_img_sm1());
		}
		ImageView img_sm2 = (ImageView) findViewById(R.id.product_detail_img_sm2);
		if(!ValidatorUtil.isEmpty(product.getProduct_img_sm2()))
		{
			LoadImageUtil.getInstance().loadImage(img_sm2, MemoryCache.getImageUrl()+product.getProduct_img_sm2());
		}
		ImageView img_sm3 = (ImageView) findViewById(R.id.product_detail_img_sm3);
		
		if(!ValidatorUtil.isEmpty(product.getProduct_img_sm3()))
		{
			LoadImageUtil.getInstance().loadImage(img_sm3, MemoryCache.getImageUrl()+product.getProduct_img_sm3());
		}		
		TextView txt_name = (TextView) findViewById(R.id.product_detail_name);
		txt_name.setTypeface(ttf_cabin_semibold);
		txt_name.setText(product.getProduct_name());
		
		RatingBar rat_rat = (RatingBar) findViewById(R.id.product_detail_rating);
		rat_rat.setRating(product.getProduct_score());
		
		TextView txt_desp = (TextView) findViewById(R.id.product_detail_desp);
		txt_desp.setTypeface(ttf_Helvetica_Neue);
		txt_desp.setText(product.getProduct_desp());
		
		TextView txt_manufacture = (TextView) findViewById(R.id.product_detail_manufacture);
		txt_manufacture.setTypeface(ttf_cabin_regular);
		txt_manufacture.setText("Manufacture:"+product.getManufacture());
		
		TextView txt_cert = (TextView) findViewById(R.id.product_detail_certification);
		txt_cert.setTypeface(ttf_cabin_regular);
		txt_cert.setText("Certification:"+product.getCertification());
		
		Button btn_rating = (Button) findViewById(R.id.product_detail_re_btn);
		btn_rating.setTypeface(ttf_cabin_regular);
		
		Button btn_full_list = (Button) findViewById(R.id.product_detail_fl_btn);
		btn_full_list.setTypeface(ttf_cabin_regular);	
		
		gallery_product = (GalleryBaseView) findViewById(R.id.product_detail_gallery);

		
	}
}
