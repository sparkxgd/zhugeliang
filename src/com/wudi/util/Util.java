package com.wudi.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.alibaba.druid.util.Base64;
import com.mysql.fabric.xmlrpc.base.Data;

public class Util {
	public final static String Cookie_NAME="cname";
    /**
     * 从输入流中获取字节数组
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {  
        byte[] buffer = new byte[1024];  
        int len = 0;  
        ByteArrayOutputStream bos = new ByteArrayOutputStream();  
        while((len = inputStream.read(buffer)) != -1) {  
            bos.write(buffer, 0, len);  
        }  
        bos.close();  
        return bos.toByteArray();  
    } 
	public static boolean isBlankOrEmpty(String string){
		return string==null || string.trim().length() == 0;
	}
	public static String getId() {
		Long t=new Date().getTime();
		Random ra =new Random();
		int a=ra.nextInt(10000);
		return t.toString()+a;
	}
	/**
	 * 获取当前时间
	 * @return
	 */
	public static String getCurrentTime(){
		DateFormat bf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
		Date date = new Date();
        String format = bf.format(date);
		return format;
	}
	
	/**
     * @Description： 图片转化成base64字符串
     * @param:    path
     * @Return:
     */
    public static String GetImageStr(String path)
    {
        //将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        //待处理的图片
        String imgFile = path;
        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try
        {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        //对字节数组Base64编码
        String  base=Base64.byteArrayToBase64(data);  
        //返回Base64编码过的字节数组字符串
        return base;
    }
    

	
	
	public static int getWeek() {
		int week = 1;
		String beginTime = "2019-02-24 00:00:00";

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		long curMillisecond = System.currentTimeMillis();

		try {

			Date date1 = format.parse(beginTime);
			long beginMillisecond = date1.getTime();
			long endMillisecond = beginMillisecond + 604800000;

			for (int i = 1; i <= 19; i++) {
				if (curMillisecond >= beginMillisecond && curMillisecond <= endMillisecond) {
					week = i;
					break;
				}

				beginMillisecond = endMillisecond + 1;
				endMillisecond = beginMillisecond + 604800000;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return week;

	}
	public static void main(String[] args) {
		String u=System.getProperty("user.dir");
		String url=u+"\\WebContent\\upload\\123.jpg";
	    String image =Util.GetImageStr(url);
	    System.out.println(image);
	}
	
}
