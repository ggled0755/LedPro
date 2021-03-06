/**   
* @Title: FilterDataCache.java 
* @Package cn.fuego.led.ui.base 
* @Description: TODO
* @author Aether
* @date 2015-7-19 下午8:09:16 
* @version V1.0   
*/ 
package cn.fuego.led.ui.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.fuego.common.contanst.ConditionTypeEnum;
import cn.fuego.common.dao.QueryCondition;
import cn.fuego.common.util.validate.ValidatorUtil;
import cn.fuego.led.constant.FilterTypeEnum;
import cn.fuego.misp.webservice.up.model.base.EnumJson;
import cn.fuego.misp.webservice.up.model.base.TableMetaJson;

/** 
 * @ClassName: FilterDataCache 
 * @Description: TODO
 * @author Aether
 * @date 2015-7-19 下午8:09:16 
 *  
 */
public class FilterDataCache
{

	private List<QueryCondition> filterList = new ArrayList<QueryCondition>();

	//当前使用数据
	private List<TableMetaJson> data = new ArrayList<TableMetaJson>();
	private Map<Integer,TableMetaJson> defaultMeta = new HashMap<Integer, TableMetaJson>();
	
	private static FilterDataCache instance;

	public static final String GROUP_SEPRATOR="-";
	public synchronized static FilterDataCache getInstance()
	{
		if(null == instance)
		{
			instance = new FilterDataCache();
		}
		return instance;
		
	}
	
/*	//初始化数据
	public void initData(Context context)
	{

		data.clear();
		
		FilterItemMeta m1 = new FilterItemMeta();
		m1.setItemType(0);
		m1.setTitle(context.getResources().getString(R.string.btn_filter_categories));
		m1.setFieldName(FilterKeyConst.prodcut_category);
		data.add(m1);
		
		FilterItemMeta m2 = new FilterItemMeta();
		m2.setItemType(0);
		m2.setTitle(context.getResources().getString(R.string.btn_filter_location));
		m2.setFieldName(FilterKeyConst.use_location);
		data.add(m2);
		
		FilterItemMeta m3 = new FilterItemMeta();
		m3.setItemType(0);
		m3.setTitle(context.getResources().getString(R.string.btn_filter_mounting));
		m3.setFieldName(FilterKeyConst.mounting_base);
		data.add(m3);
		
		FilterItemMeta m4 = new FilterItemMeta();
		m4.setItemType(0);
		m4.setTitle(context.getResources().getString(R.string.btn_filter_Subcatego));
		m4.setFieldName(FilterKeyConst.subcatego);
		data.add(m4);
		
		FilterItemMeta m5 = new FilterItemMeta();
		m5.setItemType(0);
		m5.setTitle(context.getResources().getString(R.string.btn_filter_size));
		m5.setFieldName(FilterKeyConst.product_size);
		data.add(m5);

		FilterItemMeta m6 = new FilterItemMeta();
		m6.setItemType(1);
		m6.setTitle(context.getResources().getString(R.string.title_filter_lumen));
		m6.setFieldName(FilterKeyConst.lumen_output);
		m6.setMinValue("0");
		m6.setMaxValue("100");
		m6.setTheme(RangeSeekBar.THEME_RED);
		data.add(m6);
		
		FilterItemMeta m7 = new FilterItemMeta();
		m7.setItemType(1);
		m7.setTitle(context.getResources().getString(R.string.title_filter_watt));
		m7.setFieldName(FilterKeyConst.input_watt);
		m7.setMinValue("0");
		m7.setMaxValue("100");
		m7.setTheme(RangeSeekBar.THEME_ORANGE);
		data.add(m7);
	}*/
	public List<QueryCondition> getFilterList()
	{

		if(!ValidatorUtil.isEmpty(data))
		{
/*			if(!ValidatorUtil.isEmpty(filterList))
			{
				filterList.clear();
			}*/
			for(TableMetaJson meta:data)
			{
				if(meta.getField_type()==FilterTypeEnum.INTUT.getIntValue())
				{
					if(!ValidatorUtil.isEmpty(meta.getField_value()))
					{
						filterList.add(new QueryCondition(ConditionTypeEnum.INCLUDLE, meta.getField_name(), meta.getField_value()));
					}
					
				}
				else if(meta.getField_type()==FilterTypeEnum.SELECT.getIntValue())
				{
					if(!ValidatorUtil.isEmpty(meta.getField_value()))
					{
						filterList.add(new QueryCondition(ConditionTypeEnum.EQUAL, meta.getField_name(), meta.getField_enum_key()));
					}
					
				}
				else if(meta.getField_type()==FilterTypeEnum.GROUP.getIntValue())
				{
					if(!ValidatorUtil.isEmpty(meta.getField_value()))
					{
//						//特殊化处理
//						String[] values = meta.getField_value().split(GROUP_SEPRATOR);
//						if(values.length==2)
//						{
//							filterList.add(new QueryCondition(ConditionTypeEnum.EQUAL, "product_catg", values[0]));
//							filterList.add(new QueryCondition(ConditionTypeEnum.EQUAL, "sub_catg", values[1]));
//						}
						filterList.add(new QueryCondition(ConditionTypeEnum.EQUAL, meta.getField_name(), meta.getField_enum_key()));
					}
				}
				else if(meta.getField_type()==FilterTypeEnum.SEEK.getIntValue())
				{
					if(!ValidatorUtil.isEmpty(meta.getMin_value()))
					{
						filterList.add(new QueryCondition(ConditionTypeEnum.BIGGER_EQ, meta.getField_name(), meta.getMin_value()));
					}
					if(!ValidatorUtil.isEmpty(meta.getMax_value()))
					{
						filterList.add(new QueryCondition(ConditionTypeEnum.LOWER_EQ,meta.getField_name(), meta.getMax_value()));
					}
					
				}
			}
		}
		return filterList;
	}



