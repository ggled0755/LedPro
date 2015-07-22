/**   
 * @Title: MispHttpClient.java 
 * @Package cn.fuego.misp.service.http 
 * @Description: TODO
 * @author Tang Jun   
 * @date 2015-3-27 下午6:46:07 
 * @version V1.0   
 */
package cn.fuego.misp.service.http;

import java.io.IOException;
import java.net.URLDecoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import cn.fuego.common.log.FuegoLog;

/**
 * @ClassName: MispHttpClient
 * @Description: TODO
 * @author Tang Jun
 * @date 2015-3-27 下午6:46:07
 * 
 */
public class MispHttpClient
{
	private static FuegoLog log = FuegoLog.getLog(MispHttpClient.class);

	private static final String CODE_WITH_UTF_8 = "utf-8";
	public static String httpPost(String url, String req)
	{
		// post请求返回结果
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost method = new HttpPost(url);
		String rsp = null;
		;
		try
		{
			if (null != req)
			{
				// 解决中文乱码问题
				StringEntity entity = new StringEntity(req, CODE_WITH_UTF_8);
				entity.setContentEncoding(CODE_WITH_UTF_8);
				// entity.setContentType("application/json");
				method.setEntity(entity);
			}
			HttpResponse result = httpClient.execute(method);
			url = URLDecoder.decode(url, CODE_WITH_UTF_8);
			/** 请求发送成功，并得到响应 **/
			if (result.getStatusLine().getStatusCode() == 200)
			{
				try
				{
					/** 读取服务器返回过来的json字符串数据 **/
					rsp = EntityUtils.toString(result.getEntity(),CODE_WITH_UTF_8);

					/** 把json字符串转换s成json对象 **/
				} catch (Exception e)
				{
					log.error("post请求提交失败:" + url, e);
				}
			}
		} catch (IOException e)
		{
			log.error("post请求提交失败:" + url, e);
		}
		return rsp;
	}
}
