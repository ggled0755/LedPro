package cn.fuego.common.util.list;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.fuego.common.log.FuegoLog;
import cn.fuego.common.util.validate.ValidatorUtil;

public class IteratorSelector
{
	public static FuegoLog log = FuegoLog.getLog(IteratorSelector.class);
	/**
	 * 从列表选择一唯一个属性名称为filedName,值为value 的元素，如果含有多个则返回第一个
	 * @param list
	 * @param filedName
	 * @param value
	 * @return 
	 * @return
	 * @throws Exception 
	 */
	
	public static<T> T findbyAttr(List<T> list,String filedName, Object value) 
	{
		if(ValidatorUtil.isEmpty(list)){
			return null;
		}
		Class<?> clazz=list.get(0).getClass();	
		//getAttr
		try
		{
			for(T t : list){
				Object attrValue=getAttrValue(t,filedName);
				if(attrValue.equals(value)){
					return t;
				}
			}	
		} catch (NoSuchMethodException e)
		{	
			//没有该属性
			log.error("NoSuchMethodException",e);
			return null;
		} catch (Exception e)
		{
			log.error(e.getMessage(),e);
			
			return null;
		}
		return null;
	} 
	
	/**
	 * 从一个列表里获取某一列
	 * @param list
	 * @param filedName
	 * @return List<Object>
	 */
	public static <T,E> List<E> selectColumn(List<T> list,String filedName){
		Class<?> clazz;
		List<E> result = new ArrayList<E>();
		//empty check
		if(list==null||list.isEmpty()){
			return result;
		}
		//getClass
		clazz=list.get(0).getClass();
	
		try
		{
			for(T t : list){
				
				Object attrValue=getAttrValue(t,filedName);			
				if(attrValue!=null){
					result.add((E)attrValue);
				}
			}
		} catch (NoSuchMethodException e)
		{
			//没有该属性
			log.error("NoSuchMethodException",e);
			return result;
		} catch (Exception e)
		{
			log.error(e.getMessage(),e);
			return result;
		}
		return result;
	}
	

	/**
	 * 迭代选择器
	 * 从list对象中选择fieldName为Value的元素，并组成一个新List
	 * @param list
	 * @param filedName
	 * @param Value
	 * @return
	 */
	public static <T> List<T> selectByField(List<T> list,String filedName, String value){
		Class<?> clazz;
		List<T> result = new ArrayList<T>();
		//empty check
		if(list==null||list.isEmpty()){
			return result;
		}
		//getClass
		clazz=list.get(0).getClass();
		//getAttr

		try
		{
			for(T t : list){
				Object attrValue=getAttrValue(t,filedName);
				String str = String.valueOf(attrValue);
				if(!str.isEmpty() && str.equals(value)){
					result.add(t);
				}
			}
		} catch (NoSuchMethodException e)
		{
			//没有该属性
			log.error("NoSuchMethodException",e);
			return list;
		} catch (Exception e)
		{
			log.error(e.getMessage(),e);
			return list;
		}
		return result;
	}
	
	/**
	 * 从一个列表里删除属性名为filedName,值为value 的元素，并返回删除后的列表
	 * @param list
	 * @param filedName
	 * @param value
	 * @return
	 */
	public static <T> List<T> deleteByField(List<T> list, String filedName,String value){
		Class<?> clazz;
		List<T> result = new ArrayList<T>();
		//empty check
		if(list==null||list.isEmpty()){
			return result;
		}
		//getClass
		clazz=list.get(0).getClass();
		//getAttr

		try
		{
			for(T t : list){
				Object attrValue=getAttrValue(t,filedName);
				String str = String.valueOf(attrValue);
				if(!str.equals(value)){
					result.add(t);
				}
			}
			return result;
		} catch (NoSuchMethodException e)
		{
			///没有该属性
			log.error("NoSuchMethodException",e);
			return list;
		} catch (Exception e)
		{
			log.error(e.getMessage(),e);
			return list;
		}
	}
	


	public static <T> Object getAttrValue(T t,String attrName) throws NoSuchMethodException, Exception{
		String getStr="get" + getMethodName(attrName);		
		Method m = (Method) t.getClass().getMethod(getStr);  
		Object obj = m.invoke(t);// 调用getter方法获取属性值  
        return obj;  
	}
	
	private static String getMethodName(String fildeName) throws Exception{  
        byte[] items = fildeName.getBytes();  
        items[0] = (byte) ((char) items[0] - 'a' + 'A');  
        return new String(items);  
    }

	private static Class<?> getAttrType(Object obj,String fieldName){
		HashMap<String,Class<?>> fieldHashMap=new HashMap<String,Class<?>>();
        Class<?> cls =obj.getClass();
		Field[] fieldlist = cls.getDeclaredFields();
        for (int i = 0; i < fieldlist.length; i++) {
             Field fld = fieldlist[i];  
             fieldHashMap.put(fld.getName(), fld.getType());
        }       
		return fieldHashMap.get(fieldName);	
	}
	
	

}
