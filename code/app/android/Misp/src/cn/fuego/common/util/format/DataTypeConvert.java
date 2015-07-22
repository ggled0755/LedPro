/**   
* @Title: DataConvert.java 
* @Package cn.fuego.misp.util.format 
* @Description: TODO
* @author Tang Jun   
* @date 2014-6-17 上午11:42:16 
* @version V1.0   
*/ 
package cn.fuego.common.util.format;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.fuego.common.log.FuegoLog;

/** 
 * @ClassName: DataConvert 
 * @Description: TODO
 * @author Tang Jun
 * @date 2014-6-17 上午11:42:16 
 *  
 */

public class DataTypeConvert
{
	private static final FuegoLog log = FuegoLog.getLog(DataTypeConvert.class);

	
	public static List<String> intToStr(List<Integer> idList)
	{
		List<String> strList = new ArrayList<String>();
		for(Integer e : idList)
		{
			strList.add(String.valueOf(e));
		}
		return strList;
	}
	
	public static List<Integer> strToInt(List<String> idList)
	{
		List<Integer> strList = new ArrayList<Integer>();
		for(String e : idList)
		{
			strList.add(Integer.valueOf(e));
		}
		return strList;
	}

	public static byte[] strToGbkBytes(String str)
	{
		byte[] bytes = null;
		try
		{
			bytes = str.getBytes("gbk");
		} catch (UnsupportedEncodingException e)
		{
			log.error("convert error",e);
		}
		return bytes;
	}
	
	public static String gbkBytesToStr(byte[] bytes)
	{
		String isoString = null;
		try
		{
			isoString = new String(bytes, "gbk");
		} catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			log.error("convert error",e);
		}
		return isoString;
	}
	/**将单字节数组转换为字符串**/

	public static String bytesToStr(byte[] bytes)
	{
		String isoString = null;
		try
		{
			isoString = new String(bytes, "ISO-8859-1");
		} catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			log.error("convert error",e);
		}
		return isoString;
	}
	/**为了方便处理，将java字符串当做单字节处理，用于二进制通信**/

	public static byte[] strToBytes(String str)
	{
		byte[] bytes = null;
		try
		{
			bytes = str.getBytes("ISO-8859-1");
		} catch (UnsupportedEncodingException e)
		{
			log.error("convert error",e);
		}
		return bytes;
	}
	
 
	public static List objectToList(Object str)
	{
		List strList = new ArrayList<Object>();
		if(null != str)
		{
			strList.add(str);
		}
 
		return strList;
	}
	
	public static byte[] toAsciiBytes(String str)
	{
		byte[] bytes;
		if(null != str)
		{
			bytes = new byte[str.length()];
			for(int i=0;i<str.length();i++)
			{
				bytes[i]=(byte) str.charAt(i);
			}
 		}
		else
		{
			bytes = new byte[1];
		}
		return bytes;
	}
	public static List<String> toHexStringList(String str)
	{
		List<String> hexList = new ArrayList<String>();
		byte[] bytes = strToBytes(str);
		if(null != str)
		{
			for(int i=0;i<bytes.length;i++)
			{
				int temp = bytes[i] & 0xFF;
				hexList.add(Integer.toHexString(temp));
			}
		}
		return hexList;
	}
	public static Object convertStrToObject(String value,Class fieldClass)
	{
		 Object object = null;
		if(fieldClass == int.class || fieldClass == Integer.class)
		{
 			object = Integer.valueOf(value);
		}
		else if(fieldClass == long.class || fieldClass == Long.class)
		{
 			object = Long.valueOf(value);
		}
		else if(fieldClass == float.class || fieldClass == Float.class)
		{
			object = Float.valueOf(value);
		}
		else if(fieldClass == double.class || fieldClass == Double.class)
		{
			object = Double.valueOf(value);
		}
		else if(fieldClass == boolean.class || fieldClass == Boolean.class)
		{
			object = Boolean.valueOf(value);
		}
		else if(fieldClass == byte.class || fieldClass == Byte.class)
		{
			object = Byte.valueOf(value);
		}
		else if(fieldClass == byte[].class || fieldClass == Byte[].class)
		{
			object = BytesBuider.strToBytes(value);
		}
		else if(fieldClass == String.class )
		{
			object = value;
		}
		else if(fieldClass == Date.class)
		{
			object = DateUtil.stringToDate(value);
		}
		else
		{
			log.error("can not convert to right type.the type is " + fieldClass + "the value is " + value);
		}
		return object;
	}
	/**
	 * MD5加密，32位
	 * @param str
	 * @return
	 */

    public  static String MD5(String str) 
    {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
 
        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];
 
        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);
 
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
}
