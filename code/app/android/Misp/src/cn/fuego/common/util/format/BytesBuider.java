/**   
* @Title: BytesBuider.java 
* @Package cn.fuego.common.util.format 
* @Description: TODO
* @author Tang Jun   
* @date 2015-5-23 下午6:44:02 
* @version V1.0   
*/ 
package cn.fuego.common.util.format;

import java.io.UnsupportedEncodingException;

import cn.fuego.common.log.FuegoLog;

 /** 
 * @ClassName: BytesBuider 
 * @Description: TODO
 * @author Tang Jun
 * @date 2015-5-23 下午6:44:02 
 *  
 */
public class BytesBuider
{
	private static final FuegoLog log = FuegoLog.getLog(DataTypeConvert.class);

	public static byte[] add(byte[] bytes1,byte[] bytes2)
	{
		byte[] result = new byte[bytes1.length + bytes2.length];
		System.arraycopy(bytes1, 0, result, 0, bytes1.length);
		System.arraycopy(bytes2, 0, result, bytes1.length, bytes2.length);
		return result;

	}
	
	public static byte[] longToBytesr(long value)
	{
		 return  longToBytes(value,4);
	}
	public static  byte[] longToBytes(long value,int bit)
	{
 
		 byte[] src = new byte[bit];  
		
		 for(int i=0;i<bit;i++)
		 {     
			    src[bit-1-i] =  (byte) (value>>(i*8) & 0xFF);   
		 }
  
		 
		 return src;
	}
	
	public static  byte[] intToBytes(int value)
	{
		 return  intToBytes(value,4);
	}
	
	public static  byte[] intToBytes(int value,int bit)
	{
		 if(bit > 4)
		 {
			 bit = 4;
		 }
		 byte[] src = new byte[bit];  
		
		 for(int i=0;i<bit;i++)
		 {     
			    src[bit-1-i] =  (byte) (value>>(i*8) & 0xFF);   
		 }
  
		 
		 return src;
	}
	
	public static  byte[] floatToBytes(float value)
	{ 
		int fbit = Float.floatToIntBits(value);  
 	   
	    return intToBytes(fbit);
		 
	}
	
	public static byte[] strToBytes(String str)
	{
		
		return strToBytes(str,str.length());
	}
	public static byte[] strToBytes(String str,int length)
	{
		byte[] bytes = null;
		try
		{
			String temp = str.substring(0,length);
			bytes = temp.getBytes("ISO-8859-1");
		} catch (UnsupportedEncodingException e)
		{
			log.error("convert error",e);
		}
		return bytes;
	}
	
	
	public static int getIntValue(byte[] dataBytes,int startIndex,int endIndex)
	{
		int byteValue = 1;
		int intValue = 0;
		for (int i = endIndex; i >= startIndex; i--)
		{
			intValue += getUnsignedInt(dataBytes[i]) * byteValue;
			byteValue *= 256;
		}
		return intValue;
	}
	public static int getUnsignedInt(byte by)
	{
		return by&0xff;  
	}
	
	public static long getLongValue(byte[] dataBytes,int index)
	{
		int byteValue = 1;
		long intValue = 0;
		for (int i = index; i >= index; i--)
		{
			intValue += getUnsignedInt(dataBytes[i]) * byteValue;
			byteValue *= 256;
		}
		return intValue;
	}
	public static long getLongValue(byte[] dataBytes,int startIndex,int endIndex)
	{
		long byteValue = 1;
		long intValue = 0;
		for (int i = endIndex; i >= startIndex; i--)
		{
			intValue += getUnsignedInt(dataBytes[i]) * byteValue;
			byteValue *= 256;
		}
		return intValue;
	}
	public static int getIntValue(byte[] dataBytes,int index)
	{
		int byteValue = 1;
		int intValue = 0;
		for (int i = index; i >= index; i--)
		{
			intValue += getUnsignedInt(dataBytes[i]) * byteValue;
			byteValue *= 256;
		}
		return intValue;
	}
 
	
	public static byte[] getDataBytes(byte[] dataBytes,int startIndex,int endIndex)
	{
		if(endIndex>startIndex)
		{
			byte[] bytes = new byte[endIndex-startIndex+1];
			
			for(int i=0;i<=endIndex-startIndex;i++)
			{
				if(i+startIndex < dataBytes.length)
				{
					bytes[i] = dataBytes[i+startIndex];

				}
			}
			return bytes;

		}
		else
		{
			return new byte[0];
		}
	}
	
	public static float getFloatValue(byte[] dataBytes,int startIndex,int endIndex)
	{
		float f2 = 0f;

		int i = getIntValue(dataBytes,startIndex,endIndex);
		f2 = Float.intBitsToFloat(i);
		if(Float.isNaN(f2))
		{
			f2=0.0f;
		}
		
		return f2;

	}
	
	public static String getStrValue(byte[] dataBytes, int startIndex,int endIndex)
	{
		int length = endIndex-startIndex + 1;
		byte[] temp = new byte[length];
		for(int i=0;i<length;i++)
		{
			temp[i] = dataBytes[startIndex+i];
 		}
 		return DataTypeConvert.gbkBytesToStr(temp);
	}
}
