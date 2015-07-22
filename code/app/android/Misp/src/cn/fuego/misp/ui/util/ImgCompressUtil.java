/**   
* @Title: ImgCompressUtil.java 
* @Package cn.fuego.misp.ui.util 
* @Description: TODO
* @author Aether
* @date 2015-4-1 上午10:18:05 
* @version V1.0   
*/ 
package cn.fuego.misp.ui.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.Gravity;
import android.widget.Toast;
import cn.fuego.common.util.validate.ValidatorUtil;

/** 
 * @ClassName: ImgCompressUtil 
 * @Description: TODO
 * @author Aether
 * @date 2015-4-1 上午10:18:05 
 *  
 */
public class ImgCompressUtil
{

	private static ImgCompressUtil instance;


	public synchronized static ImgCompressUtil getInstance()
	{
		if(null == instance)
		{
			instance = new ImgCompressUtil();
		}
		return instance;
	}
	public String copressImg(Context context,String oldPath,String targetPath)
	{
		Toast.makeText(context, "正在处理图片……", Toast.LENGTH_LONG).show();
		String localPath =null;
		if(!ValidatorUtil.isEmpty(oldPath))
		{
			Bitmap b =null;
			BitmapFactory.Options opts = new BitmapFactory.Options();
/*			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(oldPath, opts);
				             
			opts.inSampleSize = computeSampleSize(opts, -1, 128*128);
				//这里一定要将其设置回false，因为之前我们将其设置成了true      
*/			opts.inJustDecodeBounds = false;			
			b = BitmapFactory.decodeFile(oldPath, opts);
		    String name = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		    localPath = Environment.getExternalStorageDirectory().toString()+File.separator+targetPath+name+".jpg";
		    File myCaptureFile =new File(localPath);
			try {
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
				{
					if(!myCaptureFile.getParentFile().exists())
					{
						myCaptureFile.getParentFile().mkdirs();
					}
					//BufferedOutputStream baos;
					ByteArrayOutputStream baos = new ByteArrayOutputStream();  
					//baos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
					b.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
				    int options = 100;  
				        while ( baos.toByteArray().length / 1024>250) 
				        {  //循环判断如果压缩后图片是否大于300kb,大于继续压缩         
				             baos.reset();//重置baos即清空baos  
				            b.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中  
				            options -= 10;//每次都减少10  
				        } 
					//b.compress(Bitmap.CompressFormat.JPEG, 20, baos);
			        FileOutputStream fos = new FileOutputStream(myCaptureFile);
			        fos.write(baos.toByteArray());
			        fos.flush();
			        fos.close();
			        baos.flush();
			        baos.close();
				}
				else
				{
		        	
		        	 Toast toast= Toast.makeText(context, "保存失败，SD卡无效", Toast.LENGTH_SHORT);
		        	 toast.setGravity(Gravity.CENTER, 0, 0);
		        	 toast.show();
		        }
			} 
			catch (FileNotFoundException e) 
			{
					e.printStackTrace();
			} 
			catch (IOException e) 
			{
					e.printStackTrace();
			}

		}


		
		return localPath;
	}


	private static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) 
	{
		    int initialSize = computeInitialSampleSize(options, minSideLength,
		            maxNumOfPixels);
		 
		    int roundedSize;
		    if (initialSize <= 8) {
		        roundedSize = 1;
		        while (roundedSize < initialSize) {
		            roundedSize <<= 1;
		        }
		    } else {
		        roundedSize = (initialSize + 7) / 8 * 8;
		    }
		 
		    return roundedSize;
	}
		 
	private static int computeInitialSampleSize(BitmapFactory.Options options,
		        int minSideLength, int maxNumOfPixels) {
		    double w = options.outWidth;
		    double h = options.outHeight;
		 
		    int lowerBound = (maxNumOfPixels == -1) ? 1 :
		            (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
		    int upperBound = (minSideLength == -1) ? 128 :
		            (int) Math.min(Math.floor(w / minSideLength),
		            Math.floor(h / minSideLength));
		 
		    if (upperBound < lowerBound) {
		        return lowerBound;
		    }
		 
		    if ((maxNumOfPixels == -1) &&
		            (minSideLength == -1)) {
		        return 1;
		    } else if (minSideLength == -1) {
		        return lowerBound;
		    } else {
		        return upperBound;
		    }
		}
	
		
		
}
