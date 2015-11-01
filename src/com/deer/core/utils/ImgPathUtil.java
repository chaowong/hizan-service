package com.deer.core.utils;

import java.io.File;

public class ImgPathUtil {
	
	
	public static String getHashcode(String imagPath ,String fileName){
		int hashCold = fileName.hashCode();
		int dir1 = hashCold&0xf;
		int dir2 = (hashCold&0xf0)>>4;
		
		String path = dir1+"/"+dir2+"/";
		File file = new File(imagPath+"/"+path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return path;
	}
	
}
