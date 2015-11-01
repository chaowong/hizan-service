package com.deer.core.utils;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;

import com.deer.core.utils.trace.Trace;
import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 对上传图片的操作
 * 
 * @author lenovo
 * 
 */
public class ZipImageUtils {

	/* 按照宽高进行压缩 */
	/**
	 * 
	 * @param f
	 * @param w
	 * @param h
	 * @param freeid
	 *            记录的id用来调用pathResource时计算文件上传的真实目录的
	 * @param request
	 * @param uploadFile
	 *            上传路径
	 * @return fileName 文件名
	 * 
	 * @throws Exception
	 */
	public static String zipImage(File f, int w, int h,
			HttpServletRequest request, String uploadFile, String freeid)
			throws Exception {
		// 根据freeid计算文件的上级目录 15/4
		String pathResource = pathResource(freeid);
		BufferedImage image = ImageIO.read(f); // 读取文件

		int height = image.getHeight(null); // 获取图片的高度
		int width = image.getWidth(null); // 获取图片的宽度

		BufferedImage bi = new BufferedImage(image.getWidth(),
				image.getHeight(), BufferedImage.TYPE_INT_RGB);
		bi.getGraphics().drawImage(image, 0, 0, image.getWidth(),
				image.getHeight(), null);
		long time = System.currentTimeMillis();
		// String realPath =
		// request.getSession().getServletContext().getRealPath("/resource/")+"/"+uploadFile+"/"+pathResource+"/";
		String realPath = "E:/data1/upload" + "/" + uploadFile + "/"
				+ pathResource + "/";

		String fileName = time + ".jpg";
		File destFile = new File(realPath);
		if (!destFile.exists()) {
			destFile.mkdirs();
		}
		File file = new File(realPath + fileName);
		FileOutputStream out = new FileOutputStream(file); // 输出到文件流
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		encoder.encode(bi); // JPEG编码
		out.close();
		// zipImageFile(file, 150, request, uploadFile, freeid, fileName);
		f.delete();

		return fileName;
	}
	
	public static String zipImage(File f, HttpServletRequest request) throws Exception
	  {
	    Image image = ImageIO.read(f);
	    int height = image.getHeight(null);
	    int width = image.getWidth(null);
	    BufferedImage bi = new BufferedImage(width, height, 1);
	    bi.getGraphics().drawImage(image, 0, 0, width, height, null);
	    //long time = System.currentTimeMillis();

	    String fileName=UUID.randomUUID().toString();
	    String imagPath = request.getSession().getServletContext().getRealPath("/resource/webimg/") + "/";
	    String path = ImgPathUtil.getHashcode(imagPath, fileName+"");
	    String realPath = imagPath + path + fileName + ".jpg";
	    String savePath = "resource/webimg/" + path + fileName + ".jpg";
	    File destFile = new File(realPath);
	    FileOutputStream out = new FileOutputStream(destFile);
	    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
	    encoder.encode((BufferedImage)image);
	    f.delete();
	    out.close();
	    return savePath;
	  }

	/* 缩略图的生成 */
	/**
	 * 
	 * @param f
	 *            客户端上传的文件
	 * @param maxLength
	 *            设置缩略图最大宽度
	 * @param fileName
	 *            原图的文件名
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static String zipImageFile(int maxLength,
			HttpServletRequest request, String uploadFile, String freeid,
			String fileName) throws Exception {
		// 根据freeid计算文件的上级目录 15/4
		String pathResource = pathResource(freeid); // 缩略图的路径
													// 10/1由于是同一个freeid所以和原图的路径是相同的
		/*
		 * String realPath =
		 * request.getSession().getServletContext().getRealPath
		 * ("/resource/")+"/"+uploadFile+"/"+pathResource+"/";
		 */
		String realPath = "E:/data1/upload" + "/" + uploadFile + "/"
				+ pathResource + "/";
		Image image = ImageIO.read(new File(realPath + fileName)); // 读取新上传的原文件文件

