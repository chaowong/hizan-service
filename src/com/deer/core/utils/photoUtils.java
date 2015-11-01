package com.deer.core.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.junit.Test;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class photoUtils {
	
	/*按照宽高进行压缩*/
	public static String uploadPhoto(File file,String saveFolder,HttpServletRequest request) throws Exception{
		Image image = ImageIO.read(file); // 读取文件
		int height = image.getHeight(null); // 获取图片的高度
		int width = image.getWidth(null); // 获取图片的宽度
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		bi.getGraphics().drawImage(image, 0, 0, width, height, null);
		long time = System.currentTimeMillis();
		String realPath;
		String savePath;
		String imagPath = request.getSession().getServletContext().getRealPath("/resource/" + saveFolder + "/") + "/";
		String path = ImgPathUtil.getHashcode(imagPath, time + "");
		realPath = imagPath + path + time + ".jpg";
		savePath = "/resource/" + saveFolder + "/" + path + time + ".jpg";

		File destFile = new File(realPath);
		FileOutputStream out = new FileOutputStream(destFile); // 输出到文件流
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		encoder.encode((BufferedImage) image); // JPEG编码
		file.delete();
		out.close();
		return savePath;
	}

	/**缩略图的生成(参数含义：num(0代表传过来的是发布心愿的图片;1代表传过来的是用户上传头像的图片;3代表的是用户主页上传的照片));maxLength:缩略图的最大宽度*/
	public static String zipImageFile(File f,int maxLength,int num,HttpServletRequest request) throws Exception{    
		Image image = ImageIO.read(f); // 读取文件
		int height = image.getHeight(null); // 获取图片的高度
		int width = image.getWidth(null); // 获取图片的宽度
		int new_w = 0;  
		int new_h = 0;  
		if (width > height) { 
			 new_w = maxLength; 
             new_h = (int) Math.round(height * ((float) maxLength / width));  
		}else{
			 new_w = (int) Math.round(width * ((float) maxLength / height)); 
             new_h = maxLength; 
		}
        /**宽,高设定 */    
    	BufferedImage tag = new BufferedImage(new_w, new_h,BufferedImage.TYPE_INT_RGB);        
        tag.getGraphics().drawImage(image, 0, 0, new_w, new_h, null);    
        /**压缩之后临时存放位置 */ 
        long time = System.currentTimeMillis();
        String realPath;
        String smallPath;
        if(num==0){
        	//保存的是发布心愿所有的照片
        	String imagPath = request.getSession().getServletContext().getRealPath("/resource/wishsmallphoto/")+"/";
        	String path = ImgPathUtil.getHashcode(imagPath,time+"");
        	realPath = imagPath+path+time+".jpg";
        	smallPath = "/resource/wishsmallphoto/"+path+time+".jpg";
        }else if(num==1){
        	//保存的是用户头像所有的照片
        	String imagPath = request.getSession().getServletContext().getRealPath("/resource/usersmallavatar/")+"/";
        	String path = ImgPathUtil.getHashcode(imagPath,time+"");
        	realPath = imagPath+path+time+".jpg";
        	smallPath = "/resource/usersmallavatar/"+path+time+".jpg";
        }else{
        	//保存的是用户主页所有的照片
        	String imagPath = request.getSession().getServletContext().getRealPath("/resource/usermainsmallphoto/")+"/";
        	String path = ImgPathUtil.getHashcode(imagPath,time+"");
        	realPath = imagPath+path+time+".jpg";
        	smallPath = "/resource/usermainsmallphoto/"+path+time+".jpg";
        }
        File destFile = new File(realPath);
        FileOutputStream out = new FileOutputStream(destFile); // 输出到文件流  
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);  
        encoder.encode((BufferedImage) tag); // JPEG编码 
        out.close(); 
        return smallPath;    
    }
	
	/**
	 * 根据宽度等比例获取图片
	 * @throws Exception
	 */
	public static void getimgwidth(String filepath,int new_w,FileOutputStream out) throws Exception{    
		File f = new File(filepath);
		Image image = ImageIO.read(f); // 读取文件
		double height = image.getHeight(null); // 获取图片的高度
		double width = image.getWidth(null); // 获取图片的宽度
		//int new_w = 300;
		double scale = (width-new_w)/width;
		double sh = height*scale;
		int new_h = (int) (height-sh);  
        /**宽,高设定 */    
    	BufferedImage tag = new BufferedImage(new_w, new_h,BufferedImage.TYPE_INT_RGB);        
        tag.getGraphics().drawImage(image, 0, 0, new_w, new_h, null);  
        
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out); 
        encoder.encode((BufferedImage) tag); // JPEG编码 
        out.close(); 
    }
	@Test
	public void zipImageFile() throws Exception{    
		File f = new File("E:\\生活\\20150317140310.jpg");
		Image image = ImageIO.read(f); // 读取文件
		double height = image.getHeight(null); // 获取图片的高度
		double width = image.getWidth(null); // 获取图片的宽度
		int new_w = 700;
		double scale = (width-new_w)/width;
		double sh = height*scale;
		int new_h = (int) (height-sh);  
		/**宽,高设定 */    
		BufferedImage tag = new BufferedImage(new_w, new_h,BufferedImage.TYPE_INT_RGB);        
		tag.getGraphics().drawImage(image, 0, 0, new_w, new_h, null);    
		/**压缩之后临时存放位置 */ 
		long time = System.currentTimeMillis();
		//保存的是发布心愿所有的照片
		String imagPath ="d:/";
		String path = ImgPathUtil.getHashcode(imagPath,time+"");
		String realPath = imagPath+path+time+".jpg";
		File destFile = new File(realPath);
		FileOutputStream out = new FileOutputStream(destFile); // 输出到文件流  
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out); 
		encoder.encode((BufferedImage) tag); // JPEG编码 
		out.close(); 
	}
}
