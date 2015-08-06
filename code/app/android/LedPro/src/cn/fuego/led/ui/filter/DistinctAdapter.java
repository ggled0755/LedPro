/**   
* @Title: DistinctAdapter.java 
* @Package cn.fuego.led.ui.filter 
* @Description: TODO
* @author Aether
* @date 2015-7-19 下午10:03:29 
* @version V1.0   
*/ 
package cn.fuego.led.ui.filter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import cn.fuego.common.util.validate.ValidatorUtil;
import cn.fuego.led.R;
import cn.fuego.led.constant.FilterTypeEnum;
import cn.fuego.led.constant.SeekColorEnum;
import cn.fuego.led.ui.widget.RangeSeekBar;
import cn.fuego.led.ui.widget.RangeSeekBar.OnRangeSeekBarChangeListener;
import cn.fuego.misp.ui.util.StrUtil;
import cn.fuego.misp.webservice.up.model.base.TableMetaJson;

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
	private List<TableMetaJson> dataList =new ArrayList<TableMetaJson>();
	//private final int type_btn=0,type_seekbar=1;
	private final int type_count =4;
	private LayoutInflater layout;
	public Typeface ttf_cabin_semibold;
	public Typeface ttf_cabin_regular;
	public DistinctAdapter(Context context,List<TableMetaJson> data)
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

		return dataList.get(position).getField_type();
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
		InputViewHolder vh_input =null;
		int type=getItemViewType(position);
		
		if(convertView==null)
		{
			switch (FilterTypeEnum.getEnumByInt(type))
			{
			case INTUT:
				vh_input = new InputViewHolder();
				convertView = layout.inflate(R.layout.list_item_input, null);
				vh_input.title=(EditText) convertView.findViewById(R.id.item_input_text);
				convertView.setTag(vh_input);
				break;
			case SELECT:
				vh_btn = new BtnViewHolder();
				convertView = layout.inflate(R.layout.list_item_btn, null);
				vh_btn.title = (TextView) convertView.findViewById(R.id.item_btn_title);
				vh_btn.content= (TextView) convertView.findViewById(R.id.item_btn_value);
				convertView.setTag(vh_btn);				
				break;
			case SEEK:
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
			switch (FilterTypeEnum.getEnumByInt(type))
			{
			case INTUT:
				vh_input = (InputViewHolder) convertView.getTag();
				break;
			case SELECT:
				vh_btn = (BtnViewHolder) convertView.getTag();			
				break;
			case SEEK:
				vh_seek = (SeekbarViewHolder) convertView.getTag();	
				break;
			default:
				break;
			}
		}
		switch (FilterTypeEnum.getEnumByInt(type))
		{
		case INTUT:
			vh_input.title.setText(StrUtil.noNullStr(dataList.get(position).getField_value()));
			vh_input.title.setHint(dataList.get(position).getLabel_name());
			vh_input.title.addTextChangedListener(new TextWatcher()
			{
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count)
				{					
					if(!ValidatorUtil.isEmpty(s.toString()))
					{
						dataList.get(position).setField_value(s.toString());
						FilterDataCache.getInstance().update(dataList.get(position));
					}
					
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after)
				{
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void afterTextChanged(Editable s)
				{
					// TODO Auto-generated method stub
					
				}
			});
			break;
		case SELECT:
			vh_btn.title.setText(dataList.get(position).getLabel_name());
			vh_btn.title.setTypeface(ttf_cabin_regular);
			vh_btn.content.setTypeface(ttf_cabin_regular);
			if(ValidatorUtil.isEmpty(dataList.get(position).getField_value()))
			{
				
				vh_btn.content.setText(dataList.get(position).getDefault_value());
			}
			else
			{
				vh_btn.content.setText(dataList.get(position).getField_value());
			}
			break;
		case SEEK:
			vh_seek.title.setText(dataList.get(position).getLabel_name());
			vh_seek.title.setTypeface(ttf_cabin_regular);

			int theme = SeekColorEnum.getEnumByStr(dataList.get(position).getStyle_color()).getIntValue();
			float def_min = Float.valueOf(dataList.get(position).getMin_value());
			float def_max = Float.valueOf(dataList.get(position).getMax_value());
//			def_min=(float)(Math.round(def_min*100)/100);
//			def_max = (float)(Math.round(def_max*100)/100);
			BigDecimal   b1 = new  BigDecimal(def_min);
			float   f1   =   b1.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
			BigDecimal   b2 = new  BigDecimal(def_max);
			float   f2   =   b2.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
			
			if(null==vh_seek.seekbar)
			{
				vh_seek.seekbar = new RangeSeekBar<Float>(f1,f2, mContext, theme);				
				vh_seek.seekRoot.addView(vh_seek.seekbar);
			}
			vh_seek.minValue.setText(String.valueOf(f1));
			vh_seek.maxValue.setText(String.valueOf(f2));
			vh_seek.seekbar.setSelectedMinValue(f1);
			vh_seek.seekbar.setSelectedMaxValue(f2);

			//vh_seek.seekRoot.removeAllViews();
			//vh_seek.seekRoot.addView(seekbar);
			vh_seek.seekbar.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Float>()
			{


				@Override
				public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar,
						Float minValue, Float maxValue)
				{

					dataList.get(position).setMin_value(String.valueOf(minValue));
					dataList.get(position).setMax_value(String.valueOf(maxValue));
					FilterDataCache.getInstance().update(dataList.get(position));
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
		RangeSeekBar<Float> seekbar;
	}

	static class InputViewHolder{
		EditText title;

	}

	public List<TableMetaJson> getDataList()
	{
		return dataList;
	}

	public void setDataList(List<TableMetaJson> dataList)
	{
		this.dataList = dataList;
	}	

}
