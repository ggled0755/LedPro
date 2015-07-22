/**   
* @Title: StrUtil.java 
* @Package cn.fuego.misp.ui.util 
* @Description: TODO
* @author Aether
* @date 2015-3-17 下午5:16:25 
* @version V1.0   
*/ 
package cn.fuego.misp.ui.util;

import java.security.MessageDigest;

import android.widget.EditText;
import cn.fuego.common.util.validate.ValidatorUtil;

/** 
 * @ClassName: StrUtil 
 * @Description: TODO
 * @author Aether
 * @date 2015-3-17 下午5:16:25 
 *  
 */
public class StrUtil
{

	/**
	 * 处理字符串为空时页面显示null的问题
	 * @param str
	 * @return
	 */
	public static String noNullStr(String str)
	{
		String result;
		if(str!=null&&!ValidatorUtil.isEmpty(str))
		{
			result=str;
		}
		else
		{
			result="";
		}
		return result;
		
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
    /**
     * 取无空格的文字
     * @param text
     * @return
     */
    public static String getTrimText(EditText text)
    {
		String txt =text.getText().toString().trim();
		
    	return txt;
    	
    }
}
