/**   
* @Title: DistinctAdapter.java 
* @Package cn.fuego.led.ui.filter 
* @Description: TODO
* @author Aether
* @date 2015-7-19 下午10:03:29 
* @version V1.0   
*/ 
package cn.fuego.led.ui.filter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.fuego.common.util.validate.ValidatorUtil;
import cn.fuego.led.R;
import cn.fuego.led.ui.widget.RangeSeekBar;
import cn.fuego.led.ui.widget.RangeSeekBar.OnRangeSeekBarChangeListener;

/** 
 * @ClassName: DistinctAdapter 
 * @Description: TODO
 * @author Aether
 * @date 2015-7-19 下午10:03:29 
 *  
 */
public class DistinctAdapter extends BaseAdapter
{
	private Context mContext;
	private List<FilterItemMeta> dataList =new ArrayList<FilterItemMeta>();
	private final int type_btn=0,type_seekbar=1;
	private final int type_count =2;
	private LayoutInflater layout;
	public Typeface ttf_cabin_semibold;
	public Typeface ttf_cabin_regular;
	public DistinctAdapter(Context context,List<FilterItemMeta> data)
	{
		mContext=context;
		dataList=data;
		layout = LayoutInflater.from(context);
		ttf_cabin_semibold = Typeface.createFromAsset(context.getAssets(), "fonts/Cabin-SemiBold.ttf");
		ttf_cabin_regular = Typeface.createFromAsset(context.getAssets(), "fonts/Cabin-Regular.otf");
	}
	
	@Override
	public int getItemViewType(int position)
	{

		return dataList.get(position).getItemType();
	}

	@Override
	public int getViewTypeCount()
	{
		// TODO Auto-generated method stub
		return type_count;
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return dataList.size();
	}

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		BtnViewHolder vh_btn=null;
		SeekbarViewHolder vh_seek =null;
		int type=getItemViewType(position);
		if(convertView==null)
		{
			switch (type)
			{
			case type_btn:
				vh_btn = new BtnViewHolder();
				convertView = layout.inflate(R.layout.list_item_btn, null);
				vh_btn.title = (TextView) convertView.findViewById(R.id.item_btn_title);
				vh_btn.content= (TextView) convertView.findViewById(R.id.item_btn_value);
				convertView.setTag(vh_btn);
				break;
			case type_seekbar:
				vh_seek = new SeekbarViewHolder();
				convertView = layout.inflate(R.layout.list_item_seekbar, null);
				vh_seek.title =(TextView) convertView.findViewById(R.id.item_seekbar_title);
				vh_seek.minValue = (TextView) convertView.findViewById(R.id.item_seekbar_minvalue);
				vh_seek.maxValue = (TextView) convertView.findViewById(R.id.item_seekbar_maxvalue);
				vh_seek.seekRoot = (ViewGroup) convertView.findViewById(R.id.item_seekbar_root);
				convertView.setTag(vh_seek);
				break;
			default:
				break;
			}
		}
		else
		{
			switch (type)
			{
			case type_btn:
				vh_btn = (BtnViewHolder) convertView.getTag();			
				break;
			case type_seekbar:
				vh_seek = (SeekbarViewHolder) convertView.getTag();	
				break;
			default:
				break;
			}
		}
		switch (type)
		{
		case type_btn:
			vh_btn.title.setText(dataList.get(position).getTitle());
			vh_btn.title.setTypeface(ttf_cabin_regular);
			vh_btn.content.setTypeface(ttf_cabin_regular);
			if(ValidatorUtil.isEmpty(dataList.get(position).getContent()))
			{
				
				vh_btn.content.setText(this.mContext.getString(R.string.title_filter_all));
			}
			else
			{
				vh_btn.content.setText(dataList.get(position).getContent());
			}
			break;
		case type_seekbar:
			vh_seek.title.setText(dataList.get(position).getTitle());
			vh_seek.title.setTypeface(ttf_cabin_regular);

			vh_seek.minValue.setText(dataList.get(position).getMinValue());
			vh_seek.maxValue.setText(dataList.get(position).getMaxValue());
			final RangeSeekBar<Integer> seekbar =new RangeSeekBar<Integer>(0, 100, mContext, dataList.get(position).getTheme());
			seekbar.setSelectedMinValue(Integer.valueOf(dataList.get(position).getMinValue()));
			seekbar.setSelectedMaxValue(Integer.valueOf(dataList.get(position).getMaxValue()));

			vh_seek.seekRoot.removeAllViews();
			vh_seek.seekRoot.addView(seekbar);
			seekbar.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Integer>()
			{

				@Override
				public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar,
						Integer minValue, Integer maxValue)
				{
					dataList.get(position).setMinValue(String.valueOf(minValue));
					dataList.get(position).setMaxValue(String.valueOf(maxValue));
					
					notifyDataSetChanged();
				}
			});
			break;
		default:
			break;
		}
		return convertView;
	}
	
	static class BtnViewHolder{
		TextView title;
		TextView content;
	}
	
	static class SeekbarViewHolder{
		TextView title;
		TextView minValue;
		TextView maxValue;
		ViewGroup seekRoot;

	}

	

}