		int height = image.getHeight(null); // 获取图片的高度
		int width = image.getWidth(null); // 获取图片的宽度
		int new_w = 0;
		int new_h = 0;
		if (width > height) {
			new_w = maxLength;
			new_h = Math.round(height * ((float) maxLength / width));
		} else {
			new_w = Math.round(width * ((float) maxLength / height));
			new_h = maxLength;
		}
		/** 宽,高设定 */
		BufferedImage tag = new BufferedImage(new_w, new_h,
				BufferedImage.TYPE_INT_RGB);
		tag.getGraphics().drawImage(image, 0, 0, new_w, new_h, null);

		fileName = fileName.substring(0, fileName.lastIndexOf("."))
				+ "small.jpg";
		/** 压缩之后临时存放位置 */
		File destFile = new File(realPath + fileName);
		FileOutputStream out2 = new FileOutputStream(destFile); // 输出到文件流
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out2);
		encoder.encode(tag); // JPEG编码
		// f2.delete();
		out2.close();
		return fileName;
	}

	/**
	 * 根据id计算上传目录 id为uuid
	 * 
	 * @param freeid
	 *            传入的id
	 * @return
	 */
	public static String pathResource(String freeid) {
		String uuidFileName = freeid;// ae427cfa-4920-418a-992a-59553e54a302
		// 生成两级目录
		int hashCode = uuidFileName.hashCode();
		int dir1 = hashCode & 0xf;// 一级目录
		int dir2 = (hashCode & 0xf0) >> 4;// 二级目录
		/*
		 * int dir3 = (hashCode & 0xf0) >> 8;// 二级目录
		 */String newPath = dir1 + "/" + dir2;// 共有256目录
		String directoryPath = newPath;
		return directoryPath;
	}

	/**
	 * 用流的方式将图片写到客户端
	 * 
	 * @param filepath
	 *            文件路径
	 * @param new_w
	 *            新宽度
	 * @param servletOutputStream
	 *            输出流 response.getOutputStream()表示写入到客户端
	 * @throws Exception
	 */
	public static void getimgwidth(String filepath, int new_w, int new_height,
			ServletOutputStream servletOutputStream) throws Exception {
		File f = new File(filepath);
		Image image = ImageIO.read(f); // 读取文件
		double height = image.getHeight(null); // 获取图片的高度
		double width = image.getWidth(null); // 获取图片的宽度
		// int new_w = 300;
		double scale = (width - new_w) / width;
		double sh = height * scale;
		int new_h = (int) (height - sh);
		BufferedImage tag = null;
		/** 宽,高设定 */
		if (new_w == 0) {
			tag = new BufferedImage((int) width, (int) height,
					BufferedImage.TYPE_INT_RGB);
			tag.getGraphics().drawImage(image, 0, 0, (int) width, (int) height,
					null);
		} else {
			tag = new BufferedImage(new_w, new_h, BufferedImage.TYPE_INT_RGB);
			tag.getGraphics().drawImage(image, 0, 0, new_w, new_h, null);
		}
		JPEGImageEncoder encoder = JPEGCodec
				.createJPEGEncoder(servletOutputStream);
		encoder.encode((BufferedImage) tag); // JPEG编码
		servletOutputStream.close();
	}

	/**
	 * 手机端请求图片时对图片进行按比例切割后在压缩成手机端需要的比例
	 * @param src 原图路径
	 * @param w   客户端需求图片宽度
	 * @param h   客户端需求图片高度
	 * @param servletOutputStream  输出流
	 * @throws Exception
	 */
	public static void readUsingImageReader(String src,double w,double h,ServletOutputStream servletOutputStream) throws Exception {
		File f = new File(src);
		Image image = ImageIO.read(f); // 读取文件
		double height = image.getHeight(null); // 获取图片的高度
		double width = image.getWidth(null); // 获取图片的宽度
		BufferedImage tag = null;
		if ((int)w != 0 && (int)h != 0) {

			double ybl = width / height; // 图片比例
			double fbl = w / h; // 客户端传过来的比例
			int x = 0; // x轴
			int y = 0; // y轴
			/*
			 * int qw=0; //切割后图片的width int qh=0; //切割后图片的heigth
			 */if (ybl > fbl) {
				// 计算x轴开始点
				// 取原图片的width的中心点
				int z = (int) (width / 2);
				// 如果原比例大于客户端比例则计算重新计算width，height不变
				width = height * fbl;
				// 取截取图片的宽度的一半并用计算出图片在x轴的中心点的值减去该值定位位x坐标
				x = (int) (z - width / 2);
			} else if (ybl < fbl) {
				// 计算y轴开始点
				// 取原图的高度的一半定位图片在y轴的中心点
				int z = (int) (height / 2);
				// 如果原比例小于客户端比例则重新计算height,width不变
				height = width / fbl;
				// 计算y轴
				y = (int) (z - height / 2);
			}
			// 取得图片读入器
			Iterator readers = ImageIO.getImageReadersByFormatName("jpg");
			ImageReader reader = (ImageReader) readers.next();
			// 取得图片读入流
			InputStream source = new FileInputStream(f);
			ImageInputStream iis = ImageIO.createImageInputStream(source);
			reader.setInput(iis, true);
			// 图片参数
			ImageReadParam param = reader.getDefaultReadParam();

			// 100，200是左上起始位置，300就是取宽度为300的，就是从100开始取300宽，就是横向100~400，同理纵向200~350的区域就取高度150
			// Rectangle rect = new Rectangle(x,y, width,height);//
			Rectangle rect = new Rectangle(x, y, (int) width, (int) height);
			param.setSourceRegion(rect);
			// 生成切割后的图片
			BufferedImage bi = reader.read(0, param);
			// 对生成切割后的图片进行压缩
			tag = new BufferedImage((int) w, (int) h,
					BufferedImage.TYPE_INT_RGB);
			tag.getGraphics().drawImage(bi, 0, 0, (int) w, (int) h, null);

		} else {
			tag = new BufferedImage((int) width, (int) height,
					BufferedImage.TYPE_INT_RGB);
			tag.getGraphics().drawImage(image, 0, 0, (int) width, (int) height,
					null);
		}
		// ImageIO.write(bi, "jpg", new File(dest));
		
		/*
		 * 注释的这部分代码是用来将图片保存到本地磁盘的
		 * 需要时解注释然后把servletOuptSeream改为out即可
		 * 	String imagPath = "e:/";
			String path = "";
			String realPath = imagPath + path + "5.jpg";
			File destFile = new File(realPath);
			FileOutputStream out = new FileOutputStream(destFile); // 输出到文件流
		 */		 
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(servletOutputStream);
		encoder.encode((BufferedImage) tag); // JPEG编码
		servletOutputStream.close();
	}

	public static void main(String[] args) {
		try {
			//ZipImageUtils.readUsingImageReader("", "e:/3.jpg", 1000, 1000);
			// ZipImageUtils.getimgwidth("e:", 100, 100, servletOutputStream);
			// File("E:\\8b82b9014a90f6031ef83f473b12b31bb051ed61.jpg"), 100);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Trace.printStackTrace(e);
		}
	}

	/**
	 * 删除指定路径的文件
	 * 
	 * @param path
	 */
	public static void deletePhoto(String path) {
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
	}

	public static void getPhoto(String filepath, int w, int h)
			throws ImageFormatException, IOException {
		File f = new File("e:\\1.jpg");
		Image image = ImageIO.read(f); // 读取文件
		double height = image.getHeight(null); // 获取图片的高度
		double width = image.getWidth(null); // 获取图片的宽度
		double ybl = width / height;
		double fbl = w / h;
		if (ybl > fbl) {

		} else {

		}

		int new_w = 300;
		double scale = (width - new_w) / width;
		double sh = height * scale;
		int new_h = (int) (height - sh);
		/** 宽,高设定 */
		BufferedImage tag = new BufferedImage(new_w, new_h,
				BufferedImage.TYPE_INT_RGB);
		tag.getGraphics().drawImage(image, 0, 0, new_w, new_h, null);
		/** 压缩之后临时存放位置 */
		long time = System.currentTimeMillis();
		// 保存的是发布心愿所有的照片
		String imagPath = "e:/";
		String path = "";
		String realPath = imagPath + path + time + ".jpg";
		File destFile = new File(realPath);
		FileOutputStream out = new FileOutputStream(destFile); // 输出到文件流
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out); // 换成上一个方法的out
		encoder.encode((BufferedImage) tag); // JPEG编码
		out.close();
	}

}
