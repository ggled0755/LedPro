package cn.fuego.led.ui.home;

import java.util.List;

import org.codehaus.jackson.type.TypeReference;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import cn.fuego.common.contanst.ConditionTypeEnum;
import cn.fuego.common.dao.QueryCondition;
import cn.fuego.common.util.validate.ValidatorUtil;
import cn.fuego.led.R;
import cn.fuego.led.constant.IntentCodeConst;
import cn.fuego.led.constant.OrderFlagEnum;
import cn.fuego.led.ui.base.LedBaseListActivity;
import cn.fuego.led.ui.filter.FilterDataCache;
import cn.fuego.led.ui.filter.FilterKeyConst;
import cn.fuego.led.ui.widget.OrderButton;
import cn.fuego.led.webservice.up.model.base.ProductJson;
import cn.fuego.led.webservice.up.rest.WebServiceContext;
import cn.fuego.misp.service.MemoryCache;
import cn.fuego.misp.service.http.MispHttpMessage;
import cn.fuego.misp.ui.util.LoadImageUtil;
import cn.fuego.misp.webservice.json.MispBaseReqJson;
import cn.fuego.misp.webservice.json.MispBaseRspJson;
import cn.fuego.misp.webservice.json.MispPageDataJson;
import cn.fuego.misp.webservice.json.PageJson;

public class HomeActivity extends LedBaseListActivity<ProductJson> implements OnEditorActionListener, TextWatcher
{
	private ProgressDialog pd;
	@Override
	public void initRes()
	{
		//this.activityRes.setName(name);
		this.activityRes.setAvtivityView(R.layout.activity_home);
		this.listViewRes.setListView(R.id.home_list);
		this.listViewRes.setListItemView(R.layout.list_item_home_product);

		this.listViewRes.setClickActivityClass(ProductDetailActivity.class);

		//this.setDataList(StubData.getProductList());
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		TextView txt_title = (TextView) findViewById(R.id.home_title_txt);

        //应用字体
        txt_title.setTypeface(ttf_cabin_bold);
        txt_title.setText(getResources().getString(R.string.title_activity_home));
		search_input.setOnEditorActionListener(this);
		search_input.addTextChangedListener(this);
		if(!ValidatorUtil.isEmpty(FilterDataCache.getInstance().getFilterList()))
		{
			FilterDataCache.getInstance().getFilterList().clear();
		}
		
	}
	public static void jump(Context context)
	{
		Intent intent = new Intent(context,HomeActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent);
	
	}
	@Override
	public View getListItemView(View view, ProductJson item)
	{

		
		TextView title_manufacture = (TextView) view.findViewById(R.id.item_product_title_m);
		title_manufacture.setTypeface(ttf_cabin_regular);
		title_manufacture.setText("Manufacture");
				
		TextView txt_manufacture = (TextView) view.findViewById(R.id.item_product_manufacture);
		txt_manufacture.setTypeface(ttf_cabin_regular);
		txt_manufacture.setText(item.getManufacture());
		
		TextView txt_name = (TextView) view.findViewById(R.id.item_product_name);
		txt_name.setTypeface(ttf_cabin_semibold);
		txt_name.setText(item.getProduct_name());
		
		TextView title_warranty = (TextView) view.findViewById(R.id.item_product_title_w);
		title_warranty.setTypeface(ttf_cabin_regular);
		title_warranty.setText("Warranty");

		TextView txt_warranty = (TextView) view.findViewById(R.id.item_product_warranty);
		txt_warranty.setTypeface(ttf_cabin_regular);
		txt_warranty.setText(item.getWarranty());
		
		TextView txt_pf= (TextView) view.findViewById(R.id.item_product_pf);
		txt_pf.setTypeface(ttf_cabin_regular);
		txt_pf.setText("PF:"+item.getProduct_score());
		
		ImageView img_product = (ImageView) view.findViewById(R.id.item_product_img);
		if(!ValidatorUtil.isEmpty(item.getProduct_img()))
		{
			String img_tag=(String) img_product.getTag();
			if(null==img_tag||!img_tag.equals(item.getProduct_img()))
			{
				img_product.setTag(item.getProduct_img());
				LoadImageUtil.getInstance().loadImage(img_product, MemoryCache.getImageUrl()+item.getProduct_img());
			}

		}

		return view;
	}

	@Override
	public void loadSendList()
	{
		pd = ProgressDialog.show(this, null, getResources().getString(R.string.progress_msg_loading));
		pd.setCancelable(true);
		MispBaseReqJson req = new MispBaseReqJson();
		PageJson page = new PageJson();
		page.setCurrentPage(1);
		page.setPageSize(10);
		req.setPage(page);
		req.setConditionList(FilterDataCache.getInstance().getFilterList());
		WebServiceContext.getInstance().getProductRest(this).loadProduct(req);
	}

	@Override
	public List<ProductJson> loadListRecv(Object obj)
	{
		pd.dismiss();
		MispBaseRspJson rsp = (MispBaseRspJson) obj;
		
		MispPageDataJson<ProductJson> pageData = rsp.GetReqCommonField(new TypeReference<MispPageDataJson<ProductJson>>(){});
		List<ProductJson> productList=pageData.getRows();
		
		return productList;
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
		//Toast.makeText(this, "state"+orderState, Toast.LENGTH_LONG).show();
		String conditionType=OrderFlagEnum.getEnumByInt(orderState).getStrValue();
		clearOthers(orderBtn);
		switch (orderBtn.getId())
		{
		case R.id.order_pf_btn:
			FilterDataCache.getInstance().updateOrderCondition(FilterKeyConst.pf,ConditionTypeEnum.getEnumByStr(conditionType));
			
			break;
		case R.id.order_like_btn:
			break;
		case R.id.order_price_btn:
			FilterDataCache.getInstance().updateOrderCondition(FilterKeyConst.price,ConditionTypeEnum.getEnumByStr(conditionType));
			break;
		default:
			break;
		}
		loadSendList();
		
	}
	//排序条件只允许一个
	private void clearOthers(OrderButton orderBtn)
	{
		for(Integer id:btnList)
		{
			if(id!=orderBtn.getId())
			{
				OrderButton oBtn = (OrderButton) findViewById(id);
				oBtn.clearState();
			}
		}
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==IntentCodeConst.RESULT_CODE_FILTER_SERACH)
		{
			//showMessage("开始筛选");
			loadSendList();
		}
	}
	@Override
	public void afterTextChanged(Editable s)
	{
		// TODO Auto-generated method stub
		
	}
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,	int after)
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
    		FilterDataCache.getInstance().getFilterList().clear();
        	FilterDataCache.getInstance().getFilterList().add(new QueryCondition(ConditionTypeEnum.INCLUDLE, "product_name", keyword));
        	loadSendList();
              
            return true;  
        }  
        return false;  
	}
	

}