	public List<TableMetaJson> getData()
	{
		return data;
	}


	public void setData(List<TableMetaJson> data)
	{
		if(!ValidatorUtil.isEmpty(this.data))
		{
			this.data.clear();
		}
		this.data.addAll(data);
	}

	public void update(String field_name, EnumJson item)
	{
		for(TableMetaJson m :data)
		{
			if(m.getField_name().equals(field_name))
			{
				m.setField_value(item.getEnum_value());
				m.setField_enum_key(item.getEnum_key());
				break;
			}
		}
		
	}

	public void updateOrderCondition(String fieldName, ConditionTypeEnum orderMethod)
	{
		if(!ValidatorUtil.isEmpty(fieldName)&&!ValidatorUtil.isEmpty(orderMethod.getTypeName()))
		{
			for(int i=0;i<this.filterList.size();i++)
			{
				if(filterList.get(i).getOperate()==ConditionTypeEnum.ASC_ORDER
						||filterList.get(i).getOperate()==ConditionTypeEnum.DESC_ORDER)
				{
					filterList.remove(i);
				}
			}
			for(int i=0;i<this.filterList.size();i++)
			{
				if(filterList.get(i).getAttrName().equals(fieldName))
				{
					filterList.get(i).setOperate(orderMethod);
					return;
				}
				
			}

			filterList.add(new QueryCondition(orderMethod, fieldName));
		}
		
	}

	public void update(TableMetaJson meta)
	{
		for(int i=0;i<data.size();i++)
		{
			if(data.get(i).getMeta_id()==meta.getMeta_id())
			{
				data.get(i).setMax_value(meta.getMax_value());
				data.get(i).setMin_value(meta.getMin_value());
				data.get(i).setField_enum_key(meta.getField_enum_key());
				data.get(i).setField_value(meta.getField_value());
				//data.add(meta);
				break;
			}
		}

	}

	public void updateDefaultMata(List<TableMetaJson> rdata)
	{
		if(this.defaultMeta.size()>0)
		{
			this.defaultMeta.clear();
		}
		for(TableMetaJson m:rdata)
		{
			defaultMeta.put(m.getMeta_id(), m);
		}
	}
	public Map<Integer, TableMetaJson> getDefaultMeta()
	{
		return defaultMeta;
	}

	public void setDefaultMeta(Map<Integer, TableMetaJson> defaultMeta)
	{
		this.defaultMeta = defaultMeta;
	}

	public void resetData()
	{
		if(!ValidatorUtil.isEmpty(filterList))
		{
			filterList.clear();
		}
		for(TableMetaJson m :data)
		{
			TableMetaJson r = defaultMeta.get(m.getMeta_id());
			m.setMax_value(r.getMax_value());
			m.setMin_value(r.getMin_value());
			m.setField_enum_key(r.getField_enum_key());
			m.setField_value(r.getField_value());
		}
	
	}

	
}
