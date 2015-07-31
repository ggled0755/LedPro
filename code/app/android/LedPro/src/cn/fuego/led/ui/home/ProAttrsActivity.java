package cn.fuego.led.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;
import cn.fuego.led.R;
import cn.fuego.led.ui.base.LedBaseActivity;
import cn.fuego.led.webservice.up.model.base.ProductJson;
import cn.fuego.misp.ui.model.ListViewResInfo;

public class ProAttrsActivity extends LedBaseActivity
{

	private ProductJson product;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initView();
	}


	@Override
	public void initRes()
	{
		this.activityRes.setAvtivityView(R.layout.activity_pro_attrs);
		this.activityRes.setName(getString(R.string.title_activity_pro_attrs));
		
		product = (ProductJson) this.getIntent().getSerializableExtra(ListViewResInfo.SELECT_ITEM);
		
	}
	
	public static void jump(Context context,ProductJson product)
	{
		Intent i =new Intent();
		i.setClass(context, ProAttrsActivity.class);
		i.putExtra(ListViewResInfo.SELECT_ITEM, product);
		context.startActivity(i);
	}
	
	private void initView()
	{
		TextView txt_name = (TextView) findViewById(R.id.pro_attrs_name);
		txt_name.setText(product.getProduct_name());
		txt_name.setTypeface(ttf_cabin_semibold);
		
		RatingBar rat_score = (RatingBar) findViewById(R.id.pro_attrs_rat);
		rat_score.setRating(product.getProduct_score());
		
		TextView txt_desp = (TextView) findViewById(R.id.pro_attrs_desp);
		txt_desp.setText(product.getProduct_desp());
		txt_desp.setTypeface(ttf_cabin_regular);

		TextView txt_size = (TextView) findViewById(R.id.pro_attrs_size);
		txt_size.setText(product.getProduct_size());
		txt_size.setTypeface(ttf_cabin_regular);

		TextView txt_catg = (TextView) findViewById(R.id.pro_attrs_catg);
		txt_catg.setText(product.getProdcut_catg());
		txt_catg.setTypeface(ttf_cabin_regular);
		
		TextView txt_sub_catg = (TextView) findViewById(R.id.pro_attrs_sub_catg);
		txt_sub_catg.setText(product.getSub_catg());
		txt_sub_catg.setTypeface(ttf_cabin_regular);
		
		TextView txt_manu = (TextView) findViewById(R.id.pro_attrs_manufature);
		txt_manu.setText(getString(R.string.text_attr_manufacture)+product.getManufacture());
		txt_manu.setTypeface(ttf_cabin_regular);
		
		TextView txt_warranty = (TextView) findViewById(R.id.pro_attrs_warranty);
		txt_warranty.setText(getString(R.string.text_attr_warranty)+product.getWarranty());
		txt_warranty.setTypeface(ttf_cabin_regular);
		
		TextView txt_lumen = (TextView) findViewById(R.id.pro_attrs_lumen);
		txt_lumen.setText(getString(R.string.text_attr_lumen)+product.getLumen_output());
		txt_lumen.setTypeface(ttf_cabin_regular);
		
		TextView txt_watt = (TextView) findViewById(R.id.pro_attrs_input_watt);
		txt_watt.setText(getString(R.string.text_attr_watt)+product.getInput_watt());
		txt_watt.setTypeface(ttf_cabin_regular);
		
		TextView txt_loc = (TextView) findViewById(R.id.pro_attrs_use_location);
		txt_loc.setText(getString(R.string.text_attr_use_loc)+product.getUse_location());
		txt_loc.setTypeface(ttf_cabin_regular);
		
		TextView txt_base = (TextView) findViewById(R.id.pro_attrs_mounting_base);
		txt_base.setText(getString(R.string.text_attr_mounting_base)+product.getMounting_base());
		txt_base.setTypeface(ttf_cabin_regular);
		
	}

}
