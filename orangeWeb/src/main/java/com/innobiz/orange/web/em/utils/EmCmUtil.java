package com.innobiz.orange.web.em.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/** 공통 유틸 */
public class EmCmUtil {
	
	/** 파일명 및 확장자 가져오기 */
	public static String getFileExtension(String path, boolean isExt){
		int p;
		String fileName = null;
		p = path.replace('\\', '/').lastIndexOf('/');
		if (p >= 0) fileName = path.substring(p+1);
		else fileName = path;
		if(isExt){
			p = fileName.lastIndexOf('.');
			if (p >= 0) return fileName.substring(p+1);
			else return null;
		}
		if(fileName!=null) return fileName;
		
		return null;
	}
	
	/** 배열을 List 로 전환 */
	public static List<String> toList(String ... array){
		if(array==null || array.length==0) return null;
		List<String> list = new ArrayList<String>();
		for(String str : array){
			if(str!=null) list.add(str);
		}
		return list;
	}
	
	/** 폴더 파일 삭제 */
	public static void deleteAllFiles(String path){
		File file = new File(path);
		//폴더내 파일을 배열로 가져온다.
		File[] tempFile = file.listFiles();
		if(tempFile==null) return;
		if(tempFile.length >0){
			for (int i=0; i<tempFile.length; i++) {
				if(tempFile[i].isFile()){
					tempFile[i].delete();
				}else if(tempFile[i].isDirectory()){
					deleteAllFiles(tempFile[i].getPath());
				}
				tempFile[i].delete();
			}
			file.delete();
		}
	}
}
