/**   
* @Title: JsonConvert.java 
* @Package cn.fuego.common.util.format 
* @Description: TODO
* @author Tang Jun   
* @date 2014-12-4 上午12:51:02 
* @version V1.0   
*/ 
package cn.fuego.common.util.format;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.MapperConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import cn.fuego.common.log.FuegoLog;

 /** 
 * @ClassName: JsonConvert 
 * @Description: TODO
 * @author Tang Jun
 * @date 2014-12-4 上午12:51:02 
 *  
 */
public class JsonConvert
{
	private static final FuegoLog log = FuegoLog.getLog(DataTypeConvert.class);

	private static final boolean  isIgnore = true;
	public static String ObjectToJson(Object object)
	{
		ObjectMapper mapper = new ObjectMapper();
		
		 String json = "";
		try
		{
			json = mapper.writeValueAsString(object);
		} catch (Exception e)
		{
			log.error("object to json failed",e);
		}
		return json;
	}
	
	public static <T> T jsonToObject(String json,TypeReference<?> type)
	{
		ObjectMapper mapper = null;
		if(isIgnore)
		{
			mapper = new ObjectMapper().configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		}
		else
		{
			mapper = new ObjectMapper();
		}
  
 		T	rspObj = null;
		try
		{
			rspObj = mapper.readValue(json,type);
 			
		} catch (Exception e)
		{
			log.error("json to object failed",e);
		}
		return (T) rspObj;
	}
	public static <T> T jsonToObject(String json,Class<T> clazz)
	{
		ObjectMapper mapper = null;
		if(isIgnore)
		{
			mapper = new ObjectMapper().configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		}
		else
		{
			mapper = new ObjectMapper();
		}
		T	rspObj = null;
		try
		{
			rspObj = mapper.readValue(json,clazz);
			
		} catch (Exception e)
		{
			log.error("json to object failed",e);
		}
		return (T) rspObj;
	}
}
