package cn.fuego.led.ui.home;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.type.TypeReference;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
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
import cn.fuego.misp.ui.model.ListViewResInfo;
import cn.fuego.misp.ui.util.LoadImageUtil;
import cn.fuego.misp.webservice.json.MispBaseReqJson;
import cn.fuego.misp.webservice.json.MispBaseRspJson;
import cn.fuego.misp.webservice.json.MispPageDataJson;
import cn.fuego.misp.webservice.json.PageJson;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class HomeActivity extends LedBaseListActivity<ProductJson> implements OnEditorActionListener, TextWatcher
{
	private ProgressDialog pd;
	private int pageIndex=1;
	private int currentPageSize;
	private int pageSize=10; 
	private List<ProductJson> productCache = new ArrayList<ProductJson>();
	private View home_bottom ;
	private PullToRefreshListView home_list;
	private boolean nextRefresh=false;
	private boolean pullDown =false;
	@Override
	public void initRes()
	{
		this.activityRes.setAvtivityView(R.layout.activity_home);
		this.listViewRes.setListView(R.id.home_list);
		this.listViewRes.setListItemView(R.layout.list_item_home_product);
		this.listViewRes.setListType(ListViewResInfo.VIEW_TYPE_PAGE_LIST);
		
		this.listViewRes.setClickActivityClass(ProductDetailActivity.class);

	}
	
	@Override
	protected void OnPullToRefresh(PullToRefreshBase<ListView> refreshView)
	{
		pageIndex=1;
		pullDown=true;
		if(!ValidatorUtil.isEmpty(productCache))
		{
			productCache.clear();
		}
		loadSendList();
		
	}

	@Override
	public void onLastItemVisible()
	{
		
		if(!nextRefresh)
		{
			nextRefresh=true;
			if(pageSize>currentPageSize)
			{
				nextRefresh=false;
				showMessage("no more data");
				return;
			}
			else
			{
				pageIndex+=1;
				productCache.addAll(this.getDataList());
				loadSendList();
			}
		}
		
		
		
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
		home_bottom =  findViewById(R.id.home_bottom);
		
		home_list = (PullToRefreshListView) findViewById(R.id.home_list);

		
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
		String img_tag=(String) img_product.getTag();
		if(!ValidatorUtil.isEmpty(item.getProduct_img()))
		{
			
			if(null==img_tag||!img_tag.equals(item.getProduct_img()))
			{
				img_product.setTag(item.getProduct_img());
				LoadImageUtil.getInstance().loadImage(img_product, MemoryCache.getImageUrl()+item.getProduct_img());
			}

		}
		else
		{
			if(null==img_tag||!img_tag.equals(item.getProduct_code()))
			{
				img_product.setTag(item.getProduct_code());
				LoadImageUtil.getInstance().loadImage(img_product, R.drawable.led1);
			}
			
		}

		return view;
	}

	@Override
	public void loadSendList()
	{
		if(!pullDown)
		{
			pd = ProgressDialog.show(this, null, getResources().getString(R.string.progress_msg_loading));
			pd.setCancelable(true);
		}
		else
		{
			pullDown=false;
		}
		MispBaseReqJson req = new MispBaseReqJson();
		PageJson page = new PageJson();
		page.setCurrentPage(pageIndex);
		page.setPageSize(pageSize);
		req.setPage(page);
		req.setConditionList(FilterDataCache.getInstance().getFilterList());
		WebServiceContext.getInstance().getProductRest(this).loadProduct(req);
	}

	@Override
	public List<ProductJson> loadListRecv(Object obj)
	{
		if(null!=pd&&pd.isShowing())
		{
			pd.dismiss();
		}
		
		this.OnRefreshComplete();
		nextRefresh=false;
		
		MispBaseRspJson rsp = (MispBaseRspJson) obj;		
		MispPageDataJson<ProductJson> pageData = rsp.GetReqCommonField(new TypeReference<MispPageDataJson<ProductJson>>(){});
		currentPageSize =pageData.getTotal()-(pageIndex-1)*pageSize;
		List<ProductJson> productList = new ArrayList<ProductJson>();
		if(!ValidatorUtil.isEmpty(productCache))
		{
			if(!ValidatorUtil.isEmpty(pageData.getRows()))
			{
				productCache.addAll(pageData.getRows());
				return productCache;
			}

		}
		if(!ValidatorUtil.isEmpty(pageData.getRows()))
		{
			productList.addAll(pageData.getRows());
		}
		
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

	@Override
	public void onHeightChanged(CompoundButton buttonView)
	{
		Log.i("home_bottom", String.valueOf(home_bottom.getHeight()));
		home_list.setRefreshSize(false);
		//home_list.setPadding(home_list.getPaddingLeft(),home_list.getPaddingTop(), home_list.getPaddingRight(), home_bottom.getHeight());
		
	}
	
/*    class HomeReceiver extends BroadcastReceiver
    {

		@Override
		public void onReceive(Context context, Intent intent)
		{

			int refreshCode = intent.getIntExtra(IntentCodeConst.HOME_REFRESH, 0);

		}
	};*/
}
