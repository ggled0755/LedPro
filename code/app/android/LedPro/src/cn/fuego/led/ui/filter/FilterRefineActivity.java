package cn.fuego.led.ui.filter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import cn.fuego.led.R;
import cn.fuego.led.constant.IntentCodeConst;
import cn.fuego.led.ui.base.LedBaseActivity;
import cn.fuego.led.ui.widget.RangeSeekBar;
import cn.fuego.misp.ui.base.MispListViewAdaptScroll;
import cn.fuego.misp.ui.model.ListViewResInfo;

public class FilterRefineActivity extends LedBaseActivity implements OnItemClickListener
{

	private MispListViewAdaptScroll listview;
	private DistinctAdapter mAdapter ;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);;
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.width=(int) (wm.getDefaultDisplay().getWidth()*0.8);
		params.height = LayoutParams.MATCH_PARENT;
		params.gravity=Gravity.RIGHT;
		getWindow().setAttributes(params);
		
		listview= (MispListViewAdaptScroll) findViewById(R.id.filter_refine_list);

		if(FilterDataCache.getInstance().getData().size()==0)
		{
			FilterDataCache.getInstance().initData(this);
		}
		mAdapter = new DistinctAdapter(this, FilterDataCache.getInstance().getData());
		listview.setAdapter(mAdapter);
		//listview.setAdapter(new DistinctAdapter(this, data));
		listview.setOnItemClickListener(this);
		
		//initSeekbar();
	}


	@Override
	public void initRes()
	{
		this.activityRes.setAvtivityView(R.layout.activity_filter_refine);
		this.activityRes.setName(getResources().getString(R.string.title_activity_filter_refine));

	}


	@Override
	public void saveOnClick(View v)
	{

		setResult(IntentCodeConst.RESULT_CODE_FILTER_SERACH);
		this.finish();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		FilterItemMeta selItem=(FilterItemMeta) parent.getAdapter().getItem(position);
		if(selItem!=null&&selItem.getItemType()==0)
		{
			//loadConditionList(selItem);
			Intent i = new Intent();
			i.setClass(this, FilterListActivity.class);
			i.putExtra(ListViewResInfo.SELECT_ITEM, selItem);
			startActivityForResult(i, IntentCodeConst.REQUEST_CODE);
		}

		
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==IntentCodeConst.RESULT_CODE_ITEM_CONFIRM)
		{
			
			List<FilterItemMeta> meta = FilterDataCache.getInstance().getData();

			mAdapter.notifyDataSetChanged();
		}
	}

	private void loadConditionList(FilterItemMeta selItem)
	{
		// TODO Auto-generated method stub
		
	}
}
